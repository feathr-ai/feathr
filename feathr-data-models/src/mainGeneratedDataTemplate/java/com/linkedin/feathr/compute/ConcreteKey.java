
package com.linkedin.feathr.compute;

import java.util.List;
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
import com.linkedin.data.template.IntegerArray;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/ConcreteKey.pdl.")
public class ConcreteKey
    extends RecordTemplate
{

    private final static ConcreteKey.Fields _fields = new ConcreteKey.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record ConcreteKey{key:array[typeref NodeId=int]}", SchemaFormatType.PDL));
    private IntegerArray _keyField = null;
    private ConcreteKey.ChangeListener __changeListener = new ConcreteKey.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Key = SCHEMA.getField("key");

    public ConcreteKey() {
        super(new DataMap(2, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public ConcreteKey(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static ConcreteKey.Fields fields() {
        return _fields;
    }

    public static ConcreteKey.ProjectionMask createMask() {
        return new ConcreteKey.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for key
     * 
     * @see ConcreteKey.Fields#key
     */
    public boolean hasKey() {
        if (_keyField!= null) {
            return true;
        }
        return super._map.containsKey("key");
    }

    /**
     * Remover for key
     * 
     * @see ConcreteKey.Fields#key
     */
    public void removeKey() {
        super._map.remove("key");
    }

    /**
     * Getter for key
     * 
     * @see ConcreteKey.Fields#key
     */
    public IntegerArray getKey(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getKey();
            case DEFAULT:
            case NULL:
                if (_keyField!= null) {
                    return _keyField;
                } else {
                    Object __rawValue = super._map.get("key");
                    _keyField = ((__rawValue == null)?null:new IntegerArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keyField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for key
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see ConcreteKey.Fields#key
     */
    @Nonnull
    public IntegerArray getKey() {
        if (_keyField!= null) {
            return _keyField;
        } else {
            Object __rawValue = super._map.get("key");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("key");
            }
            _keyField = ((__rawValue == null)?null:new IntegerArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keyField;
        }
    }

    /**
     * Setter for key
     * 
     * @see ConcreteKey.Fields#key
     */
    public ConcreteKey setKey(IntegerArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKey(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field key of com.linkedin.feathr.compute.ConcreteKey");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "key", value.data());
                    _keyField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKey();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "key", value.data());
                    _keyField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "key", value.data());
                    _keyField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for key
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see ConcreteKey.Fields#key
     */
    public ConcreteKey setKey(
        @Nonnull
        IntegerArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field key of com.linkedin.feathr.compute.ConcreteKey to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "key", value.data());
            _keyField = value;
        }
        return this;
    }

    @Override
    public ConcreteKey clone()
        throws CloneNotSupportedException
    {
        ConcreteKey __clone = ((ConcreteKey) super.clone());
        __clone.__changeListener = new ConcreteKey.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public ConcreteKey copy()
        throws CloneNotSupportedException
    {
        ConcreteKey __copy = ((ConcreteKey) super.copy());
        __copy._keyField = null;
        __copy.__changeListener = new ConcreteKey.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final ConcreteKey __objectRef;

        private ChangeListener(ConcreteKey reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "key":
                    __objectRef._keyField = null;
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

        public PathSpec key() {
            return new PathSpec(getPathComponents(), "key");
        }

        public PathSpec key(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "key");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        public ConcreteKey.ProjectionMask withKey() {
            getDataMap().put("key", MaskMap.POSITIVE_MASK);
            return this;
        }

        public ConcreteKey.ProjectionMask withKey(Integer start, Integer count) {
            getDataMap().put("key", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("key").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("key").put("$count", count);
            }
            return this;
        }

    }

}
