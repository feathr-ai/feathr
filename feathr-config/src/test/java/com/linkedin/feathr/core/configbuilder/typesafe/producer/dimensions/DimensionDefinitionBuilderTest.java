package com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.feathr.core.config.ConfigObj;
import com.linkedin.feathr.core.config.producer.common.Namespace;
import com.linkedin.feathr.core.config.producer.common.Version;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.testng.annotations.Test;

import static com.linkedin.feathr.core.utils.Utils.*;
import static org.testng.Assert.*;


/**
 * Tests {@link DimensionDefinitionBuilder} for various dimension types
 */
public class DimensionDefinitionBuilderTest {

  @Test(description = "Tests build of Discrete dimension def with no bounds")
  public void testDiscreteDim1Def() {
    testDimDefBuilder(
        DimensionsFixture.namespace, DimensionsFixture.discreteDim1Name, DimensionsFixture.discreteDim1VerStr, DimensionsFixture.discreteDim1DefCfgStr, DimensionsFixture.expDiscreteDim1Def);
  }

  @Test(description = "Tests build of Discrete dimension def with bounds")
  public void testDiscreteDim2Def() {
    testDimDefBuilder(
        DimensionsFixture.namespace, DimensionsFixture.discreteDim2Name, DimensionsFixture.discreteDim2VerStr, DimensionsFixture.discreteDim2DefCfgStr, DimensionsFixture.expDiscreteDim2Def);
  }

  @Test(description = "Tests build of Continuous dimension def")
  public void testContinuousDimDef() {
    testDimDefBuilder(
        DimensionsFixture.namespace, DimensionsFixture.continuousDimName, DimensionsFixture.continuousDimVerStr, DimensionsFixture.continuousDimDefCfgStr, DimensionsFixture.expContinuousDimDef);
  }

  @Test(description = "Tests build of Text dimension def")
  public void testTextDimDef() {
    testDimDefBuilder(DimensionsFixture.namespace, DimensionsFixture.textDimName, DimensionsFixture.textDimVerStr, DimensionsFixture.textDimDefCfgStr, DimensionsFixture.expTextDimDef);
  }

  @Test(description = "Tests build of Entity dimension def")
  public void testEntityDimDef() {
    testDimDefBuilder(DimensionsFixture.namespace, DimensionsFixture.entityDimName, DimensionsFixture.entityDimVerStr, DimensionsFixture.entityDimDefCfgStr, DimensionsFixture.expEntityDimDef);
  }

  @Test(description = "Tests build of Categorical dimension def")
  public void testCategoricalDim1Def() {
    testDimDefBuilder(DimensionsFixture.namespace, DimensionsFixture.catDim1Name, DimensionsFixture.catDim1VerStr, DimensionsFixture.catDim1DefCfgStr, DimensionsFixture.expCatDim1Def);
  }

  @Test(description = "Tests build of Categorical dimension def")
  public void testCategoricalDim2Def() {
    testDimDefBuilder(DimensionsFixture.namespace, DimensionsFixture.catDim2Name, DimensionsFixture.catDim2Ver1Str, DimensionsFixture.catDim2Def1CfgStr, DimensionsFixture.expCatDim2Ver1Def);
  }

  @Test(description = "Tests build of Hashed dimension def")
  public void testHashedDimDef() {
    testDimDefBuilder(DimensionsFixture.namespace, DimensionsFixture.hashedDim1Name, DimensionsFixture.hashedDim1VerStr, DimensionsFixture.hashedDim1DefCfgStr, DimensionsFixture.expHashedDim1Def);
  }

  @Test(description = "Tests build of a dimension with special chars . and :")
  public void testDimWithSpecialChars() {
    testDimDefBuilder(
        DimensionsFixture.namespaceWithSpecialChars, DimensionsFixture.discreteDim3Name, DimensionsFixture.discreteDim3VerStr, DimensionsFixture.discreteDim3DefCfgStr,
        DimensionsFixture.expDiscreteDim3Def);
  }

  private void testDimDefBuilder(Namespace namespace, String dimName, String versionStr, String configStr,
      ConfigObj expDimDefConfigObj) {
    Config fullConfig = ConfigFactory.parseString(configStr);
    Config dimDefConfig = fullConfig.getConfig(quote(versionStr));
    ConfigObj obsDimDefConfigObj = DimensionDefinitionBuilder.build(namespace, dimName, new Version(versionStr), dimDefConfig);

    assertEquals(obsDimDefConfigObj, expDimDefConfigObj);
  }
}
