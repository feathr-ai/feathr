
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
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
import com.linkedin.data.template.UnionTemplate;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SequentialJoinFeatureSourcesAnchor.pdl.")
public class ExpansionKeyFunction
    extends RecordTemplate
{

    private final static ExpansionKeyFunction.Fields _fields = new ExpansionKeyFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel,record ExpansionKeyFunction includes/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**This transformation ensures the feature data of the base feature can be converted to the expected key format of the expansion feature.*/keyFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**Represents a transformation function that always returns the same value as the input. See https://en.wikipedia.org/wiki/Identity_function.*/record IdentityFunction{}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}]}", SchemaFormatType.PDL));
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private ExpansionKeyFunction.KeyFunction _keyFunctionField = null;
    private ExpansionKeyFunction.ChangeListener __changeListener = new ExpansionKeyFunction.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public ExpansionKeyFunction() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public ExpansionKeyFunction(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static ExpansionKeyFunction.Fields fields() {
        return _fields;
    }

    public static ExpansionKeyFunction.ProjectionMask createMask() {
        return new ExpansionKeyFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see ExpansionKeyFunction.Fields#keyPlaceholders
     */
    public boolean hasKeyPlaceholders() {
        if (_keyPlaceholdersField!= null) {
            return true;
        }
        return super._map.containsKey("keyPlaceholders");
    }

    /**
     * Remover for keyPlaceholders
     * 
     * @see ExpansionKeyFunction.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see ExpansionKeyFunction.Fields#keyPlaceholders
     */
    public KeyPlaceholderArray getKeyPlaceholders(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getKeyPlaceholders();
            case NULL:
                if (_keyPlaceholdersField!= null) {
                    return _keyPlaceholdersField;
                } else {
                    Object __rawValue = super._map.get("keyPlaceholders");
                    _keyPlaceholdersField = ((__rawValue == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keyPlaceholdersField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see ExpansionKeyFunction.Fields#keyPlaceholders
     */
    @Nonnull
    public KeyPlaceholderArray getKeyPlaceholders() {
        if (_keyPlaceholdersField!= null) {
            return _keyPlaceholdersField;
        } else {
            Object __rawValue = super._map.get("keyPlaceholders");
            if (__rawValue == null) {
                return DEFAULT_KeyPlaceholders;
            }
            _keyPlaceholdersField = ((__rawValue == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keyPlaceholdersField;
        }
    }

    /**
     * Setter for keyPlaceholders
     * 
     * @see ExpansionKeyFunction.Fields#keyPlaceholders
     */
    public ExpansionKeyFunction setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.ExpansionKeyFunction");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
                    _keyPlaceholdersField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyPlaceholders();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
                    _keyPlaceholdersField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
                    _keyPlaceholdersField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyPlaceholders
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see ExpansionKeyFunction.Fields#keyPlaceholders
     */
    public ExpansionKeyFunction setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.ExpansionKeyFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see ExpansionKeyFunction.Fields#keyFunction
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
     * @see ExpansionKeyFunction.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see ExpansionKeyFunction.Fields#keyFunction
     */
    public ExpansionKeyFunction.KeyFunction getKeyFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeyFunction();
            case DEFAULT:
            case NULL:
                if (_keyFunctionField!= null) {
                    return _keyFunctionField;
                } else {
                    Object __rawValue = super._map.get("keyFunction");
                    _keyFunctionField = ((__rawValue == null)?null:new ExpansionKeyFunction.KeyFunction(__rawValue));
                    return _keyFunctionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see ExpansionKeyFunction.Fields#keyFunction
     */
    @Nonnull
    public ExpansionKeyFunction.KeyFunction getKeyFunction() {
        if (_keyFunctionField!= null) {
            return _keyFunctionField;
        } else {
            Object __rawValue = super._map.get("keyFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keyFunction");
            }
            _keyFunctionField = ((__rawValue == null)?null:new ExpansionKeyFunction.KeyFunction(__rawValue));
            return _keyFunctionField;
        }
    }

    /**
     * Setter for keyFunction
     * 
     * @see ExpansionKeyFunction.Fields#keyFunction
     */
    public ExpansionKeyFunction setKeyFunction(ExpansionKeyFunction.KeyFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyFunction of com.linkedin.feathr.featureDataModel.ExpansionKeyFunction");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
                    _keyFunctionField = value;
                }
                break;
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
     * @see ExpansionKeyFunction.Fields#keyFunction
     */
    public ExpansionKeyFunction setKeyFunction(
        @Nonnull
        ExpansionKeyFunction.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.ExpansionKeyFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    @Override
    public ExpansionKeyFunction clone()
        throws CloneNotSupportedException
    {
        ExpansionKeyFunction __clone = ((ExpansionKeyFunction) super.clone());
        __clone.__changeListener = new ExpansionKeyFunction.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public ExpansionKeyFunction copy()
        throws CloneNotSupportedException
    {
        ExpansionKeyFunction __copy = ((ExpansionKeyFunction) super.copy());
        __copy._keyFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy.__changeListener = new ExpansionKeyFunction.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final ExpansionKeyFunction __objectRef;

        private ChangeListener(ExpansionKeyFunction reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
                    break;
                case "keyPlaceholders":
                    __objectRef._keyPlaceholdersField = null;
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
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.Fields keyPlaceholders() {
            return new com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.Fields(getPathComponents(), "keyPlaceholders");
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public PathSpec keyPlaceholders(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "keyPlaceholders");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * This transformation ensures the feature data of the base feature can be converted to the expected key format of the expansion feature.
         * 
         */
        public com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.KeyFunction.Fields keyFunction() {
            return new com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.KeyFunction.Fields(getPathComponents(), "keyFunction");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SequentialJoinFeatureSourcesAnchor.pdl.")
    public static class KeyFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**Represents a transformation function that always returns the same value as the input. See https://en.wikipedia.org/wiki/Identity_function.*/record IdentityFunction{}}{namespace com.linkedin.feathr.featureDataModel/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.IdentityFunction _identityFunctionMember = null;
        private com.linkedin.feathr.featureDataModel.UserDefinedFunction _userDefinedFunctionMember = null;
        private ExpansionKeyFunction.KeyFunction.ChangeListener __changeListener = new ExpansionKeyFunction.KeyFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
        private final static DataSchema MEMBER_IdentityFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.IdentityFunction");
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

        public static ExpansionKeyFunction.KeyFunction create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            ExpansionKeyFunction.KeyFunction newUnion = new ExpansionKeyFunction.KeyFunction();
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

        public static ExpansionKeyFunction.KeyFunction create(com.linkedin.feathr.featureDataModel.IdentityFunction value) {
            ExpansionKeyFunction.KeyFunction newUnion = new ExpansionKeyFunction.KeyFunction();
            newUnion.setIdentityFunction(value);
            return newUnion;
        }

        public boolean isIdentityFunction() {
            return memberIs("com.linkedin.feathr.featureDataModel.IdentityFunction");
        }

        public com.linkedin.feathr.featureDataModel.IdentityFunction getIdentityFunction() {
            checkNotNull();
            if (_identityFunctionMember!= null) {
                return _identityFunctionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.IdentityFunction");
            _identityFunctionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.IdentityFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _identityFunctionMember;
        }

        public void setIdentityFunction(com.linkedin.feathr.featureDataModel.IdentityFunction value) {
            checkNotNull();
            super._map.clear();
            _identityFunctionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.IdentityFunction", value.data());
        }

        public static ExpansionKeyFunction.KeyFunction create(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            ExpansionKeyFunction.KeyFunction newUnion = new ExpansionKeyFunction.KeyFunction();
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

        public static ExpansionKeyFunction.KeyFunction.ProjectionMask createMask() {
            return new ExpansionKeyFunction.KeyFunction.ProjectionMask();
        }

        @Override
        public ExpansionKeyFunction.KeyFunction clone()
            throws CloneNotSupportedException
        {
            ExpansionKeyFunction.KeyFunction __clone = ((ExpansionKeyFunction.KeyFunction) super.clone());
            __clone.__changeListener = new ExpansionKeyFunction.KeyFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public ExpansionKeyFunction.KeyFunction copy()
            throws CloneNotSupportedException
        {
            ExpansionKeyFunction.KeyFunction __copy = ((ExpansionKeyFunction.KeyFunction) super.copy());
            __copy._identityFunctionMember = null;
            __copy._mvelExpressionMember = null;
            __copy._userDefinedFunctionMember = null;
            __copy.__changeListener = new ExpansionKeyFunction.KeyFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final ExpansionKeyFunction.KeyFunction __objectRef;

            private ChangeListener(ExpansionKeyFunction.KeyFunction reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.IdentityFunction":
                        __objectRef._identityFunctionMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.MvelExpression":
                        __objectRef._mvelExpressionMember = null;
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

            public com.linkedin.feathr.featureDataModel.IdentityFunction.Fields IdentityFunction() {
                return new com.linkedin.feathr.featureDataModel.IdentityFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.IdentityFunction");
            }

            public com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields UserDefinedFunction() {
                return new com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.UserDefinedFunction");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _MvelExpressionMask;
            private com.linkedin.feathr.featureDataModel.IdentityFunction.ProjectionMask _IdentityFunctionMask;
            private com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask _UserDefinedFunctionMask;

            ProjectionMask() {
                super(4);
            }

            public ExpansionKeyFunction.KeyFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public ExpansionKeyFunction.KeyFunction.ProjectionMask withIdentityFunction(Function<com.linkedin.feathr.featureDataModel.IdentityFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.IdentityFunction.ProjectionMask> nestedMask) {
                _IdentityFunctionMask = nestedMask.apply(((_IdentityFunctionMask == null)?com.linkedin.feathr.featureDataModel.IdentityFunction.createMask():_IdentityFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.IdentityFunction", _IdentityFunctionMask.getDataMap());
                return this;
            }

            public ExpansionKeyFunction.KeyFunction.ProjectionMask withUserDefinedFunction(Function<com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask> nestedMask) {
                _UserDefinedFunctionMask = nestedMask.apply(((_UserDefinedFunctionMask == null)?com.linkedin.feathr.featureDataModel.UserDefinedFunction.createMask():_UserDefinedFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UserDefinedFunction", _UserDefinedFunctionMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.KeyFunction.ProjectionMask _keyFunctionMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public ExpansionKeyFunction.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public ExpansionKeyFunction.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public ExpansionKeyFunction.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$count", count);
            }
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public ExpansionKeyFunction.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$count", count);
            }
            return this;
        }

        /**
         * This transformation ensures the feature data of the base feature can be converted to the expected key format of the expansion feature.
         * 
         */
        public ExpansionKeyFunction.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?ExpansionKeyFunction.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * This transformation ensures the feature data of the base feature can be converted to the expected key format of the expansion feature.
         * 
         */
        public ExpansionKeyFunction.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
