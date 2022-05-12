
package com.linkedin.feathr.compute;

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
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/TimestampCol.pdl.")
public class TimestampCol
    extends RecordTemplate
{

    private final static TimestampCol.Fields _fields = new TimestampCol.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record TimestampCol{expression:string,format:string}", SchemaFormatType.PDL));
    private String _expressionField = null;
    private String _formatField = null;
    private TimestampCol.ChangeListener __changeListener = new TimestampCol.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Expression = SCHEMA.getField("expression");
    private final static RecordDataSchema.Field FIELD_Format = SCHEMA.getField("format");

    public TimestampCol() {
        super(new DataMap(3, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public TimestampCol(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TimestampCol.Fields fields() {
        return _fields;
    }

    public static TimestampCol.ProjectionMask createMask() {
        return new TimestampCol.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for expression
     * 
     * @see TimestampCol.Fields#expression
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
     * @see TimestampCol.Fields#expression
     */
    public void removeExpression() {
        super._map.remove("expression");
    }

    /**
     * Getter for expression
     * 
     * @see TimestampCol.Fields#expression
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
     * @see TimestampCol.Fields#expression
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
     * @see TimestampCol.Fields#expression
     */
    public TimestampCol setExpression(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setExpression(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field expression of com.linkedin.feathr.compute.TimestampCol");
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
     * @see TimestampCol.Fields#expression
     */
    public TimestampCol setExpression(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field expression of com.linkedin.feathr.compute.TimestampCol to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "expression", value);
            _expressionField = value;
        }
        return this;
    }

    /**
     * Existence checker for format
     * 
     * @see TimestampCol.Fields#format
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
     * @see TimestampCol.Fields#format
     */
    public void removeFormat() {
        super._map.remove("format");
    }

    /**
     * Getter for format
     * 
     * @see TimestampCol.Fields#format
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
     * @see TimestampCol.Fields#format
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
     * @see TimestampCol.Fields#format
     */
    public TimestampCol setFormat(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFormat(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field format of com.linkedin.feathr.compute.TimestampCol");
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
     * @see TimestampCol.Fields#format
     */
    public TimestampCol setFormat(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field format of com.linkedin.feathr.compute.TimestampCol to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "format", value);
            _formatField = value;
        }
        return this;
    }

    @Override
    public TimestampCol clone()
        throws CloneNotSupportedException
    {
        TimestampCol __clone = ((TimestampCol) super.clone());
        __clone.__changeListener = new TimestampCol.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TimestampCol copy()
        throws CloneNotSupportedException
    {
        TimestampCol __copy = ((TimestampCol) super.copy());
        __copy._expressionField = null;
        __copy._formatField = null;
        __copy.__changeListener = new TimestampCol.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TimestampCol __objectRef;

        private ChangeListener(TimestampCol reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "expression":
                    __objectRef._expressionField = null;
                    break;
                case "format":
                    __objectRef._formatField = null;
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

        public PathSpec expression() {
            return new PathSpec(getPathComponents(), "expression");
        }

        public PathSpec format() {
            return new PathSpec(getPathComponents(), "format");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        public TimestampCol.ProjectionMask withExpression() {
            getDataMap().put("expression", MaskMap.POSITIVE_MASK);
            return this;
        }

        public TimestampCol.ProjectionMask withFormat() {
            getDataMap().put("format", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
