
package com.linkedin.feathr.featureDataModel;

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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/KeyPlaceholders.pdl.")
public class KeyPlaceholderArray
    extends WrappingArrayTemplate<KeyPlaceholder>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.featureDataModel/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}}]", SchemaFormatType.PDL));

    public KeyPlaceholderArray() {
        this(new DataList());
    }

    public KeyPlaceholderArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public KeyPlaceholderArray(Collection<KeyPlaceholder> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public KeyPlaceholderArray(DataList data) {
        super(data, SCHEMA, KeyPlaceholder.class);
    }

    public KeyPlaceholderArray(KeyPlaceholder first, KeyPlaceholder... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static KeyPlaceholderArray.ProjectionMask createMask() {
        return new KeyPlaceholderArray.ProjectionMask();
    }

    @Override
    public KeyPlaceholderArray clone()
        throws CloneNotSupportedException
    {
        KeyPlaceholderArray __clone = ((KeyPlaceholderArray) super.clone());
        return __clone;
    }

    @Override
    public KeyPlaceholderArray copy()
        throws CloneNotSupportedException
    {
        KeyPlaceholderArray __copy = ((KeyPlaceholderArray) super.copy());
        return __copy;
    }

    @Override
    protected KeyPlaceholder coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new KeyPlaceholder(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.featureDataModel.KeyPlaceholder.Fields items() {
            return new com.linkedin.feathr.featureDataModel.KeyPlaceholder.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholder.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public KeyPlaceholderArray.ProjectionMask withItems(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholder.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholder.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?KeyPlaceholder.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
