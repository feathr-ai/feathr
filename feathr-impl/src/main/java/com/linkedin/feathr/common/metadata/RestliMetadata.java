package com.linkedin.feathr.common.metadata;

import com.linkedin.feathr.core.config.producer.sources.RestliConfig;
import java.util.Objects;

import static com.linkedin.feathr.core.config.producer.sources.SourceType.*;

/**
 * Metadata for RestLi source
 */
public class RestliMetadata extends SourceMetadata {
  private final String _resourceName;
  private String _mdStr;

  /**
   * Constructor
   * @param resourceName Name of the resource
   */
  RestliMetadata(String resourceName) {
    super(RESTLI);
    _resourceName = resourceName;
  }

  /**
   * Creates RestLi Metadata from {@link RestliConfig} object
   * @param config RestliConfig object
   */
  RestliMetadata(RestliConfig config) {
    this(config.getResourceName());
  }

  public String getResourceName() {
    return _resourceName;
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", resourceName: " + _resourceName;
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
    RestliMetadata that = (RestliMetadata) o;
    return Objects.equals(_resourceName, that._resourceName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _resourceName);
  }
}
