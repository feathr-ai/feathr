
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
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
 * A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor. This class defines supported transformation functions (eg. MVEL, UDF) that can be used for an online data source.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/TransformationFunctionForOnlineDataSource.pdl.")
public class TransformationFunctionForOnlineDataSource
    extends RecordTemplate
{

    private final static TransformationFunctionForOnlineDataSource.Fields _fields = new TransformationFunctionForOnlineDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor. This class defines supported transformation functions (eg. MVEL, UDF) that can be used for an online data source.*/record TransformationFunctionForOnlineDataSource{/**Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}", SchemaFormatType.PDL));
    private TransformationFunctionForOnlineDataSource.TransformationFunction _transformationFunctionField = null;
    private TransformationFunctionForOnlineDataSource.ChangeListener __changeListener = new TransformationFunctionForOnlineDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");

    public TransformationFunctionForOnlineDataSource() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public TransformationFunctionForOnlineDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TransformationFunctionForOnlineDataSource.Fields fields() {
        return _fields;
    }

    public static TransformationFunctionForOnlineDataSource.ProjectionMask createMask() {
        return new TransformationFunctionForOnlineDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see TransformationFunctionForOnlineDataSource.Fields#transformationFunction
     */
    public boolean hasTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("transformationFunction");
    }

    /**
     * Remover for transformationFunction
     * 
     * @see TransformationFunctionForOnlineDataSource.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see TransformationFunctionForOnlineDataSource.Fields#transformationFunction
     */
    public TransformationFunctionForOnlineDataSource.TransformationFunction getTransformationFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTransformationFunction();
            case DEFAULT:
            case NULL:
                if (_transformationFunctionField!= null) {
                    return _transformationFunctionField;
                } else {
                    Object __rawValue = super._map.get("transformationFunction");
                    _transformationFunctionField = ((__rawValue == null)?null:new TransformationFunctionForOnlineDataSource.TransformationFunction(__rawValue));
                    return _transformationFunctionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for transformationFunction
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TransformationFunctionForOnlineDataSource.Fields#transformationFunction
     */
    @Nonnull
    public TransformationFunctionForOnlineDataSource.TransformationFunction getTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return _transformationFunctionField;
        } else {
            Object __rawValue = super._map.get("transformationFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("transformationFunction");
            }
            _transformationFunctionField = ((__rawValue == null)?null:new TransformationFunctionForOnlineDataSource.TransformationFunction(__rawValue));
            return _transformationFunctionField;
        }
    }

    /**
     * Setter for transformationFunction
     * 
     * @see TransformationFunctionForOnlineDataSource.Fields#transformationFunction
     */
    public TransformationFunctionForOnlineDataSource setTransformationFunction(TransformationFunctionForOnlineDataSource.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
                    _transformationFunctionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTransformationFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
                    _transformationFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
                    _transformationFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for transformationFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TransformationFunctionForOnlineDataSource.Fields#transformationFunction
     */
    public TransformationFunctionForOnlineDataSource setTransformationFunction(
        @Nonnull
        TransformationFunctionForOnlineDataSource.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    @Override
    public TransformationFunctionForOnlineDataSource clone()
        throws CloneNotSupportedException
    {
        TransformationFunctionForOnlineDataSource __clone = ((TransformationFunctionForOnlineDataSource) super.clone());
        __clone.__changeListener = new TransformationFunctionForOnlineDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TransformationFunctionForOnlineDataSource copy()
        throws CloneNotSupportedException
    {
        TransformationFunctionForOnlineDataSource __copy = ((TransformationFunctionForOnlineDataSource) super.copy());
        __copy._transformationFunctionField = null;
        __copy.__changeListener = new TransformationFunctionForOnlineDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TransformationFunctionForOnlineDataSource __objectRef;

        private ChangeListener(TransformationFunctionForOnlineDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "transformationFunction":
                    __objectRef._transformationFunctionField = null;
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
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.Fields transformationFunction() {
            return new com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.Fields(getPathComponents(), "transformationFunction");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask _transformationFunctionMask;

        ProjectionMask() {
            super(2);
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public TransformationFunctionForOnlineDataSource.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?TransformationFunctionForOnlineDataSource.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public TransformationFunctionForOnlineDataSource.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/TransformationFunctionForOnlineDataSource.pdl.")
    public static class TransformationFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}}{namespace com.linkedin.feathr.featureDataModel/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.UserDefinedFunction _userDefinedFunctionMember = null;
        private com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction _unspecifiedTransformationFunctionMember = null;
        private TransformationFunctionForOnlineDataSource.TransformationFunction.ChangeListener __changeListener = new TransformationFunctionForOnlineDataSource.TransformationFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
        private final static DataSchema MEMBER_UserDefinedFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.UserDefinedFunction");
        private final static DataSchema MEMBER_UnspecifiedTransformationFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction");

        public TransformationFunction() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public TransformationFunction(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static TransformationFunctionForOnlineDataSource.TransformationFunction create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            TransformationFunctionForOnlineDataSource.TransformationFunction newUnion = new TransformationFunctionForOnlineDataSource.TransformationFunction();
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

        public static TransformationFunctionForOnlineDataSource.TransformationFunction create(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            TransformationFunctionForOnlineDataSource.TransformationFunction newUnion = new TransformationFunctionForOnlineDataSource.TransformationFunction();
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

        public static TransformationFunctionForOnlineDataSource.TransformationFunction create(com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction value) {
            TransformationFunctionForOnlineDataSource.TransformationFunction newUnion = new TransformationFunctionForOnlineDataSource.TransformationFunction();
            newUnion.setUnspecifiedTransformationFunction(value);
            return newUnion;
        }

        public boolean isUnspecifiedTransformationFunction() {
            return memberIs("com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction");
        }

        public com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction getUnspecifiedTransformationFunction() {
            checkNotNull();
            if (_unspecifiedTransformationFunctionMember!= null) {
                return _unspecifiedTransformationFunctionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction");
            _unspecifiedTransformationFunctionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _unspecifiedTransformationFunctionMember;
        }

        public void setUnspecifiedTransformationFunction(com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction value) {
            checkNotNull();
            super._map.clear();
            _unspecifiedTransformationFunctionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction", value.data());
        }

        public static TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask createMask() {
            return new TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask();
        }

        @Override
        public TransformationFunctionForOnlineDataSource.TransformationFunction clone()
            throws CloneNotSupportedException
        {
            TransformationFunctionForOnlineDataSource.TransformationFunction __clone = ((TransformationFunctionForOnlineDataSource.TransformationFunction) super.clone());
            __clone.__changeListener = new TransformationFunctionForOnlineDataSource.TransformationFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public TransformationFunctionForOnlineDataSource.TransformationFunction copy()
            throws CloneNotSupportedException
        {
            TransformationFunctionForOnlineDataSource.TransformationFunction __copy = ((TransformationFunctionForOnlineDataSource.TransformationFunction) super.copy());
            __copy._mvelExpressionMember = null;
            __copy._unspecifiedTransformationFunctionMember = null;
            __copy._userDefinedFunctionMember = null;
            __copy.__changeListener = new TransformationFunctionForOnlineDataSource.TransformationFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final TransformationFunctionForOnlineDataSource.TransformationFunction __objectRef;

            private ChangeListener(TransformationFunctionForOnlineDataSource.TransformationFunction reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.MvelExpression":
                        __objectRef._mvelExpressionMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction":
                        __objectRef._unspecifiedTransformationFunctionMember = null;
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

            public com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields UserDefinedFunction() {
                return new com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.UserDefinedFunction");
            }

            public com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.Fields UnspecifiedTransformationFunction() {
                return new com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _MvelExpressionMask;
            private com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask _UserDefinedFunctionMask;
            private com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask _UnspecifiedTransformationFunctionMask;

            ProjectionMask() {
                super(4);
            }

            public TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask withUserDefinedFunction(Function<com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask> nestedMask) {
                _UserDefinedFunctionMask = nestedMask.apply(((_UserDefinedFunctionMask == null)?com.linkedin.feathr.featureDataModel.UserDefinedFunction.createMask():_UserDefinedFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UserDefinedFunction", _UserDefinedFunctionMask.getDataMap());
                return this;
            }

            public TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask withUnspecifiedTransformationFunction(Function<com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask> nestedMask) {
                _UnspecifiedTransformationFunctionMask = nestedMask.apply(((_UnspecifiedTransformationFunctionMask == null)?com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.createMask():_UnspecifiedTransformationFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction", _UnspecifiedTransformationFunctionMask.getDataMap());
                return this;
            }

        }

    }

}
