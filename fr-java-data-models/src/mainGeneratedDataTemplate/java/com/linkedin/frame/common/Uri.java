
package com.linkedin.frame.common;

import java.net.URI;
import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.Custom;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;
import com.linkedin.frame.common.coercer.UriCoercer;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from fr-java-data-models/src/main/pegasus/com/linkedin/frame/common/Uri.pdl.")
public class Uri
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.frame.common@java={\"class\":\"java.net.URI\",\"coercerClass\":\"com.linkedin.frame.common.coercer.UriCoercer\"}typeref Uri=string", SchemaFormatType.PDL));

    static {
        Custom.initializeCustomClass(URI.class);
        Custom.initializeCoercerClass(UriCoercer.class);
    }

    public Uri() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
