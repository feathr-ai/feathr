
package com.linkedin.feathr.compute;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.DataSchema;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.UnionDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.UnionTemplate;


/**
 * Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\LateralView.pdl.")
public class LateralView
    extends RecordTemplate
{

    private final static LateralView.Fields _fields = new LateralView.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}", SchemaFormatType.PDL));
    private LateralView.TableGeneratingFunction _tableGeneratingFunctionField = null;
    private String _virtualTableAliasField = null;
    private LateralView.ChangeListener __changeListener = new LateralView.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TableGeneratingFunction = SCHEMA.getField("tableGeneratingFunction");
    private final static RecordDataSchema.Field FIELD_VirtualTableAlias = SCHEMA.getField("virtualTableAlias");

    public LateralView() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public LateralView(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static LateralView.Fields fields() {
        return _fields;
    }

    public static LateralView.ProjectionMask createMask() {
        return new LateralView.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for tableGeneratingFunction
     * 
     * @see LateralView.Fields#tableGeneratingFunction
     */
    public boolean hasTableGeneratingFunction() {
        if (_tableGeneratingFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("tableGeneratingFunction");
    }

    /**
     * Remover for tableGeneratingFunction
     * 
     * @see LateralView.Fields#tableGeneratingFunction
     */
    public void removeTableGeneratingFunction() {
        super._map.remove("tableGeneratingFunction");
    }

    /**
     * Getter for tableGeneratingFunction
     * 
     * @see LateralView.Fields#tableGeneratingFunction
     */
    public LateralView.TableGeneratingFunction getTableGeneratingFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTableGeneratingFunction();
            case DEFAULT:
            case NULL:
                if (_tableGeneratingFunctionField!= null) {
                    return _tableGeneratingFunctionField;
                } else {
                    Object __rawValue = super._map.get("tableGeneratingFunction");
                    _tableGeneratingFunctionField = ((__rawValue == null)?null:new LateralView.TableGeneratingFunction(__rawValue));
                    return _tableGeneratingFunctionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for tableGeneratingFunction
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see LateralView.Fields#tableGeneratingFunction
     */
    @Nonnull
    public LateralView.TableGeneratingFunction getTableGeneratingFunction() {
        if (_tableGeneratingFunctionField!= null) {
            return _tableGeneratingFunctionField;
        } else {
            Object __rawValue = super._map.get("tableGeneratingFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("tableGeneratingFunction");
            }
            _tableGeneratingFunctionField = ((__rawValue == null)?null:new LateralView.TableGeneratingFunction(__rawValue));
            return _tableGeneratingFunctionField;
        }
    }

    /**
     * Setter for tableGeneratingFunction
     * 
     * @see LateralView.Fields#tableGeneratingFunction
     */
    public LateralView setTableGeneratingFunction(LateralView.TableGeneratingFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTableGeneratingFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field tableGeneratingFunction of com.linkedin.feathr.compute.LateralView");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tableGeneratingFunction", value.data());
                    _tableGeneratingFunctionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTableGeneratingFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tableGeneratingFunction", value.data());
                    _tableGeneratingFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "tableGeneratingFunction", value.data());
                    _tableGeneratingFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for tableGeneratingFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see LateralView.Fields#tableGeneratingFunction
     */
    public LateralView setTableGeneratingFunction(
        @Nonnull
        LateralView.TableGeneratingFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field tableGeneratingFunction of com.linkedin.feathr.compute.LateralView to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "tableGeneratingFunction", value.data());
            _tableGeneratingFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for virtualTableAlias
     * 
     * @see LateralView.Fields#virtualTableAlias
     */
    public boolean hasVirtualTableAlias() {
        if (_virtualTableAliasField!= null) {
            return true;
        }
        return super._map.containsKey("virtualTableAlias");
    }

    /**
     * Remover for virtualTableAlias
     * 
     * @see LateralView.Fields#virtualTableAlias
     */
    public void removeVirtualTableAlias() {
        super._map.remove("virtualTableAlias");
    }

    /**
     * Getter for virtualTableAlias
     * 
     * @see LateralView.Fields#virtualTableAlias
     */
    public String getVirtualTableAlias(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getVirtualTableAlias();
            case DEFAULT:
            case NULL:
                if (_virtualTableAliasField!= null) {
                    return _virtualTableAliasField;
                } else {
                    Object __rawValue = super._map.get("virtualTableAlias");
                    _virtualTableAliasField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _virtualTableAliasField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for virtualTableAlias
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see LateralView.Fields#virtualTableAlias
     */
    @Nonnull
    public String getVirtualTableAlias() {
        if (_virtualTableAliasField!= null) {
            return _virtualTableAliasField;
        } else {
            Object __rawValue = super._map.get("virtualTableAlias");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("virtualTableAlias");
            }
            _virtualTableAliasField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _virtualTableAliasField;
        }
    }

    /**
     * Setter for virtualTableAlias
     * 
     * @see LateralView.Fields#virtualTableAlias
     */
    public LateralView setVirtualTableAlias(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setVirtualTableAlias(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field virtualTableAlias of com.linkedin.feathr.compute.LateralView");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "virtualTableAlias", value);
                    _virtualTableAliasField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeVirtualTableAlias();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "virtualTableAlias", value);
                    _virtualTableAliasField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "virtualTableAlias", value);
                    _virtualTableAliasField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for virtualTableAlias
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see LateralView.Fields#virtualTableAlias
     */
    public LateralView setVirtualTableAlias(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field virtualTableAlias of com.linkedin.feathr.compute.LateralView to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "virtualTableAlias", value);
            _virtualTableAliasField = value;
        }
        return this;
    }

    @Override
    public LateralView clone()
        throws CloneNotSupportedException
    {
        LateralView __clone = ((LateralView) super.clone());
        __clone.__changeListener = new LateralView.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public LateralView copy()
        throws CloneNotSupportedException
    {
        LateralView __copy = ((LateralView) super.copy());
        __copy._virtualTableAliasField = null;
        __copy._tableGeneratingFunctionField = null;
        __copy.__changeListener = new LateralView.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final LateralView __objectRef;

        private ChangeListener(LateralView reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "virtualTableAlias":
                    __objectRef._virtualTableAliasField = null;
                    break;
                case "tableGeneratingFunction":
                    __objectRef._tableGeneratingFunctionField = null;
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
         * A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.
         * 
         */
        public com.linkedin.feathr.compute.LateralView.TableGeneratingFunction.Fields tableGeneratingFunction() {
            return new com.linkedin.feathr.compute.LateralView.TableGeneratingFunction.Fields(getPathComponents(), "tableGeneratingFunction");
        }

        /**
         * Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.
         * 
         */
        public PathSpec virtualTableAlias() {
            return new PathSpec(getPathComponents(), "virtualTableAlias");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.LateralView.TableGeneratingFunction.ProjectionMask _tableGeneratingFunctionMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.
         * 
         */
        public LateralView.ProjectionMask withTableGeneratingFunction(Function<com.linkedin.feathr.compute.LateralView.TableGeneratingFunction.ProjectionMask, com.linkedin.feathr.compute.LateralView.TableGeneratingFunction.ProjectionMask> nestedMask) {
            _tableGeneratingFunctionMask = nestedMask.apply(((_tableGeneratingFunctionMask == null)?LateralView.TableGeneratingFunction.createMask():_tableGeneratingFunctionMask));
            getDataMap().put("tableGeneratingFunction", _tableGeneratingFunctionMask.getDataMap());
            return this;
        }

        /**
         * A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.
         * 
         */
        public LateralView.ProjectionMask withTableGeneratingFunction() {
            _tableGeneratingFunctionMask = null;
            getDataMap().put("tableGeneratingFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.
         * 
         */
        public LateralView.ProjectionMask withVirtualTableAlias() {
            getDataMap().put("virtualTableAlias", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\LateralView.pdl.")
    public static class TableGeneratingFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.compute.SqlExpression _sqlExpressionMember = null;
        private LateralView.TableGeneratingFunction.ChangeListener __changeListener = new LateralView.TableGeneratingFunction.ChangeListener(this);
        private final static DataSchema MEMBER_SqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.SqlExpression");

        public TableGeneratingFunction() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public TableGeneratingFunction(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static LateralView.TableGeneratingFunction create(com.linkedin.feathr.compute.SqlExpression value) {
            LateralView.TableGeneratingFunction newUnion = new LateralView.TableGeneratingFunction();
            newUnion.setSqlExpression(value);
            return newUnion;
        }

        public boolean isSqlExpression() {
            return memberIs("com.linkedin.feathr.compute.SqlExpression");
        }

        public com.linkedin.feathr.compute.SqlExpression getSqlExpression() {
            checkNotNull();
            if (_sqlExpressionMember!= null) {
                return _sqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.SqlExpression");
            _sqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.SqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sqlExpressionMember;
        }

        public void setSqlExpression(com.linkedin.feathr.compute.SqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.SqlExpression", value.data());
        }

        public static LateralView.TableGeneratingFunction.ProjectionMask createMask() {
            return new LateralView.TableGeneratingFunction.ProjectionMask();
        }

        @Override
        public LateralView.TableGeneratingFunction clone()
            throws CloneNotSupportedException
        {
            LateralView.TableGeneratingFunction __clone = ((LateralView.TableGeneratingFunction) super.clone());
            __clone.__changeListener = new LateralView.TableGeneratingFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public LateralView.TableGeneratingFunction copy()
            throws CloneNotSupportedException
        {
            LateralView.TableGeneratingFunction __copy = ((LateralView.TableGeneratingFunction) super.copy());
            __copy._sqlExpressionMember = null;
            __copy.__changeListener = new LateralView.TableGeneratingFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final LateralView.TableGeneratingFunction __objectRef;

            private ChangeListener(LateralView.TableGeneratingFunction reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.compute.SqlExpression":
                        __objectRef._sqlExpressionMember = null;
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

            public com.linkedin.feathr.compute.SqlExpression.Fields SqlExpression() {
                return new com.linkedin.feathr.compute.SqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.compute.SqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.SqlExpression.ProjectionMask _SqlExpressionMask;

            ProjectionMask() {
                super(2);
            }

            public LateralView.TableGeneratingFunction.ProjectionMask withSqlExpression(Function<com.linkedin.feathr.compute.SqlExpression.ProjectionMask, com.linkedin.feathr.compute.SqlExpression.ProjectionMask> nestedMask) {
                _SqlExpressionMask = nestedMask.apply(((_SqlExpressionMask == null)?com.linkedin.feathr.compute.SqlExpression.createMask():_SqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.compute.SqlExpression", _SqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

}
