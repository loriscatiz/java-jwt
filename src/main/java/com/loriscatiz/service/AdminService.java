package com.loriscatiz.service;

import com.loriscatiz.model.dto.res.admin.UserProfileByAdmin;

import java.util.List;

public interface AdminService {
    List<UserProfileByAdmin> getUsers();
}
