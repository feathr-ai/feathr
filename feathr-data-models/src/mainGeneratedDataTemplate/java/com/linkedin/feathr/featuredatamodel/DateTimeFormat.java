
package com.linkedin.feathr.featureDataModel;

import javax.annotation.Generated;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.schema.TyperefDataSchema;
import com.linkedin.data.template.DataTemplateUtil;
import com.linkedin.data.template.TyperefInfo;


/**
 * {@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/DateTimeFormat.pdl.")
public class DateTimeFormat
    extends TyperefInfo
{

    private final static TyperefDataSchema SCHEMA = ((TyperefDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel/**{@link java.time.format.DateTimeFormatter} pattern for a time field. e.g. yyyy/MM/dd/HH/mm/ss.*/typeref DateTimeFormat=string", SchemaFormatType.PDL));

    public DateTimeFormat() {
        super(SCHEMA);
    }

    public static TyperefDataSchema dataSchema() {
        return SCHEMA;
    }

}
