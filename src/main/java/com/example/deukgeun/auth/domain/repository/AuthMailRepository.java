package com.example.deukgeun.auth.domain.repository;

import com.example.deukgeun.auth.domain.model.entity.AuthMail;

import java.util.Optional;

public interface AuthMailRepository {
    void deleteByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndCode(String email, String code);

    Optional<AuthMail> findById(Long id);

    Optional<AuthMail> findByEmail(String email);

    Optional<AuthMail> findByEmailAndCode(String email, String code);

    AuthMail save(AuthMail authMail);
}
