package com.example.deukgeun.authMail.persistence.adapter;

import com.example.deukgeun.authMail.persistence.entity.AuthMailEntity;
import com.example.deukgeun.authMail.persistence.repository.AuthMailRepositoryImpl;
import com.example.deukgeun.authToken.domain.model.entity.AuthMail;
import com.example.deukgeun.authToken.domain.repository.AuthMailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthMailRepositoryAdapter implements AuthMailRepository {
    private final AuthMailRepositoryImpl authMailRepository;

    @Override
    public boolean existsByEmailAndCode(String email, String code) {
        return authMailRepository.existsByEmailAndCode(email, code);
    }

    @Override
    public boolean existsByEmail(String email) {
        return authMailRepository.existsByEmail(email);
    }

    @Override
    public Optional<AuthMail> findByEmail(String email) {
        Optional<AuthMailEntity> authMailEntity = authMailRepository.findByEmail(email);

        return authMailEntity.map(this::convert);
    }

    @Override
    public Optional<AuthMail> findByEmailAndCode(String email, String code) {
        Optional<AuthMailEntity> authMailEntity = authMailRepository.findByEmailAndCode(email, code);
        return authMailEntity.map(this::convert);
    }

    @Override
    public void deleteByEmail(String email) {
        authMailRepository.deleteByEmail(email);
    }

    @Override
    public AuthMail save(AuthMail authMail) {
        AuthMailEntity authMailEntity = authMailRepository.save(convert(authMail));
        return convert(authMailEntity);
    }

    @Override
    public Optional<AuthMail> findById(Long id) {
        Optional<AuthMailEntity> authMailEntity = authMailRepository.findById(id);
        return authMailEntity.map(this::convert);
    }

    private AuthMailEntity convert(AuthMail authMail) {
        return AuthMailEntity.builder()
                .id(authMail.getId())
                .email(authMail.getEmail())
                .code(authMail.getCode())
                .mailStatus(authMail.getMailStatus())
                .build();
    }

    private AuthMail convert(AuthMailEntity authMail) {
        return new AuthMail(
                authMail.getId(),
                authMail.getEmail(),
                authMail.getCode(),
                authMail.getMailStatus()
        );
    }
}
