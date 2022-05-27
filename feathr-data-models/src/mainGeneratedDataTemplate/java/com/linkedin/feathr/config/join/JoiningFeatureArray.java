
package com.linkedin.feathr.config.join;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.WrappingArrayTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/FrameFeatureJoinConfig.pdl.")
public class JoiningFeatureArray
    extends WrappingArrayTemplate<JoiningFeature>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.config.join/**JoiningFeature is the feature section of the join config. This section consists of information pertaining to a feature\nwhich is to be joined:-\na. The join keys of the input data, with which this feature is to be joined.\nb. name of the feature\nc. optional timeRange of the input data which is to be joined with this feature.\nd. optional overrideTimeDelay if this feature needs a different simulate time delay other than the one mentioned.\n\nThis is a required section of the the join config.\nExample,\n  a. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature1\"\n       AbsoluteDateRange(startDate: Date(year=2020, month=5, day=5),\n                        endDate: Date(year=2020, month=5, day=7))\n     }\n  b. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature2\"\n       overrideTimeDelay: TimeDelay(length=1, unit=\"DAY\")\n   }\n  c. JoiningFeature{\n       keys: [\"key1\"]\n       frameFeatureName: \"feature3\"\n       RelativeDateRange(numDays: 5,\n                         offset: 3)\n  }*/record JoiningFeature{/**Keys to join input with feature source, the field name of the key in the input featuized dataset.*/keys:array[string]/**Feature name as defined in Frame's feature definition configuration.\nSee https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Feature+Definition+User+Reference for details.\n\nCurrently the column in the output FDS that holds this feature will have the same name as feature name.\nIf multiple joined features have the same name and no alias is defined for them, Frame will prepend the keys to the feature name.\n\nIn the future, if \"featureAlias\" is not set, the column in the output FDS that holds this feature will have the same name as feature name.\nIf multiple joined features have the same name and no alias is defined for them, the join operation will fail\n(to avoid produciing two columns in the output FDS with the same name).\nTODO (PROML-10038): support the use of feature alias and update the comment when we have aliases*/frameFeatureName:string/**The development of this is in progress. This is not in use for now.\n\nThe name to be used for the column in the output FDS that contains the values from this joined feature.\nIf not set, the name of the feature (frameFeatureName) will be used for the output column.\nFor example, if the user request joining a feature named \"careers_job_listTime\" and provides no alias,\nthe output FDS will contain a column called \"careers_job_listTime\". However, if the user sets \"featureAlias\" to \"list_time\",\nthe column will be named \"list_time\".\n\nfeature alias can be useful for in a few cases:\n - If the user prefers to use a name different than the frame name in their model,\nthey can use an alias to control the name of the column in the output FDS.\n - Sometimes, the training datas needs to have two features that are from the same frame feature.\nFor example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B\n(viewee) and we want to use the skills of both viewer and viewee as features, we need to join frame feature\n\"member_skills\" of member A with frame feature \"member_skills\" of member B. That is, the two features are the same\nfeature but for different entiity ids). The default behavior of join is to name the output column name using the frame\nfeature name, but in a case like the above case, that would result in two columns with the same name,\nwhich is not valid for FDS. In these cases, the user has to provide an alias for at least one of these joined features.\nFor example, the user can use featureAliases such as \"viewer_skills\" and \"viewee_skills\".\nIn these cases, featureAliases becomes mandatory.\nTODO (PROML-10038): support the use of feature alias.*/featureAlias:optional string/**dateRange is used in Time-based joins, which refers to the situation when one or multiple days of input data needs\nto be used for training.\n(https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-timebasedobservation).\nOne of the common use cases where this is used, is in training with some time-insensitive features, or\ntraining pipeline that always use the full day data, one day before running (since there is only partial data for today).\nThe time for the input featurized dataset can be set using this field.\nHourly data is not allowed in this case.\n\nFor example,\na. startDate: \"20200522\", endDate: \"20200525\" implies this feature should be joined with the input data starting from\n22nd May 2020 to 25th May, 2020 with both dates included.\nWe only support yyyyMMdd format for this. In future, if there is a request, we can\nadd support for other date time formats as well.\n\nb. numDays - 5d implies, offset - 1d, if today's date is 11/09/2020, then the input data ranging from 11/08/2020\ntill 11/04/2020 willl be joined.\n\nP.S - This is different from the timeRange used in settings as the settings startTime is applicable for the entire input data,\nwhile this a feature level setting. Also, we do not support hourly time here.*/dateRange:optional union[absoluteDateRange:/**The absolute date range with start and end date being required fields.\nIt accepts a start date and an end date which should be specifiied using the [[Date.pdl]] class.\nabsoluteDateRange: {\n   startDate: Date(day=1, month=1, year=2020)\n   endDate: Date(day=3, month=1, year=2020)\n }\n In this case, the endDate > startDate.*/record AbsoluteDateRange{/**start date of the date range, with the start date included in the range.*/startDate:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}/**end date of the date range, with the end date included in the range.*/endDate:Date}relativeDateRange:/**The date range represented relative to the current date. It uses the current system date as the reference and can be used to\nexpress a range of dates with respect to the current date.\nExample, - If current date is 01/01/2020, window is 3, and offset 1 (unit is number of days)\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nIf dateOffset is not specified, it defaults to 0.\nrelativeDateRange: RelativeDateRange(numDays=2, dateOffset=1)\nrelativeDateRange: RelativeDateRange(numDays=5)*/record RelativeDateRange{/**Represents a length of time.\nnumDays is the window from the reference date to look back to obtain a dateRange.\nFor example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020\ntill 11/05/2020.*/@validate.positive={}numDays:long/**Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date\nwill be 4 days ago from today.*/dateOffset:long=0}]/**The override time delay parameter which will override the global simulate time delay specified in the settings section for\nthe particular feature.\nThis parameter is only applicable when the simulate time delay is set in the settings section\nFor example, let us say the global simulate delay was 5d, and the overrideTimeDelay is set to 3d.\nThen, for this specificc feature, a simulate delay of 3d will be applied.*/overrideTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\nbe any time in the past also, we do allow a positive or negative offset length.\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}}}]", SchemaFormatType.PDL));

    public JoiningFeatureArray() {
        this(new DataList());
    }

    public JoiningFeatureArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public JoiningFeatureArray(Collection<JoiningFeature> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public JoiningFeatureArray(DataList data) {
        super(data, SCHEMA, JoiningFeature.class);
    }

    public JoiningFeatureArray(JoiningFeature first, JoiningFeature... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static JoiningFeatureArray.ProjectionMask createMask() {
        return new JoiningFeatureArray.ProjectionMask();
    }

    @Override
    public JoiningFeatureArray clone()
        throws CloneNotSupportedException
    {
        JoiningFeatureArray __clone = ((JoiningFeatureArray) super.clone());
        return __clone;
    }

    @Override
    public JoiningFeatureArray copy()
        throws CloneNotSupportedException
    {
        JoiningFeatureArray __copy = ((JoiningFeatureArray) super.copy());
        return __copy;
    }

    @Override
    protected JoiningFeature coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new JoiningFeature(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.config.join.JoiningFeature.Fields items() {
            return new com.linkedin.feathr.config.join.JoiningFeature.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.JoiningFeature.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public JoiningFeatureArray.ProjectionMask withItems(Function<com.linkedin.feathr.config.join.JoiningFeature.ProjectionMask, com.linkedin.feathr.config.join.JoiningFeature.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?JoiningFeature.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
