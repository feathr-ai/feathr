package com.linkedin.frame.core.config.producer.common;

import java.io.Serializable;
import java.util.Objects;

import static com.linkedin.frame.core.utils.Utils.*;


/**
 * Represents Version for a feature and dimension. Only major and minor versions are supported.
 */
public class Version implements Serializable {
  /**
   * Default feature major version. This is intended to be used only by those features
   * that don't have a version or haven't defined their versions yet in metadata config
   * files (go/fml).
   */
  public static final int DEFAULT_MAJOR_VERSION = 0;

  /**
   * Default feature minor version. This is intended to be used only by those features
   * that don't have a version or haven't defined their versions yet in metadata config
   * files (go/fml).
   */
  public static final int DEFAULT_MINOR_VERSION = 0;

  /**
   * Default feature patch version. Although patch version isn't supported yet, the default value
   * is used during an MlFeatureVersionUrn construction.
   */
  public static final int DEFAULT_PATCH_VERSION = 0;

  /**
   * Default feature version object
   */
  public static final Version DEFAULT_VERSION_OBJ = new Version(DEFAULT_MAJOR_VERSION, DEFAULT_MINOR_VERSION);

  private final int _major;
  private final int _minor;

  /**
   * Builds Version object from major and minor versions
   * @param major major version
   * @param minor minor version
   */
  public Version(int major, int minor) {
    require(major >= 0 && minor >= 0, "Expected version number to be >= 0, found " + major + "." + minor);

    _major = major;
    _minor = minor;
  }

  /**
   * Builds a Version object from a version string in the format "major.minor".
   * @param versionString
   */
  public Version(String versionString) {
    String[] versionParts = versionString.split("\\.");
    require(versionParts.length == 2, "Expected version format major.minor, found " + versionString);

    _major = Integer.parseInt(versionParts[0]);
    _minor = Integer.parseInt(versionParts[1]);

    require(_major >= 0 && _minor >= 0, "Expected version number to be >= 0, found " + versionString);
  }

  /**
   * Returns the major version number
   * @return int
   */
  public int getMajor() {
    return _major;
  }

  /**
   * Returns the minor version number
   * @return int
   */
  public int getMinor() {
    return _minor;
  }

  @Override
  public String toString() {
    return _major + "." + _minor;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Version version = (Version) o;
    return _major == version._major && _minor == version._minor;
  }

  @Override
  public int hashCode() {
    return Objects.hash(_major, _minor);
  }
}
