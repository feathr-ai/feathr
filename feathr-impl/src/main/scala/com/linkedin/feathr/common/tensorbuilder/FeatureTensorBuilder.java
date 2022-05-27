package com.linkedin.feathr.common.tensorbuilder;

import com.linkedin.feathr.common.tensor.FeatureTensor;


/**
 * Type-safe builder for creating FeatureTensor instances.
 *
 * Encapsulates information needed to check and coerce dimension and value objects.
 *
 * Instances of FeatureTensorBuilder may be safely reused by calling {@link #init} to reset the builder.
 *
 * IMPORTANT: Builders are not thread safe and must not be used in a concurrent fashion.
 *
 * @deprecated Do not use. Use {@link com.linkedin.feathr.common.tensorbuilder.TensorBuilder} instead. See PROML-13156.
 */
@Deprecated
public interface FeatureTensorBuilder {

  /**
   * Initializes the builder with an estimated initial capacity (number of rows) for the tensor.
   *
   * This method (or {@link #init}) MUST be called first before using the builder (unless an implementing subclass
   * explicitly states otherwise). This method may also be used to "reset" the builder, enabling it to be reused.
   *
   * @param estimatedRows number of rows to allocate
   */
  void init(int estimatedRows);

  /**
   * Initializes the builder.
   *
   * This method (or {@link #init(int)}) MUST be called first before using the builder (unless an implementing subclass
   * explicitly states otherwise). This method may also be used to "reset" the builder, enabling it to be reused.
   */
  default void init() {
    init(0);
  }

  /**
   * Returns newly-constructed FeatureTensor based on builder contents.
   *
   * Does NOT reset the internal state, so if you plan to reuse this instance, call {@link #init} or {@link #init(int)} before reusing.
   */
  FeatureTensor build();

  /**
   * Associates (dimensions...) tuple with value.
   */
  FeatureTensorBuilder put(Object value, Object... dimensions);
}