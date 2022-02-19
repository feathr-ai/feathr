package com.linkedin.feathr.offline.source.accessor

import com.linkedin.feathr.offline.source.DataSource
import com.linkedin.feathr.offline.source.dataloader.DataLoaderFactory
import com.linkedin.feathr.offline.transformation.DataFrameExt._
import org.apache.spark.sql.{DataFrame, SparkSession}
/**
 * load a dataset from a non-partitioned source.
 * @param ss the spark session
 * @param fileLoaderFactory loader for a single file
 * @param source          datasource
 * @param expectDatumType expected datum type of the loaded dataset, could be Avro GenericRecord or Avro SpecificRecord
 */
private[offline] class NonTimeBasedDataSourceAccessor(
    ss: SparkSession,
    fileLoaderFactory: DataLoaderFactory,
    source: DataSource,
    expectDatumType: Option[Class[_]])
    extends DataSourceAccessor(source) {

  /**
   * get source data as a dataframe
   *
   * @return the dataframe
   */
  override def get(): DataFrame = {
    source.pathList.map(fileLoaderFactory.create(_).loadDataFrame()).reduce((x, y) => x.fuzzyUnion(y))
  }
}
