package com.linkedin.feathr.swj

import com.linkedin.feathr.offline.evaluator.datasource.DataSourceNodeEvaluator.getClass
import com.linkedin.feathr.swj.join.{FeatureColumnMetaData, SlidingWindowJoinIterator}
import com.linkedin.feathr.swj.transformer.FeatureTransformer
import com.linkedin.feathr.swj.transformer.FeatureTransformer._
import org.apache.log4j.Logger
import org.apache.spark.sql.internal.SQLConf
import org.apache.spark.sql.types.{ArrayType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SparkSession}


object SlidingWindowJoin {

  val log = Logger.getLogger(getClass)
  lazy val spark: SparkSession = SparkSession.builder().getOrCreate()

  private val LABEL_VIEW_NAME = "label_data"

  /**
    * Public API of [[SlidingWindowJoin]]. Perform sliding window join/aggregation on
    * a label dataset and a list of fact datasets which share the same join key. Callers
    * need to invoke this API multiple times if the label dataset need to be joined with
    * fact datasets on multiple join keys.
    *
    * @param labelDataset Label dataset as a [[LabelData]]
    * @param factDatasets List of fact datasets as [[FactData]] to join on label dataset
    * @return The result of the sliding window join/aggregation as a DataFrame
    */
  def join(
      labelDataset: LabelData,
      factDatasets: List[FactData],
      numPartitions: Int = spark.sparkContext.getConf.getInt(SQLConf.SHUFFLE_PARTITIONS.key, 200)): DataFrame = {
    factDatasets.foreach(factDataset => {
      factDataset.aggFeatures.foreach(swaFeature => {
        log.info("Evaluating feature " + swaFeature.name + "\n")
      })
      log.info("Feature's keys are " + factDataset.joinKey + "\n")
    })

    val labelDF = addLabelDataCols(labelDataset.dataSource, labelDataset)
    // Partition the label DataFrame by join_key and sort each partition with (join_key, timestamp)
    var result = labelDF.repartition(numPartitions, labelDF.col(JOIN_KEY_COL_NAME))
      .sortWithinPartitions(JOIN_KEY_COL_NAME, TIMESTAMP_COL_NAME)
      .rdd
    var resultSchema = labelDF.schema
    // Pass label data join key and timestamp column index to SlidingWindowJoinIterator
    // so these 2 columns from label data Rows can be accessed with index instead of field name.
    // Accessing column with field name requires constructing GenericRowWithSchema instead of
    // plain Row. Passing these 2 index directly to the join iterator would simplify a lot
    // for the record creation in sliding window join.
    val labelJoinKeyIndex = resultSchema.fieldIndex(JOIN_KEY_COL_NAME)
    val labelTimestampIndex = resultSchema.fieldIndex(TIMESTAMP_COL_NAME)
    // For each fact dataset, perform the same standardized form transformation and partitioning/
    // sorting. The result is iteratively sliding-window-joined with either the original label
    // data or result from previous iteration. In addition, the StructType of the final DataFrame
    // is also iteratively constructed.
    factDatasets.foreach(factDataset => {
      // Transform the input fact DataFrame into standardized feature DataFrame. Then partition
      // the feature DataFrame by join_key and sort each partition with (join_key, timestamp)
        val factDF = FeatureTransformer.transformFactData(factDataset)
        val factRDD = factDF.repartition(numPartitions, factDF.col(JOIN_KEY_COL_NAME))
          .sortWithinPartitions(JOIN_KEY_COL_NAME, TIMESTAMP_COL_NAME)
          .rdd
        val factSchema = factDF.schema
        // Use zipPartition to perform the join. Preserve partition to avoid unnecessary shuffle.
        if (!factDataset.dataSource.isEmpty) {
          result = result.zipPartitions(factRDD, preservesPartitioning = true) {
            (left: Iterator[Row], right: Iterator[Row]) => {
              new SlidingWindowJoinIterator(left, right, resultSchema, labelJoinKeyIndex,
                labelTimestampIndex, factSchema, factDataset.aggFeatures)
            }
          }
        } else {
          // If factRDD is empty, repartition will not work and will throw zipPartitions exception,
          // instead, we just populate null columns directly
          val NULL_SEQ: Seq[Any] =  factDataset.aggFeatures.map(_ => null)
          result = result.mapPartitions(iter => {
            iter.map(row => Row.merge(row, Row.fromSeq(NULL_SEQ)))
          })
        }
        // Use the same logic to construct the list of FeatureColumnMetaData on driver side.
        // Then prepare the StructType of the result of the current join iteration.
        val featureColMetadata: List[FeatureColumnMetaData] =
        SlidingWindowJoinIterator.generateFeatureColumns(factDataset.aggFeatures, factSchema)
        val featureSchema = StructType(featureColMetadata.map(col =>
          if (col.groupSpec.isEmpty) {
            StructField(col.featureName, col.aggDataType, nullable = true)
          } else {
            val elementStructType = StructType(
              StructField(GROUP_COL_NAME, col.groupColDataType.get, nullable = true) ::
                StructField("group_agg_metric", col.aggDataType, nullable = true) :: Nil
            )
            val arrayType = ArrayType(elementStructType)
            StructField(col.featureName, arrayType, nullable = true)
          }))
        resultSchema = StructType(resultSchema ++ featureSchema)

    })
    spark.createDataFrame(result, resultSchema).drop(JOIN_KEY_COL_NAME, TIMESTAMP_COL_NAME)
  }

  /**
    * Add the converted timestamp column and the join key column to the label dataset. The original
    * timestamp column might not be unix timestamp of type long, but formatted date strings instead.
    * The sliding window join library supports using the to_unix_timestamp Spark UDF to convert the
    * date string to unix timestamp. In addition, the join key column specified might involve
    * multiple columns. A struct type is created to handle multi-column join key.
    */
  private def addLabelDataCols(labelDF: DataFrame, labelDataset: LabelData): DataFrame = {
    labelDF.createOrReplaceTempView(LABEL_VIEW_NAME)
    spark.sql(
      s"""
         |SELECT
         |${if (labelDataset.joinKey.size > 1)
              s"struct(${labelDataset.joinKey.mkString(",")})"
            else
              s"${labelDataset.joinKey.head}"} AS $JOIN_KEY_COL_NAME,
         |${labelDataset.timestampCol} AS $TIMESTAMP_COL_NAME, * FROM $LABEL_VIEW_NAME
       """.stripMargin
    )
  }
}
