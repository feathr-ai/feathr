package com.linkedin.feathr.core.config.producer.dimensions;

import com.linkedin.feathr.core.config.ConfigObj;
import com.linkedin.feathr.core.config.producer.common.Namespace;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Represents the dimension metadata section in Frame's config. The configuration contains sets of dimension
 * metadata where each set is scoped by a namespace. This object represents these as a map from {@link Namespace}
 * to {@link DimensionMetadata}.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class DimensionSection implements ConfigObj, Iterable<Pair<Namespace, Set<DimensionMetadata>>> {
  private final Map<Namespace, Set<DimensionMetadata>> _dimensionMetadataSets;

  private String _str;

  /**
   * Constructor
   * @param dimensionMetadataSets
   */
  public DimensionSection(Map<Namespace, Set<DimensionMetadata>> dimensionMetadataSets) {
    _dimensionMetadataSets = Collections.unmodifiableMap(dimensionMetadataSets);
  }

  /**
   * Gets set of {@link DimensionMetadata} in the specified {@link Namespace}
   * @param namespace
   * @return Optional<Set<DimensionMetadata>>
   */
  public Optional<Set<DimensionMetadata>> getDimensionMetadataSet(Namespace namespace) {
    return Optional.ofNullable(_dimensionMetadataSets.get(namespace));
  }

  @Override
  public Iterator<Pair<Namespace, Set<DimensionMetadata>>> iterator() {
    return new DimensionMetadataSetIterator();
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = new StringJoiner(", ", DimensionSection.class.getSimpleName() + "[", "]")
          .add("dimensionMetadataSets: " + _dimensionMetadataSets)
          .toString();
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
    DimensionSection that = (DimensionSection) o;
    return Objects.equals(_dimensionMetadataSets, that._dimensionMetadataSets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_dimensionMetadataSets);
  }

  private class DimensionMetadataSetIterator implements Iterator<Pair<Namespace, Set<DimensionMetadata>>> {
    private final Iterator<Map.Entry<Namespace, Set<DimensionMetadata>>> _iterator;

    DimensionMetadataSetIterator() {
      _iterator = _dimensionMetadataSets.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
      return _iterator.hasNext();
    }

    @Override
    public Pair<Namespace, Set<DimensionMetadata>> next() {
      Map.Entry<Namespace, Set<DimensionMetadata>> entry = _iterator.next();

      return Pair.of(entry.getKey(), entry.getValue());
    }
  }
}
