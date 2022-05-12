
package com.linkedin.feathr.featureDataModel;

import java.util.List;
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
import com.linkedin.data.template.SetMode;


/**
 * Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/DataSourceRef.pdl.")
public class DataSourceRef
    extends RecordTemplate
{

    private final static DataSourceRef.Fields _fields = new DataSourceRef.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}", SchemaFormatType.PDL));
    private String _dataSourceRefField = null;
    private DataSourceRef.ChangeListener __changeListener = new DataSourceRef.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");

    public DataSourceRef() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public DataSourceRef(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static DataSourceRef.Fields fields() {
        return _fields;
    }

    public static DataSourceRef.ProjectionMask createMask() {
        return new DataSourceRef.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see DataSourceRef.Fields#dataSourceRef
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
     * @see DataSourceRef.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see DataSourceRef.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see DataSourceRef.Fields#dataSourceRef
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
     * @see DataSourceRef.Fields#dataSourceRef
     */
    public DataSourceRef setDataSourceRef(String value, SetMode mode) {
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
     * @see DataSourceRef.Fields#dataSourceRef
     */
    public DataSourceRef setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.DataSourceRef to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    @Override
    public DataSourceRef clone()
        throws CloneNotSupportedException
    {
        DataSourceRef __clone = ((DataSourceRef) super.clone());
        __clone.__changeListener = new DataSourceRef.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public DataSourceRef copy()
        throws CloneNotSupportedException
    {
        DataSourceRef __copy = ((DataSourceRef) super.copy());
        __copy._dataSourceRefField = null;
        __copy.__changeListener = new DataSourceRef.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final DataSourceRef __objectRef;

        private ChangeListener(DataSourceRef reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
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

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public DataSourceRef.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
