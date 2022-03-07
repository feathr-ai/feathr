package com.linkedin.feathr.offline.source.dataloader

import com.linkedin.feathr.offline.TestFeathr
import org.apache.avro.Schema
import org.apache.spark.sql.Row
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

import scala.collection.JavaConverters._

/**
 * unit tests for [[CsvDataLoader]]
 */
class TestCsvDataLoader extends TestFeathr {

  @Test(description = "test loading dataframe with CsvDataLoader")
  def testLoadDataFrame() : Unit = {
    val dataLoader = new CsvDataLoader(ss, "anchor1-source.csv")
    val df = dataLoader.loadDataFrame()
    val expectedRows = Array(
      Row("1", "apple", "10", "10", "0.1"),
      Row("2", "orange", "10", "3", "0.1"),
      Row("3", "banana", "10", "2", "0.9"),
      Row("4", "apple", "10", "1", "0.7"),
      Row("5", "apple", "11", "11", "1.0"),
      Row("7", "banana", "2", "10", "81.27"),
      Row("9", "banana", "4", "4", "0.4")
    )
    assertEquals(df.collect(), expectedRows)
  }


  @Test(description = "test loading Avro schema with CsvDataLoader")
  def testLoadSchema() : Unit = {
    val dataLoader = new CsvDataLoader(ss, "anchor1-source.csv")
    val schema = dataLoader.loadSchema()

    val fieldSchema = Schema.createUnion(List(Schema.create(Schema.Type.STRING), Schema.create(Schema.Type.NULL)).asJava)
    val expectedFields = List(
      new Schema.Field("alpha", fieldSchema, null, null),
      new Schema.Field("beta", fieldSchema, null, null),
      new Schema.Field("gamma", fieldSchema, null, null),
      new Schema.Field("mId", fieldSchema, null, null),
      new Schema.Field("omega", fieldSchema, null, null)
    ).asJava
    val expectedSchema = Schema.createRecord(expectedFields)
    assertEquals(schema.getFields, expectedSchema.getFields)
  }
}
