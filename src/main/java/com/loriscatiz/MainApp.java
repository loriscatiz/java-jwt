package com.loriscatiz;

import com.loriscatiz.config.DBManager;
import com.loriscatiz.config.Seeder;
import com.loriscatiz.controller.AdminController;
import com.loriscatiz.controller.AuthController;
import com.loriscatiz.controller.ProfileController;
import com.loriscatiz.exception.GlobalExceptionHandler;
import com.loriscatiz.middleware.AuthMiddleware;
import com.loriscatiz.repo.RedisRepo;
import com.loriscatiz.repo.RedisRepoImpl;
import com.loriscatiz.repo.UserRepo;
import com.loriscatiz.repo.UserRepoImpl;
import com.loriscatiz.service.*;
import io.javalin.Javalin;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.protocol.RedisCommand;

public class MainApp {

    private  Javalin app;

    private  GlobalExceptionHandler globalExceptionHandler;

    private DBManager dbManager;
    private Seeder seeder;
    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> redisConnection;
    private RedisCommands<String, String> redis;

    private UserRepo userRepo;
    private RedisRepo redisRepo;

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
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            redisConnection.close();
            redisClient.shutdown();
        }));
    }

    private void start() {
        setUpApp();
        setUpGlobalExceptionHandler();
        setUpDataBase();
        setUpRedis();
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

    private void setUpRedis() {
        redisClient = RedisClient.create("redis://localhost:6379");
        redisConnection = redisClient.connect();
        redis = redisConnection.sync();
    }

    private void setUpRepositories() {
        userRepo = new UserRepoImpl(dbManager);
        redisRepo = new RedisRepoImpl(redis);
    }


    private void setUpServices() {
        passwordHasher = new PasswordHasherImpl();
        jwtService = new JWTServiceImpl();
        authService = new AuthServiceImpl(userRepo, redisRepo, jwtService, passwordHasher);
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
        authController = new AuthController(app, authService);
        profileController = new ProfileController(app, profileService);
        adminController = new AdminController(app, adminService);

        authController.register();
        profileController.register();
        adminController.register();

    }

}

