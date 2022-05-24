package com.linkedin.frame.core.configbuilder.typesafe.producer.sources;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import com.linkedin.frame.core.configbuilder.typesafe.AbstractConfigBuilderTest;
import com.typesafe.config.Config;
import java.util.function.BiFunction;
import org.testng.annotations.Test;

import static com.linkedin.frame.core.configbuilder.typesafe.producer.sources.SourcesFixture.*;


public class SourceConfigBuilderTest extends AbstractConfigBuilderTest {

  BiFunction<String, Config, ConfigObj> configBuilder = SourceConfigBuilder::build;

  @Test(description = "Tests HDFS config without 'type' field")
  public void hdfsConfigTest1() {
    testConfigBuilder(hdfsSource1ConfigStr, configBuilder, expHdfsSource1ConfigObj);
  }

  @Test(description = "Tests HDFS config with 'type' field")
  public void hdfsConfigTest2() {
    testConfigBuilder(hdfsSource2ConfigStr, configBuilder, expHdfsSource2ConfigObj);
  }

  @Test(description = "Tests HDFS config with Dali URI")
  public void hdfsConfigTest3() {
    testConfigBuilder(hdfsSource3ConfigStr, configBuilder, expHdfsSource3ConfigObj);
  }

  @Test(description = "Tests HDFS config with sliding time window")
  public void hdfsConfigTest4() {
    testConfigBuilder(hdfsSource4ConfigStr, configBuilder, expHdfsSource4ConfigObj);
  }

  @Test(description = "Tests HDFS config with timePartitionPattern")
  public void hdfsConfigTest5WithTimePartitionPattern() {
    testConfigBuilder(hdfsSource5ConfigStrWithTimePartitionPattern, configBuilder, expHdfsSource5ConfigObjWithTimePartitionPattern);
  }

  @Test(description = "Tests HDFS config with sliding time window")
  public void hdfsConfigTest6WithLegacyTimeWindowParameters() {
    testConfigBuilder(hdfsSource6ConfigStrWithLegacyTimeWindowParameters, configBuilder, expHdfsSource6ConfigObjWithLegacyTimeWindowParameters);
  }

  @Test(description = "It should fail if both timePartitionPattern and isTimeSeries is set.", expectedExceptions = ConfigBuilderException.class)
  public void hdfsConfigTestWithTimePartitionPatternAndIsTimeSeries() {
    buildConfig(invalidHdfsSourceconfigStrWithTimePartitionPatternAndIsTimeSeries, configBuilder);
  }

  @Test(description = "It should fail if both hasTimeSnapshot and isTimeSeries is set.", expectedExceptions = ConfigBuilderException.class)
  public void hdfsConfigTestWithHasTimeSnapshotAndIsTimeSeries() {
    buildConfig(invalidHdfsSourceconfigStrWithHasTimeSnapshotAndIsTimeSeries, configBuilder);
  }

  @Test(description = "Tests Espresso config")
  public void espressoConfigTest1() {
    testConfigBuilder(espressoSource1ConfigStr, configBuilder, expEspressoSource1ConfigObj);
  }

  @Test(description = "Tests Venice config with Avro key")
  public void veniceConfigTest1() {
    testConfigBuilder(veniceSource1ConfigStr, configBuilder, expVeniceSource1ConfigObj);
  }

  @Test(description = "Tests Venice config with integer key")
  public void veniceConfigTest2() {
    testConfigBuilder(veniceSource2ConfigStr, configBuilder, expVeniceSource2ConfigObj);
  }

  @Test(description = "Tests RestLi config with entity type and path spec")
  public void restliConfigTest1() {
    testConfigBuilder(restliSource1ConfigStr, configBuilder, expRestliSource1ConfigObj);
  }

  @Test(description = "Tests RestLi config with entity type and REST request params containing 'json' object")
  public void restliConfigTest2() {
    testConfigBuilder(restliSource2ConfigStr, configBuilder, expRestliSource2ConfigObj);
  }

  @Test(description = "Tests RestLi config with entity type and REST request params containing 'jsonArray' array")
  public void restliConfigTest3() {
    testConfigBuilder(restliSource3ConfigStr, configBuilder, expRestliSource3ConfigObj);
  }

  @Test(description = "Tests RestLi config with key expression, REST request params containing 'mvel' expression")
  public void restliConfigTest4() {
    testConfigBuilder(restliSource4ConfigStr, configBuilder, expRestliSource4ConfigObj);
  }

  @Test(description = "Tests RestLi config with entity type and "
      + "REST request params containg 'json' whose value is a string enclosing an object")
  public void restliConfigTest5() {
    testConfigBuilder(restliSource5ConfigStr, configBuilder, expRestliSource5ConfigObj);
  }

  @Test(description = "Tests RestLi config with entity type and REST request params containg 'json' object"
      + "but the 'json' object is empty.")
  public void restliConfigTest6() {
    testConfigBuilder(restliSource6ConfigStr, configBuilder, expRestliSource6ConfigObj);
  }

  @Test(description = "Tests RestLi config with entity type and REST request params containing 'jsonArray' array,"
      + " but the 'json' array is empty")
  public void restliConfigTest7() {
    testConfigBuilder(restliSource7ConfigStr, configBuilder, expRestliSource7ConfigObj);
  }

  @Test(description = "Tests RestLi config with finder field")
  public void restliConfigTest8() {
    testConfigBuilder(restliSource8ConfigStr, configBuilder, expRestliSource8ConfigObj);
  }

  @Test(description = "Tests RestLi config with both keyExpr and finder field")
  public void restliConfigTest9() {
    testConfigBuilder(restliSource9ConfigStr, configBuilder, expRestliSource9ConfigObj);
  }

  @Test(description = "Tests RestLi config missing both keyExpr and finder fields results in an error", expectedExceptions = ConfigBuilderException.class)
  public void restliConfigTest10() {
    testConfigBuilder(restliSource10ConfigStr, configBuilder, null);
  }

  @Test(description = "Tests Kafka config")
  public void kafkaConfigTest1() {
    testConfigBuilder(kafkaSource1ConfigStr, configBuilder, expKafkaSource1ConfigObj);
  }

  @Test(description = "Tests Kafka config with sliding window aggregation")
  public void kafkaConfigTest2() {
    testConfigBuilder(kafkaSource2ConfigStr, configBuilder, expKafkaSource2ConfigObj);
  }

  @Test(description = "Tests RocksDB config with keyExpr field")
  public void rocksDbConfigTest1() {
    testConfigBuilder(rocksDbSource1ConfigStr, configBuilder, expRocksDbSource1ConfigObj);
  }

  @Test(description = "Tests RocksDB config without keyExpr field")
  public void rocksDbConfigTest2() {
    testConfigBuilder(rocksDbSource2ConfigStr, configBuilder, expRocksDbSource2ConfigObj);
  }

  @Test(description = "Tests PassThrough config")
  public void passThroughConfigTest1() {
    testConfigBuilder(passThroughSource1ConfigStr, configBuilder, expPassThroughSource1ConfigObj);
  }

  @Test(description = "Tests Couchbase config")
  public void couchbaseConfigTest1() {
    testConfigBuilder(couchbaseSource1ConfigStr, configBuilder, expCouchbaseSource1ConfigObj);
  }

  @Test(description = "Tests Couchbase config name with special characters")
  public void couchbaseConfigTest1WithSpecialCharacters() {
    testConfigBuilder(couchbaseSource1ConfigStrWithSpecialChars, configBuilder, expCouchbaseSourceWithSpecialCharsConfigObj);
  }

  @Test(description = "Tests Pinot config")
  public void pinotConfigTest() {
    testConfigBuilder(pinotSource1ConfigStr, configBuilder, expPinotSource1ConfigObj);
  }
}

