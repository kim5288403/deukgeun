package com.example.deukgeun.authToken.service.domain;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.authToken.domain.repository.AuthTokenRepository;
import com.example.deukgeun.authToken.domain.service.implement.AuthTokenDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthTokenDomainServiceTest {

    @InjectMocks
    private AuthTokenDomainServiceImpl authTokenDomainService;
    @Mock
    private AuthTokenRepository authTokenRepository;

    @Test
    public void givenAuthAndRefreshTokens_whenCreateToken_thenTokenSavedInRepository() {
        // Given
        String authToken = "yourAuthToken";
        String refreshToken = "yourRefreshToken";

        // When
        authTokenDomainService.createToken(authToken, refreshToken);

        // Then
        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    public void givenAuthToken_whenDeleteByAuthToken_thenTokenDeletedFromRepository() {
        // Given
        String authToken = "yourAuthToken";

        // When
        authTokenDomainService.deleteByAuthToken(authToken);

        // Then
        verify(authTokenRepository, times(1)).deleteByAuthToken(authToken);
    }

    @Test
    public void givenAuthToken_whenFindByAuthToken_thenReturnAuthToken() {
        // Given
        String authToken = "yourAuthToken";
        AuthToken expectedAuthToken = new AuthToken(123L, authToken, "refreshToken"); // 가정한 토큰 객체

        given(authTokenRepository.findByAuthToken(authToken)).willReturn(Optional.of(expectedAuthToken));

        // When
        AuthToken actualAuthToken = authTokenDomainService.findByAuthToken(authToken);

        // Then
        assertEquals(expectedAuthToken, actualAuthToken);
    }

    @Test
    public void givenAuthToken_whenFindByAuthToken_thenReturnNull() {
        // Given
        String authToken = "yourAuthToken";

        given(authTokenRepository.findByAuthToken(authToken)).willReturn(Optional.empty());

        // When
        AuthToken actualAuthToken = authTokenDomainService.findByAuthToken(authToken);

        // Then
        assertNull(actualAuthToken);
    }

    @Test
    public void givenAuthTokenAndNewAuthToken_whenUpdateAuthToken_thenAuthTokenUpdatedInRepository() {
        // Given
        String authToken = "yourAuthToken";
        String newAuthToken = "newAuthToken";
        AuthToken expectedAuthToken = new AuthToken(123L, authToken, "refreshToken"); // 가정한 토큰 객체

        given(authTokenRepository.findByAuthToken(authToken)).willReturn(Optional.of(expectedAuthToken));

        // When
        authTokenDomainService.updateAuthToken(authToken, newAuthToken);

        // Then
        verify(authTokenRepository, times(1)).save(expectedAuthToken);
    }
}
