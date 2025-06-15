package com.loriscatiz.model.dto.res.auth;

import com.loriscatiz.model.Role;

public record SignupResponse(String username, Role role, String joinedAt) {
}
