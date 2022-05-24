package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.frame.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.frame.config.featureanchor.builder.transformation.TransformationFunctionExpressionBuilder;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.anchors.FeatureConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.frame.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.frame.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.frame.core.config.producer.sources.CustomSourceConfig;
import com.linkedin.frame.core.config.producer.sources.EspressoConfig;
import com.linkedin.frame.core.config.producer.sources.HdfsConfig;
import com.linkedin.frame.core.config.producer.sources.HdfsConfigWithRegularData;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.frame.core.config.producer.sources.PinotConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.frame.core.config.producer.sources.VeniceConfig;
import com.linkedin.frame.core.utils.DerivedFeatureDependencyResolver;
import com.linkedin.frame.core.utils.MlFeatureVersionUrnCreator;
import java.util.HashMap;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class AnchorBuilderFactoryTest {

  private AnchorBuilderFactory _anchorBuilderFactory;
  @Mock
  private DerivedFeatureDependencyResolver _derivedFeatureDependencyResolver;
  @Mock
  private MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator;
  @Mock
  private TransformationFunctionExpressionBuilder _transformationFunctionExpressionBuilder;

  @BeforeMethod
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    _anchorBuilderFactory = new AnchorBuilderFactory(_derivedFeatureDependencyResolver, _mlFeatureVersionUrnCreator,
        _transformationFunctionExpressionBuilder);
  }

  @AfterMethod
  public void afterTest() {
    reset(_derivedFeatureDependencyResolver);
  }

  @Test
  public void testGetHdfsBuilder() {
    HdfsConfig hdfsConfig = new HdfsConfigWithRegularData("dummySource", "path", false);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(hdfsConfig, featureConfig, anchorConfig) instanceof HdfsAnchorBuilder);
  }

  @Test
  public void testGetVeniceBuilder() {
    VeniceConfig veniceConfig = new VeniceConfig("dummySource", "store", "key");
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(veniceConfig, featureConfig, anchorConfig) instanceof VeniceAnchorBuilder);
  }

  @Test
  public void testGetEspressoBuilder() {
    EspressoConfig espressoConfig = new EspressoConfig("dummySource", "database", "table", "d2", "key");
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(espressoConfig, featureConfig, anchorConfig) instanceof EspressoAnchorBuilder);
  }

  @Test
  public void testGetRestliBuilder() {
    RestliConfig restliConfig = new RestliConfig("dummySource", "resource", "key", new HashMap<>(), null);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(restliConfig, featureConfig, anchorConfig) instanceof RestliAnchorBuilder);
  }

  @Test
  public void testGetRestliFinderBuilder() {
    RestliConfig restliConfig = new RestliConfig("dummySource", "resource",  new HashMap<>(), null, "testFinder");
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(restliConfig, featureConfig, anchorConfig) instanceof RestliFinderAnchorBuilder);
  }

  @Test
  public void testGetCouchbaseBuilder() {
    CouchbaseConfig couchbaseConfig = new CouchbaseConfig("dummySource", "bucket", "key", "dataModel");
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(couchbaseConfig, featureConfig, anchorConfig) instanceof CouchbaseAnchorBuilder);
  }

  @Test
  public void testGetCustomSourceBuilder() {
    CustomSourceConfig restliConfig = new CustomSourceConfig("dummySource", "key[0]", "String");
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(restliConfig, featureConfig, anchorConfig) instanceof CustomSourceAnchorBuilder);
  }

  @Test
  public void testGetInMemoryPassthroughBuilder() {
    PassThroughConfig passThroughConfig = new PassThroughConfig("dummySource", "dataModel");
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(passThroughConfig, featureConfig, anchorConfig) instanceof InMemoryPassthroughAnchorBuilder);
  }

  @Test
  public void testGetObservationPassthroughBuilder() {
    PassThroughConfig passThroughConfig = new PassThroughConfig("dummySource", null);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(passThroughConfig, featureConfig, anchorConfig) instanceof ObservationPassthroughAnchorBuilder);
  }

  @Test
  public void testGetHdfsAnchorBuilderByInlineSource() {
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getHdfsAnchorBuilderByInlineSource("inlineHdfs", anchorConfig, featureConfig) instanceof HdfsAnchorBuilder);
  }

  @Test
  public void testGetOnlineFeatureSourcesAnchorBuilder() {
    DerivationConfigWithExpr derivationConfig = mock(DerivationConfigWithExpr.class);
    assertTrue(_anchorBuilderFactory.getFeatureSourcesAnchorBuilder(derivationConfig, FeatureAnchorEnvironment.ONLINE) instanceof OnlineFeatureSourcesAnchorBuilder);
  }

  @Test
  public void testGetOfflineFeatureSourcesAnchorBuilder() {
    DerivationConfigWithExtractor derivationConfig = mock(DerivationConfigWithExtractor.class);
    assertTrue(_anchorBuilderFactory.getFeatureSourcesAnchorBuilder(derivationConfig, FeatureAnchorEnvironment.OFFLINE) instanceof OfflineFeatureSourcesAnchorBuilder);
  }

  @Test
  public void testGetCrossEnvironmentFeatureSourcesAnchorBuilder() {
    SimpleDerivationConfig derivationConfig = mock(SimpleDerivationConfig.class);
    assertTrue(_anchorBuilderFactory.getFeatureSourcesAnchorBuilder(derivationConfig, FeatureAnchorEnvironment.CROSS_ENVIRONMENT) instanceof CrossEnvironmentFeatureSourcesAnchorBuilder);
  }

  @Test
  public void testGetSequentialJoinFeatureSourcesAnchorBuilder() {
    SequentialJoinConfig sequentialJoinConfig = mock(SequentialJoinConfig.class);
    assertTrue(_anchorBuilderFactory.getFeatureSourcesAnchorBuilder(sequentialJoinConfig, FeatureAnchorEnvironment.ONLINE) instanceof SequentialJoinFeatureSourcesAnchorBuilder);
  }

  @Test
  public void testGetPinotBuilder() {
    PinotConfig pinotConfig = new PinotConfig("dummyPinotSource", "testSource",
        "select * from testTable where actorId IN (?)", new String[]{"key[0]"}, new String[]{"actorId"});
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    FeatureConfig featureConfig = mock(FeatureConfig.class);
    assertTrue(_anchorBuilderFactory.getDataSourceAnchorBuilder(pinotConfig, featureConfig, anchorConfig) instanceof PinotAnchorBuilder);
  }
}
