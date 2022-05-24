package com.linkedin.frame.core.configbuilder.typesafe.presentation;

import com.linkedin.frame.core.config.presentation.PresentationsSection;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.linkedin.frame.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.presentation.PresentationFixture.*;
import static org.testng.Assert.*;


public class PresentationsSectionBuilderTest extends AbstractConfigBuilderTest {

  @Test(description = "Tests building with deprecated field", expectedExceptions = ConfigBuilderException.class)
  public void testWithOldFieldsCase() {
    Config config = ConfigFactory.parseString(oldFullPresentationsConfigStr);
    PresentationsSectionBuilder.build(config);
  }


  @Test(description = "Tests building of generation config for the case with all previously supported fields")
  public void testWithPartialFieldsCase() {
    testPresentationConfigBuilder(presentationsConfigStr, EXPECTED_PRESENTATIONS_SECTION_CONFIG);
  }

  @Test(description = "Tests building of generation config for the case with all supported fields")
  public void testWithFullFieldsCase() {
    testPresentationConfigBuilder(presentationsConfigStr2, EXPECTED_PRESENTATIONS_SECTION_CONFIG_2);
  }

  @Test(description = "Tests building fails due to conflict fields", expectedExceptions = ConfigBuilderException.class)
  public void testWithConflictFieldsCase() {
    Config config = ConfigFactory.parseString(presentationsConfigStr3);
    PresentationsSectionBuilder.build(config);
  }

  private void testPresentationConfigBuilder(String configStr, PresentationsSection expectedPresentationsSection) {
    Config withDefaultConfig = ConfigFactory.parseString(configStr);
    PresentationsSection presentationsSection = PresentationsSectionBuilder.build(withDefaultConfig);

    assertEquals(presentationsSection, expectedPresentationsSection);
  }
}
