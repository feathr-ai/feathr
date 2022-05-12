
package com.linkedin.feathr.compute;

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
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Aggregation.pdl.")
public class Aggregation
    extends RecordTemplate
{

    private final static Aggregation.Fields _fields = new Aggregation.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record Aggregation includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{input:record NodeReference{id:NodeId,keyReference:array[record KeyReference{position:int}]}function:record AggregationFunction{operator:typeref OperatorId=string,parameters:optional map[string,string]}featureName:optional string}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private ConcreteKey _concreteKeyField = null;
    private NodeReference _inputField = null;
    private AggregationFunction _functionField = null;
    private String _featureNameField = null;
    private Aggregation.ChangeListener __changeListener = new Aggregation.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_ConcreteKey = SCHEMA.getField("concreteKey");
    private final static RecordDataSchema.Field FIELD_Input = SCHEMA.getField("input");
    private final static RecordDataSchema.Field FIELD_Function = SCHEMA.getField("function");
    private final static RecordDataSchema.Field FIELD_FeatureName = SCHEMA.getField("featureName");

    public Aggregation() {
        super(new DataMap(7, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public Aggregation(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Aggregation.Fields fields() {
        return _fields;
    }

    public static Aggregation.ProjectionMask createMask() {
        return new Aggregation.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see Aggregation.Fields#id
     */
    public boolean hasId() {
        if (_idField!= null) {
            return true;
        }
        return super._map.containsKey("id");
    }

    /**
     * Remover for id
     * 
     * @see Aggregation.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see Aggregation.Fields#id
     */
    public Integer getId(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getId();
            case DEFAULT:
            case NULL:
                if (_idField!= null) {
                    return _idField;
                } else {
                    Object __rawValue = super._map.get("id");
                    _idField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _idField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for id
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Aggregation.Fields#id
     */
    @Nonnull
    public Integer getId() {
        if (_idField!= null) {
            return _idField;
        } else {
            Object __rawValue = super._map.get("id");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("id");
            }
            _idField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _idField;
        }
    }

    /**
     * Setter for id
     * 
     * @see Aggregation.Fields#id
     */
    public Aggregation setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.Aggregation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
                    _idField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeId();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
                    _idField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
                    _idField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Aggregation.Fields#id
     */
    public Aggregation setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.Aggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see Aggregation.Fields#id
     */
    public Aggregation setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for concreteKey
     * 
     * @see Aggregation.Fields#concreteKey
     */
    public boolean hasConcreteKey() {
        if (_concreteKeyField!= null) {
            return true;
        }
        return super._map.containsKey("concreteKey");
    }

    /**
     * Remover for concreteKey
     * 
     * @see Aggregation.Fields#concreteKey
     */
    public void removeConcreteKey() {
        super._map.remove("concreteKey");
    }

    /**
     * Getter for concreteKey
     * 
     * @see Aggregation.Fields#concreteKey
     */
    public ConcreteKey getConcreteKey(GetMode mode) {
        return getConcreteKey();
    }

    /**
     * Getter for concreteKey
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Aggregation.Fields#concreteKey
     */
    @Nullable
    public ConcreteKey getConcreteKey() {
        if (_concreteKeyField!= null) {
            return _concreteKeyField;
        } else {
            Object __rawValue = super._map.get("concreteKey");
            _concreteKeyField = ((__rawValue == null)?null:new ConcreteKey(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _concreteKeyField;
        }
    }

    /**
     * Setter for concreteKey
     * 
     * @see Aggregation.Fields#concreteKey
     */
    public Aggregation setConcreteKey(ConcreteKey value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setConcreteKey(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeConcreteKey();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
                    _concreteKeyField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
                    _concreteKeyField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for concreteKey
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Aggregation.Fields#concreteKey
     */
    public Aggregation setConcreteKey(
        @Nonnull
        ConcreteKey value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field concreteKey of com.linkedin.feathr.compute.Aggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
            _concreteKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for input
     * 
     * @see Aggregation.Fields#input
     */
    public boolean hasInput() {
        if (_inputField!= null) {
            return true;
        }
        return super._map.containsKey("input");
    }

    /**
     * Remover for input
     * 
     * @see Aggregation.Fields#input
     */
    public void removeInput() {
        super._map.remove("input");
    }

    /**
     * Getter for input
     * 
     * @see Aggregation.Fields#input
     */
    public NodeReference getInput(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getInput();
            case DEFAULT:
            case NULL:
                if (_inputField!= null) {
                    return _inputField;
                } else {
                    Object __rawValue = super._map.get("input");
                    _inputField = ((__rawValue == null)?null:new NodeReference(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _inputField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for input
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Aggregation.Fields#input
     */
    @Nonnull
    public NodeReference getInput() {
        if (_inputField!= null) {
            return _inputField;
        } else {
            Object __rawValue = super._map.get("input");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("input");
            }
            _inputField = ((__rawValue == null)?null:new NodeReference(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _inputField;
        }
    }

    /**
     * Setter for input
     * 
     * @see Aggregation.Fields#input
     */
    public Aggregation setInput(NodeReference value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setInput(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field input of com.linkedin.feathr.compute.Aggregation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "input", value.data());
                    _inputField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeInput();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "input", value.data());
                    _inputField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "input", value.data());
                    _inputField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for input
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Aggregation.Fields#input
     */
    public Aggregation setInput(
        @Nonnull
        NodeReference value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field input of com.linkedin.feathr.compute.Aggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "input", value.data());
            _inputField = value;
        }
        return this;
    }

    /**
     * Existence checker for function
     * 
     * @see Aggregation.Fields#function
     */
    public boolean hasFunction() {
        if (_functionField!= null) {
            return true;
        }
        return super._map.containsKey("function");
    }

    /**
     * Remover for function
     * 
     * @see Aggregation.Fields#function
     */
    public void removeFunction() {
        super._map.remove("function");
    }

    /**
     * Getter for function
     * 
     * @see Aggregation.Fields#function
     */
    public AggregationFunction getFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFunction();
            case DEFAULT:
            case NULL:
                if (_functionField!= null) {
                    return _functionField;
                } else {
                    Object __rawValue = super._map.get("function");
                    _functionField = ((__rawValue == null)?null:new AggregationFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _functionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for function
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Aggregation.Fields#function
     */
    @Nonnull
    public AggregationFunction getFunction() {
        if (_functionField!= null) {
            return _functionField;
        } else {
            Object __rawValue = super._map.get("function");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("function");
            }
            _functionField = ((__rawValue == null)?null:new AggregationFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _functionField;
        }
    }

    /**
     * Setter for function
     * 
     * @see Aggregation.Fields#function
     */
    public Aggregation setFunction(AggregationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field function of com.linkedin.feathr.compute.Aggregation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "function", value.data());
                    _functionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "function", value.data());
                    _functionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "function", value.data());
                    _functionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for function
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Aggregation.Fields#function
     */
    public Aggregation setFunction(
        @Nonnull
        AggregationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field function of com.linkedin.feathr.compute.Aggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "function", value.data());
            _functionField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureName
     * 
     * @see Aggregation.Fields#featureName
     */
    public boolean hasFeatureName() {
        if (_featureNameField!= null) {
            return true;
        }
        return super._map.containsKey("featureName");
    }

    /**
     * Remover for featureName
     * 
     * @see Aggregation.Fields#featureName
     */
    public void removeFeatureName() {
        super._map.remove("featureName");
    }

    /**
     * Getter for featureName
     * 
     * @see Aggregation.Fields#featureName
     */
    public String getFeatureName(GetMode mode) {
        return getFeatureName();
    }

    /**
     * Getter for featureName
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Aggregation.Fields#featureName
     */
    @Nullable
    public String getFeatureName() {
        if (_featureNameField!= null) {
            return _featureNameField;
        } else {
            Object __rawValue = super._map.get("featureName");
            _featureNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _featureNameField;
        }
    }

    /**
     * Setter for featureName
     * 
     * @see Aggregation.Fields#featureName
     */
    public Aggregation setFeatureName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFeatureName(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFeatureName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "featureName", value);
                    _featureNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "featureName", value);
                    _featureNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for featureName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Aggregation.Fields#featureName
     */
    public Aggregation setFeatureName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureName of com.linkedin.feathr.compute.Aggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureName", value);
            _featureNameField = value;
        }
        return this;
    }

    @Override
    public Aggregation clone()
        throws CloneNotSupportedException
    {
        Aggregation __clone = ((Aggregation) super.clone());
        __clone.__changeListener = new Aggregation.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Aggregation copy()
        throws CloneNotSupportedException
    {
        Aggregation __copy = ((Aggregation) super.copy());
        __copy._inputField = null;
        __copy._featureNameField = null;
        __copy._functionField = null;
        __copy._idField = null;
        __copy._concreteKeyField = null;
        __copy.__changeListener = new Aggregation.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Aggregation __objectRef;

        private ChangeListener(Aggregation reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "input":
                    __objectRef._inputField = null;
                    break;
                case "featureName":
                    __objectRef._featureNameField = null;
                    break;
                case "function":
                    __objectRef._functionField = null;
                    break;
                case "id":
                    __objectRef._idField = null;
                    break;
                case "concreteKey":
                    __objectRef._concreteKeyField = null;
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

        public PathSpec id() {
            return new PathSpec(getPathComponents(), "id");
        }

        /**
         * The key for which this node is being requested.
         * If this node is a Source node, the engine can use the key to fetch or join the feature.
         * If this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but
         * should follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,
         * e.g. it can be used for identifying duplicate sections of the graph that can be pruned.)
         * 
         * TODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.
         * 
         */
        public com.linkedin.feathr.compute.ConcreteKey.Fields concreteKey() {
            return new com.linkedin.feathr.compute.ConcreteKey.Fields(getPathComponents(), "concreteKey");
        }

        public com.linkedin.feathr.compute.NodeReference.Fields input() {
            return new com.linkedin.feathr.compute.NodeReference.Fields(getPathComponents(), "input");
        }

        public com.linkedin.feathr.compute.AggregationFunction.Fields function() {
            return new com.linkedin.feathr.compute.AggregationFunction.Fields(getPathComponents(), "function");
        }

        public PathSpec featureName() {
            return new PathSpec(getPathComponents(), "featureName");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.ConcreteKey.ProjectionMask _concreteKeyMask;
        private com.linkedin.feathr.compute.NodeReference.ProjectionMask _inputMask;
        private com.linkedin.feathr.compute.AggregationFunction.ProjectionMask _functionMask;

        ProjectionMask() {
            super(7);
        }

        public Aggregation.ProjectionMask withId() {
            getDataMap().put("id", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The key for which this node is being requested.
         * If this node is a Source node, the engine can use the key to fetch or join the feature.
         * If this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but
         * should follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,
         * e.g. it can be used for identifying duplicate sections of the graph that can be pruned.)
         * 
         * TODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.
         * 
         */
        public Aggregation.ProjectionMask withConcreteKey(Function<com.linkedin.feathr.compute.ConcreteKey.ProjectionMask, com.linkedin.feathr.compute.ConcreteKey.ProjectionMask> nestedMask) {
            _concreteKeyMask = nestedMask.apply(((_concreteKeyMask == null)?ConcreteKey.createMask():_concreteKeyMask));
            getDataMap().put("concreteKey", _concreteKeyMask.getDataMap());
            return this;
        }

        /**
         * The key for which this node is being requested.
         * If this node is a Source node, the engine can use the key to fetch or join the feature.
         * If this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but
         * should follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,
         * e.g. it can be used for identifying duplicate sections of the graph that can be pruned.)
         * 
         * TODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.
         * 
         */
        public Aggregation.ProjectionMask withConcreteKey() {
            _concreteKeyMask = null;
            getDataMap().put("concreteKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Aggregation.ProjectionMask withInput(Function<com.linkedin.feathr.compute.NodeReference.ProjectionMask, com.linkedin.feathr.compute.NodeReference.ProjectionMask> nestedMask) {
            _inputMask = nestedMask.apply(((_inputMask == null)?NodeReference.createMask():_inputMask));
            getDataMap().put("input", _inputMask.getDataMap());
            return this;
        }

        public Aggregation.ProjectionMask withInput() {
            _inputMask = null;
            getDataMap().put("input", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Aggregation.ProjectionMask withFunction(Function<com.linkedin.feathr.compute.AggregationFunction.ProjectionMask, com.linkedin.feathr.compute.AggregationFunction.ProjectionMask> nestedMask) {
            _functionMask = nestedMask.apply(((_functionMask == null)?AggregationFunction.createMask():_functionMask));
            getDataMap().put("function", _functionMask.getDataMap());
            return this;
        }

        public Aggregation.ProjectionMask withFunction() {
            _functionMask = null;
            getDataMap().put("function", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Aggregation.ProjectionMask withFeatureName() {
            getDataMap().put("featureName", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
