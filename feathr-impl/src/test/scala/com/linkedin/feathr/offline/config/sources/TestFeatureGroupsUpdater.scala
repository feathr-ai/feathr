package com.linkedin.feathr.offline.config.sources

import com.linkedin.feathr.common.{DateParam, JoiningFeatureParams}
import com.linkedin.feathr.offline.TestFeathr
import com.linkedin.feathr.offline.config.{FeatureGroupsGenerator, FeathrConfigLoader}
import org.testng.Assert.assertEquals
import org.testng.annotations.Test

class TestFeatureGroupsUpdater extends TestFeathr {
  private val _feathrConfigLoader = FeathrConfigLoader()
  /**
   * This tests the updation of feature groups of window agg features (feature alias and overrideTimeDelay specified).
   */
  @Test
  def testUpdateWindowAggFeatureGroups(): Unit = {
    val featureConfig =
      """
        |sources: {
        |  swaSource: {
        |    location: { path: "slidingWindowAgg/localSWAAnchorTestFeatureData/daily" }
        |    timePartitionPattern: "yyyy/MM/dd"
        |    timeWindowParameters: {
        |      timestampColumn: "timestamp"
        |      timestampColumnFormat: "yyyy-MM-dd"
        |    }
        |  }
        |}
        |
        |anchors: {
        |  swaAnchor2: {
        |    source: "swaSource"
        |    key: "x"
        |    lateralViewParameters: {
        |      lateralViewDef: explode(features)
        |      lateralViewItemAlias: feature
        |    }
        |    features: {
        |      f1Sum: {
        |        def: "feature.col.value"
        |        filter: "feature.col.name = 'f1'"
        |        aggregation: SUM
        |        groupBy: "feature.col.term"
        |        window: 3d
        |      }
        |    }
        |  }
        |}
      """.stripMargin

    val featureDefConfig = _feathrConfigLoader.load(featureConfig)
    val featureGroups = FeatureGroupsGenerator(Seq(featureDefConfig)).getFeatureGroups()
    assertEquals(featureGroups.allWindowAggFeatures.size, 1)
    val keyTaggedFeatures = Seq(
      JoiningFeatureParams(Seq("x"), "f1Sum", None, Some("3d"), Some("f1Sum_3d")),
      JoiningFeatureParams(Seq("x"), "f1Sum", None, Some("4d"), Some("f1Sum_4d"))
    )

    val updatedFeatureGroups = FeatureGroupsUpdater().updateFeatureGroups(featureGroups, keyTaggedFeatures)

    // Ensure that the allWindowAggFeatures contains both the features with the feature alias names.
    assert(updatedFeatureGroups.allWindowAggFeatures.contains("f1Sum_3d"))
    assert(updatedFeatureGroups.allWindowAggFeatures.contains("f1Sum_4d"))
    assertEquals(updatedFeatureGroups.allWindowAggFeatures.size, 3)

    // Ensure that the feature anchor objects associated it with these features are decorated it with the feature alias names.
    assertEquals(updatedFeatureGroups.allWindowAggFeatures("f1Sum_3d").featureAnchor.features, Set("f1Sum_3d"))
    assertEquals(updatedFeatureGroups.allWindowAggFeatures("f1Sum_4d").featureAnchor.features, Set("f1Sum_4d"))
  }

  /**
   * This tests the updation of feature groups of features with dateParams specified.
   */
  @Test
  def testUpdateTimeBasedFeatures(): Unit = {
    val featureConfig =
      """
        |anchors: {
        |  sampleTimeBasedFeatureFeatures: {
        |      source: partitionedHDFSSourceWithoutTimeWindowParameters
        |      key: "x"
        |      features: {
        |        sampleTimeBasedFeature: {
        |          def: count
        |          type: NUMERIC
        |        }
        |      }
        |    }
        |  }
        |sources: {
        |  partitionedHDFSSourceWithoutTimeWindowParameters:{
        |      type: "HDFS"
        |      location: {
        |        path: "/feathr/part_a/daily"
        |      }
        |      timePartitionPattern: "yyyy/MM/dd" // partition of the HDFS path
        |  }
        |}
      """.stripMargin

    val featureDefConfig = _feathrConfigLoader.load(featureConfig)
    val featureGroups = FeatureGroupsGenerator(Seq(featureDefConfig)).getFeatureGroups()
    assertEquals(featureGroups.allAnchoredFeatures.size, 1)
    val keyTaggedFeatures = Seq(
      JoiningFeatureParams(Seq("x"), "sampleTimeBasedFeature", Some(DateParam(Some("20200707"), Some("20200708"))))
    )

    val updatedFeatureGroups = FeatureGroupsUpdater().updateFeatureGroups(featureGroups, keyTaggedFeatures)
    assert(updatedFeatureGroups.allAnchoredFeatures.contains("sampleTimeBasedFeature"))

    // Ensure that the date params are populated for the anchor feature objects.
    assertEquals(updatedFeatureGroups.allAnchoredFeatures("sampleTimeBasedFeature").dateParam, Some(DateParam(Some("20200707"), Some("20200708"))))
  }

  /**
   * This tests that the feature groups are not updated if only overrideTimeDelay or featureAlias is specified.
   */
  @Test
  def testNoOpOnWindowAggFeatureGroups(): Unit = {
    val featureConfig =
      """
        |sources: {
        |  swaSource: {
        |    location: { path: "slidingWindowAgg/localSWAAnchorTestFeatureData/daily" }
        |    timePartitionPattern: "yyyy/MM/dd"
        |    timeWindowParameters: {
        |      timestampColumn: "timestamp"
        |      timestampColumnFormat: "yyyy-MM-dd"
        |    }
        |  }
        |}
        |
        |anchors: {
        |  swaAnchor2: {
        |    source: "swaSource"
        |    key: "x"
        |    lateralViewParameters: {
        |      lateralViewDef: explode(features)
        |      lateralViewItemAlias: feature
        |    }
        |    features: {
        |      f1Sum: {
        |        def: "feature.col.value"
        |        filter: "feature.col.name = 'f1'"
        |        aggregation: SUM
        |        groupBy: "feature.col.term"
        |        window: 3d
        |      }
        |      f1: {
        |        def: "feature.col.value"
        |        filter: "feature.col.name = 'f1'"
        |        aggregation: COUNT
        |        groupBy: "feature.col.term"
        |        window: 3d
        |      }
        |    }
        |  }
        |}
      """.stripMargin

    val featureDefConfig = _feathrConfigLoader.load(featureConfig)
    val featureGroups = FeatureGroupsGenerator(Seq(featureDefConfig)).getFeatureGroups()
    val keyTaggedFeatures = Seq(
      JoiningFeatureParams(Seq("x"), "f1Sum", None, Some("3d")),
      JoiningFeatureParams(Seq("x"), "f1", None, None, Some("f1_count"))
    )

    val updatedFeatureGroups = FeatureGroupsUpdater().updateFeatureGroups(featureGroups, keyTaggedFeatures)

    // Ensure that the allWindowAggFeatures contains both the features with the feature alias names.
    assert(updatedFeatureGroups.equals(featureGroups))
  }

}
