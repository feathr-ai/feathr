package com.linkedin.feathr.common.tensorbuilder;

import com.linkedin.feathr.common.tensor.GenericFeatureTensor;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.common.tensor.Representable;
import com.linkedin.feathr.common.tensorbuilder.TensorBuilder;
import com.linkedin.feathr.common.tensorbuilder.TensorBuilderFactory;
import com.linkedin.feathr.common.FeatureUrnUtil;
import com.linkedin.feathr.common.tensor.TensorType;
import java.net.URISyntaxException;

/**
 * @deprecated See PROML-13156.
 */
@Deprecated
public class FeatureTensorBuilderImpl implements FeatureTensorBuilder {
  protected final TensorType _tensorType;
  protected final TensorBuilder _tensorBuilder;
  protected final Representable[] _columnTypes;
  private final MlFeatureVersionUrn _featureUrn;
  private boolean _initialized = false;

  /**
   * Build a FeatureTensorBuilderImpl from a feature-ref, this is kept for historical reasons.
   * Quince will not support feature-ref in the future.
   *
   * @param featureRef feature reference
   * @param tensorType tensorType for which this builder is needed
   * @param tensorBuilderFactory factory to use to generate the builder
   *
   * @throws IllegalArgumentException of the featureRef cannot be translated to an URN.
   *
   * @deprecated please see @link{fromUrn}
   */
  @Deprecated
  public FeatureTensorBuilderImpl(String featureRef, TensorType tensorType, TensorBuilderFactory tensorBuilderFactory) {
    _tensorType = tensorType;
    try {
      _featureUrn = FeatureUrnUtil.toUrn(featureRef);
    } catch (URISyntaxException ex) {
      throw new IllegalArgumentException(ex);
    }
    _columnTypes = tensorType.getColumnTypes();
    _tensorBuilder = tensorBuilderFactory.getTensorBuilder(tensorType);
  }

  /**
   * Real constructor needs to be protected for backwards compatibility reasons.
   */
  protected FeatureTensorBuilderImpl(MlFeatureVersionUrn urn, TensorType tensorType,
      TensorBuilderFactory tensorBuilderFactory) {
    _tensorType = tensorType;
    _featureUrn = urn;
    _columnTypes = tensorType.getColumnTypes();
    _tensorBuilder = tensorBuilderFactory.getTensorBuilder(tensorType);
  }

  /**
   * Main builder for FeatureTensorBuilderImpl
   *
   * @param urn feature version urn
   * @param tensorType tensorType for which this builder is needed
   * @param tensorBuilderFactory factory to use to generate the builder
   *
   * @return the builder
   */
  public static FeatureTensorBuilderImpl fromUrn(MlFeatureVersionUrn urn, TensorType tensorType,
      TensorBuilderFactory tensorBuilderFactory) {
    return new FeatureTensorBuilderImpl(urn, tensorType, tensorBuilderFactory);
  }

  @Override
  public void init(int capacity) {
    _tensorBuilder.start(capacity);
    _initialized = true;
  }

  @Override
  public GenericFeatureTensor build() {
    if (!_initialized) {
      init(0);
    }
    return GenericFeatureTensor.fromUrn(_tensorBuilder.build(), _featureUrn, _tensorType);
  }

  @Override
  public FeatureTensorBuilder put(Object value, Object... dimensions) {
    if (!_initialized) {
      init(0);
    }
    //Removing Preconditions.checkArgument because old version of Guava (that our customers are stuck with)
    //converts _featureUrn to string even when it isn't required
    if (_columnTypes.length != dimensions.length + 1) {
      throw new IllegalArgumentException("Column type length check failed for feature: " + _featureUrn);
    }
    setDims(dimensions);
    // set the value
    _tensorType.getValueType().getRepresentation().from(value, _tensorBuilder, dimensions.length);
    _tensorBuilder.append();
    return this;
  }

  // This is factored this way so that CachingFeatureTensorBuilderImpl can override it...
  void setDims(Object[] dimensions) {
    _tensorType.setDimensions(_tensorBuilder, dimensions);
  }
}