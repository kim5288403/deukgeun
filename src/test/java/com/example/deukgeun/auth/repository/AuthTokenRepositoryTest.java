package com.example.deukgeun.auth.repository;

import com.example.deukgeun.auth.infrastructure.persistence.entity.AuthTokenEntity;
import com.example.deukgeun.auth.infrastructure.persistence.repository.AuthTokenRepositoryImpl;
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
public class AuthTokenRepositoryTest {
    @Autowired
    private AuthTokenRepositoryImpl authTokenRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(authTokenRepository);
    }

    @Test
    void givenToken_whenSaved_thenReturnValid() {
        // Given
        String authToken = "testAuthToken";
        String refreshToken = "testRefreshToken";
        AuthTokenEntity authTokenEntity = AuthTokenEntity
                .builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build();

        // When
        AuthTokenEntity saveToken = authTokenRepository.save(authTokenEntity);

        // Then
        AuthTokenEntity retrievedToken = authTokenRepository.findById(saveToken.getId()).orElse(null);
        assertNotNull(retrievedToken);
        assertEquals(retrievedToken.getAuthToken(), saveToken.getAuthToken());
        assertEquals(retrievedToken.getRefreshToken(), saveToken.getRefreshToken());
    }

    @Test
    void givenToken_whenFindByAuthToken_thenReturnValid() {
        // Given
        String authToken = "testAuthToken";
        String refreshToken = "testRefreshToken";
        AuthTokenEntity authTokenEntity = AuthTokenEntity
                .builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build();
        authTokenRepository.save(authTokenEntity);

        // When
        AuthTokenEntity findToken = authTokenRepository.findByAuthToken(authToken).orElse(null);

        // Then
        assertNotNull(findToken);
        assertEquals(findToken.getAuthToken(), authToken);
        assertEquals(findToken.getRefreshToken(), refreshToken);
    }
}
