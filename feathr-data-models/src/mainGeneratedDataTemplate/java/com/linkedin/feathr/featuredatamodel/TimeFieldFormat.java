
package com.linkedin.feathr.featureDataModel;

import java.util.List;
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/TimeFieldFormat.pdl.")
public class TimeFieldFormat
    extends UnionTemplate
    implements HasTyperefInfo
{

    private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}}{namespace com.linkedin.feathr.featureDataModel/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string}]", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.TimestampGranularity _timestampGranularityMember = null;
    private String _dateTimeFormatMember = null;
    private TimeFieldFormat.ChangeListener __changeListener = new TimeFieldFormat.ChangeListener(this);
    private final static DataSchema MEMBER_TimestampGranularity = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.TimestampGranularity");
    private final static DataSchema MEMBER_DateTimeFormat = SCHEMA.getTypeByMemberKey("string");
    private final static TyperefInfo TYPEREFINFO = new TimeFieldFormat.UnionTyperefInfo();

    public TimeFieldFormat() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public TimeFieldFormat(Object data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UnionDataSchema dataSchema() {
        return SCHEMA;
    }

    public static TimeFieldFormat create(com.linkedin.feathr.featureDataModel.TimestampGranularity value) {
        TimeFieldFormat newUnion = new TimeFieldFormat();
        newUnion.setTimestampGranularity(value);
        return newUnion;
    }

    public boolean isTimestampGranularity() {
        return memberIs("com.linkedin.feathr.featureDataModel.TimestampGranularity");
    }

    public com.linkedin.feathr.featureDataModel.TimestampGranularity getTimestampGranularity() {
        checkNotNull();
        if (_timestampGranularityMember!= null) {
            return _timestampGranularityMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.TimestampGranularity");
        _timestampGranularityMember = DataTemplateUtil.coerceEnumOutput(__rawValue, com.linkedin.feathr.featureDataModel.TimestampGranularity.class, com.linkedin.feathr.featureDataModel.TimestampGranularity.$UNKNOWN);
        return _timestampGranularityMember;
    }

    public void setTimestampGranularity(com.linkedin.feathr.featureDataModel.TimestampGranularity value) {
        checkNotNull();
        super._map.clear();
        _timestampGranularityMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.TimestampGranularity", value.name());
    }

    public static TimeFieldFormat create(String value) {
        TimeFieldFormat newUnion = new TimeFieldFormat();
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

    public static TimeFieldFormat.ProjectionMask createMask() {
        return new TimeFieldFormat.ProjectionMask();
    }

    @Override
    public TimeFieldFormat clone()
        throws CloneNotSupportedException
    {
        TimeFieldFormat __clone = ((TimeFieldFormat) super.clone());
        __clone.__changeListener = new TimeFieldFormat.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TimeFieldFormat copy()
        throws CloneNotSupportedException
    {
        TimeFieldFormat __copy = ((TimeFieldFormat) super.copy());
        __copy._dateTimeFormatMember = null;
        __copy._timestampGranularityMember = null;
        __copy.__changeListener = new TimeFieldFormat.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    public TyperefInfo typerefInfo() {
        return TYPEREFINFO;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TimeFieldFormat __objectRef;

        private ChangeListener(TimeFieldFormat reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "string":
                    __objectRef._dateTimeFormatMember = null;
                    break;
                case "com.linkedin.feathr.featureDataModel.TimestampGranularity":
                    __objectRef._timestampGranularityMember = null;
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

        public PathSpec TimestampGranularity() {
            return new PathSpec(getPathComponents(), "com.linkedin.feathr.featureDataModel.TimestampGranularity");
        }

        public PathSpec DateTimeFormat() {
            return new PathSpec(getPathComponents(), "string");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        public TimeFieldFormat.ProjectionMask withTimestampGranularity() {
            getDataMap().put("com.linkedin.feathr.featureDataModel.TimestampGranularity", MaskMap.POSITIVE_MASK);
            return this;
        }

        public TimeFieldFormat.ProjectionMask withDateTimeFormat() {
            getDataMap().put("string", MaskMap.POSITIVE_MASK);
            return this;
        }

    }


    /**
     * The format of the time field. It can be either the granularity of a timestamp or datetime format e.g. yyyy/MM/dd/HH/mm/ss.
     * 
     */
    private final static class UnionTyperefInfo
        extends TyperefInfo
    {

        private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**The format of the time field. It can be either the granularity of a timestamp or datetime format e.g. yyyy/MM/dd/HH/mm/ss.*/typeref TimeFieldFormat=union[/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string]", SchemaFormatType.PDL));

        public UnionTyperefInfo() {
            super(SCHEMA);
        }

        public static TyperefDataSchema dataSchema() {
            return SCHEMA;
        }

    }

}
