package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.Clazz;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.CouchbaseDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import javax.annotation.Nonnull;


class CouchbaseAnchorBuilder implements AnchorBuilder {
  private CouchbaseConfig _couchbaseConfig;
  private final FeatureConfig _featureConfig;
  private final AnchorConfig _anchorConfig;
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final KeyFunctionBuilder<KeyFunction> _keyFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public CouchbaseAnchorBuilder(@Nonnull CouchbaseConfig couchbaseConfig, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      anchoredFeatureTransformationFunctionBuilder, @Nonnull KeyFunctionBuilder<KeyFunction> keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(couchbaseConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _couchbaseConfig = couchbaseConfig;
    _featureConfig = featureConfig;
    _anchorConfig = anchorConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = keyFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  public Anchor build() {
    CouchbaseDataSourceAnchor couchbaseAnchor = new CouchbaseDataSourceAnchor();
    CouchbaseDataSource couchbaseDataSource = new CouchbaseDataSource();
    couchbaseDataSource.setBucketName(_couchbaseConfig.getBucketName());
    couchbaseAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Clazz clazz = new Clazz();
    clazz.setFullyQualifiedName(_couchbaseConfig.getDocumentModel());
    couchbaseDataSource.setDocumentDataModel(clazz);
    couchbaseDataSource.setKeyFunction(_keyFunctionBuilder.build());
    couchbaseDataSource.setDataSourceRef(_couchbaseConfig.getSourceName());
    couchbaseAnchor.setSource(couchbaseDataSource);
    couchbaseAnchor.setTransformationFunction(_anchoredFeatureTransformationFunctionBuilder.build(_featureConfig,
        _anchorConfig));
    Anchor anchor = new Anchor();
    anchor.setCouchbaseDataSourceAnchor(couchbaseAnchor);
    return anchor;
  }
}
