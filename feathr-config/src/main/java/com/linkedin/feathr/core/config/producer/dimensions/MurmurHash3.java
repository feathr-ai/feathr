package com.linkedin.feathr.core.config.producer.dimensions;

import java.util.Objects;
import java.util.Optional;


/**
 * Configuration for the MurmurHash3 hashing functions.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class MurmurHash3 extends HashFunction {
  /*
   * These constants represent the MurmurHash3 specific keys used in the config file
   */
  public static final String SEED = "seed";

  private final String _functionName;     // One of murmurhash3_x86_32 or murmurhash3_x64_128
  private final Integer _seed;            // can be optional
  private String _str;

  /**
   * Constructor
   * @param seed Seed for the hash function. If set to null, the default value (0) will be used
   */
  public MurmurHash3(String functionName, Integer seed) {
    _functionName = functionName;
    _seed = seed;
  }

  /**
   * Getter for the murmurhash3 function variant
   * @return Name of the murmurhash3 function
   */
  public String getFunctionName() {
    return _functionName;
  }

  /**
   * Getter for seed
   * @return Optionally returns the seed value
   */
  public Optional<Integer> getSeed() {
    return Optional.ofNullable(_seed);
  }

  @Override
  public String toString() {
    // for example, murmurHash3: {name: murmurhash3_x86_32, seed: 123456}
    if (_str == null) {
      _str = String.join(", ", "murmurhash3: {name: " + _functionName, SEED + ": " + _seed + "}");
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
    MurmurHash3 that = (MurmurHash3) o;
    return Objects.equals(_functionName, that._functionName) && Objects.equals(_seed, that._seed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_functionName, _seed);
  }
}
