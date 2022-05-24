package com.linkedin.frame.core.configbuilder.typesafe.presentation;

import com.linkedin.frame.core.config.presentation.PresentationConfig;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

/**
 * Test class for {@link PresentationFunctionBuilder}
 */
public class PresentationFunctionBuilderTest {
  static final String presentationFunctionConfig1 =
      " {\n" +
      "    params: { \n" +
      "        parameter1: \"value1\"\n" +
      "        parameter2: \"value2\"\n" +
      "    } \n" +
      "}\n";

  // functionType should be PresentationFunctionType enum
  static final String presentationFunctionConfig2 =
      " {\n" +
          "    functionType: \"random\" \n" +
          "    params: { \n" +
          "        parameter1: \"value1\"\n" +
          "        parameter2: \"value2\"\n" +
          "    } \n" +
          "}\n";

  @Test(description = "Test building with missing field", expectedExceptions = ConfigBuilderException.class)
  public void testWithMissingField() {
    Config config = ConfigFactory.parseString(presentationFunctionConfig1);
    PresentationFunctionBuilder.getInstance().build(config);
  }

  @Test(description = "Test building with ill-formatted field", expectedExceptions = IllegalArgumentException.class)
  public void testWithIllFormattedField() {
    Config config = ConfigFactory.parseString(presentationFunctionConfig2);
    PresentationFunctionBuilder.getInstance().build(config);
  }
}
