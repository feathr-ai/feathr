
package com.linkedin.feathr.featureDataModel;

import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;


/**
 * Represents a PathSpec string. PathSpec is used to specify projections in Rest.li request in order to select a subset of object contents. For more details, refer to https://linkedin.github.io/rest.li/Projections. Some examples: 'listingType', 'member:(firstName, lastName)'.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/PathSpec.pdl.")
public class PathSpec
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**Represents a PathSpec string. PathSpec is used to specify projections in Rest.li request in order to select a subset of object contents. For more details, refer to https://linkedin.github.io/rest.li/Projections. Some examples: 'listingType', 'member:(firstName, lastName)'.*/typeref PathSpec=string", SchemaFormatType.PDL));

    public PathSpec() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
