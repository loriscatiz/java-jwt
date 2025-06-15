package com.loriscatiz.model.dto.internal.auth;

import com.loriscatiz.model.Role;

public record AccessTokenPayloadInput(String username, Role role) {

}
