package com.example.deukgeun.authMail.domain.service;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.domain.dto.ConfirmDTO;
import com.example.deukgeun.authMail.domain.model.entity.AuthMail;

public interface AuthMailDomainService {
    void deleteByEmail(String email);

    void confirm(ConfirmDTO confirmDTO);

    boolean existsByEmail(String email);

    boolean existsByEmailAndCode(String email, String code);

    AuthMail findByEmail(String email);

    AuthMail findByEmailAndCode(String email, String code);

    void save(String toEmail, String authCode);

    boolean isEmailAuthenticated(String email);
}
