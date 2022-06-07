package com.linkedin.feathr.config.featureanchor.builder.key;

import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.sources.PassThroughConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceType;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class KeyFunctionBuilderFactoryTest {
  private final KeyFunctionBuilderFactory _keyFunctionBuilderFactory = new KeyFunctionBuilderFactory();

  @Test
  public void getKeyFunctionForRestliSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.RESTLI);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
    assertEquals(keyFunctionBuilder.getClass(), OnlineKeyFunctionBuilder.class);
  }

  @Test
  public void getKeyFunctionForVeniceSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.VENICE);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
    assertEquals(keyFunctionBuilder.getClass(), OnlineKeyFunctionBuilder.class);
  }

  @Test
  public void getKeyFunctionForEspressoSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.ESPRESSO);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
    assertEquals(keyFunctionBuilder.getClass(), OnlineKeyFunctionBuilder.class);
  }

  @Test
  public void getKeyFunctionForCouchbaseSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.COUCHBASE);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
    assertEquals(keyFunctionBuilder.getClass(), OnlineKeyFunctionBuilder.class);
  }

  @Test
  public void getKeyFunctionForHdfsSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.HDFS);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
    assertEquals(keyFunctionBuilder.getClass(), OfflineKeyFunctionBuilder.class);
  }

  @Test
  public void getKeyFunctionForInlineHdfsSource() {
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilderForInlineHdfsSource(anchorConfig);
    assertEquals(keyFunctionBuilder.getClass(), OfflineKeyFunctionBuilder.class);
  }

  @Test
  public void getKeyFunctionForObservationPassThroughSource() {
    PassThroughConfig sourceConfig = mock(PassThroughConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.PASSTHROUGH);
    when(sourceConfig.getDataModel()).thenReturn(Optional.empty());
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
    assertEquals(keyFunctionBuilder.getClass(), OfflineKeyFunctionBuilder.class);
  }

  @Test
  public void getKeyFunctionForInMemoryPassThroughSource() {
    PassThroughConfig sourceConfig = mock(PassThroughConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.PASSTHROUGH);
    when(sourceConfig.getDataModel()).thenReturn(Optional.of("test.model"));
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyFunctionBuilder keyFunctionBuilder = _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
    assertNull(keyFunctionBuilder);
  }


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getKeyFunctionForKafkaSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.KAFKA);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getKeyFunctionForRockDbSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.ROCKSDB);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    _keyFunctionBuilderFactory.getKeyFunctionBuilder(sourceConfig, anchorConfig);
  }
}
