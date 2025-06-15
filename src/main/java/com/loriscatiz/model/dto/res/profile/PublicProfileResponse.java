package com.loriscatiz.model.dto.res.profile;

import com.loriscatiz.model.Role;

import java.time.Instant;

public record PublicProfileResponse(String username, String bio, String profileImageUrl) {
}
