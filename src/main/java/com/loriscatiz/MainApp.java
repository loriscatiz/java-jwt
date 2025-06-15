package com.loriscatiz;

import com.loriscatiz.config.DBManager;
import com.loriscatiz.config.Seeder;
import com.loriscatiz.controller.AdminController;
import com.loriscatiz.controller.AuthController;
import com.loriscatiz.controller.ProfileController;
import com.loriscatiz.exception.GlobalExceptionHandler;
import com.loriscatiz.middleware.AuthMiddleware;
import com.loriscatiz.repo.UserRepo;
import com.loriscatiz.repo.UserRepoImpl;
import com.loriscatiz.service.*;
import io.javalin.Javalin;

public class MainApp {

    private  Javalin app;

    private GlobalExceptionHandler globalExceptionHandler;

    private DBManager dbManager;
    private Seeder seeder;

    private UserRepo userRepo;

    private PasswordHasher passwordHasher;
    private JWTService jwtService;
    private AuthService authService;
    private ProfileService profileService;
    private AdminService adminService;


    private AuthMiddleware authMiddleware;

    private AuthController authController;
    private ProfileController profileController;
    private AdminController adminController;

    public static void main(String[] args) {
        new MainApp().start();
    }

    private void start() {
        setUpApp();
        setUpGlobalExceptionHandler();
        setUpDataBase();
        setUpRepositories();
        setUpServices();
        seedData();
        setUpMiddleware();
        setUpControllers();
        app.start();
    }


    private void setUpApp() {
        app = Javalin.create();
    }

    private void setUpGlobalExceptionHandler() {
        globalExceptionHandler = new GlobalExceptionHandler(app);

        globalExceptionHandler.register();
    }

    private void setUpDataBase() {
        dbManager = new DBManager();
    }

    private void setUpRepositories() {
        userRepo = new UserRepoImpl(dbManager);
    }

    private void setUpServices() {
        passwordHasher = new PasswordHasherImpl();
        jwtService = new JWTServiceImpl();
        authService = new AuthServiceImpl(userRepo, passwordHasher);
        profileService = new ProfileServiceImpl(userRepo, passwordHasher);
        adminService = new AdminServiceImpl(userRepo, passwordHasher);

    }

    private void seedData() {
        new Seeder(userRepo, passwordHasher).seedAdminFromEnv();
    }

    private void setUpMiddleware() {
        authMiddleware = new AuthMiddleware(app, jwtService);

        authMiddleware.register();
    }


    private void setUpControllers() {
        authController = new AuthController(app, authService, jwtService);
        profileController = new ProfileController(app, profileService);
        adminController = new AdminController(app, adminService);

        authController.register();
        profileController.register();
        adminController.register();

    }

}
