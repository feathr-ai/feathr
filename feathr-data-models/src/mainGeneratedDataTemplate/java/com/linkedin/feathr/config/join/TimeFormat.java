
package com.linkedin.feathr.config.join;

import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;


/**
 * The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have
 * the option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\TimeFormat.pdl.")
public class TimeFormat
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**The timeformat, which accepts the formats parsed by the DateTimeFormatter java class or epoch or epoch_millis. However in future, we can have\r\nthe option of a stronger type. Example, dd/MM/yyyy, yyyy-MM-dd, epoch, epoch_millis, etc.*/typeref TimeFormat=string", SchemaFormatType.PDL));

    public TimeFormat() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
