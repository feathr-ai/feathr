package com.linkedin.feathr.exception;

/**
  * Base exception for Frame
  */
public class FrameException extends RuntimeException {
  public FrameException(String msg) {
    super(msg);
  }

  public FrameException(String msg, Throwable cause) {
    super(msg, cause);
  }

  public FrameException(ErrorLabel errorLabel, String msg, Throwable cause) {
    super(String.format("[%s]", errorLabel) + " " + msg, cause);
  }

  public FrameException(ErrorLabel errorLabel, String msg) {
    super(String.format("[%s]", errorLabel) + " " + msg);
  }
}