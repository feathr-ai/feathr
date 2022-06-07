package com.linkedin.feathr.config.featureanchor.builder;

import com.google.common.base.Preconditions;
import com.linkedin.feathr.core.config.producer.sources.HdfsConfig;
import com.linkedin.feathr.core.config.producer.sources.HdfsConfigWithRegularData;
import com.linkedin.feathr.core.config.producer.sources.SlidingWindowAggrConfig;
import com.linkedin.feathr.core.config.producer.sources.HdfsConfigWithSlidingWindow;
import com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat;
import com.linkedin.feathr.featureDataModel.StandardDateTimeFormat;
import java.util.Optional;
import javax.annotation.Nonnull;


/**
 * Builder class for {@link DatasetSnapshotTimeFormat}
 */
class DatasetSnapshotTimeFormatBuilder {
  private final static DatasetSnapshotTimeFormatBuilder INSTANCE = new DatasetSnapshotTimeFormatBuilder();
  private DatasetSnapshotTimeFormatBuilder() {
  }

  static DatasetSnapshotTimeFormatBuilder getInstance() {
    return INSTANCE;
  }

  /**
   * Based on the hdfs config, we either build standard datetime format, a custom date time format, or nothing.
   *
   * When {@link SlidingWindowAggrConfig#getTimeSeries()} of
   * {#link HdfsConfigWithSlidingWindow} is true, or {@link HdfsConfigWithRegularData#getHasTimeSnapshot()} is true,
   * we build {@link StandardDateTimeFormat}. If {@link HdfsConfig#getTimePartitionPattern()} is not empty, we build a
   * custom string format date time pattern. Otherwise, we build nothing.
   */
  Optional<DatasetSnapshotTimeFormat> build(@Nonnull HdfsConfig hdfsConfig) {
    Preconditions.checkNotNull(hdfsConfig);
    // TODO(PROML-12604) remove legacy fields check after the users migrate to new syntax
    if (hdfsConfig instanceof HdfsConfigWithSlidingWindow
        && ((HdfsConfigWithSlidingWindow) hdfsConfig).getSwaConfig().getTimeSeries()
        || hdfsConfig instanceof HdfsConfigWithRegularData
        && ((HdfsConfigWithRegularData) hdfsConfig).getHasTimeSnapshot()) {
      if (hdfsConfig.getTimePartitionPattern().isPresent()) {
        throw new IllegalArgumentException("hasTimeSnapshot and isTimeSeries are legacy fields. They cannot coexist "
            + "with timePartitionPattern");
      }
      DatasetSnapshotTimeFormat datasetSnapshotTimeFormat = new DatasetSnapshotTimeFormat();
      datasetSnapshotTimeFormat.setStandardDateTimeFormat(new StandardDateTimeFormat());
      return Optional.of(datasetSnapshotTimeFormat);
    } else if (hdfsConfig.getTimePartitionPattern().isPresent()) {
      DatasetSnapshotTimeFormat datasetSnapshotTimeFormat = new DatasetSnapshotTimeFormat();
      datasetSnapshotTimeFormat.setDateTimeFormat(hdfsConfig.getTimePartitionPattern().get());
      return Optional.of(datasetSnapshotTimeFormat);
    } else {
      return Optional.empty();
    }
  }
}
