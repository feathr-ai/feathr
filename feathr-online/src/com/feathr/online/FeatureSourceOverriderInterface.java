package com.feathr.online;


/**
 * This is to enhance the capability of source client that features the raw data.
 * Implement this if you need to transform your EntityKeys
 *
 * You can add additional parameters if it's useful.
 */
public interface FeatureSourceOverriderInterface {
  Object overrideParameter(String parameterName);

  Object overrideKey(EntityKey entityKey);
}
