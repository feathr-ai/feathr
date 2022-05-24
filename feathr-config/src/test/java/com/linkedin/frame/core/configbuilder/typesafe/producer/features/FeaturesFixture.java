package com.linkedin.frame.core.configbuilder.typesafe.producer.features;

import com.linkedin.frame.core.config.producer.common.DimensionRef;
import com.linkedin.frame.core.config.producer.common.Namespace;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.config.producer.features.Availability;
import com.linkedin.frame.core.config.producer.features.FeatureDefinition;
import com.linkedin.frame.core.config.producer.features.FeatureMetadata;
import com.linkedin.frame.core.config.producer.features.FeatureSection;
import com.linkedin.frame.core.config.producer.features.ValueType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;

import static com.linkedin.frame.core.utils.Utils.*;


/**
 * Fixture for creating and using feature definitions, feature metadata, feature sections for use in unit tests.
 */
public class FeaturesFixture {
  public static final Namespace featureNamespace = new Namespace("flagship");
  static final Namespace featureNamespaceWithSpecialChars = new Namespace("flagship.dot:colon");
  private static final String versionStr = "4.2";
  private static final Version version = new Version(versionStr);
  private static final String doc = "description of feature";

  static final String feature1Name = "f1";
  static final String feature2Name = "f2";

  static final ValueType feature1ValType = ValueType.valueOf("FLOAT");
  static final ValueType feature2ValType = ValueType.valueOf("LONG");

  // If a dim's namespace isn't provided, it's assumed to be the same as the enclosing feature's namespace
  public static final DimensionRef dim1Ref = new DimensionRef(featureNamespace, "dim1", version);
  public static final String dim1RefStr = dim1Ref.toString();

  public static final DimensionRef dim2Ref = new DimensionRef(featureNamespace, "dim2", version);
  public static final String dim2RefStr = dim2Ref.toString();

  static final Namespace dim3Namespace = new Namespace("careers");
  public static final DimensionRef dim3Ref = new DimensionRef(dim3Namespace, "dim3", version);
  public static final String dim3RefStr = dim3Ref.toString();

  static final DimensionRef dim4Ver1Ref = new DimensionRef(featureNamespace, "dim4", version);
  static final String dim4Ver1RefStr = dim4Ver1Ref.toString();
  static final DimensionRef dim4Ver2Ref = new DimensionRef(featureNamespace, "dim4", new Version("5.0"));
  static final String dim4Ver2RefStr = dim4Ver2Ref.toString();

  static final DimensionRef dim5Ver1Ref = new DimensionRef(featureNamespace, "dim5", version);
  static final String dim5Ver1RefStr = dim5Ver1Ref.toString();
  static final DimensionRef dim5Ver2Ref = new DimensionRef(featureNamespace, "dim5",
      new Version("5.2"));
  static final String dim5Ver2RefStr = dim5Ver2Ref.toString();

  static final String feature1Ver1Str = versionStr;
  static final Version feature1Ver1 = version;

  static final String feature1Ver1DefStr = String.join("\n",
      quote(feature1Ver1Str) + ": {",
      "  doc: " + doc,
      "  dims: [",
      "    " + dim1RefStr,
      "    " + dim2RefStr,
      "    " + dim3RefStr,
      "    " + dim4Ver1RefStr,
      "    " + dim5Ver1RefStr,
      "  ]",
      "}");

  static final FeatureDefinition expFeature1Ver1Def;
  static {
    List<DimensionRef> dimRefs = Arrays.asList(dim1Ref, dim2Ref, dim3Ref, dim4Ver1Ref, dim5Ver1Ref);
    expFeature1Ver1Def = new FeatureDefinition(featureNamespace, feature1Name, feature1Ver1, doc, dimRefs,
        feature1ValType);
  }

  static final String feature1Ver2Str = "5.0";
  static final Version feature1Ver2 = new Version(feature1Ver2Str);

  static final String feature1Ver2DefStr = String.join("\n",
      quote(feature1Ver2Str) + ": {",
      "  doc: " + doc,
      "  dims: [",
      "    " + dim1RefStr,
      "    " + dim2RefStr,
      "    " + dim3RefStr,
      "    " + dim4Ver2RefStr,
      "    " + dim5Ver2RefStr,
      "  ]",
      "}");

  static final FeatureDefinition expFeature1Ver2Def;
  static {
    List<DimensionRef> dimRefs = Arrays.asList(dim1Ref, dim2Ref, dim3Ref, dim4Ver2Ref, dim5Ver2Ref);
    expFeature1Ver2Def = new FeatureDefinition(featureNamespace, feature1Name, feature1Ver2, doc, dimRefs,
        feature1ValType);
  }

  static final String feature2DefStr = String.join("\n",
      quote(versionStr) + ": {",
      "  dims: []",
      "}");

  static final FeatureDefinition expFeature2Def = new FeatureDefinition(featureNamespace, feature2Name, version, null,
      Collections.emptyList(), ValueType.valueOf("LONG"));

  static final String featureNameWithSpecialChars = "feature.2:Name";
  static final String featureMdStrWithSpecialChars = String.join("\n",
      quote(featureNameWithSpecialChars) + ": {",
      "  versions: {",
      "    " + quote(versionStr) + ": {",
      "      dims: []",
      "    }",
      "  }",
      "  valType: LONG",
      "  availability: OFFLINE",
      "}");

  static final FeatureMetadata expFeatureMdWithSpecialChars;
  static {
    FeatureDefinition featureDef = new FeatureDefinition(featureNamespaceWithSpecialChars, featureNameWithSpecialChars,
        version, null, Collections.EMPTY_LIST, ValueType.LONG);
    Map<Version, FeatureDefinition> featureDefMap = Stream.of(Pair.of(version, featureDef))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    expFeatureMdWithSpecialChars = new FeatureMetadata(featureNamespaceWithSpecialChars, featureNameWithSpecialChars,
        null, featureDefMap, Availability.OFFLINE);
  }

  static final String feature1MetadataStr = String.join("\n",
      feature1Name + ": {",
      "  doc: " + doc,
      "  versions: {",
      "    " + feature1Ver1DefStr,
      "    " + feature1Ver2DefStr,
      "  }",
      "  valType: " + feature1ValType,
      "  availability: OFFLINE_ONLINE",
      "}");

  static final FeatureMetadata expFeature1Md;
  static {
    Map<Version, FeatureDefinition> featureDefMap = Stream.of(Pair.of(feature1Ver1, expFeature1Ver1Def),
        Pair.of(feature1Ver2, expFeature1Ver2Def))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    expFeature1Md = new FeatureMetadata(featureNamespace, feature1Name, doc, featureDefMap, Availability.OFFLINE_ONLINE);
  }

  static final String feature2MetadataStr = String.join("\n",
      feature2Name + ": {",
      "  doc: " + doc,
      "  versions: {",
      "    " + feature2DefStr,
      "  }",
      "  valType: " + feature2ValType,
      "  availability: OFFLINE",
      "}");

  static final FeatureMetadata expFeature2Md;
  static {
    Map<Version, FeatureDefinition> featureDefMap = Stream.of(Pair.of(version, expFeature2Def))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    expFeature2Md = new FeatureMetadata(featureNamespace, feature2Name, doc, featureDefMap, Availability.OFFLINE);
  }

  static final String featureMetadataStrWith1Owners = String.join("\n",
      feature2Name + ": {",
      "  doc: " + doc,
      "  versions: {",
      "    " + feature2DefStr,
      "  }",
      "  valType: " + feature2ValType,
      "  availability: OFFLINE",
      "  owners: [owner1]",
      "}");

  static final String featureMetadataStrWith2Owners = String.join("\n",
      feature2Name + ": {",
      "  doc: " + doc,
      "  versions: {",
      "    " + feature2DefStr,
      "  }",
      "  valType: " + feature2ValType,
      "  availability: OFFLINE",
      "  owners: [jeff1, weiner2]",
      "}");

  static final FeatureMetadata expFeatureMdWith2Owners;
  static {
    Map<Version, FeatureDefinition> featureDefMap = Stream.of(Pair.of(version, expFeature2Def))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    expFeatureMdWith2Owners = new FeatureMetadata(featureNamespace, feature2Name, doc, featureDefMap,
        Availability.OFFLINE, Arrays.asList("jeff1", "weiner2"));
  }

  static final String featureSectionStr = String.join("\n",
      "features: {",
      "  " + featureNamespace + ": {",
      "    " + feature1MetadataStr,
      "    " + feature2MetadataStr,
      "  }",
      "}");

  static final FeatureSection expFeatureSection;
  static {
    Set<FeatureMetadata> featureMdSet = Stream.of(expFeature1Md, expFeature2Md).collect(Collectors.toSet());
    Map<Namespace, Set<FeatureMetadata>> featureMDSetMap = Stream.of(Pair.of(featureNamespace, featureMdSet))
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue));
    expFeatureSection = new FeatureSection(featureMDSetMap);
  }
}
