package com.linkedin.feathr.offline.source.dataloader

import org.apache.avro.Schema
import org.apache.log4j.Logger
import org.apache.spark.sql.DataFrame

/**
 * The data loader can load source data DataFrame or RDD.
 */
private[offline] trait DataLoader extends Serializable {
  @transient lazy val log = Logger.getLogger(getClass.getName)
  /**
   * get the schema of the source. It's only used in the deprecated DataSource.getDataSetAndSchema
   * @return an Avro Schema
   */
  def loadSchema() : Schema

  /**
   * load the source data as dataframe.
   * @return an dataframe
   */
  def loadDataFrame() : DataFrame
}
