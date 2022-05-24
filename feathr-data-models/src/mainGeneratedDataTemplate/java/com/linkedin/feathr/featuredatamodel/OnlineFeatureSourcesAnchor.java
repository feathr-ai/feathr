
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
 * Represents an online anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/OnlineFeatureSourcesAnchor.pdl.")
public class OnlineFeatureSourcesAnchor
    extends RecordTemplate
{

    private final static OnlineFeatureSourcesAnchor.Fields _fields = new OnlineFeatureSourcesAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.*/@OnlineAnchor,record OnlineFeatureSourcesAnchor includes/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines one or multiple other features as source.*/source:array[/**This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.*/record FeatureSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**This is the unique id for the MlFeatureVersionEntity used as the source.*/urn:{namespace com.linkedin.frame.common/**Standardized MLFeature identifier.*/@java.class=\"com.linkedin.frame.common.urn.MlFeatureVersionUrn\"typeref MlFeatureVersionUrn=string}/**A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).*/alias:optional string/**Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRefs:array[KeyPlaceholderRef]=[]}]/**Defines the supported transformation logic in online environment (eg. MVEL) to produce feature value from feature sources.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}", SchemaFormatType.PDL));
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private FeatureSourceArray _sourceField = null;
    private OnlineFeatureSourcesAnchor.TransformationFunction _transformationFunctionField = null;
    private OnlineFeatureSourcesAnchor.ChangeListener __changeListener = new OnlineFeatureSourcesAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_Source = SCHEMA.getField("source");
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public OnlineFeatureSourcesAnchor() {
        super(new DataMap(4, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public OnlineFeatureSourcesAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static OnlineFeatureSourcesAnchor.Fields fields() {
        return _fields;
    }

    public static OnlineFeatureSourcesAnchor.ProjectionMask createMask() {
        return new OnlineFeatureSourcesAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see OnlineFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see OnlineFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see OnlineFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see OnlineFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see OnlineFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public OnlineFeatureSourcesAnchor setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor");
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
     * @see OnlineFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public OnlineFeatureSourcesAnchor setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for source
     * 
     * @see OnlineFeatureSourcesAnchor.Fields#source
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
     * @see OnlineFeatureSourcesAnchor.Fields#source
     */
    public void removeSource() {
        super._map.remove("source");
    }

    /**
     * Getter for source
     * 
     * @see OnlineFeatureSourcesAnchor.Fields#source
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
     * @see OnlineFeatureSourcesAnchor.Fields#source
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
     * @see OnlineFeatureSourcesAnchor.Fields#source
     */
    public OnlineFeatureSourcesAnchor setSource(FeatureSourceArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSource(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field source of com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor");
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
     * @see OnlineFeatureSourcesAnchor.Fields#source
     */
    public OnlineFeatureSourcesAnchor setSource(
        @Nonnull
        FeatureSourceArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field source of com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "source", value.data());
            _sourceField = value;
        }
        return this;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see OnlineFeatureSourcesAnchor.Fields#transformationFunction
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
     * @see OnlineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see OnlineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public OnlineFeatureSourcesAnchor.TransformationFunction getTransformationFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTransformationFunction();
            case DEFAULT:
            case NULL:
                if (_transformationFunctionField!= null) {
                    return _transformationFunctionField;
                } else {
                    Object __rawValue = super._map.get("transformationFunction");
                    _transformationFunctionField = ((__rawValue == null)?null:new OnlineFeatureSourcesAnchor.TransformationFunction(__rawValue));
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
     * @see OnlineFeatureSourcesAnchor.Fields#transformationFunction
     */
    @Nonnull
    public OnlineFeatureSourcesAnchor.TransformationFunction getTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return _transformationFunctionField;
        } else {
            Object __rawValue = super._map.get("transformationFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("transformationFunction");
            }
            _transformationFunctionField = ((__rawValue == null)?null:new OnlineFeatureSourcesAnchor.TransformationFunction(__rawValue));
            return _transformationFunctionField;
        }
    }

    /**
     * Setter for transformationFunction
     * 
     * @see OnlineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public OnlineFeatureSourcesAnchor setTransformationFunction(OnlineFeatureSourcesAnchor.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor");
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
     * @see OnlineFeatureSourcesAnchor.Fields#transformationFunction
     */
    public OnlineFeatureSourcesAnchor setTransformationFunction(
        @Nonnull
        OnlineFeatureSourcesAnchor.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    @Override
    public OnlineFeatureSourcesAnchor clone()
        throws CloneNotSupportedException
    {
        OnlineFeatureSourcesAnchor __clone = ((OnlineFeatureSourcesAnchor) super.clone());
        __clone.__changeListener = new OnlineFeatureSourcesAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public OnlineFeatureSourcesAnchor copy()
        throws CloneNotSupportedException
    {
        OnlineFeatureSourcesAnchor __copy = ((OnlineFeatureSourcesAnchor) super.copy());
        __copy._transformationFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy._sourceField = null;
        __copy.__changeListener = new OnlineFeatureSourcesAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final OnlineFeatureSourcesAnchor __objectRef;

        private ChangeListener(OnlineFeatureSourcesAnchor reference) {
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
         * Defines the supported transformation logic in online environment (eg. MVEL) to produce feature value from feature sources.
         * 
         */
        public com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.TransformationFunction.Fields transformationFunction() {
            return new com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.TransformationFunction.Fields(getPathComponents(), "transformationFunction");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask _sourceMask;
        private com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.TransformationFunction.ProjectionMask _transformationFunctionMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public OnlineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public OnlineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public OnlineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public OnlineFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
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
        public OnlineFeatureSourcesAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask> nestedMask) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?FeatureSourceArray.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            return this;
        }

        /**
         * Defines one or multiple other features as source.
         * 
         */
        public OnlineFeatureSourcesAnchor.ProjectionMask withSource() {
            _sourceMask = null;
            getDataMap().put("source", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Defines one or multiple other features as source.
         * 
         */
        public OnlineFeatureSourcesAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureSourceArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public OnlineFeatureSourcesAnchor.ProjectionMask withSource(Integer start, Integer count) {
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
         * Defines the supported transformation logic in online environment (eg. MVEL) to produce feature value from feature sources.
         * 
         */
        public OnlineFeatureSourcesAnchor.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?OnlineFeatureSourcesAnchor.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic in online environment (eg. MVEL) to produce feature value from feature sources.
         * 
         */
        public OnlineFeatureSourcesAnchor.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/OnlineFeatureSourcesAnchor.pdl.")
    public static class TransformationFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction _unspecifiedTransformationFunctionMember = null;
        private OnlineFeatureSourcesAnchor.TransformationFunction.ChangeListener __changeListener = new OnlineFeatureSourcesAnchor.TransformationFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
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

        public static OnlineFeatureSourcesAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            OnlineFeatureSourcesAnchor.TransformationFunction newUnion = new OnlineFeatureSourcesAnchor.TransformationFunction();
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

        public static OnlineFeatureSourcesAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction value) {
            OnlineFeatureSourcesAnchor.TransformationFunction newUnion = new OnlineFeatureSourcesAnchor.TransformationFunction();
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

        public static OnlineFeatureSourcesAnchor.TransformationFunction.ProjectionMask createMask() {
            return new OnlineFeatureSourcesAnchor.TransformationFunction.ProjectionMask();
        }

        @Override
        public OnlineFeatureSourcesAnchor.TransformationFunction clone()
            throws CloneNotSupportedException
        {
            OnlineFeatureSourcesAnchor.TransformationFunction __clone = ((OnlineFeatureSourcesAnchor.TransformationFunction) super.clone());
            __clone.__changeListener = new OnlineFeatureSourcesAnchor.TransformationFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public OnlineFeatureSourcesAnchor.TransformationFunction copy()
            throws CloneNotSupportedException
        {
            OnlineFeatureSourcesAnchor.TransformationFunction __copy = ((OnlineFeatureSourcesAnchor.TransformationFunction) super.copy());
            __copy._mvelExpressionMember = null;
            __copy._unspecifiedTransformationFunctionMember = null;
            __copy.__changeListener = new OnlineFeatureSourcesAnchor.TransformationFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final OnlineFeatureSourcesAnchor.TransformationFunction __objectRef;

            private ChangeListener(OnlineFeatureSourcesAnchor.TransformationFunction reference) {
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

            public com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.Fields UnspecifiedTransformationFunction() {
                return new com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _MvelExpressionMask;
            private com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask _UnspecifiedTransformationFunctionMask;

            ProjectionMask() {
                super(3);
            }

            public OnlineFeatureSourcesAnchor.TransformationFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public OnlineFeatureSourcesAnchor.TransformationFunction.ProjectionMask withUnspecifiedTransformationFunction(Function<com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.ProjectionMask> nestedMask) {
                _UnspecifiedTransformationFunctionMask = nestedMask.apply(((_UnspecifiedTransformationFunctionMask == null)?com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction.createMask():_UnspecifiedTransformationFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UnspecifiedTransformationFunction", _UnspecifiedTransformationFunctionMask.getDataMap());
                return this;
            }

        }

    }

}
