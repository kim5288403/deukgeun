package com.example.deukgeun.trainer.license;

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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SavaAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;
    @Test
    void shouldIllegalArgumentExceptionForInvalidRequest() throws Exception {
        // Given
        String name = "테스트 자격증";
        String no = "t1e2s3t4";

        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Claims claims = Jwts.claims().setSubject("test");
        claims.put("roles", role);
        Date now = new Date();

        String jwt = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();


        // When
        mockMvc.perform(post("/api/trainer/license/")
                        .header("Authorization", jwt)
                        .param("name", name)
                        .param("no", no)
                )
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("존재하지않는 자격증 정보 입니다."))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof IllegalArgumentException));
    }

}
