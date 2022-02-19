package com.linkedin.feathr.offline.testfwk.generation


import java.io.File
import java.util.Optional

/**
 * A simple component to collect pretty-print result for local feature gen job.
 */
class FeatureGenExperimentComponent {
  def prettyPrintFeatureGenResult(mockDataDir: String, featureNames: String, featureDefDir: String): String = {
    val genConf = s"""
                 |operational: {
                 |  name: generateWithDefaultParams
                 |  endTime: 2021-01-02
                 |  endTimeFormat: "yyyy-MM-dd"
                 |  resolution: DAILY
                 |  output:[]
                 |}
                 |features: [${featureNames}]""".stripMargin
    val featureDefFiles = getListOfFiles(featureDefDir).map(file => file.toString)
    val resourceLocations = Map(
      FeathrGenTestComponent.LocalGenConfString -> List(genConf),
      FeathrGenTestComponent.LocalConfPaths -> featureDefFiles)
    print(s"Test features in ${featureDefFiles.mkString(",")}")
    val feathrGenTestComponent = new FeathrGenTestComponent(resourceLocations).run(mockDataDir, Optional.empty())
    var prettyPrintResult = ""
    // in test mode, we always only compute one anchor
    val featurePair = feathrGenTestComponent.toSeq.head
    val schemaTreeString = featurePair._2.data.schema.treeString
    val producedFeatureNames = feathrGenTestComponent.toSeq.map(x => x._1.getFeatureName).mkString(", ")
    prettyPrintResult = prettyPrintResult + "\nThe following features are calculated: \n"
    prettyPrintResult = prettyPrintResult + "\033[92m" + producedFeatureNames + "\033[0m \n"

    prettyPrintResult = prettyPrintResult + "\nYour feature schema is: \n"
    prettyPrintResult = prettyPrintResult + "\033[92m" + schemaTreeString + "\033[0m \n"

    prettyPrintResult = prettyPrintResult + "Your feature value is: \n"
    val features = featurePair._2.data.collect()
    features.foreach(feature => {prettyPrintResult = prettyPrintResult + "\033[92m" + feature.toString + "\033[0m \n"})
    prettyPrintResult
  }

  def getListOfFiles(dir: String):List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}
