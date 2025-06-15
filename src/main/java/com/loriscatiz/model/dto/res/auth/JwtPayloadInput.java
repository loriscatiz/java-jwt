package com.loriscatiz.model.dto.res.auth;

import com.loriscatiz.model.Role;

public record JwtPayloadInput(String username, Role role) {
}
