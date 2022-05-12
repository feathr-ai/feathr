
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
 * Represents an offline anchor with observation data passthrough data source, which is used for features that already exist in the observation data.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/ObservationPassthroughDataSourceAnchor.pdl.")
public class ObservationPassthroughDataSourceAnchor
    extends RecordTemplate
{

    private final static ObservationPassthroughDataSourceAnchor.Fields _fields = new ObservationPassthroughDataSourceAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents an offline anchor with observation data passthrough data source, which is used for features that already exist in the observation data.*/@OfflineAnchor,record ObservationPassthroughDataSourceAnchor includes/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Defines an observation data passthrough data source, which is used for features that already exist in the observation data. See ObservationPassthroughDataSource for more details.*/source:/**Represents a observation data passthrough data source. Passthrough data sources are used when the data are not from external sources. It's commonly used for features that already exist in the observation data. See Pass-through Features section in go/frameoffline.*/record ObservationPassthroughDataSource includes/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineDataSourceKey{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{}/**Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF) to produce feature value for an observation data passthrough data source.*/transformationFunction:union[MvelExpression,SparkSqlExpression,UserDefinedFunction]}", SchemaFormatType.PDL));
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private ObservationPassthroughDataSource _sourceField = null;
    private ObservationPassthroughDataSourceAnchor.TransformationFunction _transformationFunctionField = null;
    private ObservationPassthroughDataSourceAnchor.ChangeListener __changeListener = new ObservationPassthroughDataSourceAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_Source = SCHEMA.getField("source");
    private final static RecordDataSchema.Field FIELD_TransformationFunction = SCHEMA.getField("transformationFunction");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public ObservationPassthroughDataSourceAnchor() {
        super(new DataMap(4, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public ObservationPassthroughDataSourceAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static ObservationPassthroughDataSourceAnchor.Fields fields() {
        return _fields;
    }

    public static ObservationPassthroughDataSourceAnchor.ProjectionMask createMask() {
        return new ObservationPassthroughDataSourceAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#keyPlaceholders
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#keyPlaceholders
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#keyPlaceholders
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#keyPlaceholders
     */
    public ObservationPassthroughDataSourceAnchor setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor");
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#keyPlaceholders
     */
    public ObservationPassthroughDataSourceAnchor setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for source
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#source
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#source
     */
    public void removeSource() {
        super._map.remove("source");
    }

    /**
     * Getter for source
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#source
     */
    public ObservationPassthroughDataSource getSource(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSource();
            case DEFAULT:
            case NULL:
                if (_sourceField!= null) {
                    return _sourceField;
                } else {
                    Object __rawValue = super._map.get("source");
                    _sourceField = ((__rawValue == null)?null:new ObservationPassthroughDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#source
     */
    @Nonnull
    public ObservationPassthroughDataSource getSource() {
        if (_sourceField!= null) {
            return _sourceField;
        } else {
            Object __rawValue = super._map.get("source");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("source");
            }
            _sourceField = ((__rawValue == null)?null:new ObservationPassthroughDataSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sourceField;
        }
    }

    /**
     * Setter for source
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#source
     */
    public ObservationPassthroughDataSourceAnchor setSource(ObservationPassthroughDataSource value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSource(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field source of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor");
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#source
     */
    public ObservationPassthroughDataSourceAnchor setSource(
        @Nonnull
        ObservationPassthroughDataSource value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field source of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "source", value.data());
            _sourceField = value;
        }
        return this;
    }

    /**
     * Existence checker for transformationFunction
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#transformationFunction
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public void removeTransformationFunction() {
        super._map.remove("transformationFunction");
    }

    /**
     * Getter for transformationFunction
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public ObservationPassthroughDataSourceAnchor.TransformationFunction getTransformationFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTransformationFunction();
            case DEFAULT:
            case NULL:
                if (_transformationFunctionField!= null) {
                    return _transformationFunctionField;
                } else {
                    Object __rawValue = super._map.get("transformationFunction");
                    _transformationFunctionField = ((__rawValue == null)?null:new ObservationPassthroughDataSourceAnchor.TransformationFunction(__rawValue));
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    @Nonnull
    public ObservationPassthroughDataSourceAnchor.TransformationFunction getTransformationFunction() {
        if (_transformationFunctionField!= null) {
            return _transformationFunctionField;
        } else {
            Object __rawValue = super._map.get("transformationFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("transformationFunction");
            }
            _transformationFunctionField = ((__rawValue == null)?null:new ObservationPassthroughDataSourceAnchor.TransformationFunction(__rawValue));
            return _transformationFunctionField;
        }
    }

    /**
     * Setter for transformationFunction
     * 
     * @see ObservationPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public ObservationPassthroughDataSourceAnchor setTransformationFunction(ObservationPassthroughDataSourceAnchor.TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTransformationFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field transformationFunction of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor");
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
     * @see ObservationPassthroughDataSourceAnchor.Fields#transformationFunction
     */
    public ObservationPassthroughDataSourceAnchor setTransformationFunction(
        @Nonnull
        ObservationPassthroughDataSourceAnchor.TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field transformationFunction of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "transformationFunction", value.data());
            _transformationFunctionField = value;
        }
        return this;
    }

    @Override
    public ObservationPassthroughDataSourceAnchor clone()
        throws CloneNotSupportedException
    {
        ObservationPassthroughDataSourceAnchor __clone = ((ObservationPassthroughDataSourceAnchor) super.clone());
        __clone.__changeListener = new ObservationPassthroughDataSourceAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public ObservationPassthroughDataSourceAnchor copy()
        throws CloneNotSupportedException
    {
        ObservationPassthroughDataSourceAnchor __copy = ((ObservationPassthroughDataSourceAnchor) super.copy());
        __copy._transformationFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy._sourceField = null;
        __copy.__changeListener = new ObservationPassthroughDataSourceAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final ObservationPassthroughDataSourceAnchor __objectRef;

        private ChangeListener(ObservationPassthroughDataSourceAnchor reference) {
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
         * Defines an observation data passthrough data source, which is used for features that already exist in the observation data. See ObservationPassthroughDataSource for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource.Fields source() {
            return new com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource.Fields(getPathComponents(), "source");
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF) to produce feature value for an observation data passthrough data source.
         * 
         */
        public com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction.Fields transformationFunction() {
            return new com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction.Fields(getPathComponents(), "transformationFunction");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource.ProjectionMask _sourceMask;
        private com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask _transformationFunctionMask;

        ProjectionMask() {
            super(4);
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
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
         * Defines an observation data passthrough data source, which is used for features that already exist in the observation data. See ObservationPassthroughDataSource for more details.
         * 
         */
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withSource(Function<com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource.ProjectionMask, com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource.ProjectionMask> nestedMask) {
            _sourceMask = nestedMask.apply(((_sourceMask == null)?ObservationPassthroughDataSource.createMask():_sourceMask));
            getDataMap().put("source", _sourceMask.getDataMap());
            return this;
        }

        /**
         * Defines an observation data passthrough data source, which is used for features that already exist in the observation data. See ObservationPassthroughDataSource for more details.
         * 
         */
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withSource() {
            _sourceMask = null;
            getDataMap().put("source", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF) to produce feature value for an observation data passthrough data source.
         * 
         */
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withTransformationFunction(Function<com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask> nestedMask) {
            _transformationFunctionMask = nestedMask.apply(((_transformationFunctionMask == null)?ObservationPassthroughDataSourceAnchor.TransformationFunction.createMask():_transformationFunctionMask));
            getDataMap().put("transformationFunction", _transformationFunctionMask.getDataMap());
            return this;
        }

        /**
         * Defines the supported transformation logic (eg. MVEL, Spark SQL, UDF) to produce feature value for an observation data passthrough data source.
         * 
         */
        public ObservationPassthroughDataSourceAnchor.ProjectionMask withTransformationFunction() {
            _transformationFunctionMask = null;
            getDataMap().put("transformationFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/ObservationPassthroughDataSourceAnchor.pdl.")
    public static class TransformationFunction
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}}{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}{namespace com.linkedin.feathr.featureDataModel/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.MvelExpression _mvelExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private com.linkedin.feathr.featureDataModel.UserDefinedFunction _userDefinedFunctionMember = null;
        private ObservationPassthroughDataSourceAnchor.TransformationFunction.ChangeListener __changeListener = new ObservationPassthroughDataSourceAnchor.TransformationFunction.ChangeListener(this);
        private final static DataSchema MEMBER_MvelExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.MvelExpression");
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        private final static DataSchema MEMBER_UserDefinedFunction = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.UserDefinedFunction");

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

        public static ObservationPassthroughDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.MvelExpression value) {
            ObservationPassthroughDataSourceAnchor.TransformationFunction newUnion = new ObservationPassthroughDataSourceAnchor.TransformationFunction();
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

        public static ObservationPassthroughDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            ObservationPassthroughDataSourceAnchor.TransformationFunction newUnion = new ObservationPassthroughDataSourceAnchor.TransformationFunction();
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

        public static ObservationPassthroughDataSourceAnchor.TransformationFunction create(com.linkedin.feathr.featureDataModel.UserDefinedFunction value) {
            ObservationPassthroughDataSourceAnchor.TransformationFunction newUnion = new ObservationPassthroughDataSourceAnchor.TransformationFunction();
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

        public static ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask createMask() {
            return new ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask();
        }

        @Override
        public ObservationPassthroughDataSourceAnchor.TransformationFunction clone()
            throws CloneNotSupportedException
        {
            ObservationPassthroughDataSourceAnchor.TransformationFunction __clone = ((ObservationPassthroughDataSourceAnchor.TransformationFunction) super.clone());
            __clone.__changeListener = new ObservationPassthroughDataSourceAnchor.TransformationFunction.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public ObservationPassthroughDataSourceAnchor.TransformationFunction copy()
            throws CloneNotSupportedException
        {
            ObservationPassthroughDataSourceAnchor.TransformationFunction __copy = ((ObservationPassthroughDataSourceAnchor.TransformationFunction) super.copy());
            __copy._mvelExpressionMember = null;
            __copy._sparkSqlExpressionMember = null;
            __copy._userDefinedFunctionMember = null;
            __copy.__changeListener = new ObservationPassthroughDataSourceAnchor.TransformationFunction.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final ObservationPassthroughDataSourceAnchor.TransformationFunction __objectRef;

            private ChangeListener(ObservationPassthroughDataSourceAnchor.TransformationFunction reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.MvelExpression":
                        __objectRef._mvelExpressionMember = null;
                        break;
                    case "com.linkedin.feathr.featureDataModel.SparkSqlExpression":
                        __objectRef._sparkSqlExpressionMember = null;
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

            public com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            }

            public com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields UserDefinedFunction() {
                return new com.linkedin.feathr.featureDataModel.UserDefinedFunction.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.UserDefinedFunction");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask _MvelExpressionMask;
            private com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;
            private com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask _UserDefinedFunctionMask;

            ProjectionMask() {
                super(4);
            }

            public ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask withMvelExpression(Function<com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.MvelExpression.ProjectionMask> nestedMask) {
                _MvelExpressionMask = nestedMask.apply(((_MvelExpressionMask == null)?com.linkedin.feathr.featureDataModel.MvelExpression.createMask():_MvelExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.MvelExpression", _MvelExpressionMask.getDataMap());
                return this;
            }

            public ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

            public ObservationPassthroughDataSourceAnchor.TransformationFunction.ProjectionMask withUserDefinedFunction(Function<com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.UserDefinedFunction.ProjectionMask> nestedMask) {
                _UserDefinedFunctionMask = nestedMask.apply(((_UserDefinedFunctionMask == null)?com.linkedin.feathr.featureDataModel.UserDefinedFunction.createMask():_UserDefinedFunctionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.UserDefinedFunction", _UserDefinedFunctionMask.getDataMap());
                return this;
            }

        }

    }

}
