
package com.linkedin.frame.common;

import javax.annotation.Generated;
import com.linkedin.data.schema.EnumDataSchema;
import com.linkedin.data.schema.SchemaFormatType;
import com.linkedin.data.template.DataTemplateUtil;


/**
 * Fabric group type of Linkedin internal data center fabrics; For example, according to linkedin. ei1 and ei2 are both type of EI. We are currently using a name length limit of 10 for FabricType.
 * 
 */
@Generated(value = "com.linkedin.pegasus.generator.JavaCodeUtil", comments = "Rest.li Data Template. Generated from fr-java-data-models/src/main/pegasus/com/linkedin/frame/common/FabricType.pdl.")
public enum FabricType {


    /**
     * Designates DEV fabrics, although it could be machines in EI physical fabric like EI1
     * 
     */
    DEV,

    /**
     * Designates Early-Integration fabrics, such EI1, EI2 etc.
     * 
     */
    EI,

    /**
     * Designates production fabrics, such as prod-ltx1, prod-lva1 etc.
     * 
     */
    PROD,

    /**
     * Designates corperation fabrics, such as corp-eat1, corp-lca1 etc.
     * 
     */
    CORP,

    /**
     * Designates infrastructure testing fabrics, such as lit-lca1-1.
     * 
     */
    LIT,

    /**
     * Designates Prime fabric group for project Einstein within Linkedin.
     * 
     */
    PRIME,

    /**
     * Designates production fabrics deployed in the Mergers and Acquisitions network (MANDA).
     * 
     */
    MANDA,

    /**
     * Designates production fabrics deployed in the Azure control plane.
     * 
     */
    AZURECONTROL,

    /**
     * Designates production fabrics deployed in the Azure.
     * 
     */
    AZUREPROD,

    /**
     * Designates Early-Integration fabrics deployed in Azure.
     * 
     */
    AZUREEI,

    /**
     * Designates command Fabrics where the LinkedIn control plane lives.
     * 
     */
    CMD,

    /**
     * Designates grid Fabrics which host LinkedIn data and services for offline processing.
     * 
     */
    GRID,

    /**
     * Designates EI command Fabrics where the LinkedIn control plane lives. EI mirror of CMD (like cmd-us-e)
     * 
     */
    EICMD,

    /**
     * Designates EI grid Fabrics which host LinkedIn data and services for offline processing. EI mirror of GRID (like grid-us-e)
     * 
     */
    EIGRID,

    /**
     * Designates EI production fabrics, such as eiprod-us-e. EI mirror of PROD (like prod-us-e)
     * 
     */
    EIPROD,

    /**
     * Designates EI internal application fabrics, such as eiiapp-us-e. EI mirror of IAPP (like iapp-us-e)
     * 
     */
    EIIAPP,

    /**
     * Designates Internal application fabrics, such as iapp-us-e.
     * 
     */
    IAPP,

    /**
     * Designates Integration Environments for Fabric build process
     * 
     */
    INT,
    $UNKNOWN;
    private final static EnumDataSchema SCHEMA = ((EnumDataSchema) DataTemplateUtil.parseSchema("namespace com.linkedin.frame.common/**Fabric group type of Linkedin internal data center fabrics; For example, according to linkedin. ei1 and ei2 are both type of EI. We are currently using a name length limit of 10 for FabricType.*/enum FabricType{/**Designates DEV fabrics, although it could be machines in EI physical fabric like EI1*/DEV/**Designates Early-Integration fabrics, such EI1, EI2 etc.*/EI/**Designates production fabrics, such as prod-ltx1, prod-lva1 etc.*/PROD/**Designates corperation fabrics, such as corp-eat1, corp-lca1 etc.*/CORP/**Designates infrastructure testing fabrics, such as lit-lca1-1.*/LIT/**Designates Prime fabric group for project Einstein within Linkedin.*/PRIME/**Designates production fabrics deployed in the Mergers and Acquisitions network (MANDA).*/MANDA/**Designates production fabrics deployed in the Azure control plane.*/AZURECONTROL/**Designates production fabrics deployed in the Azure.*/AZUREPROD/**Designates Early-Integration fabrics deployed in Azure.*/AZUREEI/**Designates command Fabrics where the LinkedIn control plane lives.*/CMD/**Designates grid Fabrics which host LinkedIn data and services for offline processing.*/GRID/**Designates EI command Fabrics where the LinkedIn control plane lives. EI mirror of CMD (like cmd-us-e)*/EICMD/**Designates EI grid Fabrics which host LinkedIn data and services for offline processing. EI mirror of GRID (like grid-us-e)*/EIGRID/**Designates EI production fabrics, such as eiprod-us-e. EI mirror of PROD (like prod-us-e)*/EIPROD/**Designates EI internal application fabrics, such as eiiapp-us-e. EI mirror of IAPP (like iapp-us-e)*/EIIAPP/**Designates Internal application fabrics, such as iapp-us-e.*/IAPP/**Designates Integration Environments for Fabric build process*/INT}", SchemaFormatType.PDL));

    public static EnumDataSchema dataSchema() {
        return SCHEMA;
    }

}
