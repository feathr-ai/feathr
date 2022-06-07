package com.linkedin.feathr.common.metadata;

import com.linkedin.feathr.core.config.producer.sources.VeniceConfig;
import java.util.Objects;

import static com.linkedin.feathr.core.config.producer.sources.SourceType.*;

/**
 * Metadata for Venice source
 */
public class VeniceMetadata extends SourceMetadata {
  private final String _storeName;
  private String _mdStr;

  /**
   * Constructor
   * @param storeName The name of the store
   */
  VeniceMetadata(String storeName) {
    super(VENICE);
    _storeName = storeName;
  }

  /**
   * Creates Venice Metadata from {@link VeniceConfig} object
   * @param config VeniceConfig object
   */
  VeniceMetadata(VeniceConfig config) {
    this(config.getStoreName());
  }

  public String getStoreName() {
    return _storeName;
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", storeName: " + _storeName;
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
    VeniceMetadata that = (VeniceMetadata) o;
    return Objects.equals(_storeName, that._storeName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _storeName);
  }
}
