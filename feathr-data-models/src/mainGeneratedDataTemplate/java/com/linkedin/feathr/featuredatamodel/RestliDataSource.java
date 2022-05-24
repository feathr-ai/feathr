
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
import com.linkedin.data.template.StringArray;


/**
 * Represents a Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource. Also see RestliFinderDataSource if you are looking for fetching source data by a finder method.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/RestliDataSource.pdl.")
public class RestliDataSource
    extends RecordTemplate
{

    private final static RestliDataSource.Fields _fields = new RestliDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource. Also see RestliFinderDataSource if you are looking for fetching source data by a finder method.*/record RestliDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.*/resourceName:string/**Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details. It should be required with a default value, but the optional flag can't be removed after its check-in, for complying with backward compatibility requirement of schema evolution.*/projections:optional array[/**Represents a PathSpec string. PathSpec is used to specify projections in Rest.li request in order to select a subset of object contents. For more details, refer to https://linkedin.github.io/rest.li/Projections. Some examples: 'listingType', 'member:(firstName, lastName)'.*/typeref PathSpec=string]=[]/**Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#methods,get, the key value pair keyed by viewer represents a request parameter of the Get method in jobPostings resource.*/requestParameters:map[string/**Represents the value part in a Rest.li request paramater key/value pair.*/typeref RequestParameterValue=union[MvelExpression/**Represents a Json string.*/typeref JsonString=string]]={}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private String _resourceNameField = null;
    private StringArray _projectionsField = null;
    private RequestParameterValueMap _requestParametersField = null;
    private RestliDataSource.ChangeListener __changeListener = new RestliDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_ResourceName = SCHEMA.getField("resourceName");
    private final static RecordDataSchema.Field FIELD_Projections = SCHEMA.getField("projections");
    private final static StringArray DEFAULT_Projections;
    private final static RecordDataSchema.Field FIELD_RequestParameters = SCHEMA.getField("requestParameters");
    private final static RequestParameterValueMap DEFAULT_RequestParameters;

    static {
        DEFAULT_Projections = ((FIELD_Projections.getDefault() == null)?null:new StringArray(DataTemplateUtil.castOrThrow(FIELD_Projections.getDefault(), DataList.class)));
        DEFAULT_RequestParameters = ((FIELD_RequestParameters.getDefault() == null)?null:new RequestParameterValueMap(DataTemplateUtil.castOrThrow(FIELD_RequestParameters.getDefault(), DataMap.class)));
    }

    public RestliDataSource() {
        super(new DataMap(7, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public RestliDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static RestliDataSource.Fields fields() {
        return _fields;
    }

    public static RestliDataSource.ProjectionMask createMask() {
        return new RestliDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see RestliDataSource.Fields#keyFunction
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
     * @see RestliDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see RestliDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see RestliDataSource.Fields#keyFunction
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
     * @see RestliDataSource.Fields#keyFunction
     */
    public RestliDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see RestliDataSource.Fields#keyFunction
     */
    public RestliDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.RestliDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see RestliDataSource.Fields#dataSourceRef
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
     * @see RestliDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see RestliDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see RestliDataSource.Fields#dataSourceRef
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
     * @see RestliDataSource.Fields#dataSourceRef
     */
    public RestliDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see RestliDataSource.Fields#dataSourceRef
     */
    public RestliDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.RestliDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for resourceName
     * 
     * @see RestliDataSource.Fields#resourceName
     */
    public boolean hasResourceName() {
        if (_resourceNameField!= null) {
            return true;
        }
        return super._map.containsKey("resourceName");
    }

    /**
     * Remover for resourceName
     * 
     * @see RestliDataSource.Fields#resourceName
     */
    public void removeResourceName() {
        super._map.remove("resourceName");
    }

    /**
     * Getter for resourceName
     * 
     * @see RestliDataSource.Fields#resourceName
     */
    public String getResourceName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getResourceName();
            case DEFAULT:
            case NULL:
                if (_resourceNameField!= null) {
                    return _resourceNameField;
                } else {
                    Object __rawValue = super._map.get("resourceName");
                    _resourceNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _resourceNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for resourceName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see RestliDataSource.Fields#resourceName
     */
    @Nonnull
    public String getResourceName() {
        if (_resourceNameField!= null) {
            return _resourceNameField;
        } else {
            Object __rawValue = super._map.get("resourceName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("resourceName");
            }
            _resourceNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _resourceNameField;
        }
    }

    /**
     * Setter for resourceName
     * 
     * @see RestliDataSource.Fields#resourceName
     */
    public RestliDataSource setResourceName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setResourceName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field resourceName of com.linkedin.feathr.featureDataModel.RestliDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "resourceName", value);
                    _resourceNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeResourceName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "resourceName", value);
                    _resourceNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "resourceName", value);
                    _resourceNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for resourceName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RestliDataSource.Fields#resourceName
     */
    public RestliDataSource setResourceName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field resourceName of com.linkedin.feathr.featureDataModel.RestliDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "resourceName", value);
            _resourceNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for projections
     * 
     * @see RestliDataSource.Fields#projections
     */
    public boolean hasProjections() {
        if (_projectionsField!= null) {
            return true;
        }
        return super._map.containsKey("projections");
    }

    /**
     * Remover for projections
     * 
     * @see RestliDataSource.Fields#projections
     */
    public void removeProjections() {
        super._map.remove("projections");
    }

    /**
     * Getter for projections
     * 
     * @see RestliDataSource.Fields#projections
     */
    public StringArray getProjections(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getProjections();
            case NULL:
                if (_projectionsField!= null) {
                    return _projectionsField;
                } else {
                    Object __rawValue = super._map.get("projections");
                    _projectionsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _projectionsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for projections
     * 
     * @return
     *     Optional field. Always check for null.
     * @see RestliDataSource.Fields#projections
     */
    @Nullable
    public StringArray getProjections() {
        if (_projectionsField!= null) {
            return _projectionsField;
        } else {
            Object __rawValue = super._map.get("projections");
            if (__rawValue == null) {
                return DEFAULT_Projections;
            }
            _projectionsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _projectionsField;
        }
    }

    /**
     * Setter for projections
     * 
     * @see RestliDataSource.Fields#projections
     */
    public RestliDataSource setProjections(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setProjections(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeProjections();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "projections", value.data());
                    _projectionsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "projections", value.data());
                    _projectionsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for projections
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RestliDataSource.Fields#projections
     */
    public RestliDataSource setProjections(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field projections of com.linkedin.feathr.featureDataModel.RestliDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "projections", value.data());
            _projectionsField = value;
        }
        return this;
    }

    /**
     * Existence checker for requestParameters
     * 
     * @see RestliDataSource.Fields#requestParameters
     */
    public boolean hasRequestParameters() {
        if (_requestParametersField!= null) {
            return true;
        }
        return super._map.containsKey("requestParameters");
    }

    /**
     * Remover for requestParameters
     * 
     * @see RestliDataSource.Fields#requestParameters
     */
    public void removeRequestParameters() {
        super._map.remove("requestParameters");
    }

    /**
     * Getter for requestParameters
     * 
     * @see RestliDataSource.Fields#requestParameters
     */
    public RequestParameterValueMap getRequestParameters(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getRequestParameters();
            case NULL:
                if (_requestParametersField!= null) {
                    return _requestParametersField;
                } else {
                    Object __rawValue = super._map.get("requestParameters");
                    _requestParametersField = ((__rawValue == null)?null:new RequestParameterValueMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _requestParametersField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for requestParameters
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see RestliDataSource.Fields#requestParameters
     */
    @Nonnull
    public RequestParameterValueMap getRequestParameters() {
        if (_requestParametersField!= null) {
            return _requestParametersField;
        } else {
            Object __rawValue = super._map.get("requestParameters");
            if (__rawValue == null) {
                return DEFAULT_RequestParameters;
            }
            _requestParametersField = ((__rawValue == null)?null:new RequestParameterValueMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _requestParametersField;
        }
    }

    /**
     * Setter for requestParameters
     * 
     * @see RestliDataSource.Fields#requestParameters
     */
    public RestliDataSource setRequestParameters(RequestParameterValueMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setRequestParameters(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field requestParameters of com.linkedin.feathr.featureDataModel.RestliDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "requestParameters", value.data());
                    _requestParametersField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeRequestParameters();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "requestParameters", value.data());
                    _requestParametersField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "requestParameters", value.data());
                    _requestParametersField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for requestParameters
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RestliDataSource.Fields#requestParameters
     */
    public RestliDataSource setRequestParameters(
        @Nonnull
        RequestParameterValueMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field requestParameters of com.linkedin.feathr.featureDataModel.RestliDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "requestParameters", value.data());
            _requestParametersField = value;
        }
        return this;
    }

    @Override
    public RestliDataSource clone()
        throws CloneNotSupportedException
    {
        RestliDataSource __clone = ((RestliDataSource) super.clone());
        __clone.__changeListener = new RestliDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public RestliDataSource copy()
        throws CloneNotSupportedException
    {
        RestliDataSource __copy = ((RestliDataSource) super.copy());
        __copy._projectionsField = null;
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy._requestParametersField = null;
        __copy._resourceNameField = null;
        __copy.__changeListener = new RestliDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final RestliDataSource __objectRef;

        private ChangeListener(RestliDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "projections":
                    __objectRef._projectionsField = null;
                    break;
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
                    break;
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
                    break;
                case "requestParameters":
                    __objectRef._requestParametersField = null;
                    break;
                case "resourceName":
                    __objectRef._resourceNameField = null;
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
         * The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.
         * 
         */
        public PathSpec resourceName() {
            return new PathSpec(getPathComponents(), "resourceName");
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details. It should be required with a default value, but the optional flag can't be removed after its check-in, for complying with backward compatibility requirement of schema evolution.
         * 
         */
        public PathSpec projections() {
            return new PathSpec(getPathComponents(), "projections");
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details. It should be required with a default value, but the optional flag can't be removed after its check-in, for complying with backward compatibility requirement of schema evolution.
         * 
         */
        public PathSpec projections(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "projections");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#methods,get, the key value pair keyed by viewer represents a request parameter of the Get method in jobPostings resource.
         * 
         */
        public com.linkedin.feathr.featureDataModel.RequestParameterValueMap.Fields requestParameters() {
            return new com.linkedin.feathr.featureDataModel.RequestParameterValueMap.Fields(getPathComponents(), "requestParameters");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;
        private com.linkedin.feathr.featureDataModel.RequestParameterValueMap.ProjectionMask _requestParametersMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public RestliDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public RestliDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public RestliDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.
         * 
         */
        public RestliDataSource.ProjectionMask withResourceName() {
            getDataMap().put("resourceName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details. It should be required with a default value, but the optional flag can't be removed after its check-in, for complying with backward compatibility requirement of schema evolution.
         * 
         */
        public RestliDataSource.ProjectionMask withProjections() {
            getDataMap().put("projections", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details. It should be required with a default value, but the optional flag can't be removed after its check-in, for complying with backward compatibility requirement of schema evolution.
         * 
         */
        public RestliDataSource.ProjectionMask withProjections(Integer start, Integer count) {
            getDataMap().put("projections", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("projections").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("projections").put("$count", count);
            }
            return this;
        }

        /**
         * Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#methods,get, the key value pair keyed by viewer represents a request parameter of the Get method in jobPostings resource.
         * 
         */
        public RestliDataSource.ProjectionMask withRequestParameters(Function<com.linkedin.feathr.featureDataModel.RequestParameterValueMap.ProjectionMask, com.linkedin.feathr.featureDataModel.RequestParameterValueMap.ProjectionMask> nestedMask) {
            _requestParametersMask = nestedMask.apply(((_requestParametersMask == null)?RequestParameterValueMap.createMask():_requestParametersMask));
            getDataMap().put("requestParameters", _requestParametersMask.getDataMap());
            return this;
        }

        /**
         * Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#methods,get, the key value pair keyed by viewer represents a request parameter of the Get method in jobPostings resource.
         * 
         */
        public RestliDataSource.ProjectionMask withRequestParameters() {
            _requestParametersMask = null;
            getDataMap().put("requestParameters", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
