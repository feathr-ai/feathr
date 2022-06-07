package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.HdfsConfigWithRegularData;
import com.linkedin.feathr.core.config.producer.sources.HdfsConfigWithSlidingWindow;
import com.linkedin.feathr.core.config.producer.sources.SlidingWindowAggrConfig;
import com.linkedin.feathr.core.config.producer.sources.TimeWindowParams;
import com.linkedin.feathr.featureDataModel.HdfsLocation;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OfflineDataSourceKey;
import com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat;
import com.linkedin.feathr.featureDataModel.HdfsDataSource;
import com.linkedin.feathr.featureDataModel.TimeField;
import java.util.Optional;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class HdfsAnchorBuilderTest {
  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;

  @Mock
  private KeyFunctionBuilder _keyFunctionBuilder;

  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @Mock
  private DatasetSnapshotTimeFormatBuilder _datasetSnapshotTimeFormatBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder, _keyFunctionBuilder, _datasetSnapshotTimeFormatBuilder);
  }

  @Test
  public void testBuildWithSourceConfig() {
    String path = "/eidata/derived/standardization/waterloo/members_std_data/#LATEST";
    HdfsConfigWithRegularData sourceConfig = new HdfsConfigWithRegularData("dummySource", path,false);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    DatasetSnapshotTimeFormat datasetSnapshotTimeFormat = new DatasetSnapshotTimeFormat();
    when(_datasetSnapshotTimeFormatBuilder.build(sourceConfig)).thenReturn(Optional.of(datasetSnapshotTimeFormat));
    HdfsAnchorBuilder hdfsAnchorBuilder = new HdfsAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder,  _keyFunctionBuilder, _keyPlaceholdersBuilder, _datasetSnapshotTimeFormatBuilder);
    OfflineDataSourceKey.KeyFunction keyFunction = new OfflineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    Anchor anchor = hdfsAnchorBuilder.build();
    HdfsDataSource hdfsDataSource = anchor.getHdfsDataSourceAnchor().getSource();
    assertTrue(hdfsDataSource.getDatasetLocation().isHdfsLocation());
    assertEquals(hdfsDataSource.getDataSourceRef(), "dummySource");
    HdfsLocation hdfsLocation = hdfsDataSource.getDatasetLocation().getHdfsLocation();
    assertEquals(hdfsLocation.getPath(), path);
    assertEquals(anchor.getHdfsDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getHdfsDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getHdfsDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
    assertEquals(anchor.getHdfsDataSourceAnchor().getSource().getDatasetSnapshotTimeFormat(), datasetSnapshotTimeFormat);
  }



  @Test
  public void testBuildWithInlinePath() {
    String path = "/eidata/derived/standardization/waterloo/members_std_data/#LATEST";
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    OfflineDataSourceKey.KeyFunction keyFunction = new OfflineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    HdfsAnchorBuilder hdfsAnchorBuilder = new HdfsAnchorBuilder(path, featureConfig, anchorConfig, _anchoredFeatureTransformationFunctionBuilder,
        _keyFunctionBuilder, _keyPlaceholdersBuilder, _datasetSnapshotTimeFormatBuilder);
    Anchor anchor = hdfsAnchorBuilder.build();
    HdfsDataSource hdfsDataSource = anchor.getHdfsDataSourceAnchor().getSource();
    assertTrue(hdfsDataSource.getDatasetLocation().isHdfsLocation());
    // for inline hdfs sources, no data source ref is present
    assertFalse(hdfsDataSource.hasDataSourceRef());
    HdfsLocation hdfsLocation = hdfsDataSource.getDatasetLocation().getHdfsLocation();
    assertEquals(hdfsLocation.getPath(), path);
    assertEquals(anchor.getHdfsDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getHdfsDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getHdfsDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
    assertFalse(anchor.getHdfsDataSourceAnchor().getSource().hasDatasetSnapshotTimeFormat());
  }

  @Test
  public void testHdfsDataSourceWithSlidingWindowParams() {
    String path = "/testPath";
    TimeWindowParams timeWindowParams = new TimeWindowParams("testField", "yyyy/MM/dd/HH/mm/ss");
    SlidingWindowAggrConfig slidingWindowAggrConfig = new SlidingWindowAggrConfig(true, timeWindowParams);
    HdfsConfigWithSlidingWindow sourceConfig = new HdfsConfigWithSlidingWindow("dummySource", path, slidingWindowAggrConfig);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    OfflineDataSourceKey.KeyFunction keyFunction = new OfflineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    DatasetSnapshotTimeFormat datasetSnapshotTimeFormat = new DatasetSnapshotTimeFormat();
    when(_datasetSnapshotTimeFormatBuilder.build(sourceConfig)).thenReturn(Optional.of(datasetSnapshotTimeFormat));
    Anchor anchor = new HdfsAnchorBuilder(sourceConfig, featureConfig, anchorConfig, _anchoredFeatureTransformationFunctionBuilder,
        _keyFunctionBuilder, _keyPlaceholdersBuilder, _datasetSnapshotTimeFormatBuilder).build();
    HdfsDataSource hdfsDataSource = anchor.getHdfsDataSourceAnchor().getSource();
    HdfsLocation hdfsLocation = hdfsDataSource.getDatasetLocation().getHdfsLocation();
    assertEquals(hdfsDataSource.getDatasetSnapshotTimeFormat(), datasetSnapshotTimeFormat);
    assertEquals(hdfsDataSource.getDataSourceRef(), "dummySource");
    assertEquals(hdfsLocation.getPath(), path);
    TimeField timeField = hdfsDataSource.getTimeField();
    assertEquals(timeField.getName(), "testField");
    assertEquals(timeField.getFormat().getDateTimeFormat(), "yyyy/MM/dd/HH/mm/ss");
  }
}

