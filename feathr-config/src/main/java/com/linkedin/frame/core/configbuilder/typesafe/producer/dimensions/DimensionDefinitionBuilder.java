package com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.frame.core.config.producer.common.DimensionRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.config.producer.dimensions.DimensionDefinition;
import com.linkedin.frame.core.config.producer.dimensions.DimensionType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import org.apache.log4j.Logger;

import static com.linkedin.frame.core.config.producer.dimensions.DimensionDefinition.*;


/**
 * Builds the {@link DimensionDefinition} object
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class DimensionDefinitionBuilder {
  private static final Logger logger = Logger.getLogger(DimensionDefinitionBuilder.class);

  private DimensionDefinitionBuilder() {
  }

  /*
   * Config object represents the object part in:
   * <version>: {
   *   doc: "..."
   *   type: ...
   * }
   */
  static DimensionDefinition build(Namespace namespace, String name, Version version, Config dimDefConfig) {
    String doc = dimDefConfig.hasPath(DOC) ? dimDefConfig.getString(DOC) : null;

    ConfigValue dimTypeValue = dimDefConfig.getValue(DIMENSION_TYPE);
    DimensionType type = DimensionTypeBuilder.build(namespace, name, dimTypeValue);

    DimensionDefinition dimensionDefinition = new DimensionDefinition(namespace, name, version, doc, type);

    if (logger.isTraceEnabled()) {
      DimensionRef dimRef = new DimensionRef(namespace, name, version);
      logger.trace("Built dimension definition for dimension " + dimRef);
    }

    return dimensionDefinition;
  }
}
