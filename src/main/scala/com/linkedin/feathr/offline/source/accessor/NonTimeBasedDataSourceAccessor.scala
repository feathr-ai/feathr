package com.linkedin.feathr.offline.source.accessor

import com.linkedin.feathr.offline.job.SimpleApp
import com.linkedin.feathr.offline.source.DataSource
import com.linkedin.feathr.offline.source.dataloader.DataLoaderFactory
import com.linkedin.feathr.offline.testfwk.TestFwkUtils
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
    // Replace with preproceessed dataframe
    val df = source.pathList.map(fileLoaderFactory.create(_).loadDataFrame()).reduce((x, y) => x.fuzzyUnion(y))
    println("NonTimeBasedDataSourceAccessor df33333333")
    println("NonTimeBasedDataSourceAccessor df33333333")
    println("NonTimeBasedDataSourceAccessor df33333333")
    println("preproccessed df33333333")
    if (TestFwkUtils.IS_DEBUGGER_ENABLED) {
      println()
      println()
      source.pathList.foreach(sourcePath => println(f"${Console.GREEN}Source is: $sourcePath${Console.RESET}"))
      println(f"${Console.GREEN}Your source data schema is: ${Console.RESET}")
      println(f"${Console.GREEN}(meaning: |-- fieldName: type (nullable = true))${Console.RESET}")
      df.printSchema()
      println(f"${Console.GREEN}Showing source data: ${Console.RESET}")
      df.show(10)
      println()
      println()
    }
    println("preprocessed df")


    val preprocessedMap = SimpleApp.preprocessedDfMap
    println(preprocessedMap)
    println(source.path)
    println(preprocessedMap.contains(source.path))
    if (preprocessedMap.contains(source.path)) {
      println("preprocessed df")
      val preprocessedDf = preprocessedMap(source.path)
      preprocessedDf.show(10)
      preprocessedDf
    } else {
      println("original df")
      df
    }

  }
}
