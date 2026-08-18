package com.solgas.solgascmsapi.revalidation;

import com.solgas.solgascmsapi.catalog.SiteCatalogChangedEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CatalogMutationInterceptor implements HandlerInterceptor {

    private static final Set<String> MUTATING_METHODS = Set.of("POST", "PUT", "DELETE");
    private static final Pattern SITE_CATALOG_PATH = Pattern.compile("^/api/sites/([^/]+)/(products|images)(?:/.*)?$");

    private final ApplicationEventPublisher events;

    public CatalogMutationInterceptor(ApplicationEventPublisher events) {
        this.events = events;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception exception) {
        if (exception != null || response.getStatus() >= 400) {
            return;
        }
        if (!MUTATING_METHODS.contains(request.getMethod())) {
            return;
        }

        Matcher matcher = SITE_CATALOG_PATH.matcher(request.getRequestURI());
        if (matcher.matches()) {
            events.publishEvent(new SiteCatalogChangedEvent(matcher.group(1)));
        }
    }
}
