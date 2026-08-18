package com.solgas.solgascmsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @NotBlank @Size(max = 200) String name,
        @NotBlank String description,
        String whatsappMessage,
        @Size(max = 512) String fallbackImageUrl,
        Integer sortOrder,
        Boolean active
) {
}
