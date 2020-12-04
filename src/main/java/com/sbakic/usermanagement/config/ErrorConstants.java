package com.sbakic.usermanagement.config;

public class ErrorConstants {

  public static final String VALIDATION_ERROR = "VALIDATION_ERROR";
  public static final String BAD_CREDENTIALS = "BAD_CREDENTIALS";
  public static final String CONSTRAINT_VALIDATION = "CONSTRAINT_VALIDATION";
  public static final String NOT_FOUND = "NOT_FOUND";
  public static final Object EMAIL_ALREADY_TAKEN = "EMAIL_ALREADY_TAKEN";

  private ErrorConstants() {
    throw new UnsupportedOperationException("Cannot instantiate");
  }
}
