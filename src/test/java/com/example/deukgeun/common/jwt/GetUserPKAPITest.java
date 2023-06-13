package com.example.deukgeun.common.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class GetUserPKAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;

    @Test
    void shouldGetUserPKForValidRequest() throws Exception {
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
        mockMvc.perform(get("/jwt/pk")
                        .header("Authorization", jwt))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("이메일 조회 성공했습니다."))
                .andExpect(jsonPath("$.data").value(email));
    }

    @Test
    void shouldBadRequestForInvalidRequest() throws Exception {
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

        // When
        mockMvc.perform(get("/jwt/pk")
                        .header("Authorization", jwt))
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 JWT signature 형식입니다."));
    }
}