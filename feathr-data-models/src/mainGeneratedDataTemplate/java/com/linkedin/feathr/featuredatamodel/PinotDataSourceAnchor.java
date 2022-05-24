
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
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


/**
 * Represents an online anchor with Pinot data source.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/PinotDataSourceAnchor.pdl.")
public class PinotDataSourceAnchor
    extends RecordTemplate
{

    private final static PinotDataSourceAnchor.Fields _fields = new PinotDataSourceAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents an online anchor with Pinot data source.*/@OnlineAnchor,record PinotDataSourceAnchor includes/**A transformation function represents the transformation logic to produce feature value from the source of FeatureAnchor. This class defines supported transformation functions (eg. MVEL, UDF) that can be used for an online data source.*/record TransformationFunctionForOnlineDataSource{/**Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.*/transformationFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}]}/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines a Pinot data source. Pinot is a realtime distributed OLAP datastore. See PinotDataSource for more details.*/source:/**Represent a Pinot data source. Pinot is a realtime distributed OLAP datastore which supports data fetching through standard SQL query syntax and semantics. It is suitable for querying time series data with lots of Dimensions and Metrics. For more details on Pinot: go/pinot. Also see <a href=\"https://docs.google.com/document/d/1nx-j-JJLWY4QaU2hgoQ6H9rDEcVG9jZo6CQVRX4ATvA/edit/\">[RFC] Pinot Frame Online</a> for the details on the Pinot data source design.*/record PinotDataSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**Represent the service name in the Pinot D2 config for the source Pinot table.*/resourceName:string/**Represent the sql query template to fetch data from Pinot table, with \u201c?\u201d as placeholders for run time value replacement from specified queryArguments in the same order. For example: \"SELECT objectAttributes, timeStampSec FROM RecentMemberActions WHERE actorId IN (?) AND timeStampSec > ?\".*/queryTemplate:string/**Represent mvel expressions whose values will be evaluated at runtime to replace the \"?\" in queryTemplate in the same order. Following the example in queryTemplate, queryArguments could be [\"key[0]\", \"System.currentTimeMillis()/1000 - 2 * 24 * 60 * 60\"].*/queryArguments:array[MvelExpression]/**Represent Pinot table column names that correspond to the mvel expressions involving key parts in queryArguments. Following the examples in queryTemplate and queryArguments, queryKeyColumns would be [\"actorId\"]. As in queryArguments, only \"key[0]\" is base on key values, and it is used with \"actorId\" column in the queryTemplate. queryKeyColumns is needed because Pinot returns data in a flatten manner like [{\u201c123\u201d, \u201cpage_view\u201d, \u201c323\u201d}, {\u201c987\", \u201cpage_view\u201d, \u201c876\"}], we need to explicitly know which column(s) are key parts to deterministically parse Pinot response and construct the collection of feature data for a list of keys.*/queryKeyColumns:array[string]}}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction _transformationFunctionField = null;
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private PinotDataSource _sourceField = null;
    private PinotDataSourceAnchor.ChangeListener __changeListener = new PinotDataSourceAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_Source = SCHEMA.getField("source");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public PinotDataSourceAnchor() {
        super(new DataMap(4, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public PinotDataSourceAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static PinotDataSourceAnchor.Fields fields() {
        return _fields;
    }

    public static PinotDataSourceAnchor.ProjectionMask createMask() {
        return new PinotDataSourceAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see PinotDataSourceAnchor.Fields#transformationFunction
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
     * @see PinotDataSourceAnchor.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see PinotDataSourceAnchor.Fields#transformationFunction
     */
    public com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction getTransformationFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTransformationFunction();
            case DEFAULT:
            case NULL:
                if (_transformationFunctionField!= null) {
                    return _transformationFunctionField;
                } else {
                    Object __rawValue = super._map.get("transformationFunction");
                    _transformationFunctionField = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction(__rawValue));
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
     * @see PinotDataSourceAnchor.Fields#transformationFunction
     */
    @Nonnull
    public com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction getTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return _transformationFunctionField;
        } else {
            Object __rawValue = super._map.get("transformationFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("transformationFunction");
            }
            _transformationFunctionField = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction(__rawValue));
            return _transformationFunctionField;
        }
    }

    /**
     * Setter for transformationFunction
     * 
     * @see PinotDataSourceAnchor.Fields#transformationFunction
     */
    public PinotDataSourceAnchor setTransformationFunction(com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor");
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
     * @see PinotDataSourceAnchor.Fields#transformationFunction
     */
    public PinotDataSourceAnchor setTransformationFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see PinotDataSourceAnchor.Fields#keyPlaceholders
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
     * @see PinotDataSourceAnchor.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see PinotDataSourceAnchor.Fields#keyPlaceholders
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
     * @see PinotDataSourceAnchor.Fields#keyPlaceholders
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
     * @see PinotDataSourceAnchor.Fields#keyPlaceholders
     */
    public PinotDataSourceAnchor setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor");
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
     * @see PinotDataSourceAnchor.Fields#keyPlaceholders
     */
    public PinotDataSourceAnchor setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for source
     * 
     * @see PinotDataSourceAnchor.Fields#source
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
     * @see PinotDataSourceAnchor.Fields#source
     */
    public void removeSource() {
        super._map.remove("source");
    }

    /**
     * Getter for source
     * 
     * @see PinotDataSourceAnchor.Fields#source
     */
    public PinotDataSource getSource(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSource();
            case DEFAULT:
            case NULL:
                if (_sourceField!= null) {
                    return _sourceField;
                } else {
                    Object __rawValue = super._map.get("source");
                    _sourceField = ((__rawValue == null)?null:new PinotDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
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
     * @see PinotDataSourceAnchor.Fields#source
     */
    @Nonnull
    public PinotDataSource getSource() {
        if (_sourceField!= null) {
            return _sourceField;
        } else {
            Object __rawValue = super._map.get("source");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("source");
            }
            _sourceField = ((__rawValue == null)?null:new PinotDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sourceField;
        }
    }

    /**
     * Setter for source
     * 
     * @see PinotDataSourceAnchor.Fields#source
     */
    public PinotDataSourceAnchor setSource(PinotDataSource value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSource(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field source of com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor");
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
     * @see PinotDataSourceAnchor.Fields#source
     */
    public PinotDataSourceAnchor setSource(
        @Nonnull
        PinotDataSource value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field source of com.linkedin.feathr.featureDataModel.PinotDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "source", value.data());
            _sourceField = value;
        }
        return this;
    }

    @Override
    public PinotDataSourceAnchor clone()
        throws CloneNotSupportedException
    {
        PinotDataSourceAnchor __clone = ((PinotDataSourceAnchor) super.clone());
        __clone.__changeListener = new PinotDataSourceAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public PinotDataSourceAnchor copy()
        throws CloneNotSupportedException
    {
        PinotDataSourceAnchor __copy = ((PinotDataSourceAnchor) super.copy());
        __copy._transformationFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy._sourceField = null;
        __copy.__changeListener = new PinotDataSourceAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final PinotDataSourceAnchor __objectRef;

        private ChangeListener(PinotDataSourceAnchor reference) {
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
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.Fields transformationFunction() {
            return new com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.Fields(getPathComponents(), "transformationFunction");
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
         * Defines a Pinot data source. Pinot is a realtime distributed OLAP datastore. See PinotDataSource for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.PinotDataSource.Fields source() {
            return new com.linkedin.feathr.featureDataModel.PinotDataSource.Fields(getPathComponents(), "source");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask _transformationFunctionMask;
        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.PinotDataSource.ProjectionMask _sourceMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public PinotDataSourceAnchor.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?com.linkedin.feathr.featureDataModel.TransformationFunctionForOnlineDataSource.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, UDF) to produce feature value from an online data source.
         * 
         */
        public PinotDataSourceAnchor.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public PinotDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public PinotDataSourceAnchor.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public PinotDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public PinotDataSourceAnchor.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
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
         * Defines a Pinot data source. Pinot is a realtime distributed OLAP datastore. See PinotDataSource for more details.
         * 
         */
        public PinotDataSourceAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.PinotDataSource.ProjectionMask, com.linkedin.feathr.featureDataModel.PinotDataSource.ProjectionMask> nestedMask) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?PinotDataSource.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            return this;
        }

        /**
         * Defines a Pinot data source. Pinot is a realtime distributed OLAP datastore. See PinotDataSource for more details.
         * 
         */
        public PinotDataSourceAnchor.ProjectionMask withSource() {
            _sourceMask = null;
            getDataMap().put("source", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
