package com.loriscatiz.controller;

import com.loriscatiz.exception.badrequest.InvalidRequestException;
import com.loriscatiz.model.dto.req.auth.LoginRequest;
import com.loriscatiz.model.dto.req.auth.SignUpRequest;
import com.loriscatiz.model.dto.res.auth.JwtPayloadInput;
import com.loriscatiz.model.dto.res.auth.LoginResponse;
import com.loriscatiz.model.dto.res.auth.SignupResponse;
import com.loriscatiz.service.AuthService;
import com.loriscatiz.service.JWTService;
import io.javalin.Javalin;

public class AuthController {
    private final Javalin app;
    private final AuthService authService;
    private final JWTService jwtService;

    public AuthController(Javalin app, AuthService authService, JWTService jwtService) {
        this.app = app;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    public void register() {
        signUp();
        login();
    }

    private void signUp() {
        app.post("/auth/sign-up", ctx -> {
            SignUpRequest request = ctx.bodyAsClass(SignUpRequest.class);

            if (request == null || request.username() == null || request.password() == null || request.confirmPassword() == null) {
                throw new InvalidRequestException("Missing fields in signup request");
            }
            ctx.json(authService.signUp(request), SignupResponse.class);
            ctx.status(201);
        });


    }

    private void login() {
        app.post("/auth/login", ctx -> {
            LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
            if (request == null || request.username() == null || request.password() == null) {
                throw new InvalidRequestException("Missing fields in login request");
            }

            JwtPayloadInput jwtPayloadInput = authService.login(request);

            String refreshToken = jwtService.createRefreshToken(jwtPayloadInput);
            String accessToken = jwtService.createAccessToken(jwtPayloadInput);


            //todo: set refresh token as http only cookie
            ctx.json(new LoginResponse(refreshToken, accessToken), LoginResponse.class);




        });
    }
}
