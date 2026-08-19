package com.solgas.solgascmsapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private final int maxAttempts;
    private final long windowMs;
    private final ConcurrentHashMap<String, AttemptWindow> attempts = new ConcurrentHashMap<>();

    public LoginRateLimitFilter(
            @Value("${app.auth.login-rate-limit.max-attempts:10}") int maxAttempts,
            @Value("${app.auth.login-rate-limit.window-seconds:900}") long windowSeconds) {
        this.maxAttempts = maxAttempts;
        this.windowMs = windowSeconds * 1000L;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        if (!isLoginAttempt(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientKey = clientKey(request);
        pruneExpired(clientKey);

        AttemptWindow window = attempts.get(clientKey);
        if (window != null && window.count() >= maxAttempts) {
            writeTooManyRequests(response);
            return;
        }

        filterChain.doFilter(request, response);

        if (response.getStatus() == HttpServletResponse.SC_UNAUTHORIZED) {
            recordFailedAttempt(clientKey);
        }
    }

    private boolean isLoginAttempt(HttpServletRequest request) {
        if (!HttpMethod.POST.matches(request.getMethod())) {
            return false;
        }
        String path = request.getRequestURI();
        return path.endsWith("/api/auth/login") || path.endsWith("/api/auth/signin");
    }

    private void recordFailedAttempt(String clientKey) {
        long now = System.currentTimeMillis();
        attempts.compute(clientKey, (key, existing) -> {
            if (existing == null || now - existing.windowStart() > windowMs) {
                return new AttemptWindow(now, 1);
            }
            return new AttemptWindow(existing.windowStart(), existing.count() + 1);
        });
    }

    private void pruneExpired(String clientKey) {
        AttemptWindow window = attempts.get(clientKey);
        if (window == null) {
            return;
        }
        if (System.currentTimeMillis() - window.windowStart() > windowMs) {
            attempts.remove(clientKey, window);
        }
    }

    private String clientKey(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void writeTooManyRequests(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"message\":\"Demasiados intentos. Espera unos minutos e inténtalo de nuevo.\"}");
    }

    private record AttemptWindow(long windowStart, int count) {
    }
}
