package org.mobilestoreapp.commons;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.time.OffsetDateTime;

public final class ProblemDetails {
    private ProblemDetails() {
    }

    public static ProblemDetail of(HttpStatus status, String title, String detail, HttpServletRequest request) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(status, detail);
        pd.setTitle(title);
        pd.setProperty("timestamp", OffsetDateTime.now());
        pd.setProperty("path", safePath(request));
        pd.setProperty("correlationId", MDC.get(CorrelationIdFilter.CORRELATION_ID_MDC_KEY));
        return pd;
    }

    private static String safePath(HttpServletRequest request) {
        return request != null ? request.getRequestURI() : "";
    }
}
