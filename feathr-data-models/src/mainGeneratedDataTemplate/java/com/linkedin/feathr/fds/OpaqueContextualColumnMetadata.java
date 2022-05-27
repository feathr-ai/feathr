
package com.linkedin.feathr.fds;

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
 * OpaqueContextualColumnMetadata structure capturing metadata of columns that contain
 * Opaque Contextual Data.
 * <p/>
 * More details can be found in the specification at docs/qt_fds_spec.md
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/OpaqueContextualColumnMetadata.pdl.")
public class OpaqueContextualColumnMetadata
    extends RecordTemplate
{

    private final static OpaqueContextualColumnMetadata.Fields _fields = new OpaqueContextualColumnMetadata.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**OpaqueContextualColumnMetadata structure capturing metadata of columns that contain\nOpaque Contextual Data.\n<p/>\nMore details can be found in the specification at docs/qt_fds_spec.md*/record OpaqueContextualColumnMetadata{}", SchemaFormatType.PDL));

    public OpaqueContextualColumnMetadata() {
        super(new DataMap(0, 0.75F), SCHEMA);
    }

    public OpaqueContextualColumnMetadata(DataMap data) {
        super(data, SCHEMA);
    }

    public static OpaqueContextualColumnMetadata.Fields fields() {
        return _fields;
    }

    public static OpaqueContextualColumnMetadata.ProjectionMask createMask() {
        return new OpaqueContextualColumnMetadata.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    @Override
    public OpaqueContextualColumnMetadata clone()
        throws CloneNotSupportedException
    {
        OpaqueContextualColumnMetadata __clone = ((OpaqueContextualColumnMetadata) super.clone());
        return __clone;
    }

    @Override
    public OpaqueContextualColumnMetadata copy()
        throws CloneNotSupportedException
    {
        OpaqueContextualColumnMetadata __copy = ((OpaqueContextualColumnMetadata) super.copy());
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
