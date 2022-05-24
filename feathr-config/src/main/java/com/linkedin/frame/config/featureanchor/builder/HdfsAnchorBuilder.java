package com.linkedin.frame.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.config.utils.UriUtil;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.HdfsConfig;
import com.linkedin.frame.core.config.producer.sources.HdfsConfigWithSlidingWindow;
import com.linkedin.frame.core.config.producer.sources.TimeWindowParams;
import com.linkedin.feathr.featureDataModel.DaliLocation;
import com.linkedin.feathr.featureDataModel.HdfsLocation;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor;
import com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction;
import com.linkedin.feathr.featureDataModel.HdfsDataSource;
import java.util.Optional;
import javax.annotation.Nonnull;


class HdfsAnchorBuilder implements AnchorBuilder {

  private final String _hdfsLocationPath;
  private final AnchorConfig _anchorConfig;
  private final FeatureConfig _featureConfig;
  private final AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      _anchoredFeatureTransformationFunctionBuilder;
  private final KeyFunctionBuilder<KeyFunction> _keyFunctionBuilder;
  private final KeyPlaceholdersBuilder _keyPlaceholdersBuilder;
  private final DatasetSnapshotTimeFormatBuilder _datasetSnapshotTimeFormatBuilder;
  private final Optional<TimeWindowParams> _timeWindowParams;
  private final Optional<HdfsConfig> _hdfsConfig;

  public HdfsAnchorBuilder(@Nonnull HdfsConfig hdfsConfig, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      anchoredFeatureTransformationFunctionBuilder, @Nonnull KeyFunctionBuilder<KeyFunction> keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder,
      @Nonnull DatasetSnapshotTimeFormatBuilder datasetSnapshotTimeFormatBuilder) {
    Preconditions.checkNotNull(hdfsConfig);
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    Preconditions.checkNotNull(datasetSnapshotTimeFormatBuilder);
    _hdfsConfig = Optional.of(hdfsConfig);
    _hdfsLocationPath = hdfsConfig.getPath();
    _anchorConfig = anchorConfig;
    _featureConfig = featureConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = keyFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
    _datasetSnapshotTimeFormatBuilder = datasetSnapshotTimeFormatBuilder;
    if (hdfsConfig instanceof HdfsConfigWithSlidingWindow) {
      _timeWindowParams = Optional.of(((HdfsConfigWithSlidingWindow) hdfsConfig).getSwaConfig().getTimeWindowParams());
    } else {
      _timeWindowParams = Optional.empty();
    }
  }

  /**
   * Frame-offline supports passing in a HDFS/Dali path directly in source field.
   * See: https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-SimpleCase
   */

  public HdfsAnchorBuilder(@Nonnull String hdfsLocationPath, @Nonnull FeatureConfig featureConfig,
      @Nonnull AnchorConfig anchorConfig, @Nonnull AnchoredFeatureTransformationFunctionBuilder<TransformationFunction>
      anchoredFeatureTransformationFunctionBuilder, @Nonnull KeyFunctionBuilder keyFunctionBuilder,
      @Nonnull KeyPlaceholdersBuilder keyPlaceholdersBuilder,
      @Nonnull DatasetSnapshotTimeFormatBuilder datasetSnapshotTimeFormatBuilder) {
    Preconditions.checkNotNull(hdfsLocationPath);
    Preconditions.checkNotNull(anchorConfig);
    Preconditions.checkNotNull(featureConfig);
    Preconditions.checkNotNull(anchoredFeatureTransformationFunctionBuilder);
    Preconditions.checkNotNull(keyFunctionBuilder);
    Preconditions.checkNotNull(keyPlaceholdersBuilder);
    Preconditions.checkNotNull(datasetSnapshotTimeFormatBuilder);
    _hdfsConfig = Optional.empty();
    _hdfsLocationPath = hdfsLocationPath;
    _anchorConfig = anchorConfig;
    _featureConfig = featureConfig;
    _anchoredFeatureTransformationFunctionBuilder = anchoredFeatureTransformationFunctionBuilder;
    _keyFunctionBuilder = keyFunctionBuilder;
    _keyPlaceholdersBuilder = keyPlaceholdersBuilder;
    _datasetSnapshotTimeFormatBuilder = datasetSnapshotTimeFormatBuilder;
    _timeWindowParams = Optional.empty();
  }

  public Anchor build() {
    HdfsDataSourceAnchor hdfsAnchor = new HdfsDataSourceAnchor();
    hdfsAnchor.setSource(buildHdfsDataSource());
    hdfsAnchor.setTransformationFunction(_anchoredFeatureTransformationFunctionBuilder.build(_featureConfig,
        _anchorConfig));
    hdfsAnchor.setKeyPlaceholders(_keyPlaceholdersBuilder.build());
    Anchor anchor = new Anchor();
    anchor.setHdfsDataSourceAnchor(hdfsAnchor);
    return anchor;
  }

  private HdfsDataSource buildHdfsDataSource() {
    HdfsDataSource hdfsDataSource = new HdfsDataSource();
    _hdfsConfig.flatMap(_datasetSnapshotTimeFormatBuilder::build)
        .ifPresent(hdfsDataSource::setDatasetSnapshotTimeFormat);
    _timeWindowParams.ifPresent(
        timeWindowParams -> hdfsDataSource.setTimeField(new HdfsTimeFieldBuilder(timeWindowParams).build())
    );
    HdfsDataSource.DatasetLocation datasetLocation = new HdfsDataSource.DatasetLocation();
    if (_hdfsLocationPath.startsWith("dalids")) {
      DaliLocation daliLocation = new DaliLocation();
      daliLocation.setUri(UriUtil.buildUri(_hdfsLocationPath));
      datasetLocation.setDaliLocation(daliLocation);
    } else {
      HdfsLocation hdfsLocation = new HdfsLocation();
      hdfsLocation.setPath(_hdfsLocationPath);
      datasetLocation.setHdfsLocation(hdfsLocation);
    }
    hdfsDataSource.setDatasetLocation(datasetLocation);
    hdfsDataSource.setKeyFunction(_keyFunctionBuilder.build());
    // Set the source name from the original config if it was a source object, otherwise, leave it empty
    _hdfsConfig.ifPresent(hdfsConfig -> hdfsDataSource.setDataSourceRef(hdfsConfig.getSourceName()));

    return hdfsDataSource;
  }
}
