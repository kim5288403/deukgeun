package com.example.deukgeun.auth.repository;

import com.example.deukgeun.auth.domain.entity.Token;
import com.example.deukgeun.auth.infrastructure.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
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
    void givenToken_whenSaved_thenReturnValid() {
        // Given
        String authToken = "testAuthToken";
        String refreshToken = "testRefreshToken";
        Token token = Token
                .builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build();

        // When
        Token saveToken = tokenRepository.save(token);

        // Then
        Token retrievedToken = tokenRepository.findById(saveToken.getId()).orElse(null);
        assertNotNull(retrievedToken);
        assertEquals(retrievedToken.getAuthToken(), saveToken.getAuthToken());
        assertEquals(retrievedToken.getRefreshToken(), saveToken.getRefreshToken());
    }

    @Test
    void givenToken_whenFindByAuthToken_thenReturnValid() {
        // Given
        String authToken = "testAuthToken";
        String refreshToken = "testRefreshToken";
        Token token = Token
                .builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);

        // When
        Token findToken = tokenRepository.findByAuthToken(authToken).orElse(null);

        // Then
        assertNotNull(findToken);
        assertEquals(findToken.getAuthToken(), authToken);
        assertEquals(findToken.getRefreshToken(), refreshToken);
    }

    @Test
    public void givenNewAuthToken_whenUpdateAuthToken_thenTokenIsUpdatedWithNewAuthToken() {
        // Given
        String authToken = "oldAuthToken";
        String refreshToken = "testRefreshToken";
        String newAuthToken = "newAuthToken";
        Token token = Token
                .builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build();
        tokenRepository.save(token);
        Token findToken = tokenRepository.findByAuthToken(authToken).orElse(null);

        // When
        assert findToken != null;
        findToken.updateAuthToken(newAuthToken);
        tokenRepository.save(findToken);

        // Then
        Token updatedToken = tokenRepository.findById(token.getId()).orElse(null);

        assertNotNull(updatedToken);
        assertEquals(newAuthToken, updatedToken.getAuthToken());
    }
}
