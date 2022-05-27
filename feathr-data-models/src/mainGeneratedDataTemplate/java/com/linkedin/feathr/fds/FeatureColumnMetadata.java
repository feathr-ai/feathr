
package com.linkedin.feathr.fds;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.IntegerArray;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;


/**
 * FeatureColumnMetadata structure capturing metadata of columns tgat cintain features (
 * as defined in the spec). In this context, "feature columns" contain features, labels,
 * entity ids, weights and all other columns that are used in computation/training (as opposed
 * to opaque contextual columns).
 * <p/>
 * More details can be found in the specification at docs/qt_fds_spec.md
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeatureColumnMetadata.pdl.")
public class FeatureColumnMetadata
    extends RecordTemplate
{

    private final static FeatureColumnMetadata.Fields _fields = new FeatureColumnMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**FeatureColumnMetadata structure capturing metadata of columns tgat cintain features (\nas defined in the spec). In this context, \"feature columns\" contain features, labels,\nentity ids, weights and all other columns that are used in computation/training (as opposed\nto opaque contextual columns).\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeatureColumnMetadata{/**Category of the tensor (e.g. TensorCategory.DENSE, TensorCategory.SPARSE)*/tensorCategory:/**Representation type of a feature\n<p/>\nMore details can be found in the specification\nhttps://docs.google.com/document/d/12VZis-6VyQN3Ivy2rQxf-OktZqCowuTvVrsIFxwTVXU/edit#heading=h.jjuk2t7bn84*/enum TensorCategory{/**A tensor where we only store values for some positions.*/SPARSE/**A tensor where we store a value for each position.*/DENSE/**RAGGED tensors (also called nested tensors) are similar to dense tensors but have variable-length\ndimensions.*/RAGGED}/**The shape (sometimes called the \u201csize\u201d or \u201cdense shape\u201d) of the tensor.\nGiven as a list of integers. The first element gives the size of the first dimension\nin the tensor, the second element gives the size of the second dimension, and so on.\nThe length of the tensorShape array is the number of dimensions in the tensor, also\ncalled the tensor's rank.\nThe data schema must match the tensor\u2019s rank as indicated in the tensorShape array;\nwhich in case of DENSE means that the level of nesting of the array must\nmatch the rank, and in case of SPARSE there must be the same number of\nindices fields as the rank.\nDimensions are allowed to have unknown size. Elements in the tensorShape array may be\nset as (-1) to indicate unknown.\nFor any dimension having dimensionType of string, that dimension\u2019s size must be set\nto (-1) (unknown).*/tensorShape:array[int]/**List of the types for each dimension. Given as an array of strings.\nFor DENSE features, must contain only DimensionType.INT. For SPARSE features,\nmust correspond to the data types of the corresponding indices fields in the data schema*/dimensionTypes:array[/**Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of\nthat dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,\nelements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2][\"Germany\"]).*/enum DimensionType{/**The tensor dimension is of type integer.*/INT/**The tensor dimension is of type long.*/LONG/**The tensor dimension is of type string.*/STRING}]/**The value type (e.g. ValueType.INT, ValueType.FLOAT)*/valueType:/**The value type for a feature (column) of FDS.*/enum ValueType{/**The value is an integer.*/INT/**The value is a long.*/LONG/**The value is a float.*/FLOAT/**The value is a double.*/DOUBLE/**The value is a boolean.*/BOOLEAN/**The value is a string.*/STRING/**The value is a byte-array.*/BYTES}/**Optional metadata that helps with linkage of a feature. See the documentation\non FeatureColumnLinkageMetadata for more info.*/linkageMetadata:optional/**Metadata that helps with linkage of a feature. That is, metadata\nneeded to link the column name to an access method for getting the contents.\nFor example, if a column is populated using a frame feature and then a model\nis trained using that feature/column, the external feature name in the model\nwill be the same as the column name. Linkage metadata allows us to link/bind\nthat external feature of the model with some value during inference. For example,\nif the FDS column corresponded to a frame feature, linkage metadata will contain\nthe unique identifier of that frame feature. This infomation can then be encoded\nin the model bundle and used to bind that external feature to a concrete value\nat inference time.*/record FeatureColumnLinkageMetadata{/** See above **/featureLinkageMetadata:union[/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}]}}", SchemaFormatType.PDL));
    private TensorCategory _tensorCategoryField = null;
    private IntegerArray _tensorShapeField = null;
    private DimensionTypeArray _dimensionTypesField = null;
    private ValueType _valueTypeField = null;
    private FeatureColumnLinkageMetadata _linkageMetadataField = null;
    private FeatureColumnMetadata.ChangeListener __changeListener = new FeatureColumnMetadata.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TensorCategory = SCHEMA.getField("tensorCategory");
    private final static RecordDataSchema.Field FIELD_TensorShape = SCHEMA.getField("tensorShape");
    private final static RecordDataSchema.Field FIELD_DimensionTypes = SCHEMA.getField("dimensionTypes");
    private final static RecordDataSchema.Field FIELD_ValueType = SCHEMA.getField("valueType");
    private final static RecordDataSchema.Field FIELD_LinkageMetadata = SCHEMA.getField("linkageMetadata");

    public FeatureColumnMetadata() {
        super(new DataMap(7, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public FeatureColumnMetadata(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeatureColumnMetadata.Fields fields() {
        return _fields;
    }

    public static FeatureColumnMetadata.ProjectionMask createMask() {
        return new FeatureColumnMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for tensorCategory
     * 
     * @see FeatureColumnMetadata.Fields#tensorCategory
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
     * @see FeatureColumnMetadata.Fields#tensorCategory
     */
    public void removeTensorCategory() {
        super._map.remove("tensorCategory");
    }

    /**
     * Getter for tensorCategory
     * 
     * @see FeatureColumnMetadata.Fields#tensorCategory
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
     * @see FeatureColumnMetadata.Fields#tensorCategory
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
     * @see FeatureColumnMetadata.Fields#tensorCategory
     */
    public FeatureColumnMetadata setTensorCategory(TensorCategory value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTensorCategory(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field tensorCategory of com.linkedin.feathr.fds.FeatureColumnMetadata");
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
     * @see FeatureColumnMetadata.Fields#tensorCategory
     */
    public FeatureColumnMetadata setTensorCategory(
        @Nonnull
        TensorCategory value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field tensorCategory of com.linkedin.feathr.fds.FeatureColumnMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "tensorCategory", value.name());
            _tensorCategoryField = value;
        }
        return this;
    }

    /**
     * Existence checker for tensorShape
     * 
     * @see FeatureColumnMetadata.Fields#tensorShape
     */
    public boolean hasTensorShape() {
        if (_tensorShapeField!= null) {
            return true;
        }
        return super._map.containsKey("tensorShape");
    }

    /**
     * Remover for tensorShape
     * 
     * @see FeatureColumnMetadata.Fields#tensorShape
     */
    public void removeTensorShape() {
        super._map.remove("tensorShape");
    }

    /**
     * Getter for tensorShape
     * 
     * @see FeatureColumnMetadata.Fields#tensorShape
     */
    public IntegerArray getTensorShape(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTensorShape();
            case DEFAULT:
            case NULL:
                if (_tensorShapeField!= null) {
                    return _tensorShapeField;
                } else {
                    Object __rawValue = super._map.get("tensorShape");
                    _tensorShapeField = ((__rawValue == null)?null:new IntegerArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _tensorShapeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for tensorShape
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureColumnMetadata.Fields#tensorShape
     */
    @Nonnull
    public IntegerArray getTensorShape() {
        if (_tensorShapeField!= null) {
            return _tensorShapeField;
        } else {
            Object __rawValue = super._map.get("tensorShape");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("tensorShape");
            }
            _tensorShapeField = ((__rawValue == null)?null:new IntegerArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _tensorShapeField;
        }
    }

    /**
     * Setter for tensorShape
     * 
     * @see FeatureColumnMetadata.Fields#tensorShape
     */
    public FeatureColumnMetadata setTensorShape(IntegerArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTensorShape(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field tensorShape of com.linkedin.feathr.fds.FeatureColumnMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tensorShape", value.data());
                    _tensorShapeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTensorShape();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tensorShape", value.data());
                    _tensorShapeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "tensorShape", value.data());
                    _tensorShapeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for tensorShape
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureColumnMetadata.Fields#tensorShape
     */
    public FeatureColumnMetadata setTensorShape(
        @Nonnull
        IntegerArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field tensorShape of com.linkedin.feathr.fds.FeatureColumnMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "tensorShape", value.data());
            _tensorShapeField = value;
        }
        return this;
    }

    /**
     * Existence checker for dimensionTypes
     * 
     * @see FeatureColumnMetadata.Fields#dimensionTypes
     */
    public boolean hasDimensionTypes() {
        if (_dimensionTypesField!= null) {
            return true;
        }
        return super._map.containsKey("dimensionTypes");
    }

    /**
     * Remover for dimensionTypes
     * 
     * @see FeatureColumnMetadata.Fields#dimensionTypes
     */
    public void removeDimensionTypes() {
        super._map.remove("dimensionTypes");
    }

    /**
     * Getter for dimensionTypes
     * 
     * @see FeatureColumnMetadata.Fields#dimensionTypes
     */
    public DimensionTypeArray getDimensionTypes(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDimensionTypes();
            case DEFAULT:
            case NULL:
                if (_dimensionTypesField!= null) {
                    return _dimensionTypesField;
                } else {
                    Object __rawValue = super._map.get("dimensionTypes");
                    _dimensionTypesField = ((__rawValue == null)?null:new DimensionTypeArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _dimensionTypesField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for dimensionTypes
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureColumnMetadata.Fields#dimensionTypes
     */
    @Nonnull
    public DimensionTypeArray getDimensionTypes() {
        if (_dimensionTypesField!= null) {
            return _dimensionTypesField;
        } else {
            Object __rawValue = super._map.get("dimensionTypes");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("dimensionTypes");
            }
            _dimensionTypesField = ((__rawValue == null)?null:new DimensionTypeArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _dimensionTypesField;
        }
    }

    /**
     * Setter for dimensionTypes
     * 
     * @see FeatureColumnMetadata.Fields#dimensionTypes
     */
    public FeatureColumnMetadata setDimensionTypes(DimensionTypeArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDimensionTypes(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field dimensionTypes of com.linkedin.feathr.fds.FeatureColumnMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dimensionTypes", value.data());
                    _dimensionTypesField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDimensionTypes();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dimensionTypes", value.data());
                    _dimensionTypesField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dimensionTypes", value.data());
                    _dimensionTypesField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dimensionTypes
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureColumnMetadata.Fields#dimensionTypes
     */
    public FeatureColumnMetadata setDimensionTypes(
        @Nonnull
        DimensionTypeArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dimensionTypes of com.linkedin.feathr.fds.FeatureColumnMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dimensionTypes", value.data());
            _dimensionTypesField = value;
        }
        return this;
    }

    /**
     * Existence checker for valueType
     * 
     * @see FeatureColumnMetadata.Fields#valueType
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
     * @see FeatureColumnMetadata.Fields#valueType
     */
    public void removeValueType() {
        super._map.remove("valueType");
    }

    /**
     * Getter for valueType
     * 
     * @see FeatureColumnMetadata.Fields#valueType
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
     * @see FeatureColumnMetadata.Fields#valueType
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
     * @see FeatureColumnMetadata.Fields#valueType
     */
    public FeatureColumnMetadata setValueType(ValueType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setValueType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field valueType of com.linkedin.feathr.fds.FeatureColumnMetadata");
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
     * @see FeatureColumnMetadata.Fields#valueType
     */
    public FeatureColumnMetadata setValueType(
        @Nonnull
        ValueType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field valueType of com.linkedin.feathr.fds.FeatureColumnMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "valueType", value.name());
            _valueTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for linkageMetadata
     * 
     * @see FeatureColumnMetadata.Fields#linkageMetadata
     */
    public boolean hasLinkageMetadata() {
        if (_linkageMetadataField!= null) {
            return true;
        }
        return super._map.containsKey("linkageMetadata");
    }

    /**
     * Remover for linkageMetadata
     * 
     * @see FeatureColumnMetadata.Fields#linkageMetadata
     */
    public void removeLinkageMetadata() {
        super._map.remove("linkageMetadata");
    }

    /**
     * Getter for linkageMetadata
     * 
     * @see FeatureColumnMetadata.Fields#linkageMetadata
     */
    public FeatureColumnLinkageMetadata getLinkageMetadata(GetMode mode) {
        return getLinkageMetadata();
    }

    /**
     * Getter for linkageMetadata
     * 
     * @return
     *     Optional field. Always check for null.
     * @see FeatureColumnMetadata.Fields#linkageMetadata
     */
    @Nullable
    public FeatureColumnLinkageMetadata getLinkageMetadata() {
        if (_linkageMetadataField!= null) {
            return _linkageMetadataField;
        } else {
            Object __rawValue = super._map.get("linkageMetadata");
            _linkageMetadataField = ((__rawValue == null)?null:new FeatureColumnLinkageMetadata(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _linkageMetadataField;
        }
    }

    /**
     * Setter for linkageMetadata
     * 
     * @see FeatureColumnMetadata.Fields#linkageMetadata
     */
    public FeatureColumnMetadata setLinkageMetadata(FeatureColumnLinkageMetadata value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLinkageMetadata(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLinkageMetadata();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "linkageMetadata", value.data());
                    _linkageMetadataField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "linkageMetadata", value.data());
                    _linkageMetadataField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for linkageMetadata
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureColumnMetadata.Fields#linkageMetadata
     */
    public FeatureColumnMetadata setLinkageMetadata(
        @Nonnull
        FeatureColumnLinkageMetadata value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field linkageMetadata of com.linkedin.feathr.fds.FeatureColumnMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "linkageMetadata", value.data());
            _linkageMetadataField = value;
        }
        return this;
    }

    @Override
    public FeatureColumnMetadata clone()
        throws CloneNotSupportedException
    {
        FeatureColumnMetadata __clone = ((FeatureColumnMetadata) super.clone());
        __clone.__changeListener = new FeatureColumnMetadata.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeatureColumnMetadata copy()
        throws CloneNotSupportedException
    {
        FeatureColumnMetadata __copy = ((FeatureColumnMetadata) super.copy());
        __copy._tensorShapeField = null;
        __copy._dimensionTypesField = null;
        __copy._linkageMetadataField = null;
        __copy._tensorCategoryField = null;
        __copy._valueTypeField = null;
        __copy.__changeListener = new FeatureColumnMetadata.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeatureColumnMetadata __objectRef;

        private ChangeListener(FeatureColumnMetadata reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "tensorShape":
                    __objectRef._tensorShapeField = null;
                    break;
                case "dimensionTypes":
                    __objectRef._dimensionTypesField = null;
                    break;
                case "linkageMetadata":
                    __objectRef._linkageMetadataField = null;
                    break;
                case "tensorCategory":
                    __objectRef._tensorCategoryField = null;
                    break;
                case "valueType":
                    __objectRef._valueTypeField = null;
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
         * Category of the tensor (e.g. TensorCategory.DENSE, TensorCategory.SPARSE)
         * 
         */
        public PathSpec tensorCategory() {
            return new PathSpec(getPathComponents(), "tensorCategory");
        }

        /**
         * The shape (sometimes called the “size” or “dense shape”) of the tensor.
         * Given as a list of integers. The first element gives the size of the first dimension
         * in the tensor, the second element gives the size of the second dimension, and so on.
         * The length of the tensorShape array is the number of dimensions in the tensor, also
         * called the tensor's rank.
         * The data schema must match the tensor’s rank as indicated in the tensorShape array;
         * which in case of DENSE means that the level of nesting of the array must
         * match the rank, and in case of SPARSE there must be the same number of
         * indices fields as the rank.
         * Dimensions are allowed to have unknown size. Elements in the tensorShape array may be
         * set as (-1) to indicate unknown.
         * For any dimension having dimensionType of string, that dimension’s size must be set
         * to (-1) (unknown).
         * 
         */
        public PathSpec tensorShape() {
            return new PathSpec(getPathComponents(), "tensorShape");
        }

        /**
         * The shape (sometimes called the “size” or “dense shape”) of the tensor.
         * Given as a list of integers. The first element gives the size of the first dimension
         * in the tensor, the second element gives the size of the second dimension, and so on.
         * The length of the tensorShape array is the number of dimensions in the tensor, also
         * called the tensor's rank.
         * The data schema must match the tensor’s rank as indicated in the tensorShape array;
         * which in case of DENSE means that the level of nesting of the array must
         * match the rank, and in case of SPARSE there must be the same number of
         * indices fields as the rank.
         * Dimensions are allowed to have unknown size. Elements in the tensorShape array may be
         * set as (-1) to indicate unknown.
         * For any dimension having dimensionType of string, that dimension’s size must be set
         * to (-1) (unknown).
         * 
         */
        public PathSpec tensorShape(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "tensorShape");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * List of the types for each dimension. Given as an array of strings.
         * For DENSE features, must contain only DimensionType.INT. For SPARSE features,
         * must correspond to the data types of the corresponding indices fields in the data schema
         * 
         */
        public PathSpec dimensionTypes() {
            return new PathSpec(getPathComponents(), "dimensionTypes");
        }

        /**
         * List of the types for each dimension. Given as an array of strings.
         * For DENSE features, must contain only DimensionType.INT. For SPARSE features,
         * must correspond to the data types of the corresponding indices fields in the data schema
         * 
         */
        public PathSpec dimensionTypes(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "dimensionTypes");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * The value type (e.g. ValueType.INT, ValueType.FLOAT)
         * 
         */
        public PathSpec valueType() {
            return new PathSpec(getPathComponents(), "valueType");
        }

        /**
         * Optional metadata that helps with linkage of a feature. See the documentation
         * on FeatureColumnLinkageMetadata for more info.
         * 
         */
        public com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.Fields linkageMetadata() {
            return new com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.Fields(getPathComponents(), "linkageMetadata");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.ProjectionMask _linkageMetadataMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Category of the tensor (e.g. TensorCategory.DENSE, TensorCategory.SPARSE)
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withTensorCategory() {
            getDataMap().put("tensorCategory", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The shape (sometimes called the “size” or “dense shape”) of the tensor.
         * Given as a list of integers. The first element gives the size of the first dimension
         * in the tensor, the second element gives the size of the second dimension, and so on.
         * The length of the tensorShape array is the number of dimensions in the tensor, also
         * called the tensor's rank.
         * The data schema must match the tensor’s rank as indicated in the tensorShape array;
         * which in case of DENSE means that the level of nesting of the array must
         * match the rank, and in case of SPARSE there must be the same number of
         * indices fields as the rank.
         * Dimensions are allowed to have unknown size. Elements in the tensorShape array may be
         * set as (-1) to indicate unknown.
         * For any dimension having dimensionType of string, that dimension’s size must be set
         * to (-1) (unknown).
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withTensorShape() {
            getDataMap().put("tensorShape", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The shape (sometimes called the “size” or “dense shape”) of the tensor.
         * Given as a list of integers. The first element gives the size of the first dimension
         * in the tensor, the second element gives the size of the second dimension, and so on.
         * The length of the tensorShape array is the number of dimensions in the tensor, also
         * called the tensor's rank.
         * The data schema must match the tensor’s rank as indicated in the tensorShape array;
         * which in case of DENSE means that the level of nesting of the array must
         * match the rank, and in case of SPARSE there must be the same number of
         * indices fields as the rank.
         * Dimensions are allowed to have unknown size. Elements in the tensorShape array may be
         * set as (-1) to indicate unknown.
         * For any dimension having dimensionType of string, that dimension’s size must be set
         * to (-1) (unknown).
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withTensorShape(Integer start, Integer count) {
            getDataMap().put("tensorShape", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("tensorShape").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("tensorShape").put("$count", count);
            }
            return this;
        }

        /**
         * List of the types for each dimension. Given as an array of strings.
         * For DENSE features, must contain only DimensionType.INT. For SPARSE features,
         * must correspond to the data types of the corresponding indices fields in the data schema
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withDimensionTypes() {
            getDataMap().put("dimensionTypes", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * List of the types for each dimension. Given as an array of strings.
         * For DENSE features, must contain only DimensionType.INT. For SPARSE features,
         * must correspond to the data types of the corresponding indices fields in the data schema
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withDimensionTypes(Integer start, Integer count) {
            getDataMap().put("dimensionTypes", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("dimensionTypes").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("dimensionTypes").put("$count", count);
            }
            return this;
        }

        /**
         * The value type (e.g. ValueType.INT, ValueType.FLOAT)
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withValueType() {
            getDataMap().put("valueType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Optional metadata that helps with linkage of a feature. See the documentation
         * on FeatureColumnLinkageMetadata for more info.
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withLinkageMetadata(Function<com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.ProjectionMask, com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.ProjectionMask> nestedMask) {
            _linkageMetadataMask = nestedMask.apply(((_linkageMetadataMask == null)?FeatureColumnLinkageMetadata.createMask():_linkageMetadataMask));
            getDataMap().put("linkageMetadata", _linkageMetadataMask.getDataMap());
            return this;
        }

        /**
         * Optional metadata that helps with linkage of a feature. See the documentation
         * on FeatureColumnLinkageMetadata for more info.
         * 
         */
        public FeatureColumnMetadata.ProjectionMask withLinkageMetadata() {
            _linkageMetadataMask = null;
            getDataMap().put("linkageMetadata", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
