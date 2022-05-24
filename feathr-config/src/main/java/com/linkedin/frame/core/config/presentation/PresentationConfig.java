package com.linkedin.frame.core.config.presentation;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.common.PresentationFunction;
import com.linkedin.frame.common.PresentationInlineMappingInfo;
import com.linkedin.frame.common.PresentationTableMappingInfo;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Represents a presentation config for a feature
 */
public class PresentationConfig implements ConfigObj {
  private final String _memberViewFeatureName;
  private final String _linkedInViewFeatureName;

  private final String _featureDescription;
  private final Optional<String> _valueTranslation;
  private final List<ExportMode> _exportModes;
  private final boolean _isValueExportable;
  private final Optional<String> _inferenceCategory;
  private final Optional<PresentationTableMappingInfo> _presentationTableMapping;
  private final Optional<PresentationInlineMappingInfo> _presentationInlineMapping;
  private final Optional<List<PresentationFunction>> _presentationFunctionMapping;

  public static final String MEMBER_VIEW_NAME = "memberViewFeatureName";
  public static final String LINKEDIN_VIEW_NAME = "linkedInViewFeatureName";
  public static final String FEATURE_DESCRIPTION = "featureDescription";
  @Deprecated
  public static final String VALUE_TRANSLATION = "valueTranslation"; // deprecated field
  public static final String EXPORT_MODES = "exportModes";
  public static final String IS_VALUE_EXPORTABLE = "isValueExportable";
  public static final String INFERENCE_CATEGORY = "inferenceCategory";

  public static final String PRESENTATION_TABLE_MAPPING = "presentationTableMapping";
  public static final String DATASET = "dataset";
  public static final String KEY_COLUMNS = "keyColumns";
  public static final String VALUE_COLUMN = "valueColumn";

  public static final String PRESENTATION_INLINE_MAPPING = "presentationInlineMapping";
  public static final String NAME = "name";
  public static final String MAPPING = "mapping";

  public static final String PRESENTATION_FUNCTION_MAPPING = "presentationFunctionMapping";
  public static final String FUNCTION_TYPE = "functionType";
  public static final String PARAMS = "params";

  public static final String PRESENTATION_CONFIG_NAME = "Frame Presentation config";

  private String _configStr;

  /**
   * Deprecated, please use {@link PresentationConfig.Builder} instead
   */
  @Deprecated
  public PresentationConfig(String memberViewFeatureName, String linkedInViewFeatureName, String featureDescription,
      String valueTranslation, List<ExportMode> exportModes, boolean isValueExportable) {
    this(memberViewFeatureName, linkedInViewFeatureName, featureDescription, valueTranslation, exportModes,
        isValueExportable, null, null, null, null);
  }

  /**
   * Please use {@link PresentationConfig.Builder} as there are too many parameters for the constructor.
   */
  private PresentationConfig(String memberViewFeatureName, String linkedInViewFeatureName, String featureDescription,
      String valueTranslation, List<ExportMode> exportModes, boolean isValueExportable, String inferenceCategory,
      PresentationTableMappingInfo presentationTableMapping, PresentationInlineMappingInfo presentationInlineMapping,
      List<PresentationFunction> presentationFunctionMapping) {
    _memberViewFeatureName = memberViewFeatureName;
    _linkedInViewFeatureName = linkedInViewFeatureName;
    _featureDescription = featureDescription;
    _valueTranslation = Optional.ofNullable(valueTranslation);
    _exportModes = exportModes;
    _isValueExportable = isValueExportable;
    _inferenceCategory = Optional.ofNullable(inferenceCategory);
    _presentationTableMapping = Optional.ofNullable(presentationTableMapping);
    _presentationInlineMapping = Optional.ofNullable(presentationInlineMapping);
    _presentationFunctionMapping = Optional.ofNullable(presentationFunctionMapping);
  }

  public String getMemberViewFeatureName() {
    return _memberViewFeatureName;
  }

  public String getLinkedInViewFeatureName() {
    return _linkedInViewFeatureName;
  }

  public String getFeatureDescription() {
    return _featureDescription;
  }

  @Deprecated
  public Optional<String> getValueTranslation() {
    return _valueTranslation;
  }

  public List<ExportMode> getExportModes() {
    return _exportModes;
  }

  public boolean isValueExportable() {
    return _isValueExportable;
  }

  public Optional<String> getInferenceCategory() {
    return _inferenceCategory;
  }

  public Optional<PresentationTableMappingInfo> getPresentationTableMapping() {
    return _presentationTableMapping;
  }

  public Optional<PresentationInlineMappingInfo> getPresentationInlineMapping() {
    return _presentationInlineMapping;
  }

  public Optional<List<PresentationFunction>> getPresentationFunctionMapping() {
    return _presentationFunctionMapping;
  }

  @Override
  public String toString() {
    if (_configStr == null) {
      StringBuilder configStrBuilder = new StringBuilder();
      configStrBuilder.append(String.join("\n",
          MEMBER_VIEW_NAME + ":" + _memberViewFeatureName,
          LINKEDIN_VIEW_NAME + ":" + _linkedInViewFeatureName,
          FEATURE_DESCRIPTION + ":" + _featureDescription,
          EXPORT_MODES + ":" + _exportModes,
          IS_VALUE_EXPORTABLE + ":" + _isValueExportable));
      // add optional field if present
      _valueTranslation.ifPresent(valueTranslation -> configStrBuilder.append(VALUE_TRANSLATION).append(":")
          .append(valueTranslation).append("\n"));
      _inferenceCategory.ifPresent(inferenceCategory -> configStrBuilder.append(INFERENCE_CATEGORY).append(":")
          .append(inferenceCategory).append("\n"));
      _presentationTableMapping.ifPresent(presentationTableMapping -> configStrBuilder
          .append(PRESENTATION_TABLE_MAPPING).append(":").append(presentationTableMapping).append("\n"));
      _presentationInlineMapping.ifPresent(presentationInlineMapping -> configStrBuilder
          .append(PRESENTATION_INLINE_MAPPING).append(":").append(presentationInlineMapping).append("\n"));
      _presentationFunctionMapping.ifPresent(list -> configStrBuilder.append(PRESENTATION_FUNCTION_MAPPING).append(":")
          .append(list.stream().map(PresentationFunction::toString)
              .collect(Collectors.joining("\n", "[", "]"))).append("\n"));

      _configStr = configStrBuilder.toString();
  }

    return _configStr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PresentationConfig that = (PresentationConfig) o;
    return _isValueExportable == that._isValueExportable && Objects.equals(_memberViewFeatureName,
        that._memberViewFeatureName) && Objects.equals(_linkedInViewFeatureName, that._linkedInViewFeatureName)
        && Objects.equals(_featureDescription, that._featureDescription) && Objects.equals(_valueTranslation,
        that._valueTranslation) && Objects.equals(_exportModes, that._exportModes) && Objects.equals(_inferenceCategory,
         that._inferenceCategory) && Objects.equals(_presentationTableMapping, that._presentationTableMapping)
        && Objects.equals(_presentationInlineMapping, that._presentationInlineMapping)
        && Objects.equals(_presentationFunctionMapping, that._presentationFunctionMapping);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_memberViewFeatureName, _linkedInViewFeatureName, _featureDescription, _valueTranslation,
        _exportModes, _isValueExportable, _inferenceCategory, _presentationTableMapping, _presentationInlineMapping,
        _presentationFunctionMapping);
  }

  /**
   * The builder for {@link PresentationConfig}
   */
  public static class Builder {
    private String _memberViewFeatureName;
    private String _linkedInViewFeatureName;

    private String _featureDescription;
    private List<ExportMode> _exportModes;
    private boolean _isValueExportable;
    private String _inferenceCategory;
    private PresentationTableMappingInfo _presentationTableMapping;
    private PresentationInlineMappingInfo _presentationInlineMapping;
    private List<PresentationFunction> _presentationFunctionMapping;

    /**
     * Set memberViewFeatureName
     * @param memberViewFeatureName the "memberViewFeatureName" field, which is the member facing feature name
     * @return this builder
     */
    public Builder setMemberViewFeatureName(String memberViewFeatureName) {
      this._memberViewFeatureName = memberViewFeatureName;
      return this;
    }

    /**
     * Set linkedInViewFeatureName
     * @param linkedInViewFeatureName the "linkedInViewFeatureName" field, which is the FeatureRef string in Frame
     * @return this builder
     */
    public Builder setLinkedInViewFeatureName(String linkedInViewFeatureName) {
      this._linkedInViewFeatureName = linkedInViewFeatureName;
      return this;
    }

    /**
     * Set featureDescription
     * @param featureDescription the "featureDescription" field, which is the member facing feature description
     * @return this builder
     */
    public Builder setFeatureDescription(String featureDescription) {
      this._featureDescription = featureDescription;
      return this;
    }

    /**
     * Set export modes
     * @param exportModes the list of modes in "exportModes" field
     * @return this builder
     */
    public Builder setExportModes(List<ExportMode> exportModes) {
      this._exportModes = exportModes;
      return this;
    }

    /**
     * Set isValueExportable
     * @param isValueExportable the boolean value set in "isValueExportable" field
     * @return this builder
     */
    public Builder setIsValueExportable(boolean isValueExportable) {
      this._isValueExportable = isValueExportable;
      return this;
    }

    /**
     * Set InferenceCategory
     * @param inferenceCategory the "inferenceCategory" field, which contains the string to construct the
     *                          InferenceCategoryUrn. For instance, if the InferenceCategoryUrn is
     *                          "urn:li:inferenceCategory:CAREER_INFERENCES", then the string here will be
     *                          "CAREER_INFERENCES".
     * @return this builder
     */
    public Builder setInferenceCategory(String inferenceCategory) {
      this._inferenceCategory = inferenceCategory;
      return this;
    }

    /**
     * Set presentationTableMapping
     * @param presentationTableMapping the {@link PresentationTableMappingInfo} built from the "presentationTableMapping" field
     * @return this builder
     */
    public Builder setPresentationTableMapping(PresentationTableMappingInfo presentationTableMapping) {
      this._presentationTableMapping = presentationTableMapping;
      return this;
    }

    /**
     * Set presentationInlineMapping
     * @param presentationInlineMapping the {@link PresentationInlineMappingInfo} built from the
     *                                  "presentationInlineMapping" field in the config
     * @return this builder
     */
    public Builder setPresentationInlineMapping(PresentationInlineMappingInfo presentationInlineMapping) {
      this._presentationInlineMapping = presentationInlineMapping;
      return this;
    }

    /**
     * Set PresentationFunctionMapping
     * @param presentationFunctionMapping the list of {@link PresentationFunction} build from the list of configs inside
     *                                    the "presentationFunctionMapping" field
     * @return this builder
     */
    public Builder setPresentationFunctionMapping(List<PresentationFunction> presentationFunctionMapping) {
      this._presentationFunctionMapping = presentationFunctionMapping;
      return this;
    }

    /**
     * Build a new {@link PresentationConfig} with existing parameters
     * @return {@link PresentationConfig} object
     */
    public PresentationConfig build() {
      return new PresentationConfig(this._memberViewFeatureName, this._linkedInViewFeatureName, this._featureDescription,
          null, this._exportModes, this._isValueExportable, this._inferenceCategory,
          this._presentationTableMapping, this._presentationInlineMapping, this._presentationFunctionMapping);
    }
  }
}
