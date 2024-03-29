package com.example.deukgeun.authToken.application.service;

import com.example.deukgeun.authToken.application.dto.response.LoginResponse;
import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface AuthTokenApplicationService {
    void createToken(String authToken, String refreshToken);
    String createAuthToken(String userPk, String roles);
    String createRefreshToken(String userPk, String roles);
    void deleteByAuthToken(String authToken);
    AuthToken findByAuthToken(String authToken);
    String getUserPk(String token);
    String getUserRole(String token);
    HashMap<String, String> getLoginData(String loginType, String email);
    Authentication getAuthentication(String token, String role);
    String getRefreshTokenByAuthToken(String authToken);
    String resolveAuthToken(HttpServletRequest request);
    String setToken(String email, HttpServletResponse response, String role);
    void setHeaderRole(HttpServletResponse response, String role);
    void setHeaderAuthToken(HttpServletResponse response, String authToken);
    void updateAuthToken(String authToken, String newAuthToken);
    boolean validateToken(String token);
    LoginResponse getLoginResponse(String authToken, String role);

}
