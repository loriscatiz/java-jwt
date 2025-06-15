package com.loriscatiz.model.dto.req.auth;

public record SignUpRequest(String username, String password, String confirmPassword) {
}
