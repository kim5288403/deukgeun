package com.example.deukgeun.common.jwt;

import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.request.TokenRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@ActiveProfiles("test")
@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TokenRepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(tokenRepository);
    }

    @Test
    void givenToken_whenSaveToken_thenSuccess() {
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

    @Test
    void givenExisting_whenFindByAuthToken_thenIsFound() {
        // Given
        String authToken = "testAuthToken";
        String refreshToken = "testRefreshToken";
        Token token = TokenRequest.create(authToken, refreshToken);
        tokenRepository.save(token);

        // When
        Token foundToken = tokenRepository.findByAuthToken(authToken).orElse(null);

        // Then
        assertNotNull(foundToken);
        assertEquals(foundToken.getAuthToken(), authToken);
        assertEquals(foundToken.getRefreshToken(), refreshToken);
    }

    @Test
    public void givenNewAuthToken_whenUpdateAuthToken_thenAuthTokenShouldBeUpdated() {
        // Given
        String authToken = "oldAuthToken";
        String newAuthToken = "newAuthToken";
        Token token = TokenRequest.create(authToken, "refreshToken");
        tokenRepository.save(token);

        // When
        Token saveToken = tokenRepository.findById(token.getId()).orElse(null);
        assert saveToken != null;
        saveToken.updateAuthToken(newAuthToken);
        tokenRepository.save(saveToken);

        // Then
        Token updatedToken = tokenRepository.findById(token.getId()).orElse(null);
        assertNotNull(updatedToken);
        assertEquals(newAuthToken, updatedToken.getAuthToken());
    }
}
