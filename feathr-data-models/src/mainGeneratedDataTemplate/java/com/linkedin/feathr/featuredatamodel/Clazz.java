
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
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
 * Reference to a class by fully-qualified name
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/Clazz.pdl.")
public class Clazz
    extends RecordTemplate
{

    private final static Clazz.Fields _fields = new Clazz.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}", SchemaFormatType.PDL));
    private String _fullyQualifiedNameField = null;
    private Clazz.ChangeListener __changeListener = new Clazz.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_FullyQualifiedName = SCHEMA.getField("fullyQualifiedName");

    public Clazz() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public Clazz(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static Clazz.Fields fields() {
        return _fields;
    }

    public static Clazz.ProjectionMask createMask() {
        return new Clazz.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for fullyQualifiedName
     * 
     * @see Clazz.Fields#fullyQualifiedName
     */
    public boolean hasFullyQualifiedName() {
        if (_fullyQualifiedNameField!= null) {
            return true;
        }
        return super._map.containsKey("fullyQualifiedName");
    }

    /**
     * Remover for fullyQualifiedName
     * 
     * @see Clazz.Fields#fullyQualifiedName
     */
    public void removeFullyQualifiedName() {
        super._map.remove("fullyQualifiedName");
    }

    /**
     * Getter for fullyQualifiedName
     * 
     * @see Clazz.Fields#fullyQualifiedName
     */
    public String getFullyQualifiedName(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getFullyQualifiedName();
            case DEFAULT:
            case NULL:
                if (_fullyQualifiedNameField!= null) {
                    return _fullyQualifiedNameField;
                } else {
                    Object __rawValue = super._map.get("fullyQualifiedName");
                    _fullyQualifiedNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _fullyQualifiedNameField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for fullyQualifiedName
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see Clazz.Fields#fullyQualifiedName
     */
    @Nonnull
    public String getFullyQualifiedName() {
        if (_fullyQualifiedNameField!= null) {
            return _fullyQualifiedNameField;
        } else {
            Object __rawValue = super._map.get("fullyQualifiedName");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("fullyQualifiedName");
            }
            _fullyQualifiedNameField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _fullyQualifiedNameField;
        }
    }

    /**
     * Setter for fullyQualifiedName
     * 
     * @see Clazz.Fields#fullyQualifiedName
     */
    public Clazz setFullyQualifiedName(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFullyQualifiedName(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field fullyQualifiedName of com.linkedin.feathr.featureDataModel.Clazz");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "fullyQualifiedName", value);
                    _fullyQualifiedNameField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFullyQualifiedName();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "fullyQualifiedName", value);
                    _fullyQualifiedNameField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "fullyQualifiedName", value);
                    _fullyQualifiedNameField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for fullyQualifiedName
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see Clazz.Fields#fullyQualifiedName
     */
    public Clazz setFullyQualifiedName(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field fullyQualifiedName of com.linkedin.feathr.featureDataModel.Clazz to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "fullyQualifiedName", value);
            _fullyQualifiedNameField = value;
        }
        return this;
    }

    @Override
    public Clazz clone()
        throws CloneNotSupportedException
    {
        Clazz __clone = ((Clazz) super.clone());
        __clone.__changeListener = new Clazz.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public Clazz copy()
        throws CloneNotSupportedException
    {
        Clazz __copy = ((Clazz) super.copy());
        __copy._fullyQualifiedNameField = null;
        __copy.__changeListener = new Clazz.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final Clazz __objectRef;

        private ChangeListener(Clazz reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "fullyQualifiedName":
                    __objectRef._fullyQualifiedNameField = null;
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
         * A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass
         * 
         */
        public PathSpec fullyQualifiedName() {
            return new PathSpec(getPathComponents(), "fullyQualifiedName");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass
         * 
         */
        public Clazz.ProjectionMask withFullyQualifiedName() {
            getDataMap().put("fullyQualifiedName", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
