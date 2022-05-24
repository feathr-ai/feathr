package com.linkedin.frame.config.featureanchor.builder.key;

import com.linkedin.frame.core.config.producer.anchors.AnchorConfig;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.config.producer.sources.CustomSourceConfig;
import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import com.linkedin.frame.core.config.producer.sources.SourceConfig;
import com.linkedin.frame.core.config.producer.sources.SourceType;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class KeyPlaceholdersBuilderFactoryTest {
  private final KeyPlaceholdersBuilderFactory _keyPlaceholdersBuilderFactory = new KeyPlaceholdersBuilderFactory();
  @Test
  public void getKeyPlaceholdersBuilderForRestliSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.RESTLI);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OnlineAnchoredFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForVeniceSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.VENICE);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder= _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OnlineAnchoredFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForEspressoSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.ESPRESSO);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OnlineAnchoredFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForCouchbaseSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.COUCHBASE);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OnlineAnchoredFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForInMemoryPassthroughSource() {
    PassThroughConfig passThroughConfig = mock(PassThroughConfig.class);
    when(passThroughConfig.getSourceType()).thenReturn(SourceType.PASSTHROUGH);
    when(passThroughConfig.getDataModel()).thenReturn(Optional.of("testDataModel"));
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(passThroughConfig, anchorConfig);
    assertNull(keyPlaceHoldersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForCustomSource() {
    CustomSourceConfig customSourceConfig = mock(CustomSourceConfig.class);
    when(customSourceConfig.getSourceType()).thenReturn(SourceType.CUSTOM);
    when(customSourceConfig.getDataModel()).thenReturn("testDataModel");
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(customSourceConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OnlineAnchoredFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForHdfsSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.HDFS);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OfflineAnchoredFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForInlineHdfsSource() {
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getKeyPlaceholdersForInlineHdfsSource(anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OfflineAnchoredFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForObservationPassthroughSource() {
    PassThroughConfig passThroughConfig = mock(PassThroughConfig.class);
    when(passThroughConfig.getDataModel()).thenReturn(Optional.empty());
    when(passThroughConfig.getSourceType()).thenReturn(SourceType.PASSTHROUGH);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(passThroughConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OfflineAnchoredFeatureKeyPlaceholdersBuilder);
  }


  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getKeyPlaceholdersBuilderForKafkaSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.KAFKA);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    _keyPlaceholdersBuilderFactory.getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void getKeyPlaceholdersBuilderForRockDbSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.ROCKSDB);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    _keyPlaceholdersBuilderFactory.getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
  }

  @Test
  public void getKeyPlaceholdersBuilderForDerivedFeature() {
    DerivationConfig derivationConfig = mock(DerivationConfig.class);
    KeyPlaceholdersBuilder keyPlaceholdersBuilder = _keyPlaceholdersBuilderFactory.getDerivedFeatureKeyPlaceholdersBuilder(derivationConfig);
    assertTrue(keyPlaceholdersBuilder instanceof DerivedFeatureKeyPlaceholdersBuilder);
  }

  @Test
  public void getKeyPlaceholdersBuilderForPinotSource() {
    SourceConfig sourceConfig = mock(SourceConfig.class);
    when(sourceConfig.getSourceType()).thenReturn(SourceType.PINOT);
    AnchorConfig anchorConfig = mock(AnchorConfig.class);
    KeyPlaceholdersBuilder keyPlaceHoldersBuilder = _keyPlaceholdersBuilderFactory
        .getAnchoredFeatureKeyPlaceholdersBuilder(sourceConfig, anchorConfig);
    assertTrue(keyPlaceHoldersBuilder instanceof OnlineAnchoredFeatureKeyPlaceholdersBuilder);
  }
}
