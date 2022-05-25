
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
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;
import com.linkedin.frame.common.coercer.UriCoercer;


/**
 * Represents a Espresso data source. Espresso the recommended source-of-truth distributed database at LinkedIn. It is optimized for balanced read/write traffic with relative small sized data records (< 1MB). For more details: go/espresso.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/EspressoDataSource.pdl.")
public class EspressoDataSource
    extends RecordTemplate
{

    private final static EspressoDataSource.Fields _fields = new EspressoDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a Espresso data source. Espresso the recommended source-of-truth distributed database at LinkedIn. It is optimized for balanced read/write traffic with relative small sized data records (< 1MB). For more details: go/espresso.*/record EspressoDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Espresso database name.*/databaseName:string/**Espresso table name.*/tableName:string/**D2 URI of the Espresso database which can be found on go/Nuage.*/d2Uri:{namespace com.linkedin.frame.common@java={\"class\":\"java.net.URI\",\"coercerClass\":\"com.linkedin.frame.common.coercer.UriCoercer\"}typeref Uri=string}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private String _databaseNameField = null;
    private String _tableNameField = null;
    private java.net.URI _d2UriField = null;
    private EspressoDataSource.ChangeListener __changeListener = new EspressoDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_DatabaseName = SCHEMA.getField("databaseName");
    private final static RecordDataSchema.Field FIELD_TableName = SCHEMA.getField("tableName");
    private final static RecordDataSchema.Field FIELD_D2Uri = SCHEMA.getField("d2Uri");

    static {
        Custom.initializeCustomClass(java.net.URI.class);
        Custom.initializeCoercerClass(UriCoercer.class);
    }

    public EspressoDataSource() {
        super(new DataMap(7, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public EspressoDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static EspressoDataSource.Fields fields() {
        return _fields;
    }

    public static EspressoDataSource.ProjectionMask createMask() {
        return new EspressoDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see EspressoDataSource.Fields#keyFunction
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
     * @see EspressoDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see EspressoDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see EspressoDataSource.Fields#keyFunction
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
     * @see EspressoDataSource.Fields#keyFunction
     */
    public EspressoDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see EspressoDataSource.Fields#keyFunction
     */
    public EspressoDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.EspressoDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see EspressoDataSource.Fields#dataSourceRef
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
     * @see EspressoDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see EspressoDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see EspressoDataSource.Fields#dataSourceRef
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
     * @see EspressoDataSource.Fields#dataSourceRef
     */
    public EspressoDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see EspressoDataSource.Fields#dataSourceRef
     */
    public EspressoDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.EspressoDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for databaseName
     * 
     * @see EspressoDataSource.Fields#databaseName
     */
    public boolean hasDatabaseName() {
        if (_databaseNameField!= null) {
            return true;
        }
        return super._map.containsKey("databaseName");
    }

    /**
     * Remover for databaseName
     * 
     * @see EspressoDataSource.Fields#databaseName
     */
    public void removeDatabaseName() {
        super._map.remove("databaseName");
    }

    /**
     * Getter for databaseName
     * 
     * @see EspressoDataSource.Fields#databaseName
     */
    public String getDatabaseName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDatabaseName();
            case DEFAULT:
            case NULL:
                if (_databaseNameField!= null) {
                    return _databaseNameField;
                } else {
                    Object __rawValue = super._map.get("databaseName");
                    _databaseNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _databaseNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for databaseName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see EspressoDataSource.Fields#databaseName
     */
    @Nonnull
    public String getDatabaseName() {
        if (_databaseNameField!= null) {
            return _databaseNameField;
        } else {
            Object __rawValue = super._map.get("databaseName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("databaseName");
            }
            _databaseNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _databaseNameField;
        }
    }

    /**
     * Setter for databaseName
     * 
     * @see EspressoDataSource.Fields#databaseName
     */
    public EspressoDataSource setDatabaseName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDatabaseName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field databaseName of com.linkedin.feathr.featureDataModel.EspressoDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "databaseName", value);
                    _databaseNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDatabaseName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "databaseName", value);
                    _databaseNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "databaseName", value);
                    _databaseNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for databaseName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see EspressoDataSource.Fields#databaseName
     */
    public EspressoDataSource setDatabaseName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field databaseName of com.linkedin.feathr.featureDataModel.EspressoDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "databaseName", value);
            _databaseNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for tableName
     * 
     * @see EspressoDataSource.Fields#tableName
     */
    public boolean hasTableName() {
        if (_tableNameField!= null) {
            return true;
        }
        return super._map.containsKey("tableName");
    }

    /**
     * Remover for tableName
     * 
     * @see EspressoDataSource.Fields#tableName
     */
    public void removeTableName() {
        super._map.remove("tableName");
    }

    /**
     * Getter for tableName
     * 
     * @see EspressoDataSource.Fields#tableName
     */
    public String getTableName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTableName();
            case DEFAULT:
            case NULL:
                if (_tableNameField!= null) {
                    return _tableNameField;
                } else {
                    Object __rawValue = super._map.get("tableName");
                    _tableNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _tableNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for tableName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see EspressoDataSource.Fields#tableName
     */
    @Nonnull
    public String getTableName() {
        if (_tableNameField!= null) {
            return _tableNameField;
        } else {
            Object __rawValue = super._map.get("tableName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("tableName");
            }
            _tableNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _tableNameField;
        }
    }

    /**
     * Setter for tableName
     * 
     * @see EspressoDataSource.Fields#tableName
     */
    public EspressoDataSource setTableName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTableName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field tableName of com.linkedin.feathr.featureDataModel.EspressoDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tableName", value);
                    _tableNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTableName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "tableName", value);
                    _tableNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "tableName", value);
                    _tableNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for tableName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see EspressoDataSource.Fields#tableName
     */
    public EspressoDataSource setTableName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field tableName of com.linkedin.feathr.featureDataModel.EspressoDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "tableName", value);
            _tableNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for d2Uri
     * 
     * @see EspressoDataSource.Fields#d2Uri
     */
    public boolean hasD2Uri() {
        if (_d2UriField!= null) {
            return true;
        }
        return super._map.containsKey("d2Uri");
    }

    /**
     * Remover for d2Uri
     * 
     * @see EspressoDataSource.Fields#d2Uri
     */
    public void removeD2Uri() {
        super._map.remove("d2Uri");
    }

    /**
     * Getter for d2Uri
     * 
     * @see EspressoDataSource.Fields#d2Uri
     */
    public java.net.URI getD2Uri(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getD2Uri();
            case DEFAULT:
            case NULL:
                if (_d2UriField!= null) {
                    return _d2UriField;
                } else {
                    Object __rawValue = super._map.get("d2Uri");
                    _d2UriField = DataTemplateUtil.coerceCustomOutput(__rawValue, java.net.URI.class);
                    return _d2UriField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for d2Uri
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see EspressoDataSource.Fields#d2Uri
     */
    @Nonnull
    public java.net.URI getD2Uri() {
        if (_d2UriField!= null) {
            return _d2UriField;
        } else {
            Object __rawValue = super._map.get("d2Uri");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("d2Uri");
            }
            _d2UriField = DataTemplateUtil.coerceCustomOutput(__rawValue, java.net.URI.class);
            return _d2UriField;
        }
    }

    /**
     * Setter for d2Uri
     * 
     * @see EspressoDataSource.Fields#d2Uri
     */
    public EspressoDataSource setD2Uri(java.net.URI value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setD2Uri(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field d2Uri of com.linkedin.feathr.featureDataModel.EspressoDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "d2Uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
                    _d2UriField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeD2Uri();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "d2Uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
                    _d2UriField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "d2Uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
                    _d2UriField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for d2Uri
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see EspressoDataSource.Fields#d2Uri
     */
    public EspressoDataSource setD2Uri(
        @Nonnull
        java.net.URI value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field d2Uri of com.linkedin.feathr.featureDataModel.EspressoDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "d2Uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
            _d2UriField = value;
        }
        return this;
    }

    @Override
    public EspressoDataSource clone()
        throws CloneNotSupportedException
    {
        EspressoDataSource __clone = ((EspressoDataSource) super.clone());
        __clone.__changeListener = new EspressoDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public EspressoDataSource copy()
        throws CloneNotSupportedException
    {
        EspressoDataSource __copy = ((EspressoDataSource) super.copy());
        __copy._databaseNameField = null;
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy._d2UriField = null;
        __copy._tableNameField = null;
        __copy.__changeListener = new EspressoDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final EspressoDataSource __objectRef;

        private ChangeListener(EspressoDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "databaseName":
                    __objectRef._databaseNameField = null;
                    break;
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
                    break;
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
                    break;
                case "d2Uri":
                    __objectRef._d2UriField = null;
                    break;
                case "tableName":
                    __objectRef._tableNameField = null;
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
         * Espresso database name.
         * 
         */
        public PathSpec databaseName() {
            return new PathSpec(getPathComponents(), "databaseName");
        }

        /**
         * Espresso table name.
         * 
         */
        public PathSpec tableName() {
            return new PathSpec(getPathComponents(), "tableName");
        }

        /**
         * D2 URI of the Espresso database which can be found on go/Nuage.
         * 
         */
        public PathSpec d2Uri() {
            return new PathSpec(getPathComponents(), "d2Uri");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public EspressoDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public EspressoDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public EspressoDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Espresso database name.
         * 
         */
        public EspressoDataSource.ProjectionMask withDatabaseName() {
            getDataMap().put("databaseName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Espresso table name.
         * 
         */
        public EspressoDataSource.ProjectionMask withTableName() {
            getDataMap().put("tableName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * D2 URI of the Espresso database which can be found on go/Nuage.
         * 
         */
        public EspressoDataSource.ProjectionMask withD2Uri() {
            getDataMap().put("d2Uri", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
