
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;


/**
 * Number of milliseconds since midnight, January 1, 1970 UTC. It must be a positive number
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/Time.pdl.")
public class Time
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Number of milliseconds since midnight, January 1, 1970 UTC. It must be a positive number*/@compliance=\"NONE\"typeref Time=long", SchemaFormatType.PDL));

    public Time() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
