package com.linkedin.feathr.common.tensor;

import java.util.Objects;


/**
 * Represents the type of a named entity. An entity is addressable by its ID.
 *
 * @deprecated Use {@link PrimitiveDimensionType#LONG}, {@link PrimitiveDimensionType#STRING}. See PROML-13156.
 */
@Deprecated
public final class EntityType extends DimensionType {
  private final String _name;

  public EntityType(String name) {
    this._name = name;
  }

  public String getName() {
    return _name;
  }

  @Override
  public Primitive getRepresentation() {
    return Primitive.LONG;
  }

  @Override
  public String indexToString(long index) {
    return Long.toString(index);
  }

  @Override
  public int getShape() {
    return UNKNOWN_SHAPE;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EntityType that = (EntityType) o;
    return _name.equals(that._name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_name);
  }
}
