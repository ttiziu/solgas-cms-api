package com.solgas.solgascmsapi.revalidation;

import com.solgas.solgascmsapi.catalog.SiteCatalogChangedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SiteCatalogRevalidationListener {

    private final WebRevalidationClient client;

    public SiteCatalogRevalidationListener(WebRevalidationClient client) {
        this.client = client;
    }

    @Async
    @EventListener
    public void onCatalogChanged(SiteCatalogChangedEvent event) {
        client.notifySite(event.siteSlug());
    }
}
