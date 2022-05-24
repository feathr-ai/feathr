
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/PinotDataSource.pdl.")
public class MvelExpressionArray
    extends WrappingArrayTemplate<MvelExpression>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}]", SchemaFormatType.PDL));

    public MvelExpressionArray() {
        this(new DataList());
    }

    public MvelExpressionArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public MvelExpressionArray(Collection<MvelExpression> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public MvelExpressionArray(DataList data) {
        super(data, SCHEMA, MvelExpression.class);
    }

    public MvelExpressionArray(MvelExpression first, MvelExpression... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static MvelExpressionArray.ProjectionMask createMask() {
        return new MvelExpressionArray.ProjectionMask();
    }

    @Override
    public MvelExpressionArray clone()
        throws CloneNotSupportedException
    {
        MvelExpressionArray __clone = ((MvelExpressionArray) super.clone());
        return __clone;
    }

    @Override
    public MvelExpressionArray copy()
        throws CloneNotSupportedException
    {
        MvelExpressionArray __copy = ((MvelExpressionArray) super.copy());
        return __copy;
    }

    @Override
    protected MvelExpression coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new MvelExpression(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.featureDataModel.MvelExpression.Fields items() {
            return new com.linkedin.feathr.featureDataModel.MvelExpression.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public MvelExpressionArray.ProjectionMask withItems(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?MvelExpression.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
