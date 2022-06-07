package com.linkedin.feathr.config.featureanchor.builder;

import com.linkedin.feathr.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.sources.PassThroughConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class ObservationPassthroughAnchorBuilderTest {
  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;

  @Mock
  private KeyFunctionBuilder<KeyFunction> _keyFunctionBuilder;

  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder, _keyPlaceholdersBuilder);
  }

  @Test
  public void testBuild() {
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    PassThroughConfig passThroughConfig = new PassThroughConfig("dummySource", null);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    ObservationPassthroughAnchorBuilder observationPassthroughAnchorBuilder = new ObservationPassthroughAnchorBuilder(
        passThroughConfig, featureConfig, anchorConfig, _anchoredFeatureTransformationFunctionBuilder, _keyFunctionBuilder,
        _keyPlaceholdersBuilder);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    KeyFunction keyFunction = new KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    FeatureAnchor.Anchor anchor = observationPassthroughAnchorBuilder.build();
    assertEquals(anchor.getObservationPassthroughDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getObservationPassthroughDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getObservationPassthroughDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
    assertEquals(anchor.getObservationPassthroughDataSourceAnchor().getSource().getDataSourceRef(), passThroughConfig.getSourceName());
  }
}
