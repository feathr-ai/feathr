package com.linkedin.feathr.offline.source

import com.linkedin.feathr.offline.source.SourceFormatType.SourceFormatType
import com.linkedin.feathr.offline.util.{AclCheckUtils, HdfsUtils, LocalFeatureJoinUtils}
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.SparkSession

import scala.util.{Failure, Success, Try}

/**
 * DataSource class
 *
 * Example:- source1: { path: xxxxx, sourceType: xxxx, "timeWindowParams":"xxxx" }
 *
 * @param rawPath path to the data source, may contain #LATEST.
 *                For LIST_PATH, it's a list of path separated by semicolon, such as /dir/file1;/dir/file2
 * @param sourceType source format type as mentioned in [[com.linkedin.feathr.offline.source.SourceFormatType]]
 * @param timeWindowParams those fields related to time window features.
 * @param timePartitionPattern format of the time partitioned feature
 */
private[offline] case class DataSource(
    private val rawPath: String,
    sourceType: SourceFormatType,
    timeWindowParams: Option[TimeWindowParams] = None,
    timePartitionPattern: Option[String] = None)
    extends Serializable {
  private lazy val ss: SparkSession = SparkSession.builder().getOrCreate()
  val path = resolveLatest(rawPath, None) match {
    case Success(resolvedPath) => resolvedPath
    case Failure(_) => rawPath // resolved failed
  }

  val pathList: Array[String] =
    if (sourceType == SourceFormatType.LIST_PATH) rawPath.split(";")
    else Array(path)

  // resolve path with #LATEST
  def resolveLatest(path: String, mockDataBaseDir: Option[String]): Try[String] = {
    Try(if (path.contains(AclCheckUtils.LATEST_PATTERN)) {
      val hadoopConf = ss.sparkContext.hadoopConfiguration
      if (ss.sparkContext.isLocal && LocalFeatureJoinUtils.getMockPathIfExist(path, hadoopConf, mockDataBaseDir).isDefined) {
        val mockPath = LocalFeatureJoinUtils.getMockPathIfExist(path, hadoopConf, mockDataBaseDir).get
        val resolvedPath = HdfsUtils.getLatestPath(mockPath, hadoopConf)
        LocalFeatureJoinUtils.getOriginalFromMockPath(resolvedPath, mockDataBaseDir)
      } else if (new Path(path).getFileSystem(hadoopConf).exists(new Path(path))) {
        HdfsUtils.getLatestPath(path, hadoopConf)
      } else {
        path
      }
    } else path)
  }
  override def toString(): String = "path: " + path + ", sourceType:" + sourceType
}

// Parameters for time window feature source
private[offline] case class TimeWindowParams(timestampColumn: String, timestampColumnFormat: String)
