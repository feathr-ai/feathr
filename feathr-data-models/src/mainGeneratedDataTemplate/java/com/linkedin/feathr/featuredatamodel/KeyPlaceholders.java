
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
import com.linkedin.data.template.SetMode;


/**
 * Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/KeyPlaceholders.pdl.")
public class KeyPlaceholders
    extends RecordTemplate
{

    private final static KeyPlaceholders.Fields _fields = new KeyPlaceholders.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}", SchemaFormatType.PDL));
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private KeyPlaceholders.ChangeListener __changeListener = new KeyPlaceholders.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public KeyPlaceholders() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public KeyPlaceholders(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static KeyPlaceholders.Fields fields() {
        return _fields;
    }

    public static KeyPlaceholders.ProjectionMask createMask() {
        return new KeyPlaceholders.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see KeyPlaceholders.Fields#keyPlaceholders
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
     * @see KeyPlaceholders.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see KeyPlaceholders.Fields#keyPlaceholders
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
     * @see KeyPlaceholders.Fields#keyPlaceholders
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
     * @see KeyPlaceholders.Fields#keyPlaceholders
     */
    public KeyPlaceholders setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.KeyPlaceholders");
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
     * @see KeyPlaceholders.Fields#keyPlaceholders
     */
    public KeyPlaceholders setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.KeyPlaceholders to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    @Override
    public KeyPlaceholders clone()
        throws CloneNotSupportedException
    {
        KeyPlaceholders __clone = ((KeyPlaceholders) super.clone());
        __clone.__changeListener = new KeyPlaceholders.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public KeyPlaceholders copy()
        throws CloneNotSupportedException
    {
        KeyPlaceholders __copy = ((KeyPlaceholders) super.copy());
        __copy._keyPlaceholdersField = null;
        __copy.__changeListener = new KeyPlaceholders.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final KeyPlaceholders __objectRef;

        private ChangeListener(KeyPlaceholders reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
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

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;

        ProjectionMask() {
            super(2);
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public KeyPlaceholders.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public KeyPlaceholders.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public KeyPlaceholders.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public KeyPlaceholders.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
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

    }

}
