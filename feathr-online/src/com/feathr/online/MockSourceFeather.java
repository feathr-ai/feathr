package com.feathr.online;

/**
 * A mock source data fetcher that fetches whatever data you like.
 *
 * In real implementation, it will talk to the database users specified.
 */
public class MockSourceFeather {

  Object getSourceData(String sourceName) {
    // return sth
    if (sourceName.equals("aerospike")) {
      return new MySampleSourceDataModel();
    } else if (sourceName.equals("requestSource")) {
      // TODO: return it from EntityKey. For now, just create it out of thin air for simplicity.
      return new MyApplicationRequestData();
    } else {
      return "software_engineer";
    }
  }

  class MySampleSourceDataModel implements SampleSourceDataModel {

    @Override
    public double getValue() {
      return 500;
    }

    @Override
    public String getCurrency() {
      return "euro";
    }
  }
}
