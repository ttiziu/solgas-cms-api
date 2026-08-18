package com.solgas.solgascmsapi.dto;

import com.solgas.solgascmsapi.entity.ImageAsset;

import java.time.Instant;

public record ImageResponse(Long id, String url, String site, String section, Instant createdAt) {

    public static ImageResponse from(ImageAsset asset) {
        String siteSlug = asset.getSite() == null ? null : asset.getSite().getSlug();
        return new ImageResponse(asset.getId(), asset.getUrl(), siteSlug, asset.getSection(), asset.getCreatedAt());
    }
}
