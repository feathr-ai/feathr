package com.linkedin.feathr.common.tensor;

import com.linkedin.feathr.common.TypedTensor;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;


/**
 * An extension of the TypedTensor that is associated with a Feature Version through its urn.
 *
 * @deprecated Use TensorData instead. See PROML-13156.
 */
@Deprecated
public interface FeatureTensor extends TypedTensor {

  String getName();

  MlFeatureVersionUrn getUrn();

}