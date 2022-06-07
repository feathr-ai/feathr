package com.linkedin.feathr.common.metadata;

import com.linkedin.feathr.core.config.producer.sources.PinotConfig;
import com.linkedin.feathr.core.config.producer.sources.SourceType;
import java.util.Objects;


/**
 * Metadata for Pinot source
 */
public class PinotMetadata extends SourceMetadata {
  private final String _resourceName;
  private final String _queryTemplate;
  private String _mdStr;

  /**
   * Constructor
   * @param resourceName D2 resoure name corresponding to the Pinot table
   * @param queryTemplate The query template to fetch data from Pinot
   */
  PinotMetadata(String resourceName, String queryTemplate) {
    super(SourceType.PINOT);
    _resourceName = resourceName;
    _queryTemplate = queryTemplate;
  }

  /**
   * Creates Espresso Metadata from {@link PinotConfig} object
   * @param config EspressoConfig object
   */
  PinotMetadata(PinotConfig config) {
    this(config.getResourceName(), config.getQueryTemplate());
  }

  public String getResourceName() {
    return _resourceName;
  }

  public String getQueryTemplate() {
    return _queryTemplate;
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", resourceName: " + _resourceName + ", queryTemplate: " + _queryTemplate;
    }

    return _mdStr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }
    PinotMetadata that = (PinotMetadata) o;
    return Objects.equals(_resourceName, that._resourceName) && Objects.equals(_queryTemplate, that._queryTemplate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _resourceName, _queryTemplate);
  }
}

