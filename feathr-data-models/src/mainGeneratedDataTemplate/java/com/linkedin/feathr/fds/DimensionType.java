
package com.linkedin.feathr.fds;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of
 * that dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,
 * elements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2]["Germany"]).
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/DimensionType.pdl.")
public enum DimensionType {


    /**
     * The tensor dimension is of type integer.
     * 
     */
    INT,

    /**
     * The tensor dimension is of type long.
     * 
     */
    LONG,

    /**
     * The tensor dimension is of type string.
     * 
     */
    STRING,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Allowable types for dimensions of a tensors stored in FDS. The type determines the acceptable values for indices of\nthat dimension. For example, if for a two-dimesional tensor, the first dimension is INT and the second is STRING,\nelements of this tensor can be referenced with an integer and a string (e.g., my_tensor[2][\"Germany\"]).*/enum DimensionType{/**The tensor dimension is of type integer.*/INT/**The tensor dimension is of type long.*/LONG/**The tensor dimension is of type string.*/STRING}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
