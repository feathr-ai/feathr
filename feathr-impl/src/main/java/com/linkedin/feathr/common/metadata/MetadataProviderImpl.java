package com.linkedin.feathr.common.metadata;

import com.linkedin.frame.core.config.producer.FeatureDefConfig;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.AnchorsConfig;
import com.linkedin.frame.core.config.producer.common.DimensionRef;
import com.linkedin.frame.core.config.producer.common.FeatureRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.config.producer.derivations.DerivationsConfig;
import com.linkedin.frame.core.config.producer.dimensions.DimensionDefinition;
import com.linkedin.frame.core.config.producer.dimensions.DimensionSection;
import com.linkedin.frame.core.config.producer.features.FeatureDefinition;
import com.linkedin.frame.core.config.producer.features.FeatureMetadata;
import com.linkedin.frame.core.config.producer.features.FeatureSection;
import com.linkedin.frame.core.config.producer.sources.EspressoConfig;
import com.linkedin.frame.core.config.producer.sources.HdfsConfig;
import com.linkedin.frame.core.config.producer.sources.KafkaConfig;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.frame.core.config.producer.sources.PinotConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.frame.core.config.producer.sources.RocksDbConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import com.linkedin.frame.core.config.producer.sources.SourceType;
import com.linkedin.frame.core.config.producer.sources.SourcesConfig;
import com.linkedin.frame.core.config.producer.sources.VeniceConfig;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;


/*
 * Implements MetadataProvider interface
 */
class MetadataProviderImpl implements MetadataProvider {
  private static final Logger logger = Logger.getLogger(MetadataProviderImpl.class);

  private FeatureDefConfig _config;
  private Map<FeatureRef, AnchorConfig> _featureRefToAnchorMap;
  private List<FeatureRef> _anchoredFeatureRefs;
  private List<FeatureRef> _derivedFeatureRefs;

  private List<FeatureRef> _allFeatureRefs;

  private Map<FeatureRef, FeatureMetadata> _featureMetadataMap;   // cache for FeatureMetadata objects

  private Map<FeatureRef, FeatureDefinition> _featureDefMap;      // cache for FeatureDefinition objects
  private Map<DimensionRef, DimensionDefinition> _dimensionDefMap;

  private Map<String, SourceMetadata> _sourceMetadataMap;         // cache for SourceMetadata objects


  MetadataProviderImpl(FeatureDefConfig config) {
    _config = config;

    /*
     * Build map of DimensionRef -> DimensionDefinition and map of FeatureRef -> FeatureDefinition, in that order
     * since a feature may reference a dimension. If the feature metadata aren't defined in the config files,
     * that is, FeatureSection object is empty, then log a warning that the feature metadata must be defined
     * in the config files if the corresponding features will be represented as tensors. OTOH, dimension
     * section can be empty if the features are all scalars. Else, a runtime exception will be thrown while
     * building the feature definition map.
     */
    _dimensionDefMap = buildDimensionDefinitionMap();
    _featureMetadataMap = buildFeatureMetadataMap();
    _featureDefMap = buildFeatureDefinitionMap();

    if (_featureDefMap.isEmpty()) {
      String warnMsg = "Can't extract feature definition as feature section couldn't be found. If the corresponding "
          + "features will be represented as tensors, their metadata must be defined in the feature section in "
          + "the config files.";
      logger.warn(warnMsg);
    }

    // Build anchored and derived feature ref list
    _featureRefToAnchorMap = buildFeatureRefToAnchorMap();
    _anchoredFeatureRefs = new ArrayList<>(_featureRefToAnchorMap.keySet());
    _derivedFeatureRefs = extractDerivedFeatureRefs();

    /*
     * Build feature refs list for all known features. Three possible scenarios:
     * Case 1: Only FeatureDef config is provided in which case use anchored and derived features ref list
     * Case 2: Only metadata config is provided in which case use feature refs obtained from metadata
     * Case 3: Both FeatureDef and metadata config are provided in which case take the union of the above two
     * cases, taking care to use only unique feature refs. Note that the feature refs from FeatureDef config
     * may not be a super-set of those from Metadata config, as not all features may have metadata whereas some
     * features may have metadata but not have anchor configs, that is, they may not be fetched from Frame.
     * In steady state, we won't have these three scenarios, just one.
     */
    Set<FeatureRef> allFeatureRefsSet = new HashSet<>(_featureMetadataMap.keySet());
    allFeatureRefsSet.addAll(_anchoredFeatureRefs);
    allFeatureRefsSet.addAll(_derivedFeatureRefs);

    _allFeatureRefs = new ArrayList<>(allFeatureRefsSet);

    _sourceMetadataMap = new HashMap<>();
  }

  @Override
  public List<FeatureRef> getAllFeatureRefs() {
    return _allFeatureRefs;
  }

  @Override
  public List<FeatureRef> getAnchoredFeatureRefs() {
    return _anchoredFeatureRefs;
  }

  @Override
  public List<FeatureRef> getDerivedFeatureRefs() {
    return _derivedFeatureRefs;
  }

  @Override
  public Optional<FeatureMetadata> getFeatureMetadata(FeatureRef featureRef) {
    return Optional.ofNullable(_featureMetadataMap.get(featureRef));
  }

  @Override
  public Map<FeatureRef, FeatureMetadata> getAllFeatureMetadata() {
    return _featureMetadataMap;
  }

  @Override
  public Optional<FeatureDefinition> getFeatureDefinition(FeatureRef featureRef) {
    return Optional.ofNullable(_featureDefMap.get(featureRef));
  }

  @Override
  public Map<FeatureRef, FeatureDefinition> getAllFeatureDefinitions() {
    return _featureDefMap;
  }

  @Override
  public Optional<DimensionDefinition> getDimensionDefinition(DimensionRef dimensionRef) {
    return Optional.ofNullable(_dimensionDefMap.get(dimensionRef));
  }

  @Override
  public Optional<SourceMetadata> getSourceMetadata(String sourceName) {
    Optional<SourceMetadata> md;

    if (_sourceMetadataMap.containsKey(sourceName)) {
      md = Optional.of(_sourceMetadataMap.get(sourceName));
    } else {
      md = buildFromSourceName(sourceName);
      if (!md.isPresent()) {
        md = buildFromSourcesConfig(sourceName);
      }
      md.ifPresent(x -> _sourceMetadataMap.put(sourceName, x));     // cache for future get's
    }

    return md;
  }

  /*
   * Builds SourceMetadata if the source name is actually a HDFS path or Dali URI.
   */
  private Optional<SourceMetadata> buildFromSourceName(String sourceName) {
    Optional<SourceMetadata> md = Optional.empty();

    if (sourceName.startsWith("hdfs://") || sourceName.startsWith("/") || sourceName.startsWith("dalids://")) {
      md = Optional.of(new HdfsMetadata(sourceName));
    }

    return md;
  }

  /*
   * Builds SourceMetadata from the SourcesConfig object, and hence from the SourceConfig object
   * corresponding to the specified source name. Returns Optional<SourceMetadata> on success else
   * Optional.empty().
   */
  private Optional<SourceMetadata> buildFromSourcesConfig(String sourceName) {
    SourceMetadata md = null;

    Optional<SourcesConfig> sourcesConfigOpt = _config.getSourcesConfig();
    if (sourcesConfigOpt.isPresent()) {
      Map<String, SourceConfig> sources = sourcesConfigOpt.get().getSources();
      SourceConfig srcConfig = sources.get(sourceName);
      if (srcConfig != null) {
        SourceType srcType = srcConfig.getSourceType();
        switch (srcType) {
          case HDFS:
            HdfsConfig hdfsConfig = (HdfsConfig) srcConfig;
            md = new HdfsMetadata(hdfsConfig);
            break;

          case ESPRESSO:
            EspressoConfig espressoConfig = (EspressoConfig) srcConfig;
            md = new EspressoMetadata(espressoConfig);
            break;

          case VENICE:
            VeniceConfig veniceConfig = (VeniceConfig) srcConfig;
            md = new VeniceMetadata(veniceConfig);
            break;

          case RESTLI:
            RestliConfig restliConfig = (RestliConfig) srcConfig;
            md = new RestliMetadata(restliConfig);
            break;

          case KAFKA:
            KafkaConfig kafkaConfig = (KafkaConfig) srcConfig;
            md = new KafkaMetadata(kafkaConfig);
            break;

          case ROCKSDB:
            RocksDbConfig rocksDbConfig = (RocksDbConfig) srcConfig;
            md = new RocksDbMetadata(rocksDbConfig);
            break;

          case PASSTHROUGH:
            PassThroughConfig passThroughConfig = (PassThroughConfig) srcConfig;
            md = new PassThroughMetadata(passThroughConfig);
            break;

          case PINOT:
            PinotConfig pinotConfig = (PinotConfig) srcConfig;
            md = new PinotMetadata(pinotConfig);
            break;
          default:
            throw new RuntimeException("Unsupported source type " + srcType);
        }
      }
    }

    return Optional.ofNullable(md);
  }

  /*
   * Iterates over the list of AnchorConfig objects and builds a map of anchored feature name to the AnchorConfig
   * in which it's specified.
   */
  private Map<FeatureRef, AnchorConfig> buildFeatureRefToAnchorMap() {
    Map<FeatureRef, AnchorConfig> featureRefToAnchorMap = new HashMap<>();

    Optional<AnchorsConfig> anchorsConfigOpt = _config.getAnchorsConfig();
    if (anchorsConfigOpt.isPresent()) {
      Collection<AnchorConfig> anchorConfigs = anchorsConfigOpt.get().getAnchors().values();

      for (AnchorConfig aConfig : anchorConfigs) {
        Set<String> featureRefStrs = aConfig.getFeatures().keySet();
        for (String refStr : featureRefStrs) {
          featureRefToAnchorMap.put(new FeatureRef(refStr), aConfig);
        }
      }
    }

    return featureRefToAnchorMap;
  }

  private List<FeatureRef> extractDerivedFeatureRefs() {
    List<FeatureRef> derivedFeatureRefs = new ArrayList<>();

    Optional<DerivationsConfig> derivationsConfigOpt = _config.getDerivationsConfig();
    if (derivationsConfigOpt.isPresent()) {
      Set<String> refStrs = derivationsConfigOpt.get().getDerivations().keySet();
      Set<FeatureRef> refs = refStrs.stream()
          .map(FeatureRef::new)
          .collect(Collectors.toSet());
      derivedFeatureRefs.addAll(refs);
    }

    return derivedFeatureRefs;
  }

  private Map<FeatureRef, FeatureMetadata> buildFeatureMetadataMap() {
    Map<FeatureRef, FeatureMetadata> featureMetadataMap = new HashMap<>();

    Optional<FeatureSection> featureSectionOpt = _config.getFeatureSection();
    if (featureSectionOpt.isPresent()) {
      FeatureSection featureSection = featureSectionOpt.get();

      // Iterate over each namespace
      for (Pair<Namespace, Set<FeatureMetadata>> pair : featureSection) {
        Namespace namespace = pair.getLeft();
        Set<FeatureMetadata> featureMetadataSet = pair.getRight();

        // Iterate over each feature metadata
        for (FeatureMetadata featureMetadata : featureMetadataSet) {
          String name = featureMetadata.getFeatureName();

          // Iterate over each (versioned) feature definition
          Set<Pair<Version, FeatureDefinition>> featureDefSet = featureMetadata.getAllFeatureDefinitions();
          for (Pair<Version, FeatureDefinition> pair2 : featureDefSet) {
            Version version = pair2.getLeft();

            FeatureRef featureRef = new FeatureRef(namespace, name, version);

            featureMetadataMap.put(featureRef, featureMetadata);
          }
        }
      }
    }

    return Collections.unmodifiableMap(featureMetadataMap);
  }

  /*
   * Build a map of FeatureRef -> FeatureDefinition so that queries can be satisfied quickly.
   */
  private Map<FeatureRef, FeatureDefinition> buildFeatureDefinitionMap() {
    Map<FeatureRef, FeatureDefinition> featureDefMap = new HashMap<>();

    for (Map.Entry<FeatureRef, FeatureMetadata> entry : _featureMetadataMap.entrySet()) {
      FeatureRef featureRef = entry.getKey();
      Version version = featureRef.getVersion().get();
      FeatureMetadata featureMetadata = entry.getValue();

      // Iterate over each (versioned) feature definition
      Set<Pair<Version, FeatureDefinition>> featureDefSet = featureMetadata.getAllFeatureDefinitions();
      for (Pair<Version, FeatureDefinition> pair : featureDefSet) {
        // Only insert FeatureDefinition if the version matches the one in the feature ref
        if (pair.getKey().equals(version)) {
          FeatureDefinition featureDef = pair.getRight();

          featureDefMap.put(featureRef, featureDef);
        }
      }
    }

    return Collections.unmodifiableMap(featureDefMap);
  }

  /*
   * Build a map of DimensionRef -> DimensionDefinition so that queries can be satisfied quickly. It's assumed
   * that the dimension section exists.
   */
  private Map<DimensionRef, DimensionDefinition> buildDimensionDefinitionMap() {
    Map<DimensionRef, DimensionDefinition> dimDefsMap = new HashMap<>();

    Optional<DimensionSection> dimSectionOpt = _config.getDimensionSection();
    if (dimSectionOpt.isPresent()) {
      DimensionSection dimSection = dimSectionOpt.get();

      // Iterate over each namespace
      for (Pair<Namespace, Set<com.linkedin.frame.core.config.producer.dimensions.DimensionMetadata>> pair : dimSection) {
        Namespace namespace = pair.getLeft();
        Set<com.linkedin.frame.core.config.producer.dimensions.DimensionMetadata> dimMetadataSet = pair.getRight();

        // Iterate over each dimension metadata
        for (com.linkedin.frame.core.config.producer.dimensions.DimensionMetadata dimMetadata : dimMetadataSet) {
          String name = dimMetadata.getName();

          // Iterate over each (versioned) dimension definition
          Set<Pair<Version, DimensionDefinition>> dimDefSet = dimMetadata.getAllDimensionDefinitions();
          for (Pair<Version, DimensionDefinition> pair2 : dimDefSet) {
            Version version = pair2.getLeft();
            DimensionDefinition dimDef = pair2.getRight();

            DimensionRef dimRef = new DimensionRef(namespace, name, version);

            dimDefsMap.put(dimRef, dimDef);
          }
        }
      }
    }

    return Collections.unmodifiableMap(dimDefsMap);
  }
}
