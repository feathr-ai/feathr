package com.linkedin.feathr.core.configbuilder.typesafe.presentation;

import com.linkedin.frame.common.FabricType;
import com.linkedin.frame.common.urn.DataPlatformUrn;
import com.linkedin.frame.common.urn.DatasetUrn;
import com.linkedin.data.template.StringArray;
import com.linkedin.data.template.StringMap;
import com.linkedin.feathr.core.config.presentation.ExportMode;
import com.linkedin.feathr.core.config.presentation.PresentationConfig;
import com.linkedin.feathr.core.config.presentation.PresentationsSection;
import com.linkedin.frame.common.PresentationFunction;
import com.linkedin.frame.common.PresentationFunctionType;
import com.linkedin.frame.common.PresentationInlineMappingInfo;
import com.linkedin.frame.common.PresentationTableMappingInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class PresentationFixture {

  static final String oldFullPresentationsConfigStr =
      "presentations {\n"
          + "my_ccpa_feature: {\n"
          + "         memberViewFeatureName: \"standardization job standardizedSkills\"\n"
          + "         linkedInViewFeatureName: standardization_job_standardizedSkillsV5\n"
          + "         featureDescription: feature description that shows to the users\n"
          + "         valueTranslation: \"translateLikelihood(waterloo_member_geoRegion, [[0, 0.33, 'Low'], [0.33, 0.66, 'Medium'],[0.66, 1.0, 'High']])\"\n"
          + "         exportModes: [\"EATIN\"]\n"
          + "         isValueExportable: true\n"
          + "}\n" + "}";

  static final String presentationsConfigStr =
      "presentations {\n"
          + "my_ccpa_feature: {\n"
          + "         memberViewFeatureName: \"standardization job standardizedSkills\"\n"
          + "         linkedInViewFeatureName: standardization_job_standardizedSkillsV5\n"
          + "         featureDescription: feature description that shows to the users\n"
          + "         exportModes: [\"EATIN\"]\n"
          + "         isValueExportable: true\n"
          + "}\n" + "}";

  static final String presentationsConfigStr2 =
          "presentations { \n" +
          "  my_ccpa_feature: { \n" +
          "    memberViewFeatureName: \"standardization job standardizedSkills\" \n" +
          "    linkedInViewFeatureName: standardization_job_standardizedSkillsV5 \n" +
          "    featureDescription: \"feature description that shows to the users\" \n" +
          "    exportModes: [\"TAKEOUT\", \"EATIN\", \"TIER3\"] \n" +
          "    isValueExportable: true \n" +
          "    \n" +
          "    inferenceCategory: \"inferenceCategoryValue\" \n" +
          "    presentationFunctionMapping: [\n" +
          "        {\n" +
          "            functionType: \"BOOLEAN\" \n" +
          "            params: { \n" +
          "                parameter1: \"value1\"\n" +
          "                parameter2: \"value2\"\n" +
          "            } \n" +
          "        },\n" +
          "        {\n" +
          "            functionType: \"BOOLEAN\" \n" +
          "        }\n" +
          "    ]\n" +
          "    presentationInlineMapping: { \n" +
          "        name: \"userDefinedPresentationInlineMappingName\"\n" +
          "        mapping: {\n" +
          "            featureValue1: \"memberFacingValue1\"\n" +
          "            featureValue2: \"memberFacingValue2\"\n" +
          "        }\n" +
          "    }\n" +
          "    presentationTableMapping: { \n" +
          "        dataset: \"urn:li:dataset:(urn:li:dataPlatform:hive,hive.table1,PROD)\" \n" +
          "        keyColumns: [\"keyColumn1\", \"keyColumn2\"] \n" +
          "        valueColumn: \"valueColumnName\"\n" +
          "    }\n" +
          "  } \n" +
          "}";

  static final String presentationsConfigStr3 =
      "presentations { \n" +
          "  my_ccpa_feature: { \n" +
          "    memberViewFeatureName: \"feature name showed to LinkedIn members\" \n" +
          "    linkedInViewFeatureName: frameFeature1 \n" +
          "    featureDescription: \"feature description showed to LinkedIn members\" \n" +
          "    exportModes: [\"TAKEOUT\", \"EATIN\", \"TIER3\"] \n" +
          "    isValueExportable: false \n" +
          "    \n" +
          "    valueTranslation: \"translateLikelihood(waterloo_member_geoRegion, [[0, 0.33, 'Low'], [0.33, 0.66, 'Medium'],[0.66, 1.0, 'High']])\"\n" +
          "    inferenceCategory: \"inferenceCategoryValue\" \n" +
          "    presentationFunctionMapping: [\n" +
          "        {\n" +
          "            functionType: \"BOOLEAN\" \n" +
          "            params: { \n" +
          "                parameter1: \"value1\"\n" +
          "                parameter2: \"value2\"\n" +
          "            } \n" +
          "        },\n" +
          "        {\n" +
          "            functionType: \"BOOLEAN\" \n" +
          "        }\n" +
          "    ]\n" +
          "    presentationInlineMapping: { \n" +
          "        name: \"userDefinedPresentationInlineMappingName\"\n" +
          "        mapping: {\n" +
          "            featureValue1: \"memberFacingValue1\"\n" +
          "            featureValue2: \"memberFacingValue2\"\n" +
          "        }\n" +
          "    }\n" +
          "    presentationTableMapping: { \n" +
          "        dataset: \"urn:li:dataset:(urn:li:dataPlatform:hive,hive.table1,PROD)\" \n" +
          "        keyColumns: [\"keyColumn1\", \"keyColumn2\"] \n" +
          "        valueColumn: \"valueColumnName\"\n" +
          "    }\n" +
          "  } \n" +
          "}";

  private static final String _presentationName = "my_ccpa_feature";
  private static final String _memberViewFeatureName = "standardization job standardizedSkills";
  private static final String _linkedInViewFeatureName = "standardization_job_standardizedSkillsV5";
  private static final String _featureDescription = "feature description that shows to the users";
  private static final boolean _exportable = true;

  static final PresentationsSection EXPECTED_PRESENTATIONS_SECTION_CONFIG;
  static {
    Map<String, PresentationConfig> presentations = new HashMap<>();
    String valueTranslation = null;
    List<ExportMode> exportModes = new LinkedList<>();
    exportModes.add(ExportMode.EATIN);
    PresentationConfig presentationConfig = new PresentationConfig(_memberViewFeatureName, _linkedInViewFeatureName,
        _featureDescription, valueTranslation, exportModes, _exportable);
    presentations.put(_presentationName, presentationConfig);
    EXPECTED_PRESENTATIONS_SECTION_CONFIG = new PresentationsSection(presentations);
  }

  static final PresentationsSection EXPECTED_PRESENTATIONS_SECTION_CONFIG_2;
  static {
    Map<String, PresentationConfig> presentations = new HashMap<>();
    List<ExportMode> exportModes = Arrays.asList(ExportMode.TAKEOUT, ExportMode.EATIN, ExportMode.TIER3);
    String inferenceCategory = "inferenceCategoryValue";
    DataPlatformUrn platformUrn = new DataPlatformUrn("hive");
    DatasetUrn datasetUrn = new DatasetUrn(platformUrn, "hive.table1", FabricType.PROD);
    PresentationTableMappingInfo tableMappingInfo = new PresentationTableMappingInfo().setDataset(datasetUrn)
        .setKeyColumns(new StringArray(Arrays.asList("keyColumn1", "keyColumn2"))).setValueColumn("valueColumnName");
    Map<String, String> inlineMap = new HashMap<>();
    inlineMap.put("featureValue1", "memberFacingValue1");
    inlineMap.put("featureValue2", "memberFacingValue2");
    PresentationInlineMappingInfo inlineMappingInfo = new PresentationInlineMappingInfo()
        .setName("userDefinedPresentationInlineMappingName").setMapping(new StringMap(inlineMap));

    Map<String, String> params = new HashMap<>();
    params.put("parameter1", "value1");
    params.put("parameter2", "value2");
    PresentationFunction presentationFunction1 = new PresentationFunction()
        .setFuntionType(PresentationFunctionType.BOOLEAN).setParams(new StringMap(params));
    PresentationFunction presentationFunction2 = new PresentationFunction()
        .setFuntionType(PresentationFunctionType.BOOLEAN);
    List<PresentationFunction> functionMappingInfo = Arrays.asList(presentationFunction1, presentationFunction2);

    PresentationConfig presentationConfig = new PresentationConfig.Builder()
        .setMemberViewFeatureName(_memberViewFeatureName).setLinkedInViewFeatureName(_linkedInViewFeatureName)
        .setFeatureDescription(_featureDescription).setExportModes(exportModes)
        .setIsValueExportable(_exportable).setInferenceCategory(inferenceCategory)
        .setPresentationTableMapping(tableMappingInfo).setPresentationInlineMapping(inlineMappingInfo)
        .setPresentationFunctionMapping(functionMappingInfo).build();

    presentations.put(_presentationName, presentationConfig);
    EXPECTED_PRESENTATIONS_SECTION_CONFIG_2 = new PresentationsSection(presentations);
  }
}
