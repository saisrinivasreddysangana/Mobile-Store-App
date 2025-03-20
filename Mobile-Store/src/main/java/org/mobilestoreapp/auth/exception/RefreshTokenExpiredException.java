package org.mobilestoreapp.auth.exception;

public class RefreshTokenExpiredException extends RuntimeException {
  public RefreshTokenExpiredException(String message) {

    super(message);
  }
}
