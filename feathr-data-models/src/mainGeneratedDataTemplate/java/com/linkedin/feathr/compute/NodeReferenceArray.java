
package com.linkedin.feathr.compute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.ArrayDataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.WrappingArrayTemplate;

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\Transformation.pdl.")
public class NodeReferenceArray
    extends WrappingArrayTemplate<NodeReference>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.compute/**This is used to refer to a node from another node. It is a combination of a node id and the indices of the keys from the\r\noriginal node array.\r\nFor example, consider:-\r\nanchorA: {\r\n key: [viewerId, vieweeId]\r\n feature: featureA\r\n}\r\nLet us say featureA is evaluated in node 1.\r\nderivation: {\r\n  key: [vieweeId, viewerId]\r\n  args1: {key: [vieweeId, viewerId], feature: featureA}\r\n  definition: args1*2\r\n}\r\nNow, the node reference (to represent args1) would be:\r\n nodeId: 1\r\n keyReference: [1,0] - // Indicates the ordering of the key indices.*/record NodeReference{/**node id of the referring node.*/id:/**A type ref to int node id*/typeref NodeId=int/**The key references in the keys of the referring node.*/keyReference:array[/**This represents the position of the key in the node which is being referred to. For example, if the original node has a key\r\nlike [x, y], and the keyReference says 1, it is referring to y.*/record KeyReference{/**Position in the original key array*/position:int}]}}]", SchemaFormatType.PDL));

    public NodeReferenceArray() {
        this(new DataList());
    }

    public NodeReferenceArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public NodeReferenceArray(Collection<NodeReference> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public NodeReferenceArray(DataList data) {
        super(data, SCHEMA, NodeReference.class);
    }

    public NodeReferenceArray(NodeReference first, NodeReference... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static NodeReferenceArray.ProjectionMask createMask() {
        return new NodeReferenceArray.ProjectionMask();
    }

    @Override
    public NodeReferenceArray clone()
        throws CloneNotSupportedException
    {
        NodeReferenceArray __clone = ((NodeReferenceArray) super.clone());
        return __clone;
    }

    @Override
    public NodeReferenceArray copy()
        throws CloneNotSupportedException
    {
        NodeReferenceArray __copy = ((NodeReferenceArray) super.copy());
        return __copy;
    }

    @Override
    protected NodeReference coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new NodeReference(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.compute.NodeReference.Fields items() {
            return new com.linkedin.feathr.compute.NodeReference.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.NodeReference.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public NodeReferenceArray.ProjectionMask withItems(Function<com.linkedin.feathr.compute.NodeReference.ProjectionMask, com.linkedin.feathr.compute.NodeReference.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?NodeReference.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
