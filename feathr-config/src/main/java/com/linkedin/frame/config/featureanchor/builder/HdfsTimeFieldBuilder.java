package com.linkedin.frame.config.featureanchor.builder;

import com.linkedin.frame.core.config.producer.sources.TimeWindowParams;
import com.linkedin.feathr.featureDataModel.TimeField;
import com.linkedin.feathr.featureDataModel.TimeFieldFormat;
import com.linkedin.feathr.featureDataModel.TimestampGranularity;
import java.time.format.DateTimeFormatter;

import static com.linkedin.frame.core.config.producer.sources.TimeWindowParams.*;


class HdfsTimeFieldBuilder {
  private final TimeWindowParams _timeWindowParams;

  public HdfsTimeFieldBuilder(TimeWindowParams timeWindowParams) {
    _timeWindowParams = timeWindowParams;
  }

  /**
   * @throws IllegalArgumentException when the provided timestamp format is not a valid DateTime format.
   */
  public TimeField build() {
    TimeField timeField = new TimeField();
    timeField.setName(_timeWindowParams.getTimestampField());
    TimeFieldFormat timeFieldFormat = new TimeFieldFormat();
    if (_timeWindowParams.getTimestampFormat().equalsIgnoreCase(TIMESTAMP_EPOCH_SECOND_FORMAT)) {
      timeFieldFormat.setTimestampGranularity(TimestampGranularity.SECONDS);
      timeField.setFormat(timeFieldFormat);
    } else if (_timeWindowParams.getTimestampFormat().equalsIgnoreCase(TIMESTAMP_EPOCH_MILLISECOND_FORMAT)) {
      timeFieldFormat.setTimestampGranularity(TimestampGranularity.MILLISECONDS);
      timeField.setFormat(timeFieldFormat);
    } else {
      String timestampFormat = _timeWindowParams.getTimestampFormat();
      DateTimeFormatter.ofPattern(timestampFormat);
      timeFieldFormat.setDateTimeFormat(timestampFormat);
      timeField.setFormat(timeFieldFormat);
    }
    return timeField;
  }
}