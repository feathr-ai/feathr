
package com.linkedin.feathr.featureDataModel;

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
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/OfflineDataSourceKey.pdl.")
public class OfflineDataSourceKey
    extends RecordTemplate
{

    private final static OfflineDataSourceKey.Fields _fields = new OfflineDataSourceKey.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineDataSourceKey{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}]}", SchemaFormatType.PDL));
    private OfflineDataSourceKey.KeyFunction _keyFunctionField = null;
    private OfflineDataSourceKey.ChangeListener __changeListener = new OfflineDataSourceKey.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");

    public OfflineDataSourceKey() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public OfflineDataSourceKey(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static OfflineDataSourceKey.Fields fields() {
        return _fields;
    }

    public static OfflineDataSourceKey.ProjectionMask createMask() {
        return new OfflineDataSourceKey.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see OfflineDataSourceKey.Fields#keyFunction
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
     * @see OfflineDataSourceKey.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see OfflineDataSourceKey.Fields#keyFunction
     */
    public OfflineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see OfflineDataSourceKey.Fields#keyFunction
     */
    @Nullable
    public OfflineDataSourceKey.KeyFunction getKeyFunction() {
        if (_keyFunctionField!= null) {
            return _keyFunctionField;
        } else {
            Object __rawValue = super._map.get("keyFunction");
            _keyFunctionField = ((__rawValue == null)?null:new OfflineDataSourceKey.KeyFunction(__rawValue));
            return _keyFunctionField;
        }
    }

    /**
     * Setter for keyFunction
     * 
     * @see OfflineDataSourceKey.Fields#keyFunction
     */
    public OfflineDataSourceKey setKeyFunction(OfflineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see OfflineDataSourceKey.Fields#keyFunction
     */
    public OfflineDataSourceKey setKeyFunction(
        @Nonnull
        OfflineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.OfflineDataSourceKey to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    @Override
    public OfflineDataSourceKey clone()
        throws CloneNotSupportedException
    {
        OfflineDataSourceKey __clone = ((OfflineDataSourceKey) super.clone());
        __clone.__changeListener = new OfflineDataSourceKey.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public OfflineDataSourceKey copy()
        throws CloneNotSupportedException
    {
        OfflineDataSourceKey __copy = ((OfflineDataSourceKey) super.copy());
        __copy._keyFunctionField = null;
        __copy.__changeListener = new OfflineDataSourceKey.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final OfflineDataSourceKey __objectRef;

        private ChangeListener(OfflineDataSourceKey reference) {
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
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.Fields keyFunction() {
            return new com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.Fields(getPathComponents(), "keyFunction");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/OfflineDataSourceKey.pdl.")
    public static class KeyFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}{namespace com.linkedin.feathr.featureDataModel/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.UserDefinedFunction _userDefinedFunctionMember = null;
        private OfflineDataSourceKey.KeyFunction.ChangeListener __changeListener = new OfflineDataSourceKey.KeyFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        private final static DataSchema MEMBER_UserDefinedFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.UserDefinedFunction");

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

        public static OfflineDataSourceKey.KeyFunction create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            OfflineDataSourceKey.KeyFunction newUnion = new OfflineDataSourceKey.KeyFunction();
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

        public static OfflineDataSourceKey.KeyFunction create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            OfflineDataSourceKey.KeyFunction newUnion = new OfflineDataSourceKey.KeyFunction();
            newUnion.setSparkSqlExpression(value);
            return newUnion;
        }

        public boolean isSparkSqlExpression() {
            return memberIs("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        }

        public com.linkedin.feathr.featureDataModel.SparkSqlExpression getSparkSqlExpression() {
            checkNotNull();
            if (_sparkSqlExpressionMember!= null) {
                return _sparkSqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            _sparkSqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SparkSqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sparkSqlExpressionMember;
        }

        public void setSparkSqlExpression(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sparkSqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SparkSqlExpression", value.data());
        }

        public static OfflineDataSourceKey.KeyFunction create(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            OfflineDataSourceKey.KeyFunction newUnion = new OfflineDataSourceKey.KeyFunction();
            newUnion.setUserDefinedFunction(value);
            return newUnion;
        }

        public boolean isUserDefinedFunction() {
            return memberIs("com.linkedin.feathr.featureDataModel.UserDefinedFunction");
        }

        public com.linkedin.feathr.featureDataModel.UserDefinedFunction getUserDefinedFunction() {
            checkNotNull();
            if (_userDefinedFunctionMember!= null) {
                return _userDefinedFunctionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.UserDefinedFunction");
            _userDefinedFunctionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.UserDefinedFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _userDefinedFunctionMember;
        }

        public void setUserDefinedFunction(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            checkNotNull();
            super._map.clear();
            _userDefinedFunctionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.UserDefinedFunction", value.data());
        }

        public static OfflineDataSourceKey.KeyFunction.ProjectionMask createMask() {
            return new OfflineDataSourceKey.KeyFunction.ProjectionMask();
        }

        @Override
        public OfflineDataSourceKey.KeyFunction clone()
            throws CloneNotSupportedException
        {
            OfflineDataSourceKey.KeyFunction __clone = ((OfflineDataSourceKey.KeyFunction) super.clone());
            __clone.__changeListener = new OfflineDataSourceKey.KeyFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public OfflineDataSourceKey.KeyFunction copy()
            throws CloneNotSupportedException
        {
            OfflineDataSourceKey.KeyFunction __copy = ((OfflineDataSourceKey.KeyFunction) super.copy());
            __copy._mvelExpressionMember = null;
            __copy._sparkSqlExpressionMember = null;
            __copy._userDefinedFunctionMember = null;
            __copy.__changeListener = new OfflineDataSourceKey.KeyFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final OfflineDataSourceKey.KeyFunction __objectRef;

            private ChangeListener(OfflineDataSourceKey.KeyFunction reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.MvelExpression":
                        __objectRef._mvelExpressionMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.SparkSqlExpression":
                        __objectRef._sparkSqlExpressionMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.UserDefinedFunction":
                        __objectRef._userDefinedFunctionMember = null;
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

            public com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            }

            public com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields UserDefinedFunction() {
                return new com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.UserDefinedFunction");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _MvelExpressionMask;
            private com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;
            private com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask _UserDefinedFunctionMask;

            ProjectionMask() {
                super(4);
            }

            public OfflineDataSourceKey.KeyFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public OfflineDataSourceKey.KeyFunction.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

            public OfflineDataSourceKey.KeyFunction.ProjectionMask withUserDefinedFunction(Function<com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask> nestedMask) {
                _UserDefinedFunctionMask = nestedMask.apply(((_UserDefinedFunctionMask == null)?com.linkedin.feathr.featureDataModel.UserDefinedFunction.createMask():_UserDefinedFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UserDefinedFunction", _UserDefinedFunctionMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;

        ProjectionMask() {
            super(2);
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public OfflineDataSourceKey.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?OfflineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public OfflineDataSourceKey.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
