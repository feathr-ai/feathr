package com.linkedin.feathr.core.utils;

import com.linkedin.frame.common.urn.MlFeatureUrn;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.common.urn.Urn;
import com.linkedin.feathr.core.config.producer.common.FeatureRef;
import com.linkedin.feathr.core.config.producer.common.Namespace;
import com.linkedin.feathr.core.config.producer.common.Version;
import java.net.URISyntaxException;
import java.util.Objects;


/**
 * Util class to construct MlFeatureVersionUrn.
 */
public class MlFeatureVersionUrnCreator {
  private static final MlFeatureVersionUrnCreator INSTANCE = new MlFeatureVersionUrnCreator();

  public static MlFeatureVersionUrnCreator getInstance() {
    return INSTANCE;
  }

  private MlFeatureVersionUrnCreator() {
  }

  /**
   * Creates a {@link MlFeatureVersionUrn} from a feature name.
   * A feature name is what we see in a Frame config. It can be uniquely converted into a FeatureRef then a MlFeatureUrn.
   */
  public MlFeatureVersionUrn create(String featureName) {
    FeatureRef featureRef = new FeatureRef(featureName);
    verifyFeatureRef(featureRef);
    String namespace = featureRef.getNamespace().orElse(Namespace.DEFAULT_NAMESPACE_OBJ).toString();
    String name = featureRef.getName();
    Version version = featureRef.getVersion().orElse(Version.DEFAULT_VERSION_OBJ);
    int majorVersion = version.getMajor();
    int minorVersion = version.getMinor();
    int patchVersion = Version.DEFAULT_PATCH_VERSION;
    try {
      MlFeatureUrn mlFeatureUrn = MlFeatureUrn.createFromUrn(Urn.createFromTuple(MlFeatureUrn.ENTITY_TYPE, namespace, name));
      return MlFeatureVersionUrn.createFromUrn(
          Urn.createFromTuple(MlFeatureVersionUrn.ENTITY_TYPE, mlFeatureUrn, majorVersion, minorVersion, patchVersion));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Invalid feature name: " + featureName, e);
    }
  }

  /**
   * Verify a {@link FeatureRef}:
   * 1. Frame feature can not use `global` as namespace explicitly, as it is a reserved namespace
   * 2. Frame feature can not have major and minor version as 0, as 0-0-0 is a reserved version number
   * The above reserved namespace and version number are used for legacy features with namespace and version number
   *  undefined. And they are used to check if the original feature's namespace and version number are missing.
   */
  private void verifyFeatureRef(FeatureRef featureRef) {
    featureRef.getNamespace().ifPresent(namespace -> {
      if (Objects.equals(namespace, Namespace.DEFAULT_NAMESPACE_OBJ)) {
        throw new IllegalArgumentException("Frame feature can not use `global` as namespace explicitly.");
      }
    });

    featureRef.getVersion().ifPresent(version -> {
      if (version.getMajor() == 0 && version.getMinor() == 0) {
        throw new IllegalArgumentException("Frame feature can not use 0-0 as version number explicitly. "
                + "Please use a non-zero version number instead.");
      }
    });
  }
}
