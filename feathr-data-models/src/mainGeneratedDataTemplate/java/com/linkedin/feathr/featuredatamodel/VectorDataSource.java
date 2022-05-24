
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
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


/**
 * Represent a Vector data source. Vector is a media asset serving service accessed via Restli unstructured data endpoint.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/VectorDataSource.pdl.")
public class VectorDataSource
    extends RecordTemplate
{

    private final static VectorDataSource.Fields _fields = new VectorDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represent a Vector data source. Vector is a media asset serving service accessed via Restli unstructured data endpoint.*/record VectorDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**This is a Vector request param needed to uniquely identify the Vector asset which a user is trying to fetch.\nFor example \"png_200_200\". This will be decided by the user at write time when the user writes an asset to Vector.\nThe type is string and not enum. The values will be decided between the team that will use the media data and Vector.*/featureSourceName:string}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private String _featureSourceNameField = null;
    private VectorDataSource.ChangeListener __changeListener = new VectorDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_FeatureSourceName = SCHEMA.getField("featureSourceName");

    public VectorDataSource() {
        super(new DataMap(4, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public VectorDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static VectorDataSource.Fields fields() {
        return _fields;
    }

    public static VectorDataSource.ProjectionMask createMask() {
        return new VectorDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see VectorDataSource.Fields#keyFunction
     */
    public boolean hasKeyFunction() {
        if (_keyFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("keyFunction");
    }

    /**
     * Remover for keyFunction
     * 
     * @see VectorDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see VectorDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see VectorDataSource.Fields#keyFunction
     */
    @Nullable
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction() {
        if (_keyFunctionField!= null) {
            return _keyFunctionField;
        } else {
            Object __rawValue = super._map.get("keyFunction");
            _keyFunctionField = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction(__rawValue));
            return _keyFunctionField;
        }
    }

    /**
     * Setter for keyFunction
     * 
     * @see VectorDataSource.Fields#keyFunction
     */
    public VectorDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
                    _keyFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
                    _keyFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see VectorDataSource.Fields#keyFunction
     */
    public VectorDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.VectorDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see VectorDataSource.Fields#dataSourceRef
     */
    public boolean hasDataSourceRef() {
        if (_dataSourceRefField!= null) {
            return true;
        }
        return super._map.containsKey("dataSourceRef");
    }

    /**
     * Remover for dataSourceRef
     * 
     * @see VectorDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see VectorDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see VectorDataSource.Fields#dataSourceRef
     */
    @Nullable
    public String getDataSourceRef() {
        if (_dataSourceRefField!= null) {
            return _dataSourceRefField;
        } else {
            Object __rawValue = super._map.get("dataSourceRef");
            _dataSourceRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _dataSourceRefField;
        }
    }

    /**
     * Setter for dataSourceRef
     * 
     * @see VectorDataSource.Fields#dataSourceRef
     */
    public VectorDataSource setDataSourceRef(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDataSourceRef(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDataSourceRef();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
                    _dataSourceRefField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
                    _dataSourceRefField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dataSourceRef
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see VectorDataSource.Fields#dataSourceRef
     */
    public VectorDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.VectorDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureSourceName
     * 
     * @see VectorDataSource.Fields#featureSourceName
     */
    public boolean hasFeatureSourceName() {
        if (_featureSourceNameField!= null) {
            return true;
        }
        return super._map.containsKey("featureSourceName");
    }

    /**
     * Remover for featureSourceName
     * 
     * @see VectorDataSource.Fields#featureSourceName
     */
    public void removeFeatureSourceName() {
        super._map.remove("featureSourceName");
    }

    /**
     * Getter for featureSourceName
     * 
     * @see VectorDataSource.Fields#featureSourceName
     */
    public String getFeatureSourceName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFeatureSourceName();
            case DEFAULT:
            case NULL:
                if (_featureSourceNameField!= null) {
                    return _featureSourceNameField;
                } else {
                    Object __rawValue = super._map.get("featureSourceName");
                    _featureSourceNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _featureSourceNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for featureSourceName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see VectorDataSource.Fields#featureSourceName
     */
    @Nonnull
    public String getFeatureSourceName() {
        if (_featureSourceNameField!= null) {
            return _featureSourceNameField;
        } else {
            Object __rawValue = super._map.get("featureSourceName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("featureSourceName");
            }
            _featureSourceNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _featureSourceNameField;
        }
    }

    /**
     * Setter for featureSourceName
     * 
     * @see VectorDataSource.Fields#featureSourceName
     */
    public VectorDataSource setFeatureSourceName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureSourceName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field featureSourceName of com.linkedin.feathr.featureDataModel.VectorDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureSourceName", value);
                    _featureSourceNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureSourceName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureSourceName", value);
                    _featureSourceNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureSourceName", value);
                    _featureSourceNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureSourceName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see VectorDataSource.Fields#featureSourceName
     */
    public VectorDataSource setFeatureSourceName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureSourceName of com.linkedin.feathr.featureDataModel.VectorDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureSourceName", value);
            _featureSourceNameField = value;
        }
        return this;
    }

    @Override
    public VectorDataSource clone()
        throws CloneNotSupportedException
    {
        VectorDataSource __clone = ((VectorDataSource) super.clone());
        __clone.__changeListener = new VectorDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public VectorDataSource copy()
        throws CloneNotSupportedException
    {
        VectorDataSource __copy = ((VectorDataSource) super.copy());
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy._featureSourceNameField = null;
        __copy.__changeListener = new VectorDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final VectorDataSource __objectRef;

        private ChangeListener(VectorDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
                    break;
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
                    break;
                case "featureSourceName":
                    __objectRef._featureSourceNameField = null;
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
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.Fields keyFunction() {
            return new com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.Fields(getPathComponents(), "keyFunction");
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public PathSpec dataSourceRef() {
            return new PathSpec(getPathComponents(), "dataSourceRef");
        }

        /**
         * This is a Vector request param needed to uniquely identify the Vector asset which a user is trying to fetch.
         * For example "png_200_200". This will be decided by the user at write time when the user writes an asset to Vector.
         * The type is string and not enum. The values will be decided between the team that will use the media data and Vector.
         * 
         */
        public PathSpec featureSourceName() {
            return new PathSpec(getPathComponents(), "featureSourceName");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public VectorDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public VectorDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public VectorDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * This is a Vector request param needed to uniquely identify the Vector asset which a user is trying to fetch.
         * For example "png_200_200". This will be decided by the user at write time when the user writes an asset to Vector.
         * The type is string and not enum. The values will be decided between the team that will use the media data and Vector.
         * 
         */
        public VectorDataSource.ProjectionMask withFeatureSourceName() {
            getDataMap().put("featureSourceName", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
