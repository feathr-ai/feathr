package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.VeniceConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.VeniceDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import javax.annotation.Nonnull;


class VeniceAnchorBuilder implements AnchorBuilder {

  private final VeniceConfig _veniceConfig;
  private final FeatureConfig _featureConfig;
  private final AnchorConfig _anchorConfig;
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final KeyFunctionBuilder<KeyFunction> _keyFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public VeniceAnchorBuilder(@Nonnull VeniceConfig veniceConfig, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      anchoredFeatureTransformationFunctionBuilder, @Nonnull KeyFunctionBuilder<KeyFunction> keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(veniceConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _veniceConfig = veniceConfig;
    _featureConfig = featureConfig;
    _anchorConfig = anchorConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = keyFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  public Anchor build() {
    VeniceDataSourceAnchor veniceAnchor = new VeniceDataSourceAnchor();
    VeniceDataSource veniceDataSource = new VeniceDataSource();
    veniceDataSource.setStoreName(_veniceConfig.getStoreName());
    veniceDataSource.setKeyFunction(_keyFunctionBuilder.build());
    veniceDataSource.setDataSourceRef(_veniceConfig.getSourceName());
    veniceAnchor.setSource(veniceDataSource);
    veniceAnchor.setTransformationFunction(_anchoredFeatureTransformationFunctionBuilder.build(_featureConfig,
        _anchorConfig));
    veniceAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Anchor anchor = new Anchor();
    anchor.setVeniceDataSourceAnchor(veniceAnchor);
    return anchor;
  }
}
