package com.example.deukgeun.auth.domain.service;

import com.example.deukgeun.auth.domain.entity.AuthMail;
import com.example.deukgeun.auth.domain.valueobject.MailStatus;

import java.util.Optional;

public interface AuthMailService {
    void save(String toEmail, String authCode);
    boolean existsByEmail(String email);
    void deleteByEmail(String email);
    boolean existsByEmailAndCode(String email, String code);
    Optional<AuthMail> findByEmail(String email);
    void updateMailStatus(MailStatus mailStatus, AuthMail authMail);
}
