package com.loriscatiz.repo;

import com.loriscatiz.config.DBManager;
import com.loriscatiz.exception.notfound.UserNotFoundException;
import com.loriscatiz.exception.server.DataAccessException;
import com.loriscatiz.model.Role;
import com.loriscatiz.model.entity.User;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepoImpl implements UserRepo {
    private static final Logger log = LoggerFactory.getLogger(UserRepoImpl.class);
    private final DBManager dbManager;

    public UserRepoImpl(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public Optional<User> saveUser(User user) {
        String sql = "INSERT INTO user (username, password_hash, role, joined_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = dbManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql,
                     Statement.RETURN_GENERATED_KEYS);) {

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPasswordHash());
            preparedStatement.setString(3, user.getRole().name());
            Instant now = Instant.now();
            preparedStatement.setTimestamp(4, Timestamp.from(now));

            int rows = preparedStatement.executeUpdate();

            if (rows == 1) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                    user.setJoinedAt(now);
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        }
        catch (SQLIntegrityConstraintViolationException e) {
            //probably duplicate username
            return Optional.empty();
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to save user", e);
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        String sql = "SELECT * FROM user WHERE id = ?";


        try (Connection connection = dbManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            User user = getUser(resultSet);

            return Optional.of(user);

        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve the user", e);
        }
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";


        try (Connection connection = dbManager.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return Optional.empty();
            }

            User user = getUser(resultSet);

            return Optional.of(user);

        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve the user", e);
        }
    }


    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM user";
        List<User> users = new ArrayList<>();

        try (Connection connection = dbManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                users.add(getUser(resultSet));
            }

        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to retrieve all users", e);
        }

        return users;

    }

    @Override
    public Long getUserIdByUsername(String username) {
        String sql = "SELECT id FROM user WHERE username = ?";


        try (Connection connection = dbManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()){
                throw new UserNotFoundException();
            }

            return resultSet.getLong("id");

        }
        catch (SQLException e) {
            throw new DataAccessException();
        }
    }

    @Override
    public void updatePasswordHash(String username, String passwordHash) {
        String sql = "UPDATE user SET password_hash = ? WHERE username = ?";

        try (Connection connection = dbManager.getDataSource().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, passwordHash);
            statement.setString(2, username);

            if(statement.executeUpdate() != 1) {
                throw new UserNotFoundException();
            }

        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to update password", e);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        String sql = "DELETE FROM user WHERE id = ?";

        try(Connection connection = dbManager.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setLong(1, id);

            if( statement.executeUpdate() != 1){
                throw new UserNotFoundException();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to delete user", e);
        }
    }

    @Override
    public void deleteUserByUsername(String username) throws UserNotFoundException, DataAccessException {
        String sql = "DELETE FROM user WHERE username = ?";

        try(Connection connection = dbManager.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, username);

            if( statement.executeUpdate() != 1){
                throw new UserNotFoundException();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("Failed to delete user", e);
        }
    }


    @NotNull
    private static User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        user.setRole(Role.valueOf(resultSet.getString("role")));
        user.setJoinedAt(resultSet.getTimestamp("joined_at").toInstant());
        user.setBio(resultSet.getString("bio"));
        user.setProfileImageUrl(resultSet.getString("profile_image_url"));
        return user;
    }
}
