package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.PinotConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.PinotDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource;
import java.util.Arrays;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;

public class PinotAnchorBuilderTest {
  private final String DUMMY_SOURCE = "dummySource";
  private final String DUMMY_PINOT_RESOURCE = "dummyPinotTableQuery";
  private final String DUMMY_QUERY_TEMPLATE = "select * from testTable where actorId IN (?) and object IN (?)";
  private final String[] DUMMY_QUERY_ARGUMENTS = new String[]{"key[0]", "key[1]"};
  private final String[] DUMMY_QUERY_KEY_COLUMNS = new String[]{"actorId", "object"};

  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;

  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder);
  }

  @Test
  public void testBuild() {
    PinotConfig sourceConfig = new PinotConfig(DUMMY_SOURCE, DUMMY_PINOT_RESOURCE, DUMMY_QUERY_TEMPLATE,
        DUMMY_QUERY_ARGUMENTS, DUMMY_QUERY_KEY_COLUMNS);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunctionForOnlineDataSource.TransformationFunction
        transformationFunction = new TransformationFunctionForOnlineDataSource.TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction
    );
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    PinotAnchorBuilder pinotAnchorBuilder = new PinotAnchorBuilder(sourceConfig, featureConfig, anchorConfig,
        _anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder);
    FeatureAnchor.Anchor anchor = pinotAnchorBuilder.build();
    PinotDataSource pinotDataSource = anchor.getPinotDataSourceAnchor().getSource();

    assertEquals(pinotDataSource.getResourceName(), DUMMY_PINOT_RESOURCE);
    assertEquals(pinotDataSource.getQueryTemplate(), DUMMY_QUERY_TEMPLATE);
    assertTrue(Arrays.equals(pinotDataSource.getQueryArguments().stream().map(s -> s.getMvel()).toArray(String[]::new), DUMMY_QUERY_ARGUMENTS));
    assertTrue(Arrays.equals(pinotDataSource.getQueryKeyColumns().toArray(), DUMMY_QUERY_KEY_COLUMNS));
    assertEquals(pinotDataSource.getDataSourceRef(), DUMMY_SOURCE);

    assertEquals(anchor.getPinotDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getPinotDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
  }
}