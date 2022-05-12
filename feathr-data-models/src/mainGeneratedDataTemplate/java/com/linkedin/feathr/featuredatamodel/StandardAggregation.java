
package com.linkedin.feathr.featureDataModel;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * 
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/featureDataModel/SequentialJoinFeatureSourcesAnchor.pdl.")
public enum StandardAggregation {


    /**
     *  Average. Apply to 0-rank numeric scalar. For example, base feature gets a job's applicants, and expansion feature gets the age of an applicant, when average is specified, the resulting feature data is the average age of all applicants for a job. 
     * 
     */
    AVG,

    /**
     *  Max. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. 
     * 
     */
    MAX,

    /**
     *  Min. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. 
     * 
     */
    MIN,

    /**
     *  Sum. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. 
     * 
     */
    SUM,

    /**
     *  Union. This combines multiple feature data into a collection. For example, base feature gets member's skill ids, and expansion feature gets a skill name based on skill id, when union is specified, the resulting feature data is a list of skill names for a given member. 
     * 
     */
    UNION,

    /**
     *  Element-wise Average, which apply average operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. For example, base feature gets member's skill ids, and expansion feature gets a skill embedding based on skill id, when ELEMENTWISE_AVG is specified, the resulting feature data is a single embedding by element-wise averaging all skill embeddings of the member. 
     * 
     */
    ELEMENTWISE_AVG,

    /**
     *  Element-wise Min, which apply min operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. 
     * 
     */
    ELEMENTWISE_MIN,

    /**
     *  Element-wise Max, which apply max operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. 
     * 
     */
    ELEMENTWISE_MAX,

    /**
     *  Element-wise Sum, which apply sum operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. 
     * 
     */
    ELEMENTWISE_SUM,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.featureDataModel,enum StandardAggregation{/** Average. Apply to 0-rank numeric scalar. For example, base feature gets a job's applicants, and expansion feature gets the age of an applicant, when average is specified, the resulting feature data is the average age of all applicants for a job. */AVG/** Max. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MAX/** Min. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */MIN/** Sum. Apply to 0-rank numeric scalar. Refer to AVG for a very similar example. */SUM/** Union. This combines multiple feature data into a collection. For example, base feature gets member's skill ids, and expansion feature gets a skill name based on skill id, when union is specified, the resulting feature data is a list of skill names for a given member. */UNION/** Element-wise Average, which apply average operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. For example, base feature gets member's skill ids, and expansion feature gets a skill embedding based on skill id, when ELEMENTWISE_AVG is specified, the resulting feature data is a single embedding by element-wise averaging all skill embeddings of the member. */ELEMENTWISE_AVG/** Element-wise Min, which apply min operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MIN/** Element-wise Max, which apply max operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_MAX/** Element-wise Sum, which apply sum operation to elements corresponding to the same positional index. It applies to 1+ rank feature data, like a dense vector. Refer to ELEMENTWISE_AVG for a very similar example. */ELEMENTWISE_SUM}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
