
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
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.GetMode;
import com.linkedin.data.template.RecordTemplate;
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;


/**
 * The location of a Dali dataset or view. See [go/dali](http://go/dali).
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/DaliLocation.pdl.")
public class DaliLocation
    extends RecordTemplate
{

    private final static DaliLocation.Fields _fields = new DaliLocation.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**The location of a Dali dataset or view. See [go/dali](http://go/dali).*/record DaliLocation{/**The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.*/uri:{namespace com.linkedin.frame.common@java.class=\"java.net.URI\"typeref Uri=string}}", SchemaFormatType.PDL));
    private java.net.URI _uriField = null;
    private DaliLocation.ChangeListener __changeListener = new DaliLocation.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Uri = SCHEMA.getField("uri");

    static {
        Custom.initializeCustomClass(java.net.URI.class);
    }

    public DaliLocation() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public DaliLocation(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static DaliLocation.Fields fields() {
        return _fields;
    }

    public static DaliLocation.ProjectionMask createMask() {
        return new DaliLocation.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for uri
     * 
     * @see DaliLocation.Fields#uri
     */
    public boolean hasUri() {
        if (_uriField!= null) {
            return true;
        }
        return super._map.containsKey("uri");
    }

    /**
     * Remover for uri
     * 
     * @see DaliLocation.Fields#uri
     */
    public void removeUri() {
        super._map.remove("uri");
    }

    /**
     * Getter for uri
     * 
     * @see DaliLocation.Fields#uri
     */
    public java.net.URI getUri(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getUri();
            case DEFAULT:
            case NULL:
                if (_uriField!= null) {
                    return _uriField;
                } else {
                    Object __rawValue = super._map.get("uri");
                    _uriField = DataTemplateUtil.coerceCustomOutput(__rawValue, java.net.URI.class);
                    return _uriField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for uri
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see DaliLocation.Fields#uri
     */
    @Nonnull
    public java.net.URI getUri() {
        if (_uriField!= null) {
            return _uriField;
        } else {
            Object __rawValue = super._map.get("uri");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("uri");
            }
            _uriField = DataTemplateUtil.coerceCustomOutput(__rawValue, java.net.URI.class);
            return _uriField;
        }
    }

    /**
     * Setter for uri
     * 
     * @see DaliLocation.Fields#uri
     */
    public DaliLocation setUri(java.net.URI value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setUri(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field uri of com.linkedin.feathr.featureDataModel.DaliLocation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
                    _uriField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeUri();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
                    _uriField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
                    _uriField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for uri
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DaliLocation.Fields#uri
     */
    public DaliLocation setUri(
        @Nonnull
        java.net.URI value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field uri of com.linkedin.feathr.featureDataModel.DaliLocation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "uri", DataTemplateUtil.coerceCustomInput(value, java.net.URI.class));
            _uriField = value;
        }
        return this;
    }

    @Override
    public DaliLocation clone()
        throws CloneNotSupportedException
    {
        DaliLocation __clone = ((DaliLocation) super.clone());
        __clone.__changeListener = new DaliLocation.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public DaliLocation copy()
        throws CloneNotSupportedException
    {
        DaliLocation __copy = ((DaliLocation) super.copy());
        __copy._uriField = null;
        __copy.__changeListener = new DaliLocation.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final DaliLocation __objectRef;

        private ChangeListener(DaliLocation reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "uri":
                    __objectRef._uriField = null;
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
         * The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.
         * 
         */
        public PathSpec uri() {
            return new PathSpec(getPathComponents(), "uri");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * The URI to the Dali dataset/view in the form of `dalids:///db_name.table_or_view_name`.
         * 
         */
        public DaliLocation.ProjectionMask withUri() {
            getDataMap().put("uri", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
