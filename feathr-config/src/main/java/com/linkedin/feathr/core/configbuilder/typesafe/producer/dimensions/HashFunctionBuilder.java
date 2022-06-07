package com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.feathr.core.config.producer.dimensions.HashFunction;
import com.linkedin.feathr.core.config.producer.dimensions.MurmurHash3;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static com.linkedin.feathr.core.config.producer.dimensions.HashFunction.*;
import static com.linkedin.feathr.core.config.producer.dimensions.MurmurHash3.SEED;
import static com.linkedin.feathr.core.utils.Utils.string;


/**
 * Builds the Hash function config
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
class HashFunctionBuilder {
  /*
   * Map of supported hash function names to builder functions
   */
  private static Map<String, Function<Config, HashFunction>> _builders = new HashMap<>();
  static {
    _builders.put(MURMUR_HASH3_X86_32, HashFunctionBuilder::buildMurmurHash3);
    _builders.put(MURMUR_HASH3_X64_128, HashFunctionBuilder::buildMurmurHash3);
  }

  // Set of supported hash functions, derived from the map above
  private static Set<String> _supportedHashFunctions = _builders.keySet();

  private HashFunctionBuilder() {
  }

  /*
   * Builds the HashFunction config object
   * hashFunctionConfig represents the value part of
   * hashFunction: { ... }
   */
  static HashFunction build(Config hashFunctionConfig) {
    String hashFunctionName = hashFunctionConfig.getString(HASH_FUNCTION_NAME); // e.g "murmurHash3_x86_32"
    if (_supportedHashFunctions.contains(hashFunctionName)) {
      return _builders.get(hashFunctionName).apply(hashFunctionConfig);
    } else {
      throw new ConfigBuilderException("Expected hash function from " + string(_supportedHashFunctions)
          + ", found " + hashFunctionName);
    }
  }

  /*
   * Builds the MurmurHash3 hash function config from the value part of:
   * hashFunction: {
   *   name: murmurHash3_x86_32 | murmurHash3_x64_128
   *   seed: ...
   * }
   */
  private static HashFunction buildMurmurHash3(Config murmurHash3Config) {
    String hashFunctionName = murmurHash3Config.getString(HASH_FUNCTION_NAME);
    Integer seed = murmurHash3Config.hasPath(SEED) ? murmurHash3Config.getInt(SEED) : null;

    return new MurmurHash3(hashFunctionName, seed);
  }
}
