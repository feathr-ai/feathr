package com.linkedin.feathr.common.metadata;

import com.linkedin.frame.core.config.producer.sources.PassThroughConfig;
import java.util.Objects;
import java.util.Optional;

import static com.linkedin.frame.core.config.producer.sources.SourceType.*;


public class PassThroughMetadata extends SourceMetadata {
  private final String _dataModel;
  private String _mdStr;

  /**
   * Constructor
   * @param dataModel Name of data model class
   */
  PassThroughMetadata(String dataModel) {
    super(PASSTHROUGH);
    _dataModel = dataModel;
  }

  /**
   * Creates PassThroughMetadata from {@link PassThroughConfig} object
   * @param config PassthroughConfig object
   */
  PassThroughMetadata(PassThroughConfig config) {
    this(config.getDataModel().orElse(null));
  }

  public Optional<String> getDataModel() {
    return Optional.ofNullable(_dataModel);
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", dataModel: " + _dataModel;
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
    PassThroughMetadata that = (PassThroughMetadata) o;
    return Objects.equals(_dataModel, that._dataModel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _dataModel);
  }
}
