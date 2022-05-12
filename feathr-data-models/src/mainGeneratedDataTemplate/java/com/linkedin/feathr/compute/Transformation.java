
package com.linkedin.feathr.compute;

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


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Transformation.pdl.")
public class Transformation
    extends RecordTemplate
{

    private final static Transformation.Fields _fields = new Transformation.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record Transformation includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{inputs:array[record NodeReference{id:NodeId,keyReference:array[record KeyReference{position:int}]}]function:record TransformationFunction{operator:typeref OperatorId=string,parameters:optional map[string,string]}featureName:optional string}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private ConcreteKey _concreteKeyField = null;
    private NodeReferenceArray _inputsField = null;
    private TransformationFunction _functionField = null;
    private String _featureNameField = null;
    private Transformation.ChangeListener __changeListener = new Transformation.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_ConcreteKey = SCHEMA.getField("concreteKey");
    private final static RecordDataSchema.Field FIELD_Inputs = SCHEMA.getField("inputs");
    private final static RecordDataSchema.Field FIELD_Function = SCHEMA.getField("function");
    private final static RecordDataSchema.Field FIELD_FeatureName = SCHEMA.getField("featureName");

    public Transformation() {
        super(new DataMap(7, 0.75F), SCHEMA, 4);
        addChangeListener(__changeListener);
    }

    public Transformation(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Transformation.Fields fields() {
        return _fields;
    }

    public static Transformation.ProjectionMask createMask() {
        return new Transformation.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see Transformation.Fields#id
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
     * @see Transformation.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see Transformation.Fields#id
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
     * @see Transformation.Fields#id
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
     * @see Transformation.Fields#id
     */
    public Transformation setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.Transformation");
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
     * @see Transformation.Fields#id
     */
    public Transformation setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see Transformation.Fields#id
     */
    public Transformation setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for concreteKey
     * 
     * @see Transformation.Fields#concreteKey
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
     * @see Transformation.Fields#concreteKey
     */
    public void removeConcreteKey() {
        super._map.remove("concreteKey");
    }

    /**
     * Getter for concreteKey
     * 
     * @see Transformation.Fields#concreteKey
     */
    public ConcreteKey getConcreteKey(GetMode mode) {
        return getConcreteKey();
    }

    /**
     * Getter for concreteKey
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Transformation.Fields#concreteKey
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
     * @see Transformation.Fields#concreteKey
     */
    public Transformation setConcreteKey(ConcreteKey value, SetMode mode) {
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
     * @see Transformation.Fields#concreteKey
     */
    public Transformation setConcreteKey(
        @Nonnull
        ConcreteKey value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field concreteKey of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
            _concreteKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public boolean hasInputs() {
        if (_inputsField!= null) {
            return true;
        }
        return super._map.containsKey("inputs");
    }

    /**
     * Remover for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public void removeInputs() {
        super._map.remove("inputs");
    }

    /**
     * Getter for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public NodeReferenceArray getInputs(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getInputs();
            case DEFAULT:
            case NULL:
                if (_inputsField!= null) {
                    return _inputsField;
                } else {
                    Object __rawValue = super._map.get("inputs");
                    _inputsField = ((__rawValue == null)?null:new NodeReferenceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _inputsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for inputs
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Transformation.Fields#inputs
     */
    @Nonnull
    public NodeReferenceArray getInputs() {
        if (_inputsField!= null) {
            return _inputsField;
        } else {
            Object __rawValue = super._map.get("inputs");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("inputs");
            }
            _inputsField = ((__rawValue == null)?null:new NodeReferenceArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _inputsField;
        }
    }

    /**
     * Setter for inputs
     * 
     * @see Transformation.Fields#inputs
     */
    public Transformation setInputs(NodeReferenceArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setInputs(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field inputs of com.linkedin.feathr.compute.Transformation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
                    _inputsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeInputs();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
                    _inputsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
                    _inputsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for inputs
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Transformation.Fields#inputs
     */
    public Transformation setInputs(
        @Nonnull
        NodeReferenceArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field inputs of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "inputs", value.data());
            _inputsField = value;
        }
        return this;
    }

    /**
     * Existence checker for function
     * 
     * @see Transformation.Fields#function
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
     * @see Transformation.Fields#function
     */
    public void removeFunction() {
        super._map.remove("function");
    }

    /**
     * Getter for function
     * 
     * @see Transformation.Fields#function
     */
    public TransformationFunction getFunction(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFunction();
            case DEFAULT:
            case NULL:
                if (_functionField!= null) {
                    return _functionField;
                } else {
                    Object __rawValue = super._map.get("function");
                    _functionField = ((__rawValue == null)?null:new TransformationFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
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
     * @see Transformation.Fields#function
     */
    @Nonnull
    public TransformationFunction getFunction() {
        if (_functionField!= null) {
            return _functionField;
        } else {
            Object __rawValue = super._map.get("function");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("function");
            }
            _functionField = ((__rawValue == null)?null:new TransformationFunction(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _functionField;
        }
    }

    /**
     * Setter for function
     * 
     * @see Transformation.Fields#function
     */
    public Transformation setFunction(TransformationFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field function of com.linkedin.feathr.compute.Transformation");
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
     * @see Transformation.Fields#function
     */
    public Transformation setFunction(
        @Nonnull
        TransformationFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field function of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "function", value.data());
            _functionField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureName
     * 
     * @see Transformation.Fields#featureName
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
     * @see Transformation.Fields#featureName
     */
    public void removeFeatureName() {
        super._map.remove("featureName");
    }

    /**
     * Getter for featureName
     * 
     * @see Transformation.Fields#featureName
     */
    public String getFeatureName(GetMode mode) {
        return getFeatureName();
    }

    /**
     * Getter for featureName
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Transformation.Fields#featureName
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
     * @see Transformation.Fields#featureName
     */
    public Transformation setFeatureName(String value, SetMode mode) {
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
     * @see Transformation.Fields#featureName
     */
    public Transformation setFeatureName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureName of com.linkedin.feathr.compute.Transformation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureName", value);
            _featureNameField = value;
        }
        return this;
    }

    @Override
    public Transformation clone()
        throws CloneNotSupportedException
    {
        Transformation __clone = ((Transformation) super.clone());
        __clone.__changeListener = new Transformation.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Transformation copy()
        throws CloneNotSupportedException
    {
        Transformation __copy = ((Transformation) super.copy());
        __copy._featureNameField = null;
        __copy._inputsField = null;
        __copy._functionField = null;
        __copy._idField = null;
        __copy._concreteKeyField = null;
        __copy.__changeListener = new Transformation.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Transformation __objectRef;

        private ChangeListener(Transformation reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "featureName":
                    __objectRef._featureNameField = null;
                    break;
                case "inputs":
                    __objectRef._inputsField = null;
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

        public com.linkedin.feathr.compute.NodeReferenceArray.Fields inputs() {
            return new com.linkedin.feathr.compute.NodeReferenceArray.Fields(getPathComponents(), "inputs");
        }

        public PathSpec inputs(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "inputs");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        public com.linkedin.feathr.compute.TransformationFunction.Fields function() {
            return new com.linkedin.feathr.compute.TransformationFunction.Fields(getPathComponents(), "function");
        }

        public PathSpec featureName() {
            return new PathSpec(getPathComponents(), "featureName");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.ConcreteKey.ProjectionMask _concreteKeyMask;
        private com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask _inputsMask;
        private com.linkedin.feathr.compute.TransformationFunction.ProjectionMask _functionMask;

        ProjectionMask() {
            super(7);
        }

        public Transformation.ProjectionMask withId() {
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
        public Transformation.ProjectionMask withConcreteKey(Function<com.linkedin.feathr.compute.ConcreteKey.ProjectionMask, com.linkedin.feathr.compute.ConcreteKey.ProjectionMask> nestedMask) {
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
        public Transformation.ProjectionMask withConcreteKey() {
            _concreteKeyMask = null;
            getDataMap().put("concreteKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Transformation.ProjectionMask withInputs(Function<com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask, com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask> nestedMask) {
            _inputsMask = nestedMask.apply(((_inputsMask == null)?NodeReferenceArray.createMask():_inputsMask));
            getDataMap().put("inputs", _inputsMask.getDataMap());
            return this;
        }

        public Transformation.ProjectionMask withInputs() {
            _inputsMask = null;
            getDataMap().put("inputs", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Transformation.ProjectionMask withInputs(Function<com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask, com.linkedin.feathr.compute.NodeReferenceArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _inputsMask = nestedMask.apply(((_inputsMask == null)?NodeReferenceArray.createMask():_inputsMask));
            getDataMap().put("inputs", _inputsMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("inputs").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("inputs").put("$count", count);
            }
            return this;
        }

        public Transformation.ProjectionMask withInputs(Integer start, Integer count) {
            _inputsMask = null;
            getDataMap().put("inputs", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("inputs").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("inputs").put("$count", count);
            }
            return this;
        }

        public Transformation.ProjectionMask withFunction(Function<com.linkedin.feathr.compute.TransformationFunction.ProjectionMask, com.linkedin.feathr.compute.TransformationFunction.ProjectionMask> nestedMask) {
            _functionMask = nestedMask.apply(((_functionMask == null)?TransformationFunction.createMask():_functionMask));
            getDataMap().put("function", _functionMask.getDataMap());
            return this;
        }

        public Transformation.ProjectionMask withFunction() {
            _functionMask = null;
            getDataMap().put("function", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Transformation.ProjectionMask withFeatureName() {
            getDataMap().put("featureName", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
