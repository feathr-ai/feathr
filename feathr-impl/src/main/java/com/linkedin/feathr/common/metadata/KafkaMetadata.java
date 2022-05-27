package com.linkedin.feathr.common.metadata;

import com.linkedin.frame.core.config.producer.sources.KafkaConfig;
import java.util.Objects;

import static com.linkedin.frame.core.config.producer.sources.SourceType.*;


/**
 * Metadata for Kafka source
 */
public class KafkaMetadata extends SourceMetadata {
  private final String _stream;
  private String _mdStr;

  /**
   * Constructor
   * @param stream Name of the Kafka stream
   */
  KafkaMetadata(String stream) {
    super(KAFKA);
    _stream = stream;
  }

  /**
   * Creates Kafka Metadata from {@link KafkaConfig} object
   * @param config KafkaConfig object
   */
  KafkaMetadata(KafkaConfig config) {
    this(config.getStream());
  }

  public String getStream() {
    return _stream;
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", stream: " + _stream;
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
    KafkaMetadata that = (KafkaMetadata) o;
    return Objects.equals(_stream, that._stream);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _stream);
  }
}
