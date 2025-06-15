package com.loriscatiz.controller;

import com.loriscatiz.model.dto.req.profile.DeleteAccountRequest;
import com.loriscatiz.model.dto.res.profile.OwnProfileResponse;
import com.loriscatiz.service.ProfileService;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class ProfileController {
    private final Javalin app;
    private final ProfileService profileService;

    public ProfileController(Javalin app, ProfileService profileService) {
        this.app = app;
        this.profileService = profileService;
    }

    public void register(){
        app.get("/profile", ctx -> {

            String username = ctx.attribute("username");

            OwnProfileResponse data = profileService.getInfo(username);

            ctx.json(data, OwnProfileResponse.class);

        });

        app.delete("/profile", ctx -> {

            DeleteAccountRequest request = ctx.bodyAsClass(DeleteAccountRequest.class);

            String username = ctx.attribute("username");

            profileService.delete(username, request.password());

            ctx.status(HttpStatus.NO_CONTENT);
        });
    }
}
