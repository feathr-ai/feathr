
package com.linkedin.feathr.fds;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * The value type for a feature (column) of FDS.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/ValueType.pdl.")
public enum ValueType {


    /**
     * The value is an integer.
     * 
     */
    INT,

    /**
     * The value is a long.
     * 
     */
    LONG,

    /**
     * The value is a float.
     * 
     */
    FLOAT,

    /**
     * The value is a double.
     * 
     */
    DOUBLE,

    /**
     * The value is a boolean.
     * 
     */
    BOOLEAN,

    /**
     * The value is a string.
     * 
     */
    STRING,

    /**
     * The value is a byte-array.
     * 
     */
    BYTES,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**The value type for a feature (column) of FDS.*/enum ValueType{/**The value is an integer.*/INT/**The value is a long.*/LONG/**The value is a float.*/FLOAT/**The value is a double.*/DOUBLE/**The value is a boolean.*/BOOLEAN/**The value is a string.*/STRING/**The value is a byte-array.*/BYTES}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
