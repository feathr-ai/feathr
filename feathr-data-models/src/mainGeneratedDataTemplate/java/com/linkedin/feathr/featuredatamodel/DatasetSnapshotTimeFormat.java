
package com.linkedin.feathr.featureDataModel;

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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/DatasetSnapshotTimeFormat.pdl.")
public class DatasetSnapshotTimeFormat
    extends UnionTemplate
    implements HasTyperefInfo
{

    private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string}{namespace com.linkedin.feathr.featureDataModel/**Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of \"Unification of SWA and time-based features\" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).*/record StandardDateTimeFormat{}}]", SchemaFormatType.PDL));
    private String _dateTimeFormatMember = null;
    private com.linkedin.feathr.featureDataModel.StandardDateTimeFormat _standardDateTimeFormatMember = null;
    private DatasetSnapshotTimeFormat.ChangeListener __changeListener = new DatasetSnapshotTimeFormat.ChangeListener(this);
    private final static DataSchema MEMBER_DateTimeFormat = SCHEMA.getTypeByMemberKey("string");
    private final static DataSchema MEMBER_StandardDateTimeFormat = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.StandardDateTimeFormat");
    private final static TyperefInfo TYPEREFINFO = new DatasetSnapshotTimeFormat.UnionTyperefInfo();

    public DatasetSnapshotTimeFormat() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public DatasetSnapshotTimeFormat(Object data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UnionDataSchema dataSchema() {
        return SCHEMA;
    }

    public static DatasetSnapshotTimeFormat create(String value) {
        DatasetSnapshotTimeFormat newUnion = new DatasetSnapshotTimeFormat();
        newUnion.setDateTimeFormat(value);
        return newUnion;
    }

    public boolean isDateTimeFormat() {
        return memberIs("string");
    }

    public String getDateTimeFormat() {
        checkNotNull();
        if (_dateTimeFormatMember!= null) {
            return _dateTimeFormatMember;
        }
        Object __rawValue = super._map.get("string");
        _dateTimeFormatMember = DataTemplateUtil.coerceStringOutput(__rawValue);
        return _dateTimeFormatMember;
    }

    public void setDateTimeFormat(String value) {
        checkNotNull();
        super._map.clear();
        _dateTimeFormatMember = value;
        CheckedUtil.putWithoutChecking(super._map, "string", value);
    }

    public static DatasetSnapshotTimeFormat create(com.linkedin.feathr.featureDataModel.StandardDateTimeFormat value) {
        DatasetSnapshotTimeFormat newUnion = new DatasetSnapshotTimeFormat();
        newUnion.setStandardDateTimeFormat(value);
        return newUnion;
    }

    public boolean isStandardDateTimeFormat() {
        return memberIs("com.linkedin.feathr.featureDataModel.StandardDateTimeFormat");
    }

    public com.linkedin.feathr.featureDataModel.StandardDateTimeFormat getStandardDateTimeFormat() {
        checkNotNull();
        if (_standardDateTimeFormatMember!= null) {
            return _standardDateTimeFormatMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.StandardDateTimeFormat");
        _standardDateTimeFormatMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.StandardDateTimeFormat(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _standardDateTimeFormatMember;
    }

    public void setStandardDateTimeFormat(com.linkedin.feathr.featureDataModel.StandardDateTimeFormat value) {
        checkNotNull();
        super._map.clear();
        _standardDateTimeFormatMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.StandardDateTimeFormat", value.data());
    }

    public static DatasetSnapshotTimeFormat.ProjectionMask createMask() {
        return new DatasetSnapshotTimeFormat.ProjectionMask();
    }

    @Override
    public DatasetSnapshotTimeFormat clone()
        throws CloneNotSupportedException
    {
        DatasetSnapshotTimeFormat __clone = ((DatasetSnapshotTimeFormat) super.clone());
        __clone.__changeListener = new DatasetSnapshotTimeFormat.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public DatasetSnapshotTimeFormat copy()
        throws CloneNotSupportedException
    {
        DatasetSnapshotTimeFormat __copy = ((DatasetSnapshotTimeFormat) super.copy());
        __copy._dateTimeFormatMember = null;
        __copy._standardDateTimeFormatMember = null;
        __copy.__changeListener = new DatasetSnapshotTimeFormat.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    public TyperefInfo typerefInfo() {
        return TYPEREFINFO;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final DatasetSnapshotTimeFormat __objectRef;

        private ChangeListener(DatasetSnapshotTimeFormat reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "string":
                    __objectRef._dateTimeFormatMember = null;
                    break;
                case "com.linkedin.feathr.featureDataModel.StandardDateTimeFormat":
                    __objectRef._standardDateTimeFormatMember = null;
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

        public PathSpec DateTimeFormat() {
            return new PathSpec(getPathComponents(), "string");
        }

        public com.linkedin.feathr.featureDataModel.StandardDateTimeFormat.Fields StandardDateTimeFormat() {
            return new com.linkedin.feathr.featureDataModel.StandardDateTimeFormat.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.StandardDateTimeFormat");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.StandardDateTimeFormat.ProjectionMask _StandardDateTimeFormatMask;

        ProjectionMask() {
            super(3);
        }

        public DatasetSnapshotTimeFormat.ProjectionMask withDateTimeFormat() {
            getDataMap().put("string", MaskMap.POSITIVE_MASK);
            return this;
        }

        public DatasetSnapshotTimeFormat.ProjectionMask withStandardDateTimeFormat(Function<com.linkedin.feathr.featureDataModel.StandardDateTimeFormat.ProjectionMask, com.linkedin.feathr.featureDataModel.StandardDateTimeFormat.ProjectionMask> nestedMask) {
            _StandardDateTimeFormatMask = nestedMask.apply(((_StandardDateTimeFormatMask == null)?com.linkedin.feathr.featureDataModel.StandardDateTimeFormat.createMask():_StandardDateTimeFormatMask));
            getDataMap().put("com.linkedin.feathr.featureDataModel.StandardDateTimeFormat", _StandardDateTimeFormatMask.getDataMap());
            return this;
        }

    }


    /**
     * Represents the time format being used to parse snapshot time from a dataset's file path, for example "2020/09/30" in /hdfs/jobs/lyndarel/features/master/members/daily/2020/09/30/. The dataset snapshot time is useful when feature data is time-partitioned for time-aware join or sliding-window aggregation. An example feature is derived skills, because derived skills change often based on user activities, modeling engineers want to join the time-sensitive observation data with the closest version of the feature. Refer to Unification of SWA and time-based features RFC (https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#) for more details.
     * 
     */
    private final static class UnionTyperefInfo
        extends TyperefInfo
    {

        private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents the time format being used to parse snapshot time from a dataset's file path, for example \"2020/09/30\" in /hdfs/jobs/lyndarel/features/master/members/daily/2020/09/30/. The dataset snapshot time is useful when feature data is time-partitioned for time-aware join or sliding-window aggregation. An example feature is derived skills, because derived skills change often based on user activities, modeling engineers want to join the time-sensitive observation data with the closest version of the feature. Refer to Unification of SWA and time-based features RFC (https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#) for more details.*/typeref DatasetSnapshotTimeFormat=union[/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string/**Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of \"Unification of SWA and time-based features\" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).*/record StandardDateTimeFormat{}]", SchemaFormatType.PDL));

        public UnionTyperefInfo() {
            super(SCHEMA);
        }

        public static TyperefDataSchema dataSchema() {
            return SCHEMA;
        }

    }

}
