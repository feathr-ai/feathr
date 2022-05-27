
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
 * Time with hourly granularity
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/HourTime.pdl.")
public class HourTime
    extends RecordTemplate
{

    private final static HourTime.Fields _fields = new HourTime.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}", SchemaFormatType.PDL));
    private Integer _dayField = null;
    private Integer _monthField = null;
    private Integer _yearField = null;
    private Integer _hourField = null;
    private HourTime.ChangeListener __changeListener = new HourTime.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Day = SCHEMA.getField("day");
    private final static RecordDataSchema.Field FIELD_Month = SCHEMA.getField("month");
    private final static RecordDataSchema.Field FIELD_Year = SCHEMA.getField("year");
    private final static RecordDataSchema.Field FIELD_Hour = SCHEMA.getField("hour");

    public HourTime() {
        super(new DataMap(6, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public HourTime(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static HourTime.Fields fields() {
        return _fields;
    }

    public static HourTime.ProjectionMask createMask() {
        return new HourTime.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for day
     * 
     * @see HourTime.Fields#day
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
     * @see HourTime.Fields#day
     */
    public void removeDay() {
        super._map.remove("day");
    }

    /**
     * Getter for day
     * 
     * @see HourTime.Fields#day
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
     * @see HourTime.Fields#day
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
     * @see HourTime.Fields#day
     */
    public HourTime setDay(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDay(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field day of com.linkedin.feathr.config.join.HourTime");
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
     * @see HourTime.Fields#day
     */
    public HourTime setDay(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field day of com.linkedin.feathr.config.join.HourTime to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "day", DataTemplateUtil.coerceIntInput(value));
            _dayField = value;
        }
        return this;
    }

    /**
     * Setter for day
     * 
     * @see HourTime.Fields#day
     */
    public HourTime setDay(int value) {
        CheckedUtil.putWithoutChecking(super._map, "day", DataTemplateUtil.coerceIntInput(value));
        _dayField = value;
        return this;
    }

    /**
     * Existence checker for month
     * 
     * @see HourTime.Fields#month
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
     * @see HourTime.Fields#month
     */
    public void removeMonth() {
        super._map.remove("month");
    }

    /**
     * Getter for month
     * 
     * @see HourTime.Fields#month
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
     * @see HourTime.Fields#month
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
     * @see HourTime.Fields#month
     */
    public HourTime setMonth(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setMonth(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field month of com.linkedin.feathr.config.join.HourTime");
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
     * @see HourTime.Fields#month
     */
    public HourTime setMonth(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field month of com.linkedin.feathr.config.join.HourTime to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "month", DataTemplateUtil.coerceIntInput(value));
            _monthField = value;
        }
        return this;
    }

    /**
     * Setter for month
     * 
     * @see HourTime.Fields#month
     */
    public HourTime setMonth(int value) {
        CheckedUtil.putWithoutChecking(super._map, "month", DataTemplateUtil.coerceIntInput(value));
        _monthField = value;
        return this;
    }

    /**
     * Existence checker for year
     * 
     * @see HourTime.Fields#year
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
     * @see HourTime.Fields#year
     */
    public void removeYear() {
        super._map.remove("year");
    }

    /**
     * Getter for year
     * 
     * @see HourTime.Fields#year
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
     * @see HourTime.Fields#year
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
     * @see HourTime.Fields#year
     */
    public HourTime setYear(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setYear(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field year of com.linkedin.feathr.config.join.HourTime");
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
     * @see HourTime.Fields#year
     */
    public HourTime setYear(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field year of com.linkedin.feathr.config.join.HourTime to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "year", DataTemplateUtil.coerceIntInput(value));
            _yearField = value;
        }
        return this;
    }

    /**
     * Setter for year
     * 
     * @see HourTime.Fields#year
     */
    public HourTime setYear(int value) {
        CheckedUtil.putWithoutChecking(super._map, "year", DataTemplateUtil.coerceIntInput(value));
        _yearField = value;
        return this;
    }

    /**
     * Existence checker for hour
     * 
     * @see HourTime.Fields#hour
     */
    public boolean hasHour() {
        if (_hourField!= null) {
            return true;
        }
        return super._map.containsKey("hour");
    }

    /**
     * Remover for hour
     * 
     * @see HourTime.Fields#hour
     */
    public void removeHour() {
        super._map.remove("hour");
    }

    /**
     * Getter for hour
     * 
     * @see HourTime.Fields#hour
     */
    public Integer getHour(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getHour();
            case DEFAULT:
            case NULL:
                if (_hourField!= null) {
                    return _hourField;
                } else {
                    Object __rawValue = super._map.get("hour");
                    _hourField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _hourField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for hour
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see HourTime.Fields#hour
     */
    @Nonnull
    public Integer getHour() {
        if (_hourField!= null) {
            return _hourField;
        } else {
            Object __rawValue = super._map.get("hour");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("hour");
            }
            _hourField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _hourField;
        }
    }

    /**
     * Setter for hour
     * 
     * @see HourTime.Fields#hour
     */
    public HourTime setHour(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setHour(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field hour of com.linkedin.feathr.config.join.HourTime");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "hour", DataTemplateUtil.coerceIntInput(value));
                    _hourField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeHour();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "hour", DataTemplateUtil.coerceIntInput(value));
                    _hourField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "hour", DataTemplateUtil.coerceIntInput(value));
                    _hourField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for hour
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HourTime.Fields#hour
     */
    public HourTime setHour(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field hour of com.linkedin.feathr.config.join.HourTime to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "hour", DataTemplateUtil.coerceIntInput(value));
            _hourField = value;
        }
        return this;
    }

    /**
     * Setter for hour
     * 
     * @see HourTime.Fields#hour
     */
    public HourTime setHour(int value) {
        CheckedUtil.putWithoutChecking(super._map, "hour", DataTemplateUtil.coerceIntInput(value));
        _hourField = value;
        return this;
    }

    @Override
    public HourTime clone()
        throws CloneNotSupportedException
    {
        HourTime __clone = ((HourTime) super.clone());
        __clone.__changeListener = new HourTime.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public HourTime copy()
        throws CloneNotSupportedException
    {
        HourTime __copy = ((HourTime) super.copy());
        __copy._monthField = null;
        __copy._hourField = null;
        __copy._yearField = null;
        __copy._dayField = null;
        __copy.__changeListener = new HourTime.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final HourTime __objectRef;

        private ChangeListener(HourTime reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "month":
                    __objectRef._monthField = null;
                    break;
                case "hour":
                    __objectRef._hourField = null;
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

        /**
         * hour
         * 
         */
        public PathSpec hour() {
            return new PathSpec(getPathComponents(), "hour");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(6);
        }

        /**
         * day
         * 
         */
        public HourTime.ProjectionMask withDay() {
            getDataMap().put("day", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * month
         * 
         */
        public HourTime.ProjectionMask withMonth() {
            getDataMap().put("month", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * year
         * 
         */
        public HourTime.ProjectionMask withYear() {
            getDataMap().put("year", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * hour
         * 
         */
        public HourTime.ProjectionMask withHour() {
            getDataMap().put("hour", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
