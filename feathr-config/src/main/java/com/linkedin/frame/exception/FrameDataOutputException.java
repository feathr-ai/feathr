package com.linkedin.frame.exception;

/**
  * This exception is thrown when the data output is not not successful.
  */
public class FrameDataOutputException extends FrameException {

  public FrameDataOutputException(ErrorLabel errorLabel, String msg, Throwable cause) {
    super(errorLabel, msg, cause);
  }

  public FrameDataOutputException(ErrorLabel errorLabel, String msg) {
    super(errorLabel, msg);
  }
}