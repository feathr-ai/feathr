package com.linkedin.frame.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.frame.core.config.producer.common.DimensionRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.ResourceRef;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.config.producer.dimensions.Categorical;
import com.linkedin.frame.core.config.producer.dimensions.CategoricalType;
import com.linkedin.frame.core.config.producer.dimensions.Continuous;
import com.linkedin.frame.core.config.producer.dimensions.DimensionDefinition;
import com.linkedin.frame.core.config.producer.dimensions.DimensionMetadata;
import com.linkedin.frame.core.config.producer.dimensions.DimensionSection;
import com.linkedin.frame.core.config.producer.dimensions.Discrete;
import com.linkedin.frame.core.config.producer.dimensions.Entity;
import com.linkedin.frame.core.config.producer.dimensions.HashFunction;
import com.linkedin.frame.core.config.producer.dimensions.Hashed;
import com.linkedin.frame.core.config.producer.dimensions.MurmurHash3;
import com.linkedin.frame.core.config.producer.dimensions.Text;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;

import static com.linkedin.frame.core.config.producer.common.TypedRef.*;
import static com.linkedin.frame.core.config.producer.dimensions.Categorical.*;
import static com.linkedin.frame.core.utils.Utils.*;


/**
 * Fixture for creating and using dimension types, dimension definitions, dimension metadata, and dimension sections
 * in unit tests.
 */
class DimensionsFixture {
  static final Namespace namespace = new Namespace("flagship");
  static final Namespace namespaceWithSpecialChars = new Namespace("ns.dot:colon");
  private static final String versionStr = "4.2";
  private static final Version version = new Version(versionStr);
  private static final String doc = "description of dimension";

  static final String discreteType1Str = "type: DISCRETE";
  static final Discrete expDiscreteType1 = new Discrete();

  static final String discreteType2CfgStr = String.join("\n",
      "type: {",
      "  discrete: {",
      "    upperBound: 200",
      "  }",
      "}");
  static final Discrete expDiscreteType2 = new Discrete(null, 200L);

  static final String discreteType3CfgStr = String.join("\n",
      "type: {",
      "  discrete: {",
      "    lowerBound: 10",
      "  }",
      "}");
  static final Discrete expDiscreteType3 = new Discrete(10L, null);

  static final String discreteType4CfgStr = String.join("\n",
      "type: {",
      "  discrete: {",
      "    lowerBound: 100",
      "    upperBound: 200",
      "  }",
      "}");
  static final Discrete expDiscreteType4 = new Discrete(100L, 200L);

  // An invalid config since lowerBound is not less than upperBound
  static final String discreteType5CfgStr = String.join("\n",
      "type: {",
      "  discrete: {",
      "    lowerBound: 10",
      "    upperBound: 10",
      "  }",
      "}");

  static final String continuousTypeStr = "type: CONTINUOUS";
  static final Continuous expContinuousType = new Continuous();

  static final String textTypeStr = "type: TEXT";
  static final Text expTextType = new Text();

  static final String entityTypeStr = "type: ENTITY";
  static final Entity expEntityType = new Entity();

  static final String catType1CfgStr = String.join("\n",
      "type: {",
      "  categorical: {",
      "    type: ORDINAL",
      "    idMappingRef: \"foo-2.0.1.jar:config/metadata/daysMapping.csv\"",
      "  }",
      "}");

  static final Categorical expCatType1;
  static {
    ResourceRef idMappingRef = new ResourceRef("foo-2.0.1.jar", "config/metadata/daysMapping.csv");
    Map<Long, String> idToCategoryMap = new HashMap<>();
    idToCategoryMap.put(0L, OUT_OF_VOCAB);
    idToCategoryMap.put(1L, "7");
    idToCategoryMap.put(2L, "14");
    idToCategoryMap.put(3L, "21");
    idToCategoryMap.put(4L, "28");
    idToCategoryMap.put(5L, "45");
    idToCategoryMap.put(6L, "90");
    idToCategoryMap.put(7L, "135");
    idToCategoryMap.put(8L, "180");

    expCatType1 = new Categorical(CategoricalType.ORDINAL, idMappingRef, idToCategoryMap);
  }

  static final String catType2CfgStr = String.join("\n",
      "type: {",
      "  categorical: {",
      "    type: NOMINAL",
      "    idMappingRef: config/fruits.csv",
      "  }",
      "}");

  static final Categorical expCatType2;
  static {
    ResourceRef idMappingRef = new ResourceRef("config/fruits.csv");
    Map<Long, String> idToCategoryMap = new HashMap<>();
    idToCategoryMap.put(0L, OUT_OF_VOCAB);
    idToCategoryMap.put(1L, "apple");
    idToCategoryMap.put(2L, "banana");
    idToCategoryMap.put(3L, "orange");
    idToCategoryMap.put(4L, "pear");
    idToCategoryMap.put(5L, "guava");

    expCatType2 = new Categorical(CategoricalType.NOMINAL, idMappingRef, idToCategoryMap);
  }

  static final String catType3CfgStr = String.join("\n",
      "type: {",
      "  categorical: {",
      "    type: NOMINAL",
      "    idMappingRef: config/fruitsWithDupIds.csv",
      "  }",
      "}");

  static final Categorical expCatType3 = expCatType2;

  static final String catType4CfgStr = String.join("\n",
      "type: {",
      "  categorical: {",
      "    type: NOMINAL",
      "    idMappingRef: config/fruitsWithDupNames.csv",
      "  }",
      "}");

  static final Categorical expCatType4 = expCatType2;

  static final String hashedType1CfgStr = String.join("\n",
      "type: {",
      "  hashed: {",
      "    hashFunction: {",
      "      name: murmurhash3_x86_32",
      "      seed: 123456",
      "    }",
      "    dimRef: " + buildDimRefStr(namespace, "dim7", version),
      "    hashToNameRef: config/hashedFruits.csv",
      "  }",
      "}");
  static final Hashed expHashedType1;
  static {
    HashFunction function = new MurmurHash3("murmurhash3_x86_32", 123456);
    DimensionRef dimRef = new DimensionRef(namespace, "dim7", version);
    ResourceRef hashToNameRef = new ResourceRef("config/hashedFruits.csv");

    Map<Long, String> hashToNameMap = new HashMap<>();
    hashToNameMap.put(123456789L, "apple");
    hashToNameMap.put(234567890L, "banana");
    hashToNameMap.put(345678901L, "orange");
    hashToNameMap.put(456789012L, "pear");
    hashToNameMap.put(567890123L, "guava");

    expHashedType1 = new Hashed(function, null, dimRef, hashToNameRef, hashToNameMap);
  }

  static final String hashedType2CfgStr = String.join("\n",
      "type: {",
      "  hashed: {",
      "    hashFunction: {",
      "      name: murmurhash3_x64_128",
      "    }",
      "    bound: 50000",
      "    dimRef: " + buildDimRefStr("dim7", version),
      "    hashToNameRef: config/hashedFruits.csv",
      "  }",
      "}");
  static final Hashed expHashedType2;
  static {
    HashFunction function = new MurmurHash3("murmurhash3_x64_128", null);
    Integer bound = 50000;
    DimensionRef dimRef = new DimensionRef(namespace, "dim7", version);
    ResourceRef hashToNameRef = new ResourceRef("config/hashedFruits.csv");
    expHashedType2 = new Hashed(function, bound, dimRef, hashToNameRef, expHashedType1.getHashToNameMap());
  }

  static final String wrongDim1TypeStr = "type: WRONG_DIM_TYPE";

  static final String wrongDim2TypeCfgStr = "type: {wrongDimType: {dummy: 123}}";

  static final String discreteDim1Name = "discreteDim1";
  static final String discreteDim1VerStr = versionStr;
  static final Version discreteDim1Ver = version;
  static final String discreteDim1DefCfgStr = buildDimDefCfgStr(discreteDim1VerStr, false, discreteType1Str);
  static final String discreteDim1MdCfgStr = buildDimMetadataCfgStr(discreteDim1Name, false, discreteDim1DefCfgStr);

  static final DimensionDefinition expDiscreteDim1Def = new DimensionDefinition(namespace, discreteDim1Name,
      discreteDim1Ver, null, expDiscreteType1);
  static final Map<Version, DimensionDefinition> discreteDim1VerDefs = Stream.of(Pair.of(discreteDim1Ver, expDiscreteDim1Def))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expDiscreteDim1Md = new DimensionMetadata(namespace, discreteDim1Name, null,
      discreteDim1VerDefs);

  static final String discreteDim2Name = "discreteDim2";
  static final String discreteDim2VerStr = versionStr;
  static final Version discreteDim2Ver = version;
  static final String discreteDim2DefCfgStr = buildDimDefCfgStr(discreteDim2VerStr, true, discreteType2CfgStr);
  static final String discreteDim2MdCfgStr = buildDimMetadataCfgStr(discreteDim2Name, true, discreteDim2DefCfgStr);

  static final DimensionDefinition expDiscreteDim2Def = new DimensionDefinition(namespace, discreteDim2Name,
      discreteDim2Ver, doc, expDiscreteType2);
  static final Map<Version, DimensionDefinition> discreteDim2VerDefs = Stream.of(Pair.of(discreteDim2Ver, expDiscreteDim2Def))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expDiscreteDim2Md = new DimensionMetadata(namespace, discreteDim2Name, doc,
      discreteDim2VerDefs);

  // Dimension name with special chars
  static final String discreteDim3Name = "discrete:Dim.3";
  static final String discreteDim3VerStr = versionStr;
  static final Version discreteDim3Ver = version;
  static final String discreteDim3DefCfgStr = buildDimDefCfgStr(discreteDim3VerStr, false, discreteType1Str);
  static final String discreteDim3MdCfgStr = buildDimMetadataCfgStr(discreteDim3Name, true, discreteDim3DefCfgStr);
//  System.out.println("discreteDim3DefCfgStr:\n" + discreteDim3DefCfgStr);
//  System.out.println("discreteDim3MdCfgStr:\n" + discreteDim3MdCfgStr);

  static final DimensionDefinition expDiscreteDim3Def = new DimensionDefinition(namespaceWithSpecialChars,
      discreteDim3Name, discreteDim3Ver, null, expDiscreteType1);
  static final Map<Version, DimensionDefinition> discreteDim3VerDefs = Stream.of(Pair.of(discreteDim3Ver, expDiscreteDim3Def))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expDiscreteDim3Md = new DimensionMetadata(namespaceWithSpecialChars, discreteDim3Name, doc,
      discreteDim3VerDefs);

  static final String continuousDimName = "continuousDim";
  static final String continuousDimVerStr = versionStr;
  static final Version continuousDimVer = version;
  static final String continuousDimDefCfgStr = buildDimDefCfgStr(continuousDimVerStr, true, continuousTypeStr);
  static final String continuousDimMdCfgStr = buildDimMetadataCfgStr(continuousDimName, true, continuousDimDefCfgStr);

  static final DimensionDefinition expContinuousDimDef = new DimensionDefinition(namespace, continuousDimName,
      continuousDimVer, doc, expContinuousType);
  static final Map<Version, DimensionDefinition> continuousDimVerDefs = Stream.of(Pair.of(continuousDimVer, expContinuousDimDef))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expContinuousDimMd = new DimensionMetadata(namespace, continuousDimName, doc,
      continuousDimVerDefs);

  static final String textDimName = "textDim";
  static final String textDimVerStr = versionStr;
  static final Version textDimVer = version;
  static final String textDimDefCfgStr = buildDimDefCfgStr(textDimVerStr, true, textTypeStr);
  static final String textDimMdCfgStr = buildDimMetadataCfgStr(textDimName, true, textDimDefCfgStr);

  static final DimensionDefinition expTextDimDef = new DimensionDefinition(namespace, textDimName, textDimVer, doc,
      expTextType);
  static final Map<Version, DimensionDefinition> textDimVerDefs = Stream.of(Pair.of(textDimVer, expTextDimDef))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expTextDimMd = new DimensionMetadata(namespace, textDimName, doc, textDimVerDefs);

  static final String entityDimName = "entityDimName";
  static final String entityDimVerStr = versionStr;
  static final Version entityDimVer = version;
  static final String entityDimDefCfgStr = buildDimDefCfgStr(entityDimVerStr, true, entityTypeStr);
  static final String entityDimMdCfgStr = buildDimMetadataCfgStr(entityDimName, true, entityDimDefCfgStr);

  static final DimensionDefinition expEntityDimDef = new DimensionDefinition(namespace, entityDimName, entityDimVer,
      doc, expEntityType);
  static final Map<Version, DimensionDefinition> entityDimVerDefs = Stream.of(Pair.of(entityDimVer, expEntityDimDef))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expEntityDimMd = new DimensionMetadata(namespace, entityDimName, doc, entityDimVerDefs);

  static final String catDim1Name = "catDim1";
  static final String catDim1VerStr = versionStr;
  static final Version catDim1Ver = version;
  static final String catDim1DefCfgStr = buildDimDefCfgStr(catDim1VerStr, true, catType1CfgStr);
  static final String catDim1MdCfgStr = buildDimMetadataCfgStr(catDim1Name, true, catDim1DefCfgStr);

  static final DimensionDefinition expCatDim1Def = new DimensionDefinition(namespace, catDim1Name, catDim1Ver, doc,
      expCatType1);
  static final Map<Version, DimensionDefinition> catDim1VerDefs = Stream.of(Pair.of(catDim1Ver, expCatDim1Def))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expCatDim1Md = new DimensionMetadata(namespace, catDim1Name, doc, catDim1VerDefs);

  static final String catDim2Name = "catDim2";
  static final String catDim2Ver1Str = versionStr;
  static final Version catDim2Ver1 = version;
  static final String catDim2Def1CfgStr = buildDimDefCfgStr(catDim2Ver1Str, true, catType2CfgStr);
  static final String catDim2Ver2Str = "5.0";
  static final Version catDim2Ver2 = new Version(catDim2Ver2Str);
  static final String catDim2Def2CfgStr = buildDimDefCfgStr(catDim2Ver2Str, true, catType2CfgStr);
  static final String catDim2MdCfgStr = buildDimMetadataCfgStr(catDim2Name, true, catDim2Def1CfgStr, catDim2Def2CfgStr);

  static final DimensionDefinition expCatDim2Ver1Def = new DimensionDefinition(namespace, catDim2Name, catDim2Ver1,
      doc, expCatType2);
  static final DimensionDefinition expCatDim2Ver2Def = new DimensionDefinition(namespace, catDim2Name, catDim2Ver2,
      doc, expCatType2);   // we reuse the same id map file so retain the same dimension type object
  static final Map<Version, DimensionDefinition> catDim2VerDefs = Stream.of(Pair.of(catDim2Ver1, expCatDim2Ver1Def),
      Pair.of(catDim2Ver2, expCatDim2Ver2Def))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expCatDim2Md = new DimensionMetadata(namespace, catDim2Name, doc, catDim2VerDefs);

  static final String catDim3Name = "catDim3";
  static final String catDim4Name = "catDim4";

  static final String hashedDim1Name = "hashedDim1";
  static final String hashedDim1VerStr = versionStr;
  static final Version hashedDim1Ver = version;
  static final String hashedDim1DefCfgStr = buildDimDefCfgStr(hashedDim1VerStr, true, hashedType1CfgStr);
  static final String hashedDim1MdCfgStr = buildDimMetadataCfgStr(hashedDim1Name, true, hashedDim1DefCfgStr);

  static final DimensionDefinition expHashedDim1Def = new DimensionDefinition(namespace, hashedDim1Name, hashedDim1Ver,
      doc, expHashedType1);
  static final Map<Version, DimensionDefinition> hashedDim1VerDefs = Stream.of(Pair.of(hashedDim1Ver, expHashedDim1Def))
      .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
  static final DimensionMetadata expHashedDim1Md = new DimensionMetadata(namespace, hashedDim1Name, doc,
      hashedDim1VerDefs);

  static final String hashedDim2Name = "hashedDim2";

  static final String wrongDim1Name = "wrongDim1";
  static final String wrongDim2Name = "wrongDim2";

  static final String dimSectionStr = String.join("\n",
      "dimensions: {",
      "  " + namespace + ": {",
      "    " + discreteDim1MdCfgStr,
      "    " + discreteDim2MdCfgStr,
      "    " + continuousDimMdCfgStr,
      "    " + textDimMdCfgStr,
      "    " + entityDimMdCfgStr,
      "    " + catDim1MdCfgStr,
      "    " + catDim2MdCfgStr,
      "    " + hashedDim1MdCfgStr,
      "  }",
      "}");

  static final DimensionSection expDimSection;
  static {
    Set<DimensionMetadata> dimMdSet = Stream.of(expDiscreteDim1Md, expDiscreteDim2Md,
        expContinuousDimMd, expTextDimMd, expEntityDimMd, expCatDim1Md, expCatDim2Md, expHashedDim1Md)
        .collect(Collectors.toSet());

    Map<Namespace, Set<DimensionMetadata>> dimMdSetMap = Stream.of(Pair.of(namespace, dimMdSet))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

    expDimSection = new DimensionSection(dimMdSetMap);
  }

  static final String dimSectionWithSpecialCharsStr = String.join("\n",
      "dimensions: {",
      "  " + quote(namespaceWithSpecialChars.toString()) + ": {",
      "    " + discreteDim3MdCfgStr,
      "  }",
      "}");

  static final DimensionSection expDimSectionWithSpecialChars;
  static {
    Set<DimensionMetadata> dimMdSet = Stream.of(expDiscreteDim3Md).collect(Collectors.toSet());

    Map<Namespace, Set<DimensionMetadata>> dimMdSetMap = Stream.of(Pair.of(namespaceWithSpecialChars, dimMdSet))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

    expDimSectionWithSpecialChars = new DimensionSection(dimMdSetMap);
  }

  private static String buildDimRefStr(Namespace namespace, String name, Version version) {
    String major = Integer.toString(version.getMajor());
    String minor = Integer.toString(version.getMinor());

    return String.join(DELIM, namespace.get(), name, major, minor);
  }

  private static String buildDimRefStr(String name, Version version) {
    String major = Integer.toString(version.getMajor());
    String minor = Integer.toString(version.getMinor());

    return String.join(DELIM, name, major, minor);
  }

  private static String buildDimDefCfgStr(String versionStr, boolean includeDoc, String dimTypeStr) {
    return String.join("\n",
        quote(versionStr) + ": {",
        "  " + (includeDoc ? ("doc: " + doc): ""),
        "  " + dimTypeStr,
        "}");
  }

  private static String buildDimMetadataCfgStr(String dimName, boolean includeDoc, String... dimDefCfgStr) {
    String dimDefs = String.join("\n", dimDefCfgStr);

    return String.join("\n",
        quote(dimName) + ": {",
        "  " + (includeDoc ? ("doc: " + doc) : ""),
        "  " + "versions: {",
        "    " + dimDefs,
        "  }",
        "}");
  }
}
