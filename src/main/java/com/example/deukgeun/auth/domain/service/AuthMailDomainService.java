package com.example.deukgeun.auth.domain.service;

import com.example.deukgeun.auth.application.dto.request.AuthMailRequest;
import com.example.deukgeun.auth.domain.model.entity.AuthMail;
import com.example.deukgeun.auth.domain.model.valueobject.MailStatus;

import java.util.Optional;

public interface AuthMailDomainService {
    void deleteByEmail(String email);

    void confirm(AuthMailRequest request);

    boolean existsByEmail(String email);

    boolean existsByEmailAndCode(String email, String code);

    AuthMail findByEmail(String email);

    AuthMail findByEmailAndCode(String email, String code);

    void save(String toEmail, String authCode);

    boolean isEmailAuthenticated(String email);
}
