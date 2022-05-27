package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.common.exception.{ErrorLabel, FeathrException}
import org.apache.avro.Schema
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * Load Parquet file for testing.
 * @param ss the spark session
 * @param path input resource path
 */
private[offline] class ParquetDataLoader(ss: SparkSession, path: String) extends DataLoader {
  override def loadSchema(): Schema = {
    // Not needed for parquet since it can load schema from itself
    ???
  }

  override def loadRdd(expectDatumType: Class[_]): RDD[_] = {
    throw new FeathrException(ErrorLabel.FEATHR_ERROR, "loadRdd not implemented for this loader")
  }

  /**
   * load the source data as DataFrame.
   */
  override def loadDataFrame(): DataFrame = {
    ss.read.parquet(path)
  }
}
