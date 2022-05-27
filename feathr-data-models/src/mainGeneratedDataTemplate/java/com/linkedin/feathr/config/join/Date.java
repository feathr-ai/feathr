
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
 * Represents a date in a calendar year including day, year and month
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/Date.pdl.")
public class Date
    extends RecordTemplate
{

    private final static Date.Fields _fields = new Date.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}", SchemaFormatType.PDL));
    private Integer _dayField = null;
    private Integer _monthField = null;
    private Integer _yearField = null;
    private Date.ChangeListener __changeListener = new Date.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Day = SCHEMA.getField("day");
    private final static RecordDataSchema.Field FIELD_Month = SCHEMA.getField("month");
    private final static RecordDataSchema.Field FIELD_Year = SCHEMA.getField("year");

    public Date() {
        super(new DataMap(4, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public Date(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Date.Fields fields() {
        return _fields;
    }

    public static Date.ProjectionMask createMask() {
        return new Date.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for day
     * 
     * @see Date.Fields#day
     */
    public boolean hasDay() {
        if (_dayField!= null) {
            return true;
        }
        return super._map.containsKey("day");
    }

    /**
     * Remover for day
     * 
     * @see Date.Fields#day
     */
    public void removeDay() {
        super._map.remove("day");
    }

    /**
     * Getter for day
     * 
     * @see Date.Fields#day
     */
    public Integer getDay(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDay();
            case DEFAULT:
            case NULL:
                if (_dayField!= null) {
                    return _dayField;
                } else {
                    Object __rawValue = super._map.get("day");
                    _dayField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _dayField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for day
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Date.Fields#day
     */
    @Nonnull
    public Integer getDay() {
        if (_dayField!= null) {
            return _dayField;
        } else {
            Object __rawValue = super._map.get("day");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("day");
            }
            _dayField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _dayField;
        }
    }

    /**
     * Setter for day
     * 
     * @see Date.Fields#day
     */
    public Date setDay(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDay(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field day of com.linkedin.feathr.config.join.Date");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "day", DataTemplateUtil.coerceIntInput(value));
                    _dayField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDay();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "day", DataTemplateUtil.coerceIntInput(value));
                    _dayField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "day", DataTemplateUtil.coerceIntInput(value));
                    _dayField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for day
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Date.Fields#day
     */
    public Date setDay(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field day of com.linkedin.feathr.config.join.Date to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "day", DataTemplateUtil.coerceIntInput(value));
            _dayField = value;
        }
        return this;
    }

    /**
     * Setter for day
     * 
     * @see Date.Fields#day
     */
    public Date setDay(int value) {
        CheckedUtil.putWithoutChecking(super._map, "day", DataTemplateUtil.coerceIntInput(value));
        _dayField = value;
        return this;
    }

    /**
     * Existence checker for month
     * 
     * @see Date.Fields#month
     */
    public boolean hasMonth() {
        if (_monthField!= null) {
            return true;
        }
        return super._map.containsKey("month");
    }

    /**
     * Remover for month
     * 
     * @see Date.Fields#month
     */
    public void removeMonth() {
        super._map.remove("month");
    }

    /**
     * Getter for month
     * 
     * @see Date.Fields#month
     */
    public Integer getMonth(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getMonth();
            case DEFAULT:
            case NULL:
                if (_monthField!= null) {
                    return _monthField;
                } else {
                    Object __rawValue = super._map.get("month");
                    _monthField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _monthField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for month
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Date.Fields#month
     */
    @Nonnull
    public Integer getMonth() {
        if (_monthField!= null) {
            return _monthField;
        } else {
            Object __rawValue = super._map.get("month");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("month");
            }
            _monthField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _monthField;
        }
    }

    /**
     * Setter for month
     * 
     * @see Date.Fields#month
     */
    public Date setMonth(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setMonth(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field month of com.linkedin.feathr.config.join.Date");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "month", DataTemplateUtil.coerceIntInput(value));
                    _monthField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeMonth();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "month", DataTemplateUtil.coerceIntInput(value));
                    _monthField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "month", DataTemplateUtil.coerceIntInput(value));
                    _monthField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for month
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Date.Fields#month
     */
    public Date setMonth(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field month of com.linkedin.feathr.config.join.Date to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "month", DataTemplateUtil.coerceIntInput(value));
            _monthField = value;
        }
        return this;
    }

    /**
     * Setter for month
     * 
     * @see Date.Fields#month
     */
    public Date setMonth(int value) {
        CheckedUtil.putWithoutChecking(super._map, "month", DataTemplateUtil.coerceIntInput(value));
        _monthField = value;
        return this;
    }

    /**
     * Existence checker for year
     * 
     * @see Date.Fields#year
     */
    public boolean hasYear() {
        if (_yearField!= null) {
            return true;
        }
        return super._map.containsKey("year");
    }

    /**
     * Remover for year
     * 
     * @see Date.Fields#year
     */
    public void removeYear() {
        super._map.remove("year");
    }

    /**
     * Getter for year
     * 
     * @see Date.Fields#year
     */
    public Integer getYear(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getYear();
            case DEFAULT:
            case NULL:
                if (_yearField!= null) {
                    return _yearField;
                } else {
                    Object __rawValue = super._map.get("year");
                    _yearField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _yearField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for year
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Date.Fields#year
     */
    @Nonnull
    public Integer getYear() {
        if (_yearField!= null) {
            return _yearField;
        } else {
            Object __rawValue = super._map.get("year");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("year");
            }
            _yearField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _yearField;
        }
    }

    /**
     * Setter for year
     * 
     * @see Date.Fields#year
     */
    public Date setYear(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setYear(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field year of com.linkedin.feathr.config.join.Date");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "year", DataTemplateUtil.coerceIntInput(value));
                    _yearField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeYear();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "year", DataTemplateUtil.coerceIntInput(value));
                    _yearField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "year", DataTemplateUtil.coerceIntInput(value));
                    _yearField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for year
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Date.Fields#year
     */
    public Date setYear(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field year of com.linkedin.feathr.config.join.Date to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "year", DataTemplateUtil.coerceIntInput(value));
            _yearField = value;
        }
        return this;
    }

    /**
     * Setter for year
     * 
     * @see Date.Fields#year
     */
    public Date setYear(int value) {
        CheckedUtil.putWithoutChecking(super._map, "year", DataTemplateUtil.coerceIntInput(value));
        _yearField = value;
        return this;
    }

    @Override
    public Date clone()
        throws CloneNotSupportedException
    {
        Date __clone = ((Date) super.clone());
        __clone.__changeListener = new Date.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Date copy()
        throws CloneNotSupportedException
    {
        Date __copy = ((Date) super.copy());
        __copy._monthField = null;
        __copy._yearField = null;
        __copy._dayField = null;
        __copy.__changeListener = new Date.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Date __objectRef;

        private ChangeListener(Date reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "month":
                    __objectRef._monthField = null;
                    break;
                case "year":
                    __objectRef._yearField = null;
                    break;
                case "day":
                    __objectRef._dayField = null;
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
         * day
         * 
         */
        public PathSpec day() {
            return new PathSpec(getPathComponents(), "day");
        }

        /**
         * month
         * 
         */
        public PathSpec month() {
            return new PathSpec(getPathComponents(), "month");
        }

        /**
         * year
         * 
         */
        public PathSpec year() {
            return new PathSpec(getPathComponents(), "year");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(4);
        }

        /**
         * day
         * 
         */
        public Date.ProjectionMask withDay() {
            getDataMap().put("day", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * month
         * 
         */
        public Date.ProjectionMask withMonth() {
            getDataMap().put("month", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * year
         * 
         */
        public Date.ProjectionMask withYear() {
            getDataMap().put("year", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
