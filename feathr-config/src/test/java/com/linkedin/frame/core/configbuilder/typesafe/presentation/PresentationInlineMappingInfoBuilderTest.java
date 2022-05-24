package com.linkedin.frame.core.configbuilder.typesafe.presentation;

import com.linkedin.frame.core.config.presentation.PresentationConfig;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

/**
 * Test class for {@link PresentationInlineMappingInfoBuilder}
 */
public class PresentationInlineMappingInfoBuilderTest {
  static final String presentationInlineMappingConfig1 =
      "presentationInlineMapping: { \n" +
      "    mapping: {\n" +
      "        featureValue1: \"memberFacingValue1\"\n" +
      "        featureValue2: \"memberFacingValue2\"\n" +
      "    }\n" +
      "}\n";

  @Test(description = "Test building with missing field", expectedExceptions = ConfigBuilderException.class)
  public void testWithMissingField() {
    Config config = ConfigFactory.parseString(presentationInlineMappingConfig1);
    PresentationInlineMappingInfoBuilder.getInstance()
        .build(config.getConfig(PresentationConfig.PRESENTATION_INLINE_MAPPING));
  }
}
