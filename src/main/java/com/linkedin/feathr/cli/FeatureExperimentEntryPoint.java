package com.linkedin.feathr.cli;

import com.linkedin.feathr.offline.testfwk.generation.FeatureGenExperimentComponent;
import py4j.GatewayServer;


/**
 * The entry point for Py4j to access the feature experiment component in Java world.
 */
public class FeatureExperimentEntryPoint {
  public String getResult(String userWorkspaceDir, String featureNames) {
    String mockDataDir = userWorkspaceDir + "/mockdata/";
    String featureDefFile = userWorkspaceDir + "/feature_conf/";
    FeatureGenExperimentComponent featureGenExperimentComponent = new FeatureGenExperimentComponent();
    return featureGenExperimentComponent.prettyPrintFeatureGenResult(mockDataDir, featureNames, featureDefFile);
  }

  public static void main(String[] args) {
    GatewayServer gatewayServer = new GatewayServer(new FeatureExperimentEntryPoint());
    gatewayServer.start();
    System.out.println("Py4J Gateway Server Started");
  }
}
