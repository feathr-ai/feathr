package com.linkedin.feathr.offline.anchored.anchorExtractor

import com.fasterxml.jackson.annotation.JsonProperty
import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrConfigException}
import com.linkedin.feathr.common.util.CoercionUtils
import com.linkedin.feathr.common.{AnchorExtractor, FeatureTypeConfig, FeatureTypes, FeatureValue}
import com.linkedin.feathr.offline
import com.linkedin.feathr.offline.FeatureDataFrame
import com.linkedin.feathr.offline.config.MVELFeatureDefinition
import com.linkedin.feathr.offline.job.{FeatureTransformation, FeatureTypeInferenceContext}
import com.linkedin.feathr.offline.mvel.{MvelContext, MvelUtils}
import com.linkedin.feathr.offline.transformation.FDSConversionUtils
import com.linkedin.feathr.offline.util.FeaturizedDatasetUtils.tensorTypeToDataFrameSchema
import com.linkedin.feathr.offline.util.{FeatureValueTypeValidator, FeaturizedDatasetUtils}
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.catalyst.expressions.GenericRowWithSchema
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.mvel2.MVEL

/**
 * A general-purpose configurable FeatureAnchor that extract features based on its definitions,
 * the definition includes mvel expression and feature type (optional)
 *
 * @param key An MVEL expression that provides the entityId for a given data record. This could be as simple as the name
 *            of the field containing this data, e.g. "a_id". The expression may return NULL if a record should be
 *            filtered out
 * @param features A map of feature name to feature definition that provides the feature for a given data record
 */
private[offline] class SimpleConfigurableAnchorExtractor( @JsonProperty("key") key: Seq[String],
                                                          @JsonProperty("features") features: Map[String, MVELFeatureDefinition])
  extends AnchorExtractor[Any] {

  @transient private lazy val log = Logger.getLogger(getClass)

  def getKeyExpression(): Seq[String] = key

  def getFeaturesDefinitions(): Map[String, MVELFeatureDefinition] = features

  override def getProvidedFeatureNames: Seq[String] = features.keys.toIndexedSeq

  @transient private lazy val parserContext = MvelContext.newParserContext

  private val keyExpression = key.map(k => MVEL.compileExpression(k, parserContext))

  /*
   * Create a map of FeatureRef string to (MVEL expression, optional FeatureType) tuple.
   * FeatureRef string is a string representation of FeatureRef and is backward compatible
   * with legacy feature names.
   */
  private val featureExpressions = features map {
    case (featureRefStr, featureDef) =>
      if (featureDef.featureExpr.isEmpty) {
        throw new FeathrConfigException(ErrorLabel.FEATHR_USER_ERROR, s"featureExpr field in feature $featureDef is not defined.")
      }
      (featureRefStr, (MVEL.compileExpression(featureDef.featureExpr, parserContext), featureDef.featureTypeConfig.map(x => x.getFeatureType)))
  }

  private val featureTypeConfigs = features map {
    case (featureRefStr, featureDef) =>
      (featureRefStr, featureDef.featureTypeConfig.getOrElse(new FeatureTypeConfig(FeatureTypes.UNSPECIFIED)))
  }
  override def getKey(datum: Any): Seq[String] = {
    MvelContext.ensureInitialized()
    // be more strict for resolving keys (don't swallow exceptions)
    keyExpression.map(k =>
      try {
        Option(MVEL.executeExpression(k, datum)) match {
          case None => null
          case Some(keys) => keys.toString
        }
      } catch {
        case e: Exception =>
          throw new FeathrConfigException(
            ErrorLabel.FEATHR_USER_ERROR,
            s"Could not evaluate key expression $k on feature data. Please fix your key expression.",
            e)
    })
  }

  // only evaluate required feature, aims to improve performance
  override def getSelectedFeatures(datum: Any, selectedFeatures: Set[String]): Map[String, FeatureValue] = {
    MvelContext.ensureInitialized()

    featureExpressions collect {
      case (featureRefStr, (expression, featureType)) if selectedFeatures.contains(featureRefStr) =>
        (featureRefStr, (MvelUtils.executeExpression(expression, datum, null, featureRefStr), featureType))
    } collect {
      // Apply a partial function only for non-empty feature values, empty feature values will be set to default later
      case (featureRefStr, (Some(value), _)) =>
        getFeature(featureRefStr, value)
    }
  }

  override def getFeatures(datum: Any): Map[String, FeatureValue] = {
    evaluateMVELWithFeatureType(datum, getProvidedFeatureNames.toSet) collect {
      // Apply a partial function only for non-empty feature values, empty feature values will be set to default later
      case (featureRefStr, (value, _)) =>
        getFeature(featureRefStr, value)
    }
  }

  /**
   * Ideally we could use reflection to inspect the type argument T of the implementing class.
   * But I don't know how to do this right now using the reflection API, and the cost of making people implement this is low.
   */
  override def getInputType: Class[_] = classOf[Object]

  /**
   * Transform and add feature column to input dataframe using mvelExtractor

   * output feature format.
   * @param mvelExtractor mvel-based anchor extractor
   * @param inputDF input dataframe
   * @param selectedFeatureNames features to calculate, if empty, will calculate all features defined in the extractor
   * @return inputDF with feature columns added in FDS format.
   */
  def transform(mvelExtractor: SimpleConfigurableAnchorExtractor, inputDF: DataFrame, selectedFeatureNames: Seq[String]): FeatureDataFrame = {
    require(selectedFeatureNames.nonEmpty, s"Features to transform cannot be empty.")
    // Features are extracted to Quince-FDS format by default
    transformToFDSTensor(mvelExtractor, inputDF, selectedFeatureNames)
  }

  /**
   * get feature type for selected features
   * @param selectedFeatureRefs selected features
   * @return featureRef to feature type
   */
  def getFeatureTypes(selectedFeatureRefs: Set[String]): Map[String, FeatureTypes] = {
    featureExpressions.collect {
      case (featureRef, (_, featureTypeOpt)) if selectedFeatureRefs.contains(featureRef) =>
        featureRef -> featureTypeOpt.getOrElse(FeatureTypes.UNSPECIFIED)
    }
  }

  /**
   * Transform and add feature column to input dataframe using mvelExtractor, output feature format is FDS Tensor
   * @param inputDF      input dataframe
   * @param featureRefStrs features to transform
   * @return inputDF with feature columns added, and the inferred feature types for features.
   *         The inferred type is based on looking at the raw feature value returned by MVEL expression. We used to
   *         infer the feature type and convert them into term-vector in the getFeatures() API. Now in this function,
   *         we still use the same rules to infer the feature type, but storing them in FDS tensor format instead of
   *         term-vector.
   */
  private def transformToFDSTensor(mvelExtractor: SimpleConfigurableAnchorExtractor, inputDF: DataFrame, featureRefStrs: Seq[String]): FeatureDataFrame = {
    val inputSchema = inputDF.schema
    val spark = SparkSession.builder().getOrCreate()
    val featureTypes = mvelExtractor.getFeatureTypes(featureRefStrs.toSet)
    val FeatureTypeInferenceContext(featureTypeAccumulators) =
      FeatureTransformation.getTypeInferenceContext(spark, featureTypes, featureRefStrs)
    val transformedRdd = inputDF.rdd.map(row => {
      // in some cases, the input dataframe row here only have Row and does not have schema attached,
      // while MVEL only works with GenericRowWithSchema, create it manually
      val rowWithSchema = if (row.isInstanceOf[GenericRowWithSchema]) {
        row
      } else {
        new GenericRowWithSchema(row.toSeq.toArray, inputSchema)
      }
      val result = mvelExtractor.evaluateMVELWithFeatureType(rowWithSchema, featureRefStrs.toSet)
      val featureValues = featureRefStrs map {
        featureRef =>
          if (result.contains(featureRef)) {
            val (featureValue, featureType) = result(featureRef)
            if (featureTypeAccumulators(featureRef).isZero) {
              // This is lazy evaluated
              featureTypeAccumulators(featureRef).add(featureType)
            }
            val inferredFeatureType = featureTypeAccumulators(featureRef).value
            val featureTypeConfig = featureTypeConfigs.getOrElse(featureRef, new FeatureTypeConfig(FeatureTypes.UNSPECIFIED))
            val tensorType = FeaturizedDatasetUtils.lookupTensorTypeForFeatureRef(featureRef, inferredFeatureType, featureTypeConfig)
            val schemaType = tensorTypeToDataFrameSchema(tensorType)
            FDSConversionUtils.rawToFDSRow(featureValue, schemaType)
          } else null
      }
      Row.merge(row, Row.fromSeq(featureValues))
    })
    val inferredFeatureTypes = FeatureTransformation.inferFeatureTypes(featureTypeAccumulators, transformedRdd, featureRefStrs)
    val inferredFeatureTypeConfigs = inferredFeatureTypes.map(featureTypeEntry => featureTypeEntry._1 -> new FeatureTypeConfig(featureTypeEntry._2))
    // Merge inferred and provided feature type configs
    val (unspecifiedTypeConfigs, providedTypeConfigs) = featureTypeConfigs.partition(x => x._2.getFeatureType == FeatureTypes.UNSPECIFIED)
    // providedTypeConfigs should take precedence over inferredFeatureTypeConfigs; inferredFeatureTypeConfigs should
    // take precedence over unspecifiedTypeConfigs
    val mergedTypeConfigs = unspecifiedTypeConfigs ++ inferredFeatureTypeConfigs ++ providedTypeConfigs
    val featureDF: DataFrame = createFDSFeatureDF(inputDF, featureRefStrs, spark, transformedRdd, mergedTypeConfigs)
    offline.FeatureDataFrame(featureDF, mergedTypeConfigs)
  }

  /**
   * Create a FDS feature dataframe from the MVEL transformed RDD.
   * This functions is used to remove the context fields that have the same names as the feature names,
   * so that we can support feature with the same name as the context field name, e.g.
   * features: {
   *   foo: foo
   * }
   * @param inputDF source dataframe
   * @param featureRefStrs feature ref strings
   * @param ss spark session
   * @param transformedRdd transformed RDD[Row] (in FDS format) from inputDF
   * @param featureTypesConfigs feature types
   * @return FDS feature dataframe
   */
  private def createFDSFeatureDF(
      inputDF: DataFrame,
      featureRefStrs: Seq[String],
      ss: SparkSession,
      transformedRdd: RDD[Row],
      featureTypesConfigs: Map[String, FeatureTypeConfig]): DataFrame = {
    // first add a prefix to the feature column name in the schema
    val featureColumnNamePrefix = "_feathr_mvel_feature_prefix_"
    val featureTensorTypeInfo = FeatureTransformation.getFDSSchemaFields(featureRefStrs, featureTypesConfigs,
      featureColumnNamePrefix)

    val outputSchema = StructType(inputDF.schema.union(StructType(featureTensorTypeInfo)))

    val transformedDF = ss.createDataFrame(transformedRdd, outputSchema)
    // drop the context column that have the same name as feature names
    val withoutDupContextFieldDF = transformedDF.drop(featureRefStrs: _*)
    // remove the prefix we just added, so that we have a dataframe with feature names as their column names
    val featureDF = featureRefStrs
      .zip(featureRefStrs)
      .foldLeft(withoutDupContextFieldDF)((baseDF, namePair) => {
        baseDF.withColumnRenamed(featureColumnNamePrefix + namePair._1, namePair._2)
      })
    featureDF
  }

  /**
   * Helper function to convert a feature data into FeatureValue.
   */
  private def getFeature(featureRefStr: String, value: Any): (String, FeatureValue) = {
    val featureTypeConfig = featureTypeConfigs(featureRefStr)
    val featureValue = offline.FeatureValue.fromTypeConfig(value, featureTypeConfig)
    FeatureValueTypeValidator.validate(featureValue, featureTypeConfigs(featureRefStr))
    (featureRefStr, featureValue)
  }

  /**
   * Evaluate mvel expression
   * @param datum record to evaluate against
   * @param selectedFeatureRefs selected features to evaluate
   * @return map featureRefStr -> feature value
   */
  private def evaluateMVEL(datum: Any, selectedFeatureRefs: Set[String]): Map[String, Option[AnyRef]] = {
    MvelContext.ensureInitialized()
    featureExpressions collect {
      case (featureRefStr, (expression, _)) if selectedFeatureRefs.contains(featureRefStr) =>
        /*
         * Note that FeatureTypes is ignored above. A feature's value type and dimension type(s) coupled with the
         * heuristics implemented in the TensorBuilder class obviate the need to consider (Feathr) FeatureTypes object
         * for building a tensor. Feature's value type and dimension type(s) are obtained via Feathr's Feature Metadata
         * Library during tensor construction.
         */
        (featureRefStr, MvelUtils.executeExpression(expression, datum, null, featureRefStr))
    }
  }

  /**
   * Evaluate mvel expression, return the feature value and inferred feature types
   * The type is inferred for each row, this is the same as the existing behavior,
   * i.e. coerce each MVEL result into a feature value by looking at the MVEL result
   * if type is not specified
   * @param datum record to evaluate against
   * @return map of (featureRefStr -> (feature value, featureType))
   */
  private def evaluateMVELWithFeatureType(datum: Any, selectedFeatureRefs: Set[String]): Map[String, (AnyRef, FeatureTypes)] = {
    val featureTypeMap = getFeatureTypes(selectedFeatureRefs)
    val result = evaluateMVEL(datum, selectedFeatureRefs)
    val resultWithType = result collect {
      // Apply a partial function only for non-empty feature values, empty feature values will be set to default later
      case (featureRefStr, Some(value)) =>

        // If user already provided the feature type, we don't need to coerce/infer it
        val coercedFeatureType = if (featureTypeMap(featureRefStr) == FeatureTypes.UNSPECIFIED) {
          CoercionUtils.getCoercedFeatureType(value)
        } else featureTypeMap(featureRefStr)
        (featureRefStr, (value, coercedFeatureType))
    }
    resultWithType map {
      case (featureRef, (featureValue, coercedFeatureType)) =>
        val userProvidedFeatureType = featureTypeMap.getOrElse(featureRef, FeatureTypes.UNSPECIFIED)
        val featureType = if (userProvidedFeatureType == FeatureTypes.UNSPECIFIED) coercedFeatureType else userProvidedFeatureType
        featureRef -> (featureValue, featureType)
    }
  }
}
