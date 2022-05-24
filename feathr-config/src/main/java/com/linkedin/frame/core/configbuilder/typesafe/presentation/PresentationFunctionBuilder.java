package com.linkedin.frame.core.configbuilder.typesafe.presentation;

import com.linkedin.data.template.StringMap;
import com.linkedin.frame.core.config.presentation.PresentationConfig;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.linkedin.frame.core.utils.ConfigUtils;
import com.linkedin.frame.common.PresentationFunction;
import com.linkedin.frame.common.PresentationFunctionType;
import com.typesafe.config.Config;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 * PresentationFunction Builder for Frame presentation config
 * Examples:
 * <pre>
 * {@code
 * {
 *     functionType: "MockPresentationFunctionTypeEnum"
 *     params: {
 *         parameter1: "value1"
 *         parameter2: "value2"
 *     }
 * },
 * {
 *     functionType: "BOOLEAN"
 * }
 * }
 * </pre>
 *
 */
public class PresentationFunctionBuilder {

  private final static Logger logger = Logger.getLogger(PresentationFunctionBuilder.class);
  private static final PresentationFunctionBuilder INSTANCE = new PresentationFunctionBuilder();
  static PresentationFunctionBuilder getInstance() {
    return INSTANCE;
  }

  private PresentationFunctionBuilder() {

  }

  PresentationFunction build(Config config) {
    if (!config.hasPath(PresentationConfig.FUNCTION_TYPE)) {
      throw new ConfigBuilderException("Failed to build PresentationFunction as the "
          + PresentationConfig.FUNCTION_TYPE + " field is missing in the " + PresentationConfig.PRESENTATION_CONFIG_NAME);
    }
    String functionType = config.getString(PresentationConfig.FUNCTION_TYPE);

    PresentationFunctionType type = PresentationFunctionType.valueOf(functionType);
    logger.trace("Built PresentationFunction object");
    PresentationFunction presentationFunction = new PresentationFunction().setFuntionType(type);
    if (config.hasPath(PresentationConfig.PARAMS)) {
      Map<String, String> params = ConfigUtils.getStringMap(config.getConfig(PresentationConfig.PARAMS));
      presentationFunction.setParams(new StringMap(params));
    }
    return presentationFunction;
  }
}
