package com.solgas.solgascmsapi.service;

import com.solgas.solgascmsapi.entity.Site;
import com.solgas.solgascmsapi.exception.UnknownSiteException;
import com.solgas.solgascmsapi.repository.SiteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteService {

    private final SiteRepository siteRepository;

    public SiteService(SiteRepository siteRepository) {
        this.siteRepository = siteRepository;
    }

    public List<Site> list() {
        return siteRepository.findAllByOrderByNameAsc();
    }

    public Site require(String slug) {
        return siteRepository.findById(slug)
                .orElseThrow(() -> new UnknownSiteException(slug));
    }
}
