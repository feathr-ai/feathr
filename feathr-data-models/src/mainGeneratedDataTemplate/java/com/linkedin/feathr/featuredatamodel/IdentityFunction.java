
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
 * Represents a transformation function that always returns the same value as the input. See https://en.wikipedia.org/wiki/Identity_function.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/IdentityFunction.pdl.")
public class IdentityFunction
    extends RecordTemplate
{

    private final static IdentityFunction.Fields _fields = new IdentityFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a transformation function that always returns the same value as the input. See https://en.wikipedia.org/wiki/Identity_function.*/record IdentityFunction{}", SchemaFormatType.PDL));

    public IdentityFunction() {
        super(new DataMap(0, 0.75F), SCHEMA);
    }

    public IdentityFunction(DataMap data) {
        super(data, SCHEMA);
    }

    public static IdentityFunction.Fields fields() {
        return _fields;
    }

    public static IdentityFunction.ProjectionMask createMask() {
        return new IdentityFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    @Override
    public IdentityFunction clone()
        throws CloneNotSupportedException
    {
        IdentityFunction __clone = ((IdentityFunction) super.clone());
        return __clone;
    }

    @Override
    public IdentityFunction copy()
        throws CloneNotSupportedException
    {
        IdentityFunction __copy = ((IdentityFunction) super.copy());
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
