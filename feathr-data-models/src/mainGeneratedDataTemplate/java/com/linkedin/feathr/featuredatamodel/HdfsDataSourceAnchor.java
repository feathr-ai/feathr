
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import com.linkedin.data.DataList;
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
 * Represents an offline anchor with HDFS data source.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/HdfsDataSourceAnchor.pdl.")
public class HdfsDataSourceAnchor
    extends RecordTemplate
{

    private final static HdfsDataSourceAnchor.Fields _fields = new HdfsDataSourceAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents an offline anchor with HDFS data source.*/record HdfsDataSourceAnchor includes/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines a HDFS data source. See HdfsDataSource for more details.*/source:/**Represents a HDFS data source.*/record HdfsDataSource includes/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineDataSourceKey{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Location of the dataset, it can be a HDFS path of the dataset, or Dali URI (dalids:///)*/datasetLocation:union[/**A location on HDFS.*/record HdfsLocation{/**Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/*/path:string}/**The location of a Dali dataset or view. See [go/dali](http://go/dali).*/record DaliLocation{/**The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.*/uri:{namespace com.linkedin.frame.common@java={\"class\":\"java.net.URI\",\"coercerClass\":\"com.linkedin.frame.common.coercer.UriCoercer\"}typeref Uri=string}}]/**Specify the time field in the dataset schema. This is an optional field used for time-series operations, such as Sliding Window Aggregation. See details in SlidingWindowAggregation section in go/feathroffline.*/timeField:optional/**Represent a time field in the dataset schema, it is used for time-series operations. One usage is Sliding Window Aggregation, see details in go/frameoffline.*/record TimeField{/**Name of the time field in the dataset schema.*/name:string/**The format that the time field uses to represent time.*/format:/**The format of the time field. It can be either the granularity of a timestamp or datetime format e.g. yyyy/MM/dd/HH/mm/ss.*/typeref TimeFieldFormat=union[/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string]}/**Represents the time format being used to parse snapshot time from a dataset's file path. See DatasetSnapshotTimeFormat for more details.*/datasetSnapshotTimeFormat:optional/**Represents the time format being used to parse snapshot time from a dataset's file path, for example \"2020/09/30\" in /hdfs/jobs/lyndarel/features/master/members/daily/2020/09/30/. The dataset snapshot time is useful when feature data is time-partitioned for time-aware join or sliding-window aggregation. An example feature is derived skills, because derived skills change often based on user activities, modeling engineers want to join the time-sensitive observation data with the closest version of the feature. Refer to Unification of SWA and time-based features RFC (https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#) for more details.*/typeref DatasetSnapshotTimeFormat=union[DateTimeFormat/**Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of \"Unification of SWA and time-based features\" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).*/record StandardDateTimeFormat{}]}/**Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF, SWA) to produce feature value from a hfds data source.*/transformationFunction:union[MvelExpression,UserDefinedFunction,SparkSqlExpression/**Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and compute the aggregagate value.*/record SlidingWindowAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types of aggregation.*/aggregationType:enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG}/**Represents the time window to look back from label data's timestamp.*/window:/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.*/lateralViews:array[/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[SparkSqlExpression]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}]=[]/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the max number of groups (with aggregation results) to return.*/limit:optional int}/**Sliding window embedding aggregation produces a single embedding by performing element-wise operations or discretization on a collection of embeddings within a given time interval. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and produce the aggregagated embedding.*/record SlidingWindowEmbeddingAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types for embedding aggregation.*/aggregationType:enum EmbeddingAggregationType{/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. */MAX_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. */MIN_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. */AVG_POOLING}/**Represents the time window to look back from label data's timestamp.*/window:Window/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]}/**This sliding window algorithm picks the latest available feature data from the source data. Note the latest here means event time instead of processing time.*/record SlidingWindowLatestAvailable{/**The target column to pick the latest available record from.*/targetColumn:union[SparkSqlExpression]/**Represents the time window to look back from label data's timestamp.*/window:optional Window/**Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.*/lateralViews:array[LateralView]=[]/**Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the filter statement before applying the sliding window algorithm.*/filter:optional union[SparkSqlExpression]/**Represents the max number of groups (with latest available feature data) to return.*/limit:optional int}]}", SchemaFormatType.PDL));
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private HdfsDataSource _sourceField = null;
    private HdfsDataSourceAnchor.TransformationFunction _transformationFunctionField = null;
    private HdfsDataSourceAnchor.ChangeListener __changeListener = new HdfsDataSourceAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_Source = SCHEMA.getField("source");
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public HdfsDataSourceAnchor() {
        super(new DataMap(4, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public HdfsDataSourceAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static HdfsDataSourceAnchor.Fields fields() {
        return _fields;
    }

    public static HdfsDataSourceAnchor.ProjectionMask createMask() {
        return new HdfsDataSourceAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see HdfsDataSourceAnchor.Fields#keyPlaceholders
     */
    public boolean hasKeyPlaceholders() {
        if (_keyPlaceholdersField!= null) {
            return true;
        }
        return super._map.containsKey("keyPlaceholders");
    }

    /**
     * Remover for keyPlaceholders
     * 
     * @see HdfsDataSourceAnchor.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see HdfsDataSourceAnchor.Fields#keyPlaceholders
     */
    public KeyPlaceholderArray getKeyPlaceholders(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getKeyPlaceholders();
            case NULL:
                if (_keyPlaceholdersField!= null) {
                    return _keyPlaceholdersField;
                } else {
                    Object __rawValue = super._map.get("keyPlaceholders");
                    _keyPlaceholdersField = ((__rawValue == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keyPlaceholdersField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see HdfsDataSourceAnchor.Fields#keyPlaceholders
     */
    @Nonnull
    public KeyPlaceholderArray getKeyPlaceholders() {
        if (_keyPlaceholdersField!= null) {
            return _keyPlaceholdersField;
        } else {
            Object __rawValue = super._map.get("keyPlaceholders");
            if (__rawValue == null) {
                return DEFAULT_KeyPlaceholders;
            }
            _keyPlaceholdersField = ((__rawValue == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keyPlaceholdersField;
        }
    }

    /**
     * Setter for keyPlaceholders
     * 
     * @see HdfsDataSourceAnchor.Fields#keyPlaceholders
     */
    public HdfsDataSourceAnchor setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
                    _keyPlaceholdersField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyPlaceholders();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
                    _keyPlaceholdersField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
                    _keyPlaceholdersField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyPlaceholders
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HdfsDataSourceAnchor.Fields#keyPlaceholders
     */
    public HdfsDataSourceAnchor setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for source
     * 
     * @see HdfsDataSourceAnchor.Fields#source
     */
    public boolean hasSource() {
        if (_sourceField!= null) {
            return true;
        }
        return super._map.containsKey("source");
    }

    /**
     * Remover for source
     * 
     * @see HdfsDataSourceAnchor.Fields#source
     */
    public void removeSource() {
        super._map.remove("source");
    }

    /**
     * Getter for source
     * 
     * @see HdfsDataSourceAnchor.Fields#source
     */
    public HdfsDataSource getSource(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSource();
            case DEFAULT:
            case NULL:
                if (_sourceField!= null) {
                    return _sourceField;
                } else {
                    Object __rawValue = super._map.get("source");
                    _sourceField = ((__rawValue == null)?null:new HdfsDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _sourceField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for source
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see HdfsDataSourceAnchor.Fields#source
     */
    @Nonnull
    public HdfsDataSource getSource() {
        if (_sourceField!= null) {
            return _sourceField;
        } else {
            Object __rawValue = super._map.get("source");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("source");
            }
            _sourceField = ((__rawValue == null)?null:new HdfsDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sourceField;
        }
    }

    /**
     * Setter for source
     * 
     * @see HdfsDataSourceAnchor.Fields#source
     */
    public HdfsDataSourceAnchor setSource(HdfsDataSource value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSource(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field source of com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "source", value.data());
                    _sourceField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeSource();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "source", value.data());
                    _sourceField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "source", value.data());
                    _sourceField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for source
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HdfsDataSourceAnchor.Fields#source
     */
    public HdfsDataSourceAnchor setSource(
        @Nonnull
        HdfsDataSource value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field source of com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "source", value.data());
            _sourceField = value;
        }
        return this;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see HdfsDataSourceAnchor.Fields#transformationFunction
     */
    public boolean hasTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("transformationFunction");
    }

    /**
     * Remover for transformationFunction
     * 
     * @see HdfsDataSourceAnchor.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see HdfsDataSourceAnchor.Fields#transformationFunction
     */
    public HdfsDataSourceAnchor.TransformationFunction getTransformationFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTransformationFunction();
            case DEFAULT:
            case NULL:
                if (_transformationFunctionField!= null) {
                    return _transformationFunctionField;
                } else {
                    Object __rawValue = super._map.get("transformationFunction");
                    _transformationFunctionField = ((__rawValue == null)?null:new HdfsDataSourceAnchor.TransformationFunction(__rawValue));
                    return _transformationFunctionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for transformationFunction
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see HdfsDataSourceAnchor.Fields#transformationFunction
     */
    @Nonnull
    public HdfsDataSourceAnchor.TransformationFunction getTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return _transformationFunctionField;
        } else {
            Object __rawValue = super._map.get("transformationFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("transformationFunction");
            }
            _transformationFunctionField = ((__rawValue == null)?null:new HdfsDataSourceAnchor.TransformationFunction(__rawValue));
            return _transformationFunctionField;
        }
    }

    /**
     * Setter for transformationFunction
     * 
     * @see HdfsDataSourceAnchor.Fields#transformationFunction
     */
    public HdfsDataSourceAnchor setTransformationFunction(HdfsDataSourceAnchor.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
                    _transformationFunctionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTransformationFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
                    _transformationFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
                    _transformationFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for transformationFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HdfsDataSourceAnchor.Fields#transformationFunction
     */
    public HdfsDataSourceAnchor setTransformationFunction(
        @Nonnull
        HdfsDataSourceAnchor.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    @Override
    public HdfsDataSourceAnchor clone()
        throws CloneNotSupportedException
    {
        HdfsDataSourceAnchor __clone = ((HdfsDataSourceAnchor) super.clone());
        __clone.__changeListener = new HdfsDataSourceAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public HdfsDataSourceAnchor copy()
        throws CloneNotSupportedException
    {
        HdfsDataSourceAnchor __copy = ((HdfsDataSourceAnchor) super.copy());
        __copy._transformationFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy._sourceField = null;
        __copy.__changeListener = new HdfsDataSourceAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final HdfsDataSourceAnchor __objectRef;

        private ChangeListener(HdfsDataSourceAnchor reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "transformationFunction":
                    __objectRef._transformationFunctionField = null;
                    break;
                case "keyPlaceholders":
                    __objectRef._keyPlaceholdersField = null;
                    break;
                case "source":
                    __objectRef._sourceField = null;
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
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.Fields keyPlaceholders() {
            return new com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.Fields(getPathComponents(), "keyPlaceholders");
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public PathSpec keyPlaceholders(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "keyPlaceholders");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Defines a HDFS data source. See HdfsDataSource for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.HdfsDataSource.Fields source() {
            return new com.linkedin.feathr.featureDataModel.HdfsDataSource.Fields(getPathComponents(), "source");
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF, SWA) to produce feature value from a hfds data source.
         * 
         */
        public com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction.Fields transformationFunction() {
            return new com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction.Fields(getPathComponents(), "transformationFunction");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.HdfsDataSource.ProjectionMask _sourceMask;
        private com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction.ProjectionMask _transformationFunctionMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$count", count);
            }
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyPlaceholders").put("$count", count);
            }
            return this;
        }

        /**
         * Defines a HDFS data source. See HdfsDataSource for more details.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.HdfsDataSource.ProjectionMask, com.linkedin.feathr.featureDataModel.HdfsDataSource.ProjectionMask> nestedMask) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?HdfsDataSource.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            return this;
        }

        /**
         * Defines a HDFS data source. See HdfsDataSource for more details.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withSource() {
            _sourceMask = null;
            getDataMap().put("source", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF, SWA) to produce feature value from a hfds data source.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?HdfsDataSourceAnchor.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF, SWA) to produce feature value from a hfds data source.
         * 
         */
        public HdfsDataSourceAnchor.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/HdfsDataSourceAnchor.pdl.")
    public static class TransformationFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}}{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}{namespace com.linkedin.feathr.featureDataModel/**Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and compute the aggregagate value.*/record SlidingWindowAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types of aggregation.*/aggregationType:enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG}/**Represents the time window to look back from label data's timestamp.*/window:/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.*/lateralViews:array[/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[SparkSqlExpression]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}]=[]/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the max number of groups (with aggregation results) to return.*/limit:optional int}}{namespace com.linkedin.feathr.featureDataModel/**Sliding window embedding aggregation produces a single embedding by performing element-wise operations or discretization on a collection of embeddings within a given time interval. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and produce the aggregagated embedding.*/record SlidingWindowEmbeddingAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types for embedding aggregation.*/aggregationType:enum EmbeddingAggregationType{/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. */MAX_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. */MIN_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. */AVG_POOLING}/**Represents the time window to look back from label data's timestamp.*/window:Window/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]}}{namespace com.linkedin.feathr.featureDataModel/**This sliding window algorithm picks the latest available feature data from the source data. Note the latest here means event time instead of processing time.*/record SlidingWindowLatestAvailable{/**The target column to pick the latest available record from.*/targetColumn:union[SparkSqlExpression]/**Represents the time window to look back from label data's timestamp.*/window:optional Window/**Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.*/lateralViews:array[LateralView]=[]/**Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the filter statement before applying the sliding window algorithm.*/filter:optional union[SparkSqlExpression]/**Represents the max number of groups (with latest available feature data) to return.*/limit:optional int}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.UserDefinedFunction _userDefinedFunctionMember = null;
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.SlidingWindowAggregation _slidingWindowAggregationMember = null;
        private com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation _slidingWindowEmbeddingAggregationMember = null;
        private com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable _slidingWindowLatestAvailableMember = null;
        private HdfsDataSourceAnchor.TransformationFunction.ChangeListener __changeListener = new HdfsDataSourceAnchor.TransformationFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
        private final static DataSchema MEMBER_UserDefinedFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.UserDefinedFunction");
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        private final static DataSchema MEMBER_SlidingWindowAggregation = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
        private final static DataSchema MEMBER_SlidingWindowEmbeddingAggregation = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation");
        private final static DataSchema MEMBER_SlidingWindowLatestAvailable = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable");

        public TransformationFunction() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public TransformationFunction(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static HdfsDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            HdfsDataSourceAnchor.TransformationFunction newUnion = new HdfsDataSourceAnchor.TransformationFunction();
            newUnion.setMvelExpression(value);
            return newUnion;
        }

        public boolean isMvelExpression() {
            return memberIs("com.linkedin.feathr.featureDataModel.MvelExpression");
        }

        public com.linkedin.feathr.featureDataModel.MvelExpression getMvelExpression() {
            checkNotNull();
            if (_mvelExpressionMember!= null) {
                return _mvelExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.MvelExpression");
            _mvelExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.MvelExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _mvelExpressionMember;
        }

        public void setMvelExpression(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            checkNotNull();
            super._map.clear();
            _mvelExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.MvelExpression", value.data());
        }

        public static HdfsDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            HdfsDataSourceAnchor.TransformationFunction newUnion = new HdfsDataSourceAnchor.TransformationFunction();
            newUnion.setUserDefinedFunction(value);
            return newUnion;
        }

        public boolean isUserDefinedFunction() {
            return memberIs("com.linkedin.feathr.featureDataModel.UserDefinedFunction");
        }

        public com.linkedin.feathr.featureDataModel.UserDefinedFunction getUserDefinedFunction() {
            checkNotNull();
            if (_userDefinedFunctionMember!= null) {
                return _userDefinedFunctionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.UserDefinedFunction");
            _userDefinedFunctionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.UserDefinedFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _userDefinedFunctionMember;
        }

        public void setUserDefinedFunction(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            checkNotNull();
            super._map.clear();
            _userDefinedFunctionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.UserDefinedFunction", value.data());
        }

        public static HdfsDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            HdfsDataSourceAnchor.TransformationFunction newUnion = new HdfsDataSourceAnchor.TransformationFunction();
            newUnion.setSparkSqlExpression(value);
            return newUnion;
        }

        public boolean isSparkSqlExpression() {
            return memberIs("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        }

        public com.linkedin.feathr.featureDataModel.SparkSqlExpression getSparkSqlExpression() {
            checkNotNull();
            if (_sparkSqlExpressionMember!= null) {
                return _sparkSqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            _sparkSqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SparkSqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sparkSqlExpressionMember;
        }

        public void setSparkSqlExpression(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sparkSqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SparkSqlExpression", value.data());
        }

        public static HdfsDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.SlidingWindowAggregation value) {
            HdfsDataSourceAnchor.TransformationFunction newUnion = new HdfsDataSourceAnchor.TransformationFunction();
            newUnion.setSlidingWindowAggregation(value);
            return newUnion;
        }

        public boolean isSlidingWindowAggregation() {
            return memberIs("com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
        }

        public com.linkedin.feathr.featureDataModel.SlidingWindowAggregation getSlidingWindowAggregation() {
            checkNotNull();
            if (_slidingWindowAggregationMember!= null) {
                return _slidingWindowAggregationMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
            _slidingWindowAggregationMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SlidingWindowAggregation(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _slidingWindowAggregationMember;
        }

        public void setSlidingWindowAggregation(com.linkedin.feathr.featureDataModel.SlidingWindowAggregation value) {
            checkNotNull();
            super._map.clear();
            _slidingWindowAggregationMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SlidingWindowAggregation", value.data());
        }

        public static HdfsDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation value) {
            HdfsDataSourceAnchor.TransformationFunction newUnion = new HdfsDataSourceAnchor.TransformationFunction();
            newUnion.setSlidingWindowEmbeddingAggregation(value);
            return newUnion;
        }

        public boolean isSlidingWindowEmbeddingAggregation() {
            return memberIs("com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation");
        }

        public com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation getSlidingWindowEmbeddingAggregation() {
            checkNotNull();
            if (_slidingWindowEmbeddingAggregationMember!= null) {
                return _slidingWindowEmbeddingAggregationMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation");
            _slidingWindowEmbeddingAggregationMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _slidingWindowEmbeddingAggregationMember;
        }

        public void setSlidingWindowEmbeddingAggregation(com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation value) {
            checkNotNull();
            super._map.clear();
            _slidingWindowEmbeddingAggregationMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation", value.data());
        }

        public static HdfsDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable value) {
            HdfsDataSourceAnchor.TransformationFunction newUnion = new HdfsDataSourceAnchor.TransformationFunction();
            newUnion.setSlidingWindowLatestAvailable(value);
            return newUnion;
        }

        public boolean isSlidingWindowLatestAvailable() {
            return memberIs("com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable");
        }

        public com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable getSlidingWindowLatestAvailable() {
            checkNotNull();
            if (_slidingWindowLatestAvailableMember!= null) {
                return _slidingWindowLatestAvailableMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable");
            _slidingWindowLatestAvailableMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _slidingWindowLatestAvailableMember;
        }

        public void setSlidingWindowLatestAvailable(com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable value) {
            checkNotNull();
            super._map.clear();
            _slidingWindowLatestAvailableMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable", value.data());
        }

        public static HdfsDataSourceAnchor.TransformationFunction.ProjectionMask createMask() {
            return new HdfsDataSourceAnchor.TransformationFunction.ProjectionMask();
        }

        @Override
        public HdfsDataSourceAnchor.TransformationFunction clone()
            throws CloneNotSupportedException
        {
            HdfsDataSourceAnchor.TransformationFunction __clone = ((HdfsDataSourceAnchor.TransformationFunction) super.clone());
            __clone.__changeListener = new HdfsDataSourceAnchor.TransformationFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public HdfsDataSourceAnchor.TransformationFunction copy()
            throws CloneNotSupportedException
        {
            HdfsDataSourceAnchor.TransformationFunction __copy = ((HdfsDataSourceAnchor.TransformationFunction) super.copy());
            __copy._slidingWindowAggregationMember = null;
            __copy._slidingWindowEmbeddingAggregationMember = null;
            __copy._mvelExpressionMember = null;
            __copy._sparkSqlExpressionMember = null;
            __copy._slidingWindowLatestAvailableMember = null;
            __copy._userDefinedFunctionMember = null;
            __copy.__changeListener = new HdfsDataSourceAnchor.TransformationFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final HdfsDataSourceAnchor.TransformationFunction __objectRef;

            private ChangeListener(HdfsDataSourceAnchor.TransformationFunction reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.SlidingWindowAggregation":
                        __objectRef._slidingWindowAggregationMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation":
                        __objectRef._slidingWindowEmbeddingAggregationMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.MvelExpression":
                        __objectRef._mvelExpressionMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.SparkSqlExpression":
                        __objectRef._sparkSqlExpressionMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable":
                        __objectRef._slidingWindowLatestAvailableMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.UserDefinedFunction":
                        __objectRef._userDefinedFunctionMember = null;
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

            public com.linkedin.feathr.featureDataModel.MvelExpression.Fields MvelExpression() {
                return new com.linkedin.feathr.featureDataModel.MvelExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.MvelExpression");
            }

            public com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields UserDefinedFunction() {
                return new com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.UserDefinedFunction");
            }

            public com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            }

            public com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Fields SlidingWindowAggregation() {
                return new com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
            }

            public com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Fields SlidingWindowEmbeddingAggregation() {
                return new com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation");
            }

            public com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Fields SlidingWindowLatestAvailable() {
                return new com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _MvelExpressionMask;
            private com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask _UserDefinedFunctionMask;
            private com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;
            private com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.ProjectionMask _SlidingWindowAggregationMask;
            private com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.ProjectionMask _SlidingWindowEmbeddingAggregationMask;
            private com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.ProjectionMask _SlidingWindowLatestAvailableMask;

            ProjectionMask() {
                super(8);
            }

            public HdfsDataSourceAnchor.TransformationFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public HdfsDataSourceAnchor.TransformationFunction.ProjectionMask withUserDefinedFunction(Function<com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask> nestedMask) {
                _UserDefinedFunctionMask = nestedMask.apply(((_UserDefinedFunctionMask == null)?com.linkedin.feathr.featureDataModel.UserDefinedFunction.createMask():_UserDefinedFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UserDefinedFunction", _UserDefinedFunctionMask.getDataMap());
                return this;
            }

            public HdfsDataSourceAnchor.TransformationFunction.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

            public HdfsDataSourceAnchor.TransformationFunction.ProjectionMask withSlidingWindowAggregation(Function<com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.ProjectionMask> nestedMask) {
                _SlidingWindowAggregationMask = nestedMask.apply(((_SlidingWindowAggregationMask == null)?com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.createMask():_SlidingWindowAggregationMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SlidingWindowAggregation", _SlidingWindowAggregationMask.getDataMap());
                return this;
            }

            public HdfsDataSourceAnchor.TransformationFunction.ProjectionMask withSlidingWindowEmbeddingAggregation(Function<com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.ProjectionMask> nestedMask) {
                _SlidingWindowEmbeddingAggregationMask = nestedMask.apply(((_SlidingWindowEmbeddingAggregationMask == null)?com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.createMask():_SlidingWindowEmbeddingAggregationMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation", _SlidingWindowEmbeddingAggregationMask.getDataMap());
                return this;
            }

            public HdfsDataSourceAnchor.TransformationFunction.ProjectionMask withSlidingWindowLatestAvailable(Function<com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.ProjectionMask> nestedMask) {
                _SlidingWindowLatestAvailableMask = nestedMask.apply(((_SlidingWindowLatestAvailableMask == null)?com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.createMask():_SlidingWindowLatestAvailableMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable", _SlidingWindowLatestAvailableMask.getDataMap());
                return this;
            }

        }

    }

}
