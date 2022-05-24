package com.linkedin.frame.config.featureanchor.builder;
import com.linkedin.frame.core.config.producer.sources.TimeWindowParams;
import com.linkedin.feathr.featureDataModel.TimeField;
import com.linkedin.feathr.featureDataModel.TimestampGranularity;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class HdfsTimeFieldBuilderTest {
  @Test
  public void testEpochSecondGranularity() {
    TimeWindowParams timeWindowParams = new TimeWindowParams("testField", "epoch");
    TimeField timeField = new HdfsTimeFieldBuilder(timeWindowParams).build();
    assertEquals(timeField.getName(), "testField");
    assertEquals(timeField.getFormat().getTimestampGranularity(), TimestampGranularity.SECONDS);
  }


  @Test
  public void testEpochMilliSecondGranularity() {
    TimeWindowParams timeWindowParams = new TimeWindowParams("testField", "epoch_millis");
    TimeField timeField = new HdfsTimeFieldBuilder(timeWindowParams).build();
    assertEquals(timeField.getName(), "testField");
    assertEquals(timeField.getFormat().getTimestampGranularity(), TimestampGranularity.MILLISECONDS);
  }

  @Test
  public void testDateTimeFormat() {
    TimeWindowParams timeWindowParams = new TimeWindowParams("testField", "yyyy/MM/dd/HH/mm/ss");
    TimeField timeField = new HdfsTimeFieldBuilder(timeWindowParams).build();
    assertEquals(timeField.getName(), "testField");
    assertEquals(timeField.getFormat().getDateTimeFormat(), "yyyy/MM/dd/HH/mm/ss");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testInvalidFormat() {
    TimeWindowParams timeWindowParams = new TimeWindowParams("testField", "invalid-format");
    TimeField timeField = new HdfsTimeFieldBuilder(timeWindowParams).build();
  }
}