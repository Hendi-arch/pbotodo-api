package com.campus.pbotodo.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CorsSecurityConfig extends OncePerRequestFilter {

    // Allowed HTTP methods
    private static final String[] ALLOWED_METHODS = { "GET", "POST", "PUT", "DELETE", "OPTIONS" };

    // Allowed HTTP headers
    private static final String ALLOWED_HEADERS = "authorization, content-type, xsrf-token";

    // Exposed HTTP headers
    private static final String EXPOSED_HEADERS = "xsrf-token";

    // Max age of preflight requests in seconds
    private static final int MAX_AGE_SECONDS = 3600;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Add the CORS headers to the response
        addCorsHeaders(response);

        // Handle the preflight requests (OPTIONS) by setting the response status to OK
        // and returning
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            // Forward the request to the next filter in the chain
            filterChain.doFilter(request, response);
        }
    }

    // Adds the necessary CORS headers to the response
    private void addCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", String.join(", ", ALLOWED_METHODS));
        response.setHeader("Access-Control-Max-Age", String.valueOf(MAX_AGE_SECONDS));
        response.setHeader("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        response.addHeader("Access-Control-Expose-Headers", EXPOSED_HEADERS);
    }
}
