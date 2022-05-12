
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
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/TransformationFunction.pdl.")
public class TransformationFunction
    extends RecordTemplate
{

    private final static TransformationFunction.Fields _fields = new TransformationFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record TransformationFunction{operator:typeref OperatorId=string,parameters:optional map[string,string]}", SchemaFormatType.PDL));
    private String _operatorField = null;
    private StringMap _parametersField = null;
    private TransformationFunction.ChangeListener __changeListener = new TransformationFunction.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Operator = SCHEMA.getField("operator");
    private final static RecordDataSchema.Field FIELD_Parameters = SCHEMA.getField("parameters");

    public TransformationFunction() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public TransformationFunction(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TransformationFunction.Fields fields() {
        return _fields;
    }

    public static TransformationFunction.ProjectionMask createMask() {
        return new TransformationFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for operator
     * 
     * @see TransformationFunction.Fields#operator
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
     * @see TransformationFunction.Fields#operator
     */
    public void removeOperator() {
        super._map.remove("operator");
    }

    /**
     * Getter for operator
     * 
     * @see TransformationFunction.Fields#operator
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
     * @see TransformationFunction.Fields#operator
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
     * @see TransformationFunction.Fields#operator
     */
    public TransformationFunction setOperator(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setOperator(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field operator of com.linkedin.feathr.compute.TransformationFunction");
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
     * @see TransformationFunction.Fields#operator
     */
    public TransformationFunction setOperator(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field operator of com.linkedin.feathr.compute.TransformationFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "operator", value);
            _operatorField = value;
        }
        return this;
    }

    /**
     * Existence checker for parameters
     * 
     * @see TransformationFunction.Fields#parameters
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
     * @see TransformationFunction.Fields#parameters
     */
    public void removeParameters() {
        super._map.remove("parameters");
    }

    /**
     * Getter for parameters
     * 
     * @see TransformationFunction.Fields#parameters
     */
    public StringMap getParameters(GetMode mode) {
        return getParameters();
    }

    /**
     * Getter for parameters
     * 
     * @return
     *     Optional field. Always check for null.
     * @see TransformationFunction.Fields#parameters
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
     * @see TransformationFunction.Fields#parameters
     */
    public TransformationFunction setParameters(StringMap value, SetMode mode) {
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
     * @see TransformationFunction.Fields#parameters
     */
    public TransformationFunction setParameters(
        @Nonnull
        StringMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field parameters of com.linkedin.feathr.compute.TransformationFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
            _parametersField = value;
        }
        return this;
    }

    @Override
    public TransformationFunction clone()
        throws CloneNotSupportedException
    {
        TransformationFunction __clone = ((TransformationFunction) super.clone());
        __clone.__changeListener = new TransformationFunction.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TransformationFunction copy()
        throws CloneNotSupportedException
    {
        TransformationFunction __copy = ((TransformationFunction) super.copy());
        __copy._parametersField = null;
        __copy._operatorField = null;
        __copy.__changeListener = new TransformationFunction.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TransformationFunction __objectRef;

        private ChangeListener(TransformationFunction reference) {
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

        public PathSpec operator() {
            return new PathSpec(getPathComponents(), "operator");
        }

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

        public TransformationFunction.ProjectionMask withOperator() {
            getDataMap().put("operator", MaskMap.POSITIVE_MASK);
            return this;
        }

        public TransformationFunction.ProjectionMask withParameters() {
            getDataMap().put("parameters", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
