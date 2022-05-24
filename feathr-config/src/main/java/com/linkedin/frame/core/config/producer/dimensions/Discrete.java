package com.linkedin.frame.core.config.producer.dimensions;

import java.util.Objects;
import java.util.Optional;

import static com.linkedin.frame.core.utils.Utils.*;


/**
 * Represents a dimension along which the values are discrete. That is, the values are countable but not measurable.
 * The discrete dimension may have lower and/or upper bound on the values along this dimension, specifically, the
 * values will be in the interval [lowerBound, upperBound). If neither bound is specified then the values are countably
 * infinite.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class Discrete extends DimensionType {
  /*
   * These constants are the keys used in the config file for specifying the lower and upper
   * bounds on the values in this dimension.
   */
  public static final String LOWER_BOUND = "lowerBound";
  public static final String UPPER_BOUND = "upperBound";

  // The bounds are optional.
  private final Long _lowerBound;
  private final Long _upperBound;
  private String _str;

  /**
   * Construct Discrete dimension with one or both of lower and upper bound
   * @param lowerBound
   * @param upperBound
   */
  public Discrete(Long lowerBound, Long upperBound) {
    super(DimensionTypeEnum.DISCRETE);

    if (lowerBound != null) {
      require(lowerBound > 0, "Expected positive lower bound, found " + lowerBound);
    }
    if (upperBound != null) {
      require(upperBound > 0, "Expected positive upper bound, found " + upperBound);
    }
    if (lowerBound != null && upperBound != null) {
      // Since upperBound is exclusive, it implies that lowerBound < upperBound must be true
      require(lowerBound < upperBound, "Expected lowerBound < upperBound, found " + lowerBound + " and "
          + upperBound + " respectively");
    }

    _lowerBound = lowerBound;
    _upperBound = upperBound;
  }

  /**
   * Construct Discrete dimension with no bounds
   */
  public Discrete() {
    this(null, null);
  }

  /**
   * Gets the lower bound
   * @return Optional<Long>
   */
  public Optional<Long> getLowerBound() {
    return Optional.ofNullable(_lowerBound);
  }

  /**
   * Gets the upper bound
   * @return Optional<Long>
   */
  public Optional<Long> getUpperBound() {
    return Optional.ofNullable(_upperBound);
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = String.join(", ",
          "Discrete: {" + (_lowerBound != null ? (LOWER_BOUND + ": " + _lowerBound) : ""),
          (_upperBound != null ? (UPPER_BOUND + ": " + _upperBound) : "") + "}");
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
    if (!super.equals(o)) {
      return false;
    }
    Discrete discrete = (Discrete) o;
    return Objects.equals(_lowerBound, discrete._lowerBound) && Objects.equals(_upperBound, discrete._upperBound);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _lowerBound, _upperBound);
  }
}
