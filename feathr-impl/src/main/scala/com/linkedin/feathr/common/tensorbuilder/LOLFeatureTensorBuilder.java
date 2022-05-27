package com.linkedin.feathr.common.tensorbuilder;

import com.linkedin.feathr.common.tensor.FeatureTensor;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.common.tensorbuilder.LOLTensorBuilderFactory;
import com.linkedin.feathr.common.FeatureUrnUtil;
import com.linkedin.feathr.common.tensor.TensorType;


/**
 * A FeatureTensorBuilder that builds LOLFeatureTensors.
 *
 * For legacy API compatibility reasons, instances of this class are allowed to be used without calling {@link #init}
 * first, which is different from the general contract for {@link FeatureTensorBuilder}.
 *
 * @deprecated Use FeatureTensorBuilderImpl instead.
 */
@Deprecated
public class LOLFeatureTensorBuilder implements FeatureTensorBuilder {
  // NOTE: LOLFeatureTensorBuilder used to be a full implementation on its own, but has been gutted to delegate to
  //       FeatureTensorBuilderImpl instead. One major behavior difference was that LOLFeatureTensorBuilder did not
  //       require users to initialize it with init(). In order to safely maintain that legacy behavior, this class
  //       will keep track of whether it has been initialized, and will initialize its delegate if it detects it is
  //       being used without having been initialized.
  private final FeatureTensorBuilderImpl _delegate;
  private boolean _initialized = false;

  public LOLFeatureTensorBuilder(final String name, final TensorType type) {
    this(FeatureUrnUtil.toUrnForce(name), type);
  }

  public LOLFeatureTensorBuilder(final MlFeatureVersionUrn urn, final TensorType type) {
    _delegate = new FeatureTensorBuilderImpl(urn, type, LOLTensorBuilderFactory.INSTANCE);
  }

  /**
   * Supported way to get a LOLFeatureTensorBuilder.
   * @param urn
   * @param type
   * @return
   */
  public static LOLFeatureTensorBuilder fromUrn(final MlFeatureVersionUrn urn, final TensorType type) {
    return new LOLFeatureTensorBuilder(urn, type);
  }

  @Override
  public void init(int capacity) {
    _delegate.init(capacity);
    _initialized = true;
  }

  @Override
  public FeatureTensor build() {
    if (!_initialized) {
      init(0);
    }
    return _delegate.build();
  }

  @Override
  public FeatureTensorBuilder put(final Object value, final Object... dimensions) {
    if (!_initialized) {
      init(0);
    }
    return _delegate.put(value, dimensions);
  }
}