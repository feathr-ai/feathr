
package com.linkedin.feathr.config.join;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.UnionTemplate;


/**
 * The absolute time range with start and end time being required fields.
 * It accepts a start time and an end time which should be specifiied using the [[Date.pdl]] or the [[HourTime.pdl]] class.
 * This model can be used to represent time range in daily or hourly interval.
 * absoluteTimeRange: {
 *    startTime: TimeHour(day=1, month=1, year=2020, hour=13)
 *    endTime: TimeHour(day=3, month=1, year=2020, hour=2)
 *  }
 * (or)
 * absoluteTimeRange: {
 *    startTime: Date(day=1, month=1, year=2020)
 *    endTime: Date(day=3, month=1, year=2020)
 *  }
 * endTime and startTime should always have the same granularity, ie - Daily or Hourly.
 * endTme > startTime
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\AbsoluteTimeRange.pdl.")
public class AbsoluteTimeRange
    extends RecordTemplate
{

    private final static AbsoluteTimeRange.Fields _fields = new AbsoluteTimeRange.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The absolute time range with start and end time being required fields.\r\nIt accepts a start time and an end time which should be specifiied using the [[Date.pdl]] or the [[HourTime.pdl]] class.\r\nThis model can be used to represent time range in daily or hourly interval.\r\nabsoluteTimeRange: {\r\n   startTime: TimeHour(day=1, month=1, year=2020, hour=13)\r\n   endTime: TimeHour(day=3, month=1, year=2020, hour=2)\r\n }\r\n(or)\r\nabsoluteTimeRange: {\r\n   startTime: Date(day=1, month=1, year=2020)\r\n   endTime: Date(day=3, month=1, year=2020)\r\n }\r\nendTime and startTime should always have the same granularity, ie - Daily or Hourly.\r\nendTme > startTime*/record AbsoluteTimeRange{/**start time of the date range, in daily or hourly format with the start date included in the range.*/startTime:union[date:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}hourTime:/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}]/**end date of the date range, in daily or hourly format with the end date included in the range.*/endTime:union[date:Date,hourTime:HourTime]}", SchemaFormatType.PDL));
    private AbsoluteTimeRange.StartTime _startTimeField = null;
    private AbsoluteTimeRange.EndTime _endTimeField = null;
    private AbsoluteTimeRange.ChangeListener __changeListener = new AbsoluteTimeRange.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_StartTime = SCHEMA.getField("startTime");
    private final static RecordDataSchema.Field FIELD_EndTime = SCHEMA.getField("endTime");

    public AbsoluteTimeRange() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public AbsoluteTimeRange(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static AbsoluteTimeRange.Fields fields() {
        return _fields;
    }

    public static AbsoluteTimeRange.ProjectionMask createMask() {
        return new AbsoluteTimeRange.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for startTime
     * 
     * @see AbsoluteTimeRange.Fields#startTime
     */
    public boolean hasStartTime() {
        if (_startTimeField!= null) {
            return true;
        }
        return super._map.containsKey("startTime");
    }

    /**
     * Remover for startTime
     * 
     * @see AbsoluteTimeRange.Fields#startTime
     */
    public void removeStartTime() {
        super._map.remove("startTime");
    }

    /**
     * Getter for startTime
     * 
     * @see AbsoluteTimeRange.Fields#startTime
     */
    public AbsoluteTimeRange.StartTime getStartTime(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getStartTime();
            case DEFAULT:
            case NULL:
                if (_startTimeField!= null) {
                    return _startTimeField;
                } else {
                    Object __rawValue = super._map.get("startTime");
                    _startTimeField = ((__rawValue == null)?null:new AbsoluteTimeRange.StartTime(__rawValue));
                    return _startTimeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for startTime
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see AbsoluteTimeRange.Fields#startTime
     */
    @Nonnull
    public AbsoluteTimeRange.StartTime getStartTime() {
        if (_startTimeField!= null) {
            return _startTimeField;
        } else {
            Object __rawValue = super._map.get("startTime");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("startTime");
            }
            _startTimeField = ((__rawValue == null)?null:new AbsoluteTimeRange.StartTime(__rawValue));
            return _startTimeField;
        }
    }

    /**
     * Setter for startTime
     * 
     * @see AbsoluteTimeRange.Fields#startTime
     */
    public AbsoluteTimeRange setStartTime(AbsoluteTimeRange.StartTime value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setStartTime(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field startTime of com.linkedin.feathr.config.join.AbsoluteTimeRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "startTime", value.data());
                    _startTimeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeStartTime();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "startTime", value.data());
                    _startTimeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "startTime", value.data());
                    _startTimeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for startTime
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see AbsoluteTimeRange.Fields#startTime
     */
    public AbsoluteTimeRange setStartTime(
        @Nonnull
        AbsoluteTimeRange.StartTime value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field startTime of com.linkedin.feathr.config.join.AbsoluteTimeRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "startTime", value.data());
            _startTimeField = value;
        }
        return this;
    }

    /**
     * Existence checker for endTime
     * 
     * @see AbsoluteTimeRange.Fields#endTime
     */
    public boolean hasEndTime() {
        if (_endTimeField!= null) {
            return true;
        }
        return super._map.containsKey("endTime");
    }

    /**
     * Remover for endTime
     * 
     * @see AbsoluteTimeRange.Fields#endTime
     */
    public void removeEndTime() {
        super._map.remove("endTime");
    }

    /**
     * Getter for endTime
     * 
     * @see AbsoluteTimeRange.Fields#endTime
     */
    public AbsoluteTimeRange.EndTime getEndTime(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getEndTime();
            case DEFAULT:
            case NULL:
                if (_endTimeField!= null) {
                    return _endTimeField;
                } else {
                    Object __rawValue = super._map.get("endTime");
                    _endTimeField = ((__rawValue == null)?null:new AbsoluteTimeRange.EndTime(__rawValue));
                    return _endTimeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for endTime
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see AbsoluteTimeRange.Fields#endTime
     */
    @Nonnull
    public AbsoluteTimeRange.EndTime getEndTime() {
        if (_endTimeField!= null) {
            return _endTimeField;
        } else {
            Object __rawValue = super._map.get("endTime");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("endTime");
            }
            _endTimeField = ((__rawValue == null)?null:new AbsoluteTimeRange.EndTime(__rawValue));
            return _endTimeField;
        }
    }

    /**
     * Setter for endTime
     * 
     * @see AbsoluteTimeRange.Fields#endTime
     */
    public AbsoluteTimeRange setEndTime(AbsoluteTimeRange.EndTime value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setEndTime(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field endTime of com.linkedin.feathr.config.join.AbsoluteTimeRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "endTime", value.data());
                    _endTimeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeEndTime();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "endTime", value.data());
                    _endTimeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "endTime", value.data());
                    _endTimeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for endTime
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see AbsoluteTimeRange.Fields#endTime
     */
    public AbsoluteTimeRange setEndTime(
        @Nonnull
        AbsoluteTimeRange.EndTime value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field endTime of com.linkedin.feathr.config.join.AbsoluteTimeRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "endTime", value.data());
            _endTimeField = value;
        }
        return this;
    }

    @Override
    public AbsoluteTimeRange clone()
        throws CloneNotSupportedException
    {
        AbsoluteTimeRange __clone = ((AbsoluteTimeRange) super.clone());
        __clone.__changeListener = new AbsoluteTimeRange.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public AbsoluteTimeRange copy()
        throws CloneNotSupportedException
    {
        AbsoluteTimeRange __copy = ((AbsoluteTimeRange) super.copy());
        __copy._startTimeField = null;
        __copy._endTimeField = null;
        __copy.__changeListener = new AbsoluteTimeRange.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final AbsoluteTimeRange __objectRef;

        private ChangeListener(AbsoluteTimeRange reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "startTime":
                    __objectRef._startTimeField = null;
                    break;
                case "endTime":
                    __objectRef._endTimeField = null;
                    break;
            }
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\AbsoluteTimeRange.pdl.")
    public static class EndTime
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[date:{namespace com.linkedin.feathr.config.join/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}}hourTime:{namespace com.linkedin.feathr.config.join/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.config.join.Date _dateMember = null;
        private com.linkedin.feathr.config.join.HourTime _hourTimeMember = null;
        private AbsoluteTimeRange.EndTime.ChangeListener __changeListener = new AbsoluteTimeRange.EndTime.ChangeListener(this);
        private final static DataSchema MEMBER_Date = SCHEMA.getTypeByMemberKey("date");
        private final static DataSchema MEMBER_HourTime = SCHEMA.getTypeByMemberKey("hourTime");

        public EndTime() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public EndTime(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static AbsoluteTimeRange.EndTime createWithDate(com.linkedin.feathr.config.join.Date value) {
            AbsoluteTimeRange.EndTime newUnion = new AbsoluteTimeRange.EndTime();
            newUnion.setDate(value);
            return newUnion;
        }

        public boolean isDate() {
            return memberIs("date");
        }

        public com.linkedin.feathr.config.join.Date getDate() {
            checkNotNull();
            if (_dateMember!= null) {
                return _dateMember;
            }
            Object __rawValue = super._map.get("date");
            _dateMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.Date(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _dateMember;
        }

        public void setDate(com.linkedin.feathr.config.join.Date value) {
            checkNotNull();
            super._map.clear();
            _dateMember = value;
            CheckedUtil.putWithoutChecking(super._map, "date", value.data());
        }

        public static AbsoluteTimeRange.EndTime createWithHourTime(com.linkedin.feathr.config.join.HourTime value) {
            AbsoluteTimeRange.EndTime newUnion = new AbsoluteTimeRange.EndTime();
            newUnion.setHourTime(value);
            return newUnion;
        }

        public boolean isHourTime() {
            return memberIs("hourTime");
        }

        public com.linkedin.feathr.config.join.HourTime getHourTime() {
            checkNotNull();
            if (_hourTimeMember!= null) {
                return _hourTimeMember;
            }
            Object __rawValue = super._map.get("hourTime");
            _hourTimeMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.HourTime(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _hourTimeMember;
        }

        public void setHourTime(com.linkedin.feathr.config.join.HourTime value) {
            checkNotNull();
            super._map.clear();
            _hourTimeMember = value;
            CheckedUtil.putWithoutChecking(super._map, "hourTime", value.data());
        }

        public static AbsoluteTimeRange.EndTime.ProjectionMask createMask() {
            return new AbsoluteTimeRange.EndTime.ProjectionMask();
        }

        @Override
        public AbsoluteTimeRange.EndTime clone()
            throws CloneNotSupportedException
        {
            AbsoluteTimeRange.EndTime __clone = ((AbsoluteTimeRange.EndTime) super.clone());
            __clone.__changeListener = new AbsoluteTimeRange.EndTime.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public AbsoluteTimeRange.EndTime copy()
            throws CloneNotSupportedException
        {
            AbsoluteTimeRange.EndTime __copy = ((AbsoluteTimeRange.EndTime) super.copy());
            __copy._dateMember = null;
            __copy._hourTimeMember = null;
            __copy.__changeListener = new AbsoluteTimeRange.EndTime.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final AbsoluteTimeRange.EndTime __objectRef;

            private ChangeListener(AbsoluteTimeRange.EndTime reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "date":
                        __objectRef._dateMember = null;
                        break;
                    case "hourTime":
                        __objectRef._hourTimeMember = null;
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

            public com.linkedin.feathr.config.join.Date.Fields Date() {
                return new com.linkedin.feathr.config.join.Date.Fields(getPathComponents(), "date");
            }

            public com.linkedin.feathr.config.join.HourTime.Fields HourTime() {
                return new com.linkedin.feathr.config.join.HourTime.Fields(getPathComponents(), "hourTime");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.config.join.Date.ProjectionMask _DateMask;
            private com.linkedin.feathr.config.join.HourTime.ProjectionMask _HourTimeMask;

            ProjectionMask() {
                super(3);
            }

            public AbsoluteTimeRange.EndTime.ProjectionMask withDate(Function<com.linkedin.feathr.config.join.Date.ProjectionMask, com.linkedin.feathr.config.join.Date.ProjectionMask> nestedMask) {
                _DateMask = nestedMask.apply(((_DateMask == null)?com.linkedin.feathr.config.join.Date.createMask():_DateMask));
                getDataMap().put("date", _DateMask.getDataMap());
                return this;
            }

            public AbsoluteTimeRange.EndTime.ProjectionMask withHourTime(Function<com.linkedin.feathr.config.join.HourTime.ProjectionMask, com.linkedin.feathr.config.join.HourTime.ProjectionMask> nestedMask) {
                _HourTimeMask = nestedMask.apply(((_HourTimeMask == null)?com.linkedin.feathr.config.join.HourTime.createMask():_HourTimeMask));
                getDataMap().put("hourTime", _HourTimeMask.getDataMap());
                return this;
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
         * start time of the date range, in daily or hourly format with the start date included in the range.
         * 
         */
        public com.linkedin.feathr.config.join.AbsoluteTimeRange.StartTime.Fields startTime() {
            return new com.linkedin.feathr.config.join.AbsoluteTimeRange.StartTime.Fields(getPathComponents(), "startTime");
        }

        /**
         * end date of the date range, in daily or hourly format with the end date included in the range.
         * 
         */
        public com.linkedin.feathr.config.join.AbsoluteTimeRange.EndTime.Fields endTime() {
            return new com.linkedin.feathr.config.join.AbsoluteTimeRange.EndTime.Fields(getPathComponents(), "endTime");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.AbsoluteTimeRange.StartTime.ProjectionMask _startTimeMask;
        private com.linkedin.feathr.config.join.AbsoluteTimeRange.EndTime.ProjectionMask _endTimeMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * start time of the date range, in daily or hourly format with the start date included in the range.
         * 
         */
        public AbsoluteTimeRange.ProjectionMask withStartTime(Function<com.linkedin.feathr.config.join.AbsoluteTimeRange.StartTime.ProjectionMask, com.linkedin.feathr.config.join.AbsoluteTimeRange.StartTime.ProjectionMask> nestedMask) {
            _startTimeMask = nestedMask.apply(((_startTimeMask == null)?AbsoluteTimeRange.StartTime.createMask():_startTimeMask));
            getDataMap().put("startTime", _startTimeMask.getDataMap());
            return this;
        }

        /**
         * start time of the date range, in daily or hourly format with the start date included in the range.
         * 
         */
        public AbsoluteTimeRange.ProjectionMask withStartTime() {
            _startTimeMask = null;
            getDataMap().put("startTime", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * end date of the date range, in daily or hourly format with the end date included in the range.
         * 
         */
        public AbsoluteTimeRange.ProjectionMask withEndTime(Function<com.linkedin.feathr.config.join.AbsoluteTimeRange.EndTime.ProjectionMask, com.linkedin.feathr.config.join.AbsoluteTimeRange.EndTime.ProjectionMask> nestedMask) {
            _endTimeMask = nestedMask.apply(((_endTimeMask == null)?AbsoluteTimeRange.EndTime.createMask():_endTimeMask));
            getDataMap().put("endTime", _endTimeMask.getDataMap());
            return this;
        }

        /**
         * end date of the date range, in daily or hourly format with the end date included in the range.
         * 
         */
        public AbsoluteTimeRange.ProjectionMask withEndTime() {
            _endTimeMask = null;
            getDataMap().put("endTime", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\AbsoluteTimeRange.pdl.")
    public static class StartTime
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[date:{namespace com.linkedin.feathr.config.join/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}}hourTime:{namespace com.linkedin.feathr.config.join/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.config.join.Date _dateMember = null;
        private com.linkedin.feathr.config.join.HourTime _hourTimeMember = null;
        private AbsoluteTimeRange.StartTime.ChangeListener __changeListener = new AbsoluteTimeRange.StartTime.ChangeListener(this);
        private final static DataSchema MEMBER_Date = SCHEMA.getTypeByMemberKey("date");
        private final static DataSchema MEMBER_HourTime = SCHEMA.getTypeByMemberKey("hourTime");

        public StartTime() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public StartTime(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static AbsoluteTimeRange.StartTime createWithDate(com.linkedin.feathr.config.join.Date value) {
            AbsoluteTimeRange.StartTime newUnion = new AbsoluteTimeRange.StartTime();
            newUnion.setDate(value);
            return newUnion;
        }

        public boolean isDate() {
            return memberIs("date");
        }

        public com.linkedin.feathr.config.join.Date getDate() {
            checkNotNull();
            if (_dateMember!= null) {
                return _dateMember;
            }
            Object __rawValue = super._map.get("date");
            _dateMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.Date(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _dateMember;
        }

        public void setDate(com.linkedin.feathr.config.join.Date value) {
            checkNotNull();
            super._map.clear();
            _dateMember = value;
            CheckedUtil.putWithoutChecking(super._map, "date", value.data());
        }

        public static AbsoluteTimeRange.StartTime createWithHourTime(com.linkedin.feathr.config.join.HourTime value) {
            AbsoluteTimeRange.StartTime newUnion = new AbsoluteTimeRange.StartTime();
            newUnion.setHourTime(value);
            return newUnion;
        }

        public boolean isHourTime() {
            return memberIs("hourTime");
        }

        public com.linkedin.feathr.config.join.HourTime getHourTime() {
            checkNotNull();
            if (_hourTimeMember!= null) {
                return _hourTimeMember;
            }
            Object __rawValue = super._map.get("hourTime");
            _hourTimeMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.HourTime(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _hourTimeMember;
        }

        public void setHourTime(com.linkedin.feathr.config.join.HourTime value) {
            checkNotNull();
            super._map.clear();
            _hourTimeMember = value;
            CheckedUtil.putWithoutChecking(super._map, "hourTime", value.data());
        }

        public static AbsoluteTimeRange.StartTime.ProjectionMask createMask() {
            return new AbsoluteTimeRange.StartTime.ProjectionMask();
        }

        @Override
        public AbsoluteTimeRange.StartTime clone()
            throws CloneNotSupportedException
        {
            AbsoluteTimeRange.StartTime __clone = ((AbsoluteTimeRange.StartTime) super.clone());
            __clone.__changeListener = new AbsoluteTimeRange.StartTime.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public AbsoluteTimeRange.StartTime copy()
            throws CloneNotSupportedException
        {
            AbsoluteTimeRange.StartTime __copy = ((AbsoluteTimeRange.StartTime) super.copy());
            __copy._dateMember = null;
            __copy._hourTimeMember = null;
            __copy.__changeListener = new AbsoluteTimeRange.StartTime.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final AbsoluteTimeRange.StartTime __objectRef;

            private ChangeListener(AbsoluteTimeRange.StartTime reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "date":
                        __objectRef._dateMember = null;
                        break;
                    case "hourTime":
                        __objectRef._hourTimeMember = null;
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

            public com.linkedin.feathr.config.join.Date.Fields Date() {
                return new com.linkedin.feathr.config.join.Date.Fields(getPathComponents(), "date");
            }

            public com.linkedin.feathr.config.join.HourTime.Fields HourTime() {
                return new com.linkedin.feathr.config.join.HourTime.Fields(getPathComponents(), "hourTime");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.config.join.Date.ProjectionMask _DateMask;
            private com.linkedin.feathr.config.join.HourTime.ProjectionMask _HourTimeMask;

            ProjectionMask() {
                super(3);
            }

            public AbsoluteTimeRange.StartTime.ProjectionMask withDate(Function<com.linkedin.feathr.config.join.Date.ProjectionMask, com.linkedin.feathr.config.join.Date.ProjectionMask> nestedMask) {
                _DateMask = nestedMask.apply(((_DateMask == null)?com.linkedin.feathr.config.join.Date.createMask():_DateMask));
                getDataMap().put("date", _DateMask.getDataMap());
                return this;
            }

            public AbsoluteTimeRange.StartTime.ProjectionMask withHourTime(Function<com.linkedin.feathr.config.join.HourTime.ProjectionMask, com.linkedin.feathr.config.join.HourTime.ProjectionMask> nestedMask) {
                _HourTimeMask = nestedMask.apply(((_HourTimeMask == null)?com.linkedin.feathr.config.join.HourTime.createMask():_HourTimeMask));
                getDataMap().put("hourTime", _HourTimeMask.getDataMap());
                return this;
            }

        }

    }

}
