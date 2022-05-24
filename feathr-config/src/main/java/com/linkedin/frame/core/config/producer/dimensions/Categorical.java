package com.linkedin.frame.core.config.producer.dimensions;

import com.linkedin.frame.core.config.producer.common.ResourceRef;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;


/**
 * Represents Categorical dimension type.
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class Categorical extends DimensionType {
  /*
   * These constants represent the Categorical-specific keys used in the config file.
   */
  public static final String CATEGORICAL_TYPE = "type";
  public static final String ID_MAPPING_REF = "idMappingRef";

  /**
   * Represents the name for out-of-vocabulary, that is, unknown categories
   */
  public static final String OUT_OF_VOCAB = "OUT_OF_VOCAB";

  private final CategoricalType _categoricalType;
  private final ResourceRef _idMappingRef;
  private final Map<Long, String> _idToCategoryMap;
  private String _str;

  /**
   * Builds a Categorical object from its constituent fields
   * @param categoricalType {@link CategoricalType}
   * @param idMappingRef {@link ResourceRef} to a file that maps category names to IDs
   * @param idToCategoryMap A map of ID to category names, built from the ID mapping file
   */
  public Categorical(CategoricalType categoricalType, ResourceRef idMappingRef, Map<Long, String> idToCategoryMap) {
    super(DimensionTypeEnum.CATEGORICAL);
    _categoricalType = categoricalType;
    _idMappingRef = idMappingRef;
    _idToCategoryMap = Collections.unmodifiableMap(idToCategoryMap);
  }

  /**
   * Returns the {@link CategoricalType}
   * @return CategoricalType
   */
  public CategoricalType getCategoricalType() {
    return _categoricalType;
  }

  /**
   * Returns the {@link ResourceRef} to the ID mapping file
   * @return ResourceRef
   */
  public ResourceRef getIdMappingRef() {
    return _idMappingRef;
  }

  /**
   * Returns the map of ID to category names in this Categorical object
   * @return Map<Long, String>
   */
  public Map<Long, String> getIdToCategoryMap() {
    return _idToCategoryMap;
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = String.join(", ",
          "Categorical: {" + CATEGORICAL_TYPE + ": " + _categoricalType,
          ID_MAPPING_REF + ": " + _idMappingRef + "}");
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
    if (!super.equals(o)) {
      return false;
    }
    Categorical that = (Categorical) o;
    return _categoricalType == that._categoricalType && Objects.equals(_idMappingRef, that._idMappingRef)
        && Objects.equals(_idToCategoryMap, that._idToCategoryMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _categoricalType, _idMappingRef, _idToCategoryMap);
  }
}
