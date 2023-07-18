package com.example.deukgeun.auth.domain.repository;

import com.example.deukgeun.auth.domain.model.entity.AuthMail;

import java.util.Optional;

public interface AuthMailRepository {
    boolean existsByEmailAndCode(String email, String code);

    boolean existsByEmail(String email);

    Optional<AuthMail> findByEmail(String email);

    void deleteByEmail(String email);

    AuthMail save(AuthMail authMail);

    Optional<AuthMail> findById(Long id);
}
