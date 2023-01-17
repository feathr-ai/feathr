
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Type of datasource node.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\DataSourceType.pdl.")
public enum DataSourceType {


    /**
     * Update data sources provide keyed data about entities. A fully specified table data source contains both a snapshot view and an update log.
     * 
     */
    UPDATE,

    /**
     * Event data sources are append-only event logs whose records need to be grouped and aggregated (e.g. counted, averaged, top-K’d)
     * over a limited window of time.
     * 
     */
    EVENT,

    /**
     * Reprent the observation data entities (like the join key or passthrough feature columns)
     * 
     */
    CONTEXT,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**Type of datasource node.*/enum DataSourceType{/**Update data sources provide keyed data about entities. A fully specified table data source contains both a snapshot view and an update log.*/UPDATE/**Event data sources are append-only event logs whose records need to be grouped and aggregated (e.g. counted, averaged, top-K\u00e2\u20ac\u2122d)\r\nover a limited window of time.*/EVENT/**Reprent the observation data entities (like the join key or passthrough feature columns)*/CONTEXT}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
