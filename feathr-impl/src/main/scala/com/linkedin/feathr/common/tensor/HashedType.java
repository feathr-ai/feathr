package com.linkedin.feathr.common.tensor;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Represent a hashed type. Hashed types are used on top of other types, for example to
 * bucketize tensors.
 *
 * stringToIndex(indexToString(x)) == x for all x.
 *
 * indexToString(stringToIndex(x)) == x if inverseMap contains x as a value.
 *
 * @deprecated Use {@link PrimitiveDimensionType#LONG}. See PROML-13156.
 */
@Deprecated
public final class HashedType extends DimensionType {
  private static final String TYPE_PREFIX = "hash_";
  public static final String DATA_PREFIX = "#";

  public static HashFunction getMurmur3_32(Integer seed) {
    return new Murmur3_32(seed);
  }
  public static HashFunction getMurmur3_64(Integer seed) {
    return new Murmur3_64(seed);
  }

  private String name;
  private final DimensionType base;
  private final Integer modulo;
  private final HashFunction hashFunction;
  private final Map<Long, String> localInverseMap;

  public HashedType(DimensionType base, Integer modulo, HashFunction hashFunction, Map<Long, String> inverseMap) {
    this.base = base;
    this.modulo = modulo;
    this.hashFunction = hashFunction;
    ImmutableMap.Builder<Long, String> builder = ImmutableMap.builder();
    inverseMap.forEach(builder::put);
    localInverseMap = builder.build();
  }

  public DimensionType getBase() {
    return base;
  }

  @Override
  public Primitive getRepresentation() {
    if (hashFunction instanceof Murmur3_32) {
      return Primitive.INT;
    }
    return Primitive.LONG;
  }

  @Override
  public void setDimensionValue(WriteableTuple target, int column, Object value) {
    // match the behavior of `stringToIndex` for compatibility reasons
    super.setDimensionValue(target, column, stringToIndex((String) value));
  }

  // WARNING: Contrary to usual expectation, this method is not the inverse of `setDimensionValue` since it relies on
  //         `indexToString`; see warning below.
  @Override
  public Object getDimensionValue(ReadableTuple tuple, int column) {
    // match the behavior of `indexToString` for compatibility reasons
    return indexToString(((Number) super.getDimensionValue(tuple, column)).longValue());
  }

  /**
   * WARNING: `indexToString(stringToIndex(x))` is likely not equal to `x` if x is not in the inverseMap.
   */
  @Override
  public String indexToString(long index) {
    String ret = localInverseMap.get(index);
    if (ret == null) {
      ret =  DATA_PREFIX + index;
    }
    return ret;
  }

  /**
   * WARNING: `indexToString(stringToIndex(x))` is likely not equal to `x` if x is not in the inverseMap.
   */
  @Override
  public long stringToIndex(String string) {
    try {
      if (string.startsWith(DATA_PREFIX)) {
        long parsed = Long.parseLong(string.substring(1));
        // In case the parsed number is not within the valid range, just fall back to hashing the string.
        if (parsed >= 0 && (modulo == null || parsed < modulo)) {
          return parsed;
        }
      }
    } catch (NumberFormatException expected) {
      // In case # is not followed by a long number, just fall back to hashing the string.
    }
    long index = hashFunction.stringToIndex(string);
    if (modulo != null) {
      index = index % modulo;
    }

    return index;
  }

  @Override
  public int getShape() {
    return modulo == null ? UNKNOWN_SHAPE : modulo;
  }

  @Override
  public String getName() {
    if (name == null) {
      name = TYPE_PREFIX + modulo;
    }
    return name;
  }

  public Optional<Integer> getModulo() {
    return Optional.ofNullable(modulo);
  }

  public HashFunction getHashFunction() {
    return hashFunction;
  }

  public Map<Long, String> getIndexToStringMap() {
    return localInverseMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HashedType that = (HashedType) o;
    return base.equals(that.base) && Objects.equals(modulo, that.modulo) && hashFunction.equals(that.hashFunction);
  }

  @Override
  public int hashCode() {
    return Objects.hash(base, modulo, hashFunction);
  }
}
