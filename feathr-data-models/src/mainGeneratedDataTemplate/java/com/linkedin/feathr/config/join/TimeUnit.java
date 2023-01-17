
package com.linkedin.feathr.config.join;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Unit of time used for defining a time range.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\config\\join\\TimeUnit.pdl.")
public enum TimeUnit {


    /**
     * Daily format
     * 
     */
    DAY,

    /**
     * Hourly format
     * 
     */
    HOUR,

    /**
     * minute format, this can be used in simulate time delay
     * 
     */
    MINUTE,

    /**
     * second format, this can be used in simulate time delay
     * 
     */
    SECOND,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.config.join/**Unit of time used for defining a time range.*/enum TimeUnit{/**Daily format*/DAY/**Hourly format*/HOUR/**minute format, this can be used in simulate time delay*/MINUTE/**second format, this can be used in simulate time delay*/SECOND}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
