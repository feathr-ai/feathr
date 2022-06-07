package com.linkedin.feathr.config.featureanchor.builder;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.linkedin.data.DataMap;
import com.linkedin.data.template.StringArray;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.RequestParameterValue;
import com.linkedin.feathr.featureDataModel.RequestParameterValueMap;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.json.JSONArray;
import org.json.JSONObject;


abstract class BaseRestliAnchorBuilder implements AnchorBuilder {
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final Optional<KeyFunctionBuilder<KeyFunction>> _keyFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;
  private final RestliConfig _restliConfig;
  private final FeatureConfig _featureConfig;
  private final AnchorConfig _anchorConfig;

  BaseRestliAnchorBuilder(@Nonnull RestliConfig restliConfig, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull AnchoredFeatureTransformationFunctionBuilder
      anchoredFeatureTransformationFunctionBuilder, @Nullable KeyFunctionBuilder keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(restliConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _restliConfig = restliConfig;
    _featureConfig = featureConfig;
    _anchorConfig = anchorConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = Optional.ofNullable(keyFunctionBuilder);
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  @Override
  public Anchor build() {
    Optional<StringArray> projections = _restliConfig.getPathSpec().map(
        pathSpec -> new StringArray(Lists.newArrayList(pathSpec.getPathComponents()))
    );
    Optional<RequestParameterValueMap> requestParameterValueMap = _restliConfig.getReqParams().map(
        requestParams -> buildRequestParameterValueMap(requestParams)
    );
    String resourceName = _restliConfig.getResourceName();
    TransformationFunction transformationFunction = _anchoredFeatureTransformationFunctionBuilder.build(_featureConfig,
        _anchorConfig);
    Optional<String> finderName = _restliConfig.getFinder();
    Optional<KeyFunction> keyFunction = _keyFunctionBuilder.map(builder -> builder.build());
    KeyPlaceholderArray keyPlaceholderArray = _keyPlaceholdersBuilder.build();
    return buildAnchor(projections.orElse(null), requestParameterValueMap.orElse(null), resourceName,
        finderName.orElse(null), transformationFunction, keyFunction.orElse(null),
        keyPlaceholderArray, _restliConfig.getSourceName());
  }

  abstract Anchor buildAnchor(StringArray projections, RequestParameterValueMap requestParameterValueMap,
      String resourceName, String finderName, TransformationFunction transformationFunction, KeyFunction keyFunction,
      KeyPlaceholderArray keyPlaceholders, String sourceName);


  /**
   * There are two types of request parameter input, MVEL type and JSON type. MVEL type request parameter input is
   * always in String format. JSON format request input can be in DataMap or List format. For JSON format parameter,
   * we print the parameter in JSON format.
   */
  @VisibleForTesting
  RequestParameterValueMap buildRequestParameterValueMap(Map<String, Object> requestParams) {
    Map<String, RequestParameterValue> requestParameterValueMap = new HashMap<>();
    for (String key : requestParams.keySet()) {
      Object value = requestParams.get(key);
      RequestParameterValue requestParameterValue = new RequestParameterValue();
      if (value instanceof String) {
        requestParameterValue.setJsonString((String) value);
      } else if (value instanceof DataMap) {
        // There are two variations for the DataMap case: 1) an MVEL expression that contains a single entry
        // keyed by "mvel" and 2) a standard static json object
        DataMap paramDataMap = (DataMap) value;
        if (paramDataMap.containsKey(RestliConfig.MVEL_KEY)) {
          MvelExpression expression = new MvelExpression();
          expression.setMvel(paramDataMap.getString(RestliConfig.MVEL_KEY));
          requestParameterValue.setMvelExpression(expression);
        } else {
          requestParameterValue.setJsonString(new JSONObject((DataMap) value).toString());
        }
      } else if (value instanceof List) {
        requestParameterValue.setJsonString(new JSONArray((List) value).toString());
      } else {
        throw new IllegalArgumentException("Invalid request param value type " + value);
      }
      requestParameterValueMap.put(key, requestParameterValue);
    }
    return new RequestParameterValueMap(requestParameterValueMap);
  }
}
