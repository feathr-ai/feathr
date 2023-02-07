package com.linkedin.feathr.offline.derived

import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrException}
import com.linkedin.feathr.common.{FeatureDerivationFunction, FeatureTypeConfig}
import com.linkedin.feathr.offline.client.DataFrameColName
import com.linkedin.feathr.offline.client.plugins.{FeathrUdfPluginContext, FeatureDerivationFunctionAdaptor}
import com.linkedin.feathr.offline.derived.functions.{MvelFeatureDerivationFunction, SQLFeatureDerivationFunction, SeqJoinDerivationFunction}
import com.linkedin.feathr.offline.derived.strategies._
import com.linkedin.feathr.offline.join.algorithms.{SequentialJoinConditionBuilder, SparkJoinWithJoinCondition}
import com.linkedin.feathr.offline.logical.FeatureGroups
import com.linkedin.feathr.offline.mvel.plugins.FeathrExpressionExecutionContext
import com.linkedin.feathr.offline.source.accessor.DataPathHandler
import com.linkedin.feathr.offline.util.FeaturizedDatasetUtils
import com.linkedin.feathr.offline.{ErasedEntityTaggedFeature, FeatureDataFrame}
import com.linkedin.feathr.sparkcommon.FeatureDerivationFunctionSpark
import com.linkedin.feathr.{common, offline}
import org.apache.logging.log4j.LogManager
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * This class is responsible for applying feature derivations.
 * @param derivationStrategies strategies for executing various derivation functions.
 */
private[offline] class DerivedFeatureEvaluator(derivationStrategies: DerivationStrategies, mvelContext: Option[FeathrExpressionExecutionContext]) {

  /**
   * Calculate a derived feature, this function support all kinds of derived features
   * @param keyTag key tags of current joinStage (used for the derived feature)
   * @param keyTagList all key tags in the join config
   * @param contextDF context dataframeres
   * @param derivedFeature derived feature to be calcuated
   * @return pair of:
   *         1. contextDF with the derived feature value appended as an extra column,
   *         whose name is generated by [[DataFrameColName.genFeatureColumnName]]
   *         2. inferred feature types
   */
  def evaluate(keyTag: Seq[Int], keyTagList: Seq[String], contextDF: DataFrame, derivedFeature: DerivedFeature): FeatureDataFrame = {
    val tags = Some(keyTag.map(keyTagList).toList)
    val producedFeatureColName = DataFrameColName.genFeatureColumnName(derivedFeature.producedFeatureNames.head, tags)

    derivedFeature.derivation match {
      case g: SeqJoinDerivationFunction =>
        val resultDF = derivationStrategies.sequentialJoinDerivationStrategy(keyTag, keyTagList, contextDF, derivedFeature, g, mvelContext)
        convertFeatureColumnToQuinceFds(producedFeatureColName, derivedFeature, resultDF)
      case h: FeatureDerivationFunctionSpark =>
        val resultDF = derivationStrategies.customDerivationSparkStrategy(keyTag, keyTagList, contextDF, derivedFeature, h, mvelContext)
        convertFeatureColumnToQuinceFds(producedFeatureColName, derivedFeature, resultDF)
      case s: SQLFeatureDerivationFunction =>
        val resultDF = derivationStrategies.sqlDerivationSparkStrategy(keyTag, keyTagList, contextDF, derivedFeature, s, mvelContext)
        convertFeatureColumnToQuinceFds(producedFeatureColName, derivedFeature, resultDF)
      case x: FeatureDerivationFunction =>
        // We should do the FDS conversion inside the rowBasedDerivationStrategy here. The result of rowBasedDerivationStrategy
        // can be NTV FeatureValue or TensorData-based Feature. NTV FeatureValue has fixed FDS schema. However, TensorData
        // doesn't have fixed DataFrame schema so that we can't return TensorData but has to return FDS.
        val resultDF = derivationStrategies.rowBasedDerivationStrategy(keyTag, keyTagList, contextDF, derivedFeature, x, mvelContext)
        offline.FeatureDataFrame(resultDF, getTypeConfigs(producedFeatureColName, derivedFeature, resultDF))
      case derivation =>
        FeathrUdfPluginContext.getRegisteredUdfAdaptor(derivation.getClass) match {
          case Some(adaptor: FeatureDerivationFunctionAdaptor) =>
            // replicating the FeatureDerivationFunction case above
            val featureDerivationFunction = adaptor.adaptUdf(derivation)
            val resultDF = derivationStrategies.rowBasedDerivationStrategy(keyTag, keyTagList, contextDF, derivedFeature, featureDerivationFunction, mvelContext)
            offline.FeatureDataFrame(resultDF, getTypeConfigs(producedFeatureColName, derivedFeature, resultDF))
          case _ =>
            throw new FeathrException(ErrorLabel.FEATHR_ERROR, s"Unsupported feature derivation function for feature ${derivedFeature.producedFeatureNames.head}.")
        }
    }
  }

  /**
   * This method converts the derived feature in Raw DataFrame format to Quince FDS representation.
   * @param columnName      DataFrame column name for the derived feature.
   * @param derivedFeature  Derived feature metadata.
   * @param df              Input DataFrame.
   * @return DataFrame with feature column converted to Quince Fds Format.
   */
  def convertFeatureColumnToQuinceFds(columnName: String, derivedFeature: DerivedFeature, df: DataFrame): FeatureDataFrame = {
    val featureColNameToFeatureNameAndType = getFeatureColNameToFeatureNameAndType(columnName, derivedFeature, df)
    val inferredFeatureTypeConfigs = getTypeConfigs(columnName, derivedFeature, df)
    val quinceFdsDf = FeaturizedDatasetUtils.convertRawDFtoQuinceFDS(df, featureColNameToFeatureNameAndType)

    offline.FeatureDataFrame(quinceFdsDf, inferredFeatureTypeConfigs)
  }

  private def getFeatureColNameToFeatureNameAndType(columnName: String, derivedFeature: DerivedFeature,
    df: DataFrame): Map[String, (String, FeatureTypeConfig)] = {
    val featureColNameToFeatureNameAndType: Map[String, (String, FeatureTypeConfig)] =
      Seq((derivedFeature.producedFeatureNames.head, columnName)).map {
        case (featureName, colName) =>
          val colType = df.schema.fields(df.schema.fieldIndex(colName)).dataType
          val inferredType = FeaturizedDatasetUtils.inferFeatureTypeFromColumnDataType(colType)
          val featureTypeConfig = derivedFeature.featureTypeConfigs.getOrElse(featureName,
            new FeatureTypeConfig(inferredType))
          (colName, (featureName, featureTypeConfig))
      }.toMap
    featureColNameToFeatureNameAndType
  }

  private def getTypeConfigs(columnName: String, derivedFeature: DerivedFeature, df: DataFrame): Map[String, FeatureTypeConfig] = {
    val featureColNameToFeatureNameAndType = getFeatureColNameToFeatureNameAndType(columnName, derivedFeature, df)
    val inferredFeatureTypeConfigs = featureColNameToFeatureNameAndType.map {
      case (_, (featureName, featureTypeConfig)) =>
        featureName -> featureTypeConfig
    }
    inferredFeatureTypeConfigs
  }
}

/**
 * Companion object with instantiation responsibilities and static utility methods.
 */
private[offline] object DerivedFeatureEvaluator {
  private val log = LogManager.getLogger(getClass)

  def apply(derivationStrategies: DerivationStrategies, mvelContext: Option[FeathrExpressionExecutionContext]): DerivedFeatureEvaluator = new DerivedFeatureEvaluator(derivationStrategies, mvelContext)

  def apply(ss: SparkSession,
            featureGroups: FeatureGroups,
            dataPathHandlers: List[DataPathHandler],
            mvelContext: Option[FeathrExpressionExecutionContext]): DerivedFeatureEvaluator = {
    val defaultStrategies = strategies.DerivationStrategies(
      new SparkUdfDerivation(),
      new RowBasedDerivation(featureGroups.allTypeConfigs, mvelContext),
      new SequentialJoinAsDerivation(ss, featureGroups, SparkJoinWithJoinCondition(SequentialJoinConditionBuilder), dataPathHandlers),
      new SqlDerivationSpark())
    new DerivedFeatureEvaluator(defaultStrategies, mvelContext)
  }

  /**
   * This utility method computes derived features by applying the provided derivation function on the
   * input (already evaluated) features.
   * @param keyTag                 Integer key tags of the derived feature.
   * @param derivedFeature         Derived feature metadata with details such as consumed features, derivation function etc
   * @param contextFeatureValues   Input features (as Map of Feature -> FeatureValues).
   * @return Evaluated derived Feature -> FeatureValue map.
   */
  def evaluateFromFeatureValues(
      keyTag: Seq[Int],
      derivedFeature: DerivedFeature,
      contextFeatureValues: Map[common.ErasedEntityTaggedFeature, common.FeatureValue],
      mvelContext: Option[FeathrExpressionExecutionContext]
    ): Map[common.ErasedEntityTaggedFeature, common.FeatureValue] = {
    try {
      val linkedInputParams = derivedFeature.consumedFeatureNames.map {
        case ErasedEntityTaggedFeature(calleeTag, featureName) =>
          ErasedEntityTaggedFeature(calleeTag.map(keyTag), featureName)
      }
      // for features with value `null`, convert Some(null) to None, to avoid null pointer exception in downstream analysis
      val keyedContextFeatureValues = contextFeatureValues.map(kv => (kv._1.getErasedTagFeatureName, kv._2))
      val resolvedInputArgs = linkedInputParams.map(taggedFeature => keyedContextFeatureValues.get(taggedFeature.getErasedTagFeatureName).flatMap(Option(_)))
      val derivedFunc = derivedFeature.getAsFeatureDerivationFunction match {
        case derivedFunc: MvelFeatureDerivationFunction =>
          derivedFunc.mvelContext = mvelContext
          derivedFunc
        case func => func
      }
      val unlinkedOutput = derivedFunc.getFeatures(resolvedInputArgs)
      val callerKeyTags = derivedFeature.producedFeatureNames.map(ErasedEntityTaggedFeature(keyTag, _))

      // This would indicate a problem with the DerivedFeature, where there are a different number of features included in
      // the response than were declared by the .producedFeatureNames method.
      require(unlinkedOutput.size == callerKeyTags.size)

      (callerKeyTags zip unlinkedOutput).collect { case (tag, Some(featureValue)) => (tag, featureValue) }.toMap
    } catch {
      case exception: Exception =>
        log.error(s"Evaluate derived feature failed with error: ${exception.getMessage}, derived feature names are: ${derivedFeature.producedFeatureNames}")
        throw exception
    }
  }
}
