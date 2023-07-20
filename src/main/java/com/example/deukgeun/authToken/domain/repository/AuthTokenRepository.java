package com.example.deukgeun.authToken.domain.repository;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;

import java.util.Optional;

public interface AuthTokenRepository {
    void deleteByAuthToken(String authToken);
    Optional<AuthToken> findByAuthToken(String authToken);
    AuthToken save(AuthToken authToken);

}
