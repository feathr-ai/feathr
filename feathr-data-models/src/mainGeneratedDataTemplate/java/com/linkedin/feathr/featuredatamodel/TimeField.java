
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;


/**
 * Represent a time field in the dataset schema, it is used for time-series operations. One usage is Sliding Window Aggregation, see details in go/frameoffline.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/TimeField.pdl.")
public class TimeField
    extends RecordTemplate
{

    private final static TimeField.Fields _fields = new TimeField.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represent a time field in the dataset schema, it is used for time-series operations. One usage is Sliding Window Aggregation, see details in go/frameoffline.*/record TimeField{/**Name of the time field in the dataset schema.*/name:string/**The format that the time field uses to represent time.*/format:/**The format of the time field. It can be either the granularity of a timestamp or datetime format e.g. yyyy/MM/dd/HH/mm/ss.*/typeref TimeFieldFormat=union[/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string]}", SchemaFormatType.PDL));
    private String _nameField = null;
    private TimeFieldFormat _formatField = null;
    private TimeField.ChangeListener __changeListener = new TimeField.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Name = SCHEMA.getField("name");
    private final static RecordDataSchema.Field FIELD_Format = SCHEMA.getField("format");

    public TimeField() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public TimeField(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TimeField.Fields fields() {
        return _fields;
    }

    public static TimeField.ProjectionMask createMask() {
        return new TimeField.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for name
     * 
     * @see TimeField.Fields#name
     */
    public boolean hasName() {
        if (_nameField!= null) {
            return true;
        }
        return super._map.containsKey("name");
    }

    /**
     * Remover for name
     * 
     * @see TimeField.Fields#name
     */
    public void removeName() {
        super._map.remove("name");
    }

    /**
     * Getter for name
     * 
     * @see TimeField.Fields#name
     */
    public String getName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getName();
            case DEFAULT:
            case NULL:
                if (_nameField!= null) {
                    return _nameField;
                } else {
                    Object __rawValue = super._map.get("name");
                    _nameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _nameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for name
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TimeField.Fields#name
     */
    @Nonnull
    public String getName() {
        if (_nameField!= null) {
            return _nameField;
        } else {
            Object __rawValue = super._map.get("name");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("name");
            }
            _nameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _nameField;
        }
    }

    /**
     * Setter for name
     * 
     * @see TimeField.Fields#name
     */
    public TimeField setName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field name of com.linkedin.feathr.featureDataModel.TimeField");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for name
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimeField.Fields#name
     */
    public TimeField setName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field name of com.linkedin.feathr.featureDataModel.TimeField to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "name", value);
            _nameField = value;
        }
        return this;
    }

    /**
     * Existence checker for format
     * 
     * @see TimeField.Fields#format
     */
    public boolean hasFormat() {
        if (_formatField!= null) {
            return true;
        }
        return super._map.containsKey("format");
    }

    /**
     * Remover for format
     * 
     * @see TimeField.Fields#format
     */
    public void removeFormat() {
        super._map.remove("format");
    }

    /**
     * Getter for format
     * 
     * @see TimeField.Fields#format
     */
    public TimeFieldFormat getFormat(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFormat();
            case DEFAULT:
            case NULL:
                if (_formatField!= null) {
                    return _formatField;
                } else {
                    Object __rawValue = super._map.get("format");
                    _formatField = ((__rawValue == null)?null:new TimeFieldFormat(__rawValue));
                    return _formatField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for format
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TimeField.Fields#format
     */
    @Nonnull
    public TimeFieldFormat getFormat() {
        if (_formatField!= null) {
            return _formatField;
        } else {
            Object __rawValue = super._map.get("format");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("format");
            }
            _formatField = ((__rawValue == null)?null:new TimeFieldFormat(__rawValue));
            return _formatField;
        }
    }

    /**
     * Setter for format
     * 
     * @see TimeField.Fields#format
     */
    public TimeField setFormat(TimeFieldFormat value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFormat(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field format of com.linkedin.feathr.featureDataModel.TimeField");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "format", value.data());
                    _formatField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFormat();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "format", value.data());
                    _formatField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "format", value.data());
                    _formatField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for format
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimeField.Fields#format
     */
    public TimeField setFormat(
        @Nonnull
        TimeFieldFormat value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field format of com.linkedin.feathr.featureDataModel.TimeField to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "format", value.data());
            _formatField = value;
        }
        return this;
    }

    @Override
    public TimeField clone()
        throws CloneNotSupportedException
    {
        TimeField __clone = ((TimeField) super.clone());
        __clone.__changeListener = new TimeField.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TimeField copy()
        throws CloneNotSupportedException
    {
        TimeField __copy = ((TimeField) super.copy());
        __copy._nameField = null;
        __copy._formatField = null;
        __copy.__changeListener = new TimeField.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TimeField __objectRef;

        private ChangeListener(TimeField reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "name":
                    __objectRef._nameField = null;
                    break;
                case "format":
                    __objectRef._formatField = null;
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
         * Name of the time field in the dataset schema.
         * 
         */
        public PathSpec name() {
            return new PathSpec(getPathComponents(), "name");
        }

        /**
         * The format that the time field uses to represent time.
         * 
         */
        public com.linkedin.feathr.featureDataModel.TimeFieldFormat.Fields format() {
            return new com.linkedin.feathr.featureDataModel.TimeFieldFormat.Fields(getPathComponents(), "format");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.TimeFieldFormat.ProjectionMask _formatMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Name of the time field in the dataset schema.
         * 
         */
        public TimeField.ProjectionMask withName() {
            getDataMap().put("name", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The format that the time field uses to represent time.
         * 
         */
        public TimeField.ProjectionMask withFormat(Function<com.linkedin.feathr.featureDataModel.TimeFieldFormat.ProjectionMask, com.linkedin.feathr.featureDataModel.TimeFieldFormat.ProjectionMask> nestedMask) {
            _formatMask = nestedMask.apply(((_formatMask == null)?TimeFieldFormat.createMask():_formatMask));
            getDataMap().put("format", _formatMask.getDataMap());
            return this;
        }

        /**
         * The format that the time field uses to represent time.
         * 
         */
        public TimeField.ProjectionMask withFormat() {
            _formatMask = null;
            getDataMap().put("format", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
