
package com.linkedin.frame.common;

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
import com.linkedin.data.template.StringMap;


/**
 * Presentation instructions defining how to fetch member facing value of an inference from a map defined along with the rest of the metadata
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from fr-java-data-models/src/main/pegasus/com/linkedin/frame/common/PresentationInlineMappingInfo.pdl.")
public class PresentationInlineMappingInfo
    extends RecordTemplate
{

    private final static PresentationInlineMappingInfo.Fields _fields = new PresentationInlineMappingInfo.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.frame.common/**Presentation instructions defining how to fetch member facing value of an inference from a map defined along with the rest of the metadata*/@gma.aspect.entity.urn=\"com.linkedin.common.InferenceUrn\"record PresentationInlineMappingInfo{/**Name of the mapping to indicate the mapping context, used in search and discovery*/name:string/**Key value map defining the mapping of inference internal value to member facing value*/mapping:map[string,string]}", SchemaFormatType.PDL));
    private String _nameField = null;
    private StringMap _mappingField = null;
    private PresentationInlineMappingInfo.ChangeListener __changeListener = new PresentationInlineMappingInfo.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Name = SCHEMA.getField("name");
    private final static RecordDataSchema.Field FIELD_Mapping = SCHEMA.getField("mapping");

    public PresentationInlineMappingInfo() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public PresentationInlineMappingInfo(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static PresentationInlineMappingInfo.Fields fields() {
        return _fields;
    }

    public static PresentationInlineMappingInfo.ProjectionMask createMask() {
        return new PresentationInlineMappingInfo.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for name
     * 
     * @see PresentationInlineMappingInfo.Fields#name
     */
    public boolean hasName() {
        if (_nameField!= null) {
            return true;
        }
        return super._map.containsKey("name");
    }

    /**
     * Remover for name
     * 
     * @see PresentationInlineMappingInfo.Fields#name
     */
    public void removeName() {
        super._map.remove("name");
    }

    /**
     * Getter for name
     * 
     * @see PresentationInlineMappingInfo.Fields#name
     */
    public String getName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getName();
            case DEFAULT:
            case NULL:
                if (_nameField!= null) {
                    return _nameField;
                } else {
                    Object __rawValue = super._map.get("name");
                    _nameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _nameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for name
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PresentationInlineMappingInfo.Fields#name
     */
    @Nonnull
    public String getName() {
        if (_nameField!= null) {
            return _nameField;
        } else {
            Object __rawValue = super._map.get("name");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("name");
            }
            _nameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _nameField;
        }
    }

    /**
     * Setter for name
     * 
     * @see PresentationInlineMappingInfo.Fields#name
     */
    public PresentationInlineMappingInfo setName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field name of com.linkedin.frame.common.PresentationInlineMappingInfo");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for name
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PresentationInlineMappingInfo.Fields#name
     */
    public PresentationInlineMappingInfo setName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field name of com.linkedin.frame.common.PresentationInlineMappingInfo to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "name", value);
            _nameField = value;
        }
        return this;
    }

    /**
     * Existence checker for mapping
     * 
     * @see PresentationInlineMappingInfo.Fields#mapping
     */
    public boolean hasMapping() {
        if (_mappingField!= null) {
            return true;
        }
        return super._map.containsKey("mapping");
    }

    /**
     * Remover for mapping
     * 
     * @see PresentationInlineMappingInfo.Fields#mapping
     */
    public void removeMapping() {
        super._map.remove("mapping");
    }

    /**
     * Getter for mapping
     * 
     * @see PresentationInlineMappingInfo.Fields#mapping
     */
    public StringMap getMapping(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getMapping();
            case DEFAULT:
            case NULL:
                if (_mappingField!= null) {
                    return _mappingField;
                } else {
                    Object __rawValue = super._map.get("mapping");
                    _mappingField = ((__rawValue == null)?null:new StringMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _mappingField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for mapping
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PresentationInlineMappingInfo.Fields#mapping
     */
    @Nonnull
    public StringMap getMapping() {
        if (_mappingField!= null) {
            return _mappingField;
        } else {
            Object __rawValue = super._map.get("mapping");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("mapping");
            }
            _mappingField = ((__rawValue == null)?null:new StringMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _mappingField;
        }
    }

    /**
     * Setter for mapping
     * 
     * @see PresentationInlineMappingInfo.Fields#mapping
     */
    public PresentationInlineMappingInfo setMapping(StringMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setMapping(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field mapping of com.linkedin.frame.common.PresentationInlineMappingInfo");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "mapping", value.data());
                    _mappingField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeMapping();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "mapping", value.data());
                    _mappingField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "mapping", value.data());
                    _mappingField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for mapping
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PresentationInlineMappingInfo.Fields#mapping
     */
    public PresentationInlineMappingInfo setMapping(
        @Nonnull
        StringMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field mapping of com.linkedin.frame.common.PresentationInlineMappingInfo to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "mapping", value.data());
            _mappingField = value;
        }
        return this;
    }

    @Override
    public PresentationInlineMappingInfo clone()
        throws CloneNotSupportedException
    {
        PresentationInlineMappingInfo __clone = ((PresentationInlineMappingInfo) super.clone());
        __clone.__changeListener = new PresentationInlineMappingInfo.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public PresentationInlineMappingInfo copy()
        throws CloneNotSupportedException
    {
        PresentationInlineMappingInfo __copy = ((PresentationInlineMappingInfo) super.copy());
        __copy._mappingField = null;
        __copy._nameField = null;
        __copy.__changeListener = new PresentationInlineMappingInfo.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final PresentationInlineMappingInfo __objectRef;

        private ChangeListener(PresentationInlineMappingInfo reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "mapping":
                    __objectRef._mappingField = null;
                    break;
                case "name":
                    __objectRef._nameField = null;
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
         * Name of the mapping to indicate the mapping context, used in search and discovery
         * 
         */
        public PathSpec name() {
            return new PathSpec(getPathComponents(), "name");
        }

        /**
         * Key value map defining the mapping of inference internal value to member facing value
         * 
         */
        public PathSpec mapping() {
            return new PathSpec(getPathComponents(), "mapping");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Name of the mapping to indicate the mapping context, used in search and discovery
         * 
         */
        public PresentationInlineMappingInfo.ProjectionMask withName() {
            getDataMap().put("name", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Key value map defining the mapping of inference internal value to member facing value
         * 
         */
        public PresentationInlineMappingInfo.ProjectionMask withMapping() {
            getDataMap().put("mapping", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
