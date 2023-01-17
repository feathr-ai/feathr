
package com.linkedin.feathr.compute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.UnionTemplate;
import com.linkedin.data.template.WrappingArrayTemplate;


/**
 * A node to represent a feature which is to be computed by using an already computed feature as the key.
 * https://iwww.corp.linkedin.com/wiki/cf/pages/viewpage.action?spaceKey=ENGS&title=feathr+Offline+User+Guide#FrameOfflineUserGuide-sequentialjoin
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\Lookup.pdl.")
public class Lookup
    extends RecordTemplate
{

    private final static Lookup.Fields _fields = new Lookup.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**A node to represent a feature which is to be computed by using an already computed feature as the key.\r\nhttps://iwww.corp.linkedin.com/wiki/cf/pages/viewpage.action?spaceKey=ENGS&title=feathr+Offline+User+Guide#FrameOfflineUserGuide-sequentialjoin*/record Lookup includes/**Generic abstraction of a node. All other nodes should derive from this node.*/record AbstractNode{/**The node would be represented by this id.*/id:/**A type ref to int node id*/typeref NodeId=int/**The key for which this node is being requested.\r\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\r\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\r\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\r\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)*/concreteKey:optional/**The key (node) for which the node in question is requested.*/record ConcreteKey{/**Most of the time, this should point to a CONTEXT SOURCE node, e.g. a key in the context called x.\r\nThe main exception would be for a Lookup feature, in which case it would point to another node where the lookup\r\nkey gets computed.*/key:array[NodeId]}}{/**An array of references to a node and keys.\r\n\r\nFor now, we do not support lookup of just a key reference, but we have added that as a placeholder.\r\n\r\nA node reference consists of node id and a key reference.\r\nIn sequential join the lookup key would be a combination of the\r\nfeature node representing the base feature (lookup node) and the key associated with it. For example,:-\r\nseqJoinFeature: {\r\n base: {key: x, feature: baseFeature}\r\n expansion: {key: y, feature: expansionFeature}\r\n aggregation: UNION\r\n}\r\nHere, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would\r\npoint to the index of \"x\" in the key array of baseFeature.*/lookupKey:array[union[/**This is used to refer to a node from another node. It is a combination of a node id and the indices of the keys from the\r\noriginal node array.\r\nFor example, consider:-\r\nanchorA: {\r\n key: [viewerId, vieweeId]\r\n feature: featureA\r\n}\r\nLet us say featureA is evaluated in node 1.\r\nderivation: {\r\n  key: [vieweeId, viewerId]\r\n  args1: {key: [vieweeId, viewerId], feature: featureA}\r\n  definition: args1*2\r\n}\r\nNow, the node reference (to represent args1) would be:\r\n nodeId: 1\r\n keyReference: [1,0] - // Indicates the ordering of the key indices.*/record NodeReference{/**node id of the referring node.*/id:NodeId/**The key references in the keys of the referring node.*/keyReference:array[/**This represents the position of the key in the node which is being referred to. For example, if the original node has a key\r\nlike [x, y], and the keyReference says 1, it is referring to y.*/record KeyReference{/**Position in the original key array*/position:int}]}KeyReference]]/**The node id of the node containing the expansion feature.*/lookupNode:NodeId/**Aggregation type as listed in\r\nhttps://jarvis.corp.linkedin.com/codesearch/result/\r\n?name=FeatureAggregationType.java&path=feathr-common%2Fframe-common%2Fsrc%2Fmain%2Fjava%2Fcom%2Flinkedin%2Fframe%2Fcommon&reponame=feathr%2Fframe-common#7\r\n*/aggregation:string/**feature name of the feature which would be computed.\r\nwe need feature name here for 2 main reasons.\r\n1. For type information. There are existing APIs that create a map from feature name -> type info from FR model and\r\nwe want to leverage that.\r\n2. For default values. Similar to above, there are existing APIs which create default value map from feature name ->\r\ndefault value.*/featureName:string/**feature version of the feature*/featureVersion:record FeatureVersion{/**Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of feathr before Tensorization and will be kept around until a full transition to Tensor types is completed*/type:/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of feathr. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO - this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}=\"UNSPECIFIED\"/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. feathr will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.*/format:optional/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in feathr, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in feathr and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and feathr.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and feathr.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime.*/shape:optional int}]}/**An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.*/defaultValue:optional/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]}}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private ConcreteKey _concreteKeyField = null;
    private Lookup.LookupKeyArray _lookupKeyField = null;
    private Integer _lookupNodeField = null;
    private String _aggregationField = null;
    private String _featureNameField = null;
    private FeatureVersion _featureVersionField = null;
    private Lookup.ChangeListener __changeListener = new Lookup.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_ConcreteKey = SCHEMA.getField("concreteKey");
    private final static RecordDataSchema.Field FIELD_LookupKey = SCHEMA.getField("lookupKey");
    private final static RecordDataSchema.Field FIELD_LookupNode = SCHEMA.getField("lookupNode");
    private final static RecordDataSchema.Field FIELD_Aggregation = SCHEMA.getField("aggregation");
    private final static RecordDataSchema.Field FIELD_FeatureName = SCHEMA.getField("featureName");
    private final static RecordDataSchema.Field FIELD_FeatureVersion = SCHEMA.getField("featureVersion");

    public Lookup() {
        super(new DataMap(10, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public Lookup(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Lookup.Fields fields() {
        return _fields;
    }

    public static Lookup.ProjectionMask createMask() {
        return new Lookup.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see Lookup.Fields#id
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
     * @see Lookup.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see Lookup.Fields#id
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
     * @see Lookup.Fields#id
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
     * @see Lookup.Fields#id
     */
    public Lookup setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.Lookup");
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
     * @see Lookup.Fields#id
     */
    public Lookup setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see Lookup.Fields#id
     */
    public Lookup setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for concreteKey
     * 
     * @see Lookup.Fields#concreteKey
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
     * @see Lookup.Fields#concreteKey
     */
    public void removeConcreteKey() {
        super._map.remove("concreteKey");
    }

    /**
     * Getter for concreteKey
     * 
     * @see Lookup.Fields#concreteKey
     */
    public ConcreteKey getConcreteKey(GetMode mode) {
        return getConcreteKey();
    }

    /**
     * Getter for concreteKey
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Lookup.Fields#concreteKey
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
     * @see Lookup.Fields#concreteKey
     */
    public Lookup setConcreteKey(ConcreteKey value, SetMode mode) {
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
     * @see Lookup.Fields#concreteKey
     */
    public Lookup setConcreteKey(
        @Nonnull
        ConcreteKey value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field concreteKey of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
            _concreteKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public boolean hasLookupKey() {
        if (_lookupKeyField!= null) {
            return true;
        }
        return super._map.containsKey("lookupKey");
    }

    /**
     * Remover for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public void removeLookupKey() {
        super._map.remove("lookupKey");
    }

    /**
     * Getter for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public Lookup.LookupKeyArray getLookupKey(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getLookupKey();
            case DEFAULT:
            case NULL:
                if (_lookupKeyField!= null) {
                    return _lookupKeyField;
                } else {
                    Object __rawValue = super._map.get("lookupKey");
                    _lookupKeyField = ((__rawValue == null)?null:new Lookup.LookupKeyArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _lookupKeyField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for lookupKey
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Lookup.Fields#lookupKey
     */
    @Nonnull
    public Lookup.LookupKeyArray getLookupKey() {
        if (_lookupKeyField!= null) {
            return _lookupKeyField;
        } else {
            Object __rawValue = super._map.get("lookupKey");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("lookupKey");
            }
            _lookupKeyField = ((__rawValue == null)?null:new Lookup.LookupKeyArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _lookupKeyField;
        }
    }

    /**
     * Setter for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public Lookup setLookupKey(Lookup.LookupKeyArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLookupKey(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field lookupKey of com.linkedin.feathr.compute.Lookup");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
                    _lookupKeyField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLookupKey();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
                    _lookupKeyField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
                    _lookupKeyField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for lookupKey
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Lookup.Fields#lookupKey
     */
    public Lookup setLookupKey(
        @Nonnull
        Lookup.LookupKeyArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field lookupKey of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
            _lookupKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public boolean hasLookupNode() {
        if (_lookupNodeField!= null) {
            return true;
        }
        return super._map.containsKey("lookupNode");
    }

    /**
     * Remover for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public void removeLookupNode() {
        super._map.remove("lookupNode");
    }

    /**
     * Getter for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public Integer getLookupNode(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getLookupNode();
            case DEFAULT:
            case NULL:
                if (_lookupNodeField!= null) {
                    return _lookupNodeField;
                } else {
                    Object __rawValue = super._map.get("lookupNode");
                    _lookupNodeField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _lookupNodeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for lookupNode
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Lookup.Fields#lookupNode
     */
    @Nonnull
    public Integer getLookupNode() {
        if (_lookupNodeField!= null) {
            return _lookupNodeField;
        } else {
            Object __rawValue = super._map.get("lookupNode");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("lookupNode");
            }
            _lookupNodeField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _lookupNodeField;
        }
    }

    /**
     * Setter for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public Lookup setLookupNode(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLookupNode(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field lookupNode of com.linkedin.feathr.compute.Lookup");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
                    _lookupNodeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLookupNode();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
                    _lookupNodeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
                    _lookupNodeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for lookupNode
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Lookup.Fields#lookupNode
     */
    public Lookup setLookupNode(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field lookupNode of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
            _lookupNodeField = value;
        }
        return this;
    }

    /**
     * Setter for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public Lookup setLookupNode(int value) {
        CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
        _lookupNodeField = value;
        return this;
    }

    /**
     * Existence checker for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public boolean hasAggregation() {
        if (_aggregationField!= null) {
            return true;
        }
        return super._map.containsKey("aggregation");
    }

    /**
     * Remover for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public void removeAggregation() {
        super._map.remove("aggregation");
    }

    /**
     * Getter for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public String getAggregation(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getAggregation();
            case DEFAULT:
            case NULL:
                if (_aggregationField!= null) {
                    return _aggregationField;
                } else {
                    Object __rawValue = super._map.get("aggregation");
                    _aggregationField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _aggregationField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for aggregation
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Lookup.Fields#aggregation
     */
    @Nonnull
    public String getAggregation() {
        if (_aggregationField!= null) {
            return _aggregationField;
        } else {
            Object __rawValue = super._map.get("aggregation");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("aggregation");
            }
            _aggregationField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _aggregationField;
        }
    }

    /**
     * Setter for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public Lookup setAggregation(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setAggregation(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field aggregation of com.linkedin.feathr.compute.Lookup");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
                    _aggregationField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeAggregation();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
                    _aggregationField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
                    _aggregationField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for aggregation
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Lookup.Fields#aggregation
     */
    public Lookup setAggregation(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field aggregation of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
            _aggregationField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureName
     * 
     * @see Lookup.Fields#featureName
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
     * @see Lookup.Fields#featureName
     */
    public void removeFeatureName() {
        super._map.remove("featureName");
    }

    /**
     * Getter for featureName
     * 
     * @see Lookup.Fields#featureName
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
     * @see Lookup.Fields#featureName
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
     * @see Lookup.Fields#featureName
     */
    public Lookup setFeatureName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureName of com.linkedin.feathr.compute.Lookup");
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
     * @see Lookup.Fields#featureName
     */
    public Lookup setFeatureName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureName of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureName", value);
            _featureNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureVersion
     * 
     * @see Lookup.Fields#featureVersion
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
     * @see Lookup.Fields#featureVersion
     */
    public void removeFeatureVersion() {
        super._map.remove("featureVersion");
    }

    /**
     * Getter for featureVersion
     * 
     * @see Lookup.Fields#featureVersion
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
     * @see Lookup.Fields#featureVersion
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
     * @see Lookup.Fields#featureVersion
     */
    public Lookup setFeatureVersion(FeatureVersion value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureVersion(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureVersion of com.linkedin.feathr.compute.Lookup");
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
     * @see Lookup.Fields#featureVersion
     */
    public Lookup setFeatureVersion(
        @Nonnull
        FeatureVersion value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureVersion of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureVersion", value.data());
            _featureVersionField = value;
        }
        return this;
    }

    @Override
    public Lookup clone()
        throws CloneNotSupportedException
    {
        Lookup __clone = ((Lookup) super.clone());
        __clone.__changeListener = new Lookup.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Lookup copy()
        throws CloneNotSupportedException
    {
        Lookup __copy = ((Lookup) super.copy());
        __copy._lookupKeyField = null;
        __copy._featureNameField = null;
        __copy._lookupNodeField = null;
        __copy._aggregationField = null;
        __copy._idField = null;
        __copy._concreteKeyField = null;
        __copy._featureVersionField = null;
        __copy.__changeListener = new Lookup.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Lookup __objectRef;

        private ChangeListener(Lookup reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "lookupKey":
                    __objectRef._lookupKeyField = null;
                    break;
                case "featureName":
                    __objectRef._featureNameField = null;
                    break;
                case "lookupNode":
                    __objectRef._lookupNodeField = null;
                    break;
                case "aggregation":
                    __objectRef._aggregationField = null;
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
         * An array of references to a node and keys.
         * 
         * For now, we do not support lookup of just a key reference, but we have added that as a placeholder.
         * 
         * A node reference consists of node id and a key reference.
         * In sequential join the lookup key would be a combination of the
         * feature node representing the base feature (lookup node) and the key associated with it. For example,:-
         * seqJoinFeature: {
         *  base: {key: x, feature: baseFeature}
         *  expansion: {key: y, feature: expansionFeature}
         *  aggregation: UNION
         * }
         * Here, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would
         * point to the index of "x" in the key array of baseFeature.
         * 
         */
        public com.linkedin.feathr.compute.Lookup.LookupKeyArray.Fields lookupKey() {
            return new com.linkedin.feathr.compute.Lookup.LookupKeyArray.Fields(getPathComponents(), "lookupKey");
        }

        /**
         * An array of references to a node and keys.
         * 
         * For now, we do not support lookup of just a key reference, but we have added that as a placeholder.
         * 
         * A node reference consists of node id and a key reference.
         * In sequential join the lookup key would be a combination of the
         * feature node representing the base feature (lookup node) and the key associated with it. For example,:-
         * seqJoinFeature: {
         *  base: {key: x, feature: baseFeature}
         *  expansion: {key: y, feature: expansionFeature}
         *  aggregation: UNION
         * }
         * Here, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would
         * point to the index of "x" in the key array of baseFeature.
         * 
         */
        public PathSpec lookupKey(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "lookupKey");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * The node id of the node containing the expansion feature.
         * 
         */
        public PathSpec lookupNode() {
            return new PathSpec(getPathComponents(), "lookupNode");
        }

        /**
         * Aggregation type as listed in
         * https://jarvis.corp.linkedin.com/codesearch/result/
         * ?name=FeatureAggregationType.java&path=feathr-common%2Fframe-common%2Fsrc%2Fmain%2Fjava%2Fcom%2Flinkedin%2Fframe%2Fcommon&reponame=feathr%2Fframe-common#7
         * 
         * 
         */
        public PathSpec aggregation() {
            return new PathSpec(getPathComponents(), "aggregation");
        }

        /**
         * feature name of the feature which would be computed.
         * we need feature name here for 2 main reasons.
         *  1. For type information. There are existing APIs that create a map from feature name -> type info from FR model and
         * we want to leverage that.
         *  2. For default values. Similar to above, there are existing APIs which create default value map from feature name ->
         * default value.
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

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\Lookup.pdl.")
    public static class LookupKey
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute/**This is used to refer to a node from another node. It is a combination of a node id and the indices of the keys from the\r\noriginal node array.\r\nFor example, consider:-\r\nanchorA: {\r\n key: [viewerId, vieweeId]\r\n feature: featureA\r\n}\r\nLet us say featureA is evaluated in node 1.\r\nderivation: {\r\n  key: [vieweeId, viewerId]\r\n  args1: {key: [vieweeId, viewerId], feature: featureA}\r\n  definition: args1*2\r\n}\r\nNow, the node reference (to represent args1) would be:\r\n nodeId: 1\r\n keyReference: [1,0] - // Indicates the ordering of the key indices.*/record NodeReference{/**node id of the referring node.*/id:/**A type ref to int node id*/typeref NodeId=int/**The key references in the keys of the referring node.*/keyReference:array[/**This represents the position of the key in the node which is being referred to. For example, if the original node has a key\r\nlike [x, y], and the keyReference says 1, it is referring to y.*/record KeyReference{/**Position in the original key array*/position:int}]}}com.linkedin.feathr.compute.KeyReference]", SchemaFormatType.PDL));
        private com.linkedin.feathr.compute.NodeReference _nodeReferenceMember = null;
        private com.linkedin.feathr.compute.KeyReference _keyReferenceMember = null;
        private Lookup.LookupKey.ChangeListener __changeListener = new Lookup.LookupKey.ChangeListener(this);
        private final static DataSchema MEMBER_NodeReference = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.NodeReference");
        private final static DataSchema MEMBER_KeyReference = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.KeyReference");

        public LookupKey() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public LookupKey(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static Lookup.LookupKey create(com.linkedin.feathr.compute.NodeReference value) {
            Lookup.LookupKey newUnion = new Lookup.LookupKey();
            newUnion.setNodeReference(value);
            return newUnion;
        }

        public boolean isNodeReference() {
            return memberIs("com.linkedin.feathr.compute.NodeReference");
        }

        public com.linkedin.feathr.compute.NodeReference getNodeReference() {
            checkNotNull();
            if (_nodeReferenceMember!= null) {
                return _nodeReferenceMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.NodeReference");
            _nodeReferenceMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.NodeReference(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _nodeReferenceMember;
        }

        public void setNodeReference(com.linkedin.feathr.compute.NodeReference value) {
            checkNotNull();
            super._map.clear();
            _nodeReferenceMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.NodeReference", value.data());
        }

        public static Lookup.LookupKey create(com.linkedin.feathr.compute.KeyReference value) {
            Lookup.LookupKey newUnion = new Lookup.LookupKey();
            newUnion.setKeyReference(value);
            return newUnion;
        }

        public boolean isKeyReference() {
            return memberIs("com.linkedin.feathr.compute.KeyReference");
        }

        public com.linkedin.feathr.compute.KeyReference getKeyReference() {
            checkNotNull();
            if (_keyReferenceMember!= null) {
                return _keyReferenceMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.KeyReference");
            _keyReferenceMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.KeyReference(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _keyReferenceMember;
        }

        public void setKeyReference(com.linkedin.feathr.compute.KeyReference value) {
            checkNotNull();
            super._map.clear();
            _keyReferenceMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.KeyReference", value.data());
        }

        public static Lookup.LookupKey.ProjectionMask createMask() {
            return new Lookup.LookupKey.ProjectionMask();
        }

        @Override
        public Lookup.LookupKey clone()
            throws CloneNotSupportedException
        {
            Lookup.LookupKey __clone = ((Lookup.LookupKey) super.clone());
            __clone.__changeListener = new Lookup.LookupKey.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public Lookup.LookupKey copy()
            throws CloneNotSupportedException
        {
            Lookup.LookupKey __copy = ((Lookup.LookupKey) super.copy());
            __copy._keyReferenceMember = null;
            __copy._nodeReferenceMember = null;
            __copy.__changeListener = new Lookup.LookupKey.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final Lookup.LookupKey __objectRef;

            private ChangeListener(Lookup.LookupKey reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.compute.KeyReference":
                        __objectRef._keyReferenceMember = null;
                        break;
                    case "com.linkedin.feathr.compute.NodeReference":
                        __objectRef._nodeReferenceMember = null;
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

            public com.linkedin.feathr.compute.NodeReference.Fields NodeReference() {
                return new com.linkedin.feathr.compute.NodeReference.Fields(getPathComponents(), "com.linkedin.feathr.compute.NodeReference");
            }

            public com.linkedin.feathr.compute.KeyReference.Fields KeyReference() {
                return new com.linkedin.feathr.compute.KeyReference.Fields(getPathComponents(), "com.linkedin.feathr.compute.KeyReference");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.NodeReference.ProjectionMask _NodeReferenceMask;
            private com.linkedin.feathr.compute.KeyReference.ProjectionMask _KeyReferenceMask;

            ProjectionMask() {
                super(3);
            }

            public Lookup.LookupKey.ProjectionMask withNodeReference(Function<com.linkedin.feathr.compute.NodeReference.ProjectionMask, com.linkedin.feathr.compute.NodeReference.ProjectionMask> nestedMask) {
                _NodeReferenceMask = nestedMask.apply(((_NodeReferenceMask == null)?com.linkedin.feathr.compute.NodeReference.createMask():_NodeReferenceMask));
                getDataMap().put("com.linkedin.feathr.compute.NodeReference", _NodeReferenceMask.getDataMap());
                return this;
            }

            public Lookup.LookupKey.ProjectionMask withKeyReference(Function<com.linkedin.feathr.compute.KeyReference.ProjectionMask, com.linkedin.feathr.compute.KeyReference.ProjectionMask> nestedMask) {
                _KeyReferenceMask = nestedMask.apply(((_KeyReferenceMask == null)?com.linkedin.feathr.compute.KeyReference.createMask():_KeyReferenceMask));
                getDataMap().put("com.linkedin.feathr.compute.KeyReference", _KeyReferenceMask.getDataMap());
                return this;
            }

        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\Lookup.pdl.")
    public static class LookupKeyArray
        extends WrappingArrayTemplate<Lookup.LookupKey>
    {

        private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[union[{namespace com.linkedin.feathr.compute/**This is used to refer to a node from another node. It is a combination of a node id and the indices of the keys from the\r\noriginal node array.\r\nFor example, consider:-\r\nanchorA: {\r\n key: [viewerId, vieweeId]\r\n feature: featureA\r\n}\r\nLet us say featureA is evaluated in node 1.\r\nderivation: {\r\n  key: [vieweeId, viewerId]\r\n  args1: {key: [vieweeId, viewerId], feature: featureA}\r\n  definition: args1*2\r\n}\r\nNow, the node reference (to represent args1) would be:\r\n nodeId: 1\r\n keyReference: [1,0] - // Indicates the ordering of the key indices.*/record NodeReference{/**node id of the referring node.*/id:/**A type ref to int node id*/typeref NodeId=int/**The key references in the keys of the referring node.*/keyReference:array[/**This represents the position of the key in the node which is being referred to. For example, if the original node has a key\r\nlike [x, y], and the keyReference says 1, it is referring to y.*/record KeyReference{/**Position in the original key array*/position:int}]}}com.linkedin.feathr.compute.KeyReference]]", SchemaFormatType.PDL));

        public LookupKeyArray() {
            this(new DataList());
        }

        public LookupKeyArray(int initialCapacity) {
            this(new DataList(initialCapacity));
        }

        public LookupKeyArray(Collection<Lookup.LookupKey> c) {
            this(new DataList(c.size()));
            addAll(c);
        }

        public LookupKeyArray(DataList data) {
            super(data, SCHEMA, Lookup.LookupKey.class);
        }

        public LookupKeyArray(Lookup.LookupKey first, Lookup.LookupKey... rest) {
            this(new DataList((rest.length + 1)));
            add(first);
            addAll(Arrays.asList(rest));
        }

        public static ArrayDataSchema dataSchema() {
            return SCHEMA;
        }

        public static Lookup.LookupKeyArray.ProjectionMask createMask() {
            return new Lookup.LookupKeyArray.ProjectionMask();
        }

        @Override
        public Lookup.LookupKeyArray clone()
            throws CloneNotSupportedException
        {
            Lookup.LookupKeyArray __clone = ((Lookup.LookupKeyArray) super.clone());
            return __clone;
        }

        @Override
        public Lookup.LookupKeyArray copy()
            throws CloneNotSupportedException
        {
            Lookup.LookupKeyArray __copy = ((Lookup.LookupKeyArray) super.copy());
            return __copy;
        }

        @Override
        protected Lookup.LookupKey coerceOutput(Object object)
            throws TemplateOutputCastException
        {
            assert(object != null);
            return ((object == null)?null:new Lookup.LookupKey(object));
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

            public com.linkedin.feathr.compute.Lookup.LookupKey.Fields items() {
                return new com.linkedin.feathr.compute.Lookup.LookupKey.Fields(getPathComponents(), PathSpec.WILDCARD);
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.Lookup.LookupKey.ProjectionMask _itemsMask;

            ProjectionMask() {
                super(4);
            }

            public Lookup.LookupKeyArray.ProjectionMask withItems(Function<com.linkedin.feathr.compute.Lookup.LookupKey.ProjectionMask, com.linkedin.feathr.compute.Lookup.LookupKey.ProjectionMask> nestedMask) {
                _itemsMask = nestedMask.apply(((_itemsMask == null)?Lookup.LookupKey.createMask():_itemsMask));
                getDataMap().put("$*", _itemsMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.ConcreteKey.ProjectionMask _concreteKeyMask;
        private com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask _lookupKeyMask;
        private com.linkedin.feathr.compute.FeatureVersion.ProjectionMask _featureVersionMask;

        ProjectionMask() {
            super(10);
        }

        /**
         * The node would be represented by this id.
         * 
         */
        public Lookup.ProjectionMask withId() {
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
        public Lookup.ProjectionMask withConcreteKey(Function<com.linkedin.feathr.compute.ConcreteKey.ProjectionMask, com.linkedin.feathr.compute.ConcreteKey.ProjectionMask> nestedMask) {
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
        public Lookup.ProjectionMask withConcreteKey() {
            _concreteKeyMask = null;
            getDataMap().put("concreteKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * An array of references to a node and keys.
         * 
         * For now, we do not support lookup of just a key reference, but we have added that as a placeholder.
         * 
         * A node reference consists of node id and a key reference.
         * In sequential join the lookup key would be a combination of the
         * feature node representing the base feature (lookup node) and the key associated with it. For example,:-
         * seqJoinFeature: {
         *  base: {key: x, feature: baseFeature}
         *  expansion: {key: y, feature: expansionFeature}
         *  aggregation: UNION
         * }
         * Here, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would
         * point to the index of "x" in the key array of baseFeature.
         * 
         */
        public Lookup.ProjectionMask withLookupKey(Function<com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask, com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask> nestedMask) {
            _lookupKeyMask = nestedMask.apply(((_lookupKeyMask == null)?Lookup.LookupKeyArray.createMask():_lookupKeyMask));
            getDataMap().put("lookupKey", _lookupKeyMask.getDataMap());
            return this;
        }

        /**
         * An array of references to a node and keys.
         * 
         * For now, we do not support lookup of just a key reference, but we have added that as a placeholder.
         * 
         * A node reference consists of node id and a key reference.
         * In sequential join the lookup key would be a combination of the
         * feature node representing the base feature (lookup node) and the key associated with it. For example,:-
         * seqJoinFeature: {
         *  base: {key: x, feature: baseFeature}
         *  expansion: {key: y, feature: expansionFeature}
         *  aggregation: UNION
         * }
         * Here, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would
         * point to the index of "x" in the key array of baseFeature.
         * 
         */
        public Lookup.ProjectionMask withLookupKey() {
            _lookupKeyMask = null;
            getDataMap().put("lookupKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * An array of references to a node and keys.
         * 
         * For now, we do not support lookup of just a key reference, but we have added that as a placeholder.
         * 
         * A node reference consists of node id and a key reference.
         * In sequential join the lookup key would be a combination of the
         * feature node representing the base feature (lookup node) and the key associated with it. For example,:-
         * seqJoinFeature: {
         *  base: {key: x, feature: baseFeature}
         *  expansion: {key: y, feature: expansionFeature}
         *  aggregation: UNION
         * }
         * Here, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would
         * point to the index of "x" in the key array of baseFeature.
         * 
         */
        public Lookup.ProjectionMask withLookupKey(Function<com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask, com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _lookupKeyMask = nestedMask.apply(((_lookupKeyMask == null)?Lookup.LookupKeyArray.createMask():_lookupKeyMask));
            getDataMap().put("lookupKey", _lookupKeyMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("lookupKey").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("lookupKey").put("$count", count);
            }
            return this;
        }

        /**
         * An array of references to a node and keys.
         * 
         * For now, we do not support lookup of just a key reference, but we have added that as a placeholder.
         * 
         * A node reference consists of node id and a key reference.
         * In sequential join the lookup key would be a combination of the
         * feature node representing the base feature (lookup node) and the key associated with it. For example,:-
         * seqJoinFeature: {
         *  base: {key: x, feature: baseFeature}
         *  expansion: {key: y, feature: expansionFeature}
         *  aggregation: UNION
         * }
         * Here, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would
         * point to the index of "x" in the key array of baseFeature.
         * 
         */
        public Lookup.ProjectionMask withLookupKey(Integer start, Integer count) {
            _lookupKeyMask = null;
            getDataMap().put("lookupKey", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("lookupKey").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("lookupKey").put("$count", count);
            }
            return this;
        }

        /**
         * The node id of the node containing the expansion feature.
         * 
         */
        public Lookup.ProjectionMask withLookupNode() {
            getDataMap().put("lookupNode", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Aggregation type as listed in
         * https://jarvis.corp.linkedin.com/codesearch/result/
         * ?name=FeatureAggregationType.java&path=feathr-common%2Fframe-common%2Fsrc%2Fmain%2Fjava%2Fcom%2Flinkedin%2Fframe%2Fcommon&reponame=feathr%2Fframe-common#7
         * 
         * 
         */
        public Lookup.ProjectionMask withAggregation() {
            getDataMap().put("aggregation", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * feature name of the feature which would be computed.
         * we need feature name here for 2 main reasons.
         *  1. For type information. There are existing APIs that create a map from feature name -> type info from FR model and
         * we want to leverage that.
         *  2. For default values. Similar to above, there are existing APIs which create default value map from feature name ->
         * default value.
         * 
         */
        public Lookup.ProjectionMask withFeatureName() {
            getDataMap().put("featureName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * feature version of the feature
         * 
         */
        public Lookup.ProjectionMask withFeatureVersion(Function<com.linkedin.feathr.compute.FeatureVersion.ProjectionMask, com.linkedin.feathr.compute.FeatureVersion.ProjectionMask> nestedMask) {
            _featureVersionMask = nestedMask.apply(((_featureVersionMask == null)?FeatureVersion.createMask():_featureVersionMask));
            getDataMap().put("featureVersion", _featureVersionMask.getDataMap());
            return this;
        }

        /**
         * feature version of the feature
         * 
         */
        public Lookup.ProjectionMask withFeatureVersion() {
            _featureVersionMask = null;
            getDataMap().put("featureVersion", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
