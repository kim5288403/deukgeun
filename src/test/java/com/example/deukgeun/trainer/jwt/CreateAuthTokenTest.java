package com.example.deukgeun.trainer.jwt;

import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CreateAuthTokenTest {
    @Autowired
    private JwtServiceImpl jwtService;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Test
    void shouldCreateAuthTokenForValidParameter() {
        // Given
        String email = "testEmail@test.com";
        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());

        // When
        String result = jwtService.createAuthToken(email, role);
        String resultEmail = Jwts.parser().setSigningKey(encodeSecretKey).parseClaimsJws(result).getBody().getSubject();
        String resultRole = (String) Jwts.parser().setSigningKey(encodeSecretKey).parseClaimsJws(result).getBody().get("roles");

        // Then
        assertEquals(email, resultEmail);
        assertEquals(role, resultRole);
    }
}
