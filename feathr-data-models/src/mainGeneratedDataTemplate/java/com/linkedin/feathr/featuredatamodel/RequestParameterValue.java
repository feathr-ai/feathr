
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/RequestParameterValue.pdl.")
public class RequestParameterValue
    extends UnionTemplate
    implements HasTyperefInfo
{

    private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**Represents a Json string.*/typeref JsonString=string}]", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
    private String _jsonStringMember = null;
    private RequestParameterValue.ChangeListener __changeListener = new RequestParameterValue.ChangeListener(this);
    private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
    private final static DataSchema MEMBER_JsonString = SCHEMA.getTypeByMemberKey("string");
    private final static TyperefInfo TYPEREFINFO = new RequestParameterValue.UnionTyperefInfo();

    public RequestParameterValue() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public RequestParameterValue(Object data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UnionDataSchema dataSchema() {
        return SCHEMA;
    }

    public static RequestParameterValue create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
        RequestParameterValue newUnion = new RequestParameterValue();
        newUnion.setMvelExpression(value);
        return newUnion;
    }

    public boolean isMvelExpression() {
        return memberIs("com.linkedin.feathr.featureDataModel.MvelExpression");
    }

    public com.linkedin.feathr.featureDataModel.MvelExpression getMvelExpression() {
        checkNotNull();
        if (_mvelExpressionMember!= null) {
            return _mvelExpressionMember;
        }
        Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.MvelExpression");
        _mvelExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.MvelExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
        return _mvelExpressionMember;
    }

    public void setMvelExpression(com.linkedin.feathr.featureDataModel.MvelExpression value) {
        checkNotNull();
        super._map.clear();
        _mvelExpressionMember = value;
        CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.MvelExpression", value.data());
    }

    public static RequestParameterValue create(String value) {
        RequestParameterValue newUnion = new RequestParameterValue();
        newUnion.setJsonString(value);
        return newUnion;
    }

    public boolean isJsonString() {
        return memberIs("string");
    }

    public String getJsonString() {
        checkNotNull();
        if (_jsonStringMember!= null) {
            return _jsonStringMember;
        }
        Object __rawValue = super._map.get("string");
        _jsonStringMember = DataTemplateUtil.coerceStringOutput(__rawValue);
        return _jsonStringMember;
    }

    public void setJsonString(String value) {
        checkNotNull();
        super._map.clear();
        _jsonStringMember = value;
        CheckedUtil.putWithoutChecking(super._map, "string", value);
    }

    public static RequestParameterValue.ProjectionMask createMask() {
        return new RequestParameterValue.ProjectionMask();
    }

    @Override
    public RequestParameterValue clone()
        throws CloneNotSupportedException
    {
        RequestParameterValue __clone = ((RequestParameterValue) super.clone());
        __clone.__changeListener = new RequestParameterValue.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public RequestParameterValue copy()
        throws CloneNotSupportedException
    {
        RequestParameterValue __copy = ((RequestParameterValue) super.copy());
        __copy._jsonStringMember = null;
        __copy._mvelExpressionMember = null;
        __copy.__changeListener = new RequestParameterValue.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    public TyperefInfo typerefInfo() {
        return TYPEREFINFO;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final RequestParameterValue __objectRef;

        private ChangeListener(RequestParameterValue reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "string":
                    __objectRef._jsonStringMember = null;
                    break;
                case "com.linkedin.feathr.featureDataModel.MvelExpression":
                    __objectRef._mvelExpressionMember = null;
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

        public com.linkedin.feathr.featureDataModel.MvelExpression.Fields MvelExpression() {
            return new com.linkedin.feathr.featureDataModel.MvelExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.MvelExpression");
        }

        public PathSpec JsonString() {
            return new PathSpec(getPathComponents(), "string");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _MvelExpressionMask;

        ProjectionMask() {
            super(3);
        }

        public RequestParameterValue.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
            _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
            getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
            return this;
        }

        public RequestParameterValue.ProjectionMask withJsonString() {
            getDataMap().put("string", MaskMap.POSITIVE_MASK);
            return this;
        }

    }


    /**
     * Represents the value part in a Rest.li request paramater key/value pair.
     * 
     */
    private final static class UnionTyperefInfo
        extends TyperefInfo
    {

        private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents the value part in a Rest.li request paramater key/value pair.*/typeref RequestParameterValue=union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**Represents a Json string.*/typeref JsonString=string]", SchemaFormatType.PDL));

        public UnionTyperefInfo() {
            super(SCHEMA);
        }

        public static TyperefDataSchema dataSchema() {
            return SCHEMA;
        }

    }

}
