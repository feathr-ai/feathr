
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\TensorFeatureFormat.pdl.")
public class DimensionArray
    extends WrappingArrayTemplate<Dimension>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.compute/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (aka dimensions).*/record Dimension{/**Type of the dimension in the tensor. Each dimension can have a different type.*/type:/**Supported dimension types for tensors in Quince and feathr.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}/**Size of the dimension in the tensor. If unset, it means the size is unknown and actual size will be determined at runtime.*/shape:optional int}}]", SchemaFormatType.PDL));

    public DimensionArray() {
        this(new DataList());
    }

    public DimensionArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public DimensionArray(Collection<Dimension> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public DimensionArray(DataList data) {
        super(data, SCHEMA, Dimension.class);
    }

    public DimensionArray(Dimension first, Dimension... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static DimensionArray.ProjectionMask createMask() {
        return new DimensionArray.ProjectionMask();
    }

    @Override
    public DimensionArray clone()
        throws CloneNotSupportedException
    {
        DimensionArray __clone = ((DimensionArray) super.clone());
        return __clone;
    }

    @Override
    public DimensionArray copy()
        throws CloneNotSupportedException
    {
        DimensionArray __copy = ((DimensionArray) super.copy());
        return __copy;
    }

    @Override
    protected Dimension coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new Dimension(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.compute.Dimension.Fields items() {
            return new com.linkedin.feathr.compute.Dimension.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.Dimension.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public DimensionArray.ProjectionMask withItems(Function<com.linkedin.feathr.compute.Dimension.ProjectionMask, com.linkedin.feathr.compute.Dimension.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?Dimension.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
