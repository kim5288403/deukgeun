package com.example.deukgeun.common.jwt;

import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.request.TokenRequest;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class FindByAuthTokenTest {
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
    void shouldFindByAuthTokenAuthToken() {
        // Given
        String authToken = "testAuthToken";
        Token token = TokenRequest.create(authToken, "testRefreshToken");
        tokenRepository.save(token);

        // When
        Token result = jwtService.findByAuthToken(authToken);

        // Then
        assertNotNull(result);
        assertEquals(authToken, result.getAuthToken());
    }
}
