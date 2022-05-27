
package com.linkedin.feathr.fds;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataList;
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
import com.linkedin.data.template.StringArray;


/**
 * Used when the feature column contains a frame feature.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeatureColumnLinkageMetadata.pdl.")
public class FrameFeatureMetadata
    extends RecordTemplate
{

    private final static FrameFeatureMetadata.Fields _fields = new FrameFeatureMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}", SchemaFormatType.PDL));
    private String _frameFeatureNameField = null;
    private StringArray _keyColumnsField = null;
    private FrameFeatureMetadata.ChangeListener __changeListener = new FrameFeatureMetadata.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_FrameFeatureName = SCHEMA.getField("frameFeatureName");
    private final static RecordDataSchema.Field FIELD_KeyColumns = SCHEMA.getField("keyColumns");

    public FrameFeatureMetadata() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public FrameFeatureMetadata(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FrameFeatureMetadata.Fields fields() {
        return _fields;
    }

    public static FrameFeatureMetadata.ProjectionMask createMask() {
        return new FrameFeatureMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for frameFeatureName
     * 
     * @see FrameFeatureMetadata.Fields#frameFeatureName
     */
    public boolean hasFrameFeatureName() {
        if (_frameFeatureNameField!= null) {
            return true;
        }
        return super._map.containsKey("frameFeatureName");
    }

    /**
     * Remover for frameFeatureName
     * 
     * @see FrameFeatureMetadata.Fields#frameFeatureName
     */
    public void removeFrameFeatureName() {
        super._map.remove("frameFeatureName");
    }

    /**
     * Getter for frameFeatureName
     * 
     * @see FrameFeatureMetadata.Fields#frameFeatureName
     */
    public String getFrameFeatureName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFrameFeatureName();
            case DEFAULT:
            case NULL:
                if (_frameFeatureNameField!= null) {
                    return _frameFeatureNameField;
                } else {
                    Object __rawValue = super._map.get("frameFeatureName");
                    _frameFeatureNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _frameFeatureNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for frameFeatureName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FrameFeatureMetadata.Fields#frameFeatureName
     */
    @Nonnull
    public String getFrameFeatureName() {
        if (_frameFeatureNameField!= null) {
            return _frameFeatureNameField;
        } else {
            Object __rawValue = super._map.get("frameFeatureName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("frameFeatureName");
            }
            _frameFeatureNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _frameFeatureNameField;
        }
    }

    /**
     * Setter for frameFeatureName
     * 
     * @see FrameFeatureMetadata.Fields#frameFeatureName
     */
    public FrameFeatureMetadata setFrameFeatureName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFrameFeatureName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field frameFeatureName of com.linkedin.feathr.fds.FrameFeatureMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
                    _frameFeatureNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFrameFeatureName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
                    _frameFeatureNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
                    _frameFeatureNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for frameFeatureName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FrameFeatureMetadata.Fields#frameFeatureName
     */
    public FrameFeatureMetadata setFrameFeatureName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field frameFeatureName of com.linkedin.feathr.fds.FrameFeatureMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "frameFeatureName", value);
            _frameFeatureNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyColumns
     * 
     * @see FrameFeatureMetadata.Fields#keyColumns
     */
    public boolean hasKeyColumns() {
        if (_keyColumnsField!= null) {
            return true;
        }
        return super._map.containsKey("keyColumns");
    }

    /**
     * Remover for keyColumns
     * 
     * @see FrameFeatureMetadata.Fields#keyColumns
     */
    public void removeKeyColumns() {
        super._map.remove("keyColumns");
    }

    /**
     * Getter for keyColumns
     * 
     * @see FrameFeatureMetadata.Fields#keyColumns
     */
    public StringArray getKeyColumns(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeyColumns();
            case DEFAULT:
            case NULL:
                if (_keyColumnsField!= null) {
                    return _keyColumnsField;
                } else {
                    Object __rawValue = super._map.get("keyColumns");
                    _keyColumnsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keyColumnsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyColumns
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FrameFeatureMetadata.Fields#keyColumns
     */
    @Nonnull
    public StringArray getKeyColumns() {
        if (_keyColumnsField!= null) {
            return _keyColumnsField;
        } else {
            Object __rawValue = super._map.get("keyColumns");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keyColumns");
            }
            _keyColumnsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keyColumnsField;
        }
    }

    /**
     * Setter for keyColumns
     * 
     * @see FrameFeatureMetadata.Fields#keyColumns
     */
    public FrameFeatureMetadata setKeyColumns(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyColumns(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyColumns of com.linkedin.feathr.fds.FrameFeatureMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
                    _keyColumnsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyColumns();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
                    _keyColumnsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
                    _keyColumnsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyColumns
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FrameFeatureMetadata.Fields#keyColumns
     */
    public FrameFeatureMetadata setKeyColumns(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyColumns of com.linkedin.feathr.fds.FrameFeatureMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyColumns", value.data());
            _keyColumnsField = value;
        }
        return this;
    }

    @Override
    public FrameFeatureMetadata clone()
        throws CloneNotSupportedException
    {
        FrameFeatureMetadata __clone = ((FrameFeatureMetadata) super.clone());
        __clone.__changeListener = new FrameFeatureMetadata.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FrameFeatureMetadata copy()
        throws CloneNotSupportedException
    {
        FrameFeatureMetadata __copy = ((FrameFeatureMetadata) super.copy());
        __copy._keyColumnsField = null;
        __copy._frameFeatureNameField = null;
        __copy.__changeListener = new FrameFeatureMetadata.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FrameFeatureMetadata __objectRef;

        private ChangeListener(FrameFeatureMetadata reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyColumns":
                    __objectRef._keyColumnsField = null;
                    break;
                case "frameFeatureName":
                    __objectRef._frameFeatureNameField = null;
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
         * Name of a frame feature, uniquely identifying a particular version of a frame feature.
         * This name includes the version (e.g., myFeature-1_0_0).
         * 
         */
        public PathSpec frameFeatureName() {
            return new PathSpec(getPathComponents(), "frameFeatureName");
        }

        /**
         * List of columns that have entity IDs for this feature.
         * 
         */
        public PathSpec keyColumns() {
            return new PathSpec(getPathComponents(), "keyColumns");
        }

        /**
         * List of columns that have entity IDs for this feature.
         * 
         */
        public PathSpec keyColumns(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "keyColumns");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Name of a frame feature, uniquely identifying a particular version of a frame feature.
         * This name includes the version (e.g., myFeature-1_0_0).
         * 
         */
        public FrameFeatureMetadata.ProjectionMask withFrameFeatureName() {
            getDataMap().put("frameFeatureName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * List of columns that have entity IDs for this feature.
         * 
         */
        public FrameFeatureMetadata.ProjectionMask withKeyColumns() {
            getDataMap().put("keyColumns", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * List of columns that have entity IDs for this feature.
         * 
         */
        public FrameFeatureMetadata.ProjectionMask withKeyColumns(Integer start, Integer count) {
            getDataMap().put("keyColumns", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("keyColumns").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyColumns").put("$count", count);
            }
            return this;
        }

    }

}
