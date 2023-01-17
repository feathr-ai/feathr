
package com.linkedin.feathr.compute;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;
import com.linkedin.data.template.StringMap;


/**
 * All parameters related to an aggregation operation. This class should be used in conjunction with the [[Aggregation]] node.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\AggregationFunction.pdl.")
public class AggregationFunction
    extends RecordTemplate
{

    private final static AggregationFunction.Fields _fields = new AggregationFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**All parameters related to an aggregation operation. This class should be used in conjunction with the [[Aggregation]] node.*/record AggregationFunction{/**The aggregation function.*/operator:/**operator id to set an operator. It can be referring to an mvel expression, sql expression or a java udf.*/typeref OperatorId=string/**All the aggregation parameters should be bundled into this map. For now, the possible parameters are:-\r\na. target_column -  Aggregation column\r\nb. window_size - aggregation window size\r\nc. window unit - aggregation window unit (ex - day, hour)\r\nd. lateral_view_expression - definition of a lateral view for the feature.\r\ne. lateral_view_table_alias - An alias for the lateral view\r\nf. filter - An expression to filter out any data before aggregation. Should be a sparkSql expression.\r\ng. groupBy - groupBy columns. Should be a sparkSql expression.*/parameters:optional map[string,string]}", SchemaFormatType.PDL));
    private String _operatorField = null;
    private StringMap _parametersField = null;
    private AggregationFunction.ChangeListener __changeListener = new AggregationFunction.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Operator = SCHEMA.getField("operator");
    private final static RecordDataSchema.Field FIELD_Parameters = SCHEMA.getField("parameters");

    public AggregationFunction() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public AggregationFunction(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static AggregationFunction.Fields fields() {
        return _fields;
    }

    public static AggregationFunction.ProjectionMask createMask() {
        return new AggregationFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for operator
     * 
     * @see AggregationFunction.Fields#operator
     */
    public boolean hasOperator() {
        if (_operatorField!= null) {
            return true;
        }
        return super._map.containsKey("operator");
    }

    /**
     * Remover for operator
     * 
     * @see AggregationFunction.Fields#operator
     */
    public void removeOperator() {
        super._map.remove("operator");
    }

    /**
     * Getter for operator
     * 
     * @see AggregationFunction.Fields#operator
     */
    public String getOperator(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getOperator();
            case DEFAULT:
            case NULL:
                if (_operatorField!= null) {
                    return _operatorField;
                } else {
                    Object __rawValue = super._map.get("operator");
                    _operatorField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _operatorField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for operator
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see AggregationFunction.Fields#operator
     */
    @Nonnull
    public String getOperator() {
        if (_operatorField!= null) {
            return _operatorField;
        } else {
            Object __rawValue = super._map.get("operator");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("operator");
            }
            _operatorField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _operatorField;
        }
    }

    /**
     * Setter for operator
     * 
     * @see AggregationFunction.Fields#operator
     */
    public AggregationFunction setOperator(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setOperator(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field operator of com.linkedin.feathr.compute.AggregationFunction");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "operator", value);
                    _operatorField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeOperator();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "operator", value);
                    _operatorField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "operator", value);
                    _operatorField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for operator
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see AggregationFunction.Fields#operator
     */
    public AggregationFunction setOperator(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field operator of com.linkedin.feathr.compute.AggregationFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "operator", value);
            _operatorField = value;
        }
        return this;
    }

    /**
     * Existence checker for parameters
     * 
     * @see AggregationFunction.Fields#parameters
     */
    public boolean hasParameters() {
        if (_parametersField!= null) {
            return true;
        }
        return super._map.containsKey("parameters");
    }

    /**
     * Remover for parameters
     * 
     * @see AggregationFunction.Fields#parameters
     */
    public void removeParameters() {
        super._map.remove("parameters");
    }

    /**
     * Getter for parameters
     * 
     * @see AggregationFunction.Fields#parameters
     */
    public StringMap getParameters(GetMode mode) {
        return getParameters();
    }

    /**
     * Getter for parameters
     * 
     * @return
     *     Optional field. Always check for null.
     * @see AggregationFunction.Fields#parameters
     */
    @Nullable
    public StringMap getParameters() {
        if (_parametersField!= null) {
            return _parametersField;
        } else {
            Object __rawValue = super._map.get("parameters");
            _parametersField = ((__rawValue == null)?null:new StringMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _parametersField;
        }
    }

    /**
     * Setter for parameters
     * 
     * @see AggregationFunction.Fields#parameters
     */
    public AggregationFunction setParameters(StringMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setParameters(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeParameters();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
                    _parametersField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
                    _parametersField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for parameters
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see AggregationFunction.Fields#parameters
     */
    public AggregationFunction setParameters(
        @Nonnull
        StringMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field parameters of com.linkedin.feathr.compute.AggregationFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
            _parametersField = value;
        }
        return this;
    }

    @Override
    public AggregationFunction clone()
        throws CloneNotSupportedException
    {
        AggregationFunction __clone = ((AggregationFunction) super.clone());
        __clone.__changeListener = new AggregationFunction.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public AggregationFunction copy()
        throws CloneNotSupportedException
    {
        AggregationFunction __copy = ((AggregationFunction) super.copy());
        __copy._parametersField = null;
        __copy._operatorField = null;
        __copy.__changeListener = new AggregationFunction.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final AggregationFunction __objectRef;

        private ChangeListener(AggregationFunction reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "parameters":
                    __objectRef._parametersField = null;
                    break;
                case "operator":
                    __objectRef._operatorField = null;
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
         * The aggregation function.
         * 
         */
        public PathSpec operator() {
            return new PathSpec(getPathComponents(), "operator");
        }

        /**
         * All the aggregation parameters should be bundled into this map. For now, the possible parameters are:-
         * a. target_column -  Aggregation column
         * b. window_size - aggregation window size
         * c. window unit - aggregation window unit (ex - day, hour)
         * d. lateral_view_expression - definition of a lateral view for the feature.
         * e. lateral_view_table_alias - An alias for the lateral view
         * f. filter - An expression to filter out any data before aggregation. Should be a sparkSql expression.
         * g. groupBy - groupBy columns. Should be a sparkSql expression.
         * 
         */
        public PathSpec parameters() {
            return new PathSpec(getPathComponents(), "parameters");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * The aggregation function.
         * 
         */
        public AggregationFunction.ProjectionMask withOperator() {
            getDataMap().put("operator", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * All the aggregation parameters should be bundled into this map. For now, the possible parameters are:-
         * a. target_column -  Aggregation column
         * b. window_size - aggregation window size
         * c. window unit - aggregation window unit (ex - day, hour)
         * d. lateral_view_expression - definition of a lateral view for the feature.
         * e. lateral_view_table_alias - An alias for the lateral view
         * f. filter - An expression to filter out any data before aggregation. Should be a sparkSql expression.
         * g. groupBy - groupBy columns. Should be a sparkSql expression.
         * 
         */
        public AggregationFunction.ProjectionMask withParameters() {
            getDataMap().put("parameters", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
