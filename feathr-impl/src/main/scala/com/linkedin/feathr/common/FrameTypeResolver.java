package com.linkedin.feathr.common;

import com.linkedin.feathr.common.metadata.MetadataException;
import com.linkedin.feathr.common.types.PrimitiveType;
import com.linkedin.frame.common.urn.MlFeatureVersionUrn;
import com.linkedin.frame.core.config.producer.common.DimensionRef;
import com.linkedin.frame.core.config.producer.common.FeatureRef;
import com.linkedin.frame.core.config.producer.common.Version;
import com.linkedin.frame.core.config.producer.dimensions.Categorical;
import com.linkedin.frame.core.config.producer.dimensions.DimensionDefinition;
import com.linkedin.frame.core.config.producer.dimensions.Discrete;
import com.linkedin.frame.core.config.producer.dimensions.HashFunction;
import com.linkedin.frame.core.config.producer.dimensions.Hashed;
import com.linkedin.frame.core.config.producer.dimensions.MurmurHash3;
import com.linkedin.frame.core.config.producer.features.FeatureDefinition;
import com.linkedin.frame.core.config.producer.features.ValueType;
import com.linkedin.feathr.common.metadata.MetadataProvider;
import com.linkedin.feathr.common.tensor.BoundedCountType;
import com.linkedin.feathr.common.tensor.CategoricalType;
import com.linkedin.feathr.common.tensor.DimensionType;
import com.linkedin.feathr.common.tensor.EntityType;
import com.linkedin.feathr.common.FeatureUrnUtil;
import com.linkedin.feathr.common.tensor.HashedType;
import com.linkedin.feathr.common.tensor.TensorType;
import com.linkedin.feathr.common.tensor.TextType;
import com.linkedin.feathr.common.tensor.TypeResolver;
import com.linkedin.feathr.common.tensor.UnboundedCountType;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Load all metadata available from Frame Feature Metadata Library (FML) eagerly.
 * This object is quite expensive to create, so applications are advised to keep this instance and share it as much as possible.
 * @deprecated FML is deprecated and the TypeResolver API is deprecated. See PROML-13156.
 */
@Deprecated
public class FrameTypeResolver implements TypeResolver {
  // https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
  private static class LazyHolder {
    static final FrameTypeResolver INSTANCE;

    static {
      try {
        INSTANCE = new FrameTypeResolver();
      } catch (final MetadataException e) {
        throw new ExceptionInInitializerError(e);
      }
    }
  }


  private final Map<String, TensorType> _tensorTypes = new HashMap<>();
  private final Map<String, DimensionType> _dimensionTypes = new HashMap<>();

  public FrameTypeResolver() throws MetadataException {
    MetadataProvider metadataProvider = MetadataProvider.get();
    Map<FeatureRef, FeatureDefinition> allFeatureDefs = metadataProvider.getAllFeatureDefinitions();
    for (Map.Entry<FeatureRef, FeatureDefinition> entry : allFeatureDefs.entrySet()) {
      TensorType tensorType = convertFeatureType(entry.getValue(), _dimensionTypes, metadataProvider);
      try {
        // TODO: remove from here after frame-core provides URNs.
        final FeatureRef featureRef = entry.getKey();

        // unpack
        String namespace = FeatureUrnUtil.DEFAULT_NAMESPACE;
        if (featureRef.getNamespace().isPresent()) {
          namespace = featureRef.getNamespace().get().get();
        }
        int major = FeatureUrnUtil.DEFAULT_MAJOR_VERSION;
        int minor = FeatureUrnUtil.DEFAULT_MINOR_VERSION;
        if (featureRef.getVersion().isPresent()) {
          final Version version = featureRef.getVersion().get();
          major = version.getMajor();
          minor = version.getMinor();
        }
        String urnString = FeatureUrnUtil.toUrnString(namespace, featureRef.getName(), major, minor);
        _tensorTypes.put(urnString, tensorType);
      } catch (URISyntaxException e) {
        throw new MetadataException(e.getMessage());
      }
    }
  }

  @Override
  public Optional<TensorType> resolveFeature(MlFeatureVersionUrn featureVersionUrn) {
    return Optional.ofNullable(_tensorTypes.get(featureVersionUrn.toString()));
  }

  @Override
  public Optional<DimensionType> resolveDimension(String dimensionReference) {
    return Optional.ofNullable(_dimensionTypes.get(dimensionReference));
  }

  /*
   * Convert FeatureDefinition to TensorType.
   * Hashed features require resolving DimensionType - dimensionTypes serves as a cache for resolving dimensions
   * from MetadataProvider.
   */
  private static TensorType convertFeatureType(FeatureDefinition featureDef,
      Map<String, DimensionType> dimensionTypes, MetadataProvider metadataProvider) {
    List<DimensionRef> dimensionRefsList = featureDef.getDimensionRefs();

    int numDimensions = dimensionRefsList.size();
    List<String> dimensionNames = new ArrayList<>(numDimensions);
    List<DimensionType> dimensionTypesList = new ArrayList<>(numDimensions);

    for (DimensionRef dimensionRef : dimensionRefsList) {
      DimensionDefinition dimensionDef = metadataProvider.getDimensionDefinition(dimensionRef).orElseThrow(() ->
          new IllegalStateException("Can't find dimension definition for dimension " + dimensionRef
              + ". Please check if metadata file for dimensions has been provided."));
      String reference = dimensionRef.toString();
      // convertDimensionType() may modify the dimensionTypes map, so cannot use computeIfAbsent().
      DimensionType dimensionType = dimensionTypes.get(reference);
      if (dimensionType == null) {
        dimensionType = convertDimensionType(dimensionDef, dimensionTypes, metadataProvider);
        dimensionTypes.put(reference, dimensionType);
      }
      dimensionTypesList.add(dimensionType);
      dimensionNames.add(reference);
    }
    PrimitiveType primitiveType = getPrimitiveType(featureDef.getValueType());
    return new TensorType(primitiveType, dimensionTypesList, dimensionNames);
  }

  private static DimensionType convertDimensionType(DimensionDefinition dimensionDef,
      Map<String, DimensionType> dimensionTypes, MetadataProvider metadataProvider) {
    DimensionType dimensionType;
    com.linkedin.frame.core.config.producer.dimensions.DimensionType frameDimType = dimensionDef.getDimensionType();

    switch (frameDimType.getType()) {
      case CATEGORICAL:
        Categorical categorical = (Categorical) frameDimType;
        boolean isOrdinal = categorical.getCategoricalType()
            == com.linkedin.frame.core.config.producer.dimensions.CategoricalType.ORDINAL;
        List<String> categories = new ArrayList<>(categorical.getIdToCategoryMap().values());
        // TODO: MII-9796 Migrate categoricals to use maps
        dimensionType = new CategoricalType(dimensionDef.getName(), isOrdinal, categories);
        break;

      case ENTITY:
        dimensionType = new EntityType(dimensionDef.getName());
        break;

      case DISCRETE:
        Optional<Long> upperBound = ((Discrete) frameDimType).getUpperBound();
        dimensionType =
            upperBound.isPresent() ? new BoundedCountType(upperBound.get().intValue()) : new UnboundedCountType();
        break;

      case HASHED:
        Hashed hashed = (Hashed) frameDimType;
        HashFunction hashFunction = hashed.getHashFunction();
        com.linkedin.feathr.common.tensor.HashFunction function;
        if (hashFunction instanceof MurmurHash3) {
          MurmurHash3 murmurHash3 = (MurmurHash3) hashFunction;
          if ("murmurhash3_x86_32".equals(murmurHash3.getFunctionName())) {
            function = HashedType.getMurmur3_32(murmurHash3.getSeed().orElse(null));
          } else if ("murmurhash3_x64_128".equals(murmurHash3.getFunctionName())) {
            function = HashedType.getMurmur3_64(murmurHash3.getSeed().orElse(null));
          } else {
            throw new IllegalArgumentException("Unsupported kind of Murmur3: " + murmurHash3.getFunctionName());
          }
        } else {
          throw new IllegalArgumentException("Unsupported hash function: " + hashFunction);
        }
        String key = hashed.getDimensionRef().toString();
        DimensionType base = dimensionTypes.get(key);
        if (base == null) {
          Optional<DimensionDefinition> baseDimDef = metadataProvider.getDimensionDefinition(hashed.getDimensionRef());
          if (!baseDimDef.isPresent()) {
            throw new IllegalArgumentException("Cannot resolve dimension " + key + ".");
          }
          base = convertDimensionType(baseDimDef.get(), dimensionTypes, metadataProvider);
          dimensionTypes.put(key, base);
        }
        dimensionType = new HashedType(base, hashed.getBound().orElse(null), function, hashed.getHashToNameMap());
        break;

      case TEXT:
        dimensionType =  new TextType();
        break;

      default:
        throw new UnsupportedOperationException(
            "Unsupported dimension type " + frameDimType + " in dimension " + dimensionDef + ".");
    }
    return dimensionType;
  }

  private static PrimitiveType getPrimitiveType(ValueType valueType) {
    switch (valueType) {
      case INT:
        return PrimitiveType.INT;
      case LONG:
        return PrimitiveType.LONG;
      case FLOAT:
        return PrimitiveType.FLOAT;
      case DOUBLE:
        return PrimitiveType.DOUBLE;
      case STRING:
        return PrimitiveType.STRING;
      case BOOLEAN:
        return PrimitiveType.BOOLEAN;
      default:
        throw new UnsupportedOperationException("Cannot convertFeatureType " + valueType + ". Only INT, LONG, FLOAT, "
            + "DOUBLE, BOOLEAN, and STRING are supported.");
    }
  }

  /**
   * Returns shared instance of lazy-loaded FrameTypeResolver.
   *
   * Can hold onto significant amounts of memory. Applications should use the constructor to manually manage lifecycle.
   *
   * @throws NoClassDefFoundError if singleton construction failed.
   */
  public static FrameTypeResolver getInstance() {
    return LazyHolder.INSTANCE;
  }

}