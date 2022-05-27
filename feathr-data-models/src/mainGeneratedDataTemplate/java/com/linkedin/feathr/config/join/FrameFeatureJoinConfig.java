
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
 * to join the feature source. Feature name is canonical frame feature names.
 * Each feature can also have a set of optional time-related parameters. These parameter override the ones provided in
 * the settings section and are applicable only to the particular feature.
 * Feature join config operation.
 * 
 * All these PDLs are moved to Frame MP:- https://rb.corp.linkedin.com/r/2356512/
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/FrameFeatureJoinConfig.pdl.")
public class FrameFeatureJoinConfig
    extends RecordTemplate
{

    private final static FrameFeatureJoinConfig.Fields _fields = new FrameFeatureJoinConfig.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The join config consists of 2 parts, settings and features section.\nSettings is related to the general settings corresponding to joining the input data set with the\nfeatures, currently there are time related settings, but this can be extended to other settings as well.\nFeatures to be joined are described by list of Keys and featureName and featureAlias.\nFeatures in the feature list should be joined to the user's input data.\nmatching the key in the input data.\nFor example,\nkey is [\"key1\"] and join feature1 and feature2 with input data\n  settings: {      // optional field\n    inputDataTimeSettings: {\n       absoluteTimeRange: {\n           startTime: Date(year=2020, month=4, day=28)\n           endTime: Date(year=2020, month=5, day=5)\n       }\n    }\n    joinTimeSettings: {\n       timestampColumn: {\n         def: timestamp\n         format: yyyy-MM-dd\n       }\n       simulateTimeDelay: 5d\n    }\n  }\n features=[\n  JoiningFeature{\n    keys: [\"key1\"]\n    frameFeatureName: \"feature1\"\n    AbsoluteDateRange(startDate: Date(year=2020, month=5, day=1),\n                      endTime: Date(year=2020, month=5, day=5))\n  }, JoiningFeature{\n    keys: [\"key1\"]\n    frameFeatureName: \"feature2\"\n    overrideTimeDelay: 5d\n  }, JoiningFeature{\n     keys: [\"key1\"]\n     frameFeatureName: \"feature3\"\n     RelativeDateRange(numDays: 5,\n                       offset: 3)\n  }, JoiningFeature{\n    keys: [\"key1\"]\n    frameFeatureName: \"feature4\"\n  }\n ]\n\nHere, the keys are corresponding to column names in the input FeaturizedDataset, which will be used\nto join the feature source. Feature name is canonical frame feature names.\nEach feature can also have a set of optional time-related parameters. These parameter override the ones provided in\nthe settings section and are applicable only to the particular feature.\nFeature join config operation.\n\nAll these PDLs are moved to Frame MP:- https://rb.corp.linkedin.com/r/2356512/*/record FrameFeatureJoinConfig{/**settings required for joining input featurized dataset with the feature data.*/settings:optional/**The settings section contains all the config parameters required for the joining of input dataset with the\nfeature data. As of now, we have only time related parameters, but in future this can be expanded.\nThis section has configs related to:-\na. How do I load the input dataset if it is time sensitive?\nb. How do I specify the join parameters for input dataset?\nFor more details - https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#\nsettings: {\n   inputDataTimeSettings: {\n     absoluteTimeRange: {\n       startTime: 20200809\n       endTime: 20200810\n       timeFormat: yyyyMMdd\n     }\n   }\n   joinTimeSettings: {\n     useLatestFeatureData: true\n   }\n}*/record Settings{/**Config parameters related to loading of the time sensitive input data. Contains parameters related to restricting the\nsize of the input data with respect to the timestamp column.*/inputDataTimeSettings:optional/**The data time settings pertaining to how much of the input dataset is to be loaded from the timestamp column. This is a way in which\nthe input data can be restricted to allow only a fixed interval of dates to be joined with the feature data. This restriction\nwill apply on the timestamp column of the input data.\ninputDataTimeSettings: {\n  absoluteTimeRange: {\n    startTime: Date(year=2020, month=8, day=8)\n    endTime: Date(year=2020, month=8, day=10)\n  }\n (or)\n relativeTimeRange: {\n   offset: TimeOffset(length=1, unit=\"DAY\")\n   window: TimeWindow(length=1, unit=\"DAY\")\n }\n}*/record InputDataTimeSettings{/**Union of [[AbsoluteTimeRange]] and [[RelativeTimeRange]].\nIt indicates the range of input data which is to be loaded. This field generally refers to how much of the input\ndata should be restricted using the time in the timestamp column.\n\nFor example,\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\n22nd May 2020 to 25th May, 2020 with both dates included.\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\nadd support for other date time formats as well.\n\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\ntill 11/04/2020 willl be joined.*/timeRange:union[absoluteTimeRange:/**The absolute time range with start and end time being required fields.\nIt accepts a start time and an end time which should be specifiied using the [[Date.pdl]] or the [[HourTime.pdl]] class.\nThis model can be used to represent time range in daily or hourly interval.\nabsoluteTimeRange: {\n   startTime: TimeHour(day=1, month=1, year=2020, hour=13)\n   endTime: TimeHour(day=3, month=1, year=2020, hour=2)\n }\n(or)\nabsoluteTimeRange: {\n   startTime: Date(day=1, month=1, year=2020)\n   endTime: Date(day=3, month=1, year=2020)\n }\nendTime and startTime should always have the same granularity, ie - Daily or Hourly.\nendTme > startTime*/record AbsoluteTimeRange{/**start time of the date range, in daily or hourly format with the start date included in the range.*/startTime:union[date:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}hourTime:/**Time with hourly granularity*/record HourTime{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int/**hour*/@validate.integerRange={\"max\":23,\"min\":0}hour:int}]/**end date of the date range, in daily or hourly format with the end date included in the range.*/endTime:union[date:Date,hourTime:HourTime]}relativeTimeRange:/**The time range represented relative to the current timestamp. It uses the current system time as the reference and can be used to\nexpress a range of times with respect to the current time.\nExample, - If current time is 01/01/2020, window is 3 days, and offset is 1 day (unit can be day or hour).\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"DAY\"), offset=TimeOffset(length=1, unit=\"Day\"))\nrelativeTimeRange: RelativeTimeRange(window=TimeWindow(length=2, unit=\"HOUR\"))*/record RelativeTimeRange{/**Window is the number of time units from the reference time units to look back to obtain the timeRange.\nFor example, window - 5days implies, if reference date is 11/09/2020, then range will be from 11/09/2020\ntill 11/05/2020 (both days included).\nwindow >= 1 TimeUnit*/window:/**Represents a length of time along with the corresponding time unit (DAY, HOUR).*/record TimeWindow{/**Amount of the duration in TimeUnits. Can be greater or equal to 1.*/@validate.positive,length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}/**Number of time units (corresponding to window's timeUnits) to backdate from current time, to obtain the reference time.\nFor example, if dateOffset is 4, and window is 2 days, then reference time\nwill be 4 days ago from today.\nExample - if today's date is 11th Dec, 2020 and offset is 4 days - Reference time will be 7th Dec, 2020.\nThis will always take the window's timeUnits.*/@validate.integerRange.min=0,offset:long=0}]}/**This contains all the parameters required to join the time sensitive input data with the feature data.*/joinTimeSettings:optional/**JoinTimeSettings contains all the parameters required to join the time sensitive input data with the feature data.\nThe input data can be time sensitive in two ways:-\na. Have a timestamp column\nb. Always join with the latest available feature data. In this case, we do not require a timestamp column.\nc. The file path is time-partition and the path time is used for the join\n(Todo PROML-11936 - Add useTimePartitionPattern field in this section)\nIn this section, the user needs to let Frame know which of the above properties is to be used for the join.*/typeref JoinTimeSettings=union[useLatestJoinTimeSettings:/**Settings needed when the input data is to be joined with the latest available feature data.\njoinTimeSettings: {\n   useLatestFeatureData: true\n}*/record UseLatestJoinTimeSettings{/**Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.\nWhen useLatestFeatureData is set, there should be no other time-based parameters.*/useLatestFeatureData:boolean=true}timestampColJoinTimeSettings:/**Settings needed when the input data has a timestamp which should be used for the join.\njoinTimeSettings: {\n   timestampColumn: {\n      def: timestamp\n      format: yyyy/MM/dd\n   }\n   simulateTimeDelay: 1d\n}*/record TimestampColJoinTimeSettings{/**The timestamp column name and timeformat which should be used for joining with the feature data.\nRefer to [[TimestampColumn]].\nExample, TimestampColumn: {\n           def: timestamp\n           format: yyyy/MM/dd\n         }*/timestampColumn:/**Timestamp column of the input featureiized dataset, which is to be used for the join.\ntimestampColumn: {\n   def: timestamp\n   format: yyyyMMdd\n }*/record TimestampColumn{/**The definiton of the timestamp column, which can be a sql expression involving the timestamp column\nor just the column name\nExample:- definition: timestamp, timestamp + 10000000.*/definition:union[columnName:string,sparkSqlExpression:/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}]/**Format of the timestamp column. Must confer to java's timestampFormatter or can be\nepoch or epoch_millis.\nExample:- epoch, epoch_millis, yyyy/MM/dd*/format:/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string}/**An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted\nfrom the input data timestamp while joining with the feature data.\nWe do support negative time delays.*/simulateTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\nbe any time in the past also, we do allow a positive or negative offset length.\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:TimeUnit}}]}/**Array of joining features.\n\nValidation rules:\n- The array must be non-empty.*/features:array[/**JoiningFeature is the feature section of the join config. This section consists of information pertaining to a feature\nwhich is to be joined:-\na. The join keys of the input data, with which this feature is to be joined.\nb. name of the feature\nc. optional timeRange of the input data which is to be joined with this feature.\nd. optional overrideTimeDelay if this feature needs a different simulate time delay other than the one mentioned.\n\nThis is a required section of the the join config.\nExample,\n  a. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature1\"\n       AbsoluteDateRange(startDate: Date(year=2020, month=5, day=5),\n                        endDate: Date(year=2020, month=5, day=7))\n     }\n  b. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature2\"\n       overrideTimeDelay: TimeDelay(length=1, unit=\"DAY\")\n   }\n  c. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature3\"\n       RelativeDateRange(numDays: 5,\n                         offset: 3)\n  }*/record JoiningFeature{/**Keys to join input with feature source, the field name of the key in the input featuized dataset.*/keys:array[string]/**Feature name as defined in Frame's feature definition configuration.\nSee https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Feature+Definition+User+Reference for details.\n\nCurrently the column in the output FDS that holds this feature will have the same name as feature name.\nIf multiple joined features have the same name and no alias is defined for them, Frame will prepend the keys to the feature name.\n\nIn the future, if \"featureAlias\" is not set, the column in the output FDS that holds this feature will have the same name as feature name.\nIf multiple joined features have the same name and no alias is defined for them, the join operation will fail\n(to avoid produciing two columns in the output FDS with the same name).\nTODO (PROML-10038): support the use of feature alias and update the comment when we have aliases*/frameFeatureName:string/**The development of this is in progress. This is not in use for now.\n\nThe name to be used for the column in the output FDS that contains the values from this joined feature.\nIf not set, the name of the feature (frameFeatureName) will be used for the output column.\nFor example, if the user request joining a feature named \"careers_job_listTime\" and provides no alias,\nthe output FDS will contain a column called \"careers_job_listTime\". However, if the user sets \"featureAlias\" to \"list_time\",\nthe column will be named \"list_time\".\n\nfeature alias can be useful for in a few cases:\n - If the user prefers to use a name different than the frame name in their model,\nthey can use an alias to control the name of the column in the output FDS.\n - Sometimes, the training datas needs to have two features that are from the same frame feature.\nFor example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B\n(viewee) and we want to use the skills of both viewer and viewee as features, we need to join frame feature\n\"member_skills\" of member A with frame feature \"member_skills\" of member B. That is, the two features are the same\nfeature but for different entiity ids). The default behavior of join is to name the output column name using the frame\nfeature name, but in a case like the above case, that would result in two columns with the same name,\nwhich is not valid for FDS. In these cases, the user has to provide an alias for at least one of these joined features.\nFor example, the user can use featureAliases such as \"viewer_skills\" and \"viewee_skills\".\nIn these cases, featureAliases becomes mandatory.\nTODO (PROML-10038): support the use of feature alias.*/featureAlias:optional string/**dateRange is used in Time-based joins, which refers to the situation when one or multiple days of input data needs\nto be used for training.\n(https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-timebasedobservation).\nOne of the common use cases where this is used, is in training with some time-insensitive features, or\ntraining pipeline that always use the full day data, one day before running (since there is only partial data for today).\nThe time for the input featurized dataset can be set using this field.\nHourly data is not allowed in this case.\n\nFor example,\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\n22nd May 2020 to 25th May, 2020 with both dates included.\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\nadd support for other date time formats as well.\n\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\ntill 11/04/2020 willl be joined.\n\nP.S - This is different from the timeRange used in settings as the settings startTime is applicable for the entire input data,\nwhile this a feature level setting. Also, we do not support hourly time here.*/dateRange:optional union[absoluteDateRange:/**The absolute date range with start and end date being required fields.\nIt accepts a start date and an end date which should be specifiied using the [[Date.pdl]] class.\nabsoluteDateRange: {\n   startDate: Date(day=1, month=1, year=2020)\n   endDate: Date(day=3, month=1, year=2020)\n }\n In this case, the endDate > startDate.*/record AbsoluteDateRange{/**start date of the date range, with the start date included in the range.*/startDate:Date/**end date of the date range, with the end date included in the range.*/endDate:Date}relativeDateRange:/**The date range represented relative to the current date. It uses the current system date as the reference and can be used to\nexpress a range of dates with respect to the current date.\nExample, - If current date is 01/01/2020, window is 3, and offset 1 (unit is number of days)\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nIf dateOffset is not specified, it defaults to 0.\nrelativeDateRange: RelativeDateRange(numDays=2, dateOffset=1)\nrelativeDateRange: RelativeDateRange(numDays=5)*/record RelativeDateRange{/**Represents a length of time.\nnumDays is the window from the reference date to look back to obtain a dateRange.\nFor example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020\ntill 11/05/2020.*/@validate.positive={}numDays:long/**Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date\nwill be 4 days ago from today.*/dateOffset:long=0}]/**The override time delay parameter which will override the global simulate time delay specified in the settings section for\nthe particular feature.\nThis parameter is only applicable when the simulate time delay is set in the settings section\nFor example, let us say the global simulate delay was 5d, and the overrideTimeDelay is set to 3d.\nThen, for this specificc feature, a simulate delay of 3d will be applied.*/overrideTimeDelay:optional TimeOffset}]}", SchemaFormatType.PDL));
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
