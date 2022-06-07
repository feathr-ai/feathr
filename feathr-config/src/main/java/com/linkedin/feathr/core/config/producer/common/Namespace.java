package com.linkedin.feathr.core.config.producer.common;

import java.io.Serializable;
import java.util.Objects;


/**
 * Represents a namespace
 */
public class Namespace implements Serializable {
  /**
   * Default feature namespace. This is intended to be used only by those features that
   * haven't yet defined their namespace in the metadata config files (go/fml).
   */
  public static final String DEFAULT_NAMESPACE = "global";

  /**
   * Default feature namespace object
   */
  public static final Namespace DEFAULT_NAMESPACE_OBJ = new Namespace(DEFAULT_NAMESPACE);

  private final String _namespace;

  /**
   * Constructor
   * @param namespace
   */
  public Namespace(String namespace) {
    _namespace = namespace;
  }

  /**
   * Returns the string representation of the namespace
   * @return String
   */
  public String get() {
    return _namespace;
  }

  @Override
  public String toString() {
    return _namespace;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Namespace namespace = (Namespace) o;
    return Objects.equals(_namespace, namespace._namespace);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_namespace);
  }
}
