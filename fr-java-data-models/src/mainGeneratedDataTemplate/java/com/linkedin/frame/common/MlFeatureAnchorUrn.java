
package com.linkedin.frame.common;

import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;


/**
 * An URN that identifies a feature anchor, which contains information about the source of the feature, its extractor, environment and derivation. See go/frameoverview
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from fr-java-data-models/src/main/pegasus/com/linkedin/frame/common/MlFeatureAnchorUrn.pdl.")
public class MlFeatureAnchorUrn
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.frame.common/**An URN that identifies a feature anchor, which contains information about the source of the feature, its extractor, environment and derivation. See go/frameoverview*/@java.class=\"com.linkedin.frame.common.urn.MlFeatureAnchorUrn\"typeref MlFeatureAnchorUrn=string", SchemaFormatType.PDL));

    static {
        Custom.initializeCustomClass(com.linkedin.frame.common.urn.MlFeatureAnchorUrn.class);
    }

    public MlFeatureAnchorUrn() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
