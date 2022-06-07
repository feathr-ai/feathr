package com.linkedin.feathr.core.configbuilder.typesafe.producer.anchors;

import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import java.util.List;
import java.util.Map;
import org.testng.annotations.Test;

import static org.testng.Assert.*;


public class FeatureConfigBuilderTest {
  @Test(description = "Parsing and building of extractor based feature config")
  public void extractorBasedFeatureConfigs() {
    testFeatureConfigBuilder(FeatureFixture.feature1ConfigStr, FeatureFixture.expFeature1ConfigObj);
  }

  @Test(description = "Parsing and building of extractor based feature config with special characters . and :")
  public void extractorBasedFeatureConfigsWithSpecialCharacters() {
    testFeatureConfigBuilder(FeatureFixture.feature1ConfigStr, FeatureFixture.expFeature1ConfigObj);
  }

  @Test(description = "Parsing and building of extractor based feature config")
  public void extractorBasedFeatureConfigsWithExtractor() {
    testFeatureConfigBuilder(FeatureFixture.feature2ConfigStr, FeatureFixture.expFeature2ConfigObj);
  }

  @Test(description = "Parsing and building of extractor based feature config with type config")
  public void extractorBasedFeatureConfigsWithExtractorWithType() {
    testFeatureConfigBuilder(FeatureFixture.feature2ConfigWithTypeStr, FeatureFixture.expFeature2WithTypeConfigObj);
  }

  @Test(description = "Parsing and building of extractor based feature config with type config and parameters")
  public void extractorBasedFeatureConfigsWithParameterizedExtractor() {
    testFeatureConfigBuilder(FeatureFixture.feature5ConfigWithTypeStr, FeatureFixture.expFeature5WithTypeConfigObj);
  }

  @Test(description = "Parsing and building of expression based feature config")
  public void expressionBasedFeatureConfigs() {
    testFeatureConfigBuilder(FeatureFixture.feature3ConfigStr, FeatureFixture.expFeature3ConfigObj);
  }

  @Test(description = "Parsing and building of time-window feature config")
  public void timeWindowFeatureConfigs() {
    testFeatureConfigBuilder(FeatureFixture.feature4ConfigStr, FeatureFixture.expFeature4ConfigObj);
}

  private Map<String, FeatureConfig> buildFeatureConfig(String featureConfigStr) {
    Config fullConfig = ConfigFactory.parseString(featureConfigStr);
    ConfigValue configValue = fullConfig.getValue(AnchorConfig.FEATURES);

    switch (configValue.valueType()) {
      case OBJECT:
        Config featuresConfig = fullConfig.getConfig(AnchorConfig.FEATURES);
        return FeatureConfigBuilder.build(featuresConfig);

      case LIST:
        List<String> featureNames = fullConfig.getStringList(AnchorConfig.FEATURES);
        return FeatureConfigBuilder.build(featureNames);

      default:
        throw new RuntimeException("Unexpected value type " + configValue.valueType()
            + " for " + AnchorConfig.FEATURES);
    }
  }

  private void testFeatureConfigBuilder(String featureConfigStr, Map<String, FeatureConfig> expFeatureConfigObj) {
    Map<String, FeatureConfig> obsFeatureConfigObj = buildFeatureConfig(featureConfigStr);
    assertEquals(obsFeatureConfigObj, expFeatureConfigObj);
  }
}
