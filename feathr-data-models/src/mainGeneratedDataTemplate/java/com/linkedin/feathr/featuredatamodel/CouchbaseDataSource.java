
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import com.linkedin.frame.common.UriArray;


/**
 * Represents a Couchbase data source. Couchbase is a distributed key-value caching solution. For more details: go/couchbase.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/CouchbaseDataSource.pdl.")
public class CouchbaseDataSource
    extends RecordTemplate
{

    private final static CouchbaseDataSource.Fields _fields = new CouchbaseDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a Couchbase data source. Couchbase is a distributed key-value caching solution. For more details: go/couchbase.*/record CouchbaseDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Name of the Couchbase bucket. A bucket is a logical entity that groups documents; allowing them to be accessed, indexed, replicated, and access-controlled.*/bucketName:string/**The list of bootstrap servers for Frame to initiate communication to the Couchbase cluster.\nThis is no longer needed as Couchbase can resolve the URIs based on bucketName field.*/@deprecated=\"No longer needed as Couchbase can now resolve the URIs based on the bucketName field.\"bootstrapServers:array[{namespace com.linkedin.frame.common@java.class=\"java.net.URI\"typeref Uri=string}]=[]/**Fully qualified Java class name for data model of the documents stored in the Couchbase bucket.*/documentDataModel:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private String _bucketNameField = null;
    private UriArray _bootstrapServersField = null;
    private Clazz _documentDataModelField = null;
    private CouchbaseDataSource.ChangeListener __changeListener = new CouchbaseDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_BucketName = SCHEMA.getField("bucketName");
    private final static RecordDataSchema.Field FIELD_BootstrapServers = SCHEMA.getField("bootstrapServers");
    private final static UriArray DEFAULT_BootstrapServers;
    private final static RecordDataSchema.Field FIELD_DocumentDataModel = SCHEMA.getField("documentDataModel");

    static {
        DEFAULT_BootstrapServers = ((FIELD_BootstrapServers.getDefault() == null)?null:new UriArray(DataTemplateUtil.castOrThrow(FIELD_BootstrapServers.getDefault(), DataList.class)));
    }

    public CouchbaseDataSource() {
        super(new DataMap(7, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public CouchbaseDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static CouchbaseDataSource.Fields fields() {
        return _fields;
    }

    public static CouchbaseDataSource.ProjectionMask createMask() {
        return new CouchbaseDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see CouchbaseDataSource.Fields#keyFunction
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
     * @see CouchbaseDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see CouchbaseDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see CouchbaseDataSource.Fields#keyFunction
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
     * @see CouchbaseDataSource.Fields#keyFunction
     */
    public CouchbaseDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see CouchbaseDataSource.Fields#keyFunction
     */
    public CouchbaseDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.CouchbaseDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see CouchbaseDataSource.Fields#dataSourceRef
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
     * @see CouchbaseDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see CouchbaseDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see CouchbaseDataSource.Fields#dataSourceRef
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
     * @see CouchbaseDataSource.Fields#dataSourceRef
     */
    public CouchbaseDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see CouchbaseDataSource.Fields#dataSourceRef
     */
    public CouchbaseDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.CouchbaseDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for bucketName
     * 
     * @see CouchbaseDataSource.Fields#bucketName
     */
    public boolean hasBucketName() {
        if (_bucketNameField!= null) {
            return true;
        }
        return super._map.containsKey("bucketName");
    }

    /**
     * Remover for bucketName
     * 
     * @see CouchbaseDataSource.Fields#bucketName
     */
    public void removeBucketName() {
        super._map.remove("bucketName");
    }

    /**
     * Getter for bucketName
     * 
     * @see CouchbaseDataSource.Fields#bucketName
     */
    public String getBucketName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getBucketName();
            case DEFAULT:
            case NULL:
                if (_bucketNameField!= null) {
                    return _bucketNameField;
                } else {
                    Object __rawValue = super._map.get("bucketName");
                    _bucketNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _bucketNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for bucketName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see CouchbaseDataSource.Fields#bucketName
     */
    @Nonnull
    public String getBucketName() {
        if (_bucketNameField!= null) {
            return _bucketNameField;
        } else {
            Object __rawValue = super._map.get("bucketName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("bucketName");
            }
            _bucketNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _bucketNameField;
        }
    }

    /**
     * Setter for bucketName
     * 
     * @see CouchbaseDataSource.Fields#bucketName
     */
    public CouchbaseDataSource setBucketName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setBucketName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field bucketName of com.linkedin.feathr.featureDataModel.CouchbaseDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "bucketName", value);
                    _bucketNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeBucketName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "bucketName", value);
                    _bucketNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "bucketName", value);
                    _bucketNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for bucketName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see CouchbaseDataSource.Fields#bucketName
     */
    public CouchbaseDataSource setBucketName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field bucketName of com.linkedin.feathr.featureDataModel.CouchbaseDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "bucketName", value);
            _bucketNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for bootstrapServers
     * 
     * @deprecated
     *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
     * @see CouchbaseDataSource.Fields#bootstrapServers
     */
    @Deprecated
    public boolean hasBootstrapServers() {
        if (_bootstrapServersField!= null) {
            return true;
        }
        return super._map.containsKey("bootstrapServers");
    }

    /**
     * Remover for bootstrapServers
     * 
     * @deprecated
     *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
     * @see CouchbaseDataSource.Fields#bootstrapServers
     */
    @Deprecated
    public void removeBootstrapServers() {
        super._map.remove("bootstrapServers");
    }

    /**
     * Getter for bootstrapServers
     * 
     * @deprecated
     *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
     * @see CouchbaseDataSource.Fields#bootstrapServers
     */
    @Deprecated
    public UriArray getBootstrapServers(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getBootstrapServers();
            case NULL:
                if (_bootstrapServersField!= null) {
                    return _bootstrapServersField;
                } else {
                    Object __rawValue = super._map.get("bootstrapServers");
                    _bootstrapServersField = ((__rawValue == null)?null:new UriArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _bootstrapServersField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for bootstrapServers
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @deprecated
     *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
     * @see CouchbaseDataSource.Fields#bootstrapServers
     */
    @Deprecated
    @Nonnull
    public UriArray getBootstrapServers() {
        if (_bootstrapServersField!= null) {
            return _bootstrapServersField;
        } else {
            Object __rawValue = super._map.get("bootstrapServers");
            if (__rawValue == null) {
                return DEFAULT_BootstrapServers;
            }
            _bootstrapServersField = ((__rawValue == null)?null:new UriArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _bootstrapServersField;
        }
    }

    /**
     * Setter for bootstrapServers
     * 
     * @deprecated
     *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
     * @see CouchbaseDataSource.Fields#bootstrapServers
     */
    @Deprecated
    public CouchbaseDataSource setBootstrapServers(UriArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setBootstrapServers(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field bootstrapServers of com.linkedin.feathr.featureDataModel.CouchbaseDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "bootstrapServers", value.data());
                    _bootstrapServersField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeBootstrapServers();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "bootstrapServers", value.data());
                    _bootstrapServersField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "bootstrapServers", value.data());
                    _bootstrapServersField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for bootstrapServers
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @deprecated
     *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
     * @see CouchbaseDataSource.Fields#bootstrapServers
     */
    @Deprecated
    public CouchbaseDataSource setBootstrapServers(
        @Nonnull
        UriArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field bootstrapServers of com.linkedin.feathr.featureDataModel.CouchbaseDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "bootstrapServers", value.data());
            _bootstrapServersField = value;
        }
        return this;
    }

    /**
     * Existence checker for documentDataModel
     * 
     * @see CouchbaseDataSource.Fields#documentDataModel
     */
    public boolean hasDocumentDataModel() {
        if (_documentDataModelField!= null) {
            return true;
        }
        return super._map.containsKey("documentDataModel");
    }

    /**
     * Remover for documentDataModel
     * 
     * @see CouchbaseDataSource.Fields#documentDataModel
     */
    public void removeDocumentDataModel() {
        super._map.remove("documentDataModel");
    }

    /**
     * Getter for documentDataModel
     * 
     * @see CouchbaseDataSource.Fields#documentDataModel
     */
    public Clazz getDocumentDataModel(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDocumentDataModel();
            case DEFAULT:
            case NULL:
                if (_documentDataModelField!= null) {
                    return _documentDataModelField;
                } else {
                    Object __rawValue = super._map.get("documentDataModel");
                    _documentDataModelField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _documentDataModelField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for documentDataModel
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see CouchbaseDataSource.Fields#documentDataModel
     */
    @Nonnull
    public Clazz getDocumentDataModel() {
        if (_documentDataModelField!= null) {
            return _documentDataModelField;
        } else {
            Object __rawValue = super._map.get("documentDataModel");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("documentDataModel");
            }
            _documentDataModelField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _documentDataModelField;
        }
    }

    /**
     * Setter for documentDataModel
     * 
     * @see CouchbaseDataSource.Fields#documentDataModel
     */
    public CouchbaseDataSource setDocumentDataModel(Clazz value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDocumentDataModel(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field documentDataModel of com.linkedin.feathr.featureDataModel.CouchbaseDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "documentDataModel", value.data());
                    _documentDataModelField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDocumentDataModel();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "documentDataModel", value.data());
                    _documentDataModelField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "documentDataModel", value.data());
                    _documentDataModelField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for documentDataModel
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see CouchbaseDataSource.Fields#documentDataModel
     */
    public CouchbaseDataSource setDocumentDataModel(
        @Nonnull
        Clazz value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field documentDataModel of com.linkedin.feathr.featureDataModel.CouchbaseDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "documentDataModel", value.data());
            _documentDataModelField = value;
        }
        return this;
    }

    @Override
    public CouchbaseDataSource clone()
        throws CloneNotSupportedException
    {
        CouchbaseDataSource __clone = ((CouchbaseDataSource) super.clone());
        __clone.__changeListener = new CouchbaseDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public CouchbaseDataSource copy()
        throws CloneNotSupportedException
    {
        CouchbaseDataSource __copy = ((CouchbaseDataSource) super.copy());
        __copy._bucketNameField = null;
        __copy._documentDataModelField = null;
        __copy._keyFunctionField = null;
        __copy._bootstrapServersField = null;
        __copy._dataSourceRefField = null;
        __copy.__changeListener = new CouchbaseDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final CouchbaseDataSource __objectRef;

        private ChangeListener(CouchbaseDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "bucketName":
                    __objectRef._bucketNameField = null;
                    break;
                case "documentDataModel":
                    __objectRef._documentDataModelField = null;
                    break;
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
                    break;
                case "bootstrapServers":
                    __objectRef._bootstrapServersField = null;
                    break;
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
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
         * Name of the Couchbase bucket. A bucket is a logical entity that groups documents; allowing them to be accessed, indexed, replicated, and access-controlled.
         * 
         */
        public PathSpec bucketName() {
            return new PathSpec(getPathComponents(), "bucketName");
        }

        /**
         * The list of bootstrap servers for Frame to initiate communication to the Couchbase cluster.
         * This is no longer needed as Couchbase can resolve the URIs based on bucketName field.
         * 
         * @deprecated
         *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
         */
        @Deprecated
        public PathSpec bootstrapServers() {
            return new PathSpec(getPathComponents(), "bootstrapServers");
        }

        /**
         * The list of bootstrap servers for Frame to initiate communication to the Couchbase cluster.
         * This is no longer needed as Couchbase can resolve the URIs based on bucketName field.
         * 
         * @deprecated
         *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
         */
        @Deprecated
        public PathSpec bootstrapServers(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "bootstrapServers");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Fully qualified Java class name for data model of the documents stored in the Couchbase bucket.
         * 
         */
        public com.linkedin.feathr.featureDataModel.Clazz.Fields documentDataModel() {
            return new com.linkedin.feathr.featureDataModel.Clazz.Fields(getPathComponents(), "documentDataModel");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;
        private com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask _documentDataModelMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public CouchbaseDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public CouchbaseDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public CouchbaseDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Name of the Couchbase bucket. A bucket is a logical entity that groups documents; allowing them to be accessed, indexed, replicated, and access-controlled.
         * 
         */
        public CouchbaseDataSource.ProjectionMask withBucketName() {
            getDataMap().put("bucketName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The list of bootstrap servers for Frame to initiate communication to the Couchbase cluster.
         * This is no longer needed as Couchbase can resolve the URIs based on bucketName field.
         * 
         * @deprecated
         *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
         */
        @Deprecated
        public CouchbaseDataSource.ProjectionMask withBootstrapServers() {
            getDataMap().put("bootstrapServers", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The list of bootstrap servers for Frame to initiate communication to the Couchbase cluster.
         * This is no longer needed as Couchbase can resolve the URIs based on bucketName field.
         * 
         * @deprecated
         *     No longer needed as Couchbase can now resolve the URIs based on the bucketName field.
         */
        @Deprecated
        public CouchbaseDataSource.ProjectionMask withBootstrapServers(Integer start, Integer count) {
            getDataMap().put("bootstrapServers", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("bootstrapServers").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("bootstrapServers").put("$count", count);
            }
            return this;
        }

        /**
         * Fully qualified Java class name for data model of the documents stored in the Couchbase bucket.
         * 
         */
        public CouchbaseDataSource.ProjectionMask withDocumentDataModel(Function<com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask, com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask> nestedMask) {
            _documentDataModelMask = nestedMask.apply(((_documentDataModelMask == null)?Clazz.createMask():_documentDataModelMask));
            getDataMap().put("documentDataModel", _documentDataModelMask.getDataMap());
            return this;
        }

        /**
         * Fully qualified Java class name for data model of the documents stored in the Couchbase bucket.
         * 
         */
        public CouchbaseDataSource.ProjectionMask withDocumentDataModel() {
            _documentDataModelMask = null;
            getDataMap().put("documentDataModel", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
