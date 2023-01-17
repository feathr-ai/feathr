
package com.linkedin.feathr.compute;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.UnionTemplate;


/**
 * Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\OfflineKeyFunction.pdl.")
public class OfflineKeyFunction
    extends RecordTemplate
{

    private final static OfflineKeyFunction.Fields _fields = new OfflineKeyFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineKeyFunction{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has x field, a key function being defined as getIdFromUrn(x) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:string/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u00e2\u20ac\ufffd } feathr will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string,string]={}}]}", SchemaFormatType.PDL));
    private OfflineKeyFunction.KeyFunction _keyFunctionField = null;
    private OfflineKeyFunction.ChangeListener __changeListener = new OfflineKeyFunction.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");

    public OfflineKeyFunction() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public OfflineKeyFunction(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static OfflineKeyFunction.Fields fields() {
        return _fields;
    }

    public static OfflineKeyFunction.ProjectionMask createMask() {
        return new OfflineKeyFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see OfflineKeyFunction.Fields#keyFunction
     */
    public boolean hasKeyFunction() {
        if (_keyFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("keyFunction");
    }

    /**
     * Remover for keyFunction
     * 
     * @see OfflineKeyFunction.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see OfflineKeyFunction.Fields#keyFunction
     */
    public OfflineKeyFunction.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see OfflineKeyFunction.Fields#keyFunction
     */
    @Nullable
    public OfflineKeyFunction.KeyFunction getKeyFunction() {
        if (_keyFunctionField!= null) {
            return _keyFunctionField;
        } else {
            Object __rawValue = super._map.get("keyFunction");
            _keyFunctionField = ((__rawValue == null)?null:new OfflineKeyFunction.KeyFunction(__rawValue));
            return _keyFunctionField;
        }
    }

    /**
     * Setter for keyFunction
     * 
     * @see OfflineKeyFunction.Fields#keyFunction
     */
    public OfflineKeyFunction setKeyFunction(OfflineKeyFunction.KeyFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
                    _keyFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
                    _keyFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see OfflineKeyFunction.Fields#keyFunction
     */
    public OfflineKeyFunction setKeyFunction(
        @Nonnull
        OfflineKeyFunction.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.compute.OfflineKeyFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    @Override
    public OfflineKeyFunction clone()
        throws CloneNotSupportedException
    {
        OfflineKeyFunction __clone = ((OfflineKeyFunction) super.clone());
        __clone.__changeListener = new OfflineKeyFunction.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public OfflineKeyFunction copy()
        throws CloneNotSupportedException
    {
        OfflineKeyFunction __copy = ((OfflineKeyFunction) super.copy());
        __copy._keyFunctionField = null;
        __copy.__changeListener = new OfflineKeyFunction.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final OfflineKeyFunction __objectRef;

        private ChangeListener(OfflineKeyFunction reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
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
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has x field, a key function being defined as getIdFromUrn(x) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution.
         * 
         */
        public com.linkedin.feathr.compute.OfflineKeyFunction.KeyFunction.Fields keyFunction() {
            return new com.linkedin.feathr.compute.OfflineKeyFunction.KeyFunction.Fields(getPathComponents(), "keyFunction");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\OfflineKeyFunction.pdl.")
    public static class KeyFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.compute/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}}{namespace com.linkedin.feathr.compute/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:string/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u00e2\u20ac\ufffd } feathr will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string,string]={}}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.compute.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.compute.SqlExpression _sqlExpressionMember = null;
        private com.linkedin.feathr.compute.UserDefinedFunction _userDefinedFunctionMember = null;
        private OfflineKeyFunction.KeyFunction.ChangeListener __changeListener = new OfflineKeyFunction.KeyFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.MvelExpression");
        private final static DataSchema MEMBER_SqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.SqlExpression");
        private final static DataSchema MEMBER_UserDefinedFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.UserDefinedFunction");

        public KeyFunction() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public KeyFunction(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static OfflineKeyFunction.KeyFunction create(com.linkedin.feathr.compute.MvelExpression value) {
            OfflineKeyFunction.KeyFunction newUnion = new OfflineKeyFunction.KeyFunction();
            newUnion.setMvelExpression(value);
            return newUnion;
        }

        public boolean isMvelExpression() {
            return memberIs("com.linkedin.feathr.compute.MvelExpression");
        }

        public com.linkedin.feathr.compute.MvelExpression getMvelExpression() {
            checkNotNull();
            if (_mvelExpressionMember!= null) {
                return _mvelExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.MvelExpression");
            _mvelExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.MvelExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _mvelExpressionMember;
        }

        public void setMvelExpression(com.linkedin.feathr.compute.MvelExpression value) {
            checkNotNull();
            super._map.clear();
            _mvelExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.MvelExpression", value.data());
        }

        public static OfflineKeyFunction.KeyFunction create(com.linkedin.feathr.compute.SqlExpression value) {
            OfflineKeyFunction.KeyFunction newUnion = new OfflineKeyFunction.KeyFunction();
            newUnion.setSqlExpression(value);
            return newUnion;
        }

        public boolean isSqlExpression() {
            return memberIs("com.linkedin.feathr.compute.SqlExpression");
        }

        public com.linkedin.feathr.compute.SqlExpression getSqlExpression() {
            checkNotNull();
            if (_sqlExpressionMember!= null) {
                return _sqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.SqlExpression");
            _sqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.SqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sqlExpressionMember;
        }

        public void setSqlExpression(com.linkedin.feathr.compute.SqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.SqlExpression", value.data());
        }

        public static OfflineKeyFunction.KeyFunction create(com.linkedin.feathr.compute.UserDefinedFunction value) {
            OfflineKeyFunction.KeyFunction newUnion = new OfflineKeyFunction.KeyFunction();
            newUnion.setUserDefinedFunction(value);
            return newUnion;
        }

        public boolean isUserDefinedFunction() {
            return memberIs("com.linkedin.feathr.compute.UserDefinedFunction");
        }

        public com.linkedin.feathr.compute.UserDefinedFunction getUserDefinedFunction() {
            checkNotNull();
            if (_userDefinedFunctionMember!= null) {
                return _userDefinedFunctionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.UserDefinedFunction");
            _userDefinedFunctionMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.UserDefinedFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _userDefinedFunctionMember;
        }

        public void setUserDefinedFunction(com.linkedin.feathr.compute.UserDefinedFunction value) {
            checkNotNull();
            super._map.clear();
            _userDefinedFunctionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.UserDefinedFunction", value.data());
        }

        public static OfflineKeyFunction.KeyFunction.ProjectionMask createMask() {
            return new OfflineKeyFunction.KeyFunction.ProjectionMask();
        }

        @Override
        public OfflineKeyFunction.KeyFunction clone()
            throws CloneNotSupportedException
        {
            OfflineKeyFunction.KeyFunction __clone = ((OfflineKeyFunction.KeyFunction) super.clone());
            __clone.__changeListener = new OfflineKeyFunction.KeyFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public OfflineKeyFunction.KeyFunction copy()
            throws CloneNotSupportedException
        {
            OfflineKeyFunction.KeyFunction __copy = ((OfflineKeyFunction.KeyFunction) super.copy());
            __copy._sqlExpressionMember = null;
            __copy._userDefinedFunctionMember = null;
            __copy._mvelExpressionMember = null;
            __copy.__changeListener = new OfflineKeyFunction.KeyFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final OfflineKeyFunction.KeyFunction __objectRef;

            private ChangeListener(OfflineKeyFunction.KeyFunction reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.compute.SqlExpression":
                        __objectRef._sqlExpressionMember = null;
                        break;
                    case "com.linkedin.feathr.compute.UserDefinedFunction":
                        __objectRef._userDefinedFunctionMember = null;
                        break;
                    case "com.linkedin.feathr.compute.MvelExpression":
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

            public com.linkedin.feathr.compute.MvelExpression.Fields MvelExpression() {
                return new com.linkedin.feathr.compute.MvelExpression.Fields(getPathComponents(), "com.linkedin.feathr.compute.MvelExpression");
            }

            public com.linkedin.feathr.compute.SqlExpression.Fields SqlExpression() {
                return new com.linkedin.feathr.compute.SqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.compute.SqlExpression");
            }

            public com.linkedin.feathr.compute.UserDefinedFunction.Fields UserDefinedFunction() {
                return new com.linkedin.feathr.compute.UserDefinedFunction.Fields(getPathComponents(), "com.linkedin.feathr.compute.UserDefinedFunction");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.MvelExpression.ProjectionMask _MvelExpressionMask;
            private com.linkedin.feathr.compute.SqlExpression.ProjectionMask _SqlExpressionMask;
            private com.linkedin.feathr.compute.UserDefinedFunction.ProjectionMask _UserDefinedFunctionMask;

            ProjectionMask() {
                super(4);
            }

            public OfflineKeyFunction.KeyFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.compute.MvelExpression.ProjectionMask, com.linkedin.feathr.compute.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.compute.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.compute.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public OfflineKeyFunction.KeyFunction.ProjectionMask withSqlExpression(Function<com.linkedin.feathr.compute.SqlExpression.ProjectionMask, com.linkedin.feathr.compute.SqlExpression.ProjectionMask> nestedMask) {
                _SqlExpressionMask = nestedMask.apply(((_SqlExpressionMask == null)?com.linkedin.feathr.compute.SqlExpression.createMask():_SqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.compute.SqlExpression", _SqlExpressionMask.getDataMap());
                return this;
            }

            public OfflineKeyFunction.KeyFunction.ProjectionMask withUserDefinedFunction(Function<com.linkedin.feathr.compute.UserDefinedFunction.ProjectionMask, com.linkedin.feathr.compute.UserDefinedFunction.ProjectionMask> nestedMask) {
                _UserDefinedFunctionMask = nestedMask.apply(((_UserDefinedFunctionMask == null)?com.linkedin.feathr.compute.UserDefinedFunction.createMask():_UserDefinedFunctionMask));
                getDataMap().put("com.linkedin.feathr.compute.UserDefinedFunction", _UserDefinedFunctionMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.OfflineKeyFunction.KeyFunction.ProjectionMask _keyFunctionMask;

        ProjectionMask() {
            super(2);
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has x field, a key function being defined as getIdFromUrn(x) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution.
         * 
         */
        public OfflineKeyFunction.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.compute.OfflineKeyFunction.KeyFunction.ProjectionMask, com.linkedin.feathr.compute.OfflineKeyFunction.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?OfflineKeyFunction.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has x field, a key function being defined as getIdFromUrn(x) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution.
         * 
         */
        public OfflineKeyFunction.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
