
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataList;
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
import com.linkedin.data.template.StringArray;


/**
 * This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/FeatureSource.pdl.")
public class FeatureSource
    extends RecordTemplate
{

    private final static FeatureSource.Fields _fields = new FeatureSource.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**This represents a feature source for FeatureAnchor. That is, it is a source of type 'feature' that can be used for creating other features. For example, say there exist two features: member skills and job skills, they can be the sources of another feature for computing their cosine similarity. In feathr, the feature that represents cosine similarity is called derived feature, refer to go/feathrglossary and go/feathroverview for more details.*/record FeatureSource includes/**Represents the identifier of a DataSource object. This class is expected to be included so the enclosed field can be reused in each concrete DataSource.*/record DataSourceRef{/**Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.*/dataSourceRef:optional string}{/**This is the unique id for the MlFeatureVersionEntity used as the source.*/urn:string/**A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).*/alias:optional string/**Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.*/keyPlaceholderRefs:array[/**Represents the identifier of KeyPlaceholder object. It should be meaningful (eg. memberId) and unique in the scope of a feature's definition. It is used to uniformly reference a KeyPlaceholder object in the feature definition. Refer to KeyPlaceholders for a full example.*/typeref KeyPlaceholderRef=string]=[]}", SchemaFormatType.PDL));
    private String _dataSourceRefField = null;
    private String _urnField = null;
    private String _aliasField = null;
    private StringArray _keyPlaceholderRefsField = null;
    private FeatureSource.ChangeListener __changeListener = new FeatureSource.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_DataSourceRef = SCHEMA.getField("dataSourceRef");
    private final static RecordDataSchema.Field FIELD_Urn = SCHEMA.getField("urn");
    private final static RecordDataSchema.Field FIELD_Alias = SCHEMA.getField("alias");
    private final static RecordDataSchema.Field FIELD_KeyPlaceholderRefs = SCHEMA.getField("keyPlaceholderRefs");
    private final static StringArray DEFAULT_KeyPlaceholderRefs;

    static {
        DEFAULT_KeyPlaceholderRefs = ((FIELD_KeyPlaceholderRefs.getDefault() == null)?null:new StringArray(DataTemplateUtil.castOrThrow(FIELD_KeyPlaceholderRefs.getDefault(), DataList.class)));
    }

    public FeatureSource() {
        super(new DataMap(6, 0.75F), SCHEMA, 2);
        addChangeListener(__changeListener);
    }

    public FeatureSource(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static FeatureSource.Fields fields() {
        return _fields;
    }

    public static FeatureSource.ProjectionMask createMask() {
        return new FeatureSource.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for dataSourceRef
     * 
     * @see FeatureSource.Fields#dataSourceRef
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
     * @see FeatureSource.Fields#dataSourceRef
     */
    public void removeDataSourceRef() {
        super._map.remove("dataSourceRef");
    }

    /**
     * Getter for dataSourceRef
     * 
     * @see FeatureSource.Fields#dataSourceRef
     */
    public String getDataSourceRef(GetMode mode) {
        return getDataSourceRef();
    }

    /**
     * Getter for dataSourceRef
     * 
     * @return
     *     Optional field. Always check for null.
     * @see FeatureSource.Fields#dataSourceRef
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
     * @see FeatureSource.Fields#dataSourceRef
     */
    public FeatureSource setDataSourceRef(String value, SetMode mode) {
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
     * @see FeatureSource.Fields#dataSourceRef
     */
    public FeatureSource setDataSourceRef(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dataSourceRef of com.linkedin.feathr.featureDataModel.FeatureSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dataSourceRef", value);
            _dataSourceRefField = value;
        }
        return this;
    }

    /**
     * Existence checker for urn
     * 
     * @see FeatureSource.Fields#urn
     */
    public boolean hasUrn() {
        if (_urnField!= null) {
            return true;
        }
        return super._map.containsKey("urn");
    }

    /**
     * Remover for urn
     * 
     * @see FeatureSource.Fields#urn
     */
    public void removeUrn() {
        super._map.remove("urn");
    }

    /**
     * Getter for urn
     * 
     * @see FeatureSource.Fields#urn
     */
    public String getUrn(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getUrn();
            case DEFAULT:
            case NULL:
                if (_urnField!= null) {
                    return _urnField;
                } else {
                    Object __rawValue = super._map.get("urn");
                    _urnField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _urnField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for urn
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureSource.Fields#urn
     */
    @Nonnull
    public String getUrn() {
        if (_urnField!= null) {
            return _urnField;
        } else {
            Object __rawValue = super._map.get("urn");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("urn");
            }
            _urnField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _urnField;
        }
    }

    /**
     * Setter for urn
     * 
     * @see FeatureSource.Fields#urn
     */
    public FeatureSource setUrn(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setUrn(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field urn of com.linkedin.feathr.featureDataModel.FeatureSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "urn", value);
                    _urnField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeUrn();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "urn", value);
                    _urnField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "urn", value);
                    _urnField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for urn
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureSource.Fields#urn
     */
    public FeatureSource setUrn(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field urn of com.linkedin.feathr.featureDataModel.FeatureSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "urn", value);
            _urnField = value;
        }
        return this;
    }

    /**
     * Existence checker for alias
     * 
     * @see FeatureSource.Fields#alias
     */
    public boolean hasAlias() {
        if (_aliasField!= null) {
            return true;
        }
        return super._map.containsKey("alias");
    }

    /**
     * Remover for alias
     * 
     * @see FeatureSource.Fields#alias
     */
    public void removeAlias() {
        super._map.remove("alias");
    }

    /**
     * Getter for alias
     * 
     * @see FeatureSource.Fields#alias
     */
    public String getAlias(GetMode mode) {
        return getAlias();
    }

    /**
     * Getter for alias
     * 
     * @return
     *     Optional field. Always check for null.
     * @see FeatureSource.Fields#alias
     */
    @Nullable
    public String getAlias() {
        if (_aliasField!= null) {
            return _aliasField;
        } else {
            Object __rawValue = super._map.get("alias");
            _aliasField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _aliasField;
        }
    }

    /**
     * Setter for alias
     * 
     * @see FeatureSource.Fields#alias
     */
    public FeatureSource setAlias(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setAlias(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeAlias();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "alias", value);
                    _aliasField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "alias", value);
                    _aliasField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for alias
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureSource.Fields#alias
     */
    public FeatureSource setAlias(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field alias of com.linkedin.feathr.featureDataModel.FeatureSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "alias", value);
            _aliasField = value;
        }
        return this;
    }

    /**
     * Existence checker for keyPlaceholderRefs
     * 
     * @see FeatureSource.Fields#keyPlaceholderRefs
     */
    public boolean hasKeyPlaceholderRefs() {
        if (_keyPlaceholderRefsField!= null) {
            return true;
        }
        return super._map.containsKey("keyPlaceholderRefs");
    }

    /**
     * Remover for keyPlaceholderRefs
     * 
     * @see FeatureSource.Fields#keyPlaceholderRefs
     */
    public void removeKeyPlaceholderRefs() {
        super._map.remove("keyPlaceholderRefs");
    }

    /**
     * Getter for keyPlaceholderRefs
     * 
     * @see FeatureSource.Fields#keyPlaceholderRefs
     */
    public StringArray getKeyPlaceholderRefs(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getKeyPlaceholderRefs();
            case NULL:
                if (_keyPlaceholderRefsField!= null) {
                    return _keyPlaceholderRefsField;
                } else {
                    Object __rawValue = super._map.get("keyPlaceholderRefs");
                    _keyPlaceholderRefsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _keyPlaceholderRefsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for keyPlaceholderRefs
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see FeatureSource.Fields#keyPlaceholderRefs
     */
    @Nonnull
    public StringArray getKeyPlaceholderRefs() {
        if (_keyPlaceholderRefsField!= null) {
            return _keyPlaceholderRefsField;
        } else {
            Object __rawValue = super._map.get("keyPlaceholderRefs");
            if (__rawValue == null) {
                return DEFAULT_KeyPlaceholderRefs;
            }
            _keyPlaceholderRefsField = ((__rawValue == null)?null:new StringArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _keyPlaceholderRefsField;
        }
    }

    /**
     * Setter for keyPlaceholderRefs
     * 
     * @see FeatureSource.Fields#keyPlaceholderRefs
     */
    public FeatureSource setKeyPlaceholderRefs(StringArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setKeyPlaceholderRefs(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field keyPlaceholderRefs of com.linkedin.feathr.featureDataModel.FeatureSource");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRefs", value.data());
                    _keyPlaceholderRefsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeKeyPlaceholderRefs();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRefs", value.data());
                    _keyPlaceholderRefsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRefs", value.data());
                    _keyPlaceholderRefsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for keyPlaceholderRefs
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see FeatureSource.Fields#keyPlaceholderRefs
     */
    public FeatureSource setKeyPlaceholderRefs(
        @Nonnull
        StringArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field keyPlaceholderRefs of com.linkedin.feathr.featureDataModel.FeatureSource to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "keyPlaceholderRefs", value.data());
            _keyPlaceholderRefsField = value;
        }
        return this;
    }

    @Override
    public FeatureSource clone()
        throws CloneNotSupportedException
    {
        FeatureSource __clone = ((FeatureSource) super.clone());
        __clone.__changeListener = new FeatureSource.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public FeatureSource copy()
        throws CloneNotSupportedException
    {
        FeatureSource __copy = ((FeatureSource) super.copy());
        __copy._urnField = null;
        __copy._dataSourceRefField = null;
        __copy._keyPlaceholderRefsField = null;
        __copy._aliasField = null;
        __copy.__changeListener = new FeatureSource.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final FeatureSource __objectRef;

        private ChangeListener(FeatureSource reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "urn":
                    __objectRef._urnField = null;
                    break;
                case "dataSourceRef":
                    __objectRef._dataSourceRefField = null;
                    break;
                case "keyPlaceholderRefs":
                    __objectRef._keyPlaceholderRefsField = null;
                    break;
                case "alias":
                    __objectRef._aliasField = null;
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
         * This is the unique id for the MlFeatureVersionEntity used as the source.
         * 
         */
        public PathSpec urn() {
            return new PathSpec(getPathComponents(), "urn");
        }

        /**
         * A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).
         * 
         */
        public PathSpec alias() {
            return new PathSpec(getPathComponents(), "alias");
        }

        /**
         * Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.
         * 
         */
        public PathSpec keyPlaceholderRefs() {
            return new PathSpec(getPathComponents(), "keyPlaceholderRefs");
        }

        /**
         * Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.
         * 
         */
        public PathSpec keyPlaceholderRefs(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "keyPlaceholderRefs");
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
            super(6);
        }

        /**
         * Represents the identifier of a DataSource object. Given a DataSource object is already enclosed in a denormalized manner in each FeatureAnchor, this field is not used for looking up the DataSource object, instead it is optional and should be meaningful to describe a data source for informational usages like metrics.
         * 
         */
        public FeatureSource.ProjectionMask withDataSourceRef() {
            getDataMap().put("dataSourceRef", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * This is the unique id for the MlFeatureVersionEntity used as the source.
         * 
         */
        public FeatureSource.ProjectionMask withUrn() {
            getDataMap().put("urn", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * A feature's alias to be used in transformation function. It should be unique in the scope of a FeatureSourcesAnchor. It is useful when the same feature is used as FeatureSource multiple times with different keys. For example, if we are modeing the problem of the probability of a member A (viewer) seeing the profile of member B (viewee) and we want to use the skills of both viewer and viewee as features. If the alias is set, the alias (instead of feature name) will be used to reference this FeatureSource in the transformation function (especially expression languages like MVEL).
         * 
         */
        public FeatureSource.ProjectionMask withAlias() {
            getDataMap().put("alias", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.
         * 
         */
        public FeatureSource.ProjectionMask withKeyPlaceholderRefs() {
            getDataMap().put("keyPlaceholderRefs", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents zero, one or multiple keyPlaceholderRefs which are used as the identifiers to reference KeyPlaceholders of the FeatureSourcesAnchor. This is to ensure corresponding key values can be propogated to the right FeatureSource. Refer to KeyPlaceholders for a full example.
         * 
         */
        public FeatureSource.ProjectionMask withKeyPlaceholderRefs(Integer start, Integer count) {
            getDataMap().put("keyPlaceholderRefs", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("keyPlaceholderRefs").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("keyPlaceholderRefs").put("$count", count);
            }
            return this;
        }

    }

}
