package com.example.deukgeun.trainer.jwt;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import io.jsonwebtoken.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class GetUserPKTest {
    @Autowired
    private JwtServiceImpl jwtService;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;

    @Test
    void shouldGetUserPKForValidRequest() {
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

        // When
        String result = jwtService.getUserPk(jwt);

        // Then
        assertEquals(email, result);
    }

    @Test
    void shouldThrowMalformedJwtExceptionForInvalidRequest() {
        // Given
        String jwt = "test";

        // When, Then
        assertThrows(MalformedJwtException.class, () -> {
            jwtService.getUserPk(jwt);
        });
    }

    @Test
    void shouldThrowSignatureExceptionForInvalidRequest() {
        // Given
        String encodeSecretKey = Base64.getEncoder().encodeToString("invalidSecretKey".getBytes());
        Claims claims = Jwts.claims().setSubject("testEmail@test.com");
        claims.put("roles", role);
        Date now = new Date();
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        // When, Then
        assertThrows(SignatureException.class, () -> {
            jwtService.getUserPk(jwt);
        });
    }

}
