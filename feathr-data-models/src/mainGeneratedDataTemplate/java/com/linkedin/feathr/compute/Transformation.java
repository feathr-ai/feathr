
package com.linkedin.feathr.compute;

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
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;


/**
 * Representation of a transformation node.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\Transformation.pdl.")
public class Transformation
    extends RecordTemplate
{

    private final static Transformation.Fields _fields = new Transformation.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Representation of a transformation node.*/record Transformation includes/**Generic abstraction of a node. All other nodes should derive from this node.*/record AbstractNode{/**The node would be represented by this id.*/id:/**A type ref to int node id*/typeref NodeId=int/**The key for which this node is being requested.\r\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\r\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\r\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\r\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)*/concreteKey:optional/**The key (node) for which the node in question is requested.*/record ConcreteKey{/**Most of the time, this should point to a CONTEXT SOURCE node, e.g. a key in the context called x.\r\nThe main exception would be for a Lookup feature, in which case it would point to another node where the lookup\r\nkey gets computed.*/key:array[NodeId]}}{/**An array of node references which should be considered as input to apply the transformation function.*/inputs:array[/**This is used to refer to a node from another node. It is a combination of a node id and the indices of the keys from the\r\noriginal node array.\r\nFor example, consider:-\r\nanchorA: {\r\n key: [viewerId, vieweeId]\r\n feature: featureA\r\n}\r\nLet us say featureA is evaluated in node 1.\r\nderivation: {\r\n  key: [vieweeId, viewerId]\r\n  args1: {key: [vieweeId, viewerId], feature: featureA}\r\n  definition: args1*2\r\n}\r\nNow, the node reference (to represent args1) would be:\r\n nodeId: 1\r\n keyReference: [1,0] - // Indicates the ordering of the key indices.*/record NodeReference{/**node id of the referring node.*/id:NodeId/**The key references in the keys of the referring node.*/keyReference:array[/**This represents the position of the key in the node which is being referred to. For example, if the original node has a key\r\nlike [x, y], and the keyReference says 1, it is referring to y.*/record KeyReference{/**Position in the original key array*/position:int}]}]/**The transformation function.*/function:/**The transformation function*/record TransformationFunction{/**Indicates the operator type to be used here. The various different operators supported are in [[Operators]] class.\r\n*/operator:/**operator id to set an operator. It can be referring to an mvel expression, sql expression or a java udf.*/typeref OperatorId=string/**The various attributes required to represent the transformation function are captured in a map format.\r\nFor example, mvel expression or java udf class name*/parameters:optional map[string,string]}/**Feature name here is used so we retain feature name, type, and default values even after graph is resolved.\r\nFeature name here is also used for feature aliasing in the case where TransformationFunction is feature_alias.*/featureName:string/**feature version of the feature*/featureVersion:record FeatureVersion{/**Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of feathr before Tensorization and will be kept around until a full transition to Tensor types is completed*/type:/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of feathr. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO - this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}=\"UNSPECIFIED\"/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. feathr will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.*/format:optional/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in feathr, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in feathr and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and feathr.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and feathr.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime.*/shape:optional int}]}/**An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.*/defaultValue:optional/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]}}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private ConcreteKey _concreteKeyField = null;
    private NodeReferenceArray _inputsField = null;
    private TransformationFunction _functionField = null;
    private String _featureNameField = null;
    private FeatureVersion _featureVersionField = null;
    private Transformation.ChangeListener __changeListener = new Transformation.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_ConcreteKey = SCHEMA.getField("concreteKey");
    private final static RecordDataSchema.Field FIELD_Inputs = SCHEMA.getField("inputs");
    private final static RecordDataSchema.Field FIELD_Function = SCHEMA.getField("function");
    private final static RecordDataSchema.Field FIELD_FeatureName = SCHEMA.getField("featureName");
    private final static RecordDataSchema.Field FIELD_FeatureVersion = SCHEMA.getField("featureVersion");

    public Transformation() {
        super(new DataMap(8, 0.75F), SCHEMA, 6);
        addChangeListener(__changeListener);
    }

    public Transformation(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Transformation.Fields fields() {
        return _fields;
    }

    public static Transformation.ProjectionMask createMask() {
        return new Transformation.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see Transformation.Fields#id
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
     * @see Transformation.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see Transformation.Fields#id
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
     * @see Transformation.Fields#id
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
     * @see Transformation.Fields#id
     */
    public Transformation setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.Transformation");
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
     * @see Transformation.Fields#id
     */
    public Transformation setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see Transformation.Fields#id
     */
    public Transformation setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for concreteKey
     * 
     * @see Transformation.Fields#concreteKey
     */
    public boolean hasConcreteKey() {
        if (_concreteKeyField!= null) {
            return true;
        }
        return super._map.containsKey("concreteKey");
    }

    /**
     * Remover for concreteKey
     * 
     * @see Transformation.Fields#concreteKey
     */
    public void removeConcreteKey() {
        super._map.remove("concreteKey");
    }

    /**
     * Getter for concreteKey
     * 
     * @see Transformation.Fields#concreteKey
     */
    public ConcreteKey getConcreteKey(GetMode mode) {
        return getConcreteKey();
    }

    /**
     * Getter for concreteKey
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Transformation.Fields#concreteKey
     */
    @Nullable
    public ConcreteKey getConcreteKey() {
        if (_concreteKeyField!= null) {
            return _concreteKeyField;
        } else {
            Object __rawValue = super._map.get("concreteKey");
            _concreteKeyField = ((__rawValue == null)?null:new ConcreteKey(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _concreteKeyField;
        }
    }

    /**
     * Setter for concreteKey
     * 
     * @see Transformation.Fields#concreteKey
     */
    public Transformation setConcreteKey(ConcreteKey value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setConcreteKey(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeConcreteKey();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
                    _concreteKeyField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
                    _concreteKeyField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for concreteKey
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Transformation.Fields#concreteKey
     */
    public Transformation setConcreteKey(
        @Nonnull
        ConcreteKey value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field concreteKey of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
            _concreteKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public boolean hasInputs() {
        if (_inputsField!= null) {
            return true;
        }
        return super._map.containsKey("inputs");
    }

    /**
     * Remover for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public void removeInputs() {
        super._map.remove("inputs");
    }

    /**
     * Getter for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public NodeReferenceArray getInputs(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getInputs();
            case DEFAULT:
            case NULL:
                if (_inputsField!= null) {
                    return _inputsField;
                } else {
                    Object __rawValue = super._map.get("inputs");
                    _inputsField = ((__rawValue == null)?null:new NodeReferenceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _inputsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for inputs
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Transformation.Fields#inputs
     */
    @Nonnull
    public NodeReferenceArray getInputs() {
        if (_inputsField!= null) {
            return _inputsField;
        } else {
            Object __rawValue = super._map.get("inputs");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("inputs");
            }
            _inputsField = ((__rawValue == null)?null:new NodeReferenceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _inputsField;
        }
    }

    /**
     * Setter for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public Transformation setInputs(NodeReferenceArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setInputs(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field inputs of com.linkedin.feathr.compute.Transformation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
                    _inputsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeInputs();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
                    _inputsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
                    _inputsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for inputs
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Transformation.Fields#inputs
     */
    public Transformation setInputs(
        @Nonnull
        NodeReferenceArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field inputs of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
            _inputsField = value;
        }
        return this;
    }

    /**
     * Existence checker for function
     * 
     * @see Transformation.Fields#function
     */
    public boolean hasFunction() {
        if (_functionField!= null) {
            return true;
        }
        return super._map.containsKey("function");
    }

    /**
     * Remover for function
     * 
     * @see Transformation.Fields#function
     */
    public void removeFunction() {
        super._map.remove("function");
    }

    /**
     * Getter for function
     * 
     * @see Transformation.Fields#function
     */
    public TransformationFunction getFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFunction();
            case DEFAULT:
            case NULL:
                if (_functionField!= null) {
                    return _functionField;
                } else {
                    Object __rawValue = super._map.get("function");
                    _functionField = ((__rawValue == null)?null:new TransformationFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _functionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for function
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Transformation.Fields#function
     */
    @Nonnull
    public TransformationFunction getFunction() {
        if (_functionField!= null) {
            return _functionField;
        } else {
            Object __rawValue = super._map.get("function");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("function");
            }
            _functionField = ((__rawValue == null)?null:new TransformationFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _functionField;
        }
    }

    /**
     * Setter for function
     * 
     * @see Transformation.Fields#function
     */
    public Transformation setFunction(TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field function of com.linkedin.feathr.compute.Transformation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "function", value.data());
                    _functionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "function", value.data());
                    _functionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "function", value.data());
                    _functionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for function
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Transformation.Fields#function
     */
    public Transformation setFunction(
        @Nonnull
        TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field function of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "function", value.data());
            _functionField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureName
     * 
     * @see Transformation.Fields#featureName
     */
    public boolean hasFeatureName() {
        if (_featureNameField!= null) {
            return true;
        }
        return super._map.containsKey("featureName");
    }

    /**
     * Remover for featureName
     * 
     * @see Transformation.Fields#featureName
     */
    public void removeFeatureName() {
        super._map.remove("featureName");
    }

    /**
     * Getter for featureName
     * 
     * @see Transformation.Fields#featureName
     */
    public String getFeatureName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFeatureName();
            case DEFAULT:
            case NULL:
                if (_featureNameField!= null) {
                    return _featureNameField;
                } else {
                    Object __rawValue = super._map.get("featureName");
                    _featureNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _featureNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for featureName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Transformation.Fields#featureName
     */
    @Nonnull
    public String getFeatureName() {
        if (_featureNameField!= null) {
            return _featureNameField;
        } else {
            Object __rawValue = super._map.get("featureName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("featureName");
            }
            _featureNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _featureNameField;
        }
    }

    /**
     * Setter for featureName
     * 
     * @see Transformation.Fields#featureName
     */
    public Transformation setFeatureName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureName of com.linkedin.feathr.compute.Transformation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureName", value);
                    _featureNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureName", value);
                    _featureNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureName", value);
                    _featureNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Transformation.Fields#featureName
     */
    public Transformation setFeatureName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureName of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureName", value);
            _featureNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureVersion
     * 
     * @see Transformation.Fields#featureVersion
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
     * @see Transformation.Fields#featureVersion
     */
    public void removeFeatureVersion() {
        super._map.remove("featureVersion");
    }

    /**
     * Getter for featureVersion
     * 
     * @see Transformation.Fields#featureVersion
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
     * @see Transformation.Fields#featureVersion
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
     * @see Transformation.Fields#featureVersion
     */
    public Transformation setFeatureVersion(FeatureVersion value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureVersion(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureVersion of com.linkedin.feathr.compute.Transformation");
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
     * @see Transformation.Fields#featureVersion
     */
    public Transformation setFeatureVersion(
        @Nonnull
        FeatureVersion value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureVersion of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureVersion", value.data());
            _featureVersionField = value;
        }
        return this;
    }

    @Override
    public Transformation clone()
        throws CloneNotSupportedException
    {
        Transformation __clone = ((Transformation) super.clone());
        __clone.__changeListener = new Transformation.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Transformation copy()
        throws CloneNotSupportedException
    {
        Transformation __copy = ((Transformation) super.copy());
        __copy._featureNameField = null;
        __copy._inputsField = null;
        __copy._functionField = null;
        __copy._idField = null;
        __copy._concreteKeyField = null;
        __copy._featureVersionField = null;
        __copy.__changeListener = new Transformation.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Transformation __objectRef;

        private ChangeListener(Transformation reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "featureName":
                    __objectRef._featureNameField = null;
                    break;
                case "inputs":
                    __objectRef._inputsField = null;
                    break;
                case "function":
                    __objectRef._functionField = null;
                    break;
                case "id":
                    __objectRef._idField = null;
                    break;
                case "concreteKey":
                    __objectRef._concreteKeyField = null;
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

        /**
         * The node would be represented by this id.
         * 
         */
        public PathSpec id() {
            return new PathSpec(getPathComponents(), "id");
        }

        /**
         * The key for which this node is being requested.
         * If this node is a Source node, the engine can use the key to fetch or join the feature.
         * If this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but
         * should follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,
         * e.g. it can be used for identifying duplicate sections of the graph that can be pruned.)
         * 
         */
        public com.linkedin.feathr.compute.ConcreteKey.Fields concreteKey() {
            return new com.linkedin.feathr.compute.ConcreteKey.Fields(getPathComponents(), "concreteKey");
        }

        /**
         * An array of node references which should be considered as input to apply the transformation function.
         * 
         */
        public com.linkedin.feathr.compute.NodeReferenceArray.Fields inputs() {
            return new com.linkedin.feathr.compute.NodeReferenceArray.Fields(getPathComponents(), "inputs");
        }

        /**
         * An array of node references which should be considered as input to apply the transformation function.
         * 
         */
        public PathSpec inputs(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "inputs");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * The transformation function.
         * 
         */
        public com.linkedin.feathr.compute.TransformationFunction.Fields function() {
            return new com.linkedin.feathr.compute.TransformationFunction.Fields(getPathComponents(), "function");
        }

        /**
         * Feature name here is used so we retain feature name, type, and default values even after graph is resolved.
         * Feature name here is also used for feature aliasing in the case where TransformationFunction is feature_alias.
         * 
         */
        public PathSpec featureName() {
            return new PathSpec(getPathComponents(), "featureName");
        }

        /**
         * feature version of the feature
         * 
         */
        public com.linkedin.feathr.compute.FeatureVersion.Fields featureVersion() {
            return new com.linkedin.feathr.compute.FeatureVersion.Fields(getPathComponents(), "featureVersion");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.ConcreteKey.ProjectionMask _concreteKeyMask;
        private com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask _inputsMask;
        private com.linkedin.feathr.compute.TransformationFunction.ProjectionMask _functionMask;
        private com.linkedin.feathr.compute.FeatureVersion.ProjectionMask _featureVersionMask;

        ProjectionMask() {
            super(8);
        }

        /**
         * The node would be represented by this id.
         * 
         */
        public Transformation.ProjectionMask withId() {
            getDataMap().put("id", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The key for which this node is being requested.
         * If this node is a Source node, the engine can use the key to fetch or join the feature.
         * If this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but
         * should follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,
         * e.g. it can be used for identifying duplicate sections of the graph that can be pruned.)
         * 
         */
        public Transformation.ProjectionMask withConcreteKey(Function<com.linkedin.feathr.compute.ConcreteKey.ProjectionMask, com.linkedin.feathr.compute.ConcreteKey.ProjectionMask> nestedMask) {
            _concreteKeyMask = nestedMask.apply(((_concreteKeyMask == null)?ConcreteKey.createMask():_concreteKeyMask));
            getDataMap().put("concreteKey", _concreteKeyMask.getDataMap());
            return this;
        }

        /**
         * The key for which this node is being requested.
         * If this node is a Source node, the engine can use the key to fetch or join the feature.
         * If this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but
         * should follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,
         * e.g. it can be used for identifying duplicate sections of the graph that can be pruned.)
         * 
         */
        public Transformation.ProjectionMask withConcreteKey() {
            _concreteKeyMask = null;
            getDataMap().put("concreteKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * An array of node references which should be considered as input to apply the transformation function.
         * 
         */
        public Transformation.ProjectionMask withInputs(Function<com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask, com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask> nestedMask) {
            _inputsMask = nestedMask.apply(((_inputsMask == null)?NodeReferenceArray.createMask():_inputsMask));
            getDataMap().put("inputs", _inputsMask.getDataMap());
            return this;
        }

        /**
         * An array of node references which should be considered as input to apply the transformation function.
         * 
         */
        public Transformation.ProjectionMask withInputs() {
            _inputsMask = null;
            getDataMap().put("inputs", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * An array of node references which should be considered as input to apply the transformation function.
         * 
         */
        public Transformation.ProjectionMask withInputs(Function<com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask, com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _inputsMask = nestedMask.apply(((_inputsMask == null)?NodeReferenceArray.createMask():_inputsMask));
            getDataMap().put("inputs", _inputsMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("inputs").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("inputs").put("$count", count);
            }
            return this;
        }

        /**
         * An array of node references which should be considered as input to apply the transformation function.
         * 
         */
        public Transformation.ProjectionMask withInputs(Integer start, Integer count) {
            _inputsMask = null;
            getDataMap().put("inputs", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("inputs").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("inputs").put("$count", count);
            }
            return this;
        }

        /**
         * The transformation function.
         * 
         */
        public Transformation.ProjectionMask withFunction(Function<com.linkedin.feathr.compute.TransformationFunction.ProjectionMask, com.linkedin.feathr.compute.TransformationFunction.ProjectionMask> nestedMask) {
            _functionMask = nestedMask.apply(((_functionMask == null)?TransformationFunction.createMask():_functionMask));
            getDataMap().put("function", _functionMask.getDataMap());
            return this;
        }

        /**
         * The transformation function.
         * 
         */
        public Transformation.ProjectionMask withFunction() {
            _functionMask = null;
            getDataMap().put("function", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Feature name here is used so we retain feature name, type, and default values even after graph is resolved.
         * Feature name here is also used for feature aliasing in the case where TransformationFunction is feature_alias.
         * 
         */
        public Transformation.ProjectionMask withFeatureName() {
            getDataMap().put("featureName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * feature version of the feature
         * 
         */
        public Transformation.ProjectionMask withFeatureVersion(Function<com.linkedin.feathr.compute.FeatureVersion.ProjectionMask, com.linkedin.feathr.compute.FeatureVersion.ProjectionMask> nestedMask) {
            _featureVersionMask = nestedMask.apply(((_featureVersionMask == null)?FeatureVersion.createMask():_featureVersionMask));
            getDataMap().put("featureVersion", _featureVersionMask.getDataMap());
            return this;
        }

        /**
         * feature version of the feature
         * 
         */
        public Transformation.ProjectionMask withFeatureVersion() {
            _featureVersionMask = null;
            getDataMap().put("featureVersion", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
