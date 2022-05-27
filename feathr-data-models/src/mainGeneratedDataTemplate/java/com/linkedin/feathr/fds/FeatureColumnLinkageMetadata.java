
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
 * Metadata that helps with linkage of a feature. That is, metadata
 * needed to link the column name to an access method for getting the contents.
 * For example, if a column is populated using a frame feature and then a model
 * is trained using that feature/column, the external feature name in the model
 * will be the same as the column name. Linkage metadata allows us to link/bind
 * that external feature of the model with some value during inference. For example,
 * if the FDS column corresponded to a frame feature, linkage metadata will contain
 * the unique identifier of that frame feature. This infomation can then be encoded
 * in the model bundle and used to bind that external feature to a concrete value
 * at inference time.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeatureColumnLinkageMetadata.pdl.")
public class FeatureColumnLinkageMetadata
    extends RecordTemplate
{

    private final static FeatureColumnLinkageMetadata.Fields _fields = new FeatureColumnLinkageMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Metadata that helps with linkage of a feature. That is, metadata\nneeded to link the column name to an access method for getting the contents.\nFor example, if a column is populated using a frame feature and then a model\nis trained using that feature/column, the external feature name in the model\nwill be the same as the column name. Linkage metadata allows us to link/bind\nthat external feature of the model with some value during inference. For example,\nif the FDS column corresponded to a frame feature, linkage metadata will contain\nthe unique identifier of that frame feature. This infomation can then be encoded\nin the model bundle and used to bind that external feature to a concrete value\nat inference time.*/record FeatureColumnLinkageMetadata{/** See above **/featureLinkageMetadata:union[/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}]}", SchemaFormatType.PDL));
    private FeatureColumnLinkageMetadata.FeatureLinkageMetadata _featureLinkageMetadataField = null;
    private FeatureColumnLinkageMetadata.ChangeListener __changeListener = new FeatureColumnLinkageMetadata.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_FeatureLinkageMetadata = SCHEMA.getField("featureLinkageMetadata");

    public FeatureColumnLinkageMetadata() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public FeatureColumnLinkageMetadata(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeatureColumnLinkageMetadata.Fields fields() {
        return _fields;
    }

    public static FeatureColumnLinkageMetadata.ProjectionMask createMask() {
        return new FeatureColumnLinkageMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for featureLinkageMetadata
     * 
     * @see FeatureColumnLinkageMetadata.Fields#featureLinkageMetadata
     */
    public boolean hasFeatureLinkageMetadata() {
        if (_featureLinkageMetadataField!= null) {
            return true;
        }
        return super._map.containsKey("featureLinkageMetadata");
    }

    /**
     * Remover for featureLinkageMetadata
     * 
     * @see FeatureColumnLinkageMetadata.Fields#featureLinkageMetadata
     */
    public void removeFeatureLinkageMetadata() {
        super._map.remove("featureLinkageMetadata");
    }

    /**
     * Getter for featureLinkageMetadata
     * 
     * @see FeatureColumnLinkageMetadata.Fields#featureLinkageMetadata
     */
    public FeatureColumnLinkageMetadata.FeatureLinkageMetadata getFeatureLinkageMetadata(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFeatureLinkageMetadata();
            case DEFAULT:
            case NULL:
                if (_featureLinkageMetadataField!= null) {
                    return _featureLinkageMetadataField;
                } else {
                    Object __rawValue = super._map.get("featureLinkageMetadata");
                    _featureLinkageMetadataField = ((__rawValue == null)?null:new FeatureColumnLinkageMetadata.FeatureLinkageMetadata(__rawValue));
                    return _featureLinkageMetadataField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for featureLinkageMetadata
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureColumnLinkageMetadata.Fields#featureLinkageMetadata
     */
    @Nonnull
    public FeatureColumnLinkageMetadata.FeatureLinkageMetadata getFeatureLinkageMetadata() {
        if (_featureLinkageMetadataField!= null) {
            return _featureLinkageMetadataField;
        } else {
            Object __rawValue = super._map.get("featureLinkageMetadata");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("featureLinkageMetadata");
            }
            _featureLinkageMetadataField = ((__rawValue == null)?null:new FeatureColumnLinkageMetadata.FeatureLinkageMetadata(__rawValue));
            return _featureLinkageMetadataField;
        }
    }

    /**
     * Setter for featureLinkageMetadata
     * 
     * @see FeatureColumnLinkageMetadata.Fields#featureLinkageMetadata
     */
    public FeatureColumnLinkageMetadata setFeatureLinkageMetadata(FeatureColumnLinkageMetadata.FeatureLinkageMetadata value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureLinkageMetadata(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureLinkageMetadata of com.linkedin.feathr.fds.FeatureColumnLinkageMetadata");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureLinkageMetadata", value.data());
                    _featureLinkageMetadataField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureLinkageMetadata();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureLinkageMetadata", value.data());
                    _featureLinkageMetadataField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureLinkageMetadata", value.data());
                    _featureLinkageMetadataField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureLinkageMetadata
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureColumnLinkageMetadata.Fields#featureLinkageMetadata
     */
    public FeatureColumnLinkageMetadata setFeatureLinkageMetadata(
        @Nonnull
        FeatureColumnLinkageMetadata.FeatureLinkageMetadata value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureLinkageMetadata of com.linkedin.feathr.fds.FeatureColumnLinkageMetadata to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureLinkageMetadata", value.data());
            _featureLinkageMetadataField = value;
        }
        return this;
    }

    @Override
    public FeatureColumnLinkageMetadata clone()
        throws CloneNotSupportedException
    {
        FeatureColumnLinkageMetadata __clone = ((FeatureColumnLinkageMetadata) super.clone());
        __clone.__changeListener = new FeatureColumnLinkageMetadata.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeatureColumnLinkageMetadata copy()
        throws CloneNotSupportedException
    {
        FeatureColumnLinkageMetadata __copy = ((FeatureColumnLinkageMetadata) super.copy());
        __copy._featureLinkageMetadataField = null;
        __copy.__changeListener = new FeatureColumnLinkageMetadata.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeatureColumnLinkageMetadata __objectRef;

        private ChangeListener(FeatureColumnLinkageMetadata reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "featureLinkageMetadata":
                    __objectRef._featureLinkageMetadataField = null;
                    break;
            }
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeatureColumnLinkageMetadata.pdl.")
    public static class FeatureLinkageMetadata
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.fds/**Used when the feature column contains a frame feature.*/record FrameFeatureMetadata{/**Name of a frame feature, uniquely identifying a particular version of a frame feature.\nThis name includes the version (e.g., myFeature-1_0_0).*/frameFeatureName:string/**List of columns that have entity IDs for this feature.*/keyColumns:array[string]}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.fds.FrameFeatureMetadata _frameFeatureMetadataMember = null;
        private FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ChangeListener __changeListener = new FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ChangeListener(this);
        private final static DataSchema MEMBER_FrameFeatureMetadata = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.fds.FrameFeatureMetadata");

        public FeatureLinkageMetadata() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public FeatureLinkageMetadata(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static FeatureColumnLinkageMetadata.FeatureLinkageMetadata create(com.linkedin.feathr.fds.FrameFeatureMetadata value) {
            FeatureColumnLinkageMetadata.FeatureLinkageMetadata newUnion = new FeatureColumnLinkageMetadata.FeatureLinkageMetadata();
            newUnion.setFrameFeatureMetadata(value);
            return newUnion;
        }

        public boolean isFrameFeatureMetadata() {
            return memberIs("com.linkedin.feathr.fds.FrameFeatureMetadata");
        }

        public com.linkedin.feathr.fds.FrameFeatureMetadata getFrameFeatureMetadata() {
            checkNotNull();
            if (_frameFeatureMetadataMember!= null) {
                return _frameFeatureMetadataMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.fds.FrameFeatureMetadata");
            _frameFeatureMetadataMember = ((__rawValue == null)?null:new com.linkedin.feathr.fds.FrameFeatureMetadata(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _frameFeatureMetadataMember;
        }

        public void setFrameFeatureMetadata(com.linkedin.feathr.fds.FrameFeatureMetadata value) {
            checkNotNull();
            super._map.clear();
            _frameFeatureMetadataMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.fds.FrameFeatureMetadata", value.data());
        }

        public static FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ProjectionMask createMask() {
            return new FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ProjectionMask();
        }

        @Override
        public FeatureColumnLinkageMetadata.FeatureLinkageMetadata clone()
            throws CloneNotSupportedException
        {
            FeatureColumnLinkageMetadata.FeatureLinkageMetadata __clone = ((FeatureColumnLinkageMetadata.FeatureLinkageMetadata) super.clone());
            __clone.__changeListener = new FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public FeatureColumnLinkageMetadata.FeatureLinkageMetadata copy()
            throws CloneNotSupportedException
        {
            FeatureColumnLinkageMetadata.FeatureLinkageMetadata __copy = ((FeatureColumnLinkageMetadata.FeatureLinkageMetadata) super.copy());
            __copy._frameFeatureMetadataMember = null;
            __copy.__changeListener = new FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final FeatureColumnLinkageMetadata.FeatureLinkageMetadata __objectRef;

            private ChangeListener(FeatureColumnLinkageMetadata.FeatureLinkageMetadata reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.fds.FrameFeatureMetadata":
                        __objectRef._frameFeatureMetadataMember = null;
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

            public com.linkedin.feathr.fds.FrameFeatureMetadata.Fields FrameFeatureMetadata() {
                return new com.linkedin.feathr.fds.FrameFeatureMetadata.Fields(getPathComponents(), "com.linkedin.feathr.fds.FrameFeatureMetadata");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.fds.FrameFeatureMetadata.ProjectionMask _FrameFeatureMetadataMask;

            ProjectionMask() {
                super(2);
            }

            public FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ProjectionMask withFrameFeatureMetadata(Function<com.linkedin.feathr.fds.FrameFeatureMetadata.ProjectionMask, com.linkedin.feathr.fds.FrameFeatureMetadata.ProjectionMask> nestedMask) {
                _FrameFeatureMetadataMask = nestedMask.apply(((_FrameFeatureMetadataMask == null)?com.linkedin.feathr.fds.FrameFeatureMetadata.createMask():_FrameFeatureMetadataMask));
                getDataMap().put("com.linkedin.feathr.fds.FrameFeatureMetadata", _FrameFeatureMetadataMask.getDataMap());
                return this;
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
         *  See above *
         * 
         */
        public com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.FeatureLinkageMetadata.Fields featureLinkageMetadata() {
            return new com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.FeatureLinkageMetadata.Fields(getPathComponents(), "featureLinkageMetadata");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ProjectionMask _featureLinkageMetadataMask;

        ProjectionMask() {
            super(2);
        }

        /**
         *  See above *
         * 
         */
        public FeatureColumnLinkageMetadata.ProjectionMask withFeatureLinkageMetadata(Function<com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ProjectionMask, com.linkedin.feathr.fds.FeatureColumnLinkageMetadata.FeatureLinkageMetadata.ProjectionMask> nestedMask) {
            _featureLinkageMetadataMask = nestedMask.apply(((_featureLinkageMetadataMask == null)?FeatureColumnLinkageMetadata.FeatureLinkageMetadata.createMask():_featureLinkageMetadataMask));
            getDataMap().put("featureLinkageMetadata", _featureLinkageMetadataMask.getDataMap());
            return this;
        }

        /**
         *  See above *
         * 
         */
        public FeatureColumnLinkageMetadata.ProjectionMask withFeatureLinkageMetadata() {
            _featureLinkageMetadataMask = null;
            getDataMap().put("featureLinkageMetadata", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
