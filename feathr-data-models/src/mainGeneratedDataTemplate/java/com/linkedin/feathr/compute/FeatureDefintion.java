
package com.linkedin.feathr.compute;

import java.util.List;
import java.util.function.Function;
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
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/FeatureDefintion.pdl.")
public class FeatureDefintion
    extends RecordTemplate
{

    private final static FeatureDefintion.Fields _fields = new FeatureDefintion.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record FeatureDefintion{nodeId:int,featureVersion:/**A Feature can have multiple FeatureVersions. Versioning of a feature is declared by feature producers per semantic versioning. Every time the definition of a feature changes, a new FeatureVersion should be created. Each FeatureVersion enclosed attributes that don't change across environments.*/@gma.aspect.entity.urn=\"com.linkedin.common.MlFeatureVersionUrn\"record FeatureVersion{/**Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of Frame before Tensorization and will be kept around until a full transition to Tensor types is completed*/type:/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of Frame. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO(PROML-13658): this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}=\"UNSPECIFIED\"/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Frame will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.*/format:optional/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in Frame, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in Frame and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and Frame.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and Frame.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime. TODO(PROML): TODO(PROML-10173): To decide whether to use -1 or optional to indicate unknown shape in the week of 08/10/2020.*/shape:optional int}]}/**An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.*/defaultValue:optional/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]}}", SchemaFormatType.PDL));
    private Integer _nodeIdField = null;
    private FeatureVersion _featureVersionField = null;
    private FeatureDefintion.ChangeListener __changeListener = new FeatureDefintion.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_NodeId = SCHEMA.getField("nodeId");
    private final static RecordDataSchema.Field FIELD_FeatureVersion = SCHEMA.getField("featureVersion");

    public FeatureDefintion() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public FeatureDefintion(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeatureDefintion.Fields fields() {
        return _fields;
    }

    public static FeatureDefintion.ProjectionMask createMask() {
        return new FeatureDefintion.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for nodeId
     * 
     * @see FeatureDefintion.Fields#nodeId
     */
    public boolean hasNodeId() {
        if (_nodeIdField!= null) {
            return true;
        }
        return super._map.containsKey("nodeId");
    }

    /**
     * Remover for nodeId
     * 
     * @see FeatureDefintion.Fields#nodeId
     */
    public void removeNodeId() {
        super._map.remove("nodeId");
    }

    /**
     * Getter for nodeId
     * 
     * @see FeatureDefintion.Fields#nodeId
     */
    public Integer getNodeId(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getNodeId();
            case DEFAULT:
            case NULL:
                if (_nodeIdField!= null) {
                    return _nodeIdField;
                } else {
                    Object __rawValue = super._map.get("nodeId");
                    _nodeIdField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _nodeIdField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for nodeId
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureDefintion.Fields#nodeId
     */
    @Nonnull
    public Integer getNodeId() {
        if (_nodeIdField!= null) {
            return _nodeIdField;
        } else {
            Object __rawValue = super._map.get("nodeId");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("nodeId");
            }
            _nodeIdField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _nodeIdField;
        }
    }

    /**
     * Setter for nodeId
     * 
     * @see FeatureDefintion.Fields#nodeId
     */
    public FeatureDefintion setNodeId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setNodeId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field nodeId of com.linkedin.feathr.compute.FeatureDefintion");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "nodeId", DataTemplateUtil.coerceIntInput(value));
                    _nodeIdField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeNodeId();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "nodeId", DataTemplateUtil.coerceIntInput(value));
                    _nodeIdField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "nodeId", DataTemplateUtil.coerceIntInput(value));
                    _nodeIdField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for nodeId
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureDefintion.Fields#nodeId
     */
    public FeatureDefintion setNodeId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field nodeId of com.linkedin.feathr.compute.FeatureDefintion to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "nodeId", DataTemplateUtil.coerceIntInput(value));
            _nodeIdField = value;
        }
        return this;
    }

    /**
     * Setter for nodeId
     * 
     * @see FeatureDefintion.Fields#nodeId
     */
    public FeatureDefintion setNodeId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "nodeId", DataTemplateUtil.coerceIntInput(value));
        _nodeIdField = value;
        return this;
    }

    /**
     * Existence checker for featureVersion
     * 
     * @see FeatureDefintion.Fields#featureVersion
     */
    public boolean hasFeatureVersion() {
        if (_featureVersionField!= null) {
            return true;
        }
        return super._map.containsKey("featureVersion");
    }

    /**
     * Remover for featureVersion
     * 
     * @see FeatureDefintion.Fields#featureVersion
     */
    public void removeFeatureVersion() {
        super._map.remove("featureVersion");
    }

    /**
     * Getter for featureVersion
     * 
     * @see FeatureDefintion.Fields#featureVersion
     */
    public FeatureVersion getFeatureVersion(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFeatureVersion();
            case DEFAULT:
            case NULL:
                if (_featureVersionField!= null) {
                    return _featureVersionField;
                } else {
                    Object __rawValue = super._map.get("featureVersion");
                    _featureVersionField = ((__rawValue == null)?null:new FeatureVersion(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _featureVersionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for featureVersion
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureDefintion.Fields#featureVersion
     */
    @Nonnull
    public FeatureVersion getFeatureVersion() {
        if (_featureVersionField!= null) {
            return _featureVersionField;
        } else {
            Object __rawValue = super._map.get("featureVersion");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("featureVersion");
            }
            _featureVersionField = ((__rawValue == null)?null:new FeatureVersion(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _featureVersionField;
        }
    }

    /**
     * Setter for featureVersion
     * 
     * @see FeatureDefintion.Fields#featureVersion
     */
    public FeatureDefintion setFeatureVersion(FeatureVersion value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureVersion(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureVersion of com.linkedin.feathr.compute.FeatureDefintion");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureVersion", value.data());
                    _featureVersionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureVersion();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureVersion", value.data());
                    _featureVersionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureVersion", value.data());
                    _featureVersionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureVersion
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureDefintion.Fields#featureVersion
     */
    public FeatureDefintion setFeatureVersion(
        @Nonnull
        FeatureVersion value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureVersion of com.linkedin.feathr.compute.FeatureDefintion to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureVersion", value.data());
            _featureVersionField = value;
        }
        return this;
    }

    @Override
    public FeatureDefintion clone()
        throws CloneNotSupportedException
    {
        FeatureDefintion __clone = ((FeatureDefintion) super.clone());
        __clone.__changeListener = new FeatureDefintion.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeatureDefintion copy()
        throws CloneNotSupportedException
    {
        FeatureDefintion __copy = ((FeatureDefintion) super.copy());
        __copy._nodeIdField = null;
        __copy._featureVersionField = null;
        __copy.__changeListener = new FeatureDefintion.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeatureDefintion __objectRef;

        private ChangeListener(FeatureDefintion reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "nodeId":
                    __objectRef._nodeIdField = null;
                    break;
                case "featureVersion":
                    __objectRef._featureVersionField = null;
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

        public PathSpec nodeId() {
            return new PathSpec(getPathComponents(), "nodeId");
        }

        public com.linkedin.feathr.compute.FeatureVersion.Fields featureVersion() {
            return new com.linkedin.feathr.compute.FeatureVersion.Fields(getPathComponents(), "featureVersion");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.FeatureVersion.ProjectionMask _featureVersionMask;

        ProjectionMask() {
            super(3);
        }

        public FeatureDefintion.ProjectionMask withNodeId() {
            getDataMap().put("nodeId", MaskMap.POSITIVE_MASK);
            return this;
        }

        public FeatureDefintion.ProjectionMask withFeatureVersion(Function<com.linkedin.feathr.compute.FeatureVersion.ProjectionMask, com.linkedin.feathr.compute.FeatureVersion.ProjectionMask> nestedMask) {
            _featureVersionMask = nestedMask.apply(((_featureVersionMask == null)?FeatureVersion.createMask():_featureVersionMask));
            getDataMap().put("featureVersion", _featureVersionMask.getDataMap());
            return this;
        }

        public FeatureDefintion.ProjectionMask withFeatureVersion() {
            _featureVersionMask = null;
            getDataMap().put("featureVersion", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
