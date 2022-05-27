
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
import com.linkedin.data.template.SetMode;


/**
 * Settings needed when the input data is to be joined with the latest available feature data.
 * joinTimeSettings: {
 *    useLatestFeatureData: true
 * }
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/UseLatestJoinTimeSettings.pdl.")
public class UseLatestJoinTimeSettings
    extends RecordTemplate
{

    private final static UseLatestJoinTimeSettings.Fields _fields = new UseLatestJoinTimeSettings.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**Settings needed when the input data is to be joined with the latest available feature data.\njoinTimeSettings: {\n   useLatestFeatureData: true\n}*/record UseLatestJoinTimeSettings{/**Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.\nWhen useLatestFeatureData is set, there should be no other time-based parameters.*/useLatestFeatureData:boolean=true}", SchemaFormatType.PDL));
    private Boolean _useLatestFeatureDataField = null;
    private UseLatestJoinTimeSettings.ChangeListener __changeListener = new UseLatestJoinTimeSettings.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_UseLatestFeatureData = SCHEMA.getField("useLatestFeatureData");
    private final static Boolean DEFAULT_UseLatestFeatureData;

    static {
        DEFAULT_UseLatestFeatureData = DataTemplateUtil.coerceBooleanOutput(FIELD_UseLatestFeatureData.getDefault());
    }

    public UseLatestJoinTimeSettings() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public UseLatestJoinTimeSettings(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UseLatestJoinTimeSettings.Fields fields() {
        return _fields;
    }

    public static UseLatestJoinTimeSettings.ProjectionMask createMask() {
        return new UseLatestJoinTimeSettings.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for useLatestFeatureData
     * 
     * @see UseLatestJoinTimeSettings.Fields#useLatestFeatureData
     */
    public boolean hasUseLatestFeatureData() {
        if (_useLatestFeatureDataField!= null) {
            return true;
        }
        return super._map.containsKey("useLatestFeatureData");
    }

    /**
     * Remover for useLatestFeatureData
     * 
     * @see UseLatestJoinTimeSettings.Fields#useLatestFeatureData
     */
    public void removeUseLatestFeatureData() {
        super._map.remove("useLatestFeatureData");
    }

    /**
     * Getter for useLatestFeatureData
     * 
     * @see UseLatestJoinTimeSettings.Fields#useLatestFeatureData
     */
    public Boolean isUseLatestFeatureData(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return isUseLatestFeatureData();
            case NULL:
                if (_useLatestFeatureDataField!= null) {
                    return _useLatestFeatureDataField;
                } else {
                    Object __rawValue = super._map.get("useLatestFeatureData");
                    _useLatestFeatureDataField = DataTemplateUtil.coerceBooleanOutput(__rawValue);
                    return _useLatestFeatureDataField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for useLatestFeatureData
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see UseLatestJoinTimeSettings.Fields#useLatestFeatureData
     */
    @Nonnull
    public Boolean isUseLatestFeatureData() {
        if (_useLatestFeatureDataField!= null) {
            return _useLatestFeatureDataField;
        } else {
            Object __rawValue = super._map.get("useLatestFeatureData");
            if (__rawValue == null) {
                return DEFAULT_UseLatestFeatureData;
            }
            _useLatestFeatureDataField = DataTemplateUtil.coerceBooleanOutput(__rawValue);
            return _useLatestFeatureDataField;
        }
    }

    /**
     * Setter for useLatestFeatureData
     * 
     * @see UseLatestJoinTimeSettings.Fields#useLatestFeatureData
     */
    public UseLatestJoinTimeSettings setUseLatestFeatureData(Boolean value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setUseLatestFeatureData(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field useLatestFeatureData of com.linkedin.feathr.config.join.UseLatestJoinTimeSettings");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "useLatestFeatureData", value);
                    _useLatestFeatureDataField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeUseLatestFeatureData();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "useLatestFeatureData", value);
                    _useLatestFeatureDataField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "useLatestFeatureData", value);
                    _useLatestFeatureDataField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for useLatestFeatureData
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see UseLatestJoinTimeSettings.Fields#useLatestFeatureData
     */
    public UseLatestJoinTimeSettings setUseLatestFeatureData(
        @Nonnull
        Boolean value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field useLatestFeatureData of com.linkedin.feathr.config.join.UseLatestJoinTimeSettings to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "useLatestFeatureData", value);
            _useLatestFeatureDataField = value;
        }
        return this;
    }

    /**
     * Setter for useLatestFeatureData
     * 
     * @see UseLatestJoinTimeSettings.Fields#useLatestFeatureData
     */
    public UseLatestJoinTimeSettings setUseLatestFeatureData(boolean value) {
        CheckedUtil.putWithoutChecking(super._map, "useLatestFeatureData", value);
        _useLatestFeatureDataField = value;
        return this;
    }

    @Override
    public UseLatestJoinTimeSettings clone()
        throws CloneNotSupportedException
    {
        UseLatestJoinTimeSettings __clone = ((UseLatestJoinTimeSettings) super.clone());
        __clone.__changeListener = new UseLatestJoinTimeSettings.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public UseLatestJoinTimeSettings copy()
        throws CloneNotSupportedException
    {
        UseLatestJoinTimeSettings __copy = ((UseLatestJoinTimeSettings) super.copy());
        __copy._useLatestFeatureDataField = null;
        __copy.__changeListener = new UseLatestJoinTimeSettings.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final UseLatestJoinTimeSettings __objectRef;

        private ChangeListener(UseLatestJoinTimeSettings reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "useLatestFeatureData":
                    __objectRef._useLatestFeatureDataField = null;
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
         * Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.
         * When useLatestFeatureData is set, there should be no other time-based parameters.
         * 
         */
        public PathSpec useLatestFeatureData() {
            return new PathSpec(getPathComponents(), "useLatestFeatureData");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * Boolean value, if set to true, indicates that the latest available feature data is to be used for joining.
         * When useLatestFeatureData is set, there should be no other time-based parameters.
         * 
         */
        public UseLatestJoinTimeSettings.ProjectionMask withUseLatestFeatureData() {
            getDataMap().put("useLatestFeatureData", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
