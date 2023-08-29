package com.example.deukgeun.authMail.application.service;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import org.springframework.messaging.handler.annotation.Payload;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;

public interface AuthMailApplicationService {
    void confirm(AuthMailRequest request);
    String createCode();
    MimeMessage createMailForm(String toEmail, String authCode) throws MessagingException;
    void deleteByEmail(String email);
    boolean existsByEmailAndCode(String email, String code);
    boolean isEmailAuthenticated(String email) throws EntityNotFoundException;
    void save(String toEmail, String authCode);
    void send(String payload) throws MessagingException;
    String setContext(String code);
}
