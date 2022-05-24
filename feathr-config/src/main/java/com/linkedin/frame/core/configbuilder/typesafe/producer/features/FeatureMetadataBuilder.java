package com.linkedin.frame.core.configbuilder.typesafe.producer.features;

import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.TypedRef;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.config.producer.features.Availability;
import com.linkedin.frame.core.config.producer.features.FeatureDefinition;
import com.linkedin.frame.core.config.producer.features.FeatureMetadata;
import com.linkedin.frame.core.config.producer.features.ValueType;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

import static com.linkedin.frame.core.config.producer.features.FeatureMetadata.*;


/**
 * Builds a {@link FeatureMetadata} object
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class FeatureMetadataBuilder {
  private static final Logger logger = Logger.getLogger(FeatureMetadataBuilder.class);

  private FeatureMetadataBuilder() {
  }

  /*
   * Config represents the object part in:
   * <featureName>: {
   *   doc: "..."
   *   versions: {
   *     <major.minor version>: {
   *       doc: "..."
   *       dims: [ // dimension refs ]
   *     }
   *     // other versions, if specified
   *   }
   *   valType: ...
   *   availability: ...
   *   owners: [ // feature owners, at least two ]
   *   // In future, we may have other metadata fields
   * }
   */
  public static FeatureMetadata build(Namespace namespace, String featureName, Config featureMetadataConfig) {
    String doc = featureMetadataConfig.hasPath(DOC) ? featureMetadataConfig.getString(DOC) : null;

    String valueTypeString = featureMetadataConfig.getString(FEATURE_VALUE_TYPE);
    Optional<ValueType> valueTypeOpt = ValueType.fromName(valueTypeString);
    if (!valueTypeOpt.isPresent()) {
      throw new ConfigBuilderException("Unknown feature value type " + valueTypeString + " for feature "
          + namespace + "/" + featureName);
    }
    ValueType valueType = valueTypeOpt.get();

    Config verFeatureDefsConfig = featureMetadataConfig.getConfig(VERSIONS);
    Map<Version, FeatureDefinition> verFeatureDefs = buildVerFeatureDefs(namespace, featureName,
        valueType, verFeatureDefsConfig);

    String availString = featureMetadataConfig.getString(AVAILABILITY);
    Optional<Availability> availOpt = Availability.fromName(availString);
    if (!availOpt.isPresent()) {
      throw new ConfigBuilderException("Unknown availability value " + availString + " for feature "
          + namespace + TypedRef.DELIM + featureName);
    }
    Availability availability = availOpt.get();

    List<String> ownerStringsList =
        featureMetadataConfig.hasPath(OWNERS) ? featureMetadataConfig.getStringList(OWNERS) : null;

    // owners field is optional, but if specified, there should at least be two owners
    if (ownerStringsList != null && ownerStringsList.size() < 2) {
      String errMsg = String.join("", "The number of owners for feature ",
          namespace + TypedRef.DELIM + featureName, " is less than two: ",
          ownerStringsList.stream().collect(Collectors.joining(",", "[", "]")));
      throw new ConfigBuilderException(errMsg);
    }

    FeatureMetadata featureMetadata =
        new FeatureMetadata(namespace, featureName, doc, verFeatureDefs, availability, ownerStringsList);
    logger.debug("Built metadata for feature " + namespace + TypedRef.DELIM + featureName);

    return featureMetadata;
  }

  /*
   * Config represents the object part in:
   * versions: {
   *   <major.minor version>: {
   *     doc: "..."
   *     dims: [ // dimension refs ]
   *   }
   *   // other versions, if specified
   * }
   */
  private static Map<Version, FeatureDefinition> buildVerFeatureDefs(Namespace namespace,
      String featureName, ValueType valueType, Config verFeatureDefsCfg) {
    Map<Version, FeatureDefinition> verFeatureDefs = new HashMap<>();

    ConfigObject verFeatureDefsCfgObj = verFeatureDefsCfg.root();

    for (Map.Entry<String, ConfigValue> entry : verFeatureDefsCfgObj.entrySet()) {
      String versionString = entry.getKey();
      Version version = new Version(versionString);

      ConfigObject featureDefCfgObj = (ConfigObject) entry.getValue();
      Config featureDefCfg = featureDefCfgObj.toConfig();

      FeatureDefinition featureDef = FeatureDefinitionBuilder.build(namespace, featureName, version, valueType,
          featureDefCfg);

      verFeatureDefs.put(version, featureDef);
    }

    return verFeatureDefs;
  }
}
