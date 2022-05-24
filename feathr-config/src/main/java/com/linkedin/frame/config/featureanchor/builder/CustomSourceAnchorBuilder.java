package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.CustomSourceConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.Clazz;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.CustomDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import javax.annotation.Nonnull;


class CustomSourceAnchorBuilder implements AnchorBuilder {

  private final CustomSourceConfig _customSourceConfig;
  private final FeatureConfig _featureConfig;
  private final AnchorConfig _anchorConfig;
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final KeyFunctionBuilder<KeyFunction> _keyFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public CustomSourceAnchorBuilder(@Nonnull CustomSourceConfig customSourceConfig, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      anchoredFeatureTransformationFunctionBuilder, @Nonnull KeyFunctionBuilder<KeyFunction> keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(customSourceConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _customSourceConfig = customSourceConfig;
    _featureConfig = featureConfig;
    _anchorConfig = anchorConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = keyFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  public Anchor build() {
    CustomDataSourceAnchor customSourceAnchor = new CustomDataSourceAnchor();
    CustomDataSource customDataSource = new CustomDataSource();
    Clazz clazz = new Clazz().setFullyQualifiedName(_customSourceConfig.getDataModel());
    customDataSource.setDataModel(clazz);
    customDataSource.setKeyFunction(_keyFunctionBuilder.build());
    customDataSource.setDataSourceRef(_customSourceConfig.getSourceName());
    customSourceAnchor.setSource(customDataSource);
    customSourceAnchor.setTransformationFunction(_anchoredFeatureTransformationFunctionBuilder.build(_featureConfig,
        _anchorConfig));
    customSourceAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Anchor anchor = new Anchor();
    anchor.setCustomDataSourceAnchor(customSourceAnchor);
    return anchor;
  }
}
