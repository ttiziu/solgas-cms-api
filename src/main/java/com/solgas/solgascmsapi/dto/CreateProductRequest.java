package com.solgas.solgascmsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateProductRequest(
        @NotBlank @Size(max = 120)
        @Pattern(regexp = "^[a-z0-9]+(?:-[a-z0-9]+)*$", message = "Usa minúsculas y guiones, ej. balon-10kg")
        String productKey,
        @NotBlank @Size(max = 200) String name,
        @NotBlank String description,
        String whatsappMessage,
        @Size(max = 512) String fallbackImageUrl,
        Integer sortOrder
) {
}
