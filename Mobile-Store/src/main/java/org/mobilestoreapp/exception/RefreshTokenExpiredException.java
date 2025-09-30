package org.mobilestoreapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
public class RefreshTokenExpiredException extends RuntimeException {
  public RefreshTokenExpiredException(String message) {

    super(message);
  }
}
