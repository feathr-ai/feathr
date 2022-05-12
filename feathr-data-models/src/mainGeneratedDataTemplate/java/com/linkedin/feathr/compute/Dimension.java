
package com.linkedin.feathr.compute;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
 * Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Dimension.pdl.")
public class Dimension
    extends RecordTemplate
{

    private final static Dimension.Fields _fields = new Dimension.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and Frame.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime. TODO(PROML): TODO(PROML-10173): To decide whether to use -1 or optional to indicate unknown shape in the week of 08/10/2020.*/shape:optional int}", SchemaFormatType.PDL));
    private DimensionType _typeField = null;
    private Integer _shapeField = null;
    private Dimension.ChangeListener __changeListener = new Dimension.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Type = SCHEMA.getField("type");
    private final static RecordDataSchema.Field FIELD_Shape = SCHEMA.getField("shape");

    public Dimension() {
        super(new DataMap(3, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public Dimension(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Dimension.Fields fields() {
        return _fields;
    }

    public static Dimension.ProjectionMask createMask() {
        return new Dimension.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for type
     * 
     * @see Dimension.Fields#type
     */
    public boolean hasType() {
        if (_typeField!= null) {
            return true;
        }
        return super._map.containsKey("type");
    }

    /**
     * Remover for type
     * 
     * @see Dimension.Fields#type
     */
    public void removeType() {
        super._map.remove("type");
    }

    /**
     * Getter for type
     * 
     * @see Dimension.Fields#type
     */
    public DimensionType getType(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getType();
            case DEFAULT:
            case NULL:
                if (_typeField!= null) {
                    return _typeField;
                } else {
                    Object __rawValue = super._map.get("type");
                    _typeField = DataTemplateUtil.coerceEnumOutput(__rawValue, DimensionType.class, DimensionType.$UNKNOWN);
                    return _typeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for type
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Dimension.Fields#type
     */
    @Nonnull
    public DimensionType getType() {
        if (_typeField!= null) {
            return _typeField;
        } else {
            Object __rawValue = super._map.get("type");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("type");
            }
            _typeField = DataTemplateUtil.coerceEnumOutput(__rawValue, DimensionType.class, DimensionType.$UNKNOWN);
            return _typeField;
        }
    }

    /**
     * Setter for type
     * 
     * @see Dimension.Fields#type
     */
    public Dimension setType(DimensionType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field type of com.linkedin.feathr.compute.Dimension");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "type", value.name());
                    _typeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeType();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "type", value.name());
                    _typeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "type", value.name());
                    _typeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for type
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Dimension.Fields#type
     */
    public Dimension setType(
        @Nonnull
        DimensionType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field type of com.linkedin.feathr.compute.Dimension to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "type", value.name());
            _typeField = value;
        }
        return this;
    }

    /**
     * Existence checker for shape
     * 
     * @see Dimension.Fields#shape
     */
    public boolean hasShape() {
        if (_shapeField!= null) {
            return true;
        }
        return super._map.containsKey("shape");
    }

    /**
     * Remover for shape
     * 
     * @see Dimension.Fields#shape
     */
    public void removeShape() {
        super._map.remove("shape");
    }

    /**
     * Getter for shape
     * 
     * @see Dimension.Fields#shape
     */
    public Integer getShape(GetMode mode) {
        return getShape();
    }

    /**
     * Getter for shape
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Dimension.Fields#shape
     */
    @Nullable
    public Integer getShape() {
        if (_shapeField!= null) {
            return _shapeField;
        } else {
            Object __rawValue = super._map.get("shape");
            _shapeField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _shapeField;
        }
    }

    /**
     * Setter for shape
     * 
     * @see Dimension.Fields#shape
     */
    public Dimension setShape(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setShape(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeShape();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "shape", DataTemplateUtil.coerceIntInput(value));
                    _shapeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "shape", DataTemplateUtil.coerceIntInput(value));
                    _shapeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for shape
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Dimension.Fields#shape
     */
    public Dimension setShape(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field shape of com.linkedin.feathr.compute.Dimension to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "shape", DataTemplateUtil.coerceIntInput(value));
            _shapeField = value;
        }
        return this;
    }

    /**
     * Setter for shape
     * 
     * @see Dimension.Fields#shape
     */
    public Dimension setShape(int value) {
        CheckedUtil.putWithoutChecking(super._map, "shape", DataTemplateUtil.coerceIntInput(value));
        _shapeField = value;
        return this;
    }

    @Override
    public Dimension clone()
        throws CloneNotSupportedException
    {
        Dimension __clone = ((Dimension) super.clone());
        __clone.__changeListener = new Dimension.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Dimension copy()
        throws CloneNotSupportedException
    {
        Dimension __copy = ((Dimension) super.copy());
        __copy._shapeField = null;
        __copy._typeField = null;
        __copy.__changeListener = new Dimension.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Dimension __objectRef;

        private ChangeListener(Dimension reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "shape":
                    __objectRef._shapeField = null;
                    break;
                case "type":
                    __objectRef._typeField = null;
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
         * Type of the dimension in the tensor. Each dimension can have a different type.
         * 
         */
        public PathSpec type() {
            return new PathSpec(getPathComponents(), "type");
        }

        /**
         * Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime. TODO(PROML): TODO(PROML-10173): To decide whether to use -1 or optional to indicate unknown shape in the week of 08/10/2020.
         * 
         */
        public PathSpec shape() {
            return new PathSpec(getPathComponents(), "shape");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Type of the dimension in the tensor. Each dimension can have a different type.
         * 
         */
        public Dimension.ProjectionMask withType() {
            getDataMap().put("type", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime. TODO(PROML): TODO(PROML-10173): To decide whether to use -1 or optional to indicate unknown shape in the week of 08/10/2020.
         * 
         */
        public Dimension.ProjectionMask withShape() {
            getDataMap().put("shape", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
