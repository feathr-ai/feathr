
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
 * DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/UnspecifiedTransformationFunction.pdl.")
public class UnspecifiedTransformationFunction
    extends RecordTemplate
{

    private final static UnspecifiedTransformationFunction.Fields _fields = new UnspecifiedTransformationFunction.Fields();
    private final static RecordDataSchema SCHEMA = ((RecordDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**DO NOT USE!!! This was a placeholder to fill in the union of supported transformation functions for each anchor type. It was needed because the transformationFunction field should be a required top-level field but we were not ready to add concrete types into the union due to the ongoing Frame v2 effort. This record has been deprecated because concrete transformation functions have been filled into the transformationFunction field of each anchor.*/@Deprecated,record UnspecifiedTransformationFunction{}", SchemaFormatType.PDL));

    public UnspecifiedTransformationFunction() {
        super(new DataMap(0, 0.75F), SCHEMA);
    }

    public UnspecifiedTransformationFunction(DataMap data) {
        super(data, SCHEMA);
    }

    public static UnspecifiedTransformationFunction.Fields fields() {
        return _fields;
    }

    public static UnspecifiedTransformationFunction.ProjectionMask createMask() {
        return new UnspecifiedTransformationFunction.ProjectionMask();
    }

    public static RecordDataSchema dataSchema() {
        return SCHEMA;
    }

    @Override
    public UnspecifiedTransformationFunction clone()
        throws CloneNotSupportedException
    {
        UnspecifiedTransformationFunction __clone = ((UnspecifiedTransformationFunction) super.clone());
        return __clone;
    }

    @Override
    public UnspecifiedTransformationFunction copy()
        throws CloneNotSupportedException
    {
        UnspecifiedTransformationFunction __copy = ((UnspecifiedTransformationFunction) super.copy());
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
