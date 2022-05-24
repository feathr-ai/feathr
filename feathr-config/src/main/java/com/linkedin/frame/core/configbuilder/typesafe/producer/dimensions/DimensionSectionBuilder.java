package com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.dimensions.DimensionMetadata;
import com.linkedin.frame.core.config.producer.dimensions.DimensionSection;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

import static com.linkedin.frame.core.utils.Utils.*;
import static java.util.stream.Collectors.*;


/**
 * Builds the dimension objects in the dimension section.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class DimensionSectionBuilder {
  private static final Logger logger = Logger.getLogger(DimensionSectionBuilder.class);

  private DimensionSectionBuilder() {
  }

  /*
   * Config represents the object part in:
   * dimensions: {...}
   */
  public static DimensionSection build(Config config) {
    // Get the set of all namespaces
    ConfigObject configObj = config.root();

    Set<Namespace> namespaces = configObj.keySet().stream().map(Namespace::new).collect(toSet());

    /*
     * Build Map of Namespace -> Set<DimensionMetadata> by iterating over all namespaces, building a set of
     * dimension metadata in each iteration.
     */

    Map<Namespace, Set<DimensionMetadata>> dimMetadataSetMap = new HashMap<>();

    for (Namespace ns : namespaces) {
      // First, getOrCompute the value part of <namespace> : { ... }
      Config nsCfg = config.getConfig(quote(ns.get()));

      // Next, get all dimension names in this namespace
      ConfigObject nsCfgObject = nsCfg.root();
      Set<String> dimNamesSet = nsCfgObject.keySet();

      // Finally, iterate over all dimension names and build the set of dimension metadata
      Set<DimensionMetadata> dimMetadataSet = new HashSet<>();
      for (String dimName : dimNamesSet) {
        // Get the value part of <dimName> : { ... }
        Config dimMetadataCfg = nsCfg.getConfig(quote(dimName));

        // Delegate to the child builder to build this dimension's metadata
        DimensionMetadata dimMetadata = DimensionMetadataBuilder.build(ns, dimName, dimMetadataCfg);
        dimMetadataSet.add(dimMetadata);
      }

      // Insert the set of dimension metadata into the map
      dimMetadataSetMap.put(ns, dimMetadataSet);
    }

    DimensionSection dimensionSection = new DimensionSection(dimMetadataSetMap);
    logger.debug("Built all dimension metadata objects");

    return dimensionSection;
  }
}
