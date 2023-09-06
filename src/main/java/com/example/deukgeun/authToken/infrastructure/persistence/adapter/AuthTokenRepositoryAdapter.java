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

    /**
     * 주어진 인증 토큰에 해당하는 토큰 정보를 삭제합니다.
     *
     * @param authToken 삭제할 인증 토큰
     */
    @Override
    public void deleteByAuthToken(String authToken) {
        authTokenRepository.deleteByAuthToken(authToken);
    }

    /**
     * 주어진 인증 토큰에 해당하는 토큰 정보를 검색합니다.
     *
     * @param authToken 검색할 인증 토큰
     * @return 검색한 토큰 정보의 Optional 래퍼 또는 빈 Optional (토큰이 존재하지 않는 경우)
     */
    @Override
    public Optional<AuthToken> findByAuthToken(String authToken) {
        Optional<AuthTokenEntity> authTokenEntity = authTokenRepository.findByAuthToken(authToken);
        return authTokenEntity.map(this::convert);
    }

    /**
     * 주어진 인증 토큰 정보를 저장합니다.
     *
     * @param authToken 저장할 인증 토큰 정보
     * @return 저장된 인증 토큰 정보
     */
    @Override
    public AuthToken save(AuthToken authToken) {
        // 인증 토큰 정보를 엔티티로 변환합니다.
        AuthTokenEntity authTokenEntity = convert(authToken);

        // 변환된 엔티티를 데이터베이스에 저장하고, 저장된 엔티티를 반환합니다.
        AuthTokenEntity saveAuthToken = authTokenRepository.save(authTokenEntity);

        // 저장된 엔티티를 다시 도메인 객체로 변환하여 반환합니다.
        return convert(saveAuthToken);
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

}
