package com.solgas.solgascmsapi.revalidation;

import com.solgas.solgascmsapi.config.RevalidateProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Component
public class WebRevalidationClient {

    private static final Logger log = LoggerFactory.getLogger(WebRevalidationClient.class);

    private final RevalidateProperties properties;
    private final RestClient restClient;

    public WebRevalidationClient(RevalidateProperties properties) {
        this.properties = properties;
        this.restClient = RestClient.create();
    }

    public void notifySite(String siteSlug) {
        String secret = properties.getSecret();
        if (secret == null || secret.isBlank()) {
            return;
        }

        String webhookUrls = properties.getWebhooks().get(siteSlug);
        if (webhookUrls == null || webhookUrls.isBlank()) {
            return;
        }

        String tag = "cms-products-" + siteSlug;
        for (String rawUrl : webhookUrls.split(",")) {
            String url = rawUrl.trim();
            if (url.isEmpty()) {
                continue;
            }
            try {
                restClient.post()
                        .uri(url)
                        .header("x-revalidate-secret", secret)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Map.of("tag", tag))
                        .retrieve()
                        .toBodilessEntity();
                log.info("Caché de web invalidada: site={}, url={}", siteSlug, url);
            } catch (Exception exception) {
                log.warn("No se pudo invalidar caché: site={}, url={}, error={}", siteSlug, url, exception.getMessage());
            }
        }
    }
}
