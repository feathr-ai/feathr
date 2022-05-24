package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.data.DataMap;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class InMemoryPassthroughAnchorBuilderTest {
  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchoredFeatureTransformationFunctionBuilder);
  }

  @Test
  public void testBuildWithMvelFunction() {
    PassThroughConfig sourceConfig = new PassThroughConfig("sourceName", "com.linkedin.some.service.SomeEntity");
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    InMemoryPassthroughAnchorBuilder inMemoryPassthroughAnchorBuilder = new InMemoryPassthroughAnchorBuilder(sourceConfig,
        featureConfig, anchorConfig, _anchoredFeatureTransformationFunctionBuilder);
    Anchor anchor = inMemoryPassthroughAnchorBuilder.build();
    InMemoryPassthroughDataSource inMemoryPassthroughDataSource = anchor.getInMemoryPassthroughDataSourceAnchor().getSource();
    Assert.assertEquals(inMemoryPassthroughDataSource.getDataModel().getFullyQualifiedName(), "com.linkedin.some.service.SomeEntity");
    Assert.assertEquals(inMemoryPassthroughDataSource.getDataSourceRef(), "sourceName");
    assertEquals(anchor.getInMemoryPassthroughDataSourceAnchor().getTransformationFunction(), transformationFunction);
  }
}
