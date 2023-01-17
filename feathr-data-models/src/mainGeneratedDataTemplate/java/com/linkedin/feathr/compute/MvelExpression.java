
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
 * An expression in MVEL language. For more information please refer to go/framemvel.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\MvelExpression.pdl.")
public class MvelExpression
    extends RecordTemplate
{

    private final static MvelExpression.Fields _fields = new MvelExpression.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}", SchemaFormatType.PDL));
    private String _mvelField = null;
    private MvelExpression.ChangeListener __changeListener = new MvelExpression.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Mvel = SCHEMA.getField("mvel");

    public MvelExpression() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public MvelExpression(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static MvelExpression.Fields fields() {
        return _fields;
    }

    public static MvelExpression.ProjectionMask createMask() {
        return new MvelExpression.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for mvel
     * 
     * @see MvelExpression.Fields#mvel
     */
    public boolean hasMvel() {
        if (_mvelField!= null) {
            return true;
        }
        return super._map.containsKey("mvel");
    }

    /**
     * Remover for mvel
     * 
     * @see MvelExpression.Fields#mvel
     */
    public void removeMvel() {
        super._map.remove("mvel");
    }

    /**
     * Getter for mvel
     * 
     * @see MvelExpression.Fields#mvel
     */
    public String getMvel(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getMvel();
            case DEFAULT:
            case NULL:
                if (_mvelField!= null) {
                    return _mvelField;
                } else {
                    Object __rawValue = super._map.get("mvel");
                    _mvelField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _mvelField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for mvel
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see MvelExpression.Fields#mvel
     */
    @Nonnull
    public String getMvel() {
        if (_mvelField!= null) {
            return _mvelField;
        } else {
            Object __rawValue = super._map.get("mvel");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("mvel");
            }
            _mvelField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _mvelField;
        }
    }

    /**
     * Setter for mvel
     * 
     * @see MvelExpression.Fields#mvel
     */
    public MvelExpression setMvel(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setMvel(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field mvel of com.linkedin.feathr.compute.MvelExpression");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "mvel", value);
                    _mvelField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeMvel();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "mvel", value);
                    _mvelField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "mvel", value);
                    _mvelField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for mvel
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see MvelExpression.Fields#mvel
     */
    public MvelExpression setMvel(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field mvel of com.linkedin.feathr.compute.MvelExpression to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "mvel", value);
            _mvelField = value;
        }
        return this;
    }

    @Override
    public MvelExpression clone()
        throws CloneNotSupportedException
    {
        MvelExpression __clone = ((MvelExpression) super.clone());
        __clone.__changeListener = new MvelExpression.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public MvelExpression copy()
        throws CloneNotSupportedException
    {
        MvelExpression __copy = ((MvelExpression) super.copy());
        __copy._mvelField = null;
        __copy.__changeListener = new MvelExpression.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final MvelExpression __objectRef;

        private ChangeListener(MvelExpression reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "mvel":
                    __objectRef._mvelField = null;
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
         * The MVEL expression.
         * 
         */
        public PathSpec mvel() {
            return new PathSpec(getPathComponents(), "mvel");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * The MVEL expression.
         * 
         */
        public MvelExpression.ProjectionMask withMvel() {
            getDataMap().put("mvel", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
