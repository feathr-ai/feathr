
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
 * Sequential join is useful when the feature data of one feature is used as the key values for another feature. Unlike a traditional FeatureSourcesAnchor, the source of SequentialJoinFeatureSourcesAnchor include a base feature and an expansion feature. A high-level data flow is: feature data of the base feature is used as key values to compute the expansion feature, the result will contain multiple feature data for different key values, then a reduce function will be applied to converge them into a single feature data. For more details, refer to go/frame/sequentialjoin.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SequentialJoinFeatureSourcesAnchor.pdl.")
public class SequentialJoinFeatureSourcesAnchor
    extends RecordTemplate
{

    private final static SequentialJoinFeatureSourcesAnchor.Fields _fields = new SequentialJoinFeatureSourcesAnchor.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Sequential join is useful when the feature data of one feature is used as the key values for another feature. Unlike a traditional FeatureSourcesAnchor, the source of SequentialJoinFeatureSourcesAnchor include a base feature and an expansion feature. A high-level data flow is: feature data of the base feature is used as key values to compute the expansion feature, the result will contain multiple feature data for different key values, then a reduce function will be applied to converge them into a single feature data. For more details, refer to go/frame/sequentialjoin.*/@OfflineAnchor@OnlineAnchor,record SequentialJoinFeatureSourcesAnchor includes/**Represents a list of placeholders in which key values will be assigned dynamically at inference or training time. KeyPlaceholders allows feature producers to author feature definition without needing the actual key value. For example, at the authoring time of defining skill_similarity feature, KeyPlaceholders can be defined as [{keyPlaceholderRef: memberId, valueType: LONG, ...}, {keyPlaceholderRef: jobId, valueType: STRING, ...}], the keyPlaceholderRefs (ie. memberId and jobId) can be used to uniformly reference these KeyPlaceholders in the feature definition. For example, specifying a RestliDataSource with memberId being embedded in a memberUrn as the primary key and jobId being embedded in a request parameter value. Another example is: if skills_similarity is derived from performing cosine similarity on top of member_skills feature and job_skills feature, at high-level the feature definition should be specified as computing member_skills with given memberId and computing job_skills with given jobId then performing a cosine similarity. By referencing keyPlaceholderRefs (ie. memberId, jobId) in related FeatureSources, it ensures actual key values can be propogated all the way from skills_similiarity to the corresponding FeatureSource (ie. member_skills or job_skills). At inference time, the actual key values of these two KeyPlaceholders will come from inference requests. At training time, the actual key values of these two KeyPlaceholders will come from applying specified transformations on each row of feature's data source. Key placeholders are a top-level concept in FeatureAnchor and will be shared by almost all types of FeatureAnchors. This class is expected to be included so the definitions of enclosed fields can be reused.*/record KeyPlaceholders{/**Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.*/keyPlaceholders:array[/**Represents a placeholder in which key value will be assigned dynamically at inference or training time. The key placeholder allows feature producers to author feature definition without needing the actual key value. A feature can have zero, one or multiple key placeholders. Refer to KeyPlaceholders for a full example. TODO (PROML-9870): Add EntityType (MEMBER, JOB, COMPANY, etc) and ValueType (INT, LONG, STRING, etc) to the class.*/record KeyPlaceholder{/**Represents an identifier of KeyPlaceholder and it should be meaningful (eg. memberId) and unique in the scope of a feature's definition. Other parts of the feature definition can reference this KeyPlaceholder by its keyPlaceholderRef. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRef:/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string}]=[]}{/**Represents the base feature, its feature data is used as key values to compute the expansion feature.*/base:/**This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.*/record FeatureSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**This is the unique id for the MlFeatureVersionEntity used as the source.*/urn:string/**A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).*/alias:optional string/**Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRefs:array[KeyPlaceholderRef]=[]}/**After feature data of the base feature is computed, it may require additional transformation to match expected key format of the expansion feature.*/expansionKeyFunction:record ExpansionKeyFunction includes KeyPlaceholders{/**This transformation ensures the feature data of the base feature can be converted to the expected key format of the expansion feature.*/keyFunction:union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**Represents a transformation function that always returns the same value as the input. See https://en.wikipedia.org/wiki/Identity_function.*/record IdentityFunction{}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}]}/**Represents the expansion feature, which uses the output of expansionKeyFunction as key values to compute feature data of the expansion feature.*/expansion:FeatureSource/**The computation of the expansion feature will fanout to multiple feature data for different key values, a reduce function will be applied after the fanout to ensure a single feature data being returned.*/reductionFunction:typeref ReductionFunction=union[enum StandardAggregation{/** Average. Apply to 0-rank numeric scalar. For example, base feature gets a job's applicants, and expansion feature gets the age of an applicant, when average is specified, the resulting feature data is the average age of all applicants for a job. */AVG/** Max. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MAX/** Min. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MIN/** Sum. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */SUM/** Union. This combines multiple feature data into a collection. For example, base feature gets member's skill ids, and expansion feature gets a skill name based on skill id, when union is specified, the resulting feature data is a list of skill names for a given member. */UNION/** Element-wise Average, which apply average operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. For example, base feature gets member's skill ids, and expansion feature gets a skill embedding based on skill id, when ELEMENTWISE_AVG is specified, the resulting feature data is a single embedding by element-wise averaging all skill embeddings of the member. */ELEMENTWISE_AVG/** Element-wise Min, which apply min operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MIN/** Element-wise Max, which apply max operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MAX/** Element-wise Sum, which apply sum operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_SUM}]}", SchemaFormatType.PDL));
    private KeyPlaceholderArray _keyPlaceholdersField = null;
    private FeatureSource _baseField = null;
    private ExpansionKeyFunction _expansionKeyFunctionField = null;
    private FeatureSource _expansionField = null;
    private ReductionFunction _reductionFunctionField = null;
    private SequentialJoinFeatureSourcesAnchor.ChangeListener __changeListener = new SequentialJoinFeatureSourcesAnchor.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyPlaceholders = SCHEMA.getField("keyPlaceholders");
    private final static KeyPlaceholderArray DEFAULT_KeyPlaceholders;
    private final static RecordDataSchema.Field FIELD_Base = SCHEMA.getField("base");
    private final static RecordDataSchema.Field FIELD_ExpansionKeyFunction = SCHEMA.getField("expansionKeyFunction");
    private final static RecordDataSchema.Field FIELD_Expansion = SCHEMA.getField("expansion");
    private final static RecordDataSchema.Field FIELD_ReductionFunction = SCHEMA.getField("reductionFunction");

    static {
        DEFAULT_KeyPlaceholders = ((FIELD_KeyPlaceholders.getDefault() == null)?null:new KeyPlaceholderArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholders.getDefault(), DataList.class)));
    }

    public SequentialJoinFeatureSourcesAnchor() {
        super(new DataMap(7, 0.75F), SCHEMA, 7);
        addChangeListener(__changeListener);
    }

    public SequentialJoinFeatureSourcesAnchor(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static SequentialJoinFeatureSourcesAnchor.Fields fields() {
        return _fields;
    }

    public static SequentialJoinFeatureSourcesAnchor.ProjectionMask createMask() {
        return new SequentialJoinFeatureSourcesAnchor.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyPlaceholders
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see SequentialJoinFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public void removeKeyPlaceholders() {
        super._map.remove("keyPlaceholders");
    }

    /**
     * Getter for keyPlaceholders
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see SequentialJoinFeatureSourcesAnchor.Fields#keyPlaceholders
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
     * @see SequentialJoinFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public SequentialJoinFeatureSourcesAnchor setKeyPlaceholders(KeyPlaceholderArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholders(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholders of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
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
     * @see SequentialJoinFeatureSourcesAnchor.Fields#keyPlaceholders
     */
    public SequentialJoinFeatureSourcesAnchor setKeyPlaceholders(
        @Nonnull
        KeyPlaceholderArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholders of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholders", value.data());
            _keyPlaceholdersField = value;
        }
        return this;
    }

    /**
     * Existence checker for base
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#base
     */
    public boolean hasBase() {
        if (_baseField!= null) {
            return true;
        }
        return super._map.containsKey("base");
    }

    /**
     * Remover for base
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#base
     */
    public void removeBase() {
        super._map.remove("base");
    }

    /**
     * Getter for base
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#base
     */
    public FeatureSource getBase(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getBase();
            case DEFAULT:
            case NULL:
                if (_baseField!= null) {
                    return _baseField;
                } else {
                    Object __rawValue = super._map.get("base");
                    _baseField = ((__rawValue == null)?null:new FeatureSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _baseField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for base
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#base
     */
    @Nonnull
    public FeatureSource getBase() {
        if (_baseField!= null) {
            return _baseField;
        } else {
            Object __rawValue = super._map.get("base");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("base");
            }
            _baseField = ((__rawValue == null)?null:new FeatureSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _baseField;
        }
    }

    /**
     * Setter for base
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#base
     */
    public SequentialJoinFeatureSourcesAnchor setBase(FeatureSource value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setBase(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field base of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "base", value.data());
                    _baseField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeBase();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "base", value.data());
                    _baseField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "base", value.data());
                    _baseField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for base
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#base
     */
    public SequentialJoinFeatureSourcesAnchor setBase(
        @Nonnull
        FeatureSource value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field base of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "base", value.data());
            _baseField = value;
        }
        return this;
    }

    /**
     * Existence checker for expansionKeyFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansionKeyFunction
     */
    public boolean hasExpansionKeyFunction() {
        if (_expansionKeyFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("expansionKeyFunction");
    }

    /**
     * Remover for expansionKeyFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansionKeyFunction
     */
    public void removeExpansionKeyFunction() {
        super._map.remove("expansionKeyFunction");
    }

    /**
     * Getter for expansionKeyFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansionKeyFunction
     */
    public ExpansionKeyFunction getExpansionKeyFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getExpansionKeyFunction();
            case DEFAULT:
            case NULL:
                if (_expansionKeyFunctionField!= null) {
                    return _expansionKeyFunctionField;
                } else {
                    Object __rawValue = super._map.get("expansionKeyFunction");
                    _expansionKeyFunctionField = ((__rawValue == null)?null:new ExpansionKeyFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _expansionKeyFunctionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for expansionKeyFunction
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansionKeyFunction
     */
    @Nonnull
    public ExpansionKeyFunction getExpansionKeyFunction() {
        if (_expansionKeyFunctionField!= null) {
            return _expansionKeyFunctionField;
        } else {
            Object __rawValue = super._map.get("expansionKeyFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("expansionKeyFunction");
            }
            _expansionKeyFunctionField = ((__rawValue == null)?null:new ExpansionKeyFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _expansionKeyFunctionField;
        }
    }

    /**
     * Setter for expansionKeyFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansionKeyFunction
     */
    public SequentialJoinFeatureSourcesAnchor setExpansionKeyFunction(ExpansionKeyFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setExpansionKeyFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field expansionKeyFunction of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "expansionKeyFunction", value.data());
                    _expansionKeyFunctionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeExpansionKeyFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "expansionKeyFunction", value.data());
                    _expansionKeyFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "expansionKeyFunction", value.data());
                    _expansionKeyFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for expansionKeyFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansionKeyFunction
     */
    public SequentialJoinFeatureSourcesAnchor setExpansionKeyFunction(
        @Nonnull
        ExpansionKeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field expansionKeyFunction of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "expansionKeyFunction", value.data());
            _expansionKeyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for expansion
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansion
     */
    public boolean hasExpansion() {
        if (_expansionField!= null) {
            return true;
        }
        return super._map.containsKey("expansion");
    }

    /**
     * Remover for expansion
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansion
     */
    public void removeExpansion() {
        super._map.remove("expansion");
    }

    /**
     * Getter for expansion
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansion
     */
    public FeatureSource getExpansion(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getExpansion();
            case DEFAULT:
            case NULL:
                if (_expansionField!= null) {
                    return _expansionField;
                } else {
                    Object __rawValue = super._map.get("expansion");
                    _expansionField = ((__rawValue == null)?null:new FeatureSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _expansionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for expansion
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansion
     */
    @Nonnull
    public FeatureSource getExpansion() {
        if (_expansionField!= null) {
            return _expansionField;
        } else {
            Object __rawValue = super._map.get("expansion");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("expansion");
            }
            _expansionField = ((__rawValue == null)?null:new FeatureSource(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _expansionField;
        }
    }

    /**
     * Setter for expansion
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansion
     */
    public SequentialJoinFeatureSourcesAnchor setExpansion(FeatureSource value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setExpansion(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field expansion of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "expansion", value.data());
                    _expansionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeExpansion();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "expansion", value.data());
                    _expansionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "expansion", value.data());
                    _expansionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for expansion
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#expansion
     */
    public SequentialJoinFeatureSourcesAnchor setExpansion(
        @Nonnull
        FeatureSource value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field expansion of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "expansion", value.data());
            _expansionField = value;
        }
        return this;
    }

    /**
     * Existence checker for reductionFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#reductionFunction
     */
    public boolean hasReductionFunction() {
        if (_reductionFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("reductionFunction");
    }

    /**
     * Remover for reductionFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#reductionFunction
     */
    public void removeReductionFunction() {
        super._map.remove("reductionFunction");
    }

    /**
     * Getter for reductionFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#reductionFunction
     */
    public ReductionFunction getReductionFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getReductionFunction();
            case DEFAULT:
            case NULL:
                if (_reductionFunctionField!= null) {
                    return _reductionFunctionField;
                } else {
                    Object __rawValue = super._map.get("reductionFunction");
                    _reductionFunctionField = ((__rawValue == null)?null:new ReductionFunction(__rawValue));
                    return _reductionFunctionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for reductionFunction
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#reductionFunction
     */
    @Nonnull
    public ReductionFunction getReductionFunction() {
        if (_reductionFunctionField!= null) {
            return _reductionFunctionField;
        } else {
            Object __rawValue = super._map.get("reductionFunction");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("reductionFunction");
            }
            _reductionFunctionField = ((__rawValue == null)?null:new ReductionFunction(__rawValue));
            return _reductionFunctionField;
        }
    }

    /**
     * Setter for reductionFunction
     * 
     * @see SequentialJoinFeatureSourcesAnchor.Fields#reductionFunction
     */
    public SequentialJoinFeatureSourcesAnchor setReductionFunction(ReductionFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setReductionFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field reductionFunction of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "reductionFunction", value.data());
                    _reductionFunctionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeReductionFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "reductionFunction", value.data());
                    _reductionFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "reductionFunction", value.data());
                    _reductionFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for reductionFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SequentialJoinFeatureSourcesAnchor.Fields#reductionFunction
     */
    public SequentialJoinFeatureSourcesAnchor setReductionFunction(
        @Nonnull
        ReductionFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field reductionFunction of com.linkedin.feathr.featureDataModel.SequentialJoinFeatureSourcesAnchor to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "reductionFunction", value.data());
            _reductionFunctionField = value;
        }
        return this;
    }

    @Override
    public SequentialJoinFeatureSourcesAnchor clone()
        throws CloneNotSupportedException
    {
        SequentialJoinFeatureSourcesAnchor __clone = ((SequentialJoinFeatureSourcesAnchor) super.clone());
        __clone.__changeListener = new SequentialJoinFeatureSourcesAnchor.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public SequentialJoinFeatureSourcesAnchor copy()
        throws CloneNotSupportedException
    {
        SequentialJoinFeatureSourcesAnchor __copy = ((SequentialJoinFeatureSourcesAnchor) super.copy());
        __copy._expansionKeyFunctionField = null;
        __copy._keyPlaceholdersField = null;
        __copy._reductionFunctionField = null;
        __copy._baseField = null;
        __copy._expansionField = null;
        __copy.__changeListener = new SequentialJoinFeatureSourcesAnchor.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final SequentialJoinFeatureSourcesAnchor __objectRef;

        private ChangeListener(SequentialJoinFeatureSourcesAnchor reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "expansionKeyFunction":
                    __objectRef._expansionKeyFunctionField = null;
                    break;
                case "keyPlaceholders":
                    __objectRef._keyPlaceholdersField = null;
                    break;
                case "reductionFunction":
                    __objectRef._reductionFunctionField = null;
                    break;
                case "base":
                    __objectRef._baseField = null;
                    break;
                case "expansion":
                    __objectRef._expansionField = null;
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
         * Represents the base feature, its feature data is used as key values to compute the expansion feature.
         * 
         */
        public com.linkedin.feathr.featureDataModel.FeatureSource.Fields base() {
            return new com.linkedin.feathr.featureDataModel.FeatureSource.Fields(getPathComponents(), "base");
        }

        /**
         * After feature data of the base feature is computed, it may require additional transformation to match expected key format of the expansion feature.
         * 
         */
        public com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.Fields expansionKeyFunction() {
            return new com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.Fields(getPathComponents(), "expansionKeyFunction");
        }

        /**
         * Represents the expansion feature, which uses the output of expansionKeyFunction as key values to compute feature data of the expansion feature.
         * 
         */
        public com.linkedin.feathr.featureDataModel.FeatureSource.Fields expansion() {
            return new com.linkedin.feathr.featureDataModel.FeatureSource.Fields(getPathComponents(), "expansion");
        }

        /**
         * The computation of the expansion feature will fanout to multiple feature data for different key values, a reduce function will be applied after the fanout to ensure a single feature data being returned.
         * 
         */
        public com.linkedin.feathr.featureDataModel.ReductionFunction.Fields reductionFunction() {
            return new com.linkedin.feathr.featureDataModel.ReductionFunction.Fields(getPathComponents(), "reductionFunction");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask _keyPlaceholdersMask;
        private com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask _baseMask;
        private com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.ProjectionMask _expansionKeyFunctionMask;
        private com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask _expansionMask;
        private com.linkedin.feathr.featureDataModel.ReductionFunction.ProjectionMask _reductionFunctionMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask) {
            _keyPlaceholdersMask = nestedMask.apply(((_keyPlaceholdersMask == null)?KeyPlaceholderArray.createMask():_keyPlaceholdersMask));
            getDataMap().put("keyPlaceholders", _keyPlaceholdersMask.getDataMap());
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders() {
            _keyPlaceholdersMask = null;
            getDataMap().put("keyPlaceholders", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents an array of key placeholders. A feature can have zero, one or multiple key placeholders.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Function<com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask, com.linkedin.feathr.featureDataModel.KeyPlaceholderArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withKeyPlaceholders(Integer start, Integer count) {
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
         * Represents the base feature, its feature data is used as key values to compute the expansion feature.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withBase(Function<com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask> nestedMask) {
            _baseMask = nestedMask.apply(((_baseMask == null)?FeatureSource.createMask():_baseMask));
            getDataMap().put("base", _baseMask.getDataMap());
            return this;
        }

        /**
         * Represents the base feature, its feature data is used as key values to compute the expansion feature.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withBase() {
            _baseMask = null;
            getDataMap().put("base", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * After feature data of the base feature is computed, it may require additional transformation to match expected key format of the expansion feature.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withExpansionKeyFunction(Function<com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.ExpansionKeyFunction.ProjectionMask> nestedMask) {
            _expansionKeyFunctionMask = nestedMask.apply(((_expansionKeyFunctionMask == null)?ExpansionKeyFunction.createMask():_expansionKeyFunctionMask));
            getDataMap().put("expansionKeyFunction", _expansionKeyFunctionMask.getDataMap());
            return this;
        }

        /**
         * After feature data of the base feature is computed, it may require additional transformation to match expected key format of the expansion feature.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withExpansionKeyFunction() {
            _expansionKeyFunctionMask = null;
            getDataMap().put("expansionKeyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the expansion feature, which uses the output of expansionKeyFunction as key values to compute feature data of the expansion feature.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withExpansion(Function<com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask, com.linkedin.feathr.featureDataModel.FeatureSource.ProjectionMask> nestedMask) {
            _expansionMask = nestedMask.apply(((_expansionMask == null)?FeatureSource.createMask():_expansionMask));
            getDataMap().put("expansion", _expansionMask.getDataMap());
            return this;
        }

        /**
         * Represents the expansion feature, which uses the output of expansionKeyFunction as key values to compute feature data of the expansion feature.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withExpansion() {
            _expansionMask = null;
            getDataMap().put("expansion", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The computation of the expansion feature will fanout to multiple feature data for different key values, a reduce function will be applied after the fanout to ensure a single feature data being returned.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withReductionFunction(Function<com.linkedin.feathr.featureDataModel.ReductionFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.ReductionFunction.ProjectionMask> nestedMask) {
            _reductionFunctionMask = nestedMask.apply(((_reductionFunctionMask == null)?ReductionFunction.createMask():_reductionFunctionMask));
            getDataMap().put("reductionFunction", _reductionFunctionMask.getDataMap());
            return this;
        }

        /**
         * The computation of the expansion feature will fanout to multiple feature data for different key values, a reduce function will be applied after the fanout to ensure a single feature data being returned.
         * 
         */
        public SequentialJoinFeatureSourcesAnchor.ProjectionMask withReductionFunction() {
            _reductionFunctionMask = null;
            getDataMap().put("reductionFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
