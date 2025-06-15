package com.loriscatiz.middleware;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.exception.auth.ForbiddenException;
import com.loriscatiz.exception.auth.InvalidAccessTokenException;
import com.loriscatiz.model.Role;
import com.loriscatiz.service.JWTService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthMiddleware {
    private static final Logger log = LoggerFactory.getLogger(AuthMiddleware.class);
    private final Javalin app;
    private final JWTService jwtService;


    public AuthMiddleware(Javalin app, JWTService jwtService) {
        this.app = app;
        this.jwtService = jwtService;
    }

    public void requireAuthForAdminEndpoints() {
    }

    public void register() {
        app.before("/profile/*", ctx -> {
            isUserAuthenticated(ctx);
        });

        app.before("/moderator/*", ctx -> {
            isUserAuthenticated(ctx);
            Role role = ctx.attribute("role");

            if (role != null && !role.equals(Role.MODERATOR) && !role.equals(Role.ADMIN)) {
                throw new ForbiddenException();
            }
        });

        app.before("/admin/*", ctx -> {
            isUserAuthenticated(ctx);
            Role role = ctx.attribute("role");

            if (role != null && !role.equals(Role.ADMIN))
                throw new ForbiddenException();

        });
    }

    private void isUserAuthenticated(Context ctx) {
        String authHeader = ctx.header("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidAccessTokenException();
        }

        String accessToken = authHeader.split(" ")[1];
        DecodedJWT jwt;

        try {
            jwt = jwtService.validateToken(accessToken);
        }
        catch (TokenExpiredException e) {
            throw new InvalidAccessTokenException("Access token has expired", e);
        }
        catch (AlgorithmMismatchException e) {
            throw new InvalidAccessTokenException("Token algorithm mismatch", e);
        }
        catch (SignatureVerificationException e) {
            throw new InvalidAccessTokenException("Invalid token signature", e);
        }
        catch (JWTVerificationException e) {
            throw new InvalidAccessTokenException(InvalidAccessTokenException.DEFAULT_MESSAGE, e);
        }
        String username = jwt.getSubject();
        if (username == null || username.isBlank()) {
            throw new InvalidAccessTokenException("username is missing");
        }

        String roleClaim = jwt.getClaim("role").asString();

        if (roleClaim == null) {
            throw new InvalidAccessTokenException("role is missing");
        }

        Role role;
        try {
            role = Role.valueOf(roleClaim);
        }
        catch (IllegalArgumentException e) {
            throw new InvalidAccessTokenException("role is invalid");
        }

        ctx.attribute("username", username);
        ctx.attribute("role", role);
    }
}
