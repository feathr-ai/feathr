
package com.linkedin.feathr.config.join;

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
 * The time range represented relative to the current timestamp. It uses the current system time as the reference and can be used to
 * express a range of times with respect to the current time.
 * Example, - If current time is 01/01/2020, window is 3 days, and offset is 1 day (unit can be day or hour).
 * then this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.
 * 
 * relativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit="DAY"), offset=TimeOffset(length=1, unit="Day"))
 * relativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit="HOUR"))
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/RelativeTimeRange.pdl.")
public class RelativeTimeRange
    extends RecordTemplate
{

    private final static RelativeTimeRange.Fields _fields = new RelativeTimeRange.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The time range represented relative to the current timestamp. It uses the current system time as the reference and can be used to\nexpress a range of times with respect to the current time.\nExample, - If current time is 01/01/2020, window is 3 days, and offset is 1 day (unit can be day or hour).\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"DAY\"), offset=TimeOffset(length=1, unit=\"Day\"))\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"HOUR\"))*/record RelativeTimeRange{/**Window is the number of time units from the reference time units to look back to obtain the timeRange.\nFor example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020\ntill 11/05/2020 (both days included).\nwindow >= 1 TimeUnit*/window:/**Represents a length of time along with the corresponding time unit (DAY, HOUR).*/record TimeWindow{/**Amount of the duration in TimeUnits. Can be greater or equal to 1.*/@validate.positive,length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}/**Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.\nFor example, if dateOffset is 4, and window is 2 days, then reference time\nwill be 4 days ago from today.\nExample - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.\nThis will always take the window's timeUnits.*/@validate.integerRange.min=0,offset:long=0}", SchemaFormatType.PDL));
    private TimeWindow _windowField = null;
    private Long _offsetField = null;
    private RelativeTimeRange.ChangeListener __changeListener = new RelativeTimeRange.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Window = SCHEMA.getField("window");
    private final static RecordDataSchema.Field FIELD_Offset = SCHEMA.getField("offset");
    private final static Long DEFAULT_Offset;

    static {
        DEFAULT_Offset = DataTemplateUtil.coerceLongOutput(FIELD_Offset.getDefault());
    }

    public RelativeTimeRange() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public RelativeTimeRange(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static RelativeTimeRange.Fields fields() {
        return _fields;
    }

    public static RelativeTimeRange.ProjectionMask createMask() {
        return new RelativeTimeRange.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for window
     * 
     * @see RelativeTimeRange.Fields#window
     */
    public boolean hasWindow() {
        if (_windowField!= null) {
            return true;
        }
        return super._map.containsKey("window");
    }

    /**
     * Remover for window
     * 
     * @see RelativeTimeRange.Fields#window
     */
    public void removeWindow() {
        super._map.remove("window");
    }

    /**
     * Getter for window
     * 
     * @see RelativeTimeRange.Fields#window
     */
    public TimeWindow getWindow(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getWindow();
            case DEFAULT:
            case NULL:
                if (_windowField!= null) {
                    return _windowField;
                } else {
                    Object __rawValue = super._map.get("window");
                    _windowField = ((__rawValue == null)?null:new TimeWindow(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _windowField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for window
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see RelativeTimeRange.Fields#window
     */
    @Nonnull
    public TimeWindow getWindow() {
        if (_windowField!= null) {
            return _windowField;
        } else {
            Object __rawValue = super._map.get("window");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("window");
            }
            _windowField = ((__rawValue == null)?null:new TimeWindow(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _windowField;
        }
    }

    /**
     * Setter for window
     * 
     * @see RelativeTimeRange.Fields#window
     */
    public RelativeTimeRange setWindow(TimeWindow value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setWindow(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field window of com.linkedin.feathr.config.join.RelativeTimeRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "window", value.data());
                    _windowField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeWindow();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "window", value.data());
                    _windowField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "window", value.data());
                    _windowField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for window
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RelativeTimeRange.Fields#window
     */
    public RelativeTimeRange setWindow(
        @Nonnull
        TimeWindow value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field window of com.linkedin.feathr.config.join.RelativeTimeRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "window", value.data());
            _windowField = value;
        }
        return this;
    }

    /**
     * Existence checker for offset
     * 
     * @see RelativeTimeRange.Fields#offset
     */
    public boolean hasOffset() {
        if (_offsetField!= null) {
            return true;
        }
        return super._map.containsKey("offset");
    }

    /**
     * Remover for offset
     * 
     * @see RelativeTimeRange.Fields#offset
     */
    public void removeOffset() {
        super._map.remove("offset");
    }

    /**
     * Getter for offset
     * 
     * @see RelativeTimeRange.Fields#offset
     */
    public Long getOffset(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getOffset();
            case NULL:
                if (_offsetField!= null) {
                    return _offsetField;
                } else {
                    Object __rawValue = super._map.get("offset");
                    _offsetField = DataTemplateUtil.coerceLongOutput(__rawValue);
                    return _offsetField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for offset
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see RelativeTimeRange.Fields#offset
     */
    @Nonnull
    public Long getOffset() {
        if (_offsetField!= null) {
            return _offsetField;
        } else {
            Object __rawValue = super._map.get("offset");
            if (__rawValue == null) {
                return DEFAULT_Offset;
            }
            _offsetField = DataTemplateUtil.coerceLongOutput(__rawValue);
            return _offsetField;
        }
    }

    /**
     * Setter for offset
     * 
     * @see RelativeTimeRange.Fields#offset
     */
    public RelativeTimeRange setOffset(Long value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setOffset(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field offset of com.linkedin.feathr.config.join.RelativeTimeRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "offset", DataTemplateUtil.coerceLongInput(value));
                    _offsetField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeOffset();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "offset", DataTemplateUtil.coerceLongInput(value));
                    _offsetField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "offset", DataTemplateUtil.coerceLongInput(value));
                    _offsetField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for offset
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RelativeTimeRange.Fields#offset
     */
    public RelativeTimeRange setOffset(
        @Nonnull
        Long value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field offset of com.linkedin.feathr.config.join.RelativeTimeRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "offset", DataTemplateUtil.coerceLongInput(value));
            _offsetField = value;
        }
        return this;
    }

    /**
     * Setter for offset
     * 
     * @see RelativeTimeRange.Fields#offset
     */
    public RelativeTimeRange setOffset(long value) {
        CheckedUtil.putWithoutChecking(super._map, "offset", DataTemplateUtil.coerceLongInput(value));
        _offsetField = value;
        return this;
    }

    @Override
    public RelativeTimeRange clone()
        throws CloneNotSupportedException
    {
        RelativeTimeRange __clone = ((RelativeTimeRange) super.clone());
        __clone.__changeListener = new RelativeTimeRange.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public RelativeTimeRange copy()
        throws CloneNotSupportedException
    {
        RelativeTimeRange __copy = ((RelativeTimeRange) super.copy());
        __copy._offsetField = null;
        __copy._windowField = null;
        __copy.__changeListener = new RelativeTimeRange.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final RelativeTimeRange __objectRef;

        private ChangeListener(RelativeTimeRange reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "offset":
                    __objectRef._offsetField = null;
                    break;
                case "window":
                    __objectRef._windowField = null;
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
         * Window is the number of time units from the reference time units to look back to obtain the timeRange.
         * For example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020
         * till 11/05/2020 (both days included).
         * window >= 1 TimeUnit
         * 
         */
        public com.linkedin.feathr.config.join.TimeWindow.Fields window() {
            return new com.linkedin.feathr.config.join.TimeWindow.Fields(getPathComponents(), "window");
        }

        /**
         * Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.
         * For example, if dateOffset is 4, and window is 2 days, then reference time
         * will be 4 days ago from today.
         * Example - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.
         * This will always take the window's timeUnits.
         * 
         */
        public PathSpec offset() {
            return new PathSpec(getPathComponents(), "offset");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.TimeWindow.ProjectionMask _windowMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Window is the number of time units from the reference time units to look back to obtain the timeRange.
         * For example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020
         * till 11/05/2020 (both days included).
         * window >= 1 TimeUnit
         * 
         */
        public RelativeTimeRange.ProjectionMask withWindow(Function<com.linkedin.feathr.config.join.TimeWindow.ProjectionMask, com.linkedin.feathr.config.join.TimeWindow.ProjectionMask> nestedMask) {
            _windowMask = nestedMask.apply(((_windowMask == null)?TimeWindow.createMask():_windowMask));
            getDataMap().put("window", _windowMask.getDataMap());
            return this;
        }

        /**
         * Window is the number of time units from the reference time units to look back to obtain the timeRange.
         * For example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020
         * till 11/05/2020 (both days included).
         * window >= 1 TimeUnit
         * 
         */
        public RelativeTimeRange.ProjectionMask withWindow() {
            _windowMask = null;
            getDataMap().put("window", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.
         * For example, if dateOffset is 4, and window is 2 days, then reference time
         * will be 4 days ago from today.
         * Example - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.
         * This will always take the window's timeUnits.
         * 
         */
        public RelativeTimeRange.ProjectionMask withOffset() {
            getDataMap().put("offset", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
