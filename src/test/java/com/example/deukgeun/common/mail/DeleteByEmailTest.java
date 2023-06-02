package com.example.deukgeun.common.mail;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
public class DeleteByEmailTest {
    @Autowired
    private MailServiceImpl mailService;
    @Autowired
    private AuthMailRepository authMailRepository;

    @Test
    void shouldDeleteByEmailForValidParameter() {
        // Given
        String email = "testEmail@Email.com";
        String code = "1t2e3s4t";
        MailStatus status = MailStatus.N;
        AuthMail authMail = AuthMailRequest.create(email, code, status);
        authMailRepository.save(authMail);
        boolean before = authMailRepository.existsByEmail(email);

        // When
        mailService.deleteByEmail(email);
        boolean result = authMailRepository.existsByEmail(email);

        // Then
        assertFalse(result);
        assertTrue(before);
    }
}
