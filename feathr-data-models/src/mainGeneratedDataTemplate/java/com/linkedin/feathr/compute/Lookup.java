
package com.linkedin.feathr.compute;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataList;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
import com.linkedin.data.schema.ArrayDataSchema;
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
import com.linkedin.data.template.TemplateOutputCastException;
import com.linkedin.data.template.UnionTemplate;
import com.linkedin.data.template.WrappingArrayTemplate;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Lookup.pdl.")
public class Lookup
    extends RecordTemplate
{

    private final static Lookup.Fields _fields = new Lookup.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record Lookup includes record AbstractNode{id:typeref NodeId=int/**The key for which this node is being requested.\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)\n\nTODO: From the Engines' point of view, this field should be private. Maybe we should consider revising the data model.*/concreteKey:optional record ConcreteKey{key:array[NodeId]}}{lookupKey:array[union[record NodeReference{id:NodeId,keyReference:array[record KeyReference{position:int}]}KeyReference]]lookupNode:NodeId,aggregation:string,featureName:optional string}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private ConcreteKey _concreteKeyField = null;
    private Lookup.LookupKeyArray _lookupKeyField = null;
    private Integer _lookupNodeField = null;
    private String _aggregationField = null;
    private String _featureNameField = null;
    private Lookup.ChangeListener __changeListener = new Lookup.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_ConcreteKey = SCHEMA.getField("concreteKey");
    private final static RecordDataSchema.Field FIELD_LookupKey = SCHEMA.getField("lookupKey");
    private final static RecordDataSchema.Field FIELD_LookupNode = SCHEMA.getField("lookupNode");
    private final static RecordDataSchema.Field FIELD_Aggregation = SCHEMA.getField("aggregation");
    private final static RecordDataSchema.Field FIELD_FeatureName = SCHEMA.getField("featureName");

    public Lookup() {
        super(new DataMap(8, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public Lookup(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Lookup.Fields fields() {
        return _fields;
    }

    public static Lookup.ProjectionMask createMask() {
        return new Lookup.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see Lookup.Fields#id
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
     * @see Lookup.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see Lookup.Fields#id
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
     * @see Lookup.Fields#id
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
     * @see Lookup.Fields#id
     */
    public Lookup setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.Lookup");
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
     * @see Lookup.Fields#id
     */
    public Lookup setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see Lookup.Fields#id
     */
    public Lookup setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for concreteKey
     * 
     * @see Lookup.Fields#concreteKey
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
     * @see Lookup.Fields#concreteKey
     */
    public void removeConcreteKey() {
        super._map.remove("concreteKey");
    }

    /**
     * Getter for concreteKey
     * 
     * @see Lookup.Fields#concreteKey
     */
    public ConcreteKey getConcreteKey(GetMode mode) {
        return getConcreteKey();
    }

    /**
     * Getter for concreteKey
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Lookup.Fields#concreteKey
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
     * @see Lookup.Fields#concreteKey
     */
    public Lookup setConcreteKey(ConcreteKey value, SetMode mode) {
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
     * @see Lookup.Fields#concreteKey
     */
    public Lookup setConcreteKey(
        @Nonnull
        ConcreteKey value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field concreteKey of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
            _concreteKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public boolean hasLookupKey() {
        if (_lookupKeyField!= null) {
            return true;
        }
        return super._map.containsKey("lookupKey");
    }

    /**
     * Remover for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public void removeLookupKey() {
        super._map.remove("lookupKey");
    }

    /**
     * Getter for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public Lookup.LookupKeyArray getLookupKey(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getLookupKey();
            case DEFAULT:
            case NULL:
                if (_lookupKeyField!= null) {
                    return _lookupKeyField;
                } else {
                    Object __rawValue = super._map.get("lookupKey");
                    _lookupKeyField = ((__rawValue == null)?null:new Lookup.LookupKeyArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _lookupKeyField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for lookupKey
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Lookup.Fields#lookupKey
     */
    @Nonnull
    public Lookup.LookupKeyArray getLookupKey() {
        if (_lookupKeyField!= null) {
            return _lookupKeyField;
        } else {
            Object __rawValue = super._map.get("lookupKey");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("lookupKey");
            }
            _lookupKeyField = ((__rawValue == null)?null:new Lookup.LookupKeyArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _lookupKeyField;
        }
    }

    /**
     * Setter for lookupKey
     * 
     * @see Lookup.Fields#lookupKey
     */
    public Lookup setLookupKey(Lookup.LookupKeyArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLookupKey(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field lookupKey of com.linkedin.feathr.compute.Lookup");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
                    _lookupKeyField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLookupKey();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
                    _lookupKeyField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
                    _lookupKeyField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for lookupKey
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Lookup.Fields#lookupKey
     */
    public Lookup setLookupKey(
        @Nonnull
        Lookup.LookupKeyArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field lookupKey of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "lookupKey", value.data());
            _lookupKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public boolean hasLookupNode() {
        if (_lookupNodeField!= null) {
            return true;
        }
        return super._map.containsKey("lookupNode");
    }

    /**
     * Remover for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public void removeLookupNode() {
        super._map.remove("lookupNode");
    }

    /**
     * Getter for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public Integer getLookupNode(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getLookupNode();
            case DEFAULT:
            case NULL:
                if (_lookupNodeField!= null) {
                    return _lookupNodeField;
                } else {
                    Object __rawValue = super._map.get("lookupNode");
                    _lookupNodeField = DataTemplateUtil.coerceIntOutput(__rawValue);
                    return _lookupNodeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for lookupNode
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Lookup.Fields#lookupNode
     */
    @Nonnull
    public Integer getLookupNode() {
        if (_lookupNodeField!= null) {
            return _lookupNodeField;
        } else {
            Object __rawValue = super._map.get("lookupNode");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("lookupNode");
            }
            _lookupNodeField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _lookupNodeField;
        }
    }

    /**
     * Setter for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public Lookup setLookupNode(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLookupNode(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field lookupNode of com.linkedin.feathr.compute.Lookup");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
                    _lookupNodeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLookupNode();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
                    _lookupNodeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
                    _lookupNodeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for lookupNode
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Lookup.Fields#lookupNode
     */
    public Lookup setLookupNode(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field lookupNode of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
            _lookupNodeField = value;
        }
        return this;
    }

    /**
     * Setter for lookupNode
     * 
     * @see Lookup.Fields#lookupNode
     */
    public Lookup setLookupNode(int value) {
        CheckedUtil.putWithoutChecking(super._map, "lookupNode", DataTemplateUtil.coerceIntInput(value));
        _lookupNodeField = value;
        return this;
    }

    /**
     * Existence checker for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public boolean hasAggregation() {
        if (_aggregationField!= null) {
            return true;
        }
        return super._map.containsKey("aggregation");
    }

    /**
     * Remover for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public void removeAggregation() {
        super._map.remove("aggregation");
    }

    /**
     * Getter for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public String getAggregation(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getAggregation();
            case DEFAULT:
            case NULL:
                if (_aggregationField!= null) {
                    return _aggregationField;
                } else {
                    Object __rawValue = super._map.get("aggregation");
                    _aggregationField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _aggregationField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for aggregation
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Lookup.Fields#aggregation
     */
    @Nonnull
    public String getAggregation() {
        if (_aggregationField!= null) {
            return _aggregationField;
        } else {
            Object __rawValue = super._map.get("aggregation");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("aggregation");
            }
            _aggregationField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _aggregationField;
        }
    }

    /**
     * Setter for aggregation
     * 
     * @see Lookup.Fields#aggregation
     */
    public Lookup setAggregation(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setAggregation(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field aggregation of com.linkedin.feathr.compute.Lookup");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
                    _aggregationField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeAggregation();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
                    _aggregationField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
                    _aggregationField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for aggregation
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Lookup.Fields#aggregation
     */
    public Lookup setAggregation(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field aggregation of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "aggregation", value);
            _aggregationField = value;
        }
        return this;
    }

    /**
     * Existence checker for featureName
     * 
     * @see Lookup.Fields#featureName
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
     * @see Lookup.Fields#featureName
     */
    public void removeFeatureName() {
        super._map.remove("featureName");
    }

    /**
     * Getter for featureName
     * 
     * @see Lookup.Fields#featureName
     */
    public String getFeatureName(GetMode mode) {
        return getFeatureName();
    }

    /**
     * Getter for featureName
     * 
     * @return
     *     Optional field. Always check for null.
     * @see Lookup.Fields#featureName
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
     * @see Lookup.Fields#featureName
     */
    public Lookup setFeatureName(String value, SetMode mode) {
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
     * @see Lookup.Fields#featureName
     */
    public Lookup setFeatureName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field featureName of com.linkedin.feathr.compute.Lookup to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "featureName", value);
            _featureNameField = value;
        }
        return this;
    }

    @Override
    public Lookup clone()
        throws CloneNotSupportedException
    {
        Lookup __clone = ((Lookup) super.clone());
        __clone.__changeListener = new Lookup.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Lookup copy()
        throws CloneNotSupportedException
    {
        Lookup __copy = ((Lookup) super.copy());
        __copy._lookupKeyField = null;
        __copy._featureNameField = null;
        __copy._lookupNodeField = null;
        __copy._aggregationField = null;
        __copy._idField = null;
        __copy._concreteKeyField = null;
        __copy.__changeListener = new Lookup.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Lookup __objectRef;

        private ChangeListener(Lookup reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "lookupKey":
                    __objectRef._lookupKeyField = null;
                    break;
                case "featureName":
                    __objectRef._featureNameField = null;
                    break;
                case "lookupNode":
                    __objectRef._lookupNodeField = null;
                    break;
                case "aggregation":
                    __objectRef._aggregationField = null;
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

        public com.linkedin.feathr.compute.Lookup.LookupKeyArray.Fields lookupKey() {
            return new com.linkedin.feathr.compute.Lookup.LookupKeyArray.Fields(getPathComponents(), "lookupKey");
        }

        public PathSpec lookupKey(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "lookupKey");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        public PathSpec lookupNode() {
            return new PathSpec(getPathComponents(), "lookupNode");
        }

        public PathSpec aggregation() {
            return new PathSpec(getPathComponents(), "aggregation");
        }

        public PathSpec featureName() {
            return new PathSpec(getPathComponents(), "featureName");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Lookup.pdl.")
    public static class LookupKey
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute,record NodeReference{id:typeref NodeId=int,keyReference:array[record KeyReference{position:int}]}}com.linkedin.feathr.compute.KeyReference]", SchemaFormatType.PDL));
        private com.linkedin.feathr.compute.NodeReference _nodeReferenceMember = null;
        private com.linkedin.feathr.compute.KeyReference _keyReferenceMember = null;
        private Lookup.LookupKey.ChangeListener __changeListener = new Lookup.LookupKey.ChangeListener(this);
        private final static DataSchema MEMBER_NodeReference = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.NodeReference");
        private final static DataSchema MEMBER_KeyReference = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.KeyReference");

        public LookupKey() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public LookupKey(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static Lookup.LookupKey create(com.linkedin.feathr.compute.NodeReference value) {
            Lookup.LookupKey newUnion = new Lookup.LookupKey();
            newUnion.setNodeReference(value);
            return newUnion;
        }

        public boolean isNodeReference() {
            return memberIs("com.linkedin.feathr.compute.NodeReference");
        }

        public com.linkedin.feathr.compute.NodeReference getNodeReference() {
            checkNotNull();
            if (_nodeReferenceMember!= null) {
                return _nodeReferenceMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.NodeReference");
            _nodeReferenceMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.NodeReference(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _nodeReferenceMember;
        }

        public void setNodeReference(com.linkedin.feathr.compute.NodeReference value) {
            checkNotNull();
            super._map.clear();
            _nodeReferenceMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.NodeReference", value.data());
        }

        public static Lookup.LookupKey create(com.linkedin.feathr.compute.KeyReference value) {
            Lookup.LookupKey newUnion = new Lookup.LookupKey();
            newUnion.setKeyReference(value);
            return newUnion;
        }

        public boolean isKeyReference() {
            return memberIs("com.linkedin.feathr.compute.KeyReference");
        }

        public com.linkedin.feathr.compute.KeyReference getKeyReference() {
            checkNotNull();
            if (_keyReferenceMember!= null) {
                return _keyReferenceMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.KeyReference");
            _keyReferenceMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.KeyReference(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _keyReferenceMember;
        }

        public void setKeyReference(com.linkedin.feathr.compute.KeyReference value) {
            checkNotNull();
            super._map.clear();
            _keyReferenceMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.KeyReference", value.data());
        }

        public static Lookup.LookupKey.ProjectionMask createMask() {
            return new Lookup.LookupKey.ProjectionMask();
        }

        @Override
        public Lookup.LookupKey clone()
            throws CloneNotSupportedException
        {
            Lookup.LookupKey __clone = ((Lookup.LookupKey) super.clone());
            __clone.__changeListener = new Lookup.LookupKey.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public Lookup.LookupKey copy()
            throws CloneNotSupportedException
        {
            Lookup.LookupKey __copy = ((Lookup.LookupKey) super.copy());
            __copy._keyReferenceMember = null;
            __copy._nodeReferenceMember = null;
            __copy.__changeListener = new Lookup.LookupKey.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final Lookup.LookupKey __objectRef;

            private ChangeListener(Lookup.LookupKey reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.compute.KeyReference":
                        __objectRef._keyReferenceMember = null;
                        break;
                    case "com.linkedin.feathr.compute.NodeReference":
                        __objectRef._nodeReferenceMember = null;
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

            public com.linkedin.feathr.compute.NodeReference.Fields NodeReference() {
                return new com.linkedin.feathr.compute.NodeReference.Fields(getPathComponents(), "com.linkedin.feathr.compute.NodeReference");
            }

            public com.linkedin.feathr.compute.KeyReference.Fields KeyReference() {
                return new com.linkedin.feathr.compute.KeyReference.Fields(getPathComponents(), "com.linkedin.feathr.compute.KeyReference");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.NodeReference.ProjectionMask _NodeReferenceMask;
            private com.linkedin.feathr.compute.KeyReference.ProjectionMask _KeyReferenceMask;

            ProjectionMask() {
                super(3);
            }

            public Lookup.LookupKey.ProjectionMask withNodeReference(Function<com.linkedin.feathr.compute.NodeReference.ProjectionMask, com.linkedin.feathr.compute.NodeReference.ProjectionMask> nestedMask) {
                _NodeReferenceMask = nestedMask.apply(((_NodeReferenceMask == null)?com.linkedin.feathr.compute.NodeReference.createMask():_NodeReferenceMask));
                getDataMap().put("com.linkedin.feathr.compute.NodeReference", _NodeReferenceMask.getDataMap());
                return this;
            }

            public Lookup.LookupKey.ProjectionMask withKeyReference(Function<com.linkedin.feathr.compute.KeyReference.ProjectionMask, com.linkedin.feathr.compute.KeyReference.ProjectionMask> nestedMask) {
                _KeyReferenceMask = nestedMask.apply(((_KeyReferenceMask == null)?com.linkedin.feathr.compute.KeyReference.createMask():_KeyReferenceMask));
                getDataMap().put("com.linkedin.feathr.compute.KeyReference", _KeyReferenceMask.getDataMap());
                return this;
            }

        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Lookup.pdl.")
    public static class LookupKeyArray
        extends WrappingArrayTemplate<Lookup.LookupKey>
    {

        private final static ArrayDataSchema SCHEMA = ((ArrayDataSchema) DataTemplateUtil.parseSchema("array[union[{namespace com.linkedin.feathr.compute,record NodeReference{id:typeref NodeId=int,keyReference:array[record KeyReference{position:int}]}}com.linkedin.feathr.compute.KeyReference]]", SchemaFormatType.PDL));

        public LookupKeyArray() {
            this(new DataList());
        }

        public LookupKeyArray(int initialCapacity) {
            this(new DataList(initialCapacity));
        }

        public LookupKeyArray(Collection<Lookup.LookupKey> c) {
            this(new DataList(c.size()));
            addAll(c);
        }

        public LookupKeyArray(DataList data) {
            super(data, SCHEMA, Lookup.LookupKey.class);
        }

        public LookupKeyArray(Lookup.LookupKey first, Lookup.LookupKey... rest) {
            this(new DataList((rest.length + 1)));
            add(first);
            addAll(Arrays.asList(rest));
        }

        public static ArrayDataSchema dataSchema() {
            return SCHEMA;
        }

        public static Lookup.LookupKeyArray.ProjectionMask createMask() {
            return new Lookup.LookupKeyArray.ProjectionMask();
        }

        @Override
        public Lookup.LookupKeyArray clone()
            throws CloneNotSupportedException
        {
            Lookup.LookupKeyArray __clone = ((Lookup.LookupKeyArray) super.clone());
            return __clone;
        }

        @Override
        public Lookup.LookupKeyArray copy()
            throws CloneNotSupportedException
        {
            Lookup.LookupKeyArray __copy = ((Lookup.LookupKeyArray) super.copy());
            return __copy;
        }

        @Override
        protected Lookup.LookupKey coerceOutput(Object object)
            throws TemplateOutputCastException
        {
            assert(object != null);
            return ((object == null)?null:new Lookup.LookupKey(object));
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

            public com.linkedin.feathr.compute.Lookup.LookupKey.Fields items() {
                return new com.linkedin.feathr.compute.Lookup.LookupKey.Fields(getPathComponents(), PathSpec.WILDCARD);
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.Lookup.LookupKey.ProjectionMask _itemsMask;

            ProjectionMask() {
                super(4);
            }

            public Lookup.LookupKeyArray.ProjectionMask withItems(Function<com.linkedin.feathr.compute.Lookup.LookupKey.ProjectionMask, com.linkedin.feathr.compute.Lookup.LookupKey.ProjectionMask> nestedMask) {
                _itemsMask = nestedMask.apply(((_itemsMask == null)?Lookup.LookupKey.createMask():_itemsMask));
                getDataMap().put("$*", _itemsMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.ConcreteKey.ProjectionMask _concreteKeyMask;
        private com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask _lookupKeyMask;

        ProjectionMask() {
            super(8);
        }

        public Lookup.ProjectionMask withId() {
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
        public Lookup.ProjectionMask withConcreteKey(Function<com.linkedin.feathr.compute.ConcreteKey.ProjectionMask, com.linkedin.feathr.compute.ConcreteKey.ProjectionMask> nestedMask) {
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
        public Lookup.ProjectionMask withConcreteKey() {
            _concreteKeyMask = null;
            getDataMap().put("concreteKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Lookup.ProjectionMask withLookupKey(Function<com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask, com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask> nestedMask) {
            _lookupKeyMask = nestedMask.apply(((_lookupKeyMask == null)?Lookup.LookupKeyArray.createMask():_lookupKeyMask));
            getDataMap().put("lookupKey", _lookupKeyMask.getDataMap());
            return this;
        }

        public Lookup.ProjectionMask withLookupKey() {
            _lookupKeyMask = null;
            getDataMap().put("lookupKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Lookup.ProjectionMask withLookupKey(Function<com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask, com.linkedin.feathr.compute.Lookup.LookupKeyArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _lookupKeyMask = nestedMask.apply(((_lookupKeyMask == null)?Lookup.LookupKeyArray.createMask():_lookupKeyMask));
            getDataMap().put("lookupKey", _lookupKeyMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("lookupKey").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("lookupKey").put("$count", count);
            }
            return this;
        }

        public Lookup.ProjectionMask withLookupKey(Integer start, Integer count) {
            _lookupKeyMask = null;
            getDataMap().put("lookupKey", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("lookupKey").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("lookupKey").put("$count", count);
            }
            return this;
        }

        public Lookup.ProjectionMask withLookupNode() {
            getDataMap().put("lookupNode", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Lookup.ProjectionMask withAggregation() {
            getDataMap().put("aggregation", MaskMap.POSITIVE_MASK);
            return this;
        }

        public Lookup.ProjectionMask withFeatureName() {
            getDataMap().put("featureName", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
