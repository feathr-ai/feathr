
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
 * Represents a time window used in sliding window algorithms.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/Window.pdl.")
public class Window
    extends RecordTemplate
{

    private final static Window.Fields _fields = new Window.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}", SchemaFormatType.PDL));
    private Integer _sizeField = null;
    private Unit _unitField = null;
    private Window.ChangeListener __changeListener = new Window.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Size = SCHEMA.getField("size");
    private final static RecordDataSchema.Field FIELD_Unit = SCHEMA.getField("unit");

    public Window() {
        super(new DataMap(3, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public Window(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Window.Fields fields() {
        return _fields;
    }

    public static Window.ProjectionMask createMask() {
        return new Window.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for size
     * 
     * @see Window.Fields#size
     */
    public boolean hasSize() {
        if (_sizeField!= null) {
            return true;
        }
        return super._map.containsKey("size");
    }

    /**
     * Remover for size
     * 
     * @see Window.Fields#size
     */
    public void removeSize() {
        super._map.remove("size");
    }

    /**
     * Getter for size
     * 
     * @see Window.Fields#size
     */
    public Integer getSize(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSize();
            case DEFAULT:
            case NULL:
                if (_sizeField!= null) {
                    return _sizeField;
                } else {
                    Object __rawValue = super._map.get("size");
                    _sizeField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _sizeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for size
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Window.Fields#size
     */
    @Nonnull
    public Integer getSize() {
        if (_sizeField!= null) {
            return _sizeField;
        } else {
            Object __rawValue = super._map.get("size");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("size");
            }
            _sizeField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _sizeField;
        }
    }

    /**
     * Setter for size
     * 
     * @see Window.Fields#size
     */
    public Window setSize(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSize(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field size of com.linkedin.feathr.featureDataModel.Window");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "size", DataTemplateUtil.coerceIntInput(value));
                    _sizeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeSize();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "size", DataTemplateUtil.coerceIntInput(value));
                    _sizeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "size", DataTemplateUtil.coerceIntInput(value));
                    _sizeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for size
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Window.Fields#size
     */
    public Window setSize(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field size of com.linkedin.feathr.featureDataModel.Window to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "size", DataTemplateUtil.coerceIntInput(value));
            _sizeField = value;
        }
        return this;
    }

    /**
     * Setter for size
     * 
     * @see Window.Fields#size
     */
    public Window setSize(int value) {
        CheckedUtil.putWithoutChecking(super._map, "size", DataTemplateUtil.coerceIntInput(value));
        _sizeField = value;
        return this;
    }

    /**
     * Existence checker for unit
     * 
     * @see Window.Fields#unit
     */
    public boolean hasUnit() {
        if (_unitField!= null) {
            return true;
        }
        return super._map.containsKey("unit");
    }

    /**
     * Remover for unit
     * 
     * @see Window.Fields#unit
     */
    public void removeUnit() {
        super._map.remove("unit");
    }

    /**
     * Getter for unit
     * 
     * @see Window.Fields#unit
     */
    public Unit getUnit(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getUnit();
            case DEFAULT:
            case NULL:
                if (_unitField!= null) {
                    return _unitField;
                } else {
                    Object __rawValue = super._map.get("unit");
                    _unitField = DataTemplateUtil.coerceEnumOutput(__rawValue, Unit.class, Unit.$UNKNOWN);
                    return _unitField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for unit
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Window.Fields#unit
     */
    @Nonnull
    public Unit getUnit() {
        if (_unitField!= null) {
            return _unitField;
        } else {
            Object __rawValue = super._map.get("unit");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("unit");
            }
            _unitField = DataTemplateUtil.coerceEnumOutput(__rawValue, Unit.class, Unit.$UNKNOWN);
            return _unitField;
        }
    }

    /**
     * Setter for unit
     * 
     * @see Window.Fields#unit
     */
    public Window setUnit(Unit value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setUnit(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field unit of com.linkedin.feathr.featureDataModel.Window");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
                    _unitField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeUnit();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
                    _unitField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
                    _unitField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for unit
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Window.Fields#unit
     */
    public Window setUnit(
        @Nonnull
        Unit value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field unit of com.linkedin.feathr.featureDataModel.Window to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "unit", value.name());
            _unitField = value;
        }
        return this;
    }

    @Override
    public Window clone()
        throws CloneNotSupportedException
    {
        Window __clone = ((Window) super.clone());
        __clone.__changeListener = new Window.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Window copy()
        throws CloneNotSupportedException
    {
        Window __copy = ((Window) super.copy());
        __copy._unitField = null;
        __copy._sizeField = null;
        __copy.__changeListener = new Window.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Window __objectRef;

        private ChangeListener(Window reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "unit":
                    __objectRef._unitField = null;
                    break;
                case "size":
                    __objectRef._sizeField = null;
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
         * Represents the duration of the window.
         * 
         */
        public PathSpec size() {
            return new PathSpec(getPathComponents(), "size");
        }

        /**
         * Represents a unit of time.
         * 
         */
        public PathSpec unit() {
            return new PathSpec(getPathComponents(), "unit");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Represents the duration of the window.
         * 
         */
        public Window.ProjectionMask withSize() {
            getDataMap().put("size", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents a unit of time.
         * 
         */
        public Window.ProjectionMask withUnit() {
            getDataMap().put("unit", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
