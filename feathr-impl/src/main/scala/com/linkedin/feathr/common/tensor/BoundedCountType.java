package com.linkedin.feathr.common.tensor;

import java.util.Objects;


/**
 * Represents the interval 0 .. bound-1
 * @deprecated Use {@link PrimitiveDimensionType#INT}. See PROML-13156.
 */
@Deprecated
public final class BoundedCountType extends DimensionType {
  private static final String PREFIX = "bounded_";
  private String _name;
  private final int _bound;
  private final Primitive _representation;

  public BoundedCountType(int bound) {
    this(bound, Primitive.INT);
  }

  public BoundedCountType(int bound, Primitive representation) {
    this._bound = bound;
    this._representation = representation;
  }

  public int getBound() {
    return _bound;
  }

  @Override
  public void setDimensionValue(WriteableTuple target, int column, Object value) {
    Objects.requireNonNull(value);
    long dimValue;
    if (value instanceof Number) {
      dimValue = ((Number) value).longValue();
    } else if (value instanceof String) {
      // legacy behavior
      dimValue = Long.parseLong((String) value);
    } else {
      throw new IllegalArgumentException("Unexpected type " + value.getClass());
    }
    if (dimValue < 0 || dimValue >= _bound) {
      throw new IllegalArgumentException(dimValue + " must be within [0, " + _bound + ").");
    }
    super.setDimensionValue(target, column, dimValue);
  }

  @Override
  public Primitive getRepresentation() {
    return _representation;
  }

  @Override
  public String indexToString(long index) {
    return Long.toString(index);
  }

  @Override
  public long stringToIndex(String string) {
    long index = Long.parseLong(string);
    if (index < 0 || index >= _bound) {
      throw new IllegalArgumentException(string + " must be within [0, " + _bound + ").");
    }
    return index;
  }

  @Override
  public int getShape() {
    return _bound;
  }

  @Override
  public String getName() {
    if (_name == null) {
      _name = PREFIX + _bound;
    }
    return _name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BoundedCountType that = (BoundedCountType) o;
    return _bound == that._bound && _representation == that._representation;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_bound, _representation);
  }
}