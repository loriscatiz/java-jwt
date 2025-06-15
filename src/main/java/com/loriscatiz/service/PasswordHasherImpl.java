package com.loriscatiz.service;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasherImpl implements PasswordHasher{
    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean checkPassword(String rawPassword, String passwordHash) {
        return BCrypt.checkpw(rawPassword, passwordHash);
    }
}
