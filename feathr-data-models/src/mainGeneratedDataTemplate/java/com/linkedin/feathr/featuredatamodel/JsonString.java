
package com.linkedin.feathr.featureDataModel;

import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;


/**
 * Represents a Json string.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/JsonString.pdl.")
public class JsonString
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a Json string.*/typeref JsonString=string", SchemaFormatType.PDL));

    public JsonString() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
