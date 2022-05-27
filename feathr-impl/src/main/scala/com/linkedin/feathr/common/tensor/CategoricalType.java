package com.linkedin.feathr.common.tensor;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * Represent a set of fixed values which can be either ordinal or nominal.
 * Keeps a mapping from the categories to integer to allow for a more compact representation.
 * @deprecated Use {@link PrimitiveDimensionType#INT} or {@link PrimitiveDimensionType#STRING}. See PROML-13156.
 */
@Deprecated
public class CategoricalType extends DimensionType {
  private final String _name;
  private final boolean _ordinal;
  private final List<String> _categories;
  private final Map<String, Integer> _stringToIndexMap;
  private final boolean _lenient;

  /**
   * Build a categorical type.
   *
   * @param name name for this type
   * @param ordinal true for ordinal or false for nominal
   * @param categories list of names for the categories.
   * @param lenient if out-of-vocab should return 0 instead of throwing exception.
   */
  public CategoricalType(String name, boolean ordinal, List<String> categories, boolean lenient) {
    this._name = name;
    this._ordinal = ordinal;
    this._categories = categories;
    ImmutableMap.Builder<String, Integer> builder = ImmutableMap.builder();
    for (int i = 0; i < this._categories.size(); i++) {
      builder.put(categories.get(i), i);
    }
    _stringToIndexMap = builder.build();
    this._lenient = lenient;
  }

  public CategoricalType(String name, boolean ordinal, List<String> categories) {
    this(name, ordinal, categories, true);
  }

  @Override
  public String getName() {
    return _name;
  }

  public boolean isOrdinal() {
    return _ordinal;
  }

  public List<String> getCategories() {
    return _categories;
  }

  @Override
  public Primitive getRepresentation() {
    return Primitive.INT;
  }

  @Override
  public void setDimensionValue(WriteableTuple target, int column, Object value) {
    if (!(value instanceof String || value instanceof Integer || value instanceof Long)) {
      throw new IllegalArgumentException("Got dimension of type " + value.getClass() + ", expected String|int|long");
    }
    super.setDimensionValue(target, column, stringToIndex(value.toString()));
  }

  @Override
  public Object getDimensionValue(ReadableTuple tuple, int column) {
    return indexToString(((Number) super.getDimensionValue(tuple, column)).longValue());
  }

  @Override
  public String indexToString(long index) {
    return _categories.get((int) index);
  }

  @Override
  public long stringToIndex(String string) {
    Integer index = _stringToIndexMap.get(string);
    if (index == null) {
      if (_lenient) {
        return 0L;
      }
      throw new IllegalArgumentException(string + " is out of vocabulary.");
    } else {
      return index.longValue();
    }
  }

  @Override
  public int getShape() {
    return _categories.size();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CategoricalType that = (CategoricalType) o;
    return _ordinal == that._ordinal && _name.equals(that._name) && _categories.equals(that._categories);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_name, _ordinal, _categories);
  }
}
