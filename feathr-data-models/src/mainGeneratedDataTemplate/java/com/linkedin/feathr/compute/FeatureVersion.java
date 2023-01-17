
package com.linkedin.feathr.compute;

import java.util.List;
import java.util.function.Function;
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
import com.linkedin.data.template.SetMode;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\FeatureVersion.pdl.")
public class FeatureVersion
    extends RecordTemplate
{

    private final static FeatureVersion.Fields _fields = new FeatureVersion.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record FeatureVersion{/**Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of feathr before Tensorization and will be kept around until a full transition to Tensor types is completed*/type:/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of feathr. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO - this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}=\"UNSPECIFIED\"/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. feathr will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.*/format:optional/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in feathr, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in feathr and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and feathr.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and feathr.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime.*/shape:optional int}]}/**An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.*/defaultValue:optional/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]}", SchemaFormatType.PDL));
    private FrameFeatureType _typeField = null;
    private TensorFeatureFormat _formatField = null;
    private FeatureValue _defaultValueField = null;
    private FeatureVersion.ChangeListener __changeListener = new FeatureVersion.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Type = SCHEMA.getField("type");
    private final static FrameFeatureType DEFAULT_Type;
    private final static RecordDataSchema.Field FIELD_Format = SCHEMA.getField("format");
    private final static RecordDataSchema.Field FIELD_DefaultValue = SCHEMA.getField("defaultValue");

    static {
        DEFAULT_Type = DataTemplateUtil.coerceEnumOutput(FIELD_Type.getDefault(), FrameFeatureType.class, FrameFeatureType.$UNKNOWN);
    }

    public FeatureVersion() {
        super(new DataMap(4, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public FeatureVersion(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeatureVersion.Fields fields() {
        return _fields;
    }

    public static FeatureVersion.ProjectionMask createMask() {
        return new FeatureVersion.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for type
     * 
     * @see FeatureVersion.Fields#type
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
     * @see FeatureVersion.Fields#type
     */
    public void removeType() {
        super._map.remove("type");
    }

    /**
     * Getter for type
     * 
     * @see FeatureVersion.Fields#type
     */
    public FrameFeatureType getType(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getType();
            case NULL:
                if (_typeField!= null) {
                    return _typeField;
                } else {
                    Object __rawValue = super._map.get("type");
                    _typeField = DataTemplateUtil.coerceEnumOutput(__rawValue, FrameFeatureType.class, FrameFeatureType.$UNKNOWN);
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
     * @see FeatureVersion.Fields#type
     */
    @Nonnull
    public FrameFeatureType getType() {
        if (_typeField!= null) {
            return _typeField;
        } else {
            Object __rawValue = super._map.get("type");
            if (__rawValue == null) {
                return DEFAULT_Type;
            }
            _typeField = DataTemplateUtil.coerceEnumOutput(__rawValue, FrameFeatureType.class, FrameFeatureType.$UNKNOWN);
            return _typeField;
        }
    }

    /**
     * Setter for type
     * 
     * @see FeatureVersion.Fields#type
     */
    public FeatureVersion setType(FrameFeatureType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field type of com.linkedin.feathr.compute.FeatureVersion");
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
     * @see FeatureVersion.Fields#type
     */
    public FeatureVersion setType(
        @Nonnull
        FrameFeatureType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field type of com.linkedin.feathr.compute.FeatureVersion to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "type", value.name());
            _typeField = value;
        }
        return this;
    }

    /**
     * Existence checker for format
     * 
     * @see FeatureVersion.Fields#format
     */
    public boolean hasFormat() {
        if (_formatField!= null) {
            return true;
        }
        return super._map.containsKey("format");
    }

    /**
     * Remover for format
     * 
     * @see FeatureVersion.Fields#format
     */
    public void removeFormat() {
        super._map.remove("format");
    }

    /**
     * Getter for format
     * 
     * @see FeatureVersion.Fields#format
     */
    public TensorFeatureFormat getFormat(GetMode mode) {
        return getFormat();
    }

    /**
     * Getter for format
     * 
     * @return
     *     Optional field. Always check for null.
     * @see FeatureVersion.Fields#format
     */
    @Nullable
    public TensorFeatureFormat getFormat() {
        if (_formatField!= null) {
            return _formatField;
        } else {
            Object __rawValue = super._map.get("format");
            _formatField = ((__rawValue == null)?null:new TensorFeatureFormat(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _formatField;
        }
    }

    /**
     * Setter for format
     * 
     * @see FeatureVersion.Fields#format
     */
    public FeatureVersion setFormat(TensorFeatureFormat value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFormat(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFormat();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "format", value.data());
                    _formatField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "format", value.data());
                    _formatField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for format
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureVersion.Fields#format
     */
    public FeatureVersion setFormat(
        @Nonnull
        TensorFeatureFormat value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field format of com.linkedin.feathr.compute.FeatureVersion to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "format", value.data());
            _formatField = value;
        }
        return this;
    }

    /**
     * Existence checker for defaultValue
     * 
     * @see FeatureVersion.Fields#defaultValue
     */
    public boolean hasDefaultValue() {
        if (_defaultValueField!= null) {
            return true;
        }
        return super._map.containsKey("defaultValue");
    }

    /**
     * Remover for defaultValue
     * 
     * @see FeatureVersion.Fields#defaultValue
     */
    public void removeDefaultValue() {
        super._map.remove("defaultValue");
    }

    /**
     * Getter for defaultValue
     * 
     * @see FeatureVersion.Fields#defaultValue
     */
    public FeatureValue getDefaultValue(GetMode mode) {
        return getDefaultValue();
    }

    /**
     * Getter for defaultValue
     * 
     * @return
     *     Optional field. Always check for null.
     * @see FeatureVersion.Fields#defaultValue
     */
    @Nullable
    public FeatureValue getDefaultValue() {
        if (_defaultValueField!= null) {
            return _defaultValueField;
        } else {
            Object __rawValue = super._map.get("defaultValue");
            _defaultValueField = ((__rawValue == null)?null:new FeatureValue(__rawValue));
            return _defaultValueField;
        }
    }

    /**
     * Setter for defaultValue
     * 
     * @see FeatureVersion.Fields#defaultValue
     */
    public FeatureVersion setDefaultValue(FeatureValue value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDefaultValue(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDefaultValue();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "defaultValue", value.data());
                    _defaultValueField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "defaultValue", value.data());
                    _defaultValueField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for defaultValue
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureVersion.Fields#defaultValue
     */
    public FeatureVersion setDefaultValue(
        @Nonnull
        FeatureValue value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field defaultValue of com.linkedin.feathr.compute.FeatureVersion to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "defaultValue", value.data());
            _defaultValueField = value;
        }
        return this;
    }

    @Override
    public FeatureVersion clone()
        throws CloneNotSupportedException
    {
        FeatureVersion __clone = ((FeatureVersion) super.clone());
        __clone.__changeListener = new FeatureVersion.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeatureVersion copy()
        throws CloneNotSupportedException
    {
        FeatureVersion __copy = ((FeatureVersion) super.copy());
        __copy._defaultValueField = null;
        __copy._formatField = null;
        __copy._typeField = null;
        __copy.__changeListener = new FeatureVersion.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeatureVersion __objectRef;

        private ChangeListener(FeatureVersion reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "defaultValue":
                    __objectRef._defaultValueField = null;
                    break;
                case "format":
                    __objectRef._formatField = null;
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
         * Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of feathr before Tensorization and will be kept around until a full transition to Tensor types is completed
         * 
         */
        public PathSpec type() {
            return new PathSpec(getPathComponents(), "type");
        }

        /**
         * Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. feathr will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.
         * 
         */
        public com.linkedin.feathr.compute.TensorFeatureFormat.Fields format() {
            return new com.linkedin.feathr.compute.TensorFeatureFormat.Fields(getPathComponents(), "format");
        }

        /**
         * An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.
         * 
         */
        public com.linkedin.feathr.compute.FeatureValue.Fields defaultValue() {
            return new com.linkedin.feathr.compute.FeatureValue.Fields(getPathComponents(), "defaultValue");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.TensorFeatureFormat.ProjectionMask _formatMask;
        private com.linkedin.feathr.compute.FeatureValue.ProjectionMask _defaultValueMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of feathr before Tensorization and will be kept around until a full transition to Tensor types is completed
         * 
         */
        public FeatureVersion.ProjectionMask withType() {
            getDataMap().put("type", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. feathr will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.
         * 
         */
        public FeatureVersion.ProjectionMask withFormat(Function<com.linkedin.feathr.compute.TensorFeatureFormat.ProjectionMask, com.linkedin.feathr.compute.TensorFeatureFormat.ProjectionMask> nestedMask) {
            _formatMask = nestedMask.apply(((_formatMask == null)?TensorFeatureFormat.createMask():_formatMask));
            getDataMap().put("format", _formatMask.getDataMap());
            return this;
        }

        /**
         * Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. feathr will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.
         * 
         */
        public FeatureVersion.ProjectionMask withFormat() {
            _formatMask = null;
            getDataMap().put("format", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.
         * 
         */
        public FeatureVersion.ProjectionMask withDefaultValue(Function<com.linkedin.feathr.compute.FeatureValue.ProjectionMask, com.linkedin.feathr.compute.FeatureValue.ProjectionMask> nestedMask) {
            _defaultValueMask = nestedMask.apply(((_defaultValueMask == null)?FeatureValue.createMask():_defaultValueMask));
            getDataMap().put("defaultValue", _defaultValueMask.getDataMap());
            return this;
        }

        /**
         * An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.
         * 
         */
        public FeatureVersion.ProjectionMask withDefaultValue() {
            _defaultValueMask = null;
            getDataMap().put("defaultValue", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
