
package com.linkedin.feathr.featureDataModel;

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
 * Represents a in-memory passthrough data source. Passthrough data sources are used when the data are not from external sources such as Rest.li, Venice and Espresso. It's commonly used for contextual features that are supplied as part of an online request. See Passthrough section in go/frameonline.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/InMemoryPassthroughDataSource.pdl.")
public class InMemoryPassthroughDataSource
    extends RecordTemplate
{

    private final static InMemoryPassthroughDataSource.Fields _fields = new InMemoryPassthroughDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a in-memory passthrough data source. Passthrough data sources are used when the data are not from external sources such as Rest.li, Venice and Espresso. It's commonly used for contextual features that are supplied as part of an online request. See Passthrough section in go/frameonline.*/record InMemoryPassthroughDataSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**The fully qualified Java class name for the data model of passthrough (in-memory) data that is supplied as part of the online request.*/dataModel:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}}", SchemaFormatType.PDL));
    private String _dataSourceRefField = null;
    private Clazz _dataModelField = null;
    private InMemoryPassthroughDataSource.ChangeListener __changeListener = new InMemoryPassthroughDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_DataModel = SCHEMA.getField("dataModel");

    public InMemoryPassthroughDataSource() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public InMemoryPassthroughDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static InMemoryPassthroughDataSource.Fields fields() {
        return _fields;
    }

    public static InMemoryPassthroughDataSource.ProjectionMask createMask() {
        return new InMemoryPassthroughDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataSourceRef
     */
    public boolean hasDataSourceRef() {
        if (_dataSourceRefField!= null) {
            return true;
        }
        return super._map.containsKey("dataSourceRef");
    }

    /**
     * Remover for dataSourceRef
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see InMemoryPassthroughDataSource.Fields#dataSourceRef
     */
    @Nullable
    public String getDataSourceRef() {
        if (_dataSourceRefField!= null) {
            return _dataSourceRefField;
        } else {
            Object __rawValue = super._map.get("dataSourceRef");
            _dataSourceRefField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _dataSourceRefField;
        }
    }

    /**
     * Setter for dataSourceRef
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataSourceRef
     */
    public InMemoryPassthroughDataSource setDataSourceRef(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDataSourceRef(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDataSourceRef();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
                    _dataSourceRefField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
                    _dataSourceRefField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dataSourceRef
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see InMemoryPassthroughDataSource.Fields#dataSourceRef
     */
    public InMemoryPassthroughDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataModel
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataModel
     */
    public boolean hasDataModel() {
        if (_dataModelField!= null) {
            return true;
        }
        return super._map.containsKey("dataModel");
    }

    /**
     * Remover for dataModel
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataModel
     */
    public void removeDataModel() {
        super._map.remove("dataModel");
    }

    /**
     * Getter for dataModel
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataModel
     */
    public Clazz getDataModel(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getDataModel();
            case DEFAULT:
            case NULL:
                if (_dataModelField!= null) {
                    return _dataModelField;
                } else {
                    Object __rawValue = super._map.get("dataModel");
                    _dataModelField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _dataModelField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for dataModel
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see InMemoryPassthroughDataSource.Fields#dataModel
     */
    @Nonnull
    public Clazz getDataModel() {
        if (_dataModelField!= null) {
            return _dataModelField;
        } else {
            Object __rawValue = super._map.get("dataModel");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("dataModel");
            }
            _dataModelField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _dataModelField;
        }
    }

    /**
     * Setter for dataModel
     * 
     * @see InMemoryPassthroughDataSource.Fields#dataModel
     */
    public InMemoryPassthroughDataSource setDataModel(Clazz value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDataModel(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field dataModel of com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
                    _dataModelField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDataModel();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
                    _dataModelField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
                    _dataModelField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dataModel
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see InMemoryPassthroughDataSource.Fields#dataModel
     */
    public InMemoryPassthroughDataSource setDataModel(
        @Nonnull
        Clazz value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataModel of com.linkedin.feathr.featureDataModel.InMemoryPassthroughDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataModel", value.data());
            _dataModelField = value;
        }
        return this;
    }

    @Override
    public InMemoryPassthroughDataSource clone()
        throws CloneNotSupportedException
    {
        InMemoryPassthroughDataSource __clone = ((InMemoryPassthroughDataSource) super.clone());
        __clone.__changeListener = new InMemoryPassthroughDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public InMemoryPassthroughDataSource copy()
        throws CloneNotSupportedException
    {
        InMemoryPassthroughDataSource __copy = ((InMemoryPassthroughDataSource) super.copy());
        __copy._dataSourceRefField = null;
        __copy._dataModelField = null;
        __copy.__changeListener = new InMemoryPassthroughDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final InMemoryPassthroughDataSource __objectRef;

        private ChangeListener(InMemoryPassthroughDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
                    break;
                case "dataModel":
                    __objectRef._dataModelField = null;
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
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public PathSpec dataSourceRef() {
            return new PathSpec(getPathComponents(), "dataSourceRef");
        }

        /**
         * The fully qualified Java class name for the data model of passthrough (in-memory) data that is supplied as part of the online request.
         * 
         */
        public com.linkedin.feathr.featureDataModel.Clazz.Fields dataModel() {
            return new com.linkedin.feathr.featureDataModel.Clazz.Fields(getPathComponents(), "dataModel");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask _dataModelMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public InMemoryPassthroughDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * The fully qualified Java class name for the data model of passthrough (in-memory) data that is supplied as part of the online request.
         * 
         */
        public InMemoryPassthroughDataSource.ProjectionMask withDataModel(Function<com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask, com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask> nestedMask) {
            _dataModelMask = nestedMask.apply(((_dataModelMask == null)?Clazz.createMask():_dataModelMask));
            getDataMap().put("dataModel", _dataModelMask.getDataMap());
            return this;
        }

        /**
         * The fully qualified Java class name for the data model of passthrough (in-memory) data that is supplied as part of the online request.
         * 
         */
        public InMemoryPassthroughDataSource.ProjectionMask withDataModel() {
            _dataModelMask = null;
            getDataMap().put("dataModel", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
