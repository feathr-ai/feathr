
package com.linkedin.feathr.featureDataModel;

import java.util.List;
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
 * Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/KeyPlaceholder.pdl.")
public class KeyPlaceholder
    extends RecordTemplate
{

    private final static KeyPlaceholder.Fields _fields = new KeyPlaceholder.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}", SchemaFormatType.PDL));
    private String _keyPlaceholderRefField = null;
    private KeyPlaceholder.ChangeListener __changeListener = new KeyPlaceholder.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholderRef = SCHEMA.getField("keyPlaceholderRef");

    public KeyPlaceholder() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public KeyPlaceholder(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static KeyPlaceholder.Fields fields() {
        return _fields;
    }

    public static KeyPlaceholder.ProjectionMask createMask() {
        return new KeyPlaceholder.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholderRef
     * 
     * @see KeyPlaceholder.Fields#keyPlaceholderRef
     */
    public boolean hasKeyPlaceholderRef() {
        if (_keyPlaceholderRefField!= null) {
            return true;
        }
        return super._map.containsKey("keyPlaceholderRef");
    }

    /**
     * Remover for keyPlaceholderRef
     * 
     * @see KeyPlaceholder.Fields#keyPlaceholderRef
     */
    public void removeKeyPlaceholderRef() {
        super._map.remove("keyPlaceholderRef");
    }

    /**
     * Getter for keyPlaceholderRef
     * 
     * @see KeyPlaceholder.Fields#keyPlaceholderRef
     */
    public String getKeyPlaceholderRef(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeyPlaceholderRef();
            case DEFAULT:
            case NULL:
                if (_keyPlaceholderRefField!= null) {
                    return _keyPlaceholderRefField;
                } else {
                    Object __rawValue = super._map.get("keyPlaceholderRef");
                    _keyPlaceholderRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _keyPlaceholderRefField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyPlaceholderRef
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see KeyPlaceholder.Fields#keyPlaceholderRef
     */
    @Nonnull
    public String getKeyPlaceholderRef() {
        if (_keyPlaceholderRefField!= null) {
            return _keyPlaceholderRefField;
        } else {
            Object __rawValue = super._map.get("keyPlaceholderRef");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keyPlaceholderRef");
            }
            _keyPlaceholderRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _keyPlaceholderRefField;
        }
    }

    /**
     * Setter for keyPlaceholderRef
     * 
     * @see KeyPlaceholder.Fields#keyPlaceholderRef
     */
    public KeyPlaceholder setKeyPlaceholderRef(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholderRef(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholderRef of com.linkedin.feathr.featureDataModel.KeyPlaceholder");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRef", value);
                    _keyPlaceholderRefField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyPlaceholderRef();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRef", value);
                    _keyPlaceholderRefField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRef", value);
                    _keyPlaceholderRefField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyPlaceholderRef
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see KeyPlaceholder.Fields#keyPlaceholderRef
     */
    public KeyPlaceholder setKeyPlaceholderRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholderRef of com.linkedin.feathr.featureDataModel.KeyPlaceholder to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRef", value);
            _keyPlaceholderRefField = value;
        }
        return this;
    }

    @Override
    public KeyPlaceholder clone()
        throws CloneNotSupportedException
    {
        KeyPlaceholder __clone = ((KeyPlaceholder) super.clone());
        __clone.__changeListener = new KeyPlaceholder.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public KeyPlaceholder copy()
        throws CloneNotSupportedException
    {
        KeyPlaceholder __copy = ((KeyPlaceholder) super.copy());
        __copy._keyPlaceholderRefField = null;
        __copy.__changeListener = new KeyPlaceholder.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final KeyPlaceholder __objectRef;

        private ChangeListener(KeyPlaceholder reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyPlaceholderRef":
                    __objectRef._keyPlaceholderRefField = null;
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
         * Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.
         * 
         */
        public PathSpec keyPlaceholderRef() {
            return new PathSpec(getPathComponents(), "keyPlaceholderRef");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.
         * 
         */
        public KeyPlaceholder.ProjectionMask withKeyPlaceholderRef() {
            getDataMap().put("keyPlaceholderRef", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
