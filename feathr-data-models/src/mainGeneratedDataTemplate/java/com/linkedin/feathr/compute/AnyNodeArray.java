
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/ComputeGraph.pdl.")
public class AnyNodeArray
    extends WrappingArrayTemplate<AnyNode>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[union[{namespace com.linkedin.feathr.compute,record Aggregation includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{input:record NodeReference{id:NodeId,keyReference:array[record KeyReference{position:int}]}function:record AggregationFunction{operator:typeref OperatorId=string,parameters:optional map[string,string]}featureName:optional string}}{namespace com.linkedin.feathr.compute,record DataSource includes AbstractNode{sourceType:enum DataSourceType{UPDATE,EVENT,CONTEXT}externalSourceRef:string,keyExpression:string,keyExpressionType:enum KeyExpressionType{MVEL,SQL,UDF}filePartitionFormat:optional string,timestampColumnInfo:optional record TimestampCol{expression:string,format:string}window:optional{namespace com.linkedin.feathr.featureDataModel/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}}}}{namespace com.linkedin.feathr.compute,record Lookup includes AbstractNode{lookupKey:array[union[NodeReference,KeyReference]]lookupNode:NodeId,aggregation:string,featureName:optional string}}{namespace com.linkedin.feathr.compute,record Transformation includes AbstractNode{inputs:array[NodeReference]function:record TransformationFunction{operator:OperatorId,parameters:optional map[string,string]}featureName:optional string}}{namespace com.linkedin.feathr.compute,record External includes AbstractNode{name:string}}]]", SchemaFormatType.PDL));

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
