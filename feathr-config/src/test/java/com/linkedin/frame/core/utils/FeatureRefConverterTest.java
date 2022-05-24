package com.linkedin.frame.core.utils;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class FeatureRefConverterTest {
    @Test
    public void testGetLegacyFeatureRef() {
        String featureRefString = "testFeatureName";
        testFeatureRefConverterHelper(featureRefString);
    }

    @Test
    public void testGetFeatureRef() {
        String featureRefString = "testNamespace-testFeatureName-0-1";
        testFeatureRefConverterHelper(featureRefString);
    }

    private void testFeatureRefConverterHelper(String featureRefString) {
        MlFeatureVersionUrn mlFeatureVersionUrn = MlFeatureVersionUrnCreator.getInstance().create(featureRefString);
        String resolvedFeatureRefString = FeatureRefConverter.getInstance().getFeatureRefStr(mlFeatureVersionUrn);
        assertEquals(featureRefString, resolvedFeatureRefString);
    }
}
