package com.linkedin.frame.core.config.producer.features;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.core.config.producer.common.DimensionRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;


/**
 * Represents Feature definition. It includes the feature's namespace, name, version,
 * dimension (references), and value type.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class FeatureDefinition implements ConfigObj {
  public static final String DOC = "doc";
  public static final String DIMS = "dims";

  private final Namespace _namespace;
  private final String _name;
  private final Version _version;
  private final String _doc;
  private final List<DimensionRef> _dimensionRefs;
  private final ValueType _valueType;
  private String _str;

  /**
   * Constructs feature definition by specifying all the constituent fields.
   * @param namespace feature {@link Namespace}
   * @param name feature name
   * @param version feature {@link Version}
   * @param doc A short description of the feature, optional
   * @param dimensionRefs List of {@link DimensionRef} for this feature
   * @param valueType feature {@link ValueType}
   */
  public FeatureDefinition(Namespace namespace, String name, Version version, String doc,
      List<DimensionRef> dimensionRefs, ValueType valueType) {
    _namespace = namespace;
    _name = name;
    _version = version;
    _doc = doc;
    _dimensionRefs = dimensionRefs;
    _valueType = valueType;
  }

  public Namespace getNamespace() {
    return _namespace;
  }

  public String getName() {
    return _name;
  }

  public Version getVersion() {
    return _version;
  }

  public Optional<String> getDoc() {
    return Optional.ofNullable(_doc);
  }

  public List<DimensionRef> getDimensionRefs() {
    return _dimensionRefs;
  }

  public ValueType getValueType() {
    return _valueType;
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = new StringJoiner(", ", FeatureDefinition.class.getSimpleName() + "[", "]")
          .add("namespace: " + _namespace)
          .add("name: '" + _name + "'")
          .add("version: " + _version)
          .add("doc: '" + _doc + "'")
          .add("dimensionRefs: " + _dimensionRefs)
          .add("valueType: " + _valueType)
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
    FeatureDefinition that = (FeatureDefinition) o;
    return Objects.equals(_namespace, that._namespace) && Objects.equals(_name, that._name)
        && Objects.equals(_version, that._version) && Objects.equals(_dimensionRefs, that._dimensionRefs)
        && _valueType == that._valueType;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_namespace, _name, _version, _dimensionRefs, _valueType);
  }
}
