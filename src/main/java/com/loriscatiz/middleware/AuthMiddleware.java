package com.loriscatiz.middleware;

import com.auth0.jwt.interfaces.Claim;
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
            isModeratorOrMore(ctx);
        });

        app.before("/admin/*", ctx -> {
            isUserAuthenticated(ctx);
            isAdmin(ctx);
        });
    }


    private void isUserAuthenticated(Context ctx) {
        String accessToken = extractAccessTokenFromAuthHeader(ctx.header("Authorization"));
        DecodedJWT decodedAccessToken = jwtService.getValidAccessToken(accessToken);


        ctx.attribute("username", decodedAccessToken.getSubject());

        Claim roleClaim = decodedAccessToken.getClaim("role");
        ctx.attribute("role", roleClaim.as(Role.class));
    }


    private String extractAccessTokenFromAuthHeader(String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidAccessTokenException();
        }

        return authHeader.split(" ")[1];

    }

    private  void isModeratorOrMore(Context ctx) {
        Role role = ctx.attribute("role");
        if (role != Role.ADMIN && role != Role.MODERATOR) {
            throw new ForbiddenException();
        }
    }

    private  void isAdmin(Context ctx) {
        Role role = ctx.attribute("role");
        if (role != Role.ADMIN) {
            throw new ForbiddenException();
        }
    }
}
