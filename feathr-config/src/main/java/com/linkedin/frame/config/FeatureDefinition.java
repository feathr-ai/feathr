package com.linkedin.frame.config;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.compute.FeatureVersion;
import java.util.Set;
import javax.annotation.Nonnull;


/**
 * Feature definition represents all implementation of a feature version where each implementation includes a
 * source and transformation The model details are documented in https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/AIM+Feature+Registry+Data+Models
 * This class serves as a top-level wrapper that groups all pegaus models together.
 */
public class FeatureDefinition {
  private final FeatureVersion _featureVersion;

  private final Set<FeatureAnchor> _featureAnchors;

  public FeatureDefinition(@Nonnull FeatureVersion featureVersion, @Nonnull Set<FeatureAnchor> featureAnchors) {
    Preconditions.checkNotNull(featureVersion);
    Preconditions.checkNotNull(featureAnchors);
    _featureVersion = featureVersion;
    _featureAnchors = featureAnchors;
  }

  /**
   * Get all {@link FeatureAnchor} of a feature. Feature anchor defines where a feature is extracted from and how it is
   * computed. A feature can have one or more anchors.
   *
   * @return feature anchors.
   */
  public Set<FeatureAnchor> getFeatureAnchors() {
    return _featureAnchors;
  }

  /**
   * Get the {@link FeatureVersion}, which encloses attributes that don't change across environments (eg. description,
   * format, status, ownership).
   *
   * @return feature version.
   */
  public FeatureVersion getFeatureVersion() {
    return _featureVersion;
  }

  @Override
  public boolean equals(Object expression) {
    if (this == expression) {
      return true;
    }
    if (!(expression instanceof FeatureDefinition)) {
      return false;
    }
    FeatureDefinition that = (FeatureDefinition) expression;
    return Objects.equal(_featureVersion, that._featureVersion) && Objects.equal(_featureAnchors, that._featureAnchors);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(_featureVersion, _featureAnchors);
  }
}
