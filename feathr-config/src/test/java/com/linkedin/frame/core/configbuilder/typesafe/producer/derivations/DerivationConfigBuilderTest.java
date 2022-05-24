package com.linkedin.frame.core.configbuilder.typesafe.producer.derivations;

import com.linkedin.frame.core.config.producer.derivations.DerivationConfig;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.derivations.DerivationsFixture.*;
import static org.testng.Assert.assertEquals;


public class DerivationConfigBuilderTest {

  @Test
  public void testSimpleDerivation() {
    testDerivation(derivation1ConfigStr, expDerivation1ConfigObj);
  }

  @Test
  public void testSimpleDerivationWithSpecialCharacters() {
    testDerivation(derivation1ConfigStrWithSpecialChars, expDerivation1ConfigObjWithSpecialChars);
  }

  @Test
  public void testSimpleDerivationWithSqlExpr() {
    testDerivation(derivationConfigStrWithSqlExpr, expDerivationConfigObjWithSqlExpr);
  }

  @Test
  public void testSimpleDerivationWithType() {
    testDerivation(derivationConfigStrWithType, expDerivationConfigObjWithDef);
  }

  @Test
  public void testDerivationWithMvelExpr() {
    testDerivation(derivation2ConfigStr, expDerivation2ConfigObj);
  }

  @Test
  public void testDerivationWithExtractor() {
    testDerivation(derivation3ConfigStr, expDerivation3ConfigObj);
  }

  @Test
  public void testDerivationWithSqlExpr() {
    testDerivation(derivation4ConfigStr, expDerivation4ConfigObj);
  }

  @Test
  public void testSequentialJoinConfig() {
    testDerivation(sequentialJoin1ConfigStr, expSequentialJoin1ConfigObj);
  }

  @Test(description = "test sequential join config where base feature has outputKey and transformation field")
  public void testSequentialJoinConfig2() {
    testDerivation(sequentialJoin2ConfigStr, expSequentialJoin2ConfigObj);
  }

  @Test(description = "test sequential join config with transformation class")
  public void testSequentialJoinWithTransformationClass() {
    testDerivation(sequentialJoinWithTransformationClassConfigStr, expSequentialJoinWithTransformationClassConfigObj);
  }

  @Test(description = "test sequential join config with both transformation and transformationClass", expectedExceptions = ConfigBuilderException.class)
  public void testSequentialJoinWithInvalidTransformation() {
    Config fullConfig = ConfigFactory.parseString(sequentialJoinWithInvalidTransformationConfigStr);
    DerivationConfigBuilder.build("seq_join_feature", fullConfig);
  }

  private void testDerivation(String configStr, DerivationConfig expDerivationConfig) {
    Config fullConfig = ConfigFactory.parseString(configStr);
    String derivedFeatureName = fullConfig.root().keySet().iterator().next();

    DerivationConfig obsDerivationConfigObj = DerivationConfigBuilder.build(derivedFeatureName, fullConfig);

    assertEquals(obsDerivationConfigObj, expDerivationConfig);
  }
}
