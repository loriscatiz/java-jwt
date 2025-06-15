package com.loriscatiz.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.config.Config;
import com.loriscatiz.model.dto.res.auth.JwtPayloadInput;

import java.time.Instant;
import java.util.UUID;

public class JWTServiceImpl implements JWTService{

    private static final long ACCESS_EXPIRATION_TIME_SECONDS = Long.parseLong(Config.get("JWT_ACCESS_EXPIRY_SECONDS"));
    private static final long REFRESH_EXPIRATION_TIME_SECONDS = Long.parseLong(Config.get("JWT_REFRESH_EXPIRY_SECONDS"));
    private static final String ISSUER = "Loris";

    @Override
    public String createAccessToken(JwtPayloadInput jwtPayloadInput) {

        return JWT.create()
                .withSubject(jwtPayloadInput.username())
                .withClaim("role", jwtPayloadInput.role().name())
                .withIssuer(ISSUER)
                .withExpiresAt(Instant.now().plusSeconds(ACCESS_EXPIRATION_TIME_SECONDS))
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(Config.get("JWT_SECRET")));
    }

    @Override
    public String createRefreshToken(JwtPayloadInput jwtPayloadInput) {
        return JWT.create()
                .withSubject(jwtPayloadInput.username())
                .withJWTId(UUID.randomUUID().toString())
                .withIssuer(ISSUER)
                .withExpiresAt(Instant.now().plusSeconds(REFRESH_EXPIRATION_TIME_SECONDS))
                .withIssuedAt(Instant.now())
                .sign(Algorithm.HMAC256(Config.get("JWT_SECRET")));
    }

    @Override
    public DecodedJWT validateToken(String jwt) {
        DecodedJWT decodedJWT = JWT.decode(jwt);
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Config.get("JWT_SECRET"))).build();
        return jwtVerifier.verify(decodedJWT);
    }

}
