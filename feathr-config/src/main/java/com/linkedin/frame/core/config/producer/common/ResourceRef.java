package com.linkedin.frame.core.config.producer.common;

import com.linkedin.frame.core.config.ConfigObj;
import java.util.Objects;
import java.util.Optional;

import static com.linkedin.frame.core.utils.Utils.*;


/**
 * Represents a reference to a resource which may be local to the enclosing library, or may
 * be available in an external library. If the latter, it's expected that the library is on the
 * class path.
 */
public class ResourceRef implements ConfigObj {
  /*
   * Library name. It's the jar name, typically derived from the module name. e.g. "foo-2.1.3.jar" and "bar-baz-2.5.jar".
   * In future, we'd like to make it simpler by asking for only the prefix (e.g."foo" and "bar-baz" in above examples)
   */
  private final String _libraryName;

  // EBNF is: (dirName(/dirName)*/)?resourceName
  private final String _resourcePath;

  // Used for debugging/logging purposes
  private final String _str;

  /**
   * Builds a ResourceRef object for an external resource
   * @param libraryName external library that contains the resource
   * @param resourcePath path of the resource in the library
   */
  public ResourceRef(String libraryName, String resourcePath) {
    if (libraryName != null) {
      require(libraryName.endsWith(".jar"), "Expected " + libraryName + " to have .jar suffix");
    }

    _libraryName = libraryName;
    _resourcePath = resourcePath;

    _str = String.join(":",
        (_libraryName != null) ? _libraryName : "",
        _resourcePath);
  }

  /**
   * Builds a ResourceRef object for a local resource
   * @param resourcePath
   */
  public ResourceRef(String resourcePath) {
    this(null, resourcePath);
  }

  /**
   * Returns the external library name.
   * @return Optional<String>
   */
  public Optional<String> getLibraryName() {
    return Optional.ofNullable(_libraryName);
  }

  /**
   * Returns the resource's path
   * @return String
   */
  public String getResourcePath() {
    return _resourcePath;
  }

  @Override
  public String toString() {
    return _str;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResourceRef that = (ResourceRef) o;
    return Objects.equals(_libraryName, that._libraryName) && Objects.equals(_resourcePath,
        that._resourcePath);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_libraryName, _resourcePath);
  }
}
