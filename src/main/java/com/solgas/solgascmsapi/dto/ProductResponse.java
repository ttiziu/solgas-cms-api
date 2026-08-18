package com.solgas.solgascmsapi.dto;

public record ProductResponse(
        String key,
        String name,
        String description,
        String whatsappMessage,
        String imageUrl,
        String fallbackImageUrl,
        int sortOrder,
        boolean active,
        Long cmsImageId
) {
}
