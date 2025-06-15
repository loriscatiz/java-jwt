package com.loriscatiz.model.dto.internal.auth;

public record RefreshTokenRedisDTO(String jti, String username, long ttlSeconds) {
}
