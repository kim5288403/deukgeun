package com.example.deukgeun.authToken.repository;

import com.example.deukgeun.authToken.infrastructure.persistence.entity.AuthTokenEntity;
import com.example.deukgeun.authToken.infrastructure.persistence.repository.AuthTokenJpaRepository;
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
    private AuthTokenJpaRepository authTokenRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(authTokenRepository);
    }

    @Test
    void givenValidAuthToken_whenSave_thenAuthTokenIsSaved() {
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
        assertNotNull(saveToken);
        assertEquals(authTokenEntity.getAuthToken(), saveToken.getAuthToken());
        assertEquals(authTokenEntity.getRefreshToken(), saveToken.getRefreshToken());
    }

    @Test
    void givenValidAuthToken_whenFindByAuthToken_thenReturnFoundIsAuthToken() {
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
