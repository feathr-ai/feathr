package com.linkedin.feathr.config.featureanchor;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;


/**
 * Sha 256 implementation of {@link FeatureAnchorHasher}.
 */
public class Sha256FeatureAnchorHasher implements FeatureAnchorHasher {
  private static final Sha256FeatureAnchorHasher INSTANCE = new Sha256FeatureAnchorHasher();
  public static Sha256FeatureAnchorHasher getInstance() {
    return INSTANCE;
  }

  @Override
  public long hash(FeatureAnchor featureAnchor) {
    return Hashing.sha256().hashString(featureAnchor.toString(), Charsets.UTF_8).asLong();
  }
}
