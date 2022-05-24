package com.linkedin.frame.core.config.producer.features;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Represents a feature's metadata which consists of its (versioned) definitions and other metadata fields
 * such as availability. A feature's metadata is scoped under the feature's namespace and name.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class FeatureMetadata implements ConfigObj {
  public static final String DOC = "doc";
  public static final String VERSIONS = "versions";
  public static final String FEATURE_VALUE_TYPE = "valType";
  public static final String AVAILABILITY = "availability";
  public static final String OWNERS = "owners";

  private final Namespace _namespace;
  private final String _featureName;
  private final String _doc;
  private final Map<Version, FeatureDefinition> _featureDefs;
  private final Availability _availability;
  private final List<String> _owners;

  private Set<Pair<Version, FeatureDefinition>> _featureDefsSet;
  private String _str;

  /**
   * Constructor with missing owners information
   * @param namespace Feature namespace
   * @param featureName Feature name
   * @param doc Optional description for this feature
   * @param featureDefinitions Map of feature {@link Version} to a {@link FeatureDefinition}
   * @param availability Availability of this feature
   * @deprecated please use the constructor with owners setup
   */
  @Deprecated
  public FeatureMetadata(Namespace namespace, String featureName, String doc,
      Map<Version, FeatureDefinition> featureDefinitions, Availability availability) {
    _namespace = namespace;
    _featureName = featureName;
    _doc = doc;
    _featureDefs = featureDefinitions;
    _availability = availability;
    _owners = null;
  }

  /**
   * Constructor
   * @param namespace Feature namespace
   * @param featureName Feature name
   * @param doc Optional description for this feature
   * @param featureDefinitions Map of feature {@link Version} to a {@link FeatureDefinition}
   * @param availability Availability of this feature
   * @param owners List of owners of this feature, if not null, then it should contains at least two owners
   *               This is checked in the FeatureMetadataBuilder
   */
  public FeatureMetadata(Namespace namespace, String featureName, String doc,
      Map<Version, FeatureDefinition> featureDefinitions, Availability availability, List<String> owners) {
    _namespace = namespace;
    _featureName = featureName;
    _doc = doc;
    _featureDefs = featureDefinitions;
    _availability = availability;
    _owners = owners;
  }

  public Namespace getNamespace() {
    return _namespace;
  }

  public String getFeatureName() {
    return _featureName;
  }

  public Optional<String> getDoc() {
    return Optional.ofNullable(_doc);
  }

  public Optional<FeatureDefinition> getFeatureDefinition(Version version) {
    return Optional.ofNullable(_featureDefs.get(version));
  }

  /**
   * Returns all feature definitions as a set of pairs of (Version, FeatureDefinition)
   * @return Set of Pair&lt;Version, FeatureDefinition&gt;
   */
  public Set<Pair<Version, FeatureDefinition>> getAllFeatureDefinitions() {
    if (_featureDefsSet == null) {    // The pairs of (version, feature definition) are built lazily
      Set<Pair<Version, FeatureDefinition>> featureDefs = _featureDefs.entrySet()
          .stream()
          .map(e -> Pair.of(e.getKey(), e.getValue()))
          .collect(Collectors.toSet());
      _featureDefsSet = Collections.unmodifiableSet(featureDefs);
    }

    return _featureDefsSet;
  }

  public Availability getAvailability() {
    return _availability;
  }

  public Optional<List<String>> getOwners() {
    return Optional.ofNullable(_owners);
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = new StringJoiner(", ", FeatureMetadata.class.getSimpleName() + "[", "]")
          .add("namespace: " + _namespace)
          .add("featureName: '" + _featureName + "'")
          .add("doc: '" + _doc + "'")
          .add("featureDefinitionMap: " + _featureDefs)
          .add("availability: " + _availability)
          .add("owners: " + _owners)
          .toString();
    }

    return _str;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeatureMetadata that = (FeatureMetadata) o;
    return _namespace.equals(that._namespace) && _featureName.equals(that._featureName)
        && _featureDefs.equals(that._featureDefs) && _availability == that._availability
        && Objects.equals(_owners, that._owners);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_namespace, _featureName, _featureDefs, _availability, _owners);
  }
}
