package com.linkedin.feathr.offline.config.datasource

/**
 * A base class to cluster all data source related configs
 * Add ConfigStr options in this class when new data source is enabled.
 *
 * @param redisConfigStr
 * @param s3ConfigStr
 * @param adlsConfigStr
 * @param blobConfigStr
 * @param sqlConfigStr
 */
class DataSourceConfigs(
                         val redisConfigStr: Option[String] = None,
                         val s3ConfigStr: Option[String] = None,
                         val adlsConfigStr: Option[String] = None,
                         val blobConfigStr: Option[String] = None,
                         val sqlConfigStr: Option[String] = None
                       ) {
  val redisConfig: AuthContext = parseConfigStr(redisConfigStr)
  val s3Config: AuthContext = parseConfigStr(s3ConfigStr)
  val adlsConfig: AuthContext = parseConfigStr(adlsConfigStr)
  val blobConfig: AuthContext = parseConfigStr(blobConfigStr)
  val sqlConfig: AuthContext = parseConfigStr(sqlConfigStr)

  def parseConfigStr(configStr: Option[String] = None): AuthContext = {
    new AuthContext(configStr)
  }
}
