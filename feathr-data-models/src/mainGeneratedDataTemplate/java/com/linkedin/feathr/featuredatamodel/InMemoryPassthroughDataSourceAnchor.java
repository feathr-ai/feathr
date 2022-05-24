
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;


/**
 * Represents an online anchor with in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/InMemoryPassthroughDataSourceAnchor.pdl.")
public class InMemoryPassthroughDataSourceAnchor
    extends RecordTemplate
{

    private final static InMemoryPassthroughDataSourceAnchor.Fields _fields = new InMemoryPassthroughDataSourceAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request.*/@OnlineAnchor,record InMemoryPassthroughDataSourceAnchor includes/**A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor. This class defines supported transformation functions (eg. MVEL, UDF) that can be used for an online data source.*/record TransformationFunctionForOnlineDataSource{/**Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}{/**Defines an in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request. See InMemoryPassthroughDataSource for more details.*/source:/**Represents a in-memory passthrough data source. Passthrough data sources are used when the data are not from external sources such as Rest.li, Venice and Espresso. It's commonly used for contextual features that are supplied as part of an online request. See Passthrough section in go/frameonline.*/record InMemoryPassthroughDataSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**The fully qualified Java class name for the data model of passthrough (in-memory) data that is supplied as part of the online request.*/dataModel:Clazz}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction _transformationFunctionField = null;
    private InMemoryPassthroughDataSource _sourceField = null;
    private InMemoryPassthroughDataSourceAnchor.ChangeListener __changeListener = new InMemoryPassthroughDataSourceAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");
    private final static RecordDataSchema.Field FIELD_Source = SCHEMA.getField("source");

    public InMemoryPassthroughDataSourceAnchor() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public InMemoryPassthroughDataSourceAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static InMemoryPassthroughDataSourceAnchor.Fields fields() {
        return _fields;
    }

    public static InMemoryPassthroughDataSourceAnchor.ProjectionMask createMask() {
        return new InMemoryPassthroughDataSourceAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see InMemoryPassthroughDataSourceAnchor.Fields#transformationFunction
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
     * @see InMemoryPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see InMemoryPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction getTransformationFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTransformationFunction();
            case DEFAULT:
            case NULL:
                if (_transformationFunctionField!= null) {
                    return _transformationFunctionField;
                } else {
                    Object __rawValue = super._map.get("transformationFunction");
                    _transformationFunctionField = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction(__rawValue));
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
     * @see InMemoryPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    @Nonnull
    public com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction getTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return _transformationFunctionField;
        } else {
            Object __rawValue = super._map.get("transformationFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("transformationFunction");
            }
            _transformationFunctionField = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction(__rawValue));
            return _transformationFunctionField;
        }
    }

    /**
     * Setter for transformationFunction
     * 
     * @see InMemoryPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public InMemoryPassthroughDataSourceAnchor setTransformationFunction(com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor");
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
     * @see InMemoryPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public InMemoryPassthroughDataSourceAnchor setTransformationFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for source
     * 
     * @see InMemoryPassthroughDataSourceAnchor.Fields#source
     */
    public boolean hasSource() {
        if (_sourceField!= null) {
            return true;
        }
        return super._map.containsKey("source");
    }

    /**
     * Remover for source
     * 
     * @see InMemoryPassthroughDataSourceAnchor.Fields#source
     */
    public void removeSource() {
        super._map.remove("source");
    }

    /**
     * Getter for source
     * 
     * @see InMemoryPassthroughDataSourceAnchor.Fields#source
     */
    public InMemoryPassthroughDataSource getSource(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSource();
            case DEFAULT:
            case NULL:
                if (_sourceField!= null) {
                    return _sourceField;
                } else {
                    Object __rawValue = super._map.get("source");
                    _sourceField = ((__rawValue == null)?null:new InMemoryPassthroughDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _sourceField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for source
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see InMemoryPassthroughDataSourceAnchor.Fields#source
     */
    @Nonnull
    public InMemoryPassthroughDataSource getSource() {
        if (_sourceField!= null) {
            return _sourceField;
        } else {
            Object __rawValue = super._map.get("source");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("source");
            }
            _sourceField = ((__rawValue == null)?null:new InMemoryPassthroughDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sourceField;
        }
    }

    /**
     * Setter for source
     * 
     * @see InMemoryPassthroughDataSourceAnchor.Fields#source
     */
    public InMemoryPassthroughDataSourceAnchor setSource(InMemoryPassthroughDataSource value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSource(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field source of com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "source", value.data());
                    _sourceField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeSource();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "source", value.data());
                    _sourceField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "source", value.data());
                    _sourceField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for source
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see InMemoryPassthroughDataSourceAnchor.Fields#source
     */
    public InMemoryPassthroughDataSourceAnchor setSource(
        @Nonnull
        InMemoryPassthroughDataSource value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field source of com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "source", value.data());
            _sourceField = value;
        }
        return this;
    }

    @Override
    public InMemoryPassthroughDataSourceAnchor clone()
        throws CloneNotSupportedException
    {
        InMemoryPassthroughDataSourceAnchor __clone = ((InMemoryPassthroughDataSourceAnchor) super.clone());
        __clone.__changeListener = new InMemoryPassthroughDataSourceAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public InMemoryPassthroughDataSourceAnchor copy()
        throws CloneNotSupportedException
    {
        InMemoryPassthroughDataSourceAnchor __copy = ((InMemoryPassthroughDataSourceAnchor) super.copy());
        __copy._transformationFunctionField = null;
        __copy._sourceField = null;
        __copy.__changeListener = new InMemoryPassthroughDataSourceAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final InMemoryPassthroughDataSourceAnchor __objectRef;

        private ChangeListener(InMemoryPassthroughDataSourceAnchor reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "transformationFunction":
                    __objectRef._transformationFunctionField = null;
                    break;
                case "source":
                    __objectRef._sourceField = null;
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

        /**
         * Defines an in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request. See InMemoryPassthroughDataSource for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource.Fields source() {
            return new com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource.Fields(getPathComponents(), "source");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask _transformationFunctionMask;
        private com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource.ProjectionMask _sourceMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public InMemoryPassthroughDataSourceAnchor.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public InMemoryPassthroughDataSourceAnchor.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Defines an in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request. See InMemoryPassthroughDataSource for more details.
         * 
         */
        public InMemoryPassthroughDataSourceAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource.ProjectionMask, com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource.ProjectionMask> nestedMask) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?InMemoryPassthroughDataSource.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            return this;
        }

        /**
         * Defines an in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request. See InMemoryPassthroughDataSource for more details.
         * 
         */
        public InMemoryPassthroughDataSourceAnchor.ProjectionMask withSource() {
            _sourceMask = null;
            getDataMap().put("source", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
