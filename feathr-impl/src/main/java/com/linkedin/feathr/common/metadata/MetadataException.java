package com.linkedin.feathr.common.metadata;


/**
 * The exception thrown when an error is encountered during creating a {@link com.linkedin.feathr.common.metadata.MetadataProvider} instance.
 */
public class MetadataException extends Exception {
  /**
   * Constructor
   * @param msg the error message
   */
  public MetadataException(String msg) {
    super(msg);
  }

  /**
   * Constructor
   * @param msg the error message
   * @param cause the {@link Throwable} instance describing the cause
   */
  public MetadataException(String msg, Throwable cause) {
    super(msg, cause);
  }
}
