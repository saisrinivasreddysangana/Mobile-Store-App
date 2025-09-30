package org.mobilestoreapp.commons;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String inbound = request.getHeader(CORRELATION_ID_HEADER);
        String cid = (inbound == null || inbound.isBlank()) ? UUID.randomUUID().toString() : inbound;

        MDC.put(CORRELATION_ID_MDC_KEY, cid);
        response.setHeader(CORRELATION_ID_HEADER, cid);

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(CORRELATION_ID_MDC_KEY);
        }
    }

}
