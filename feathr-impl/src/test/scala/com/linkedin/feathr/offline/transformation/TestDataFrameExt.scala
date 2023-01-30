package com.linkedin.feathr.offline.transformation

import com.linkedin.feathr.offline.TestFeathr
import org.scalatest.mockito.MockitoSugar
import org.testng.annotations.Test
import com.linkedin.feathr.offline.transformation.DataFrameExt._
import com.linkedin.feathr.offline.AssertFeatureUtils._
/**
 * Unit test class for DataFrameExt.
 */
class TestDataFrameExt extends TestFeathr {
  @Test
  def testFuzzyUnion(): Unit = {
    val sqlContext = new org.apache.spark.sql.SQLContext(ss.sparkContext)
    import sqlContext.implicits._
    val df1 = Seq((1, 2, 3)).toDF("col0", "col1", "col2")
    val df2 = Seq((4, 5, 6)).toDF("col1", "col0", "col3")
    val expectDf = Seq((1, 2, Some(3), None), (5, 4, None, Some(6))).toDF("col0", "col1", "col2", "col3")
    val unionDf = df1.fuzzyUnion(df2)
    assertDataFrameEquals(unionDf, expectDf)
  }
}
