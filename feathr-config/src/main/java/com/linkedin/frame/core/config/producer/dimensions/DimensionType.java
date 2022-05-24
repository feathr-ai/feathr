package com.linkedin.frame.core.config.producer.dimensions;

import java.util.Objects;


/**
 * An abstract class for representing dimension types such as {@link Categorical}, {@link Discrete}, etc.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public abstract class DimensionType {
  private DimensionTypeEnum _type;

  /**
   * Initializes the dimension type explicitly.
   * @param type
   */
  DimensionType(DimensionTypeEnum type) {
    _type = type;
  }

  /**
   * Gets this dimension's type.
   * @return {@link DimensionTypeEnum}
   */
  public DimensionTypeEnum getType() {
    return _type;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DimensionType that = (DimensionType) o;
    return _type == that._type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_type);
  }
}
