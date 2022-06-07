package com.linkedin.feathr.core.configbuilder.typesafe.presentation;

import com.linkedin.feathr.core.config.presentation.PresentationConfig;
import com.linkedin.feathr.core.config.presentation.PresentationsSection;
import com.typesafe.config.Config;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;


/**
 * Presentation config builder
 * Example:
 * presentation {
 *   my_ccpa_feature: {
 *     linkedInViewFeatureName: standardization_job_standardizedSkillsV5
 *     featureDescription: feature description that shows to the users
 *     valueTranslation: “translateLikelihood(waterloo_member_geoRegion)”
 *   }
 * }
 */
public class PresentationsSectionBuilder {
  private final static Logger logger = Logger.getLogger(PresentationsSectionBuilder.class);
  private final static String PRESENTATIONS = "presentations";

  private PresentationsSectionBuilder() {
  }

  public static PresentationsSection build(Config config) {
    Config rootConfig = config.getConfig(PRESENTATIONS);

    Stream<String> presentationNames = rootConfig.root().keySet().stream();

    Map<String, PresentationConfig> presentations = presentationNames.collect(
        Collectors.toMap(Function.identity(), presentationName ->
            PresentationConfigBuilder.build(rootConfig.getConfig(presentationName))));

    PresentationsSection presentationsSection = new PresentationsSection(presentations);
    logger.trace("Built PresentationsSection object");
    return presentationsSection;
  }
}
