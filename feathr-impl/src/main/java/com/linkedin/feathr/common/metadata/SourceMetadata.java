package com.linkedin.feathr.common.metadata;

import com.linkedin.frame.core.config.producer.sources.SourceType;
import java.util.Objects;


/**
 * Abstract class for source metadata
 */
public abstract class SourceMetadata {
  private final SourceType _sourceType;
  private String _mdStr;

  SourceMetadata(SourceType sourceType) {
    _sourceType = sourceType;
  }

  public String getSourceType() {
    return _sourceType.toString();
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = "type: " + getSourceType();
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
    SourceMetadata that = (SourceMetadata) o;
    return Objects.equals(_sourceType, that._sourceType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_sourceType);
  }
}
