package com.example.deukgeun.auth.infrastructure.persistence.adapter;

import com.example.deukgeun.auth.domain.model.entity.AuthToken;
import com.example.deukgeun.auth.domain.repository.AuthTokenRepository;
import com.example.deukgeun.auth.infrastructure.persistence.entity.AuthTokenEntity;
import com.example.deukgeun.auth.infrastructure.persistence.repository.AuthTokenRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthTokenRepositoryAdapter implements AuthTokenRepository {
    private final AuthTokenRepositoryImpl authTokenRepository;

    @Override
    public void deleteByAuthToken(String authToken) {
        authTokenRepository.deleteByAuthToken(authToken);
    }

    @Override
    public Optional<AuthToken> findByAuthToken(String authToken) {
        Optional<AuthTokenEntity> authTokenEntity = authTokenRepository.findByAuthToken(authToken);
        return authTokenEntity.map(this::convert);
    }

    private AuthTokenEntity convert(AuthToken authToken) {
        return AuthTokenEntity.builder()
                .id(authToken.getId())
                .authToken(authToken.getAuthToken())
                .refreshToken(authToken.getRefreshToken())
                .build();
    }

    private AuthToken convert(AuthTokenEntity authTokenEntity) {
        return new AuthToken(
                authTokenEntity.getId(),
                authTokenEntity.getAuthToken(),
                authTokenEntity.getRefreshToken()
        );
    }

    @Override
    public AuthToken save(AuthToken authToken) {
        AuthTokenEntity authTokenEntity = convert(authToken);
        AuthTokenEntity saveAuthToken = authTokenRepository.save(authTokenEntity);
        return convert(saveAuthToken);
    }

}
