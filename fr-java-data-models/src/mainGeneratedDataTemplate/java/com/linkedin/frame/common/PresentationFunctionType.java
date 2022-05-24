
package com.linkedin.frame.common;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Presentation function types that are available for transforming inference internal value to member facing value
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from fr-java-data-models/src/main/pegasus/com/linkedin/frame/common/PresentationFunctionType.pdl.")
public enum PresentationFunctionType {


    /**
     * Transform internal boolean value like T, F, 0, 1 to member facing value like True, False
     * 
     */
    BOOLEAN,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.frame.common/**Presentation function types that are available for transforming inference internal value to member facing value*/enum PresentationFunctionType{/**Transform internal boolean value like T, F, 0, 1 to member facing value like True, False*/BOOLEAN}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
