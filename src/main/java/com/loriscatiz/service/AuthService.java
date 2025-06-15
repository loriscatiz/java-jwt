package com.loriscatiz.service;

import com.loriscatiz.exception.auth.InvalidCredentialsException;
import com.loriscatiz.exception.badrequest.ConfirmPasswordDoesNotMatchException;
import com.loriscatiz.exception.badrequest.UsernameAlreadyPresentException;
import com.loriscatiz.model.dto.req.auth.LoginRequest;
import com.loriscatiz.model.dto.req.auth.SignUpRequest;
import com.loriscatiz.model.dto.res.auth.SignupResponse;
import com.loriscatiz.model.dto.res.auth.TokensResponse;

public interface AuthService {
    SignupResponse signUp(SignUpRequest request) throws
            UsernameAlreadyPresentException,
            ConfirmPasswordDoesNotMatchException;

    TokensResponse login(LoginRequest request) throws InvalidCredentialsException;

    TokensResponse refresh(String refreshToken);
}
