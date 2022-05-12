
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Transformation.pdl.")
public class NodeReferenceArray
    extends WrappingArrayTemplate<NodeReference>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.compute,record NodeReference{id:typeref NodeId=int,keyReference:array[record KeyReference{position:int}]}}]", SchemaFormatType.PDL));

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
