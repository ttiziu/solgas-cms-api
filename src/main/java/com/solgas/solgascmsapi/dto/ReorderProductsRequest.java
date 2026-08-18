package com.solgas.solgascmsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ReorderProductsRequest(
        @NotEmpty List<@NotBlank String> productKeys
) {
}
