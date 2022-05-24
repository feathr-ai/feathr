package com.linkedin.frame.core.configbuilder.typesafe.producer.features;

import com.linkedin.frame.core.config.producer.common.DimensionRef;
import com.linkedin.frame.core.config.producer.common.FeatureRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.config.producer.features.FeatureDefinition;
import com.linkedin.frame.core.config.producer.features.ValueType;
import com.typesafe.config.Config;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

import static com.linkedin.frame.core.config.producer.features.FeatureDefinition.*;


/**
 * Builds a {@link FeatureDefinition} object
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
class FeatureDefinitionBuilder {
  private static final Logger logger = Logger.getLogger(FeatureDefinitionBuilder.class);

  private FeatureDefinitionBuilder() {
  }

  /*
   * Config represents the object part in:
   * <version>: {
   *   doc: "..."
   *   dims: [ // dimension refs]
   * }
   */
  static FeatureDefinition build(Namespace namespace, String featureName, Version version, ValueType valueType,
      Config featureDefConfig) {
    String doc = featureDefConfig.hasPath(DOC) ? featureDefConfig.getString(DOC) : null;

    List<String> dimRefStringsList = featureDefConfig.getStringList(DIMS);
    List<DimensionRef> dimensionRefs = getDimensionRefs(namespace, dimRefStringsList);

    FeatureDefinition featureDef = new FeatureDefinition(namespace, featureName, version, doc, dimensionRefs, valueType);

    if (logger.isTraceEnabled()) {
      FeatureRef featureRef = new FeatureRef(namespace, featureName, version);
      logger.trace("Built feature definition for feature " + featureRef);
    }

    return featureDef;
  }

  private static List<DimensionRef> getDimensionRefs(Namespace featureNamespace, List<String> dimRefStringsList) {
    List<DimensionRef> dimRefsList = new ArrayList<>();

    for (String dimRefString : dimRefStringsList) {
      DimensionRef dimRef = new DimensionRef(featureNamespace, dimRefString);
      dimRefsList.add(dimRef);
    }

    return dimRefsList;
  }
}
