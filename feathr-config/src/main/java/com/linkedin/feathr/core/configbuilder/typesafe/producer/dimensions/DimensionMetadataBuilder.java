package com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.feathr.core.config.producer.common.Namespace;
import com.linkedin.feathr.core.config.producer.common.TypedRef;
import com.linkedin.feathr.core.config.producer.common.Version;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionDefinition;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionMetadata;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import static com.linkedin.feathr.core.config.producer.dimensions.DimensionMetadata.*;


/**
 * Builder for {@link DimensionMetadata} objects
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class DimensionMetadataBuilder {
  private static final Logger logger = Logger.getLogger(DimensionMetadataBuilder.class);

  private DimensionMetadataBuilder() {
  }

  /*
   * Config represents the object part in:
   * <dimName>: {
   *   doc: "..."
   *   versions: {
   *     <major.minor version>: {
   *       doc: "..."
   *       type: ...
   *     }
   *     // other versions, if specified
   *   }
   *   // In future, we may have other metadata fields
   * }
   */
  public static DimensionMetadata build(Namespace namespace, String dimName, Config dimMetadataConfig) {
    String doc = dimMetadataConfig.hasPath(DOC) ? dimMetadataConfig.getString(DOC) : null;

    Config verDimDefsConfig = dimMetadataConfig.getConfig(VERSIONS);
    Map<Version, DimensionDefinition> verDimDefs = buildVerDimDefs(namespace, dimName, verDimDefsConfig);

    DimensionMetadata dimMetadata = new DimensionMetadata(namespace, dimName, doc, verDimDefs);
    logger.debug("Built metadata for dimension " + namespace + TypedRef.DELIM + dimName);

    return dimMetadata;
  }

  /*
   * Config represents the object part in:
   * versions: {
   *   <major.minor version>: {
   *     doc: "..."
   *     type: ...
   *   }
   *   // other versions, if specified
   * }
   */
  private static Map<Version, DimensionDefinition> buildVerDimDefs(Namespace namespace,
      String dimName, Config verDimDefsCfg) {
    Map<Version, DimensionDefinition> verDimDefs = new HashMap<>();

    ConfigObject verDimDefsCfgObj = verDimDefsCfg.root();

    for (Map.Entry<String, ConfigValue> entry : verDimDefsCfgObj.entrySet()) {
      String versionString = entry.getKey();
      Version version = new Version(versionString);

      ConfigObject dimDefCfgObj = (ConfigObject) entry.getValue();
      Config dimDefCfg = dimDefCfgObj.toConfig();

      DimensionDefinition dimDef = DimensionDefinitionBuilder.build(namespace, dimName, version, dimDefCfg);

      verDimDefs.put(version, dimDef);
    }

    return verDimDefs;
  }

}
