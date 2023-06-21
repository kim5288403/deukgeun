package com.example.deukgeun.common.service;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.service.implement.AuthMailServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

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

}
