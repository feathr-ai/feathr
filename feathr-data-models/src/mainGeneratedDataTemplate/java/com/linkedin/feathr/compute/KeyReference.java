
package com.linkedin.feathr.compute;

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
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/KeyReference.pdl.")
public class KeyReference
    extends RecordTemplate
{

    private final static KeyReference.Fields _fields = new KeyReference.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record KeyReference{position:int}", SchemaFormatType.PDL));
    private Integer _positionField = null;
    private KeyReference.ChangeListener __changeListener = new KeyReference.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Position = SCHEMA.getField("position");

    public KeyReference() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public KeyReference(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static KeyReference.Fields fields() {
        return _fields;
    }

    public static KeyReference.ProjectionMask createMask() {
        return new KeyReference.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for position
     * 
     * @see KeyReference.Fields#position
     */
    public boolean hasPosition() {
        if (_positionField!= null) {
            return true;
        }
        return super._map.containsKey("position");
    }

    /**
     * Remover for position
     * 
     * @see KeyReference.Fields#position
     */
    public void removePosition() {
        super._map.remove("position");
    }

    /**
     * Getter for position
     * 
     * @see KeyReference.Fields#position
     */
    public Integer getPosition(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getPosition();
            case DEFAULT:
            case NULL:
                if (_positionField!= null) {
                    return _positionField;
                } else {
                    Object __rawValue = super._map.get("position");
                    _positionField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _positionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for position
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see KeyReference.Fields#position
     */
    @Nonnull
    public Integer getPosition() {
        if (_positionField!= null) {
            return _positionField;
        } else {
            Object __rawValue = super._map.get("position");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("position");
            }
            _positionField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _positionField;
        }
    }

    /**
     * Setter for position
     * 
     * @see KeyReference.Fields#position
     */
    public KeyReference setPosition(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setPosition(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field position of com.linkedin.feathr.compute.KeyReference");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "position", DataTemplateUtil.coerceIntInput(value));
                    _positionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removePosition();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "position", DataTemplateUtil.coerceIntInput(value));
                    _positionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "position", DataTemplateUtil.coerceIntInput(value));
                    _positionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for position
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see KeyReference.Fields#position
     */
    public KeyReference setPosition(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field position of com.linkedin.feathr.compute.KeyReference to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "position", DataTemplateUtil.coerceIntInput(value));
            _positionField = value;
        }
        return this;
    }

    /**
     * Setter for position
     * 
     * @see KeyReference.Fields#position
     */
    public KeyReference setPosition(int value) {
        CheckedUtil.putWithoutChecking(super._map, "position", DataTemplateUtil.coerceIntInput(value));
        _positionField = value;
        return this;
    }

    @Override
    public KeyReference clone()
        throws CloneNotSupportedException
    {
        KeyReference __clone = ((KeyReference) super.clone());
        __clone.__changeListener = new KeyReference.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public KeyReference copy()
        throws CloneNotSupportedException
    {
        KeyReference __copy = ((KeyReference) super.copy());
        __copy._positionField = null;
        __copy.__changeListener = new KeyReference.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final KeyReference __objectRef;

        private ChangeListener(KeyReference reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "position":
                    __objectRef._positionField = null;
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

        public PathSpec position() {
            return new PathSpec(getPathComponents(), "position");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        public KeyReference.ProjectionMask withPosition() {
            getDataMap().put("position", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
