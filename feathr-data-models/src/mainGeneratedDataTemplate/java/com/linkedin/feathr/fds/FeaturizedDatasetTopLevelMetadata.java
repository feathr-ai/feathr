
package com.linkedin.feathr.fds;

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
 * Mandatory Metadata for the whole Featurized Dataset (not tied to indivual columns).
 * <p/>
 * More details can be found in the specification at docs/qt_fds_spec.md
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeaturizedDatasetTopLevelMetadata.pdl.")
public class FeaturizedDatasetTopLevelMetadata
    extends RecordTemplate
{

    private final static FeaturizedDatasetTopLevelMetadata.Fields _fields = new FeaturizedDatasetTopLevelMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Mandatory Metadata for the whole Featurized Dataset (not tied to indivual columns).\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeaturizedDatasetTopLevelMetadata{/**Schema version of FDS.*/fdsSchemaVersion:/**Schema version for the FDS. This value determines how the schema of an FDS must be interpretted.*/enum FeaturizedDatasetSchemaVersion{/**With V0, the FDS still has the legacy metadata type (FeaturizedDatasetColumnMetadata) for\nfeature columns but also has the new metadata types (FeaturizedDatasetTopLevelMetadata and\nColumnMetadata). With this version, the new metadata is not used yet. It is just\npopulated but all the decisions are made based on legacy metadata.\nThis version is not supported as of Jan 2021.*/V0/**With V1, the deprecated metadata type is not present. The new metadata types\n(FeaturizedDatasetTopLevelMetadata and ColumnMetadata) are guaranteed to be\npresent in any valid FDS.*/V1}}", SchemaFormatType.PDL));
    private FeaturizedDatasetSchemaVersion _fdsSchemaVersionField = null;
    private FeaturizedDatasetTopLevelMetadata.ChangeListener __changeListener = new FeaturizedDatasetTopLevelMetadata.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_FdsSchemaVersion = SCHEMA.getField("fdsSchemaVersion");

    public FeaturizedDatasetTopLevelMetadata() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public FeaturizedDatasetTopLevelMetadata(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeaturizedDatasetTopLevelMetadata.Fields fields() {
        return _fields;
    }

    public static FeaturizedDatasetTopLevelMetadata.ProjectionMask createMask() {
        return new FeaturizedDatasetTopLevelMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for fdsSchemaVersion
     * 
     * @see FeaturizedDatasetTopLevelMetadata.Fields#fdsSchemaVersion
     */
    public boolean hasFdsSchemaVersion() {
        if (_fdsSchemaVersionField!= null) {
            return true;
        }
        return super._map.containsKey("fdsSchemaVersion");
    }

    /**
     * Remover for fdsSchemaVersion
     * 
     * @see FeaturizedDatasetTopLevelMetadata.Fields#fdsSchemaVersion
     */
    public void removeFdsSchemaVersion() {
        super._map.remove("fdsSchemaVersion");
    }

    /**
     * Getter for fdsSchemaVersion
     * 
     * @see FeaturizedDatasetTopLevelMetadata.Fields#fdsSchemaVersion
     */
    public FeaturizedDatasetSchemaVersion getFdsSchemaVersion(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFdsSchemaVersion();
            case DEFAULT:
            case NULL:
                if (_fdsSchemaVersionField!= null) {
                    return _fdsSchemaVersionField;
                } else {
                    Object __rawValue = super._map.get("fdsSchemaVersion");
                    _fdsSchemaVersionField = DataTemplateUtil.coerceEnumOutput(__rawValue, FeaturizedDatasetSchemaVersion.class, FeaturizedDatasetSchemaVersion.$UNKNOWN);
                    return _fdsSchemaVersionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for fdsSchemaVersion
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeaturizedDatasetTopLevelMetadata.Fields#fdsSchemaVersion
     */
    @Nonnull
    public FeaturizedDatasetSchemaVersion getFdsSchemaVersion() {
        if (_fdsSchemaVersionField!= null) {
            return _fdsSchemaVersionField;
        } else {
            Object __rawValue = super._map.get("fdsSchemaVersion");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("fdsSchemaVersion");
            }
            _fdsSchemaVersionField = DataTemplateUtil.coerceEnumOutput(__rawValue, FeaturizedDatasetSchemaVersion.class, FeaturizedDatasetSchemaVersion.$UNKNOWN);
            return _fdsSchemaVersionField;
        }
    }

    /**
     * Setter for fdsSchemaVersion
     * 
     * @see FeaturizedDatasetTopLevelMetadata.Fields#fdsSchemaVersion
     */
    public FeaturizedDatasetTopLevelMetadata setFdsSchemaVersion(FeaturizedDatasetSchemaVersion value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFdsSchemaVersion(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field fdsSchemaVersion of com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "fdsSchemaVersion", value.name());
                    _fdsSchemaVersionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFdsSchemaVersion();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "fdsSchemaVersion", value.name());
                    _fdsSchemaVersionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "fdsSchemaVersion", value.name());
                    _fdsSchemaVersionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for fdsSchemaVersion
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeaturizedDatasetTopLevelMetadata.Fields#fdsSchemaVersion
     */
    public FeaturizedDatasetTopLevelMetadata setFdsSchemaVersion(
        @Nonnull
        FeaturizedDatasetSchemaVersion value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field fdsSchemaVersion of com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "fdsSchemaVersion", value.name());
            _fdsSchemaVersionField = value;
        }
        return this;
    }

    @Override
    public FeaturizedDatasetTopLevelMetadata clone()
        throws CloneNotSupportedException
    {
        FeaturizedDatasetTopLevelMetadata __clone = ((FeaturizedDatasetTopLevelMetadata) super.clone());
        __clone.__changeListener = new FeaturizedDatasetTopLevelMetadata.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeaturizedDatasetTopLevelMetadata copy()
        throws CloneNotSupportedException
    {
        FeaturizedDatasetTopLevelMetadata __copy = ((FeaturizedDatasetTopLevelMetadata) super.copy());
        __copy._fdsSchemaVersionField = null;
        __copy.__changeListener = new FeaturizedDatasetTopLevelMetadata.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeaturizedDatasetTopLevelMetadata __objectRef;

        private ChangeListener(FeaturizedDatasetTopLevelMetadata reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "fdsSchemaVersion":
                    __objectRef._fdsSchemaVersionField = null;
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
         * Schema version of FDS.
         * 
         */
        public PathSpec fdsSchemaVersion() {
            return new PathSpec(getPathComponents(), "fdsSchemaVersion");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * Schema version of FDS.
         * 
         */
        public FeaturizedDatasetTopLevelMetadata.ProjectionMask withFdsSchemaVersion() {
            getDataMap().put("fdsSchemaVersion", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
