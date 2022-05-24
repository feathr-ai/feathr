
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
 * Represents a Venice data source. Venice is a data store for derived data. All derived data accessed by primary key, whether produced offline on Hadoop or via nearline stream processing in Samza, can be served by Venice. For more details: go/venice.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/VeniceDataSource.pdl.")
public class VeniceDataSource
    extends RecordTemplate
{

    private final static VeniceDataSource.Fields _fields = new VeniceDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a Venice data source. Venice is a data store for derived data. All derived data accessed by primary key, whether produced offline on Hadoop or via nearline stream processing in Samza, can be served by Venice. For more details: go/venice.*/record VeniceDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**The Venice store name.*/storeName:string}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private String _storeNameField = null;
    private VeniceDataSource.ChangeListener __changeListener = new VeniceDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_StoreName = SCHEMA.getField("storeName");

    public VeniceDataSource() {
        super(new DataMap(4, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public VeniceDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static VeniceDataSource.Fields fields() {
        return _fields;
    }

    public static VeniceDataSource.ProjectionMask createMask() {
        return new VeniceDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see VeniceDataSource.Fields#keyFunction
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
     * @see VeniceDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see VeniceDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see VeniceDataSource.Fields#keyFunction
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
     * @see VeniceDataSource.Fields#keyFunction
     */
    public VeniceDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see VeniceDataSource.Fields#keyFunction
     */
    public VeniceDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.VeniceDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see VeniceDataSource.Fields#dataSourceRef
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
     * @see VeniceDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see VeniceDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see VeniceDataSource.Fields#dataSourceRef
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
     * @see VeniceDataSource.Fields#dataSourceRef
     */
    public VeniceDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see VeniceDataSource.Fields#dataSourceRef
     */
    public VeniceDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.VeniceDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for storeName
     * 
     * @see VeniceDataSource.Fields#storeName
     */
    public boolean hasStoreName() {
        if (_storeNameField!= null) {
            return true;
        }
        return super._map.containsKey("storeName");
    }

    /**
     * Remover for storeName
     * 
     * @see VeniceDataSource.Fields#storeName
     */
    public void removeStoreName() {
        super._map.remove("storeName");
    }

    /**
     * Getter for storeName
     * 
     * @see VeniceDataSource.Fields#storeName
     */
    public String getStoreName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getStoreName();
            case DEFAULT:
            case NULL:
                if (_storeNameField!= null) {
                    return _storeNameField;
                } else {
                    Object __rawValue = super._map.get("storeName");
                    _storeNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _storeNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for storeName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see VeniceDataSource.Fields#storeName
     */
    @Nonnull
    public String getStoreName() {
        if (_storeNameField!= null) {
            return _storeNameField;
        } else {
            Object __rawValue = super._map.get("storeName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("storeName");
            }
            _storeNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _storeNameField;
        }
    }

    /**
     * Setter for storeName
     * 
     * @see VeniceDataSource.Fields#storeName
     */
    public VeniceDataSource setStoreName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setStoreName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field storeName of com.linkedin.feathr.featureDataModel.VeniceDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "storeName", value);
                    _storeNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeStoreName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "storeName", value);
                    _storeNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "storeName", value);
                    _storeNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for storeName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see VeniceDataSource.Fields#storeName
     */
    public VeniceDataSource setStoreName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field storeName of com.linkedin.feathr.featureDataModel.VeniceDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "storeName", value);
            _storeNameField = value;
        }
        return this;
    }

    @Override
    public VeniceDataSource clone()
        throws CloneNotSupportedException
    {
        VeniceDataSource __clone = ((VeniceDataSource) super.clone());
        __clone.__changeListener = new VeniceDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public VeniceDataSource copy()
        throws CloneNotSupportedException
    {
        VeniceDataSource __copy = ((VeniceDataSource) super.copy());
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy._storeNameField = null;
        __copy.__changeListener = new VeniceDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final VeniceDataSource __objectRef;

        private ChangeListener(VeniceDataSource reference) {
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
                case "storeName":
                    __objectRef._storeNameField = null;
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
         * The Venice store name.
         * 
         */
        public PathSpec storeName() {
            return new PathSpec(getPathComponents(), "storeName");
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
        public VeniceDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public VeniceDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public VeniceDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The Venice store name.
         * 
         */
        public VeniceDataSource.ProjectionMask withStoreName() {
            getDataMap().put("storeName", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
