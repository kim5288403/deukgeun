package com.example.deukgeun.auth.domain.service;

import com.example.deukgeun.auth.domain.model.entity.AuthToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthTokenDomainService {
    void createToken(String authToken, String refreshToken);
    void deleteByAuthToken(String authToken);
    AuthToken findByAuthToken(String authToken);
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;
    void updateAuthToken(String authToken, String newAuthToken);
}
