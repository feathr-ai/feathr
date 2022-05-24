package com.linkedin.frame.config.featureanchor.builder.key;

import com.linkedin.frame.config.featureanchor.builder.key.DerivedFeatureKeyPlaceholdersBuilder;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExpr;
import com.linkedin.frame.core.config.producer.derivations.DerivationConfigWithExtractor;
import com.linkedin.frame.core.config.producer.derivations.SequentialJoinConfig;
import com.linkedin.frame.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.feathr.featureDataModel.KeyPlaceholderArray;
import java.util.Arrays;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class DerivedFeatureKeyPlaceholdersBuilderTest {
  @Test
  public void testBuildFromDerivationConfig() {
    DerivationConfigWithExtractor derivationConfigWithExtractor = mock(DerivationConfigWithExtractor.class);
    when(derivationConfigWithExtractor.getKeys()).thenReturn(Arrays.asList("key1", "key2"));
    DerivedFeatureKeyPlaceholdersBuilder derivedFeatureKeyPlaceholdersBuilder =
        new DerivedFeatureKeyPlaceholdersBuilder(derivationConfigWithExtractor);
    KeyPlaceholderArray actual = derivedFeatureKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 2);
    assertEquals(actual.get(0).getKeyPlaceholderRef(), "key1");
    assertEquals(actual.get(1).getKeyPlaceholderRef(), "key2");

    when(derivationConfigWithExtractor.getKeys()).thenReturn(null);
    derivedFeatureKeyPlaceholdersBuilder = new DerivedFeatureKeyPlaceholdersBuilder(derivationConfigWithExtractor);
    actual = derivedFeatureKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 0);

    DerivationConfigWithExpr derivationConfigWithExpr = mock(DerivationConfigWithExpr.class);
    when(derivationConfigWithExpr.getKeys()).thenReturn(Arrays.asList("key3", "key4"));
    derivedFeatureKeyPlaceholdersBuilder = new DerivedFeatureKeyPlaceholdersBuilder(derivationConfigWithExpr);
    actual = derivedFeatureKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 2);
    assertEquals(actual.get(0).getKeyPlaceholderRef(), "key3");
    assertEquals(actual.get(1).getKeyPlaceholderRef(), "key4");

    when(derivationConfigWithExpr.getKeys()).thenReturn(null);
    derivedFeatureKeyPlaceholdersBuilder = new DerivedFeatureKeyPlaceholdersBuilder(derivationConfigWithExpr);
    actual = derivedFeatureKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 0);

    SequentialJoinConfig sequentialJoinConfig = mock(SequentialJoinConfig.class);
    when(sequentialJoinConfig.getKeys()).thenReturn(Arrays.asList("key5", "key6"));
    derivedFeatureKeyPlaceholdersBuilder = new DerivedFeatureKeyPlaceholdersBuilder(sequentialJoinConfig);
    actual = derivedFeatureKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 2);
    assertEquals(actual.get(0).getKeyPlaceholderRef(), "key5");
    assertEquals(actual.get(1).getKeyPlaceholderRef(), "key6");

    when(sequentialJoinConfig.getKeys()).thenReturn(null);
    actual = derivedFeatureKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 0);

    SimpleDerivationConfig simpleDerivationConfig = mock(SimpleDerivationConfig.class);
    derivedFeatureKeyPlaceholdersBuilder = new DerivedFeatureKeyPlaceholdersBuilder(simpleDerivationConfig);
    actual = derivedFeatureKeyPlaceholdersBuilder.build();
    assertEquals(actual.size(), 0);
  }
}
