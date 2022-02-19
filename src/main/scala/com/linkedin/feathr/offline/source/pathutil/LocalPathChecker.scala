package com.linkedin.feathr.offline.source.pathutil

import com.linkedin.feathr.offline.util.{HdfsUtils, LocalFeatureJoinUtils, SourceUtils}
import org.apache.hadoop.conf.Configuration

/**
 * path checker for local test files.
 * @param hadoopConf hadoop configuration
 */
private[offline] class LocalPathChecker(hadoopConf: Configuration) extends PathChecker {

  private val TEST_AVRO_JSON_FILE = "/data.avro.json"

  /**
   * check whether the path is a local mock folder
   * @param path input path
   * @return true if the local mock folder exists.
   */
  override def isMock(path: String): Boolean = {
    LocalFeatureJoinUtils.getMockPathIfExist(path, hadoopConf, None).isDefined
  }

  /**
   * check whether the input path exists. It will try different formats for local test.
   * @param path input path.
   * @return true if the path exists.
   */
  override def exists(path: String): Boolean = {
    if (HdfsUtils.exists(path)) return true
    if (LocalFeatureJoinUtils.getMockPathIfExist(path, hadoopConf, None).isDefined) return true
    if (getClass.getClassLoader.getResource(path) != null) return true
    if (getClass.getClassLoader.getResource(path + TEST_AVRO_JSON_FILE) != null) return true
    false
  }
}
