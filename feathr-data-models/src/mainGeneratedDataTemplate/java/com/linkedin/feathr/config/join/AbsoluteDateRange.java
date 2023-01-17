
package com.linkedin.feathr.config.join;

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


/**
 * The absolute date range with start and end date being required fields.
 * It accepts a start date and an end date which should be specifiied using the [[Date.pdl]] class.
 * absoluteDateRange: {
 *    startDate: Date(day=1, month=1, year=2020)
 *    endDate: Date(day=3, month=1, year=2020)
 *  }
 *  In this case, the endDate > startDate.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\AbsoluteDateRange.pdl.")
public class AbsoluteDateRange
    extends RecordTemplate
{

    private final static AbsoluteDateRange.Fields _fields = new AbsoluteDateRange.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The absolute date range with start and end date being required fields.\r\nIt accepts a start date and an end date which should be specifiied using the [[Date.pdl]] class.\r\nabsoluteDateRange: {\r\n   startDate: Date(day=1, month=1, year=2020)\r\n   endDate: Date(day=3, month=1, year=2020)\r\n }\r\n In this case, the endDate > startDate.*/record AbsoluteDateRange{/**start date of the date range, with the start date included in the range.*/startDate:/**Represents a date in a calendar year including day, year and month*/record Date{/**day*/@validate.integerRange={\"max\":31,\"min\":1}day:int/**month*/@validate.integerRange={\"max\":12,\"min\":1}month:int/**year*/@validate.integerRange={\"max\":2099,\"min\":1970}year:int}/**end date of the date range, with the end date included in the range.*/endDate:Date}", SchemaFormatType.PDL));
    private Date _startDateField = null;
    private Date _endDateField = null;
    private AbsoluteDateRange.ChangeListener __changeListener = new AbsoluteDateRange.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_StartDate = SCHEMA.getField("startDate");
    private final static RecordDataSchema.Field FIELD_EndDate = SCHEMA.getField("endDate");

    public AbsoluteDateRange() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public AbsoluteDateRange(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static AbsoluteDateRange.Fields fields() {
        return _fields;
    }

    public static AbsoluteDateRange.ProjectionMask createMask() {
        return new AbsoluteDateRange.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for startDate
     * 
     * @see AbsoluteDateRange.Fields#startDate
     */
    public boolean hasStartDate() {
        if (_startDateField!= null) {
            return true;
        }
        return super._map.containsKey("startDate");
    }

    /**
     * Remover for startDate
     * 
     * @see AbsoluteDateRange.Fields#startDate
     */
    public void removeStartDate() {
        super._map.remove("startDate");
    }

    /**
     * Getter for startDate
     * 
     * @see AbsoluteDateRange.Fields#startDate
     */
    public Date getStartDate(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getStartDate();
            case DEFAULT:
            case NULL:
                if (_startDateField!= null) {
                    return _startDateField;
                } else {
                    Object __rawValue = super._map.get("startDate");
                    _startDateField = ((__rawValue == null)?null:new Date(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _startDateField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for startDate
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see AbsoluteDateRange.Fields#startDate
     */
    @Nonnull
    public Date getStartDate() {
        if (_startDateField!= null) {
            return _startDateField;
        } else {
            Object __rawValue = super._map.get("startDate");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("startDate");
            }
            _startDateField = ((__rawValue == null)?null:new Date(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _startDateField;
        }
    }

    /**
     * Setter for startDate
     * 
     * @see AbsoluteDateRange.Fields#startDate
     */
    public AbsoluteDateRange setStartDate(Date value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setStartDate(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field startDate of com.linkedin.feathr.config.join.AbsoluteDateRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "startDate", value.data());
                    _startDateField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeStartDate();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "startDate", value.data());
                    _startDateField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "startDate", value.data());
                    _startDateField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for startDate
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see AbsoluteDateRange.Fields#startDate
     */
    public AbsoluteDateRange setStartDate(
        @Nonnull
        Date value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field startDate of com.linkedin.feathr.config.join.AbsoluteDateRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "startDate", value.data());
            _startDateField = value;
        }
        return this;
    }

    /**
     * Existence checker for endDate
     * 
     * @see AbsoluteDateRange.Fields#endDate
     */
    public boolean hasEndDate() {
        if (_endDateField!= null) {
            return true;
        }
        return super._map.containsKey("endDate");
    }

    /**
     * Remover for endDate
     * 
     * @see AbsoluteDateRange.Fields#endDate
     */
    public void removeEndDate() {
        super._map.remove("endDate");
    }

    /**
     * Getter for endDate
     * 
     * @see AbsoluteDateRange.Fields#endDate
     */
    public Date getEndDate(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getEndDate();
            case DEFAULT:
            case NULL:
                if (_endDateField!= null) {
                    return _endDateField;
                } else {
                    Object __rawValue = super._map.get("endDate");
                    _endDateField = ((__rawValue == null)?null:new Date(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _endDateField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for endDate
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see AbsoluteDateRange.Fields#endDate
     */
    @Nonnull
    public Date getEndDate() {
        if (_endDateField!= null) {
            return _endDateField;
        } else {
            Object __rawValue = super._map.get("endDate");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("endDate");
            }
            _endDateField = ((__rawValue == null)?null:new Date(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _endDateField;
        }
    }

    /**
     * Setter for endDate
     * 
     * @see AbsoluteDateRange.Fields#endDate
     */
    public AbsoluteDateRange setEndDate(Date value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setEndDate(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field endDate of com.linkedin.feathr.config.join.AbsoluteDateRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "endDate", value.data());
                    _endDateField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeEndDate();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "endDate", value.data());
                    _endDateField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "endDate", value.data());
                    _endDateField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for endDate
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see AbsoluteDateRange.Fields#endDate
     */
    public AbsoluteDateRange setEndDate(
        @Nonnull
        Date value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field endDate of com.linkedin.feathr.config.join.AbsoluteDateRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "endDate", value.data());
            _endDateField = value;
        }
        return this;
    }

    @Override
    public AbsoluteDateRange clone()
        throws CloneNotSupportedException
    {
        AbsoluteDateRange __clone = ((AbsoluteDateRange) super.clone());
        __clone.__changeListener = new AbsoluteDateRange.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public AbsoluteDateRange copy()
        throws CloneNotSupportedException
    {
        AbsoluteDateRange __copy = ((AbsoluteDateRange) super.copy());
        __copy._endDateField = null;
        __copy._startDateField = null;
        __copy.__changeListener = new AbsoluteDateRange.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final AbsoluteDateRange __objectRef;

        private ChangeListener(AbsoluteDateRange reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "endDate":
                    __objectRef._endDateField = null;
                    break;
                case "startDate":
                    __objectRef._startDateField = null;
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
         * start date of the date range, with the start date included in the range.
         * 
         */
        public com.linkedin.feathr.config.join.Date.Fields startDate() {
            return new com.linkedin.feathr.config.join.Date.Fields(getPathComponents(), "startDate");
        }

        /**
         * end date of the date range, with the end date included in the range.
         * 
         */
        public com.linkedin.feathr.config.join.Date.Fields endDate() {
            return new com.linkedin.feathr.config.join.Date.Fields(getPathComponents(), "endDate");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.Date.ProjectionMask _startDateMask;
        private com.linkedin.feathr.config.join.Date.ProjectionMask _endDateMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * start date of the date range, with the start date included in the range.
         * 
         */
        public AbsoluteDateRange.ProjectionMask withStartDate(Function<com.linkedin.feathr.config.join.Date.ProjectionMask, com.linkedin.feathr.config.join.Date.ProjectionMask> nestedMask) {
            _startDateMask = nestedMask.apply(((_startDateMask == null)?Date.createMask():_startDateMask));
            getDataMap().put("startDate", _startDateMask.getDataMap());
            return this;
        }

        /**
         * start date of the date range, with the start date included in the range.
         * 
         */
        public AbsoluteDateRange.ProjectionMask withStartDate() {
            _startDateMask = null;
            getDataMap().put("startDate", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * end date of the date range, with the end date included in the range.
         * 
         */
        public AbsoluteDateRange.ProjectionMask withEndDate(Function<com.linkedin.feathr.config.join.Date.ProjectionMask, com.linkedin.feathr.config.join.Date.ProjectionMask> nestedMask) {
            _endDateMask = nestedMask.apply(((_endDateMask == null)?Date.createMask():_endDateMask));
            getDataMap().put("endDate", _endDateMask.getDataMap());
            return this;
        }

        /**
         * end date of the date range, with the end date included in the range.
         * 
         */
        public AbsoluteDateRange.ProjectionMask withEndDate() {
            _endDateMask = null;
            getDataMap().put("endDate", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
