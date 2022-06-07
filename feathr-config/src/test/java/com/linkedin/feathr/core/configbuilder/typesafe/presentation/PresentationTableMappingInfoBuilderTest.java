package com.linkedin.feathr.core.configbuilder.typesafe.presentation;

import com.linkedin.feathr.core.config.presentation.PresentationConfig;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;


/**
 * Test class for {@link PresentationTableMappingInfoBuilder}
 */
public class PresentationTableMappingInfoBuilderTest {
  // the dataset should be an URN
  static final String presentationTableMappingConfig1 =
      "presentationTableMapping: { \n" +
      "    dataset: \"hive.table\" \n" +
      "    keyColumns: [\"keyColumn1\", \"keyColumn2\"] \n" +
      "    valueColumn: \"valueColumnName\"\n" +
      "}\n";

  // config with missing field "dataset"
  static final String presentationTableMappingConfig2 =
      "presentationTableMapping: { \n" +
      "    keyColumns: [\"keyColumn1\", \"keyColumn2\"] \n" +
      "    valueColumn: \"valueColumnName\"\n" +
      "}\n";


  @Test(description = "Test building with ill-formatted dataset URN", expectedExceptions = ConfigBuilderException.class)
  public void testWithIllFormattedField() {
    Config config = ConfigFactory.parseString(presentationTableMappingConfig1);
    PresentationTableMappingInfoBuilder.getInstance()
        .build(config.getConfig(PresentationConfig.PRESENTATION_TABLE_MAPPING));
  }

  @Test(description = "Test building with missing field", expectedExceptions = ConfigBuilderException.class)
  public void testWithMissingField() {
    Config config = ConfigFactory.parseString(presentationTableMappingConfig2);
    PresentationTableMappingInfoBuilder.getInstance()
        .build(config.getConfig(PresentationConfig.PRESENTATION_TABLE_MAPPING));
  }
}
