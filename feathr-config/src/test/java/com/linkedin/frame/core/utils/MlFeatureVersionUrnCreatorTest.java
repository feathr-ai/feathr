package com.linkedin.frame.core.utils;

import com.linkedin.common.Version;
import com.linkedin.frame.common.urn.MlFeatureUrn;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.common.urn.TupleKey;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

/**
 * Test class for {@link MlFeatureVersionUrnCreator}
 */
public class MlFeatureVersionUrnCreatorTest {
    private MlFeatureVersionUrnCreator _mlFeatureVersionUrnCreator = MlFeatureVersionUrnCreator.getInstance();

    @Test(description = "test invalid namespace global", expectedExceptions = IllegalArgumentException.class)
    public void testInvalidNamespace() {
        String featureRefString = "global-testFeatureName-0-1";
        _mlFeatureVersionUrnCreator.create(featureRefString);
    }

    @Test(description = "test invalid zero version", expectedExceptions = IllegalArgumentException.class)
    public void testInvalidVersion() {
        String featureRefString = "testNamespace-testFeatureName-0-0";
        _mlFeatureVersionUrnCreator.create(featureRefString);
    }

    @Test(description = "test create MlFeatureVersionUrn from legacy feature")
    public void testCreatLegacyFeature() {
        String featureRefString = "testFeatureName";
        MlFeatureVersionUrn mlFeatureVersionUrn = _mlFeatureVersionUrnCreator.create(featureRefString);
        assertEquals(featureRefString, mlFeatureVersionUrn.getEntityKey().getAs(0, MlFeatureUrn.class)
                .getEntityKey().getAs(1, String.class));
    }

    @Test(description = "test create MlFeatureVersionUrn")
    public void testCreatFeature() {
        String featureRefString = "testNamespace-testFeatureName-1-0";
        MlFeatureVersionUrn mlFeatureVersionUrn = _mlFeatureVersionUrnCreator.create(featureRefString);
        TupleKey mlFeatureUrnEntities =
                mlFeatureVersionUrn.getEntityKey().getAs(0, MlFeatureUrn.class).getEntityKey();
        int major = mlFeatureVersionUrn.getEntityKey().getAs(1, Integer.class);
        assertEquals("testNamespace", mlFeatureUrnEntities.getAs(0, String.class));
        assertEquals("testFeatureName", mlFeatureUrnEntities.getAs(1, String.class));
        assertEquals(major, 1);
    }
}
