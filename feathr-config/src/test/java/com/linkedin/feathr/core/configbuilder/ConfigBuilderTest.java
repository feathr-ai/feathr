package com.linkedin.feathr.core.configbuilder;

import com.linkedin.feathr.core.configbuilder.typesafe.producer.FeatureDefFixture;
import com.linkedin.feathr.core.config.producer.FeatureDefConfig;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class ConfigBuilderTest {

  @Test(description = "Tests build of FeatureDefConfig object for a syntactically valid config")
  public void testFeatureDefConfig() {
    ConfigBuilder configBuilder = ConfigBuilder.get();
    try {
      FeatureDefConfig obsFeatureDefConfigObj = configBuilder.buildFeatureDefConfigFromString(
          FeatureDefFixture.featureDefConfigStr1);
      assertEquals(obsFeatureDefConfigObj, FeatureDefFixture.expFeatureDefConfigObj1);
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
