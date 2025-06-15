package com.loriscatiz.config;

import com.loriscatiz.model.Role;
import com.loriscatiz.model.entity.User;
import com.loriscatiz.repo.UserRepo;
import com.loriscatiz.service.PasswordHasher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;

public class Seeder {
    private static final Logger log = LoggerFactory.getLogger(Seeder.class);
    private final UserRepo userRepo;
    private final PasswordHasher passwordHasher;

    public Seeder(UserRepo userRepo, PasswordHasher passwordHasher) {
        this.userRepo = userRepo;
        this.passwordHasher = passwordHasher;
    }

    public void seedAdminFromEnv() {
        String adminUsername = Config.get("ADMIN_USERNAME");
        String adminPassword = Config.get("ADMIN_PASSWORD");

        if (adminUsername == null || adminPassword == null) {
            log.info("Admin credentials not provided. Skipping seeding.");
            return;
        }

        if (userRepo.getUserByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPasswordHash(passwordHasher.hashPassword(adminPassword));
            admin.setRole(Role.ADMIN);

            userRepo.saveUser(admin);
            log.info("Default admin created.");
        }
    }

}
