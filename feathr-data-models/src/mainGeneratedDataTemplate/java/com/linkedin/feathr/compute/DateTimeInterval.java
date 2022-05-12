
package com.linkedin.feathr.compute;

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
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/DateTimeInterval.pdl.")
public class DateTimeInterval
    extends RecordTemplate
{

    private final static DateTimeInterval.Fields _fields = new DateTimeInterval.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,record DateTimeInterval{/**Represents the inclusive (greater than or equal to) value in which to start the range. This field is optional. An unset field here indicates an open range; for example, if end is 1455309628000 (Fri, 12 Feb 2016 20:40:28 GMT), and start is not set, it would indicate times up to, but excluding, 1455309628000.  Note that this interpretation was not originally documented. New uses of this model should follow this interpretation, but older models may not, and their documentation should reflect this fact.*/start:optional/**Number of milliseconds since midnight, January 1, 1970 UTC. It must be a positive number*/@compliance=\"NONE\"typeref Time=long/**Represents the exclusive (strictly less than) value in which to end the range. This field is optional. An unset field here indicates an open range; for example, if start is 1455309628000 (Fri, 12 Feb 2016 20:40:28 GMT), and end is not set, it would mean everything at, or after, 1455309628000.  New uses of this model should follow this interpretation, but older models may not, and their documentation should reflect this fact.*/end:optional Time}", SchemaFormatType.PDL));
    private Long _startField = null;
    private Long _endField = null;
    private DateTimeInterval.ChangeListener __changeListener = new DateTimeInterval.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Start = SCHEMA.getField("start");
    private final static RecordDataSchema.Field FIELD_End = SCHEMA.getField("end");

    public DateTimeInterval() {
        super(new DataMap(3, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public DateTimeInterval(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static DateTimeInterval.Fields fields() {
        return _fields;
    }

    public static DateTimeInterval.ProjectionMask createMask() {
        return new DateTimeInterval.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for start
     * 
     * @see DateTimeInterval.Fields#start
     */
    public boolean hasStart() {
        if (_startField!= null) {
            return true;
        }
        return super._map.containsKey("start");
    }

    /**
     * Remover for start
     * 
     * @see DateTimeInterval.Fields#start
     */
    public void removeStart() {
        super._map.remove("start");
    }

    /**
     * Getter for start
     * 
     * @see DateTimeInterval.Fields#start
     */
    public Long getStart(GetMode mode) {
        return getStart();
    }

    /**
     * Getter for start
     * 
     * @return
     *     Optional field. Always check for null.
     * @see DateTimeInterval.Fields#start
     */
    @Nullable
    public Long getStart() {
        if (_startField!= null) {
            return _startField;
        } else {
            Object __rawValue = super._map.get("start");
            _startField = DataTemplateUtil.coerceLongOutput(__rawValue);
            return _startField;
        }
    }

    /**
     * Setter for start
     * 
     * @see DateTimeInterval.Fields#start
     */
    public DateTimeInterval setStart(Long value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setStart(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeStart();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "start", DataTemplateUtil.coerceLongInput(value));
                    _startField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "start", DataTemplateUtil.coerceLongInput(value));
                    _startField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for start
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DateTimeInterval.Fields#start
     */
    public DateTimeInterval setStart(
        @Nonnull
        Long value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field start of com.linkedin.feathr.compute.DateTimeInterval to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "start", DataTemplateUtil.coerceLongInput(value));
            _startField = value;
        }
        return this;
    }

    /**
     * Setter for start
     * 
     * @see DateTimeInterval.Fields#start
     */
    public DateTimeInterval setStart(long value) {
        CheckedUtil.putWithoutChecking(super._map, "start", DataTemplateUtil.coerceLongInput(value));
        _startField = value;
        return this;
    }

    /**
     * Existence checker for end
     * 
     * @see DateTimeInterval.Fields#end
     */
    public boolean hasEnd() {
        if (_endField!= null) {
            return true;
        }
        return super._map.containsKey("end");
    }

    /**
     * Remover for end
     * 
     * @see DateTimeInterval.Fields#end
     */
    public void removeEnd() {
        super._map.remove("end");
    }

    /**
     * Getter for end
     * 
     * @see DateTimeInterval.Fields#end
     */
    public Long getEnd(GetMode mode) {
        return getEnd();
    }

    /**
     * Getter for end
     * 
     * @return
     *     Optional field. Always check for null.
     * @see DateTimeInterval.Fields#end
     */
    @Nullable
    public Long getEnd() {
        if (_endField!= null) {
            return _endField;
        } else {
            Object __rawValue = super._map.get("end");
            _endField = DataTemplateUtil.coerceLongOutput(__rawValue);
            return _endField;
        }
    }

    /**
     * Setter for end
     * 
     * @see DateTimeInterval.Fields#end
     */
    public DateTimeInterval setEnd(Long value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setEnd(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeEnd();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "end", DataTemplateUtil.coerceLongInput(value));
                    _endField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "end", DataTemplateUtil.coerceLongInput(value));
                    _endField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for end
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see DateTimeInterval.Fields#end
     */
    public DateTimeInterval setEnd(
        @Nonnull
        Long value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field end of com.linkedin.feathr.compute.DateTimeInterval to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "end", DataTemplateUtil.coerceLongInput(value));
            _endField = value;
        }
        return this;
    }

    /**
     * Setter for end
     * 
     * @see DateTimeInterval.Fields#end
     */
    public DateTimeInterval setEnd(long value) {
        CheckedUtil.putWithoutChecking(super._map, "end", DataTemplateUtil.coerceLongInput(value));
        _endField = value;
        return this;
    }

    @Override
    public DateTimeInterval clone()
        throws CloneNotSupportedException
    {
        DateTimeInterval __clone = ((DateTimeInterval) super.clone());
        __clone.__changeListener = new DateTimeInterval.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public DateTimeInterval copy()
        throws CloneNotSupportedException
    {
        DateTimeInterval __copy = ((DateTimeInterval) super.copy());
        __copy._startField = null;
        __copy._endField = null;
        __copy.__changeListener = new DateTimeInterval.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final DateTimeInterval __objectRef;

        private ChangeListener(DateTimeInterval reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "start":
                    __objectRef._startField = null;
                    break;
                case "end":
                    __objectRef._endField = null;
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
         * Represents the inclusive (greater than or equal to) value in which to start the range. This field is optional. An unset field here indicates an open range; for example, if end is 1455309628000 (Fri, 12 Feb 2016 20:40:28 GMT), and start is not set, it would indicate times up to, but excluding, 1455309628000.  Note that this interpretation was not originally documented. New uses of this model should follow this interpretation, but older models may not, and their documentation should reflect this fact.
         * 
         */
        public PathSpec start() {
            return new PathSpec(getPathComponents(), "start");
        }

        /**
         * Represents the exclusive (strictly less than) value in which to end the range. This field is optional. An unset field here indicates an open range; for example, if start is 1455309628000 (Fri, 12 Feb 2016 20:40:28 GMT), and end is not set, it would mean everything at, or after, 1455309628000.  New uses of this model should follow this interpretation, but older models may not, and their documentation should reflect this fact.
         * 
         */
        public PathSpec end() {
            return new PathSpec(getPathComponents(), "end");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(3);
        }

        /**
         * Represents the inclusive (greater than or equal to) value in which to start the range. This field is optional. An unset field here indicates an open range; for example, if end is 1455309628000 (Fri, 12 Feb 2016 20:40:28 GMT), and start is not set, it would indicate times up to, but excluding, 1455309628000.  Note that this interpretation was not originally documented. New uses of this model should follow this interpretation, but older models may not, and their documentation should reflect this fact.
         * 
         */
        public DateTimeInterval.ProjectionMask withStart() {
            getDataMap().put("start", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * Represents the exclusive (strictly less than) value in which to end the range. This field is optional. An unset field here indicates an open range; for example, if start is 1455309628000 (Fri, 12 Feb 2016 20:40:28 GMT), and end is not set, it would mean everything at, or after, 1455309628000.  New uses of this model should follow this interpretation, but older models may not, and their documentation should reflect this fact.
         * 
         */
        public DateTimeInterval.ProjectionMask withEnd() {
            getDataMap().put("end", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
