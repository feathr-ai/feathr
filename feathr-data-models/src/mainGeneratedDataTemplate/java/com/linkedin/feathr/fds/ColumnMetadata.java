
package com.linkedin.feathr.fds;

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
 * Mandatory Metadata for each column in a Featurized Dataset.
 * <p/>
 * More details can be found in the specification at docs/qt_fds_spec.md
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/ColumnMetadata.pdl.")
public class ColumnMetadata
    extends RecordTemplate
{

    private final static ColumnMetadata.Fields _fields = new ColumnMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Mandatory Metadata for each column in a Featurized Dataset.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record ColumnMetadata{/**metadata for a single column in FDS.*/metadata:union[/**FeatureColumnMetadata structure capturing metadata of columns tgat cintain features (\nas defined in the spec). In this context, \"feature columns\" contain features, labels,\nentity ids, weights and all other columns that are used in computation/training (as opposed\nto opaque contextual columns).\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeatureColumnMetadata{/**Category of the tensor (e.g. TensorCategory.DENSE, TensorCategory.SPARSE)*/tensorCategory:/**Representation type of a feature\n<p/>\nMore details can be found in the specification\nhttps://docs.google.com/document/d/12VZis-6VyQN3Ivy2rQxf-OktZqCowuTvVrsIFxwTVXU/edit#heading=h.jjuk2t7bn84*/enum TensorCategory{/**A tensor where we only store values for some positions.*/SPARSE/**A tensor where we store a value for each position.*/DENSE/**RAGGED tensors (also called nested tensors) are similar to dense tensors but have variable-length\ndimensions.*/RAGGED}/**The shape (sometimes called the \u201csize\u201d or \u201cdense shape\u201d) of the tensor.\nGiven as a list of integers. The first element gives the size of the first dimension\nin the tensor, the second element gives the size of the second dimension, and so on.\nThe length of the tensorShape array is the number of dimensions in the tensor, also\ncalled the tensor's rank.\nThe data schema must match the tensor\u2019s rank as indicated in the tensorShape array;\nwhich in case of DENSE means that the level of nesting of the array must\nmatch the rank, and in case of SPARSE there must be the same number of\nindices fields as the rank.\nDimensions are allowed to have unknown size. Elements in the tensorShape array may be\nset as (-1) to indicate unknown.\nFor any dimension having dimensionType of string, that dimension\u2019s size must be set\nto (-1) (unknown).*/tensorShape:array[int]/**List of the types for each dimension. Given as an array of strings.\nFor DENSE features, must contain only DimensionType.INT. For SPARSE features,\nmust correspond to the data types of the corresponding indices fields in the data schema*/dimensionTypes:array[/**Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of\nthat dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,\nelements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2][\"Germany\"]).*/enum DimensionType{/**The tensor dimension is of type integer.*/INT/**The tensor dimension is of type long.*/LONG/**The tensor dimension is of type string.*/STRING}]/**The value type (e.g. ValueType.INT, ValueType.FLOAT)*/valueType:/**The value type for a feature (column) of FDS.*/enum ValueType{/**The value is an integer.*/INT/**The value is a long.*/LONG/**The value is a float.*/FLOAT/**The value is a double.*/DOUBLE/**The value is a boolean.*/BOOLEAN/**The value is a string.*/STRING/**The value is a byte-array.*/BYTES}/**Optional metadata that helps with linkage of a feature. See the documentation\non FeatureColumnLinkageMetadata for more info.*/linkageMetadata:optional/**Metadata that helps with linkage of a feature. That is, metadata\nneeded to link the column name to an access method for getting the contents.\nFor example, if a column is populated using a frame feature and then a model\nis trained using that feature/column, the external feature name in the model\nwill be the same as the column name. Linkage metadata allows us to link/bind\nthat external feature of the model with some value during inference. For example,\nif the FDS column corresponded to a frame feature, linkage metadata will contain\nthe unique identifier of that frame feature. This infomation can then be encoded\nin the model bundle and used to bind that external feature to a concrete value\nat inference time.*/record FeatureColumnLinkageMetadata{/** See above **/featureLinkageMetadata:union[/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}]}}/**OpaqueContextualColumnMetadata structure capturing metadata of columns that contain\nOpaque Contextual Data.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record OpaqueContextualColumnMetadata{}]}", SchemaFormatType.PDL));
    private ColumnMetadata.Metadata _metadataField = null;
    private ColumnMetadata.ChangeListener __changeListener = new ColumnMetadata.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Metadata = SCHEMA.getField("metadata");

    public ColumnMetadata() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public ColumnMetadata(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static ColumnMetadata.Fields fields() {
        return _fields;
    }

    public static ColumnMetadata.ProjectionMask createMask() {
        return new ColumnMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for metadata
     * 
     * @see ColumnMetadata.Fields#metadata
     */
    public boolean hasMetadata() {
        if (_metadataField!= null) {
            return true;
        }
        return super._map.containsKey("metadata");
    }

    /**
     * Remover for metadata
     * 
     * @see ColumnMetadata.Fields#metadata
     */
    public void removeMetadata() {
        super._map.remove("metadata");
    }

    /**
     * Getter for metadata
     * 
     * @see ColumnMetadata.Fields#metadata
     */
    public ColumnMetadata.Metadata getMetadata(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getMetadata();
            case DEFAULT:
            case NULL:
                if (_metadataField!= null) {
                    return _metadataField;
                } else {
                    Object __rawValue = super._map.get("metadata");
                    _metadataField = ((__rawValue == null)?null:new ColumnMetadata.Metadata(__rawValue));
                    return _metadataField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for metadata
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see ColumnMetadata.Fields#metadata
     */
    @Nonnull
    public ColumnMetadata.Metadata getMetadata() {
        if (_metadataField!= null) {
            return _metadataField;
        } else {
            Object __rawValue = super._map.get("metadata");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("metadata");
            }
            _metadataField = ((__rawValue == null)?null:new ColumnMetadata.Metadata(__rawValue));
            return _metadataField;
        }
    }

    /**
     * Setter for metadata
     * 
     * @see ColumnMetadata.Fields#metadata
     */
    public ColumnMetadata setMetadata(ColumnMetadata.Metadata value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setMetadata(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field metadata of com.linkedin.feathr.fds.ColumnMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "metadata", value.data());
                    _metadataField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeMetadata();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "metadata", value.data());
                    _metadataField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "metadata", value.data());
                    _metadataField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for metadata
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see ColumnMetadata.Fields#metadata
     */
    public ColumnMetadata setMetadata(
        @Nonnull
        ColumnMetadata.Metadata value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field metadata of com.linkedin.feathr.fds.ColumnMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "metadata", value.data());
            _metadataField = value;
        }
        return this;
    }

    @Override
    public ColumnMetadata clone()
        throws CloneNotSupportedException
    {
        ColumnMetadata __clone = ((ColumnMetadata) super.clone());
        __clone.__changeListener = new ColumnMetadata.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public ColumnMetadata copy()
        throws CloneNotSupportedException
    {
        ColumnMetadata __copy = ((ColumnMetadata) super.copy());
        __copy._metadataField = null;
        __copy.__changeListener = new ColumnMetadata.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final ColumnMetadata __objectRef;

        private ChangeListener(ColumnMetadata reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "metadata":
                    __objectRef._metadataField = null;
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
         * metadata for a single column in FDS.
         * 
         */
        public com.linkedin.feathr.fds.ColumnMetadata.Metadata.Fields metadata() {
            return new com.linkedin.feathr.fds.ColumnMetadata.Metadata.Fields(getPathComponents(), "metadata");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/ColumnMetadata.pdl.")
    public static class Metadata
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.fds/**FeatureColumnMetadata structure capturing metadata of columns tgat cintain features (\nas defined in the spec). In this context, \"feature columns\" contain features, labels,\nentity ids, weights and all other columns that are used in computation/training (as opposed\nto opaque contextual columns).\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record FeatureColumnMetadata{/**Category of the tensor (e.g. TensorCategory.DENSE, TensorCategory.SPARSE)*/tensorCategory:/**Representation type of a feature\n<p/>\nMore details can be found in the specification\nhttps://docs.google.com/document/d/12VZis-6VyQN3Ivy2rQxf-OktZqCowuTvVrsIFxwTVXU/edit#heading=h.jjuk2t7bn84*/enum TensorCategory{/**A tensor where we only store values for some positions.*/SPARSE/**A tensor where we store a value for each position.*/DENSE/**RAGGED tensors (also called nested tensors) are similar to dense tensors but have variable-length\ndimensions.*/RAGGED}/**The shape (sometimes called the \u201csize\u201d or \u201cdense shape\u201d) of the tensor.\nGiven as a list of integers. The first element gives the size of the first dimension\nin the tensor, the second element gives the size of the second dimension, and so on.\nThe length of the tensorShape array is the number of dimensions in the tensor, also\ncalled the tensor's rank.\nThe data schema must match the tensor\u2019s rank as indicated in the tensorShape array;\nwhich in case of DENSE means that the level of nesting of the array must\nmatch the rank, and in case of SPARSE there must be the same number of\nindices fields as the rank.\nDimensions are allowed to have unknown size. Elements in the tensorShape array may be\nset as (-1) to indicate unknown.\nFor any dimension having dimensionType of string, that dimension\u2019s size must be set\nto (-1) (unknown).*/tensorShape:array[int]/**List of the types for each dimension. Given as an array of strings.\nFor DENSE features, must contain only DimensionType.INT. For SPARSE features,\nmust correspond to the data types of the corresponding indices fields in the data schema*/dimensionTypes:array[/**Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of\nthat dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,\nelements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2][\"Germany\"]).*/enum DimensionType{/**The tensor dimension is of type integer.*/INT/**The tensor dimension is of type long.*/LONG/**The tensor dimension is of type string.*/STRING}]/**The value type (e.g. ValueType.INT, ValueType.FLOAT)*/valueType:/**The value type for a feature (column) of FDS.*/enum ValueType{/**The value is an integer.*/INT/**The value is a long.*/LONG/**The value is a float.*/FLOAT/**The value is a double.*/DOUBLE/**The value is a boolean.*/BOOLEAN/**The value is a string.*/STRING/**The value is a byte-array.*/BYTES}/**Optional metadata that helps with linkage of a feature. See the documentation\non FeatureColumnLinkageMetadata for more info.*/linkageMetadata:optional/**Metadata that helps with linkage of a feature. That is, metadata\nneeded to link the column name to an access method for getting the contents.\nFor example, if a column is populated using a frame feature and then a model\nis trained using that feature/column, the external feature name in the model\nwill be the same as the column name. Linkage metadata allows us to link/bind\nthat external feature of the model with some value during inference. For example,\nif the FDS column corresponded to a frame feature, linkage metadata will contain\nthe unique identifier of that frame feature. This infomation can then be encoded\nin the model bundle and used to bind that external feature to a concrete value\nat inference time.*/record FeatureColumnLinkageMetadata{/** See above **/featureLinkageMetadata:union[/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}]}}}{namespace com.linkedin.feathr.fds/**OpaqueContextualColumnMetadata structure capturing metadata of columns that contain\nOpaque Contextual Data.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record OpaqueContextualColumnMetadata{}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.fds.FeatureColumnMetadata _featureColumnMetadataMember = null;
        private com.linkedin.feathr.fds.OpaqueContextualColumnMetadata _opaqueContextualColumnMetadataMember = null;
        private ColumnMetadata.Metadata.ChangeListener __changeListener = new ColumnMetadata.Metadata.ChangeListener(this);
        private final static DataSchema MEMBER_FeatureColumnMetadata = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.fds.FeatureColumnMetadata");
        private final static DataSchema MEMBER_OpaqueContextualColumnMetadata = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.fds.OpaqueContextualColumnMetadata");

        public Metadata() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public Metadata(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static ColumnMetadata.Metadata create(com.linkedin.feathr.fds.FeatureColumnMetadata value) {
            ColumnMetadata.Metadata newUnion = new ColumnMetadata.Metadata();
            newUnion.setFeatureColumnMetadata(value);
            return newUnion;
        }

        public boolean isFeatureColumnMetadata() {
            return memberIs("com.linkedin.feathr.fds.FeatureColumnMetadata");
        }

        public com.linkedin.feathr.fds.FeatureColumnMetadata getFeatureColumnMetadata() {
            checkNotNull();
            if (_featureColumnMetadataMember!= null) {
                return _featureColumnMetadataMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.fds.FeatureColumnMetadata");
            _featureColumnMetadataMember = ((__rawValue == null)?null:new com.linkedin.feathr.fds.FeatureColumnMetadata(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _featureColumnMetadataMember;
        }

        public void setFeatureColumnMetadata(com.linkedin.feathr.fds.FeatureColumnMetadata value) {
            checkNotNull();
            super._map.clear();
            _featureColumnMetadataMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.fds.FeatureColumnMetadata", value.data());
        }

        public static ColumnMetadata.Metadata create(com.linkedin.feathr.fds.OpaqueContextualColumnMetadata value) {
            ColumnMetadata.Metadata newUnion = new ColumnMetadata.Metadata();
            newUnion.setOpaqueContextualColumnMetadata(value);
            return newUnion;
        }

        public boolean isOpaqueContextualColumnMetadata() {
            return memberIs("com.linkedin.feathr.fds.OpaqueContextualColumnMetadata");
        }

        public com.linkedin.feathr.fds.OpaqueContextualColumnMetadata getOpaqueContextualColumnMetadata() {
            checkNotNull();
            if (_opaqueContextualColumnMetadataMember!= null) {
                return _opaqueContextualColumnMetadataMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.fds.OpaqueContextualColumnMetadata");
            _opaqueContextualColumnMetadataMember = ((__rawValue == null)?null:new com.linkedin.feathr.fds.OpaqueContextualColumnMetadata(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _opaqueContextualColumnMetadataMember;
        }

        public void setOpaqueContextualColumnMetadata(com.linkedin.feathr.fds.OpaqueContextualColumnMetadata value) {
            checkNotNull();
            super._map.clear();
            _opaqueContextualColumnMetadataMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.fds.OpaqueContextualColumnMetadata", value.data());
        }

        public static ColumnMetadata.Metadata.ProjectionMask createMask() {
            return new ColumnMetadata.Metadata.ProjectionMask();
        }

        @Override
        public ColumnMetadata.Metadata clone()
            throws CloneNotSupportedException
        {
            ColumnMetadata.Metadata __clone = ((ColumnMetadata.Metadata) super.clone());
            __clone.__changeListener = new ColumnMetadata.Metadata.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public ColumnMetadata.Metadata copy()
            throws CloneNotSupportedException
        {
            ColumnMetadata.Metadata __copy = ((ColumnMetadata.Metadata) super.copy());
            __copy._opaqueContextualColumnMetadataMember = null;
            __copy._featureColumnMetadataMember = null;
            __copy.__changeListener = new ColumnMetadata.Metadata.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final ColumnMetadata.Metadata __objectRef;

            private ChangeListener(ColumnMetadata.Metadata reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.fds.OpaqueContextualColumnMetadata":
                        __objectRef._opaqueContextualColumnMetadataMember = null;
                        break;
                    case "com.linkedin.feathr.fds.FeatureColumnMetadata":
                        __objectRef._featureColumnMetadataMember = null;
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

            public com.linkedin.feathr.fds.FeatureColumnMetadata.Fields FeatureColumnMetadata() {
                return new com.linkedin.feathr.fds.FeatureColumnMetadata.Fields(getPathComponents(), "com.linkedin.feathr.fds.FeatureColumnMetadata");
            }

            public com.linkedin.feathr.fds.OpaqueContextualColumnMetadata.Fields OpaqueContextualColumnMetadata() {
                return new com.linkedin.feathr.fds.OpaqueContextualColumnMetadata.Fields(getPathComponents(), "com.linkedin.feathr.fds.OpaqueContextualColumnMetadata");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.fds.FeatureColumnMetadata.ProjectionMask _FeatureColumnMetadataMask;
            private com.linkedin.feathr.fds.OpaqueContextualColumnMetadata.ProjectionMask _OpaqueContextualColumnMetadataMask;

            ProjectionMask() {
                super(3);
            }

            public ColumnMetadata.Metadata.ProjectionMask withFeatureColumnMetadata(Function<com.linkedin.feathr.fds.FeatureColumnMetadata.ProjectionMask, com.linkedin.feathr.fds.FeatureColumnMetadata.ProjectionMask> nestedMask) {
                _FeatureColumnMetadataMask = nestedMask.apply(((_FeatureColumnMetadataMask == null)?com.linkedin.feathr.fds.FeatureColumnMetadata.createMask():_FeatureColumnMetadataMask));
                getDataMap().put("com.linkedin.feathr.fds.FeatureColumnMetadata", _FeatureColumnMetadataMask.getDataMap());
                return this;
            }

            public ColumnMetadata.Metadata.ProjectionMask withOpaqueContextualColumnMetadata(Function<com.linkedin.feathr.fds.OpaqueContextualColumnMetadata.ProjectionMask, com.linkedin.feathr.fds.OpaqueContextualColumnMetadata.ProjectionMask> nestedMask) {
                _OpaqueContextualColumnMetadataMask = nestedMask.apply(((_OpaqueContextualColumnMetadataMask == null)?com.linkedin.feathr.fds.OpaqueContextualColumnMetadata.createMask():_OpaqueContextualColumnMetadataMask));
                getDataMap().put("com.linkedin.feathr.fds.OpaqueContextualColumnMetadata", _OpaqueContextualColumnMetadataMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.fds.ColumnMetadata.Metadata.ProjectionMask _metadataMask;

        ProjectionMask() {
            super(2);
        }

        /**
         * metadata for a single column in FDS.
         * 
         */
        public ColumnMetadata.ProjectionMask withMetadata(Function<com.linkedin.feathr.fds.ColumnMetadata.Metadata.ProjectionMask, com.linkedin.feathr.fds.ColumnMetadata.Metadata.ProjectionMask> nestedMask) {
            _metadataMask = nestedMask.apply(((_metadataMask == null)?ColumnMetadata.Metadata.createMask():_metadataMask));
            getDataMap().put("metadata", _metadataMask.getDataMap());
            return this;
        }

        /**
         * metadata for a single column in FDS.
         * 
         */
        public ColumnMetadata.ProjectionMask withMetadata() {
            _metadataMask = null;
            getDataMap().put("metadata", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
