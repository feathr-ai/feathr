package com.linkedin.feathr.core.configvalidator;

import com.linkedin.feathr.core.config.ConfigType;
import com.linkedin.feathr.core.config.consumer.JoinConfig;
import com.linkedin.feathr.core.configdataprovider.ConfigDataProvider;
import com.linkedin.feathr.core.configdataprovider.ResourceConfigDataProvider;
import com.linkedin.feathr.core.configdataprovider.StringConfigDataProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigSyntax;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.linkedin.feathr.core.config.ConfigType.*;
import static com.linkedin.feathr.core.configvalidator.ValidationStatus.*;
import static com.linkedin.feathr.core.configvalidator.ValidationType.*;
import static org.testng.Assert.*;


/**
 * Unit tests for {@link ConfigValidator}
 */
/*
 * Note: These tests exercise the validation API and aren't intended to test syntax validation itself.
 * Such (exhaustive) syntax tests should be added in typesafe/ConfigSchemaTest.
 */
public class ConfigValidatorTest {
  private ConfigValidator _validator;

  @BeforeClass
  public void init() {
    _validator = ConfigValidator.getInstance();
  }

  @Test(description = "Attempts to validate syntax of config with invalid HOCON syntax")
  public void testConfigWithInvalidHocon() {
    List<String> configStrings = Arrays.asList(
        ConfigValidatorFixture.invalidHoconStr1, ConfigValidatorFixture.invalidHoconStr2);

    for (String cfgStr : configStrings) {
      try (ConfigDataProvider cdp = new StringConfigDataProvider(cfgStr)) {
        ValidationResult obsResult = _validator.validate(FeatureDef, SYNTACTIC, cdp);

        assertEquals(obsResult.getValidationStatus(), INVALID);
        assertTrue(obsResult.getDetails().isPresent());
        assertTrue(obsResult.getCause().isPresent());
        assertEquals(obsResult.getCause().get().getClass(), ConfigException.Parse.class);
      } catch (Exception e) {
        fail("Caught exception: " + e.getMessage(), e);
      }
    }
  }

  @Test(description = "Tests syntax validation of a valid FeatureDef config")
  public void testFeatureDefConfigWithValidSyntax() {
    ValidationResult expResult = new ValidationResult(SYNTACTIC, VALID);

    try (ConfigDataProvider cdp = new StringConfigDataProvider(ConfigValidatorFixture.validFeatureDefConfig)) {
      ValidationResult obsResult = _validator.validate(FeatureDef, SYNTACTIC, cdp);

      assertEquals(obsResult, expResult);
    } catch (Exception e) {
      fail("Caught exception: " + e.getMessage(), e);
    }
  }

  @Test(description = "Tests syntax validation of an invalid FeatureDef config")
  public void testFeatureDefConfigWithInvalidSyntax() {
    try (ConfigDataProvider cdp = new StringConfigDataProvider(ConfigValidatorFixture.invalidFeatureDefConfig)) {
      ValidationResult obsResult = _validator.validate(FeatureDef, SYNTACTIC, cdp);

      assertEquals(obsResult.getValidationStatus(), INVALID);
      assertTrue(obsResult.getDetails().isPresent());
      assertTrue(obsResult.getCause().isPresent());

      // Get details and verify that there are no error messages related to (syntactially valid) anchor A3
      String details = obsResult.getDetails().get();
      assertFalse(details.contains("#/anchors/A3"));
    } catch (Exception e) {
      fail("Caught exception: " + e.getMessage(), e);
    }
  }

  @Test(description = "Tests syntax validation of a valid Join config")
  public void testJoinConfigWithValidSyntax() {
    List<String> configStrings = Arrays.asList(ConfigValidatorFixture.validJoinConfigWithSingleFeatureBag, ConfigValidatorFixture.validJoinConfigWithMultFeatureBags);

    ValidationResult expResult = new ValidationResult(SYNTACTIC, VALID);

    for (String cfgStr : configStrings) {
      try (ConfigDataProvider cdp = new StringConfigDataProvider(cfgStr)) {
        ValidationResult obsResult = _validator.validate(Join, SYNTACTIC, cdp);

        assertEquals(obsResult, expResult);
      } catch (Exception e) {
        fail("Caught exception: " + e.getMessage(), e);
      }
    }
  }

  @Test(description = "Tests syntax validation of an invalid Join config")
  public void testJoinConfigWithInvalidSyntax() {
    try (ConfigDataProvider cdp = new StringConfigDataProvider(ConfigValidatorFixture.invalidJoinConfig)) {
      ValidationResult obsResult = _validator.validate(Join, SYNTACTIC, cdp);

      assertEquals(obsResult.getValidationStatus(), INVALID);
      assertTrue(obsResult.getDetails().isPresent());
      assertTrue(obsResult.getCause().isPresent());
    } catch (Exception e) {
      fail("Caught exception: " + e.getMessage(), e);
    }
  }

  @Test(description = "Tests syntax validation of both FeatureDef and Join config together")
  public void testFeatureDefAndJoinConfigSyntax() {
    Map<ConfigType, ConfigDataProvider> configTypeWithDataProvider = new HashMap<>();

    try (ConfigDataProvider featureDefCdp = new StringConfigDataProvider(ConfigValidatorFixture.validFeatureDefConfig);
        ConfigDataProvider joinCdp = new StringConfigDataProvider(
            ConfigValidatorFixture.validJoinConfigWithSingleFeatureBag)
    ) {
      configTypeWithDataProvider.put(FeatureDef, featureDefCdp);
      configTypeWithDataProvider.put(Join, joinCdp);

      ValidationResult expResult = new ValidationResult(SYNTACTIC, VALID);

      Map<ConfigType, ValidationResult> obsResult = _validator.validate(configTypeWithDataProvider, SYNTACTIC);
      assertEquals(obsResult.get(FeatureDef), expResult);
      assertEquals(obsResult.get(Join), expResult);
    } catch (Exception e) {
      fail("Caught exception: " + e.getMessage(), e);
    }
  }
}
