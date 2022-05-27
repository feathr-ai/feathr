
package com.linkedin.feathr.config.join;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
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


/**
 * An expression in Spark SQL.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/SparkSqlExpression.pdl.")
public class SparkSqlExpression
    extends RecordTemplate
{

    private final static SparkSqlExpression.Fields _fields = new SparkSqlExpression.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}", SchemaFormatType.PDL));
    private String _expressionField = null;
    private SparkSqlExpression.ChangeListener __changeListener = new SparkSqlExpression.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Expression = SCHEMA.getField("expression");

    public SparkSqlExpression() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public SparkSqlExpression(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static SparkSqlExpression.Fields fields() {
        return _fields;
    }

    public static SparkSqlExpression.ProjectionMask createMask() {
        return new SparkSqlExpression.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for expression
     * 
     * @see SparkSqlExpression.Fields#expression
     */
    public boolean hasExpression() {
        if (_expressionField!= null) {
            return true;
        }
        return super._map.containsKey("expression");
    }

    /**
     * Remover for expression
     * 
     * @see SparkSqlExpression.Fields#expression
     */
    public void removeExpression() {
        super._map.remove("expression");
    }

    /**
     * Getter for expression
     * 
     * @see SparkSqlExpression.Fields#expression
     */
    public String getExpression(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getExpression();
            case DEFAULT:
            case NULL:
                if (_expressionField!= null) {
                    return _expressionField;
                } else {
                    Object __rawValue = super._map.get("expression");
                    _expressionField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _expressionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for expression
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SparkSqlExpression.Fields#expression
     */
    @Nonnull
    public String getExpression() {
        if (_expressionField!= null) {
            return _expressionField;
        } else {
            Object __rawValue = super._map.get("expression");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("expression");
            }
            _expressionField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _expressionField;
        }
    }

    /**
     * Setter for expression
     * 
     * @see SparkSqlExpression.Fields#expression
     */
    public SparkSqlExpression setExpression(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setExpression(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field expression of com.linkedin.feathr.config.join.SparkSqlExpression");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "expression", value);
                    _expressionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeExpression();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "expression", value);
                    _expressionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "expression", value);
                    _expressionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for expression
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SparkSqlExpression.Fields#expression
     */
    public SparkSqlExpression setExpression(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field expression of com.linkedin.feathr.config.join.SparkSqlExpression to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "expression", value);
            _expressionField = value;
        }
        return this;
    }

    @Override
    public SparkSqlExpression clone()
        throws CloneNotSupportedException
    {
        SparkSqlExpression __clone = ((SparkSqlExpression) super.clone());
        __clone.__changeListener = new SparkSqlExpression.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public SparkSqlExpression copy()
        throws CloneNotSupportedException
    {
        SparkSqlExpression __copy = ((SparkSqlExpression) super.copy());
        __copy._expressionField = null;
        __copy.__changeListener = new SparkSqlExpression.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final SparkSqlExpression __objectRef;

        private ChangeListener(SparkSqlExpression reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "expression":
                    __objectRef._expressionField = null;
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
         * The Spark SQL expression.
         * 
         */
        public PathSpec expression() {
            return new PathSpec(getPathComponents(), "expression");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * The Spark SQL expression.
         * 
         */
        public SparkSqlExpression.ProjectionMask withExpression() {
            getDataMap().put("expression", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
