package com.linkedin.feathr.offline.fds;

import com.linkedin.feathr.fds.FeaturizedDatasetSchemaVersion;
import java.util.Collections;
import java.util.Set;


/**
 * This class encapsulates information about supported versions of FDS.
 */
public final class FdsVersions {
  private static final FdsVersions _INSTANCE = new FdsVersions();

  /**
   * The latest version of FDS schema currently in use.
   */
  private final FeaturizedDatasetSchemaVersion _latestActiveVersion;

  /**
   * All the currently supported/active versions of FDS schema. These versions can be read by our tools.
   */
  private final Set<FeaturizedDatasetSchemaVersion> _allActiveVersions;

  private FdsVersions() {
    _latestActiveVersion = FeaturizedDatasetSchemaVersion.V1;
    _allActiveVersions = Collections.singleton(FeaturizedDatasetSchemaVersion.V1);
  }

  public static FdsVersions getInstance() {
    return _INSTANCE;
  }

  public FeaturizedDatasetSchemaVersion getLatestActiveVersion() {
    return _latestActiveVersion;
  }

  public Set<FeaturizedDatasetSchemaVersion> getAllActiveVersions() {
    return _allActiveVersions;
  }

  public boolean isASupportedVersion(FeaturizedDatasetSchemaVersion ver) {
    return _allActiveVersions.contains(ver);
  }
}
