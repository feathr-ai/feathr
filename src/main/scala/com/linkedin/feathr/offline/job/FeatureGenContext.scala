package com.linkedin.feathr.offline.job

import com.linkedin.feathr.offline.util.{CmdLineParser, OptionParam}
import com.typesafe.config.{Config, ConfigFactory}

/**
  * @param feathrLocalFeatureDefPath feathr local feature config path
  * @param feathrFeatureDefPaths feathr feature config path
  * @param localOverrideAll local config to override feature repo config
  */
class FeatureDefinitionsInput(
    val feathrLocalFeatureDefPath: Option[String],
    val feathrFeatureDefPaths: Option[String],
    val localOverrideAll: String)
/**
 * Feature generation job context
 *
 * @param workDir   work directory, used to store temporary results.
 * @param paramsOverride    parameter to override in feature generation config
 * @param featureConfOverride   parameter to override in feature definition config
 */

class FeatureGenJobContext(
    val workDir: String,
    val paramsOverride: Option[String] = None,
    val featureConfOverride: Option[String] = None,
    val redisConfigStr: Option[String] = None,
    val s3ConfigStr: Option[String] = None,
    val adlsConfigStr: Option[String] = None,
    val blobConfigStr: Option[String] = None) {
    val redisConfig: Option[Config] = redisConfigStr.map(configStr => ConfigFactory.parseString(configStr))
    val s3Config: Option[Config] = s3ConfigStr.map(configStr => ConfigFactory.parseString(configStr))
    val adlsConfig: Option[Config] = adlsConfigStr.map(configStr => ConfigFactory.parseString(configStr))
    val blobConfig: Option[Config] = blobConfigStr.map(configStr => ConfigFactory.parseString(configStr))
}

object FeatureGenJobContext {
    /**
     * Parse command line arguments, which includes application config,
     * Feathr feature definition configs and other settings
     *
     * @param args command line arguments
     * @return (applicationConfigPath, feature defintions and FeatureGenJobContext)
     * Results wil be used to construct FeathrFeatureGenJobContext
     */
    def parse(args: Array[String]): FeatureGenJobContext = {
        val params = Map(
            // option long name, short name, description, arg name (null means not argument), default value (null means required)
            "work-dir" -> OptionParam("wd", "work directory, used to store temporary results, etc.", "WORK_DIR", ""),
            "params-override" -> OptionParam("ac", "parameter to override in feature generation config", "PARAM_OVERRIDE", "[]"),
            "feature-conf-override" -> OptionParam("fco", "parameter to override in feature definition config", "FEATURE_CONF_OVERRIDE", "[]"))

        val cmdParser = new CmdLineParser(args, params)

        val paramsOverride = cmdParser.extractOptionalValue("params-override")
        val featureConfOverride = cmdParser.extractOptionalValue("feature-conf-override").map(convertToHoconConfig)
        val workDir = cmdParser.extractRequiredValue("work-dir")
        new FeatureGenJobContext(workDir, paramsOverride, featureConfOverride)
    }

    // Convert parameters passed from hadoop template into global vars section for feature conf
    private def convertToHoconConfig(params: String): String = {
        params.stripPrefix("[").stripSuffix("]")
    }
}
