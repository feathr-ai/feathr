
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models\\src\\main\\pegasus\\com\\linkedin\\feathr\\compute\\SlidingWindowFeature.pdl.")
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

    /**
     *  Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. 
     * 
     */
    MAX_POOLING,

    /**
     *  Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. 
     * 
     */
    MIN_POOLING,

    /**
     *  Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. 
     * 
     */
    AVG_POOLING,

    /**
     *  Latest 
     * 
     */
    LATEST,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute,enum AggregationType{/** Sum. */SUM/** Count. */COUNT/** Max. */MAX/** Min. */MIN/** Average. */AVG/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Max pooling is done by applying a max filter to (usually) non-overlapping subregions of the initial representation. */MAX_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Min pooling is done by applying a min filter to (usually) non-overlapping subregions of the initial representation. */MIN_POOLING/** Pooling is a sample-based discretization process. The objective is to down-sample an input representation and reduce its dimensionality. Average pooling is done by applying a average filter to (usually) non-overlapping subregions of the initial representation. */AVG_POOLING/** Latest */LATEST}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
