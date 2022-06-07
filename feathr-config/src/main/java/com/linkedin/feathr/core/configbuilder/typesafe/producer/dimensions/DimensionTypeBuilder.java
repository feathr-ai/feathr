package com.linkedin.feathr.core.configbuilder.typesafe.producer.dimensions;

import com.linkedin.feathr.core.configbuilder.typesafe.producer.common.ResourceRefBuilder;
import com.linkedin.feathr.core.config.producer.common.DimensionRef;
import com.linkedin.feathr.core.config.producer.common.Namespace;
import com.linkedin.feathr.core.config.producer.common.ResourceRef;
import com.linkedin.feathr.core.config.producer.common.TypedRef;
import com.linkedin.feathr.core.config.producer.dimensions.Categorical;
import com.linkedin.feathr.core.config.producer.dimensions.CategoricalType;
import com.linkedin.feathr.core.config.producer.dimensions.Continuous;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionType;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionTypeEnum;
import com.linkedin.feathr.core.config.producer.dimensions.Discrete;
import com.linkedin.feathr.core.config.producer.dimensions.Entity;
import com.linkedin.feathr.core.config.producer.dimensions.HashFunction;
import com.linkedin.feathr.core.config.producer.dimensions.Hashed;
import com.linkedin.feathr.core.config.producer.dimensions.Text;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueFactory;
import com.typesafe.config.ConfigValueType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.jar.JarFile;
import java.util.stream.Stream;
import org.apache.log4j.Logger;

import static com.linkedin.feathr.core.config.producer.dimensions.Categorical.*;
import static com.linkedin.feathr.core.config.producer.dimensions.DimensionTypeEnum.*;
import static com.linkedin.feathr.core.config.producer.dimensions.Discrete.*;
import static com.linkedin.feathr.core.config.producer.dimensions.Hashed.*;
import static com.linkedin.feathr.core.utils.Utils.*;


/**
 * Builds the various dimension types
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
class DimensionTypeBuilder {
  private static final Logger logger = Logger.getLogger(DimensionTypeBuilder.class);

  /*
   * Category names are implicitly or explicitly mapped to numeric IDs (longs). Similarly, some dimensions
   * (e.g. Entity) may have their values hashed. The mapping from ID/Hashed value to Name is specified in
   * a csv file. Also, comments are allowed, and are marked with the standard '//'.
   * The constants below specify the comment marker and the field separator.
   */
  private static final String COMMENT = "//";         // comments start with '//'
  private static final String SEPARATOR = ",";        // fields in id mapping file are comma separated

  private DimensionTypeBuilder() {
  }

  /*
   * ConfigValue represents the value part in:
   * type: ...
   * The value may be a string (representing the dimension type name) or it may be an object (representing the config
   * of the dimension type)
   */
  static DimensionType build(Namespace dimNamespace, String dimName, ConfigValue dimTypeValue) {
    ConfigValueType valueType = dimTypeValue.valueType();

    DimensionType dimType;
    switch (valueType) {
      case STRING:
        String dimTypeName = (String) dimTypeValue.unwrapped();   // e.g "DISCRETE"
        DimensionTypeEnum dimTypeEnum = getDimensionTypeEnum(dimTypeName);

        switch (dimTypeEnum) {
          case DISCRETE:
            dimType = new Discrete();
            break;

          case CONTINUOUS:
            dimType = new Continuous();
            break;

          case TEXT:
            dimType = new Text();
            break;

          case ENTITY:
            dimType = new Entity();
            break;

          default:
            String expDimTypeEnums = String.join(" | ", DISCRETE.toString(), CONTINUOUS.toString(), TEXT.toString(),
                ENTITY.toString());
            throw new ConfigBuilderException("Expected dimension type " + expDimTypeEnums + ", found " + dimTypeEnum);
        }
        break;

      case OBJECT:
        /*
         * The value type is an OBJECT, and therefore when the config value is unwrapped it returns
         * a Map<String, Object>. As such we can safely cast the object to Map<String, Object>.
         */
        @SuppressWarnings("Unchecked cast from Object to Map<String, Object>")
        Map<String, Object> map = (Map<String, Object>) dimTypeValue.unwrapped();

        // Obtain the ConfigObject and thence the Config
        ConfigObject dimTypeCfgObj = ConfigValueFactory.fromMap(map);
        Config dimTypeCfg = dimTypeCfgObj.toConfig();                 // value part in type: { ... }
        dimTypeName = dimTypeCfgObj.keySet().iterator().next();       // e.g. "categorical"
        dimTypeEnum = getDimensionTypeEnum(dimTypeName);

        switch (dimTypeEnum) {
          case DISCRETE:
            Config discreteCfg = dimTypeCfg.getConfig(dimTypeName);   // value part in discrete: { ... }
            dimType = buildDiscrete(discreteCfg);
            break;

          case CATEGORICAL:
            Config catCfg = dimTypeCfg.getConfig(dimTypeName);        // value part in categorical: { ... }
            dimType = buildCategorical(catCfg);
            break;

          case HASHED:
            Config hashedCfg = dimTypeCfg.getConfig(dimTypeName);     // value part in hashed: { ... }
            dimType = buildHashed(dimNamespace, hashedCfg);
            break;

          default:
            String expDimTypeEnums = String.join(" | ", DISCRETE.toString(), CATEGORICAL.toString(), HASHED.toString());
            throw new ConfigBuilderException("Expected dimension type " + expDimTypeEnums + ", found " + dimTypeEnum);
        }
        break;

        default:
          throw new ConfigBuilderException("Expected String or Object for dimension type value, found " + valueType);
    }

    logger.trace("Built dimension type object for dimension " + dimNamespace + TypedRef.DELIM + dimName);

    return dimType;
  }

  private static Discrete buildDiscrete(Config discreteCfg) {
    Long lowerBound = null;
    Long upperBound = null;

    if (discreteCfg.hasPath(LOWER_BOUND)) {
      lowerBound = discreteCfg.getLong(LOWER_BOUND);
    }

    if (discreteCfg.hasPath(UPPER_BOUND)) {
      upperBound = discreteCfg.getLong(UPPER_BOUND);
    }

    return new Discrete(lowerBound, upperBound);
  }

  private static Categorical buildCategorical(Config catCfg) {
    String catTypeName = catCfg.getString(CATEGORICAL_TYPE);      // e.g "NOMINAL"
    CategoricalType catType = getCategoricalType(catTypeName);
    String idMappingRefString = catCfg.getString(ID_MAPPING_REF);
    ResourceRef idMappingRef = ResourceRefBuilder.build(idMappingRefString);

    // Build the map of ID -> category name by delegating to a helper method.
    Map<Long, String> idToCategoryMap = buildMap(idMappingRef);
    logger.trace("idToCategoryMap: \n" + string(idToCategoryMap, "\n"));

    // Validate that the map has 0 -> OUT_OF_VOCAB mapping
    if (!(idToCategoryMap.containsKey(0L) && idToCategoryMap.get(0L).equals(OUT_OF_VOCAB))) {
      throw new ConfigBuilderException("Didn't find " + OUT_OF_VOCAB + " row, specifically: 0, " + OUT_OF_VOCAB);
    }

    return new Categorical(catType, idMappingRef, idToCategoryMap);
  }

  private static Hashed buildHashed(Namespace dimNamespace, Config hashedConfig) {
    Config hashFunctionConfig = hashedConfig.getConfig(HASH_FUNCTION);
    HashFunction hashFunction = HashFunctionBuilder.build(hashFunctionConfig);

    Integer bound = hashedConfig.hasPath(BOUND) ? hashedConfig.getInt(BOUND) : null;

    String dimRefStr = hashedConfig.getString(DIM_REF);
    DimensionRef dimRef = new DimensionRef(dimNamespace, dimRefStr);

    String resourceRef = hashedConfig.getString(HASH_TO_NAME_REF);
    ResourceRef hashToNameRef = ResourceRefBuilder.build(resourceRef);

    // Build the map of Hash -> Name by delegating to a helper method.
    Map<Long, String> hashToNameMap = buildMap(hashToNameRef);
    logger.trace("hashToNameMap: \n" + string(hashToNameMap, "\n"));

    return new Hashed(hashFunction, bound, dimRef, hashToNameRef, hashToNameMap);
  }

  private static DimensionTypeEnum getDimensionTypeEnum(String dimTypeName) {
    Optional<DimensionTypeEnum> dimTypeOpt = DimensionTypeEnum.fromName(dimTypeName);
    if (!dimTypeOpt.isPresent()) {
      throw new ConfigBuilderException("Unsupported/unknown dimension type " + dimTypeName);
    }
    return dimTypeOpt.get();
  }

  private static CategoricalType getCategoricalType(String catTypeName) {
    Optional<CategoricalType> catTypeOpt = CategoricalType.fromName(catTypeName);
    if (!catTypeOpt.isPresent()) {
      throw new ConfigBuilderException("Unsupported/unknown categorical type " + catTypeName);
    }
    return catTypeOpt.get();
  }

  /*
   * Read the file using the provided reference to file, and then build the map of ID -> name.
   */
  private static Map<Long, String> buildMap(ResourceRef resourceRef) {
    Map<Long, String> map;

    String resource = resourceRef.getResourcePath();

    Optional<String> libNameOpt = resourceRef.getLibraryName();

    // If an external jar is specified, read the file from the jar else read it from the class path
    try {
      if (libNameOpt.isPresent()) {
        String jarFileName = libNameOpt.get();

        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL jarFileUrl = loader.getResource(jarFileName);
        if (jarFileUrl == null) {
          throw new ConfigBuilderException("Error: couldn't load/locate resource " + jarFileName);
        }

        // delegate to a helper method
        map = buildMap(jarFileUrl, resource);
      } else {
        // delegate to a helper method
        map = buildMap(resource);
      }
    } catch (Exception e) {
      throw new ConfigBuilderException("Error in reading idMapping file " + resourceRef, e);
    }

    return map;
  }

  private static Map<Long, String> buildMap(URL jarFileUrl, String resource) throws IOException {
    Map<Long, String> map;

    String path = jarFileUrl.getPath();

    /*
     * To get the resource from the jar, create a JarFile which is then used to get InputStream.
     * Wrap the InputStream successively, and ending up with a BufferedReader which is used
     * to get the Stream<String>.
     */
    try (JarFile jarFile = new JarFile(path);
        InputStream in = jarFile.getInputStream(jarFile.getJarEntry(resource));
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Stream<String> lines = reader.lines()
    ) {
      // Once we get the Stream of lines, we delegate the task of building the map to a helper method
      map = buildMap(lines);
    }
    logger.trace("Built the ID -> category name from " + path.substring(path.lastIndexOf("/") + 1) + ":" + resource);

    return map;
  }

  private static Map<Long, String> buildMap(String resource) throws IOException {
    Map<Long, String> map;

    // Use the current thread's context class loader to load the resource
    ClassLoader loader = Thread.currentThread().getContextClassLoader();

    try (InputStream in = loader.getResourceAsStream(resource);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        Stream<String> lines = reader.lines()
    ) {
      // Once we get the Stream of lines, we delegate the task of building the map to a helper method
      map = buildMap(lines);
    }
    logger.trace("Built the ID -> category name from " + resource);

    return map;
  }

  /*
   * Build a map of id -> category name from the given stream of lines
   *
   * Example of a file with IDs and names.
   *
   * // This is a comment
   * // So is this
   * // 0 is reserved for mapping to OUT_OF_VOCAB, that is, unknown category names
   * 0,   OUT_OF_VOCAB
   * 789, apple
   * 456, banana
   * 123, pear
   */
  private static Map<Long, String> buildMap(Stream<String> lines) {
    Map<Long, String> map = new HashMap<>();
    HashSet<Long> idSet = new HashSet<>();      // used for ensuring unique IDs
    HashSet<String> nameSet = new HashSet<>();  // used for ensuring unique names

    // filter out lines with comments and then process each line
    lines.filter(line -> !line.trim().startsWith(COMMENT)).forEach(line -> {
      String[] parts = line.split(SEPARATOR);
      if (parts.length == 2) {
        Long id = Long.parseLong(parts[0].trim());
        String name = parts[1].trim();

        // Validate that id and name are unique
        if (!idSet.contains(id)) {
          idSet.add(id);
        } else {
          throw new ConfigBuilderException("Duplicate id found: " + id);
        }

        if (!nameSet.contains(name)) {
          nameSet.add(name);
        } else {
          throw new ConfigBuilderException("Duplicate name found: " + name);
        }

        map.put(id, name);
      } else {
        throw new ConfigBuilderException("Invalid row \"" + line + "\", expected format <id, name>");
      }
    });

    return map;
  }
}
