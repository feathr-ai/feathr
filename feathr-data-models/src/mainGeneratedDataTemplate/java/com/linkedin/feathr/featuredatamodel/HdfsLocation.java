
package com.linkedin.feathr.featureDataModel;

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
 * A location on HDFS.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/HdfsLocation.pdl.")
public class HdfsLocation
    extends RecordTemplate
{

    private final static HdfsLocation.Fields _fields = new HdfsLocation.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**A location on HDFS.*/record HdfsLocation{/**Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/*/path:string}", SchemaFormatType.PDL));
    private String _pathField = null;
    private HdfsLocation.ChangeListener __changeListener = new HdfsLocation.ChangeListener(this);
    private final static RecordDataSchema.Field FIELD_Path = SCHEMA.getField("path");

    public HdfsLocation() {
        super(new DataMap(2, 0.75F), SCHEMA);
        addChangeListener(__changeListener);
    }

    public HdfsLocation(DataMap data) {
        super(data, SCHEMA);
        addChangeListener(__changeListener);
    }

    public static HdfsLocation.Fields fields() {
        return _fields;
    }

    public static HdfsLocation.ProjectionMask createMask() {
        return new HdfsLocation.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    /**
     * Existence checker for path
     * 
     * @see HdfsLocation.Fields#path
     */
    public boolean hasPath() {
        if (_pathField!= null) {
            return true;
        }
        return super._map.containsKey("path");
    }

    /**
     * Remover for path
     * 
     * @see HdfsLocation.Fields#path
     */
    public void removePath() {
        super._map.remove("path");
    }

    /**
     * Getter for path
     * 
     * @see HdfsLocation.Fields#path
     */
    public String getPath(GetMode mode) {
        switch (mode) {
            case STRICT:
                return getPath();
            case DEFAULT:
            case NULL:
                if (_pathField!= null) {
                    return _pathField;
                } else {
                    Object __rawValue = super._map.get("path");
                    _pathField = DataTemplateUtil.coerceStringOutput(__rawValue);
                    return _pathField;
                }
        }
        throw new IllegalStateException(("Unknown mode "+ mode));
    }

    /**
     * Getter for path
     * 
     * @return
     *     Required field. Could be null for partial record.
     * @see HdfsLocation.Fields#path
     */
    @Nonnull
    public String getPath() {
        if (_pathField!= null) {
            return _pathField;
        } else {
            Object __rawValue = super._map.get("path");
            if (__rawValue == null) {
                throw new RequiredFieldNotPresentException("path");
            }
            _pathField = DataTemplateUtil.coerceStringOutput(__rawValue);
            return _pathField;
        }
    }

    /**
     * Setter for path
     * 
     * @see HdfsLocation.Fields#path
     */
    public HdfsLocation setPath(String value, SetMode mode) {
        switch (mode) {
            case DISALLOW_NULL:
                return setPath(value);
            case REMOVE_OPTIONAL_IF_NULL:
                if (value == null) {
                    throw new IllegalArgumentException("Cannot remove mandatory field path of com.linkedin.feathr.featureDataModel.HdfsLocation");
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "path", value);
                    _pathField = value;
                }
                break;
            case REMOVE_IF_NULL:
                if (value == null) {
                    removePath();
                } else {
                    CheckedUtil.putWithoutChecking(super._map, "path", value);
                    _pathField = value;
                }
                break;
            case IGNORE_NULL:
                if (value!= null) {
                    CheckedUtil.putWithoutChecking(super._map, "path", value);
                    _pathField = value;
                }
                break;
        }
        return this;
    }

    /**
     * Setter for path
     * 
     * @param value
     *     Must not be null. For more control, use setters with mode instead.
     * @see HdfsLocation.Fields#path
     */
    public HdfsLocation setPath(
        @Nonnull
        String value) {
        if (value == null) {
            throw new NullPointerException("Cannot set field path of com.linkedin.feathr.featureDataModel.HdfsLocation to null");
        } else {
            CheckedUtil.putWithoutChecking(super._map, "path", value);
            _pathField = value;
        }
        return this;
    }

    @Override
    public HdfsLocation clone()
        throws CloneNotSupportedException
    {
        HdfsLocation __clone = ((HdfsLocation) super.clone());
        __clone.__changeListener = new HdfsLocation.ChangeListener(__clone);
        __clone.addChangeListener(__clone.__changeListener);
        return __clone;
    }

    @Override
    public HdfsLocation copy()
        throws CloneNotSupportedException
    {
        HdfsLocation __copy = ((HdfsLocation) super.copy());
        __copy._pathField = null;
        __copy.__changeListener = new HdfsLocation.ChangeListener(__copy);
        __copy.addChangeListener(__copy.__changeListener);
        return __copy;
    }

    private static class ChangeListener
        implements com.linkedin.data.collections.CheckedMap.ChangeListener<String, Object>
    {

        private final HdfsLocation __objectRef;

        private ChangeListener(HdfsLocation reference) {
            __objectRef = reference;
        }

        @Override
        public void onUnderlyingMapChanged(String key, Object value) {
            switch (key) {
                case "path":
                    __objectRef._pathField = null;
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
         * Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/
         * 
         */
        public PathSpec path() {
            return new PathSpec(getPathComponents(), "path");
        }

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(2);
        }

        /**
         * Path to the location on HDFS. It can be: 1. A file path, e.g. /user/test/file.avro, or 2. A directory, e.g. /user/test/files/
         * 
         */
        public HdfsLocation.ProjectionMask withPath() {
            getDataMap().put("path", MaskMap.POSITIVE_MASK);
            return this;
        }

    }

}
