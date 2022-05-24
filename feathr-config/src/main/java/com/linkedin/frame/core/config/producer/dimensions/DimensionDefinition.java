package com.linkedin.frame.core.config.producer.dimensions;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;


/**
 * Represents the definition of a dimension. It includes the dimension's namespace, name, version, and dimension type.
 * And an optional doc string.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class DimensionDefinition implements ConfigObj {
  /*
   * Field names in the dimension definition
   */
  public static final String DOC = "doc";
  public static final String DIMENSION_TYPE = "type";

  private final Namespace _namespace;
  private final String _name;
  private final String _doc;
  private final Version _version;
  private final DimensionType _dimensionType;

  private String _str;

  /**
   * Constructor
   * @param namespace
   * @param name
   * @param version
   * @param doc
   * @param dimensionType
   */
  public DimensionDefinition(Namespace namespace, String name, Version version, String doc,
      DimensionType dimensionType) {
    _namespace = namespace;
    _name = name;
    _version = version;
    _doc = doc;
    _dimensionType = dimensionType;
  }

  /**
   * Gets the namespace of the dimension
   * @return {@link Namespace}
   */
  public Namespace getNamespace() {
    return _namespace;
  }

  /**
   * Gets the name of the dimension
   * @return String
   */
  public String getName() {
    return _name;
  }

  /**
   * Gets the {@link Version} of the dimension
   * @return Version
   */
  public Version getVersion() {
    return _version;
  }

  /**
   * Gets the description of the dimension
   * @return Optional<String>
   */
  public Optional<String> getDoc() {
    return Optional.ofNullable(_doc);
  }

  /**
   * Gets the dimension type
   * @return {@link DimensionType}
   */
  public DimensionType getDimensionType() {
    return _dimensionType;
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = new StringJoiner(", ", DimensionDefinition.class.getSimpleName() + "[", "]")
          .add("namespace = " + _namespace)
          .add("name = '" + _name + "'")
          .add("doc = '" + _doc + "'")
          .add("version = " + _version)
          .add("dimensionType = " + _dimensionType)
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
    DimensionDefinition that = (DimensionDefinition) o;
    return Objects.equals(_namespace, that._namespace) && Objects.equals(_name, that._name)
        && Objects.equals(_version, that._version) && Objects.equals(_dimensionType, that._dimensionType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_namespace, _name, _version, _dimensionType);
  }
}
