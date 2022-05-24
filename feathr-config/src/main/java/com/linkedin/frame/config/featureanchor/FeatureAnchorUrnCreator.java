package com.linkedin.frame.config.featureanchor;

import com.linkedin.frame.common.urn.MlFeatureAnchorUrn;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.common.urn.Urn;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import java.net.URISyntaxException;


/**
 * Util class to construct feature anchor urn.
 */
public class FeatureAnchorUrnCreator {
  private final FeatureAnchorHasher _featureAnchorHasher;

  /**
   * public constructor
   * @param featureAnchorHasher hasher that hashes feature anchor object to generate urn.
   */
  public FeatureAnchorUrnCreator(FeatureAnchorHasher featureAnchorHasher) {
    _featureAnchorHasher = featureAnchorHasher;
  }

  /**
   * Construct feature anchor urn. This urn will contains a hashcode of the {@link FeatureAnchor} object, which will be
   * used as the key to dedupe the same feature anchor defined in different multiproducts and different versions.
   *
   * @param mlFeatureVersionUrn feature version urn.
   * @param featureAnchor feature anchor object.
   * @return feature anchor urn.
   */
  public MlFeatureAnchorUrn create(MlFeatureVersionUrn mlFeatureVersionUrn, FeatureAnchor featureAnchor) {
    long featureAnchorSha256Hash = _featureAnchorHasher.hash(featureAnchor);

    try {
      return MlFeatureAnchorUrn.createFromUrn(
          Urn.createFromTuple(MlFeatureAnchorUrn.ENTITY_TYPE, mlFeatureVersionUrn, featureAnchorSha256Hash));
    } catch (URISyntaxException ex) {
      // This exception should never happen. A feature name should be deterministically resolved to MlFeatureVersionUrn.
      throw new IllegalArgumentException("MlFeatureAnchorUrn can't be created with feature version urn: "
          + mlFeatureVersionUrn + ", feature anchor: " + featureAnchor + ", feature anchor hash: " + featureAnchorSha256Hash, ex);
    }
  }
}
