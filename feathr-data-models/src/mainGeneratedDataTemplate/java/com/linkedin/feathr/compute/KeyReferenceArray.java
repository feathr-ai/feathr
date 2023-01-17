
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\NodeReference.pdl.")
public class KeyReferenceArray
    extends WrappingArrayTemplate<KeyReference>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.compute/**This represents the position of the key in the node which is being referred to. For example, if the original node has a key\r\nlike [x, y], and the keyReference says 1, it is referring to y.*/record KeyReference{/**Position in the original key array*/position:int}}]", SchemaFormatType.PDL));

    public KeyReferenceArray() {
        this(new DataList());
    }

    public KeyReferenceArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public KeyReferenceArray(Collection<KeyReference> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public KeyReferenceArray(DataList data) {
        super(data, SCHEMA, KeyReference.class);
    }

    public KeyReferenceArray(KeyReference first, KeyReference... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static KeyReferenceArray.ProjectionMask createMask() {
        return new KeyReferenceArray.ProjectionMask();
    }

    @Override
    public KeyReferenceArray clone()
        throws CloneNotSupportedException
    {
        KeyReferenceArray __clone = ((KeyReferenceArray) super.clone());
        return __clone;
    }

    @Override
    public KeyReferenceArray copy()
        throws CloneNotSupportedException
    {
        KeyReferenceArray __copy = ((KeyReferenceArray) super.copy());
        return __copy;
    }

    @Override
    protected KeyReference coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new KeyReference(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.compute.KeyReference.Fields items() {
            return new com.linkedin.feathr.compute.KeyReference.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.KeyReference.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public KeyReferenceArray.ProjectionMask withItems(Function<com.linkedin.feathr.compute.KeyReference.ProjectionMask, com.linkedin.feathr.compute.KeyReference.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?KeyReference.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
