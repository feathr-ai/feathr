package com.linkedin.frame.core.config.producer.dimensions;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Represents a dimension's metadata which consists of its (versioned) definitions and other metadata fields.
 * A dimension's metadata is scoped under the dimension's namespace and name.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class DimensionMetadata implements ConfigObj {
  /*
   * Field names in the dimension metadata
   */
  public static final String DOC = "doc";
  public static final String VERSIONS = "versions";

  private final Namespace _namespace;
  private final String _name;
  private final String _doc;
  private final Map<Version, DimensionDefinition> _dimDefs;

  private Set<Pair<Version, DimensionDefinition>> _dimDefsSet;
  private String _str;

  /**
   * Constructor
   * @param namespace Dimension's {@link Namespace}
   * @param name Dimension's name
   * @param doc Optional description for this dimension
   * @param dimensionDefinitions Map of dimension version to dimension definition
   */
  public DimensionMetadata(Namespace namespace, String name, String doc,
      Map<Version, DimensionDefinition> dimensionDefinitions) {
    _namespace = namespace;
    _name = name;
    _doc = doc;
    _dimDefs = dimensionDefinitions;
  }

  public Namespace getNamespace() {
    return _namespace;
  }

  public String getName() {
    return _name;
  }

  public Optional<String> getDoc() {
    return Optional.ofNullable(_doc);
  }

  public Optional<DimensionDefinition> getDimensionDefinition(Version version) {
    return Optional.ofNullable(_dimDefs.get(version));
  }

  /**
   * Returns set of all (Version, DimensionDefinition) pairs
   */
  public Set<Pair<Version, DimensionDefinition>> getAllDimensionDefinitions() {
    if (_dimDefsSet == null) {
      Set<Pair<Version, DimensionDefinition>> dimDefs = _dimDefs.entrySet()
          .stream()
          .map(e -> Pair.of(e.getKey(), e.getValue()))
          .collect(Collectors.toSet());
      _dimDefsSet = Collections.unmodifiableSet(dimDefs);
    }

    return _dimDefsSet;
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = new StringJoiner(", ", DimensionMetadata.class.getSimpleName() + "[", "]")
          .add("namespace = " + _namespace)
          .add("name = '" + _name + "'")
          .add("doc = '" + _doc + "'")
          .add("dimDefs = " + _dimDefs)
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
    DimensionMetadata that = (DimensionMetadata) o;
    return _namespace.equals(that._namespace) && _name.equals(that._name) && _dimDefs.equals(that._dimDefs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_namespace, _name, _dimDefs);
  }
}
