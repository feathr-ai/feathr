package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.data.template.StringArray;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.RequestParameterValueMap;
import com.linkedin.feathr.featureDataModel.RestliDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


class RestliAnchorBuilder extends BaseRestliAnchorBuilder implements AnchorBuilder {
  public RestliAnchorBuilder(RestliConfig restliConfig, FeatureConfig featureConfig, AnchorConfig anchorConfig,
      AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder,
      KeyFunctionBuilder keyFunctionBuilder, KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    super(restliConfig, featureConfig, anchorConfig, anchoredFeatureTransformationFunctionBuilder,
        keyFunctionBuilder, keyPlaceholdersBuilder);
  }

  @Override
  public Anchor buildAnchor(@Nullable StringArray projections,
      @Nullable RequestParameterValueMap requestParameterValueMap, @Nonnull String resourceName, String finderName,
      @Nonnull TransformationFunction transformationFunction, @Nonnull KeyFunction keyFunction,
      @Nonnull KeyPlaceholderArray keyPlaceholders, String sourceName) {
    Preconditions.checkNotNull(resourceName);
    Preconditions.checkNotNull(transformationFunction);
    Preconditions.checkNotNull(keyFunction);
    Preconditions.checkNotNull(keyPlaceholders);
    Preconditions.checkArgument(finderName == null,
        String.format("Trying to set finder %s to Restli anchor.", finderName));
    RestliDataSourceAnchor restliAnchor = new RestliDataSourceAnchor();
    RestliDataSource restliDataSource = new RestliDataSource();
    restliDataSource.setKeyFunction(keyFunction);
    restliDataSource.setResourceName(resourceName);
    restliDataSource.setDataSourceRef(sourceName);
    if (projections != null) {
      restliDataSource.setProjections(projections);
    }
    if (requestParameterValueMap != null) {
      restliDataSource.setRequestParameters(requestParameterValueMap);
    }
    restliAnchor.setSource(restliDataSource);
    restliAnchor.setTransformationFunction(transformationFunction);
    restliAnchor.setKeyPlaceholders(keyPlaceholders);
    Anchor anchor = new Anchor();
    anchor.setRestliDataSourceAnchor(restliAnchor);
    return anchor;
  }
}
