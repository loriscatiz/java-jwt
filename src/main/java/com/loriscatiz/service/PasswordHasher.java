package com.loriscatiz.service;

public interface PasswordHasher {
    String hashPassword(String password);
    boolean checkPassword(String rawPassword, String passwordHash);
}
