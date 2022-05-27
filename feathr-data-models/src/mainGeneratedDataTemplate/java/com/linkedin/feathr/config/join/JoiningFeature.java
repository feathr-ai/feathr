
package com.linkedin.feathr.config.join;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataList;
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
import com.linkedin.data.template.StringArray;
import com.linkedin.data.template.UnionTemplate;


/**
 * JoiningFeature is the feature section of the join config. This section consists of information pertaining to a feature
 * which is to be joined:-
 * a. The join keys of the input data, with which this feature is to be joined.
 * b. name of the feature
 * c. optional timeRange of the input data which is to be joined with this feature.
 * d. optional overrideTimeDelay if this feature needs a different simulate time delay other than the one mentioned.
 * 
 * This is a required section of the the join config.
 * Example,
 *   a. JoiningFeature{
 *        keys: ["key1"]
 *        frameFeatureName: "feature1"
 *        AbsoluteDateRange(startDate: Date(year=2020, month=5, day=5),
 *                         endDate: Date(year=2020, month=5, day=7))
 *      }
 *   b. JoiningFeature{
 *        keys: ["key1"]
 *        frameFeatureName: "feature2"
 *        overrideTimeDelay: TimeDelay(length=1, unit="DAY")
 *    }
 *   c. JoiningFeature{
 *        keys: ["key1"]
 *        frameFeatureName: "feature3"
 *        RelativeDateRange(numDays: 5,
 *                          offset: 3)
 *   }
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/JoiningFeature.pdl.")
public class JoiningFeature
    extends RecordTemplate
{

    private final static JoiningFeature.Fields _fields = new JoiningFeature.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**JoiningFeature is the feature section of the join config. This section consists of information pertaining to a feature\nwhich is to be joined:-\na. The join keys of the input data, with which this feature is to be joined.\nb. name of the feature\nc. optional timeRange of the input data which is to be joined with this feature.\nd. optional overrideTimeDelay if this feature needs a different simulate time delay other than the one mentioned.\n\nThis is a required section of the the join config.\nExample,\n  a. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature1\"\n       AbsoluteDateRange(startDate: Date(year=2020, month=5, day=5),\n                        endDate: Date(year=2020, month=5, day=7))\n     }\n  b. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature2\"\n       overrideTimeDelay: TimeDelay(length=1, unit=\"DAY\")\n   }\n  c. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature3\"\n       RelativeDateRange(numDays: 5,\n                         offset: 3)\n  }*/record JoiningFeature{/**Keys to join input with feature source, the field name of the key in the input featuized dataset.*/keys:array[string]/**Feature name as defined in Frame's feature definition configuration.\nSee https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Feature+Definition+User+Reference for details.\n\nCurrently the column in the output FDS that holds this feature will have the same name as feature name.\nIf multiple joined features have the same name and no alias is defined for them, Frame will prepend the keys to the feature name.\n\nIn the future, if \"featureAlias\" is not set, the column in the output FDS that holds this feature will have the same name as feature name.\nIf multiple joined features have the same name and no alias is defined for them, the join operation will fail\n(to avoid produciing two columns in the output FDS with the same name).\nTODO (PROML-10038): support the use of feature alias and update the comment when we have aliases*/frameFeatureName:string/**The development of this is in progress. This is not in use for now.\n\nThe name to be used for the column in the output FDS that contains the values from this joined feature.\nIf not set, the name of the feature (frameFeatureName) will be used for the output column.\nFor example, if the user request joining a feature named \"careers_job_listTime\" and provides no alias,\nthe output FDS will contain a column called \"careers_job_listTime\". However, if the user sets \"featureAlias\" to \"list_time\",\nthe column will be named \"list_time\".\n\nfeature alias can be useful for in a few cases:\n - If the user prefers to use a name different than the frame name in their model,\nthey can use an alias to control the name of the column in the output FDS.\n - Sometimes, the training datas needs to have two features that are from the same frame feature.\nFor example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B\n(viewee) and we want to use the skills of both viewer and viewee as features, we need to join frame feature\n\"member_skills\" of member A with frame feature \"member_skills\" of member B. That is, the two features are the same\nfeature but for different entiity ids). The default behavior of join is to name the output column name using the frame\nfeature name, but in a case like the above case, that would result in two columns with the same name,\nwhich is not valid for FDS. In these cases, the user has to provide an alias for at least one of these joined features.\nFor example, the user can use featureAliases such as \"viewer_skills\" and \"viewee_skills\".\nIn these cases, featureAliases becomes mandatory.\nTODO (PROML-10038): support the use of feature alias.*/featureAlias:optional string/**dateRange is used in Time-based joins, which refers to the situation when one or multiple days of input data needs\nto be used for training.\n(https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-timebasedobservation).\nOne of the common use cases where this is used, is in training with some time-insensitive features, or\ntraining pipeline that always use the full day data, one day before running (since there is only partial data for today).\nThe time for the input featurized dataset can be set using this field.\nHourly data is not allowed in this case.\n\nFor example,\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\n22nd May 2020 to 25th May, 2020 with both dates included.\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\nadd support for other date time formats as well.\n\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\ntill 11/04/2020 willl be joined.\n\nP.S - This is different from the timeRange used in settings as the settings startTime is applicable for the entire input data,\nwhile this a feature level setting. Also, we do not support hourly time here.*/dateRange:optional union[absoluteDateRange:/**The absolute date range with start and end date being required fields.\nIt accepts a start date and an end date which should be specifiied using the [[Date.pdl]] class.\nabsoluteDateRange: {\n   startDate: Date(day=1, month=1, year=2020)\n   endDate: Date(day=3, month=1, year=2020)\n }\n In this case, the endDate > startDate.*/record AbsoluteDateRange{/**start date of the date range, with the start date included in the range.*/startDate:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}/**end date of the date range, with the end date included in the range.*/endDate:Date}relativeDateRange:/**The date range represented relative to the current date. It uses the current system date as the reference and can be used to\nexpress a range of dates with respect to the current date.\nExample, - If current date is 01/01/2020, window is 3, and offset 1 (unit is number of days)\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nIf dateOffset is not specified, it defaults to 0.\nrelativeDateRange: RelativeDateRange(numDays=2, dateOffset=1)\nrelativeDateRange: RelativeDateRange(numDays=5)*/record RelativeDateRange{/**Represents a length of time.\nnumDays is the window from the reference date to look back to obtain a dateRange.\nFor example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020\ntill 11/05/2020.*/@validate.positive={}numDays:long/**Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date\nwill be 4 days ago from today.*/dateOffset:long=0}]/**The override time delay parameter which will override the global simulate time delay specified in the settings section for\nthe particular feature.\nThis parameter is only applicable when the simulate time delay is set in the settings section\nFor example, let us say the global simulate delay was 5d, and the overrideTimeDelay is set to 3d.\nThen, for this specificc feature, a simulate delay of 3d will be applied.*/overrideTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\nbe any time in the past also, we do allow a positive or negative offset length.\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}}", SchemaFormatType.PDL));
    private StringArray _keysField = null;
    private String _frameFeatureNameField = null;
    private String _featureAliasField = null;
    private JoiningFeature.DateRange _dateRangeField = null;
    private TimeOffset _overrideTimeDelayField = null;
    private JoiningFeature.ChangeListener __changeListener = new JoiningFeature.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Keys = SCHEMA.getField("keys");
    private final static RecordDataSchema.Field FIELD_FrameFeatureName = SCHEMA.getField("frameFeatureName");
    private final static RecordDataSchema.Field FIELD_FeatureAlias = SCHEMA.getField("featureAlias");
    private final static RecordDataSchema.Field FIELD_DateRange = SCHEMA.getField("dateRange");
    private final static RecordDataSchema.Field FIELD_OverrideTimeDelay = SCHEMA.getField("overrideTimeDelay");

    public JoiningFeature() {
        super(new DataMap(7, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public JoiningFeature(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static JoiningFeature.Fields fields() {
        return _fields;
    }

    public static JoiningFeature.ProjectionMask createMask() {
        return new JoiningFeature.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keys
     * 
     * @see JoiningFeature.Fields#keys
     */
    public boolean hasKeys() {
        if (_keysField!= null) {
            return true;
        }
        return super._map.containsKey("keys");
    }

    /**
     * Remover for keys
     * 
     * @see JoiningFeature.Fields#keys
     */
    public void removeKeys() {
        super._map.remove("keys");
    }

    /**
     * Getter for keys
     * 
     * @see JoiningFeature.Fields#keys
     */
    public StringArray getKeys(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeys();
            case DEFAULT:
            case NULL:
                if (_keysField!= null) {
                    return _keysField;
                } else {
                    Object __rawValue = super._map.get("keys");
                    _keysField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keysField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keys
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see JoiningFeature.Fields#keys
     */
    @Nonnull
    public StringArray getKeys() {
        if (_keysField!= null) {
            return _keysField;
        } else {
            Object __rawValue = super._map.get("keys");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keys");
            }
            _keysField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keysField;
        }
    }

    /**
     * Setter for keys
     * 
     * @see JoiningFeature.Fields#keys
     */
    public JoiningFeature setKeys(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeys(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keys of com.linkedin.feathr.config.join.JoiningFeature");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keys", value.data());
                    _keysField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeys();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keys", value.data());
                    _keysField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keys", value.data());
                    _keysField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keys
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see JoiningFeature.Fields#keys
     */
    public JoiningFeature setKeys(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keys of com.linkedin.feathr.config.join.JoiningFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keys", value.data());
            _keysField = value;
        }
        return this;
    }

    /**
     * Existence checker for frameFeatureName
     * 
     * @see JoiningFeature.Fields#frameFeatureName
     */
    public boolean hasFrameFeatureName() {
        if (_frameFeatureNameField!= null) {
            return true;
        }
        return super._map.containsKey("frameFeatureName");
    }

    /**
     * Remover for frameFeatureName
     * 
     * @see JoiningFeature.Fields#frameFeatureName
     */
    public void removeFrameFeatureName() {
        super._map.remove("frameFeatureName");
    }

    /**
     * Getter for frameFeatureName
     * 
     * @see JoiningFeature.Fields#frameFeatureName
     */
    public String getFrameFeatureName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFrameFeatureName();
            case DEFAULT:
            case NULL:
                if (_frameFeatureNameField!= null) {
                    return _frameFeatureNameField;
                } else {
                    Object __rawValue = super._map.get("frameFeatureName");
                    _frameFeatureNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _frameFeatureNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for frameFeatureName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see JoiningFeature.Fields#frameFeatureName
     */
    @Nonnull
    public String getFrameFeatureName() {
        if (_frameFeatureNameField!= null) {
            return _frameFeatureNameField;
        } else {
            Object __rawValue = super._map.get("frameFeatureName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("frameFeatureName");
            }
            _frameFeatureNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _frameFeatureNameField;
        }
    }

    /**
     * Setter for frameFeatureName
     * 
     * @see JoiningFeature.Fields#frameFeatureName
     */
    public JoiningFeature setFrameFeatureName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFrameFeatureName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field frameFeatureName of com.linkedin.feathr.config.join.JoiningFeature");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
                    _frameFeatureNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFrameFeatureName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
                    _frameFeatureNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
                    _frameFeatureNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for frameFeatureName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see JoiningFeature.Fields#frameFeatureName
     */
    public JoiningFeature setFrameFeatureName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field frameFeatureName of com.linkedin.feathr.config.join.JoiningFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
            _frameFeatureNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureAlias
     * 
     * @see JoiningFeature.Fields#featureAlias
     */
    public boolean hasFeatureAlias() {
        if (_featureAliasField!= null) {
            return true;
        }
        return super._map.containsKey("featureAlias");
    }

    /**
     * Remover for featureAlias
     * 
     * @see JoiningFeature.Fields#featureAlias
     */
    public void removeFeatureAlias() {
        super._map.remove("featureAlias");
    }

    /**
     * Getter for featureAlias
     * 
     * @see JoiningFeature.Fields#featureAlias
     */
    public String getFeatureAlias(GetMode mode) {
        return getFeatureAlias();
    }

    /**
     * Getter for featureAlias
     * 
     * @return
     *     Optional field. Always check for null.
     * @see JoiningFeature.Fields#featureAlias
     */
    @Nullable
    public String getFeatureAlias() {
        if (_featureAliasField!= null) {
            return _featureAliasField;
        } else {
            Object __rawValue = super._map.get("featureAlias");
            _featureAliasField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _featureAliasField;
        }
    }

    /**
     * Setter for featureAlias
     * 
     * @see JoiningFeature.Fields#featureAlias
     */
    public JoiningFeature setFeatureAlias(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureAlias(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureAlias();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureAlias", value);
                    _featureAliasField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureAlias", value);
                    _featureAliasField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureAlias
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see JoiningFeature.Fields#featureAlias
     */
    public JoiningFeature setFeatureAlias(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureAlias of com.linkedin.feathr.config.join.JoiningFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureAlias", value);
            _featureAliasField = value;
        }
        return this;
    }

    /**
     * Existence checker for dateRange
     * 
     * @see JoiningFeature.Fields#dateRange
     */
    public boolean hasDateRange() {
        if (_dateRangeField!= null) {
            return true;
        }
        return super._map.containsKey("dateRange");
    }

    /**
     * Remover for dateRange
     * 
     * @see JoiningFeature.Fields#dateRange
     */
    public void removeDateRange() {
        super._map.remove("dateRange");
    }

    /**
     * Getter for dateRange
     * 
     * @see JoiningFeature.Fields#dateRange
     */
    public JoiningFeature.DateRange getDateRange(GetMode mode) {
        return getDateRange();
    }

    /**
     * Getter for dateRange
     * 
     * @return
     *     Optional field. Always check for null.
     * @see JoiningFeature.Fields#dateRange
     */
    @Nullable
    public JoiningFeature.DateRange getDateRange() {
        if (_dateRangeField!= null) {
            return _dateRangeField;
        } else {
            Object __rawValue = super._map.get("dateRange");
            _dateRangeField = ((__rawValue == null)?null:new JoiningFeature.DateRange(__rawValue));
            return _dateRangeField;
        }
    }

    /**
     * Setter for dateRange
     * 
     * @see JoiningFeature.Fields#dateRange
     */
    public JoiningFeature setDateRange(JoiningFeature.DateRange value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDateRange(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDateRange();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dateRange", value.data());
                    _dateRangeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dateRange", value.data());
                    _dateRangeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dateRange
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see JoiningFeature.Fields#dateRange
     */
    public JoiningFeature setDateRange(
        @Nonnull
        JoiningFeature.DateRange value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dateRange of com.linkedin.feathr.config.join.JoiningFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dateRange", value.data());
            _dateRangeField = value;
        }
        return this;
    }

    /**
     * Existence checker for overrideTimeDelay
     * 
     * @see JoiningFeature.Fields#overrideTimeDelay
     */
    public boolean hasOverrideTimeDelay() {
        if (_overrideTimeDelayField!= null) {
            return true;
        }
        return super._map.containsKey("overrideTimeDelay");
    }

    /**
     * Remover for overrideTimeDelay
     * 
     * @see JoiningFeature.Fields#overrideTimeDelay
     */
    public void removeOverrideTimeDelay() {
        super._map.remove("overrideTimeDelay");
    }

    /**
     * Getter for overrideTimeDelay
     * 
     * @see JoiningFeature.Fields#overrideTimeDelay
     */
    public TimeOffset getOverrideTimeDelay(GetMode mode) {
        return getOverrideTimeDelay();
    }

    /**
     * Getter for overrideTimeDelay
     * 
     * @return
     *     Optional field. Always check for null.
     * @see JoiningFeature.Fields#overrideTimeDelay
     */
    @Nullable
    public TimeOffset getOverrideTimeDelay() {
        if (_overrideTimeDelayField!= null) {
            return _overrideTimeDelayField;
        } else {
            Object __rawValue = super._map.get("overrideTimeDelay");
            _overrideTimeDelayField = ((__rawValue == null)?null:new TimeOffset(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _overrideTimeDelayField;
        }
    }

    /**
     * Setter for overrideTimeDelay
     * 
     * @see JoiningFeature.Fields#overrideTimeDelay
     */
    public JoiningFeature setOverrideTimeDelay(TimeOffset value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setOverrideTimeDelay(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeOverrideTimeDelay();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "overrideTimeDelay", value.data());
                    _overrideTimeDelayField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "overrideTimeDelay", value.data());
                    _overrideTimeDelayField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for overrideTimeDelay
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see JoiningFeature.Fields#overrideTimeDelay
     */
    public JoiningFeature setOverrideTimeDelay(
        @Nonnull
        TimeOffset value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field overrideTimeDelay of com.linkedin.feathr.config.join.JoiningFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "overrideTimeDelay", value.data());
            _overrideTimeDelayField = value;
        }
        return this;
    }

    @Override
    public JoiningFeature clone()
        throws CloneNotSupportedException
    {
        JoiningFeature __clone = ((JoiningFeature) super.clone());
        __clone.__changeListener = new JoiningFeature.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public JoiningFeature copy()
        throws CloneNotSupportedException
    {
        JoiningFeature __copy = ((JoiningFeature) super.copy());
        __copy._overrideTimeDelayField = null;
        __copy._featureAliasField = null;
        __copy._dateRangeField = null;
        __copy._keysField = null;
        __copy._frameFeatureNameField = null;
        __copy.__changeListener = new JoiningFeature.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final JoiningFeature __objectRef;

        private ChangeListener(JoiningFeature reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "overrideTimeDelay":
                    __objectRef._overrideTimeDelayField = null;
                    break;
                case "featureAlias":
                    __objectRef._featureAliasField = null;
                    break;
                case "dateRange":
                    __objectRef._dateRangeField = null;
                    break;
                case "keys":
                    __objectRef._keysField = null;
                    break;
                case "frameFeatureName":
                    __objectRef._frameFeatureNameField = null;
                    break;
            }
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/JoiningFeature.pdl.")
    public static class DateRange
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[absoluteDateRange:{namespace com.linkedin.feathr.config.join/**The absolute date range with start and end date being required fields.\nIt accepts a start date and an end date which should be specifiied using the [[Date.pdl]] class.\nabsoluteDateRange: {\n   startDate: Date(day=1, month=1, year=2020)\n   endDate: Date(day=3, month=1, year=2020)\n }\n In this case, the endDate > startDate.*/record AbsoluteDateRange{/**start date of the date range, with the start date included in the range.*/startDate:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}/**end date of the date range, with the end date included in the range.*/endDate:Date}}relativeDateRange:{namespace com.linkedin.feathr.config.join/**The date range represented relative to the current date. It uses the current system date as the reference and can be used to\nexpress a range of dates with respect to the current date.\nExample, - If current date is 01/01/2020, window is 3, and offset 1 (unit is number of days)\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nIf dateOffset is not specified, it defaults to 0.\nrelativeDateRange: RelativeDateRange(numDays=2, dateOffset=1)\nrelativeDateRange: RelativeDateRange(numDays=5)*/record RelativeDateRange{/**Represents a length of time.\nnumDays is the window from the reference date to look back to obtain a dateRange.\nFor example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020\ntill 11/05/2020.*/@validate.positive={}numDays:long/**Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date\nwill be 4 days ago from today.*/dateOffset:long=0}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.config.join.AbsoluteDateRange _absoluteDateRangeMember = null;
        private com.linkedin.feathr.config.join.RelativeDateRange _relativeDateRangeMember = null;
        private JoiningFeature.DateRange.ChangeListener __changeListener = new JoiningFeature.DateRange.ChangeListener(this);
        private final static DataSchema MEMBER_AbsoluteDateRange = SCHEMA.getTypeByMemberKey("absoluteDateRange");
        private final static DataSchema MEMBER_RelativeDateRange = SCHEMA.getTypeByMemberKey("relativeDateRange");

        public DateRange() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public DateRange(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static JoiningFeature.DateRange createWithAbsoluteDateRange(com.linkedin.feathr.config.join.AbsoluteDateRange value) {
            JoiningFeature.DateRange newUnion = new JoiningFeature.DateRange();
            newUnion.setAbsoluteDateRange(value);
            return newUnion;
        }

        public boolean isAbsoluteDateRange() {
            return memberIs("absoluteDateRange");
        }

        public com.linkedin.feathr.config.join.AbsoluteDateRange getAbsoluteDateRange() {
            checkNotNull();
            if (_absoluteDateRangeMember!= null) {
                return _absoluteDateRangeMember;
            }
            Object __rawValue = super._map.get("absoluteDateRange");
            _absoluteDateRangeMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.AbsoluteDateRange(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _absoluteDateRangeMember;
        }

        public void setAbsoluteDateRange(com.linkedin.feathr.config.join.AbsoluteDateRange value) {
            checkNotNull();
            super._map.clear();
            _absoluteDateRangeMember = value;
            CheckedUtil.putWithoutChecking(super._map, "absoluteDateRange", value.data());
        }

        public static JoiningFeature.DateRange createWithRelativeDateRange(com.linkedin.feathr.config.join.RelativeDateRange value) {
            JoiningFeature.DateRange newUnion = new JoiningFeature.DateRange();
            newUnion.setRelativeDateRange(value);
            return newUnion;
        }

        public boolean isRelativeDateRange() {
            return memberIs("relativeDateRange");
        }

        public com.linkedin.feathr.config.join.RelativeDateRange getRelativeDateRange() {
            checkNotNull();
            if (_relativeDateRangeMember!= null) {
                return _relativeDateRangeMember;
            }
            Object __rawValue = super._map.get("relativeDateRange");
            _relativeDateRangeMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.RelativeDateRange(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _relativeDateRangeMember;
        }

        public void setRelativeDateRange(com.linkedin.feathr.config.join.RelativeDateRange value) {
            checkNotNull();
            super._map.clear();
            _relativeDateRangeMember = value;
            CheckedUtil.putWithoutChecking(super._map, "relativeDateRange", value.data());
        }

        public static JoiningFeature.DateRange.ProjectionMask createMask() {
            return new JoiningFeature.DateRange.ProjectionMask();
        }

        @Override
        public JoiningFeature.DateRange clone()
            throws CloneNotSupportedException
        {
            JoiningFeature.DateRange __clone = ((JoiningFeature.DateRange) super.clone());
            __clone.__changeListener = new JoiningFeature.DateRange.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public JoiningFeature.DateRange copy()
            throws CloneNotSupportedException
        {
            JoiningFeature.DateRange __copy = ((JoiningFeature.DateRange) super.copy());
            __copy._absoluteDateRangeMember = null;
            __copy._relativeDateRangeMember = null;
            __copy.__changeListener = new JoiningFeature.DateRange.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final JoiningFeature.DateRange __objectRef;

            private ChangeListener(JoiningFeature.DateRange reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "absoluteDateRange":
                        __objectRef._absoluteDateRangeMember = null;
                        break;
                    case "relativeDateRange":
                        __objectRef._relativeDateRangeMember = null;
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

            public com.linkedin.feathr.config.join.AbsoluteDateRange.Fields AbsoluteDateRange() {
                return new com.linkedin.feathr.config.join.AbsoluteDateRange.Fields(getPathComponents(), "absoluteDateRange");
            }

            public com.linkedin.feathr.config.join.RelativeDateRange.Fields RelativeDateRange() {
                return new com.linkedin.feathr.config.join.RelativeDateRange.Fields(getPathComponents(), "relativeDateRange");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.config.join.AbsoluteDateRange.ProjectionMask _AbsoluteDateRangeMask;
            private com.linkedin.feathr.config.join.RelativeDateRange.ProjectionMask _RelativeDateRangeMask;

            ProjectionMask() {
                super(3);
            }

            public JoiningFeature.DateRange.ProjectionMask withAbsoluteDateRange(Function<com.linkedin.feathr.config.join.AbsoluteDateRange.ProjectionMask, com.linkedin.feathr.config.join.AbsoluteDateRange.ProjectionMask> nestedMask) {
                _AbsoluteDateRangeMask = nestedMask.apply(((_AbsoluteDateRangeMask == null)?com.linkedin.feathr.config.join.AbsoluteDateRange.createMask():_AbsoluteDateRangeMask));
                getDataMap().put("absoluteDateRange", _AbsoluteDateRangeMask.getDataMap());
                return this;
            }

            public JoiningFeature.DateRange.ProjectionMask withRelativeDateRange(Function<com.linkedin.feathr.config.join.RelativeDateRange.ProjectionMask, com.linkedin.feathr.config.join.RelativeDateRange.ProjectionMask> nestedMask) {
                _RelativeDateRangeMask = nestedMask.apply(((_RelativeDateRangeMask == null)?com.linkedin.feathr.config.join.RelativeDateRange.createMask():_RelativeDateRangeMask));
                getDataMap().put("relativeDateRange", _RelativeDateRangeMask.getDataMap());
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
         * Keys to join input with feature source, the field name of the key in the input featuized dataset.
         * 
         */
        public PathSpec keys() {
            return new PathSpec(getPathComponents(), "keys");
        }

        /**
         * Keys to join input with feature source, the field name of the key in the input featuized dataset.
         * 
         */
        public PathSpec keys(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "keys");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Feature name as defined in Frame's feature definition configuration.
         * See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Feature+Definition+User+Reference for details.
         * 
         * Currently the column in the output FDS that holds this feature will have the same name as feature name.
         * If multiple joined features have the same name and no alias is defined for them, Frame will prepend the keys to the feature name.
         * 
         * In the future, if "featureAlias" is not set, the column in the output FDS that holds this feature will have the same name as feature name.
         * If multiple joined features have the same name and no alias is defined for them, the join operation will fail
         * (to avoid produciing two columns in the output FDS with the same name).
         * TODO (PROML-10038): support the use of feature alias and update the comment when we have aliases
         * 
         */
        public PathSpec frameFeatureName() {
            return new PathSpec(getPathComponents(), "frameFeatureName");
        }

        /**
         * The development of this is in progress. This is not in use for now.
         * 
         * The name to be used for the column in the output FDS that contains the values from this joined feature.
         * If not set, the name of the feature (frameFeatureName) will be used for the output column.
         * For example, if the user request joining a feature named "careers_job_listTime" and provides no alias,
         * the output FDS will contain a column called "careers_job_listTime". However, if the user sets "featureAlias" to "list_time",
         * the column will be named "list_time".
         * 
         * feature alias can be useful for in a few cases:
         *  - If the user prefers to use a name different than the frame name in their model,
         * they can use an alias to control the name of the column in the output FDS.
         *  - Sometimes, the training datas needs to have two features that are from the same frame feature.
         * For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B
         * (viewee) and we want to use the skills of both viewer and viewee as features, we need to join frame feature
         * "member_skills" of member A with frame feature "member_skills" of member B. That is, the two features are the same
         * feature but for different entiity ids). The default behavior of join is to name the output column name using the frame
         * feature name, but in a case like the above case, that would result in two columns with the same name,
         * which is not valid for FDS. In these cases, the user has to provide an alias for at least one of these joined features.
         * For example, the user can use featureAliases such as "viewer_skills" and "viewee_skills".
         * In these cases, featureAliases becomes mandatory.
         * TODO (PROML-10038): support the use of feature alias.
         * 
         */
        public PathSpec featureAlias() {
            return new PathSpec(getPathComponents(), "featureAlias");
        }

        /**
         * dateRange is used in Time-based joins, which refers to the situation when one or multiple days of input data needs
         * to be used for training.
         * (https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-timebasedobservation).
         * One of the common use cases where this is used, is in training with some time-insensitive features, or
         * training pipeline that always use the full day data, one day before running (since there is only partial data for today).
         * The time for the input featurized dataset can be set using this field.
         * Hourly data is not allowed in this case.
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
         * P.S - This is different from the timeRange used in settings as the settings startTime is applicable for the entire input data,
         * while this a feature level setting. Also, we do not support hourly time here.
         * 
         */
        public com.linkedin.feathr.config.join.JoiningFeature.DateRange.Fields dateRange() {
            return new com.linkedin.feathr.config.join.JoiningFeature.DateRange.Fields(getPathComponents(), "dateRange");
        }

        /**
         * The override time delay parameter which will override the global simulate time delay specified in the settings section for
         * the particular feature.
         * This parameter is only applicable when the simulate time delay is set in the settings section
         * For example, let us say the global simulate delay was 5d, and the overrideTimeDelay is set to 3d.
         * Then, for this specificc feature, a simulate delay of 3d will be applied.
         * 
         */
        public com.linkedin.feathr.config.join.TimeOffset.Fields overrideTimeDelay() {
            return new com.linkedin.feathr.config.join.TimeOffset.Fields(getPathComponents(), "overrideTimeDelay");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.JoiningFeature.DateRange.ProjectionMask _dateRangeMask;
        private com.linkedin.feathr.config.join.TimeOffset.ProjectionMask _overrideTimeDelayMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Keys to join input with feature source, the field name of the key in the input featuized dataset.
         * 
         */
        public JoiningFeature.ProjectionMask withKeys() {
            getDataMap().put("keys", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Keys to join input with feature source, the field name of the key in the input featuized dataset.
         * 
         */
        public JoiningFeature.ProjectionMask withKeys(Integer start, Integer count) {
            getDataMap().put("keys", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("keys").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keys").put("$count", count);
            }
            return this;
        }

        /**
         * Feature name as defined in Frame's feature definition configuration.
         * See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Feature+Definition+User+Reference for details.
         * 
         * Currently the column in the output FDS that holds this feature will have the same name as feature name.
         * If multiple joined features have the same name and no alias is defined for them, Frame will prepend the keys to the feature name.
         * 
         * In the future, if "featureAlias" is not set, the column in the output FDS that holds this feature will have the same name as feature name.
         * If multiple joined features have the same name and no alias is defined for them, the join operation will fail
         * (to avoid produciing two columns in the output FDS with the same name).
         * TODO (PROML-10038): support the use of feature alias and update the comment when we have aliases
         * 
         */
        public JoiningFeature.ProjectionMask withFrameFeatureName() {
            getDataMap().put("frameFeatureName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The development of this is in progress. This is not in use for now.
         * 
         * The name to be used for the column in the output FDS that contains the values from this joined feature.
         * If not set, the name of the feature (frameFeatureName) will be used for the output column.
         * For example, if the user request joining a feature named "careers_job_listTime" and provides no alias,
         * the output FDS will contain a column called "careers_job_listTime". However, if the user sets "featureAlias" to "list_time",
         * the column will be named "list_time".
         * 
         * feature alias can be useful for in a few cases:
         *  - If the user prefers to use a name different than the frame name in their model,
         * they can use an alias to control the name of the column in the output FDS.
         *  - Sometimes, the training datas needs to have two features that are from the same frame feature.
         * For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B
         * (viewee) and we want to use the skills of both viewer and viewee as features, we need to join frame feature
         * "member_skills" of member A with frame feature "member_skills" of member B. That is, the two features are the same
         * feature but for different entiity ids). The default behavior of join is to name the output column name using the frame
         * feature name, but in a case like the above case, that would result in two columns with the same name,
         * which is not valid for FDS. In these cases, the user has to provide an alias for at least one of these joined features.
         * For example, the user can use featureAliases such as "viewer_skills" and "viewee_skills".
         * In these cases, featureAliases becomes mandatory.
         * TODO (PROML-10038): support the use of feature alias.
         * 
         */
        public JoiningFeature.ProjectionMask withFeatureAlias() {
            getDataMap().put("featureAlias", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * dateRange is used in Time-based joins, which refers to the situation when one or multiple days of input data needs
         * to be used for training.
         * (https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-timebasedobservation).
         * One of the common use cases where this is used, is in training with some time-insensitive features, or
         * training pipeline that always use the full day data, one day before running (since there is only partial data for today).
         * The time for the input featurized dataset can be set using this field.
         * Hourly data is not allowed in this case.
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
         * P.S - This is different from the timeRange used in settings as the settings startTime is applicable for the entire input data,
         * while this a feature level setting. Also, we do not support hourly time here.
         * 
         */
        public JoiningFeature.ProjectionMask withDateRange(Function<com.linkedin.feathr.config.join.JoiningFeature.DateRange.ProjectionMask, com.linkedin.feathr.config.join.JoiningFeature.DateRange.ProjectionMask> nestedMask) {
            _dateRangeMask = nestedMask.apply(((_dateRangeMask == null)?JoiningFeature.DateRange.createMask():_dateRangeMask));
            getDataMap().put("dateRange", _dateRangeMask.getDataMap());
            return this;
        }

        /**
         * dateRange is used in Time-based joins, which refers to the situation when one or multiple days of input data needs
         * to be used for training.
         * (https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-timebasedobservation).
         * One of the common use cases where this is used, is in training with some time-insensitive features, or
         * training pipeline that always use the full day data, one day before running (since there is only partial data for today).
         * The time for the input featurized dataset can be set using this field.
         * Hourly data is not allowed in this case.
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
         * P.S - This is different from the timeRange used in settings as the settings startTime is applicable for the entire input data,
         * while this a feature level setting. Also, we do not support hourly time here.
         * 
         */
        public JoiningFeature.ProjectionMask withDateRange() {
            _dateRangeMask = null;
            getDataMap().put("dateRange", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The override time delay parameter which will override the global simulate time delay specified in the settings section for
         * the particular feature.
         * This parameter is only applicable when the simulate time delay is set in the settings section
         * For example, let us say the global simulate delay was 5d, and the overrideTimeDelay is set to 3d.
         * Then, for this specificc feature, a simulate delay of 3d will be applied.
         * 
         */
        public JoiningFeature.ProjectionMask withOverrideTimeDelay(Function<com.linkedin.feathr.config.join.TimeOffset.ProjectionMask, com.linkedin.feathr.config.join.TimeOffset.ProjectionMask> nestedMask) {
            _overrideTimeDelayMask = nestedMask.apply(((_overrideTimeDelayMask == null)?TimeOffset.createMask():_overrideTimeDelayMask));
            getDataMap().put("overrideTimeDelay", _overrideTimeDelayMask.getDataMap());
            return this;
        }

        /**
         * The override time delay parameter which will override the global simulate time delay specified in the settings section for
         * the particular feature.
         * This parameter is only applicable when the simulate time delay is set in the settings section
         * For example, let us say the global simulate delay was 5d, and the overrideTimeDelay is set to 3d.
         * Then, for this specificc feature, a simulate delay of 3d will be applied.
         * 
         */
        public JoiningFeature.ProjectionMask withOverrideTimeDelay() {
            _overrideTimeDelayMask = null;
            getDataMap().put("overrideTimeDelay", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
