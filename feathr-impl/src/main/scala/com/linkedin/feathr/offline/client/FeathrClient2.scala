package com.linkedin.feathr.offline.client

import java.time.Duration
import com.linkedin.frame.common.urn.MlFeatureVersionUrn
import com.linkedin.feathr.common.{FeatureTypeConfig, JoiningFeatureParams, TaggedFeatureName}
import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrConfigException}
import com.linkedin.feathr.compute.Resolver.FeatureRequest
import com.linkedin.feathr.compute._
import com.linkedin.frame.config.featureanchor.FeatureAnchorEnvironment
import com.linkedin.feathr.config.join.FrameFeatureJoinConfig
import com.linkedin.feathr.fds.ColumnMetadata
import com.linkedin.frame.config.{FeatureDefinition, FeatureDefinitionLoaderFactory}
import com.linkedin.frame.core.config.producer.common.KeyListExtractor
import com.linkedin.frame.core.configdataprovider.StringConfigDataProvider
import com.linkedin.feathr.offline.{FeatureDataFrame, PostTransformationUtil}
import com.linkedin.feathr.offline.exception.DataFrameApiUnsupportedOperationException
import com.linkedin.feathr.offline.anchored.WindowTimeUnit
import com.linkedin.feathr.offline.client.FeathrClient.Builder
import com.linkedin.feathr.offline.config.anchors.PegasusRecordDurationConverter
import com.linkedin.feathr.offline.config.FeatureJoinConfig
import com.linkedin.feathr.offline.config.join.converters.PegasusRecordFrameFeatureJoinConfigConverter
import com.linkedin.feathr.offline.config.{ConfigLoaderUtils, PegasusRecordDefaultValueConverter, PegasusRecordFeatureTypeConverter}
import com.linkedin.feathr.offline.derived.strategies.SeqJoinAggregator
import com.linkedin.feathr.offline.derived.strategies.SequentialJoinAsDerivation.getDefaultTransformation
import com.linkedin.feathr.offline.evaluator.datasource.{ContextNodeEvaluator, EventNodeEvaluator, TableNodeEvaluator, TransformationNodeEvaluator}
import com.linkedin.feathr.offline.job.{FeatureGenSpec, FeatureTransformation, JoinJobContext}
import com.linkedin.feathr.offline.join.algorithms.{EqualityJoinConditionBuilder, JoinType, SequentialJoinConditionBuilder, SparkJoinWithJoinCondition}
import com.linkedin.feathr.offline.source.DataSource
import com.linkedin.feathr.offline.swa.SlidingWindowFeatureUtils
import com.linkedin.feathr.offline.transformation.DataFrameDefaultValueSubstituter.substituteDefaults
import com.linkedin.feathr.offline.transformation.{FeatureColumnFormat, MvelDefinition}
import com.linkedin.feathr.offline.transformation.FeatureColumnFormat.{FDS_TENSOR, FeatureColumnFormat, RAW}
import com.linkedin.feathr.offline.util.datetime.OfflineDateTimeUtils
import com.linkedin.feathr.offline.util.{AnchorUtils, DataFrameSplitterMerger, SparkFeaturizedDataset}
import com.linkedin.feathr.offline.fds.FDSUtils
import com.linkedin.feathr.offline.fds.FDSUtils.FeatureWithKeyAndColumnName
import com.linkedin.feathr.offline.job.FeatureTransformation.convertFCMResultDFToFDS
import com.linkedin.feathr.offline.source.accessor.DataPathHandler
import com.linkedin.feathr.swj.{FactData, GroupBySpec, LabelData, LateralViewParams, SlidingWindowFeature, SlidingWindowJoin, WindowSpec}
import com.linkedin.feathr.swj.aggregate.{AggregationType, AvgAggregate, AvgPoolingAggregate, CountAggregate, LatestAggregate, MaxAggregate, MaxPoolingAggregate, MinAggregate, MinPoolingAggregate, SumAggregate}
import org.apache.spark.sql.functions.{col, lit, monotonically_increasing_id}
import org.apache.spark.sql.types.{DataType, Metadata}
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.{Failure, Success}
// scalastyle:off
/**
 * A new implementation of FrameClient that uses the new Feature Compute Model :-)
 *
 * INCOMPLETE DRAFT
 */

case class NodeContext(df: DataFrame /* TODO is DF needed? */ , keyExpression: Seq[String], featureColumn: Option[String] = None,
  dataSource: Option[DataSource] = None)
// hold a DF and column for each node?

class FeathrClient2(ss: SparkSession, computeGraph: ComputeGraph, dataPathHandlers: List[DataPathHandler]) {

  // TODO Will the Columns need unique names? Their node ID and some description might be suitable.
  val nodeContext = mutable.HashMap[Int, NodeContext]()
  def joinFeatures(frameJoinConfig: FrameFeatureJoinConfig, obsData: SparkFeaturizedDataset, jobContext: JoinJobContext): SparkFeaturizedDataset = {
    val joinConfig = PegasusRecordFrameFeatureJoinConfigConverter.convert(frameJoinConfig)
    joinFeatures(joinConfig, obsData, jobContext)
  }

  private def findInvalidFeatureRefs(features: Seq[String]): List[String] = {
    features.foldLeft(List.empty[String]) { (acc, f) =>
      // featureRefStr could have '-' now.
      // TODO (PROML-8037) unify featureRef/featureName and check for '-'
      val featureRefStrInDF = DataFrameColName.getEncodedFeatureRefStrForColName(f)
      val isValidSyntax = AnchorUtils.featureNamePattern.matcher(featureRefStrInDF).matches()
      if (isValidSyntax) acc
      else f :: acc
    }
  }

  @deprecated
  def joinFeatures(joinConfig: FeatureJoinConfig, obsData: SparkFeaturizedDataset, jobContext: JoinJobContext = JoinJobContext()): SparkFeaturizedDataset = {
    val featureNames = joinConfig.joinFeatures.map(_.featureName)
    val duplicateFeatureNames = featureNames.diff(featureNames.distinct).distinct
    val joinFeatures = joinConfig.joinFeatures.map {
      case JoiningFeatureParams(keyTags, featureName, dateParam, timeDelay, featureAlias) =>
        val delay = if (timeDelay.isDefined) {
          WindowTimeUnit.parseWindowTime(timeDelay.get)
        } else {
          if (joinConfig.settings.isDefined && joinConfig.settings.get.joinTimeSetting.isDefined &&
            joinConfig.settings.get.joinTimeSetting.get.simulateTimeDelay.isDefined) {
            joinConfig.settings.get.joinTimeSetting.get.simulateTimeDelay.get
          } else {
            Duration.ZERO
          }
        }
        if (duplicateFeatureNames.contains(featureName)) {
          new FeatureRequest(featureName, keyTags.toList.asJava, delay, keyTags.mkString("_") + "__" + featureName)
        } else {
          new FeatureRequest(featureName, keyTags.toList.asJava, delay, featureAlias.orNull)
        }
    }.toList.asJava

    // Check for invalid feature names
    val allFeaturesInGraph = computeGraph.getFeatureNames.asScala.keys.toSeq
    val invalidFeatureNames = findInvalidFeatureRefs(allFeaturesInGraph)
    if (invalidFeatureNames.nonEmpty) {
      throw new DataFrameApiUnsupportedOperationException(
        "With DataFrame API, feature names must conform to " +
          s"regular expression: ${AnchorUtils.featureNamePattern}, but found feature names: $invalidFeatureNames")
    }
    val conflictFeatureNames: Seq[String] = allFeaturesInGraph.intersect(obsData.data.schema.fieldNames)
    if (conflictFeatureNames.nonEmpty) {
      throw new FeathrConfigException(
        ErrorLabel.FEATHR_USER_ERROR,
        "Feature names must be different from field names in the observation data. " +
          s"Please rename feature ${conflictFeatureNames} or rename the same field names in the observation data.")
    }

    // The idea is that this next line does a large portion of the join planning by itself
    val resolvedGraph = new Resolver(computeGraph).resolveForRequest(joinFeatures)

    // Execute the resolved graph
    val newDf = execute(joinConfig, computeGraph, resolvedGraph, obsData.data)
    newDf.df.show()

    // Note here that feature name is equal to the column name in df.
    val featuresWithKeysAndColumnNames = joinConfig.joinFeatures.map {
      case JoiningFeatureParams(keyTags, featureName, dateParam, timeDelay, featureAlias) =>
        if (duplicateFeatureNames.contains(featureName)) {
          FeatureWithKeyAndColumnName(keyTags.mkString("_") + "__" + featureName, keyTags, keyTags.mkString("_") + "__" + featureName)
        } else {
          FeatureWithKeyAndColumnName(featureName, keyTags, featureName)
        }
    }
    val userProvidedfeatureTypeConfigs = resolvedGraph.getFeatureNames.asScala.map(e =>
      (e._1, if (e._2.hasFeatureVersion) PegasusRecordFeatureTypeConverter().convert(e._2.getFeatureVersion) else None)).collect {
      case (key, Some(value)) => (key, value) // filter out Nones and get rid of Option
    }.toMap
    val fds = FDSUtils.buildSparkFeaturizedDatasetForFCM(newDf.df, newDf.inferredFeatureType, featuresWithKeysAndColumnNames, null, userProvidedfeatureTypeConfigs)

    // return the new SparkFeaturizedDataset
    fds
  }

  def execute(featureJoinConfig: FeatureJoinConfig, unresolvedGraph: ComputeGraph, resolvedGraph: ComputeGraph, df: DataFrame): FeatureDataFrame = {
    // TODO: Provide more comments on this feature aliasing behavior when there are duplicate feature names in the join config and refactor
    // to remove duplicated code logic.
    val featureNames = featureJoinConfig.joinFeatures.map(_.featureName)
    val duplicateFeatureNames = featureNames.diff(featureNames.distinct).distinct
    val featureNameToKeys = featureJoinConfig.featureGroupings.values.flatten.map(x =>
      if (duplicateFeatureNames.contains(x.featureName)) {
        x.keyTags.mkString("_") + "__" + x.featureName -> x.keyTags
      } else {
        x.featureName -> x.keyTags
      }
    ).toMap

    val nodes = resolvedGraph.getNodes().asScala
    val SeqJoinJoiner = SparkJoinWithJoinCondition(SequentialJoinConditionBuilder)
    val nodeIdToNode = mutable.Map.empty[Integer, Seq[Integer]]
    val stack = mutable.Stack[Int]()

    // Derived feature alias map for cases where we have derived features with different keys requested. Node id -> feature alias map
    val derivedFeatureAliasMap = resolvedGraph.getFeatureNames.asScala.map(x => x._2.getNodeId -> x._1)

    // nodeId to feature name constructed via iterating through all nodes which can represent named features
    val nodeIdToFeatureName = nodes.filter(node => node.isLookup || node.isAggregation || node.isTransformation).map(node =>
      if (node.isLookup) {
        if (derivedFeatureAliasMap.contains(node.getLookup.getId)) {
          (node.getLookup.getId, derivedFeatureAliasMap(node.getLookup.getId))
        } else {
          (node.getLookup.getId, node.getLookup.getFeatureName)
        }
      } else if (node.isAggregation) {
        if (derivedFeatureAliasMap.contains(node.getAggregation.getId)) {
          (node.getAggregation.getId, derivedFeatureAliasMap(node.getAggregation.getId))
        } else {
          (node.getAggregation.getId, node.getAggregation.getFeatureName)
        }
      } else {
        if (derivedFeatureAliasMap.contains(node.getTransformation.getId)) {
          (node.getTransformation.getId, derivedFeatureAliasMap(node.getTransformation.getId))
        } else if (node.getTransformation.hasFeatureName) {
          (node.getTransformation.getId, node.getTransformation.getFeatureName)
        } else {
          (node.getTransformation.getId, "__seq__join__feature") //TODO: Currently have hacky hard coded names, should add logic for generating names.
        }
      }
    ).toMap
    val joiner = SparkJoinWithJoinCondition(EqualityJoinConditionBuilder)
    var contextDf = df

    // Feature type configs used for FDS conversion and default value computation. Note we have to use the unresolved graph for this
    // purpose since the resolved graph loses this information.
    val featureTypeConfigs = unresolvedGraph.getFeatureNames.asScala.map(e => (e._1, PegasusRecordFeatureTypeConverter().convert(e._2.getFeatureVersion))).collect {
      case (key, Some(value)) => (key, value) // filter out Nones and get rid of Option
    }.toMap
    val featureVersionMap = unresolvedGraph.getFeatureNames.asScala.map(x => x._1 -> x._2.getFeatureVersion).toMap
    val defaultConverter = PegasusRecordDefaultValueConverter().convert(featureVersionMap)
    val seqJoinNodes = nodes.filter(node => node.getLookup != null)
    val seqJoinExpansionNodes = seqJoinNodes.map(node => node.getLookup.getLookupNode)
    val seqJoinBaseNodes = seqJoinNodes.map(node => node.getLookup.getLookupKey.asScala.find(x => x.isNodeReference).get.getNodeReference.getId)

    // Create a map of requested feature names to FeatureColumnFormat (Raw or FDS) for FDS conversion sake at the end of
    // execution. All features will default to Raw unless specified otherwise. Currently only MVEL operator will return
    // result as FDS, all other operators will be raw and need to be converted.
    val featureColumnFormatsMap = mutable.HashMap[String, FeatureColumnFormat](featureJoinConfig.joinFeatures.map(joinFeature => (joinFeature.featureName, FeatureColumnFormat.RAW)): _*)

    resolvedGraph.getFeatureNames.asScala.values.foreach(x => stack.push(x.getNodeId)) // push feature nodes onto stack

    sealed trait VisitedState
    case object NOT_VISITED extends VisitedState
    case object IN_PROGRESS extends VisitedState
    case object VISITED extends VisitedState

    val visitedState = Array.fill[VisitedState](nodes.length)(NOT_VISITED)
    while (stack.nonEmpty) {
      // SOMEHOW: TRAVERSE THE GRAPH up to the source nodes, "apply" attached concrete key to them (using the input
      // dataframe for CONTEXT keys e.g. memberID etc.), producing joined feature-dataframes, and then apply the
      // transformations and lookups while walking back down the graph towards the feature nodes??

      val nodeId = stack.pop
      if (visitedState(nodeId) != VISITED) {
        val node = nodes(nodeId)
        val dependencies = new Dependencies().getDependencies(node).asScala
        val unfinishedDependencies = dependencies.filter(visitedState(_) != VISITED)
        if (unfinishedDependencies.nonEmpty) {
          if (visitedState(nodeId) == IN_PROGRESS) {
            throw new RuntimeException("Encountered dependency cycle involving node " + nodeId)
          }
          stack.push(nodeId) // revisit this node after its dependencies
          unfinishedDependencies.foreach(stack.push(_)) // visit dependencies
          visitedState(nodeId) = IN_PROGRESS
        } else {
          // actually handle this node, since all its dependencies (if any) are ready
          assert(!nodeContext.contains(nodeId)) // we haven't set the NodeContext for this node yet, right?
          if (node.isDataSource) {
            val dataSource = node.getDataSource
            // TODO cache the datasource
            dataSource.getSourceType match {
              case DataSourceType.CONTEXT => // passthrough features
                if (dataSource.hasConcreteKey) {
                  val keyArray = dataSource.getConcreteKey.getKey
                  val key = nodeContext(keyArray.asScala.head).keyExpression
                  val df = nodeContext(keyArray.asScala.head).df
                  nodeContext(nodeId) = NodeContext(df, key)
                } else {
                  nodeContext(nodeId) = ContextNodeEvaluator.processContextNode(df, dataSource)
                }
              case DataSourceType.UPDATE =>
                nodeContext(nodeId) = TableNodeEvaluator.processTableNode(ss, dataSource, dataPathHandlers = dataPathHandlers)
              case DataSourceType.EVENT =>
                val obsTimeRange = SlidingWindowFeatureUtils.getObsSwaDataTimeRange(contextDf, featureJoinConfig.settings)._1
                val duration = PegasusRecordDurationConverter.convert(dataSource.getWindow())
                if (featureJoinConfig.settings.isEmpty || featureJoinConfig.settings.get.joinTimeSetting.isEmpty) {
                  throw new FeathrConfigException(
                    ErrorLabel.FEATHR_USER_ERROR,
                    "joinTimeSettings section is not defined in join config," +
                      " cannot perform window aggregation operation")
                }

                val adjustedTimeRange = OfflineDateTimeUtils.getFactDataTimeRange(obsTimeRange.get, duration, Array(featureJoinConfig.settings.get.joinTimeSetting.get.simulateTimeDelay.getOrElse(Duration.ZERO)))
                nodeContext(nodeId) = EventNodeEvaluator.processEventNode(ss, dataSource, Some(adjustedTimeRange), dataPathHandlers = dataPathHandlers)
            }
            nodeIdToNode.put(nodeId, if (dataSource.hasConcreteKey) dataSource.getConcreteKey.getKey().asScala else null)
          } else if (node.isExternal) {
            // An invalid case. There shouldn't be "unlinked" external feature references at this stage.
            throw new RuntimeException()
          } else if (node.isTransformation) {
            // Algorithm
            // 1. Load a UDF for the transformation function
            // 2. Apply it to the Input Col(s)
            // 3. Substitute default values (we need to do this at each step in case of derived features)
            // 4. Resulting Col is the transformed feature.
            val transformation = node.getTransformation

            val inputNodeId = transformation.getInputs.get(0).getId
            val featureName = if (transformation.getFeatureName == null) nodeIdToFeatureName(nodeId) else transformation.getFeatureName

            // TODO simplify this
            if (transformation.getFunction.getOperator == Operators.OPERATOR_FEATURE_ALIAS) {
              // In the case of a feature alias operator we can optimize this by just doing a withColumn call on the
              // contextDf instead of doing a join.
              contextDf = contextDf.withColumn(featureName, col(nodeIdToFeatureName(inputNodeId)))
              nodeContext(nodeId) = NodeContext(contextDf, nodeContext(inputNodeId).keyExpression)
            } else {
              val transformedContext = if (nodes(transformation.getInputs.get(0).getId()).isDataSource) {
                TransformationNodeEvaluator.processTransformationNode(featureName, nodeContext(inputNodeId).keyExpression,
                  nodeContext(inputNodeId), transformation, featureColumnFormatsMap, featureTypeConfigs, ss, dataPathHandlers = dataPathHandlers)
              } else {
                val isSeqJoinTransformation = if (seqJoinBaseNodes.contains(nodeId)) true else false
                var derivedContext = TransformationNodeEvaluator.processDerivedTransformationNode(featureName, contextDf, transformation,
                  featureColumnFormatsMap, featureTypeConfigs, nodeIdToFeatureName, derivedFeatureAliasMap.get(nodeId), isSeqJoinTransformation)
                if (transformation.getInputs.size() == 1) {
                  // Propagate key expression in the case of single input derived feature.
                  derivedContext = NodeContext(derivedContext.df, nodeContext(inputNodeId).keyExpression, derivedContext.featureColumn)
                }
                derivedContext
              }
              nodeContext(nodeId) = NodeContext(transformedContext.df, transformedContext.keyExpression, transformedContext.featureColumn)

              val concreteKeys = transformation.getConcreteKey.getKey.asScala.flatMap(x => {
                if (nodeContext(x).featureColumn.isDefined) {
                  Seq(nodeContext(x).featureColumn.get)
                } else {
                  nodeContext(x).keyExpression
                }
              })

              // Always join non-derived features to context
              // If the anchor feature is present in the contextDf, it must have been needed for a derived feature. Drop the
              // column and join the new one.
              if (contextDf.columns.contains(featureName)) {
                contextDf = contextDf.drop(featureName)
              }
              if (nodes(transformation.getInputs.get(0).getId()).isDataSource && !seqJoinExpansionNodes.contains(nodeId)) {
                val result = joiner.join(concreteKeys, contextDf,
                  nodeContext(nodeId).keyExpression, nodeContext(nodeId).df, JoinType.left_outer).drop(nodeContext(nodeId).keyExpression: _*)
                contextDf = substituteDefaults(result, Seq(featureName), defaultConverter, featureTypeConfigs, ss)
              } else if (!nodes(transformation.getInputs.get(0).getId()).isDataSource && !seqJoinExpansionNodes.contains(nodeId)) {
                contextDf = nodeContext(nodeId).df
                contextDf = substituteDefaults(contextDf, Seq(featureName), defaultConverter, featureTypeConfigs, ss)
              } else { // should be a expansion
                val withDefaultsDf = substituteDefaults(transformedContext.df, Seq(featureName), defaultConverter, featureTypeConfigs, ss)
                nodeContext(nodeId) = NodeContext(withDefaultsDf, transformedContext.keyExpression, transformedContext.featureColumn)
              }
            }

            nodeIdToNode.put(nodeId, transformation.getConcreteKey.getKey.asScala)
          } else if (node.isAggregation) {
            val featureName = nodeIdToFeatureName(node.getAggregation.getId)
            val concreteKeys = node.getAggregation.getConcreteKey.getKey.asScala.flatMap(x => nodeContext(x).keyExpression)
            val obsKeys = concreteKeys.map(k => s"CAST (${k} AS string)")
            val timestampCol = SlidingWindowFeatureUtils.constructTimeStampExpr(featureJoinConfig.settings.get.joinTimeSetting.get.timestampColumn.name,
              featureJoinConfig.settings.get.joinTimeSetting.get.timestampColumn.format)
            val updatedTimestampExpr = if (featureJoinConfig.settings.isDefined && featureJoinConfig.settings.get.joinTimeSetting.isDefined &&
              featureJoinConfig.settings.get.joinTimeSetting.get.useLatestFeatureData) {
              "unix_timestamp()"
            } else timestampCol

            val labelData = LabelData(contextDf, obsKeys, updatedTimestampExpr)
            val featureDf = nodeContext(node.getAggregation.getInput.getId()).df
            val featureKeys = nodeContext(node.getAggregation.getInput.getId()).keyExpression.dropRight(1)

            // todo - this is super hacky. Find a better way of retrieving this info.
            val timestampExpr = nodeContext(node.getAggregation.getInput.getId()).keyExpression.last

            val aggType = AggregationType.withName(node.getAggregation.getFunction.getParameters.get("aggregation_type"))
            val featureDef = node.getAggregation.getFunction.getParameters.get("target_column")
            val rewrittenFeatureDef = if (featureDef.contains(FeatureTransformation.USER_FACING_MULTI_DIM_FDS_TENSOR_UDF_NAME)) {
              // If the feature definition contains USER_FACING_MULTI_DIM_FDS_TENSOR_UDF_NAME then the feature column is already in FDS format.
              // So we strip the udf name and return only the feature name.
              (FeatureTransformation.parseMultiDimTensorExpr(featureDef), FDS_TENSOR)
            } else (featureDef, RAW)
            val aggregationSpec = aggType match {
              case AggregationType.SUM => new SumAggregate(rewrittenFeatureDef._1)
              case AggregationType.COUNT =>
                // The count aggregation in spark-algorithms MP is implemented as Sum over partial counts.
                // In Frame's use case, we want to treat the count aggregation as simple count of non-null items.
                val rewrittenDef = s"CASE WHEN ${rewrittenFeatureDef._1} IS NOT NULL THEN 1 ELSE 0 END"
                new CountAggregate(rewrittenDef)
              case AggregationType.AVG => new AvgAggregate(rewrittenFeatureDef._1) // TODO: deal with avg. of pre-aggregated data
              case AggregationType.MAX => new MaxAggregate(rewrittenFeatureDef._1)
              case AggregationType.MIN => new MinAggregate(rewrittenFeatureDef._1)
              case AggregationType.LATEST => new LatestAggregate(rewrittenFeatureDef._1)
              case AggregationType.MAX_POOLING => new MaxPoolingAggregate(rewrittenFeatureDef._1)
              case AggregationType.MIN_POOLING => new MinPoolingAggregate(rewrittenFeatureDef._1)
              case AggregationType.AVG_POOLING => new AvgPoolingAggregate(rewrittenFeatureDef._1)
            }

            val window = Duration.parse(node.getAggregation.getFunction.getParameters.get("window_size"))

            // todo change it to scala case match and extract to method
            val simTimeDelay = if (featureJoinConfig.featuresToTimeDelayMap.get(featureName).isDefined) {
              WindowTimeUnit.parseWindowTime(featureJoinConfig.featuresToTimeDelayMap(featureName))
            } else {
              if (featureJoinConfig.settings.isDefined && featureJoinConfig.settings.get.joinTimeSetting.isDefined &&
                featureJoinConfig.settings.get.joinTimeSetting.get.simulateTimeDelay.isDefined) {
                featureJoinConfig.settings.get.joinTimeSetting.get.simulateTimeDelay.get
              } else {
                Duration.ZERO
              }
            }
            val filterCondition = node.getAggregation.getFunction.getParameters.get("filter_expression") match {
              case x: String => Some(x)
              case null => None
            }

            val lateralViewDef = node.getAggregation.getFunction.getParameters.get("lateral_view_expression_0") match {
              case x: String => Some(x)
              case null => None
            }

            val lateralViewAlias = node.getAggregation.getFunction.getParameters.get("lateral_view_table_alias_0") match {
              case x: String => Some(x)
              case null => None
            }

            val lateralViewParams = if (lateralViewDef.isDefined && lateralViewAlias.isDefined) {
              Some(LateralViewParams(lateralViewDef.get, lateralViewAlias.get, None))
            } else None

            val groupBy = node.getAggregation.getFunction.getParameters.get("group_by_expression") match {
              case x: String => Some(x)
              case null => None
            }

            val limit = node.getAggregation.getFunction.getParameters.get("max_number_groups") match {
              case x: String => Some(x.toInt)
              case null => Some(0)
            }

            val groupbySpec = if (groupBy.isDefined) {
              Some(GroupBySpec(groupBy.get, limit.get))
            } else None

            val slidingWindowFeature = SlidingWindowFeature(featureName, aggregationSpec, WindowSpec(window, simTimeDelay), filterCondition, groupbySpec, lateralViewParams)
            val factDataSet = FactData(featureDf, featureKeys, timestampExpr, List(slidingWindowFeature))
            contextDf = SlidingWindowJoin.join(labelData, List(factDataSet))
            contextDf = substituteDefaults(contextDf, Seq(featureName), defaultConverter, featureTypeConfigs, ss)
            featureColumnFormatsMap(featureName) = rewrittenFeatureDef._2
            nodeContext(node.getAggregation.getId) = NodeContext(contextDf, featureNameToKeys(featureName), Some(featureName))
            nodeIdToNode.put(nodeId, node.getAggregation.getConcreteKey.getKey.asScala)
          } else if (node.isLookup) {
            val lookUpNode = node.getLookup
            // Assume there is only one lookup key that is a node reference. In the future this may not be true and will have to be changed.
            val baseNodeRef = lookUpNode.getLookupKey.asScala.find(x => x.isNodeReference).get.getNodeReference
            val baseNode = nodeContext(baseNodeRef.getId)
            val expansionNodeConcreteKey = nodeIdToNode.get(lookUpNode.getLookupNode)
            val baseDf = baseNode.df

            val baseKeyColumns = nodeIdToNode(lookUpNode.getLookupNode).flatMap(x => if (nodeContext(x).featureColumn.isDefined) {
              Seq(nodeContext(x).featureColumn.get)
            } else {
              nodeContext(x).keyExpression
            })

            // rename columns to know which columns are to be dropped
            val baseNodeRenamedCols = baseDf.columns.map(c => "__base__" + c).toSeq
            val expansionNodeId = lookUpNode.getLookupNode()
            val expansionNode = nodeContext(expansionNodeId)
            val expansionNodeRenamedCols = expansionNode.df.columns.map(c => "__expansion__" + c).toSeq
            val expansionNodeDfWithRenamedCols = expansionNode.df.toDF(expansionNodeRenamedCols: _*)

            // coerce left join keys before joining base and expansion features
            // val coercedBaseDf = SeqJoinAggregator.coerceLeftDfForSeqJoin(baseKeyColumns, contextDf)
            val left: DataFrame = PostTransformationUtil.transformFeatures(Seq((baseNode.featureColumn.get, baseNode.featureColumn.get)), contextDf,
              Map.empty[String, MvelDefinition], getDefaultTransformation)

            // Partition base feature (left) side of the join based on null values. This is an optimization so we don't waste
            // time joining nulls from the left df.
            val (coercedBaseDfWithNoNull, coercedBaseDfWithNull) = DataFrameSplitterMerger.splitOnNull(left, baseNode.featureColumn.get)

            val groupByColumn = "__frame_seq_join_group_by_id"
            /* We group by the monotonically_increasing_id to ensure we do not lose any of the observation data.
             * This is essentially grouping by all the columns in the left table
             * Note: we cannot add the monotonically_increasing_id before DataFrameSplitterMerger.splitOnNull.
             * the implementation of monotonically_increasing_id is non-deterministic because its result depends on partition IDs.
             * and it can generate duplicate ids between the withNoNull and WithNull part.
             * see: https://jira01.corp.linkedin.com:8443/browse/PROML-14360 and https://godatadriven.com/blog/spark-surprises-for-the-uninitiated
             */
            val leftWithUidDF = coercedBaseDfWithNoNull.withColumn(groupByColumn, monotonically_increasing_id)
            val seqJoinFeatureName = nodeIdToFeatureName(lookUpNode.getId)
            val (adjustedLeftJoinKey, explodedLeft) = SeqJoinAggregator.explodeLeftJoinKey(ss, leftWithUidDF, baseKeyColumns, seqJoinFeatureName)

            // join base feature's results with expansion feature's results
            val intermediateResult = SeqJoinJoiner.join(adjustedLeftJoinKey, explodedLeft,
              nodeContext(expansionNodeId).keyExpression.map(c => "__expansion__" + c), expansionNodeDfWithRenamedCols, JoinType.left_outer)
            val producedFeatureName = "__expansion__" + nodeIdToFeatureName(expansionNodeId)

            /*
             * Substitute defaults. The Sequential Join inherits the default values from the expansion feature definition.
             * This step is done before applying aggregations becaUSE the default values should be factored in.
             */
            val expansionFeatureDefaultValue = defaultConverter.get(nodeIdToFeatureName(expansionNodeId))
            val intermediateResultWithDefault =
              SeqJoinAggregator.substituteDefaultValuesForSeqJoinFeature(intermediateResult, producedFeatureName, expansionFeatureDefaultValue, ss)

            // apply aggregation to non-null part
            val aggregationType = lookUpNode.getAggregation
            val aggDf = SeqJoinAggregator.applyAggregationFunction(seqJoinFeatureName, producedFeatureName, intermediateResultWithDefault, aggregationType, groupByColumn)

            // Similarly, substitute the default values and apply aggregation function to the null part.
            val coercedBaseDfWithNullWithDefault = SeqJoinAggregator.substituteDefaultValuesForSeqJoinFeature(
              coercedBaseDfWithNull.withColumn(producedFeatureName, lit(null).cast(intermediateResult.schema(producedFeatureName).dataType)),
              producedFeatureName,
              expansionFeatureDefaultValue,
              ss)
            val coercedBaseDfWithNullWithAgg = SeqJoinAggregator.applyAggregationFunction(
              seqJoinFeatureName,
              producedFeatureName,
              coercedBaseDfWithNullWithDefault.withColumn(groupByColumn, monotonically_increasing_id),
              aggregationType,
              groupByColumn)

            // Union the rows that participated in the join and the rows with nulls
            val finalRes = DataFrameSplitterMerger.merge(aggDf, coercedBaseDfWithNullWithAgg)

            val resWithDroppedCols = finalRes.drop(nodeContext(expansionNodeId).keyExpression.map(c => "__expansion__" + c): _*)
              .drop("__base__" + baseNode.featureColumn.get)
            val finalResAfterDroppingCols = resWithDroppedCols.withColumnRenamed(producedFeatureName, nodeIdToFeatureName(lookUpNode.getId))

            // If the expansion feature is already in tensor format, then the seq join feature will also be in tensor format.
            if (featureColumnFormatsMap.get(nodeIdToFeatureName(expansionNodeId)).contains(FeatureColumnFormat.FDS_TENSOR)) {
              featureColumnFormatsMap(nodeIdToFeatureName(lookUpNode.getId)) = FeatureColumnFormat.FDS_TENSOR
            }

            contextDf = finalResAfterDroppingCols

            nodeContext(lookUpNode.getId) = NodeContext(finalResAfterDroppingCols, baseNode.keyExpression.map(x => x.split("__").last), Some(nodeIdToFeatureName(lookUpNode.getId)))
            nodeIdToNode.put(lookUpNode.getId, node.getLookup.getConcreteKey.getKey.asScala)
          }
          visitedState(nodeId) = VISITED
        }
      }
    }
    // Convert requested feature columns to FDS if needed and return df
    val necessaryColumns = resolvedGraph.getFeatureNames.asScala.keys ++ df.columns
    val toDropCols = contextDf.columns diff necessaryColumns.toSeq
    contextDf = contextDf.drop(toDropCols: _*)
    convertFCMResultDFToFDS(resolvedGraph.getFeatureNames.asScala.keys.toSeq,
      featureColumnFormatsMap.toMap, contextDf, featureTypeConfigs)
  }

  def generateFeatures(featureGenSpec: FeatureGenSpec): Map[TaggedFeatureName, SparkFeaturizedDataset] = {
    throw new UnsupportedOperationException()
  }
}
object FeathrClient2 {

  /**
   * Create an instance of a builder for constructing a FrameClient
   * @param sparkSession  the SparkSession required for the FrameClient to perform its operations
   * @return  Builder class
   */
  def builder(sparkSession: SparkSession): Builder = {
    new Builder(sparkSession)
  }

  // Mimicking FrameClient's builder
  class Builder(ss: SparkSession) {
    private val featureDefinitionLoader = FeatureDefinitionLoaderFactory.getInstance()

    private var featureDef: List[String] = List()
    private var localOverrideDef: List[String] = List()
    private var featureDefPath: List[String] = List()
    private var localOverrideDefPath: List[String] = List()
    private var dataPathHandlers: List[DataPathHandler] = List()

    // COPIED FROM FrameClient
    def addFeatureDef(featureDef: String): Builder = {
      this.featureDef = featureDef :: this.featureDef
      this
    }

    // COPIED FROM FrameClient
    def addFeatureDef(featureDef: Option[String]): Builder = {
      if (featureDef.isDefined) addFeatureDef(featureDef.get) else this
    }

    // COPIED FROM FrameClient
    def addLocalOverrideDef(localOverrideDef: String): Builder = {
      this.localOverrideDef = localOverrideDef :: this.localOverrideDef
      this
    }

    // COPIED FROM FrameClient
    def addLocalOverrideDef(localOverrideDef: Option[String]): Builder = {
      if (localOverrideDef.isDefined) addFeatureDef(localOverrideDef.get) else this
    }

    // COPIED FROM FrameClient
    def addFeatureDefPath(featureDefPath: String): Builder = {
      this.featureDefPath = featureDefPath :: this.featureDefPath
      this
    }

    // COPIED FROM FrameClient
    def addFeatureDefPath(featureDefPath: Option[String]): Builder = {
      if (featureDefPath.isDefined) addFeatureDefPath(featureDefPath.get) else this
    }

    // COPIED FROM FrameClient
    def addLocalOverrideDefPath(localOverrideDefPath: String): Builder = {
      this.localOverrideDefPath = localOverrideDefPath :: this.localOverrideDefPath
      this
    }

    // COPIED FROM FrameClient
    def addLocalOverrideDefPath(localOverrideDefPath: Option[String]): Builder = {
      if (localOverrideDefPath.isDefined) addLocalOverrideDefPath(localOverrideDefPath.get) else this
    }


    /**
     * Add a list of data path handlers to the builder. Used to handle accessing and loading paths caught by user's udf, validatePath
     *
     * @param dataPathHandlers custom data path handlers
     * @return FeathrClient.Builder
     */
    def addDataPathHandlers(dataPathHandlers: List[DataPathHandler]): Builder = {
      this.dataPathHandlers = dataPathHandlers ++ this.dataPathHandlers
      this
    }

    /**
     * Add a data path handler to the builder. Used to handle accessing and loading paths caught by user's udf, validatePath
     *
     * @param dataPathHandler custom data path handler
     * @return FeathrClient.Builder
     */
    def addDataPathHandler(dataPathHandler: DataPathHandler): Builder = {
      this.dataPathHandlers = dataPathHandler :: this.dataPathHandlers
      this
    }

    /**
     * Same as {@code addDataPathHandler(DataPathHandler)} but the input dataPathHandlers is optional and when it is missing,
     * this method performs an no-op.
     *
     * @param dataPathHandler custom data path handler
     * @return FeathrClient.Builder
     */
    def addDataPathHandler(dataPathHandler: Option[DataPathHandler]): Builder = {
      if (dataPathHandler.isDefined) addDataPathHandler(dataPathHandler.get) else this
    }

    /**
     * Build a new instance of the FrameClient2 from the added frame definition configs and any local overrides.
     *
     * @throws [[IllegalArgumentException]] an error when no feature definitions nor local overrides are configured.
     */
    def build(): FeathrClient2 = {
      import scala.collection.JavaConverters._

      require(
        localOverrideDefPath.nonEmpty || localOverrideDef.nonEmpty || featureDefPath.nonEmpty || featureDef.nonEmpty,
        "Cannot build frameClient without a feature def conf file/string or local override def conf file/string")

      // Append all the configs to this empty list, with the local override def config going last
      val configDocsInOrder = featureDef ::: featureDefPath.flatMap(x => readHdfsFile(Some(x))) :::
        localOverrideDef ::: localOverrideDefPath.flatMap(x => readHdfsFile(Some(x)))

      val featureDefs: mutable.Map[MlFeatureVersionUrn, FeatureDefinition] = mutable.HashMap()
      configDocsInOrder.map(new StringConfigDataProvider(_)).foreach(config =>
        featureDefs ++= featureDefinitionLoader.loadAllFeatureDefinitions(config, FeatureAnchorEnvironment.OFFLINE).asScala)

      val graph: ComputeGraph = FeatureDefinitionsConverter.buildFromFeatureUrns(featureDefs.asJava)

      new FeathrClient2(ss, graph, dataPathHandlers)
    }

    private def readHdfsFile(path: Option[String]): Option[String] =
      path.map(p => ss.sparkContext.textFile(p).collect.mkString("\n"))
  }
}
// scalastyle:on