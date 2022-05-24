package com.linkedin.frame.core.config.producer.common;

/**
 * Represents a fully-qualified reference to a feature.
 */
public class FeatureRef extends TypedRef {

  /**
   * Constructor
   * @param namespace String representation of namespace of referenced feature
   * @param name Name of referenced feature
   * @param major Major version of referenced feature
   * @param minor Minor version of referenced feature
   */
  public FeatureRef(String namespace, String name, int major, int minor) {
    super(namespace, name, major, minor);
  }

  /**
   * Constructor
   * @param namespace {@link Namespace} of referenced feature
   * @param name Name of referenced feature
   * @param version {@link Version} of referenced feature
   */
  public FeatureRef(Namespace namespace, String name, Version version) {
    super(namespace, name, version);
  }

  /**
   * Constructor to create FeatureRef from a string representation.
   * @param featureRefString A string representation of the feature reference
   * @see TypedRef#TypedRef(String)
   */
  public FeatureRef(String featureRefString) {
    super(featureRefString);
  }
}
