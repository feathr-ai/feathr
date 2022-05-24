package com.linkedin.frame.core.config.presentation;

import com.linkedin.frame.core.config.ConfigObj;
import java.util.Map;
import java.util.Objects;


/**
 * Represents a presentation config that hosts multiple presentations for multiple features.
 */
public class PresentationsSection implements ConfigObj {
  public static final String PRESENTATIONS = "presentations";

  private final Map<String, PresentationConfig> _presentations;
  private String _configStr;

  public PresentationsSection(Map<String, PresentationConfig> presentations) {
    _presentations = presentations;
  }

  public Map<String, PresentationConfig> getPresentations() {
    return _presentations;
  }

  @Override
  public String toString() {
    if (_configStr == null) {
      _configStr = String.join(":", PRESENTATIONS, _presentations.toString());
    }

    return _configStr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PresentationsSection that = (PresentationsSection) o;
    return Objects.equals(_presentations, that._presentations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_presentations);
  }
}
