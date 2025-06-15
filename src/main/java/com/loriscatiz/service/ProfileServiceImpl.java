package com.loriscatiz.service;

import com.loriscatiz.exception.auth.InvalidCredentialsException;
import com.loriscatiz.exception.badrequest.ConfirmPasswordDoesNotMatchException;
import com.loriscatiz.exception.notfound.UserNotFoundException;
import com.loriscatiz.model.dto.req.profile.ChangePasswordRequest;
import com.loriscatiz.model.dto.res.profile.OwnProfileResponse;
import com.loriscatiz.model.entity.User;
import com.loriscatiz.repo.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ProfileServiceImpl implements ProfileService {
    private static final Logger log = LoggerFactory.getLogger(ProfileServiceImpl.class);
    private final UserRepo userRepo;
    private final PasswordHasher passwordHasher;

    public ProfileServiceImpl(UserRepo userRepo, PasswordHasher passwordHasher) {
        this.userRepo = userRepo;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public void changePassword(String username, ChangePasswordRequest request) {

        User user = getUserOrThrow(username);

        checkPasswordOrThrow(request.oldPassword(), user);

        if (!(request.newPassword().equals(request.confirmNewPassword()))) {
            throw new ConfirmPasswordDoesNotMatchException();
        }

        AuthServiceImpl.checkPasswordStrength(request.newPassword());

        userRepo.updatePasswordHash(username, passwordHasher.hashPassword(request.newPassword()));

    }


    @Override
    public void delete(String username, String password) {

        User user;

        //delete http method should be idempotent
        try {
            user = getUserOrThrow(username);
        } catch (UserNotFoundException e){
            log.info("user deletion attempt but user not found", e);
            return;
        }

        checkPasswordOrThrow(password, user);

        userRepo.deleteUserByUsername(username);

    }


    @Override
    public OwnProfileResponse getInfo(String username) {
        User user = getUserOrThrow(username);

        return new OwnProfileResponse(user.getUsername(),
                user.getRole(),
                user.getJoinedAt().toString(),
                user.getBio(),
                user.getProfileImageUrl());
    }

    private User getUserOrThrow(String username) throws UserNotFoundException {
        Optional<User> queryResult = userRepo.getUserByUsername(username);

        if (queryResult.isEmpty()) {
            throw new UserNotFoundException();
        }

        return queryResult.get();
    }

    private void checkPasswordOrThrow(String passwordInput, User user) {
        if (!passwordHasher.checkPassword(passwordInput, user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }
    }
}
