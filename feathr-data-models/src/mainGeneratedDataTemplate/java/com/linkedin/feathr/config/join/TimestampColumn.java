
package com.linkedin.feathr.config.join;

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
 * Timestamp column of the input featureiized dataset, which is to be used for the join.
 * timestampColumn: {
 *    def: timestamp
 *    format: yyyyMMdd
 *  }
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/TimestampColumn.pdl.")
public class TimestampColumn
    extends RecordTemplate
{

    private final static TimestampColumn.Fields _fields = new TimestampColumn.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**Timestamp column of the input featureiized dataset, which is to be used for the join.\ntimestampColumn: {\n   def: timestamp\n   format: yyyyMMdd\n }*/record TimestampColumn{/**The definiton of the timestamp column, which can be a sql expression involving the timestamp column\nor just the column name\nExample:- definition: timestamp, timestamp + 10000000.*/definition:union[columnName:string,sparkSqlExpression:/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}]/**Format of the timestamp column. Must confer to java's timestampFormatter or can be\nepoch or epoch_millis.\nExample:- epoch, epoch_millis, yyyy/MM/dd*/format:/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string}", SchemaFormatType.PDL));
    private TimestampColumn.Definition _definitionField = null;
    private String _formatField = null;
    private TimestampColumn.ChangeListener __changeListener = new TimestampColumn.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Definition = SCHEMA.getField("definition");
    private final static RecordDataSchema.Field FIELD_Format = SCHEMA.getField("format");

    public TimestampColumn() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public TimestampColumn(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TimestampColumn.Fields fields() {
        return _fields;
    }

    public static TimestampColumn.ProjectionMask createMask() {
        return new TimestampColumn.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for definition
     * 
     * @see TimestampColumn.Fields#definition
     */
    public boolean hasDefinition() {
        if (_definitionField!= null) {
            return true;
        }
        return super._map.containsKey("definition");
    }

    /**
     * Remover for definition
     * 
     * @see TimestampColumn.Fields#definition
     */
    public void removeDefinition() {
        super._map.remove("definition");
    }

    /**
     * Getter for definition
     * 
     * @see TimestampColumn.Fields#definition
     */
    public TimestampColumn.Definition getDefinition(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDefinition();
            case DEFAULT:
            case NULL:
                if (_definitionField!= null) {
                    return _definitionField;
                } else {
                    Object __rawValue = super._map.get("definition");
                    _definitionField = ((__rawValue == null)?null:new TimestampColumn.Definition(__rawValue));
                    return _definitionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for definition
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TimestampColumn.Fields#definition
     */
    @Nonnull
    public TimestampColumn.Definition getDefinition() {
        if (_definitionField!= null) {
            return _definitionField;
        } else {
            Object __rawValue = super._map.get("definition");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("definition");
            }
            _definitionField = ((__rawValue == null)?null:new TimestampColumn.Definition(__rawValue));
            return _definitionField;
        }
    }

    /**
     * Setter for definition
     * 
     * @see TimestampColumn.Fields#definition
     */
    public TimestampColumn setDefinition(TimestampColumn.Definition value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDefinition(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field definition of com.linkedin.feathr.config.join.TimestampColumn");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "definition", value.data());
                    _definitionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDefinition();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "definition", value.data());
                    _definitionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "definition", value.data());
                    _definitionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for definition
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimestampColumn.Fields#definition
     */
    public TimestampColumn setDefinition(
        @Nonnull
        TimestampColumn.Definition value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field definition of com.linkedin.feathr.config.join.TimestampColumn to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "definition", value.data());
            _definitionField = value;
        }
        return this;
    }

    /**
     * Existence checker for format
     * 
     * @see TimestampColumn.Fields#format
     */
    public boolean hasFormat() {
        if (_formatField!= null) {
            return true;
        }
        return super._map.containsKey("format");
    }

    /**
     * Remover for format
     * 
     * @see TimestampColumn.Fields#format
     */
    public void removeFormat() {
        super._map.remove("format");
    }

    /**
     * Getter for format
     * 
     * @see TimestampColumn.Fields#format
     */
    public String getFormat(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFormat();
            case DEFAULT:
            case NULL:
                if (_formatField!= null) {
                    return _formatField;
                } else {
                    Object __rawValue = super._map.get("format");
                    _formatField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _formatField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for format
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TimestampColumn.Fields#format
     */
    @Nonnull
    public String getFormat() {
        if (_formatField!= null) {
            return _formatField;
        } else {
            Object __rawValue = super._map.get("format");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("format");
            }
            _formatField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _formatField;
        }
    }

    /**
     * Setter for format
     * 
     * @see TimestampColumn.Fields#format
     */
    public TimestampColumn setFormat(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFormat(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field format of com.linkedin.feathr.config.join.TimestampColumn");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "format", value);
                    _formatField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFormat();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "format", value);
                    _formatField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "format", value);
                    _formatField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for format
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimestampColumn.Fields#format
     */
    public TimestampColumn setFormat(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field format of com.linkedin.feathr.config.join.TimestampColumn to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "format", value);
            _formatField = value;
        }
        return this;
    }

    @Override
    public TimestampColumn clone()
        throws CloneNotSupportedException
    {
        TimestampColumn __clone = ((TimestampColumn) super.clone());
        __clone.__changeListener = new TimestampColumn.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TimestampColumn copy()
        throws CloneNotSupportedException
    {
        TimestampColumn __copy = ((TimestampColumn) super.copy());
        __copy._formatField = null;
        __copy._definitionField = null;
        __copy.__changeListener = new TimestampColumn.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TimestampColumn __objectRef;

        private ChangeListener(TimestampColumn reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "format":
                    __objectRef._formatField = null;
                    break;
                case "definition":
                    __objectRef._definitionField = null;
                    break;
            }
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/TimestampColumn.pdl.")
    public static class Definition
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[columnName:string,sparkSqlExpression:{namespace com.linkedin.feathr.config.join/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}}]", SchemaFormatType.PDL));
        private String _columnNameMember = null;
        private com.linkedin.feathr.config.join.SparkSqlExpression _sparkSqlExpressionMember = null;
        private TimestampColumn.Definition.ChangeListener __changeListener = new TimestampColumn.Definition.ChangeListener(this);
        private final static DataSchema MEMBER_ColumnName = SCHEMA.getTypeByMemberKey("columnName");
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("sparkSqlExpression");

        public Definition() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public Definition(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static TimestampColumn.Definition createWithColumnName(String value) {
            TimestampColumn.Definition newUnion = new TimestampColumn.Definition();
            newUnion.setColumnName(value);
            return newUnion;
        }

        public boolean isColumnName() {
            return memberIs("columnName");
        }

        public String getColumnName() {
            checkNotNull();
            if (_columnNameMember!= null) {
                return _columnNameMember;
            }
            Object __rawValue = super._map.get("columnName");
            _columnNameMember = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _columnNameMember;
        }

        public void setColumnName(String value) {
            checkNotNull();
            super._map.clear();
            _columnNameMember = value;
            CheckedUtil.putWithoutChecking(super._map, "columnName", value);
        }

        public static TimestampColumn.Definition createWithSparkSqlExpression(com.linkedin.feathr.config.join.SparkSqlExpression value) {
            TimestampColumn.Definition newUnion = new TimestampColumn.Definition();
            newUnion.setSparkSqlExpression(value);
            return newUnion;
        }

        public boolean isSparkSqlExpression() {
            return memberIs("sparkSqlExpression");
        }

        public com.linkedin.feathr.config.join.SparkSqlExpression getSparkSqlExpression() {
            checkNotNull();
            if (_sparkSqlExpressionMember!= null) {
                return _sparkSqlExpressionMember;
            }
            Object __rawValue = super._map.get("sparkSqlExpression");
            _sparkSqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.config.join.SparkSqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sparkSqlExpressionMember;
        }

        public void setSparkSqlExpression(com.linkedin.feathr.config.join.SparkSqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sparkSqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "sparkSqlExpression", value.data());
        }

        public static TimestampColumn.Definition.ProjectionMask createMask() {
            return new TimestampColumn.Definition.ProjectionMask();
        }

        @Override
        public TimestampColumn.Definition clone()
            throws CloneNotSupportedException
        {
            TimestampColumn.Definition __clone = ((TimestampColumn.Definition) super.clone());
            __clone.__changeListener = new TimestampColumn.Definition.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public TimestampColumn.Definition copy()
            throws CloneNotSupportedException
        {
            TimestampColumn.Definition __copy = ((TimestampColumn.Definition) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy._columnNameMember = null;
            __copy.__changeListener = new TimestampColumn.Definition.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final TimestampColumn.Definition __objectRef;

            private ChangeListener(TimestampColumn.Definition reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "sparkSqlExpression":
                        __objectRef._sparkSqlExpressionMember = null;
                        break;
                    case "columnName":
                        __objectRef._columnNameMember = null;
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

            public PathSpec ColumnName() {
                return new PathSpec(getPathComponents(), "columnName");
            }

            public com.linkedin.feathr.config.join.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.config.join.SparkSqlExpression.Fields(getPathComponents(), "sparkSqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.config.join.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;

            ProjectionMask() {
                super(3);
            }

            public TimestampColumn.Definition.ProjectionMask withColumnName() {
                getDataMap().put("columnName", MaskMap.POSITIVE_MASK);
                return this;
            }

            public TimestampColumn.Definition.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.config.join.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.config.join.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.config.join.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("sparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
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
         * The definiton of the timestamp column, which can be a sql expression involving the timestamp column
         * or just the column name
         * Example:- definition: timestamp, timestamp + 10000000.
         * 
         */
        public com.linkedin.feathr.config.join.TimestampColumn.Definition.Fields definition() {
            return new com.linkedin.feathr.config.join.TimestampColumn.Definition.Fields(getPathComponents(), "definition");
        }

        /**
         * Format of the timestamp column. Must confer to java's timestampFormatter or can be
         * epoch or epoch_millis.
         * Example:- epoch, epoch_millis, yyyy/MM/dd
         * 
         */
        public PathSpec format() {
            return new PathSpec(getPathComponents(), "format");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.TimestampColumn.Definition.ProjectionMask _definitionMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * The definiton of the timestamp column, which can be a sql expression involving the timestamp column
         * or just the column name
         * Example:- definition: timestamp, timestamp + 10000000.
         * 
         */
        public TimestampColumn.ProjectionMask withDefinition(Function<com.linkedin.feathr.config.join.TimestampColumn.Definition.ProjectionMask, com.linkedin.feathr.config.join.TimestampColumn.Definition.ProjectionMask> nestedMask) {
            _definitionMask = nestedMask.apply(((_definitionMask == null)?TimestampColumn.Definition.createMask():_definitionMask));
            getDataMap().put("definition", _definitionMask.getDataMap());
            return this;
        }

        /**
         * The definiton of the timestamp column, which can be a sql expression involving the timestamp column
         * or just the column name
         * Example:- definition: timestamp, timestamp + 10000000.
         * 
         */
        public TimestampColumn.ProjectionMask withDefinition() {
            _definitionMask = null;
            getDataMap().put("definition", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Format of the timestamp column. Must confer to java's timestampFormatter or can be
         * epoch or epoch_millis.
         * Example:- epoch, epoch_millis, yyyy/MM/dd
         * 
         */
        public TimestampColumn.ProjectionMask withFormat() {
            getDataMap().put("format", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
