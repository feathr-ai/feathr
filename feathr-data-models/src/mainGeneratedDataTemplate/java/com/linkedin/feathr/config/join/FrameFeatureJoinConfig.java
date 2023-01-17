
package com.linkedin.feathr.config.join;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataList;
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
 * The join config consists of 2 parts, settings and features section.
 * Settings is related to the general settings corresponding to joining the input data set with the
 * features, currently there are time related settings, but this can be extended to other settings as well.
 * Features to be joined are described by list of Keys and featureName and featureAlias.
 * Features in the feature list should be joined to the user's input data.
 * matching the key in the input data.
 * For example,
 * key is ["key1"] and join feature1 and feature2 with input data
 *   settings: {      // optional field
 *     inputDataTimeSettings: {
 *        absoluteTimeRange: {
 *            startTime: Date(year=2020, month=4, day=28)
 *            endTime: Date(year=2020, month=5, day=5)
 *        }
 *     }
 *     joinTimeSettings: {
 *        timestampColumn: {
 *          def: timestamp
 *          format: yyyy-MM-dd
 *        }
 *        simulateTimeDelay: 5d
 *     }
 *   }
 *  features=[
 *   JoiningFeature{
 *     keys: ["key1"]
 *     frameFeatureName: "feature1"
 *     AbsoluteDateRange(startDate: Date(year=2020, month=5, day=1),
 *                       endTime: Date(year=2020, month=5, day=5))
 *   }, JoiningFeature{
 *     keys: ["key1"]
 *     frameFeatureName: "feature2"
 *     overrideTimeDelay: 5d
 *   }, JoiningFeature{
 *      keys: ["key1"]
 *      frameFeatureName: "feature3"
 *      RelativeDateRange(numDays: 5,
 *                        offset: 3)
 *   }, JoiningFeature{
 *     keys: ["key1"]
 *     frameFeatureName: "feature4"
 *   }
 *  ]
 * 
 * Here, the keys are corresponding to column names in the input FeaturizedDataset, which will be used
 * to join the feature source. Feature name is canonical feathr feature names.
 * Each feature can also have a set of optional time-related parameters. These parameter override the ones provided in
 * the settings section and are applicable only to the particular feature.
 * Feature join config operation.
 * 
 * All these PDLs are moved to feathr MP:- https://rb.corp.linkedin.com/r/2356512/
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\FrameFeatureJoinConfig.pdl.")
public class FrameFeatureJoinConfig
    extends RecordTemplate
{

    private final static FrameFeatureJoinConfig.Fields _fields = new FrameFeatureJoinConfig.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The join config consists of 2 parts, settings and features section.\r\nSettings is related to the general settings corresponding to joining the input data set with the\r\nfeatures, currently there are time related settings, but this can be extended to other settings as well.\r\nFeatures to be joined are described by list of Keys and featureName and featureAlias.\r\nFeatures in the feature list should be joined to the user's input data.\r\nmatching the key in the input data.\r\nFor example,\r\nkey is [\"key1\"] and join feature1 and feature2 with input data\r\n  settings: {      // optional field\r\n    inputDataTimeSettings: {\r\n       absoluteTimeRange: {\r\n           startTime: Date(year=2020, month=4, day=28)\r\n           endTime: Date(year=2020, month=5, day=5)\r\n       }\r\n    }\r\n    joinTimeSettings: {\r\n       timestampColumn: {\r\n         def: timestamp\r\n         format: yyyy-MM-dd\r\n       }\r\n       simulateTimeDelay: 5d\r\n    }\r\n  }\r\n features=[\r\n  JoiningFeature{\r\n    keys: [\"key1\"]\r\n    frameFeatureName: \"feature1\"\r\n    AbsoluteDateRange(startDate: Date(year=2020, month=5, day=1),\r\n                      endTime: Date(year=2020, month=5, day=5))\r\n  }, JoiningFeature{\r\n    keys: [\"key1\"]\r\n    frameFeatureName: \"feature2\"\r\n    overrideTimeDelay: 5d\r\n  }, JoiningFeature{\r\n     keys: [\"key1\"]\r\n     frameFeatureName: \"feature3\"\r\n     RelativeDateRange(numDays: 5,\r\n                       offset: 3)\r\n  }, JoiningFeature{\r\n    keys: [\"key1\"]\r\n    frameFeatureName: \"feature4\"\r\n  }\r\n ]\r\n\r\nHere, the keys are corresponding to column names in the input FeaturizedDataset, which will be used\r\nto join the feature source. Feature name is canonical feathr feature names.\r\nEach feature can also have a set of optional time-related parameters. These parameter override the ones provided in\r\nthe settings section and are applicable only to the particular feature.\r\nFeature join config operation.\r\n\r\nAll these PDLs are moved to feathr MP:- https://rb.corp.linkedin.com/r/2356512/*/record FrameFeatureJoinConfig{/**settings required for joining input featurized dataset with the feature data.*/settings:optional/**The settings section contains all the config parameters required for the joining of input dataset with the\r\nfeature data. As of now, we have only time related parameters, but in future this can be expanded.\r\nThis section has configs related to:-\r\na. How do I load the input dataset if it is time sensitive?\r\nb. How do I specify the join parameters for input dataset?\r\nFor more details - https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#\r\nsettings: {\r\n   inputDataTimeSettings: {\r\n     absoluteTimeRange: {\r\n       startTime: 20200809\r\n       endTime: 20200810\r\n       timeFormat: yyyyMMdd\r\n     }\r\n   }\r\n   joinTimeSettings: {\r\n     useLatestFeatureData: true\r\n   }\r\n}*/record Settings{/**Config parameters related to loading of the time sensitive input data. Contains parameters related to restricting the\r\nsize of the input data with respect to the timestamp column.*/inputDataTimeSettings:optional/**The data time settings pertaining to how much of the input dataset is to be loaded from the timestamp column. This is a way in which\r\nthe input data can be restricted to allow only a fixed interval of dates to be joined with the feature data. This restriction\r\nwill apply on the timestamp column of the input data.\r\ninputDataTimeSettings: {\r\n  absoluteTimeRange: {\r\n    startTime: Date(year=2020, month=8, day=8)\r\n    endTime: Date(year=2020, month=8, day=10)\r\n  }\r\n (or)\r\n relativeTimeRange: {\r\n   offset: TimeOffset(length=1, unit=\"DAY\")\r\n   window: TimeWindow(length=1, unit=\"DAY\")\r\n }\r\n}*/record InputDataTimeSettings{/**Union of [[AbsoluteTimeRange]] and [[RelativeTimeRange]].\r\nIt indicates the range of input data which is to be loaded. This field generally refers to how much of the input\r\ndata should be restricted using the time in the timestamp column.\r\n\r\nFor example,\r\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\r\n22nd May 2020 to 25th May, 2020 with both dates included.\r\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\r\nadd support for other date time formats as well.\r\n\r\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\r\ntill 11/04/2020 willl be joined.*/timeRange:union[absoluteTimeRange:/**The absolute time range with start and end time being required fields.\r\nIt accepts a start time and an end time which should be specifiied using the [[Date.pdl]] or the [[HourTime.pdl]] class.\r\nThis model can be used to represent time range in daily or hourly interval.\r\nabsoluteTimeRange: {\r\n   startTime: TimeHour(day=1, month=1, year=2020, hour=13)\r\n   endTime: TimeHour(day=3, month=1, year=2020, hour=2)\r\n }\r\n(or)\r\nabsoluteTimeRange: {\r\n   startTime: Date(day=1, month=1, year=2020)\r\n   endTime: Date(day=3, month=1, year=2020)\r\n }\r\nendTime and startTime should always have the same granularity, ie - Daily or Hourly.\r\nendTme > startTime*/record AbsoluteTimeRange{/**start time of the date range, in daily or hourly format with the start date included in the range.*/startTime:union[date:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}hourTime:/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}]/**end date of the date range, in daily or hourly format with the end date included in the range.*/endTime:union[date:Date,hourTime:HourTime]}relativeTimeRange:/**The time range represented relative to the current timestamp. It uses the current system time as the reference and can be used to\r\nexpress a range of times with respect to the current time.\r\nExample, - If current time is 01/01/2020, window is 3 days, and offset is 1 day (unit can be day or hour).\r\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\r\n\r\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"DAY\"), offset=TimeOffset(length=1, unit=\"Day\"))\r\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"HOUR\"))*/record RelativeTimeRange{/**Window is the number of time units from the reference time units to look back to obtain the timeRange.\r\nFor example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020\r\ntill 11/05/2020 (both days included).\r\nwindow >= 1 TimeUnit*/window:/**Represents a length of time along with the corresponding time unit (DAY, HOUR).*/record TimeWindow{/**Amount of the duration in TimeUnits. Can be greater or equal to 1.*/@validate.positive,length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}/**Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.\r\nFor example, if dateOffset is 4, and window is 2 days, then reference time\r\nwill be 4 days ago from today.\r\nExample - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.\r\nThis will always take the window's timeUnits.*/@validate.integerRange.min=0,offset:long=0}]}/**This contains all the parameters required to join the time sensitive input data with the feature data.*/joinTimeSettings:optional/**JoinTimeSettings contains all the parameters required to join the time sensitive input data with the feature data.\r\nThe input data can be time sensitive in two ways:-\r\na. Have a timestamp column\r\nb. Always join with the latest available feature data. In this case, we do not require a timestamp column.\r\nc. The file path is time-partition and the path time is used for the join\r\n(Todo - Add useTimePartitionPattern field in this section)\r\nIn this section, the user needs to let feathr know which of the above properties is to be used for the join.*/typeref JoinTimeSettings=union[useLatestJoinTimeSettings:/**Settings needed when the input data is to be joined with the latest available feature data.\r\njoinTimeSettings: {\r\n   useLatestFeatureData: true\r\n}*/record UseLatestJoinTimeSettings{/**Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.\r\nWhen useLatestFeatureData is set, there should be no other time-based parameters.*/useLatestFeatureData:boolean=true}timestampColJoinTimeSettings:/**Settings needed when the input data has a timestamp which should be used for the join.\r\njoinTimeSettings: {\r\n   timestampColumn: {\r\n      def: timestamp\r\n      format: yyyy/MM/dd\r\n   }\r\n   simulateTimeDelay: 1d\r\n}*/record TimestampColJoinTimeSettings{/**The timestamp column name and timeformat which should be used for joining with the feature data.\r\nRefer to [[TimestampColumn]].\r\nExample, TimestampColumn: {\r\n           def: timestamp\r\n           format: yyyy/MM/dd\r\n         }*/timestampColumn:/**Timestamp column of the input featureiized dataset, which is to be used for the join.\r\ntimestampColumn: {\r\n   def: timestamp\r\n   format: yyyyMMdd\r\n }*/record TimestampColumn{/**The definiton of the timestamp column, which can be a sql expression involving the timestamp column\r\nor just the column name\r\nExample:- definition: timestamp, timestamp + 10000000.*/definition:union[columnName:string,sparkSqlExpression:/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}]/**Format of the timestamp column. Must confer to java's timestampFormatter or can be\r\nepoch or epoch_millis.\r\nExample:- epoch, epoch_millis, yyyy/MM/dd*/format:/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\r\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string}/**An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted\r\nfrom the input data timestamp while joining with the feature data.\r\nWe do support negative time delays.*/simulateTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\r\nbe any time in the past also, we do allow a positive or negative offset length.\r\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:TimeUnit}}]}/**Array of joining features.\r\n\r\nValidation rules:\r\n- The array must be non-empty.*/features:array[/**JoiningFeature is the feature section of the join config. This section consists of information pertaining to a feature\r\nwhich is to be joined:-\r\na. The join keys of the input data, with which this feature is to be joined.\r\nb. name of the feature\r\nc. optional timeRange of the input data which is to be joined with this feature.\r\nd. optional overrideTimeDelay if this feature needs a different simulate time delay other than the one mentioned.\r\n\r\nThis is a required section of the the join config.\r\nExample,\r\n  a. JoiningFeature{\r\n       keys: [\"key1\"]\r\n       frameFeatureName: \"feature1\"\r\n       AbsoluteDateRange(startDate: Date(year=2020, month=5, day=5),\r\n                        endDate: Date(year=2020, month=5, day=7))\r\n     }\r\n  b. JoiningFeature{\r\n       keys: [\"key1\"]\r\n       frameFeatureName: \"feature2\"\r\n       overrideTimeDelay: TimeDelay(length=1, unit=\"DAY\")\r\n   }\r\n  c. JoiningFeature{\r\n       keys: [\"key1\"]\r\n       frameFeatureName: \"feature3\"\r\n       RelativeDateRange(numDays: 5,\r\n                         offset: 3)\r\n  }*/record JoiningFeature{/**Keys to join input with feature source, the field name of the key in the input featuized dataset.*/keys:array[string]/**Feature name as defined in feathr's feature definition configuration.\r\n\r\nCurrently the column in the output FDS that holds this feature will have the same name as feature name.\r\nIf multiple joined features have the same name and no alias is defined for them, feathr will prepend the keys to the feature name.\r\n\r\nIn the future, if \"featureAlias\" is not set, the column in the output FDS that holds this feature will have the same name as feature name.\r\nIf multiple joined features have the same name and no alias is defined for them, the join operation will fail\r\n(to avoid produciing two columns in the output FDS with the same name).*/frameFeatureName:string/**The development of this is in progress. This is not in use for now.\r\n\r\nThe name to be used for the column in the output FDS that contains the values from this joined feature.\r\nIf not set, the name of the feature (frameFeatureName) will be used for the output column.\r\nFor example, if the user request joining a feature named \"careers_job_listTime\" and provides no alias,\r\nthe output FDS will contain a column called \"careers_job_listTime\". However, if the user sets \"featureAlias\" to \"list_time\",\r\nthe column will be named \"list_time\".\r\n\r\nfeature alias can be useful for in a few cases:\r\n - If the user prefers to use a name different than the feathr name in their model,\r\nthey can use an alias to control the name of the column in the output FDS.\r\n - Sometimes, the training datas needs to have two features that are from the same feathr feature.\r\nFor example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B\r\n(viewee) and we want to use the skills of both viewer and viewee as features, we need to join feathr feature\r\n\"member_skills\" of member A with feathr feature \"member_skills\" of member B. That is, the two features are the same\r\nfeature but for different entiity ids). The default behavior of join is to name the output column name using the feathr\r\nfeature name, but in a case like the above case, that would result in two columns with the same name,\r\nwhich is not valid for FDS. In these cases, the user has to provide an alias for at least one of these joined features.\r\nFor example, the user can use featureAliases such as \"viewer_skills\" and \"viewee_skills\".\r\nIn these cases, featureAliases becomes mandatory.*/featureAlias:optional string/**dateRange is used in Time-based joins, which refers to the situation when one or multiple days of input data needs\r\nto be used for training.\r\nOne of the common use cases where this is used, is in training with some time-insensitive features, or\r\ntraining pipeline that always use the full day data, one day before running (since there is only partial data for today).\r\nThe time for the input featurized dataset can be set using this field.\r\nHourly data is not allowed in this case.\r\n\r\nFor example,\r\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\r\n22nd May 2020 to 25th May, 2020 with both dates included.\r\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\r\nadd support for other date time formats as well.\r\n\r\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\r\ntill 11/04/2020 willl be joined.\r\n\r\nP.S - This is different from the timeRange used in settings as the settings startTime is applicable for the entire input data,\r\nwhile this a feature level setting. Also, we do not support hourly time here.*/dateRange:optional union[absoluteDateRange:/**The absolute date range with start and end date being required fields.\r\nIt accepts a start date and an end date which should be specifiied using the [[Date.pdl]] class.\r\nabsoluteDateRange: {\r\n   startDate: Date(day=1, month=1, year=2020)\r\n   endDate: Date(day=3, month=1, year=2020)\r\n }\r\n In this case, the endDate > startDate.*/record AbsoluteDateRange{/**start date of the date range, with the start date included in the range.*/startDate:Date/**end date of the date range, with the end date included in the range.*/endDate:Date}relativeDateRange:/**The date range represented relative to the current date. It uses the current system date as the reference and can be used to\r\nexpress a range of dates with respect to the current date.\r\nExample, - If current date is 01/01/2020, window is 3, and offset 1 (unit is number of days)\r\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\r\n\r\nIf dateOffset is not specified, it defaults to 0.\r\nrelativeDateRange: RelativeDateRange(numDays=2, dateOffset=1)\r\nrelativeDateRange: RelativeDateRange(numDays=5)*/record RelativeDateRange{/**Represents a length of time.\r\nnumDays is the window from the reference date to look back to obtain a dateRange.\r\nFor example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020\r\ntill 11/05/2020.*/@validate.positive={}numDays:long/**Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date\r\nwill be 4 days ago from today.*/dateOffset:long=0}]/**The override time delay parameter which will override the global simulate time delay specified in the settings section for\r\nthe particular feature.\r\nThis parameter is only applicable when the simulate time delay is set in the settings section\r\nFor example, let us say the global simulate delay was 5d, and the overrideTimeDelay is set to 3d.\r\nThen, for this specificc feature, a simulate delay of 3d will be applied.*/overrideTimeDelay:optional TimeOffset}]}", SchemaFormatType.PDL));
    private Settings _settingsField = null;
    private JoiningFeatureArray _featuresField = null;
    private FrameFeatureJoinConfig.ChangeListener __changeListener = new FrameFeatureJoinConfig.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Settings = SCHEMA.getField("settings");
    private final static RecordDataSchema.Field FIELD_Features = SCHEMA.getField("features");

    public FrameFeatureJoinConfig() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public FrameFeatureJoinConfig(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FrameFeatureJoinConfig.Fields fields() {
        return _fields;
    }

    public static FrameFeatureJoinConfig.ProjectionMask createMask() {
        return new FrameFeatureJoinConfig.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for settings
     * 
     * @see FrameFeatureJoinConfig.Fields#settings
     */
    public boolean hasSettings() {
        if (_settingsField!= null) {
            return true;
        }
        return super._map.containsKey("settings");
    }

    /**
     * Remover for settings
     * 
     * @see FrameFeatureJoinConfig.Fields#settings
     */
    public void removeSettings() {
        super._map.remove("settings");
    }

    /**
     * Getter for settings
     * 
     * @see FrameFeatureJoinConfig.Fields#settings
     */
    public Settings getSettings(GetMode mode) {
        return getSettings();
    }

    /**
     * Getter for settings
     * 
     * @return
     *     Optional field. Always check for null.
     * @see FrameFeatureJoinConfig.Fields#settings
     */
    @Nullable
    public Settings getSettings() {
        if (_settingsField!= null) {
            return _settingsField;
        } else {
            Object __rawValue = super._map.get("settings");
            _settingsField = ((__rawValue == null)?null:new Settings(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _settingsField;
        }
    }

    /**
     * Setter for settings
     * 
     * @see FrameFeatureJoinConfig.Fields#settings
     */
    public FrameFeatureJoinConfig setSettings(Settings value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSettings(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeSettings();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "settings", value.data());
                    _settingsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "settings", value.data());
                    _settingsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for settings
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FrameFeatureJoinConfig.Fields#settings
     */
    public FrameFeatureJoinConfig setSettings(
        @Nonnull
        Settings value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field settings of com.linkedin.feathr.config.join.FrameFeatureJoinConfig to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "settings", value.data());
            _settingsField = value;
        }
        return this;
    }

    /**
     * Existence checker for features
     * 
     * @see FrameFeatureJoinConfig.Fields#features
     */
    public boolean hasFeatures() {
        if (_featuresField!= null) {
            return true;
        }
        return super._map.containsKey("features");
    }

    /**
     * Remover for features
     * 
     * @see FrameFeatureJoinConfig.Fields#features
     */
    public void removeFeatures() {
        super._map.remove("features");
    }

    /**
     * Getter for features
     * 
     * @see FrameFeatureJoinConfig.Fields#features
     */
    public JoiningFeatureArray getFeatures(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFeatures();
            case DEFAULT:
            case NULL:
                if (_featuresField!= null) {
                    return _featuresField;
                } else {
                    Object __rawValue = super._map.get("features");
                    _featuresField = ((__rawValue == null)?null:new JoiningFeatureArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _featuresField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for features
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FrameFeatureJoinConfig.Fields#features
     */
    @Nonnull
    public JoiningFeatureArray getFeatures() {
        if (_featuresField!= null) {
            return _featuresField;
        } else {
            Object __rawValue = super._map.get("features");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("features");
            }
            _featuresField = ((__rawValue == null)?null:new JoiningFeatureArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _featuresField;
        }
    }

    /**
     * Setter for features
     * 
     * @see FrameFeatureJoinConfig.Fields#features
     */
    public FrameFeatureJoinConfig setFeatures(JoiningFeatureArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatures(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field features of com.linkedin.feathr.config.join.FrameFeatureJoinConfig");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "features", value.data());
                    _featuresField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatures();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "features", value.data());
                    _featuresField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "features", value.data());
                    _featuresField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for features
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FrameFeatureJoinConfig.Fields#features
     */
    public FrameFeatureJoinConfig setFeatures(
        @Nonnull
        JoiningFeatureArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field features of com.linkedin.feathr.config.join.FrameFeatureJoinConfig to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "features", value.data());
            _featuresField = value;
        }
        return this;
    }

    @Override
    public FrameFeatureJoinConfig clone()
        throws CloneNotSupportedException
    {
        FrameFeatureJoinConfig __clone = ((FrameFeatureJoinConfig) super.clone());
        __clone.__changeListener = new FrameFeatureJoinConfig.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FrameFeatureJoinConfig copy()
        throws CloneNotSupportedException
    {
        FrameFeatureJoinConfig __copy = ((FrameFeatureJoinConfig) super.copy());
        __copy._settingsField = null;
        __copy._featuresField = null;
        __copy.__changeListener = new FrameFeatureJoinConfig.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FrameFeatureJoinConfig __objectRef;

        private ChangeListener(FrameFeatureJoinConfig reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "settings":
                    __objectRef._settingsField = null;
                    break;
                case "features":
                    __objectRef._featuresField = null;
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
         * settings required for joining input featurized dataset with the feature data.
         * 
         */
        public com.linkedin.feathr.config.join.Settings.Fields settings() {
            return new com.linkedin.feathr.config.join.Settings.Fields(getPathComponents(), "settings");
        }

        /**
         * Array of joining features.
         * 
         * Validation rules:
         * - The array must be non-empty.
         * 
         */
        public com.linkedin.feathr.config.join.JoiningFeatureArray.Fields features() {
            return new com.linkedin.feathr.config.join.JoiningFeatureArray.Fields(getPathComponents(), "features");
        }

        /**
         * Array of joining features.
         * 
         * Validation rules:
         * - The array must be non-empty.
         * 
         */
        public PathSpec features(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "features");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.Settings.ProjectionMask _settingsMask;
        private com.linkedin.feathr.config.join.JoiningFeatureArray.ProjectionMask _featuresMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * settings required for joining input featurized dataset with the feature data.
         * 
         */
        public FrameFeatureJoinConfig.ProjectionMask withSettings(Function<com.linkedin.feathr.config.join.Settings.ProjectionMask, com.linkedin.feathr.config.join.Settings.ProjectionMask> nestedMask) {
            _settingsMask = nestedMask.apply(((_settingsMask == null)?Settings.createMask():_settingsMask));
            getDataMap().put("settings", _settingsMask.getDataMap());
            return this;
        }

        /**
         * settings required for joining input featurized dataset with the feature data.
         * 
         */
        public FrameFeatureJoinConfig.ProjectionMask withSettings() {
            _settingsMask = null;
            getDataMap().put("settings", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Array of joining features.
         * 
         * Validation rules:
         * - The array must be non-empty.
         * 
         */
        public FrameFeatureJoinConfig.ProjectionMask withFeatures(Function<com.linkedin.feathr.config.join.JoiningFeatureArray.ProjectionMask, com.linkedin.feathr.config.join.JoiningFeatureArray.ProjectionMask> nestedMask) {
            _featuresMask = nestedMask.apply(((_featuresMask == null)?JoiningFeatureArray.createMask():_featuresMask));
            getDataMap().put("features", _featuresMask.getDataMap());
            return this;
        }

        /**
         * Array of joining features.
         * 
         * Validation rules:
         * - The array must be non-empty.
         * 
         */
        public FrameFeatureJoinConfig.ProjectionMask withFeatures() {
            _featuresMask = null;
            getDataMap().put("features", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Array of joining features.
         * 
         * Validation rules:
         * - The array must be non-empty.
         * 
         */
        public FrameFeatureJoinConfig.ProjectionMask withFeatures(Function<com.linkedin.feathr.config.join.JoiningFeatureArray.ProjectionMask, com.linkedin.feathr.config.join.JoiningFeatureArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _featuresMask = nestedMask.apply(((_featuresMask == null)?JoiningFeatureArray.createMask():_featuresMask));
            getDataMap().put("features", _featuresMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("features").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("features").put("$count", count);
            }
            return this;
        }

        /**
         * Array of joining features.
         * 
         * Validation rules:
         * - The array must be non-empty.
         * 
         */
        public FrameFeatureJoinConfig.ProjectionMask withFeatures(Integer start, Integer count) {
            _featuresMask = null;
            getDataMap().put("features", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("features").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("features").put("$count", count);
            }
            return this;
        }

    }

}
