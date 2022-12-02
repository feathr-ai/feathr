package com.linkedin.feathr.core.configvalidator.typesafe;

import com.linkedin.feathr.core.config.consumer.JoinConfig;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigSyntax;
import java.io.InputStream;
import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.annotations.Test;


public class PresentationsConfigSchemaTest {

  ConfigRenderOptions _renderOptions = ConfigRenderOptions.defaults()
      .setComments(false)
      .setOriginComments(false)
      .setFormatted(true)
      .setJson(true);
  ConfigParseOptions _parseOptions = ConfigParseOptions.defaults()
      .setSyntax(ConfigSyntax.CONF)   // HOCON document
      .setAllowMissing(false);


  @Test(description = "Tests build of identifying valid presentations configs")
  public void testPresentationsConfigValidCases() {
    InputStream inputStream = JoinConfig.class.getClassLoader().getResourceAsStream("PresentationsConfigSchema.json");
    JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
    Schema schema = SchemaLoader.load(rawSchema);
    Config myCfg = ConfigFactory.parseResources("PresentationsSchemaTestCases.conf", _parseOptions);
    String jsonStr = myCfg.root().render(_renderOptions);
    JSONTokener tokener = new JSONTokener(jsonStr);
    JSONObject root = new JSONObject(tokener);
    schema.validate(root);
  }
}
