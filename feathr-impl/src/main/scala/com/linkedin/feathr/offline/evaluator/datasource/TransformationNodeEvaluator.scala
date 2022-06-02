package com.linkedin.feathr.offline.evaluator.datasource

import com.linkedin.feathr.common
import com.linkedin.feathr.common.{AnchorExtractor, AnchorExtractorBase, FeatureDerivationFunction, FeatureTypeConfig, FeatureTypes}
import com.linkedin.feathr.compute.{NodeReference, Operators, Transformation}
import com.linkedin.frame.core.config.producer.common.KeyListExtractor
import com.linkedin.frame.exception.{ErrorLabel, FrameFeatureTransformationException}
import com.linkedin.feathr.offline.derived.functions.{MvelFeatureDerivationFunction1, SimpleMvelDerivationFunction}
import com.linkedin.feathr.offline.anchored.anchorExtractor.{SQLConfigurableAnchorExtractor, SQLKeys, SimpleConfigurableAnchorExtractor}
import com.linkedin.feathr.offline.anchored.keyExtractor.{MVELSourceKeyExtractor, SQLSourceKeyExtractor, SpecificRecordSourceKeyExtractor}
import com.linkedin.feathr.offline.client.{DataFrameColName, NodeContext}
import com.linkedin.feathr.offline.config.{ConfigLoaderUtils, MVELFeatureDefinition, SQLFeatureDefinition}
import com.linkedin.feathr.offline.job.FeatureTransformation.{applyRowBasedTransformOnRdd, getFeatureJoinKey}
import com.linkedin.feathr.offline.source.accessor.{DataPathHandler, DataSourceAccessor, NonTimeBasedDataSourceAccessor}
import com.linkedin.feathr.offline.transformation.{DataFrameBasedRowEvaluator, FDSConversionUtils, FeatureColumnFormat}
import com.linkedin.feathr.offline.transformation.FeatureColumnFormat.FeatureColumnFormat
import com.linkedin.feathr.offline.util.FeaturizedDatasetUtils.tensorTypeToDataFrameSchema
import com.linkedin.feathr.offline.util.{CoercionUtilsScala, FeaturizedDatasetUtils, SourceUtils}
import com.linkedin.feathr.sparkcommon.{FDSExtractor, GenericAnchorExtractorSpark, SimpleAnchorExtractorSpark}
import com.linkedin.feathr.compute.FeatureVersion
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.{StructField, StructType}
import org.apache.spark.sql.{Column, DataFrame, Row, SparkSession}

import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.collection.mutable

// TODO - Change it to a companion object format
private[offline] object TransformationNodeEvaluator {
  private val FDSExtractorUserFacingName = "com.linkedin.frame.sparkcommon.FDSExtractor"

  // TODO: Break out handling of different operators to their own functions
  def processTransformationNode(featureName: String, keySeq: Seq[String], inputNodeContext: NodeContext,
    transformationNode: Transformation, featureColumnFormatMap: mutable.HashMap[String, FeatureColumnFormat],
    featureTypeConfigs: Map[String, FeatureTypeConfig], ss: SparkSession, dataPathHandlers: List[DataPathHandler]): NodeContext = {
    // Algorithm
    // 1. Load a UDF for the transformation function
    // 2. Apply it to the Input Col(s)
    // 3. Resulting Col is the transformed feature.
    // 4. In the case of MVEL transformations, label feature as FDS in FeatureColumnFormat map if requested
    // ASSUME THIS IS AN EXTRACTOR, AND THAT THERE MUST ONLY BE ONE INPUT. The other case where there are multiple
    // features as inputs, needs to be coded different at the operator level. TODO
    val transformationFunction = transformationNode.getFunction

    val (result, outputKeyColumnNames) = if (transformationFunction.getOperator == Operators.OPERATOR_ID_MVEL
      || transformationFunction.getOperator == Operators.OPERATOR_ID_EXTRACT_FROM_TUPLE) {
      val mvelExpr = transformationFunction.getParameters.get("expression")
      val mvelExtractor = new SimpleConfigurableAnchorExtractor(keySeq,
        Map(featureName -> MVELFeatureDefinition(mvelExpr, featureTypeConfigs.get(featureName))))

      // Here we make the assumption that the key expression is of the same type of operator as the feature definition and
      // evaluate and append the key columns. Same logic is repeated for SQL expressions too
      // TODO: Extract out the logic from SourceKeyExtractor so we can move away from the concept of key extractors
      val mvelKeyExtractor = new MVELSourceKeyExtractor(mvelExtractor)
      val withKeyColumnDF = mvelKeyExtractor.appendKeyColumns(inputNodeContext.df)
      val outputJoinKeyColumnNames = getFeatureJoinKey(mvelKeyExtractor, withKeyColumnDF)

      (DataFrameBasedRowEvaluator.transform(mvelExtractor, withKeyColumnDF, Seq((featureName, "")), featureTypeConfigs).df, outputJoinKeyColumnNames)
    } else if (transformationFunction.getOperator == Operators.OPERATOR_ID_SPARK_SQL_FEATURE_EXTRACTOR) {
      val sqlExpr = transformationFunction.getParameters.get("expression")

      val featureSchemas = Seq(featureName)
        .map(featureName => {
          // Currently assumes that tensor type is undefined TODO: incorporate user defined tensor type if there is one
          val tensorType = FeaturizedDatasetUtils.lookupTensorTypeForFeatureRef(featureName, None, FeatureTypeConfig.UNDEFINED_TYPE_CONFIG)
          val schema = FeaturizedDatasetUtils.tensorTypeToDataFrameSchema(tensorType)
          featureName -> schema
        })
        .toMap
      val sqlExtractor = new SQLConfigurableAnchorExtractor(SQLKeys(keySeq), Map(featureName -> SQLFeatureDefinition(sqlExpr)))

      val inputDf = inputNodeContext.df
      val transformedCols = sqlExtractor.getTensorFeatures(inputDf, featureSchemas)

      val sqlKeyExtractor = new SQLSourceKeyExtractor(keySeq)
      val withKeyColumnDF = sqlKeyExtractor.appendKeyColumns(inputNodeContext.df)
      val withFeaturesDf = createFeatureDF(withKeyColumnDF, transformedCols.keys.toSeq)
      val outputJoinKeyColumnNames = getFeatureJoinKey(sqlKeyExtractor, withFeaturesDf)

      (withKeyColumnDF.withColumn(transformedCols.head._1._1, transformedCols.head._1._2), outputJoinKeyColumnNames)
    } else if (transformationFunction.getOperator == Operators.OPERATOR_ID_JAVA_UDF_FEATURE_EXTRACTOR) {
      // Grab extractor class and create appropriate extractor
      val className = transformationFunction.getParameters.get("class")
      val extractor = if (className.equals(FDSExtractorUserFacingName)) { // Support for FDSExtractor, which is a canned extractor.
        new FDSExtractor(Set(featureName))
      } else {
        Class.forName(className).newInstance
      }
      if (extractor.isInstanceOf[SimpleAnchorExtractorSpark]) {
        // Note that for Spark UDFs we only support SQL keys.
        val sqlKeyExtractor = new SQLSourceKeyExtractor(keySeq)
        val withKeyColumnDF = sqlKeyExtractor.appendKeyColumns(inputNodeContext.df)
        val outputJoinKeyColumnNames = getFeatureJoinKey(sqlKeyExtractor, withKeyColumnDF)

        val sparkExtractor = extractor.asInstanceOf[SimpleAnchorExtractorSpark]
        val tensorizedFeatureColumns = sparkExtractor.getFeatures(inputNodeContext.df, Map())

        val transformedColsAndFormats: Map[(String, Column), FeatureColumnFormat] = if (extractor.isInstanceOf[SQLConfigurableAnchorExtractor]) {
          // If instance of SQLConfigurableAnchorExtractor, get Tensor features
          // Get DataFrame schema for tensor based on FML or inferred tensor type.
          val featureSchemas = Seq(featureName)
            .map(featureName => {
              // Currently assumes that tensor type is undefined
              val featureTypeConfig = featureTypeConfigs.getOrElse(featureName, FeatureTypeConfig.UNDEFINED_TYPE_CONFIG)
              val tensorType = FeaturizedDatasetUtils.lookupTensorTypeForFeatureRef(featureName, None, featureTypeConfig)
              val schema = FeaturizedDatasetUtils.tensorTypeToDataFrameSchema(tensorType)
              featureName -> schema
            })
            .toMap
          extractor.asInstanceOf[SQLConfigurableAnchorExtractor].getTensorFeatures(inputNodeContext.df, featureSchemas)
        } else if (extractor.isInstanceOf[FDSExtractor]) {
          // While using the FDS extractor, the feature columns are already in FDS format.
          featureColumnFormatMap(featureName) = FeatureColumnFormat.FDS_TENSOR
          extractor.asInstanceOf[FDSExtractor].transformAsColumns(inputNodeContext.df).map(c => (c, FeatureColumnFormat.FDS_TENSOR)).toMap
        } else if (tensorizedFeatureColumns.isEmpty) {
          // If transform.getFeatures() returns empty Seq, then transform using transformAsColumns
          sparkExtractor.transformAsColumns(inputNodeContext.df).map(c => (c, FeatureColumnFormat.RAW)).toMap
        } else {
          // transform.getFeature() now expects user to return FDS tensor
          featureColumnFormatMap(featureName) = FeatureColumnFormat.FDS_TENSOR
          tensorizedFeatureColumns.map(c => (c, FeatureColumnFormat.FDS_TENSOR)).toMap
        }
        val transformedDF = createFeatureDF(withKeyColumnDF, transformedColsAndFormats.keys.toSeq)
        (transformedDF, outputJoinKeyColumnNames)
      } else if (extractor.isInstanceOf[GenericAnchorExtractorSpark]) {
        // Note that for Spark UDFs we only support SQL keys.
        val sqlKeyExtractor = new SQLSourceKeyExtractor(keySeq)
        val withKeyColumnDF = sqlKeyExtractor.appendKeyColumns(inputNodeContext.df)
        val outputJoinKeyColumnNames = getFeatureJoinKey(sqlKeyExtractor, withKeyColumnDF)

        val sparkExtractor = extractor.asInstanceOf[GenericAnchorExtractorSpark]
        val transformedDF = sparkExtractor.transform(inputNodeContext.df)
        (transformedDF, outputJoinKeyColumnNames)
      } else extractor match {
        case rowBasedExtractor: AnchorExtractorBase[Any] =>
          // Note that for row based extractors we will be using MVEL source key extractor
          val userProvidedFeatureTypes = featureTypeConfigs map { case (key, value) => (key, value.getFeatureType) }
          val dataSource = inputNodeContext.dataSource.get
          val expectDatumType = SourceUtils.getExpectDatumType(Seq(rowBasedExtractor))
          val dataSourceAccessor = DataSourceAccessor(ss, dataSource, None, Some(expectDatumType), failOnMissingPartition = false, dataPathHandlers = dataPathHandlers)
          val (transformedDf, keyNames) = applyRowBasedTransformOnRdd(userProvidedFeatureTypes, Seq(featureName),
            dataSourceAccessor.asInstanceOf[NonTimeBasedDataSourceAccessor].getAsRdd().asInstanceOf[RDD[_]],
            Seq(new SpecificRecordSourceKeyExtractor(extractor.asInstanceOf[AnchorExtractor[Any]], Seq.empty[String])),
            Seq(rowBasedExtractor), featureTypeConfigs)
          (transformedDf, keyNames)
        case _ =>
          throw new UnsupportedOperationException()
      }
    } else {
      throw new UnsupportedOperationException() // TODO
    }

    val (withRenamedColsDF, renamedKeyColumns) = dropAndRenameCols(result, outputKeyColumnNames, featureName)
    NodeContext(withRenamedColsDF, renamedKeyColumns, Some(featureName))
  }

  def processDerivedTransformationNode(featureName: String, inputDf: DataFrame,
    transformationNode: Transformation, featureColumnFormatMap: mutable.HashMap[String, FeatureColumnFormat],
    featureTypeConfigs: Map[String, FeatureTypeConfig], nodeIdToFeatureName: Map[Integer, String], featureAlias: Option[String],
    isSeqJoinTransformationNode: Boolean): NodeContext = {
    // If the feature name is already in the inputDf, drop that column
    var changedInputDf = inputDf
    if (inputDf.columns.contains(featureName)) {
      changedInputDf = inputDf.drop(featureName)
    }
    // Algorithm
    // 1. Load a UDF for the transformation function
    // 2. Apply it to the Input Col(s)
    // 3. Resulting Col is the transformed feature.
    // ASSUME THIS IS AN EXTRACTOR, AND THAT THERE MUST ONLY BE ONE INPUT. The other case where there are multiple
    // features as inputs, needs to be coded different at the operator level. TODO
    val transformationFunction = transformationNode.getFunction
    val inputs = transformationNode.getInputs
    val inputFeatureNames = inputs.toArray.map(input => {
      val inp = input.asInstanceOf[NodeReference]
      nodeIdToFeatureName(inp.getId)
    })

    val derivationFunction = if (transformationFunction.getOperator == "frame:java_udf_feature_extractor:0") {
      val udfClass = transformationFunction.getParameters.get("class")
      Class.forName(udfClass).newInstance().asInstanceOf[FeatureDerivationFunction]
    } else if (transformationFunction.getOperator == "frame:extract_from_tuple:0") {
      new MvelFeatureDerivationFunction1(inputFeatureNames, transformationFunction.getParameters.get("expression"), featureName,
        featureTypeConfigs.get(featureName))
    } else { // no key derived feature - mvel extractor
      new SimpleMvelDerivationFunction(transformationFunction.getParameters.get("expression"), featureName, featureTypeConfigs.get(featureName))
        .asInstanceOf[FeatureDerivationFunction]
    }

    if (!isSeqJoinTransformationNode) {
      // prepare context values
      val featureTypeConfig = featureTypeConfigs.getOrElse(featureName, new FeatureTypeConfig(FeatureTypes.UNSPECIFIED))
      val tensorType = FeaturizedDatasetUtils.lookupTensorTypeForNonFMLFeatureRef(featureName, FeatureTypes.UNSPECIFIED, featureTypeConfig)
      val newSchema = tensorTypeToDataFrameSchema(tensorType)
      val inputSchema = changedInputDf.schema
      val outputSchema = StructType(inputSchema.union(StructType(Seq(StructField(featureName, newSchema, nullable = true)))))
      val encoder = RowEncoder(outputSchema)

      val outputDf = changedInputDf.map(row => {
        try {
          val contextFeatureValues = mutable.Map.empty[String, common.FeatureValue]
          inputFeatureNames.map(inputFeatureName => {
            val featureTypeConfig = featureTypeConfigs.getOrElse(inputFeatureName, FeatureTypeConfig.UNDEFINED_TYPE_CONFIG)
            val featureValue = CoercionUtilsScala.coerceFieldToFeatureValue(row, inputSchema, inputFeatureName, featureTypeConfig)
            contextFeatureValues.put(inputFeatureName, featureValue)
          }
          )
          val featureValues = contextFeatureValues.map(fv => Option(fv._2)).toSeq
          val unlinkedOutput = derivationFunction.getFeatures(featureValues)
          val featureType = featureTypeConfigs
            .getOrElse(featureName, FeatureTypeConfig.UNDEFINED_TYPE_CONFIG).getFeatureType
          val fdFeatureValue = unlinkedOutput.map(fv => {
            if (fv.isDefined) {
              if (featureType == FeatureTypes.TENSOR && !derivationFunction.isInstanceOf[SimpleMvelDerivationFunction]) {
                // Convert to FDS directly when tensor type is specified
                FDSConversionUtils.rawToFDSRow(fv.get.getAsTensorData, newSchema)
              } else {
                FDSConversionUtils.rawToFDSRow(fv.get.getAsTermVector.asScala, newSchema)
              }
            } else {
              null
            }
          })
          Row.fromSeq(outputSchema.indices.map { i => {
            if (i >= inputSchema.size) {
              fdFeatureValue(i - inputSchema.size)
            } else {
              row.get(i)
            }
          }
          })
        } catch {
          case e: Exception =>
            throw new FrameFeatureTransformationException(
              ErrorLabel.FRAME_USER_ERROR,
              s"Fail to calculate derived feature ",
              e)
        }
      })(encoder)
      val result = if (featureAlias.isDefined) {
        featureColumnFormatMap(featureAlias.get) = FeatureColumnFormat.RAW
        outputDf.withColumnRenamed(featureName, featureAlias.get)
      } else outputDf

      NodeContext(result, Seq.empty, Some(featureName))
    } else { // handle seq join transformation nodes
      val mvelExpr = transformationFunction.getParameters.get("expression")
      val mvelExtractor = new SimpleConfigurableAnchorExtractor(Seq.empty,
        Map(featureName -> MVELFeatureDefinition(mvelExpr, featureTypeConfigs.get(featureName))))

      var result = DataFrameBasedRowEvaluator.transform(mvelExtractor, changedInputDf, Seq((featureName, "")), featureTypeConfigs).df
      if (featureAlias.isDefined) {
        result = result.withColumn(featureAlias.get, col(featureName))
        featureColumnFormatMap(featureAlias.get) = FeatureColumnFormat.RAW
      }
      NodeContext(result, Seq.empty, Some(featureName))
    }
  }

  private def dropAndRenameCols(df: DataFrame, keyCols: Seq[String], featureName: String): (DataFrame, Seq[String]) = {
    val toDropCols = df.columns diff (keyCols ++ Seq(featureName))
    val modifiedDf = df.drop(toDropCols: _*)
    val renamedKeyColumns = keyCols.map(c => "__frame__key__column__" + c)
    val oldKeyColToNewKeyCOl = (keyCols zip renamedKeyColumns).toMap
    val withRenamedColsDF = modifiedDf.select(
      modifiedDf.columns.map(c => modifiedDf(c).alias(oldKeyColToNewKeyCOl.getOrElse(c, c))): _*
    )
    (withRenamedColsDF, renamedKeyColumns)
  }

  private def createFeatureDF(inputDf: DataFrame, featureColumnDefs: Seq[(String, Column)]): DataFrame = {
    // first add a prefix to the feature column name in the schema
    val featureColumnNamePrefix = "_frame_sql_feature_prefix_"
    val transformedDF = featureColumnDefs.foldLeft(inputDf)((baseDF, columnWithName) => {
      val columnName = featureColumnNamePrefix + columnWithName._1
      baseDF.withColumn(columnName, columnWithName._2)
    })
    val featureNames = featureColumnDefs.map(_._1)
    // drop the context column that have the same name as feature names
    val withoutDupContextFieldDF = transformedDF.drop(featureNames: _*)
    // remove the prefix we just added, so that we have a dataframe with feature names as their column names
    featureNames
      .zip(featureNames)
      .foldLeft(withoutDupContextFieldDF)((baseDF, namePair) => {
        baseDF.withColumnRenamed(featureColumnNamePrefix + namePair._1, namePair._2)
      })
  }
}
