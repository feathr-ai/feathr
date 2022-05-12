
package com.linkedin.feathr.compute;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of Frame. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO(PROML-13658): this is expected to be deprecated once the full transition to TensorType is completed
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/compute/FrameFeatureType.pdl.")
public enum FrameFeatureType {


    /**
     *  Boolean valued feature 
     * 
     */
    BOOLEAN,

    /**
     *  Numerically valued feature such as INT, LONG, DOUBLE, etc 
     * 
     */
    NUMERIC,

    /**
     *  Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) 
     * 
     */
    CATEGORICAL,

    /**
     *  Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) 
     * 
     */
    CATEGORICAL_SET,

    /**
     *  Represents a feature in vector format where the the majority of the elements are non-zero 
     * 
     */
    DENSE_VECTOR,

    /**
     *  Represents features that has string terms and numeric value
     * 
     */
    TERM_VECTOR,

    /**
     *  Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).
     * 
     */
    TENSOR,

    /**
     *  Placeholder for when no types are specified 
     * 
     */
    UNSPECIFIED,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.compute/**The high level types associated with a feature.  In contrast with TensorFeatureFormat which contains additional metadata about the type of the tensor, this represents the high level semantic types supported by early versions of Frame. See https://iwww.corp.linkedin.com/wiki/cf/display/ENGS/Feature+Representation+and+Feature+Type+System for more detais. TODO(PROML-13658): this is expected to be deprecated once the full transition to TensorType is completed*/enum FrameFeatureType{/** Boolean valued feature */BOOLEAN/** Numerically valued feature such as INT, LONG, DOUBLE, etc */NUMERIC/** Represents a feature that consists of a single category (e.g. MOBILE, DESKSTOP) */CATEGORICAL/** Represents a feature that consists of multiple categories (e.g. MOBILE, DESKSTOP) */CATEGORICAL_SET/** Represents a feature in vector format where the the majority of the elements are non-zero */DENSE_VECTOR/** Represents features that has string terms and numeric value*/TERM_VECTOR/** Represents tensor based features.  Note: this represents the high level semantic tensor type but does not include the low level tensor format such as category, shape, dimension and value types.  The latter are defined as part of the new tensor annotation (via TensorFeatureFormat) or the legacy FML (go/FML).*/TENSOR/** Placeholder for when no types are specified */UNSPECIFIED}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
