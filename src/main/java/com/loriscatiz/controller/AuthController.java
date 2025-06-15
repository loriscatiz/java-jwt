package com.loriscatiz.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.exception.auth.InvalidAccessTokenException;
import com.loriscatiz.exception.badrequest.InvalidRequestException;
import com.loriscatiz.model.dto.req.auth.LoginRequest;
import com.loriscatiz.model.dto.req.auth.RefreshRequest;
import com.loriscatiz.model.dto.req.auth.SignUpRequest;
import com.loriscatiz.model.dto.internal.auth.AccessTokenPayloadInput;
import com.loriscatiz.model.dto.res.auth.TokensResponse;
import com.loriscatiz.model.dto.res.auth.SignupResponse;
import com.loriscatiz.service.AuthService;
import io.javalin.Javalin;

public class AuthController {
    private final Javalin app;
    private final AuthService authService;

    public AuthController(Javalin app, AuthService authService) {
        this.app = app;
        this.authService = authService;
    }

    public void register() {
        signUp();
        login();
        refresh();
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

            TokensResponse response = authService.login(request);



            ctx.json(response, TokensResponse.class);


        });
    }

    private void refresh(){
        app.post("/auth/refresh", ctx -> {
            String refreshToken;

//            if (ctx.cookie("refreshToken") != null) {
//                refreshToken = ctx.cookie("refreshToken");
//            } else {
//                refreshToken = ctx.bodyAsClass(RefreshRequest.class).refreshToken();
//            }
            refreshToken = ctx.bodyAsClass(RefreshRequest.class).refreshToken();



           TokensResponse response = authService.refresh(refreshToken);

           ctx.json(response);


        });
    }




}

