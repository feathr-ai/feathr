
package com.linkedin.feathr.fds;

import java.util.List;
import java.util.function.Function;
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
 * Container for full FDS metadata. Note that this container is not stored on disk as
 * part of Avro Schema. It is only used as a container to hold both top-level and per-column
 * metadata in one place in memory.
 * <p/>
 * More details can be found in the specification at docs/qt_fds_spec.md
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeaturizedDatasetMetadata.pdl.")
public class FeaturizedDatasetMetadata
    extends RecordTemplate
{

    private final static FeaturizedDatasetMetadata.Fields _fields = new FeaturizedDatasetMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Container for full FDS metadata. Note that this container is not stored on disk as\npart of Avro Schema. It is only used as a container to hold both top-level and per-column\nmetadata in one place in memory.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeaturizedDatasetMetadata{/**Metadata on the whole FDS (column agnostic).*/topLevelMetadata:/**Mandatory Metadata for the whole Featurized Dataset (not tied to indivual columns).\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeaturizedDatasetTopLevelMetadata{/**Schema version of FDS.*/fdsSchemaVersion:/**Schema version for the FDS. This value determines how the schema of an FDS must be interpretted.*/enum FeaturizedDatasetSchemaVersion{/**With V0, the FDS still has the legacy metadata type (FeaturizedDatasetColumnMetadata) for\nfeature columns but also has the new metadata types (FeaturizedDatasetTopLevelMetadata and\nColumnMetadata). With this version, the new metadata is not used yet. It is just\npopulated but all the decisions are made based on legacy metadata.\nThis version is not supported as of Jan 2021.*/V0/**With V1, the deprecated metadata type is not present. The new metadata types\n(FeaturizedDatasetTopLevelMetadata and ColumnMetadata) are guaranteed to be\npresent in any valid FDS.*/V1}}/**Metadata on each column of FDS.*/columnMetadata:map[string/**Mandatory Metadata for each column in a Featurized Dataset.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record ColumnMetadata{/**metadata for a single column in FDS.*/metadata:union[/**FeatureColumnMetadata structure capturing metadata of columns tgat cintain features (\nas defined in the spec). In this context, \"feature columns\" contain features, labels,\nentity ids, weights and all other columns that are used in computation/training (as opposed\nto opaque contextual columns).\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeatureColumnMetadata{/**Category of the tensor (e.g. TensorCategory.DENSE, TensorCategory.SPARSE)*/tensorCategory:/**Representation type of a feature\n<p/>\nMore details can be found in the specification\nhttps://docs.google.com/document/d/12VZis-6VyQN3Ivy2rQxf-OktZqCowuTvVrsIFxwTVXU/edit#heading=h.jjuk2t7bn84*/enum TensorCategory{/**A tensor where we only store values for some positions.*/SPARSE/**A tensor where we store a value for each position.*/DENSE/**RAGGED tensors (also called nested tensors) are similar to dense tensors but have variable-length\ndimensions.*/RAGGED}/**The shape (sometimes called the \u201csize\u201d or \u201cdense shape\u201d) of the tensor.\nGiven as a list of integers. The first element gives the size of the first dimension\nin the tensor, the second element gives the size of the second dimension, and so on.\nThe length of the tensorShape array is the number of dimensions in the tensor, also\ncalled the tensor's rank.\nThe data schema must match the tensor\u2019s rank as indicated in the tensorShape array;\nwhich in case of DENSE means that the level of nesting of the array must\nmatch the rank, and in case of SPARSE there must be the same number of\nindices fields as the rank.\nDimensions are allowed to have unknown size. Elements in the tensorShape array may be\nset as (-1) to indicate unknown.\nFor any dimension having dimensionType of string, that dimension\u2019s size must be set\nto (-1) (unknown).*/tensorShape:array[int]/**List of the types for each dimension. Given as an array of strings.\nFor DENSE features, must contain only DimensionType.INT. For SPARSE features,\nmust correspond to the data types of the corresponding indices fields in the data schema*/dimensionTypes:array[/**Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of\nthat dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,\nelements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2][\"Germany\"]).*/enum DimensionType{/**The tensor dimension is of type integer.*/INT/**The tensor dimension is of type long.*/LONG/**The tensor dimension is of type string.*/STRING}]/**The value type (e.g. ValueType.INT, ValueType.FLOAT)*/valueType:/**The value type for a feature (column) of FDS.*/enum ValueType{/**The value is an integer.*/INT/**The value is a long.*/LONG/**The value is a float.*/FLOAT/**The value is a double.*/DOUBLE/**The value is a boolean.*/BOOLEAN/**The value is a string.*/STRING/**The value is a byte-array.*/BYTES}/**Optional metadata that helps with linkage of a feature. See the documentation\non FeatureColumnLinkageMetadata for more info.*/linkageMetadata:optional/**Metadata that helps with linkage of a feature. That is, metadata\nneeded to link the column name to an access method for getting the contents.\nFor example, if a column is populated using a frame feature and then a model\nis trained using that feature/column, the external feature name in the model\nwill be the same as the column name. Linkage metadata allows us to link/bind\nthat external feature of the model with some value during inference. For example,\nif the FDS column corresponded to a frame feature, linkage metadata will contain\nthe unique identifier of that frame feature. This infomation can then be encoded\nin the model bundle and used to bind that external feature to a concrete value\nat inference time.*/record FeatureColumnLinkageMetadata{/** See above **/featureLinkageMetadata:union[/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}]}}/**OpaqueContextualColumnMetadata structure capturing metadata of columns that contain\nOpaque Contextual Data.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record OpaqueContextualColumnMetadata{}]}]}", SchemaFormatType.PDL));
    private FeaturizedDatasetTopLevelMetadata _topLevelMetadataField = null;
    private ColumnMetadataMap _columnMetadataField = null;
    private FeaturizedDatasetMetadata.ChangeListener __changeListener = new FeaturizedDatasetMetadata.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TopLevelMetadata = SCHEMA.getField("topLevelMetadata");
    private final static RecordDataSchema.Field FIELD_ColumnMetadata = SCHEMA.getField("columnMetadata");

    public FeaturizedDatasetMetadata() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public FeaturizedDatasetMetadata(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeaturizedDatasetMetadata.Fields fields() {
        return _fields;
    }

    public static FeaturizedDatasetMetadata.ProjectionMask createMask() {
        return new FeaturizedDatasetMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for topLevelMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#topLevelMetadata
     */
    public boolean hasTopLevelMetadata() {
        if (_topLevelMetadataField!= null) {
            return true;
        }
        return super._map.containsKey("topLevelMetadata");
    }

    /**
     * Remover for topLevelMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#topLevelMetadata
     */
    public void removeTopLevelMetadata() {
        super._map.remove("topLevelMetadata");
    }

    /**
     * Getter for topLevelMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#topLevelMetadata
     */
    public FeaturizedDatasetTopLevelMetadata getTopLevelMetadata(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTopLevelMetadata();
            case DEFAULT:
            case NULL:
                if (_topLevelMetadataField!= null) {
                    return _topLevelMetadataField;
                } else {
                    Object __rawValue = super._map.get("topLevelMetadata");
                    _topLevelMetadataField = ((__rawValue == null)?null:new FeaturizedDatasetTopLevelMetadata(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _topLevelMetadataField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for topLevelMetadata
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeaturizedDatasetMetadata.Fields#topLevelMetadata
     */
    @Nonnull
    public FeaturizedDatasetTopLevelMetadata getTopLevelMetadata() {
        if (_topLevelMetadataField!= null) {
            return _topLevelMetadataField;
        } else {
            Object __rawValue = super._map.get("topLevelMetadata");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("topLevelMetadata");
            }
            _topLevelMetadataField = ((__rawValue == null)?null:new FeaturizedDatasetTopLevelMetadata(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _topLevelMetadataField;
        }
    }

    /**
     * Setter for topLevelMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#topLevelMetadata
     */
    public FeaturizedDatasetMetadata setTopLevelMetadata(FeaturizedDatasetTopLevelMetadata value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTopLevelMetadata(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field topLevelMetadata of com.linkedin.feathr.fds.FeaturizedDatasetMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "topLevelMetadata", value.data());
                    _topLevelMetadataField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTopLevelMetadata();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "topLevelMetadata", value.data());
                    _topLevelMetadataField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "topLevelMetadata", value.data());
                    _topLevelMetadataField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for topLevelMetadata
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeaturizedDatasetMetadata.Fields#topLevelMetadata
     */
    public FeaturizedDatasetMetadata setTopLevelMetadata(
        @Nonnull
        FeaturizedDatasetTopLevelMetadata value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field topLevelMetadata of com.linkedin.feathr.fds.FeaturizedDatasetMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "topLevelMetadata", value.data());
            _topLevelMetadataField = value;
        }
        return this;
    }

    /**
     * Existence checker for columnMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#columnMetadata
     */
    public boolean hasColumnMetadata() {
        if (_columnMetadataField!= null) {
            return true;
        }
        return super._map.containsKey("columnMetadata");
    }

    /**
     * Remover for columnMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#columnMetadata
     */
    public void removeColumnMetadata() {
        super._map.remove("columnMetadata");
    }

    /**
     * Getter for columnMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#columnMetadata
     */
    public ColumnMetadataMap getColumnMetadata(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getColumnMetadata();
            case DEFAULT:
            case NULL:
                if (_columnMetadataField!= null) {
                    return _columnMetadataField;
                } else {
                    Object __rawValue = super._map.get("columnMetadata");
                    _columnMetadataField = ((__rawValue == null)?null:new ColumnMetadataMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _columnMetadataField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for columnMetadata
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeaturizedDatasetMetadata.Fields#columnMetadata
     */
    @Nonnull
    public ColumnMetadataMap getColumnMetadata() {
        if (_columnMetadataField!= null) {
            return _columnMetadataField;
        } else {
            Object __rawValue = super._map.get("columnMetadata");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("columnMetadata");
            }
            _columnMetadataField = ((__rawValue == null)?null:new ColumnMetadataMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _columnMetadataField;
        }
    }

    /**
     * Setter for columnMetadata
     * 
     * @see FeaturizedDatasetMetadata.Fields#columnMetadata
     */
    public FeaturizedDatasetMetadata setColumnMetadata(ColumnMetadataMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setColumnMetadata(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field columnMetadata of com.linkedin.feathr.fds.FeaturizedDatasetMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "columnMetadata", value.data());
                    _columnMetadataField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeColumnMetadata();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "columnMetadata", value.data());
                    _columnMetadataField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "columnMetadata", value.data());
                    _columnMetadataField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for columnMetadata
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeaturizedDatasetMetadata.Fields#columnMetadata
     */
    public FeaturizedDatasetMetadata setColumnMetadata(
        @Nonnull
        ColumnMetadataMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field columnMetadata of com.linkedin.feathr.fds.FeaturizedDatasetMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "columnMetadata", value.data());
            _columnMetadataField = value;
        }
        return this;
    }

    @Override
    public FeaturizedDatasetMetadata clone()
        throws CloneNotSupportedException
    {
        FeaturizedDatasetMetadata __clone = ((FeaturizedDatasetMetadata) super.clone());
        __clone.__changeListener = new FeaturizedDatasetMetadata.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeaturizedDatasetMetadata copy()
        throws CloneNotSupportedException
    {
        FeaturizedDatasetMetadata __copy = ((FeaturizedDatasetMetadata) super.copy());
        __copy._topLevelMetadataField = null;
        __copy._columnMetadataField = null;
        __copy.__changeListener = new FeaturizedDatasetMetadata.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeaturizedDatasetMetadata __objectRef;

        private ChangeListener(FeaturizedDatasetMetadata reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "topLevelMetadata":
                    __objectRef._topLevelMetadataField = null;
                    break;
                case "columnMetadata":
                    __objectRef._columnMetadataField = null;
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
         * Metadata on the whole FDS (column agnostic).
         * 
         */
        public com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata.Fields topLevelMetadata() {
            return new com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata.Fields(getPathComponents(), "topLevelMetadata");
        }

        /**
         * Metadata on each column of FDS.
         * 
         */
        public com.linkedin.feathr.fds.ColumnMetadataMap.Fields columnMetadata() {
            return new com.linkedin.feathr.fds.ColumnMetadataMap.Fields(getPathComponents(), "columnMetadata");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata.ProjectionMask _topLevelMetadataMask;
        private com.linkedin.feathr.fds.ColumnMetadataMap.ProjectionMask _columnMetadataMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Metadata on the whole FDS (column agnostic).
         * 
         */
        public FeaturizedDatasetMetadata.ProjectionMask withTopLevelMetadata(Function<com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata.ProjectionMask, com.linkedin.feathr.fds.FeaturizedDatasetTopLevelMetadata.ProjectionMask> nestedMask) {
            _topLevelMetadataMask = nestedMask.apply(((_topLevelMetadataMask == null)?FeaturizedDatasetTopLevelMetadata.createMask():_topLevelMetadataMask));
            getDataMap().put("topLevelMetadata", _topLevelMetadataMask.getDataMap());
            return this;
        }

        /**
         * Metadata on the whole FDS (column agnostic).
         * 
         */
        public FeaturizedDatasetMetadata.ProjectionMask withTopLevelMetadata() {
            _topLevelMetadataMask = null;
            getDataMap().put("topLevelMetadata", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Metadata on each column of FDS.
         * 
         */
        public FeaturizedDatasetMetadata.ProjectionMask withColumnMetadata(Function<com.linkedin.feathr.fds.ColumnMetadataMap.ProjectionMask, com.linkedin.feathr.fds.ColumnMetadataMap.ProjectionMask> nestedMask) {
            _columnMetadataMask = nestedMask.apply(((_columnMetadataMask == null)?ColumnMetadataMap.createMask():_columnMetadataMask));
            getDataMap().put("columnMetadata", _columnMetadataMask.getDataMap());
            return this;
        }

        /**
         * Metadata on each column of FDS.
         * 
         */
        public FeaturizedDatasetMetadata.ProjectionMask withColumnMetadata() {
            _columnMetadataMask = null;
            getDataMap().put("columnMetadata", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
