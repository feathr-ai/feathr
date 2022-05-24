package com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions.DimensionsFixture.*;
import static com.linkedin.frame.core.utils.Utils.*;
import static org.testng.Assert.*;


/**
 * Tests {@link DimensionDefinitionBuilder} for various dimension types
 */
public class DimensionDefinitionBuilderTest {

  @Test(description = "Tests build of Discrete dimension def with no bounds")
  public void testDiscreteDim1Def() {
    testDimDefBuilder(namespace, discreteDim1Name, discreteDim1VerStr, discreteDim1DefCfgStr, expDiscreteDim1Def);
  }

  @Test(description = "Tests build of Discrete dimension def with bounds")
  public void testDiscreteDim2Def() {
    testDimDefBuilder(namespace, discreteDim2Name, discreteDim2VerStr, discreteDim2DefCfgStr, expDiscreteDim2Def);
  }

  @Test(description = "Tests build of Continuous dimension def")
  public void testContinuousDimDef() {
    testDimDefBuilder(namespace, continuousDimName, continuousDimVerStr, continuousDimDefCfgStr, expContinuousDimDef);
  }

  @Test(description = "Tests build of Text dimension def")
  public void testTextDimDef() {
    testDimDefBuilder(namespace, textDimName, textDimVerStr, textDimDefCfgStr, expTextDimDef);
  }

  @Test(description = "Tests build of Entity dimension def")
  public void testEntityDimDef() {
    testDimDefBuilder(namespace, entityDimName, entityDimVerStr, entityDimDefCfgStr, expEntityDimDef);
  }

  @Test(description = "Tests build of Categorical dimension def")
  public void testCategoricalDim1Def() {
    testDimDefBuilder(namespace, catDim1Name, catDim1VerStr, catDim1DefCfgStr, expCatDim1Def);
  }

  @Test(description = "Tests build of Categorical dimension def")
  public void testCategoricalDim2Def() {
    testDimDefBuilder(namespace, catDim2Name, catDim2Ver1Str, catDim2Def1CfgStr, expCatDim2Ver1Def);
  }

  @Test(description = "Tests build of Hashed dimension def")
  public void testHashedDimDef() {
    testDimDefBuilder(namespace, hashedDim1Name, hashedDim1VerStr, hashedDim1DefCfgStr, expHashedDim1Def);
  }

  @Test(description = "Tests build of a dimension with special chars . and :")
  public void testDimWithSpecialChars() {
    testDimDefBuilder(namespaceWithSpecialChars, discreteDim3Name, discreteDim3VerStr, discreteDim3DefCfgStr,
        expDiscreteDim3Def);
  }

  private void testDimDefBuilder(Namespace namespace, String dimName, String versionStr, String configStr,
      ConfigObj expDimDefConfigObj) {
    Config fullConfig = ConfigFactory.parseString(configStr);
    Config dimDefConfig = fullConfig.getConfig(quote(versionStr));
    ConfigObj obsDimDefConfigObj = DimensionDefinitionBuilder.build(namespace, dimName, new Version(versionStr), dimDefConfig);

    assertEquals(obsDimDefConfigObj, expDimDefConfigObj);
  }
}
