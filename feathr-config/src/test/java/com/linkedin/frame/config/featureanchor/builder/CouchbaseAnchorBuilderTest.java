package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.frame.config.featureanchor.builder.key.KeyFunctionBuilder;
import com.linkedin.frame.config.featureanchor.builder.key.KeyPlaceholdersBuilder;
import com.linkedin.frame.config.featureanchor.builder.transformation.AnchoredFeatureTransformationFunctionBuilder;
import com.linkedin.frame.config.utils.UriUtil;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor;
import com.linkedin.feathr.featureDataModel.Clazz;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey;
import com.linkedin.feathr.featureDataModel.CouchbaseDataSource;
import com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction;
import java.net.URI;
import java.util.List;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class CouchbaseAnchorBuilderTest {
  @Mock
  private AnchoredFeatureTransformationFunctionBuilder _anchoredFeatureTransformationFunctionBuilder;

  @Mock
  private KeyFunctionBuilder _keyFunctionBuilder;

  @Mock
  private KeyPlaceholdersBuilder _keyPlaceholdersBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @AfterMethod
  public void afterTest() {
    reset(_anchoredFeatureTransformationFunctionBuilder, _keyPlaceholdersBuilder, _keyFunctionBuilder);
  }

  @Test
  public void testBuild() {
    String bucketName = "testBucket";
    String keyExpr = "key[0]";
    String[] bootstrapUris = new String[] {"some-app.linkedin.com:8091", "other-app.linkedin.com:8091"};
    String documentModel = "com.linkedin.some.Document";
    CouchbaseConfig sourceConfig = new CouchbaseConfig("dummySource", bucketName, keyExpr, documentModel);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    CouchbaseAnchorBuilder couchbaseAnchorBuilder = new CouchbaseAnchorBuilder(sourceConfig, featureConfig,
        anchorConfig, _anchoredFeatureTransformationFunctionBuilder,  _keyFunctionBuilder,
        _keyPlaceholdersBuilder);
    TransformationFunction transformationFunction = new TransformationFunction();
    when(_anchoredFeatureTransformationFunctionBuilder.build(featureConfig, anchorConfig)).thenReturn(
        transformationFunction);
    OnlineDataSourceKey.KeyFunction keyFunction = new OnlineDataSourceKey.KeyFunction();
    when(_keyFunctionBuilder.build()).thenReturn(keyFunction);
    KeyPlaceholderArray keyPlaceholders = new KeyPlaceholderArray();
    when(_keyPlaceholdersBuilder.build()).thenReturn(keyPlaceholders);
    Anchor anchor = couchbaseAnchorBuilder.build();

    CouchbaseDataSource couchbaseDataSource = anchor.getCouchbaseDataSourceAnchor().getSource();
    assertEquals(couchbaseDataSource.getBucketName(), bucketName);
    assertEquals(couchbaseDataSource.getDataSourceRef(), "dummySource");
    Clazz modelClazz = couchbaseDataSource.getDocumentDataModel();
    assertEquals(modelClazz.getFullyQualifiedName(), documentModel);
    assertEquals(anchor.getCouchbaseDataSourceAnchor().getTransformationFunction(), transformationFunction);
    assertEquals(anchor.getCouchbaseDataSourceAnchor().getKeyPlaceholders(), keyPlaceholders);
    assertEquals(anchor.getCouchbaseDataSourceAnchor().getSource().getKeyFunction(), keyFunction);
  }
}
