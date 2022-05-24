package com.linkedin.frame.core.config.producer.common;

/**
 * Features include references to their dimension(s). Also, a Hashed dimension has a reference to the dimension whose
 * values have been hashed. This class represents such a reference. The reference consists of (namespace, name, version)
 * tuple.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class DimensionRef extends TypedRef {

  /**
   * Constructor
   * @param namespace dimension {@link Namespace}
   * @param name dimension name
   * @param version dimension {@link Version}
   */
  public DimensionRef(Namespace namespace, String name, Version version) {
    super(namespace, name, version);
  }

  /**
   * Constructor to create DimensionRef from a string representation.
   * @param dimRefString A string representation of the dimension reference
   * @see TypedRef#TypedRef(String)
   */
  public DimensionRef(String dimRefString) {
    super(dimRefString);
  }

  /**
   * Constructor to create DimensionRef from a string representation.
   * @param namespace {@link Namespace} to use if the string doesn't contain a namespace
   * @param dimRefString The reference string for a dimension
   * @see TypedRef#TypedRef(Namespace, String)
   */
  public DimensionRef(Namespace namespace, String dimRefString) {
    super(namespace, dimRefString);
  }
}
