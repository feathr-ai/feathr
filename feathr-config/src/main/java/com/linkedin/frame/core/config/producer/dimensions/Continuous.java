package com.linkedin.frame.core.config.producer.dimensions;

/**
 * Represents Continuous dimension type. At present, it's just a marker class.
 * Values along this dimension are measurable but not countable.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class Continuous extends DimensionType {
  public Continuous() {
    super(DimensionTypeEnum.CONTINUOUS);
  }

  @Override
  public String toString() {
    return getType().toString();
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }
}
