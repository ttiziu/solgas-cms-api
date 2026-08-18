package com.solgas.solgascmsapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "app.revalidate")
public class RevalidateProperties {

    private String secret = "";
    private Map<String, String> webhooks = new HashMap<>();

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret != null ? secret : "";
    }

    public Map<String, String> getWebhooks() {
        return webhooks;
    }

    public void setWebhooks(Map<String, String> webhooks) {
        this.webhooks = webhooks != null ? webhooks : new HashMap<>();
    }
}
