package com.linkedin.feathr.common.metadata;

import com.linkedin.frame.core.config.producer.sources.EspressoConfig;
import com.linkedin.frame.core.config.producer.sources.SourceType;
import java.util.Objects;


/**
 * Metadata for Espresso source
 */
public class EspressoMetadata extends SourceMetadata {
  private final String _database;
  private final String _table;
  private String _mdStr;

  /**
   * Constructor
   * @param database Name of the database
   * @param table Name of the table
   */
  EspressoMetadata(String database, String table) {
    super(SourceType.ESPRESSO);
    _database = database;
    _table = table;
  }

  /**
   * Creates Espresso Metadata from {@link EspressoConfig} object
   * @param config EspressoConfig object
   */
  EspressoMetadata(EspressoConfig config) {
    this(config.getDatabase(), config.getTable());
  }

  public String getDatabase() {
    return _database;
  }

  public String getTable() {
    return _table;
  }

  @Override
  public String toString() {
    if (_mdStr == null) {
      _mdStr = super.toString() + ", database: " + _database + ", table: " + _table;
    }

    return _mdStr;
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
    EspressoMetadata that = (EspressoMetadata) o;
    return Objects.equals(_database, that._database) && Objects.equals(_table, that._table);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), _database, _table);
  }
}
