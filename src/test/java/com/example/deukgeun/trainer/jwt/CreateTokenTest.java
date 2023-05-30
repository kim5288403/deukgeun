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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class CreateTokenTest {
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

    @Test
    void shouldCreateTokenForValidParameter() {
        // Given
        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Claims claims = Jwts.claims().setSubject("testEmail@test.com");
        claims.put("roles", role);
        Date now = new Date();

        String authToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        Claims refreshClaims = Jwts.claims().setSubject("");
        claims.put("roles", role);

        String refreshToken = Jwts.builder()
                .setClaims(refreshClaims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        Token token = TokenRequest.create(authToken, refreshToken);

        // When
        jwtService.createToken(token);
        Token result = tokenRepository.findByAuthToken(token.getAuthToken()).orElse(null);

        // Then
        assertNotNull(result);
        assertEquals(token.getAuthToken(), result.getAuthToken());
        assertEquals(token.getRefreshToken(), result.getRefreshToken());
    }

    @Test
    void shouldDataIntegrityViolationExceptionForInvalidParameter() {
        // Given
        String authToken = null;
        String refreshToken = "";
        Token token = TokenRequest.create(authToken, refreshToken);

        // When, Then
        assertThrows(DataIntegrityViolationException.class, () -> {
            jwtService.createToken(token);
        });
    }
}
