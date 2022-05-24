package com.linkedin.frame.config.featureanchor.builder.key;

import com.linkedin.frame.core.config.producer.ExprType;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithExtractor;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKey;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithKeyExtractor;
import com.linkedin.frame.core.config.producer.anchors.AnchorConfigWithOnlyMvel;
import com.linkedin.frame.core.config.producer.anchors.TypedKey;
import com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class OfflineKeyFunctionBuilderTest {
  @Test
  public void testBuildFromAnchorConfigWithKey() {
    AnchorConfigWithKey anchorConfigWithKey = mock(AnchorConfigWithKey.class);
    String keyExpr = "testExpr";
    when(anchorConfigWithKey.getTypedKey()).thenReturn(new TypedKey(keyExpr, ExprType.MVEL));
    OfflineKeyFunctionBuilder offlineKeyFunctionBuilder = new OfflineKeyFunctionBuilder(anchorConfigWithKey);
    KeyFunction actual = offlineKeyFunctionBuilder.build();
    assertEquals(actual.getMvelExpression().getMvel(), "testExpr");

    when(anchorConfigWithKey.getTypedKey()).thenReturn(new TypedKey(keyExpr, ExprType.SQL));
    actual = offlineKeyFunctionBuilder.build();
    assertEquals(actual.getSparkSqlExpression().getSql(), "testExpr");
  }

  @Test
  public void testBuildFromAnchorConfigWithKeyExtractor() {
    AnchorConfigWithKeyExtractor anchorConfigWithKeyExtractor = mock(AnchorConfigWithKeyExtractor.class);
    when(anchorConfigWithKeyExtractor.getKeyExtractor()).thenReturn("com.test.key.extractor2");
    OfflineKeyFunctionBuilder offlineKeyFunctionBuilder = new OfflineKeyFunctionBuilder(anchorConfigWithKeyExtractor);
    KeyFunction actual = offlineKeyFunctionBuilder.build();
    assertEquals(actual.getUserDefinedFunction().getClazz().getFullyQualifiedName(), "com.test.key.extractor2");
  }

  @Test
  public void testBuildFromAnchorConfigWithExtractorAndKeyExtractor() {
    AnchorConfigWithExtractor anchorConfigWithExtractor = mock(AnchorConfigWithExtractor.class);
    when(anchorConfigWithExtractor.getKeyExtractor()).thenReturn(Optional.of("com.test.key.extractor"));
    OfflineKeyFunctionBuilder offlineKeyFunctionBuilder = new OfflineKeyFunctionBuilder(anchorConfigWithExtractor);
    KeyFunction actual = offlineKeyFunctionBuilder.build();
    assertEquals(actual.getUserDefinedFunction().getClazz().getFullyQualifiedName(), "com.test.key.extractor");
  }

  @Test
  public void testBuildFromAnchorConfigWithExtractorAndNoKeyExtractor() {
    AnchorConfigWithExtractor anchorConfigWithExtractor = mock(AnchorConfigWithExtractor.class);
    when(anchorConfigWithExtractor.getKeyExtractor()).thenReturn(Optional.empty());
    when(anchorConfigWithExtractor.getExtractor()).thenReturn("com.test.extractor");
    OfflineKeyFunctionBuilder offlineKeyFunctionBuilder = new OfflineKeyFunctionBuilder(anchorConfigWithExtractor);
    KeyFunction actual = offlineKeyFunctionBuilder.build();
    assertEquals(actual.getUserDefinedFunction().getClazz().getFullyQualifiedName(), "com.test.extractor");
  }


  @Test
  public void testBuildFromAnchorConfigWithKeyAndExtractorAndNoKeyExtractor() {
    AnchorConfigWithExtractor anchorConfigWithExtractor = mock(AnchorConfigWithExtractor.class);
    when(anchorConfigWithExtractor.getTypedKey()).thenReturn(Optional.of(new TypedKey("testExpr", ExprType.MVEL)));
    when(anchorConfigWithExtractor.getKeyExtractor()).thenReturn(Optional.empty());
    when(anchorConfigWithExtractor.getExtractor()).thenReturn("com.test.extractor");
    OfflineKeyFunctionBuilder offlineKeyFunctionBuilder = new OfflineKeyFunctionBuilder(anchorConfigWithExtractor);
    KeyFunction actual = offlineKeyFunctionBuilder.build();
    assertEquals(actual.getMvelExpression().getMvel(), "testExpr");
  }

  @Test
  public void testBuildFromAnchorConfigWithNoKeyAndExtractorAndNoKeyExtractor() {
    AnchorConfigWithExtractor anchorConfigWithExtractor = mock(AnchorConfigWithExtractor.class);
    when(anchorConfigWithExtractor.getTypedKey()).thenReturn(Optional.empty());
    when(anchorConfigWithExtractor.getKeyExtractor()).thenReturn(Optional.empty());
    when(anchorConfigWithExtractor.getExtractor()).thenReturn("com.test.extractor");
    OfflineKeyFunctionBuilder offlineKeyFunctionBuilder = new OfflineKeyFunctionBuilder(anchorConfigWithExtractor);
    KeyFunction actual = offlineKeyFunctionBuilder.build();
    assertEquals(actual.getUserDefinedFunction().getClazz().getFullyQualifiedName(), "com.test.extractor");
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testBuildFromAnchorConfigWithOnlyMvel() {
    AnchorConfigWithOnlyMvel anchorConfigWithOnlyMvel = mock(AnchorConfigWithOnlyMvel.class);
    OfflineKeyFunctionBuilder offlineKeyFunctionBuilder = new OfflineKeyFunctionBuilder(anchorConfigWithOnlyMvel);
    offlineKeyFunctionBuilder.build();
  }
}
