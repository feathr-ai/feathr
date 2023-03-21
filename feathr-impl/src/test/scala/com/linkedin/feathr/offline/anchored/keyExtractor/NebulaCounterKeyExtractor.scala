package com.linkedin.feathr.offline.anchored.keyExtractor

import com.linkedin.feathr.sparkcommon.SourceKeyExtractor
import org.apache.spark.sql.functions._
import org.apache.spark.sql.DataFrame
/**
 * This Spark job pulls online counters and features, computes derived features and labels the data to train the xgboost model in invitation scorer for ATO detection.
 */
class NebulaCounterKeyExtractor extends SourceKeyExtractor {

  override def getKeyColumnNames(datum: Option[Any]): Seq[String] = Seq("memberId")

  // This extractor is used as a pre-processor. Columns not present in the resulting DF will be unavailable to FRAME features
  override def appendKeyColumns(dataFrame: DataFrame): DataFrame = {
    import dataFrame.sparkSession.implicits._
    // dataFrame.withColumn("roundedSentTimeToHr", from_unixtime($"send_time"/1000, "yyyy-MM-dd-HH"))
    //   .withColumn("roundedMin", (from_unixtime($"send_time"/1000, "mm")/5).cast("integer")*5)
    //   .withColumn("bucketizedSentTimeToFiveMin",
    //                     concat($"roundedSentTimeToHr", when($"roundedMin">=10, lit("-")).otherwise(lit("-0")), $"roundedMin"))
    // .select($"sender_id" as "memberId",
    //   unix_timestamp(col("bucketizedSentTimeToFiveMin"),"yyyy-MM-dd-HH-mm").as("unixBucket"),
    //   $"send_time" as "time")

    val df = dataFrame.withColumn("roundedSentTimeToHr", from_unixtime($"header.time"/1000, "yyyy-MM-dd-HH"))
      .withColumn("roundedMin", (from_unixtime($"header.time"/1000, "mm")/5).cast("integer")*5)
      .withColumn("bucketizedSentTimeToFiveMin",
        concat($"roundedSentTimeToHr", when($"roundedMin">=10, lit("-")).otherwise(lit("-0")), $"roundedMin"))
      .select($"header.memberid" as "memberId",
        unix_timestamp(col("bucketizedSentTimeToFiveMin"),"yyyy-MM-dd-HH-mm").as("unixBucket"),
        $"header.time" as "time")
    df.show()
    df
  }
}
