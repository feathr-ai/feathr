package com.linkedin.frame.core.config.producer.dimensions;

import com.linkedin.frame.core.config.ConfigObj;


/**
 * Abstract class for representing the configuration of the hashing function
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public abstract class HashFunction implements ConfigObj {
  /*
   * Constant representing the HashFunction-specific key used in the config file
   */
  public static final String HASH_FUNCTION_NAME = "name";

  /*
   * Currently we support only MurmurHash3 functions. Other hash functions when supported, should be added below.
   */
  public static final String MURMUR_HASH3_X86_32 = "murmurhash3_x86_32";
  public static final String MURMUR_HASH3_X64_128 = "murmurhash3_x64_128";

  HashFunction() {
  }

  /**
   * Subclasses are required to override this method.
   * @return String
   */
  @Override
  public abstract String toString();
}