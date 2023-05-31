package com.linkedin.feathr.offline.source.pathutil

import com.linkedin.feathr.offline.util.{HdfsUtils, LocalFeatureJoinUtils, SourceUtils}
import com.linkedin.feathr.offline.source.dataloader.DataLoaderHandler
import org.apache.hadoop.conf.Configuration

import java.io.File

/**
 * path checker for local test files.
 * @param hadoopConf hadoop configuration
 */
private[offline] class LocalPathChecker(hadoopConf: Configuration, dataLoaderHandlers: List[DataLoaderHandler]) extends PathChecker {
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
   * check whether the input path is an external data source. Needs to have separate function, as there are class conflicts with Breaks.getClass
   * @param path input path.
   * @return true if the path is an external data source.
   */
  def isExternalDataSource(path: String): Boolean = {
    import scala.util.control.Breaks._

    var isExternalDataSourceFlag: Boolean = false
    breakable {
      for(dataLoaderHandler <- dataLoaderHandlers) {
        if (dataLoaderHandler.validatePath(path)) {
          isExternalDataSourceFlag = true
          break
        }
      }
    }
    isExternalDataSourceFlag
  }

  /**
   * check whether the input path exists. It will try different formats for local test.
   * @param path input path.
   * @return true if the path exists.
   */
  override def exists(path: String): Boolean = {
    if (!isExternalDataSource(path) && HdfsUtils.exists(path)) return true
    if (LocalFeatureJoinUtils.getMockPathIfExist(path, hadoopConf, None).isDefined) return true
    if (getClass.getClassLoader.getResource(path) != null) return true
    if (getClass.getClassLoader.getResource(path + TEST_AVRO_JSON_FILE) != null) return true
    false
  }

  /**
   * Checks if the given path is non empty. If the path is a directory, check if it has files listed under it. Else, just check the
   * file length.
   * @param path input path
   * @return true if the path is non empty.
   */
  override def nonEmpty(path: String): Boolean = {
    if (!isExternalDataSource(path) && HdfsUtils.nonEmpty(path)) return true
    val filePath = if (LocalFeatureJoinUtils.getMockPathIfExist(path, hadoopConf, None).isDefined) {
      new File(LocalFeatureJoinUtils.getMockPathIfExist(path, hadoopConf, None).get)
    } else {
      new File(getClass.getClassLoader.getResource(path).toURI)
    }

    if (filePath.isDirectory) { // we do not need to check if the file have any length
      filePath.listFiles().length > 0
    } else {
      filePath.length() > 0
    }
  }
}
