
package com.linkedin.feathr.featureDataModel;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * TimestampGranularity is to represent the granularity of a timestamp.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/TimestampGranularity.pdl.")
public enum TimestampGranularity {


    /**
     * Indicates the timestamp is represented in seconds.
     * 
     */
    SECONDS,

    /**
     * Indicates the timestamp is represented in milliseconds.
     * 
     */
    MILLISECONDS,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**TimestampGranularity is to represent the granularity of a timestamp.*/enum TimestampGranularity{/**Indicates the timestamp is represented in seconds.*/SECONDS/**Indicates the timestamp is represented in milliseconds.*/MILLISECONDS}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
