package com.linkedin.frame.config.featureanchor.builder.key;

import com.linkedin.frame.config.featureanchor.builder.key.OnlineKeyFunctionBuilder;
import com.linkedin.frame.core.config.producer.sources.CouchbaseConfig;
import com.linkedin.frame.core.config.producer.sources.EspressoConfig;
import com.linkedin.frame.core.config.producer.sources.RestliConfig;
import com.linkedin.frame.core.config.producer.sources.VeniceConfig;
import com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction;
import java.util.Optional;
import org.testng.annotations.Test;

import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class OnlineKeyFunctionBuilderTest {
  @Test
  public void testBuildFromRestliConfig() {
    RestliConfig restliConfig = mock(RestliConfig.class);
    when(restliConfig.getOptionalKeyExpr()).thenReturn(Optional.of("testRestliKeyExpr"));
    OnlineKeyFunctionBuilder onlineKeyFunctionBuilder = new OnlineKeyFunctionBuilder(restliConfig);
    KeyFunction actual = onlineKeyFunctionBuilder.build();
    assertEquals(actual.getMvelExpression().getMvel(), "testRestliKeyExpr");
  }

  @Test
  public void testBuildFromCouchbaseConfig() {
    CouchbaseConfig couchbaseConfig = mock(CouchbaseConfig.class);
    when(couchbaseConfig.getKeyExpr()).thenReturn("testCouchbaseKeyExpr");
    OnlineKeyFunctionBuilder onlineKeyFunctionBuilder = new OnlineKeyFunctionBuilder(couchbaseConfig);
    KeyFunction actual = onlineKeyFunctionBuilder.build();
    assertEquals(actual.getMvelExpression().getMvel(), "testCouchbaseKeyExpr");
  }

  @Test
  public void testBuildFromEspressoConfig() {
    EspressoConfig espressoConfig = mock(EspressoConfig.class);
    when(espressoConfig.getKeyExpr()).thenReturn("testEspressoKeyExpr");
    OnlineKeyFunctionBuilder onlineKeyFunctionBuilder = new OnlineKeyFunctionBuilder(espressoConfig);
    KeyFunction actual = onlineKeyFunctionBuilder.build();
    assertEquals(actual.getMvelExpression().getMvel(), "testEspressoKeyExpr");
  }

  @Test
  public void testBuildFromVeniceConfig() {
    VeniceConfig veniceConfig = mock(VeniceConfig.class);
    when(veniceConfig.getKeyExpr()).thenReturn("testVeniceKeyExpr");
    OnlineKeyFunctionBuilder onlineKeyFunctionBuilder = new OnlineKeyFunctionBuilder(veniceConfig);
    KeyFunction actual = onlineKeyFunctionBuilder.build();
    assertEquals(actual.getMvelExpression().getMvel(), "testVeniceKeyExpr");
  }
}
