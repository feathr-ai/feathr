package com.linkedin.frame.config;

import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class FeatureDefinitionLoaderFactoryTest {
  @Test
  public void testGetFeatureDefinitionLoader() {
    FeatureDefinitionLoader featureDefinitionConsumerLoader =
        FeatureDefinitionLoaderFactory.getInstance();
    assertNotNull(featureDefinitionConsumerLoader);
  }
}
