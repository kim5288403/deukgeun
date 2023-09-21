package com.example.deukgeun.authMail.application.service;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;

import javax.persistence.EntityNotFoundException;

public interface AuthMailApplicationService {
    void confirm(AuthMailRequest request);
    String createCode();
    void deleteByEmail(String email);
    boolean existsByEmailAndCode(String email, String code);
    boolean isEmailAuthenticated(String email) throws EntityNotFoundException;
    void save(AuthMailRequest authMailRequest);
}
