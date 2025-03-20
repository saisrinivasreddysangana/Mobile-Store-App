package org.mobilestoreapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MobileServiceException extends RuntimeException {
    public MobileServiceException(String message) {

        super(message);
    }
}
