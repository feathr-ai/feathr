package com.linkedin.feathr.common.tensor;

import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.common.tensor.TensorData;
import com.linkedin.feathr.common.Equal;
import com.linkedin.feathr.common.Hasher;
import com.linkedin.feathr.common.FeatureUrnUtil;
import com.linkedin.feathr.common.tensor.TensorType;
import java.net.URISyntaxException;
import java.util.Objects;


/**
 * Feature encapsulates tensor data and type information to provide convenient runtime functionality.
 *
 * @deprecated See PROML-13156.
 */
@Deprecated
public class GenericFeatureTensor extends GenericTypedTensor implements FeatureTensor {

  private final MlFeatureVersionUrn _urn;

  /**
   * Constructs tensor for given data and type. This constructor is deprecated because it
   * uses a feature-ref. Quince will not support feature-ref in the future.
   *
   * @throws IllegalArgumentException of the featureRef cannot be translated to an URN.
   *
   * @deprecated please use @link{fromUrn} instead
   */
  @Deprecated
  public GenericFeatureTensor(final TensorData data, final String featureRef, final TensorType type) {
    super(Objects.requireNonNull(data), Objects.requireNonNull(type));
    try {
      _urn = FeatureUrnUtil.toUrn(featureRef);
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException(e);
    }
  }

  /**
   * Main constructor is private for backwards compatibility reasons.
   */
  protected GenericFeatureTensor(final TensorData data, final MlFeatureVersionUrn urn, final TensorType type) {
    super(Objects.requireNonNull(data), Objects.requireNonNull(type));
    _urn = Objects.requireNonNull(urn);
  }

  /**
   * This is the preferred builder for GenericFeatureTensor.
   *
   * @param data actual data for the tensor
   * @param urn feature version urn
   * @param type the tensor type
   *
   * @return the GenericFeatureTensor
   */
  public static GenericFeatureTensor fromUrn(final TensorData data, final MlFeatureVersionUrn urn, final TensorType type) {
    return new GenericFeatureTensor(data, urn, type);
  }

  /**
   * Returns opaque reference identifier.
   */
  @Override
  public String getName() {
    return FeatureUrnUtil.toFeatureRef(_urn);
  }

  /**
   * @return the feature urn.
   */
  @Override
  public MlFeatureVersionUrn getUrn() {
    return _urn;
  }

  /**
   * Is never equal to instances of other classes, including Marmalade-generated.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GenericFeatureTensor that = (GenericFeatureTensor) o;
    return _urn.equals(that._urn) && _type.equals(that._type) && Equal.INSTANCE.apply(_data, that._data);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_urn, Hasher.INSTANCE.apply(_data, false), _type);
  }
}