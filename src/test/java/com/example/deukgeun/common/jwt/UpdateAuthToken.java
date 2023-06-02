package com.example.deukgeun.common.jwt;

import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.request.TokenRequest;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UpdateAuthToken {
    @Autowired
    private JwtServiceImpl jwtService;
    @Autowired
    private TokenRepository tokenRepository;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;

    private String authToken;

    @Test
    void shouldUpdateAuthToken() {
        // Given
        setAuthToken();
        String email = "testEmail@test.com";
        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        String newAuthToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        // When
        jwtService.updateAuthToken(authToken, newAuthToken);
        Token result = tokenRepository.findByAuthToken(newAuthToken).orElse(null);

        // Then
        assertNotNull(result);
        assertNotEquals(authToken, result.getAuthToken());

        tokenRepository.deleteByAuthToken(result.getAuthToken());
    }

    private void setAuthToken() {
        authToken = "testAuthToken";
        Token token = TokenRequest.create(authToken, "testRefreshToken");
        tokenRepository.save(token);
    }
}
