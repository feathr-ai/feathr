
package com.linkedin.feathr.compute;

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
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/NodeReference.pdl.")
public class NodeReference
    extends RecordTemplate
{

    private final static NodeReference.Fields _fields = new NodeReference.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record NodeReference{id:typeref NodeId=int,keyReference:array[record KeyReference{position:int}]}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private KeyReferenceArray _keyReferenceField = null;
    private NodeReference.ChangeListener __changeListener = new NodeReference.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_KeyReference = SCHEMA.getField("keyReference");

    public NodeReference() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public NodeReference(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static NodeReference.Fields fields() {
        return _fields;
    }

    public static NodeReference.ProjectionMask createMask() {
        return new NodeReference.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see NodeReference.Fields#id
     */
    public boolean hasId() {
        if (_idField!= null) {
            return true;
        }
        return super._map.containsKey("id");
    }

    /**
     * Remover for id
     * 
     * @see NodeReference.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see NodeReference.Fields#id
     */
    public Integer getId(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getId();
            case DEFAULT:
            case NULL:
                if (_idField!= null) {
                    return _idField;
                } else {
                    Object __rawValue = super._map.get("id");
                    _idField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _idField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for id
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see NodeReference.Fields#id
     */
    @Nonnull
    public Integer getId() {
        if (_idField!= null) {
            return _idField;
        } else {
            Object __rawValue = super._map.get("id");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("id");
            }
            _idField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _idField;
        }
    }

    /**
     * Setter for id
     * 
     * @see NodeReference.Fields#id
     */
    public NodeReference setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.NodeReference");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
                    _idField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeId();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
                    _idField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
                    _idField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see NodeReference.Fields#id
     */
    public NodeReference setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.NodeReference to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see NodeReference.Fields#id
     */
    public NodeReference setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for keyReference
     * 
     * @see NodeReference.Fields#keyReference
     */
    public boolean hasKeyReference() {
        if (_keyReferenceField!= null) {
            return true;
        }
        return super._map.containsKey("keyReference");
    }

    /**
     * Remover for keyReference
     * 
     * @see NodeReference.Fields#keyReference
     */
    public void removeKeyReference() {
        super._map.remove("keyReference");
    }

    /**
     * Getter for keyReference
     * 
     * @see NodeReference.Fields#keyReference
     */
    public KeyReferenceArray getKeyReference(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeyReference();
            case DEFAULT:
            case NULL:
                if (_keyReferenceField!= null) {
                    return _keyReferenceField;
                } else {
                    Object __rawValue = super._map.get("keyReference");
                    _keyReferenceField = ((__rawValue == null)?null:new KeyReferenceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keyReferenceField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyReference
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see NodeReference.Fields#keyReference
     */
    @Nonnull
    public KeyReferenceArray getKeyReference() {
        if (_keyReferenceField!= null) {
            return _keyReferenceField;
        } else {
            Object __rawValue = super._map.get("keyReference");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keyReference");
            }
            _keyReferenceField = ((__rawValue == null)?null:new KeyReferenceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keyReferenceField;
        }
    }

    /**
     * Setter for keyReference
     * 
     * @see NodeReference.Fields#keyReference
     */
    public NodeReference setKeyReference(KeyReferenceArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyReference(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyReference of com.linkedin.feathr.compute.NodeReference");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyReference", value.data());
                    _keyReferenceField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyReference();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyReference", value.data());
                    _keyReferenceField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyReference", value.data());
                    _keyReferenceField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyReference
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see NodeReference.Fields#keyReference
     */
    public NodeReference setKeyReference(
        @Nonnull
        KeyReferenceArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyReference of com.linkedin.feathr.compute.NodeReference to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyReference", value.data());
            _keyReferenceField = value;
        }
        return this;
    }

    @Override
    public NodeReference clone()
        throws CloneNotSupportedException
    {
        NodeReference __clone = ((NodeReference) super.clone());
        __clone.__changeListener = new NodeReference.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public NodeReference copy()
        throws CloneNotSupportedException
    {
        NodeReference __copy = ((NodeReference) super.copy());
        __copy._keyReferenceField = null;
        __copy._idField = null;
        __copy.__changeListener = new NodeReference.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final NodeReference __objectRef;

        private ChangeListener(NodeReference reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyReference":
                    __objectRef._keyReferenceField = null;
                    break;
                case "id":
                    __objectRef._idField = null;
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

        public PathSpec id() {
            return new PathSpec(getPathComponents(), "id");
        }

        public com.linkedin.feathr.compute.KeyReferenceArray.Fields keyReference() {
            return new com.linkedin.feathr.compute.KeyReferenceArray.Fields(getPathComponents(), "keyReference");
        }

        public PathSpec keyReference(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "keyReference");
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

        private com.linkedin.feathr.compute.KeyReferenceArray.ProjectionMask _keyReferenceMask;

        ProjectionMask() {
            super(3);
        }

        public NodeReference.ProjectionMask withId() {
            getDataMap().put("id", MaskMap.POSITIVE_MASK);
            return this;
        }

        public NodeReference.ProjectionMask withKeyReference(Function<com.linkedin.feathr.compute.KeyReferenceArray.ProjectionMask, com.linkedin.feathr.compute.KeyReferenceArray.ProjectionMask> nestedMask) {
            _keyReferenceMask = nestedMask.apply(((_keyReferenceMask == null)?KeyReferenceArray.createMask():_keyReferenceMask));
            getDataMap().put("keyReference", _keyReferenceMask.getDataMap());
            return this;
        }

        public NodeReference.ProjectionMask withKeyReference() {
            _keyReferenceMask = null;
            getDataMap().put("keyReference", MaskMap.POSITIVE_MASK);
            return this;
        }

        public NodeReference.ProjectionMask withKeyReference(Function<com.linkedin.feathr.compute.KeyReferenceArray.ProjectionMask, com.linkedin.feathr.compute.KeyReferenceArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _keyReferenceMask = nestedMask.apply(((_keyReferenceMask == null)?KeyReferenceArray.createMask():_keyReferenceMask));
            getDataMap().put("keyReference", _keyReferenceMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("keyReference").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyReference").put("$count", count);
            }
            return this;
        }

        public NodeReference.ProjectionMask withKeyReference(Integer start, Integer count) {
            _keyReferenceMask = null;
            getDataMap().put("keyReference", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("keyReference").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyReference").put("$count", count);
            }
            return this;
        }

    }

}
