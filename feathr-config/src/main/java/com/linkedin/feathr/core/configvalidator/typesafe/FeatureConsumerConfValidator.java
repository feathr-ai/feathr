package com.linkedin.feathr.core.configvalidator.typesafe;

import com.linkedin.feathr.core.config.ConfigType;
import com.linkedin.feathr.core.config.consumer.JoinConfig;
import com.linkedin.feathr.core.config.producer.FeatureDefConfig;
import com.linkedin.feathr.core.configbuilder.ConfigBuilder;
import com.linkedin.feathr.core.configdataprovider.ConfigDataProvider;
import com.linkedin.feathr.core.configvalidator.ConfigValidationException;
import com.linkedin.feathr.core.configvalidator.ValidationResult;
import com.linkedin.feathr.core.configvalidator.ValidationType;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * Validator specific for Frame feature consumer clients.
 *
 * The validator provides syntax and semantic validation for Frame configs in the Frame feature consumer clients.
 * For instance, it checks the syntax restrictions from Frame libraries. Some examples of semantic validation will
 * be checking if requested features are reachable (feature is said reachable if the feature is defined in anchors
 * section in FeatureDef config, or if it is a derived feature, then the depended features are reachable),
 * and checking if the source used in feature definition is defined.
 *
 */
public class FeatureConsumerConfValidator extends TypesafeConfigValidator {

  /**
   * validate configs for Frame feature consumer
   *
   * @see ConfigValidator#validate(Map, ValidationType)
   */
  @Override
  public Map<ConfigType, ValidationResult> validate(Map<ConfigType, ConfigDataProvider> configTypeWithDataProvider,
      ValidationType validationType) {

    switch (validationType) {
      case SYNTACTIC:
        // reuse default implementation in super class to perform syntax validation
        return super.validate(configTypeWithDataProvider, ValidationType.SYNTACTIC);
      case SEMANTIC:
        return validateSemantics(configTypeWithDataProvider);
      default:
        throw new ConfigValidationException("Unsupported validation type: " + validationType.name());
    }
  }

  /**
   * Perform semantic validations for provided configs:
   * 1. if no FeatureDef config provided, then return empty result, as all semantic validation requires at least
   *    FeatureDef config provided
   * 2. if only FeatureDef config provided, then perform semantic validation for FeatureDef config
   * 3. if Join config provided, then perform semantic validation for Join config, together with the information provided
   *    in FeatureDef config. For instance, check if features requested in Join config are reachable features in
   *    FeatureDef config
   * 4. if FeatureGeneration config provided, then perform semantic validation for FeatureGeneration config, together
   *    with the information provided in FeatureDef config
   */
  private Map<ConfigType, ValidationResult> validateSemantics(Map<ConfigType, ConfigDataProvider> configTypeWithDataProvider) {
    Map<ConfigType, ValidationResult> result = new HashMap<>();

    // edge cases when the input is not valid or is empty
    if (configTypeWithDataProvider == null || configTypeWithDataProvider.isEmpty()) {
      return result;
    }
    ConfigBuilder configBuilder = ConfigBuilder.get();

    Optional<FeatureDefConfig> optionalFeatureDefConfig;
    if (configTypeWithDataProvider.containsKey(ConfigType.FeatureDef)) {
      ConfigDataProvider featureDefConfigDataProvider = configTypeWithDataProvider.get(ConfigType.FeatureDef);
      optionalFeatureDefConfig = Optional.of(configBuilder.buildFeatureDefConfig(featureDefConfigDataProvider));
    } else {
      optionalFeatureDefConfig = Optional.empty();
    }

    if (configTypeWithDataProvider.containsKey(ConfigType.Join)) {
      ConfigDataProvider joinConfigDataProvider = configTypeWithDataProvider.get(ConfigType.Join);
      JoinConfig joinConfig = configBuilder.buildJoinConfig(joinConfigDataProvider);
      String errMsg = String.join("", "Can not perform semantic validation as the Join config is",
          "provided but the FeatureDef config is missing.");
      FeatureDefConfig featureDefConfig = optionalFeatureDefConfig.orElseThrow(() -> new ConfigValidationException(errMsg));
      return validateConsumerConfigSemantics(joinConfig, featureDefConfig);
    }

    // TODO add feature generation config semantic validation support

    // only perform semantic check for FeatureDef config
    FeatureDefConfig featureDefConfig = optionalFeatureDefConfig.orElseThrow(() ->
        new ConfigValidationException("Can not perform semantic validation as the FeatureDef config is missing."));
    return Collections.singletonMap(ConfigType.FeatureDef, validateSemantics(featureDefConfig));
  }

  /**
   * Validates feature consumer configs semantically. Requires both {@link JoinConfig} and {@link FeatureDefConfig} to be passed in.
   * @param joinConfig {@link JoinConfig}
   * @param featureDefConfig {@link FeatureDefConfig}
   * @return Map of ConfigType and the {@link ValidationResult}
   */
  private Map<ConfigType, ValidationResult> validateConsumerConfigSemantics(JoinConfig joinConfig, FeatureDefConfig featureDefConfig) {
    Map<ConfigType, ValidationResult> validationResultMap = new HashMap<>();
    FeatureDefConfigSemanticValidator featureDefConfSemanticValidator = new FeatureDefConfigSemanticValidator(true, true);
    validationResultMap.put(ConfigType.FeatureDef, featureDefConfSemanticValidator.validate(featureDefConfig));

    JoinConfSemanticValidator joinConfSemanticValidator = new JoinConfSemanticValidator();
    validationResultMap.put(ConfigType.Join, joinConfSemanticValidator.validate(joinConfig,
        featureDefConfSemanticValidator.getFeatureAccessInfo(featureDefConfig)));
    return validationResultMap;
  }

  /**
   * Validates FeatureDef config semantically
   * @param featureDefConfig {@link FeatureDefConfig}
   * @return {@link ValidationResult}
   */
  @Override
  public ValidationResult validateSemantics(FeatureDefConfig featureDefConfig) {
    return new FeatureDefConfigSemanticValidator(true, true).validate(featureDefConfig);
  }
}
