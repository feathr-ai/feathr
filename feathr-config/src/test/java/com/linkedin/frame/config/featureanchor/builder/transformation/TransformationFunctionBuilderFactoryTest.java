package com.linkedin.frame.config.featureanchor.builder.transformation;

import com.linkedin.frame.config.featureanchor.FeatureAnchorEnvironment;
import com.linkedin.frame.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.frame.core.config.producer.sources.EspressoConfig;
import com.linkedin.frame.core.config.producer.sources.HdfsConfig;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import com.linkedin.frame.core.config.producer.sources.SourceType;
import com.linkedin.frame.core.config.producer.sources.VeniceConfig;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class TransformationFunctionBuilderFactoryTest {
  private final TransformationFunctionBuilderFactory _transformationFunctionBuilderFactory =
      new TransformationFunctionBuilderFactory(mock(TransformationFunctionExpressionBuilder.class));

  @Test
  public void getTransformationFunctionForRestliSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.RESTLI);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), OnlineDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForVeniceSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.VENICE);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), OnlineDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForEspressoSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.ESPRESSO);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), OnlineDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForCustomSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.CUSTOM);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), OnlineDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForCouchbaseSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.COUCHBASE);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), OnlineDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForPinotSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.PINOT);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), OnlineDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForInMemoryPassthroughSource() {
    PassThroughConfig passThroughConfig = mock(PassThroughConfig.class);
    when(passThroughConfig.getDataModel()).thenReturn(Optional.of("testDataModel"));
    when(passThroughConfig.getSourceType()).thenReturn(SourceType.PASSTHROUGH);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(passThroughConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), OnlineDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForHdfsSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.HDFS);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), HdfsDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForInlineHdfsSource() {
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getTransformationFunctionBuilderForInlineHdfsSource();
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), HdfsDataSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForObservationPassthroughSource() {
    PassThroughConfig passThroughConfig = mock(PassThroughConfig.class);
    when(passThroughConfig.getDataModel()).thenReturn(Optional.empty());
    when(passThroughConfig.getSourceType()).thenReturn(SourceType.PASSTHROUGH);
    AnchoredFeatureTransformationFunctionBuilder anchoredFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(passThroughConfig);
    assertEquals(anchoredFeatureTransformationFunctionBuilder.getClass(), ObservationPassthroughDataSourceTransformationFunctionBuilder.class);
  }


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getTransformationFunctionForKafkaSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.KAFKA);
    _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getTransformationFunctionForRockDbSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.ROCKSDB);
    _transformationFunctionBuilderFactory.getAnchoredFeatureTransformationFunctionBuilder(sourceConfig);
  }

  @Test
  public void getTransformationFunctionForOnlineDerivedFeature() {
    DerivedFeatureTransformationFunctionBuilder derivedFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getDerivedFeatureTransformationFunctionBuilder(FeatureAnchorEnvironment.ONLINE);
    assertEquals(derivedFeatureTransformationFunctionBuilder.getClass(), OnlineFeatureSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForOfflineDerivedFeature() {
    DerivedFeatureTransformationFunctionBuilder derivedFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getDerivedFeatureTransformationFunctionBuilder(FeatureAnchorEnvironment.OFFLINE);
    assertEquals(derivedFeatureTransformationFunctionBuilder.getClass(), OfflineFeatureSourceTransformationFunctionBuilder.class);
  }

  @Test
  public void getTransformationFunctionForCrossEnvironmentDerivedFeature() {
    DerivedFeatureTransformationFunctionBuilder derivedFeatureTransformationFunctionBuilder =
        _transformationFunctionBuilderFactory.getDerivedFeatureTransformationFunctionBuilder(FeatureAnchorEnvironment.CROSS_ENVIRONMENT);
    assertEquals(derivedFeatureTransformationFunctionBuilder.getClass(), CrossEnvironmentFeatureSourceTransformationFunctionBuilder.class);
  }
}
