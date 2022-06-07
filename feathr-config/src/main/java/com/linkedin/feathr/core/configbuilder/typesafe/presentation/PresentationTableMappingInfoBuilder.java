package com.linkedin.feathr.core.configbuilder.typesafe.presentation;

import com.linkedin.frame.common.urn.DatasetUrn;
import com.linkedin.frame.common.urn.Urn;
import com.linkedin.data.template.StringArray;
import com.linkedin.feathr.core.config.presentation.PresentationConfig;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.linkedin.frame.common.PresentationTableMappingInfo;
import com.typesafe.config.Config;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

/**
 * PresentationTableMappingInfo for Frame presentation config
 * Example:
 * <pre>
 *   {@code
 *       presentationTableMapping: {
 *         dataset: "urn:li:dataset:(urn:li:dataPlatform:hive,hive.table1,PROD)"
 *         keyColumns: ["keyColumn1", "keyColumn2"]
 *         valueColumn: "valueColumnName"
 *     }
 *   }
 * </pre>
 */
class PresentationTableMappingInfoBuilder {

  private final static Logger logger = Logger.getLogger(PresentationTableMappingInfoBuilder.class);
  private static final PresentationTableMappingInfoBuilder INSTANCE = new PresentationTableMappingInfoBuilder();
  static PresentationTableMappingInfoBuilder getInstance() {
    return INSTANCE;
  }

  private PresentationTableMappingInfoBuilder() {

  }

  PresentationTableMappingInfo build(Config config) {
    List<String> missingFields = Stream.of(PresentationConfig.DATASET, PresentationConfig.KEY_COLUMNS,
        PresentationConfig.VALUE_COLUMN).filter(s -> !config.hasPath(s)).collect(Collectors.toList());

    if (!missingFields.isEmpty()) {
      throw new ConfigBuilderException("The following fields are missing when building PresentationTableMappingInfo: "
          + String.join(", ", missingFields));
    }

    String datasetStr = config.getString(PresentationConfig.DATASET);
    List<String> keyColumns = config.getStringList(PresentationConfig.KEY_COLUMNS);
    String valueColumn = config.getString(PresentationConfig.VALUE_COLUMN);

    try {
      DatasetUrn datasetUrn = DatasetUrn.createFromUrn(Urn.createFromString(datasetStr));
      logger.trace("Built PresentationTableMappingInfo object");
      return new PresentationTableMappingInfo().setDataset(datasetUrn).setKeyColumns(new StringArray(keyColumns))
          .setValueColumn(valueColumn);
    } catch (URISyntaxException e) {
      throw new ConfigBuilderException("Failed to build PresentationTableMappingInfo as the dataset URN is not valid: "
          + datasetStr);
    }
  }
}
