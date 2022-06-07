package com.linkedin.feathr.core.config.producer.dimensions;

/**
 * Represents an Entity dimension. Values along this dimension are entity IDs.
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class Entity extends DimensionType {
  public Entity() {
    super(DimensionTypeEnum.ENTITY);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return super.equals(obj);
  }

  @Override
  public String toString() {
    return getType().toString();
  }
}
