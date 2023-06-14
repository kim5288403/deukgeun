package com.example.deukgeun.common.jwt;

import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.request.TokenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(tokenRepository);
    }

    @Test
    void testSaveUser() {
        // Given
        String authToken = "testAuthToken";
        String refreshToken = "testRefreshToken";
        Token token = TokenRequest.create(authToken, refreshToken);

        // When
        Token saveToken = tokenRepository.save(token);

        // Then
        Token retrievedToken = tokenRepository.findById(saveToken.getId()).orElse(null);
        assertNotNull(retrievedToken);
        assertEquals(retrievedToken.getAuthToken(), saveToken.getAuthToken());
        assertEquals(retrievedToken.getRefreshToken(), saveToken.getRefreshToken());
    }

}
