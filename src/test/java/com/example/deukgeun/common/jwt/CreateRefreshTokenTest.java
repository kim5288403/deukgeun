package com.example.deukgeun.common.jwt;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CreateRefreshTokenTest {
    @Autowired
    private JwtServiceImpl jwtService;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Test
    void shouldCreateRefreshTokenForValidParameter() {
        // Given
        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        String email = "testEmail@email.com";

        // When
        String result = jwtService.createRefreshToken(email, role);
        String resultRole = (String) Jwts.parser().setSigningKey(encodeSecretKey).parseClaimsJws(result).getBody().get("roles");
        String resultEmail = Jwts.parser().setSigningKey(encodeSecretKey).parseClaimsJws(result).getBody().getSubject();

        // Then
        assertEquals(role, resultRole);
        assertEquals(email, resultEmail);
    }
}
