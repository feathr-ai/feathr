package com.linkedin.feathr.core.config.presentation;

/**
 * Enumeration class for inference takeout mode
 */
public enum ExportMode {
  TAKEOUT,
  EATIN,
  TIER3
  /*
   * Warning: For any new item added, please make sure to change the code in MceBuilder.toFeatureInferenceExportMode
   *  to convert ExportMode to FeatureInferenceExportMode.
   *
   * The conversion is not implemented here as we do not want to introduce the dependency of 'mxe-avro' module of
   *  metadata-models MP, where FeatureInferenceExportMode is defined, in the current module.
   */
}
