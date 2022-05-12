
package com.linkedin.feathr.featureDataModel;

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
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SparkSqlExpression.pdl.")
public class SparkSqlExpression
    extends RecordTemplate
{

    private final static SparkSqlExpression.Fields _fields = new SparkSqlExpression.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}", SchemaFormatType.PDL));
    private String _sqlField = null;
    private SparkSqlExpression.ChangeListener __changeListener = new SparkSqlExpression.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Sql = SCHEMA.getField("sql");

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
     * Existence checker for sql
     * 
     * @see SparkSqlExpression.Fields#sql
     */
    public boolean hasSql() {
        if (_sqlField!= null) {
            return true;
        }
        return super._map.containsKey("sql");
    }

    /**
     * Remover for sql
     * 
     * @see SparkSqlExpression.Fields#sql
     */
    public void removeSql() {
        super._map.remove("sql");
    }

    /**
     * Getter for sql
     * 
     * @see SparkSqlExpression.Fields#sql
     */
    public String getSql(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSql();
            case DEFAULT:
            case NULL:
                if (_sqlField!= null) {
                    return _sqlField;
                } else {
                    Object __rawValue = super._map.get("sql");
                    _sqlField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _sqlField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for sql
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SparkSqlExpression.Fields#sql
     */
    @Nonnull
    public String getSql() {
        if (_sqlField!= null) {
            return _sqlField;
        } else {
            Object __rawValue = super._map.get("sql");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("sql");
            }
            _sqlField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _sqlField;
        }
    }

    /**
     * Setter for sql
     * 
     * @see SparkSqlExpression.Fields#sql
     */
    public SparkSqlExpression setSql(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSql(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field sql of com.linkedin.feathr.featureDataModel.SparkSqlExpression");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "sql", value);
                    _sqlField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeSql();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "sql", value);
                    _sqlField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "sql", value);
                    _sqlField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for sql
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SparkSqlExpression.Fields#sql
     */
    public SparkSqlExpression setSql(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field sql of com.linkedin.feathr.featureDataModel.SparkSqlExpression to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "sql", value);
            _sqlField = value;
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
        __copy._sqlField = null;
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
                case "sql":
                    __objectRef._sqlField = null;
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
        public PathSpec sql() {
            return new PathSpec(getPathComponents(), "sql");
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
        public SparkSqlExpression.ProjectionMask withSql() {
            getDataMap().put("sql", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
