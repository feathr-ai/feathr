package com.linkedin.frame.config.featureanchor.builder.key;

import com.linkedin.frame.config.featureanchor.builder.key.OfflineAnchoredFeatureKeyPlaceholdersBuilder;
import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithExtractor;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKeyExtractor;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithOnlyMvel;
import com.linkedin.frame.core.config.producer.anchors.TypedKey;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.mockito.Mock;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class OfflineKeyPlaceholdersBuilderTest {
  @Test
  public void testBuildFromAnchorConfigWithKey() {
    AnchorConfigWithKey anchorConfig = mock(AnchorConfigWithKey.class);
    when(anchorConfig.getKeyAlias()).thenReturn(Optional.of(Arrays.asList("alias1", "alias2")));
    OfflineAnchoredFeatureKeyPlaceholdersBuilder offlineKeyPlaceholdersBuilder =
        new OfflineAnchoredFeatureKeyPlaceholdersBuilder(anchorConfig);
    KeyPlaceholderArray actual = offlineKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 2);
    assertEquals(actual.get(0).getKeyPlaceholderRef(), "alias1");
    assertEquals(actual.get(1).getKeyPlaceholderRef(), "alias2");
  }

  @Test
  public void testBuildFromAnchorConfigWithExtractor() {
    AnchorConfigWithExtractor anchorConfig = mock(AnchorConfigWithExtractor.class);
    when(anchorConfig.getKeyAlias()).thenReturn(Optional.of(Arrays.asList("alias1", "alias2", "alias3")));
    OfflineAnchoredFeatureKeyPlaceholdersBuilder offlineKeyPlaceholdersBuilder =
        new OfflineAnchoredFeatureKeyPlaceholdersBuilder(anchorConfig);
    KeyPlaceholderArray actual = offlineKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 3);
    assertEquals(actual.get(0).getKeyPlaceholderRef(), "alias1");
    assertEquals(actual.get(1).getKeyPlaceholderRef(), "alias2");
    assertEquals(actual.get(2).getKeyPlaceholderRef(), "alias3");
  }

  @Test
  public void testBuildFromAnchorConfigWithOnlyMvel() {
    AnchorConfigWithOnlyMvel anchorConfig = mock(AnchorConfigWithOnlyMvel.class);
    OfflineAnchoredFeatureKeyPlaceholdersBuilder offlineKeyPlaceholdersBuilder =
        new OfflineAnchoredFeatureKeyPlaceholdersBuilder(anchorConfig);
    KeyPlaceholderArray actual = offlineKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 0);
  }

  @Test
  public void testBuildFromAnchorConfigWithKeyExtracotr() {
    AnchorConfigWithKeyExtractor anchorConfig = mock(AnchorConfigWithKeyExtractor.class);
    OfflineAnchoredFeatureKeyPlaceholdersBuilder offlineKeyPlaceholdersBuilder =
        new OfflineAnchoredFeatureKeyPlaceholdersBuilder(anchorConfig);
    KeyPlaceholderArray actual = offlineKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 0);
  }
}
