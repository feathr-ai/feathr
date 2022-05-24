package com.linkedin.frame.core.config.presentation;

import com.linkedin.data.template.StringMap;
import com.linkedin.frame.common.PresentationFunction;
import com.linkedin.frame.common.PresentationFunctionType;
import com.linkedin.frame.common.PresentationInlineMappingInfo;
import com.linkedin.frame.common.PresentationTableMappingInfo;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.testng.annotations.Test;

/**
 * Test class for {@link PresentationConfig}
 */
public class PresentationConfigTest {
  @Test(description = "test equals and hashcode")
  public void testEqualsHashcode() {
    PresentationTableMappingInfo tableMappingInfo1 = new PresentationTableMappingInfo().setValueColumn("value1");
    PresentationTableMappingInfo tableMappingInfo2 = new PresentationTableMappingInfo().setValueColumn("value2");

    PresentationInlineMappingInfo inlineMappingInfo1 = new PresentationInlineMappingInfo().setName("name1");
    PresentationInlineMappingInfo inlineMappingInfo2 = new PresentationInlineMappingInfo().setName("name2");

    PresentationFunction presentationFunction1
        = new PresentationFunction().setFuntionType(PresentationFunctionType.BOOLEAN);
    PresentationFunction presentationFunction2
        = new PresentationFunction().setFuntionType(PresentationFunctionType.BOOLEAN).setParams(new StringMap());

    EqualsVerifier.forClass(PresentationConfig.class)
        .usingGetClass()
        // EqualsVerifier can not work with recursive data structure
        .withPrefabValues(PresentationTableMappingInfo.class, tableMappingInfo1, tableMappingInfo2)
        .withPrefabValues(PresentationInlineMappingInfo.class, inlineMappingInfo1, inlineMappingInfo2)
        .withPrefabValues(PresentationFunction.class, presentationFunction1, presentationFunction2)
        .withIgnoredFields("_configStr") // toString field won't be used for equal check as it is redundant
        .verify();
  }
}
