package com.linkedin.frame.config.featureanchor;

import com.linkedin.frame.common.urn.MlFeatureAnchorUrn;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.featureDataModel.FeatureAnchor;
import java.net.URISyntaxException;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.*;


public class FeatureAnchorUrnCreatorTest {
  @Test(description = "test construct feature anchor urn")
  public void testConstructFeatureAnchorUrn() throws URISyntaxException {
    FeatureAnchor featureAnchor = mock(FeatureAnchor.class);
    FeatureAnchorHasher featureAnchorHasher = mock(FeatureAnchorHasher.class);
    FeatureAnchorUrnCreator featureAnchorUrnCreator = new FeatureAnchorUrnCreator(featureAnchorHasher);
    long hash = 123456789L;
    when(featureAnchorHasher.hash(eq(featureAnchor))).thenReturn(hash);
    MlFeatureVersionUrn featureVersionUrn = MlFeatureVersionUrn.deserialize("urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature),1,2,0)");
    MlFeatureAnchorUrn featureAnchorUrn = featureAnchorUrnCreator.create(featureVersionUrn,  featureAnchor);
    String expected = String.format("urn:li:mlFeatureAnchor:(urn:li:mlFeatureVersion:(urn:li:mlFeature:(testSpace,testFeature),1,2,0),%d)", hash);
    assertEquals(featureAnchorUrn.toString(), expected);
  }
}
