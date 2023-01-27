package com.linkedin.feathr.offline

import com.linkedin.feathr.offline.util.FeathrUtils

import org.testng.Assert._
import org.testng.annotations.Test

class TestFeathrUtils {
  private val TEST_FEATHR_VERSION = "1.2.3"

  /**
   * Test feathr version is properly being read from metric.properties resource file.
   */
  @Test
  def testContextCollector(): Unit = {
    val feathrVersion = FeathrUtils.feathrVersion
    assertEquals(feathrVersion, TEST_FEATHR_VERSION)
  }
}
