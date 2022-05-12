
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
 * Represents a HDFS data source.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/HdfsDataSource.pdl.")
public class HdfsDataSource
    extends RecordTemplate
{

    private final static HdfsDataSource.Fields _fields = new HdfsDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a HDFS data source.*/record HdfsDataSource includes/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineDataSourceKey{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Location of the dataset, it can be a HDFS path of the dataset, or Dali URI (dalids:///)*/datasetLocation:union[/**A location on HDFS.*/record HdfsLocation{/**Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/*/path:string}/**The location of a Dali dataset or view. See [go/dali](http://go/dali).*/record DaliLocation{/**The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.*/uri:string}]/**Specify the time field in the dataset schema. This is an optional field used for time-series operations, such as Sliding Window Aggregation. See details in SlidingWindowAggregation section in go/feathroffline.*/timeField:optional/**Represent a time field in the dataset schema, it is used for time-series operations. One usage is Sliding Window Aggregation, see details in go/frameoffline.*/record TimeField{/**Name of the time field in the dataset schema.*/name:string/**The format that the time field uses to represent time.*/format:/**The format of the time field. It can be either the granularity of a timestamp or datetime format e.g. yyyy/MM/dd/HH/mm/ss.*/typeref TimeFieldFormat=union[/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string]}/**Represents the time format being used to parse snapshot time from a dataset's file path. See DatasetSnapshotTimeFormat for more details.*/datasetSnapshotTimeFormat:optional/**Represents the time format being used to parse snapshot time from a dataset's file path, for example \"2020/09/30\" in /hdfs/jobs/lyndarel/features/master/members/daily/2020/09/30/. The dataset snapshot time is useful when feature data is time-partitioned for time-aware join or sliding-window aggregation. An example feature is derived skills, because derived skills change often based on user activities, modeling engineers want to join the time-sensitive observation data with the closest version of the feature. Refer to Unification of SWA and time-based features RFC (https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#) for more details.*/typeref DatasetSnapshotTimeFormat=union[DateTimeFormat/**Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of \"Unification of SWA and time-based features\" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).*/record StandardDateTimeFormat{}]}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private HdfsDataSource.DatasetLocation _datasetLocationField = null;
    private TimeField _timeFieldField = null;
    private DatasetSnapshotTimeFormat _datasetSnapshotTimeFormatField = null;
    private HdfsDataSource.ChangeListener __changeListener = new HdfsDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_DatasetLocation = SCHEMA.getField("datasetLocation");
    private final static RecordDataSchema.Field FIELD_TimeField = SCHEMA.getField("timeField");
    private final static RecordDataSchema.Field FIELD_DatasetSnapshotTimeFormat = SCHEMA.getField("datasetSnapshotTimeFormat");

    public HdfsDataSource() {
        super(new DataMap(7, 0.75F), SCHEMA, 6);
        addChangeListener(__changeListener);
    }

    public HdfsDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static HdfsDataSource.Fields fields() {
        return _fields;
    }

    public static HdfsDataSource.ProjectionMask createMask() {
        return new HdfsDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see HdfsDataSource.Fields#keyFunction
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
     * @see HdfsDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see HdfsDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see HdfsDataSource.Fields#keyFunction
     */
    @Nullable
    public com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction getKeyFunction() {
        if (_keyFunctionField!= null) {
            return _keyFunctionField;
        } else {
            Object __rawValue = super._map.get("keyFunction");
            _keyFunctionField = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction(__rawValue));
            return _keyFunctionField;
        }
    }

    /**
     * Setter for keyFunction
     * 
     * @see HdfsDataSource.Fields#keyFunction
     */
    public HdfsDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction value, SetMode mode) {
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
     * @see HdfsDataSource.Fields#keyFunction
     */
    public HdfsDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.HdfsDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see HdfsDataSource.Fields#dataSourceRef
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
     * @see HdfsDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see HdfsDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see HdfsDataSource.Fields#dataSourceRef
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
     * @see HdfsDataSource.Fields#dataSourceRef
     */
    public HdfsDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see HdfsDataSource.Fields#dataSourceRef
     */
    public HdfsDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.HdfsDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for datasetLocation
     * 
     * @see HdfsDataSource.Fields#datasetLocation
     */
    public boolean hasDatasetLocation() {
        if (_datasetLocationField!= null) {
            return true;
        }
        return super._map.containsKey("datasetLocation");
    }

    /**
     * Remover for datasetLocation
     * 
     * @see HdfsDataSource.Fields#datasetLocation
     */
    public void removeDatasetLocation() {
        super._map.remove("datasetLocation");
    }

    /**
     * Getter for datasetLocation
     * 
     * @see HdfsDataSource.Fields#datasetLocation
     */
    public HdfsDataSource.DatasetLocation getDatasetLocation(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDatasetLocation();
            case DEFAULT:
            case NULL:
                if (_datasetLocationField!= null) {
                    return _datasetLocationField;
                } else {
                    Object __rawValue = super._map.get("datasetLocation");
                    _datasetLocationField = ((__rawValue == null)?null:new HdfsDataSource.DatasetLocation(__rawValue));
                    return _datasetLocationField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for datasetLocation
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see HdfsDataSource.Fields#datasetLocation
     */
    @Nonnull
    public HdfsDataSource.DatasetLocation getDatasetLocation() {
        if (_datasetLocationField!= null) {
            return _datasetLocationField;
        } else {
            Object __rawValue = super._map.get("datasetLocation");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("datasetLocation");
            }
            _datasetLocationField = ((__rawValue == null)?null:new HdfsDataSource.DatasetLocation(__rawValue));
            return _datasetLocationField;
        }
    }

    /**
     * Setter for datasetLocation
     * 
     * @see HdfsDataSource.Fields#datasetLocation
     */
    public HdfsDataSource setDatasetLocation(HdfsDataSource.DatasetLocation value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDatasetLocation(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field datasetLocation of com.linkedin.feathr.featureDataModel.HdfsDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "datasetLocation", value.data());
                    _datasetLocationField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDatasetLocation();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "datasetLocation", value.data());
                    _datasetLocationField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "datasetLocation", value.data());
                    _datasetLocationField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for datasetLocation
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HdfsDataSource.Fields#datasetLocation
     */
    public HdfsDataSource setDatasetLocation(
        @Nonnull
        HdfsDataSource.DatasetLocation value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field datasetLocation of com.linkedin.feathr.featureDataModel.HdfsDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "datasetLocation", value.data());
            _datasetLocationField = value;
        }
        return this;
    }

    /**
     * Existence checker for timeField
     * 
     * @see HdfsDataSource.Fields#timeField
     */
    public boolean hasTimeField() {
        if (_timeFieldField!= null) {
            return true;
        }
        return super._map.containsKey("timeField");
    }

    /**
     * Remover for timeField
     * 
     * @see HdfsDataSource.Fields#timeField
     */
    public void removeTimeField() {
        super._map.remove("timeField");
    }

    /**
     * Getter for timeField
     * 
     * @see HdfsDataSource.Fields#timeField
     */
    public TimeField getTimeField(GetMode mode) {
        return getTimeField();
    }

    /**
     * Getter for timeField
     * 
     * @return
     *     Optional field. Always check for null.
     * @see HdfsDataSource.Fields#timeField
     */
    @Nullable
    public TimeField getTimeField() {
        if (_timeFieldField!= null) {
            return _timeFieldField;
        } else {
            Object __rawValue = super._map.get("timeField");
            _timeFieldField = ((__rawValue == null)?null:new TimeField(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _timeFieldField;
        }
    }

    /**
     * Setter for timeField
     * 
     * @see HdfsDataSource.Fields#timeField
     */
    public HdfsDataSource setTimeField(TimeField value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTimeField(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTimeField();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "timeField", value.data());
                    _timeFieldField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "timeField", value.data());
                    _timeFieldField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for timeField
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HdfsDataSource.Fields#timeField
     */
    public HdfsDataSource setTimeField(
        @Nonnull
        TimeField value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field timeField of com.linkedin.feathr.featureDataModel.HdfsDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "timeField", value.data());
            _timeFieldField = value;
        }
        return this;
    }

    /**
     * Existence checker for datasetSnapshotTimeFormat
     * 
     * @see HdfsDataSource.Fields#datasetSnapshotTimeFormat
     */
    public boolean hasDatasetSnapshotTimeFormat() {
        if (_datasetSnapshotTimeFormatField!= null) {
            return true;
        }
        return super._map.containsKey("datasetSnapshotTimeFormat");
    }

    /**
     * Remover for datasetSnapshotTimeFormat
     * 
     * @see HdfsDataSource.Fields#datasetSnapshotTimeFormat
     */
    public void removeDatasetSnapshotTimeFormat() {
        super._map.remove("datasetSnapshotTimeFormat");
    }

    /**
     * Getter for datasetSnapshotTimeFormat
     * 
     * @see HdfsDataSource.Fields#datasetSnapshotTimeFormat
     */
    public DatasetSnapshotTimeFormat getDatasetSnapshotTimeFormat(GetMode mode) {
        return getDatasetSnapshotTimeFormat();
    }

    /**
     * Getter for datasetSnapshotTimeFormat
     * 
     * @return
     *     Optional field. Always check for null.
     * @see HdfsDataSource.Fields#datasetSnapshotTimeFormat
     */
    @Nullable
    public DatasetSnapshotTimeFormat getDatasetSnapshotTimeFormat() {
        if (_datasetSnapshotTimeFormatField!= null) {
            return _datasetSnapshotTimeFormatField;
        } else {
            Object __rawValue = super._map.get("datasetSnapshotTimeFormat");
            _datasetSnapshotTimeFormatField = ((__rawValue == null)?null:new DatasetSnapshotTimeFormat(__rawValue));
            return _datasetSnapshotTimeFormatField;
        }
    }

    /**
     * Setter for datasetSnapshotTimeFormat
     * 
     * @see HdfsDataSource.Fields#datasetSnapshotTimeFormat
     */
    public HdfsDataSource setDatasetSnapshotTimeFormat(DatasetSnapshotTimeFormat value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDatasetSnapshotTimeFormat(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDatasetSnapshotTimeFormat();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "datasetSnapshotTimeFormat", value.data());
                    _datasetSnapshotTimeFormatField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "datasetSnapshotTimeFormat", value.data());
                    _datasetSnapshotTimeFormatField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for datasetSnapshotTimeFormat
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HdfsDataSource.Fields#datasetSnapshotTimeFormat
     */
    public HdfsDataSource setDatasetSnapshotTimeFormat(
        @Nonnull
        DatasetSnapshotTimeFormat value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field datasetSnapshotTimeFormat of com.linkedin.feathr.featureDataModel.HdfsDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "datasetSnapshotTimeFormat", value.data());
            _datasetSnapshotTimeFormatField = value;
        }
        return this;
    }

    @Override
    public HdfsDataSource clone()
        throws CloneNotSupportedException
    {
        HdfsDataSource __clone = ((HdfsDataSource) super.clone());
        __clone.__changeListener = new HdfsDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public HdfsDataSource copy()
        throws CloneNotSupportedException
    {
        HdfsDataSource __copy = ((HdfsDataSource) super.copy());
        __copy._datasetLocationField = null;
        __copy._datasetSnapshotTimeFormatField = null;
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy._timeFieldField = null;
        __copy.__changeListener = new HdfsDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final HdfsDataSource __objectRef;

        private ChangeListener(HdfsDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "datasetLocation":
                    __objectRef._datasetLocationField = null;
                    break;
                case "datasetSnapshotTimeFormat":
                    __objectRef._datasetSnapshotTimeFormatField = null;
                    break;
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
                    break;
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
                    break;
                case "timeField":
                    __objectRef._timeFieldField = null;
                    break;
            }
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/HdfsDataSource.pdl.")
    public static class DatasetLocation
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**A location on HDFS.*/record HdfsLocation{/**Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/*/path:string}}{namespace com.linkedin.feathr.featureDataModel/**The location of a Dali dataset or view. See [go/dali](http://go/dali).*/record DaliLocation{/**The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.*/uri:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.HdfsLocation _hdfsLocationMember = null;
        private com.linkedin.feathr.featureDataModel.DaliLocation _daliLocationMember = null;
        private HdfsDataSource.DatasetLocation.ChangeListener __changeListener = new HdfsDataSource.DatasetLocation.ChangeListener(this);
        private final static DataSchema MEMBER_HdfsLocation = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.HdfsLocation");
        private final static DataSchema MEMBER_DaliLocation = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.DaliLocation");

        public DatasetLocation() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public DatasetLocation(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static HdfsDataSource.DatasetLocation create(com.linkedin.feathr.featureDataModel.HdfsLocation value) {
            HdfsDataSource.DatasetLocation newUnion = new HdfsDataSource.DatasetLocation();
            newUnion.setHdfsLocation(value);
            return newUnion;
        }

        public boolean isHdfsLocation() {
            return memberIs("com.linkedin.feathr.featureDataModel.HdfsLocation");
        }

        public com.linkedin.feathr.featureDataModel.HdfsLocation getHdfsLocation() {
            checkNotNull();
            if (_hdfsLocationMember!= null) {
                return _hdfsLocationMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.HdfsLocation");
            _hdfsLocationMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.HdfsLocation(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _hdfsLocationMember;
        }

        public void setHdfsLocation(com.linkedin.feathr.featureDataModel.HdfsLocation value) {
            checkNotNull();
            super._map.clear();
            _hdfsLocationMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.HdfsLocation", value.data());
        }

        public static HdfsDataSource.DatasetLocation create(com.linkedin.feathr.featureDataModel.DaliLocation value) {
            HdfsDataSource.DatasetLocation newUnion = new HdfsDataSource.DatasetLocation();
            newUnion.setDaliLocation(value);
            return newUnion;
        }

        public boolean isDaliLocation() {
            return memberIs("com.linkedin.feathr.featureDataModel.DaliLocation");
        }

        public com.linkedin.feathr.featureDataModel.DaliLocation getDaliLocation() {
            checkNotNull();
            if (_daliLocationMember!= null) {
                return _daliLocationMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.DaliLocation");
            _daliLocationMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.DaliLocation(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _daliLocationMember;
        }

        public void setDaliLocation(com.linkedin.feathr.featureDataModel.DaliLocation value) {
            checkNotNull();
            super._map.clear();
            _daliLocationMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.DaliLocation", value.data());
        }

        public static HdfsDataSource.DatasetLocation.ProjectionMask createMask() {
            return new HdfsDataSource.DatasetLocation.ProjectionMask();
        }

        @Override
        public HdfsDataSource.DatasetLocation clone()
            throws CloneNotSupportedException
        {
            HdfsDataSource.DatasetLocation __clone = ((HdfsDataSource.DatasetLocation) super.clone());
            __clone.__changeListener = new HdfsDataSource.DatasetLocation.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public HdfsDataSource.DatasetLocation copy()
            throws CloneNotSupportedException
        {
            HdfsDataSource.DatasetLocation __copy = ((HdfsDataSource.DatasetLocation) super.copy());
            __copy._hdfsLocationMember = null;
            __copy._daliLocationMember = null;
            __copy.__changeListener = new HdfsDataSource.DatasetLocation.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final HdfsDataSource.DatasetLocation __objectRef;

            private ChangeListener(HdfsDataSource.DatasetLocation reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.HdfsLocation":
                        __objectRef._hdfsLocationMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.DaliLocation":
                        __objectRef._daliLocationMember = null;
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

            public com.linkedin.feathr.featureDataModel.HdfsLocation.Fields HdfsLocation() {
                return new com.linkedin.feathr.featureDataModel.HdfsLocation.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.HdfsLocation");
            }

            public com.linkedin.feathr.featureDataModel.DaliLocation.Fields DaliLocation() {
                return new com.linkedin.feathr.featureDataModel.DaliLocation.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.DaliLocation");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.HdfsLocation.ProjectionMask _HdfsLocationMask;
            private com.linkedin.feathr.featureDataModel.DaliLocation.ProjectionMask _DaliLocationMask;

            ProjectionMask() {
                super(3);
            }

            public HdfsDataSource.DatasetLocation.ProjectionMask withHdfsLocation(Function<com.linkedin.feathr.featureDataModel.HdfsLocation.ProjectionMask, com.linkedin.feathr.featureDataModel.HdfsLocation.ProjectionMask> nestedMask) {
                _HdfsLocationMask = nestedMask.apply(((_HdfsLocationMask == null)?com.linkedin.feathr.featureDataModel.HdfsLocation.createMask():_HdfsLocationMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.HdfsLocation", _HdfsLocationMask.getDataMap());
                return this;
            }

            public HdfsDataSource.DatasetLocation.ProjectionMask withDaliLocation(Function<com.linkedin.feathr.featureDataModel.DaliLocation.ProjectionMask, com.linkedin.feathr.featureDataModel.DaliLocation.ProjectionMask> nestedMask) {
                _DaliLocationMask = nestedMask.apply(((_DaliLocationMask == null)?com.linkedin.feathr.featureDataModel.DaliLocation.createMask():_DaliLocationMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.DaliLocation", _DaliLocationMask.getDataMap());
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
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.Fields keyFunction() {
            return new com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.Fields(getPathComponents(), "keyFunction");
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public PathSpec dataSourceRef() {
            return new PathSpec(getPathComponents(), "dataSourceRef");
        }

        /**
         * Location of the dataset, it can be a HDFS path of the dataset, or Dali URI (dalids:///)
         * 
         */
        public com.linkedin.feathr.featureDataModel.HdfsDataSource.DatasetLocation.Fields datasetLocation() {
            return new com.linkedin.feathr.featureDataModel.HdfsDataSource.DatasetLocation.Fields(getPathComponents(), "datasetLocation");
        }

        /**
         * Specify the time field in the dataset schema. This is an optional field used for time-series operations, such as Sliding Window Aggregation. See details in SlidingWindowAggregation section in go/feathroffline.
         * 
         */
        public com.linkedin.feathr.featureDataModel.TimeField.Fields timeField() {
            return new com.linkedin.feathr.featureDataModel.TimeField.Fields(getPathComponents(), "timeField");
        }

        /**
         * Represents the time format being used to parse snapshot time from a dataset's file path. See DatasetSnapshotTimeFormat for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat.Fields datasetSnapshotTimeFormat() {
            return new com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat.Fields(getPathComponents(), "datasetSnapshotTimeFormat");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;
        private com.linkedin.feathr.featureDataModel.HdfsDataSource.DatasetLocation.ProjectionMask _datasetLocationMask;
        private com.linkedin.feathr.featureDataModel.TimeField.ProjectionMask _timeFieldMask;
        private com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat.ProjectionMask _datasetSnapshotTimeFormatMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public HdfsDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public HdfsDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public HdfsDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Location of the dataset, it can be a HDFS path of the dataset, or Dali URI (dalids:///)
         * 
         */
        public HdfsDataSource.ProjectionMask withDatasetLocation(Function<com.linkedin.feathr.featureDataModel.HdfsDataSource.DatasetLocation.ProjectionMask, com.linkedin.feathr.featureDataModel.HdfsDataSource.DatasetLocation.ProjectionMask> nestedMask) {
            _datasetLocationMask = nestedMask.apply(((_datasetLocationMask == null)?HdfsDataSource.DatasetLocation.createMask():_datasetLocationMask));
            getDataMap().put("datasetLocation", _datasetLocationMask.getDataMap());
            return this;
        }

        /**
         * Location of the dataset, it can be a HDFS path of the dataset, or Dali URI (dalids:///)
         * 
         */
        public HdfsDataSource.ProjectionMask withDatasetLocation() {
            _datasetLocationMask = null;
            getDataMap().put("datasetLocation", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Specify the time field in the dataset schema. This is an optional field used for time-series operations, such as Sliding Window Aggregation. See details in SlidingWindowAggregation section in go/feathroffline.
         * 
         */
        public HdfsDataSource.ProjectionMask withTimeField(Function<com.linkedin.feathr.featureDataModel.TimeField.ProjectionMask, com.linkedin.feathr.featureDataModel.TimeField.ProjectionMask> nestedMask) {
            _timeFieldMask = nestedMask.apply(((_timeFieldMask == null)?TimeField.createMask():_timeFieldMask));
            getDataMap().put("timeField", _timeFieldMask.getDataMap());
            return this;
        }

        /**
         * Specify the time field in the dataset schema. This is an optional field used for time-series operations, such as Sliding Window Aggregation. See details in SlidingWindowAggregation section in go/feathroffline.
         * 
         */
        public HdfsDataSource.ProjectionMask withTimeField() {
            _timeFieldMask = null;
            getDataMap().put("timeField", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the time format being used to parse snapshot time from a dataset's file path. See DatasetSnapshotTimeFormat for more details.
         * 
         */
        public HdfsDataSource.ProjectionMask withDatasetSnapshotTimeFormat(Function<com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat.ProjectionMask, com.linkedin.feathr.featureDataModel.DatasetSnapshotTimeFormat.ProjectionMask> nestedMask) {
            _datasetSnapshotTimeFormatMask = nestedMask.apply(((_datasetSnapshotTimeFormatMask == null)?DatasetSnapshotTimeFormat.createMask():_datasetSnapshotTimeFormatMask));
            getDataMap().put("datasetSnapshotTimeFormat", _datasetSnapshotTimeFormatMask.getDataMap());
            return this;
        }

        /**
         * Represents the time format being used to parse snapshot time from a dataset's file path. See DatasetSnapshotTimeFormat for more details.
         * 
         */
        public HdfsDataSource.ProjectionMask withDatasetSnapshotTimeFormat() {
            _datasetSnapshotTimeFormatMask = null;
            getDataMap().put("datasetSnapshotTimeFormat", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
