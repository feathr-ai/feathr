package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.offline.config.location.InputLocation
import org.apache.log4j.Logger
import org.apache.spark.customized.CustomGenericRowWithSchema
import org.apache.spark.sql.SparkSession

/**
 * DataLoaderFactory trait that will create a data loader based on the type of the input.
 */
private[offline] trait DataLoaderFactory {
  @transient lazy val log = Logger.getLogger(getClass.getName)
  /**
   * create a data loader based on the file type.
   *
   * @param path the input file path
   * @return a [[DataLoader]]
   */
  def create(path: String): DataLoader

  def createFromLocation(input: InputLocation): DataLoader = create(input.getPath)
}

private[offline] object DataLoaderFactory {
  CustomGenericRowWithSchema
  /**
   * construct a specific loader factory based on whether the spark session is local or not.
   */
  def apply(ss: SparkSession, streaming: Boolean = false): DataLoaderFactory = {
    if (streaming) {
      new StreamingDataLoaderFactory(ss)
    } else if (ss.sparkContext.isLocal) {
      // For test
      new LocalDataLoaderFactory(ss)
    } else {
      new BatchDataLoaderFactory(ss)
    }
  }
}
