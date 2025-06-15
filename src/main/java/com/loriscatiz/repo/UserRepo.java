package com.loriscatiz.repo;

import com.loriscatiz.exception.notfound.UserNotFoundException;
import com.loriscatiz.exception.server.DataAccessException;
import com.loriscatiz.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepo {
    Optional<User> saveUser(User user) throws DataAccessException;

    Optional<User> getUserById(Long id) throws DataAccessException;

    Optional<User> getUserByUsername(String username) throws DataAccessException;

    List<User> getAllUsers() throws DataAccessException;

    Long getUserIdByUsername(String username) throws UserNotFoundException, DataAccessException;

    void updatePasswordHash(String username, String passwordHash) throws UserNotFoundException,
            DataAccessException;

    void deleteUserById(Long id) throws UserNotFoundException, DataAccessException;

    void deleteUserByUsername(String username) throws UserNotFoundException, DataAccessException;
}
