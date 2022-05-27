package com.linkedin.feathr.offline.fds;

import com.linkedin.feathr.fds.ColumnMetadata;
import com.linkedin.feathr.fds.ColumnMetadataMap;
import com.linkedin.feathr.fds.FeatureColumnMetadata;
import com.linkedin.feathr.fds.FeaturizedDatasetMetadata;
import com.linkedin.feathr.fds.FeaturizedDatasetSchemaVersion;
import com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata;
import java.util.Map;


/**
 * A builder class for assembling {@link FeaturizedDatasetMetadata} column by column.
 */
public class FeaturizedDatasetMetadataBuilder {
  private FeaturizedDatasetSchemaVersion _fdsVersion = FdsVersions.getInstance().getLatestActiveVersion();
  private ColumnMetadataMap _columnMetadataMap = new ColumnMetadataMap();

  /**
   * Builds and returns the {@link FeaturizedDatasetMetadata} based on the state set by the other methods.
   */
  public FeaturizedDatasetMetadata build() {
    FeaturizedDatasetTopLevelMetadata topLevelMetadata = new FeaturizedDatasetTopLevelMetadata();
    topLevelMetadata.setFdsSchemaVersion(_fdsVersion);
    return new FeaturizedDatasetMetadata()
        .setColumnMetadata(_columnMetadataMap)
        .setTopLevelMetadata(topLevelMetadata);
  }

  /**
   * Adds a map of {@link ColumnMetadata} to this builder
   */
  public FeaturizedDatasetMetadataBuilder addAllColumnMetadata(Map<String, ColumnMetadata> columnMetadataMap) {
    _columnMetadataMap.putAll(columnMetadataMap);
    return this;
  }

  /**
   * Adds a feature column to this builder
   */
  public FeaturizedDatasetMetadataBuilder addFeatureColumn(String featureName, FeatureColumnMetadata featureColumnMetadata) {
    _columnMetadataMap.put(featureName, new ColumnMetadata().setMetadata(ColumnMetadata.Metadata.create(featureColumnMetadata)));
    return this;
  }
}
