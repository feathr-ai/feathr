
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import java.util.function.Function;
import javax.annotation.Generated;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import com.linkedin.data.DataList;
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
 * Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and compute the aggregagate value.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowAggregation.pdl.")
public class SlidingWindowAggregation
    extends RecordTemplate
{

    private final static SlidingWindowAggregation.Fields _fields = new SlidingWindowAggregation.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, Frame looks back the configurable time window from each entry's timestamp and compute the aggregagate value.*/record SlidingWindowAggregation{/**The target column to perform aggregation against.*/targetColumn:union[/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}]/**Represents supported types of aggregation.*/aggregationType:enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG}/**Represents the time window to look back from label data's timestamp.*/window:/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.*/lateralViews:array[/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[SparkSqlExpression]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}]=[]/**Represents the filter statement before the aggregation.*/filter:optional union[SparkSqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the max number of groups (with aggregation results) to return.*/limit:optional int}", SchemaFormatType.PDL));
    private SlidingWindowAggregation.TargetColumn _targetColumnField = null;
    private AggregationType _aggregationTypeField = null;
    private Window _windowField = null;
    private LateralViewArray _lateralViewsField = null;
    private SlidingWindowAggregation.Filter _filterField = null;
    private SlidingWindowAggregation.GroupBy _groupByField = null;
    private Integer _limitField = null;
    private SlidingWindowAggregation.ChangeListener __changeListener = new SlidingWindowAggregation.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TargetColumn = SCHEMA.getField("targetColumn");
    private final static RecordDataSchema.Field FIELD_AggregationType = SCHEMA.getField("aggregationType");
    private final static RecordDataSchema.Field FIELD_Window = SCHEMA.getField("window");
    private final static RecordDataSchema.Field FIELD_LateralViews = SCHEMA.getField("lateralViews");
    private final static LateralViewArray DEFAULT_LateralViews;
    private final static RecordDataSchema.Field FIELD_Filter = SCHEMA.getField("filter");
    private final static RecordDataSchema.Field FIELD_GroupBy = SCHEMA.getField("groupBy");
    private final static RecordDataSchema.Field FIELD_Limit = SCHEMA.getField("limit");

    static {
        DEFAULT_LateralViews = ((FIELD_LateralViews.getDefault() == null)?null:new LateralViewArray(DataTemplateUtil.castOrThrow(FIELD_LateralViews.getDefault(), DataList.class)));
    }

    public SlidingWindowAggregation() {
        super(new DataMap(10, 0.75F), SCHEMA, 7);
        addChangeListener(__changeListener);
    }

    public SlidingWindowAggregation(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static SlidingWindowAggregation.Fields fields() {
        return _fields;
    }

    public static SlidingWindowAggregation.ProjectionMask createMask() {
        return new SlidingWindowAggregation.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for targetColumn
     * 
     * @see SlidingWindowAggregation.Fields#targetColumn
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
     * @see SlidingWindowAggregation.Fields#targetColumn
     */
    public void removeTargetColumn() {
        super._map.remove("targetColumn");
    }

    /**
     * Getter for targetColumn
     * 
     * @see SlidingWindowAggregation.Fields#targetColumn
     */
    public SlidingWindowAggregation.TargetColumn getTargetColumn(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTargetColumn();
            case DEFAULT:
            case NULL:
                if (_targetColumnField!= null) {
                    return _targetColumnField;
                } else {
                    Object __rawValue = super._map.get("targetColumn");
                    _targetColumnField = ((__rawValue == null)?null:new SlidingWindowAggregation.TargetColumn(__rawValue));
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
     * @see SlidingWindowAggregation.Fields#targetColumn
     */
    @Nonnull
    public SlidingWindowAggregation.TargetColumn getTargetColumn() {
        if (_targetColumnField!= null) {
            return _targetColumnField;
        } else {
            Object __rawValue = super._map.get("targetColumn");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("targetColumn");
            }
            _targetColumnField = ((__rawValue == null)?null:new SlidingWindowAggregation.TargetColumn(__rawValue));
            return _targetColumnField;
        }
    }

    /**
     * Setter for targetColumn
     * 
     * @see SlidingWindowAggregation.Fields#targetColumn
     */
    public SlidingWindowAggregation setTargetColumn(SlidingWindowAggregation.TargetColumn value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTargetColumn(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field targetColumn of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
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
     * @see SlidingWindowAggregation.Fields#targetColumn
     */
    public SlidingWindowAggregation setTargetColumn(
        @Nonnull
        SlidingWindowAggregation.TargetColumn value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field targetColumn of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "targetColumn", value.data());
            _targetColumnField = value;
        }
        return this;
    }

    /**
     * Existence checker for aggregationType
     * 
     * @see SlidingWindowAggregation.Fields#aggregationType
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
     * @see SlidingWindowAggregation.Fields#aggregationType
     */
    public void removeAggregationType() {
        super._map.remove("aggregationType");
    }

    /**
     * Getter for aggregationType
     * 
     * @see SlidingWindowAggregation.Fields#aggregationType
     */
    public AggregationType getAggregationType(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getAggregationType();
            case DEFAULT:
            case NULL:
                if (_aggregationTypeField!= null) {
                    return _aggregationTypeField;
                } else {
                    Object __rawValue = super._map.get("aggregationType");
                    _aggregationTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, AggregationType.class, AggregationType.$UNKNOWN);
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
     * @see SlidingWindowAggregation.Fields#aggregationType
     */
    @Nonnull
    public AggregationType getAggregationType() {
        if (_aggregationTypeField!= null) {
            return _aggregationTypeField;
        } else {
            Object __rawValue = super._map.get("aggregationType");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("aggregationType");
            }
            _aggregationTypeField = DataTemplateUtil.coerceEnumOutput(__rawValue, AggregationType.class, AggregationType.$UNKNOWN);
            return _aggregationTypeField;
        }
    }

    /**
     * Setter for aggregationType
     * 
     * @see SlidingWindowAggregation.Fields#aggregationType
     */
    public SlidingWindowAggregation setAggregationType(AggregationType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setAggregationType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field aggregationType of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
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
     * @see SlidingWindowAggregation.Fields#aggregationType
     */
    public SlidingWindowAggregation setAggregationType(
        @Nonnull
        AggregationType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field aggregationType of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "aggregationType", value.name());
            _aggregationTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for window
     * 
     * @see SlidingWindowAggregation.Fields#window
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
     * @see SlidingWindowAggregation.Fields#window
     */
    public void removeWindow() {
        super._map.remove("window");
    }

    /**
     * Getter for window
     * 
     * @see SlidingWindowAggregation.Fields#window
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
     * @see SlidingWindowAggregation.Fields#window
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
     * @see SlidingWindowAggregation.Fields#window
     */
    public SlidingWindowAggregation setWindow(Window value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setWindow(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field window of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
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
     * @see SlidingWindowAggregation.Fields#window
     */
    public SlidingWindowAggregation setWindow(
        @Nonnull
        Window value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field window of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "window", value.data());
            _windowField = value;
        }
        return this;
    }

    /**
     * Existence checker for lateralViews
     * 
     * @see SlidingWindowAggregation.Fields#lateralViews
     */
    public boolean hasLateralViews() {
        if (_lateralViewsField!= null) {
            return true;
        }
        return super._map.containsKey("lateralViews");
    }

    /**
     * Remover for lateralViews
     * 
     * @see SlidingWindowAggregation.Fields#lateralViews
     */
    public void removeLateralViews() {
        super._map.remove("lateralViews");
    }

    /**
     * Getter for lateralViews
     * 
     * @see SlidingWindowAggregation.Fields#lateralViews
     */
    public LateralViewArray getLateralViews(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getLateralViews();
            case NULL:
                if (_lateralViewsField!= null) {
                    return _lateralViewsField;
                } else {
                    Object __rawValue = super._map.get("lateralViews");
                    _lateralViewsField = ((__rawValue == null)?null:new LateralViewArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
                    return _lateralViewsField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for lateralViews
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see SlidingWindowAggregation.Fields#lateralViews
     */
    @Nonnull
    public LateralViewArray getLateralViews() {
        if (_lateralViewsField!= null) {
            return _lateralViewsField;
        } else {
            Object __rawValue = super._map.get("lateralViews");
            if (__rawValue == null) {
                return DEFAULT_LateralViews;
            }
            _lateralViewsField = ((__rawValue == null)?null:new LateralViewArray(DataTemplateUtil.castOrThrow(__rawValue, DataList.class)));
            return _lateralViewsField;
        }
    }

    /**
     * Setter for lateralViews
     * 
     * @see SlidingWindowAggregation.Fields#lateralViews
     */
    public SlidingWindowAggregation setLateralViews(LateralViewArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLateralViews(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field lateralViews of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lateralViews", value.data());
                    _lateralViewsField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLateralViews();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "lateralViews", value.data());
                    _lateralViewsField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "lateralViews", value.data());
                    _lateralViewsField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for lateralViews
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SlidingWindowAggregation.Fields#lateralViews
     */
    public SlidingWindowAggregation setLateralViews(
        @Nonnull
        LateralViewArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field lateralViews of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "lateralViews", value.data());
            _lateralViewsField = value;
        }
        return this;
    }

    /**
     * Existence checker for filter
     * 
     * @see SlidingWindowAggregation.Fields#filter
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
     * @see SlidingWindowAggregation.Fields#filter
     */
    public void removeFilter() {
        super._map.remove("filter");
    }

    /**
     * Getter for filter
     * 
     * @see SlidingWindowAggregation.Fields#filter
     */
    public SlidingWindowAggregation.Filter getFilter(GetMode mode) {
        return getFilter();
    }

    /**
     * Getter for filter
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowAggregation.Fields#filter
     */
    @Nullable
    public SlidingWindowAggregation.Filter getFilter() {
        if (_filterField!= null) {
            return _filterField;
        } else {
            Object __rawValue = super._map.get("filter");
            _filterField = ((__rawValue == null)?null:new SlidingWindowAggregation.Filter(__rawValue));
            return _filterField;
        }
    }

    /**
     * Setter for filter
     * 
     * @see SlidingWindowAggregation.Fields#filter
     */
    public SlidingWindowAggregation setFilter(SlidingWindowAggregation.Filter value, SetMode mode) {
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
     * @see SlidingWindowAggregation.Fields#filter
     */
    public SlidingWindowAggregation setFilter(
        @Nonnull
        SlidingWindowAggregation.Filter value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field filter of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "filter", value.data());
            _filterField = value;
        }
        return this;
    }

    /**
     * Existence checker for groupBy
     * 
     * @see SlidingWindowAggregation.Fields#groupBy
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
     * @see SlidingWindowAggregation.Fields#groupBy
     */
    public void removeGroupBy() {
        super._map.remove("groupBy");
    }

    /**
     * Getter for groupBy
     * 
     * @see SlidingWindowAggregation.Fields#groupBy
     */
    public SlidingWindowAggregation.GroupBy getGroupBy(GetMode mode) {
        return getGroupBy();
    }

    /**
     * Getter for groupBy
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowAggregation.Fields#groupBy
     */
    @Nullable
    public SlidingWindowAggregation.GroupBy getGroupBy() {
        if (_groupByField!= null) {
            return _groupByField;
        } else {
            Object __rawValue = super._map.get("groupBy");
            _groupByField = ((__rawValue == null)?null:new SlidingWindowAggregation.GroupBy(__rawValue));
            return _groupByField;
        }
    }

    /**
     * Setter for groupBy
     * 
     * @see SlidingWindowAggregation.Fields#groupBy
     */
    public SlidingWindowAggregation setGroupBy(SlidingWindowAggregation.GroupBy value, SetMode mode) {
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
     * @see SlidingWindowAggregation.Fields#groupBy
     */
    public SlidingWindowAggregation setGroupBy(
        @Nonnull
        SlidingWindowAggregation.GroupBy value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field groupBy of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "groupBy", value.data());
            _groupByField = value;
        }
        return this;
    }

    /**
     * Existence checker for limit
     * 
     * @see SlidingWindowAggregation.Fields#limit
     */
    public boolean hasLimit() {
        if (_limitField!= null) {
            return true;
        }
        return super._map.containsKey("limit");
    }

    /**
     * Remover for limit
     * 
     * @see SlidingWindowAggregation.Fields#limit
     */
    public void removeLimit() {
        super._map.remove("limit");
    }

    /**
     * Getter for limit
     * 
     * @see SlidingWindowAggregation.Fields#limit
     */
    public Integer getLimit(GetMode mode) {
        return getLimit();
    }

    /**
     * Getter for limit
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowAggregation.Fields#limit
     */
    @Nullable
    public Integer getLimit() {
        if (_limitField!= null) {
            return _limitField;
        } else {
            Object __rawValue = super._map.get("limit");
            _limitField = DataTemplateUtil.coerceIntOutput(__rawValue);
            return _limitField;
        }
    }

    /**
     * Setter for limit
     * 
     * @see SlidingWindowAggregation.Fields#limit
     */
    public SlidingWindowAggregation setLimit(Integer value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLimit(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeLimit();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
                    _limitField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
                    _limitField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for limit
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see SlidingWindowAggregation.Fields#limit
     */
    public SlidingWindowAggregation setLimit(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field limit of com.linkedin.feathr.featureDataModel.SlidingWindowAggregation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
            _limitField = value;
        }
        return this;
    }

    /**
     * Setter for limit
     * 
     * @see SlidingWindowAggregation.Fields#limit
     */
    public SlidingWindowAggregation setLimit(int value) {
        CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
        _limitField = value;
        return this;
    }

    @Override
    public SlidingWindowAggregation clone()
        throws CloneNotSupportedException
    {
        SlidingWindowAggregation __clone = ((SlidingWindowAggregation) super.clone());
        __clone.__changeListener = new SlidingWindowAggregation.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public SlidingWindowAggregation copy()
        throws CloneNotSupportedException
    {
        SlidingWindowAggregation __copy = ((SlidingWindowAggregation) super.copy());
        __copy._filterField = null;
        __copy._aggregationTypeField = null;
        __copy._targetColumnField = null;
        __copy._limitField = null;
        __copy._windowField = null;
        __copy._groupByField = null;
        __copy._lateralViewsField = null;
        __copy.__changeListener = new SlidingWindowAggregation.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final SlidingWindowAggregation __objectRef;

        private ChangeListener(SlidingWindowAggregation reference) {
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
                case "limit":
                    __objectRef._limitField = null;
                    break;
                case "window":
                    __objectRef._windowField = null;
                    break;
                case "groupBy":
                    __objectRef._groupByField = null;
                    break;
                case "lateralViews":
                    __objectRef._lateralViewsField = null;
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
        public com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.TargetColumn.Fields targetColumn() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.TargetColumn.Fields(getPathComponents(), "targetColumn");
        }

        /**
         * Represents supported types of aggregation.
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
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.LateralViewArray.Fields lateralViews() {
            return new com.linkedin.feathr.featureDataModel.LateralViewArray.Fields(getPathComponents(), "lateralViews");
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public PathSpec lateralViews(Integer start, Integer count) {
            PathSpec arrayPathSpec = new PathSpec(getPathComponents(), "lateralViews");
            if (start!= null) {
                arrayPathSpec.setAttribute("start", start);
            }
            if (count!= null) {
                arrayPathSpec.setAttribute("count", count);
            }
            return arrayPathSpec;
        }

        /**
         * Represents the filter statement before the aggregation.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Filter.Fields filter() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Filter.Fields(getPathComponents(), "filter");
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.GroupBy.Fields groupBy() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.GroupBy.Fields(getPathComponents(), "groupBy");
        }

        /**
         * Represents the max number of groups (with aggregation results) to return.
         * 
         */
        public PathSpec limit() {
            return new PathSpec(getPathComponents(), "limit");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowAggregation.pdl.")
    public static class Filter
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowAggregation.Filter.ChangeListener __changeListener = new SlidingWindowAggregation.Filter.ChangeListener(this);
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

        public static SlidingWindowAggregation.Filter create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowAggregation.Filter newUnion = new SlidingWindowAggregation.Filter();
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

        public static SlidingWindowAggregation.Filter.ProjectionMask createMask() {
            return new SlidingWindowAggregation.Filter.ProjectionMask();
        }

        @Override
        public SlidingWindowAggregation.Filter clone()
            throws CloneNotSupportedException
        {
            SlidingWindowAggregation.Filter __clone = ((SlidingWindowAggregation.Filter) super.clone());
            __clone.__changeListener = new SlidingWindowAggregation.Filter.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowAggregation.Filter copy()
            throws CloneNotSupportedException
        {
            SlidingWindowAggregation.Filter __copy = ((SlidingWindowAggregation.Filter) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowAggregation.Filter.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowAggregation.Filter __objectRef;

            private ChangeListener(SlidingWindowAggregation.Filter reference) {
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

            public SlidingWindowAggregation.Filter.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowAggregation.pdl.")
    public static class GroupBy
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowAggregation.GroupBy.ChangeListener __changeListener = new SlidingWindowAggregation.GroupBy.ChangeListener(this);
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

        public static SlidingWindowAggregation.GroupBy create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowAggregation.GroupBy newUnion = new SlidingWindowAggregation.GroupBy();
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

        public static SlidingWindowAggregation.GroupBy.ProjectionMask createMask() {
            return new SlidingWindowAggregation.GroupBy.ProjectionMask();
        }

        @Override
        public SlidingWindowAggregation.GroupBy clone()
            throws CloneNotSupportedException
        {
            SlidingWindowAggregation.GroupBy __clone = ((SlidingWindowAggregation.GroupBy) super.clone());
            __clone.__changeListener = new SlidingWindowAggregation.GroupBy.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowAggregation.GroupBy copy()
            throws CloneNotSupportedException
        {
            SlidingWindowAggregation.GroupBy __copy = ((SlidingWindowAggregation.GroupBy) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowAggregation.GroupBy.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowAggregation.GroupBy __objectRef;

            private ChangeListener(SlidingWindowAggregation.GroupBy reference) {
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

            public SlidingWindowAggregation.GroupBy.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.TargetColumn.ProjectionMask _targetColumnMask;
        private com.linkedin.feathr.featureDataModel.Window.ProjectionMask _windowMask;
        private com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask _lateralViewsMask;
        private com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Filter.ProjectionMask _filterMask;
        private com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.GroupBy.ProjectionMask _groupByMask;

        ProjectionMask() {
            super(10);
        }

        /**
         * The target column to perform aggregation against.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withTargetColumn(Function<com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.TargetColumn.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.TargetColumn.ProjectionMask> nestedMask) {
            _targetColumnMask = nestedMask.apply(((_targetColumnMask == null)?SlidingWindowAggregation.TargetColumn.createMask():_targetColumnMask));
            getDataMap().put("targetColumn", _targetColumnMask.getDataMap());
            return this;
        }

        /**
         * The target column to perform aggregation against.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withTargetColumn() {
            _targetColumnMask = null;
            getDataMap().put("targetColumn", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents supported types of aggregation.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withAggregationType() {
            getDataMap().put("aggregationType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withWindow(Function<com.linkedin.feathr.featureDataModel.Window.ProjectionMask, com.linkedin.feathr.featureDataModel.Window.ProjectionMask> nestedMask) {
            _windowMask = nestedMask.apply(((_windowMask == null)?Window.createMask():_windowMask));
            getDataMap().put("window", _windowMask.getDataMap());
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withWindow() {
            _windowMask = null;
            getDataMap().put("window", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withLateralViews(Function<com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask, com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask> nestedMask) {
            _lateralViewsMask = nestedMask.apply(((_lateralViewsMask == null)?LateralViewArray.createMask():_lateralViewsMask));
            getDataMap().put("lateralViews", _lateralViewsMask.getDataMap());
            return this;
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withLateralViews() {
            _lateralViewsMask = null;
            getDataMap().put("lateralViews", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withLateralViews(Function<com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask, com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask> nestedMask, Integer start, Integer count) {
            _lateralViewsMask = nestedMask.apply(((_lateralViewsMask == null)?LateralViewArray.createMask():_lateralViewsMask));
            getDataMap().put("lateralViews", _lateralViewsMask.getDataMap());
            if (start!= null) {
                getDataMap().getDataMap("lateralViews").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("lateralViews").put("$count", count);
            }
            return this;
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withLateralViews(Integer start, Integer count) {
            _lateralViewsMask = null;
            getDataMap().put("lateralViews", new DataMap(3));
            if (start!= null) {
                getDataMap().getDataMap("lateralViews").put("$start", start);
            }
            if (count!= null) {
                getDataMap().getDataMap("lateralViews").put("$count", count);
            }
            return this;
        }

        /**
         * Represents the filter statement before the aggregation.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withFilter(Function<com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Filter.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.Filter.ProjectionMask> nestedMask) {
            _filterMask = nestedMask.apply(((_filterMask == null)?SlidingWindowAggregation.Filter.createMask():_filterMask));
            getDataMap().put("filter", _filterMask.getDataMap());
            return this;
        }

        /**
         * Represents the filter statement before the aggregation.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withFilter() {
            _filterMask = null;
            getDataMap().put("filter", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withGroupBy(Function<com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.GroupBy.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowAggregation.GroupBy.ProjectionMask> nestedMask) {
            _groupByMask = nestedMask.apply(((_groupByMask == null)?SlidingWindowAggregation.GroupBy.createMask():_groupByMask));
            getDataMap().put("groupBy", _groupByMask.getDataMap());
            return this;
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withGroupBy() {
            _groupByMask = null;
            getDataMap().put("groupBy", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the max number of groups (with aggregation results) to return.
         * 
         */
        public SlidingWindowAggregation.ProjectionMask withLimit() {
            getDataMap().put("limit", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowAggregation.pdl.")
    public static class TargetColumn
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowAggregation.TargetColumn.ChangeListener __changeListener = new SlidingWindowAggregation.TargetColumn.ChangeListener(this);
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

        public static SlidingWindowAggregation.TargetColumn create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowAggregation.TargetColumn newUnion = new SlidingWindowAggregation.TargetColumn();
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

        public static SlidingWindowAggregation.TargetColumn.ProjectionMask createMask() {
            return new SlidingWindowAggregation.TargetColumn.ProjectionMask();
        }

        @Override
        public SlidingWindowAggregation.TargetColumn clone()
            throws CloneNotSupportedException
        {
            SlidingWindowAggregation.TargetColumn __clone = ((SlidingWindowAggregation.TargetColumn) super.clone());
            __clone.__changeListener = new SlidingWindowAggregation.TargetColumn.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowAggregation.TargetColumn copy()
            throws CloneNotSupportedException
        {
            SlidingWindowAggregation.TargetColumn __copy = ((SlidingWindowAggregation.TargetColumn) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowAggregation.TargetColumn.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowAggregation.TargetColumn __objectRef;

            private ChangeListener(SlidingWindowAggregation.TargetColumn reference) {
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

            public SlidingWindowAggregation.TargetColumn.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

}
