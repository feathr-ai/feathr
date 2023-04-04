package com.linkedin.feathr.offline.source.pathutil

import com.linkedin.feathr.offline.util.HdfsUtils

/**
 * path checker for non-testing environment.
 */
private[offline] class HdfsPathChecker extends PathChecker {
  override def isMock(path: String): Boolean = false

  override def exists(path: String): Boolean = HdfsUtils.exists(path)

  /**
   * Check whether the given path has any file content. If it is directory, then ensure it has atleast one file, else check
   * the file size is not empty.
   *
   * @param path input path
   * @return true if the path is non empty.
   */
  override def nonEmpty(path: String): Boolean = HdfsUtils.nonEmpty(path)
}
