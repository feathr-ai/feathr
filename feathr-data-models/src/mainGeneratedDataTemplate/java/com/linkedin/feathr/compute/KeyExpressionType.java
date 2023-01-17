
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Different key formats supported.
 * Todo - We probably do not want to generalize this as a kind of key-operator in the core compute model,
 * with instances such as for MVEL or SQL being available (e.g. via an OperatorId reference).
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\KeyExpressionType.pdl.")
public enum KeyExpressionType {


    /**
     * Java-based MVEL
     * 
     */
    MVEL,

    /**
     * Spark-SQL
     * 
     */
    SQL,

    /**
     * Custom java/scala UDF
     * 
     */
    UDF,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Different key formats supported.\r\nTodo - We probably do not want to generalize this as a kind of key-operator in the core compute model,\r\nwith instances such as for MVEL or SQL being available (e.g. via an OperatorId reference).*/enum KeyExpressionType{/**Java-based MVEL*/MVEL/**Spark-SQL*/SQL/**Custom java/scala UDF*/UDF}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
