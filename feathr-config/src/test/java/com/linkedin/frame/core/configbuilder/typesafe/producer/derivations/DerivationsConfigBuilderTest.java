package com.linkedin.frame.core.configbuilder.typesafe.producer.derivations;

import com.linkedin.frame.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.derivations.DerivationsFixture.*;


public class DerivationsConfigBuilderTest extends AbstractConfigBuilderTest {

  @Test
  public void derivationsTest() {
    testConfigBuilder(derivationsConfigStr, DerivationsConfigBuilder::build, expDerivationsConfigObj);
  }
}
