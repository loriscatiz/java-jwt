package com.loriscatiz.service;

import com.loriscatiz.exception.auth.InvalidCredentialsException;
import com.loriscatiz.exception.badrequest.ConfirmPasswordDoesNotMatchException;
import com.loriscatiz.exception.badrequest.UsernameAlreadyPresentException;
import com.loriscatiz.model.dto.req.auth.LoginRequest;
import com.loriscatiz.model.dto.req.auth.SignUpRequest;
import com.loriscatiz.model.dto.res.auth.JwtPayloadInput;
import com.loriscatiz.model.dto.res.auth.SignupResponse;

public interface AuthService {
    SignupResponse signUp(SignUpRequest request) throws
            UsernameAlreadyPresentException,
            ConfirmPasswordDoesNotMatchException;

    JwtPayloadInput login(LoginRequest request) throws InvalidCredentialsException;

}
