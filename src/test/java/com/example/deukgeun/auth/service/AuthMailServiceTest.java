package com.example.deukgeun.auth.service;

import com.example.deukgeun.auth.application.dto.request.AuthMailRequest;
import com.example.deukgeun.auth.application.service.implement.AuthMailApplicationServiceImpl;
import com.example.deukgeun.auth.domain.entity.AuthMail;
import com.example.deukgeun.auth.infrastructure.repository.AuthMailRepository;
import com.example.deukgeun.auth.domain.service.implement.AuthMailServiceImpl;
import com.example.deukgeun.auth.domain.valueobject.MailStatus;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthMailServiceTest {

    @Mock
    private AuthMailRepository authMailRepository;
    @Mock
    private AuthMailServiceImpl authMailService;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private JavaMailSender emailSender;
    @Mock
    private JavaMailSender mockEmailSender;
    @Mock
    private SpringTemplateEngine mockTemplateEngine;


    @Test
    void given_whenCreateCode_thenValidCodeGenerated() {
        // Given
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When
        String code = authMailApplicationService.createCode();

        // Then
        assertNotNull(code);
        assertEquals(8, code.length());

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            assertTrue(Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c));
        }
    }

    @Test
    void givenEmailAndCode_whenSave_thenIsSaved() {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When
        authMailApplicationService.save(toEmail, authCode);

        // Then
        verify(authMailService, times(1)).save(toEmail, authCode);
    }

    @Test
    void givenToEmailAndAuthCode_whenCreateMailForm_thenIsCreated() throws MessagingException {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        String title = "득근득근 회원가입 인증 번호";
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(emailSender, templateEngine, authMailService);

        // When
        MimeMessage message = authMailApplicationService.createMailForm(toEmail, authCode);

        // Then
        assertNotNull(message);
        assertEquals(toEmail, message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals(title, message.getSubject());
        assertEquals("text/plain", message.getContentType());
    }

    @Test
    void givenToEmailAndAuthCode_whenSend_thenIsSend() throws MessagingException {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mockEmailSender.createMimeMessage()).willReturn(mimeMessage);
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When
        authMailApplicationService.send(toEmail, authCode);

        // Then
        verify(mockEmailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void givenCode_whenSetContext_thenProcessTemplate() {
        // Given
        String code = "123456";
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(emailSender, templateEngine, authMailService);

        // When
        String result = authMailApplicationService.setContext(code);

        // Then
        assertNotNull(result);
    }

    @Test
    void givenEmail_whenDeleteByEmail_thenIsDeleted() {
        // Given
        String email = "test@example.com";
        given(authMailService.existsByEmail(email)).willReturn(true);
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When
        authMailApplicationService.deleteByEmail(email);

        // Then
        verify(authMailService, times(1)).deleteByEmail(email);
    }

    @Test
    void givenEmailAndCode_whenConfirmMail_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        String code = "123456";
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        given(authMailService.existsByEmailAndCode(email, code)).willReturn(true);

        // When
        boolean result = authMailApplicationService.existsByEmailAndCode(email, code);

        // Then
        assertTrue(result);
        verify(authMailService, times(1)).existsByEmailAndCode(email, code);
    }

    @Test
    void givenAuthMail_whenIsEmailAuthenticated_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        AuthMail authMail = AuthMail
                .builder()
                .email(email)
                .mailStatus(MailStatus.Y)
                .build();

        given(authMailService.findByEmail(email)).willReturn(Optional.of(authMail));
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When
        boolean result = authMailApplicationService.isEmailAuthenticated(email);

        // Then
        assertTrue(result);
        verify(authMailService, times(1)).findByEmail(email);
    }

    @Test
    void givenUnauthenticatedEmail_whenIsEmailAuthenticated_thenReturnFalse() {
        // Given
        String email = "example@example.com";
        AuthMail authMail = AuthMail
                .builder()
                .email(email)
                .mailStatus(MailStatus.N)
                .build();

        given(authMailService.findByEmail(email)).willReturn(Optional.of(authMail));
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When
        boolean result = authMailApplicationService.isEmailAuthenticated(email);

        // Then
        assertFalse(result);
        verify(authMailService, times(1)).findByEmail(email);
    }

    @Test
    void givenNonexistentEmail_whenIsEmailAuthenticated_thenThrowEntityNotFoundException() {
        // Given
        String email = "example@example.com";
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);
        given(authMailService.findByEmail(email)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> authMailApplicationService.isEmailAuthenticated(email));
        verify(authMailService, times(1)).findByEmail(email);
    }


    @Test
    void givenAuthMailRequest_whenConfirm_thenMailStatusUpdated() throws EntityNotFoundException {
        // Given
        AuthMailRequest request = new AuthMailRequest();
        request.setEmail("example@example.com");
        AuthMail authMail = AuthMail
                .builder()
                .mailStatus(MailStatus.N)
                .build();
        given(authMailService.findByEmail(request.getEmail())).willReturn(Optional.of(authMail));
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When
        authMailApplicationService.confirm(request);

        // Then
        verify(authMailService, times(1)).findByEmail(request.getEmail());
        verify(authMailService, times(1)).updateMailStatus(MailStatus.Y, authMail);
    }

    @Test
    void givenNonexistentAuthMailRequest_whenConfirm_thenThrowEntityNotFoundException() throws EntityNotFoundException {
        // Given
        AuthMailRequest request = new AuthMailRequest();
        request.setEmail("example@example.com");

        given(authMailService.findByEmail(request.getEmail())).willReturn(Optional.empty());
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailService);

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> authMailApplicationService.confirm(request));
        verify(authMailService, times(1)).findByEmail(request.getEmail());
        verify(authMailService, never()).updateMailStatus(any(MailStatus.class), any(AuthMail.class));
    }
}
