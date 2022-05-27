package com.linkedin.feathr.common;

import com.google.common.annotations.VisibleForTesting;
import com.linkedin.feathr.common.types.PrimitiveType;
import com.linkedin.feathr.common.util.TensorBuilder;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.feathr.compute.FeatureVersion;
import com.linkedin.feathr.common.tensor.FeatureTensor;
import com.linkedin.feathr.common.types.FeatureType;
import com.linkedin.feathr.common.tensor.TensorType;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class extracts default {@link FeatureValue} from pegasus models
 */
public class PegasusDefaultFeatureValueResolver {
  private static final String DEFAULT_VALUE_PATH = "MOCK_DEFAULT_VALUE_PATH";
  private static final String HOCON_PREFIX = "{ ";
  private static final String HOCON_SUFFIX = " }";
  private static final String HOCON_DELIM = " : ";

  private static final PegasusDefaultFeatureValueResolver INSTANCE =
      new PegasusDefaultFeatureValueResolver(PegasusFeatureTypeResolver.getInstance());

  private final PegasusFeatureTypeResolver _pegasusFeatureTypeResolver;

  private static final Logger LOG = LoggerFactory.getLogger(PegasusDefaultFeatureValueResolver.class.getSimpleName());

  public static PegasusDefaultFeatureValueResolver getInstance() {
    return INSTANCE;
  }

  /**
   * Package private constructor for testing with mock
   */
  PegasusDefaultFeatureValueResolver(PegasusFeatureTypeResolver pegasusFeatureTypeResolver) {
    _pegasusFeatureTypeResolver = pegasusFeatureTypeResolver;
  }

  /**
   * Resolve default value in the format of {@link FeatureValue} from {@link FeatureVersion}.
   * The resolver does not cache the intermediate and final result.
   *
   * @param mlFeatureVersionUrn the feature's {@link MlFeatureVersionUrn}
   * @param featureVersion the Pegasus {@link FeatureVersion} record
   * @return Optional of {@link FeatureValue}, empty if there is resolving exceptions, or if the input does not contain default value information
   */
  public Optional<FeatureValue> resolveDefaultValue(MlFeatureVersionUrn mlFeatureVersionUrn, FeatureVersion featureVersion) {
    if (!featureVersion.hasDefaultValue()) {
      return Optional.empty();
    }

    if (!Objects.requireNonNull(featureVersion.getDefaultValue()).isString()) {
      throw new RuntimeException("The default value type for " + mlFeatureVersionUrn.toString()
          + " is not supported, currently only support HOCON string");
    }

    String rawExpr = featureVersion.getDefaultValue().getString();

    /*
     * The default value stored in FeatureVersion is always a HOCON expression.
     * The HOCON expression can not be directly parsed.
     * Here we construct a valid HOCON string from the expression, and load the HOCON string with ConfigFactory.
     *
     * For instance, suppose the default value HOCON expression is "true", it can not be directly converted to a valid
     *  HOCON object. To correctly parse it, we build a valid HOCON string as follows
     * "{ MOCK_DEFAULT_VALUE_PATH: true }".
     */
    StringBuilder hoconStringBuilder = new StringBuilder();
    hoconStringBuilder.append(HOCON_PREFIX).append(DEFAULT_VALUE_PATH).append(HOCON_DELIM).append(rawExpr).append(HOCON_SUFFIX);
    String hoconFullString = hoconStringBuilder.toString();
    Config config = ConfigFactory.parseString(hoconFullString);

    FeatureTypeConfig featureTypeConfig = _pegasusFeatureTypeResolver.resolveFeatureType(featureVersion);
    Optional<FeatureValue> featureValue = resolveDefaultValue(mlFeatureVersionUrn, featureTypeConfig, config);

    if (!featureValue.isPresent()) {
      String errMessage = String.join("", "Fail to extract default FeatureValue for ", mlFeatureVersionUrn.toString(),
          " from raw expression:\n", rawExpr);
      throw new RuntimeException(errMessage);
    }

    LOG.info("The default value for feature {} is resolved as {}", mlFeatureVersionUrn, featureValue.get());

    return featureValue;
  }

  private Optional<FeatureValue> resolveDefaultValue(MlFeatureVersionUrn mlFeatureVersionUrn,
      FeatureTypeConfig featureTypeConfig, Config config) {

    ConfigValue defaultConfigValue = config.getValue(DEFAULT_VALUE_PATH);
    // taking advantage of HOCON lib to extract default value Java object
    // TODO(PROML-14639)
    // The behaviour here between JACKSON parser and TypeSafe config is slightly different.
    // JACKSON parser allows us to specify the type via syntax like: 1.2f, 1.2d, 1.2L to respectively show they are
    // float, double and Long. However, there is no way to do this in TypeSafe config. In TypeSafe config,
    // 1.2f, 1.2d and 1.2L will all be considered as String.
    Object defaultValueObj = defaultConfigValue.unwrapped();
    Optional<Object> normalizedDefaultValue = normalize(defaultValueObj);

    if (!normalizedDefaultValue.isPresent()) {
      return Optional.empty();
    }

    Object defaultData = normalizedDefaultValue.get();
    FeatureTypes featureType = featureTypeConfig.getFeatureType();
    if (featureType != FeatureTypes.TENSOR) {
      FeatureValue featureValue = new FeatureValue(defaultData, featureType);
      return Optional.of(featureValue);
    } else if (featureTypeConfig.getTensorType() != null) {
      TensorType tensorType = featureTypeConfig.getTensorType();
      Object coercedDefault = defaultData;
      // For float and double, we need to coerce it to make it more flexible.
      // Otherwise it's quite common to see the two being incompatible.
      // We are doing it here instead of inside FeatureValue.createTensor, since FeatureValue.createTensor is called
      // more frequent and expensive and here it's usually called once during initialization.
      if (tensorType.getDimensionTypes().size() == 0 && defaultData instanceof Number) {
        Number num = (Number) defaultData;
        // for scalar, defaultData is either double, string, or boolean so we need to coerce into corresponding types here.
        if (tensorType.getValueType() == PrimitiveType.FLOAT) {
          coercedDefault = num.floatValue();
        } else if (tensorType.getValueType() == PrimitiveType.DOUBLE) {
          coercedDefault = num.doubleValue();
        } else if (tensorType.getValueType() == PrimitiveType.INT) {
          coercedDefault = num.intValue();
        } else if (tensorType.getValueType() == PrimitiveType.LONG) {
          coercedDefault = num.longValue();
        }
      }

      FeatureValue featureValue = FeatureValue.createTensor(coercedDefault, featureTypeConfig.getTensorType());
      return Optional.of(featureValue);
    } else {
      /*
       * Create a TensorBuilder for FML based feature each time the API is called.
       * The efficiency is not an issue as in most use case, the default FeatureValue will only be resolved once for
       *   each feature, not mentioning that FML is deprecated.
       * It is dangerous to cache the TensorBuilder inside the resolver,
       *   as the feature FML definition might change outside of the resolver.
       * The API user is responsible for caching if the efficiency becomes a concern.
       */
      TensorBuilder legacyTensorBuilder = new TensorBuilder(mlFeatureVersionUrn);
      FeatureTensor featureTensor = legacyTensorBuilder.build(defaultData);
      LOG.debug("Default value is resolved for the FML based tensor feature: {}", mlFeatureVersionUrn);
      FeatureValue featureValue = new FeatureValue(featureTensor);
      return Optional.of(featureValue);
    }
  }

  @VisibleForTesting
  Optional<Object> normalize(Object defaultValue) {
    if (defaultValue instanceof Number) {
      return Optional.of(normalizeNumber(defaultValue));
    } else if (defaultValue instanceof List) {
      return normalizeList(defaultValue);
    } else if (defaultValue instanceof Map) {
      return normalizeMap(defaultValue);
    } else {
      // the rest type (String and Boolean) are directly supported
      return Optional.of(defaultValue);
    }
  }

  private Optional<Object> normalizeList(Object defaultValue) {
    ArrayList<Object> defaultList = new ArrayList<>();

    List<Object> list = (List<Object>) defaultValue;

    for (Object elem : list) {
      if (elem instanceof String) {
        defaultList.add(elem);
      } else if (elem instanceof Number) {
        defaultList.add(normalizeNumber(elem));
      } else if (elem instanceof Boolean) {
        defaultList.add(Boolean.valueOf(elem.toString()));
      } else {
        // value type can only be String or numeric
        LOG.error("List element type not supported when resolving default value: {} .\n"
            + "Only List<String> and List<Numeric> are supported when defining List type default value.", elem);
        return Optional.empty();
      }
    }
    return Optional.of(defaultList);
  }

  private Optional<Object> normalizeMap(Object defaultValue) {
    Map<String, Object> defaultMap = new HashMap<>();
    HashMap<String, Object> map = (HashMap<String, Object>) defaultValue;
    for (String key : map.keySet()) {
      Object valueObj = map.get(key);
      if (valueObj instanceof Number) {
        Number num = (Number) valueObj;
        defaultMap.put(key, num.floatValue());
      } else if (valueObj instanceof Boolean) {
        defaultMap.put(key, Boolean.valueOf(valueObj.toString()));
      } else {
        // The value type can only be numeric
        LOG.error(
            "Only Map<String, Number> type is supported when defining Map typed default value. The value type is not supported: "
                + valueObj);
        return Optional.empty();
      }
    }
    return Optional.of(defaultMap);
  }

  private Double normalizeNumber(Object defaultValue) {
    Number num = (Number) defaultValue;
    return num.doubleValue();
  }
}