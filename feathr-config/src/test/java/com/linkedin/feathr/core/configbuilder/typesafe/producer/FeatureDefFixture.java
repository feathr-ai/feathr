package com.linkedin.feathr.core.configbuilder.typesafe.producer;

import com.google.common.collect.ImmutableMap;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.feathr.core.config.producer.FeatureDefConfig;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfig;
import com.linkedin.feathr.core.config.producer.anchors.AnchorConfigWithExtractor;
import com.linkedin.feathr.core.config.producer.anchors.AnchorsConfig;
import com.linkedin.feathr.core.config.producer.anchors.FeatureConfig;
import com.linkedin.feathr.core.config.producer.anchors.SimpleFeatureConfig;
import com.linkedin.feathr.core.config.producer.common.DimensionRef;
import com.linkedin.feathr.core.config.producer.common.Namespace;
import com.linkedin.feathr.core.config.producer.common.Version;
import com.linkedin.feathr.core.config.producer.derivations.DerivationConfig;
import com.linkedin.feathr.core.config.producer.derivations.DerivationsConfig;
import com.linkedin.feathr.core.config.producer.derivations.SimpleDerivationConfig;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionDefinition;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionMetadata;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionSection;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionType;
import com.linkedin.feathr.core.config.producer.dimensions.Discrete;
import com.linkedin.feathr.core.config.producer.features.Availability;
import com.linkedin.feathr.core.config.producer.features.FeatureDefinition;
import com.linkedin.feathr.core.config.producer.features.FeatureMetadata;
import com.linkedin.feathr.core.config.producer.features.FeatureSection;
import com.linkedin.feathr.core.config.producer.features.ValueType;
import com.linkedin.feathr.core.config.producer.sources.RestliConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceConfig;
import com.linkedin.feathr.core.config.producer.sources.SourcesConfig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;


public class FeatureDefFixture {
  /*
   * The following config strings have been extracted and culled from feature-prod.conf in frame-feature-careers MP.
   * https://jarvis.corp.linkedin.com/codesearch/result/?name=feature-prod.conf&path=frame-feature-careers%2Fframe-feature-careers-online%2Fsrc%2Fmain%2Fresources%2Fconfig%2Fonline%2Fprod&reponame=multiproducts%2Fframe-feature-careers
   */
  static final String sourcesConfigStr = String.join("\n",
      "sources: {",
      "  JobsTargetingSegments: {",
      "    type: RESTLI",
      "    restResourceName: jobsTargetingSegments",
      "    restEntityType: jobPosting",
      "    pathSpec: targetingFacetsSet",
      "  },",
      "  Profile: {",
      "    type: RESTLI",
      "    restResourceName: profiles",
      "    keyExpr: \"toComplexResourceKey({\\\"id\\\": key[0]},{:})\"",
      "    restReqParams: {",
      "      viewerId: {mvel: \"key[0]\"}",
      "    }",
      "    pathSpec: positions",
      "  },",
      "  MemberPreferenceData: {",
      "    type: RESTLI",
      "    restResourceName: jobSeekers",
      "    restEntityType: member",
      "  }",
      "}");


  static final SourcesConfig expSourcesConfigObj;
  static {
    Function<String, String> toKeyExpr = entityType -> "toUrn(\"" + entityType + "\", key[0])";

    String resourceName1 = "jobsTargetingSegments";
    String keyExpr1 = toKeyExpr.apply("jobPosting");
    Map<String, Object> reqParams1 = null;
    PathSpec pathSpec1 = new PathSpec("targetingFacetsSet");
    RestliConfig expSource1ConfigObj = new RestliConfig("JobsTargetingSegments", resourceName1, keyExpr1, reqParams1, pathSpec1);

    String resourceName2 = "profiles";
    String keyExpr2 = "toComplexResourceKey({\"id\": key[0]},{:})";
    Map<String, Object> paramsMap = new HashMap<>();
    paramsMap.put("viewerId", new DataMap(ImmutableMap.of(RestliConfig.MVEL_KEY, "key[0]")));
    Map<String, Object> reqParams2 = paramsMap;
    PathSpec pathSpec2 = new PathSpec("positions");
    RestliConfig expSource2ConfigObj = new RestliConfig("Profile", resourceName2, keyExpr2, reqParams2, pathSpec2);

    String resourceName3 = "jobSeekers";
    String keyExpr3 = toKeyExpr.apply("member");
    Map<String, Object> reqParams3 = null;
    PathSpec pathSpec3 = null;
    RestliConfig expSource3ConfigObj = new RestliConfig("MemberPreferenceData", resourceName3, keyExpr3, reqParams3, pathSpec3);

    Map<String, SourceConfig> sources = new HashMap<>();
    sources.put("JobsTargetingSegments", expSource1ConfigObj);
    sources.put("Profile", expSource2ConfigObj);
    sources.put("MemberPreferenceData", expSource3ConfigObj);

    expSourcesConfigObj = new SourcesConfig(sources);
  }

  static final String anchorsConfigStr = String.join("\n",
      "anchors: {",
      "  jobs-targeting-term-vectors: {",
      "    source: JobsTargetingSegments",
      "    extractor: com.linkedin.jobs.relevance.frame.online.extractor.JobsTargetingSegmentTermVectorExtractor",
      "    keyAlias: [jobId] ",
      "    features: [",
      "      careers_targeting_companies,",
      "      careers_targeting_functions",
      "    ]",
      "  },",
      "  member-profile-yoe: {",
      "    source: Profile",
      "    extractor: com.linkedin.jobs.relevance.frame.online.extractor.ISBYoeTermVectorExtractor",
      "    features: [",
      "      careers_member_positionsYoE",
      "    ]",
      "  },",
      "  jfu-member-preferences: {",
      "    source: MemberPreferenceData",
      "    extractor: com.linkedin.jobs.relevance.frame.online.extractor.MemberPreferenceExtractor",
      "    features: [",
      "      careers_preference_companySize,",
      "      careers_preference_industry,",
      "      careers_preference_location",
      "    ]",
      "  }",
      "}");

  static final AnchorsConfig expAnchorsConfigObj;
  static {

    String source1 = "JobsTargetingSegments";
    String extractor1 = "com.linkedin.jobs.relevance.frame.online.extractor.JobsTargetingSegmentTermVectorExtractor";
    Map<String, FeatureConfig> features1 = new HashMap<>();
    features1.put("careers_targeting_companies", new SimpleFeatureConfig("careers_targeting_companies"));
    features1.put("careers_targeting_functions", new SimpleFeatureConfig("careers_targeting_functions"));
    AnchorConfigWithExtractor expAnchor1ConfigObj =
        new AnchorConfigWithExtractor(source1, null, null,
            Collections.singletonList("jobId"), extractor1, features1);

    String source2 = "Profile";
    String extractor2 = "com.linkedin.jobs.relevance.frame.online.extractor.ISBYoeTermVectorExtractor";
    Map<String, FeatureConfig> features2 = new HashMap<>();
    features2.put("careers_member_positionsYoE", new SimpleFeatureConfig("careers_member_positionsYoE"));
    AnchorConfigWithExtractor expAnchor2ConfigObj =
        new AnchorConfigWithExtractor(source2,  extractor2, features2);

    String source3 = "MemberPreferenceData";
    String extractor3 = "com.linkedin.jobs.relevance.frame.online.extractor.MemberPreferenceExtractor";
    Map<String, FeatureConfig> features3 = new HashMap<>();
    features3.put("careers_preference_companySize", new SimpleFeatureConfig("careers_preference_companySize"));
    features3.put("careers_preference_industry", new SimpleFeatureConfig("careers_preference_industry"));
    features3.put("careers_preference_location", new SimpleFeatureConfig("careers_preference_location"));
    AnchorConfigWithExtractor expAnchor3ConfigObj =
        new AnchorConfigWithExtractor(source3,  extractor3, features3);

    Map<String, AnchorConfig> anchors = new HashMap<>();

    anchors.put("jobs-targeting-term-vectors", expAnchor1ConfigObj);
    anchors.put("member-profile-yoe", expAnchor2ConfigObj);
    anchors.put("jfu-member-preferences", expAnchor3ConfigObj);

    expAnchorsConfigObj = new AnchorsConfig(anchors);
  }

  static final String derivationsConfigStr = String.join("\n",
      "derivations: {",
      "  waterloo_job_regionCode: \"import com.linkedin.jobs.relevance.frame.common.StandardizedLocationGeoRegionExtractor; StandardizedLocationGeoRegionExtractor.extractRegionCode(waterloo_job_location)\"",
      "  waterloo_member_regionCode: \"import com.linkedin.jobs.relevance.frame.common.StandardizedLocationGeoRegionExtractor; StandardizedLocationGeoRegionExtractor.extractRegionCode(waterloo_member_location)\"",
      "  CustomPlusLatentPreferences_LOCATION: \"isNonZero(careers_preference_location) ? careers_preference_location : careers_latentPreference_location\"",
      "}");

  static final DerivationsConfig expDerivationsConfigObj;
  static {
    SimpleDerivationConfig expDerivation1ConfigObj = new SimpleDerivationConfig("import com.linkedin.jobs.relevance.frame.common.StandardizedLocationGeoRegionExtractor; StandardizedLocationGeoRegionExtractor.extractRegionCode(waterloo_job_location)");
    SimpleDerivationConfig expDerivation2ConfigObj = new SimpleDerivationConfig("import com.linkedin.jobs.relevance.frame.common.StandardizedLocationGeoRegionExtractor; StandardizedLocationGeoRegionExtractor.extractRegionCode(waterloo_member_location)");
    SimpleDerivationConfig expDerivation3ConfigObj = new SimpleDerivationConfig("isNonZero(careers_preference_location) ? careers_preference_location : careers_latentPreference_location");

    Map<String, DerivationConfig> derivations = new HashMap<>();

    derivations.put("waterloo_job_regionCode", expDerivation1ConfigObj);
    derivations.put("waterloo_member_regionCode", expDerivation2ConfigObj);
    derivations.put("CustomPlusLatentPreferences_LOCATION", expDerivation3ConfigObj);

    expDerivationsConfigObj = new DerivationsConfig(derivations);
  }

  /*
   * Note: We didn't add all the features referenced above in anchors. This fragment is only for testing that the
   * feature section is built
   */
  static final String featureSectionStr = String.join("\n",
      "features: {",
      "  careers: {",
      "    careers_preference_companySize: {",
      "      versions: {",
      "        \"1.0\": {",
      "           dims: []",
      "        }",
      "      }",
      "      valType: INT",
      "      availability: ONLINE",
      "    }",
      "  }",
      "}");

  static final FeatureSection expFeatureSection;
  static {
    Namespace namespace = new Namespace("careers");
    String name = "careers_preference_companySize";
    Version version = new Version("1.0");
    List<DimensionRef> dimRefs = new ArrayList<>();
    ValueType valType = ValueType.valueOf("INT");
    FeatureDefinition featureDef = new FeatureDefinition(namespace, name, version, null, dimRefs, valType);

    Map<Version, FeatureDefinition> verFeatureDefs = new HashMap<>();
    verFeatureDefs.put(version, featureDef);

    Availability availability = Availability.valueOf("ONLINE");

    FeatureMetadata featureMetadata = new FeatureMetadata(namespace, name, null, verFeatureDefs, availability);

    Set<FeatureMetadata> featureMetadataSet = new HashSet<>(Collections.singletonList(featureMetadata));
    Map<Namespace, Set<FeatureMetadata>> featureMetadataSetMap = new HashMap<>();
    featureMetadataSetMap.put(namespace, featureMetadataSet);
    expFeatureSection = new FeatureSection(featureMetadataSetMap);
  }

  /*
   * Note: We didn't add any known dimensions. This fragment is only for testing that the dimension section is built
   */
  static final String dimensionSectionStr = String.join("\n",
      "dimensions: {",
      "  careers: {",
      "    dim1: {",
      "      versions: {",
      "        \"4.2\": {",
      "          type: DISCRETE",
      "        }",
      "      }",
      "    }",
      "  }",
      "}");

  static final DimensionSection expDimensionSection;
  static {
    Namespace namespace = new Namespace("careers");
    String name = "dim1";
    Version version = new Version("4.2");
    DimensionType type = new Discrete();

    DimensionDefinition dimDef = new DimensionDefinition(namespace, name, version, null, type);
    Map<Version, DimensionDefinition> dimDefMap = new HashMap<>();
    dimDefMap.put(version, dimDef);

    DimensionMetadata dimMetadata = new DimensionMetadata(namespace, name, null, dimDefMap);

    Set<DimensionMetadata> dimMetadataSet = new HashSet<>(Collections.singletonList(dimMetadata));
    Map<Namespace, Set<DimensionMetadata>> dimMetadataMap = new HashMap<>();
    dimMetadataMap.put(namespace, dimMetadataSet);

    expDimensionSection = new DimensionSection(dimMetadataMap);
  }

  public static final String featureDefConfigStr1 = String.join("\n",
      sourcesConfigStr,
      anchorsConfigStr,
      derivationsConfigStr);

  public static final FeatureDefConfig expFeatureDefConfigObj1 =
      new FeatureDefConfig(expSourcesConfigObj,
          expAnchorsConfigObj, expDerivationsConfigObj, null, null);

  static final String featureDefConfigStr2 = anchorsConfigStr;

  static final FeatureDefConfig expFeatureDefConfigObj2 =
      new FeatureDefConfig(null, expAnchorsConfigObj, null, null, null);

  public static final String featureDefConfigStr3 = String.join("\n",
      sourcesConfigStr,
      anchorsConfigStr,
      derivationsConfigStr,
      featureSectionStr,
      dimensionSectionStr);

  public static final FeatureDefConfig expFeatureDefConfigObj3 =
      new FeatureDefConfig(expSourcesConfigObj,
          expAnchorsConfigObj, expDerivationsConfigObj, expFeatureSection, expDimensionSection);
}
