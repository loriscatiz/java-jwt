package com.loriscatiz.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.model.dto.res.auth.JwtPayloadInput;

public interface JWTService {
    String createAccessToken(JwtPayloadInput jwtPayloadInput);
    String createRefreshToken(JwtPayloadInput jwtPayloadInput);
    DecodedJWT validateToken(String token);
}
