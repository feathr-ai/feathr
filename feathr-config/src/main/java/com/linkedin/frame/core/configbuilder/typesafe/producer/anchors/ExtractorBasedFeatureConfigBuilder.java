package com.linkedin.frame.core.configbuilder.typesafe.producer.anchors;

import com.linkedin.frame.core.config.producer.anchors.SimpleFeatureConfig;
import com.linkedin.frame.core.config.producer.common.FeatureTypeConfig;
import com.linkedin.frame.core.configbuilder.typesafe.producer.common.FeatureTypeConfigBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

import static com.linkedin.frame.core.config.producer.anchors.FeatureConfig.*;


/**
 * Builds an ExtractorBasedFeatureConfig object
 */
class ExtractorBasedFeatureConfigBuilder {
  private final static Logger logger = Logger.getLogger(ExtractorBasedFeatureConfigBuilder.class);

  private ExtractorBasedFeatureConfigBuilder() {
  }

  public static SimpleFeatureConfig build(String featureName, Config featureConfig) {

    FeatureTypeConfig featureTypeConfig = FeatureTypeConfigBuilder.build(featureConfig);

    String defaultValue = featureConfig.hasPath(DEFAULT) ? featureConfig.getValue(DEFAULT).render() : null;
    Map<String, String> parameters =
        featureConfig.hasPath(PARAMETERS) ? getParameters(featureConfig) : Collections.emptyMap();
    logger.trace("Built ExtractorBasedFeatureConfig for feature" + featureName);
    return new SimpleFeatureConfig(featureName, featureTypeConfig, defaultValue, parameters);
  }

  public static Map<String, String> getParameters(Config anchorConfig) {
    logger.debug("Building Parameters objects in anchor " + anchorConfig);

    Config config = anchorConfig.getConfig(PARAMETERS);
    ConfigObject featuresConfigObj = config.root();
    return featuresConfigObj.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().render(ConfigRenderOptions.concise())));
  }
}
