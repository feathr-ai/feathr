package com.linkedin.feathr.common.metadata;

import com.linkedin.frame.core.config.producer.sources.HdfsConfig;
import java.util.Objects;

import static com.linkedin.frame.core.config.producer.sources.SourceType.*;


/**
 * Metadata for HDFS source
 */
public class HdfsMetadata extends SourceMetadata {
  private final String _path;
  private String _mdStr;

  /**
   * Constructor
   *
   * @param path HDFS path or Dali URI representing an underlying HDFS source
   */
  HdfsMetadata(String path) {
    super(HDFS);
    _path = path;
  }

  /**
   * Creates HDFS Metadata from the {@link HdfsConfig} object
   * @param config HdfsConfig object
   */
  HdfsMetadata(HdfsConfig config) {
    this(config.getPath());
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", path: " + _path;
    }

    return _mdStr;
  }

  public String getPath() {
    return _path;
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
    HdfsMetadata that = (HdfsMetadata) o;
    return Objects.equals(_path, that._path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _path);
  }
}
