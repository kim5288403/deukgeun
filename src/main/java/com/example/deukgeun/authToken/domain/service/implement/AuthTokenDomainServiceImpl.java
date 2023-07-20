package com.example.deukgeun.authToken.domain.service.implement;

import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.authToken.domain.repository.AuthTokenRepository;
import com.example.deukgeun.authToken.domain.service.AuthTokenDomainService;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthTokenDomainServiceImpl implements AuthTokenDomainService {
    private final AuthTokenRepository authTokenRepository;
    private final MemberRepository memberRepository;

    @Override
    public void createToken(String authToken, String refreshToken) {
        AuthToken createAuthToken = AuthToken.create(authToken, refreshToken);

        authTokenRepository.save(createAuthToken);
    }

    @Override
    public void deleteByAuthToken(String authToken) {
        authTokenRepository.deleteByAuthToken(authToken);
    }


    @Override
    public AuthToken findByAuthToken(String authToken) {
        return authTokenRepository.findByAuthToken(authToken).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return memberRepository.loadUserByUsername(email);
    }

    @Override
    public void updateAuthToken(String authToken, String newAuthToken) {
        AuthToken saveToken = authTokenRepository.findByAuthToken(authToken).orElse(null);
        assert saveToken != null;
        saveToken.updateAuthToken(newAuthToken);
        authTokenRepository.save(saveToken);
    }
}
