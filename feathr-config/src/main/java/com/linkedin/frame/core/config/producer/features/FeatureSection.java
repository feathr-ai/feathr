package com.linkedin.frame.core.config.producer.features;

import com.linkedin.frame.core.config.ConfigObj;
import com.linkedin.frame.core.config.producer.common.Namespace;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import org.apache.commons.lang3.tuple.Pair;


/**
 * Represents the feature metadata section in Frame's config. The configuration includes sets of
 * {@link FeatureMetadata} each of which is scoped by a {@link Namespace}. This class represents the
 * configuration via a mapping between Namespace and Set<FeatureMetadata>.
 *
 * @deprecated deprecated as part of the Feed Tensorization: Shifting Strategy. See PROML-13156
 */
@Deprecated
public class FeatureSection implements ConfigObj, Iterable<Pair<Namespace, Set<FeatureMetadata>>> {
  private final Map<Namespace, Set<FeatureMetadata>> _featureMetadataSets;
  private String _str;

  public FeatureSection(Map<Namespace, Set<FeatureMetadata>> featureMetadataSets) {
    _featureMetadataSets = Collections.unmodifiableMap(featureMetadataSets);
  }

  public Optional<Set<FeatureMetadata>> getFeatureMetadataSet(Namespace namespace) {
    return Optional.ofNullable(_featureMetadataSets.get(namespace));
  }

  @Override
  public Iterator<Pair<Namespace, Set<FeatureMetadata>>> iterator() {
    return new FeatureMetadataSetIterator();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FeatureSection pairs = (FeatureSection) o;
    return _featureMetadataSets.equals(pairs._featureMetadataSets);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_featureMetadataSets);
  }

  @Override
  public String toString() {
    if (_str == null) {
      _str = new StringJoiner(", ", FeatureSection.class.getSimpleName() + "[", "]")
          .add("featureMetadataSets: " + _featureMetadataSets)
          .toString();
    }
    return _str;
  }

  private class FeatureMetadataSetIterator implements Iterator<Pair<Namespace, Set<FeatureMetadata>>> {
    private final Iterator<Map.Entry<Namespace, Set<FeatureMetadata>>> _iterator;

    FeatureMetadataSetIterator() {
      _iterator = _featureMetadataSets.entrySet().iterator();
    }

    @Override
    public boolean hasNext() {
      return _iterator.hasNext();
    }

    @Override
    public Pair<Namespace, Set<FeatureMetadata>> next() {
      Map.Entry<Namespace, Set<FeatureMetadata>> entry = _iterator.next();

      return Pair.of(entry.getKey(), entry.getValue());
    }
  }
}
