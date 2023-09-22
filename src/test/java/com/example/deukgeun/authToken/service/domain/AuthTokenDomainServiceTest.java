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
    public void givenValidAuthAndRefreshToken_whenCreateToken_thenSaveCalled() {
        // Given
        String authToken = "yourAuthToken";
        String refreshToken = "yourRefreshToken";

        // When
        authTokenDomainService.createToken(authToken, refreshToken);

        // Then
        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }

    @Test
    public void givenValidAuthToken_whenDeleteByAuthToken_thenDeleteByAuthTokenCalled() {
        // Given
        String authToken = "yourAuthToken";

        // When
        authTokenDomainService.deleteByAuthToken(authToken);

        // Then
        verify(authTokenRepository, times(1)).deleteByAuthToken(anyString());
    }

    @Test
    public void givenValidAuthToken_whenFindByAuthToken_thenReturnFoundIsAuthToken() {
        // Given
        String authToken = "yourAuthToken";
        AuthToken expectedAuthToken = new AuthToken(123L, authToken, "refreshToken"); // 가정한 토큰 객체

        given(authTokenRepository.findByAuthToken(authToken)).willReturn(Optional.of(expectedAuthToken));

        // When
        AuthToken actualAuthToken = authTokenDomainService.findByAuthToken(authToken);

        // Then
        assertEquals(expectedAuthToken, actualAuthToken);
        verify(authTokenRepository, times(1)).findByAuthToken(anyString());
    }

    @Test
    public void givenAuthToken_whenFindByAuthToken_thenReturnNull() {
        // Given
        String authToken = "yourAuthToken";

        // When
        AuthToken actualAuthToken = authTokenDomainService.findByAuthToken(authToken);

        // Then
        assertNull(actualAuthToken);
        verify(authTokenRepository, times(1)).findByAuthToken(anyString());
    }

    @Test
    public void givenValidAuthTokenAndNewAuthToken_whenUpdateAuthToken_thenAuthTokenIsUpdated() {
        // Given
        String authToken = "yourAuthToken";
        String newAuthToken = "newAuthToken";
        AuthToken expectedAuthToken = AuthToken.create(authToken, "refreshToken");

        given(authTokenRepository.findByAuthToken(anyString())).willReturn(Optional.of(expectedAuthToken));

        // When
        authTokenDomainService.updateAuthToken(authToken, newAuthToken);

        // Then
        assertEquals(newAuthToken, expectedAuthToken.getAuthToken());
        verify(authTokenRepository, times(1)).save(any(AuthToken.class));
    }
}
