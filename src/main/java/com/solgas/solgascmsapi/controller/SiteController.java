package com.solgas.solgascmsapi.controller;

import com.solgas.solgascmsapi.dto.SiteResponse;
import com.solgas.solgascmsapi.service.SiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/sites")
public class SiteController {

    private final SiteService siteService;

    public SiteController(SiteService siteService) {
        this.siteService = siteService;
    }

    @GetMapping
    public List<SiteResponse> list() {
        return siteService.list().stream()
                .map(site -> new SiteResponse(site.getSlug(), site.getName(), site.getPublicUrl()))
                .toList();
    }
}
