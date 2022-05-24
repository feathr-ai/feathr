package com.linkedin.frame.core.configbuilder.typesafe.producer.common;

import com.linkedin.frame.core.config.producer.common.ResourceRef;
import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import org.apache.log4j.Logger;


/**
 * Builds a {@link ResourceRef}
 */
public class ResourceRefBuilder {
  private static final Logger logger = Logger.getLogger(ResourceRefBuilder.class);

  /*
   * The EBNF for ResourceRef
   */
  // ResourceRef := (LibraryName:)?ResourcePath
  // LibraryName := string    // a syntactically valid qualified name, e.g. foo-1.2.3.jar
  // ResourcePath := (dirName(/dirName)*/)?resourceName   // Note: ResourcePath doesn't begin with a '/'
  // dirName := string          // a syntactically valid qualified name
  // resourceName := string     // a syntactically valid qualified name

  private ResourceRefBuilder() {
  }

  public static ResourceRef build(String resourceRefString) {
    String[] parts = resourceRefString.split(":");
    String resourcePath;
    String libraryName;

    switch (parts.length) {
      case 1:
        libraryName = null;
        resourcePath = parts[0];
        break;

      case 2:
        libraryName = parts[0];
        resourcePath = parts[1];
        break;

      default:
        throw new ConfigBuilderException("Invalid resource reference: " + resourceRefString);
    }

    ResourceRef resourceRef = new ResourceRef(libraryName, resourcePath);

    logger.trace("Built resource ref for " + resourceRefString);

    return resourceRef;
  }
}
