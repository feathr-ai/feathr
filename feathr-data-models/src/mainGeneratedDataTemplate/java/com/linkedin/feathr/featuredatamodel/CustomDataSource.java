
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
 * Represents a custom data source which uses user-defined data fetching mechanism. Its difference with InMemoryPassthroughDataSource is: CustomDataSource allows dependency injection via Offspring for supporting complex data fetching, whereas InMemoryPassthroughDataSource assumes data is already available in-memory along with the retrieval request. See CustomSource section in go/frameonline.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/CustomDataSource.pdl.")
public class CustomDataSource
    extends RecordTemplate
{

    private final static CustomDataSource.Fields _fields = new CustomDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a custom data source which uses user-defined data fetching mechanism. Its difference with InMemoryPassthroughDataSource is: CustomDataSource allows dependency injection via Offspring for supporting complex data fetching, whereas InMemoryPassthroughDataSource assumes data is already available in-memory along with the retrieval request. See CustomSource section in go/frameonline.*/record CustomDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**The fully qualified Java class name of the response data model retrieved from the custom data source.*/dataModel:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private Clazz _dataModelField = null;
    private CustomDataSource.ChangeListener __changeListener = new CustomDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_DataModel = SCHEMA.getField("dataModel");

    public CustomDataSource() {
        super(new DataMap(4, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public CustomDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static CustomDataSource.Fields fields() {
        return _fields;
    }

    public static CustomDataSource.ProjectionMask createMask() {
        return new CustomDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see CustomDataSource.Fields#keyFunction
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
     * @see CustomDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see CustomDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see CustomDataSource.Fields#keyFunction
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
     * @see CustomDataSource.Fields#keyFunction
     */
    public CustomDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see CustomDataSource.Fields#keyFunction
     */
    public CustomDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.CustomDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see CustomDataSource.Fields#dataSourceRef
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
     * @see CustomDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see CustomDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see CustomDataSource.Fields#dataSourceRef
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
     * @see CustomDataSource.Fields#dataSourceRef
     */
    public CustomDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see CustomDataSource.Fields#dataSourceRef
     */
    public CustomDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.CustomDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataModel
     * 
     * @see CustomDataSource.Fields#dataModel
     */
    public boolean hasDataModel() {
        if (_dataModelField!= null) {
            return true;
        }
        return super._map.containsKey("dataModel");
    }

    /**
     * Remover for dataModel
     * 
     * @see CustomDataSource.Fields#dataModel
     */
    public void removeDataModel() {
        super._map.remove("dataModel");
    }

    /**
     * Getter for dataModel
     * 
     * @see CustomDataSource.Fields#dataModel
     */
    public Clazz getDataModel(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDataModel();
            case DEFAULT:
            case NULL:
                if (_dataModelField!= null) {
                    return _dataModelField;
                } else {
                    Object __rawValue = super._map.get("dataModel");
                    _dataModelField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _dataModelField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for dataModel
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see CustomDataSource.Fields#dataModel
     */
    @Nonnull
    public Clazz getDataModel() {
        if (_dataModelField!= null) {
            return _dataModelField;
        } else {
            Object __rawValue = super._map.get("dataModel");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("dataModel");
            }
            _dataModelField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _dataModelField;
        }
    }

    /**
     * Setter for dataModel
     * 
     * @see CustomDataSource.Fields#dataModel
     */
    public CustomDataSource setDataModel(Clazz value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDataModel(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field dataModel of com.linkedin.feathr.featureDataModel.CustomDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
                    _dataModelField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDataModel();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
                    _dataModelField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
                    _dataModelField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dataModel
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see CustomDataSource.Fields#dataModel
     */
    public CustomDataSource setDataModel(
        @Nonnull
        Clazz value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataModel of com.linkedin.feathr.featureDataModel.CustomDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
            _dataModelField = value;
        }
        return this;
    }

    @Override
    public CustomDataSource clone()
        throws CloneNotSupportedException
    {
        CustomDataSource __clone = ((CustomDataSource) super.clone());
        __clone.__changeListener = new CustomDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public CustomDataSource copy()
        throws CloneNotSupportedException
    {
        CustomDataSource __copy = ((CustomDataSource) super.copy());
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy._dataModelField = null;
        __copy.__changeListener = new CustomDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final CustomDataSource __objectRef;

        private ChangeListener(CustomDataSource reference) {
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
                case "dataModel":
                    __objectRef._dataModelField = null;
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
         * The fully qualified Java class name of the response data model retrieved from the custom data source.
         * 
         */
        public com.linkedin.feathr.featureDataModel.Clazz.Fields dataModel() {
            return new com.linkedin.feathr.featureDataModel.Clazz.Fields(getPathComponents(), "dataModel");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;
        private com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask _dataModelMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public CustomDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public CustomDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public CustomDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The fully qualified Java class name of the response data model retrieved from the custom data source.
         * 
         */
        public CustomDataSource.ProjectionMask withDataModel(Function<com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask, com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask> nestedMask) {
            _dataModelMask = nestedMask.apply(((_dataModelMask == null)?Clazz.createMask():_dataModelMask));
            getDataMap().put("dataModel", _dataModelMask.getDataMap());
            return this;
        }

        /**
         * The fully qualified Java class name of the response data model retrieved from the custom data source.
         * 
         */
        public CustomDataSource.ProjectionMask withDataModel() {
            _dataModelMask = null;
            getDataMap().put("dataModel", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
