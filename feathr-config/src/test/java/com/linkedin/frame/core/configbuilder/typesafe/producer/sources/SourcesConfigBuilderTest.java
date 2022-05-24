package com.linkedin.frame.core.configbuilder.typesafe.producer.sources;

import com.linkedin.frame.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.sources.SourcesFixture.*;


public class SourcesConfigBuilderTest extends AbstractConfigBuilderTest {

  @Test(description = "Tests build of all offline source configs")
  public void offlineSourcesConfigTest() {
    testConfigBuilder(offlineSourcesConfigStr, SourcesConfigBuilder::build, expOfflineSourcesConfigObj);
  }

  @Test(description = "Tests build of all online source configs")
  public void onlineSourcesConfigTest() {
    testConfigBuilder(onlineSourcesConfigStr, SourcesConfigBuilder::build, expOnlineSourcesConfigObj);
  }
}
