package com.loriscatiz.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.exception.auth.InvalidAccessTokenException;
import com.loriscatiz.exception.auth.InvalidRefreshTokenException;
import com.loriscatiz.model.dto.internal.auth.AccessTokenPayloadInput;

public interface JWTService {
    String createAccessToken(AccessTokenPayloadInput jwtPayloadInput);
    String createRefreshToken(String username);
    DecodedJWT getValidAccessToken(String accessToken) throws InvalidAccessTokenException;
    DecodedJWT getValidRefreshToken(String refreshToken) throws InvalidRefreshTokenException;


}
