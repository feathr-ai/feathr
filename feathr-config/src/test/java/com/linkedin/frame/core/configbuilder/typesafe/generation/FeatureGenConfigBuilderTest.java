package com.linkedin.frame.core.configbuilder.typesafe.generation;

import com.linkedin.frame.core.config.generation.FeatureGenConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.generation.GenerationFixture.*;
import static org.testng.Assert.*;


/**
 * test of Frame feature generation config object
 */
public class FeatureGenConfigBuilderTest {

  @Test(description = "Tests building of generation config for the case with all supported fields")
  public void testWithFullFieldsCase() {
    testFeatureGenConfigBuilder(generationConfigStr1, expGenerationConfigObj1);
  }

  @Test(description = "Tests building of generation config for cases with minimal supported fields")
  public void testWithDefaultFieldsCase() {
    testFeatureGenConfigBuilder(generationConfigStr2, expGenerationConfigObj2);
  }

  @Test(description = "Tests building of nearline generation config for all possible cases")
  public void testWithNealineFieldsCase() {
    testFeatureGenConfigBuilder(nearlineGenerationConfigStr, nearlineGenerationConfigObj);
  }

  private void testFeatureGenConfigBuilder(String configStr, FeatureGenConfig expFeatureGenConfigObj) {
    Config withDefaultConfig = ConfigFactory.parseString(configStr);
    FeatureGenConfig generationConfigObj = FeatureGenConfigBuilder.build(withDefaultConfig);
    assertEquals(generationConfigObj, expFeatureGenConfigObj);
  }
}
