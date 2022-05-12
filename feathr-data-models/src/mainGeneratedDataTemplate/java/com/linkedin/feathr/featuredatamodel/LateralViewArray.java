
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

@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowAggregation.pdl.")
public class LateralViewArray
    extends WrappingArrayTemplate<LateralView>
{

    private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[{namespace com.linkedin.feathr.featureDataModel/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}}]", SchemaFormatType.PDL));

    public LateralViewArray() {
        this(new DataList());
    }

    public LateralViewArray(int initialCapacity) {
        this(new DataList(initialCapacity));
    }

    public LateralViewArray(Collection<LateralView> c) {
        this(new DataList(c.size()));
        addAll(c);
    }

    public LateralViewArray(DataList data) {
        super(data, SCHEMA, LateralView.class);
    }

    public LateralViewArray(LateralView first, LateralView... rest) {
        this(new DataList((rest.length + 1)));
        add(first);
        addAll(Arrays.asList(rest));
    }

    public static ArrayDataSchema dataSchema() {
        return SCHEMA;
    }

    public static LateralViewArray.ProjectionMask createMask() {
        return new LateralViewArray.ProjectionMask();
    }

    @Override
    public LateralViewArray clone()
        throws CloneNotSupportedException
    {
        LateralViewArray __clone = ((LateralViewArray) super.clone());
        return __clone;
    }

    @Override
    public LateralViewArray copy()
        throws CloneNotSupportedException
    {
        LateralViewArray __copy = ((LateralViewArray) super.copy());
        return __copy;
    }

    @Override
    protected LateralView coerceOutput(Object object)
        throws TemplateOutputCastException
    {
        assert(object != null);
        return ((object == null)?null:new LateralView(DataTemplateUtil.castOrThrow(object, DataMap.class)));
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

        public com.linkedin.feathr.featureDataModel.LateralView.Fields items() {
            return new com.linkedin.feathr.featureDataModel.LateralView.Fields(getPathComponents(), PathSpec.WILDCARD);
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.LateralView.ProjectionMask _itemsMask;

        ProjectionMask() {
            super(4);
        }

        public LateralViewArray.ProjectionMask withItems(Function<com.linkedin.feathr.featureDataModel.LateralView.ProjectionMask, com.linkedin.feathr.featureDataModel.LateralView.ProjectionMask> nestedMask) {
            _itemsMask = nestedMask.apply(((_itemsMask == null)?LateralView.createMask():_itemsMask));
            getDataMap().put("$*", _itemsMask.getDataMap());
            return this;
        }

    }

}
