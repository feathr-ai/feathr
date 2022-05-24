package com.linkedin.frame.core.config.producer.dimensions;

import java.util.Optional;


/**
 * Enum for supported dimension types
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public enum DimensionTypeEnum {
  /**
   * Data along this dimension is countable but may be countably infinite
   */
  DISCRETE,

  /**
   * Data along this dimension is real-valued
   */
  CONTINUOUS,

  /**
   * Data along this dimension represent characteristics and is further classified as Ordinal and Nominal
   */
  CATEGORICAL,

  /**
   * Data along this dimension represent entities such as job, member, company, etc.
   */
  ENTITY,

  /**
   * Data along this dimension represents text in NLP use cases
   */
  TEXT,

  /**
   * Data along this dimension has been hashed
   */
  HASHED;

  public static Optional<DimensionTypeEnum> fromName(String name) {
    DimensionTypeEnum res = null;

    for (DimensionTypeEnum dimTypeEnum : values()) {
      String dimTypeName = dimTypeEnum.name();
      if (name.equalsIgnoreCase(dimTypeName)) {
        res = dimTypeEnum;
        break;
      }
    }

    return Optional.ofNullable(res);
  }
}
