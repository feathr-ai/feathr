package com.linkedin.frame.core.config.producer.dimensions;

import java.util.Optional;


/**
 * Enumeration for Categorical types - Ordinal and Nominal.
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public enum CategoricalType {
  ORDINAL,    // values are ordered, for example: small, medium, large
  NOMINAL;    // values don't have any ordering, for example: apple, banana, orange

  public static Optional<CategoricalType> fromName(String name) {
    CategoricalType res = null;

    for (CategoricalType type : values()) {
      if (type.name().equalsIgnoreCase(name)) {
        res = type;
        break;
      }
    }

    return Optional.ofNullable(res);
  }
}
