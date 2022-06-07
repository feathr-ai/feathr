package com.linkedin.feathr.core.config.producer.dimensions;

/**
 * Represents Text dimension type. The values along this dimension are used for NLP.
 * At present, it's just a marker class.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class Text extends DimensionType {
  public Text() {
    super(DimensionTypeEnum.TEXT);
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
