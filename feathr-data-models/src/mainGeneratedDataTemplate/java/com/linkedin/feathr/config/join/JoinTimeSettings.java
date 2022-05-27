
package com.linkedin.feathr.config.join;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.HasTyperefInfo;
import com.linkedin.data.template.TyperefInfo;
import com.linkedin.data.template.UnionTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/JoinTimeSettings.pdl.")
public class JoinTimeSettings
    extends UnionTemplate
    implements HasTyperefInfo
{

    private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[useLatestJoinTimeSettings:{namespace com.linkedin.feathr.config.join/**Settings needed when the input data is to be joined with the latest available feature data.\njoinTimeSettings: {\n   useLatestFeatureData: true\n}*/record UseLatestJoinTimeSettings{/**Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.\nWhen useLatestFeatureData is set, there should be no other time-based parameters.*/useLatestFeatureData:boolean=true}}timestampColJoinTimeSettings:{namespace com.linkedin.feathr.config.join/**Settings needed when the input data has a timestamp which should be used for the join.\njoinTimeSettings: {\n   timestampColumn: {\n      def: timestamp\n      format: yyyy/MM/dd\n   }\n   simulateTimeDelay: 1d\n}*/record TimestampColJoinTimeSettings{/**The timestamp column name and timeformat which should be used for joining with the feature data.\nRefer to [[TimestampColumn]].\nExample, TimestampColumn: {\n           def: timestamp\n           format: yyyy/MM/dd\n         }*/timestampColumn:/**Timestamp column of the input featureiized dataset, which is to be used for the join.\ntimestampColumn: {\n   def: timestamp\n   format: yyyyMMdd\n }*/record TimestampColumn{/**The definiton of the timestamp column, which can be a sql expression involving the timestamp column\nor just the column name\nExample:- definition: timestamp, timestamp + 10000000.*/definition:union[columnName:string,sparkSqlExpression:/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}]/**Format of the timestamp column. Must confer to java's timestampFormatter or can be\nepoch or epoch_millis.\nExample:- epoch, epoch_millis, yyyy/MM/dd*/format:/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string}/**An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted\nfrom the input data timestamp while joining with the feature data.\nWe do support negative time delays.*/simulateTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\nbe any time in the past also, we do allow a positive or negative offset length.\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}}}]", SchemaFormatType.PDL));
    private com.linkedin.feathr.config.join.UseLatestJoinTimeSettings _useLatestJoinTimeSettingsMember = null;
    private com.linkedin.feathr.config.join.TimestampColJoinTimeSettings _timestampColJoinTimeSettingsMember = null;
    private JoinTimeSettings.ChangeListener __changeListener = new JoinTimeSettings.ChangeListener(this);
    private final static DataSchema MEMBER_UseLatestJoinTimeSettings = SCHEMA.getTypeByMemberKey("useLatestJoinTimeSettings");
    private final static DataSchema MEMBER_TimestampColJoinTimeSettings = SCHEMA.getTypeByMemberKey("timestampColJoinTimeSettings");
    private final static TyperefInfo TYPEREFINFO = new JoinTimeSettings.UnionTyperefInfo();

    public JoinTimeSettings() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public JoinTimeSettings(Object data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UnionDataSchema dataSchema() {
        return SCHEMA;
    }

    public static JoinTimeSettings createWithUseLatestJoinTimeSettings(com.linkedin.feathr.config.join.UseLatestJoinTimeSettings value) {
        JoinTimeSettings newUnion = new JoinTimeSettings();
        newUnion.setUseLatestJoinTimeSettings(value);
        return newUnion;
    }

    public boolean isUseLatestJoinTimeSettings() {
        return memberIs("useLatestJoinTimeSettings");
    }

    public com.linkedin.feathr.config.join.UseLatestJoinTimeSettings getUseLatestJoinTimeSettings() {
        checkNotNull();
        if (_useLatestJoinTimeSettingsMember!= null) {
            return _useLatestJoinTimeSettingsMember;
        }
        Object __rawValue = super._map.get("useLatestJoinTimeSettings");
        _useLatestJoinTimeSettingsMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.UseLatestJoinTimeSettings(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _useLatestJoinTimeSettingsMember;
    }

    public void setUseLatestJoinTimeSettings(com.linkedin.feathr.config.join.UseLatestJoinTimeSettings value) {
        checkNotNull();
        super._map.clear();
        _useLatestJoinTimeSettingsMember = value;
        CheckedUtil.putWithoutChecking(super._map, "useLatestJoinTimeSettings", value.data());
    }

    public static JoinTimeSettings createWithTimestampColJoinTimeSettings(com.linkedin.feathr.config.join.TimestampColJoinTimeSettings value) {
        JoinTimeSettings newUnion = new JoinTimeSettings();
        newUnion.setTimestampColJoinTimeSettings(value);
        return newUnion;
    }

    public boolean isTimestampColJoinTimeSettings() {
        return memberIs("timestampColJoinTimeSettings");
    }

    public com.linkedin.feathr.config.join.TimestampColJoinTimeSettings getTimestampColJoinTimeSettings() {
        checkNotNull();
        if (_timestampColJoinTimeSettingsMember!= null) {
            return _timestampColJoinTimeSettingsMember;
        }
        Object __rawValue = super._map.get("timestampColJoinTimeSettings");
        _timestampColJoinTimeSettingsMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.TimestampColJoinTimeSettings(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _timestampColJoinTimeSettingsMember;
    }

    public void setTimestampColJoinTimeSettings(com.linkedin.feathr.config.join.TimestampColJoinTimeSettings value) {
        checkNotNull();
        super._map.clear();
        _timestampColJoinTimeSettingsMember = value;
        CheckedUtil.putWithoutChecking(super._map, "timestampColJoinTimeSettings", value.data());
    }

    public static JoinTimeSettings.ProjectionMask createMask() {
        return new JoinTimeSettings.ProjectionMask();
    }

    @Override
    public JoinTimeSettings clone()
        throws CloneNotSupportedException
    {
        JoinTimeSettings __clone = ((JoinTimeSettings) super.clone());
        __clone.__changeListener = new JoinTimeSettings.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public JoinTimeSettings copy()
        throws CloneNotSupportedException
    {
        JoinTimeSettings __copy = ((JoinTimeSettings) super.copy());
        __copy._useLatestJoinTimeSettingsMember = null;
        __copy._timestampColJoinTimeSettingsMember = null;
        __copy.__changeListener = new JoinTimeSettings.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    public TyperefInfo typerefInfo() {
        return TYPEREFINFO;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final JoinTimeSettings __objectRef;

        private ChangeListener(JoinTimeSettings reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "useLatestJoinTimeSettings":
                    __objectRef._useLatestJoinTimeSettingsMember = null;
                    break;
                case "timestampColJoinTimeSettings":
                    __objectRef._timestampColJoinTimeSettingsMember = null;
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

        public com.linkedin.feathr.config.join.UseLatestJoinTimeSettings.Fields UseLatestJoinTimeSettings() {
            return new com.linkedin.feathr.config.join.UseLatestJoinTimeSettings.Fields(getPathComponents(), "useLatestJoinTimeSettings");
        }

        public com.linkedin.feathr.config.join.TimestampColJoinTimeSettings.Fields TimestampColJoinTimeSettings() {
            return new com.linkedin.feathr.config.join.TimestampColJoinTimeSettings.Fields(getPathComponents(), "timestampColJoinTimeSettings");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.UseLatestJoinTimeSettings.ProjectionMask _UseLatestJoinTimeSettingsMask;
        private com.linkedin.feathr.config.join.TimestampColJoinTimeSettings.ProjectionMask _TimestampColJoinTimeSettingsMask;

        ProjectionMask() {
            super(3);
        }

        public JoinTimeSettings.ProjectionMask withUseLatestJoinTimeSettings(Function<com.linkedin.feathr.config.join.UseLatestJoinTimeSettings.ProjectionMask, com.linkedin.feathr.config.join.UseLatestJoinTimeSettings.ProjectionMask> nestedMask) {
            _UseLatestJoinTimeSettingsMask = nestedMask.apply(((_UseLatestJoinTimeSettingsMask == null)?com.linkedin.feathr.config.join.UseLatestJoinTimeSettings.createMask():_UseLatestJoinTimeSettingsMask));
            getDataMap().put("useLatestJoinTimeSettings", _UseLatestJoinTimeSettingsMask.getDataMap());
            return this;
        }

        public JoinTimeSettings.ProjectionMask withTimestampColJoinTimeSettings(Function<com.linkedin.feathr.config.join.TimestampColJoinTimeSettings.ProjectionMask, com.linkedin.feathr.config.join.TimestampColJoinTimeSettings.ProjectionMask> nestedMask) {
            _TimestampColJoinTimeSettingsMask = nestedMask.apply(((_TimestampColJoinTimeSettingsMask == null)?com.linkedin.feathr.config.join.TimestampColJoinTimeSettings.createMask():_TimestampColJoinTimeSettingsMask));
            getDataMap().put("timestampColJoinTimeSettings", _TimestampColJoinTimeSettingsMask.getDataMap());
            return this;
        }

    }


    /**
     * JoinTimeSettings contains all the parameters required to join the time sensitive input data with the feature data.
     * The input data can be time sensitive in two ways:-
     * a. Have a timestamp column
     * b. Always join with the latest available feature data. In this case, we do not require a timestamp column.
     * c. The file path is time-partition and the path time is used for the join
     * (Todo PROML-11936 - Add useTimePartitionPattern field in this section)
     * In this section, the user needs to let Frame know which of the above properties is to be used for the join.
     * 
     */
    private final static class UnionTyperefInfo
        extends TyperefInfo
    {

        private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**JoinTimeSettings contains all the parameters required to join the time sensitive input data with the feature data.\nThe input data can be time sensitive in two ways:-\na. Have a timestamp column\nb. Always join with the latest available feature data. In this case, we do not require a timestamp column.\nc. The file path is time-partition and the path time is used for the join\n(Todo PROML-11936 - Add useTimePartitionPattern field in this section)\nIn this section, the user needs to let Frame know which of the above properties is to be used for the join.*/typeref JoinTimeSettings=union[useLatestJoinTimeSettings:/**Settings needed when the input data is to be joined with the latest available feature data.\njoinTimeSettings: {\n   useLatestFeatureData: true\n}*/record UseLatestJoinTimeSettings{/**Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.\nWhen useLatestFeatureData is set, there should be no other time-based parameters.*/useLatestFeatureData:boolean=true}timestampColJoinTimeSettings:/**Settings needed when the input data has a timestamp which should be used for the join.\njoinTimeSettings: {\n   timestampColumn: {\n      def: timestamp\n      format: yyyy/MM/dd\n   }\n   simulateTimeDelay: 1d\n}*/record TimestampColJoinTimeSettings{/**The timestamp column name and timeformat which should be used for joining with the feature data.\nRefer to [[TimestampColumn]].\nExample, TimestampColumn: {\n           def: timestamp\n           format: yyyy/MM/dd\n         }*/timestampColumn:/**Timestamp column of the input featureiized dataset, which is to be used for the join.\ntimestampColumn: {\n   def: timestamp\n   format: yyyyMMdd\n }*/record TimestampColumn{/**The definiton of the timestamp column, which can be a sql expression involving the timestamp column\nor just the column name\nExample:- definition: timestamp, timestamp + 10000000.*/definition:union[columnName:string,sparkSqlExpression:/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}]/**Format of the timestamp column. Must confer to java's timestampFormatter or can be\nepoch or epoch_millis.\nExample:- epoch, epoch_millis, yyyy/MM/dd*/format:/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string}/**An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted\nfrom the input data timestamp while joining with the feature data.\nWe do support negative time delays.*/simulateTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\nbe any time in the past also, we do allow a positive or negative offset length.\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}}]", SchemaFormatType.PDL));

        public UnionTyperefInfo() {
            super(SCHEMA);
        }

        public static TyperefDataSchema dataSchema() {
            return SCHEMA;
        }

    }

}
