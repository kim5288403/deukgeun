package com.example.deukgeun.common.service;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.commom.service.implement.AuthMailServiceImpl;
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
public class AuthMailTest {

    @Mock
    private AuthMailRepository authMailRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private JavaMailSender emailSender;
    @Mock
    private JavaMailSender mockEmailSender;
    @Mock
    private SpringTemplateEngine mockTemplateEngine;

    @Test
    public void given_whenCreateCode_thenValidCodeGenerated() {
        // Given
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        // When
        String code = authMailService.createCode();

        // Then
        assertNotNull(code);
        assertEquals(8, code.length());

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            assertTrue(Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c));
        }
    }

    @Test
    public void givenEmailAndCode_whenSave_thenIsSaved() {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        // When
        authMailService.save(toEmail, authCode);

        // Then
        verify(authMailRepository, times(1)).save(any(AuthMail.class));
    }

    @Test
    public void givenToEmailAndAuthCode_whenCreateMailForm_thenIsCreated() throws MessagingException {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        String title = "득근득근 회원가입 인증 번호";
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, emailSender, templateEngine);

        // When
        MimeMessage message = authMailService.createMailForm(toEmail, authCode);

        // Then
        assertNotNull(message);
        assertEquals(toEmail, message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals(title, message.getSubject());
        assertEquals("text/plain", message.getContentType());
    }

    @Test
    public void givenToEmailAndAuthCode_whenSend_thenIsSend() throws MessagingException {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mockEmailSender.createMimeMessage()).willReturn(mimeMessage);
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, templateEngine);

        // When
        authMailService.send(toEmail, authCode);

        // Then
        verify(mockEmailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    public void givenCode_whenSetContext_thenProcessTemplate() {
        // Given
        String code = "123456";
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, templateEngine);

        // When
        String result = authMailService.setContext(code);

        // Then
        assertNotNull(result);
    }

    @Test
    public void givenEmail_whenDeleteByEmail_thenIsDeleted() {
        // Given
        String email = "test@example.com";
        given(authMailRepository.existsByEmail(email)).willReturn(true);
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        // When
        authMailService.deleteByEmail(email);

        // Then
        verify(authMailRepository, times(1)).deleteByEmail(email);
    }

    @Test
    public void givenEmailAndCode_whenConfirmMail_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        String code = "123456";
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        when(authMailRepository.existsByEmailAndCode(email, code)).thenReturn(true);

        // When
        boolean result = authMailService.confirmMail(email, code);

        // Then
        assertTrue(result);
        verify(authMailRepository, times(1)).existsByEmailAndCode(email, code);
    }

    @Test
    public void givenAuthMail_whenIsEmailAuthenticated_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        AuthMail authMail = AuthMail
                .builder()
                .email(email)
                .mailStatus(MailStatus.Y)
                .build();

        given(authMailRepository.findByEmail(email)).willReturn(Optional.of(authMail));
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        // When
        boolean result = authMailService.isEmailAuthenticated(email);

        // Then
        assertTrue(result);
        verify(authMailRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenUnauthenticatedEmail_whenIsEmailAuthenticated_thenReturnFalse() {
        // Given
        String email = "example@example.com";
        AuthMail authMail = AuthMail
                .builder()
                .email(email)
                .mailStatus(MailStatus.N)
                .build();

        given(authMailRepository.findByEmail(email)).willReturn(Optional.of(authMail));
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        // When
        boolean result = authMailService.isEmailAuthenticated(email);

        // Then
        assertFalse(result);
        verify(authMailRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenNonexistentEmail_whenIsEmailAuthenticated_thenThrowEntityNotFoundException() {
        // Given
        String email = "example@example.com";
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);
        given(authMailRepository.findByEmail(email)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> authMailService.isEmailAuthenticated(email));
        verify(authMailRepository, times(1)).findByEmail(email);
    }


    @Test
    public void givenAuthMailRequest_whenConfirm_thenMailStatusUpdated() throws EntityNotFoundException {
        // Given
        AuthMailRequest request = new AuthMailRequest();
        request.setEmail("example@example.com");
        AuthMail authMail = AuthMail
                .builder()
                .mailStatus(MailStatus.N)
                .build();
        given(authMailRepository.findByEmail(request.getEmail())).willReturn(Optional.of(authMail));
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        // When
        authMailService.confirm(request);

        // Then
        verify(authMailRepository, times(1)).findByEmail(request.getEmail());
        verify(authMailRepository, times(1)).save(authMail);
        assertEquals(MailStatus.Y, authMail.getMailStatus());
    }

    @Test
    public void givenNonexistentAuthMailRequest_whenConfirm_thenThrowEntityNotFoundException() throws EntityNotFoundException {
        // Given
        AuthMailRequest request = new AuthMailRequest();
        request.setEmail("example@example.com");
        AuthMail authMail = AuthMail
                .builder()
                .mailStatus(MailStatus.N)
                .build();
        given(authMailRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());
        AuthMailServiceImpl authMailService = new AuthMailServiceImpl(authMailRepository, mockEmailSender, mockTemplateEngine);

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> authMailService.confirm(request));
        verify(authMailRepository, times(1)).findByEmail(request.getEmail());
        verify(authMailRepository, never()).save(any(AuthMail.class));
    }

}
