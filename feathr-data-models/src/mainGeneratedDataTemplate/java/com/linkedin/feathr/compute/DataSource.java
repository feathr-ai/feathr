
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
 * Representation of the datasource node. There are 3 types of datasource nodes:-
 * Context - To represent the observation data entities (like the join key or passthrough feature columns)
 * Update - To represent a non-timepartitioned datasource node.
 * Event - To represent a time-partitioned datasource node.
 * 
 * TODO - Maybe, it makes sense more sense to refactor it by make this an abstract object, and deriving the three different nodes from it.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\DataSource.pdl.")
public class DataSource
    extends RecordTemplate
{

    private final static DataSource.Fields _fields = new DataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Representation of the datasource node. There are 3 types of datasource nodes:-\r\nContext - To represent the observation data entities (like the join key or passthrough feature columns)\r\nUpdate - To represent a non-timepartitioned datasource node.\r\nEvent - To represent a time-partitioned datasource node.\r\n\r\nTODO - Maybe, it makes sense more sense to refactor it by make this an abstract object, and deriving the three different nodes from it.*/record DataSource includes/**Generic abstraction of a node. All other nodes should derive from this node.*/record AbstractNode{/**The node would be represented by this id.*/id:/**A type ref to int node id*/typeref NodeId=int/**The key for which this node is being requested.\r\nIf this node is a Source node, the engine can use the key to fetch or join the feature.\r\nIf this node is NOT a Source node, the engine should NOT use the key to determine fetch/join behavior, but\r\nshould follow the node's inputs. (The core libraries may use the key information in order to optimize the graph,\r\ne.g. it can be used for identifying duplicate sections of the graph that can be pruned.)*/concreteKey:optional/**The key (node) for which the node in question is requested.*/record ConcreteKey{/**Most of the time, this should point to a CONTEXT SOURCE node, e.g. a key in the context called x.\r\nThe main exception would be for a Lookup feature, in which case it would point to another node where the lookup\r\nkey gets computed.*/key:array[NodeId]}}{/**Type of node, ie - Context, Update, Event*/sourceType:/**Type of datasource node.*/enum DataSourceType{/**Update data sources provide keyed data about entities. A fully specified table data source contains both a snapshot view and an update log.*/UPDATE/**Event data sources are append-only event logs whose records need to be grouped and aggregated (e.g. counted, averaged, top-K\u00e2\u20ac\u2122d)\r\nover a limited window of time.*/EVENT/**Reprent the observation data entities (like the join key or passthrough feature columns)*/CONTEXT}/**for CONTEXT type, this is the name of the context column. otherwise, it should be a path or URI.*/externalSourceRef:string/**Raw key expression as entered by the user. This hocon parsing happens at the execution engine side.*/keyExpression:string/**mvel or spark or user-defined class*/keyExpressionType:/**Different key formats supported.\r\nTodo - We probably do not want to generalize this as a kind of key-operator in the core compute model,\r\nwith instances such as for MVEL or SQL being available (e.g. via an OperatorId reference).*/enum KeyExpressionType{/**Java-based MVEL*/MVEL/**Spark-SQL*/SQL/**Custom java/scala UDF*/UDF}/**File partition format.*/filePartitionFormat:optional string/**Timestamp column info, to be available only for an event datasource node.*/timestampColumnInfo:optional/**Representation of a timestamp column field*/record TimestampCol{/**Timestamp column expression.*/expression:string/**Format of the timestamp, example - yyyy/MM/dd, epoch, epoch_millis*/format:string}}", SchemaFormatType.PDL));
    private Integer _idField = null;
    private ConcreteKey _concreteKeyField = null;
    private DataSourceType _sourceTypeField = null;
    private String _externalSourceRefField = null;
    private String _keyExpressionField = null;
    private KeyExpressionType _keyExpressionTypeField = null;
    private String _filePartitionFormatField = null;
    private TimestampCol _timestampColumnInfoField = null;
    private DataSource.ChangeListener __changeListener = new DataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Id = SCHEMA.getField("id");
    private final static RecordDataSchema.Field FIELD_ConcreteKey = SCHEMA.getField("concreteKey");
    private final static RecordDataSchema.Field FIELD_SourceType = SCHEMA.getField("sourceType");
    private final static RecordDataSchema.Field FIELD_ExternalSourceRef = SCHEMA.getField("externalSourceRef");
    private final static RecordDataSchema.Field FIELD_KeyExpression = SCHEMA.getField("keyExpression");
    private final static RecordDataSchema.Field FIELD_KeyExpressionType = SCHEMA.getField("keyExpressionType");
    private final static RecordDataSchema.Field FIELD_FilePartitionFormat = SCHEMA.getField("filePartitionFormat");
    private final static RecordDataSchema.Field FIELD_TimestampColumnInfo = SCHEMA.getField("timestampColumnInfo");

    public DataSource() {
        super(new DataMap(11, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public DataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static DataSource.Fields fields() {
        return _fields;
    }

    public static DataSource.ProjectionMask createMask() {
        return new DataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for id
     * 
     * @see DataSource.Fields#id
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
     * @see DataSource.Fields#id
     */
    public void removeId() {
        super._map.remove("id");
    }

    /**
     * Getter for id
     * 
     * @see DataSource.Fields#id
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
     * @see DataSource.Fields#id
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
     * @see DataSource.Fields#id
     */
    public DataSource setId(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setId(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field id of com.linkedin.feathr.compute.DataSource");
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
     * @see DataSource.Fields#id
     */
    public DataSource setId(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field id of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
            _idField = value;
        }
        return this;
    }

    /**
     * Setter for id
     * 
     * @see DataSource.Fields#id
     */
    public DataSource setId(int value) {
        CheckedUtil.putWithoutChecking(super._map, "id", DataTemplateUtil.coerceIntInput(value));
        _idField = value;
        return this;
    }

    /**
     * Existence checker for concreteKey
     * 
     * @see DataSource.Fields#concreteKey
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
     * @see DataSource.Fields#concreteKey
     */
    public void removeConcreteKey() {
        super._map.remove("concreteKey");
    }

    /**
     * Getter for concreteKey
     * 
     * @see DataSource.Fields#concreteKey
     */
    public ConcreteKey getConcreteKey(GetMode mode) {
        return getConcreteKey();
    }

    /**
     * Getter for concreteKey
     * 
     * @return
     *     Optional field. Always check for null.
     * @see DataSource.Fields#concreteKey
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
     * @see DataSource.Fields#concreteKey
     */
    public DataSource setConcreteKey(ConcreteKey value, SetMode mode) {
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
     * @see DataSource.Fields#concreteKey
     */
    public DataSource setConcreteKey(
        @Nonnull
        ConcreteKey value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field concreteKey of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "concreteKey", value.data());
            _concreteKeyField = value;
        }
        return this;
    }

    /**
     * Existence checker for sourceType
     * 
     * @see DataSource.Fields#sourceType
     */
    public boolean hasSourceType() {
        if (_sourceTypeField!= null) {
            return true;
        }
        return super._map.containsKey("sourceType");
    }

    /**
     * Remover for sourceType
     * 
     * @see DataSource.Fields#sourceType
     */
    public void removeSourceType() {
        super._map.remove("sourceType");
    }

    /**
     * Getter for sourceType
     * 
     * @see DataSource.Fields#sourceType
     */
    public DataSourceType getSourceType(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getSourceType();
            case DEFAULT:
            case NULL:
                if (_sourceTypeField!= null) {
                    return _sourceTypeField;
                } else {
                    Object __rawValue = super._map.get("sourceType");
                    _sourceTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, DataSourceType.class, DataSourceType.$UNKNOWN);
                    return _sourceTypeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for sourceType
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see DataSource.Fields#sourceType
     */
    @Nonnull
    public DataSourceType getSourceType() {
        if (_sourceTypeField!= null) {
            return _sourceTypeField;
        } else {
            Object __rawValue = super._map.get("sourceType");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("sourceType");
            }
            _sourceTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, DataSourceType.class, DataSourceType.$UNKNOWN);
            return _sourceTypeField;
        }
    }

    /**
     * Setter for sourceType
     * 
     * @see DataSource.Fields#sourceType
     */
    public DataSource setSourceType(DataSourceType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSourceType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field sourceType of com.linkedin.feathr.compute.DataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "sourceType", value.name());
                    _sourceTypeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeSourceType();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "sourceType", value.name());
                    _sourceTypeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "sourceType", value.name());
                    _sourceTypeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for sourceType
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DataSource.Fields#sourceType
     */
    public DataSource setSourceType(
        @Nonnull
        DataSourceType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field sourceType of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "sourceType", value.name());
            _sourceTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for externalSourceRef
     * 
     * @see DataSource.Fields#externalSourceRef
     */
    public boolean hasExternalSourceRef() {
        if (_externalSourceRefField!= null) {
            return true;
        }
        return super._map.containsKey("externalSourceRef");
    }

    /**
     * Remover for externalSourceRef
     * 
     * @see DataSource.Fields#externalSourceRef
     */
    public void removeExternalSourceRef() {
        super._map.remove("externalSourceRef");
    }

    /**
     * Getter for externalSourceRef
     * 
     * @see DataSource.Fields#externalSourceRef
     */
    public String getExternalSourceRef(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getExternalSourceRef();
            case DEFAULT:
            case NULL:
                if (_externalSourceRefField!= null) {
                    return _externalSourceRefField;
                } else {
                    Object __rawValue = super._map.get("externalSourceRef");
                    _externalSourceRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _externalSourceRefField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for externalSourceRef
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see DataSource.Fields#externalSourceRef
     */
    @Nonnull
    public String getExternalSourceRef() {
        if (_externalSourceRefField!= null) {
            return _externalSourceRefField;
        } else {
            Object __rawValue = super._map.get("externalSourceRef");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("externalSourceRef");
            }
            _externalSourceRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _externalSourceRefField;
        }
    }

    /**
     * Setter for externalSourceRef
     * 
     * @see DataSource.Fields#externalSourceRef
     */
    public DataSource setExternalSourceRef(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setExternalSourceRef(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field externalSourceRef of com.linkedin.feathr.compute.DataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "externalSourceRef", value);
                    _externalSourceRefField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeExternalSourceRef();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "externalSourceRef", value);
                    _externalSourceRefField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "externalSourceRef", value);
                    _externalSourceRefField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for externalSourceRef
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DataSource.Fields#externalSourceRef
     */
    public DataSource setExternalSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field externalSourceRef of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "externalSourceRef", value);
            _externalSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyExpression
     * 
     * @see DataSource.Fields#keyExpression
     */
    public boolean hasKeyExpression() {
        if (_keyExpressionField!= null) {
            return true;
        }
        return super._map.containsKey("keyExpression");
    }

    /**
     * Remover for keyExpression
     * 
     * @see DataSource.Fields#keyExpression
     */
    public void removeKeyExpression() {
        super._map.remove("keyExpression");
    }

    /**
     * Getter for keyExpression
     * 
     * @see DataSource.Fields#keyExpression
     */
    public String getKeyExpression(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeyExpression();
            case DEFAULT:
            case NULL:
                if (_keyExpressionField!= null) {
                    return _keyExpressionField;
                } else {
                    Object __rawValue = super._map.get("keyExpression");
                    _keyExpressionField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _keyExpressionField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyExpression
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see DataSource.Fields#keyExpression
     */
    @Nonnull
    public String getKeyExpression() {
        if (_keyExpressionField!= null) {
            return _keyExpressionField;
        } else {
            Object __rawValue = super._map.get("keyExpression");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keyExpression");
            }
            _keyExpressionField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _keyExpressionField;
        }
    }

    /**
     * Setter for keyExpression
     * 
     * @see DataSource.Fields#keyExpression
     */
    public DataSource setKeyExpression(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyExpression(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyExpression of com.linkedin.feathr.compute.DataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyExpression", value);
                    _keyExpressionField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyExpression();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyExpression", value);
                    _keyExpressionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyExpression", value);
                    _keyExpressionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyExpression
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DataSource.Fields#keyExpression
     */
    public DataSource setKeyExpression(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyExpression of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyExpression", value);
            _keyExpressionField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyExpressionType
     * 
     * @see DataSource.Fields#keyExpressionType
     */
    public boolean hasKeyExpressionType() {
        if (_keyExpressionTypeField!= null) {
            return true;
        }
        return super._map.containsKey("keyExpressionType");
    }

    /**
     * Remover for keyExpressionType
     * 
     * @see DataSource.Fields#keyExpressionType
     */
    public void removeKeyExpressionType() {
        super._map.remove("keyExpressionType");
    }

    /**
     * Getter for keyExpressionType
     * 
     * @see DataSource.Fields#keyExpressionType
     */
    public KeyExpressionType getKeyExpressionType(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKeyExpressionType();
            case DEFAULT:
            case NULL:
                if (_keyExpressionTypeField!= null) {
                    return _keyExpressionTypeField;
                } else {
                    Object __rawValue = super._map.get("keyExpressionType");
                    _keyExpressionTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, KeyExpressionType.class, KeyExpressionType.$UNKNOWN);
                    return _keyExpressionTypeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyExpressionType
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see DataSource.Fields#keyExpressionType
     */
    @Nonnull
    public KeyExpressionType getKeyExpressionType() {
        if (_keyExpressionTypeField!= null) {
            return _keyExpressionTypeField;
        } else {
            Object __rawValue = super._map.get("keyExpressionType");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("keyExpressionType");
            }
            _keyExpressionTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, KeyExpressionType.class, KeyExpressionType.$UNKNOWN);
            return _keyExpressionTypeField;
        }
    }

    /**
     * Setter for keyExpressionType
     * 
     * @see DataSource.Fields#keyExpressionType
     */
    public DataSource setKeyExpressionType(KeyExpressionType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyExpressionType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyExpressionType of com.linkedin.feathr.compute.DataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyExpressionType", value.name());
                    _keyExpressionTypeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyExpressionType();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyExpressionType", value.name());
                    _keyExpressionTypeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyExpressionType", value.name());
                    _keyExpressionTypeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyExpressionType
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DataSource.Fields#keyExpressionType
     */
    public DataSource setKeyExpressionType(
        @Nonnull
        KeyExpressionType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyExpressionType of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyExpressionType", value.name());
            _keyExpressionTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for filePartitionFormat
     * 
     * @see DataSource.Fields#filePartitionFormat
     */
    public boolean hasFilePartitionFormat() {
        if (_filePartitionFormatField!= null) {
            return true;
        }
        return super._map.containsKey("filePartitionFormat");
    }

    /**
     * Remover for filePartitionFormat
     * 
     * @see DataSource.Fields#filePartitionFormat
     */
    public void removeFilePartitionFormat() {
        super._map.remove("filePartitionFormat");
    }

    /**
     * Getter for filePartitionFormat
     * 
     * @see DataSource.Fields#filePartitionFormat
     */
    public String getFilePartitionFormat(GetMode mode) {
        return getFilePartitionFormat();
    }

    /**
     * Getter for filePartitionFormat
     * 
     * @return
     *     Optional field. Always check for null.
     * @see DataSource.Fields#filePartitionFormat
     */
    @Nullable
    public String getFilePartitionFormat() {
        if (_filePartitionFormatField!= null) {
            return _filePartitionFormatField;
        } else {
            Object __rawValue = super._map.get("filePartitionFormat");
            _filePartitionFormatField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _filePartitionFormatField;
        }
    }

    /**
     * Setter for filePartitionFormat
     * 
     * @see DataSource.Fields#filePartitionFormat
     */
    public DataSource setFilePartitionFormat(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFilePartitionFormat(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFilePartitionFormat();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "filePartitionFormat", value);
                    _filePartitionFormatField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "filePartitionFormat", value);
                    _filePartitionFormatField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for filePartitionFormat
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DataSource.Fields#filePartitionFormat
     */
    public DataSource setFilePartitionFormat(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field filePartitionFormat of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "filePartitionFormat", value);
            _filePartitionFormatField = value;
        }
        return this;
    }

    /**
     * Existence checker for timestampColumnInfo
     * 
     * @see DataSource.Fields#timestampColumnInfo
     */
    public boolean hasTimestampColumnInfo() {
        if (_timestampColumnInfoField!= null) {
            return true;
        }
        return super._map.containsKey("timestampColumnInfo");
    }

    /**
     * Remover for timestampColumnInfo
     * 
     * @see DataSource.Fields#timestampColumnInfo
     */
    public void removeTimestampColumnInfo() {
        super._map.remove("timestampColumnInfo");
    }

    /**
     * Getter for timestampColumnInfo
     * 
     * @see DataSource.Fields#timestampColumnInfo
     */
    public TimestampCol getTimestampColumnInfo(GetMode mode) {
        return getTimestampColumnInfo();
    }

    /**
     * Getter for timestampColumnInfo
     * 
     * @return
     *     Optional field. Always check for null.
     * @see DataSource.Fields#timestampColumnInfo
     */
    @Nullable
    public TimestampCol getTimestampColumnInfo() {
        if (_timestampColumnInfoField!= null) {
            return _timestampColumnInfoField;
        } else {
            Object __rawValue = super._map.get("timestampColumnInfo");
            _timestampColumnInfoField = ((__rawValue == null)?null:new TimestampCol(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _timestampColumnInfoField;
        }
    }

    /**
     * Setter for timestampColumnInfo
     * 
     * @see DataSource.Fields#timestampColumnInfo
     */
    public DataSource setTimestampColumnInfo(TimestampCol value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTimestampColumnInfo(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTimestampColumnInfo();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "timestampColumnInfo", value.data());
                    _timestampColumnInfoField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "timestampColumnInfo", value.data());
                    _timestampColumnInfoField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for timestampColumnInfo
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DataSource.Fields#timestampColumnInfo
     */
    public DataSource setTimestampColumnInfo(
        @Nonnull
        TimestampCol value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field timestampColumnInfo of com.linkedin.feathr.compute.DataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "timestampColumnInfo", value.data());
            _timestampColumnInfoField = value;
        }
        return this;
    }

    @Override
    public DataSource clone()
        throws CloneNotSupportedException
    {
        DataSource __clone = ((DataSource) super.clone());
        __clone.__changeListener = new DataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public DataSource copy()
        throws CloneNotSupportedException
    {
        DataSource __copy = ((DataSource) super.copy());
        __copy._keyExpressionTypeField = null;
        __copy._sourceTypeField = null;
        __copy._externalSourceRefField = null;
        __copy._timestampColumnInfoField = null;
        __copy._keyExpressionField = null;
        __copy._filePartitionFormatField = null;
        __copy._idField = null;
        __copy._concreteKeyField = null;
        __copy.__changeListener = new DataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final DataSource __objectRef;

        private ChangeListener(DataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyExpressionType":
                    __objectRef._keyExpressionTypeField = null;
                    break;
                case "sourceType":
                    __objectRef._sourceTypeField = null;
                    break;
                case "externalSourceRef":
                    __objectRef._externalSourceRefField = null;
                    break;
                case "timestampColumnInfo":
                    __objectRef._timestampColumnInfoField = null;
                    break;
                case "keyExpression":
                    __objectRef._keyExpressionField = null;
                    break;
                case "filePartitionFormat":
                    __objectRef._filePartitionFormatField = null;
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

        /**
         * The node would be represented by this id.
         * 
         */
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
         */
        public com.linkedin.feathr.compute.ConcreteKey.Fields concreteKey() {
            return new com.linkedin.feathr.compute.ConcreteKey.Fields(getPathComponents(), "concreteKey");
        }

        /**
         * Type of node, ie - Context, Update, Event
         * 
         */
        public PathSpec sourceType() {
            return new PathSpec(getPathComponents(), "sourceType");
        }

        /**
         * for CONTEXT type, this is the name of the context column. otherwise, it should be a path or URI.
         * 
         */
        public PathSpec externalSourceRef() {
            return new PathSpec(getPathComponents(), "externalSourceRef");
        }

        /**
         * Raw key expression as entered by the user. This hocon parsing happens at the execution engine side.
         * 
         */
        public PathSpec keyExpression() {
            return new PathSpec(getPathComponents(), "keyExpression");
        }

        /**
         * mvel or spark or user-defined class
         * 
         */
        public PathSpec keyExpressionType() {
            return new PathSpec(getPathComponents(), "keyExpressionType");
        }

        /**
         * File partition format.
         * 
         */
        public PathSpec filePartitionFormat() {
            return new PathSpec(getPathComponents(), "filePartitionFormat");
        }

        /**
         * Timestamp column info, to be available only for an event datasource node.
         * 
         */
        public com.linkedin.feathr.compute.TimestampCol.Fields timestampColumnInfo() {
            return new com.linkedin.feathr.compute.TimestampCol.Fields(getPathComponents(), "timestampColumnInfo");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.ConcreteKey.ProjectionMask _concreteKeyMask;
        private com.linkedin.feathr.compute.TimestampCol.ProjectionMask _timestampColumnInfoMask;

        ProjectionMask() {
            super(11);
        }

        /**
         * The node would be represented by this id.
         * 
         */
        public DataSource.ProjectionMask withId() {
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
         */
        public DataSource.ProjectionMask withConcreteKey(Function<com.linkedin.feathr.compute.ConcreteKey.ProjectionMask, com.linkedin.feathr.compute.ConcreteKey.ProjectionMask> nestedMask) {
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
         */
        public DataSource.ProjectionMask withConcreteKey() {
            _concreteKeyMask = null;
            getDataMap().put("concreteKey", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Type of node, ie - Context, Update, Event
         * 
         */
        public DataSource.ProjectionMask withSourceType() {
            getDataMap().put("sourceType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * for CONTEXT type, this is the name of the context column. otherwise, it should be a path or URI.
         * 
         */
        public DataSource.ProjectionMask withExternalSourceRef() {
            getDataMap().put("externalSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Raw key expression as entered by the user. This hocon parsing happens at the execution engine side.
         * 
         */
        public DataSource.ProjectionMask withKeyExpression() {
            getDataMap().put("keyExpression", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * mvel or spark or user-defined class
         * 
         */
        public DataSource.ProjectionMask withKeyExpressionType() {
            getDataMap().put("keyExpressionType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * File partition format.
         * 
         */
        public DataSource.ProjectionMask withFilePartitionFormat() {
            getDataMap().put("filePartitionFormat", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Timestamp column info, to be available only for an event datasource node.
         * 
         */
        public DataSource.ProjectionMask withTimestampColumnInfo(Function<com.linkedin.feathr.compute.TimestampCol.ProjectionMask, com.linkedin.feathr.compute.TimestampCol.ProjectionMask> nestedMask) {
            _timestampColumnInfoMask = nestedMask.apply(((_timestampColumnInfoMask == null)?TimestampCol.createMask():_timestampColumnInfoMask));
            getDataMap().put("timestampColumnInfo", _timestampColumnInfoMask.getDataMap());
            return this;
        }

        /**
         * Timestamp column info, to be available only for an event datasource node.
         * 
         */
        public DataSource.ProjectionMask withTimestampColumnInfo() {
            _timestampColumnInfoMask = null;
            getDataMap().put("timestampColumnInfo", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
