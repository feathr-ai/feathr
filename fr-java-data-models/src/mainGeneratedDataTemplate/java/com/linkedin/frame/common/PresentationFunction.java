
package com.linkedin.frame.common;

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
 * Presentation function model capturing the type of function and any parameters needed for the function
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from fr-java-data-models/src/main/pegasus/com/linkedin/frame/common/PresentationFunction.pdl.")
public class PresentationFunction
    extends RecordTemplate
{

    private final static PresentationFunction.Fields _fields = new PresentationFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.frame.common/**Presentation function model capturing the type of function and any parameters needed for the function*/record PresentationFunction{/**Presentation function types that are available for transforming inference internal value to member facing value*/funtionType:/**Presentation function types that are available for transforming inference internal value to member facing value*/enum PresentationFunctionType{/**Transform internal boolean value like T, F, 0, 1 to member facing value like True, False*/BOOLEAN}/**Optional map of function parameters which are in addition to inference value parameter*/params:optional map[string,string]}", SchemaFormatType.PDL));
    private PresentationFunctionType _funtionTypeField = null;
    private StringMap _paramsField = null;
    private PresentationFunction.ChangeListener __changeListener = new PresentationFunction.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_FuntionType = SCHEMA.getField("funtionType");
    private final static RecordDataSchema.Field FIELD_Params = SCHEMA.getField("params");

    public PresentationFunction() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public PresentationFunction(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static PresentationFunction.Fields fields() {
        return _fields;
    }

    public static PresentationFunction.ProjectionMask createMask() {
        return new PresentationFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for funtionType
     * 
     * @see PresentationFunction.Fields#funtionType
     */
    public boolean hasFuntionType() {
        if (_funtionTypeField!= null) {
            return true;
        }
        return super._map.containsKey("funtionType");
    }

    /**
     * Remover for funtionType
     * 
     * @see PresentationFunction.Fields#funtionType
     */
    public void removeFuntionType() {
        super._map.remove("funtionType");
    }

    /**
     * Getter for funtionType
     * 
     * @see PresentationFunction.Fields#funtionType
     */
    public PresentationFunctionType getFuntionType(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFuntionType();
            case DEFAULT:
            case NULL:
                if (_funtionTypeField!= null) {
                    return _funtionTypeField;
                } else {
                    Object __rawValue = super._map.get("funtionType");
                    _funtionTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, PresentationFunctionType.class, PresentationFunctionType.$UNKNOWN);
                    return _funtionTypeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for funtionType
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PresentationFunction.Fields#funtionType
     */
    @Nonnull
    public PresentationFunctionType getFuntionType() {
        if (_funtionTypeField!= null) {
            return _funtionTypeField;
        } else {
            Object __rawValue = super._map.get("funtionType");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("funtionType");
            }
            _funtionTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, PresentationFunctionType.class, PresentationFunctionType.$UNKNOWN);
            return _funtionTypeField;
        }
    }

    /**
     * Setter for funtionType
     * 
     * @see PresentationFunction.Fields#funtionType
     */
    public PresentationFunction setFuntionType(PresentationFunctionType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFuntionType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field funtionType of com.linkedin.frame.common.PresentationFunction");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "funtionType", value.name());
                    _funtionTypeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFuntionType();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "funtionType", value.name());
                    _funtionTypeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "funtionType", value.name());
                    _funtionTypeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for funtionType
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PresentationFunction.Fields#funtionType
     */
    public PresentationFunction setFuntionType(
        @Nonnull
        PresentationFunctionType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field funtionType of com.linkedin.frame.common.PresentationFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "funtionType", value.name());
            _funtionTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for params
     * 
     * @see PresentationFunction.Fields#params
     */
    public boolean hasParams() {
        if (_paramsField!= null) {
            return true;
        }
        return super._map.containsKey("params");
    }

    /**
     * Remover for params
     * 
     * @see PresentationFunction.Fields#params
     */
    public void removeParams() {
        super._map.remove("params");
    }

    /**
     * Getter for params
     * 
     * @see PresentationFunction.Fields#params
     */
    public StringMap getParams(GetMode mode) {
        return getParams();
    }

    /**
     * Getter for params
     * 
     * @return
     *     Optional field. Always check for null.
     * @see PresentationFunction.Fields#params
     */
    @Nullable
    public StringMap getParams() {
        if (_paramsField!= null) {
            return _paramsField;
        } else {
            Object __rawValue = super._map.get("params");
            _paramsField = ((__rawValue == null)?null:new StringMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _paramsField;
        }
    }

    /**
     * Setter for params
     * 
     * @see PresentationFunction.Fields#params
     */
    public PresentationFunction setParams(StringMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setParams(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeParams();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "params", value.data());
                    _paramsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "params", value.data());
                    _paramsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for params
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PresentationFunction.Fields#params
     */
    public PresentationFunction setParams(
        @Nonnull
        StringMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field params of com.linkedin.frame.common.PresentationFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "params", value.data());
            _paramsField = value;
        }
        return this;
    }

    @Override
    public PresentationFunction clone()
        throws CloneNotSupportedException
    {
        PresentationFunction __clone = ((PresentationFunction) super.clone());
        __clone.__changeListener = new PresentationFunction.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public PresentationFunction copy()
        throws CloneNotSupportedException
    {
        PresentationFunction __copy = ((PresentationFunction) super.copy());
        __copy._funtionTypeField = null;
        __copy._paramsField = null;
        __copy.__changeListener = new PresentationFunction.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final PresentationFunction __objectRef;

        private ChangeListener(PresentationFunction reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "funtionType":
                    __objectRef._funtionTypeField = null;
                    break;
                case "params":
                    __objectRef._paramsField = null;
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
         * Presentation function types that are available for transforming inference internal value to member facing value
         * 
         */
        public PathSpec funtionType() {
            return new PathSpec(getPathComponents(), "funtionType");
        }

        /**
         * Optional map of function parameters which are in addition to inference value parameter
         * 
         */
        public PathSpec params() {
            return new PathSpec(getPathComponents(), "params");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Presentation function types that are available for transforming inference internal value to member facing value
         * 
         */
        public PresentationFunction.ProjectionMask withFuntionType() {
            getDataMap().put("funtionType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Optional map of function parameters which are in addition to inference value parameter
         * 
         */
        public PresentationFunction.ProjectionMask withParams() {
            getDataMap().put("params", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
