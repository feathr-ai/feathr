package com.feathr.online;


/**
 * This is to enhance the capability of source client that features the raw data.
 * Implement this if you need to transform your EntityKeys
 *
 * You can add additional parameters if it's useful.
 */
public class MyFeatureSourceOverrider implements FeatureSourceOverriderInterface {
  private String sourceName;

  public MyFeatureSourceOverrider(String sourceName) {
    this.sourceName = sourceName;
  }

  /**
   * Override parameters used in the source if needed.
   * @param parameterName
   * @return
   */
  public Object overrideParameter(String parameterName) {
    return "" + parameterName;
  }

  /**
   * In some cases, the EntityKey provided by the application, like memberId, companyName, may not exactly match
   * that of the database. So you want to transformKey here so it matches the database, like adding some prefix,
   * convert to the expected data types.
   * If you don't implement this, we use send the key over as-is.
   * @param entityKey
   * @return
   */
  public Object overrideKey(EntityKey entityKey) {
    Object databaseKey = "urn:" + entityKey.getEntityKey("memberId");
    return databaseKey;
  }
}
