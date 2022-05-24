
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
 * Represents an offline anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/OfflineFeatureSourcesAnchor.pdl.")
public class OfflineFeatureSourcesAnchor
    extends RecordTemplate
{

    private final static OfflineFeatureSourcesAnchor.Fields _fields = new OfflineFeatureSourcesAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents an offline anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.*/@OfflineAnchor,record OfflineFeatureSourcesAnchor includes/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines one or multiple other features as source.*/source:array[/**This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.*/record FeatureSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**This is the unique id for the MlFeatureVersionEntity used as the source.*/urn:{namespace com.linkedin.frame.common/**Standardized MLFeature identifier.*/@java.class=\"com.linkedin.frame.common.urn.MlFeatureVersionUrn\"typeref MlFeatureVersionUrn=string}/**A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).*/alias:optional string/**Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRefs:array[KeyPlaceholderRef]=[]}]/**Defines the supported transformation logic in offline environment (eg. MVEL, Spark SQL, UDF) to produce feature value from feature sources.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}", SchemaFormatType.PDL));
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private FeatureSourceArray _sourceField = null;
    private OfflineFeatureSourcesAnchor.TransformationFunction _transformationFunctionField = null;
    private OfflineFeatureSourcesAnchor.ChangeListener __changeListener = new OfflineFeatureSourcesAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_Source = SCHEMA.getField("source");
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public OfflineFeatureSourcesAnchor() {
        super(new DataMap(4, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public OfflineFeatureSourcesAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static OfflineFeatureSourcesAnchor.Fields fields() {
        return _fields;
    }

    public static OfflineFeatureSourcesAnchor.ProjectionMask createMask() {
        return new OfflineFeatureSourcesAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see OfflineFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see OfflineFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see OfflineFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public OfflineFeatureSourcesAnchor setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor");
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
     * @see OfflineFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public OfflineFeatureSourcesAnchor setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for source
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#source
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
     * @see OfflineFeatureSourcesAnchor.Fields#source
     */
    public void removeSource() {
        super._map.remove("source");
    }

    /**
     * Getter for source
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#source
     */
    public FeatureSourceArray getSource(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSource();
            case DEFAULT:
            case NULL:
                if (_sourceField!= null) {
                    return _sourceField;
                } else {
                    Object __rawValue = super._map.get("source");
                    _sourceField = ((__rawValue == null)?null:new FeatureSourceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
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
     * @see OfflineFeatureSourcesAnchor.Fields#source
     */
    @Nonnull
    public FeatureSourceArray getSource() {
        if (_sourceField!= null) {
            return _sourceField;
        } else {
            Object __rawValue = super._map.get("source");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("source");
            }
            _sourceField = ((__rawValue == null)?null:new FeatureSourceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _sourceField;
        }
    }

    /**
     * Setter for source
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#source
     */
    public OfflineFeatureSourcesAnchor setSource(FeatureSourceArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSource(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field source of com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor");
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
     * @see OfflineFeatureSourcesAnchor.Fields#source
     */
    public OfflineFeatureSourcesAnchor setSource(
        @Nonnull
        FeatureSourceArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field source of com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "source", value.data());
            _sourceField = value;
        }
        return this;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#transformationFunction
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
     * @see OfflineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public OfflineFeatureSourcesAnchor.TransformationFunction getTransformationFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTransformationFunction();
            case DEFAULT:
            case NULL:
                if (_transformationFunctionField!= null) {
                    return _transformationFunctionField;
                } else {
                    Object __rawValue = super._map.get("transformationFunction");
                    _transformationFunctionField = ((__rawValue == null)?null:new OfflineFeatureSourcesAnchor.TransformationFunction(__rawValue));
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
     * @see OfflineFeatureSourcesAnchor.Fields#transformationFunction
     */
    @Nonnull
    public OfflineFeatureSourcesAnchor.TransformationFunction getTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return _transformationFunctionField;
        } else {
            Object __rawValue = super._map.get("transformationFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("transformationFunction");
            }
            _transformationFunctionField = ((__rawValue == null)?null:new OfflineFeatureSourcesAnchor.TransformationFunction(__rawValue));
            return _transformationFunctionField;
        }
    }

    /**
     * Setter for transformationFunction
     * 
     * @see OfflineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public OfflineFeatureSourcesAnchor setTransformationFunction(OfflineFeatureSourcesAnchor.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor");
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
     * @see OfflineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public OfflineFeatureSourcesAnchor setTransformationFunction(
        @Nonnull
        OfflineFeatureSourcesAnchor.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    @Override
    public OfflineFeatureSourcesAnchor clone()
        throws CloneNotSupportedException
    {
        OfflineFeatureSourcesAnchor __clone = ((OfflineFeatureSourcesAnchor) super.clone());
        __clone.__changeListener = new OfflineFeatureSourcesAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public OfflineFeatureSourcesAnchor copy()
        throws CloneNotSupportedException
    {
        OfflineFeatureSourcesAnchor __copy = ((OfflineFeatureSourcesAnchor) super.copy());
        __copy._transformationFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy._sourceField = null;
        __copy.__changeListener = new OfflineFeatureSourcesAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final OfflineFeatureSourcesAnchor __objectRef;

        private ChangeListener(OfflineFeatureSourcesAnchor reference) {
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
         * Defines one or multiple other features as source.
         * 
         */
        public com.linkedin.feathr.featureDataModel.FeatureSourceArray.Fields source() {
            return new com.linkedin.feathr.featureDataModel.FeatureSourceArray.Fields(getPathComponents(), "source");
        }

        /**
         * Defines one or multiple other features as source.
         * 
         */
        public PathSpec source(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "source");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Defines the supported transformation logic in offline environment (eg. MVEL, Spark SQL, UDF) to produce feature value from feature sources.
         * 
         */
        public com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.TransformationFunction.Fields transformationFunction() {
            return new com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.TransformationFunction.Fields(getPathComponents(), "transformationFunction");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask _sourceMask;
        private com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask _transformationFunctionMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public OfflineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
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
         * Defines one or multiple other features as source.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask> nestedMask) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?FeatureSourceArray.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            return this;
        }

        /**
         * Defines one or multiple other features as source.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withSource() {
            _sourceMask = null;
            getDataMap().put("source", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Defines one or multiple other features as source.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?FeatureSourceArray.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("source").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("source").put("$count", count);
            }
            return this;
        }

        /**
         * Defines one or multiple other features as source.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withSource(Integer start, Integer count) {
            _sourceMask = null;
            getDataMap().put("source", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("source").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("source").put("$count", count);
            }
            return this;
        }

        /**
         * Defines the supported transformation logic in offline environment (eg. MVEL, Spark SQL, UDF) to produce feature value from feature sources.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?OfflineFeatureSourcesAnchor.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic in offline environment (eg. MVEL, Spark SQL, UDF) to produce feature value from feature sources.
         * 
         */
        public OfflineFeatureSourcesAnchor.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/OfflineFeatureSourcesAnchor.pdl.")
    public static class TransformationFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}}{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}{namespace com.linkedin.feathr.featureDataModel/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.UserDefinedFunction _userDefinedFunctionMember = null;
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction _unspecifiedTransformationFunctionMember = null;
        private OfflineFeatureSourcesAnchor.TransformationFunction.ChangeListener __changeListener = new OfflineFeatureSourcesAnchor.TransformationFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
        private final static DataSchema MEMBER_UserDefinedFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.UserDefinedFunction");
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
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

        public static OfflineFeatureSourcesAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            OfflineFeatureSourcesAnchor.TransformationFunction newUnion = new OfflineFeatureSourcesAnchor.TransformationFunction();
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

        public static OfflineFeatureSourcesAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            OfflineFeatureSourcesAnchor.TransformationFunction newUnion = new OfflineFeatureSourcesAnchor.TransformationFunction();
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

        public static OfflineFeatureSourcesAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            OfflineFeatureSourcesAnchor.TransformationFunction newUnion = new OfflineFeatureSourcesAnchor.TransformationFunction();
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

        public static OfflineFeatureSourcesAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction value) {
            OfflineFeatureSourcesAnchor.TransformationFunction newUnion = new OfflineFeatureSourcesAnchor.TransformationFunction();
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

        public static OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask createMask() {
            return new OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask();
        }

        @Override
        public OfflineFeatureSourcesAnchor.TransformationFunction clone()
            throws CloneNotSupportedException
        {
            OfflineFeatureSourcesAnchor.TransformationFunction __clone = ((OfflineFeatureSourcesAnchor.TransformationFunction) super.clone());
            __clone.__changeListener = new OfflineFeatureSourcesAnchor.TransformationFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public OfflineFeatureSourcesAnchor.TransformationFunction copy()
            throws CloneNotSupportedException
        {
            OfflineFeatureSourcesAnchor.TransformationFunction __copy = ((OfflineFeatureSourcesAnchor.TransformationFunction) super.copy());
            __copy._mvelExpressionMember = null;
            __copy._sparkSqlExpressionMember = null;
            __copy._unspecifiedTransformationFunctionMember = null;
            __copy._userDefinedFunctionMember = null;
            __copy.__changeListener = new OfflineFeatureSourcesAnchor.TransformationFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final OfflineFeatureSourcesAnchor.TransformationFunction __objectRef;

            private ChangeListener(OfflineFeatureSourcesAnchor.TransformationFunction reference) {
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

            public com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SparkSqlExpression");
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
            private com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;
            private com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask _UnspecifiedTransformationFunctionMask;

            ProjectionMask() {
                super(6);
            }

            public OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask withUserDefinedFunction(Function<com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask> nestedMask) {
                _UserDefinedFunctionMask = nestedMask.apply(((_UserDefinedFunctionMask == null)?com.linkedin.feathr.featureDataModel.UserDefinedFunction.createMask():_UserDefinedFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UserDefinedFunction", _UserDefinedFunctionMask.getDataMap());
                return this;
            }

            public OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

            public OfflineFeatureSourcesAnchor.TransformationFunction.ProjectionMask withUnspecifiedTransformationFunction(Function<com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask> nestedMask) {
                _UnspecifiedTransformationFunctionMask = nestedMask.apply(((_UnspecifiedTransformationFunctionMask == null)?com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.createMask():_UnspecifiedTransformationFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction", _UnspecifiedTransformationFunctionMask.getDataMap());
                return this;
            }

        }

    }

}
