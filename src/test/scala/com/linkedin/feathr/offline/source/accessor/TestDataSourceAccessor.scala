package com.linkedin.feathr.offline.source.accessor

import com.linkedin.feathr.offline.TestFeathr
import com.linkedin.feathr.offline.TestUtils.createDailyInterval
import com.linkedin.feathr.offline.source.{DataSource, SourceFormatType}
import org.testng.Assert.assertTrue
import org.testng.annotations.{BeforeClass, Test}

class TestDataSourceAccessor extends TestFeathr {

  val testDataRoot = "src/test/resources/"

  private val sourceInterval = Some(createDailyInterval("2018-04-30", "2018-05-02"))

  @BeforeClass
  override def setup(): Unit = {
    ss = TestFeathr.getOrCreateSparkSessionWithHive
    super.setup()
  }

  @Test(description = "It should create a NonTimeBasedDataSourceAccessor if the time interval is not provided")
  def testCreateWithNoTimeInterval(): Unit = {
    val source = DataSource("anchor5-source.avro.json", SourceFormatType.FIXED_PATH)
    val accessor = DataSourceAccessor(ss, source, None, None, failOnMissingPartition = false)
    assertTrue(accessor.isInstanceOf[NonTimeBasedDataSourceAccessor])
  }

  @Test(description = "It should create a NonTimeBasedDataSourceAccessor if it's fixed path")
  def testCreateWithFixedPath(): Unit = {
    val interval = Some(createDailyInterval("2019-12-09", "2019-12-10"))
    val source = DataSource("anchor5-source.avro.json", SourceFormatType.FIXED_PATH)
    val accessor = DataSourceAccessor(ss, source, interval, None, failOnMissingPartition = false)
    assertTrue(accessor.isInstanceOf[NonTimeBasedDataSourceAccessor])
  }

  @Test(description = "It should create a NonTimeBasedDataSourceAccessor if it's list path")
  def testCreateWithListPath(): Unit = {
    val interval = Some(createDailyInterval("2019-12-09", "2019-12-10"))
    val source = DataSource("anchor1-source.csv;anchor5-source.avro.json", SourceFormatType.FIXED_PATH)
    val accessor = DataSourceAccessor(ss, source, interval, None, failOnMissingPartition = false)
    assertTrue(accessor.isInstanceOf[NonTimeBasedDataSourceAccessor])
  }


  @Test(description = "It should create a PathPartitionedTimeSeriesSourceAccessor from a daily path")
  def testCreateFromPartitionedFiles(): Unit = {
    val source = DataSource("localTimeAwareTestFeatureData/daily", SourceFormatType.TIME_SERIES_PATH)
    val accessor = DataSourceAccessor(ss, source, sourceInterval, None, failOnMissingPartition = false)
    assertTrue(accessor.isInstanceOf[PathPartitionedTimeSeriesSourceAccessor])
  }

  @Test(description = "It should create a PathPartitionedTimeSeriesSourceAccessor from a path with time path pattern")
  def testCreateFromPartitionedFilesWithTimePathPattern(): Unit = {
    val source = DataSource("localTimeAwareTestFeatureData/daily", SourceFormatType.TIME_SERIES_PATH, None, Some("yyyy/MM/dd"))
    val accessor = DataSourceAccessor(ss, source, sourceInterval, None, failOnMissingPartition = false)
    assertTrue(accessor.isInstanceOf[PathPartitionedTimeSeriesSourceAccessor])
  }

  @Test(description = "It should create a NonTimeBasedDataSourceAccessor from a single file")
  def testCreateFromSingleFile(): Unit = {
    val source = DataSource("anchor1-source.csv", SourceFormatType.FIXED_PATH)
    val accessor = DataSourceAccessor(ss, source, sourceInterval, None, failOnMissingPartition = false)
    assertTrue(accessor.isInstanceOf[NonTimeBasedDataSourceAccessor])
  }
}
