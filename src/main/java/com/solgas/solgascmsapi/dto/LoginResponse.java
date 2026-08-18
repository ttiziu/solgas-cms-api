package com.solgas.solgascmsapi.dto;

public record LoginResponse(String token, String tokenType, long expiresIn) {
}
