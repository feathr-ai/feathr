
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/OperatorId.pdl.")
public class OperatorId
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,typeref OperatorId=string", SchemaFormatType.PDL));

    public OperatorId() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
