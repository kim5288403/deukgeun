package com.example.deukgeun.authToken.domain.service;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;

public interface AuthTokenDomainService {
    void createToken(String authToken, String refreshToken);
    void deleteByAuthToken(String authToken);
    AuthToken findByAuthToken(String authToken);
    void updateAuthToken(String authToken, String newAuthToken);
}
