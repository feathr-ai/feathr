
package com.linkedin.feathr.config.join;

import java.util.List;
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
 * TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can
 * be any time in the past also, we do allow a positive or negative offset length.
 *  offset - 1 day implies the previous from the reference day.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\TimeOffset.pdl.")
public class TimeOffset
    extends RecordTemplate
{

    private final static TimeOffset.Fields _fields = new TimeOffset.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\r\nbe any time in the past also, we do allow a positive or negative offset length.\r\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}", SchemaFormatType.PDL));
    private Long _lengthField = null;
    private TimeUnit _unitField = null;
    private TimeOffset.ChangeListener __changeListener = new TimeOffset.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Length = SCHEMA.getField("length");
    private final static RecordDataSchema.Field FIELD_Unit = SCHEMA.getField("unit");

    public TimeOffset() {
        super(new DataMap(3, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public TimeOffset(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TimeOffset.Fields fields() {
        return _fields;
    }

    public static TimeOffset.ProjectionMask createMask() {
        return new TimeOffset.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for length
     * 
     * @see TimeOffset.Fields#length
     */
    public boolean hasLength() {
        if (_lengthField!= null) {
            return true;
        }
        return super._map.containsKey("length");
    }

    /**
     * Remover for length
     * 
     * @see TimeOffset.Fields#length
     */
    public void removeLength() {
        super._map.remove("length");
    }

    /**
     * Getter for length
     * 
     * @see TimeOffset.Fields#length
     */
    public Long getLength(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getLength();
            case DEFAULT:
            case NULL:
                if (_lengthField!= null) {
                    return _lengthField;
                } else {
                    Object __rawValue = super._map.get("length");
                    _lengthField = DataTemplateUtil.coerceLongOutput(__rawValue);
                    return _lengthField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for length
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TimeOffset.Fields#length
     */
    @Nonnull
    public Long getLength() {
        if (_lengthField!= null) {
            return _lengthField;
        } else {
            Object __rawValue = super._map.get("length");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("length");
            }
            _lengthField = DataTemplateUtil.coerceLongOutput(__rawValue);
            return _lengthField;
        }
    }

    /**
     * Setter for length
     * 
     * @see TimeOffset.Fields#length
     */
    public TimeOffset setLength(Long value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLength(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field length of com.linkedin.feathr.config.join.TimeOffset");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "length", DataTemplateUtil.coerceLongInput(value));
                    _lengthField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLength();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "length", DataTemplateUtil.coerceLongInput(value));
                    _lengthField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "length", DataTemplateUtil.coerceLongInput(value));
                    _lengthField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for length
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimeOffset.Fields#length
     */
    public TimeOffset setLength(
        @Nonnull
        Long value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field length of com.linkedin.feathr.config.join.TimeOffset to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "length", DataTemplateUtil.coerceLongInput(value));
            _lengthField = value;
        }
        return this;
    }

    /**
     * Setter for length
     * 
     * @see TimeOffset.Fields#length
     */
    public TimeOffset setLength(long value) {
        CheckedUtil.putWithoutChecking(super._map, "length", DataTemplateUtil.coerceLongInput(value));
        _lengthField = value;
        return this;
    }

    /**
     * Existence checker for unit
     * 
     * @see TimeOffset.Fields#unit
     */
    public boolean hasUnit() {
        if (_unitField!= null) {
            return true;
        }
        return super._map.containsKey("unit");
    }

    /**
     * Remover for unit
     * 
     * @see TimeOffset.Fields#unit
     */
    public void removeUnit() {
        super._map.remove("unit");
    }

    /**
     * Getter for unit
     * 
     * @see TimeOffset.Fields#unit
     */
    public TimeUnit getUnit(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getUnit();
            case DEFAULT:
            case NULL:
                if (_unitField!= null) {
                    return _unitField;
                } else {
                    Object __rawValue = super._map.get("unit");
                    _unitField = DataTemplateUtil.coerceEnumOutput(__rawValue, TimeUnit.class, TimeUnit.$UNKNOWN);
                    return _unitField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for unit
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TimeOffset.Fields#unit
     */
    @Nonnull
    public TimeUnit getUnit() {
        if (_unitField!= null) {
            return _unitField;
        } else {
            Object __rawValue = super._map.get("unit");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("unit");
            }
            _unitField = DataTemplateUtil.coerceEnumOutput(__rawValue, TimeUnit.class, TimeUnit.$UNKNOWN);
            return _unitField;
        }
    }

    /**
     * Setter for unit
     * 
     * @see TimeOffset.Fields#unit
     */
    public TimeOffset setUnit(TimeUnit value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setUnit(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field unit of com.linkedin.feathr.config.join.TimeOffset");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
                    _unitField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeUnit();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
                    _unitField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
                    _unitField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for unit
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimeOffset.Fields#unit
     */
    public TimeOffset setUnit(
        @Nonnull
        TimeUnit value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field unit of com.linkedin.feathr.config.join.TimeOffset to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
            _unitField = value;
        }
        return this;
    }

    @Override
    public TimeOffset clone()
        throws CloneNotSupportedException
    {
        TimeOffset __clone = ((TimeOffset) super.clone());
        __clone.__changeListener = new TimeOffset.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TimeOffset copy()
        throws CloneNotSupportedException
    {
        TimeOffset __copy = ((TimeOffset) super.copy());
        __copy._unitField = null;
        __copy._lengthField = null;
        __copy.__changeListener = new TimeOffset.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TimeOffset __objectRef;

        private ChangeListener(TimeOffset reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "unit":
                    __objectRef._unitField = null;
                    break;
                case "length":
                    __objectRef._lengthField = null;
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
         * Amount of the duration in TimeUnits. Can be positive or negative.
         * 
         */
        public PathSpec length() {
            return new PathSpec(getPathComponents(), "length");
        }

        /**
         * Time unit for "length". For example, TimeUnit.DAY or TimeUnit.HOUR.
         * 
         */
        public PathSpec unit() {
            return new PathSpec(getPathComponents(), "unit");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Amount of the duration in TimeUnits. Can be positive or negative.
         * 
         */
        public TimeOffset.ProjectionMask withLength() {
            getDataMap().put("length", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Time unit for "length". For example, TimeUnit.DAY or TimeUnit.HOUR.
         * 
         */
        public TimeOffset.ProjectionMask withUnit() {
            getDataMap().put("unit", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
