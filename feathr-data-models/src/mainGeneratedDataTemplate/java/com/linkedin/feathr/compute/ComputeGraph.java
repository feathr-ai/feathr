
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
 * Feature computation graph.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/ComputeGraph.pdl.")
public class ComputeGraph
    extends RecordTemplate
{

    private final static ComputeGraph.Fields _fields = new ComputeGraph.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Feature computation graph.*/record ComputeGraph{/**The nodes in the graph (order does not matter)*/nodes:array[typeref AnyNode=union[record Aggregation includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{input:record NodeReference{id:NodeId,keyReference:array[record KeyReference{position:int}]}function:record AggregationFunction{operator:typeref OperatorId=string,parameters:optional map[string,string]}featureName:optional string}record DataSource includes AbstractNode{sourceType:enum DataSourceType{UPDATE,EVENT,CONTEXT}externalSourceRef:string,keyExpression:string,filePartitionFormat:optional string,timestampColumnInfo:optional record TimestampCol{expression:string,format:string}window:optional{namespace com.linkedin.feathr.featureDataModel/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}}}record Lookup includes AbstractNode{lookupKey:array[union[NodeReference,KeyReference]]lookupNode:NodeId,aggregation:string,featureName:optional string}record Transformation includes AbstractNode{inputs:array[NodeReference]function:record TransformationFunction{operator:OperatorId,parameters:optional map[string,string]}featureName:optional string}record External includes AbstractNode{name:string}]]/**Map from feature name to node ID, for those nodes in the graph that represent named features.*/featureNames:map[string,record FeatureDefintion{nodeId:int,featureVersion:/**A Feature can have multiple FeatureVersions. Versioning of a feature is declared by feature producers per semantic versioning. Every time the definition of a feature changes, a new FeatureVersion should be created. Each FeatureVersion enclosed attributes that don't change across environments.*/@gma.aspect.entity.urn=\"com.linkedin.common.MlFeatureVersionUrn\"record FeatureVersion{/**Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of Frame before Tensorization and will be kept around until a full transition to Tensor types is completed*/type:/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of Frame. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO(PROML-13658): this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}=\"UNSPECIFIED\"/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Frame will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.*/format:optional/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in Frame, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in Frame and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and Frame.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and Frame.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime. TODO(PROML): TODO(PROML-10173): To decide whether to use -1 or optional to indicate unknown shape in the week of 08/10/2020.*/shape:optional int}]}/**An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.*/defaultValue:optional/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]}}]}", SchemaFormatType.PDL));
    private AnyNodeArray _nodesField = null;
    private FeatureDefintionMap _featureNamesField = null;
    private ComputeGraph.ChangeListener __changeListener = new ComputeGraph.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Nodes = SCHEMA.getField("nodes");
    private final static RecordDataSchema.Field FIELD_FeatureNames = SCHEMA.getField("featureNames");

    public ComputeGraph() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public ComputeGraph(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static ComputeGraph.Fields fields() {
        return _fields;
    }

    public static ComputeGraph.ProjectionMask createMask() {
        return new ComputeGraph.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for nodes
     * 
     * @see ComputeGraph.Fields#nodes
     */
    public boolean hasNodes() {
        if (_nodesField!= null) {
            return true;
        }
        return super._map.containsKey("nodes");
    }

    /**
     * Remover for nodes
     * 
     * @see ComputeGraph.Fields#nodes
     */
    public void removeNodes() {
        super._map.remove("nodes");
    }

    /**
     * Getter for nodes
     * 
     * @see ComputeGraph.Fields#nodes
     */
    public AnyNodeArray getNodes(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getNodes();
            case DEFAULT:
            case NULL:
                if (_nodesField!= null) {
                    return _nodesField;
                } else {
                    Object __rawValue = super._map.get("nodes");
                    _nodesField = ((__rawValue == null)?null:new AnyNodeArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _nodesField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for nodes
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see ComputeGraph.Fields#nodes
     */
    @Nonnull
    public AnyNodeArray getNodes() {
        if (_nodesField!= null) {
            return _nodesField;
        } else {
            Object __rawValue = super._map.get("nodes");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("nodes");
            }
            _nodesField = ((__rawValue == null)?null:new AnyNodeArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _nodesField;
        }
    }

    /**
     * Setter for nodes
     * 
     * @see ComputeGraph.Fields#nodes
     */
    public ComputeGraph setNodes(AnyNodeArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setNodes(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field nodes of com.linkedin.feathr.compute.ComputeGraph");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "nodes", value.data());
                    _nodesField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeNodes();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "nodes", value.data());
                    _nodesField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "nodes", value.data());
                    _nodesField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for nodes
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see ComputeGraph.Fields#nodes
     */
    public ComputeGraph setNodes(
        @Nonnull
        AnyNodeArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field nodes of com.linkedin.feathr.compute.ComputeGraph to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "nodes", value.data());
            _nodesField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureNames
     * 
     * @see ComputeGraph.Fields#featureNames
     */
    public boolean hasFeatureNames() {
        if (_featureNamesField!= null) {
            return true;
        }
        return super._map.containsKey("featureNames");
    }

    /**
     * Remover for featureNames
     * 
     * @see ComputeGraph.Fields#featureNames
     */
    public void removeFeatureNames() {
        super._map.remove("featureNames");
    }

    /**
     * Getter for featureNames
     * 
     * @see ComputeGraph.Fields#featureNames
     */
    public FeatureDefintionMap getFeatureNames(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFeatureNames();
            case DEFAULT:
            case NULL:
                if (_featureNamesField!= null) {
                    return _featureNamesField;
                } else {
                    Object __rawValue = super._map.get("featureNames");
                    _featureNamesField = ((__rawValue == null)?null:new FeatureDefintionMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _featureNamesField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for featureNames
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see ComputeGraph.Fields#featureNames
     */
    @Nonnull
    public FeatureDefintionMap getFeatureNames() {
        if (_featureNamesField!= null) {
            return _featureNamesField;
        } else {
            Object __rawValue = super._map.get("featureNames");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("featureNames");
            }
            _featureNamesField = ((__rawValue == null)?null:new FeatureDefintionMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _featureNamesField;
        }
    }

    /**
     * Setter for featureNames
     * 
     * @see ComputeGraph.Fields#featureNames
     */
    public ComputeGraph setFeatureNames(FeatureDefintionMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureNames(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureNames of com.linkedin.feathr.compute.ComputeGraph");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureNames", value.data());
                    _featureNamesField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureNames();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureNames", value.data());
                    _featureNamesField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureNames", value.data());
                    _featureNamesField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureNames
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see ComputeGraph.Fields#featureNames
     */
    public ComputeGraph setFeatureNames(
        @Nonnull
        FeatureDefintionMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureNames of com.linkedin.feathr.compute.ComputeGraph to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureNames", value.data());
            _featureNamesField = value;
        }
        return this;
    }

    @Override
    public ComputeGraph clone()
        throws CloneNotSupportedException
    {
        ComputeGraph __clone = ((ComputeGraph) super.clone());
        __clone.__changeListener = new ComputeGraph.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public ComputeGraph copy()
        throws CloneNotSupportedException
    {
        ComputeGraph __copy = ((ComputeGraph) super.copy());
        __copy._nodesField = null;
        __copy._featureNamesField = null;
        __copy.__changeListener = new ComputeGraph.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final ComputeGraph __objectRef;

        private ChangeListener(ComputeGraph reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "nodes":
                    __objectRef._nodesField = null;
                    break;
                case "featureNames":
                    __objectRef._featureNamesField = null;
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
         * The nodes in the graph (order does not matter)
         * 
         */
        public com.linkedin.feathr.compute.AnyNodeArray.Fields nodes() {
            return new com.linkedin.feathr.compute.AnyNodeArray.Fields(getPathComponents(), "nodes");
        }

        /**
         * The nodes in the graph (order does not matter)
         * 
         */
        public PathSpec nodes(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "nodes");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Map from feature name to node ID, for those nodes in the graph that represent named features.
         * 
         */
        public com.linkedin.feathr.compute.FeatureDefintionMap.Fields featureNames() {
            return new com.linkedin.feathr.compute.FeatureDefintionMap.Fields(getPathComponents(), "featureNames");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.AnyNodeArray.ProjectionMask _nodesMask;
        private com.linkedin.feathr.compute.FeatureDefintionMap.ProjectionMask _featureNamesMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * The nodes in the graph (order does not matter)
         * 
         */
        public ComputeGraph.ProjectionMask withNodes(Function<com.linkedin.feathr.compute.AnyNodeArray.ProjectionMask, com.linkedin.feathr.compute.AnyNodeArray.ProjectionMask> nestedMask) {
            _nodesMask = nestedMask.apply(((_nodesMask == null)?AnyNodeArray.createMask():_nodesMask));
            getDataMap().put("nodes", _nodesMask.getDataMap());
            return this;
        }

        /**
         * The nodes in the graph (order does not matter)
         * 
         */
        public ComputeGraph.ProjectionMask withNodes() {
            _nodesMask = null;
            getDataMap().put("nodes", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The nodes in the graph (order does not matter)
         * 
         */
        public ComputeGraph.ProjectionMask withNodes(Function<com.linkedin.feathr.compute.AnyNodeArray.ProjectionMask, com.linkedin.feathr.compute.AnyNodeArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _nodesMask = nestedMask.apply(((_nodesMask == null)?AnyNodeArray.createMask():_nodesMask));
            getDataMap().put("nodes", _nodesMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("nodes").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("nodes").put("$count", count);
            }
            return this;
        }

        /**
         * The nodes in the graph (order does not matter)
         * 
         */
        public ComputeGraph.ProjectionMask withNodes(Integer start, Integer count) {
            _nodesMask = null;
            getDataMap().put("nodes", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("nodes").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("nodes").put("$count", count);
            }
            return this;
        }

        /**
         * Map from feature name to node ID, for those nodes in the graph that represent named features.
         * 
         */
        public ComputeGraph.ProjectionMask withFeatureNames(Function<com.linkedin.feathr.compute.FeatureDefintionMap.ProjectionMask, com.linkedin.feathr.compute.FeatureDefintionMap.ProjectionMask> nestedMask) {
            _featureNamesMask = nestedMask.apply(((_featureNamesMask == null)?FeatureDefintionMap.createMask():_featureNamesMask));
            getDataMap().put("featureNames", _featureNamesMask.getDataMap());
            return this;
        }

        /**
         * Map from feature name to node ID, for those nodes in the graph that represent named features.
         * 
         */
        public ComputeGraph.ProjectionMask withFeatureNames() {
            _featureNamesMask = null;
            getDataMap().put("featureNames", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
