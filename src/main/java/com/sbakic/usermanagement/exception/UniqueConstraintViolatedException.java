package com.sbakic.usermanagement.exception;

public class UniqueConstraintViolatedException extends RuntimeException {

  public UniqueConstraintViolatedException() {
  }

  public UniqueConstraintViolatedException(String message) {
    super(message);
  }

  public UniqueConstraintViolatedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UniqueConstraintViolatedException(Throwable cause) {
    super(cause);
  }

  public UniqueConstraintViolatedException(String message, Throwable cause,
      boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
