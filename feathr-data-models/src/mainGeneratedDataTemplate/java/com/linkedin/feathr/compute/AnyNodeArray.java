
package com.linkedin.feathr.compute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataList;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.WrappingArrayTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\ComputeGraph.pdl.")
public class AnyNodeArray
    extends WrappingArrayTemplate<AnyNode>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[union[{namespace com.linkedin.feathr.compute/**A node to represent an aggregation step. The aggregation inputs like the groupBy field, agg function are delegated to [[AggregationFunction]].\r\nThis node can represent a feature. As of now, in this step we will be using the SWA library from Spark-algorithms.*/record Aggregation includes/**Generic abstraction of a node. All other nodes should derive from this node.*/record AbstractNode{/**The node would be represented by this id.*/id:/**A type ref to int node id*/typeref NodeId=int/**The key for which this node is being requested.\r\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\r\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\r\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\r\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)*/concreteKey:optional/**The key (node) for which the node in question is requested.*/record ConcreteKey{/**Most of the time, this should point to a CONTEXT SOURCE node, e.g. a key in the context called x.\r\nThe main exception would be for a Lookup feature, in which case it would point to another node where the lookup\r\nkey gets computed.*/key:array[NodeId]}}{/**The input node on which aggregation is to be performed. As of now, we would only be supporting this node to be a data source node.*/input:/**This is used to refer to a node from another node. It is a combination of a node id and the indices of the keys from the\r\noriginal node array.\r\nFor example, consider:-\r\nanchorA: {\r\n key: [viewerId, vieweeId]\r\n feature: featureA\r\n}\r\nLet us say featureA is evaluated in node 1.\r\nderivation: {\r\n  key: [vieweeId, viewerId]\r\n  args1: {key: [vieweeId, viewerId], feature: featureA}\r\n  definition: args1*2\r\n}\r\nNow, the node reference (to represent args1) would be:\r\n nodeId: 1\r\n keyReference: [1,0] - // Indicates the ordering of the key indices.*/record NodeReference{/**node id of the referring node.*/id:NodeId/**The key references in the keys of the referring node.*/keyReference:array[/**This represents the position of the key in the node which is being referred to. For example, if the original node has a key\r\nlike [x, y], and the keyReference says 1, it is referring to y.*/record KeyReference{/**Position in the original key array*/position:int}]}/**All the aggregation related parameters and functions are bundled into this.*/function:/**All parameters related to an aggregation operation. This class should be used in conjunction with the [[Aggregation]] node.*/record AggregationFunction{/**The aggregation function.*/operator:/**operator id to set an operator. It can be referring to an mvel expression, sql expression or a java udf.*/typeref OperatorId=string/**All the aggregation parameters should be bundled into this map. For now, the possible parameters are:-\r\na. target_column -  Aggregation column\r\nb. window_size - aggregation window size\r\nc. window unit - aggregation window unit (ex - day, hour)\r\nd. lateral_view_expression - definition of a lateral view for the feature.\r\ne. lateral_view_table_alias - An alias for the lateral view\r\nf. filter - An expression to filter out any data before aggregation. Should be a sparkSql expression.\r\ng. groupBy - groupBy columns. Should be a sparkSql expression.*/parameters:optional map[string,string]}/**If the node is representing a feature, the feature name should be associated with the node.*/featureName:string/**feature version of the feature*/featureVersion:record FeatureVersion{/**Defines the high level semantic type of a feature.  The high level semantic types are supported in early version of feathr before Tensorization and will be kept around until a full transition to Tensor types is completed*/type:/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of feathr. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO - this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}=\"UNSPECIFIED\"/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. feathr will make some default assumptions if FeatureFormat is not provided, but this should be considered limited support, and format should be defined for all new features.*/format:optional/**Defines the format of feature data. Feature data is produced by applying transformation on source, in a FeatureAnchor. Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions). Each row defines a single key/value pair, each column can have a different type. For more details, refer to doc: https://docs.google.com/document/d/1D3JZWBwI7sgHrNzkHZwV3YNEHn69lZcl4VfhdHVmDJo/edit#. Currently in feathr, there are two ways to specify Feature formats, one is via Name-Term-Value (NTV) types (eg. NUMERIC, TERM_VECTOR, CATEGORICAL, see go/featuretypes), the other is via FML metadata (Feature Metadata Library, go/fml). For NTV types, there is a conversion path to Quince Tensor via Auto Tensorization. Existing NTV types can be mapped to different combinations of valueType and dimensionTypes in a deterministic manner. Refer to doc: https://docs.google.com/document/d/10bJMYlCixhsghCtyD08FsQaoQdAJMcpGnRyGe64TSr4/edit#. Feature owners can choose to define FML metadata (eg. valType, dimension's type, etc, see go/fml), which will also be converted to Quince Tensor internally. The data model in this class should be able to uniformly represent both cases.*/record TensorFeatureFormat{/**Type of the tensor, for example, dense tensor.*/tensorCategory:/**Supported Tensor categories in feathr and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}/**Type of the value column.*/valueType:/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and feathr.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}/**A feature data can have zero or more dimensions (columns that represent keys).*/dimensions:array[/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and feathr.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime.*/shape:optional int}]}/**An optional default value can be provided. In case of missing data or errors occurred while applying transformation on source in FeatureAnchor, the default value will be used to populate feature data.*/defaultValue:optional/**Defines supported types that can be used to represent the value of a feature data. An example usage is specifying feature's default value. It currently starts with scalar types and more complex types can be added along with more use cases.*/typeref FeatureValue=union[boolean,int,long,float,double,string,bytes]}}}{namespace com.linkedin.feathr.compute/**Representation of the datasource node. There are 3 types of datasource nodes:-\r\nContext - To represent the observation data entities (like the join key or passthrough feature columns)\r\nUpdate - To represent a non-timepartitioned datasource node.\r\nEvent - To represent a time-partitioned datasource node.\r\n\r\nTODO - Maybe, it makes sense more sense to refactor it by make this an abstract object, and deriving the three different nodes from it.*/record DataSource includes AbstractNode{/**Type of node, ie - Context, Update, Event*/sourceType:/**Type of datasource node.*/enum DataSourceType{/**Update data sources provide keyed data about entities. A fully specified table data source contains both a snapshot view and an update log.*/UPDATE/**Event data sources are append-only event logs whose records need to be grouped and aggregated (e.g. counted, averaged, top-K\u00e2\u20ac\u2122d)\r\nover a limited window of time.*/EVENT/**Reprent the observation data entities (like the join key or passthrough feature columns)*/CONTEXT}/**for CONTEXT type, this is the name of the context column. otherwise, it should be a path or URI.*/externalSourceRef:string/**Raw key expression as entered by the user. This hocon parsing happens at the execution engine side.*/keyExpression:string/**mvel or spark or user-defined class*/keyExpressionType:/**Different key formats supported.\r\nTodo - We probably do not want to generalize this as a kind of key-operator in the core compute model,\r\nwith instances such as for MVEL or SQL being available (e.g. via an OperatorId reference).*/enum KeyExpressionType{/**Java-based MVEL*/MVEL/**Spark-SQL*/SQL/**Custom java/scala UDF*/UDF}/**File partition format.*/filePartitionFormat:optional string/**Timestamp column info, to be available only for an event datasource node.*/timestampColumnInfo:optional/**Representation of a timestamp column field*/record TimestampCol{/**Timestamp column expression.*/expression:string/**Format of the timestamp, example - yyyy/MM/dd, epoch, epoch_millis*/format:string}}}{namespace com.linkedin.feathr.compute/**A node to represent a feature which is to be computed by using an already computed feature as the key.\r\nhttps://iwww.corp.linkedin.com/wiki/cf/pages/viewpage.action?spaceKey=ENGS&title=feathr+Offline+User+Guide#FrameOfflineUserGuide-sequentialjoin*/record Lookup includes AbstractNode{/**An array of references to a node and keys.\r\n\r\nFor now, we do not support lookup of just a key reference, but we have added that as a placeholder.\r\n\r\nA node reference consists of node id and a key reference.\r\nIn sequential join the lookup key would be a combination of the\r\nfeature node representing the base feature (lookup node) and the key associated with it. For example,:-\r\nseqJoinFeature: {\r\n base: {key: x, feature: baseFeature}\r\n expansion: {key: y, feature: expansionFeature}\r\n aggregation: UNION\r\n}\r\nHere, the lookupKey's node reference would point to the node which computes the base feature, and the keyReference would\r\npoint to the index of \"x\" in the key array of baseFeature.*/lookupKey:array[union[NodeReference,KeyReference]]/**The node id of the node containing the expansion feature.*/lookupNode:NodeId/**Aggregation type as listed in\r\nhttps://jarvis.corp.linkedin.com/codesearch/result/\r\n?name=FeatureAggregationType.java&path=feathr-common%2Fframe-common%2Fsrc%2Fmain%2Fjava%2Fcom%2Flinkedin%2Fframe%2Fcommon&reponame=feathr%2Fframe-common#7\r\n*/aggregation:string/**feature name of the feature which would be computed.\r\nwe need feature name here for 2 main reasons.\r\n1. For type information. There are existing APIs that create a map from feature name -> type info from FR model and\r\nwe want to leverage that.\r\n2. For default values. Similar to above, there are existing APIs which create default value map from feature name ->\r\ndefault value.*/featureName:string/**feature version of the feature*/featureVersion:FeatureVersion}}{namespace com.linkedin.feathr.compute/**Representation of a transformation node.*/record Transformation includes AbstractNode{/**An array of node references which should be considered as input to apply the transformation function.*/inputs:array[NodeReference]/**The transformation function.*/function:/**The transformation function*/record TransformationFunction{/**Indicates the operator type to be used here. The various different operators supported are in [[Operators]] class.\r\n*/operator:OperatorId/**The various attributes required to represent the transformation function are captured in a map format.\r\nFor example, mvel expression or java udf class name*/parameters:optional map[string,string]}/**Feature name here is used so we retain feature name, type, and default values even after graph is resolved.\r\nFeature name here is also used for feature aliasing in the case where TransformationFunction is feature_alias.*/featureName:string/**feature version of the feature*/featureVersion:FeatureVersion}}{namespace com.linkedin.feathr.compute/**A temporary node which would exist only while parsing the graph. For example, when parsing an object if there is a reference to a feature\r\nname, we will create an external node. This would get resolved later in the computation.*/record External includes AbstractNode{/**Name of the external object it should refer to.*/name:string}}]]", SchemaFormatType.PDL));

    public AnyNodeArray() {
        this(new DataList());
    }

    public AnyNodeArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public AnyNodeArray(Collection<AnyNode> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public AnyNodeArray(DataList data) {
        super(data, SCHEMA, AnyNode.class);
    }

    public AnyNodeArray(AnyNode first, AnyNode... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static AnyNodeArray.ProjectionMask createMask() {
        return new AnyNodeArray.ProjectionMask();
    }

    @Override
    public AnyNodeArray clone()
        throws CloneNotSupportedException
    {
        AnyNodeArray __clone = ((AnyNodeArray) super.clone());
        return __clone;
    }

    @Override
    public AnyNodeArray copy()
        throws CloneNotSupportedException
    {
        AnyNodeArray __copy = ((AnyNodeArray) super.copy());
        return __copy;
    }

    @Override
    protected AnyNode coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new AnyNode(object));
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

        public com.linkedin.feathr.compute.AnyNode.Fields items() {
            return new com.linkedin.feathr.compute.AnyNode.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.AnyNode.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public AnyNodeArray.ProjectionMask withItems(Function<com.linkedin.feathr.compute.AnyNode.ProjectionMask, com.linkedin.feathr.compute.AnyNode.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?AnyNode.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
