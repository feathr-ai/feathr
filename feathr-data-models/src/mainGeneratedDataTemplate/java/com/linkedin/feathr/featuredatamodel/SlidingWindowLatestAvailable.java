
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
 * This sliding window algorithm picks the latest available feature data from the source data. Note the latest here means event time instead of processing time.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowLatestAvailable.pdl.")
public class SlidingWindowLatestAvailable
    extends RecordTemplate
{

    private final static SlidingWindowLatestAvailable.Fields _fields = new SlidingWindowLatestAvailable.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**This sliding window algorithm picks the latest available feature data from the source data. Note the latest here means event time instead of processing time.*/record SlidingWindowLatestAvailable{/**The target column to pick the latest available record from.*/targetColumn:union[/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}]/**Represents the time window to look back from label data's timestamp.*/window:optional/**Represents a time window used in sliding window algorithms.*/record Window{/**Represents the duration of the window.*/size:int/**Represents a unit of time.*/unit:enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}}/**Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.*/lateralViews:array[/**Lateral view is used in conjunction with table generating functions (eg. the most commonly used explode()), which typically generates zero or more output rows for each input row. A lateral view first applies the table generating function to each row of base table, and then joins resulting output rows to the input rows to form a virtual table with the supplied table alias. For more details and examples, refer to https://cwiki.apache.org/confluence/display/Hive/LanguageManual+LateralView.*/record LateralView{/**A table-generating function transforms a single input row to multiple output rows. For example, explode(array('A','B','C') will produce 3 one-column rows, which are row1: 'A'; row2: 'B'; row3: 'C'.*/tableGeneratingFunction:union[SparkSqlExpression]/**Represents the alias for referencing the generated virtual table. It will be used in subsequent statements (eg. filter, groupBy) in the sliding window feature definition.*/virtualTableAlias:string}]=[]/**Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.*/groupBy:optional union[SparkSqlExpression]/**Represents the filter statement before applying the sliding window algorithm.*/filter:optional union[SparkSqlExpression]/**Represents the max number of groups (with latest available feature data) to return.*/limit:optional int}", SchemaFormatType.PDL));
    private SlidingWindowLatestAvailable.TargetColumn _targetColumnField = null;
    private Window _windowField = null;
    private LateralViewArray _lateralViewsField = null;
    private SlidingWindowLatestAvailable.GroupBy _groupByField = null;
    private SlidingWindowLatestAvailable.Filter _filterField = null;
    private Integer _limitField = null;
    private SlidingWindowLatestAvailable.ChangeListener __changeListener = new SlidingWindowLatestAvailable.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TargetColumn = SCHEMA.getField("targetColumn");
    private final static RecordDataSchema.Field FIELD_Window = SCHEMA.getField("window");
    private final static RecordDataSchema.Field FIELD_LateralViews = SCHEMA.getField("lateralViews");
    private final static LateralViewArray DEFAULT_LateralViews;
    private final static RecordDataSchema.Field FIELD_GroupBy = SCHEMA.getField("groupBy");
    private final static RecordDataSchema.Field FIELD_Filter = SCHEMA.getField("filter");
    private final static RecordDataSchema.Field FIELD_Limit = SCHEMA.getField("limit");

    static {
        DEFAULT_LateralViews = ((FIELD_LateralViews.getDefault() == null)?null:new LateralViewArray(DataTemplateUtil.castOrThrow(FIELD_LateralViews.getDefault(), DataList.class)));
    }

    public SlidingWindowLatestAvailable() {
        super(new DataMap(8, 0.75F), SCHEMA, 7);
        addChangeListener(__changeListener);
    }

    public SlidingWindowLatestAvailable(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static SlidingWindowLatestAvailable.Fields fields() {
        return _fields;
    }

    public static SlidingWindowLatestAvailable.ProjectionMask createMask() {
        return new SlidingWindowLatestAvailable.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for targetColumn
     * 
     * @see SlidingWindowLatestAvailable.Fields#targetColumn
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
     * @see SlidingWindowLatestAvailable.Fields#targetColumn
     */
    public void removeTargetColumn() {
        super._map.remove("targetColumn");
    }

    /**
     * Getter for targetColumn
     * 
     * @see SlidingWindowLatestAvailable.Fields#targetColumn
     */
    public SlidingWindowLatestAvailable.TargetColumn getTargetColumn(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTargetColumn();
            case DEFAULT:
            case NULL:
                if (_targetColumnField!= null) {
                    return _targetColumnField;
                } else {
                    Object __rawValue = super._map.get("targetColumn");
                    _targetColumnField = ((__rawValue == null)?null:new SlidingWindowLatestAvailable.TargetColumn(__rawValue));
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
     * @see SlidingWindowLatestAvailable.Fields#targetColumn
     */
    @Nonnull
    public SlidingWindowLatestAvailable.TargetColumn getTargetColumn() {
        if (_targetColumnField!= null) {
            return _targetColumnField;
        } else {
            Object __rawValue = super._map.get("targetColumn");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("targetColumn");
            }
            _targetColumnField = ((__rawValue == null)?null:new SlidingWindowLatestAvailable.TargetColumn(__rawValue));
            return _targetColumnField;
        }
    }

    /**
     * Setter for targetColumn
     * 
     * @see SlidingWindowLatestAvailable.Fields#targetColumn
     */
    public SlidingWindowLatestAvailable setTargetColumn(SlidingWindowLatestAvailable.TargetColumn value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTargetColumn(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field targetColumn of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable");
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
     * @see SlidingWindowLatestAvailable.Fields#targetColumn
     */
    public SlidingWindowLatestAvailable setTargetColumn(
        @Nonnull
        SlidingWindowLatestAvailable.TargetColumn value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field targetColumn of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "targetColumn", value.data());
            _targetColumnField = value;
        }
        return this;
    }

    /**
     * Existence checker for window
     * 
     * @see SlidingWindowLatestAvailable.Fields#window
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
     * @see SlidingWindowLatestAvailable.Fields#window
     */
    public void removeWindow() {
        super._map.remove("window");
    }

    /**
     * Getter for window
     * 
     * @see SlidingWindowLatestAvailable.Fields#window
     */
    public Window getWindow(GetMode mode) {
        return getWindow();
    }

    /**
     * Getter for window
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowLatestAvailable.Fields#window
     */
    @Nullable
    public Window getWindow() {
        if (_windowField!= null) {
            return _windowField;
        } else {
            Object __rawValue = super._map.get("window");
            _windowField = ((__rawValue == null)?null:new Window(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _windowField;
        }
    }

    /**
     * Setter for window
     * 
     * @see SlidingWindowLatestAvailable.Fields#window
     */
    public SlidingWindowLatestAvailable setWindow(Window value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setWindow(value);
            case REMOVE_OPTIONAL_IF_NULL:
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
     * @see SlidingWindowLatestAvailable.Fields#window
     */
    public SlidingWindowLatestAvailable setWindow(
        @Nonnull
        Window value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field window of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "window", value.data());
            _windowField = value;
        }
        return this;
    }

    /**
     * Existence checker for lateralViews
     * 
     * @see SlidingWindowLatestAvailable.Fields#lateralViews
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
     * @see SlidingWindowLatestAvailable.Fields#lateralViews
     */
    public void removeLateralViews() {
        super._map.remove("lateralViews");
    }

    /**
     * Getter for lateralViews
     * 
     * @see SlidingWindowLatestAvailable.Fields#lateralViews
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
     * @see SlidingWindowLatestAvailable.Fields#lateralViews
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
     * @see SlidingWindowLatestAvailable.Fields#lateralViews
     */
    public SlidingWindowLatestAvailable setLateralViews(LateralViewArray value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setLateralViews(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field lateralViews of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable");
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
     * @see SlidingWindowLatestAvailable.Fields#lateralViews
     */
    public SlidingWindowLatestAvailable setLateralViews(
        @Nonnull
        LateralViewArray value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field lateralViews of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "lateralViews", value.data());
            _lateralViewsField = value;
        }
        return this;
    }

    /**
     * Existence checker for groupBy
     * 
     * @see SlidingWindowLatestAvailable.Fields#groupBy
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
     * @see SlidingWindowLatestAvailable.Fields#groupBy
     */
    public void removeGroupBy() {
        super._map.remove("groupBy");
    }

    /**
     * Getter for groupBy
     * 
     * @see SlidingWindowLatestAvailable.Fields#groupBy
     */
    public SlidingWindowLatestAvailable.GroupBy getGroupBy(GetMode mode) {
        return getGroupBy();
    }

    /**
     * Getter for groupBy
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowLatestAvailable.Fields#groupBy
     */
    @Nullable
    public SlidingWindowLatestAvailable.GroupBy getGroupBy() {
        if (_groupByField!= null) {
            return _groupByField;
        } else {
            Object __rawValue = super._map.get("groupBy");
            _groupByField = ((__rawValue == null)?null:new SlidingWindowLatestAvailable.GroupBy(__rawValue));
            return _groupByField;
        }
    }

    /**
     * Setter for groupBy
     * 
     * @see SlidingWindowLatestAvailable.Fields#groupBy
     */
    public SlidingWindowLatestAvailable setGroupBy(SlidingWindowLatestAvailable.GroupBy value, SetMode mode) {
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
     * @see SlidingWindowLatestAvailable.Fields#groupBy
     */
    public SlidingWindowLatestAvailable setGroupBy(
        @Nonnull
        SlidingWindowLatestAvailable.GroupBy value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field groupBy of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "groupBy", value.data());
            _groupByField = value;
        }
        return this;
    }

    /**
     * Existence checker for filter
     * 
     * @see SlidingWindowLatestAvailable.Fields#filter
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
     * @see SlidingWindowLatestAvailable.Fields#filter
     */
    public void removeFilter() {
        super._map.remove("filter");
    }

    /**
     * Getter for filter
     * 
     * @see SlidingWindowLatestAvailable.Fields#filter
     */
    public SlidingWindowLatestAvailable.Filter getFilter(GetMode mode) {
        return getFilter();
    }

    /**
     * Getter for filter
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowLatestAvailable.Fields#filter
     */
    @Nullable
    public SlidingWindowLatestAvailable.Filter getFilter() {
        if (_filterField!= null) {
            return _filterField;
        } else {
            Object __rawValue = super._map.get("filter");
            _filterField = ((__rawValue == null)?null:new SlidingWindowLatestAvailable.Filter(__rawValue));
            return _filterField;
        }
    }

    /**
     * Setter for filter
     * 
     * @see SlidingWindowLatestAvailable.Fields#filter
     */
    public SlidingWindowLatestAvailable setFilter(SlidingWindowLatestAvailable.Filter value, SetMode mode) {
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
     * @see SlidingWindowLatestAvailable.Fields#filter
     */
    public SlidingWindowLatestAvailable setFilter(
        @Nonnull
        SlidingWindowLatestAvailable.Filter value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field filter of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "filter", value.data());
            _filterField = value;
        }
        return this;
    }

    /**
     * Existence checker for limit
     * 
     * @see SlidingWindowLatestAvailable.Fields#limit
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
     * @see SlidingWindowLatestAvailable.Fields#limit
     */
    public void removeLimit() {
        super._map.remove("limit");
    }

    /**
     * Getter for limit
     * 
     * @see SlidingWindowLatestAvailable.Fields#limit
     */
    public Integer getLimit(GetMode mode) {
        return getLimit();
    }

    /**
     * Getter for limit
     * 
     * @return
     *     Optional field. Always check for null.
     * @see SlidingWindowLatestAvailable.Fields#limit
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
     * @see SlidingWindowLatestAvailable.Fields#limit
     */
    public SlidingWindowLatestAvailable setLimit(Integer value, SetMode mode) {
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
     * @see SlidingWindowLatestAvailable.Fields#limit
     */
    public SlidingWindowLatestAvailable setLimit(
        @Nonnull
        Integer value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field limit of com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
            _limitField = value;
        }
        return this;
    }

    /**
     * Setter for limit
     * 
     * @see SlidingWindowLatestAvailable.Fields#limit
     */
    public SlidingWindowLatestAvailable setLimit(int value) {
        CheckedUtil.putWithoutChecking(super._map, "limit", DataTemplateUtil.coerceIntInput(value));
        _limitField = value;
        return this;
    }

    @Override
    public SlidingWindowLatestAvailable clone()
        throws CloneNotSupportedException
    {
        SlidingWindowLatestAvailable __clone = ((SlidingWindowLatestAvailable) super.clone());
        __clone.__changeListener = new SlidingWindowLatestAvailable.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public SlidingWindowLatestAvailable copy()
        throws CloneNotSupportedException
    {
        SlidingWindowLatestAvailable __copy = ((SlidingWindowLatestAvailable) super.copy());
        __copy._filterField = null;
        __copy._targetColumnField = null;
        __copy._limitField = null;
        __copy._windowField = null;
        __copy._groupByField = null;
        __copy._lateralViewsField = null;
        __copy.__changeListener = new SlidingWindowLatestAvailable.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final SlidingWindowLatestAvailable __objectRef;

        private ChangeListener(SlidingWindowLatestAvailable reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "filter":
                    __objectRef._filterField = null;
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
         * The target column to pick the latest available record from.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.TargetColumn.Fields targetColumn() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.TargetColumn.Fields(getPathComponents(), "targetColumn");
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public com.linkedin.feathr.featureDataModel.Window.Fields window() {
            return new com.linkedin.feathr.featureDataModel.Window.Fields(getPathComponents(), "window");
        }

        /**
         * Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.
         * 
         */
        public com.linkedin.feathr.featureDataModel.LateralViewArray.Fields lateralViews() {
            return new com.linkedin.feathr.featureDataModel.LateralViewArray.Fields(getPathComponents(), "lateralViews");
        }

        /**
         * Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.
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
         * Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.GroupBy.Fields groupBy() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.GroupBy.Fields(getPathComponents(), "groupBy");
        }

        /**
         * Represents the filter statement before applying the sliding window algorithm.
         * 
         */
        public com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Filter.Fields filter() {
            return new com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Filter.Fields(getPathComponents(), "filter");
        }

        /**
         * Represents the max number of groups (with latest available feature data) to return.
         * 
         */
        public PathSpec limit() {
            return new PathSpec(getPathComponents(), "limit");
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowLatestAvailable.pdl.")
    public static class Filter
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowLatestAvailable.Filter.ChangeListener __changeListener = new SlidingWindowLatestAvailable.Filter.ChangeListener(this);
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

        public static SlidingWindowLatestAvailable.Filter create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowLatestAvailable.Filter newUnion = new SlidingWindowLatestAvailable.Filter();
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

        public static SlidingWindowLatestAvailable.Filter.ProjectionMask createMask() {
            return new SlidingWindowLatestAvailable.Filter.ProjectionMask();
        }

        @Override
        public SlidingWindowLatestAvailable.Filter clone()
            throws CloneNotSupportedException
        {
            SlidingWindowLatestAvailable.Filter __clone = ((SlidingWindowLatestAvailable.Filter) super.clone());
            __clone.__changeListener = new SlidingWindowLatestAvailable.Filter.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowLatestAvailable.Filter copy()
            throws CloneNotSupportedException
        {
            SlidingWindowLatestAvailable.Filter __copy = ((SlidingWindowLatestAvailable.Filter) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowLatestAvailable.Filter.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowLatestAvailable.Filter __objectRef;

            private ChangeListener(SlidingWindowLatestAvailable.Filter reference) {
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

            public SlidingWindowLatestAvailable.Filter.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowLatestAvailable.pdl.")
    public static class GroupBy
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowLatestAvailable.GroupBy.ChangeListener __changeListener = new SlidingWindowLatestAvailable.GroupBy.ChangeListener(this);
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

        public static SlidingWindowLatestAvailable.GroupBy create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowLatestAvailable.GroupBy newUnion = new SlidingWindowLatestAvailable.GroupBy();
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

        public static SlidingWindowLatestAvailable.GroupBy.ProjectionMask createMask() {
            return new SlidingWindowLatestAvailable.GroupBy.ProjectionMask();
        }

        @Override
        public SlidingWindowLatestAvailable.GroupBy clone()
            throws CloneNotSupportedException
        {
            SlidingWindowLatestAvailable.GroupBy __clone = ((SlidingWindowLatestAvailable.GroupBy) super.clone());
            __clone.__changeListener = new SlidingWindowLatestAvailable.GroupBy.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowLatestAvailable.GroupBy copy()
            throws CloneNotSupportedException
        {
            SlidingWindowLatestAvailable.GroupBy __copy = ((SlidingWindowLatestAvailable.GroupBy) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowLatestAvailable.GroupBy.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowLatestAvailable.GroupBy __objectRef;

            private ChangeListener(SlidingWindowLatestAvailable.GroupBy reference) {
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

            public SlidingWindowLatestAvailable.GroupBy.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.TargetColumn.ProjectionMask _targetColumnMask;
        private com.linkedin.feathr.featureDataModel.Window.ProjectionMask _windowMask;
        private com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask _lateralViewsMask;
        private com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.GroupBy.ProjectionMask _groupByMask;
        private com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Filter.ProjectionMask _filterMask;

        ProjectionMask() {
            super(8);
        }

        /**
         * The target column to pick the latest available record from.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withTargetColumn(Function<com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.TargetColumn.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.TargetColumn.ProjectionMask> nestedMask) {
            _targetColumnMask = nestedMask.apply(((_targetColumnMask == null)?SlidingWindowLatestAvailable.TargetColumn.createMask():_targetColumnMask));
            getDataMap().put("targetColumn", _targetColumnMask.getDataMap());
            return this;
        }

        /**
         * The target column to pick the latest available record from.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withTargetColumn() {
            _targetColumnMask = null;
            getDataMap().put("targetColumn", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withWindow(Function<com.linkedin.feathr.featureDataModel.Window.ProjectionMask, com.linkedin.feathr.featureDataModel.Window.ProjectionMask> nestedMask) {
            _windowMask = nestedMask.apply(((_windowMask == null)?Window.createMask():_windowMask));
            getDataMap().put("window", _windowMask.getDataMap());
            return this;
        }

        /**
         * Represents the time window to look back from label data's timestamp.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withWindow() {
            _windowMask = null;
            getDataMap().put("window", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withLateralViews(Function<com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask, com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask> nestedMask) {
            _lateralViewsMask = nestedMask.apply(((_lateralViewsMask == null)?LateralViewArray.createMask():_lateralViewsMask));
            getDataMap().put("lateralViews", _lateralViewsMask.getDataMap());
            return this;
        }

        /**
         * Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withLateralViews() {
            _lateralViewsMask = null;
            getDataMap().put("lateralViews", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withLateralViews(Function<com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask, com.linkedin.feathr.featureDataModel.LateralViewArray.ProjectionMask> nestedMask, Integer start, Integer count) {
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
         * Represents lateral view statements to be applied before applying the sliding window algorithm. Refer to LateralView for more details.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withLateralViews(Integer start, Integer count) {
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
         * Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withGroupBy(Function<com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.GroupBy.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.GroupBy.ProjectionMask> nestedMask) {
            _groupByMask = nestedMask.apply(((_groupByMask == null)?SlidingWindowLatestAvailable.GroupBy.createMask():_groupByMask));
            getDataMap().put("groupBy", _groupByMask.getDataMap());
            return this;
        }

        /**
         * Represents the target to be grouped by before applying the sliding window algorithm. If groupBy is not set, the aggregation will be performed over the entire dataset.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withGroupBy() {
            _groupByMask = null;
            getDataMap().put("groupBy", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the filter statement before applying the sliding window algorithm.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withFilter(Function<com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Filter.ProjectionMask, com.linkedin.feathr.featureDataModel.SlidingWindowLatestAvailable.Filter.ProjectionMask> nestedMask) {
            _filterMask = nestedMask.apply(((_filterMask == null)?SlidingWindowLatestAvailable.Filter.createMask():_filterMask));
            getDataMap().put("filter", _filterMask.getDataMap());
            return this;
        }

        /**
         * Represents the filter statement before applying the sliding window algorithm.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withFilter() {
            _filterMask = null;
            getDataMap().put("filter", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the max number of groups (with latest available feature data) to return.
         * 
         */
        public SlidingWindowLatestAvailable.ProjectionMask withLimit() {
            getDataMap().put("limit", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

    @Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowLatestAvailable.pdl.")
    public static class TargetColumn
        extends UnionTemplate
    {

        private final static UnionDataSchema SCHEMA = ((UnionDataSchema) DataTemplateUtil.parseSchema("union[{namespace com.linkedin.feathr.featureDataModel/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/sql:string}}]", SchemaFormatType.PDL));
        private com.linkedin.feathr.featureDataModel.SparkSqlExpression _sparkSqlExpressionMember = null;
        private SlidingWindowLatestAvailable.TargetColumn.ChangeListener __changeListener = new SlidingWindowLatestAvailable.TargetColumn.ChangeListener(this);
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

        public static SlidingWindowLatestAvailable.TargetColumn create(com.linkedin.feathr.featureDataModel.SparkSqlExpression value) {
            SlidingWindowLatestAvailable.TargetColumn newUnion = new SlidingWindowLatestAvailable.TargetColumn();
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

        public static SlidingWindowLatestAvailable.TargetColumn.ProjectionMask createMask() {
            return new SlidingWindowLatestAvailable.TargetColumn.ProjectionMask();
        }

        @Override
        public SlidingWindowLatestAvailable.TargetColumn clone()
            throws CloneNotSupportedException
        {
            SlidingWindowLatestAvailable.TargetColumn __clone = ((SlidingWindowLatestAvailable.TargetColumn) super.clone());
            __clone.__changeListener = new SlidingWindowLatestAvailable.TargetColumn.ChangeListener(__clone);
            __clone.addChangeListener(__clone.__changeListener);
            return __clone;
        }

        @Override
        public SlidingWindowLatestAvailable.TargetColumn copy()
            throws CloneNotSupportedException
        {
            SlidingWindowLatestAvailable.TargetColumn __copy = ((SlidingWindowLatestAvailable.TargetColumn) super.copy());
            __copy._sparkSqlExpressionMember = null;
            __copy.__changeListener = new SlidingWindowLatestAvailable.TargetColumn.ChangeListener(__copy);
            __copy.addChangeListener(__copy.__changeListener);
            return __copy;
        }

        private static class ChangeListener
            implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
        {

            private final SlidingWindowLatestAvailable.TargetColumn __objectRef;

            private ChangeListener(SlidingWindowLatestAvailable.TargetColumn reference) {
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

            public SlidingWindowLatestAvailable.TargetColumn.ProjectionMask withSparkSqlExpression(Function<com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask, com.linkedin.feathr.featureDataModel.SparkSqlExpression.ProjectionMask> nestedMask) {
                _SparkSqlExpressionMask = nestedMask.apply(((_SparkSqlExpressionMask == null)?com.linkedin.feathr.featureDataModel.SparkSqlExpression.createMask():_SparkSqlExpressionMask));
                getDataMap().put("com.linkedin.feathr.featureDataModel.SparkSqlExpression", _SparkSqlExpressionMask.getDataMap());
                return this;
            }

        }

    }

}
