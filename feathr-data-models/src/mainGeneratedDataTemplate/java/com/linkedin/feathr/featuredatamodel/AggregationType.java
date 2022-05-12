
package com.linkedin.feathr.featureDataModel;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SlidingWindowAggregation.pdl.")
public enum AggregationType {


    /**
     *  Sum. 
     * 
     */
    SUM,

    /**
     *  Count. 
     * 
     */
    COUNT,

    /**
     *  Max. 
     * 
     */
    MAX,

    /**
     *  Min. 
     * 
     */
    MIN,

    /**
     *  Average. 
     * 
     */
    AVG,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel,enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
