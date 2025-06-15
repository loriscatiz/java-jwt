package com.loriscatiz.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.config.Config;
import com.loriscatiz.exception.auth.InvalidRefreshTokenException;
import com.loriscatiz.exception.auth.InvalidAccessTokenException;
import com.loriscatiz.model.Role;
import com.loriscatiz.model.dto.internal.auth.AccessTokenPayloadInput;
import com.loriscatiz.model.dto.internal.auth.RefreshTokenRedisDTO;
import com.loriscatiz.repo.RedisRepo;

import java.time.Instant;
import java.util.UUID;

public class JWTServiceImpl implements JWTService {

    private static final long ACCESS_EXPIRATION_TIME_SECONDS = Long.parseLong(Config.get("JWT_ACCESS_EXPIRY_SECONDS"));
    public static final long REFRESH_EXPIRATION_TIME_SECONDS = Long.parseLong(Config.get("JWT_REFRESH_EXPIRY_SECONDS"));
    private static final String ISSUER = "Loris";


    @Override
    public String createAccessToken(AccessTokenPayloadInput jwtPayloadInput) {
        return JWT.create().withSubject(jwtPayloadInput.username()).withClaim("role", jwtPayloadInput.role().name()).withIssuer(ISSUER).withExpiresAt(Instant.now().plusSeconds(ACCESS_EXPIRATION_TIME_SECONDS)).withIssuedAt(Instant.now()).sign(Algorithm.HMAC256(Config.get("JWT_SECRET")));
    }

    @Override
    public String createRefreshToken(String username) {
        String jti = UUID.randomUUID().toString();

        String retvalue = JWT.create().withSubject(username).withJWTId(jti).withIssuer(ISSUER).withExpiresAt(Instant.now().plusSeconds(REFRESH_EXPIRATION_TIME_SECONDS)).withIssuedAt(Instant.now()).sign(Algorithm.HMAC256(Config.get("JWT_SECRET")));


        return retvalue;
    }

    @Override
    public DecodedJWT getValidAccessToken(String accessToken) {
        DecodedJWT retvalue = validateToken(accessToken);

        String roleClaim = retvalue.getClaim("role").asString();
        boolean isValidRole = false;

        for (Role r : Role.values()){
            if (r.name().equals(roleClaim)) {
                isValidRole = true;
                break;
            }
        }

        if (!isValidRole){
            throw new InvalidAccessTokenException("role is invalid");
        }

        return retvalue;
    }

    @Override
    public DecodedJWT getValidRefreshToken(String refreshToken) {
        DecodedJWT retvalue = validateToken(refreshToken);

        if (retvalue.getId() == null || retvalue.getId().isEmpty()) {
            throw new InvalidRefreshTokenException();
        }

        return retvalue;


    }


    private DecodedJWT validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(Config.get("JWT_SECRET"))).build();
            String username = decodedJWT.getSubject();
            if (username == null || username.isBlank())
                throw new InvalidAccessTokenException("username is missing");
            return jwtVerifier.verify(decodedJWT);
        }
        catch (TokenExpiredException e) {
            throw new InvalidAccessTokenException("token has expired", e);
        }
        catch (AlgorithmMismatchException e) {
            throw new InvalidAccessTokenException("token algorithm mismatch", e);
        }
        catch (SignatureVerificationException e) {
            throw new InvalidAccessTokenException("invalid token signature", e);
        }
        catch (JWTVerificationException e) {
            throw new InvalidAccessTokenException(InvalidAccessTokenException.DEFAULT_MESSAGE, e);
        }
    }

}
