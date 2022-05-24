package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilderFactory;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilderFactory;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.DerivedFeatureTransformationFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.TransformationFunctionBuilderFactory;
import com.linkedin.frame.config.featureanchor.builder.transformation.TransformationFunctionExpressionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.frame.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.frame.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.frame.core.config.producer.sources.CustomSourceConfig;
import com.linkedin.frame.core.config.producer.sources.EspressoConfig;
import com.linkedin.frame.core.config.producer.sources.HdfsConfig;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.frame.core.config.producer.sources.PinotConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import com.linkedin.frame.core.config.producer.sources.VeniceConfig;
import com.linkedin.frame.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.frame.core.utils.MlFeatureVersionUrnCreator;
import javax.annotation.Nonnull;


/**
 * AnchorBuilderFactory returns a specific {@link AnchorBuilder} based on inputs.
 */
public class AnchorBuilderFactory {

  private final DerivedFeatureDependencyResolver _derivedFeatureDependencyResolver;
  private final MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  private final TransformationFunctionBuilderFactory _transformationFunctionBuilderFactory;
  private final KeyFunctionBuilderFactory _keyFunctionBuilderFactory;
  private final KeyPlaceholdersBuilderFactory _keyPlaceholdersBuilderFactory;

  public AnchorBuilderFactory(@Nonnull DerivedFeatureDependencyResolver derivedFeatureDependencyResolver,
       @Nonnull MlFeatureVersionUrnCreator mlFeatureVersionUrnCreator, @Nonnull TransformationFunctionExpressionBuilder
      transformationFunctionExpressionBuilder) {
    Preconditions.checkNotNull(derivedFeatureDependencyResolver);
    Preconditions.checkNotNull(mlFeatureVersionUrnCreator);
    Preconditions.checkNotNull(transformationFunctionExpressionBuilder);
    _derivedFeatureDependencyResolver = derivedFeatureDependencyResolver;
    _mlFeatureVersionUrnCreator = mlFeatureVersionUrnCreator;
    _transformationFunctionBuilderFactory = new TransformationFunctionBuilderFactory(transformationFunctionExpressionBuilder);
    _keyFunctionBuilderFactory = new KeyFunctionBuilderFactory();
    _keyPlaceholdersBuilderFactory = new KeyPlaceholdersBuilderFactory();
  }

  /**
   * Gets a {@link AnchorBuilder} to build anchored feature, in which source is from a raw data source.
   */
  public AnchorBuilder getDataSourceAnchorBuilder(@Nonnull SourceConfig sourceConfig, @Nonnull FeatureConfig
      featureConfig, @Nonnull AnchorConfig anchorConfig) {
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder
        = _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(
        sourceConfig, anchorConfig);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
    switch (sourceConfig.getSourceType()) {
      case HDFS:
        return new HdfsAnchorBuilder((HdfsConfig) sourceConfig, featureConfig, anchorConfig,
            anchoredFeatureTransformationFunctionBuilder, keyFunctionBuilder, keyPlaceHoldersBuilder,
            DatasetSnapshotTimeFormatBuilder.getInstance());
      case COUCHBASE:
        return new CouchbaseAnchorBuilder((CouchbaseConfig) sourceConfig, featureConfig, anchorConfig,
            anchoredFeatureTransformationFunctionBuilder, keyFunctionBuilder, keyPlaceHoldersBuilder);
      case ESPRESSO:
        return new EspressoAnchorBuilder((EspressoConfig) sourceConfig, featureConfig, anchorConfig,
            anchoredFeatureTransformationFunctionBuilder, keyFunctionBuilder, keyPlaceHoldersBuilder);
      case RESTLI:
        if (((RestliConfig) sourceConfig).getFinder().isPresent()) {
          return new RestliFinderAnchorBuilder((RestliConfig) sourceConfig, featureConfig, anchorConfig,
              anchoredFeatureTransformationFunctionBuilder, keyPlaceHoldersBuilder);
        } else {
          return new RestliAnchorBuilder((RestliConfig) sourceConfig, featureConfig, anchorConfig,
              anchoredFeatureTransformationFunctionBuilder, keyFunctionBuilder, keyPlaceHoldersBuilder);
        }
      case VENICE:
        return new VeniceAnchorBuilder((VeniceConfig) sourceConfig, featureConfig, anchorConfig,
            anchoredFeatureTransformationFunctionBuilder, keyFunctionBuilder, keyPlaceHoldersBuilder);
      case CUSTOM:
        return new CustomSourceAnchorBuilder((CustomSourceConfig) sourceConfig, featureConfig, anchorConfig,
            anchoredFeatureTransformationFunctionBuilder, keyFunctionBuilder, keyPlaceHoldersBuilder);
      case PASSTHROUGH:
        return ((PassThroughConfig) sourceConfig).getDataModel().isPresent()
            ? new InMemoryPassthroughAnchorBuilder((PassThroughConfig) sourceConfig, featureConfig, anchorConfig,
            anchoredFeatureTransformationFunctionBuilder)
            : new ObservationPassthroughAnchorBuilder((PassThroughConfig) sourceConfig, featureConfig, anchorConfig,
                anchoredFeatureTransformationFunctionBuilder, keyFunctionBuilder, keyPlaceHoldersBuilder);
      case PINOT:
        return new PinotAnchorBuilder((PinotConfig) sourceConfig, featureConfig, anchorConfig,
            anchoredFeatureTransformationFunctionBuilder, keyPlaceHoldersBuilder);
      case KAFKA:
      case ROCKSDB:
      default:
        throw new IllegalArgumentException(String.format("source type %s is not supported",
            sourceConfig.getSourceType().toString()));
    }
  }

  /**
   * Gets a {@link AnchorBuilder} for a special treatment in frame-offline, which supports passing in a HDFS/Dali path directly in source field.
   * For more details, see: https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-SimpleCase
   */

  public AnchorBuilder getHdfsAnchorBuilderByInlineSource(@Nonnull String hdfsDatasetPath,
      @Nonnull AnchorConfig anchorConfig, @Nonnull FeatureConfig featureConfig) {
    return new HdfsAnchorBuilder(hdfsDatasetPath, featureConfig, anchorConfig,
        _transformationFunctionBuilderFactory.getTransformationFunctionBuilderForInlineHdfsSource(),
        _keyFunctionBuilderFactory.getKeyFunctionBuilderForInlineHdfsSource(anchorConfig),
        _keyPlaceholdersBuilderFactory.getKeyPlaceholdersForInlineHdfsSource(anchorConfig),
        DatasetSnapshotTimeFormatBuilder.getInstance());
  }

  /**
   * Gets a {@link AnchorBuilder} to build derived feature, in which source is from other features.
   */
  public AnchorBuilder getFeatureSourcesAnchorBuilder(@Nonnull DerivationConfig derivationConfig,
      FeatureAnchorEnvironment anchorEnvironment) {
    KeyPlaceholdersBuilder keyPlaceholdersBuilder = _keyPlaceholdersBuilderFactory
        .getDerivedFeatureKeyPlaceholdersBuilder(derivationConfig);
    if (derivationConfig instanceof DerivationConfigWithExpr
        || derivationConfig instanceof DerivationConfigWithExtractor
        || derivationConfig instanceof SimpleDerivationConfig) {
      DerivedFeatureTransformationFunctionBuilder derivedFeatureTransformationFunctionBuilder =
          _transformationFunctionBuilderFactory.getDerivedFeatureTransformationFunctionBuilder(anchorEnvironment);
      switch (anchorEnvironment) {
        case ONLINE:
          return new OnlineFeatureSourcesAnchorBuilder(derivationConfig, _derivedFeatureDependencyResolver,
              derivedFeatureTransformationFunctionBuilder, keyPlaceholdersBuilder);
        case OFFLINE:
          return new OfflineFeatureSourcesAnchorBuilder(derivationConfig, _derivedFeatureDependencyResolver,
              derivedFeatureTransformationFunctionBuilder, keyPlaceholdersBuilder);
        case CROSS_ENVIRONMENT:
          return new CrossEnvironmentFeatureSourcesAnchorBuilder(derivationConfig, _derivedFeatureDependencyResolver,
              derivedFeatureTransformationFunctionBuilder, keyPlaceholdersBuilder);

        default:
          throw new IllegalArgumentException("Invalid anchor environment: " + anchorEnvironment);
      }
    } else if (derivationConfig instanceof SequentialJoinConfig) {
      return new SequentialJoinFeatureSourcesAnchorBuilder((SequentialJoinConfig) derivationConfig,
          _mlFeatureVersionUrnCreator, keyPlaceholdersBuilder);
    } else {
      throw new IllegalArgumentException("Invalid derivation config type: " + derivationConfig.getClass());
    }
  }
}
