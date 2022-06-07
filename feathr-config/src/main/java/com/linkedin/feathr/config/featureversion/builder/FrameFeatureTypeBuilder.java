package com.linkedin.feathr.config.featureversion.builder;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.compute.FrameFeatureType;
import com.linkedin.feathr.core.config.producer.definitions.FeatureType;
import com.linkedin.feathr.core.config.producer.common.FeatureTypeConfig;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Builder class that builds {@link FrameFeatureType} pegasus object that is used as the legacy type of a feature.
 */
public class FrameFeatureTypeBuilder {

  private static final FrameFeatureTypeBuilder INSTANCE = new FrameFeatureTypeBuilder();

  public static FrameFeatureTypeBuilder getInstance() {
    return INSTANCE;
  }

  private FrameFeatureTypeBuilder() {
    // singleton constructor
  }

  /**
   * Build {@link FrameFeatureType} pegasus object if [[FeatureTypeConfig]] contains legacy feature types
   */
  public Optional<FrameFeatureType> build(@Nonnull FeatureTypeConfig featureTypeConfig) {
    Preconditions.checkNotNull(featureTypeConfig);
    Preconditions.checkNotNull(featureTypeConfig.getFeatureType());

    FrameFeatureType featureType;

    if (featureTypeConfig.getFeatureType() == FeatureType.UNSPECIFIED) {
      throw new IllegalArgumentException("UNSPECIFIED feature type should not be used in feature config");
    } else if (TensorTypeTensorFeatureFormatBuilder.VALID_FEATURE_TYPES.contains(featureTypeConfig.getFeatureType())) {
      // high level type is always TENSOR, for DENSE_TENSOR, SPARSE_TENSOR, and RAGGED_TENSOR
      featureType = FrameFeatureType.TENSOR;
    } else {
      // For legacy type, since there is a 1:1 mapping of the types between com.linkedin.proml.mlFeatureVersion.FeatureType
      //   and com.linkedin.frame.core.config.producer.definitions.FeatureType for the rest types,
      //   build directly by name
      featureType = FrameFeatureType.valueOf(featureTypeConfig.getFeatureType().toString());
    }

    return Optional.of(featureType);
  }
}
