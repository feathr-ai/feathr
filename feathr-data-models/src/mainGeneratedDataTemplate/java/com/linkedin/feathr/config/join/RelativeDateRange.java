
package com.linkedin.feathr.config.join;

import java.util.List;
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
 * The date range represented relative to the current date. It uses the current system date as the reference and can be used to
 * express a range of dates with respect to the current date.
 * Example, - If current date is 01/01/2020, window is 3, and offset 1 (unit is number of days)
 * then this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.
 * 
 * If dateOffset is not specified, it defaults to 0.
 * relativeDateRange: RelativeDateRange(numDays=2, dateOffset=1)
 * relativeDateRange: RelativeDateRange(numDays=5)
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/config/join/RelativeDateRange.pdl.")
public class RelativeDateRange
    extends RecordTemplate
{

    private final static RelativeDateRange.Fields _fields = new RelativeDateRange.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The date range represented relative to the current date. It uses the current system date as the reference and can be used to\nexpress a range of dates with respect to the current date.\nExample, - If current date is 01/01/2020, window is 3, and offset 1 (unit is number of days)\nthen this corresponds to the following 3 days, ie- starting from (current date - offset), ie - 12/31/2019, 12/30/2019 and 12/29/2019.\n\nIf dateOffset is not specified, it defaults to 0.\nrelativeDateRange: RelativeDateRange(numDays=2, dateOffset=1)\nrelativeDateRange: RelativeDateRange(numDays=5)*/record RelativeDateRange{/**Represents a length of time.\nnumDays is the window from the reference date to look back to obtain a dateRange.\nFor example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020\ntill 11/05/2020.*/@validate.positive={}numDays:long/**Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date\nwill be 4 days ago from today.*/dateOffset:long=0}", SchemaFormatType.PDL));
    private Long _numDaysField = null;
    private Long _dateOffsetField = null;
    private RelativeDateRange.ChangeListener __changeListener = new RelativeDateRange.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_NumDays = SCHEMA.getField("numDays");
    private final static RecordDataSchema.Field FIELD_DateOffset = SCHEMA.getField("dateOffset");
    private final static Long DEFAULT_DateOffset;

    static {
        DEFAULT_DateOffset = DataTemplateUtil.coerceLongOutput(FIELD_DateOffset.getDefault());
    }

    public RelativeDateRange() {
        super(new DataMap(3, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public RelativeDateRange(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static RelativeDateRange.Fields fields() {
        return _fields;
    }

    public static RelativeDateRange.ProjectionMask createMask() {
        return new RelativeDateRange.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for numDays
     * 
     * @see RelativeDateRange.Fields#numDays
     */
    public boolean hasNumDays() {
        if (_numDaysField!= null) {
            return true;
        }
        return super._map.containsKey("numDays");
    }

    /**
     * Remover for numDays
     * 
     * @see RelativeDateRange.Fields#numDays
     */
    public void removeNumDays() {
        super._map.remove("numDays");
    }

    /**
     * Getter for numDays
     * 
     * @see RelativeDateRange.Fields#numDays
     */
    public Long getNumDays(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getNumDays();
            case DEFAULT:
            case NULL:
                if (_numDaysField!= null) {
                    return _numDaysField;
                } else {
                    Object __rawValue = super._map.get("numDays");
                    _numDaysField = DataTemplateUtil.coerceLongOutput(__rawValue);
                    return _numDaysField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for numDays
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see RelativeDateRange.Fields#numDays
     */
    @Nonnull
    public Long getNumDays() {
        if (_numDaysField!= null) {
            return _numDaysField;
        } else {
            Object __rawValue = super._map.get("numDays");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("numDays");
            }
            _numDaysField = DataTemplateUtil.coerceLongOutput(__rawValue);
            return _numDaysField;
        }
    }

    /**
     * Setter for numDays
     * 
     * @see RelativeDateRange.Fields#numDays
     */
    public RelativeDateRange setNumDays(Long value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setNumDays(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field numDays of com.linkedin.feathr.config.join.RelativeDateRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "numDays", DataTemplateUtil.coerceLongInput(value));
                    _numDaysField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeNumDays();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "numDays", DataTemplateUtil.coerceLongInput(value));
                    _numDaysField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "numDays", DataTemplateUtil.coerceLongInput(value));
                    _numDaysField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for numDays
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RelativeDateRange.Fields#numDays
     */
    public RelativeDateRange setNumDays(
        @Nonnull
        Long value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field numDays of com.linkedin.feathr.config.join.RelativeDateRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "numDays", DataTemplateUtil.coerceLongInput(value));
            _numDaysField = value;
        }
        return this;
    }

    /**
     * Setter for numDays
     * 
     * @see RelativeDateRange.Fields#numDays
     */
    public RelativeDateRange setNumDays(long value) {
        CheckedUtil.putWithoutChecking(super._map, "numDays", DataTemplateUtil.coerceLongInput(value));
        _numDaysField = value;
        return this;
    }

    /**
     * Existence checker for dateOffset
     * 
     * @see RelativeDateRange.Fields#dateOffset
     */
    public boolean hasDateOffset() {
        if (_dateOffsetField!= null) {
            return true;
        }
        return super._map.containsKey("dateOffset");
    }

    /**
     * Remover for dateOffset
     * 
     * @see RelativeDateRange.Fields#dateOffset
     */
    public void removeDateOffset() {
        super._map.remove("dateOffset");
    }

    /**
     * Getter for dateOffset
     * 
     * @see RelativeDateRange.Fields#dateOffset
     */
    public Long getDateOffset(GetMode mode) {
        switch (mode) {
            case STRICT:
            case DEFAULT:
                return getDateOffset();
            case NULL:
                if (_dateOffsetField!= null) {
                    return _dateOffsetField;
                } else {
                    Object __rawValue = super._map.get("dateOffset");
                    _dateOffsetField = DataTemplateUtil.coerceLongOutput(__rawValue);
                    return _dateOffsetField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for dateOffset
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see RelativeDateRange.Fields#dateOffset
     */
    @Nonnull
    public Long getDateOffset() {
        if (_dateOffsetField!= null) {
            return _dateOffsetField;
        } else {
            Object __rawValue = super._map.get("dateOffset");
            if (__rawValue == null) {
                return DEFAULT_DateOffset;
            }
            _dateOffsetField = DataTemplateUtil.coerceLongOutput(__rawValue);
            return _dateOffsetField;
        }
    }

    /**
     * Setter for dateOffset
     * 
     * @see RelativeDateRange.Fields#dateOffset
     */
    public RelativeDateRange setDateOffset(Long value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setDateOffset(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field dateOffset of com.linkedin.feathr.config.join.RelativeDateRange");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dateOffset", DataTemplateUtil.coerceLongInput(value));
                    _dateOffsetField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeDateOffset();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "dateOffset", DataTemplateUtil.coerceLongInput(value));
                    _dateOffsetField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "dateOffset", DataTemplateUtil.coerceLongInput(value));
                    _dateOffsetField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for dateOffset
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see RelativeDateRange.Fields#dateOffset
     */
    public RelativeDateRange setDateOffset(
        @Nonnull
        Long value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field dateOffset of com.linkedin.feathr.config.join.RelativeDateRange to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "dateOffset", DataTemplateUtil.coerceLongInput(value));
            _dateOffsetField = value;
        }
        return this;
    }

    /**
     * Setter for dateOffset
     * 
     * @see RelativeDateRange.Fields#dateOffset
     */
    public RelativeDateRange setDateOffset(long value) {
        CheckedUtil.putWithoutChecking(super._map, "dateOffset", DataTemplateUtil.coerceLongInput(value));
        _dateOffsetField = value;
        return this;
    }

    @Override
    public RelativeDateRange clone()
        throws CloneNotSupportedException
    {
        RelativeDateRange __clone = ((RelativeDateRange) super.clone());
        __clone.__changeListener = new RelativeDateRange.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public RelativeDateRange copy()
        throws CloneNotSupportedException
    {
        RelativeDateRange __copy = ((RelativeDateRange) super.copy());
        __copy._numDaysField = null;
        __copy._dateOffsetField = null;
        __copy.__changeListener = new RelativeDateRange.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final RelativeDateRange __objectRef;

        private ChangeListener(RelativeDateRange reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "numDays":
                    __objectRef._numDaysField = null;
                    break;
                case "dateOffset":
                    __objectRef._dateOffsetField = null;
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
         * Represents a length of time.
         * numDays is the window from the reference date to look back to obtain a dateRange.
         * For example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020
         * till 11/05/2020.
         * 
         */
        public PathSpec numDays() {
            return new PathSpec(getPathComponents(), "numDays");
        }

        /**
         * Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date
         * will be 4 days ago from today.
         * 
         */
        public PathSpec dateOffset() {
            return new PathSpec(getPathComponents(), "dateOffset");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Represents a length of time.
         * numDays is the window from the reference date to look back to obtain a dateRange.
         * For example, numDays - 5 implies, if reference date is 11/09/2020, then numDays will range from 11/09/2020
         * till 11/05/2020.
         * 
         */
        public RelativeDateRange.ProjectionMask withNumDays() {
            getDataMap().put("numDays", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Number of days to backdate from current date, to obtain the reference date. For example, if dateOffset is 4, then reference date
         * will be 4 days ago from today.
         * 
         */
        public RelativeDateRange.ProjectionMask withDateOffset() {
            getDataMap().put("dateOffset", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
