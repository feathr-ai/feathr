
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and Frame.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/ValueType.pdl.")
public enum ValueType {


    /**
     *  Integer. 
     * 
     */
    INT,

    /**
     *  Long. 
     * 
     */
    LONG,

    /**
     *  Float. 
     * 
     */
    FLOAT,

    /**
     *  Double. 
     * 
     */
    DOUBLE,

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

    /**
     *  Byte array. 
     * 
     */
    BYTES,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Tensor is used to represent feature data. A tensor is a generalization of vectors and matrices to potentially higher dimensions. In Quince Tensor specifically, the last column is designated as the value, and the rest of the columns are keys (or dimensions); Each row defines a single key/value pair. This enum defines supported value types for tensors in Quince and Frame.*/enum ValueType{/** Integer. */INT/** Long. */LONG/** Float. */FLOAT/** Double. */DOUBLE/** String. */STRING/** Boolean. */BOOLEAN/** Byte array. */BYTES}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
