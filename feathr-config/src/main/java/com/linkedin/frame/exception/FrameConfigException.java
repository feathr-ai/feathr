package com.linkedin.frame.exception;

/**
  * This exception is thrown when the feature definition is incorrect.
  */
public class FrameConfigException extends FrameException {

  public FrameConfigException(ErrorLabel errorLabel, String msg, Throwable cause) {
    super(errorLabel, msg, cause);
  }

  public FrameConfigException(ErrorLabel errorLabel, String msg) {
    super(errorLabel, msg);
  }
}