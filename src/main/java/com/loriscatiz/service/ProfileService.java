package com.loriscatiz.service;

import com.loriscatiz.exception.auth.InvalidCredentialsException;
import com.loriscatiz.exception.badrequest.ConfirmPasswordDoesNotMatchException;
import com.loriscatiz.model.dto.req.profile.ChangePasswordRequest;
import com.loriscatiz.model.dto.res.profile.OwnProfileResponse;

public interface ProfileService {
    void changePassword(String username, ChangePasswordRequest request) throws InvalidCredentialsException, ConfirmPasswordDoesNotMatchException;

    void delete(String username, String password);

    OwnProfileResponse getInfo(String username);
}
