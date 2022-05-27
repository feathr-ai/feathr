
package com.linkedin.feathr.fds;

import java.util.List;
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
import com.linkedin.data.template.StringArray;


/**
 * Metadata that will be present if this feature was provided by Frame.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FrameMetadata.pdl.")
@Deprecated
public class FrameMetadata
    extends RecordTemplate
{

    private final static FrameMetadata.Fields _fields = new FrameMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Metadata that will be present if this feature was provided by Frame.*/@deprecated,record FrameMetadata{/**The fully-qualified Frame featureRef string of the feature; e.g. \"namespace-feature-major-minor\"\n@see [[com.linkedin.frame.core.config.producer.common.FeatureRef]]*/featureRef:string/**The names of the join keys (\u201ckey tags\u201d) that were used when Frame joined these features.*/joinKeys:array[string]}", SchemaFormatType.PDL));
    private String _featureRefField = null;
    private StringArray _joinKeysField = null;
    private FrameMetadata.ChangeListener __changeListener = new FrameMetadata.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_FeatureRef = SCHEMA.getField("featureRef");
    private final static RecordDataSchema.Field FIELD_JoinKeys = SCHEMA.getField("joinKeys");

    public FrameMetadata() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public FrameMetadata(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FrameMetadata.Fields fields() {
        return _fields;
    }

    public static FrameMetadata.ProjectionMask createMask() {
        return new FrameMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for featureRef
     * 
     * @see FrameMetadata.Fields#featureRef
     */
    public boolean hasFeatureRef() {
        if (_featureRefField!= null) {
            return true;
        }
        return super._map.containsKey("featureRef");
    }

    /**
     * Remover for featureRef
     * 
     * @see FrameMetadata.Fields#featureRef
     */
    public void removeFeatureRef() {
        super._map.remove("featureRef");
    }

    /**
     * Getter for featureRef
     * 
     * @see FrameMetadata.Fields#featureRef
     */
    public String getFeatureRef(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFeatureRef();
            case DEFAULT:
            case NULL:
                if (_featureRefField!= null) {
                    return _featureRefField;
                } else {
                    Object __rawValue = super._map.get("featureRef");
                    _featureRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _featureRefField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for featureRef
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FrameMetadata.Fields#featureRef
     */
    @Nonnull
    public String getFeatureRef() {
        if (_featureRefField!= null) {
            return _featureRefField;
        } else {
            Object __rawValue = super._map.get("featureRef");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("featureRef");
            }
            _featureRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _featureRefField;
        }
    }

    /**
     * Setter for featureRef
     * 
     * @see FrameMetadata.Fields#featureRef
     */
    public FrameMetadata setFeatureRef(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureRef(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureRef of com.linkedin.feathr.fds.FrameMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureRef", value);
                    _featureRefField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureRef();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureRef", value);
                    _featureRefField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureRef", value);
                    _featureRefField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureRef
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FrameMetadata.Fields#featureRef
     */
    public FrameMetadata setFeatureRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureRef of com.linkedin.feathr.fds.FrameMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureRef", value);
            _featureRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for joinKeys
     * 
     * @see FrameMetadata.Fields#joinKeys
     */
    public boolean hasJoinKeys() {
        if (_joinKeysField!= null) {
            return true;
        }
        return super._map.containsKey("joinKeys");
    }

    /**
     * Remover for joinKeys
     * 
     * @see FrameMetadata.Fields#joinKeys
     */
    public void removeJoinKeys() {
        super._map.remove("joinKeys");
    }

    /**
     * Getter for joinKeys
     * 
     * @see FrameMetadata.Fields#joinKeys
     */
    public StringArray getJoinKeys(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getJoinKeys();
            case DEFAULT:
            case NULL:
                if (_joinKeysField!= null) {
                    return _joinKeysField;
                } else {
                    Object __rawValue = super._map.get("joinKeys");
                    _joinKeysField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _joinKeysField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for joinKeys
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FrameMetadata.Fields#joinKeys
     */
    @Nonnull
    public StringArray getJoinKeys() {
        if (_joinKeysField!= null) {
            return _joinKeysField;
        } else {
            Object __rawValue = super._map.get("joinKeys");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("joinKeys");
            }
            _joinKeysField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _joinKeysField;
        }
    }

    /**
     * Setter for joinKeys
     * 
     * @see FrameMetadata.Fields#joinKeys
     */
    public FrameMetadata setJoinKeys(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setJoinKeys(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field joinKeys of com.linkedin.feathr.fds.FrameMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "joinKeys", value.data());
                    _joinKeysField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeJoinKeys();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "joinKeys", value.data());
                    _joinKeysField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "joinKeys", value.data());
                    _joinKeysField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for joinKeys
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FrameMetadata.Fields#joinKeys
     */
    public FrameMetadata setJoinKeys(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field joinKeys of com.linkedin.feathr.fds.FrameMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "joinKeys", value.data());
            _joinKeysField = value;
        }
        return this;
    }

    @Override
    public FrameMetadata clone()
        throws CloneNotSupportedException
    {
        FrameMetadata __clone = ((FrameMetadata) super.clone());
        __clone.__changeListener = new FrameMetadata.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FrameMetadata copy()
        throws CloneNotSupportedException
    {
        FrameMetadata __copy = ((FrameMetadata) super.copy());
        __copy._joinKeysField = null;
        __copy._featureRefField = null;
        __copy.__changeListener = new FrameMetadata.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FrameMetadata __objectRef;

        private ChangeListener(FrameMetadata reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "joinKeys":
                    __objectRef._joinKeysField = null;
                    break;
                case "featureRef":
                    __objectRef._featureRefField = null;
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
         * The fully-qualified Frame featureRef string of the feature; e.g. "namespace-feature-major-minor"
         * @see [[com.linkedin.frame.core.config.producer.common.FeatureRef]]
         * 
         */
        public PathSpec featureRef() {
            return new PathSpec(getPathComponents(), "featureRef");
        }

        /**
         * The names of the join keys (“key tags”) that were used when Frame joined these features.
         * 
         */
        public PathSpec joinKeys() {
            return new PathSpec(getPathComponents(), "joinKeys");
        }

        /**
         * The names of the join keys (“key tags”) that were used when Frame joined these features.
         * 
         */
        public PathSpec joinKeys(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "joinKeys");
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


        ProjectionMask() {
            super(3);
        }

        /**
         * The fully-qualified Frame featureRef string of the feature; e.g. "namespace-feature-major-minor"
         * @see [[com.linkedin.frame.core.config.producer.common.FeatureRef]]
         * 
         */
        public FrameMetadata.ProjectionMask withFeatureRef() {
            getDataMap().put("featureRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The names of the join keys (“key tags”) that were used when Frame joined these features.
         * 
         */
        public FrameMetadata.ProjectionMask withJoinKeys() {
            getDataMap().put("joinKeys", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The names of the join keys (“key tags”) that were used when Frame joined these features.
         * 
         */
        public FrameMetadata.ProjectionMask withJoinKeys(Integer start, Integer count) {
            getDataMap().put("joinKeys", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("joinKeys").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("joinKeys").put("$count", count);
            }
            return this;
        }

    }

}
