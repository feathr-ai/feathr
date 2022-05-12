
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
import com.linkedin.data.template.SetMode;


/**
 * Represents a observation data passthrough data source. Passthrough data sources are used when the data are not from external sources. It's commonly used for features that already exist in the observation data. See Pass-through Features section in go/frameoffline.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/ObservationPassthroughDataSource.pdl.")
public class ObservationPassthroughDataSource
    extends RecordTemplate
{

    private final static ObservationPassthroughDataSource.Fields _fields = new ObservationPassthroughDataSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a observation data passthrough data source. Passthrough data sources are used when the data are not from external sources. It's commonly used for features that already exist in the observation data. See Pass-through Features section in go/frameoffline.*/record ObservationPassthroughDataSource includes/**Represents a feature's key that is extracted from each row of an offline data source and is used to join with observation data to form a training dataset. This class is expected to be included so the definitions of enclosed fields can be reused.*/record OfflineDataSourceKey{/**Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.*/keyFunction:optional union[/**An expression in MVEL language. For more information please refer to go/framemvel.*/record MvelExpression{/**The MVEL expression.*/mvel:string}/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}]}/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{}", SchemaFormatType.PDL));
    private com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction _keyFunctionField = null;
    private String _dataSourceRefField = null;
    private ObservationPassthroughDataSource.ChangeListener __changeListener = new ObservationPassthroughDataSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_KeyFunction = SCHEMA.getField("keyFunction");
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");

    public ObservationPassthroughDataSource() {
        super(new DataMap(3, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public ObservationPassthroughDataSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static ObservationPassthroughDataSource.Fields fields() {
        return _fields;
    }

    public static ObservationPassthroughDataSource.ProjectionMask createMask() {
        return new ObservationPassthroughDataSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for keyFunction
     * 
     * @see ObservationPassthroughDataSource.Fields#keyFunction
     */
    public boolean hasKeyFunction() {
        if (_keyFunctionField!= null) {
            return true;
        }
        return super._map.containsKey("keyFunction");
    }

    /**
     * Remover for keyFunction
     * 
     * @see ObservationPassthroughDataSource.Fields#keyFunction
     */
    public void removeKeyFunction() {
        super._map.remove("keyFunction");
    }

    /**
     * Getter for keyFunction
     * 
     * @see ObservationPassthroughDataSource.Fields#keyFunction
     */
    public com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction getKeyFunction(GetMode mode) {
        return getKeyFunction();
    }

    /**
     * Getter for keyFunction
     * 
     * @return
     *     Optional field. Always check for null.
     * @see ObservationPassthroughDataSource.Fields#keyFunction
     */
    @Nullable
    public com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction getKeyFunction() {
        if (_keyFunctionField!= null) {
            return _keyFunctionField;
        } else {
            Object __rawValue = super._map.get("keyFunction");
            _keyFunctionField = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction(__rawValue));
            return _keyFunctionField;
        }
    }

    /**
     * Setter for keyFunction
     * 
     * @see ObservationPassthroughDataSource.Fields#keyFunction
     */
    public ObservationPassthroughDataSource setKeyFunction(com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyFunction(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyFunction();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
                    _keyFunctionField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
                    _keyFunctionField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyFunction
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see ObservationPassthroughDataSource.Fields#keyFunction
     */
    public ObservationPassthroughDataSource setKeyFunction(
        @Nonnull
        com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyFunction of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyFunction", value.data());
            _keyFunctionField = value;
        }
        return this;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see ObservationPassthroughDataSource.Fields#dataSourceRef
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
     * @see ObservationPassthroughDataSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see ObservationPassthroughDataSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see ObservationPassthroughDataSource.Fields#dataSourceRef
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
     * @see ObservationPassthroughDataSource.Fields#dataSourceRef
     */
    public ObservationPassthroughDataSource setDataSourceRef(String value, SetMode mode) {
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
     * @see ObservationPassthroughDataSource.Fields#dataSourceRef
     */
    public ObservationPassthroughDataSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.ObservationPassthroughDataSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    @Override
    public ObservationPassthroughDataSource clone()
        throws CloneNotSupportedException
    {
        ObservationPassthroughDataSource __clone = ((ObservationPassthroughDataSource) super.clone());
        __clone.__changeListener = new ObservationPassthroughDataSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public ObservationPassthroughDataSource copy()
        throws CloneNotSupportedException
    {
        ObservationPassthroughDataSource __copy = ((ObservationPassthroughDataSource) super.copy());
        __copy._keyFunctionField = null;
        __copy._dataSourceRefField = null;
        __copy.__changeListener = new ObservationPassthroughDataSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final ObservationPassthroughDataSource __objectRef;

        private ChangeListener(ObservationPassthroughDataSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "keyFunction":
                    __objectRef._keyFunctionField = null;
                    break;
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
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.Fields keyFunction() {
            return new com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.Fields(getPathComponents(), "keyFunction");
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

        private com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask _keyFunctionMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public ObservationPassthroughDataSource.ProjectionMask withKeyFunction(Function<com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask, com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.ProjectionMask> nestedMask) {
            _keyFunctionMask = nestedMask.apply(((_keyFunctionMask == null)?com.linkedin.feathr.featureDataModel.OfflineDataSourceKey.KeyFunction.createMask():_keyFunctionMask));
            getDataMap().put("keyFunction", _keyFunctionMask.getDataMap());
            return this;
        }

        /**
         * Key function specifies how to extract the feature's key from each row of the offline data source. For example, an offline dataset has memberUrn field, a key function being defined as getIdFromUrn(memberUrn) means the feature key is a numeric member id, which can later be used to join with observation data that also has numeric member id column. A feature's key can have one key part or multiple key parts (compound key). This field should be required, keeping it optional for fulfilling backward compatiblity requirement during schema evolution. For more details, refer to https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Frame+Offline+User+Guide#FrameOfflineUserGuide-KeyExtraction.
         * 
         */
        public ObservationPassthroughDataSource.ProjectionMask withKeyFunction() {
            _keyFunctionMask = null;
            getDataMap().put("keyFunction", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public ObservationPassthroughDataSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
