package com.linkedin.feathr.config.featureanchor.builder;

import com.google.common.collect.ImmutableList;
import com.linkedin.data.DataList;
import com.linkedin.feathr.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.feathr.config.featureanchor.builder.transformation.DerivedFeatureTransformationFunctionBuilder;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.TransformationFunction;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.FeatureSource;
import java.net.URISyntaxException;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class CrossEnvironmentFeatureSourcesAnchorBuildTest {

  @Mock
  private DerivedFeatureTransformationFunctionBuilder _derivedFeatureTransformationFunctionBuilder;

  @Mock
  private DerivedFeatureDependencyResolver _derivedFeatureDependencyResolver;

  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_derivedFeatureTransformationFunctionBuilder, _derivedFeatureDependencyResolver,  _keyPlaceholdersBuilder);
  }

  @Test
  public void testBuild() throws URISyntaxException {
    //mock
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    FeatureSource featureSource1 = new FeatureSource();
    FeatureSource featureSource2 = new FeatureSource();
    List<FeatureSource> featureSources = ImmutableList.of(featureSource1, featureSource2);
    when(_derivedFeatureDependencyResolver.getDependentFeatures(eq(derivationConfig))).thenReturn(featureSources);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_derivedFeatureTransformationFunctionBuilder.build(derivationConfig)).thenReturn(transformationFunction);
    //trigger
    KeyPlaceholderArray keyPlaceholderArray = mock(KeyPlaceholderArray.class);
    when(keyPlaceholderArray.data()).thenReturn(mock(DataList.class));
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholderArray);
    //trigger
    CrossEnvironmentFeatureSourcesAnchorBuilder builder = new CrossEnvironmentFeatureSourcesAnchorBuilder(
        derivationConfig, _derivedFeatureDependencyResolver, _derivedFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder);
    Anchor anchor = builder.build();

    //assertion
    assertEquals(anchor.getCrossEnvironmentFeatureSourcesAnchor().getSource(), featureSources);
    assertEquals(anchor.getCrossEnvironmentFeatureSourcesAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getCrossEnvironmentFeatureSourcesAnchor().getKeyPlaceholders(), keyPlaceholderArray);
  }
}
