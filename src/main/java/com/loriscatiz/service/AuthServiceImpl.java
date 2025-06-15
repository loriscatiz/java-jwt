package com.loriscatiz.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.loriscatiz.exception.auth.InvalidCredentialsException;
import com.loriscatiz.exception.badrequest.ConfirmPasswordDoesNotMatchException;
import com.loriscatiz.exception.badrequest.PasswordNotStrongEnoughException;
import com.loriscatiz.exception.badrequest.UsernameAlreadyPresentException;
import com.loriscatiz.exception.notfound.UserNotFoundException;
import com.loriscatiz.model.Role;
import com.loriscatiz.model.dto.internal.auth.RefreshTokenRedisDTO;
import com.loriscatiz.model.dto.req.auth.LoginRequest;
import com.loriscatiz.model.dto.req.auth.SignUpRequest;
import com.loriscatiz.model.dto.internal.auth.AccessTokenPayloadInput;
import com.loriscatiz.model.dto.res.auth.SignupResponse;
import com.loriscatiz.model.dto.res.auth.TokensResponse;
import com.loriscatiz.model.entity.User;
import com.loriscatiz.repo.RedisRepo;
import com.loriscatiz.repo.UserRepo;

import java.util.Optional;

public class AuthServiceImpl implements AuthService {
    private final UserRepo userRepo;
    private final RedisRepo redisRepo;
    private final JWTService jwtService;
    private final PasswordHasher passwordHasher;

    public AuthServiceImpl(UserRepo userRepo, RedisRepo redisRepo, JWTService jwtService, PasswordHasher passwordHasher) {
        this.userRepo = userRepo;
        this.redisRepo = redisRepo;
        this.jwtService = jwtService;
        this.passwordHasher = passwordHasher;
    }


    @Override
    public SignupResponse signUp(SignUpRequest request) {


        if (!request.password().equals(request.confirmPassword())) {
            throw new ConfirmPasswordDoesNotMatchException();
        }

        checkPasswordStrength(request.password());

        User candidate = new User();
        candidate.setUsername(request.username());
        candidate.setPasswordHash(passwordHasher.hashPassword(request.password()));
        candidate.setRole(Role.USER);

        Optional<User> updateResult = userRepo.saveUser(candidate);

        if (updateResult.isEmpty()) {
            throw new UsernameAlreadyPresentException();
        }

        User saved = updateResult.get();

        return new SignupResponse( saved.getUsername(), saved.getRole(), saved.getJoinedAt().toString());

    }

    @Override
    public TokensResponse login(LoginRequest request) {
        String username = request.username();
        Optional<User> queryResult = userRepo.getUserByUsername(username);

        if (queryResult.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        User user = queryResult.get();

        if (!passwordHasher.checkPassword(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        String refreshToken = jwtService.createRefreshToken(username);
        DecodedJWT decodedRefreshToken = JWT.decode(refreshToken);
        var redisPayloadInput = new RefreshTokenRedisDTO(decodedRefreshToken.getId(), username, JWTServiceImpl.REFRESH_EXPIRATION_TIME_SECONDS);
        redisRepo.saveRefreshToken(redisPayloadInput);

        String accessToken = jwtService.createAccessToken(new AccessTokenPayloadInput(username, user.getRole()));

        return new TokensResponse(refreshToken, accessToken);
    }

    @Override
    public TokensResponse refresh(String prevRefreshToken) {
        DecodedJWT decodedPrevRefreshToken = jwtService.getValidRefreshToken(prevRefreshToken);

        String prevTokenId = decodedPrevRefreshToken.getId();
        redisRepo.validateRefreshToken(prevTokenId);
        redisRepo.deleteRefreshToken(prevTokenId);
        
        String username = decodedPrevRefreshToken.getSubject();
        Optional<User> queryResult = userRepo.getUserByUsername(username);

        if (queryResult.isEmpty()){
            throw new UserNotFoundException();
        }

        User user = queryResult.get();

        String nextRefreshToken = jwtService.createRefreshToken(username);
        DecodedJWT nextRefreshTokenDecoded = JWT.decode(nextRefreshToken) ;

        redisRepo.saveRefreshToken(new RefreshTokenRedisDTO(nextRefreshTokenDecoded.getId(), username, JWTServiceImpl.REFRESH_EXPIRATION_TIME_SECONDS));

        String access = jwtService.createAccessToken(new AccessTokenPayloadInput(username, user.getRole()));

        return new TokensResponse(nextRefreshToken, access);


    }

    static void checkPasswordStrength(String password) {
        final int MIN_LENGTH = 8;

        if (password.length() < MIN_LENGTH){
            throw new PasswordNotStrongEnoughException("password must be at least " + MIN_LENGTH + "characters long");
        }

        if (!password.matches(".*[A-Z].*")){
            throw new PasswordNotStrongEnoughException("password must contain at least 1 uppercase letter");
        }

         if (!password.matches(".*[a-z].*")){
            throw new PasswordNotStrongEnoughException("password must contain at least 1 lowercase letter");
        }

         if (!password.matches(".*\\d.*")){
             throw new PasswordNotStrongEnoughException("password must contain at least 1 digit");
         }

        if (!password.matches(".*[^a-zA-Z0-9].*")){
            throw new PasswordNotStrongEnoughException("password must contain at least 1 special character");
        }

    }
}
