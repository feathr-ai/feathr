
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
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
 * FeatureAnchor defines where a feature is extracted or derived from, and how a feature is transformed. A FeatureVersion can have multiple FeatureAnchors for different environments (eg. online, offline, nearline, galene). Refer to go/ai-metadata/schema for more details.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/FeatureAnchor.pdl.")
public class FeatureAnchor
    extends RecordTemplate
{

    private final static FeatureAnchor.Fields _fields = new FeatureAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema(new StringBuilder().append("namespace com.linkedin.feathr.featureDataModel/**FeatureAnchor defines where a feature is extracted or derived from, and how a feature is transformed. A FeatureVersion can have multiple FeatureAnchors for different environments (eg. online, offline, nearline, galene). Refer to go/ai-metadata/schema for more details.*/@gma.aspect.entity.urn=\"com.linkedin.common.MlFeatureAnchorUrn\"record FeatureAnchor{/**All the supported anchors. Each anchor defines which source and environment a feature is extracted or derived from and how a feature is transformed.*/anchor:union[/**Represents an online anchor with Couchbase data source.*/@OnlineAnchor,record CouchbaseDataSourceAnchor includes/**A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor. This class defines supported transformation functions (eg. MVEL, UDF) that can be used for an online data source.*/record TransformationFunctionForOnlineDataSource{/**Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines a Couchbase data source. Couchbase is a distributed key-value caching solution. See CouchbaseDataSource for more details.*/source:/**Represents a Couchbase data source. Couchbase is a distributed key-value caching solution. For more details: go/couchbase.*/record CouchbaseDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[MvelExpression]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Name of the Couchbase bucket. A bucket is a logical entity that groups documents; allowing them to be accessed, indexed, replicated, and access-controlled.*/bucketName:string/**The list of bootstrap servers for Frame to initiate communication to the Couchbase cluster.\nThis is no longer needed as Couchbase can resolve the URIs based on bucketName field.*/@deprecated=\"No longer needed as Couchbase can now resolve the URIs based on the bucketName field.\"bootstrapServers:array[{namespace com.linkedin.frame.common@java={\"class\":\"java.net.URI\",\"coercerClass\":\"com.linkedin.frame.common.coercer.UriCoercer\"}typeref Uri=string}]=[]/**Fully qualified Java class name for data model of the documents stored in the Couchbase bucket.*/documentDataModel:Clazz}}/**Represents an online anchor with custom data source which uses user-defined data fetching mechanism.*/@OnlineAnchor,record CustomDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a custom data source which uses user-defined data fetching mechanism. See CustomDataSource for more details.*/source:/**Represents a custom data source which uses user-defined data fetching mechanism. Its difference with InMemoryPassthroughDataSource is: CustomDataSource allows dependency injection via Offspring for supporting complex data fetching, whereas InMemoryPassthroughDataSource assumes data is already available in-memory along with the retrieval request. See CustomSource section in go/frameonline.*/record CustomDataSource includes OnlineDataSourceKey,DataSourceRef{/**The fully qualified Java class name of the response data model retrieved from the custom data source.*/dataModel:Clazz}}/**Represents an online anchor with Espresso data source.*/@OnlineAnchor,record EspressoDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Espresso data source. Espresso the recommended source-of-truth distributed database at LinkedIn. See EspressoDataSource for more details.*/source:/**Represents a Espresso data source. Espresso the recommended source-of-truth distributed database at LinkedIn. It is optimized for balanced read/write traffic with relative small sized data records (< 1MB). For more details: go/espresso.*/record EspressoDataSource includes OnlineDataSourceKey,DataSourceRef{/**Espresso database name.*/databaseName:string/**Espresso table name.*/tableName:string/**D2 URI of the Espresso database which can be found on go/Nuage.*/d2Uri:com.linkedin.frame.common.Uri}}/**Represents an online anchor with in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request.*/@OnlineAnchor,record InMemoryPassthroughDataSourceAnchor includes TransformationFunctionForOnlineDataSource{/**Defines an in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request. See InMemoryPassthroughDataSource for more details.*/source:/**Represents a in-memory passthrough data source. Passthrough data sources are used when the data are not from external sources such as Rest.li, Venice and Espresso. It's commonly used for contextual features that are supplied as part of an online request. See Passthrough section in go/frameonline.*/record InMemoryPassthroughDataSource includes DataSourceRef{/**The fully qualified Java class name for the data model of passthrough (in-memory) data that is supplied as part of the online request.*/dataModel:Clazz}}/**Represents an online anchor with Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource.*/@OnlineAnchor,record RestliDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource. See RestliDataSource for more details.*/source:/**Represents a Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource. Also see RestliFinderDataSource if you are looking for fetching source data by a finder method.*/record RestliDataSource includes OnlineDataSourceKey,DataSourceRef{/**The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.*/resourceName:string/**Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details. It should be required with a default value, but the optional flag can't be removed after its check-in, for complying with backward compatibility requirement of schema evolution.*/projections:optional array[/**Represents a PathSpec string. PathSpec is used to specify projections in Rest.li request in order to select a subset of object contents. For more details, refer to https://linkedin.github.io/rest.li/Projections. Some examples: 'listingType', 'member:(firstName, lastName)'.*/typeref PathSpec=string]=[]/**Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#methods,get, the key value pair keyed by viewer represents a request parameter of the Get method in jobPostings resource.*/requestParameters:map[string/**Represents the value part in a Rest.li request paramater key/value pair.*/typeref RequestParameterValue=union[MvelExpression,JsonString]]={}}}/**Represents an online anchor with Rest.li finder data source, in which source data is fetched by a Rest.li finder method.*/@OnlineAnchor,record RestliFinderDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. See RestliFinderDataSource for more details.*/source:/**Represents a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. For more details, refer to https://linkedin.github.io/rest.li/user_guide/restli_server#finder. Note: it is possible for a finder source to have an keyExpr for association resources since the key parts of the association key can be used as query parameters which are modeled as CompoundKey. See https://linkedin.github.io/rest.li/user_guide/restli_server#finder for more details*/record RestliFinderDataSource includes OnlineDataSourceKey,DataSourceRef{/**The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.*/resourceName:string/**Represents the finder method name of the resource.*/finderMethod:string/**Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.*/projections:array[PathSpec]=[]/**Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#finders,companyJobCodes, the key value pair keyed by contract represents a request parameter of the companyJobCodes finder method in jobPostings resource.*/requestParameters:map[string,RequestParameterValue]={}}}/**Represents an online anchor with Venice data source.*/@OnlineAnchor,record VeniceDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Venice data source. Venice is a data store for derived data. See VeniceDataSource for more details.*/source:/**Represents a Venice data source. Venice is a data store for derived data. All derived data accessed by primary key, whether produced offline on Hadoop or via nearline stream processing in Samza, can be served by Venice. For more details: go/venice.*/record VeniceDataSource includes OnlineDataSourceKey,DataSourceRef{/**The Venice store name.*/storeName:string}}/**Represents an online anchor with Pinot data source.*/@OnlineAnchor,record PinotDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Pinot data source. Pinot is a realtime distributed OLAP datastore. See PinotDataSource for more details.*/source:/**Represent a Pinot data source. Pinot is a realtime distributed OLAP datastore which supports data fetching through standard SQL query syntax and semantics. It is suitable for querying time series data with lots of Dimensions and Metrics. For more details on Pinot: go/pinot. Also see <a href=\"https://docs.google.com/document/d/1nx-j-JJLWY4QaU2hgoQ6H9rDEcVG9jZo6CQVRX4ATvA/edit/\">[RFC] Pinot Frame Online</a> for the details on the Pinot data source design.*/record PinotDataSource includes DataSourceRef{/**Represent the service name in the Pinot D2 config for the source Pinot table.*/resourceName:string/**Represent the sql query template to fetch data from Pinot table, with \u201c?\u201d as placeholders for run time value replacement from specified queryArguments in the same order. For example: \"SELECT objectAttributes, timeStampSec FROM RecentMemberActions WHERE actorId IN (?) AND timeStampSec > ?\".*/queryTemplate:string/**Represent mvel expressions whose values will be evaluated at runtime to replace the \"?\" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be [\"key[0]\", \"System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60\"].*/queryArguments:array[MvelExpression]/**Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be [\"actorId\"]. As in queryArguments, only \"key[0]\" is base on key values, and it is used with \"actorId\" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{\u201c123\u201d, \u201cpage_view\u201d, \u201c323\u201d}, {\u201c987\", \u201cpage_view\u201d, \u201c876\"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.*/queryKeyColumns:array[string]}}/**Represents an online anchor with Vector data source.*/record VectorDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Vector data source, in which source data is fetched by assetUrn + featureSourceName via Restli unstructured\ndata endpoint.*/source:/**Represent a Vector data source. Vector is a media asset serving service accessed via Restli unstructured data endpoint.*/record VectorDataSource includes OnlineDataSourceKey,DataSourceRef{/**This is a Vector request param needed to uniquely identify the Vector asset which a user is trying to fetch.\nFor example \"png_200_200\". This will be decided by the user at write time when the user writes an asset to Vector.\nThe type is string and not enum. The values will be decided between the team that will use the media data and Vector.*/featureSourceName:string}}/**Represents an offline anchor with HDFS data source.*/record HdfsDataSourceAnchor includes KeyPlaceholders{/**Defines a HDFS data source. See HdfsDataSource for more details.*/source:/**Represents a HDFS data source.*/record HdfsDataSource includes/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineDataSourceKey{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.*/keyFunction:optional union[MvelExpression/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}UserDefinedFunction]}DataSourceRef{/**Location of the dataset, it can be a HDFS path of the dataset, or Dali URI (dalids:///)*/datasetLocation:union[/**A location on HDFS.*/record HdfsLocation{/**Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/*/path:string}/**The location of a Dali dataset or view. See [go/dali](http://go/dali).*/record DaliLocation{/**The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.*/uri:com.linkedin.frame.common.Uri}]/**Specify the time field in the dataset schema. This is an optional field used for time-series operations, such as Sliding Window Aggregation. See details in SlidingWindowAggregation section in go/feathroffline.*/timeField:optional/**Represent a time field in the dataset schema, it is used for time-series operations. One usage is Sliding Window Aggregation, see details in go/frameoffline.*/record TimeField{/**Name of the time field in the dataset schema.*/name:string/**The format that the time field uses to represent time.*/format:/**The format of the time field. It can be either the granularity of a timestamp or datetime format e.g. yyyy/MM/dd/HH/mm/ss.*/typeref TimeFieldFormat=union[/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string]}/**Represents the time format being used to parse snapshot time from a dataset's file path. See DatasetSnapshotTimeFormat for more details.*/datasetSnapshotTimeFormat:optional/**Represents the time format being used to parse snapshot time from a dataset's file path, for example \"2020/09/30\" in /hdfs/jobs/lyndarel/features/master/members/daily/2020/09/30/. The dataset snapshot time is useful when feature data is time-partitioned for time-aware join or sliding-window aggregation. An example feature is derived skills, because derived skills change often based on user activities, modeling engineers want to join the time-sensitive observation data with the closest version of the feature. Refer to Unification of SWA and time-based features RFC (https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#) for more details.*/typeref DatasetSnapshotTimeFormat=union[DateTimeFormat/**Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of \"Unification of SWA and time-based features\" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).*/record StandardDateTimeFormat{}]}/**Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF, SWA) to produce feature value from a hfds data source.*/transformationFunction:union[MvelExpression,UserDefinedFunction,SparkSqlExpression/**Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and compute the aggregagate value.*/record SlidingWindowAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types of aggregation.*/aggregationType:enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG}/**Represents the time window to look back from label data's timestamp.*/window:/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.*/lateralViews:array[/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[SparkSqlExpression]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}]=[]/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the max number of groups (with aggregation results) to return.*/limit:optional int}/**Sliding window embedding aggregation produces a single embedding by performing element-wise operations or discretization on a collection of embeddings within a given time interval. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and produce the aggregagated embedding.*/record SlidingWindowEmbeddingAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types for embedding aggregation.*/aggregationType:enum EmbeddingAggregationType{/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. */MAX_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. */MIN_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. */AVG_POOLING}/**Represents the time window to look back from label data's timestamp.*/window:Window/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]}/**This sliding window algorithm picks the latest available feature data from the source data. Note the latest here means event time instead of processing time.*/record SlidingWindowLatestAvailable{/**The target column to pick the latest available record from.*/targetColumn:union[SparkSqlExpression]/**Represents the time window to look back from label data's timestamp.*/window:optional Window/**Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.*/lateralViews:array[LateralView]=[]/**Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the filter statement before applying the sliding window algorithm.*/filter:optional union[SparkSqlExpression]/**Represents the max number of groups (with latest available feature data) to return.*/limit:optional int}]}/**Represents an offline anchor with observation data passthrough data source, which is used for features that already exist in the observation data.*/@OfflineAnchor,record ObservationPassthroughDataSourceAnchor includes KeyPlaceholders{/**Defines an observation data passthrough data source, which is used for features that already exist in the observation data. See ObservationPassthroughDataSource for more details.*/source:/**Represents a observation data passthrough data source. Passthrough data sources are used when the data are not from external sources. It's commonly used for features that already exist in the observation data. See Pass-through Features section in go/frameoffline.*/record ObservationPassthroughDataSource includes OfflineDataSourceKey,DataSourceRef{}/**Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF) to produce feature value for an observation data passthrough data source.*/transformationFunction:union[MvelExpression,SparkSqlExpression,UserDefinedFunction]}/**Represents an offline anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.*/@OfflineAnchor,record OfflineFeatureSourcesAnchor includes KeyPlaceholders{/**Defines one or multiple other features as source.*/source:array[/**This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.*/record FeatureSource includes DataSourceRef{/**This is the unique id for the MlFeatureVersionEntity used as the source.*/urn:{namespace com.linkedin.frame.common/**Standardized MLFeature identifier.*/@java.class=\"com.linkedin.frame.common.urn.MlFeatureVersionUrn\"typeref MlFeatureVersionUrn=string}/**A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).*/alias:optional string/**Represents zero, one or multiple keyPlaceholderRe").append("fs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRefs:array[KeyPlaceholderRef]=[]}]/**Defines the supported transformation logic in offline environment (eg. MVEL, Spark SQL, UDF) to produce feature value from feature sources.*/transformationFunction:union[MvelExpression,UserDefinedFunction,SparkSqlExpression,UnspecifiedTransformationFunction]}/**Represents an online anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.*/@OnlineAnchor,record OnlineFeatureSourcesAnchor includes KeyPlaceholders{/**Defines one or multiple other features as source.*/source:array[FeatureSource]/**Defines the supported transformation logic in online environment (eg. MVEL) to produce feature value from feature sources.*/transformationFunction:union[MvelExpression,UnspecifiedTransformationFunction]}/**Represents an anchor that uses one or multiple other features as source and can be used in an environment-agnostic way. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate other environment-specific feature source anchor and use this class across the board.*/@OfflineAnchor@OnlineAnchor,record CrossEnvironmentFeatureSourcesAnchor includes KeyPlaceholders{/**Defines one or multiple other features as source.*/source:array[FeatureSource]/**Defines the supported transformation logic uniformly across all environments (eg. MVEL) to produce feature value from feature sources.*/transformationFunction:union[MvelExpression,UnspecifiedTransformationFunction]}/**Sequential join is useful when the feature data of one feature is used as the key values for another feature. Unlike a traditional FeatureSourcesAnchor, the source of SequentialJoinFeatureSourcesAnchor include a base feature and an expansion feature. A high-level data flow is: feature data of the base feature is used as key values to compute the expansion feature, the result will contain multiple feature data for different key values, then a reduce function will be applied to converge them into a single feature data. For more details, refer to go/frame/sequentialjoin.*/@OfflineAnchor@OnlineAnchor,record SequentialJoinFeatureSourcesAnchor includes KeyPlaceholders{/**Represents the base feature, its feature data is used as key values to compute the expansion feature.*/base:FeatureSource/**After feature data of the base feature is computed, it may require additional transformation to match expected key format of the expansion feature.*/expansionKeyFunction:record ExpansionKeyFunction includes KeyPlaceholders{/**This transformation ensures the feature data of the base feature can be converted to the expected key format of the expansion feature.*/keyFunction:union[MvelExpression/**Represents a transformation function that always returns the same value as the input. See https://en.wikipedia.org/wiki/Identity_function.*/record IdentityFunction{}UserDefinedFunction]}/**Represents the expansion feature, which uses the output of expansionKeyFunction as key values to compute feature data of the expansion feature.*/expansion:FeatureSource/**The computation of the expansion feature will fanout to multiple feature data for different key values, a reduce function will be applied after the fanout to ensure a single feature data being returned.*/reductionFunction:typeref ReductionFunction=union[enum StandardAggregation{/** Average. Apply to 0-rank numeric scalar. For example, base feature gets a job's applicants, and expansion feature gets the age of an applicant, when average is specified, the resulting feature data is the average age of all applicants for a job. */AVG/** Max. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MAX/** Min. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MIN/** Sum. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */SUM/** Union. This combines multiple feature data into a collection. For example, base feature gets member's skill ids, and expansion feature gets a skill name based on skill id, when union is specified, the resulting feature data is a list of skill names for a given member. */UNION/** Element-wise Average, which apply average operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. For example, base feature gets member's skill ids, and expansion feature gets a skill embedding based on skill id, when ELEMENTWISE_AVG is specified, the resulting feature data is a single embedding by element-wise averaging all skill embeddings of the member. */ELEMENTWISE_AVG/** Element-wise Min, which apply min operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MIN/** Element-wise Max, which apply max operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MAX/** Element-wise Sum, which apply sum operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_SUM}]}]}").toString(), SchemaFormatType.PDL));
    private FeatureAnchor.Anchor _anchorField = null;
    private FeatureAnchor.ChangeListener __changeListener = new FeatureAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Anchor = SCHEMA.getField("anchor");

    public FeatureAnchor() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public FeatureAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeatureAnchor.Fields fields() {
        return _fields;
    }

    public static FeatureAnchor.ProjectionMask createMask() {
        return new FeatureAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for anchor
     * 
     * @see FeatureAnchor.Fields#anchor
     */
    public boolean hasAnchor() {
        if (_anchorField!= null) {
            return true;
        }
        return super._map.containsKey("anchor");
    }

    /**
     * Remover for anchor
     * 
     * @see FeatureAnchor.Fields#anchor
     */
    public void removeAnchor() {
        super._map.remove("anchor");
    }

    /**
     * Getter for anchor
     * 
     * @see FeatureAnchor.Fields#anchor
     */
    public FeatureAnchor.Anchor getAnchor(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getAnchor();
            case DEFAULT:
            case NULL:
                if (_anchorField!= null) {
                    return _anchorField;
                } else {
                    Object __rawValue = super._map.get("anchor");
                    _anchorField = ((__rawValue == null)?null:new FeatureAnchor.Anchor(__rawValue));
                    return _anchorField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for anchor
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureAnchor.Fields#anchor
     */
    @Nonnull
    public FeatureAnchor.Anchor getAnchor() {
        if (_anchorField!= null) {
            return _anchorField;
        } else {
            Object __rawValue = super._map.get("anchor");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("anchor");
            }
            _anchorField = ((__rawValue == null)?null:new FeatureAnchor.Anchor(__rawValue));
            return _anchorField;
        }
    }

    /**
     * Setter for anchor
     * 
     * @see FeatureAnchor.Fields#anchor
     */
    public FeatureAnchor setAnchor(FeatureAnchor.Anchor value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setAnchor(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field anchor of com.linkedin.feathr.featureDataModel.FeatureAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "anchor", value.data());
                    _anchorField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeAnchor();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "anchor", value.data());
                    _anchorField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "anchor", value.data());
                    _anchorField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for anchor
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureAnchor.Fields#anchor
     */
    public FeatureAnchor setAnchor(
        @Nonnull
        FeatureAnchor.Anchor value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field anchor of com.linkedin.feathr.featureDataModel.FeatureAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "anchor", value.data());
            _anchorField = value;
        }
        return this;
    }

    @Override
    public FeatureAnchor clone()
        throws CloneNotSupportedException
    {
        FeatureAnchor __clone = ((FeatureAnchor) super.clone());
        __clone.__changeListener = new FeatureAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeatureAnchor copy()
        throws CloneNotSupportedException
    {
        FeatureAnchor __copy = ((FeatureAnchor) super.copy());
        __copy._anchorField = null;
        __copy.__changeListener = new FeatureAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/FeatureAnchor.pdl.")
    public static class Anchor
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema(new StringBuilder().append("union[{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Couchbase data source.*/@OnlineAnchor,record CouchbaseDataSourceAnchor includes/**A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor. This class defines supported transformation functions (eg. MVEL, UDF) that can be used for an online data source.*/record TransformationFunctionForOnlineDataSource{/**Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines a Couchbase data source. Couchbase is a distributed key-value caching solution. See CouchbaseDataSource for more details.*/source:/**Represents a Couchbase data source. Couchbase is a distributed key-value caching solution. For more details: go/couchbase.*/record CouchbaseDataSource includes/**Represents the primary key that is used to fetch source data from the corresponding online data source. Note that the vast majority of online data sources available in Frame are key value stores. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OnlineDataSourceKey{/**Key function specifies how to construct the primary key to fetch source data from the corresponding online data source. For example, in a Venice store, the key schema is {mid: string, jid: long}, key function specifies a recipe to build the key, with key placholders memberId and jobId to be replaced with the actual key values at runtime. Keeping it optional for two reasons: 1. fulfilling backward compatiblity requirement during schema evolution 2. some online data sources (Rest.li finder, in-memory passthrough) don't have a key. For more details, refer to go/frameonline.*/keyFunction:optional union[MvelExpression]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Name of the Couchbase bucket. A bucket is a logical entity that groups documents; allowing them to be accessed, indexed, replicated, and access-controlled.*/bucketName:string/**The list of bootstrap servers for Frame to initiate communication to the Couchbase cluster.\nThis is no longer needed as Couchbase can resolve the URIs based on bucketName field.*/@deprecated=\"No longer needed as Couchbase can now resolve the URIs based on the bucketName field.\"bootstrapServers:array[{namespace com.linkedin.frame.common@java={\"class\":\"java.net.URI\",\"coercerClass\":\"com.linkedin.frame.common.coercer.UriCoercer\"}typeref Uri=string}]=[]/**Fully qualified Java class name for data model of the documents stored in the Couchbase bucket.*/documentDataModel:Clazz}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with custom data source which uses user-defined data fetching mechanism.*/@OnlineAnchor,record CustomDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a custom data source which uses user-defined data fetching mechanism. See CustomDataSource for more details.*/source:/**Represents a custom data source which uses user-defined data fetching mechanism. Its difference with InMemoryPassthroughDataSource is: CustomDataSource allows dependency injection via Offspring for supporting complex data fetching, whereas InMemoryPassthroughDataSource assumes data is already available in-memory along with the retrieval request. See CustomSource section in go/frameonline.*/record CustomDataSource includes OnlineDataSourceKey,DataSourceRef{/**The fully qualified Java class name of the response data model retrieved from the custom data source.*/dataModel:Clazz}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Espresso data source.*/@OnlineAnchor,record EspressoDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Espresso data source. Espresso the recommended source-of-truth distributed database at LinkedIn. See EspressoDataSource for more details.*/source:/**Represents a Espresso data source. Espresso the recommended source-of-truth distributed database at LinkedIn. It is optimized for balanced read/write traffic with relative small sized data records (< 1MB). For more details: go/espresso.*/record EspressoDataSource includes OnlineDataSourceKey,DataSourceRef{/**Espresso database name.*/databaseName:string/**Espresso table name.*/tableName:string/**D2 URI of the Espresso database which can be found on go/Nuage.*/d2Uri:com.linkedin.frame.common.Uri}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request.*/@OnlineAnchor,record InMemoryPassthroughDataSourceAnchor includes TransformationFunctionForOnlineDataSource{/**Defines an in-memory passthrough data source, which is commonly used for contextual features that are supplied as part of an online request. See InMemoryPassthroughDataSource for more details.*/source:/**Represents a in-memory passthrough data source. Passthrough data sources are used when the data are not from external sources such as Rest.li, Venice and Espresso. It's commonly used for contextual features that are supplied as part of an online request. See Passthrough section in go/frameonline.*/record InMemoryPassthroughDataSource includes DataSourceRef{/**The fully qualified Java class name for the data model of passthrough (in-memory) data that is supplied as part of the online request.*/dataModel:Clazz}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource.*/@OnlineAnchor,record RestliDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource. See RestliDataSource for more details.*/source:/**Represents a Rest.li data source, in which source data is fetched by the primary key of the Rest.li resource. Also see RestliFinderDataSource if you are looking for fetching source data by a finder method.*/record RestliDataSource includes OnlineDataSourceKey,DataSourceRef{/**The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.*/resourceName:string/**Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details. It should be required with a default value, but the optional flag can't be removed after its check-in, for complying with backward compatibility requirement of schema evolution.*/projections:optional array[/**Represents a PathSpec string. PathSpec is used to specify projections in Rest.li request in order to select a subset of object contents. For more details, refer to https://linkedin.github.io/rest.li/Projections. Some examples: 'listingType', 'member:(firstName, lastName)'.*/typeref PathSpec=string]=[]/**Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#methods,get, the key value pair keyed by viewer represents a request parameter of the Get method in jobPostings resource.*/requestParameters:map[string/**Represents the value part in a Rest.li request paramater key/value pair.*/typeref RequestParameterValue=union[MvelExpression,JsonString]]={}}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Rest.li finder data source, in which source data is fetched by a Rest.li finder method.*/@OnlineAnchor,record RestliFinderDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. See RestliFinderDataSource for more details.*/source:/**Represents a Rest.li finder data source, in which source data is fetched by a Rest.li finder method. For more details, refer to https://linkedin.github.io/rest.li/user_guide/restli_server#finder. Note: it is possible for a finder source to have an keyExpr for association resources since the key parts of the association key can be used as query parameters which are modeled as CompoundKey. See https://linkedin.github.io/rest.li/user_guide/restli_server#finder for more details*/record RestliFinderDataSource includes OnlineDataSourceKey,DataSourceRef{/**The Rest.li resource name for the Rest.li service registered in D2. For example, profiles is the resource name for https://sceptre.corp.linkedin.com/service-detail/profiles/resource/profiles/details. A complete list of resources can be found at go/restli.*/resourceName:string/**Represents the finder method name of the resource.*/finderMethod:string/**Projections are used to select a subset of object contents for performance reasons or based on security policy. See https://linkedin.github.io/rest.li/Projections for details.*/projections:array[PathSpec]=[]/**Represents the request parameters needed by the resource to fetch the source data. For example, in https://sceptre.corp.linkedin.com/service-detail/jobPostings/resource/jobPostings/details#finders,companyJobCodes, the key value pair keyed by contract represents a request parameter of the companyJobCodes finder method in jobPostings resource.*/requestParameters:map[string,RequestParameterValue]={}}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Venice data source.*/@OnlineAnchor,record VeniceDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Venice data source. Venice is a data store for derived data. See VeniceDataSource for more details.*/source:/**Represents a Venice data source. Venice is a data store for derived data. All derived data accessed by primary key, whether produced offline on Hadoop or via nearline stream processing in Samza, can be served by Venice. For more details: go/venice.*/record VeniceDataSource includes OnlineDataSourceKey,DataSourceRef{/**The Venice store name.*/storeName:string}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Pinot data source.*/@OnlineAnchor,record PinotDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Pinot data source. Pinot is a realtime distributed OLAP datastore. See PinotDataSource for more details.*/source:/**Represent a Pinot data source. Pinot is a realtime distributed OLAP datastore which supports data fetching through standard SQL query syntax and semantics. It is suitable for querying time series data with lots of Dimensions and Metrics. For more details on Pinot: go/pinot. Also see <a href=\"https://docs.google.com/document/d/1nx-j-JJLWY4QaU2hgoQ6H9rDEcVG9jZo6CQVRX4ATvA/edit/\">[RFC] Pinot Frame Online</a> for the details on the Pinot data source design.*/record PinotDataSource includes DataSourceRef{/**Represent the service name in the Pinot D2 config for the source Pinot table.*/resourceName:string/**Represent the sql query template to fetch data from Pinot table, with \u201c?\u201d as placeholders for run time value replacement from specified queryArguments in the same order. For example: \"SELECT objectAttributes, timeStampSec FROM RecentMemberActions WHERE actorId IN (?) AND timeStampSec > ?\".*/queryTemplate:string/**Represent mvel expressions whose values will be evaluated at runtime to replace the \"?\" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be [\"key[0]\", \"System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60\"].*/queryArguments:array[MvelExpression]/**Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be [\"actorId\"]. As in queryArguments, only \"key[0]\" is base on key values, and it is used with \"actorId\" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{\u201c123\u201d, \u201cpage_view\u201d, \u201c323\u201d}, {\u201c987\", \u201cpage_view\u201d, \u201c876\"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.*/queryKeyColumns:array[string]}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Vector data source.*/record VectorDataSourceAnchor includes TransformationFunctionForOnlineDataSource,KeyPlaceholders{/**Defines a Vector data source, in which source data is fetched by assetUrn + featureSourceName via Restli unstructured\ndata endpoint.*/source:/**Represent a Vector data source. Vector is a media asset serving service accessed via Restli unstructured data endpoint.*/record VectorDataSource includes OnlineDataSourceKey,DataSourceRef{/**This is a Vector request param needed to uniquely identify the Vector asset which a user is trying to fetch.\nFor example \"png_200_200\". This will be decided by the user at write time when the user writes an asset to Vector.\nThe type is string and not enum. The values will be decided between the team that will use the media data and Vector.*/featureSourceName:string}}}{namespace com.linkedin.feathr.featureDataModel/**Represents an offline anchor with HDFS data source.*/record HdfsDataSourceAnchor includes KeyPlaceholders{/**Defines a HDFS data source. See HdfsDataSource for more details.*/source:/**Represents a HDFS data source.*/record HdfsDataSource includes/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineDataSourceKey{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.*/keyFunction:optional union[MvelExpression/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}UserDefinedFunction]}DataSourceRef{/**Location of the dataset, it can be a HDFS path of the dataset, or Dali URI (dalids:///)*/datasetLocation:union[/**A location on HDFS.*/record HdfsLocation{/**Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/*/path:string}/**The location of a Dali dataset or view. See [go/dali](http://go/dali).*/record DaliLocation{/**The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.*/uri:com.linkedin.frame.common.Uri}]/**Specify the time field in the dataset schema. This is an optional field used for time-series operations, such as Sliding Window Aggregation. See details in SlidingWindowAggregation section in go/feathroffline.*/timeField:optional/**Represent a time field in the dataset schema, it is used for time-series operations. One usage is Sliding Window Aggregation, see details in go/frameoffline.*/record TimeField{/**Name of the time field in the dataset schema.*/name:string/**The format that the time field uses to represent time.*/format:/**The format of the time field. It can be either the granularity of a timestamp or datetime format e.g. yyyy/MM/dd/HH/mm/ss.*/typeref TimeFieldFormat=union[/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string]}/**Represents the time format being used to parse snapshot time from a dataset's file path. See DatasetSnapshotTimeFormat for more details.*/datasetSnapshotTimeFormat:optional/**Represents the time format being used to parse snapshot time from a dataset's file path, for example \"2020/09/30\" in /hdfs/jobs/lyndarel/features/master/members/daily/2020/09/30/. The dataset snapshot time is useful when feature data is time-partitioned for time-aware join or sliding-window aggregation. An example feature is derived skills, because derived skills change often based on user activities, modeling engineers want to join the time-sensitive observation data with the closest version of the feature. Refer to Unification of SWA and time-based features RFC (https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#) for more details.*/typeref DatasetSnapshotTimeFormat=union[DateTimeFormat/**Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of \"Unification of SWA and time-based features\" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).*/record StandardDateTimeFormat{}]}/**Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF, SWA) to produce feature value from a hfds data source.*/transformationFunction:union[MvelExpression,UserDefinedFunction,SparkSqlExpression/**Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and compute the aggregagate value.*/record SlidingWindowAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types of aggregation.*/aggregationType:enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG}/**Represents the time window to look back from label data's timestamp.*/window:/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.*/lateralViews:array[/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[SparkSqlExpression]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}]=[]/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the max number of groups (with aggregation results) to return.*/limit:optional int}/**Sliding window embedding aggregation produces a single embedding by performing element-wise operations or discretization on a collection of embeddings within a given time interval. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and produce the aggregagated embedding.*/record SlidingWindowEmbeddingAggregation{/**The target column to perform aggregation against.*/targetColumn:union[SparkSqlExpression]/**Represents supported types for embedding aggregation.*/aggregationType:enum EmbeddingAggregationType{/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. */MAX_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. */MIN_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. */AVG_POOLING}/**Represents the time window to look back from label data's timestamp.*/window:Window/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]}/**This sliding window algorithm picks the latest available feature data from the source data. Note the latest here means event time instead of processing time.*/record SlidingWindowLatestAvailable{/**The target column to pick the latest available record from.*/targetColumn:union[SparkSqlExpression]/**Represents the time window to look back from label data's timestamp.*/window:optional Window/**Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.*/lateralViews:array[LateralView]=[]/**Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the filter statement before applying the sliding window algorithm.*/filter:optional union[SparkSqlExpression]/**Represents the max number of groups (with latest available feature data) to return.*/limit:optional int}]}}{namespace com.linkedin.feathr.featureDataModel/**Represents an offline anchor with observation data passthrough data source, which is used for features that already exist in the observation data.*/@OfflineAnchor,record ObservationPassthroughDataSourceAnchor includes KeyPlaceholders{/**Defines an observation data passthrough data source, which is used for features that already exist in the observation data. See ObservationPassthroughDataSource for more details.*/source:/**Represents a observation data passthrough data source. Passthrough data sources are used when the data are not from external sources. It's commonly used for features that already exist in the observation data. See Pass-through Features section in go/frameoffline.*/record ObservationPassthroughDataSource includes OfflineDataSourceKey,DataSourceRef{}/**Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF) to produce feature value for an observation data passthrough data source.*/transformationFunction:union[MvelExpression,SparkSqlExpression,UserDefinedFunction]}}{namespace com.linkedin.feathr.featureDataModel/**Represents an offline anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.*/@OfflineAnchor,record OfflineFeatureSourcesAnchor includes KeyPlaceholders{/**Defines one or multiple other features as source.*/source:array[/**This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.*/record FeatureSource includes DataSourceRef{/**This is the unique id for the MlFeatureVersionEntity used as the source.*/urn:{namespace com.linkedin.frame.common/**Standardized MLFeature identifier.*/@java.class=\"com.linkedin.frame.common.urn.MlFeatureVersionUrn\"typeref MlFeatureVersionUrn=string}/**A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).*/alias:optional string/**Represents zero, one or multiple keyP").append("laceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRefs:array[KeyPlaceholderRef]=[]}]/**Defines the supported transformation logic in offline environment (eg. MVEL, Spark SQL, UDF) to produce feature value from feature sources.*/transformationFunction:union[MvelExpression,UserDefinedFunction,SparkSqlExpression,UnspecifiedTransformationFunction]}}{namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor that uses one or multiple other features as source. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate this class.*/@OnlineAnchor,record OnlineFeatureSourcesAnchor includes KeyPlaceholders{/**Defines one or multiple other features as source.*/source:array[FeatureSource]/**Defines the supported transformation logic in online environment (eg. MVEL) to produce feature value from feature sources.*/transformationFunction:union[MvelExpression,UnspecifiedTransformationFunction]}}{namespace com.linkedin.feathr.featureDataModel/**Represents an anchor that uses one or multiple other features as source and can be used in an environment-agnostic way. Ideally, feature source anchor (aka derived feature) should all be environment-agnostic and use CrossEnvFeatureSourcesAnchor. However, currently a large portion of derived features are defined differently in different environments (eg. online environment only supports MVEL, offline environment supports MVEL, UDF, and Spark SQL, galene envionment doesn't support derivation). In Frame v2, one improvement area is to fix the inconsistency across environments, by the completion of Frame v2 and user migration, we can deprecate other environment-specific feature source anchor and use this class across the board.*/@OfflineAnchor@OnlineAnchor,record CrossEnvironmentFeatureSourcesAnchor includes KeyPlaceholders{/**Defines one or multiple other features as source.*/source:array[FeatureSource]/**Defines the supported transformation logic uniformly across all environments (eg. MVEL) to produce feature value from feature sources.*/transformationFunction:union[MvelExpression,UnspecifiedTransformationFunction]}}{namespace com.linkedin.feathr.featureDataModel/**Sequential join is useful when the feature data of one feature is used as the key values for another feature. Unlike a traditional FeatureSourcesAnchor, the source of SequentialJoinFeatureSourcesAnchor include a base feature and an expansion feature. A high-level data flow is: feature data of the base feature is used as key values to compute the expansion feature, the result will contain multiple feature data for different key values, then a reduce function will be applied to converge them into a single feature data. For more details, refer to go/frame/sequentialjoin.*/@OfflineAnchor@OnlineAnchor,record SequentialJoinFeatureSourcesAnchor includes KeyPlaceholders{/**Represents the base feature, its feature data is used as key values to compute the expansion feature.*/base:FeatureSource/**After feature data of the base feature is computed, it may require additional transformation to match expected key format of the expansion feature.*/expansionKeyFunction:record ExpansionKeyFunction includes KeyPlaceholders{/**This transformation ensures the feature data of the base feature can be converted to the expected key format of the expansion feature.*/keyFunction:union[MvelExpression/**Represents a transformation function that always returns the same value as the input. See https://en.wikipedia.org/wiki/Identity_function.*/record IdentityFunction{}UserDefinedFunction]}/**Represents the expansion feature, which uses the output of expansionKeyFunction as key values to compute feature data of the expansion feature.*/expansion:FeatureSource/**The computation of the expansion feature will fanout to multiple feature data for different key values, a reduce function will be applied after the fanout to ensure a single feature data being returned.*/reductionFunction:typeref ReductionFunction=union[enum StandardAggregation{/** Average. Apply to 0-rank numeric scalar. For example, base feature gets a job's applicants, and expansion feature gets the age of an applicant, when average is specified, the resulting feature data is the average age of all applicants for a job. */AVG/** Max. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MAX/** Min. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MIN/** Sum. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */SUM/** Union. This combines multiple feature data into a collection. For example, base feature gets member's skill ids, and expansion feature gets a skill name based on skill id, when union is specified, the resulting feature data is a list of skill names for a given member. */UNION/** Element-wise Average, which apply average operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. For example, base feature gets member's skill ids, and expansion feature gets a skill embedding based on skill id, when ELEMENTWISE_AVG is specified, the resulting feature data is a single embedding by element-wise averaging all skill embeddings of the member. */ELEMENTWISE_AVG/** Element-wise Min, which apply min operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MIN/** Element-wise Max, which apply max operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MAX/** Element-wise Sum, which apply sum operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_SUM}]}}]").toString(), SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor _couchbaseDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor _customDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor _espressoDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor _inMemoryPassthroughDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor _restliDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor _restliFinderDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor _veniceDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor _pinotDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor _vectorDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor _hdfsDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor _observationPassthroughDataSourceAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor _offlineFeatureSourcesAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor _onlineFeatureSourcesAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor _crossEnvironmentFeatureSourcesAnchorMember = null;
        private com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor _sequentialJoinFeatureSourcesAnchorMember = null;
        private FeatureAnchor.Anchor.ChangeListener __changeListener = new FeatureAnchor.Anchor.ChangeListener(this);
        private final static DataSchema MEMBER_CouchbaseDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor");
        private final static DataSchema MEMBER_CustomDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor");
        private final static DataSchema MEMBER_EspressoDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor");
        private final static DataSchema MEMBER_InMemoryPassthroughDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor");
        private final static DataSchema MEMBER_RestliDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor");
        private final static DataSchema MEMBER_RestliFinderDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor");
        private final static DataSchema MEMBER_VeniceDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor");
        private final static DataSchema MEMBER_PinotDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor");
        private final static DataSchema MEMBER_VectorDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor");
        private final static DataSchema MEMBER_HdfsDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor");
        private final static DataSchema MEMBER_ObservationPassthroughDataSourceAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor");
        private final static DataSchema MEMBER_OfflineFeatureSourcesAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor");
        private final static DataSchema MEMBER_OnlineFeatureSourcesAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor");
        private final static DataSchema MEMBER_CrossEnvironmentFeatureSourcesAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor");
        private final static DataSchema MEMBER_SequentialJoinFeatureSourcesAnchor = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");

        public Anchor() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public Anchor(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setCouchbaseDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isCouchbaseDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor getCouchbaseDataSourceAnchor() {
            checkNotNull();
            if (_couchbaseDataSourceAnchorMember!= null) {
                return _couchbaseDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor");
            _couchbaseDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _couchbaseDataSourceAnchorMember;
        }

        public void setCouchbaseDataSourceAnchor(com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _couchbaseDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setCustomDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isCustomDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor getCustomDataSourceAnchor() {
            checkNotNull();
            if (_customDataSourceAnchorMember!= null) {
                return _customDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor");
            _customDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _customDataSourceAnchorMember;
        }

        public void setCustomDataSourceAnchor(com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _customDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setEspressoDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isEspressoDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor getEspressoDataSourceAnchor() {
            checkNotNull();
            if (_espressoDataSourceAnchorMember!= null) {
                return _espressoDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor");
            _espressoDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _espressoDataSourceAnchorMember;
        }

        public void setEspressoDataSourceAnchor(com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _espressoDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setInMemoryPassthroughDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isInMemoryPassthroughDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor getInMemoryPassthroughDataSourceAnchor() {
            checkNotNull();
            if (_inMemoryPassthroughDataSourceAnchorMember!= null) {
                return _inMemoryPassthroughDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor");
            _inMemoryPassthroughDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _inMemoryPassthroughDataSourceAnchorMember;
        }

        public void setInMemoryPassthroughDataSourceAnchor(com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _inMemoryPassthroughDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setRestliDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isRestliDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor getRestliDataSourceAnchor() {
            checkNotNull();
            if (_restliDataSourceAnchorMember!= null) {
                return _restliDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor");
            _restliDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _restliDataSourceAnchorMember;
        }

        public void setRestliDataSourceAnchor(com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _restliDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setRestliFinderDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isRestliFinderDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor getRestliFinderDataSourceAnchor() {
            checkNotNull();
            if (_restliFinderDataSourceAnchorMember!= null) {
                return _restliFinderDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor");
            _restliFinderDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _restliFinderDataSourceAnchorMember;
        }

        public void setRestliFinderDataSourceAnchor(com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _restliFinderDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setVeniceDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isVeniceDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor getVeniceDataSourceAnchor() {
            checkNotNull();
            if (_veniceDataSourceAnchorMember!= null) {
                return _veniceDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor");
            _veniceDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _veniceDataSourceAnchorMember;
        }

        public void setVeniceDataSourceAnchor(com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _veniceDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setPinotDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isPinotDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor getPinotDataSourceAnchor() {
            checkNotNull();
            if (_pinotDataSourceAnchorMember!= null) {
                return _pinotDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor");
            _pinotDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _pinotDataSourceAnchorMember;
        }

        public void setPinotDataSourceAnchor(com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _pinotDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setVectorDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isVectorDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor getVectorDataSourceAnchor() {
            checkNotNull();
            if (_vectorDataSourceAnchorMember!= null) {
                return _vectorDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor");
            _vectorDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _vectorDataSourceAnchorMember;
        }

        public void setVectorDataSourceAnchor(com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _vectorDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setHdfsDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isHdfsDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor getHdfsDataSourceAnchor() {
            checkNotNull();
            if (_hdfsDataSourceAnchorMember!= null) {
                return _hdfsDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor");
            _hdfsDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _hdfsDataSourceAnchorMember;
        }

        public void setHdfsDataSourceAnchor(com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _hdfsDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setObservationPassthroughDataSourceAnchor(value);
            return newUnion;
        }

        public boolean isObservationPassthroughDataSourceAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor");
        }

        public com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor getObservationPassthroughDataSourceAnchor() {
            checkNotNull();
            if (_observationPassthroughDataSourceAnchorMember!= null) {
                return _observationPassthroughDataSourceAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor");
            _observationPassthroughDataSourceAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _observationPassthroughDataSourceAnchorMember;
        }

        public void setObservationPassthroughDataSourceAnchor(com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor value) {
            checkNotNull();
            super._map.clear();
            _observationPassthroughDataSourceAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setOfflineFeatureSourcesAnchor(value);
            return newUnion;
        }

        public boolean isOfflineFeatureSourcesAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor");
        }

        public com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor getOfflineFeatureSourcesAnchor() {
            checkNotNull();
            if (_offlineFeatureSourcesAnchorMember!= null) {
                return _offlineFeatureSourcesAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor");
            _offlineFeatureSourcesAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _offlineFeatureSourcesAnchorMember;
        }

        public void setOfflineFeatureSourcesAnchor(com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor value) {
            checkNotNull();
            super._map.clear();
            _offlineFeatureSourcesAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setOnlineFeatureSourcesAnchor(value);
            return newUnion;
        }

        public boolean isOnlineFeatureSourcesAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor");
        }

        public com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor getOnlineFeatureSourcesAnchor() {
            checkNotNull();
            if (_onlineFeatureSourcesAnchorMember!= null) {
                return _onlineFeatureSourcesAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor");
            _onlineFeatureSourcesAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _onlineFeatureSourcesAnchorMember;
        }

        public void setOnlineFeatureSourcesAnchor(com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor value) {
            checkNotNull();
            super._map.clear();
            _onlineFeatureSourcesAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setCrossEnvironmentFeatureSourcesAnchor(value);
            return newUnion;
        }

        public boolean isCrossEnvironmentFeatureSourcesAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor");
        }

        public com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor getCrossEnvironmentFeatureSourcesAnchor() {
            checkNotNull();
            if (_crossEnvironmentFeatureSourcesAnchorMember!= null) {
                return _crossEnvironmentFeatureSourcesAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor");
            _crossEnvironmentFeatureSourcesAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _crossEnvironmentFeatureSourcesAnchorMember;
        }

        public void setCrossEnvironmentFeatureSourcesAnchor(com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor value) {
            checkNotNull();
            super._map.clear();
            _crossEnvironmentFeatureSourcesAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor", value.data());
        }

        public static FeatureAnchor.Anchor create(com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor value) {
            FeatureAnchor.Anchor newUnion = new FeatureAnchor.Anchor();
            newUnion.setSequentialJoinFeatureSourcesAnchor(value);
            return newUnion;
        }

        public boolean isSequentialJoinFeatureSourcesAnchor() {
            return memberIs("com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
        }

        public com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor getSequentialJoinFeatureSourcesAnchor() {
            checkNotNull();
            if (_sequentialJoinFeatureSourcesAnchorMember!= null) {
                return _sequentialJoinFeatureSourcesAnchorMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
            _sequentialJoinFeatureSourcesAnchorMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sequentialJoinFeatureSourcesAnchorMember;
        }

        public void setSequentialJoinFeatureSourcesAnchor(com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor value) {
            checkNotNull();
            super._map.clear();
            _sequentialJoinFeatureSourcesAnchorMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor", value.data());
        }

        public static FeatureAnchor.Anchor.ProjectionMask createMask() {
            return new FeatureAnchor.Anchor.ProjectionMask();
        }

        @Override
        public FeatureAnchor.Anchor clone()
            throws CloneNotSupportedException
        {
            FeatureAnchor.Anchor __clone = ((FeatureAnchor.Anchor) super.clone());
            __clone.__changeListener = new FeatureAnchor.Anchor.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public FeatureAnchor.Anchor copy()
            throws CloneNotSupportedException
        {
            FeatureAnchor.Anchor __copy = ((FeatureAnchor.Anchor) super.copy());
            __copy._inMemoryPassthroughDataSourceAnchorMember = null;
            __copy._hdfsDataSourceAnchorMember = null;
            __copy._veniceDataSourceAnchorMember = null;
            __copy._restliDataSourceAnchorMember = null;
            __copy._observationPassthroughDataSourceAnchorMember = null;
            __copy._onlineFeatureSourcesAnchorMember = null;
            __copy._restliFinderDataSourceAnchorMember = null;
            __copy._espressoDataSourceAnchorMember = null;
            __copy._offlineFeatureSourcesAnchorMember = null;
            __copy._couchbaseDataSourceAnchorMember = null;
            __copy._crossEnvironmentFeatureSourcesAnchorMember = null;
            __copy._sequentialJoinFeatureSourcesAnchorMember = null;
            __copy._customDataSourceAnchorMember = null;
            __copy._vectorDataSourceAnchorMember = null;
            __copy._pinotDataSourceAnchorMember = null;
            __copy.__changeListener = new FeatureAnchor.Anchor.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final FeatureAnchor.Anchor __objectRef;

            private ChangeListener(FeatureAnchor.Anchor reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor":
                        __objectRef._inMemoryPassthroughDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor":
                        __objectRef._hdfsDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor":
                        __objectRef._veniceDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor":
                        __objectRef._restliDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor":
                        __objectRef._observationPassthroughDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor":
                        __objectRef._onlineFeatureSourcesAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor":
                        __objectRef._restliFinderDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor":
                        __objectRef._espressoDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor":
                        __objectRef._offlineFeatureSourcesAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor":
                        __objectRef._couchbaseDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor":
                        __objectRef._crossEnvironmentFeatureSourcesAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor":
                        __objectRef._sequentialJoinFeatureSourcesAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor":
                        __objectRef._customDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor":
                        __objectRef._vectorDataSourceAnchorMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor":
                        __objectRef._pinotDataSourceAnchorMember = null;
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

            public com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor.Fields CouchbaseDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor.Fields CustomDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor.Fields EspressoDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor.Fields InMemoryPassthroughDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor.Fields RestliDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor.Fields RestliFinderDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor.Fields VeniceDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor.Fields PinotDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor.Fields VectorDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.Fields HdfsDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.Fields ObservationPassthroughDataSourceAnchor() {
                return new com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor");
            }

            public com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.Fields OfflineFeatureSourcesAnchor() {
                return new com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor");
            }

            public com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.Fields OnlineFeatureSourcesAnchor() {
                return new com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor");
            }

            public com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.Fields CrossEnvironmentFeatureSourcesAnchor() {
                return new com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor");
            }

            public com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor.Fields SequentialJoinFeatureSourcesAnchor() {
                return new com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor.ProjectionMask _CouchbaseDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor.ProjectionMask _CustomDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor.ProjectionMask _EspressoDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor.ProjectionMask _InMemoryPassthroughDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor.ProjectionMask _RestliDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor.ProjectionMask _RestliFinderDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor.ProjectionMask _VeniceDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor.ProjectionMask _PinotDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor.ProjectionMask _VectorDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.ProjectionMask _HdfsDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.ProjectionMask _ObservationPassthroughDataSourceAnchorMask;
            private com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.ProjectionMask _OfflineFeatureSourcesAnchorMask;
            private com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.ProjectionMask _OnlineFeatureSourcesAnchorMask;
            private com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.ProjectionMask _CrossEnvironmentFeatureSourcesAnchorMask;
            private com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor.ProjectionMask _SequentialJoinFeatureSourcesAnchorMask;

            ProjectionMask() {
            }

            public FeatureAnchor.Anchor.ProjectionMask withCouchbaseDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor.ProjectionMask> nestedMask) {
                _CouchbaseDataSourceAnchorMask = nestedMask.apply(((_CouchbaseDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor.createMask():_CouchbaseDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.CouchbaseDataSourceAnchor", _CouchbaseDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withCustomDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor.ProjectionMask> nestedMask) {
                _CustomDataSourceAnchorMask = nestedMask.apply(((_CustomDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor.createMask():_CustomDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.CustomDataSourceAnchor", _CustomDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withEspressoDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor.ProjectionMask> nestedMask) {
                _EspressoDataSourceAnchorMask = nestedMask.apply(((_EspressoDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor.createMask():_EspressoDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.EspressoDataSourceAnchor", _EspressoDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withInMemoryPassthroughDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor.ProjectionMask> nestedMask) {
                _InMemoryPassthroughDataSourceAnchorMask = nestedMask.apply(((_InMemoryPassthroughDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor.createMask():_InMemoryPassthroughDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSourceAnchor", _InMemoryPassthroughDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withRestliDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor.ProjectionMask> nestedMask) {
                _RestliDataSourceAnchorMask = nestedMask.apply(((_RestliDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor.createMask():_RestliDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.RestliDataSourceAnchor", _RestliDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withRestliFinderDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor.ProjectionMask> nestedMask) {
                _RestliFinderDataSourceAnchorMask = nestedMask.apply(((_RestliFinderDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor.createMask():_RestliFinderDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.RestliFinderDataSourceAnchor", _RestliFinderDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withVeniceDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor.ProjectionMask> nestedMask) {
                _VeniceDataSourceAnchorMask = nestedMask.apply(((_VeniceDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor.createMask():_VeniceDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.VeniceDataSourceAnchor", _VeniceDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withPinotDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor.ProjectionMask> nestedMask) {
                _PinotDataSourceAnchorMask = nestedMask.apply(((_PinotDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor.createMask():_PinotDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor", _PinotDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withVectorDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor.ProjectionMask> nestedMask) {
                _VectorDataSourceAnchorMask = nestedMask.apply(((_VectorDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor.createMask():_VectorDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.VectorDataSourceAnchor", _VectorDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withHdfsDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.ProjectionMask> nestedMask) {
                _HdfsDataSourceAnchorMask = nestedMask.apply(((_HdfsDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor.createMask():_HdfsDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.HdfsDataSourceAnchor", _HdfsDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withObservationPassthroughDataSourceAnchor(Function<com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.ProjectionMask> nestedMask) {
                _ObservationPassthroughDataSourceAnchorMask = nestedMask.apply(((_ObservationPassthroughDataSourceAnchorMask == null)?com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.createMask():_ObservationPassthroughDataSourceAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor", _ObservationPassthroughDataSourceAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withOfflineFeatureSourcesAnchor(Function<com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.ProjectionMask> nestedMask) {
                _OfflineFeatureSourcesAnchorMask = nestedMask.apply(((_OfflineFeatureSourcesAnchorMask == null)?com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor.createMask():_OfflineFeatureSourcesAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.OfflineFeatureSourcesAnchor", _OfflineFeatureSourcesAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withOnlineFeatureSourcesAnchor(Function<com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.ProjectionMask> nestedMask) {
                _OnlineFeatureSourcesAnchorMask = nestedMask.apply(((_OnlineFeatureSourcesAnchorMask == null)?com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor.createMask():_OnlineFeatureSourcesAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.OnlineFeatureSourcesAnchor", _OnlineFeatureSourcesAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withCrossEnvironmentFeatureSourcesAnchor(Function<com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.ProjectionMask> nestedMask) {
                _CrossEnvironmentFeatureSourcesAnchorMask = nestedMask.apply(((_CrossEnvironmentFeatureSourcesAnchorMask == null)?com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor.createMask():_CrossEnvironmentFeatureSourcesAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.CrossEnvironmentFeatureSourcesAnchor", _CrossEnvironmentFeatureSourcesAnchorMask.getDataMap());
                return this;
            }

            public FeatureAnchor.Anchor.ProjectionMask withSequentialJoinFeatureSourcesAnchor(Function<com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor.ProjectionMask, com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor.ProjectionMask> nestedMask) {
                _SequentialJoinFeatureSourcesAnchorMask = nestedMask.apply(((_SequentialJoinFeatureSourcesAnchorMask == null)?com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor.createMask():_SequentialJoinFeatureSourcesAnchorMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor", _SequentialJoinFeatureSourcesAnchorMask.getDataMap());
                return this;
            }

        }

    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeatureAnchor __objectRef;

        private ChangeListener(FeatureAnchor reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "anchor":
                    __objectRef._anchorField = null;
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
         * All the supported anchors. Each anchor defines which source and environment a feature is extracted or derived from and how a feature is transformed.
         * 
         */
        public com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor.Fields anchor() {
            return new com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor.Fields(getPathComponents(), "anchor");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor.ProjectionMask _anchorMask;

        ProjectionMask() {
            super(2);
        }

        /**
         * All the supported anchors. Each anchor defines which source and environment a feature is extracted or derived from and how a feature is transformed.
         * 
         */
        public FeatureAnchor.ProjectionMask withAnchor(Function<com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureAnchor.Anchor.ProjectionMask> nestedMask) {
            _anchorMask = nestedMask.apply(((_anchorMask == null)?FeatureAnchor.Anchor.createMask():_anchorMask));
            getDataMap().put("anchor", _anchorMask.getDataMap());
            return this;
        }

        /**
         * All the supported anchors. Each anchor defines which source and environment a feature is extracted or derived from and how a feature is transformed.
         * 
         */
        public FeatureAnchor.ProjectionMask withAnchor() {
            _anchorMask = null;
            getDataMap().put("anchor", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
