package com.linkedin.feathr.core.configvalidator.typesafe;

import com.linkedin.feathr.core.configbuilder.typesafe.consumer.JoinFixture;
import com.linkedin.feathr.core.config.consumer.JoinConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigSyntax;
import java.io.IOException;
import java.io.InputStream;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;
import org.everit.json.schema.loader.SchemaLoader;

import static org.testng.Assert.assertEquals;


public class ConfigSchemaTest {

  ConfigRenderOptions _renderOptions = ConfigRenderOptions.defaults()
      .setComments(false)
      .setOriginComments(false)
      .setFormatted(true)
      .setJson(true);
  ConfigParseOptions _parseOptions = ConfigParseOptions.defaults()
      .setSyntax(ConfigSyntax.CONF)   // HOCON document
      .setAllowMissing(false);

  @Test(description = "Tests build of identifying invalid Frame configs")
  public void testFrameConfigInvalidCases() {
    int invalidCount = 0;
    // initialize to different numbers and overwrite by test code below
    int totalCount = -999;
    try (InputStream inputStream = JoinConfig.class.getClassLoader()
        .getResourceAsStream("FeatureDefConfigSchema.json")) {
      try {
        JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
        Schema schema = SchemaLoader.load(rawSchema);

        Config myCfg = ConfigFactory.parseResources("FeatureDefSchemaTestInvalidCases.conf", _parseOptions);
        String jsonStr = myCfg.root().render(_renderOptions);
        JSONTokener tokener = new JSONTokener(jsonStr);
        JSONObject root = new JSONObject(tokener);

        JSONObject anchors = root.getJSONObject("anchors");
        JSONObject sources = root.getJSONObject("sources");
        JSONObject derivations = root.getJSONObject("derivations");
        totalCount = anchors.keySet().size() + sources.keySet().size() + derivations.keySet().size();
        JSONObject newConfig = new JSONObject();
        newConfig.put("anchors", new JSONObject());
        newConfig.put("sources", new JSONObject());
        newConfig.put("derivations", new JSONObject());
        // construct a case for each one of the anchors/sources/derived features to test
        for (String key : anchors.keySet()) {
          newConfig.getJSONObject("anchors").put(key, anchors.getJSONObject(key));
          try {
            schema.validate(newConfig);
          } catch (ValidationException ex) {
            invalidCount += 1;
          }
          newConfig.getJSONObject("anchors").remove(key);
        }
        for (String key : sources.keySet()) {
          newConfig.getJSONObject("sources").put(key, sources.getJSONObject(key));
          try {
            schema.validate(newConfig);
          } catch (ValidationException ex) {
            invalidCount += 1;
          }
          newConfig.getJSONObject("sources").remove(key);
        }
        for (String key : derivations.keySet()) {
          if (derivations.get(key) instanceof JSONObject) {
            newConfig.getJSONObject("derivations").put(key, derivations.getJSONObject(key));
          } else {
            newConfig.getJSONObject("derivations").put(key, derivations.get(key));
          }
          try {
            schema.validate(newConfig);
          } catch (ValidationException ex) {
            invalidCount += 1;
          }
          newConfig.getJSONObject("derivations").remove(key);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    assertEquals(invalidCount, totalCount);
  }

  @Test(description = "Tests build of identifying valid Frame configs")
  public void testFrameConfigValidCases() {
    InputStream inputStream = JoinConfig.class.getClassLoader()
        .getResourceAsStream("FeatureDefConfigSchema.json");
    JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
    Schema schema = SchemaLoader.load(rawSchema);
    Config myCfg = ConfigFactory.parseResources("FeatureDefSchemaTestCases.conf", _parseOptions);
    String jsonStr = myCfg.root().render(_renderOptions);
    JSONTokener tokener = new JSONTokener(jsonStr);
    JSONObject root = new JSONObject(tokener);
    try {
      schema.validate(root);
    } catch (ValidationException e) {
      System.out.println(e.toJSON());
      throw e;
    }
  }


  @Test(description = "Tests build of identifying valid join configs")
  public void testJoinConfigValidCases() {
    Config myCfg = ConfigFactory.parseResources("JoinSchemaTestCases.conf", _parseOptions);
    validateJoinConfig(myCfg);
  }


  @Test(description = "Tests build of valid join config with absolute time range")
  public void testJoinConfigWithAbsTimeRange() {
    Config myCfg = ConfigFactory.parseString(JoinFixture.settingsWithAbsoluteTimeRange, _parseOptions);
    validateJoinConfig(myCfg);
  }

  @Test(description = "Tests build of valid join config with useLatestFeatureData")
  public void testJoinConfigWithUseLatestFeatureData() {
    Config myCfg = ConfigFactory.parseString(JoinFixture.settingsWithLatestFeatureData, _parseOptions);
    validateJoinConfig(myCfg);
  }


  @Test(description = "Tests valid join config with time_window_join and negative value for simulate_time_delay")
  public void testSettingWithNegativeSimulateTimeDelay() {
    Config myCfg = ConfigFactory.parseString(JoinFixture.settingsWithTimeWindowConfigAndNegativeTimeDelay, _parseOptions);
    validateJoinConfig(myCfg);
  }

  @Test(expectedExceptions = ValidationException.class,
      description = "Tests invalid join config invalid pattern for simulate_time_delay")
  public void testTimeWindowJoinSettingWithInvalidNegativeSimulateTimeDelay() {
    Config myCfg = ConfigFactory.parseString(JoinFixture.invalidSettingsWithTimeWindowConfigNegativeTimeDelay, _parseOptions);
    validateJoinConfig(myCfg);
  }

  @Test(expectedExceptions = ValidationException.class, description = "Tests invalid join config with only start time")
  public void testTimeWindowJoinSettingWithNoEndTime() {
    Config myCfg = ConfigFactory.parseString(JoinFixture.invalidWithOnlyStartTime, _parseOptions);
    validateJoinConfig(myCfg);
  }

  @Test(expectedExceptions = ValidationException.class, description = "Tests invalid join config with no timestamp format")
  public void testTimeWindowJoinSettingWithNoTimestampFormat() {
    Config myCfg = ConfigFactory.parseString(JoinFixture.invalidWithNoTimestampFormat, _parseOptions);
    validateJoinConfig(myCfg);
  }

  private void validateJoinConfig(Config cfg) {
    InputStream inputStream = JoinConfig.class.getClassLoader().getResourceAsStream("JoinConfigSchema.json");
    JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
    Schema schema = SchemaLoader.load(rawSchema);
    String jsonStr = cfg.root().render(_renderOptions);
    JSONTokener tokener = new JSONTokener(jsonStr);
    JSONObject root = new JSONObject(tokener);
    schema.validate(root);
  }
}
