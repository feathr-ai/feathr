
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
 * Represents a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. For more details, refer to https://linkedin.github.io/rest.li/user_guide/restli_server#finder. Note: it is possible for a finder source to have an keyExpr for association resources since the key parts of the association key can be used as query parameters which are modeled as CompoundKey. See https://linkedin.github.io/rest.li/user_guide/restli_server#finder for more details
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/RestliFinderDataSource.pdl.")
public class RestliFinderDataSource
    extends RecordTemplate
{

    private final static RestliFinderDataSource.Fields _fields = new RestliFinderDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. For more details, refer to https://linkedin.github.io/rest.li/user_guide/restli_server#finder. Note: it is possible for a finder source to have an keyExpr for association resources since the key parts of the association key can be used as query parameters which are modeled as CompoundKey. See https://linkedin.github.io/rest.li/user_guide/restli_server#finder for more details*/record RestliFinderDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.*/resourceName:string/**Represents the finder method name of the resource.*/finderMethod:string/**Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.*/projections:array[/**Represents a PathSpec string. PathSpec is used to specify projections in Rest.li request in order to select a subset of object contents. For more details, refer to https://linkedin.github.io/rest.li/Projections. Some examples: 'listingType', 'member:(firstName, lastName)'.*/typeref PathSpec=string]=[]/**Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#finders,companyJobCodes, the key value pair keyed by contract represents a request parameter of the companyJobCodes finder method in jobPostings resource.*/requestParameters:map[string/**Represents the value part in a Rest.li request paramater key/value pair.*/typeref RequestParameterValue=union[MvelExpression/**Represents a Json string.*/typeref JsonString=string]]={}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private String _resourceNameField = null;
    private String _finderMethodField = null;
    private StringArray _projectionsField = null;
    private RequestParameterValueMap _requestParametersField = null;
    private RestliFinderDataSource.ChangeListener __changeListener = new RestliFinderDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_ResourceName = SCHEMA.getField("resourceName");
    private final static RecordDataSchema.Field FIELD_FinderMethod = SCHEMA.getField("finderMethod");
    private final static RecordDataSchema.Field FIELD_Projections = SCHEMA.getField("projections");
    private final static StringArray DEFAULT_Projections;
    private final static RecordDataSchema.Field FIELD_RequestParameters = SCHEMA.getField("requestParameters");
    private final static RequestParameterValueMap DEFAULT_RequestParameters;

    static {
        DEFAULT_Projections = ((FIELD_Projections.getDefault() == null)?null:new StringArray(DataTemplateUtil.castOrThrow(FIELD_Projections.getDefault(), DataList.class)));
        DEFAULT_RequestParameters = ((FIELD_RequestParameters.getDefault() == null)?null:new RequestParameterValueMap(DataTemplateUtil.castOrThrow(FIELD_RequestParameters.getDefault(), DataMap.class)));
    }

    public RestliFinderDataSource() {
        super(new DataMap(8, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public RestliFinderDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static RestliFinderDataSource.Fields fields() {
        return _fields;
    }

    public static RestliFinderDataSource.ProjectionMask createMask() {
        return new RestliFinderDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see RestliFinderDataSource.Fields#keyFunction
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
     * @see RestliFinderDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see RestliFinderDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see RestliFinderDataSource.Fields#keyFunction
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
     * @see RestliFinderDataSource.Fields#keyFunction
     */
    public RestliFinderDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see RestliFinderDataSource.Fields#keyFunction
     */
    public RestliFinderDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.RestliFinderDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see RestliFinderDataSource.Fields#dataSourceRef
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
     * @see RestliFinderDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see RestliFinderDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see RestliFinderDataSource.Fields#dataSourceRef
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
     * @see RestliFinderDataSource.Fields#dataSourceRef
     */
    public RestliFinderDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see RestliFinderDataSource.Fields#dataSourceRef
     */
    public RestliFinderDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.RestliFinderDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for resourceName
     * 
     * @see RestliFinderDataSource.Fields#resourceName
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
     * @see RestliFinderDataSource.Fields#resourceName
     */
    public void removeResourceName() {
        super._map.remove("resourceName");
    }

    /**
     * Getter for resourceName
     * 
     * @see RestliFinderDataSource.Fields#resourceName
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
     * @see RestliFinderDataSource.Fields#resourceName
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
     * @see RestliFinderDataSource.Fields#resourceName
     */
    public RestliFinderDataSource setResourceName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setResourceName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field resourceName of com.linkedin.feathr.featureDataModel.RestliFinderDataSource");
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
     * @see RestliFinderDataSource.Fields#resourceName
     */
    public RestliFinderDataSource setResourceName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field resourceName of com.linkedin.feathr.featureDataModel.RestliFinderDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "resourceName", value);
            _resourceNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for finderMethod
     * 
     * @see RestliFinderDataSource.Fields#finderMethod
     */
    public boolean hasFinderMethod() {
        if (_finderMethodField!= null) {
            return true;
        }
        return super._map.containsKey("finderMethod");
    }

    /**
     * Remover for finderMethod
     * 
     * @see RestliFinderDataSource.Fields#finderMethod
     */
    public void removeFinderMethod() {
        super._map.remove("finderMethod");
    }

    /**
     * Getter for finderMethod
     * 
     * @see RestliFinderDataSource.Fields#finderMethod
     */
    public String getFinderMethod(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFinderMethod();
            case DEFAULT:
            case NULL:
                if (_finderMethodField!= null) {
                    return _finderMethodField;
                } else {
                    Object __rawValue = super._map.get("finderMethod");
                    _finderMethodField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _finderMethodField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for finderMethod
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see RestliFinderDataSource.Fields#finderMethod
     */
    @Nonnull
    public String getFinderMethod() {
        if (_finderMethodField!= null) {
            return _finderMethodField;
        } else {
            Object __rawValue = super._map.get("finderMethod");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("finderMethod");
            }
            _finderMethodField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _finderMethodField;
        }
    }

    /**
     * Setter for finderMethod
     * 
     * @see RestliFinderDataSource.Fields#finderMethod
     */
    public RestliFinderDataSource setFinderMethod(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFinderMethod(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field finderMethod of com.linkedin.feathr.featureDataModel.RestliFinderDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "finderMethod", value);
                    _finderMethodField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFinderMethod();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "finderMethod", value);
                    _finderMethodField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "finderMethod", value);
                    _finderMethodField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for finderMethod
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RestliFinderDataSource.Fields#finderMethod
     */
    public RestliFinderDataSource setFinderMethod(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field finderMethod of com.linkedin.feathr.featureDataModel.RestliFinderDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "finderMethod", value);
            _finderMethodField = value;
        }
        return this;
    }

    /**
     * Existence checker for projections
     * 
     * @see RestliFinderDataSource.Fields#projections
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
     * @see RestliFinderDataSource.Fields#projections
     */
    public void removeProjections() {
        super._map.remove("projections");
    }

    /**
     * Getter for projections
     * 
     * @see RestliFinderDataSource.Fields#projections
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
     *     Required field. Could be null for partial record.
     * @see RestliFinderDataSource.Fields#projections
     */
    @Nonnull
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
     * @see RestliFinderDataSource.Fields#projections
     */
    public RestliFinderDataSource setProjections(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setProjections(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field projections of com.linkedin.feathr.featureDataModel.RestliFinderDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "projections", value.data());
                    _projectionsField = value;
                }
                break;
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
     * @see RestliFinderDataSource.Fields#projections
     */
    public RestliFinderDataSource setProjections(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field projections of com.linkedin.feathr.featureDataModel.RestliFinderDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "projections", value.data());
            _projectionsField = value;
        }
        return this;
    }

    /**
     * Existence checker for requestParameters
     * 
     * @see RestliFinderDataSource.Fields#requestParameters
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
     * @see RestliFinderDataSource.Fields#requestParameters
     */
    public void removeRequestParameters() {
        super._map.remove("requestParameters");
    }

    /**
     * Getter for requestParameters
     * 
     * @see RestliFinderDataSource.Fields#requestParameters
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
     * @see RestliFinderDataSource.Fields#requestParameters
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
     * @see RestliFinderDataSource.Fields#requestParameters
     */
    public RestliFinderDataSource setRequestParameters(RequestParameterValueMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setRequestParameters(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field requestParameters of com.linkedin.feathr.featureDataModel.RestliFinderDataSource");
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
     * @see RestliFinderDataSource.Fields#requestParameters
     */
    public RestliFinderDataSource setRequestParameters(
        @Nonnull
        RequestParameterValueMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field requestParameters of com.linkedin.feathr.featureDataModel.RestliFinderDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "requestParameters", value.data());
            _requestParametersField = value;
        }
        return this;
    }

    @Override
    public RestliFinderDataSource clone()
        throws CloneNotSupportedException
    {
        RestliFinderDataSource __clone = ((RestliFinderDataSource) super.clone());
        __clone.__changeListener = new RestliFinderDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public RestliFinderDataSource copy()
        throws CloneNotSupportedException
    {
        RestliFinderDataSource __copy = ((RestliFinderDataSource) super.copy());
        __copy._finderMethodField = null;
        __copy._projectionsField = null;
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy._requestParametersField = null;
        __copy._resourceNameField = null;
        __copy.__changeListener = new RestliFinderDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final RestliFinderDataSource __objectRef;

        private ChangeListener(RestliFinderDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "finderMethod":
                    __objectRef._finderMethodField = null;
                    break;
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
         * Represents the finder method name of the resource.
         * 
         */
        public PathSpec finderMethod() {
            return new PathSpec(getPathComponents(), "finderMethod");
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.
         * 
         */
        public PathSpec projections() {
            return new PathSpec(getPathComponents(), "projections");
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.
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
         * Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#finders,companyJobCodes, the key value pair keyed by contract represents a request parameter of the companyJobCodes finder method in jobPostings resource.
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
            super(8);
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OnlineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withResourceName() {
            getDataMap().put("resourceName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the finder method name of the resource.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withFinderMethod() {
            getDataMap().put("finderMethod", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withProjections() {
            getDataMap().put("projections", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withProjections(Integer start, Integer count) {
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
         * Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#finders,companyJobCodes, the key value pair keyed by contract represents a request parameter of the companyJobCodes finder method in jobPostings resource.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withRequestParameters(Function<com.linkedin.feathr.featureDataModel.RequestParameterValueMap.ProjectionMask, com.linkedin.feathr.featureDataModel.RequestParameterValueMap.ProjectionMask> nestedMask) {
            _requestParametersMask = nestedMask.apply(((_requestParametersMask == null)?RequestParameterValueMap.createMask():_requestParametersMask));
            getDataMap().put("requestParameters", _requestParametersMask.getDataMap());
            return this;
        }

        /**
         * Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#finders,companyJobCodes, the key value pair keyed by contract represents a request parameter of the companyJobCodes finder method in jobPostings resource.
         * 
         */
        public RestliFinderDataSource.ProjectionMask withRequestParameters() {
            _requestParametersMask = null;
            getDataMap().put("requestParameters", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
