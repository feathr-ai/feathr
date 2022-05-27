package com.linkedin.feathr.common.tensor;

/**
 *  A dimension type to represent text strings.
 *  It cannot be used in stand-alone manner because it doesn't support stringToIndex and indexToString operation.
 *  Sole purpose of TextType is to serve as base dimension for  {@link HashedType} dimension.
 *
 * @deprecated Use {@link PrimitiveDimensionType#STRING}. See PROML-13156.
 */
@Deprecated
public class TextType extends DimensionType {
  private static final String NAME = "STRING";

  @Override
  public Primitive getRepresentation() {
    return Primitive.STRING;
  }

  @Override
  public long stringToIndex(String term) {
    throw new UnsupportedOperationException("Text dimension doesn't support term to index. Consider using HashedType");
  }

  @Override
  public String indexToString(long index) {
    throw new UnsupportedOperationException("Text dimension doesn't support index to string. Consider using HashedType");
  }

  @Override
  public String getName() {
    return NAME;
  }


  @Override
  public boolean equals(Object o) {
    return this == o || o != null && getClass() == o.getClass();
  }

  @Override
  public int hashCode() {
    return 42;
  }
}
