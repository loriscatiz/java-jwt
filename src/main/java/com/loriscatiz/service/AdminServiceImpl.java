package com.loriscatiz.service;

import com.loriscatiz.model.dto.res.admin.UserProfileByAdmin;
import com.loriscatiz.model.entity.User;
import com.loriscatiz.repo.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    private final UserRepo userRepo;
    private final PasswordHasher passwordHasher;

    public AdminServiceImpl(UserRepo userRepo, PasswordHasher passwordHasher) {
        this.userRepo = userRepo;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public List<UserProfileByAdmin> getUsers() {
        List<UserProfileByAdmin> retvalue = new ArrayList<>();

        for (User user : userRepo.getAllUsers()){
            retvalue.add(getDtoFromEntity(user))    ;
        }

        return retvalue;
    }

    private UserProfileByAdmin getDtoFromEntity(User user){
        return new UserProfileByAdmin(user.getId(), user.getUsername(), user.getRole(), user.getJoinedAt().toString(), user.getBio(), user.getProfileImageUrl());
    }
}
