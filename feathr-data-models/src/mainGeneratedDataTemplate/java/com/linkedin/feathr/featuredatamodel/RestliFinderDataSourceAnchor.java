
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataList;
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
 * Represents an online anchor with Rest.li finder data source, in which source data is fetched by a Rest.li finder method.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/RestliFinderDataSourceAnchor.pdl.")
public class RestliFinderDataSourceAnchor
    extends RecordTemplate
{

    private final static RestliFinderDataSourceAnchor.Fields _fields = new RestliFinderDataSourceAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Rest.li finder data source, in which source data is fetched by a Rest.li finder method.*/@OnlineAnchor,record RestliFinderDataSourceAnchor includes/**A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor. This class defines supported transformation functions (eg. MVEL, UDF) that can be used for an online data source.*/record TransformationFunctionForOnlineDataSource{/**Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. See RestliFinderDataSource for more details.*/source:/**Represents a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. For more details, refer to https://linkedin.github.io/rest.li/user_guide/restli_server#finder. Note: it is possible for a finder source to have an keyExpr for association resources since the key parts of the association key can be used as query parameters which are modeled as CompoundKey. See https://linkedin.github.io/rest.li/user_guide/restli_server#finder for more details*/record RestliFinderDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[MvelExpression]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.*/resourceName:string/**Represents the finder method name of the resource.*/finderMethod:string/**Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.*/projections:array[/**Represents a PathSpec string. PathSpec is used to specify projections in Rest.li request in order to select a subset of object contents. For more details, refer to https://linkedin.github.io/rest.li/Projections. Some examples: 'listingType', 'member:(firstName, lastName)'.*/typeref PathSpec=string]=[]/**Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#finders,companyJobCodes, the key value pair keyed by contract represents a request parameter of the companyJobCodes finder method in jobPostings resource.*/requestParameters:map[string/**Represents the value part in a Rest.li request paramater key/value pair.*/typeref RequestParameterValue=union[MvelExpression,JsonString]]={}}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction _transformationFunctionField = null;
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private RestliFinderDataSource _sourceField = null;
    private RestliFinderDataSourceAnchor.ChangeListener __changeListener = new RestliFinderDataSourceAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_Source = SCHEMA.getField("source");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public RestliFinderDataSourceAnchor() {
        super(new DataMap(4, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public RestliFinderDataSourceAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static RestliFinderDataSourceAnchor.Fields fields() {
        return _fields;
    }

    public static RestliFinderDataSourceAnchor.ProjectionMask createMask() {
        return new RestliFinderDataSourceAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see RestliFinderDataSourceAnchor.Fields#transformationFunction
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
     * @see RestliFinderDataSourceAnchor.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see RestliFinderDataSourceAnchor.Fields#transformationFunction
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
     * @see RestliFinderDataSourceAnchor.Fields#transformationFunction
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
     * @see RestliFinderDataSourceAnchor.Fields#transformationFunction
     */
    public RestliFinderDataSourceAnchor setTransformationFunction(com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor");
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
     * @see RestliFinderDataSourceAnchor.Fields#transformationFunction
     */
    public RestliFinderDataSourceAnchor setTransformationFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see RestliFinderDataSourceAnchor.Fields#keyPlaceholders
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
     * @see RestliFinderDataSourceAnchor.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see RestliFinderDataSourceAnchor.Fields#keyPlaceholders
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
     * @see RestliFinderDataSourceAnchor.Fields#keyPlaceholders
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
     * @see RestliFinderDataSourceAnchor.Fields#keyPlaceholders
     */
    public RestliFinderDataSourceAnchor setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor");
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
     * @see RestliFinderDataSourceAnchor.Fields#keyPlaceholders
     */
    public RestliFinderDataSourceAnchor setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for source
     * 
     * @see RestliFinderDataSourceAnchor.Fields#source
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
     * @see RestliFinderDataSourceAnchor.Fields#source
     */
    public void removeSource() {
        super._map.remove("source");
    }

    /**
     * Getter for source
     * 
     * @see RestliFinderDataSourceAnchor.Fields#source
     */
    public RestliFinderDataSource getSource(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSource();
            case DEFAULT:
            case NULL:
                if (_sourceField!= null) {
                    return _sourceField;
                } else {
                    Object __rawValue = super._map.get("source");
                    _sourceField = ((__rawValue == null)?null:new RestliFinderDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
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
     * @see RestliFinderDataSourceAnchor.Fields#source
     */
    @Nonnull
    public RestliFinderDataSource getSource() {
        if (_sourceField!= null) {
            return _sourceField;
        } else {
            Object __rawValue = super._map.get("source");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("source");
            }
            _sourceField = ((__rawValue == null)?null:new RestliFinderDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sourceField;
        }
    }

    /**
     * Setter for source
     * 
     * @see RestliFinderDataSourceAnchor.Fields#source
     */
    public RestliFinderDataSourceAnchor setSource(RestliFinderDataSource value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSource(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field source of com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor");
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
     * @see RestliFinderDataSourceAnchor.Fields#source
     */
    public RestliFinderDataSourceAnchor setSource(
        @Nonnull
        RestliFinderDataSource value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field source of com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "source", value.data());
            _sourceField = value;
        }
        return this;
    }

    @Override
    public RestliFinderDataSourceAnchor clone()
        throws CloneNotSupportedException
    {
        RestliFinderDataSourceAnchor __clone = ((RestliFinderDataSourceAnchor) super.clone());
        __clone.__changeListener = new RestliFinderDataSourceAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public RestliFinderDataSourceAnchor copy()
        throws CloneNotSupportedException
    {
        RestliFinderDataSourceAnchor __copy = ((RestliFinderDataSourceAnchor) super.copy());
        __copy._transformationFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy._sourceField = null;
        __copy.__changeListener = new RestliFinderDataSourceAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final RestliFinderDataSourceAnchor __objectRef;

        private ChangeListener(RestliFinderDataSourceAnchor reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "transformationFunction":
                    __objectRef._transformationFunctionField = null;
                    break;
                case "keyPlaceholders":
                    __objectRef._keyPlaceholdersField = null;
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
         * Defines a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. See RestliFinderDataSource for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.RestliFinderDataSource.Fields source() {
            return new com.linkedin.feathr.featureDataModel.RestliFinderDataSource.Fields(getPathComponents(), "source");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask _transformationFunctionMask;
        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.RestliFinderDataSource.ProjectionMask _sourceMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public RestliFinderDataSourceAnchor.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public RestliFinderDataSourceAnchor.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public RestliFinderDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public RestliFinderDataSourceAnchor.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public RestliFinderDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public RestliFinderDataSourceAnchor.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
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
         * Defines a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. See RestliFinderDataSource for more details.
         * 
         */
        public RestliFinderDataSourceAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.RestliFinderDataSource.ProjectionMask, com.linkedin.feathr.featureDataModel.RestliFinderDataSource.ProjectionMask> nestedMask) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?RestliFinderDataSource.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            return this;
        }

        /**
         * Defines a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. See RestliFinderDataSource for more details.
         * 
         */
        public RestliFinderDataSourceAnchor.ProjectionMask withSource() {
            _sourceMask = null;
            getDataMap().put("source", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
