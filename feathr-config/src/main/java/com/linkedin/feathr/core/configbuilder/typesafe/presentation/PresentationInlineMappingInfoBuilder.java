package com.linkedin.feathr.core.configbuilder.typesafe.presentation;

import com.linkedin.data.template.StringMap;
import com.linkedin.feathr.core.config.presentation.PresentationConfig;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.linkedin.feathr.core.utils.ConfigUtils;
import com.linkedin.frame.common.PresentationInlineMappingInfo;
import com.typesafe.config.Config;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

/**
 * PresentationInlineMappingInfo Builder for Frame presentation config
 * Example:
 * <pre>
 *   {@code
 *       presentationInlineMapping: {
 *         name: "userDefinedPresentationInlineMappingName"
 *         mapping: {
 *             featureValue1: "memberFacingValue1"
 *             featureValue2: "memberFacingValue2"
 *         }
 *     }
 *   }
 * </pre>
 */
public class PresentationInlineMappingInfoBuilder {

  private final static Logger logger = Logger.getLogger(PresentationInlineMappingInfoBuilder.class);
  private static final PresentationInlineMappingInfoBuilder INSTANCE = new PresentationInlineMappingInfoBuilder();
  static PresentationInlineMappingInfoBuilder getInstance() {
    return INSTANCE;
  }

  private PresentationInlineMappingInfoBuilder() {

  }

  PresentationInlineMappingInfo build(Config config) {
    List<String> missingFields = Stream.of(PresentationConfig.NAME, PresentationConfig.MAPPING)
        .filter(s -> !config.hasPath(s)).collect(Collectors.toList());

    if (!missingFields.isEmpty()) {
      throw new ConfigBuilderException("The following fields are missing when building PresentationInlineMappingInfo: "
          + String.join(", ", missingFields));
    }

    String mappingName = config.getString(PresentationConfig.NAME);
    Map<String, String> mapping = ConfigUtils.getStringMap(config.getConfig(PresentationConfig.MAPPING));

    logger.trace("Built PresentationInlineMappingInfo object");
    PresentationInlineMappingInfo inlineMappingInfo = new PresentationInlineMappingInfo().setName(mappingName)
        .setMapping(new StringMap(mapping));
    return inlineMappingInfo;
  }
}
