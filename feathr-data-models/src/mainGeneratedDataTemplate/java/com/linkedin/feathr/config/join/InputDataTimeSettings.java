
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
 * The data time settings pertaining to how much of the input dataset is to be loaded from the timestamp column. This is a way in which
 * the input data can be restricted to allow only a fixed interval of dates to be joined with the feature data. This restriction
 * will apply on the timestamp column of the input data.
 * inputDataTimeSettings: {
 *   absoluteTimeRange: {
 *     startTime: Date(year=2020, month=8, day=8)
 *     endTime: Date(year=2020, month=8, day=10)
 *   }
 *  (or)
 *  relativeTimeRange: {
 *    offset: TimeOffset(length=1, unit="DAY")
 *    window: TimeWindow(length=1, unit="DAY")
 *  }
 * }
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/InputDataTimeSettings.pdl.")
public class InputDataTimeSettings
    extends RecordTemplate
{

    private final static InputDataTimeSettings.Fields _fields = new InputDataTimeSettings.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The data time settings pertaining to how much of the input dataset is to be loaded from the timestamp column. This is a way in which\nthe input data can be restricted to allow only a fixed interval of dates to be joined with the feature data. This restriction\nwill apply on the timestamp column of the input data.\ninputDataTimeSettings: {\n  absoluteTimeRange: {\n    startTime: Date(year=2020, month=8, day=8)\n    endTime: Date(year=2020, month=8, day=10)\n  }\n (or)\n relativeTimeRange: {\n   offset: TimeOffset(length=1, unit=\"DAY\")\n   window: TimeWindow(length=1, unit=\"DAY\")\n }\n}*/record InputDataTimeSettings{/**Union of [[AbsoluteTimeRange]] and [[RelativeTimeRange]].\nIt indicates the range of input data which is to be loaded. This field generally refers to how much of the input\ndata should be restricted using the time in the timestamp column.\n\nFor example,\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\n22nd May 2020 to 25th May, 2020 with both dates included.\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\nadd support for other date time formats as well.\n\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\ntill 11/04/2020 willl be joined.*/timeRange:union[absoluteTimeRange:/**The absolute time range with start and end time being required fields.\nIt accepts a start time and an end time which should be specifiied using the [[Date.pdl]] or the [[HourTime.pdl]] class.\nThis model can be used to represent time range in daily or hourly interval.\nabsoluteTimeRange: {\n   startTime: TimeHour(day=1, month=1, year=2020, hour=13)\n   endTime: TimeHour(day=3, month=1, year=2020, hour=2)\n }\n(or)\nabsoluteTimeRange: {\n   startTime: Date(day=1, month=1, year=2020)\n   endTime: Date(day=3, month=1, year=2020)\n }\nendTime and startTime should always have the same granularity, ie - Daily or Hourly.\nendTme > startTime*/record AbsoluteTimeRange{/**start time of the date range, in daily or hourly format with the start date included in the range.*/startTime:union[date:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}hourTime:/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}]/**end date of the date range, in daily or hourly format with the end date included in the range.*/endTime:union[date:Date,hourTime:HourTime]}relativeTimeRange:/**The time range represented relative to the current timestamp. It uses the current system time as the reference and can be used to\nexpress a range of times with respect to the current time.\nExample, - If current time is 01/01/2020, window is 3 days, and offset is 1 day (unit can be day or hour).\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"DAY\"), offset=TimeOffset(length=1, unit=\"Day\"))\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"HOUR\"))*/record RelativeTimeRange{/**Window is the number of time units from the reference time units to look back to obtain the timeRange.\nFor example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020\ntill 11/05/2020 (both days included).\nwindow >= 1 TimeUnit*/window:/**Represents a length of time along with the corresponding time unit (DAY, HOUR).*/record TimeWindow{/**Amount of the duration in TimeUnits. Can be greater or equal to 1.*/@validate.positive,length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}/**Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.\nFor example, if dateOffset is 4, and window is 2 days, then reference time\nwill be 4 days ago from today.\nExample - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.\nThis will always take the window's timeUnits.*/@validate.integerRange.min=0,offset:long=0}]}", SchemaFormatType.PDL));
    private InputDataTimeSettings.TimeRange _timeRangeField = null;
    private InputDataTimeSettings.ChangeListener __changeListener = new InputDataTimeSettings.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TimeRange = SCHEMA.getField("timeRange");

    public InputDataTimeSettings() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public InputDataTimeSettings(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static InputDataTimeSettings.Fields fields() {
        return _fields;
    }

    public static InputDataTimeSettings.ProjectionMask createMask() {
        return new InputDataTimeSettings.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for timeRange
     * 
     * @see InputDataTimeSettings.Fields#timeRange
     */
    public boolean hasTimeRange() {
        if (_timeRangeField!= null) {
            return true;
        }
        return super._map.containsKey("timeRange");
    }

    /**
     * Remover for timeRange
     * 
     * @see InputDataTimeSettings.Fields#timeRange
     */
    public void removeTimeRange() {
        super._map.remove("timeRange");
    }

    /**
     * Getter for timeRange
     * 
     * @see InputDataTimeSettings.Fields#timeRange
     */
    public InputDataTimeSettings.TimeRange getTimeRange(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTimeRange();
            case DEFAULT:
            case NULL:
                if (_timeRangeField!= null) {
                    return _timeRangeField;
                } else {
                    Object __rawValue = super._map.get("timeRange");
                    _timeRangeField = ((__rawValue == null)?null:new InputDataTimeSettings.TimeRange(__rawValue));
                    return _timeRangeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for timeRange
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see InputDataTimeSettings.Fields#timeRange
     */
    @Nonnull
    public InputDataTimeSettings.TimeRange getTimeRange() {
        if (_timeRangeField!= null) {
            return _timeRangeField;
        } else {
            Object __rawValue = super._map.get("timeRange");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("timeRange");
            }
            _timeRangeField = ((__rawValue == null)?null:new InputDataTimeSettings.TimeRange(__rawValue));
            return _timeRangeField;
        }
    }

    /**
     * Setter for timeRange
     * 
     * @see InputDataTimeSettings.Fields#timeRange
     */
    public InputDataTimeSettings setTimeRange(InputDataTimeSettings.TimeRange value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTimeRange(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field timeRange of com.linkedin.feathr.config.join.InputDataTimeSettings");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "timeRange", value.data());
                    _timeRangeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTimeRange();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "timeRange", value.data());
                    _timeRangeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "timeRange", value.data());
                    _timeRangeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for timeRange
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see InputDataTimeSettings.Fields#timeRange
     */
    public InputDataTimeSettings setTimeRange(
        @Nonnull
        InputDataTimeSettings.TimeRange value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field timeRange of com.linkedin.feathr.config.join.InputDataTimeSettings to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "timeRange", value.data());
            _timeRangeField = value;
        }
        return this;
    }

    @Override
    public InputDataTimeSettings clone()
        throws CloneNotSupportedException
    {
        InputDataTimeSettings __clone = ((InputDataTimeSettings) super.clone());
        __clone.__changeListener = new InputDataTimeSettings.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public InputDataTimeSettings copy()
        throws CloneNotSupportedException
    {
        InputDataTimeSettings __copy = ((InputDataTimeSettings) super.copy());
        __copy._timeRangeField = null;
        __copy.__changeListener = new InputDataTimeSettings.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final InputDataTimeSettings __objectRef;

        private ChangeListener(InputDataTimeSettings reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "timeRange":
                    __objectRef._timeRangeField = null;
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
         * Union of [[AbsoluteTimeRange]] and [[RelativeTimeRange]].
         * It indicates the range of input data which is to be loaded. This field generally refers to how much of the input
         * data should be restricted using the time in the timestamp column.
         * 
         * For example,
         * a. startDate: "20200522", endDate: "20200525" implies this feature should be joined with the input data starting from
         *  22nd May 2020 to 25th May, 2020 with both dates included.
         * We only support yyyyMMdd format for this. In future, if there is a request, we can
         * add support for other date time formats as well.
         * 
         * b. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020
         * till 11/04/2020 willl be joined.
         * 
         */
        public com.linkedin.feathr.config.join.InputDataTimeSettings.TimeRange.Fields timeRange() {
            return new com.linkedin.feathr.config.join.InputDataTimeSettings.TimeRange.Fields(getPathComponents(), "timeRange");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.InputDataTimeSettings.TimeRange.ProjectionMask _timeRangeMask;

        ProjectionMask() {
            super(2);
        }

        /**
         * Union of [[AbsoluteTimeRange]] and [[RelativeTimeRange]].
         * It indicates the range of input data which is to be loaded. This field generally refers to how much of the input
         * data should be restricted using the time in the timestamp column.
         * 
         * For example,
         * a. startDate: "20200522", endDate: "20200525" implies this feature should be joined with the input data starting from
         *  22nd May 2020 to 25th May, 2020 with both dates included.
         * We only support yyyyMMdd format for this. In future, if there is a request, we can
         * add support for other date time formats as well.
         * 
         * b. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020
         * till 11/04/2020 willl be joined.
         * 
         */
        public InputDataTimeSettings.ProjectionMask withTimeRange(Function<com.linkedin.feathr.config.join.InputDataTimeSettings.TimeRange.ProjectionMask, com.linkedin.feathr.config.join.InputDataTimeSettings.TimeRange.ProjectionMask> nestedMask) {
            _timeRangeMask = nestedMask.apply(((_timeRangeMask == null)?InputDataTimeSettings.TimeRange.createMask():_timeRangeMask));
            getDataMap().put("timeRange", _timeRangeMask.getDataMap());
            return this;
        }

        /**
         * Union of [[AbsoluteTimeRange]] and [[RelativeTimeRange]].
         * It indicates the range of input data which is to be loaded. This field generally refers to how much of the input
         * data should be restricted using the time in the timestamp column.
         * 
         * For example,
         * a. startDate: "20200522", endDate: "20200525" implies this feature should be joined with the input data starting from
         *  22nd May 2020 to 25th May, 2020 with both dates included.
         * We only support yyyyMMdd format for this. In future, if there is a request, we can
         * add support for other date time formats as well.
         * 
         * b. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020
         * till 11/04/2020 willl be joined.
         * 
         */
        public InputDataTimeSettings.ProjectionMask withTimeRange() {
            _timeRangeMask = null;
            getDataMap().put("timeRange", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/InputDataTimeSettings.pdl.")
    public static class TimeRange
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[absoluteTimeRange:{namespace com.linkedin.feathr.config.join/**The absolute time range with start and end time being required fields.\nIt accepts a start time and an end time which should be specifiied using the [[Date.pdl]] or the [[HourTime.pdl]] class.\nThis model can be used to represent time range in daily or hourly interval.\nabsoluteTimeRange: {\n   startTime: TimeHour(day=1, month=1, year=2020, hour=13)\n   endTime: TimeHour(day=3, month=1, year=2020, hour=2)\n }\n(or)\nabsoluteTimeRange: {\n   startTime: Date(day=1, month=1, year=2020)\n   endTime: Date(day=3, month=1, year=2020)\n }\nendTime and startTime should always have the same granularity, ie - Daily or Hourly.\nendTme > startTime*/record AbsoluteTimeRange{/**start time of the date range, in daily or hourly format with the start date included in the range.*/startTime:union[date:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}hourTime:/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}]/**end date of the date range, in daily or hourly format with the end date included in the range.*/endTime:union[date:Date,hourTime:HourTime]}}relativeTimeRange:{namespace com.linkedin.feathr.config.join/**The time range represented relative to the current timestamp. It uses the current system time as the reference and can be used to\nexpress a range of times with respect to the current time.\nExample, - If current time is 01/01/2020, window is 3 days, and offset is 1 day (unit can be day or hour).\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"DAY\"), offset=TimeOffset(length=1, unit=\"Day\"))\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"HOUR\"))*/record RelativeTimeRange{/**Window is the number of time units from the reference time units to look back to obtain the timeRange.\nFor example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020\ntill 11/05/2020 (both days included).\nwindow >= 1 TimeUnit*/window:/**Represents a length of time along with the corresponding time unit (DAY, HOUR).*/record TimeWindow{/**Amount of the duration in TimeUnits. Can be greater or equal to 1.*/@validate.positive,length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}/**Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.\nFor example, if dateOffset is 4, and window is 2 days, then reference time\nwill be 4 days ago from today.\nExample - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.\nThis will always take the window's timeUnits.*/@validate.integerRange.min=0,offset:long=0}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.config.join.AbsoluteTimeRange _absoluteTimeRangeMember = null;
        private com.linkedin.feathr.config.join.RelativeTimeRange _relativeTimeRangeMember = null;
        private InputDataTimeSettings.TimeRange.ChangeListener __changeListener = new InputDataTimeSettings.TimeRange.ChangeListener(this);
        private final static DataSchema MEMBER_AbsoluteTimeRange = SCHEMA.getTypeByMemberKey("absoluteTimeRange");
        private final static DataSchema MEMBER_RelativeTimeRange = SCHEMA.getTypeByMemberKey("relativeTimeRange");

        public TimeRange() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public TimeRange(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static InputDataTimeSettings.TimeRange createWithAbsoluteTimeRange(com.linkedin.feathr.config.join.AbsoluteTimeRange value) {
            InputDataTimeSettings.TimeRange newUnion = new InputDataTimeSettings.TimeRange();
            newUnion.setAbsoluteTimeRange(value);
            return newUnion;
        }

        public boolean isAbsoluteTimeRange() {
            return memberIs("absoluteTimeRange");
        }

        public com.linkedin.feathr.config.join.AbsoluteTimeRange getAbsoluteTimeRange() {
            checkNotNull();
            if (_absoluteTimeRangeMember!= null) {
                return _absoluteTimeRangeMember;
            }
            Object __rawValue = super._map.get("absoluteTimeRange");
            _absoluteTimeRangeMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.AbsoluteTimeRange(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _absoluteTimeRangeMember;
        }

        public void setAbsoluteTimeRange(com.linkedin.feathr.config.join.AbsoluteTimeRange value) {
            checkNotNull();
            super._map.clear();
            _absoluteTimeRangeMember = value;
            CheckedUtil.putWithoutChecking(super._map, "absoluteTimeRange", value.data());
        }

        public static InputDataTimeSettings.TimeRange createWithRelativeTimeRange(com.linkedin.feathr.config.join.RelativeTimeRange value) {
            InputDataTimeSettings.TimeRange newUnion = new InputDataTimeSettings.TimeRange();
            newUnion.setRelativeTimeRange(value);
            return newUnion;
        }

        public boolean isRelativeTimeRange() {
            return memberIs("relativeTimeRange");
        }

        public com.linkedin.feathr.config.join.RelativeTimeRange getRelativeTimeRange() {
            checkNotNull();
            if (_relativeTimeRangeMember!= null) {
                return _relativeTimeRangeMember;
            }
            Object __rawValue = super._map.get("relativeTimeRange");
            _relativeTimeRangeMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.RelativeTimeRange(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _relativeTimeRangeMember;
        }

        public void setRelativeTimeRange(com.linkedin.feathr.config.join.RelativeTimeRange value) {
            checkNotNull();
            super._map.clear();
            _relativeTimeRangeMember = value;
            CheckedUtil.putWithoutChecking(super._map, "relativeTimeRange", value.data());
        }

        public static InputDataTimeSettings.TimeRange.ProjectionMask createMask() {
            return new InputDataTimeSettings.TimeRange.ProjectionMask();
        }

        @Override
        public InputDataTimeSettings.TimeRange clone()
            throws CloneNotSupportedException
        {
            InputDataTimeSettings.TimeRange __clone = ((InputDataTimeSettings.TimeRange) super.clone());
            __clone.__changeListener = new InputDataTimeSettings.TimeRange.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public InputDataTimeSettings.TimeRange copy()
            throws CloneNotSupportedException
        {
            InputDataTimeSettings.TimeRange __copy = ((InputDataTimeSettings.TimeRange) super.copy());
            __copy._relativeTimeRangeMember = null;
            __copy._absoluteTimeRangeMember = null;
            __copy.__changeListener = new InputDataTimeSettings.TimeRange.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final InputDataTimeSettings.TimeRange __objectRef;

            private ChangeListener(InputDataTimeSettings.TimeRange reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "relativeTimeRange":
                        __objectRef._relativeTimeRangeMember = null;
                        break;
                    case "absoluteTimeRange":
                        __objectRef._absoluteTimeRangeMember = null;
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

            public com.linkedin.feathr.config.join.AbsoluteTimeRange.Fields AbsoluteTimeRange() {
                return new com.linkedin.feathr.config.join.AbsoluteTimeRange.Fields(getPathComponents(), "absoluteTimeRange");
            }

            public com.linkedin.feathr.config.join.RelativeTimeRange.Fields RelativeTimeRange() {
                return new com.linkedin.feathr.config.join.RelativeTimeRange.Fields(getPathComponents(), "relativeTimeRange");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.config.join.AbsoluteTimeRange.ProjectionMask _AbsoluteTimeRangeMask;
            private com.linkedin.feathr.config.join.RelativeTimeRange.ProjectionMask _RelativeTimeRangeMask;

            ProjectionMask() {
                super(3);
            }

            public InputDataTimeSettings.TimeRange.ProjectionMask withAbsoluteTimeRange(Function<com.linkedin.feathr.config.join.AbsoluteTimeRange.ProjectionMask, com.linkedin.feathr.config.join.AbsoluteTimeRange.ProjectionMask> nestedMask) {
                _AbsoluteTimeRangeMask = nestedMask.apply(((_AbsoluteTimeRangeMask == null)?com.linkedin.feathr.config.join.AbsoluteTimeRange.createMask():_AbsoluteTimeRangeMask));
                getDataMap().put("absoluteTimeRange", _AbsoluteTimeRangeMask.getDataMap());
                return this;
            }

            public InputDataTimeSettings.TimeRange.ProjectionMask withRelativeTimeRange(Function<com.linkedin.feathr.config.join.RelativeTimeRange.ProjectionMask, com.linkedin.feathr.config.join.RelativeTimeRange.ProjectionMask> nestedMask) {
                _RelativeTimeRangeMask = nestedMask.apply(((_RelativeTimeRangeMask == null)?com.linkedin.feathr.config.join.RelativeTimeRange.createMask():_RelativeTimeRangeMask));
                getDataMap().put("relativeTimeRange", _RelativeTimeRangeMask.getDataMap());
                return this;
            }

        }

    }

}
