
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.MapDataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.WrappingMapTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/RestliDataSource.pdl.")
public class RequestParameterValueMap
    extends WrappingMapTemplate<RequestParameterValue>
{

    private final static MapDataSchema SCHEMA = ((MapDataSchema) DataTemplateUtil.parseSchema("map[string,union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**Represents a Json string.*/typeref JsonString=string}]]", SchemaFormatType.PDL));

    public RequestParameterValueMap() {
        this(new DataMap());
    }

    public RequestParameterValueMap(int initialCapacity) {
        this(new DataMap(initialCapacity));
    }

    public RequestParameterValueMap(int initialCapacity, float loadFactor) {
        this(new DataMap(initialCapacity, loadFactor));
    }

    public RequestParameterValueMap(Map<String, RequestParameterValue> m) {
        this(newDataMapOfSize(m.size()));
        putAll(m);
    }

    public RequestParameterValueMap(DataMap data) {
        super(data, SCHEMA, RequestParameterValue.class);
    }

    public static MapDataSchema dataSchema() {
        return SCHEMA;
    }

    public static RequestParameterValueMap.ProjectionMask createMask() {
        return new RequestParameterValueMap.ProjectionMask();
    }

    @Override
    public RequestParameterValueMap clone()
        throws CloneNotSupportedException
    {
        RequestParameterValueMap __clone = ((RequestParameterValueMap) super.clone());
        return __clone;
    }

    @Override
    public RequestParameterValueMap copy()
        throws CloneNotSupportedException
    {
        RequestParameterValueMap __copy = ((RequestParameterValueMap) super.copy());
        return __copy;
    }

    @Override
    protected RequestParameterValue coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        if (object == null) {
            return null;
        }
        return ((object == null)?null:new RequestParameterValue(object));
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

        public com.linkedin.feathr.featureDataModel.RequestParameterValue.Fields values() {
            return new com.linkedin.feathr.featureDataModel.RequestParameterValue.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.RequestParameterValue.ProjectionMask _valuesMask;

        ProjectionMask() {
            super(4);
        }

        public RequestParameterValueMap.ProjectionMask withValues(Function<com.linkedin.feathr.featureDataModel.RequestParameterValue.ProjectionMask, com.linkedin.feathr.featureDataModel.RequestParameterValue.ProjectionMask> nestedMask) {
            _valuesMask = nestedMask.apply(((_valuesMask == null)?RequestParameterValue.createMask():_valuesMask));
            getDataMap().put("$*", _valuesMask.getDataMap());
            return this;
        }

    }

}
