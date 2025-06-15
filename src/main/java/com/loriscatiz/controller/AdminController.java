package com.loriscatiz.controller;

import com.loriscatiz.model.dto.res.admin.UserProfileByAdmin;
import com.loriscatiz.service.AdminService;
import io.javalin.Javalin;

import java.util.List;

public class AdminController {
    private final Javalin app;
    private final AdminService adminService;

    public AdminController(Javalin app, AdminService adminService) {
        this.app = app;
        this.adminService = adminService;
    }

    public void register(){
        app.get("/admin/users", ctx -> {

            ctx.json(adminService.getUsers());
        });
    }
}
