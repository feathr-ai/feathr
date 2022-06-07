package com.linkedin.feathr.common.metadata;

import com.linkedin.feathr.core.config.producer.FeatureDefConfig;
import com.linkedin.feathr.core.config.producer.common.DimensionRef;
import com.linkedin.feathr.core.config.producer.common.FeatureRef;
import com.linkedin.feathr.core.config.producer.dimensions.DimensionDefinition;
import com.linkedin.feathr.core.config.producer.features.FeatureDefinition;
import com.linkedin.feathr.core.config.producer.features.FeatureMetadata;
import com.linkedin.feathr.core.configbuilder.ConfigBuilder;
import com.linkedin.feathr.core.configbuilder.ConfigBuilderException;
import com.linkedin.feathr.core.configdataprovider.ConfigDataProvider;
import com.linkedin.feathr.core.configdataprovider.ManifestConfigDataProvider;
import com.linkedin.feathr.core.configdataprovider.ReaderConfigDataProvider;
import com.linkedin.feathr.core.configdataprovider.ResourceConfigDataProvider;
import com.linkedin.feathr.core.configdataprovider.StringConfigDataProvider;
import com.linkedin.feathr.core.configdataprovider.UrlConfigDataProvider;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.Reader;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Interface for obtaining metadata about features and their sources. Instance of a class implementing this interface
 * can be obtained from the static factory methods.
 * The operations provided by this interface aren't multithread-safe. It's the responsibility
 * of the client to ensure MT-safety as necessary.
 */
public interface MetadataProvider {
  String MANIFEST_RESOURCE = "com.linkedin.frame.core.metadata.manifest";

  /**
   * Returns all unique feature references found in the Frame FeatureDef and Metadata configs
   */
  List<FeatureRef> getAllFeatureRefs();

  /**
   * Returns the feature references of all anchored features found in the FeatureDef config
   */
  List<FeatureRef> getAnchoredFeatureRefs();

  /**
   * Returns the feature references of all derived features found in the FeatureDef config
   */
  List<FeatureRef> getDerivedFeatureRefs();

  /**
   * Returns the metadata of the feature. The specified feature can be anchored or derived. If the feature ref
   * is unknown or if it doesn't have a metadata config then an empty {@link Optional} instance is returned.
   * @param featureRef the {@link FeatureRef}
   * @return Optionally returns the {@link FeatureMetadata}
   */
  Optional<FeatureMetadata> getFeatureMetadata(FeatureRef featureRef);

  /**
   * Returns the metadata of all features that provide metadata
   * @return A map of {@link FeatureRef} to {@link FeatureMetadata}.
   */
  Map<FeatureRef, FeatureMetadata> getAllFeatureMetadata();

  /**
   * Returns the definition of a feature specified through its {@link FeatureRef}
   * @param featureRef Feature's reference
   * @return Optionally returns the {@link FeatureDefinition}
   */
  Optional<FeatureDefinition> getFeatureDefinition(FeatureRef featureRef);

  /**
   * Returns the definitions of all features that provide metadata
   * @return A map of {@link FeatureRef} to {@link FeatureDefinition}. If no feature defintions are found
   * then an empty map is returned
   */
  Map<FeatureRef, FeatureDefinition> getAllFeatureDefinitions();

  /**
   * Returns the definition of a dimension specified through its {@link DimensionRef}
   * @param dimensionRef Dimension's reference
   * @return Optionally return the {@link DimensionDefinition}
   */
  Optional<DimensionDefinition> getDimensionDefinition(DimensionRef dimensionRef);

  /**
   * Returns the metadata of the data source associated with a feature. If the source is unknown then an empty
   * {@link Optional} instance is returned.
   * @param sourceName the name of the source
   * @return Optionally returns the {@link com.linkedin.feathr.common.metadata.SourceMetadata}
   */
  Optional<SourceMetadata> getSourceMetadata(String sourceName);

  /*
   * Factory methods to create a MetadataProvider instance
   */

  /**
   * Creates MetadataProvider instance from Frame FeatureDef config data that is provided by a
   * {@link ConfigDataProvider}
   * @param configDataProvider ConfigDataProvider
   * @return MetadataProvider
   * @throws MetadataException
   */
  static MetadataProvider get(ConfigDataProvider configDataProvider) throws MetadataException {
    ConfigBuilder configBuilder = ConfigBuilder.get();

    try {
      FeatureDefConfig config = configBuilder.buildFeatureDefConfig(configDataProvider);
      return new MetadataProviderImpl(config);
    } catch (ConfigBuilderException e) {
      throw new MetadataException("Error creating MetadataProvider instance", e);
    }
  }

  /**
   * Creates MetadataProvider instance from a Frame FeatureDef config file on the classpath.
   * @param resourceName config file name specified as a resource
   * @return MetadataProvider instance
   * @throws {@link MetadataException}
   * @deprecated Please use {@link #get(ConfigDataProvider)}
   */
  @Deprecated
  static MetadataProvider get(String resourceName) throws MetadataException {
    return get(Collections.singletonList(resourceName));
  }

  /**
   * Creates MetadataProvider instance from a list of Frame FeatureDef config files on the classpath.
   * @param resourceNames List of resource names
   * @return MetadataProvider instance
   * @throws {@link MetadataException}
   * @deprecated Please use {@link #get(ConfigDataProvider)}
   */
  @Deprecated
  static MetadataProvider get(List<String> resourceNames) throws MetadataException {
    try (ConfigDataProvider cdp = new ResourceConfigDataProvider(resourceNames)) {
      return get(cdp);
    } catch (Exception e) {
      throw new MetadataException("Error creating MetadataProvider instance", e);
    }
  }

  /**
   * Creates MetadataProvider instance from a URL of Frame FeatureDef config URL.
   * @param url A URL
   * @return MetadataProvider instance
   * @throws {@link MetadataException}
   * @deprecated Please use {@link #get(ConfigDataProvider)}
   */
  @Deprecated
  static MetadataProvider getFromUrl(URL url) throws MetadataException {
    return getFromUrls(Collections.singletonList(url));
  }

  /**
   * Creates MetadataProvider instance from a list of Frame FeatureDef config URLs.
   * @param urls List of URLs
   * @return MetadataProvider instance
   * @throws {@link MetadataException}
   * @deprecated Please use {@link #get(ConfigDataProvider)}
   */
  @Deprecated
  static MetadataProvider getFromUrls(List<URL> urls) throws MetadataException {
    try (ConfigDataProvider cdp = new UrlConfigDataProvider(urls)) {
      return get(cdp);
    } catch (Exception e) {
      throw new MetadataException("Error creating MetadataProvider instance", e);
    }
  }

  /**
   * Creates MetadataProvider instance from a Frame FeatureDef config string.
   * @param configString FeatureDef configuration string
   * @return MetadataProvider instance
   * @throws {@link MetadataException}
   * @deprecated Please use {@link #get(ConfigDataProvider)}
   */
  @Deprecated
  static MetadataProvider getFromString(String configString) throws MetadataException {
    try (ConfigDataProvider cdp = new StringConfigDataProvider(configString)) {
      return get(cdp);
    } catch (Exception e) {
      throw new MetadataException("Error creating MetadataProvider instance", e);
    }
  }

  /**
   * Creates MetadataProvider instance from a character-stream Reader from which the Frame FeatureDef config can be read
   * @param in {@link Reader} instance
   * @return MetadataProvider instance
   * @throws {@link MetadataException}
   * @deprecated Please use {@link #get(ConfigDataProvider)}
   */
  @Deprecated
  static MetadataProvider get(Reader in) throws MetadataException {
    try (ConfigDataProvider cdp = new ReaderConfigDataProvider(in)) {
      return get(cdp);
    } catch (Exception e) {
      throw new MetadataException("Error creating MetadataProvider instance", e);
    }
  }

  /**
   * Creates MetadataProvider instance by reading the manifest that specifies jar's and the conf files. The manifest itself
   * is a HOCON file. It's location is specified in this library's resource.conf file. An example manifest file is
   * shown below:
   *
   * <pre>
   *   {@code
   *   manifest: [
   *     {
   *       jar: local                 // 'local' indicates that the conf files are available as resources in this lib
   *       conf: [feature-prod.conf]
   *     },
   *     {
   *       jar: frame-feature-waterloo-online-1.1.4.jar   // The jar file must be on the class path
   *       conf: [config/online/prod/feature-prod.conf]
   *     }
   *   ]
   *   }
   * </pre>
   * @return MetadataProvider instance
   * @throws MetadataException if the manifest isn't available or if a config error is encountered
   */
  static MetadataProvider get() throws MetadataException {
    /*
     * The resource.conf file is loaded and read using the Lightbend (Typesafe) Config library. The manifest
     * resource name is then passed to the Frame Config library to build the FeatureDefConfig object.
     */

    Config libConfig = ConfigFactory.load();
    String manifestResource;
    if (!libConfig.hasPath(MANIFEST_RESOURCE)) {
      throw new MetadataException("Unable to find manifest file location since the key " + MANIFEST_RESOURCE
      + " isn't defined. Please check that the file resources/reference.conf is defined");
    }
    manifestResource = libConfig.getString(MANIFEST_RESOURCE);

    try (ConfigDataProvider cdp = new ManifestConfigDataProvider(manifestResource)) {
      return get(cdp);
    } catch (Exception e) {
      throw new MetadataException("Error creating MetadataProvider instance", e);
    }
  }
}
