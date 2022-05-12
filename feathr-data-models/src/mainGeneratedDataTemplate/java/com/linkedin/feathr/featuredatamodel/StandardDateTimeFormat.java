
package com.linkedin.feathr.featureDataModel;

import java.util.List;
import javax.annotation.Generated;
import com.linkedin.data.DataMap;
import com.linkedin.data.schema.MaskMap;
import com.linkedin.data.schema.PathSpec;
import com.linkedin.data.schema.RecordDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.RecordTemplate;


/**
 * Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of "Unification of SWA and time-based features" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/StandardDateTimeFormat.pdl.")
public class StandardDateTimeFormat
    extends RecordTemplate
{

    private final static StandardDateTimeFormat.Fields _fields = new StandardDateTimeFormat.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents the standard DateTimeFormat used across LinkedIn, ie. yyyy/MM/dd for daily datasets and yyyy/MM/dd/hh for hourly datasets. The convention is widely adopted, for example, storing tracking data under /data/tracking/ on HDFS. TODO(PROML-12707): This data model is expected to be deprecated when the dev work and migration of \"Unification of SWA and time-based features\" is completed (RFC: https://docs.google.com/document/d/1C6u2CKWSmOmHDQEL8Ovm5V5ZZFKhC_HdxVxU9D1F9lg/edit#).*/record StandardDateTimeFormat{}", SchemaFormatType.PDL));

    public StandardDateTimeFormat() {
        super(new DataMap(0, 0.75F), SCHEMA);
    }

    public StandardDateTimeFormat(DataMap data) {
        super(data, SCHEMA);
    }

    public static StandardDateTimeFormat.Fields fields() {
        return _fields;
    }

    public static StandardDateTimeFormat.ProjectionMask createMask() {
        return new StandardDateTimeFormat.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    @Override
    public StandardDateTimeFormat clone()
        throws CloneNotSupportedException
    {
        StandardDateTimeFormat __clone = ((StandardDateTimeFormat) super.clone());
        return __clone;
    }

    @Override
    public StandardDateTimeFormat copy()
        throws CloneNotSupportedException
    {
        StandardDateTimeFormat __copy = ((StandardDateTimeFormat) super.copy());
        return __copy;
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

    }

    public static class ProjectionMask
        extends MaskMap
    {


        ProjectionMask() {
            super(0);
        }

    }

}
