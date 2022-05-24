package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.StringArray;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.RequestParameterValueMap;
import com.linkedin.feathr.featureDataModel.RestliFinderDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;


public class RestliFinderAnchorBuilder extends BaseRestliAnchorBuilder implements AnchorBuilder {
  public RestliFinderAnchorBuilder(RestliConfig restliConfig, FeatureConfig featureConfig, AnchorConfig anchorConfig,
      AnchoredFeatureTransformationFunctionBuilder onlineDataSourceTransformationFunctionBuilder,
      KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    super(restliConfig, featureConfig, anchorConfig, onlineDataSourceTransformationFunctionBuilder,
        null, keyPlaceholdersBuilder);
  }

  @Override
  public Anchor buildAnchor(@Nullable StringArray projections,
      @Nullable RequestParameterValueMap requestParameterValueMap, @Nonnull String resourceName, @Nonnull String finderName,
      @Nonnull TransformationFunctionForOnlineDataSource.TransformationFunction transformationFunction,
      @Nullable KeyFunction keyFunction, @Nonnull KeyPlaceholderArray keyPlaceholders, String sourceName) {
    Preconditions.checkNotNull(resourceName);
    Preconditions.checkNotNull(transformationFunction);
    Preconditions.checkNotNull(finderName);
    Preconditions.checkNotNull(keyPlaceholders);
    RestliFinderDataSourceAnchor restliFinderAnchor = new RestliFinderDataSourceAnchor();
    RestliFinderDataSource restliFinderDataSource = new RestliFinderDataSource();
    restliFinderDataSource.setResourceName(resourceName);
    restliFinderDataSource.setFinderMethod(finderName);
    restliFinderDataSource.setDataSourceRef(sourceName);
    restliFinderDataSource.setKeyFunction(keyFunction, SetMode.IGNORE_NULL);
    restliFinderDataSource.setProjections(projections, SetMode.IGNORE_NULL);
    restliFinderDataSource.setRequestParameters(requestParameterValueMap, SetMode.IGNORE_NULL);
    restliFinderAnchor.setSource(restliFinderDataSource);
    restliFinderAnchor.setTransformationFunction(transformationFunction);
    restliFinderAnchor.setKeyPlaceholders(keyPlaceholders);
    Anchor anchor = new Anchor();
    anchor.setRestliFinderDataSourceAnchor(restliFinderAnchor);
    return anchor;
  }
}
