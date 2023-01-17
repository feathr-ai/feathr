
package com.linkedin.feathr.compute;

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
 * Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, feathr looks back the configurable time window from each entry's timestamp and compute the aggregagate value.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\SlidingWindowFeature.pdl.")
public class SlidingWindowFeature
    extends RecordTemplate
{

    private final static SlidingWindowFeature.Fields _fields = new SlidingWindowFeature.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Sliding window aggregation produces feature data by aggregating a collection of data within a given time interval into an aggregate value. It ensures point-in-time correctness, when joining with label data, feathr looks back the configurable time window from each entry's timestamp and compute the aggregagate value.*/record SlidingWindowFeature{/**The target column to perform aggregation against.*/targetColumn:union[/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}]/**Represents supported types of aggregation.*/aggregationType:enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. */MAX_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. */MIN_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. */AVG_POOLING/** Latest */LATEST}/**Represents the time window to look back from label data's timestamp.*/window:/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.*/lateralViews:array[/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[SqlExpression]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}]=[]/**Represents the filter statement before the aggregation.*/filter:optional union[SqlExpression]/**Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SqlExpression]/**Represents the max number of groups (with aggregation results) to return.*/limit:optional int}", SchemaFormatType.PDL));
    private SlidingWindowFeature.TargetColumn _targetColumnField = null;
    private AggregationType _aggregationTypeField = null;
    private Window _windowField = null;
    private LateralViewArray _lateralViewsField = null;
    private SlidingWindowFeature.Filter _filterField = null;
    private SlidingWindowFeature.GroupBy _groupByField = null;
    private Integer _limitField = null;
    private SlidingWindowFeature.ChangeListener __changeListener = new SlidingWindowFeature.ChangeListener(this);
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

    public SlidingWindowFeature() {
        super(new DataMap(10, 0.75F), SCHEMA, 7);
        addChangeListener(__changeListener);
    }

    public SlidingWindowFeature(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static SlidingWindowFeature.Fields fields() {
        return _fields;
    }

    public static SlidingWindowFeature.ProjectionMask createMask() {
        return new SlidingWindowFeature.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for targetColumn
     * 
     * @see SlidingWindowFeature.Fields#targetColumn
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
     * @see SlidingWindowFeature.Fields#targetColumn
     */
    public void removeTargetColumn() {
        super._map.remove("targetColumn");
    }

    /**
     * Getter for targetColumn
     * 
     * @see SlidingWindowFeature.Fields#targetColumn
     */
    public SlidingWindowFeature.TargetColumn getTargetColumn(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTargetColumn();
            case DEFAULT:
            case NULL:
                if (_targetColumnField!= null) {
                    return _targetColumnField;
                } else {
                    Object __rawValue = super._map.get("targetColumn");
                    _targetColumnField = ((__rawValue == null)?null:new SlidingWindowFeature.TargetColumn(__rawValue));
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
     * @see SlidingWindowFeature.Fields#targetColumn
     */
    @Nonnull
    public SlidingWindowFeature.TargetColumn getTargetColumn() {
        if (_targetColumnField!= null) {
            return _targetColumnField;
        } else {
            Object __rawValue = super._map.get("targetColumn");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("targetColumn");
            }
            _targetColumnField = ((__rawValue == null)?null:new SlidingWindowFeature.TargetColumn(__rawValue));
            return _targetColumnField;
        }
    }

    /**
     * Setter for targetColumn
     * 
     * @see SlidingWindowFeature.Fields#targetColumn
     */
    public SlidingWindowFeature setTargetColumn(SlidingWindowFeature.TargetColumn value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTargetColumn(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field targetColumn of com.linkedin.feathr.compute.SlidingWindowFeature");
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
     * @see SlidingWindowFeature.Fields#targetColumn
     */
    public SlidingWindowFeature setTargetColumn(
        @Nonnull
        SlidingWindowFeature.TargetColumn value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field targetColumn of com.linkedin.feathr.compute.SlidingWindowFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "targetColumn", value.data());
            _targetColumnField = value;
        }
        return this;
    }

    /**
     * Existence checker for aggregationType
     * 
     * @see SlidingWindowFeature.Fields#aggregationType
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
     * @see SlidingWindowFeature.Fields#aggregationType
     */
    public void removeAggregationType() {
        super._map.remove("aggregationType");
    }

    /**
     * Getter for aggregationType
     * 
     * @see SlidingWindowFeature.Fields#aggregationType
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
     * @see SlidingWindowFeature.Fields#aggregationType
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
     * @see SlidingWindowFeature.Fields#aggregationType
     */
    public SlidingWindowFeature setAggregationType(AggregationType value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setAggregationType(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field aggregationType of com.linkedin.feathr.compute.SlidingWindowFeature");
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
     * @see SlidingWindowFeature.Fields#aggregationType
     */
    public SlidingWindowFeature setAggregationType(
        @Nonnull
        AggregationType value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field aggregationType of com.linkedin.feathr.compute.SlidingWindowFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "aggregationType", value.name());
            _aggregationTypeField = value;
        }
        return this;
    }

    /**
     * Existence checker for window
     * 
     * @see SlidingWindowFeature.Fields#window
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
     * @see SlidingWindowFeature.Fields#window
     */
    public void removeWindow() {
        super._map.remove("window");
    }

    /**
     * Getter for window
     * 
     * @see SlidingWindowFeature.Fields#window
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
     * @see SlidingWindowFeature.Fields#window
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
     * @see SlidingWindowFeature.Fields#window
     */
    public SlidingWindowFeature setWindow(Window value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setWindow(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field window of com.linkedin.feathr.compute.SlidingWindowFeature");
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
     * @see SlidingWindowFeature.Fields#window
     */
    public SlidingWindowFeature setWindow(
        @Nonnull
        Window value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field window of com.linkedin.feathr.compute.SlidingWindowFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "window", value.data());
            _windowField = value;
        }
        return this;
    }

    /**
     * Existence checker for lateralViews
     * 
     * @see SlidingWindowFeature.Fields#lateralViews
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
     * @see SlidingWindowFeature.Fields#lateralViews
     */
    public void removeLateralViews() {
        super._map.remove("lateralViews");
    }

    /**
     * Getter for lateralViews
     * 
     * @see SlidingWindowFeature.Fields#lateralViews
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
     * @see SlidingWindowFeature.Fields#lateralViews
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
     * @see SlidingWindowFeature.Fields#lateralViews
     */
    public SlidingWindowFeature setLateralViews(LateralViewArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLateralViews(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field lateralViews of com.linkedin.feathr.compute.SlidingWindowFeature");
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
     * @see SlidingWindowFeature.Fields#lateralViews
     */
    public SlidingWindowFeature setLateralViews(
        @Nonnull
        LateralViewArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field lateralViews of com.linkedin.feathr.compute.SlidingWindowFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "lateralViews", value.data());
            _lateralViewsField = value;
        }
        return this;
    }

    /**
     * Existence checker for filter
     * 
     * @see SlidingWindowFeature.Fields#filter
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
     * @see SlidingWindowFeature.Fields#filter
     */
    public void removeFilter() {
        super._map.remove("filter");
    }

    /**
     * Getter for filter
     * 
     * @see SlidingWindowFeature.Fields#filter
     */
    public SlidingWindowFeature.Filter getFilter(GetMode mode) {
        return getFilter();
    }

    /**
     * Getter for filter
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowFeature.Fields#filter
     */
    @Nullable
    public SlidingWindowFeature.Filter getFilter() {
        if (_filterField!= null) {
            return _filterField;
        } else {
            Object __rawValue = super._map.get("filter");
            _filterField = ((__rawValue == null)?null:new SlidingWindowFeature.Filter(__rawValue));
            return _filterField;
        }
    }

    /**
     * Setter for filter
     * 
     * @see SlidingWindowFeature.Fields#filter
     */
    public SlidingWindowFeature setFilter(SlidingWindowFeature.Filter value, SetMode mode) {
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
     * @see SlidingWindowFeature.Fields#filter
     */
    public SlidingWindowFeature setFilter(
        @Nonnull
        SlidingWindowFeature.Filter value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field filter of com.linkedin.feathr.compute.SlidingWindowFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "filter", value.data());
            _filterField = value;
        }
        return this;
    }

    /**
     * Existence checker for groupBy
     * 
     * @see SlidingWindowFeature.Fields#groupBy
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
     * @see SlidingWindowFeature.Fields#groupBy
     */
    public void removeGroupBy() {
        super._map.remove("groupBy");
    }

    /**
     * Getter for groupBy
     * 
     * @see SlidingWindowFeature.Fields#groupBy
     */
    public SlidingWindowFeature.GroupBy getGroupBy(GetMode mode) {
        return getGroupBy();
    }

    /**
     * Getter for groupBy
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowFeature.Fields#groupBy
     */
    @Nullable
    public SlidingWindowFeature.GroupBy getGroupBy() {
        if (_groupByField!= null) {
            return _groupByField;
        } else {
            Object __rawValue = super._map.get("groupBy");
            _groupByField = ((__rawValue == null)?null:new SlidingWindowFeature.GroupBy(__rawValue));
            return _groupByField;
        }
    }

    /**
     * Setter for groupBy
     * 
     * @see SlidingWindowFeature.Fields#groupBy
     */
    public SlidingWindowFeature setGroupBy(SlidingWindowFeature.GroupBy value, SetMode mode) {
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
     * @see SlidingWindowFeature.Fields#groupBy
     */
    public SlidingWindowFeature setGroupBy(
        @Nonnull
        SlidingWindowFeature.GroupBy value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field groupBy of com.linkedin.feathr.compute.SlidingWindowFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "groupBy", value.data());
            _groupByField = value;
        }
        return this;
    }

    /**
     * Existence checker for limit
     * 
     * @see SlidingWindowFeature.Fields#limit
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
     * @see SlidingWindowFeature.Fields#limit
     */
    public void removeLimit() {
        super._map.remove("limit");
    }

    /**
     * Getter for limit
     * 
     * @see SlidingWindowFeature.Fields#limit
     */
    public Integer getLimit(GetMode mode) {
        return getLimit();
    }

    /**
     * Getter for limit
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowFeature.Fields#limit
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
     * @see SlidingWindowFeature.Fields#limit
     */
    public SlidingWindowFeature setLimit(Integer value, SetMode mode) {
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
     * @see SlidingWindowFeature.Fields#limit
     */
    public SlidingWindowFeature setLimit(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field limit of com.linkedin.feathr.compute.SlidingWindowFeature to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
            _limitField = value;
        }
        return this;
    }

    /**
     * Setter for limit
     * 
     * @see SlidingWindowFeature.Fields#limit
     */
    public SlidingWindowFeature setLimit(int value) {
        CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
        _limitField = value;
        return this;
    }

    @Override
    public SlidingWindowFeature clone()
        throws CloneNotSupportedException
    {
        SlidingWindowFeature __clone = ((SlidingWindowFeature) super.clone());
        __clone.__changeListener = new SlidingWindowFeature.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public SlidingWindowFeature copy()
        throws CloneNotSupportedException
    {
        SlidingWindowFeature __copy = ((SlidingWindowFeature) super.copy());
        __copy._filterField = null;
        __copy._aggregationTypeField = null;
        __copy._targetColumnField = null;
        __copy._limitField = null;
        __copy._windowField = null;
        __copy._groupByField = null;
        __copy._lateralViewsField = null;
        __copy.__changeListener = new SlidingWindowFeature.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final SlidingWindowFeature __objectRef;

        private ChangeListener(SlidingWindowFeature reference) {
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
        public com.linkedin.feathr.compute.SlidingWindowFeature.TargetColumn.Fields targetColumn() {
            return new com.linkedin.feathr.compute.SlidingWindowFeature.TargetColumn.Fields(getPathComponents(), "targetColumn");
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
        public com.linkedin.feathr.compute.Window.Fields window() {
            return new com.linkedin.feathr.compute.Window.Fields(getPathComponents(), "window");
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public com.linkedin.feathr.compute.LateralViewArray.Fields lateralViews() {
            return new com.linkedin.feathr.compute.LateralViewArray.Fields(getPathComponents(), "lateralViews");
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
        public com.linkedin.feathr.compute.SlidingWindowFeature.Filter.Fields filter() {
            return new com.linkedin.feathr.compute.SlidingWindowFeature.Filter.Fields(getPathComponents(), "filter");
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public com.linkedin.feathr.compute.SlidingWindowFeature.GroupBy.Fields groupBy() {
            return new com.linkedin.feathr.compute.SlidingWindowFeature.GroupBy.Fields(getPathComponents(), "groupBy");
        }

        /**
         * Represents the max number of groups (with aggregation results) to return.
         * 
         */
        public PathSpec limit() {
            return new PathSpec(getPathComponents(), "limit");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\SlidingWindowFeature.pdl.")
    public static class Filter
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.compute.SqlExpression _sqlExpressionMember = null;
        private SlidingWindowFeature.Filter.ChangeListener __changeListener = new SlidingWindowFeature.Filter.ChangeListener(this);
        private final static DataSchema MEMBER_SqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.SqlExpression");

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

        public static SlidingWindowFeature.Filter create(com.linkedin.feathr.compute.SqlExpression value) {
            SlidingWindowFeature.Filter newUnion = new SlidingWindowFeature.Filter();
            newUnion.setSqlExpression(value);
            return newUnion;
        }

        public boolean isSqlExpression() {
            return memberIs("com.linkedin.feathr.compute.SqlExpression");
        }

        public com.linkedin.feathr.compute.SqlExpression getSqlExpression() {
            checkNotNull();
            if (_sqlExpressionMember!= null) {
                return _sqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.SqlExpression");
            _sqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.SqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sqlExpressionMember;
        }

        public void setSqlExpression(com.linkedin.feathr.compute.SqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.SqlExpression", value.data());
        }

        public static SlidingWindowFeature.Filter.ProjectionMask createMask() {
            return new SlidingWindowFeature.Filter.ProjectionMask();
        }

        @Override
        public SlidingWindowFeature.Filter clone()
            throws CloneNotSupportedException
        {
            SlidingWindowFeature.Filter __clone = ((SlidingWindowFeature.Filter) super.clone());
            __clone.__changeListener = new SlidingWindowFeature.Filter.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowFeature.Filter copy()
            throws CloneNotSupportedException
        {
            SlidingWindowFeature.Filter __copy = ((SlidingWindowFeature.Filter) super.copy());
            __copy._sqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowFeature.Filter.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowFeature.Filter __objectRef;

            private ChangeListener(SlidingWindowFeature.Filter reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.compute.SqlExpression":
                        __objectRef._sqlExpressionMember = null;
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

            public com.linkedin.feathr.compute.SqlExpression.Fields SqlExpression() {
                return new com.linkedin.feathr.compute.SqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.compute.SqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.SqlExpression.ProjectionMask _SqlExpressionMask;

            ProjectionMask() {
                super(2);
            }

            public SlidingWindowFeature.Filter.ProjectionMask withSqlExpression(Function<com.linkedin.feathr.compute.SqlExpression.ProjectionMask, com.linkedin.feathr.compute.SqlExpression.ProjectionMask> nestedMask) {
                _SqlExpressionMask = nestedMask.apply(((_SqlExpressionMask == null)?com.linkedin.feathr.compute.SqlExpression.createMask():_SqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.compute.SqlExpression", _SqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\SlidingWindowFeature.pdl.")
    public static class GroupBy
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.compute.SqlExpression _sqlExpressionMember = null;
        private SlidingWindowFeature.GroupBy.ChangeListener __changeListener = new SlidingWindowFeature.GroupBy.ChangeListener(this);
        private final static DataSchema MEMBER_SqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.SqlExpression");

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

        public static SlidingWindowFeature.GroupBy create(com.linkedin.feathr.compute.SqlExpression value) {
            SlidingWindowFeature.GroupBy newUnion = new SlidingWindowFeature.GroupBy();
            newUnion.setSqlExpression(value);
            return newUnion;
        }

        public boolean isSqlExpression() {
            return memberIs("com.linkedin.feathr.compute.SqlExpression");
        }

        public com.linkedin.feathr.compute.SqlExpression getSqlExpression() {
            checkNotNull();
            if (_sqlExpressionMember!= null) {
                return _sqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.SqlExpression");
            _sqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.SqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sqlExpressionMember;
        }

        public void setSqlExpression(com.linkedin.feathr.compute.SqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.SqlExpression", value.data());
        }

        public static SlidingWindowFeature.GroupBy.ProjectionMask createMask() {
            return new SlidingWindowFeature.GroupBy.ProjectionMask();
        }

        @Override
        public SlidingWindowFeature.GroupBy clone()
            throws CloneNotSupportedException
        {
            SlidingWindowFeature.GroupBy __clone = ((SlidingWindowFeature.GroupBy) super.clone());
            __clone.__changeListener = new SlidingWindowFeature.GroupBy.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowFeature.GroupBy copy()
            throws CloneNotSupportedException
        {
            SlidingWindowFeature.GroupBy __copy = ((SlidingWindowFeature.GroupBy) super.copy());
            __copy._sqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowFeature.GroupBy.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowFeature.GroupBy __objectRef;

            private ChangeListener(SlidingWindowFeature.GroupBy reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.compute.SqlExpression":
                        __objectRef._sqlExpressionMember = null;
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

            public com.linkedin.feathr.compute.SqlExpression.Fields SqlExpression() {
                return new com.linkedin.feathr.compute.SqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.compute.SqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.SqlExpression.ProjectionMask _SqlExpressionMask;

            ProjectionMask() {
                super(2);
            }

            public SlidingWindowFeature.GroupBy.ProjectionMask withSqlExpression(Function<com.linkedin.feathr.compute.SqlExpression.ProjectionMask, com.linkedin.feathr.compute.SqlExpression.ProjectionMask> nestedMask) {
                _SqlExpressionMask = nestedMask.apply(((_SqlExpressionMask == null)?com.linkedin.feathr.compute.SqlExpression.createMask():_SqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.compute.SqlExpression", _SqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.compute.SlidingWindowFeature.TargetColumn.ProjectionMask _targetColumnMask;
        private com.linkedin.feathr.compute.Window.ProjectionMask _windowMask;
        private com.linkedin.feathr.compute.LateralViewArray.ProjectionMask _lateralViewsMask;
        private com.linkedin.feathr.compute.SlidingWindowFeature.Filter.ProjectionMask _filterMask;
        private com.linkedin.feathr.compute.SlidingWindowFeature.GroupBy.ProjectionMask _groupByMask;

        ProjectionMask() {
            super(10);
        }

        /**
         * The target column to perform aggregation against.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withTargetColumn(Function<com.linkedin.feathr.compute.SlidingWindowFeature.TargetColumn.ProjectionMask, com.linkedin.feathr.compute.SlidingWindowFeature.TargetColumn.ProjectionMask> nestedMask) {
            _targetColumnMask = nestedMask.apply(((_targetColumnMask == null)?SlidingWindowFeature.TargetColumn.createMask():_targetColumnMask));
            getDataMap().put("targetColumn", _targetColumnMask.getDataMap());
            return this;
        }

        /**
         * The target column to perform aggregation against.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withTargetColumn() {
            _targetColumnMask = null;
            getDataMap().put("targetColumn", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents supported types of aggregation.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withAggregationType() {
            getDataMap().put("aggregationType", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withWindow(Function<com.linkedin.feathr.compute.Window.ProjectionMask, com.linkedin.feathr.compute.Window.ProjectionMask> nestedMask) {
            _windowMask = nestedMask.apply(((_windowMask == null)?Window.createMask():_windowMask));
            getDataMap().put("window", _windowMask.getDataMap());
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withWindow() {
            _windowMask = null;
            getDataMap().put("window", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withLateralViews(Function<com.linkedin.feathr.compute.LateralViewArray.ProjectionMask, com.linkedin.feathr.compute.LateralViewArray.ProjectionMask> nestedMask) {
            _lateralViewsMask = nestedMask.apply(((_lateralViewsMask == null)?LateralViewArray.createMask():_lateralViewsMask));
            getDataMap().put("lateralViews", _lateralViewsMask.getDataMap());
            return this;
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withLateralViews() {
            _lateralViewsMask = null;
            getDataMap().put("lateralViews", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents lateral view statements to be applied before the aggregation. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withLateralViews(Function<com.linkedin.feathr.compute.LateralViewArray.ProjectionMask, com.linkedin.feathr.compute.LateralViewArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
        public SlidingWindowFeature.ProjectionMask withLateralViews(Integer start, Integer count) {
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
        public SlidingWindowFeature.ProjectionMask withFilter(Function<com.linkedin.feathr.compute.SlidingWindowFeature.Filter.ProjectionMask, com.linkedin.feathr.compute.SlidingWindowFeature.Filter.ProjectionMask> nestedMask) {
            _filterMask = nestedMask.apply(((_filterMask == null)?SlidingWindowFeature.Filter.createMask():_filterMask));
            getDataMap().put("filter", _filterMask.getDataMap());
            return this;
        }

        /**
         * Represents the filter statement before the aggregation.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withFilter() {
            _filterMask = null;
            getDataMap().put("filter", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withGroupBy(Function<com.linkedin.feathr.compute.SlidingWindowFeature.GroupBy.ProjectionMask, com.linkedin.feathr.compute.SlidingWindowFeature.GroupBy.ProjectionMask> nestedMask) {
            _groupByMask = nestedMask.apply(((_groupByMask == null)?SlidingWindowFeature.GroupBy.createMask():_groupByMask));
            getDataMap().put("groupBy", _groupByMask.getDataMap());
            return this;
        }

        /**
         * Represents the target to be grouped by before aggregation. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withGroupBy() {
            _groupByMask = null;
            getDataMap().put("groupBy", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the max number of groups (with aggregation results) to return.
         * 
         */
        public SlidingWindowFeature.ProjectionMask withLimit() {
            getDataMap().put("limit", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\SlidingWindowFeature.pdl.")
    public static class TargetColumn
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.compute/**An expression in Spark SQL.*/record SqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.compute.SqlExpression _sqlExpressionMember = null;
        private SlidingWindowFeature.TargetColumn.ChangeListener __changeListener = new SlidingWindowFeature.TargetColumn.ChangeListener(this);
        private final static DataSchema MEMBER_SqlExpression = SCHEMA.getTypeByMemberKey("com.linkedin.feathr.compute.SqlExpression");

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

        public static SlidingWindowFeature.TargetColumn create(com.linkedin.feathr.compute.SqlExpression value) {
            SlidingWindowFeature.TargetColumn newUnion = new SlidingWindowFeature.TargetColumn();
            newUnion.setSqlExpression(value);
            return newUnion;
        }

        public boolean isSqlExpression() {
            return memberIs("com.linkedin.feathr.compute.SqlExpression");
        }

        public com.linkedin.feathr.compute.SqlExpression getSqlExpression() {
            checkNotNull();
            if (_sqlExpressionMember!= null) {
                return _sqlExpressionMember;
            }
            Object __rawValue = super._map.get("com.linkedin.feathr.compute.SqlExpression");
            _sqlExpressionMember = ((__rawValue == null)?null:new com.linkedin.feathr.compute.SqlExpression(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _sqlExpressionMember;
        }

        public void setSqlExpression(com.linkedin.feathr.compute.SqlExpression value) {
            checkNotNull();
            super._map.clear();
            _sqlExpressionMember = value;
            CheckedUtil.putWithoutChecking(super._map, "com.linkedin.feathr.compute.SqlExpression", value.data());
        }

        public static SlidingWindowFeature.TargetColumn.ProjectionMask createMask() {
            return new SlidingWindowFeature.TargetColumn.ProjectionMask();
        }

        @Override
        public SlidingWindowFeature.TargetColumn clone()
            throws CloneNotSupportedException
        {
            SlidingWindowFeature.TargetColumn __clone = ((SlidingWindowFeature.TargetColumn) super.clone());
            __clone.__changeListener = new SlidingWindowFeature.TargetColumn.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowFeature.TargetColumn copy()
            throws CloneNotSupportedException
        {
            SlidingWindowFeature.TargetColumn __copy = ((SlidingWindowFeature.TargetColumn) super.copy());
            __copy._sqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowFeature.TargetColumn.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowFeature.TargetColumn __objectRef;

            private ChangeListener(SlidingWindowFeature.TargetColumn reference) {
                __objectRef = reference;
            }

            @Override
            public void onUnderlyingMapChanged(String key, Object value) {
                switch (key) {
                    case "com.linkedin.feathr.compute.SqlExpression":
                        __objectRef._sqlExpressionMember = null;
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

            public com.linkedin.feathr.compute.SqlExpression.Fields SqlExpression() {
                return new com.linkedin.feathr.compute.SqlExpression.Fields(getPathComponents(), "com.linkedin.feathr.compute.SqlExpression");
            }

        }

        public static class ProjectionMask
            extends MaskMap
        {

            private com.linkedin.feathr.compute.SqlExpression.ProjectionMask _SqlExpressionMask;

            ProjectionMask() {
                super(2);
            }

            public SlidingWindowFeature.TargetColumn.ProjectionMask withSqlExpression(Function<com.linkedin.feathr.compute.SqlExpression.ProjectionMask, com.linkedin.feathr.compute.SqlExpression.ProjectionMask> nestedMask) {
                _SqlExpressionMask = nestedMask.apply(((_SqlExpressionMask == null)?com.linkedin.feathr.compute.SqlExpression.createMask():_SqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.compute.SqlExpression", _SqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

}
