package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.data.template.StringArray;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.PinotConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.MvelExpression;
import com.linkedin.feathr.featureDataModel.MvelExpressionArray;
import com.linkedin.feathr.featureDataModel.PinotDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource;
import java.util.Arrays;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

/**
 * Build a {@link FeatureAnchor.Anchor} with {@link PinotDataSourceAnchor} from {@link PinotConfig}
 */
public class PinotAnchorBuilder implements AnchorBuilder {
  private PinotConfig _pinotConfig;
  private final FeatureConfig _featureConfig;
  private final AnchorConfig _anchorConfig;
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunctionForOnlineDataSource.TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  public PinotAnchorBuilder(@Nonnull PinotConfig pinotConfig, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig,
      @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunctionForOnlineDataSource.TransformationFunction>
          anchoredFeatureTransformationFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder) {
    Preconditions.checkNotNull(pinotConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    _pinotConfig = pinotConfig;
    _featureConfig = featureConfig;
    _anchorConfig = anchorConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
  }

  public FeatureAnchor.Anchor build() {
    PinotDataSourceAnchor pinotAnchor = new PinotDataSourceAnchor();
    PinotDataSource pinotDataSource = new PinotDataSource();
    pinotDataSource.setResourceName(_pinotConfig.getResourceName());
    pinotDataSource.setQueryTemplate(_pinotConfig.getQueryTemplate());
    MvelExpressionArray queryArguments = new MvelExpressionArray(Arrays.stream(_pinotConfig.getQueryArguments()).map(
        s -> new MvelExpression().setMvel(s)).collect(Collectors.toList()));
    pinotDataSource.setQueryArguments(queryArguments);
    StringArray queryKeyColumns = new StringArray(Arrays.asList(_pinotConfig.getQueryKeyColumns()));
    pinotDataSource.setQueryKeyColumns(queryKeyColumns);
    pinotAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    pinotDataSource.setDataSourceRef(_pinotConfig.getSourceName());
    pinotAnchor.setSource(pinotDataSource);
    pinotAnchor.setTransformationFunction(_anchoredFeatureTransformationFunctionBuilder.build(_featureConfig,
        _anchorConfig));
    FeatureAnchor.Anchor anchor = new FeatureAnchor.Anchor();
    anchor.setPinotDataSourceAnchor(pinotAnchor);
    return anchor;
  }
}


