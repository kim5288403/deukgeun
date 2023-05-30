package com.example.deukgeun.trainer.jwt;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
public class DeleteTokenTest {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JwtServiceImpl jwtService;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;

    @Test
    void shouldDeleteTokenForValidAuthToken() {
        // Given
        String email = "testEmail@test.com";
        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        Token token = TokenRequest.create(jwt, jwt);

        tokenRepository.save(token);

        // When
        jwtService.deleteToken(jwt);
        Token result = tokenRepository.findByAuthToken(jwt).orElse(null);

        // Then
        assertNull(result);
    }
}
