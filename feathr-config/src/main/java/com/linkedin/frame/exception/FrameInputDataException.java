package com.linkedin.frame.exception;

/**
  * This exception is thrown when the data input is incorrect.
  */
public class FrameInputDataException extends FrameException {

  public FrameInputDataException(ErrorLabel errorLabel, String msg, Throwable cause) {
    super(errorLabel, msg, cause);
  }

  public FrameInputDataException(ErrorLabel errorLabel, String msg) {
    super(errorLabel, msg);
  }
}