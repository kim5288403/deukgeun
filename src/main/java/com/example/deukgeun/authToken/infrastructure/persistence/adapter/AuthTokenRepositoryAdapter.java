package com.example.deukgeun.authToken.infrastructure.persistence.adapter;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.authToken.domain.repository.AuthTokenRepository;
import com.example.deukgeun.authToken.infrastructure.persistence.entity.AuthTokenEntity;
import com.example.deukgeun.authToken.infrastructure.persistence.repository.AuthTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthTokenRepositoryAdapter implements AuthTokenRepository {
    private final AuthTokenJpaRepository authTokenRepository;

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
