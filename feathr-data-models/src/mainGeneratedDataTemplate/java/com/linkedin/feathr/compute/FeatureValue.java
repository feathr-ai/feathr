
package com.linkedin.feathr.compute;

import java.util.List;
import javax.annotation.Generated;
import com.linkedin.data.ByteString;
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\FeatureValue.pdl.")
public class FeatureValue
    extends UnionTemplate
    implements HasTyperefInfo
{

    private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[boolean,int,long,float,double,string,bytes]", SchemaFormatType.PDL));
    private java.lang.Boolean _booleanMember = null;
    private Integer _intMember = null;
    private java.lang.Long _longMember = null;
    private java.lang.Float _floatMember = null;
    private java.lang.Double _doubleMember = null;
    private java.lang.String _stringMember = null;
    private ByteString _bytesMember = null;
    private FeatureValue.ChangeListener __changeListener = new FeatureValue.ChangeListener(this);
    private final static DataSchema MEMBER_Boolean = SCHEMA.getTypeByMemberKey("boolean");
    private final static DataSchema MEMBER_Int = SCHEMA.getTypeByMemberKey("int");
    private final static DataSchema MEMBER_Long = SCHEMA.getTypeByMemberKey("long");
    private final static DataSchema MEMBER_Float = SCHEMA.getTypeByMemberKey("float");
    private final static DataSchema MEMBER_Double = SCHEMA.getTypeByMemberKey("double");
    private final static DataSchema MEMBER_String = SCHEMA.getTypeByMemberKey("string");
    private final static DataSchema MEMBER_Bytes = SCHEMA.getTypeByMemberKey("bytes");
    private final static TyperefInfo TYPEREFINFO = new FeatureValue.UnionTyperefInfo();

    public FeatureValue() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public FeatureValue(Object data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UnionDataSchema dataSchema() {
        return SCHEMA;
    }

    public static FeatureValue create(java.lang.Boolean value) {
        FeatureValue newUnion = new FeatureValue();
        newUnion.setBoolean(value);
        return newUnion;
    }

    public boolean isBoolean() {
        return memberIs("boolean");
    }

    public java.lang.Boolean getBoolean() {
        checkNotNull();
        if (_booleanMember!= null) {
            return _booleanMember;
        }
        Object __rawValue = super._map.get("boolean");
        _booleanMember = DataTemplateUtil.coerceBooleanOutput(__rawValue);
        return _booleanMember;
    }

    public void setBoolean(java.lang.Boolean value) {
        checkNotNull();
        super._map.clear();
        _booleanMember = value;
        CheckedUtil.putWithoutChecking(super._map, "boolean", value);
    }

    public static FeatureValue create(Integer value) {
        FeatureValue newUnion = new FeatureValue();
        newUnion.setInt(value);
        return newUnion;
    }

    public boolean isInt() {
        return memberIs("int");
    }

    public Integer getInt() {
        checkNotNull();
        if (_intMember!= null) {
            return _intMember;
        }
        Object __rawValue = super._map.get("int");
        _intMember = DataTemplateUtil.coerceIntOutput(__rawValue);
        return _intMember;
    }

    public void setInt(Integer value) {
        checkNotNull();
        super._map.clear();
        _intMember = value;
        CheckedUtil.putWithoutChecking(super._map, "int", DataTemplateUtil.coerceIntInput(value));
    }

    public static FeatureValue create(java.lang.Long value) {
        FeatureValue newUnion = new FeatureValue();
        newUnion.setLong(value);
        return newUnion;
    }

    public boolean isLong() {
        return memberIs("long");
    }

    public java.lang.Long getLong() {
        checkNotNull();
        if (_longMember!= null) {
            return _longMember;
        }
        Object __rawValue = super._map.get("long");
        _longMember = DataTemplateUtil.coerceLongOutput(__rawValue);
        return _longMember;
    }

    public void setLong(java.lang.Long value) {
        checkNotNull();
        super._map.clear();
        _longMember = value;
        CheckedUtil.putWithoutChecking(super._map, "long", DataTemplateUtil.coerceLongInput(value));
    }

    public static FeatureValue create(java.lang.Float value) {
        FeatureValue newUnion = new FeatureValue();
        newUnion.setFloat(value);
        return newUnion;
    }

    public boolean isFloat() {
        return memberIs("float");
    }

    public java.lang.Float getFloat() {
        checkNotNull();
        if (_floatMember!= null) {
            return _floatMember;
        }
        Object __rawValue = super._map.get("float");
        _floatMember = DataTemplateUtil.coerceFloatOutput(__rawValue);
        return _floatMember;
    }

    public void setFloat(java.lang.Float value) {
        checkNotNull();
        super._map.clear();
        _floatMember = value;
        CheckedUtil.putWithoutChecking(super._map, "float", DataTemplateUtil.coerceFloatInput(value));
    }

    public static FeatureValue create(java.lang.Double value) {
        FeatureValue newUnion = new FeatureValue();
        newUnion.setDouble(value);
        return newUnion;
    }

    public boolean isDouble() {
        return memberIs("double");
    }

    public java.lang.Double getDouble() {
        checkNotNull();
        if (_doubleMember!= null) {
            return _doubleMember;
        }
        Object __rawValue = super._map.get("double");
        _doubleMember = DataTemplateUtil.coerceDoubleOutput(__rawValue);
        return _doubleMember;
    }

    public void setDouble(java.lang.Double value) {
        checkNotNull();
        super._map.clear();
        _doubleMember = value;
        CheckedUtil.putWithoutChecking(super._map, "double", DataTemplateUtil.coerceDoubleInput(value));
    }

    public static FeatureValue create(java.lang.String value) {
        FeatureValue newUnion = new FeatureValue();
        newUnion.setString(value);
        return newUnion;
    }

    public boolean isString() {
        return memberIs("string");
    }

    public java.lang.String getString() {
        checkNotNull();
        if (_stringMember!= null) {
            return _stringMember;
        }
        Object __rawValue = super._map.get("string");
        _stringMember = DataTemplateUtil.coerceStringOutput(__rawValue);
        return _stringMember;
    }

    public void setString(java.lang.String value) {
        checkNotNull();
        super._map.clear();
        _stringMember = value;
        CheckedUtil.putWithoutChecking(super._map, "string", value);
    }

    public static FeatureValue create(ByteString value) {
        FeatureValue newUnion = new FeatureValue();
        newUnion.setBytes(value);
        return newUnion;
    }

    public boolean isBytes() {
        return memberIs("bytes");
    }

    public ByteString getBytes() {
        checkNotNull();
        if (_bytesMember!= null) {
            return _bytesMember;
        }
        Object __rawValue = super._map.get("bytes");
        _bytesMember = DataTemplateUtil.coerceBytesOutput(__rawValue);
        return _bytesMember;
    }

    public void setBytes(ByteString value) {
        checkNotNull();
        super._map.clear();
        _bytesMember = value;
        CheckedUtil.putWithoutChecking(super._map, "bytes", value);
    }

    public static FeatureValue.ProjectionMask createMask() {
        return new FeatureValue.ProjectionMask();
    }

    @Override
    public FeatureValue clone()
        throws CloneNotSupportedException
    {
        FeatureValue __clone = ((FeatureValue) super.clone());
        __clone.__changeListener = new FeatureValue.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeatureValue copy()
        throws CloneNotSupportedException
    {
        FeatureValue __copy = ((FeatureValue) super.copy());
        __copy._booleanMember = null;
        __copy._stringMember = null;
        __copy._doubleMember = null;
        __copy._bytesMember = null;
        __copy._floatMember = null;
        __copy._intMember = null;
        __copy._longMember = null;
        __copy.__changeListener = new FeatureValue.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    public TyperefInfo typerefInfo() {
        return TYPEREFINFO;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<java.lang.String, Object>
    {

        private final FeatureValue __objectRef;

        private ChangeListener(FeatureValue reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(java.lang.String key, Object value) {
            switch (key) {
                case "boolean":
                    __objectRef._booleanMember = null;
                    break;
                case "string":
                    __objectRef._stringMember = null;
                    break;
                case "double":
                    __objectRef._doubleMember = null;
                    break;
                case "bytes":
                    __objectRef._bytesMember = null;
                    break;
                case "float":
                    __objectRef._floatMember = null;
                    break;
                case "int":
                    __objectRef._intMember = null;
                    break;
                case "long":
                    __objectRef._longMember = null;
                    break;
            }
        }

    }

    public static class Fields
        extends PathSpec
    {


        public Fields(List<java.lang.String> path, java.lang.String name) {
            super(path, name);
        }

        public Fields() {
            super();
        }

        public PathSpec Boolean() {
            return new PathSpec(getPathComponents(), "boolean");
        }

        public PathSpec Int() {
            return new PathSpec(getPathComponents(), "int");
        }

        public PathSpec Long() {
            return new PathSpec(getPathComponents(), "long");
        }

        public PathSpec Float() {
            return new PathSpec(getPathComponents(), "float");
        }

        public PathSpec Double() {
            return new PathSpec(getPathComponents(), "double");
        }

        public PathSpec String() {
            return new PathSpec(getPathComponents(), "string");
        }

        public PathSpec Bytes() {
            return new PathSpec(getPathComponents(), "bytes");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(10);
        }

        public FeatureValue.ProjectionMask withBoolean() {
            getDataMap().put("boolean", MaskMap.POSITIVE_MASK);
            return this;
        }

        public FeatureValue.ProjectionMask withInt() {
            getDataMap().put("int", MaskMap.POSITIVE_MASK);
            return this;
        }

        public FeatureValue.ProjectionMask withLong() {
            getDataMap().put("long", MaskMap.POSITIVE_MASK);
            return this;
        }

        public FeatureValue.ProjectionMask withFloat() {
            getDataMap().put("float", MaskMap.POSITIVE_MASK);
            return this;
        }

        public FeatureValue.ProjectionMask withDouble() {
            getDataMap().put("double", MaskMap.POSITIVE_MASK);
            return this;
        }

        public FeatureValue.ProjectionMask withString() {
            getDataMap().put("string", MaskMap.POSITIVE_MASK);
            return this;
        }

        public FeatureValue.ProjectionMask withBytes() {
            getDataMap().put("bytes", MaskMap.POSITIVE_MASK);
            return this;
        }

    }


    /**
     * Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.
     * 
     */
    private final static class UnionTyperefInfo
        extends TyperefInfo
    {

        private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]", SchemaFormatType.PDL));

        public UnionTyperefInfo() {
            super(SCHEMA);
        }

        public static TyperefDataSchema dataSchema() {
            return SCHEMA;
        }

    }

}
