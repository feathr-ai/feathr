
package com.linkedin.feathr.config.join;

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
import com.linkedin.data.template.RequiredFieldNotPresentException;
import com.linkedin.data.template.SetMode;


/**
 * Settings needed when the input data has a timestamp which should be used for the join.
 * joinTimeSettings: {
 *    timestampColumn: {
 *       def: timestamp
 *       format: yyyy/MM/dd
 *    }
 *    simulateTimeDelay: 1d
 * }
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\TimestampColJoinTimeSettings.pdl.")
public class TimestampColJoinTimeSettings
    extends RecordTemplate
{

    private final static TimestampColJoinTimeSettings.Fields _fields = new TimestampColJoinTimeSettings.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**Settings needed when the input data has a timestamp which should be used for the join.\r\njoinTimeSettings: {\r\n   timestampColumn: {\r\n      def: timestamp\r\n      format: yyyy/MM/dd\r\n   }\r\n   simulateTimeDelay: 1d\r\n}*/record TimestampColJoinTimeSettings{/**The timestamp column name and timeformat which should be used for joining with the feature data.\r\nRefer to [[TimestampColumn]].\r\nExample, TimestampColumn: {\r\n           def: timestamp\r\n           format: yyyy/MM/dd\r\n         }*/timestampColumn:/**Timestamp column of the input featureiized dataset, which is to be used for the join.\r\ntimestampColumn: {\r\n   def: timestamp\r\n   format: yyyyMMdd\r\n }*/record TimestampColumn{/**The definiton of the timestamp column, which can be a sql expression involving the timestamp column\r\nor just the column name\r\nExample:- definition: timestamp, timestamp + 10000000.*/definition:union[columnName:string,sparkSqlExpression:/**An expression in Spark SQL.*/record SparkSqlExpression{/**The Spark SQL expression.*/expression:string}]/**Format of the timestamp column. Must confer to java's timestampFormatter or can be\r\nepoch or epoch_millis.\r\nExample:- epoch, epoch_millis, yyyy/MM/dd*/format:/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\r\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string}/**An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted\r\nfrom the input data timestamp while joining with the feature data.\r\nWe do support negative time delays.*/simulateTimeDelay:optional/**TimeOffset is the amount of time we need to push back the current time wrt a reference time. Since, reference time can\r\nbe any time in the past also, we do allow a positive or negative offset length.\r\n offset - 1 day implies the previous from the reference day.*/record TimeOffset{/**Amount of the duration in TimeUnits. Can be positive or negative.*/length:long/**Time unit for \"length\". For example, TimeUnit.DAY or TimeUnit.HOUR.*/unit:/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}}}", SchemaFormatType.PDL));
    private TimestampColumn _timestampColumnField = null;
    private TimeOffset _simulateTimeDelayField = null;
    private TimestampColJoinTimeSettings.ChangeListener __changeListener = new TimestampColJoinTimeSettings.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_TimestampColumn = SCHEMA.getField("timestampColumn");
    private final static RecordDataSchema.Field FIELD_SimulateTimeDelay = SCHEMA.getField("simulateTimeDelay");

    public TimestampColJoinTimeSettings() {
        super(new DataMap(3, 0.75F), SCHEMA, 3);
        addChangeListener(__changeListener);
    }

    public TimestampColJoinTimeSettings(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static TimestampColJoinTimeSettings.Fields fields() {
        return _fields;
    }

    public static TimestampColJoinTimeSettings.ProjectionMask createMask() {
        return new TimestampColJoinTimeSettings.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for timestampColumn
     * 
     * @see TimestampColJoinTimeSettings.Fields#timestampColumn
     */
    public boolean hasTimestampColumn() {
        if (_timestampColumnField!= null) {
            return true;
        }
        return super._map.containsKey("timestampColumn");
    }

    /**
     * Remover for timestampColumn
     * 
     * @see TimestampColJoinTimeSettings.Fields#timestampColumn
     */
    public void removeTimestampColumn() {
        super._map.remove("timestampColumn");
    }

    /**
     * Getter for timestampColumn
     * 
     * @see TimestampColJoinTimeSettings.Fields#timestampColumn
     */
    public TimestampColumn getTimestampColumn(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getTimestampColumn();
            case DEFAULT:
            case NULL:
                if (_timestampColumnField!= null) {
                    return _timestampColumnField;
                } else {
                    Object __rawValue = super._map.get("timestampColumn");
                    _timestampColumnField = ((__rawValue == null)?null:new TimestampColumn(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
                    return _timestampColumnField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for timestampColumn
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see TimestampColJoinTimeSettings.Fields#timestampColumn
     */
    @Nonnull
    public TimestampColumn getTimestampColumn() {
        if (_timestampColumnField!= null) {
            return _timestampColumnField;
        } else {
            Object __rawValue = super._map.get("timestampColumn");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("timestampColumn");
            }
            _timestampColumnField = ((__rawValue == null)?null:new TimestampColumn(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _timestampColumnField;
        }
    }

    /**
     * Setter for timestampColumn
     * 
     * @see TimestampColJoinTimeSettings.Fields#timestampColumn
     */
    public TimestampColJoinTimeSettings setTimestampColumn(TimestampColumn value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setTimestampColumn(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field timestampColumn of com.linkedin.feathr.config.join.TimestampColJoinTimeSettings");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "timestampColumn", value.data());
                    _timestampColumnField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeTimestampColumn();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "timestampColumn", value.data());
                    _timestampColumnField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "timestampColumn", value.data());
                    _timestampColumnField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for timestampColumn
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimestampColJoinTimeSettings.Fields#timestampColumn
     */
    public TimestampColJoinTimeSettings setTimestampColumn(
        @Nonnull
        TimestampColumn value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field timestampColumn of com.linkedin.feathr.config.join.TimestampColJoinTimeSettings to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "timestampColumn", value.data());
            _timestampColumnField = value;
        }
        return this;
    }

    /**
     * Existence checker for simulateTimeDelay
     * 
     * @see TimestampColJoinTimeSettings.Fields#simulateTimeDelay
     */
    public boolean hasSimulateTimeDelay() {
        if (_simulateTimeDelayField!= null) {
            return true;
        }
        return super._map.containsKey("simulateTimeDelay");
    }

    /**
     * Remover for simulateTimeDelay
     * 
     * @see TimestampColJoinTimeSettings.Fields#simulateTimeDelay
     */
    public void removeSimulateTimeDelay() {
        super._map.remove("simulateTimeDelay");
    }

    /**
     * Getter for simulateTimeDelay
     * 
     * @see TimestampColJoinTimeSettings.Fields#simulateTimeDelay
     */
    public TimeOffset getSimulateTimeDelay(GetMode mode) {
        return getSimulateTimeDelay();
    }

    /**
     * Getter for simulateTimeDelay
     * 
     * @return
     *     Optional field. Always check for null.
     * @see TimestampColJoinTimeSettings.Fields#simulateTimeDelay
     */
    @Nullable
    public TimeOffset getSimulateTimeDelay() {
        if (_simulateTimeDelayField!= null) {
            return _simulateTimeDelayField;
        } else {
            Object __rawValue = super._map.get("simulateTimeDelay");
            _simulateTimeDelayField = ((__rawValue == null)?null:new TimeOffset(DataTemplateUtil.castOrThrow(__rawValue, DataMap.class)));
            return _simulateTimeDelayField;
        }
    }

    /**
     * Setter for simulateTimeDelay
     * 
     * @see TimestampColJoinTimeSettings.Fields#simulateTimeDelay
     */
    public TimestampColJoinTimeSettings setSimulateTimeDelay(TimeOffset value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setSimulateTimeDelay(value);
            case REMOVE_OPTIONAL_IF_NULL:
            case REMOVE_IF_NULL:
                if (value == null) {
                    removeSimulateTimeDelay();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "simulateTimeDelay", value.data());
                    _simulateTimeDelayField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "simulateTimeDelay", value.data());
                    _simulateTimeDelayField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for simulateTimeDelay
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see TimestampColJoinTimeSettings.Fields#simulateTimeDelay
     */
    public TimestampColJoinTimeSettings setSimulateTimeDelay(
        @Nonnull
        TimeOffset value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field simulateTimeDelay of com.linkedin.feathr.config.join.TimestampColJoinTimeSettings to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "simulateTimeDelay", value.data());
            _simulateTimeDelayField = value;
        }
        return this;
    }

    @Override
    public TimestampColJoinTimeSettings clone()
        throws CloneNotSupportedException
    {
        TimestampColJoinTimeSettings __clone = ((TimestampColJoinTimeSettings) super.clone());
        __clone.__changeListener = new TimestampColJoinTimeSettings.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public TimestampColJoinTimeSettings copy()
        throws CloneNotSupportedException
    {
        TimestampColJoinTimeSettings __copy = ((TimestampColJoinTimeSettings) super.copy());
        __copy._simulateTimeDelayField = null;
        __copy._timestampColumnField = null;
        __copy.__changeListener = new TimestampColJoinTimeSettings.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final TimestampColJoinTimeSettings __objectRef;

        private ChangeListener(TimestampColJoinTimeSettings reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "simulateTimeDelay":
                    __objectRef._simulateTimeDelayField = null;
                    break;
                case "timestampColumn":
                    __objectRef._timestampColumnField = null;
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
         * The timestamp column name and timeformat which should be used for joining with the feature data.
         * Refer to [[TimestampColumn]].
         * Example, TimestampColumn: {
         *            def: timestamp
         *            format: yyyy/MM/dd
         *          }
         * 
         */
        public com.linkedin.feathr.config.join.TimestampColumn.Fields timestampColumn() {
            return new com.linkedin.feathr.config.join.TimestampColumn.Fields(getPathComponents(), "timestampColumn");
        }

        /**
         * An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted
         * from the input data timestamp while joining with the feature data.
         * We do support negative time delays.
         * 
         */
        public com.linkedin.feathr.config.join.TimeOffset.Fields simulateTimeDelay() {
            return new com.linkedin.feathr.config.join.TimeOffset.Fields(getPathComponents(), "simulateTimeDelay");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {

        private com.linkedin.feathr.config.join.TimestampColumn.ProjectionMask _timestampColumnMask;
        private com.linkedin.feathr.config.join.TimeOffset.ProjectionMask _simulateTimeDelayMask;

        ProjectionMask() {
            super(3);
        }

        /**
         * The timestamp column name and timeformat which should be used for joining with the feature data.
         * Refer to [[TimestampColumn]].
         * Example, TimestampColumn: {
         *            def: timestamp
         *            format: yyyy/MM/dd
         *          }
         * 
         */
        public TimestampColJoinTimeSettings.ProjectionMask withTimestampColumn(Function<com.linkedin.feathr.config.join.TimestampColumn.ProjectionMask, com.linkedin.feathr.config.join.TimestampColumn.ProjectionMask> nestedMask) {
            _timestampColumnMask = nestedMask.apply(((_timestampColumnMask == null)?TimestampColumn.createMask():_timestampColumnMask));
            getDataMap().put("timestampColumn", _timestampColumnMask.getDataMap());
            return this;
        }

        /**
         * The timestamp column name and timeformat which should be used for joining with the feature data.
         * Refer to [[TimestampColumn]].
         * Example, TimestampColumn: {
         *            def: timestamp
         *            format: yyyy/MM/dd
         *          }
         * 
         */
        public TimestampColJoinTimeSettings.ProjectionMask withTimestampColumn() {
            _timestampColumnMask = null;
            getDataMap().put("timestampColumn", MaskMap.POSITIVE_MASK);
            return this;
        }

        /**
         * An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted
         * from the input data timestamp while joining with the feature data.
         * We do support negative time delays.
         * 
         */
        public TimestampColJoinTimeSettings.ProjectionMask withSimulateTimeDelay(Function<com.linkedin.feathr.config.join.TimeOffset.ProjectionMask, com.linkedin.feathr.config.join.TimeOffset.ProjectionMask> nestedMask) {
            _simulateTimeDelayMask = nestedMask.apply(((_simulateTimeDelayMask == null)?TimeOffset.createMask():_simulateTimeDelayMask));
            getDataMap().put("simulateTimeDelay", _simulateTimeDelayMask.getDataMap());
            return this;
        }

        /**
         * An optional simulate time delay parameter which can be set by the user. Indicates the amount of time that is to subtracted
         * from the input data timestamp while joining with the feature data.
         * We do support negative time delays.
         * 
         */
        public TimestampColJoinTimeSettings.ProjectionMask withSimulateTimeDelay() {
            _simulateTimeDelayMask = null;
            getDataMap().put("simulateTimeDelay", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
