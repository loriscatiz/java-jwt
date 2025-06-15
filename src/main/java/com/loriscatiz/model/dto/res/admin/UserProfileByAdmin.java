package com.loriscatiz.model.dto.res.admin;

import com.loriscatiz.model.Role;


public record UserProfileByAdmin(Long id, String username, Role role, String joinedAt, String bio, String profileImageUrl) {
}
