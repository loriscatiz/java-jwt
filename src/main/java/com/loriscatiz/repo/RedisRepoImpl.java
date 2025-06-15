package com.loriscatiz.repo;

import com.loriscatiz.exception.auth.InvalidRefreshTokenException;
import com.loriscatiz.model.dto.internal.auth.RefreshTokenRedisDTO;
import io.lettuce.core.api.sync.RedisCommands;

public class RedisRepoImpl implements RedisRepo{
    private final RedisCommands<String, String> redis;

    public RedisRepoImpl(RedisCommands<String, String> redis) {
        this.redis = redis;
    }


    @Override
    public void saveRefreshToken(RefreshTokenRedisDTO payload) {
        String key = "refresh:" + payload.jti();
        redis.setex(key, payload.ttlSeconds(), payload.username());
    }

    @Override
    public void validateRefreshToken(String jti) throws InvalidRefreshTokenException {
        String key = "refresh:" + jti;
        String username = redis.get(key);
        if (username == null){
            throw new InvalidRefreshTokenException();
        }
    }

    @Override
    public void deleteRefreshToken(String jti) {
        String key = "refresh:" + jti;
        redis.del(key);
    }
}
