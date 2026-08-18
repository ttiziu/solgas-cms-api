package com.solgas.solgascmsapi.config;

import com.solgas.solgascmsapi.revalidation.CatalogMutationInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final CatalogMutationInterceptor catalogMutationInterceptor;

    public WebConfig(CatalogMutationInterceptor catalogMutationInterceptor) {
        this.catalogMutationInterceptor = catalogMutationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(catalogMutationInterceptor)
                .addPathPatterns(
                        "/api/sites/*/products",
                        "/api/sites/*/products/**",
                        "/api/sites/*/images",
                        "/api/sites/*/images/**");
    }
}
