package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource;
import javax.annotation.Nonnull;


class ObservationPassthroughAnchorBuilder implements AnchorBuilder {
  private AnchorConfig _anchorConfig;
  private PassThroughConfig _passThroughConfig;
  private final FeatureConfig _featureConfig;
  private AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final KeyFunctionBuilder<KeyFunction> _keyFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public ObservationPassthroughAnchorBuilder(@Nonnull PassThroughConfig passThroughConfig,
      @Nonnull FeatureConfig featureConfig, @Nonnull AnchorConfig anchorConfig,
      @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction> anchoredFeatureTransformationFunctionBuilder,
      @Nonnull KeyFunctionBuilder<KeyFunction> keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(passThroughConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _anchorConfig = anchorConfig;
    _passThroughConfig = passThroughConfig;
    _featureConfig = featureConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = keyFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  public Anchor build() {
    ObservationPassthroughDataSource observationPassthroughDataSource = new ObservationPassthroughDataSource();
    observationPassthroughDataSource.setKeyFunction(_keyFunctionBuilder.build());
    observationPassthroughDataSource.setDataSourceRef(_passThroughConfig.getSourceName());
    ObservationPassthroughDataSourceAnchor observationPassthroughAnchor = new ObservationPassthroughDataSourceAnchor();
    observationPassthroughAnchor.setSource(observationPassthroughDataSource);
    observationPassthroughAnchor.setTransformationFunction(
        _anchoredFeatureTransformationFunctionBuilder.build(_featureConfig, _anchorConfig));
    observationPassthroughAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Anchor anchor = new Anchor();
    anchor.setObservationPassthroughDataSourceAnchor(observationPassthroughAnchor);
    return anchor;
  }
}
