package com.linkedin.frame.core.configbuilder.typesafe.producer.features;

import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.features.FeatureMetadata;
import com.linkedin.frame.core.config.producer.features.FeatureSection;
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
 * Builds a {@link FeatureSection} object
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class FeatureSectionBuilder {
  private static final Logger logger = Logger.getLogger(FeatureSectionBuilder.class);

  private FeatureSectionBuilder() {
  }

  /*
   * Config represents the object part in:
   * features: {...}
   */
  public static FeatureSection build(Config featuresSectionConfig) {
    // Get the set of all namespaces
    ConfigObject configObj = featuresSectionConfig.root();

    Set<Namespace> namespaces = configObj.keySet().stream().map(Namespace::new).collect(toSet());

    /*
     * Build Map of Namespace -> Set<FeatureMetadata> by iterating over all namespaces, building a set of
     * feature metadata in each iteration.
     */

    Map<Namespace, Set<FeatureMetadata>> featureMetadataSetMap = new HashMap<>();

    for (Namespace ns : namespaces) {
      // First, get the value part of <namespace> : { ... }
      Config nsCfg = featuresSectionConfig.getConfig(quote(ns.get()));

      // Next, get all feature names in this namespace
      ConfigObject nsCfgObject = nsCfg.root();
      Set<String> featureNamesSet = nsCfgObject.keySet();

      // Finally, iterate over all feature names and build the set of feature metadata
      Set<FeatureMetadata> featureMetadataSet = new HashSet<>();
      for (String featureName : featureNamesSet) {
        // Get the value part of <featureName> : { ... }
        Config featureMetadataCfg = nsCfg.getConfig(quote(featureName));

        // Delegate to the child builder to build this feature's metadata
        FeatureMetadata featureMetadata = FeatureMetadataBuilder.build(ns, featureName, featureMetadataCfg);
        featureMetadataSet.add(featureMetadata);
      }

      // Insert the set of feature metadata into the map
      featureMetadataSetMap.put(ns, featureMetadataSet);
    }

    FeatureSection featureSection = new FeatureSection(featureMetadataSetMap);
    logger.debug("Built all feature metadata objects");

    return featureSection;
  }
}
