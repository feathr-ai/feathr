package com.linkedin.feathr.common.util;

import com.linkedin.feathr.common.metadata.MetadataException;
import com.linkedin.feathr.common.tensor.CategoricalType;
import com.linkedin.feathr.common.tensor.DimensionType;
import com.linkedin.feathr.common.tensor.TensorType;
import com.linkedin.feathr.common.tensor.UnboundedCountType;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.common.tensor.FeatureTensor;
import com.linkedin.feathr.common.tensorbuilder.LOLFeatureTensorBuilder;
import com.linkedin.feathr.common.FrameTypeResolver;
import com.linkedin.feathr.common.tensor.BoundedCountType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import org.apache.log4j.Logger;


/**
 * A class to build {@link FeatureTensor} from object returned from the execution of a MVEL expression.
 */
public class TensorBuilder {
  private final MlFeatureVersionUrn _featureUrn;
  private final TensorType _tensorType;
  private final int _rank;
  private LOLFeatureTensorBuilder _tensorBuilder;

  private static final Logger LOG = Logger.getLogger(TensorBuilder.class);

  /*
   * Creating this object is expensive, so we do it just once. Creation of FrameTypeResolver will fail
   * if no feature metadata has been provided or it can't be accessed. In that case, catch the initialization
   * error and re-throw it as a runtime exception.
   */
  private static FrameTypeResolver typeResolver;
  static {
    try {
      typeResolver = FrameTypeResolver.getInstance();
    } catch (ExceptionInInitializerError error) {
      Throwable cause = error.getCause();
      if (cause instanceof MetadataException) {
        String errMsg = "Tensors can't be built. Please check if feature metadata file has been provided.";
        LOG.error(errMsg);
        throw new IllegalStateException(errMsg, cause);
      } else {
        throw error;
      }
    }
  }

  /**
   * Constructor. A TensorBuilder object is specific to a single tensor being built for a feature. As such it's
   * initialized with the {@link MlFeatureVersionUrn}
   * @param featureUrn
   */
  public TensorBuilder(MlFeatureVersionUrn featureUrn) {
    Objects.requireNonNull(featureUrn, "MlFeatureVersionUrn object can't be null");

    _featureUrn = featureUrn;

    // Obtain the TensorType for this feature (Quince obtains this from FML)
    _tensorType = typeResolver.resolveFeature(_featureUrn)
        .orElseThrow(() -> new IllegalArgumentException("TensorType for feature " + _featureUrn
            + " doesn't exist; please check feature reference/name"));

    _rank = _tensorType.getDimensionTypes().size();

    _tensorBuilder = LOLFeatureTensorBuilder.fromUrn(_featureUrn, _tensorType);
  }

  /**
   * Build the {@link FeatureTensor} from the Object returned from the execution of a MVEL expression
   * @param rawFeatureValue Object representing value of the feature
   * @return {@link FeatureTensor}
   */
  public FeatureTensor build(Object rawFeatureValue) {
    Objects.requireNonNull(rawFeatureValue, "Object providing feature value can't be null");

    // Note: A builder has to be initialized for each new tensor creation.
    _tensorBuilder.init(0);

    FeatureTensor tensor;

    switch (_rank) {
      case 0:
        tensor = buildScalar(rawFeatureValue);
        break;

      case 1:
        tensor = buildVector(rawFeatureValue);
        break;

      default:
        tensor = buildHigherRankTensor(rawFeatureValue);
        break;
    }

    return tensor;
  }

  /*
   * Build a Rank-0 tensor, that is, a scalar
   */
  private FeatureTensor buildScalar(Object rawFeatureValue) {
    return _tensorBuilder.put(rawFeatureValue).build();
  }

  /*
   * Build a Rank-1 tensor, that is, a vector
   */
  @SuppressWarnings("unchecked")
  private FeatureTensor buildVector(Object rawFeatureValue) {
    FeatureTensor featureTensor;

    if (rawFeatureValue instanceof List) {
      /*
       * First check if we have a list of Maps. If so, build a term-vector from it.
       */
      List<?> list = (List) rawFeatureValue;
      Object head = list.get(0);
      if (head instanceof Map) {
        featureTensor = buildTermVector((List) list);
      } else {

        /*
         * If it's not a list of maps, then the objects in the list can constitute a categorical set or a dense vector.
         * So check the dimension type and then invoke the appropriate builder. The builders are different as the
         * categorical set and dense vector currently are represented differently. An example categorical set
         * {"apple", "orange", ...} would be represented as [["apple", 1], ["orange", 1], ...] whereas an example dense
         * vector [2.1, 4.2, 8.4, ...] would be represented as [[0, 2.1], [1, 4.2], [2, 8.4],...]. Thus, the categories
         * are represented as dimension and the value is always 1 whereas the indices for dense vector are the natural
         * indices 0, 1, etc.
         * That said, the above representations aren't ideal and are carried over from the NTV representation.
         * Representing categories in a categorical set as dimensions is counter-intuitive, and so is enumerating the
         * indices of a dense vector. Until FeatureTensor supports these as first-class entities, we'll use the above.
         */

        DimensionType dimType = _tensorType.getDimensionTypes().get(0);
        if (dimType instanceof CategoricalType) {
          featureTensor = buildCategoricalSet(rawFeatureValue);
        } else if (dimType instanceof BoundedCountType || dimType instanceof UnboundedCountType) {
          featureTensor = buildDenseVector(rawFeatureValue);
        } else {
          throw new UnsupportedOperationException("Unsupported dimension " + dimType.getName() + " for object "
              + rawFeatureValue);
        }
      }
    } else if (rawFeatureValue instanceof Map) {
      featureTensor = buildTermVector((Map) rawFeatureValue);
    } else {
      throw new UnsupportedOperationException("Unsupported type for object " + rawFeatureValue);
    }

    return featureTensor;
  }

  /*
   * Build a FeatureTensor from a raw feature value representing a categorical set
   */
  private FeatureTensor buildCategoricalSet(Object rawFeatureValue) {
    List<?> objects = (List) rawFeatureValue;

    IntStream.range(0, objects.size())
        .forEach(i -> _tensorBuilder.put(1, objects.get(i)));

    return _tensorBuilder.build();
  }

  /*
   * Build a FeatureTensor from a raw feature value representing a dense vector
   */
  private FeatureTensor buildDenseVector(Object rawFeatureValue) {
    List<?> objects = (List) rawFeatureValue;

    IntStream.range(0, objects.size())
        .forEach(i -> _tensorBuilder.put(objects.get(i), i));

    return _tensorBuilder.build();
  }

  /*
   * Build a FeatureTensor from a raw feature value representing list of maps where each map represents a term-vector
   */
  private <K, V> FeatureTensor buildTermVector(List<Map<? extends K, ? extends V>> listOfMaps) {
    // First, coalesce the individual maps into a single term-value map (aka term-vector)
    Map<K, V> termValueMap = new HashMap<>();
    for (Map<? extends K, ? extends V> elem : listOfMaps) {
      termValueMap.putAll(elem);
    }

    // Next, delegate to method that builds FeatureTensor from term-vector
    return buildTermVector(termValueMap);
  }

  /*
   * Build a FeatureTensor from a raw feature value representing a term-value map (aka term-vector)
   */
  private <K, V> FeatureTensor buildTermVector(Map<K, V> termValueMap) {
    for (Map.Entry<K, V> termValue : termValueMap.entrySet()) {
      _tensorBuilder.put(termValue.getValue(), termValue.getKey());
    }

    return _tensorBuilder.build();
  }

  private FeatureTensor buildHigherRankTensor(Object rawFeatureValue) {
    /*
     * TODO: Will have to split dimensions on a delimiter between term components; e.g. for {"foo|bar|7" : 4.2} will
     *  be split into dimension values "foo", "bar" and 7, and the value will be 4.2. However this imposes
     *  a constraint on the client that the MVEL expression must produce delimited term components.
     *  May be able to leverage TermValueConverter in Quince
     */
    throw new RuntimeException("Not yet implemented");
  }
}