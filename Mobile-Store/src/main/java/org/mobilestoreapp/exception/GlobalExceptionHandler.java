package org.mobilestoreapp.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.mobilestoreapp.commons.ProblemDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        logger.warn("Resource not found: {}", ex.getMessage());
        return ProblemDetails.of(HttpStatus.NOT_FOUND, "Resource not found", ex.getMessage(), req);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ProblemDetail handleBadCredentials(BadCredentialsException ex, HttpServletRequest req) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        return ProblemDetails.of(HttpStatus.UNAUTHORIZED, "Authentication failed", "Invalid credentials", req);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ProblemDetail handleUserNotFound(UsernameNotFoundException ex, HttpServletRequest req) {
        logger.warn("User not found during auth: {}", ex.getMessage());
        return ProblemDetails.of(HttpStatus.UNAUTHORIZED, "Authentication failed", "Invalid credentials", req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        logger.warn("Validation failed: {}", errors);
        ProblemDetail pd = ProblemDetails.of(HttpStatus.BAD_REQUEST, "Validation error", "Request validation failed", req);
        pd.setProperty("errors", errors);
        return pd;
    }

    @ExceptionHandler(io.jsonwebtoken.ExpiredJwtException.class)
    public ProblemDetail handleJwtExpired(io.jsonwebtoken.ExpiredJwtException ex, HttpServletRequest req) {
        logger.warn("JWT expired: {}", ex.getMessage());
        return ProblemDetails.of(HttpStatus.UNAUTHORIZED, "Unauthorized", "JWT token has expired. Please log in again.", req);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        logger.error("Invalid argument: {}", ex.getMessage());
        return ProblemDetails.of(HttpStatus.BAD_REQUEST, "Bad request", ex.getMessage(), req);
    }

    @ExceptionHandler(MobileServiceException.class)
    public ProblemDetail handleService(MobileServiceException ex, HttpServletRequest req) {
        logger.error("Service error: {}", ex.getMessage());
        return ProblemDetails.of(HttpStatus.INTERNAL_SERVER_ERROR, "Service error", ex.getMessage(), req);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnexpected(Exception ex, HttpServletRequest req) {
        logger.error("Unexpected error", ex);
        return ProblemDetails.of(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something went wrong, please try again later", req);
    }
}
