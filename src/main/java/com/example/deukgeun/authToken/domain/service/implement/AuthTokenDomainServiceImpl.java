package com.example.deukgeun.authToken.domain.service.implement;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.authToken.domain.repository.AuthTokenRepository;
import com.example.deukgeun.authToken.domain.service.AuthTokenDomainService;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import com.example.deukgeun.trainer.domain.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenDomainServiceImpl implements AuthTokenDomainService {
    private final AuthTokenRepository authTokenRepository;
    private final MemberRepository memberRepository;
    private final TrainerRepository trainerRepository;

    /**
     * 주어진 인증 토큰과 갱신 토큰을 사용하여 새로운 토큰 정보를 생성하고 저장합니다.
     *
     * @param authToken    인증 토큰
     * @param refreshToken 갱신 토큰
     */
    @Override
    public void createToken(String authToken, String refreshToken) {
        AuthToken createAuthToken = AuthToken.create(authToken, refreshToken);

        authTokenRepository.save(createAuthToken);
    }

    /**
     * 주어진 인증 토큰에 해당하는 토큰 정보를 데이터베이스에서 삭제합니다.
     *
     * @param authToken 삭제할 인증 토큰
     */
    @Override
    public void deleteByAuthToken(String authToken) {
        authTokenRepository.deleteByAuthToken(authToken);
    }

    /**
     * 주어진 인증 토큰에 해당하는 토큰 정보를 검색합니다.
     * 검색 결과가 없는 경우 null 반환합니다.
     *
     * @param authToken 검색할 인증 토큰
     * @return 검색한 토큰 정보 또는 null (토큰이 존재하지 않는 경우)
     */
    @Override
    public AuthToken findByAuthToken(String authToken) {
        return authTokenRepository.findByAuthToken(authToken).orElse(null);
    }

    /**
     * 주어진 인증 토큰에 해당하는 토큰 정보를 검색하고, 새로운 인증 토큰으로 업데이트 합니다.
     *
     * @param authToken    기존 인증 토큰
     * @param newAuthToken 새로운 인증 토큰
     */
    @Override
    public void updateAuthToken(String authToken, String newAuthToken) {
        AuthToken saveToken = authTokenRepository.findByAuthToken(authToken).orElse(null);
        assert saveToken != null;
        saveToken.updateAuthToken(newAuthToken);
        authTokenRepository.save(saveToken);
    }
}
