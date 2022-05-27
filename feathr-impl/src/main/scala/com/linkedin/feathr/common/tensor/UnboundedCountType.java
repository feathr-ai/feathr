package com.linkedin.feathr.common.tensor;

/**
 * Type for a dimension without (known) bounds.
 *
 * @deprecated Use {@link PrimitiveDimensionType#LONG} or {@link PrimitiveDimensionType#INT}. See PROML-13156.
 */
@Deprecated
public final class UnboundedCountType extends DimensionType {
  private static final String PREFIX = "unbounded_";

  private String _name;

  @Override
  public Primitive getRepresentation() {
    return Primitive.INT;
  }

  @Override
  public int getShape() {
    return UNKNOWN_SHAPE;
  }

  @Override
  public String getName() {
    if (_name == null) {
      _name = PREFIX + this.hashCode();
    }
    return _name;
  }

  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass();
  }

  @Override
  public int hashCode() {
    return 13;
  }
}
