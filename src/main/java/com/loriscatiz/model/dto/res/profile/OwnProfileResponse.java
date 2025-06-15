package com.loriscatiz.model.dto.res.profile;

import com.loriscatiz.model.Role;

import java.time.Instant;

public record OwnProfileResponse(String username, Role role, String joinedAt, String bio, String profileImageUrl) {
}
