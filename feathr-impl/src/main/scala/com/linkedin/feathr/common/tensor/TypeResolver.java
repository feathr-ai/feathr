package com.linkedin.feathr.common.tensor;

import com.linkedin.feathr.common.FeatureUrnUtil;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import java.net.URISyntaxException;
import java.util.Optional;


/**
 * @deprecated FML and named semantic dimension types in Quince deprecated. Use primitive tensor types. See PROML-13156.
 */
@Deprecated
public interface TypeResolver {
  String FEATURE_REF_DELIMITER = "-";

  /**
   * Resolve a feature reference to its type.
   * @param featureReference in format namespace-name-major-minor.
   * @return the resolved tensor type of the feature.
   *
   * @throws IllegalArgumentException if the featureReference is not in the format specified above.
   *
   * @deprecated in favor of the MLFeatureVersionUrn one.
   */
  @Deprecated
  default Optional<TensorType> resolveFeature(String featureReference) {
    try {
      return resolveFeature(FeatureUrnUtil.toUrn(featureReference));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Resolve a feature urn to its type.
   * @param featureVersionUrn in format urn:li:mlFeatureVersion:(urn:li:mlFeature:(namespace,name),major,minor,patch).
   * @return the resolved tensor type of the feature.
   */
  Optional<TensorType> resolveFeature(MlFeatureVersionUrn featureVersionUrn);

  /**
   * Resolve a dimension reference to its type.
   * @param dimensionReference in format namespace:name:major.minor.
   * @return the resolved type of the dimension.
   */
  Optional<DimensionType> resolveDimension(String dimensionReference);
}
