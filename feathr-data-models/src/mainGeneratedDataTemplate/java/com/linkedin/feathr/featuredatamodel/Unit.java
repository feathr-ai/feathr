
package com.linkedin.feathr.featureDataModel;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/Window.pdl.")
public enum Unit {


    /**
     *  A day. 
     * 
     */
    DAY,

    /**
     *  An hour. 
     * 
     */
    HOUR,

    /**
     *  A minute. 
     * 
     */
    MINUTE,

    /**
     *  A second. 
     * 
     */
    SECOND,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel,enum Unit{/** A day. */DAY/** An hour. */HOUR/** A minute. */MINUTE/** A second. */SECOND}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
