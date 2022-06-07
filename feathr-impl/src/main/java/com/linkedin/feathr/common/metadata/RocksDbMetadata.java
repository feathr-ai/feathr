package com.linkedin.feathr.common.metadata;

import com.linkedin.feathr.core.config.producer.sources.RocksDbConfig;
import java.util.Objects;

import static com.linkedin.feathr.core.config.producer.sources.SourceType.*;

/**
 * Metadata for RocksDB source
 */
public class RocksDbMetadata extends SourceMetadata {
  private final String _referenceSource;
  private String _mdStr;

  /**
   * Constructor
   * @param referenceSource the (Kafka) source used by RocksDB source
   */
  RocksDbMetadata(String referenceSource) {
    super(ROCKSDB);
    _referenceSource = referenceSource;
  }

  /**
   * Creates RocksDB Metadata from {@link RocksDbConfig} object
   * @param config RocksDbConfig object
   */
  RocksDbMetadata(RocksDbConfig config) {
    this(config.getReferenceSource());
  }

  public String getReferenceSource() {
    return _referenceSource;
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", referenceSource: " + _referenceSource;
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
    RocksDbMetadata that = (RocksDbMetadata) o;
    return Objects.equals(_referenceSource, that._referenceSource);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _referenceSource);
  }
}
