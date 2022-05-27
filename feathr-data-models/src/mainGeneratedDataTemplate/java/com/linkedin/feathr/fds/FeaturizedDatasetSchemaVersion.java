
package com.linkedin.feathr.fds;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Schema version for the FDS. This value determines how the schema of an FDS must be interpretted.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from feathr-data-models/src/main/pegasus/com/linkedin/feathr/fds/FeaturizedDatasetSchemaVersion.pdl.")
public enum FeaturizedDatasetSchemaVersion {


    /**
     * With V0, the FDS still has the legacy metadata type (FeaturizedDatasetColumnMetadata) for
     * feature columns but also has the new metadata types (FeaturizedDatasetTopLevelMetadata and
     * ColumnMetadata). With this version, the new metadata is not used yet. It is just
     * populated but all the decisions are made based on legacy metadata.
     * This version is not supported as of Jan 2021.
     * 
     */
    V0,

    /**
     * With V1, the deprecated metadata type is not present. The new metadata types
     * (FeaturizedDatasetTopLevelMetadata and ColumnMetadata) are guaranteed to be
     * present in any valid FDS.
     * 
     */
    V1,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.feathr.fds/**Schema version for the FDS. This value determines how the schema of an FDS must be interpretted.*/enum FeaturizedDatasetSchemaVersion{/**With V0, the FDS still has the legacy metadata type (FeaturizedDatasetColumnMetadata) for\nfeature columns but also has the new metadata types (FeaturizedDatasetTopLevelMetadata and\nColumnMetadata). With this version, the new metadata is not used yet. It is just\npopulated but all the decisions are made based on legacy metadata.\nThis version is not supported as of Jan 2021.*/V0/**With V1, the deprecated metadata type is not present. The new metadata types\n(FeaturizedDatasetTopLevelMetadata and ColumnMetadata) are guaranteed to be\npresent in any valid FDS.*/V1}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
