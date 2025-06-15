package com.loriscatiz.model.dto.req.profile;

public record ChangePasswordRequest(String oldPassword, String newPassword, String confirmNewPassword) {
}
