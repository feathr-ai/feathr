
package com.linkedin.feathr.fds;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Representation type of a feature
 * <p/>
 * More details can be found in the specification
 * https://docs.google.com/document/d/12VZis-6VyQN3Ivy2rQxf-OktZqCowuTvVrsIFxwTVXU/edit#heading=h.jjuk2t7bn84
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/TensorCategory.pdl.")
public enum TensorCategory {


    /**
     * A tensor where we only store values for some positions.
     * 
     */
    SPARSE,

    /**
     * A tensor where we store a value for each position.
     * 
     */
    DENSE,

    /**
     * RAGGED tensors (also called nested tensors) are similar to dense tensors but have variable-length
     * dimensions.
     * 
     */
    RAGGED,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Representation type of a feature\n<p/>\nMore details can be found in the specification\nhttps://docs.google.com/document/d/12VZis-6VyQN3Ivy2rQxf-OktZqCowuTvVrsIFxwTVXU/edit#heading=h.jjuk2t7bn84*/enum TensorCategory{/**A tensor where we only store values for some positions.*/SPARSE/**A tensor where we store a value for each position.*/DENSE/**RAGGED tensors (also called nested tensors) are similar to dense tensors but have variable-length\ndimensions.*/RAGGED}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
