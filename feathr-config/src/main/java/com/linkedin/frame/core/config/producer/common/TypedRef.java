package com.linkedin.frame.core.config.producer.common;

import com.linkedin.frame.core.configbuilder.ConfigBuilderException;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.linkedin.frame.core.utils.Utils.require;


/**
 * Base class to provide a fully-qualified typed reference via a (namespace, name, version) 3-tuple.
 */
public class TypedRef implements Serializable {
  private final Namespace _namespace;
  private final String _name;
  private final Version _version;
  private String _str;

  /*
   * We use the following four fields to name the capturing groups, for ease of use
   */
  private static final String NAMESPACE = "namespace";
  private static final String NAME = "name";
  private static final String MAJOR = "major";
  private static final String MINOR = "minor";

  /*
   * The delimiter used to separate namespace, name and version fields. It must be chosen such that it doesn't
   * conflict with the restricted characters used in HOCON, Pegasus's PathSpec and the characters used in Java
   * variable names.
   */
  public static final String DELIM = "-";

  // BNF of the typed ref is: (namespace-)?name(-major-minor)?
  public static final String TYPED_REF_BNF = String .join(DELIM, "(namespace", ")?name(", "major", "minor)?");

  /*
   * For all of the regex's below, the outer group where applicable, is made non-capturing by using "?:" construct.
   * This is done since we want to extract only "foo" in "foo-". Also, we use named-capturing groups by using "?<name>"
   * construct. This is done for ease of reference when getting the matched value of the group.
   */

  // Represents the regex for (namespace-)?
  private static final String NAMESPACE_REGEX = "(?:(?<" + NAMESPACE + ">[a-zA-Z][\\w]+)" + DELIM + ")?";

  // Represents the regex for name
  // Note: We shouldn't allow '.' or ':' in name, but in some legacy feature names, "." or ":" are being used.
  // Build validation project will gradually migrate these legacy feature names off from using special characters,
  // when a clean state is reached, we should remove these special characters from the regex.
  private static final String NAME_REGEX = "(?<" + NAME + ">[a-zA-Z][.:\\w]*)";
  private static final String STRICT_NAME_REGEX = "(?<" + NAME + ">[a-zA-Z][\\w]*)";

  // Represents the regex for only feature name
  private static final String FEATURE_NAME_REGEX = "([a-zA-Z][.:\\w]*)";

  // Represents regex for (-major-minor)?
  private static final String VERSION_REGEX = "((?:" + DELIM + "(?<" + MAJOR + ">[\\d]+))(?:" + DELIM + "(?<" + MINOR + ">[\\d]+)))?";

  private static final String TYPED_REF_REGEX = NAMESPACE_REGEX + NAME_REGEX + VERSION_REGEX;
  private static final Pattern TYPED_REF_PATTERN = Pattern.compile(TYPED_REF_REGEX);

  private static final String STRICT_TYPED_REF_REGEX = "^" + NAMESPACE_REGEX + STRICT_NAME_REGEX + VERSION_REGEX + "$";
  public static final Pattern STRICT_TYPED_REF_PATTERN = Pattern.compile(STRICT_TYPED_REF_REGEX);
  public static final Pattern FEATURE_NAME_PATTERN = Pattern.compile(FEATURE_NAME_REGEX);

  /**
   * Constructor
   * @param namespace namespace of the referenced object
   * @param name name of the referenced object
   * @param major major version of the referenced object
   * @param minor minor version of the referenced object
   */
  TypedRef(String namespace, String name, int major, int minor) {
    require(name != null, "Name of referenced object can't be null");

    _namespace = namespace != null ? new Namespace(namespace) : null;
    _name = name;
    _version = new Version(major, minor);
  }

  /**
   * Constructor
   * @param namespace namespace of the referenced object
   * @param name name of the referenced object
   * @param version version of the referenced object
   */
  TypedRef(Namespace namespace, String name, Version version) {
    require(name != null, "Name of referenced object can't be null");

    _namespace = namespace;
    _name = name;
    _version = version;
  }

  /**
   * Constructor. If the reference string doesn't contain a namespace, the TypedRef object is created with
   * the parent namespace.
   * @param parentNamespace A parent namespace, assigned if the reference string doesn't contain one.
   * @param refString The reference string from which TypedRef is created.
   */
  TypedRef(Namespace parentNamespace, String refString) {
    Matcher matcher = TYPED_REF_PATTERN.matcher(refString);

    if (matcher.matches()) {
      String namespaceStr = matcher.group(NAMESPACE);

      _namespace = namespaceStr != null ? new Namespace(namespaceStr) : parentNamespace;

      _name = matcher.group(NAME);
      require(_name != null, "Name can't be null in the typed reference " + refString);

      String majorString = matcher.group(MAJOR);
      String minorString = matcher.group(MINOR);
      require((majorString != null) == (minorString != null), "Expected either both major and minor versions or "
          + "neither, found only one of them in " + refString);

      if (majorString != null) {  // at this point minorString will be non-null if majorString is non-null
        int major = Integer.parseInt(majorString);
        int minor = Integer.parseInt(minorString);
        _version = new Version(major, minor);
      } else {
        _version = null;
      }
    } else {
      throw new ConfigBuilderException("Invalid typed reference " + refString
          + ", expected pattern (in BNF syntax) " + TYPED_REF_BNF);
    }
  }

  /**
   * Constructor that can be used to construct from a string representation. Possible representations:
   * namespace/name/major/minor, name/major/minor, namespace/name, name. The last representation (that is,
   * name), is for legacy features that don't have namespace or versions defined.
   * @param refString A string representation of the reference
   */
  TypedRef(String refString) {
    this(null, refString);
  }

  /**
   * Get the namespace of the referenced object
   * @return Optionally returns the namespace
   */
  public Optional<Namespace> getNamespace() {
    return Optional.ofNullable(_namespace);
  }

  /**
   * Get the name of the referenced object
   * @return name of the referenced object
   */
  public String getName() {
    return _name;
  }

  /**
   * Get the version of the referenced object
   * @return {@link Version}
   */
  public Optional<Version> getVersion() {
    return Optional.ofNullable(_version);
  }

  @Override
  public String toString() {
    if (_str == null) {
      StringBuilder bldr = new StringBuilder();

      if (_namespace != null) {
        bldr.append(_namespace).append(DELIM);
      }

      bldr.append(_name);

      if (_version != null) {
        bldr.append(DELIM).append(_version.getMajor()).append(DELIM).append(_version.getMinor());
      }

      _str = bldr.toString();
    }

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
    TypedRef typedRef = (TypedRef) o;
    return Objects.equals(_namespace, typedRef._namespace) && Objects.equals(_name, typedRef._name) && Objects.equals(
        _version, typedRef._version);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_namespace, _name, _version);
  }
}
