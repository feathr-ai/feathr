package com.linkedin.feathr.offline.source.dataloader

import org.apache.avro.Schema
import org.apache.log4j.Logger
import org.apache.spark.rdd.RDD
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
   * load the source data as RDD.
   * @param expectDatumType the class of the RDD data type. Ususally it's GenericRecord or a subclass of SpecificRecord.
   * @return an RDD
   */
  def loadRdd(expectDatumType: Class[_]) : RDD[_]

  /**
   * load the source data as dataframe.
   * @return an dataframe
   */
  def loadDataFrame() : DataFrame
}
