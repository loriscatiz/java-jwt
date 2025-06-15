package com.loriscatiz.repo;

import com.loriscatiz.exception.auth.InvalidRefreshTokenException;
import com.loriscatiz.model.dto.internal.auth.RefreshTokenRedisDTO;

public interface RedisRepo {
    void saveRefreshToken(RefreshTokenRedisDTO value);
    void validateRefreshToken(String jti) throws InvalidRefreshTokenException;
    void deleteRefreshToken(String jti);
}
