package com.linkedin.feathr.common;

import com.linkedin.frame.common.urn.MlFeatureUrn;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.common.urn.TupleKey;
import com.linkedin.frame.common.urn.Urn;
import java.net.URISyntaxException;
import java.util.Objects;

/**
 * Manipulate feature refs and urns.
 */
public final class FeatureUrnUtil {
  public static final String DEFAULT_NAMESPACE = "global";

  public static final int DEFAULT_MAJOR_VERSION = 0;

  public static final int DEFAULT_MINOR_VERSION = 0;

  public static final int DEFAULT_PATCH_VERSION = 0;
  /**
   * Singleton for empty URN, mostly used for testing.
   */
  public static final MlFeatureVersionUrn EMPTY_URN = buildEmptyUrn();

  private static final String EMPTY_NAME = "EMPTY";

  private static final String FEATURE_REF_DELIMITER = "-";

  private static final String DEFAULT_MINOR_VERSION_STR = String.valueOf(DEFAULT_MINOR_VERSION);

  private static final String DEFAULT_MAJOR_VERSION_STR = String.valueOf(DEFAULT_MAJOR_VERSION);

  private static final String DEFAULT_PATCH_VERSION_STR = String.valueOf(DEFAULT_PATCH_VERSION);

  private FeatureUrnUtil() {
  }

  /**
   * Private method to intern special MLFeatureVersionUrns.
   *
   * @return empty MlFeatureVersionUrn
   */
  private static MlFeatureVersionUrn buildEmptyUrn() {
    try {
      return MlFeatureVersionUrn.createFromUrn(
          Urn.createFromString("urn:li:mlFeatureVersion:(urn:li:mlFeature:(" + EMPTY_NAME + "," +  EMPTY_NAME + "),0,0,0)"));
    } catch (URISyntaxException ignored) {
      // There are unit tests to make sure this does not fail.
    }
    return null;
  }

  /**
   * Translate from an urn back to a feature-ref string. Note that feature-ref is going away.
   * This method has some heuristics to make the tests pass.
   *
   * @param featureVersionUrn urn to translate
   *
   * @return a feature-ref string in the namespace-name-major-minor format.
   */
  public static String toFeatureRef(MlFeatureVersionUrn featureVersionUrn) {
    final String[] parts = splitUrn(featureVersionUrn);
    final String namespace = parts[0];
    final String name = parts[1];
    final String major = parts[2];
    final String minor = parts[3];

    String featureRefString;
    if (namespace.equals(DEFAULT_NAMESPACE) && DEFAULT_MAJOR_VERSION_STR.equals(major)
        && DEFAULT_MINOR_VERSION_STR.equals(minor)) {
      featureRefString = name;
    } else if (namespace.equals(EMPTY_NAME) && name.equals(EMPTY_NAME)) {
      // This is an empty Urn, that is, namespace and name have EMPTY_NAME value. As such return an empty string.
      featureRefString = "";
    } else {
      featureRefString = String.join(FEATURE_REF_DELIMITER, namespace, name, major, minor);
    }

    return featureRefString;
  }

  private static String[] splitFeatureRef(String featureRef) throws URISyntaxException {
    Objects.requireNonNull(featureRef, "featureRef string can't be null");

    final String[] parts = featureRef.split(FEATURE_REF_DELIMITER);
    if (parts.length == 1) {
      // Allow name only feature-ref for historical reasons.
      return new String[]{DEFAULT_NAMESPACE, parts[0], DEFAULT_MAJOR_VERSION_STR, DEFAULT_MINOR_VERSION_STR};
    }
    if (parts.length == 4) {
      return parts;
    }
    throw new URISyntaxException(featureRef, "Could not parse featureRef string, should have 4 parts separated with "
        + FEATURE_REF_DELIMITER);
  }


  /**
   * Split an urn into constituent parts.
   * This is an inverse of toUrn
   * @return array of [namespace, name, major, minor]
   */
  public static String[] splitUrn(MlFeatureVersionUrn urn) {
    final MlFeatureUrn featureUrn = urn.getEntityKey().getAs(0, MlFeatureUrn.class);
    final String major = urn.getEntityKey().getAs(1, String.class);
    final String minor = urn.getEntityKey().getAs(2, String.class);
    final String namespace = featureUrn.getEntityKey().getAs(0, String.class);
    final String name = featureUrn.getEntityKey().getAs(1, String.class);
    return new String[]{namespace, name, major, minor};
  }

  /**
   * Build an URN from a feature-ref.
   *
   * @param featureRef either an empty string or in the format namespace-name-major-minor.
   * @return a feature version urn
   *
   * @throws URISyntaxException if the feature-ref is not in the expected format
   */
  public static MlFeatureVersionUrn toUrn(String featureRef) throws URISyntaxException {
    Objects.requireNonNull(featureRef, "featureRef string can't be null");

    if (featureRef.length() == 0) {
      return EMPTY_URN;
    }

    final String[] parts = splitFeatureRef(featureRef);
    final int majorVersion = Integer.parseInt(parts[2]);
    final int minorVersion = Integer.parseInt(parts[3]);
    return toUrn(parts[0], parts[1], majorVersion, minorVersion);
  }


  /**
   * Translate from unpacked (or preparsed) FeatureRef to URN.
   *
   * @param namespace namespace of the feature
   * @param name name of the feature
   * @param major major version of feature
   * @param minor minor version of the feature
   * @return MLFeatureVersionUrn based on the input
   * @throws URISyntaxException in case of invalid inputs
   */
  public static MlFeatureVersionUrn toUrn(String namespace, String name, int major, int minor) throws URISyntaxException {
    final Urn rawFeatureUrn = new Urn(MlFeatureUrn.ENTITY_TYPE, TupleKey.create(namespace, name));
    final MlFeatureUrn featureUrn = MlFeatureUrn.createFromUrn(rawFeatureUrn);
    final Urn rawUrn = new Urn(MlFeatureVersionUrn.ENTITY_TYPE,
        TupleKey.create(featureUrn, major, minor, 0));
    return MlFeatureVersionUrn.createFromUrn(rawUrn);
  }

  /**
   * Translate from the unpacked feature-ref to the string representation of the URN.
   *
   * This is done for performance reasons when the URN is just consumed as string.
   *
   * @return String representation of the MlFeatureVersionUrn corresponding to unpacked feature-ref
   * @throws URISyntaxException in case of invalid inputs
   */
  public static String toUrnString(String namespace, String name, int major, int minor) throws URISyntaxException {
    return "urn:li:mlFeatureVersion:(urn:li:mlFeature:(" + namespace + "," + name + ")," + major + "," + minor
        + "," + DEFAULT_PATCH_VERSION_STR + ")";
  }

  /**
   * Build an URN from a feature-ref, throws an unchecked exception.
   *
   * @param featureRef featur-ref to convert to URN
   *
   * @return URN
   *
   * @throws IllegalArgumentException if the feature-ref is in the wrong format.
   */
  public static MlFeatureVersionUrn toUrnForce(String featureRef) {
    try {
      return toUrn(featureRef);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Build an URN from parts, throws an unchecked exception.
   * @throws IllegalArgumentException if feature component parts are incorrect.
   */
  public static MlFeatureVersionUrn toUrnForce(String namespace, String name, int major, int minor) {
    try {
      return toUrn(namespace, name, major, minor);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }
}