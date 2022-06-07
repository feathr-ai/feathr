package com.linkedin.feathr.core.configbuilder.typesafe.presentation;

import com.linkedin.feathr.core.config.presentation.ExportMode;
import com.linkedin.feathr.core.config.presentation.PresentationConfig;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.linkedin.frame.common.PresentationFunction;
import com.linkedin.frame.common.PresentationInlineMappingInfo;
import com.linkedin.frame.common.PresentationTableMappingInfo;
import com.typesafe.config.Config;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;


/**
 * FeaturePresentationConfig builder
 * Example:
 * presentations {
 *   ccpaFeature1: {
 *     memberViewFeatureName: "feature name showed to LinkedIn members"
 *     linkedInViewFeatureName: frameFeature1
 *     featureDescription: "feature description showed to LinkedIn members"
 *     exportModes: ["TAKEOUT", "EATIN", "TIER3"]
 *     isValueExportable: false
 *
 *     inferenceCategory: "CAREER_INFERENCES"
 *
 *     presentationFunctionMapping: [
 *         {
 *             functionType: "MockPresentationFunctionTypeEnum"
 *             params: {
 *                 parameter1: "value1"
 *                 parameter2: "value2"
 *             }
 *         },
 *         {
 *             functionType: "BOOLEAN"
 *         }
 *     ]
 *
 *     presentationInlineMapping: {
 *         name: "userDefinedPresentationInlineMappingName"
 *         mapping: {
 *             featureValue1: "memberFacingValue1"
 *             featureValue2: "memberFacingValue2"
 *         }
 *     }
 *
 *     presentationTableMapping: {
 *         dataset: "urn:li:dataset:(urn:li:dataPlatform:hive,hive.table1,PROD)"
 *         keyColumns: ["keyColumn1", "keyColumn2"]
 *         valueColumn: "valueColumnName"
 *     }
 *   }
 * }
 */
public class PresentationConfigBuilder {
  private final static Logger logger = Logger.getLogger(PresentationConfigBuilder.class);

  private PresentationConfigBuilder() {
  }

  public static PresentationConfig build(Config config) {
    String memberViewFeatureName = config.getString(PresentationConfig.MEMBER_VIEW_NAME);
    String linkedInViewFeatureName = config.getString(PresentationConfig.LINKEDIN_VIEW_NAME);
    String description = config.getString(PresentationConfig.FEATURE_DESCRIPTION);
    /*
     * The valueTranslation field is deprecated, since it is never used in production, we strictly forbid it
     * in the config
     */
    if (config.hasPath(PresentationConfig.VALUE_TRANSLATION)) {
      StringBuilder messageStringBuilder = new StringBuilder();
      messageStringBuilder.append(PresentationConfig.VALUE_TRANSLATION).append(" is deprecated and won't be supported in CCPA. ")
          .append("Please remove the field and use ").append(PresentationConfig.PRESENTATION_TABLE_MAPPING)
          .append(", ").append(PresentationConfig.PRESENTATION_INLINE_MAPPING).append(", or ")
          .append(PresentationConfig.PRESENTATION_FUNCTION_MAPPING).append(" instead.\n");
      throw new ConfigBuilderException(messageStringBuilder.toString());
    }
    List<String> exportModesList = config.getStringList(PresentationConfig.EXPORT_MODES);

    List<ExportMode> exportModes = exportModesList.stream().map(ExportMode::valueOf).collect(Collectors.toList());
    boolean exportable = config.getBoolean(PresentationConfig.IS_VALUE_EXPORTABLE);

    String inferenceCategory = config.hasPath(PresentationConfig.INFERENCE_CATEGORY)
        ? config.getString(PresentationConfig.INFERENCE_CATEGORY) : null;

    PresentationTableMappingInfo tableMappingInfo = config.hasPath(PresentationConfig.PRESENTATION_TABLE_MAPPING)
        ? PresentationTableMappingInfoBuilder.getInstance().
        build(config.getConfig(PresentationConfig.PRESENTATION_TABLE_MAPPING)) : null;
    PresentationInlineMappingInfo inlineMappingInfo = config.hasPath(PresentationConfig.PRESENTATION_INLINE_MAPPING)
        ? PresentationInlineMappingInfoBuilder.getInstance()
        .build(config.getConfig(PresentationConfig.PRESENTATION_INLINE_MAPPING)) : null;
    List<PresentationFunction> functionMappingInfo = config.hasPath(PresentationConfig.PRESENTATION_FUNCTION_MAPPING)
        ? buildPresentationFunctionMapping(config.getConfigList(PresentationConfig.PRESENTATION_FUNCTION_MAPPING))
        : null;

    PresentationConfig presentationConfig = new PresentationConfig.Builder()
        .setMemberViewFeatureName(memberViewFeatureName).setLinkedInViewFeatureName(linkedInViewFeatureName)
        .setFeatureDescription(description).setExportModes(exportModes)
        .setIsValueExportable(exportable).setInferenceCategory(inferenceCategory)
        .setPresentationTableMapping(tableMappingInfo).setPresentationInlineMapping(inlineMappingInfo)
        .setPresentationFunctionMapping(functionMappingInfo).build();

    logger.trace("Built PresentationConfigConfig object");
    return presentationConfig;
  }

  private static List<PresentationFunction> buildPresentationFunctionMapping(List<? extends Config> configList) {
    return configList.stream().map(PresentationFunctionBuilder.getInstance()::build).collect(Collectors.toList());
  }
}
