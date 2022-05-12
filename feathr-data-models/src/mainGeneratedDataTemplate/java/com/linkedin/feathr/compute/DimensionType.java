
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Supported dimension types for tensors in Quince and Frame.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/DimensionType.pdl.")
public enum DimensionType {


    /**
     *  Long. 
     * 
     */
    LONG,

    /**
     *  Integer. 
     * 
     */
    INT,

    /**
     *  String. 
     * 
     */
    STRING,

    /**
     *  Boolean. 
     * 
     */
    BOOLEAN,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Supported dimension types for tensors in Quince and Frame.*/enum DimensionType{/** Long. */LONG/** Integer. */INT/** String. */STRING/** Boolean. */BOOLEAN}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
