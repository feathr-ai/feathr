package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.feathr.core.config.producer.sources.HdfsConfigWithRegularData;
import com.linkedin.feathr.core.config.producer.sources.HdfsConfigWithSlidingWindow;
import com.linkedin.feathr.core.config.producer.sources.SlidingWindowAggrConfig;
import com.linkedin.feathr.core.config.producer.sources.TimeWindowParams;
import com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class DatasetSnapshotTimeFormatBuilderTest {
  private DatasetSnapshotTimeFormatBuilder _datasetSnapshotTimeFormatBuilder = DatasetSnapshotTimeFormatBuilder.getInstance();

  @Test
  public void testStandardDateFormat() {
    TimeWindowParams timeWindowParams = new TimeWindowParams("testField", "yyyy/MM/dd/HH/mm/ss");
    SlidingWindowAggrConfig slidingWindowAggrConfig = new SlidingWindowAggrConfig(true, timeWindowParams);
    HdfsConfigWithSlidingWindow hdfsConfig = new HdfsConfigWithSlidingWindow("hdfSource", "/test/path", slidingWindowAggrConfig);
    Optional<DatasetSnapshotTimeFormat> datasetSnapshotTimeFormat = _datasetSnapshotTimeFormatBuilder.build(hdfsConfig);
    assertTrue(datasetSnapshotTimeFormat.isPresent());
    assertTrue(datasetSnapshotTimeFormat.get().isStandardDateTimeFormat());

    HdfsConfigWithRegularData hdfsConfigWithRegularData = new HdfsConfigWithRegularData("hdfSource", "/test/path", true);
    datasetSnapshotTimeFormat = _datasetSnapshotTimeFormatBuilder.build(hdfsConfigWithRegularData);
    assertTrue(datasetSnapshotTimeFormat.isPresent());
    assertTrue(datasetSnapshotTimeFormat.get().isStandardDateTimeFormat());
  }

  @Test
  public void testCustomDateFormat() {
    HdfsConfigWithRegularData hdfsConfigWithRegularData = new HdfsConfigWithRegularData("hdfSource", "/test/path", "yyMMdd", false);
    Optional<DatasetSnapshotTimeFormat> datasetSnapshotTimeFormat = _datasetSnapshotTimeFormatBuilder.build(hdfsConfigWithRegularData);
    assertTrue(datasetSnapshotTimeFormat.isPresent());
    assertTrue(datasetSnapshotTimeFormat.get().isDateTimeFormat());
    assertEquals(datasetSnapshotTimeFormat.get().getDateTimeFormat(), "yyMMdd");
  }

  @Test
  public void testEmptyDateFormat() {
    HdfsConfigWithRegularData hdfsConfigWithRegularData = new HdfsConfigWithRegularData("hdfSource", "/test/path", false);
    Optional<DatasetSnapshotTimeFormat> datasetSnapshotTimeFormat = _datasetSnapshotTimeFormatBuilder.build(hdfsConfigWithRegularData);
    assertFalse(datasetSnapshotTimeFormat.isPresent());
  }


}
