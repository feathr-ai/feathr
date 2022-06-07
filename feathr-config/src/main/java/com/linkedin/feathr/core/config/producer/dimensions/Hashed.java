package com.linkedin.feathr.core.config.producer.dimensions;

import com.linkedin.feathr.core.config.producer.common.DimensionRef;
import com.linkedin.feathr.core.config.producer.common.ResourceRef;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


/**
 * Represent Hashed dimension type. Values along this dimension have been hashed.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class Hashed extends DimensionType {
  /*
   * These constants represent the Hashed-specific keys used in the config file.
   */
  public static final String HASH_FUNCTION = "hashFunction";
  public static final String BOUND = "bound";
  public static final String DIM_REF = "dimRef";
  public static final String HASH_TO_NAME_REF = "hashToNameRef";

  private final HashFunction _hashFunction;   // The hash function used
  private final Integer _bound;               // Bound on the number of hashed values
  private final DimensionRef _dimensionRef;   // Reference to the dimension whose values have been hashed.
  private final ResourceRef _hashToNameRef;   // Reference to file that contains hashed value and name that's been hashed

  private final Map<Long, String> _hashToNameMap;
  private String _str;

  /**
   * Constructor
   * @param hashFunction {@link HashFunction}
   * @param dimensionRef {@link DimensionRef}
   */
  public Hashed(HashFunction hashFunction, Integer bound, DimensionRef dimensionRef, ResourceRef hashToNameRef,
      Map<Long, String> hashToNameMap) {
    super(DimensionTypeEnum.HASHED);
    _hashFunction = hashFunction;
    _bound = bound;
    _dimensionRef = dimensionRef;
    _hashToNameRef = hashToNameRef;
    _hashToNameMap = hashToNameMap;
  }

  /**
   * Getter for hash function used
   * @return {@link HashFunction}
   */
  public HashFunction getHashFunction() {
    return _hashFunction;
  }

  public Optional<Integer> getBound() {
    return Optional.ofNullable(_bound);
  }

  /**
   * Getter for reference to dimension whose values are hashed
   * @return {@link DimensionRef}
   */
  public DimensionRef getDimensionRef() {
    return _dimensionRef;
  }

  /**
   * Getter for reference to file that contains hashed value and name that's been hashed
   * @return {@link ResourceRef}
   */
  public ResourceRef getHashToNameRef() {
    return _hashToNameRef;
  }

  /**
   * Getter for hash to name map
   * @return Map&lt;Long, String&gt;
   */
  public Map<Long, String> getHashToNameMap() {
    return _hashToNameMap;
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = String.join(", ",
          "Hashed: {" + HASH_FUNCTION + ": " + _hashFunction,
          BOUND + ": " + _bound,
          DIM_REF + ": " + _dimensionRef,
          HASH_TO_NAME_REF + ": " + _hashToNameRef + "}");
    }

    return _str;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    Hashed hashed = (Hashed) o;
    return Objects.equals(_hashFunction, hashed._hashFunction) && Objects.equals(_bound, hashed._bound)
        && Objects.equals(_dimensionRef, hashed._dimensionRef) && Objects.equals(_hashToNameRef, hashed._hashToNameRef)
        && Objects.equals(_hashToNameMap, hashed._hashToNameMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _hashFunction, _bound, _dimensionRef, _hashToNameRef, _hashToNameMap);
  }
}
