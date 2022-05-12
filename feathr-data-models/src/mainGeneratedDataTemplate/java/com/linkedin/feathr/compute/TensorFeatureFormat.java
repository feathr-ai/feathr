
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
 * Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in Frame, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/TensorFeatureFormat.pdl.")
public class TensorFeatureFormat
    extends RecordTemplate
{

    private final static TensorFeatureFormat.Fields _fields = new TensorFeatureFormat.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in Frame, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in Frame and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and Frame.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and Frame.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime. TODO(PROML): TODO(PROML-10173): To decide whether to use -1 or optional to indicate unknown shape in the week of 08/10/2020.*/shape:optional int}]}", SchemaFormatType.PDL));
    private TensorCategory _tensorCategoryField = null;
    private ValueType _valueTypeField = null;
    private DimensionArray _dimensionsField = null;
    private TensorFeatureFormat.ChangeListener __changeListener = new TensorFeatureFormat.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TensorCategory = SCHEMA.getField("tensorCategory");
    private final static RecordDataSchema.Field FIELD_ValueType = SCHEMA.getField("valueType");
    private final static RecordDataSchema.Field FIELD_Dimensions = SCHEMA.getField("dimensions");

    public TensorFeatureFormat() {
        super(new DataMap(4, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public TensorFeatureFormat(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TensorFeatureFormat.Fields fields() {
        return _fields;
    }

    public static TensorFeatureFormat.ProjectionMask createMask() {
        return new TensorFeatureFormat.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for tensorCategory
     * 
     * @see TensorFeatureFormat.Fields#tensorCategory
     */
    public boolean hasTensorCategory() {
        if (_tensorCategoryField!= null) {
            return true;
        }
        return super._map.containsKey("tensorCategory");
    }

    /**
     * Remover for tensorCategory
     * 
     * @see TensorFeatureFormat.Fields#tensorCategory
     */
    public void removeTensorCategory() {
        super._map.remove("tensorCategory");
    }

    /**
     * Getter for tensorCategory
     * 
     * @see TensorFeatureFormat.Fields#tensorCategory
     */
    public TensorCategory getTensorCategory(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTensorCategory();
            case DEFAULT:
            case NULL:
                if (_tensorCategoryField!= null) {
                    return _tensorCategoryField;
                } else {
                    Object __rawValue = super._map.get("tensorCategory");
                    _tensorCategoryField = DataTemplateUtil.coerceEnumOutput(__rawValue, TensorCategory.class, TensorCategory.$UNKNOWN);
                    return _tensorCategoryField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for tensorCategory
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TensorFeatureFormat.Fields#tensorCategory
     */
    @Nonnull
    public TensorCategory getTensorCategory() {
        if (_tensorCategoryField!= null) {
            return _tensorCategoryField;
        } else {
            Object __rawValue = super._map.get("tensorCategory");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("tensorCategory");
            }
            _tensorCategoryField = DataTemplateUtil.coerceEnumOutput(__rawValue, TensorCategory.class, TensorCategory.$UNKNOWN);
            return _tensorCategoryField;
        }
    }

    /**
     * Setter for tensorCategory
     * 
     * @see TensorFeatureFormat.Fields#tensorCategory
     */
    public TensorFeatureFormat setTensorCategory(TensorCategory value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTensorCategory(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field tensorCategory of com.linkedin.feathr.compute.TensorFeatureFormat");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tensorCategory", value.name());
                    _tensorCategoryField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTensorCategory();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tensorCategory", value.name());
                    _tensorCategoryField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "tensorCategory", value.name());
                    _tensorCategoryField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for tensorCategory
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TensorFeatureFormat.Fields#tensorCategory
     */
    public TensorFeatureFormat setTensorCategory(
        @Nonnull
        TensorCategory value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field tensorCategory of com.linkedin.feathr.compute.TensorFeatureFormat to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "tensorCategory", value.name());
            _tensorCategoryField = value;
        }
        return this;
    }

    /**
     * Existence checker for valueType
     * 
     * @see TensorFeatureFormat.Fields#valueType
     */
    public boolean hasValueType() {
        if (_valueTypeField!= null) {
            return true;
        }
        return super._map.containsKey("valueType");
    }

    /**
     * Remover for valueType
     * 
     * @see TensorFeatureFormat.Fields#valueType
     */
    public void removeValueType() {
        super._map.remove("valueType");
    }

    /**
     * Getter for valueType
     * 
     * @see TensorFeatureFormat.Fields#valueType
     */
    public ValueType getValueType(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getValueType();
            case DEFAULT:
            case NULL:
                if (_valueTypeField!= null) {
                    return _valueTypeField;
                } else {
                    Object __rawValue = super._map.get("valueType");
                    _valueTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, ValueType.class, ValueType.$UNKNOWN);
                    return _valueTypeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for valueType
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TensorFeatureFormat.Fields#valueType
     */
    @Nonnull
    public ValueType getValueType() {
        if (_valueTypeField!= null) {
            return _valueTypeField;
        } else {
            Object __rawValue = super._map.get("valueType");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("valueType");
            }
            _valueTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, ValueType.class, ValueType.$UNKNOWN);
            return _valueTypeField;
        }
    }

    /**
     * Setter for valueType
     * 
     * @see TensorFeatureFormat.Fields#valueType
     */
    public TensorFeatureFormat setValueType(ValueType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setValueType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field valueType of com.linkedin.feathr.compute.TensorFeatureFormat");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "valueType", value.name());
                    _valueTypeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeValueType();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "valueType", value.name());
                    _valueTypeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "valueType", value.name());
                    _valueTypeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for valueType
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TensorFeatureFormat.Fields#valueType
     */
    public TensorFeatureFormat setValueType(
        @Nonnull
        ValueType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field valueType of com.linkedin.feathr.compute.TensorFeatureFormat to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "valueType", value.name());
            _valueTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for dimensions
     * 
     * @see TensorFeatureFormat.Fields#dimensions
     */
    public boolean hasDimensions() {
        if (_dimensionsField!= null) {
            return true;
        }
        return super._map.containsKey("dimensions");
    }

    /**
     * Remover for dimensions
     * 
     * @see TensorFeatureFormat.Fields#dimensions
     */
    public void removeDimensions() {
        super._map.remove("dimensions");
    }

    /**
     * Getter for dimensions
     * 
     * @see TensorFeatureFormat.Fields#dimensions
     */
    public DimensionArray getDimensions(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDimensions();
            case DEFAULT:
            case NULL:
                if (_dimensionsField!= null) {
                    return _dimensionsField;
                } else {
                    Object __rawValue = super._map.get("dimensions");
                    _dimensionsField = ((__rawValue == null)?null:new DimensionArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _dimensionsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for dimensions
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TensorFeatureFormat.Fields#dimensions
     */
    @Nonnull
    public DimensionArray getDimensions() {
        if (_dimensionsField!= null) {
            return _dimensionsField;
        } else {
            Object __rawValue = super._map.get("dimensions");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("dimensions");
            }
            _dimensionsField = ((__rawValue == null)?null:new DimensionArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _dimensionsField;
        }
    }

    /**
     * Setter for dimensions
     * 
     * @see TensorFeatureFormat.Fields#dimensions
     */
    public TensorFeatureFormat setDimensions(DimensionArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDimensions(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field dimensions of com.linkedin.feathr.compute.TensorFeatureFormat");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dimensions", value.data());
                    _dimensionsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDimensions();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dimensions", value.data());
                    _dimensionsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dimensions", value.data());
                    _dimensionsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dimensions
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TensorFeatureFormat.Fields#dimensions
     */
    public TensorFeatureFormat setDimensions(
        @Nonnull
        DimensionArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dimensions of com.linkedin.feathr.compute.TensorFeatureFormat to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dimensions", value.data());
            _dimensionsField = value;
        }
        return this;
    }

    @Override
    public TensorFeatureFormat clone()
        throws CloneNotSupportedException
    {
        TensorFeatureFormat __clone = ((TensorFeatureFormat) super.clone());
        __clone.__changeListener = new TensorFeatureFormat.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TensorFeatureFormat copy()
        throws CloneNotSupportedException
    {
        TensorFeatureFormat __copy = ((TensorFeatureFormat) super.copy());
        __copy._tensorCategoryField = null;
        __copy._valueTypeField = null;
        __copy._dimensionsField = null;
        __copy.__changeListener = new TensorFeatureFormat.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TensorFeatureFormat __objectRef;

        private ChangeListener(TensorFeatureFormat reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "tensorCategory":
                    __objectRef._tensorCategoryField = null;
                    break;
                case "valueType":
                    __objectRef._valueTypeField = null;
                    break;
                case "dimensions":
                    __objectRef._dimensionsField = null;
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
         * Type of the tensor, for example, dense tensor.
         * 
         */
        public PathSpec tensorCategory() {
            return new PathSpec(getPathComponents(), "tensorCategory");
        }

        /**
         * Type of the value column.
         * 
         */
        public PathSpec valueType() {
            return new PathSpec(getPathComponents(), "valueType");
        }

        /**
         * A feature data can have zero or more dimensions (columns that represent keys).
         * 
         */
        public com.linkedin.feathr.compute.DimensionArray.Fields dimensions() {
            return new com.linkedin.feathr.compute.DimensionArray.Fields(getPathComponents(), "dimensions");
        }

        /**
         * A feature data can have zero or more dimensions (columns that represent keys).
         * 
         */
        public PathSpec dimensions(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "dimensions");
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

        private com.linkedin.feathr.compute.DimensionArray.ProjectionMask _dimensionsMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Type of the tensor, for example, dense tensor.
         * 
         */
        public TensorFeatureFormat.ProjectionMask withTensorCategory() {
            getDataMap().put("tensorCategory", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Type of the value column.
         * 
         */
        public TensorFeatureFormat.ProjectionMask withValueType() {
            getDataMap().put("valueType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * A feature data can have zero or more dimensions (columns that represent keys).
         * 
         */
        public TensorFeatureFormat.ProjectionMask withDimensions(Function<com.linkedin.feathr.compute.DimensionArray.ProjectionMask, com.linkedin.feathr.compute.DimensionArray.ProjectionMask> nestedMask) {
            _dimensionsMask = nestedMask.apply(((_dimensionsMask == null)?DimensionArray.createMask():_dimensionsMask));
            getDataMap().put("dimensions", _dimensionsMask.getDataMap());
            return this;
        }

        /**
         * A feature data can have zero or more dimensions (columns that represent keys).
         * 
         */
        public TensorFeatureFormat.ProjectionMask withDimensions() {
            _dimensionsMask = null;
            getDataMap().put("dimensions", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * A feature data can have zero or more dimensions (columns that represent keys).
         * 
         */
        public TensorFeatureFormat.ProjectionMask withDimensions(Function<com.linkedin.feathr.compute.DimensionArray.ProjectionMask, com.linkedin.feathr.compute.DimensionArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _dimensionsMask = nestedMask.apply(((_dimensionsMask == null)?DimensionArray.createMask():_dimensionsMask));
            getDataMap().put("dimensions", _dimensionsMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("dimensions").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("dimensions").put("$count", count);
            }
            return this;
        }

        /**
         * A feature data can have zero or more dimensions (columns that represent keys).
         * 
         */
        public TensorFeatureFormat.ProjectionMask withDimensions(Integer start, Integer count) {
            _dimensionsMask = null;
            getDataMap().put("dimensions", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("dimensions").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("dimensions").put("$count", count);
            }
            return this;
        }

    }

}
