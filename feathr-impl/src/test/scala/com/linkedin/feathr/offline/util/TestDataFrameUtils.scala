package com.linkedin.feathr.offline.util

import com.linkedin.feathr.offline.TestFeathr
import org.testng.annotations.Test

class TestDataFrameUtils extends TestFeathr {
  @Test(description = "Verifies that the null keys are filtered from the dataframe")
  def testFilterNulls(): Unit = {
    val sqlContext = ss.sqlContext
    import sqlContext.implicits._
    val df = Seq(
      ("q", "a", "x"),
      (null, "b", "y"),
      ("a", null, "z"),
      (null, null, "w")
    ).toDF("key1", "key2", "x")
    val filteredDf = DataFrameUtils.filterNulls(df, Seq("key1", "key2"))
    assert(filteredDf.count() == 3)
  }
}
