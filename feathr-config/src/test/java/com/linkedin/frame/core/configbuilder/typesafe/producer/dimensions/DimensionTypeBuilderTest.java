package com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.frame.core.config.producer.dimensions.DimensionType;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.config.producer.dimensions.DimensionDefinition.*;
import static com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions.DimensionsFixture.*;
import static org.testng.Assert.*;

/*
 * Tests build of various dimension types
 */
public class DimensionTypeBuilderTest {

  @Test(description = "Tests build of Discrete dimension with no bounds")
  public void testDiscrete1() {
    testDimensionType(discreteDim1Name, discreteType1Str, expDiscreteType1);
  }

  @Test(description = "Tests build of Discrete dimension with upper bound")
  public void testDiscrete2() {
    testDimensionType(discreteDim2Name, discreteType2CfgStr, expDiscreteType2);
  }

  @Test(description = "Tests build of Discrete dimension with lower bound")
  public void testDiscrete3() {
    testDimensionType(discreteDim2Name, discreteType3CfgStr, expDiscreteType3);
  }

  @Test(description = "Tests build of Discrete dimension with both lower and upper bounds")
  public void testDiscrete4() {
    testDimensionType(discreteDim2Name, discreteType4CfgStr, expDiscreteType4);
  }

  @Test(description = "Tests build of Discrete dimension with invalid lower and upper bounds",
      expectedExceptions = IllegalArgumentException.class)
  public void testDiscrete5() {
    testDimensionType(discreteDim2Name, discreteType5CfgStr, expDiscreteType4);
  }

  @Test(description = "Tests build of Continuous dimension")
  public void testContinuous() {
    testDimensionType(continuousDimName, continuousTypeStr, expContinuousType);
  }

  @Test(description = "Tests build of Text dimension")
  public void testText() {
    testDimensionType(textDimName, textTypeStr, expTextType);
  }

  @Test(description = "Tests build of Entity dimension")
  public void testEntity() {
    testDimensionType(entityDimName, entityTypeStr, expEntityType);
  }

  @Test(description = "Tests build of Categorical dimension")
  public void testCategorical1() {
    testDimensionType(catDim1Name, catType1CfgStr, expCatType1);
  }

  @Test(description = "Tests build of Categorical dimension")
  public void testCategorical2() {
    testDimensionType(catDim2Name, catType2CfgStr, expCatType2);
  }

  @Test(description = "Tests build of Categorical dimension with duplicate Ids",
      expectedExceptions = ConfigBuilderException.class)
  public void testCategorical3() {
    testDimensionType(catDim3Name, catType3CfgStr, expCatType3);
  }

  @Test(description = "Tests build of Categorical dimension with duplicate Names",
      expectedExceptions = ConfigBuilderException.class)
  public void testCategorical4() {
    testDimensionType(catDim4Name, catType4CfgStr, expCatType4);

  }

  @Test(description = "Tests build of Hashed dimension with MurmurHash3 function with explicit seed and namespace ref")
  public void testHashed1() {
    testDimensionType(hashedDim1Name, hashedType1CfgStr, expHashedType1);
  }

  @Test(description = "Tests build of Hashed dimension with MurmurHash3 function with default seed and namespace ref")
  public void testHashed2() {
    testDimensionType(hashedDim2Name, hashedType2CfgStr, expHashedType2);
  }

  @Test(description = "Tests build of wrong dimension",
      expectedExceptions = ConfigBuilderException.class)
  public void testWrongDim1() {
    testDimensionType(wrongDim1Name, wrongDim1TypeStr, expEntityType);
  }

  @Test(description = "Tests build of wrong dimension",
      expectedExceptions = ConfigBuilderException.class)
  public void testWrongDim2() {
    testDimensionType(wrongDim2Name, wrongDim2TypeCfgStr, expHashedType1);
  }

  private void testDimensionType(String dimName, String dimTypeConfigStr, DimensionType expDimType) {
    Config config = ConfigFactory.parseString(dimTypeConfigStr);
    ConfigValue configValue = config.getValue(DIMENSION_TYPE);

    DimensionType obsDimType = DimensionTypeBuilder.build(namespace, dimName, configValue);
    assertEquals(obsDimType, expDimType);
  }
}
