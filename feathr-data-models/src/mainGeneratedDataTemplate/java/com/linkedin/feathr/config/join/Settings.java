
package com.linkedin.feathr.config.join;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.SetMode;


/**
 * The settings section contains all the config parameters required for the joining of input dataset with the
 * feature data. As of now, we have only time related parameters, but in future this can be expanded.
 * This section has configs related to:-
 * a. How do I load the input dataset if it is time sensitive?
 * b. How do I specify the join parameters for input dataset?
 * For more details - https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#
 * settings: {
 *    inputDataTimeSettings: {
 *      absoluteTimeRange: {
 *        startTime: 20200809
 *        endTime: 20200810
 *        timeFormat: yyyyMMdd
 *      }
 *    }
 *    joinTimeSettings: {
 *      useLatestFeatureData: true
 *    }
 * }
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\Settings.pdl.")
public class Settings
    extends RecordTemplate
{

    private final static Settings.Fields _fields = new Settings.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The settings section contains all the config parameters required for the joining of input dataset with the\r\nfeature data. As of now, we have only time related parameters, but in future this can be expanded.\r\nThis section has configs related to:-\r\na. How do I load the input dataset if it is time sensitive?\r\nb. How do I specify the join parameters for input dataset?\r\nFor more details - https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#\r\nsettings: {\r\n   inputDataTimeSettings: {\r\n     absoluteTimeRange: {\r\n       startTime: 20200809\r\n       endTime: 20200810\r\n       timeFormat: yyyyMMdd\r\n     }\r\n   }\r\n   joinTimeSettings: {\r\n     useLatestFeatureData: true\r\n   }\r\n}*/record Settings{/**Config parameters related to loading of the time sensitive input data. Contains parameters related to restricting the\r\nsize of the input data with respect to the timestamp column.*/inputDataTimeSettings:optional/**The data time settings pertaining to how much of the input dataset is to be loaded from the timestamp column. This is a way in which\r\nthe input data can be restricted to allow only a fixed interval of dates to be joined with the feature data. This restriction\r\nwill apply on the timestamp column of the input data.\r\ninputDataTimeSettings: {\r\n  absoluteTimeRange: {\r\n    startTime: Date(year=2020, month=8, day=8)\r\n    endTime: Date(year=2020, month=8, day=10)\r\n  }\r\n (or)\r\n relativeTimeRange: {\r\n   offset: TimeOffset(length=1, unit=\"DAY\")\r\n   window: TimeWindow(length=1, unit=\"DAY\")\r\n }\r\n}*/record InputDataTimeSettings{/**Union of [[AbsoluteTimeRange]] and [[RelativeTimeRange]].\r\nIt indicates the range of input data which is to be loaded. This field generally refers to how much of the input\r\ndata should be restricted using the time in the timestamp column.\r\n\r\nFor example,\r\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\r\n22nd May 2020 to 25th May, 2020 with both dates included.\r\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\r\nadd support for other date time formats as well.\r\n\r\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\r\ntill 11/04/2020 willl be joined.*/timeRange:union[absoluteTimeRange:/**The absolute time range with start and end time being required fields.\r\nIt accepts a start time and an end time which should be specifiied using the [[Date.pdl]] or the [[HourTime.pdl]] class.\r\nThis model can be used to represent time range in daily or hourly interval.\r\nabsoluteTimeRange: {\r\n   startTime: TimeHour(day=1, month=1, year=2020, hour=13)\r\n   endTime: TimeHour(day=3, month=1, year=2020, hour=2)\r\n }\r\n(or)\r\nabsoluteTimeRange: {\r\n   startTime: Date(day=1, month=1, year=2020)\r\n   endTime: Date(day=3, month=1, year=2020)\r\n }\r\nendTime and startTime should always have the same granularity, ie - Daily or Hourly.\r\nendTme > startTime*/record AbsoluteTimeRange{/**start time of the date range, in daily or hourly format with the start date included in the range.*/startTime:union[date:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}hourTime:/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}]/**end date of the date range, in daily or hourly format with the end date included in the range.*/endTime:union[date:Date,hourTime:HourTime]}relativeTimeRange:/**The time range represented relative to the current timestamp. It uses the current system time as the reference and can be used to\r\nexpress a range of times with respect to the current time.\r\nExample, - If current time is 01/01/2020, window is 3 days, and offset is 1 day (unit can be day or hour).\r\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\r\n\r\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"DAY\"), offset=TimeOffset(length=1, unit=\"Day\"))\r\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"HOUR\"))*/record RelativeTimeRange{/**Window is the number of time units from the reference time units to look back to obtain the timeRange.\r\nFor example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020\r\ntill 11/05/2020 (both days included).\r\nwindow >= 1 TimeUnit*/window:/**Represents a length of time along with the corresponding time unit (DAY, HOUR).*/record TimeWindow{/**Amount of the duration in TimeUnits. Can be greater or equal to 1.*/@validate.positive,length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}/**Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.\r\nFor example, if dateOffset is 4, and window is 2 days, then reference time\r\nwill be 4 days ago from today.\r\nExample - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.\r\nThis will always take the window's timeUnits.*/@validate.integerRange.min=0,offset:long=0}]}/**This contains all the parameters required to join the time sensitive input data with the feature data.*/joinTimeSettings:optional/**JoinTimeSettings contains all the parameters required to join the time sensitive input data with the feature data.\r\nThe input data can be time sensitive in two ways:-\r\na. Have a timestamp column\r\nb. Always join with the latest available feature data. In this case, we do not require a timestamp column.\r\nc. The file path is time-partition and the path time is used for the join\r\n(Todo - Add useTimePartitionPattern field in this section)\r\nIn this section, the user needs to let feathr know which of the above properties is to be used for the join.*/typeref JoinTimeSettings=union[useLatestJoinTimeSettings:/**Settings needed when the input data is to be joined with the latest available feature data.\r\njoinTimeSettings: {\r\n   useLatestFeatureData: true\r\n}*/record UseLatestJoinTimeSettings{/**Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.\r\nWhen useLatestFeatureData is set, there should be no other time-based parameters.*/useLatestFeatureData:boolean=true}timestampColJoinTimeSettings:/**Settings needed when the input data has a timestamp which should be used for the join.\r\njoinTimeSettings: {\r\n   timestampColumn: {\r\n      def: timestamp\r\n      format: yyyy/MM/dd\r\n   }\r\n   simulateTimeDelay: 1d\r\n}*/record TimestampColJoinTimeSettings{/**The timestamp column name and timeformat which should be used for joining with the feature data.\r\nRefer to [[TimestampColumn]].\r\nExample, TimestampColumn: {\r\n           def: timestamp\r\n           format: yyyy/MM/dd\r\n         }*/timestampColumn:/**Timestamp column of the input featureiized dataset, which is to be used for the join.\r\ntimestampColumn: {\r\n   def: timestamp\r\n   format: yyyyMMdd\r\n }*/record TimestampColumn{/**The definiton of the timestamp column, which can be a sql expression involving the timestamp column\r\nor just the column name\r\nExample:- definition: timestamp, timestamp + 10000000.*/definition:union[columnName:string,sparkSqlExpression:/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}]/**Format of the timestamp column. Must confer to java's timestampFormatter or can be\r\nepoch or epoch_millis.\r\nExample:- epoch, epoch_millis, yyyy/MM/dd*/format:/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\r\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string}/**An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted\r\nfrom the input data timestamp while joining with the feature data.\r\nWe do support negative time delays.*/simulateTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\r\nbe any time in the past also, we do allow a positive or negative offset length.\r\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:TimeUnit}}]}", SchemaFormatType.PDL));
    private InputDataTimeSettings _inputDataTimeSettingsField = null;
    private JoinTimeSettings _joinTimeSettingsField = null;
    private Settings.ChangeListener __changeListener = new Settings.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_InputDataTimeSettings = SCHEMA.getField("inputDataTimeSettings");
    private final static RecordDataSchema.Field FIELD_JoinTimeSettings = SCHEMA.getField("joinTimeSettings");

    public Settings() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public Settings(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Settings.Fields fields() {
        return _fields;
    }

    public static Settings.ProjectionMask createMask() {
        return new Settings.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for inputDataTimeSettings
     * 
     * @see Settings.Fields#inputDataTimeSettings
     */
    public boolean hasInputDataTimeSettings() {
        if (_inputDataTimeSettingsField!= null) {
            return true;
        }
        return super._map.containsKey("inputDataTimeSettings");
    }

    /**
     * Remover for inputDataTimeSettings
     * 
     * @see Settings.Fields#inputDataTimeSettings
     */
    public void removeInputDataTimeSettings() {
        super._map.remove("inputDataTimeSettings");
    }

    /**
     * Getter for inputDataTimeSettings
     * 
     * @see Settings.Fields#inputDataTimeSettings
     */
    public InputDataTimeSettings getInputDataTimeSettings(GetMode mode) {
        return getInputDataTimeSettings();
    }

    /**
     * Getter for inputDataTimeSettings
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Settings.Fields#inputDataTimeSettings
     */
    @Nullable
    public InputDataTimeSettings getInputDataTimeSettings() {
        if (_inputDataTimeSettingsField!= null) {
            return _inputDataTimeSettingsField;
        } else {
            Object __rawValue = super._map.get("inputDataTimeSettings");
            _inputDataTimeSettingsField = ((__rawValue == null)?null:new InputDataTimeSettings(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _inputDataTimeSettingsField;
        }
    }

    /**
     * Setter for inputDataTimeSettings
     * 
     * @see Settings.Fields#inputDataTimeSettings
     */
    public Settings setInputDataTimeSettings(InputDataTimeSettings value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setInputDataTimeSettings(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeInputDataTimeSettings();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "inputDataTimeSettings", value.data());
                    _inputDataTimeSettingsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "inputDataTimeSettings", value.data());
                    _inputDataTimeSettingsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for inputDataTimeSettings
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Settings.Fields#inputDataTimeSettings
     */
    public Settings setInputDataTimeSettings(
        @Nonnull
        InputDataTimeSettings value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field inputDataTimeSettings of com.linkedin.feathr.config.join.Settings to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "inputDataTimeSettings", value.data());
            _inputDataTimeSettingsField = value;
        }
        return this;
    }

    /**
     * Existence checker for joinTimeSettings
     * 
     * @see Settings.Fields#joinTimeSettings
     */
    public boolean hasJoinTimeSettings() {
        if (_joinTimeSettingsField!= null) {
            return true;
        }
        return super._map.containsKey("joinTimeSettings");
    }

    /**
     * Remover for joinTimeSettings
     * 
     * @see Settings.Fields#joinTimeSettings
     */
    public void removeJoinTimeSettings() {
        super._map.remove("joinTimeSettings");
    }

    /**
     * Getter for joinTimeSettings
     * 
     * @see Settings.Fields#joinTimeSettings
     */
    public JoinTimeSettings getJoinTimeSettings(GetMode mode) {
        return getJoinTimeSettings();
    }

    /**
     * Getter for joinTimeSettings
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Settings.Fields#joinTimeSettings
     */
    @Nullable
    public JoinTimeSettings getJoinTimeSettings() {
        if (_joinTimeSettingsField!= null) {
            return _joinTimeSettingsField;
        } else {
            Object __rawValue = super._map.get("joinTimeSettings");
            _joinTimeSettingsField = ((__rawValue == null)?null:new JoinTimeSettings(__rawValue));
            return _joinTimeSettingsField;
        }
    }

    /**
     * Setter for joinTimeSettings
     * 
     * @see Settings.Fields#joinTimeSettings
     */
    public Settings setJoinTimeSettings(JoinTimeSettings value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setJoinTimeSettings(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeJoinTimeSettings();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "joinTimeSettings", value.data());
                    _joinTimeSettingsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "joinTimeSettings", value.data());
                    _joinTimeSettingsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for joinTimeSettings
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Settings.Fields#joinTimeSettings
     */
    public Settings setJoinTimeSettings(
        @Nonnull
        JoinTimeSettings value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field joinTimeSettings of com.linkedin.feathr.config.join.Settings to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "joinTimeSettings", value.data());
            _joinTimeSettingsField = value;
        }
        return this;
    }

    @Override
    public Settings clone()
        throws CloneNotSupportedException
    {
        Settings __clone = ((Settings) super.clone());
        __clone.__changeListener = new Settings.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Settings copy()
        throws CloneNotSupportedException
    {
        Settings __copy = ((Settings) super.copy());
        __copy._inputDataTimeSettingsField = null;
        __copy._joinTimeSettingsField = null;
        __copy.__changeListener = new Settings.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Settings __objectRef;

        private ChangeListener(Settings reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "inputDataTimeSettings":
                    __objectRef._inputDataTimeSettingsField = null;
                    break;
                case "joinTimeSettings":
                    __objectRef._joinTimeSettingsField = null;
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
         * Config parameters related to loading of the time sensitive input data. Contains parameters related to restricting the
         * size of the input data with respect to the timestamp column.
         * 
         */
        public com.linkedin.feathr.config.join.InputDataTimeSettings.Fields inputDataTimeSettings() {
            return new com.linkedin.feathr.config.join.InputDataTimeSettings.Fields(getPathComponents(), "inputDataTimeSettings");
        }

        /**
         * This contains all the parameters required to join the time sensitive input data with the feature data.
         * 
         */
        public com.linkedin.feathr.config.join.JoinTimeSettings.Fields joinTimeSettings() {
            return new com.linkedin.feathr.config.join.JoinTimeSettings.Fields(getPathComponents(), "joinTimeSettings");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.InputDataTimeSettings.ProjectionMask _inputDataTimeSettingsMask;
        private com.linkedin.feathr.config.join.JoinTimeSettings.ProjectionMask _joinTimeSettingsMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Config parameters related to loading of the time sensitive input data. Contains parameters related to restricting the
         * size of the input data with respect to the timestamp column.
         * 
         */
        public Settings.ProjectionMask withInputDataTimeSettings(Function<com.linkedin.feathr.config.join.InputDataTimeSettings.ProjectionMask, com.linkedin.feathr.config.join.InputDataTimeSettings.ProjectionMask> nestedMask) {
            _inputDataTimeSettingsMask = nestedMask.apply(((_inputDataTimeSettingsMask == null)?InputDataTimeSettings.createMask():_inputDataTimeSettingsMask));
            getDataMap().put("inputDataTimeSettings", _inputDataTimeSettingsMask.getDataMap());
            return this;
        }

        /**
         * Config parameters related to loading of the time sensitive input data. Contains parameters related to restricting the
         * size of the input data with respect to the timestamp column.
         * 
         */
        public Settings.ProjectionMask withInputDataTimeSettings() {
            _inputDataTimeSettingsMask = null;
            getDataMap().put("inputDataTimeSettings", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * This contains all the parameters required to join the time sensitive input data with the feature data.
         * 
         */
        public Settings.ProjectionMask withJoinTimeSettings(Function<com.linkedin.feathr.config.join.JoinTimeSettings.ProjectionMask, com.linkedin.feathr.config.join.JoinTimeSettings.ProjectionMask> nestedMask) {
            _joinTimeSettingsMask = nestedMask.apply(((_joinTimeSettingsMask == null)?JoinTimeSettings.createMask():_joinTimeSettingsMask));
            getDataMap().put("joinTimeSettings", _joinTimeSettingsMask.getDataMap());
            return this;
        }

        /**
         * This contains all the parameters required to join the time sensitive input data with the feature data.
         * 
         */
        public Settings.ProjectionMask withJoinTimeSettings() {
            _joinTimeSettingsMask = null;
            getDataMap().put("joinTimeSettings", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
