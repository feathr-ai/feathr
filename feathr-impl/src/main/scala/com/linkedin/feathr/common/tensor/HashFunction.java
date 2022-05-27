package com.linkedin.feathr.common.tensor;

/**
 * Custom implementation of {@link HashedType} should implement this interface.
 */
public interface HashFunction {
  /**
   * Hash the string.
   * @param string
   * @return the non-negative hash code of the string.
   */
  long stringToIndex(String string);
}
