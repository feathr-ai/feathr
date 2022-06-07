package com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.feathr.core.config.producer.dimensions.DimensionType;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import org.testng.annotations.Test;

import static com.linkedin.feathr.core.config.producer.dimensions.DimensionDefinition.*;
import static org.testng.Assert.*;

/*
 * Tests build of various dimension types
 */
public class DimensionTypeBuilderTest {

  @Test(description = "Tests build of Discrete dimension with no bounds")
  public void testDiscrete1() {
    testDimensionType(
        DimensionsFixture.discreteDim1Name, DimensionsFixture.discreteType1Str, DimensionsFixture.expDiscreteType1);
  }

  @Test(description = "Tests build of Discrete dimension with upper bound")
  public void testDiscrete2() {
    testDimensionType(
        DimensionsFixture.discreteDim2Name, DimensionsFixture.discreteType2CfgStr, DimensionsFixture.expDiscreteType2);
  }

  @Test(description = "Tests build of Discrete dimension with lower bound")
  public void testDiscrete3() {
    testDimensionType(
        DimensionsFixture.discreteDim2Name, DimensionsFixture.discreteType3CfgStr, DimensionsFixture.expDiscreteType3);
  }

  @Test(description = "Tests build of Discrete dimension with both lower and upper bounds")
  public void testDiscrete4() {
    testDimensionType(
        DimensionsFixture.discreteDim2Name, DimensionsFixture.discreteType4CfgStr, DimensionsFixture.expDiscreteType4);
  }

  @Test(description = "Tests build of Discrete dimension with invalid lower and upper bounds",
      expectedExceptions = IllegalArgumentException.class)
  public void testDiscrete5() {
    testDimensionType(
        DimensionsFixture.discreteDim2Name, DimensionsFixture.discreteType5CfgStr, DimensionsFixture.expDiscreteType4);
  }

  @Test(description = "Tests build of Continuous dimension")
  public void testContinuous() {
    testDimensionType(
        DimensionsFixture.continuousDimName, DimensionsFixture.continuousTypeStr, DimensionsFixture.expContinuousType);
  }

  @Test(description = "Tests build of Text dimension")
  public void testText() {
    testDimensionType(DimensionsFixture.textDimName, DimensionsFixture.textTypeStr, DimensionsFixture.expTextType);
  }

  @Test(description = "Tests build of Entity dimension")
  public void testEntity() {
    testDimensionType(DimensionsFixture.entityDimName, DimensionsFixture.entityTypeStr, DimensionsFixture.expEntityType);
  }

  @Test(description = "Tests build of Categorical dimension")
  public void testCategorical1() {
    testDimensionType(DimensionsFixture.catDim1Name, DimensionsFixture.catType1CfgStr, DimensionsFixture.expCatType1);
  }

  @Test(description = "Tests build of Categorical dimension")
  public void testCategorical2() {
    testDimensionType(DimensionsFixture.catDim2Name, DimensionsFixture.catType2CfgStr, DimensionsFixture.expCatType2);
  }

  @Test(description = "Tests build of Categorical dimension with duplicate Ids",
      expectedExceptions = ConfigBuilderException.class)
  public void testCategorical3() {
    testDimensionType(DimensionsFixture.catDim3Name, DimensionsFixture.catType3CfgStr, DimensionsFixture.expCatType3);
  }

  @Test(description = "Tests build of Categorical dimension with duplicate Names",
      expectedExceptions = ConfigBuilderException.class)
  public void testCategorical4() {
    testDimensionType(DimensionsFixture.catDim4Name, DimensionsFixture.catType4CfgStr, DimensionsFixture.expCatType4);

  }

  @Test(description = "Tests build of Hashed dimension with MurmurHash3 function with explicit seed and namespace ref")
  public void testHashed1() {
    testDimensionType(
        DimensionsFixture.hashedDim1Name, DimensionsFixture.hashedType1CfgStr, DimensionsFixture.expHashedType1);
  }

  @Test(description = "Tests build of Hashed dimension with MurmurHash3 function with default seed and namespace ref")
  public void testHashed2() {
    testDimensionType(
        DimensionsFixture.hashedDim2Name, DimensionsFixture.hashedType2CfgStr, DimensionsFixture.expHashedType2);
  }

  @Test(description = "Tests build of wrong dimension",
      expectedExceptions = ConfigBuilderException.class)
  public void testWrongDim1() {
    testDimensionType(
        DimensionsFixture.wrongDim1Name, DimensionsFixture.wrongDim1TypeStr, DimensionsFixture.expEntityType);
  }

  @Test(description = "Tests build of wrong dimension",
      expectedExceptions = ConfigBuilderException.class)
  public void testWrongDim2() {
    testDimensionType(
        DimensionsFixture.wrongDim2Name, DimensionsFixture.wrongDim2TypeCfgStr, DimensionsFixture.expHashedType1);
  }

  private void testDimensionType(String dimName, String dimTypeConfigStr, DimensionType expDimType) {
    Config config = ConfigFactory.parseString(dimTypeConfigStr);
    ConfigValue configValue = config.getValue(DIMENSION_TYPE);

    DimensionType obsDimType = DimensionTypeBuilder.build(DimensionsFixture.namespace, dimName, configValue);
    assertEquals(obsDimType, expDimType);
  }
}
