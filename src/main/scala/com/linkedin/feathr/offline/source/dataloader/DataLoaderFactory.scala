package com.linkedin.feathr.offline.source.dataloader

import org.apache.log4j.Logger
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
}

private[offline] object DataLoaderFactory {

  /**
   * construct a specific loader factory based on whether the spark session is local or not.
   */
  def apply(ss: SparkSession): DataLoaderFactory = {
    if (ss.sparkContext.isLocal) {
      new LocalDataLoaderFactory(ss)
    } else {
      new HdfsDataLoaderFactory(ss)
    }
  }
}
