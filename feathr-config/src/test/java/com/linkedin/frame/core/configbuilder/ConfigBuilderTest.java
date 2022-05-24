package com.linkedin.frame.core.configbuilder;

import com.linkedin.frame.core.config.producer.FeatureDefConfig;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.FeatureDefFixture.*;
import static org.testng.Assert.*;


public class ConfigBuilderTest {

  @Test(description = "Tests build of FeatureDefConfig object for a syntactically valid config")
  public void testFeatureDefConfig() {
    ConfigBuilder configBuilder = ConfigBuilder.get();
    try {
      FeatureDefConfig obsFeatureDefConfigObj = configBuilder.buildFeatureDefConfigFromString(featureDefConfigStr1);
      assertEquals(obsFeatureDefConfigObj, expFeatureDefConfigObj1);
    } catch (ConfigBuilderException e) {
      fail("Test failed", e);
    }
  }

  @Test
  public void testFeatureCareers() {
    ConfigBuilder configBuilder = ConfigBuilder.get();
    try {
      FeatureDefConfig obsFeatureDefConfigObj
          = configBuilder.buildFeatureDefConfig("frame-feature-careers-featureDef-offline.conf");
    } catch (ConfigBuilderException e) {
      fail("Test failed", e);
    }
  }
}
