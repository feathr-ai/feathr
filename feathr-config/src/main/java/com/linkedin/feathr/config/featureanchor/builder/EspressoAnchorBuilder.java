package com.linkedin.feathr.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.EspressoConfig;
import com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.EspressoDataSource;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import java.net.URI;
import java.net.URISyntaxException;
import javax.annotation.Nonnull;


class EspressoAnchorBuilder implements AnchorBuilder {
  private final EspressoConfig _espressoConfig;
  private final FeatureConfig _featureConfig;
  private final AnchorConfig _anchorConfig;
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final KeyFunctionBuilder<KeyFunction> _keyFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public EspressoAnchorBuilder(@Nonnull EspressoConfig espressoConfig, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      anchoredFeatureTransformationFunctionBuilder, @Nonnull KeyFunctionBuilder<KeyFunction> keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(espressoConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _espressoConfig = espressoConfig;
    _featureConfig = featureConfig;
    _anchorConfig = anchorConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = keyFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  public Anchor build() {
    EspressoDataSourceAnchor espressoAnchor = new EspressoDataSourceAnchor();
    EspressoDataSource espressoDataSource = new EspressoDataSource();
    try {
      espressoDataSource.setD2Uri(new URI(_espressoConfig.getD2Uri()));
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    espressoDataSource.setDatabaseName(_espressoConfig.getDatabase());
    espressoDataSource.setTableName(_espressoConfig.getTable());
    espressoDataSource.setKeyFunction(_keyFunctionBuilder.build());
    espressoDataSource.setDataSourceRef(_espressoConfig.getSourceName());
    espressoAnchor.setSource(espressoDataSource);
    espressoAnchor.setTransformationFunction(_anchoredFeatureTransformationFunctionBuilder.build(_featureConfig,
        _anchorConfig));
    espressoAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Anchor anchor = new Anchor();
    anchor.setEspressoDataSourceAnchor(espressoAnchor);
    return anchor;
  }
}
