
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataMap;
import com.linkedin.data.collections.CheckedUtil;
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
import com.linkedin.data.template.UnionTemplate;


/**
 * Sliding window embedding aggregation produces a single embedding by performing element-wise operations or discretization on a collection of embeddings within a given time interval. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and produce the aggregagated embedding.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowEmbeddingAggregation.pdl.")
public class SlidingWindowEmbeddingAggregation
    extends RecordTemplate
{

    private final static SlidingWindowEmbeddingAggregation.Fields _fields = new SlidingWindowEmbeddingAggregation.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Sliding window embedding aggregation produces a single embedding by performing element-wise operations or discretization on a collection of embeddings within a given time interval. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and produce the aggregagated embedding.*/record SlidingWindowEmbeddingAggregation{/**The target column to perform aggregation against.*/targetColumn:union[/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}]/**Represents supported types for embedding aggregation.*/aggregationType:enum EmbeddingAggregationType{/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. */MAX_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. */MIN_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. */AVG_POOLING}/**Represents the time window to look back from label data's timestamp.*/window:/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]}", SchemaFormatType.PDL));
    private SlidingWindowEmbeddingAggregation.TargetColumn _targetColumnField = null;
    private EmbeddingAggregationType _aggregationTypeField = null;
    private Window _windowField = null;
    private SlidingWindowEmbeddingAggregation.Filter _filterField = null;
    private SlidingWindowEmbeddingAggregation.GroupBy _groupByField = null;
    private SlidingWindowEmbeddingAggregation.ChangeListener __changeListener = new SlidingWindowEmbeddingAggregation.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TargetColumn = SCHEMA.getField("targetColumn");
    private final static RecordDataSchema.Field FIELD_AggregationType = SCHEMA.getField("aggregationType");
    private final static RecordDataSchema.Field FIELD_Window = SCHEMA.getField("window");
    private final static RecordDataSchema.Field FIELD_Filter = SCHEMA.getField("filter");
    private final static RecordDataSchema.Field FIELD_GroupBy = SCHEMA.getField("groupBy");

    public SlidingWindowEmbeddingAggregation() {
        super(new DataMap(7, 0.75F), SCHEMA, 6);
        addChangeListener(__changeListener);
    }

    public SlidingWindowEmbeddingAggregation(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static SlidingWindowEmbeddingAggregation.Fields fields() {
        return _fields;
    }

    public static SlidingWindowEmbeddingAggregation.ProjectionMask createMask() {
        return new SlidingWindowEmbeddingAggregation.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for targetColumn
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#targetColumn
     */
    public boolean hasTargetColumn() {
        if (_targetColumnField!= null) {
            return true;
        }
        return super._map.containsKey("targetColumn");
    }

    /**
     * Remover for targetColumn
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#targetColumn
     */
    public void removeTargetColumn() {
        super._map.remove("targetColumn");
    }

    /**
     * Getter for targetColumn
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#targetColumn
     */
    public SlidingWindowEmbeddingAggregation.TargetColumn getTargetColumn(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTargetColumn();
            case DEFAULT:
            case NULL:
                if (_targetColumnField!= null) {
                    return _targetColumnField;
                } else {
                    Object __rawValue = super._map.get("targetColumn");
                    _targetColumnField = ((__rawValue == null)?null:new SlidingWindowEmbeddingAggregation.TargetColumn(__rawValue));
                    return _targetColumnField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for targetColumn
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SlidingWindowEmbeddingAggregation.Fields#targetColumn
     */
    @Nonnull
    public SlidingWindowEmbeddingAggregation.TargetColumn getTargetColumn() {
        if (_targetColumnField!= null) {
            return _targetColumnField;
        } else {
            Object __rawValue = super._map.get("targetColumn");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("targetColumn");
            }
            _targetColumnField = ((__rawValue == null)?null:new SlidingWindowEmbeddingAggregation.TargetColumn(__rawValue));
            return _targetColumnField;
        }
    }

    /**
     * Setter for targetColumn
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#targetColumn
     */
    public SlidingWindowEmbeddingAggregation setTargetColumn(SlidingWindowEmbeddingAggregation.TargetColumn value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTargetColumn(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field targetColumn of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "targetColumn", value.data());
                    _targetColumnField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTargetColumn();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "targetColumn", value.data());
                    _targetColumnField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "targetColumn", value.data());
                    _targetColumnField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for targetColumn
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SlidingWindowEmbeddingAggregation.Fields#targetColumn
     */
    public SlidingWindowEmbeddingAggregation setTargetColumn(
        @Nonnull
        SlidingWindowEmbeddingAggregation.TargetColumn value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field targetColumn of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "targetColumn", value.data());
            _targetColumnField = value;
        }
        return this;
    }

    /**
     * Existence checker for aggregationType
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#aggregationType
     */
    public boolean hasAggregationType() {
        if (_aggregationTypeField!= null) {
            return true;
        }
        return super._map.containsKey("aggregationType");
    }

    /**
     * Remover for aggregationType
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#aggregationType
     */
    public void removeAggregationType() {
        super._map.remove("aggregationType");
    }

    /**
     * Getter for aggregationType
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#aggregationType
     */
    public EmbeddingAggregationType getAggregationType(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getAggregationType();
            case DEFAULT:
            case NULL:
                if (_aggregationTypeField!= null) {
                    return _aggregationTypeField;
                } else {
                    Object __rawValue = super._map.get("aggregationType");
                    _aggregationTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, EmbeddingAggregationType.class, EmbeddingAggregationType.$UNKNOWN);
                    return _aggregationTypeField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for aggregationType
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SlidingWindowEmbeddingAggregation.Fields#aggregationType
     */
    @Nonnull
    public EmbeddingAggregationType getAggregationType() {
        if (_aggregationTypeField!= null) {
            return _aggregationTypeField;
        } else {
            Object __rawValue = super._map.get("aggregationType");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("aggregationType");
            }
            _aggregationTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, EmbeddingAggregationType.class, EmbeddingAggregationType.$UNKNOWN);
            return _aggregationTypeField;
        }
    }

    /**
     * Setter for aggregationType
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#aggregationType
     */
    public SlidingWindowEmbeddingAggregation setAggregationType(EmbeddingAggregationType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setAggregationType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field aggregationType of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "aggregationType", value.name());
                    _aggregationTypeField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeAggregationType();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "aggregationType", value.name());
                    _aggregationTypeField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "aggregationType", value.name());
                    _aggregationTypeField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for aggregationType
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SlidingWindowEmbeddingAggregation.Fields#aggregationType
     */
    public SlidingWindowEmbeddingAggregation setAggregationType(
        @Nonnull
        EmbeddingAggregationType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field aggregationType of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "aggregationType", value.name());
            _aggregationTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for window
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#window
     */
    public boolean hasWindow() {
        if (_windowField!= null) {
            return true;
        }
        return super._map.containsKey("window");
    }

    /**
     * Remover for window
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#window
     */
    public void removeWindow() {
        super._map.remove("window");
    }

    /**
     * Getter for window
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#window
     */
    public Window getWindow(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getWindow();
            case DEFAULT:
            case NULL:
                if (_windowField!= null) {
                    return _windowField;
                } else {
                    Object __rawValue = super._map.get("window");
                    _windowField = ((__rawValue == null)?null:new Window(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _windowField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for window
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SlidingWindowEmbeddingAggregation.Fields#window
     */
    @Nonnull
    public Window getWindow() {
        if (_windowField!= null) {
            return _windowField;
        } else {
            Object __rawValue = super._map.get("window");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("window");
            }
            _windowField = ((__rawValue == null)?null:new Window(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _windowField;
        }
    }

    /**
     * Setter for window
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#window
     */
    public SlidingWindowEmbeddingAggregation setWindow(Window value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setWindow(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field window of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "window", value.data());
                    _windowField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeWindow();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "window", value.data());
                    _windowField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "window", value.data());
                    _windowField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for window
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SlidingWindowEmbeddingAggregation.Fields#window
     */
    public SlidingWindowEmbeddingAggregation setWindow(
        @Nonnull
        Window value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field window of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "window", value.data());
            _windowField = value;
        }
        return this;
    }

    /**
     * Existence checker for filter
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#filter
     */
    public boolean hasFilter() {
        if (_filterField!= null) {
            return true;
        }
        return super._map.containsKey("filter");
    }

    /**
     * Remover for filter
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#filter
     */
    public void removeFilter() {
        super._map.remove("filter");
    }

    /**
     * Getter for filter
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#filter
     */
    public SlidingWindowEmbeddingAggregation.Filter getFilter(GetMode mode) {
        return getFilter();
    }

    /**
     * Getter for filter
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowEmbeddingAggregation.Fields#filter
     */
    @Nullable
    public SlidingWindowEmbeddingAggregation.Filter getFilter() {
        if (_filterField!= null) {
            return _filterField;
        } else {
            Object __rawValue = super._map.get("filter");
            _filterField = ((__rawValue == null)?null:new SlidingWindowEmbeddingAggregation.Filter(__rawValue));
            return _filterField;
        }
    }

    /**
     * Setter for filter
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#filter
     */
    public SlidingWindowEmbeddingAggregation setFilter(SlidingWindowEmbeddingAggregation.Filter value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setFilter(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeFilter();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "filter", value.data());
                    _filterField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "filter", value.data());
                    _filterField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for filter
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SlidingWindowEmbeddingAggregation.Fields#filter
     */
    public SlidingWindowEmbeddingAggregation setFilter(
        @Nonnull
        SlidingWindowEmbeddingAggregation.Filter value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field filter of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "filter", value.data());
            _filterField = value;
        }
        return this;
    }

    /**
     * Existence checker for groupBy
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#groupBy
     */
    public boolean hasGroupBy() {
        if (_groupByField!= null) {
            return true;
        }
        return super._map.containsKey("groupBy");
    }

    /**
     * Remover for groupBy
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#groupBy
     */
    public void removeGroupBy() {
        super._map.remove("groupBy");
    }

    /**
     * Getter for groupBy
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#groupBy
     */
    public SlidingWindowEmbeddingAggregation.GroupBy getGroupBy(GetMode mode) {
        return getGroupBy();
    }

    /**
     * Getter for groupBy
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowEmbeddingAggregation.Fields#groupBy
     */
    @Nullable
    public SlidingWindowEmbeddingAggregation.GroupBy getGroupBy() {
        if (_groupByField!= null) {
            return _groupByField;
        } else {
            Object __rawValue = super._map.get("groupBy");
            _groupByField = ((__rawValue == null)?null:new SlidingWindowEmbeddingAggregation.GroupBy(__rawValue));
            return _groupByField;
        }
    }

    /**
     * Setter for groupBy
     * 
     * @see SlidingWindowEmbeddingAggregation.Fields#groupBy
     */
    public SlidingWindowEmbeddingAggregation setGroupBy(SlidingWindowEmbeddingAggregation.GroupBy value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setGroupBy(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeGroupBy();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "groupBy", value.data());
                    _groupByField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "groupBy", value.data());
                    _groupByField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for groupBy
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SlidingWindowEmbeddingAggregation.Fields#groupBy
     */
    public SlidingWindowEmbeddingAggregation setGroupBy(
        @Nonnull
        SlidingWindowEmbeddingAggregation.GroupBy value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field groupBy of com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "groupBy", value.data());
            _groupByField = value;
        }
        return this;
    }

    @Override
    public SlidingWindowEmbeddingAggregation clone()
        throws CloneNotSupportedException
    {
        SlidingWindowEmbeddingAggregation __clone = ((SlidingWindowEmbeddingAggregation) super.clone());
        __clone.__changeListener = new SlidingWindowEmbeddingAggregation.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public SlidingWindowEmbeddingAggregation copy()
        throws CloneNotSupportedException
    {
        SlidingWindowEmbeddingAggregation __copy = ((SlidingWindowEmbeddingAggregation) super.copy());
        __copy._filterField = null;
        __copy._aggregationTypeField = null;
        __copy._targetColumnField = null;
        __copy._windowField = null;
        __copy._groupByField = null;
        __copy.__changeListener = new SlidingWindowEmbeddingAggregation.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final SlidingWindowEmbeddingAggregation __objectRef;

        private ChangeListener(SlidingWindowEmbeddingAggregation reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "filter":
                    __objectRef._filterField = null;
                    break;
                case "aggregationType":
                    __objectRef._aggregationTypeField = null;
                    break;
                case "targetColumn":
                    __objectRef._targetColumnField = null;
                    break;
                case "window":
                    __objectRef._windowField = null;
                    break;
                case "groupBy":
                    __objectRef._groupByField = null;
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
         * The target column to perform aggregation against.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.TargetColumn.Fields targetColumn() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.TargetColumn.Fields(getPathComponents(), "targetColumn");
        }

        /**
         * Represents supported types for embedding aggregation.
         * 
         */
        public PathSpec aggregationType() {
            return new PathSpec(getPathComponents(), "aggregationType");
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public com.linkedin.feathr.featureDataModel.Window.Fields window() {
            return new com.linkedin.feathr.featureDataModel.Window.Fields(getPathComponents(), "window");
        }

        /**
         * Represents the filter statement before the aggregation.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Filter.Fields filter() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Filter.Fields(getPathComponents(), "filter");
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.GroupBy.Fields groupBy() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.GroupBy.Fields(getPathComponents(), "groupBy");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowEmbeddingAggregation.pdl.")
    public static class Filter
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowEmbeddingAggregation.Filter.ChangeListener __changeListener = new SlidingWindowEmbeddingAggregation.Filter.ChangeListener(this);
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SparkSqlExpression");

        public Filter() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public Filter(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static SlidingWindowEmbeddingAggregation.Filter create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowEmbeddingAggregation.Filter newUnion = new SlidingWindowEmbeddingAggregation.Filter();
            newUnion.setSparkSqlExpression(value);
            return newUnion;
        }

        public boolean isSparkSqlExpression() {
            return memberIs("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        }

        public com.linkedin.feathr.featureDataModel.SparkSqlExpression getSparkSqlExpression() {
            checkNotNull();
            if (_sparkSqlExpressionMember!= null) {
                return _sparkSqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            _sparkSqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SparkSqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sparkSqlExpressionMember;
        }

        public void setSparkSqlExpression(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sparkSqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SparkSqlExpression", value.data());
        }

        public static SlidingWindowEmbeddingAggregation.Filter.ProjectionMask createMask() {
            return new SlidingWindowEmbeddingAggregation.Filter.ProjectionMask();
        }

        @Override
        public SlidingWindowEmbeddingAggregation.Filter clone()
            throws CloneNotSupportedException
        {
            SlidingWindowEmbeddingAggregation.Filter __clone = ((SlidingWindowEmbeddingAggregation.Filter) super.clone());
            __clone.__changeListener = new SlidingWindowEmbeddingAggregation.Filter.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowEmbeddingAggregation.Filter copy()
            throws CloneNotSupportedException
        {
            SlidingWindowEmbeddingAggregation.Filter __copy = ((SlidingWindowEmbeddingAggregation.Filter) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowEmbeddingAggregation.Filter.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowEmbeddingAggregation.Filter __objectRef;

            private ChangeListener(SlidingWindowEmbeddingAggregation.Filter reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.SparkSqlExpression":
                        __objectRef._sparkSqlExpressionMember = null;
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

            public com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;

            ProjectionMask() {
                super(2);
            }

            public SlidingWindowEmbeddingAggregation.Filter.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowEmbeddingAggregation.pdl.")
    public static class GroupBy
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowEmbeddingAggregation.GroupBy.ChangeListener __changeListener = new SlidingWindowEmbeddingAggregation.GroupBy.ChangeListener(this);
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SparkSqlExpression");

        public GroupBy() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public GroupBy(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static SlidingWindowEmbeddingAggregation.GroupBy create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowEmbeddingAggregation.GroupBy newUnion = new SlidingWindowEmbeddingAggregation.GroupBy();
            newUnion.setSparkSqlExpression(value);
            return newUnion;
        }

        public boolean isSparkSqlExpression() {
            return memberIs("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        }

        public com.linkedin.feathr.featureDataModel.SparkSqlExpression getSparkSqlExpression() {
            checkNotNull();
            if (_sparkSqlExpressionMember!= null) {
                return _sparkSqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            _sparkSqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SparkSqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sparkSqlExpressionMember;
        }

        public void setSparkSqlExpression(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sparkSqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SparkSqlExpression", value.data());
        }

        public static SlidingWindowEmbeddingAggregation.GroupBy.ProjectionMask createMask() {
            return new SlidingWindowEmbeddingAggregation.GroupBy.ProjectionMask();
        }

        @Override
        public SlidingWindowEmbeddingAggregation.GroupBy clone()
            throws CloneNotSupportedException
        {
            SlidingWindowEmbeddingAggregation.GroupBy __clone = ((SlidingWindowEmbeddingAggregation.GroupBy) super.clone());
            __clone.__changeListener = new SlidingWindowEmbeddingAggregation.GroupBy.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowEmbeddingAggregation.GroupBy copy()
            throws CloneNotSupportedException
        {
            SlidingWindowEmbeddingAggregation.GroupBy __copy = ((SlidingWindowEmbeddingAggregation.GroupBy) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowEmbeddingAggregation.GroupBy.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowEmbeddingAggregation.GroupBy __objectRef;

            private ChangeListener(SlidingWindowEmbeddingAggregation.GroupBy reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.SparkSqlExpression":
                        __objectRef._sparkSqlExpressionMember = null;
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

            public com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;

            ProjectionMask() {
                super(2);
            }

            public SlidingWindowEmbeddingAggregation.GroupBy.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.TargetColumn.ProjectionMask _targetColumnMask;
        private com.linkedin.feathr.featureDataModel.Window.ProjectionMask _windowMask;
        private com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Filter.ProjectionMask _filterMask;
        private com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.GroupBy.ProjectionMask _groupByMask;

        ProjectionMask() {
            super(7);
        }

        /**
         * The target column to perform aggregation against.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withTargetColumn(Function<com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.TargetColumn.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.TargetColumn.ProjectionMask> nestedMask) {
            _targetColumnMask = nestedMask.apply(((_targetColumnMask == null)?SlidingWindowEmbeddingAggregation.TargetColumn.createMask():_targetColumnMask));
            getDataMap().put("targetColumn", _targetColumnMask.getDataMap());
            return this;
        }

        /**
         * The target column to perform aggregation against.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withTargetColumn() {
            _targetColumnMask = null;
            getDataMap().put("targetColumn", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents supported types for embedding aggregation.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withAggregationType() {
            getDataMap().put("aggregationType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withWindow(Function<com.linkedin.feathr.featureDataModel.Window.ProjectionMask, com.linkedin.feathr.featureDataModel.Window.ProjectionMask> nestedMask) {
            _windowMask = nestedMask.apply(((_windowMask == null)?Window.createMask():_windowMask));
            getDataMap().put("window", _windowMask.getDataMap());
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withWindow() {
            _windowMask = null;
            getDataMap().put("window", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the filter statement before the aggregation.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withFilter(Function<com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Filter.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.Filter.ProjectionMask> nestedMask) {
            _filterMask = nestedMask.apply(((_filterMask == null)?SlidingWindowEmbeddingAggregation.Filter.createMask():_filterMask));
            getDataMap().put("filter", _filterMask.getDataMap());
            return this;
        }

        /**
         * Represents the filter statement before the aggregation.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withFilter() {
            _filterMask = null;
            getDataMap().put("filter", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withGroupBy(Function<com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.GroupBy.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowEmbeddingAggregation.GroupBy.ProjectionMask> nestedMask) {
            _groupByMask = nestedMask.apply(((_groupByMask == null)?SlidingWindowEmbeddingAggregation.GroupBy.createMask():_groupByMask));
            getDataMap().put("groupBy", _groupByMask.getDataMap());
            return this;
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowEmbeddingAggregation.ProjectionMask withGroupBy() {
            _groupByMask = null;
            getDataMap().put("groupBy", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowEmbeddingAggregation.pdl.")
    public static class TargetColumn
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowEmbeddingAggregation.TargetColumn.ChangeListener __changeListener = new SlidingWindowEmbeddingAggregation.TargetColumn.ChangeListener(this);
        private final static DataSchema MEMBER_SparkSqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.featureDataModel.SparkSqlExpression");

        public TargetColumn() {
            super(new DataMap(2, 0.75F), SCHEMA);
            addChangeListener(__changeListener);
        }

        public TargetColumn(Object data) {
            super(data, SCHEMA);
            addChangeListener(__changeListener);
        }

        public static UnionDataSchema dataSchema() {
            return SCHEMA;
        }

        public static SlidingWindowEmbeddingAggregation.TargetColumn create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowEmbeddingAggregation.TargetColumn newUnion = new SlidingWindowEmbeddingAggregation.TargetColumn();
            newUnion.setSparkSqlExpression(value);
            return newUnion;
        }

        public boolean isSparkSqlExpression() {
            return memberIs("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
        }

        public com.linkedin.feathr.featureDataModel.SparkSqlExpression getSparkSqlExpression() {
            checkNotNull();
            if (_sparkSqlExpressionMember!= null) {
                return _sparkSqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            _sparkSqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.featureDataModel.SparkSqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sparkSqlExpressionMember;
        }

        public void setSparkSqlExpression(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sparkSqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.featureDataModel.SparkSqlExpression", value.data());
        }

        public static SlidingWindowEmbeddingAggregation.TargetColumn.ProjectionMask createMask() {
            return new SlidingWindowEmbeddingAggregation.TargetColumn.ProjectionMask();
        }

        @Override
        public SlidingWindowEmbeddingAggregation.TargetColumn clone()
            throws CloneNotSupportedException
        {
            SlidingWindowEmbeddingAggregation.TargetColumn __clone = ((SlidingWindowEmbeddingAggregation.TargetColumn) super.clone());
            __clone.__changeListener = new SlidingWindowEmbeddingAggregation.TargetColumn.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowEmbeddingAggregation.TargetColumn copy()
            throws CloneNotSupportedException
        {
            SlidingWindowEmbeddingAggregation.TargetColumn __copy = ((SlidingWindowEmbeddingAggregation.TargetColumn) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowEmbeddingAggregation.TargetColumn.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowEmbeddingAggregation.TargetColumn __objectRef;

            private ChangeListener(SlidingWindowEmbeddingAggregation.TargetColumn reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.featureDataModel.SparkSqlExpression":
                        __objectRef._sparkSqlExpressionMember = null;
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

            public com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields SparkSqlExpression() {
                return new com.linkedin.feathr.featureDataModel.SparkSqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.featureDataModel.SparkSqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask _SparkSqlExpressionMask;

            ProjectionMask() {
                super(2);
            }

            public SlidingWindowEmbeddingAggregation.TargetColumn.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

}
