
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
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
import com.linkedin.data.template.StringMap;


/**
 * User defined function that can be used in feature extraction or derivation.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/UserDefinedFunction.pdl.")
public class UserDefinedFunction
    extends RecordTemplate
{

    private final static UserDefinedFunction.Fields _fields = new UserDefinedFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**User defined function that can be used in feature extraction or derivation.*/record UserDefinedFunction{/**Reference to the class that implements the user defined function.*/clazz:/**Reference to a class by fully-qualified name*/record Clazz{/**A fully-qualified class name. e.g. com.linkedin.proml.mlFeatureAnchor.MyClass*/fullyQualifiedName:string}/**Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : [\"waterlooCompany_terms_hashed\", \"waterlooCompany_values\"], param2 : \"com.linkedin.quasar.encoding.SomeEncodingClass\u201d } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.*/parameters:map[string/**Represents a Json string.*/typeref JsonString=string]={}}", SchemaFormatType.PDL));
    private Clazz _clazzField = null;
    private StringMap _parametersField = null;
    private UserDefinedFunction.ChangeListener __changeListener = new UserDefinedFunction.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Clazz = SCHEMA.getField("clazz");
    private final static RecordDataSchema.Field FIELD_Parameters = SCHEMA.getField("parameters");
    private final static StringMap DEFAULT_Parameters;

    static {
        DEFAULT_Parameters = ((FIELD_Parameters.getDefault() == null)?null:new StringMap(DataTemplateUtil.castOrThrow(FIELD_Parameters.getDefault(), DataMap.class)));
    }

    public UserDefinedFunction() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public UserDefinedFunction(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static UserDefinedFunction.Fields fields() {
        return _fields;
    }

    public static UserDefinedFunction.ProjectionMask createMask() {
        return new UserDefinedFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for clazz
     * 
     * @see UserDefinedFunction.Fields#clazz
     */
    public boolean hasClazz() {
        if (_clazzField!= null) {
            return true;
        }
        return super._map.containsKey("clazz");
    }

    /**
     * Remover for clazz
     * 
     * @see UserDefinedFunction.Fields#clazz
     */
    public void removeClazz() {
        super._map.remove("clazz");
    }

    /**
     * Getter for clazz
     * 
     * @see UserDefinedFunction.Fields#clazz
     */
    public Clazz getClazz(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getClazz();
            case DEFAULT:
            case NULL:
                if (_clazzField!= null) {
                    return _clazzField;
                } else {
                    Object __rawValue = super._map.get("clazz");
                    _clazzField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _clazzField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for clazz
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see UserDefinedFunction.Fields#clazz
     */
    @Nonnull
    public Clazz getClazz() {
        if (_clazzField!= null) {
            return _clazzField;
        } else {
            Object __rawValue = super._map.get("clazz");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("clazz");
            }
            _clazzField = ((__rawValue == null)?null:new Clazz(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _clazzField;
        }
    }

    /**
     * Setter for clazz
     * 
     * @see UserDefinedFunction.Fields#clazz
     */
    public UserDefinedFunction setClazz(Clazz value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setClazz(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field clazz of com.linkedin.feathr.featureDataModel.UserDefinedFunction");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "clazz", value.data());
                    _clazzField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeClazz();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "clazz", value.data());
                    _clazzField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "clazz", value.data());
                    _clazzField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for clazz
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see UserDefinedFunction.Fields#clazz
     */
    public UserDefinedFunction setClazz(
        @Nonnull
        Clazz value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field clazz of com.linkedin.feathr.featureDataModel.UserDefinedFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "clazz", value.data());
            _clazzField = value;
        }
        return this;
    }

    /**
     * Existence checker for parameters
     * 
     * @see UserDefinedFunction.Fields#parameters
     */
    public boolean hasParameters() {
        if (_parametersField!= null) {
            return true;
        }
        return super._map.containsKey("parameters");
    }

    /**
     * Remover for parameters
     * 
     * @see UserDefinedFunction.Fields#parameters
     */
    public void removeParameters() {
        super._map.remove("parameters");
    }

    /**
     * Getter for parameters
     * 
     * @see UserDefinedFunction.Fields#parameters
     */
    public StringMap getParameters(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getParameters();
            case NULL:
                if (_parametersField!= null) {
                    return _parametersField;
                } else {
                    Object __rawValue = super._map.get("parameters");
                    _parametersField = ((__rawValue == null)?null:new StringMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _parametersField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for parameters
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see UserDefinedFunction.Fields#parameters
     */
    @Nonnull
    public StringMap getParameters() {
        if (_parametersField!= null) {
            return _parametersField;
        } else {
            Object __rawValue = super._map.get("parameters");
            if (__rawValue == null) {
                return DEFAULT_Parameters;
            }
            _parametersField = ((__rawValue == null)?null:new StringMap(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _parametersField;
        }
    }

    /**
     * Setter for parameters
     * 
     * @see UserDefinedFunction.Fields#parameters
     */
    public UserDefinedFunction setParameters(StringMap value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setParameters(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field parameters of com.linkedin.feathr.featureDataModel.UserDefinedFunction");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
                    _parametersField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeParameters();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
                    _parametersField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
                    _parametersField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for parameters
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see UserDefinedFunction.Fields#parameters
     */
    public UserDefinedFunction setParameters(
        @Nonnull
        StringMap value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field parameters of com.linkedin.feathr.featureDataModel.UserDefinedFunction to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "parameters", value.data());
            _parametersField = value;
        }
        return this;
    }

    @Override
    public UserDefinedFunction clone()
        throws CloneNotSupportedException
    {
        UserDefinedFunction __clone = ((UserDefinedFunction) super.clone());
        __clone.__changeListener = new UserDefinedFunction.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public UserDefinedFunction copy()
        throws CloneNotSupportedException
    {
        UserDefinedFunction __copy = ((UserDefinedFunction) super.copy());
        __copy._clazzField = null;
        __copy._parametersField = null;
        __copy.__changeListener = new UserDefinedFunction.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final UserDefinedFunction __objectRef;

        private ChangeListener(UserDefinedFunction reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "clazz":
                    __objectRef._clazzField = null;
                    break;
                case "parameters":
                    __objectRef._parametersField = null;
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
         * Reference to the class that implements the user defined function.
         * 
         */
        public com.linkedin.feathr.featureDataModel.Clazz.Fields clazz() {
            return new com.linkedin.feathr.featureDataModel.Clazz.Fields(getPathComponents(), "clazz");
        }

        /**
         * Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : ["waterlooCompany_terms_hashed", "waterlooCompany_values"], param2 : "com.linkedin.quasar.encoding.SomeEncodingClass” } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.
         * 
         */
        public PathSpec parameters() {
            return new PathSpec(getPathComponents(), "parameters");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask _clazzMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * Reference to the class that implements the user defined function.
         * 
         */
        public UserDefinedFunction.ProjectionMask withClazz(Function<com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask, com.linkedin.feathr.featureDataModel.Clazz.ProjectionMask> nestedMask) {
            _clazzMask = nestedMask.apply(((_clazzMask == null)?Clazz.createMask():_clazzMask));
            getDataMap().put("clazz", _clazzMask.getDataMap());
            return this;
        }

        /**
         * Reference to the class that implements the user defined function.
         * 
         */
        public UserDefinedFunction.ProjectionMask withClazz() {
            _clazzMask = null;
            getDataMap().put("clazz", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Some UserDefinedFunction requires additional custom parameters. This field defines the custom parameters of the user defined function, represented as a map of string to json blob. The key is the parameter name, and the value is the parameter value represented as a json blob. For example, the parameters may look like: { param1 : ["waterlooCompany_terms_hashed", "waterlooCompany_values"], param2 : "com.linkedin.quasar.encoding.SomeEncodingClass” } Frame will be responsible of parsing the parameters map into a CustomParameters class defined by application: public class CustomParameters { List<String> param1; String param2; } CustomParameters will be used in the constructor of the UserDefinedFunction.
         * 
         */
        public UserDefinedFunction.ProjectionMask withParameters() {
            getDataMap().put("parameters", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
