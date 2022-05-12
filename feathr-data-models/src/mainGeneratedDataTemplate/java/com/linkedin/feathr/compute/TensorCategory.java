
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Supported Tensor categories in Frame and Quince.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/TensorCategory.pdl.")
public enum TensorCategory {


    /**
     * Dense tensors store values in a contiguous sequential block of memory where all values are represented.
     * 
     */
    DENSE,

    /**
     * Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.
     * 
     */
    SPARSE,

    /**
     * Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.
     * 
     */
    RAGGED,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Supported Tensor categories in Frame and Quince.*/enum TensorCategory{/**Dense tensors store values in a contiguous sequential block of memory where all values are represented.*/DENSE/**Sparse tensor represents a dataset in which most of the entries are zero. It does not store the whole values of the tensor object but stores the non-zero values and the corresponding coordinates of them.*/SPARSE/**Ragged tensors (also known as nested tensors) are similar to dense tensors but have variable-length dimensions.*/RAGGED}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
