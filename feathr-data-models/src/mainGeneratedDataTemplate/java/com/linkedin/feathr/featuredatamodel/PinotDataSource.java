
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
 * Represent a Pinot data source. Pinot is a realtime distributed OLAP datastore which supports data fetching through standard SQL query syntax and semantics. It is suitable for querying time series data with lots of Dimensions and Metrics. For more details on Pinot: go/pinot. Also see <a href="https://docs.google.com/document/d/1nx-j-JJLWY4QaU2hgoQ6H9rDEcVG9jZo6CQVRX4ATvA/edit/">[RFC] Pinot Frame Online</a> for the details on the Pinot data source design.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/PinotDataSource.pdl.")
public class PinotDataSource
    extends RecordTemplate
{

    private final static PinotDataSource.Fields _fields = new PinotDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represent a Pinot data source. Pinot is a realtime distributed OLAP datastore which supports data fetching through standard SQL query syntax and semantics. It is suitable for querying time series data with lots of Dimensions and Metrics. For more details on Pinot: go/pinot. Also see <a href=\"https://docs.google.com/document/d/1nx-j-JJLWY4QaU2hgoQ6H9rDEcVG9jZo6CQVRX4ATvA/edit/\">[RFC] Pinot Frame Online</a> for the details on the Pinot data source design.*/record PinotDataSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Represent the service name in the Pinot D2 config for the source Pinot table.*/resourceName:string/**Represent the sql query template to fetch data from Pinot table, with \u201c?\u201d as placeholders for run time value replacement from specified queryArguments in the same order. For example: \"SELECT objectAttributes, timeStampSec FROM RecentMemberActions WHERE actorId IN (?) AND timeStampSec > ?\".*/queryTemplate:string/**Represent mvel expressions whose values will be evaluated at runtime to replace the \"?\" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be [\"key[0]\", \"System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60\"].*/queryArguments:array[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}]/**Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be [\"actorId\"]. As in queryArguments, only \"key[0]\" is base on key values, and it is used with \"actorId\" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{\u201c123\u201d, \u201cpage_view\u201d, \u201c323\u201d}, {\u201c987\", \u201cpage_view\u201d, \u201c876\"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.*/queryKeyColumns:array[string]}", SchemaFormatType.PDL));
    private String _dataSourceRefField = null;
    private String _resourceNameField = null;
    private String _queryTemplateField = null;
    private MvelExpressionArray _queryArgumentsField = null;
    private StringArray _queryKeyColumnsField = null;
    private PinotDataSource.ChangeListener __changeListener = new PinotDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_ResourceName = SCHEMA.getField("resourceName");
    private final static RecordDataSchema.Field FIELD_QueryTemplate = SCHEMA.getField("queryTemplate");
    private final static RecordDataSchema.Field FIELD_QueryArguments = SCHEMA.getField("queryArguments");
    private final static RecordDataSchema.Field FIELD_QueryKeyColumns = SCHEMA.getField("queryKeyColumns");

    public PinotDataSource() {
        super(new DataMap(7, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public PinotDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static PinotDataSource.Fields fields() {
        return _fields;
    }

    public static PinotDataSource.ProjectionMask createMask() {
        return new PinotDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see PinotDataSource.Fields#dataSourceRef
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
     * @see PinotDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see PinotDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see PinotDataSource.Fields#dataSourceRef
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
     * @see PinotDataSource.Fields#dataSourceRef
     */
    public PinotDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see PinotDataSource.Fields#dataSourceRef
     */
    public PinotDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.PinotDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for resourceName
     * 
     * @see PinotDataSource.Fields#resourceName
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
     * @see PinotDataSource.Fields#resourceName
     */
    public void removeResourceName() {
        super._map.remove("resourceName");
    }

    /**
     * Getter for resourceName
     * 
     * @see PinotDataSource.Fields#resourceName
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
     * @see PinotDataSource.Fields#resourceName
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
     * @see PinotDataSource.Fields#resourceName
     */
    public PinotDataSource setResourceName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setResourceName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field resourceName of com.linkedin.feathr.featureDataModel.PinotDataSource");
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
     * @see PinotDataSource.Fields#resourceName
     */
    public PinotDataSource setResourceName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field resourceName of com.linkedin.feathr.featureDataModel.PinotDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "resourceName", value);
            _resourceNameField = value;
        }
        return this;
    }

    /**
     * Existence checker for queryTemplate
     * 
     * @see PinotDataSource.Fields#queryTemplate
     */
    public boolean hasQueryTemplate() {
        if (_queryTemplateField!= null) {
            return true;
        }
        return super._map.containsKey("queryTemplate");
    }

    /**
     * Remover for queryTemplate
     * 
     * @see PinotDataSource.Fields#queryTemplate
     */
    public void removeQueryTemplate() {
        super._map.remove("queryTemplate");
    }

    /**
     * Getter for queryTemplate
     * 
     * @see PinotDataSource.Fields#queryTemplate
     */
    public String getQueryTemplate(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getQueryTemplate();
            case DEFAULT:
            case NULL:
                if (_queryTemplateField!= null) {
                    return _queryTemplateField;
                } else {
                    Object __rawValue = super._map.get("queryTemplate");
                    _queryTemplateField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _queryTemplateField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for queryTemplate
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PinotDataSource.Fields#queryTemplate
     */
    @Nonnull
    public String getQueryTemplate() {
        if (_queryTemplateField!= null) {
            return _queryTemplateField;
        } else {
            Object __rawValue = super._map.get("queryTemplate");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("queryTemplate");
            }
            _queryTemplateField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _queryTemplateField;
        }
    }

    /**
     * Setter for queryTemplate
     * 
     * @see PinotDataSource.Fields#queryTemplate
     */
    public PinotDataSource setQueryTemplate(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setQueryTemplate(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field queryTemplate of com.linkedin.feathr.featureDataModel.PinotDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "queryTemplate", value);
                    _queryTemplateField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeQueryTemplate();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "queryTemplate", value);
                    _queryTemplateField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "queryTemplate", value);
                    _queryTemplateField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for queryTemplate
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PinotDataSource.Fields#queryTemplate
     */
    public PinotDataSource setQueryTemplate(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field queryTemplate of com.linkedin.feathr.featureDataModel.PinotDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "queryTemplate", value);
            _queryTemplateField = value;
        }
        return this;
    }

    /**
     * Existence checker for queryArguments
     * 
     * @see PinotDataSource.Fields#queryArguments
     */
    public boolean hasQueryArguments() {
        if (_queryArgumentsField!= null) {
            return true;
        }
        return super._map.containsKey("queryArguments");
    }

    /**
     * Remover for queryArguments
     * 
     * @see PinotDataSource.Fields#queryArguments
     */
    public void removeQueryArguments() {
        super._map.remove("queryArguments");
    }

    /**
     * Getter for queryArguments
     * 
     * @see PinotDataSource.Fields#queryArguments
     */
    public MvelExpressionArray getQueryArguments(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getQueryArguments();
            case DEFAULT:
            case NULL:
                if (_queryArgumentsField!= null) {
                    return _queryArgumentsField;
                } else {
                    Object __rawValue = super._map.get("queryArguments");
                    _queryArgumentsField = ((__rawValue == null)?null:new MvelExpressionArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _queryArgumentsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for queryArguments
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PinotDataSource.Fields#queryArguments
     */
    @Nonnull
    public MvelExpressionArray getQueryArguments() {
        if (_queryArgumentsField!= null) {
            return _queryArgumentsField;
        } else {
            Object __rawValue = super._map.get("queryArguments");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("queryArguments");
            }
            _queryArgumentsField = ((__rawValue == null)?null:new MvelExpressionArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _queryArgumentsField;
        }
    }

    /**
     * Setter for queryArguments
     * 
     * @see PinotDataSource.Fields#queryArguments
     */
    public PinotDataSource setQueryArguments(MvelExpressionArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setQueryArguments(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field queryArguments of com.linkedin.feathr.featureDataModel.PinotDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "queryArguments", value.data());
                    _queryArgumentsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeQueryArguments();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "queryArguments", value.data());
                    _queryArgumentsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "queryArguments", value.data());
                    _queryArgumentsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for queryArguments
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PinotDataSource.Fields#queryArguments
     */
    public PinotDataSource setQueryArguments(
        @Nonnull
        MvelExpressionArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field queryArguments of com.linkedin.feathr.featureDataModel.PinotDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "queryArguments", value.data());
            _queryArgumentsField = value;
        }
        return this;
    }

    /**
     * Existence checker for queryKeyColumns
     * 
     * @see PinotDataSource.Fields#queryKeyColumns
     */
    public boolean hasQueryKeyColumns() {
        if (_queryKeyColumnsField!= null) {
            return true;
        }
        return super._map.containsKey("queryKeyColumns");
    }

    /**
     * Remover for queryKeyColumns
     * 
     * @see PinotDataSource.Fields#queryKeyColumns
     */
    public void removeQueryKeyColumns() {
        super._map.remove("queryKeyColumns");
    }

    /**
     * Getter for queryKeyColumns
     * 
     * @see PinotDataSource.Fields#queryKeyColumns
     */
    public StringArray getQueryKeyColumns(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getQueryKeyColumns();
            case DEFAULT:
            case NULL:
                if (_queryKeyColumnsField!= null) {
                    return _queryKeyColumnsField;
                } else {
                    Object __rawValue = super._map.get("queryKeyColumns");
                    _queryKeyColumnsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _queryKeyColumnsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for queryKeyColumns
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see PinotDataSource.Fields#queryKeyColumns
     */
    @Nonnull
    public StringArray getQueryKeyColumns() {
        if (_queryKeyColumnsField!= null) {
            return _queryKeyColumnsField;
        } else {
            Object __rawValue = super._map.get("queryKeyColumns");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("queryKeyColumns");
            }
            _queryKeyColumnsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _queryKeyColumnsField;
        }
    }

    /**
     * Setter for queryKeyColumns
     * 
     * @see PinotDataSource.Fields#queryKeyColumns
     */
    public PinotDataSource setQueryKeyColumns(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setQueryKeyColumns(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field queryKeyColumns of com.linkedin.feathr.featureDataModel.PinotDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "queryKeyColumns", value.data());
                    _queryKeyColumnsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeQueryKeyColumns();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "queryKeyColumns", value.data());
                    _queryKeyColumnsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "queryKeyColumns", value.data());
                    _queryKeyColumnsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for queryKeyColumns
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see PinotDataSource.Fields#queryKeyColumns
     */
    public PinotDataSource setQueryKeyColumns(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field queryKeyColumns of com.linkedin.feathr.featureDataModel.PinotDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "queryKeyColumns", value.data());
            _queryKeyColumnsField = value;
        }
        return this;
    }

    @Override
    public PinotDataSource clone()
        throws CloneNotSupportedException
    {
        PinotDataSource __clone = ((PinotDataSource) super.clone());
        __clone.__changeListener = new PinotDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public PinotDataSource copy()
        throws CloneNotSupportedException
    {
        PinotDataSource __copy = ((PinotDataSource) super.copy());
        __copy._queryTemplateField = null;
        __copy._queryArgumentsField = null;
        __copy._queryKeyColumnsField = null;
        __copy._dataSourceRefField = null;
        __copy._resourceNameField = null;
        __copy.__changeListener = new PinotDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final PinotDataSource __objectRef;

        private ChangeListener(PinotDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "queryTemplate":
                    __objectRef._queryTemplateField = null;
                    break;
                case "queryArguments":
                    __objectRef._queryArgumentsField = null;
                    break;
                case "queryKeyColumns":
                    __objectRef._queryKeyColumnsField = null;
                    break;
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
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
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public PathSpec dataSourceRef() {
            return new PathSpec(getPathComponents(), "dataSourceRef");
        }

        /**
         * Represent the service name in the Pinot D2 config for the source Pinot table.
         * 
         */
        public PathSpec resourceName() {
            return new PathSpec(getPathComponents(), "resourceName");
        }

        /**
         * Represent the sql query template to fetch data from Pinot table, with “?” as placeholders for run time value replacement from specified queryArguments in the same order. For example: "SELECT objectAttributes, timeStampSec FROM RecentMemberActions WHERE actorId IN (?) AND timeStampSec > ?".
         * 
         */
        public PathSpec queryTemplate() {
            return new PathSpec(getPathComponents(), "queryTemplate");
        }

        /**
         * Represent mvel expressions whose values will be evaluated at runtime to replace the "?" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be ["key[0]", "System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60"].
         * 
         */
        public com.linkedin.feathr.featureDataModel.MvelExpressionArray.Fields queryArguments() {
            return new com.linkedin.feathr.featureDataModel.MvelExpressionArray.Fields(getPathComponents(), "queryArguments");
        }

        /**
         * Represent mvel expressions whose values will be evaluated at runtime to replace the "?" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be ["key[0]", "System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60"].
         * 
         */
        public PathSpec queryArguments(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "queryArguments");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be ["actorId"]. As in queryArguments, only "key[0]" is base on key values, and it is used with "actorId" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{“123”, “page_view”, “323”}, {“987", “page_view”, “876"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.
         * 
         */
        public PathSpec queryKeyColumns() {
            return new PathSpec(getPathComponents(), "queryKeyColumns");
        }

        /**
         * Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be ["actorId"]. As in queryArguments, only "key[0]" is base on key values, and it is used with "actorId" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{“123”, “page_view”, “323”}, {“987", “page_view”, “876"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.
         * 
         */
        public PathSpec queryKeyColumns(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "queryKeyColumns");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.MvelExpressionArray.ProjectionMask _queryArgumentsMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public PinotDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represent the service name in the Pinot D2 config for the source Pinot table.
         * 
         */
        public PinotDataSource.ProjectionMask withResourceName() {
            getDataMap().put("resourceName", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represent the sql query template to fetch data from Pinot table, with “?” as placeholders for run time value replacement from specified queryArguments in the same order. For example: "SELECT objectAttributes, timeStampSec FROM RecentMemberActions WHERE actorId IN (?) AND timeStampSec > ?".
         * 
         */
        public PinotDataSource.ProjectionMask withQueryTemplate() {
            getDataMap().put("queryTemplate", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represent mvel expressions whose values will be evaluated at runtime to replace the "?" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be ["key[0]", "System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60"].
         * 
         */
        public PinotDataSource.ProjectionMask withQueryArguments(Function<com.linkedin.feathr.featureDataModel.MvelExpressionArray.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpressionArray.ProjectionMask> nestedMask) {
            _queryArgumentsMask = nestedMask.apply(((_queryArgumentsMask == null)?MvelExpressionArray.createMask():_queryArgumentsMask));
            getDataMap().put("queryArguments", _queryArgumentsMask.getDataMap());
            return this;
        }

        /**
         * Represent mvel expressions whose values will be evaluated at runtime to replace the "?" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be ["key[0]", "System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60"].
         * 
         */
        public PinotDataSource.ProjectionMask withQueryArguments() {
            _queryArgumentsMask = null;
            getDataMap().put("queryArguments", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represent mvel expressions whose values will be evaluated at runtime to replace the "?" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be ["key[0]", "System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60"].
         * 
         */
        public PinotDataSource.ProjectionMask withQueryArguments(Function<com.linkedin.feathr.featureDataModel.MvelExpressionArray.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpressionArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _queryArgumentsMask = nestedMask.apply(((_queryArgumentsMask == null)?MvelExpressionArray.createMask():_queryArgumentsMask));
            getDataMap().put("queryArguments", _queryArgumentsMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("queryArguments").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("queryArguments").put("$count", count);
            }
            return this;
        }

        /**
         * Represent mvel expressions whose values will be evaluated at runtime to replace the "?" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be ["key[0]", "System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60"].
         * 
         */
        public PinotDataSource.ProjectionMask withQueryArguments(Integer start, Integer count) {
            _queryArgumentsMask = null;
            getDataMap().put("queryArguments", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("queryArguments").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("queryArguments").put("$count", count);
            }
            return this;
        }

        /**
         * Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be ["actorId"]. As in queryArguments, only "key[0]" is base on key values, and it is used with "actorId" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{“123”, “page_view”, “323”}, {“987", “page_view”, “876"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.
         * 
         */
        public PinotDataSource.ProjectionMask withQueryKeyColumns() {
            getDataMap().put("queryKeyColumns", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be ["actorId"]. As in queryArguments, only "key[0]" is base on key values, and it is used with "actorId" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{“123”, “page_view”, “323”}, {“987", “page_view”, “876"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.
         * 
         */
        public PinotDataSource.ProjectionMask withQueryKeyColumns(Integer start, Integer count) {
            getDataMap().put("queryKeyColumns", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("queryKeyColumns").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("queryKeyColumns").put("$count", count);
            }
            return this;
        }

    }

}
