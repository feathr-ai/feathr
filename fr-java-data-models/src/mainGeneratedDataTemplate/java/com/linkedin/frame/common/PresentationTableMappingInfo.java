
package com.linkedin.frame.common;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.StringArray;


/**
 * Presentation instructions defining how to fetch member facing value of an inference from a mapping table
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from fr-java-data-models/src/main/pegasus/com/linkedin/frame/common/PresentationTableMappingInfo.pdl.")
public class PresentationTableMappingInfo
    extends RecordTemplate
{

    private final static PresentationTableMappingInfo.Fields _fields = new PresentationTableMappingInfo.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.frame.common/**Presentation instructions defining how to fetch member facing value of an inference from a mapping table*/@gma.aspect.entity.urn=\"com.linkedin.common.InferenceUrn\"record PresentationTableMappingInfo{/**Dataset urn in which the mapping exists*/dataset:@java.class=\"com.linkedin.frame.common.urn.DatasetUrn\"typeref DatasetUrn=string/**Array of key columns that need to be joined with the source to get the presentation value. For example, (titleid + language) need to be joined to get the title name from the standardization mapping table*/keyColumns:array[string]/**Name of the column containing inference value*/valueColumn:string}", SchemaFormatType.PDL));
    private com.linkedin.frame.common.urn.DatasetUrn _datasetField = null;
    private StringArray _keyColumnsField = null;
    private String _valueColumnField = null;
    private PresentationTableMappingInfo.ChangeListener __changeListener = new PresentationTableMappingInfo.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Dataset = SCHEMA.getField("dataset");
    private final static RecordDataSchema.Field FIELD_KeyColumns = SCHEMA.getField("keyColumns");
    private final static RecordDataSchema.Field FIELD_ValueColumn = SCHEMA.getField("valueColumn");

    static {
        Custom.initializeCustomClass(com.linkedin.frame.common.urn.DatasetUrn.class);
    }

    public PresentationTableMappingInfo() {
        super(new DataMap(4, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public PresentationTableMappingInfo(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static PresentationTableMappingInfo.Fields fields() {
        return _fields;
    }

    public static PresentationTableMappingInfo.ProjectionMask createMask() {
        return new PresentationTableMappingInfo.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for dataset
     * 
     * @see PresentationTableMappingInfo.Fields#dataset
     */
    public boolean hasDataset() {
        if (_datasetField!= null) {
            return true;
        }
        return super._map.containsKey("dataset");
    }

    /**
     * Remover for dataset
     * 
     * @see PresentationTableMappingInfo.Fields#dataset
     */
    public void removeDataset() {
        super._map.remove("dataset");
    }

    /**
     * Getter for dataset
     * 
     * @see PresentationTableMappingInfo.Fields#dataset
     */
    public com.linkedin.frame.common.urn.DatasetUrn getDataset(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDataset();
            case DEFAULT:
            case NULL:
                if (_datasetField!= null) {
                    return _datasetField;
                } else {
                    Object __rawValue = super._map.get("dataset");
                    _datasetField = DataTemplateUtil.coerceCustomOutput(__rawValue, com.linkedin.frame.common.urn.DatasetUrn.class);
                    return _datasetField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for dataset
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PresentationTableMappingInfo.Fields#dataset
     */
    @Nonnull
    public com.linkedin.frame.common.urn.DatasetUrn getDataset() {
        if (_datasetField!= null) {
            return _datasetField;
        } else {
            Object __rawValue = super._map.get("dataset");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("dataset");
            }
            _datasetField = DataTemplateUtil.coerceCustomOutput(__rawValue, com.linkedin.frame.common.urn.DatasetUrn.class);
            return _datasetField;
        }
    }

    /**
     * Setter for dataset
     * 
     * @see PresentationTableMappingInfo.Fields#dataset
     */
    public PresentationTableMappingInfo setDataset(com.linkedin.frame.common.urn.DatasetUrn value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDataset(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field dataset of com.linkedin.frame.common.PresentationTableMappingInfo");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataset", DataTemplateUtil.coerceCustomInput(value, com.linkedin.frame.common.urn.DatasetUrn.class));
                    _datasetField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDataset();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataset", DataTemplateUtil.coerceCustomInput(value, com.linkedin.frame.common.urn.DatasetUrn.class));
                    _datasetField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dataset", DataTemplateUtil.coerceCustomInput(value, com.linkedin.frame.common.urn.DatasetUrn.class));
                    _datasetField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dataset
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PresentationTableMappingInfo.Fields#dataset
     */
    public PresentationTableMappingInfo setDataset(
        @Nonnull
        com.linkedin.frame.common.urn.DatasetUrn value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataset of com.linkedin.frame.common.PresentationTableMappingInfo to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataset", DataTemplateUtil.coerceCustomInput(value, com.linkedin.frame.common.urn.DatasetUrn.class));
            _datasetField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyColumns
     * 
     * @see PresentationTableMappingInfo.Fields#keyColumns
     */
    public boolean hasKeyColumns() {
        if (_keyColumnsField!= null) {
            return true;
        }
        return super._map.containsKey("keyColumns");
    }

    /**
     * Remover for keyColumns
     * 
     * @see PresentationTableMappingInfo.Fields#keyColumns
     */
    public void removeKeyColumns() {
        super._map.remove("keyColumns");
    }

    /**
     * Getter for keyColumns
     * 
     * @see PresentationTableMappingInfo.Fields#keyColumns
     */
    public StringArray getKeyColumns(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeyColumns();
            case DEFAULT:
            case NULL:
                if (_keyColumnsField!= null) {
                    return _keyColumnsField;
                } else {
                    Object __rawValue = super._map.get("keyColumns");
                    _keyColumnsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keyColumnsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyColumns
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PresentationTableMappingInfo.Fields#keyColumns
     */
    @Nonnull
    public StringArray getKeyColumns() {
        if (_keyColumnsField!= null) {
            return _keyColumnsField;
        } else {
            Object __rawValue = super._map.get("keyColumns");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keyColumns");
            }
            _keyColumnsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keyColumnsField;
        }
    }

    /**
     * Setter for keyColumns
     * 
     * @see PresentationTableMappingInfo.Fields#keyColumns
     */
    public PresentationTableMappingInfo setKeyColumns(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyColumns(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyColumns of com.linkedin.frame.common.PresentationTableMappingInfo");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
                    _keyColumnsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyColumns();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
                    _keyColumnsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
                    _keyColumnsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyColumns
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PresentationTableMappingInfo.Fields#keyColumns
     */
    public PresentationTableMappingInfo setKeyColumns(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyColumns of com.linkedin.frame.common.PresentationTableMappingInfo to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
            _keyColumnsField = value;
        }
        return this;
    }

    /**
     * Existence checker for valueColumn
     * 
     * @see PresentationTableMappingInfo.Fields#valueColumn
     */
    public boolean hasValueColumn() {
        if (_valueColumnField!= null) {
            return true;
        }
        return super._map.containsKey("valueColumn");
    }

    /**
     * Remover for valueColumn
     * 
     * @see PresentationTableMappingInfo.Fields#valueColumn
     */
    public void removeValueColumn() {
        super._map.remove("valueColumn");
    }

    /**
     * Getter for valueColumn
     * 
     * @see PresentationTableMappingInfo.Fields#valueColumn
     */
    public String getValueColumn(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getValueColumn();
            case DEFAULT:
            case NULL:
                if (_valueColumnField!= null) {
                    return _valueColumnField;
                } else {
                    Object __rawValue = super._map.get("valueColumn");
                    _valueColumnField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _valueColumnField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for valueColumn
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PresentationTableMappingInfo.Fields#valueColumn
     */
    @Nonnull
    public String getValueColumn() {
        if (_valueColumnField!= null) {
            return _valueColumnField;
        } else {
            Object __rawValue = super._map.get("valueColumn");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("valueColumn");
            }
            _valueColumnField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _valueColumnField;
        }
    }

    /**
     * Setter for valueColumn
     * 
     * @see PresentationTableMappingInfo.Fields#valueColumn
     */
    public PresentationTableMappingInfo setValueColumn(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setValueColumn(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field valueColumn of com.linkedin.frame.common.PresentationTableMappingInfo");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "valueColumn", value);
                    _valueColumnField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeValueColumn();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "valueColumn", value);
                    _valueColumnField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "valueColumn", value);
                    _valueColumnField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for valueColumn
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PresentationTableMappingInfo.Fields#valueColumn
     */
    public PresentationTableMappingInfo setValueColumn(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field valueColumn of com.linkedin.frame.common.PresentationTableMappingInfo to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "valueColumn", value);
            _valueColumnField = value;
        }
        return this;
    }

    @Override
    public PresentationTableMappingInfo clone()
        throws CloneNotSupportedException
    {
        PresentationTableMappingInfo __clone = ((PresentationTableMappingInfo) super.clone());
        __clone.__changeListener = new PresentationTableMappingInfo.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public PresentationTableMappingInfo copy()
        throws CloneNotSupportedException
    {
        PresentationTableMappingInfo __copy = ((PresentationTableMappingInfo) super.copy());
        __copy._valueColumnField = null;
        __copy._keyColumnsField = null;
        __copy._datasetField = null;
        __copy.__changeListener = new PresentationTableMappingInfo.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final PresentationTableMappingInfo __objectRef;

        private ChangeListener(PresentationTableMappingInfo reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "valueColumn":
                    __objectRef._valueColumnField = null;
                    break;
                case "keyColumns":
                    __objectRef._keyColumnsField = null;
                    break;
                case "dataset":
                    __objectRef._datasetField = null;
                    break;
            }
        }

    }

    public static class Fields
        extends PathSpec
    {


        public Fields(List<String> path, String name) {
            super(path, name);
        }

        public Fields() {
            super();
        }

        /**
         * Dataset urn in which the mapping exists
         * 
         */
        public PathSpec dataset() {
            return new PathSpec(getPathComponents(), "dataset");
        }

        /**
         * Array of key columns that need to be joined with the source to get the presentation value. For example, (titleid + language) need to be joined to get the title name from the standardization mapping table
         * 
         */
        public PathSpec keyColumns() {
            return new PathSpec(getPathComponents(), "keyColumns");
        }

        /**
         * Array of key columns that need to be joined with the source to get the presentation value. For example, (titleid + language) need to be joined to get the title name from the standardization mapping table
         * 
         */
        public PathSpec keyColumns(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "keyColumns");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Name of the column containing inference value
         * 
         */
        public PathSpec valueColumn() {
            return new PathSpec(getPathComponents(), "valueColumn");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(4);
        }

        /**
         * Dataset urn in which the mapping exists
         * 
         */
        public PresentationTableMappingInfo.ProjectionMask withDataset() {
            getDataMap().put("dataset", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Array of key columns that need to be joined with the source to get the presentation value. For example, (titleid + language) need to be joined to get the title name from the standardization mapping table
         * 
         */
        public PresentationTableMappingInfo.ProjectionMask withKeyColumns() {
            getDataMap().put("keyColumns", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Array of key columns that need to be joined with the source to get the presentation value. For example, (titleid + language) need to be joined to get the title name from the standardization mapping table
         * 
         */
        public PresentationTableMappingInfo.ProjectionMask withKeyColumns(Integer start, Integer count) {
            getDataMap().put("keyColumns", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("keyColumns").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyColumns").put("$count", count);
            }
            return this;
        }

        /**
         * Name of the column containing inference value
         * 
         */
        public PresentationTableMappingInfo.ProjectionMask withValueColumn() {
            getDataMap().put("valueColumn", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
