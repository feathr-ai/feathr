
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
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/External.pdl.")
public class External
    extends RecordTemplate
{

    private final static External.Fields _fields = new External.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record External includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{name:string}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private ConcreteKey _concreteKeyField = null;
    private String _nameField = null;
    private External.ChangeListener __changeListener = new External.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_ConcreteKey = SCHEMA.getField("concreteKey");
    private final static RecordDataSchema.Field FIELD_Name = SCHEMA.getField("name");

    public External() {
        super(new DataMap(4, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public External(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static External.Fields fields() {
        return _fields;
    }

    public static External.ProjectionMask createMask() {
        return new External.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see External.Fields#id
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
     * @see External.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see External.Fields#id
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
     * @see External.Fields#id
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
     * @see External.Fields#id
     */
    public External setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.External");
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
     * @see External.Fields#id
     */
    public External setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.External to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see External.Fields#id
     */
    public External setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for concreteKey
     * 
     * @see External.Fields#concreteKey
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
     * @see External.Fields#concreteKey
     */
    public void removeConcreteKey() {
        super._map.remove("concreteKey");
    }

    /**
     * Getter for concreteKey
     * 
     * @see External.Fields#concreteKey
     */
    public ConcreteKey getConcreteKey(GetMode mode) {
        return getConcreteKey();
    }

    /**
     * Getter for concreteKey
     * 
     * @return
     *     Optional field. Always check for null.
     * @see External.Fields#concreteKey
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
     * @see External.Fields#concreteKey
     */
    public External setConcreteKey(ConcreteKey value, SetMode mode) {
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
     * @see External.Fields#concreteKey
     */
    public External setConcreteKey(
        @Nonnull
        ConcreteKey value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field concreteKey of com.linkedin.feathr.compute.External to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
            _concreteKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for name
     * 
     * @see External.Fields#name
     */
    public boolean hasName() {
        if (_nameField!= null) {
            return true;
        }
        return super._map.containsKey("name");
    }

    /**
     * Remover for name
     * 
     * @see External.Fields#name
     */
    public void removeName() {
        super._map.remove("name");
    }

    /**
     * Getter for name
     * 
     * @see External.Fields#name
     */
    public String getName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getName();
            case DEFAULT:
            case NULL:
                if (_nameField!= null) {
                    return _nameField;
                } else {
                    Object __rawValue = super._map.get("name");
                    _nameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _nameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for name
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see External.Fields#name
     */
    @Nonnull
    public String getName() {
        if (_nameField!= null) {
            return _nameField;
        } else {
            Object __rawValue = super._map.get("name");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("name");
            }
            _nameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _nameField;
        }
    }

    /**
     * Setter for name
     * 
     * @see External.Fields#name
     */
    public External setName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field name of com.linkedin.feathr.compute.External");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "name", value);
                    _nameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for name
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see External.Fields#name
     */
    public External setName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field name of com.linkedin.feathr.compute.External to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "name", value);
            _nameField = value;
        }
        return this;
    }

    @Override
    public External clone()
        throws CloneNotSupportedException
    {
        External __clone = ((External) super.clone());
        __clone.__changeListener = new External.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public External copy()
        throws CloneNotSupportedException
    {
        External __copy = ((External) super.copy());
        __copy._nameField = null;
        __copy._idField = null;
        __copy._concreteKeyField = null;
        __copy.__changeListener = new External.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final External __objectRef;

        private ChangeListener(External reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "name":
                    __objectRef._nameField = null;
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

        public PathSpec name() {
            return new PathSpec(getPathComponents(), "name");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.ConcreteKey.ProjectionMask _concreteKeyMask;

        ProjectionMask() {
            super(4);
        }

        public External.ProjectionMask withId() {
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
        public External.ProjectionMask withConcreteKey(Function<com.linkedin.feathr.compute.ConcreteKey.ProjectionMask, com.linkedin.feathr.compute.ConcreteKey.ProjectionMask> nestedMask) {
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
        public External.ProjectionMask withConcreteKey() {
            _concreteKeyMask = null;
            getDataMap().put("concreteKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        public External.ProjectionMask withName() {
            getDataMap().put("name", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
