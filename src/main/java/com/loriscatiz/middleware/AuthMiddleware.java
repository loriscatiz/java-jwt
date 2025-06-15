package com.loriscatiz.middleware;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.exception.auth.ForbiddenException;
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

        DecodedJWT jwt = jwtService.getValidAccessToken(authHeader);


        ctx.attribute("username", jwt.getSubject());
        ctx.attribute("role", jwt.getClaim("role"));
    }
}
