package com.example.deukgeun.authMail.service.application;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.service.implement.AuthMailApplicationServiceImpl;
import com.example.deukgeun.authMail.domain.dto.ConfirmDTO;
import com.example.deukgeun.authMail.domain.service.AuthMailDomainService;
import com.example.deukgeun.authMail.infrastructure.persistence.mapper.AuthMailMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthMailApplicationServiceTest {
    @Mock
    private AuthMailDomainService authMailDomainService;
    @InjectMocks
    private AuthMailApplicationServiceImpl mockAuthMailApplicationService;
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private JavaMailSender emailSender;
    @Mock
    private AuthMailMapper authMailMapper;
    @Mock
    private JavaMailSender mockEmailSender;
    @Mock
    private SpringTemplateEngine mockTemplateEngine;

    @Test
    void givenAuthMailRequest_whenConfirmInvoked_thenInvokeConfirmMethod() throws EntityNotFoundException {
        // Given
        AuthMailRequest request = mock(AuthMailRequest.class);
        ConfirmDTO confirmDTO = mock(ConfirmDTO.class);
        given(authMailMapper.toConfirmDto(any(AuthMailRequest.class))).willReturn(confirmDTO);

        // When
        mockAuthMailApplicationService.confirm(request);

        // Then
        verify(authMailDomainService, times(1)).confirm(confirmDTO);
    }

    @Test
    void given_whenCreateCode_thenValidCodeGenerated() {
        // When
        String code = mockAuthMailApplicationService.createCode();

        // Then
        assertNotNull(code);
        assertEquals(8, code.length());

        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);

            assertTrue(Character.isLowerCase(c) || Character.isUpperCase(c) || Character.isDigit(c));
        }
    }

    @Test
    void givenToEmailAndAuthCode_whenCreateMailForm_thenIsCreated() throws MessagingException {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        String title = "득근득근 회원가입 인증 번호";
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(emailSender, templateEngine, authMailDomainService, authMailMapper);

        // When
        MimeMessage message = authMailApplicationService.createMailForm(toEmail, authCode);

        // Then
        assertNotNull(message);
        assertEquals(toEmail, message.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
        assertEquals(title, message.getSubject());
        assertEquals("text/plain", message.getContentType());
    }

    @Test
    void givenEmail_whenDeleteByEmail_thenInvokeDeleteByEmailMethod() {
        // Given
        String email = "test@example.com";

        // When
        mockAuthMailApplicationService.deleteByEmail(email);

        // Then
        verify(authMailDomainService, times(1)).deleteByEmail(email);
    }

    @Test
    void givenEmailAndCode_whenExistsByEmailAndCode_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(email, code)).willReturn(true);

        // When
        boolean result = mockAuthMailApplicationService.existsByEmailAndCode(email, code);

        // Then
        assertTrue(result);
        verify(authMailDomainService, times(1)).existsByEmailAndCode(email, code);
    }

    @Test
    void givenEmailAndCode_whenExistsByEmailAndCode_thenReturnFalse() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(email, code)).willReturn(false);

        // When
        boolean result = mockAuthMailApplicationService.existsByEmailAndCode(email, code);

        // Then
        assertFalse(result);
        verify(authMailDomainService, times(1)).existsByEmailAndCode(email, code);
    }

    @Test
    void givenAuthMail_whenIsEmailAuthenticated_thenReturnTrue() {
        // Given
        String email = "example@example.com";

        given(authMailDomainService.isEmailAuthenticated(email)).willReturn(true);

        // When
        boolean result = mockAuthMailApplicationService.isEmailAuthenticated(email);

        // Then
        assertTrue(result);
        verify(authMailDomainService, times(1)).isEmailAuthenticated(email);
    }

    @Test
    void givenUnauthenticatedEmail_whenIsEmailAuthenticated_thenReturnFalse() {
        // Given
        String email = "example@example.com";

        given(authMailDomainService.isEmailAuthenticated(email)).willReturn(false);

        // When
        boolean result = mockAuthMailApplicationService.isEmailAuthenticated(email);

        // Then
        assertFalse(result);
        verify(authMailDomainService, times(1)).isEmailAuthenticated(email);
    }

    @Test
    void givenNonexistentEmail_whenIsEmailAuthenticated_thenThrowEntityNotFoundException() {
        // Given
        String email = "example@example.com";
        given(authMailDomainService.isEmailAuthenticated(email)).willThrow(new EntityNotFoundException());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> mockAuthMailApplicationService.isEmailAuthenticated(email));
        verify(authMailDomainService, times(1)).isEmailAuthenticated(email);
    }

    @Test
    void givenToEmailAndAuthCode_whenSend_thenIsSend() throws MessagingException {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        MimeMessage mimeMessage = mock(MimeMessage.class);
        given(mockEmailSender.createMimeMessage()).willReturn(mimeMessage);
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(mockEmailSender, mockTemplateEngine, authMailDomainService, authMailMapper);
        AuthMailRequest authMailRequest = new AuthMailRequest();
        authMailRequest.setEmail(toEmail);
        authMailRequest.setCode(authCode);

        // When
        authMailApplicationService.send(authMailRequest);

        // Then
        verify(mockEmailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void givenCode_whenSetContext_thenProcessTemplate() {
        // Given
        String code = "123456";
        AuthMailApplicationServiceImpl authMailApplicationService = new AuthMailApplicationServiceImpl(emailSender, templateEngine, authMailDomainService, authMailMapper);

        // When
        String result = authMailApplicationService.setContext(code);

        // Then
        assertNotNull(result);
    }

    @Test
    void givenEmailAndCode_whenSave_thenInvokeSaveMethod() {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";
        AuthMailRequest authMailRequest = new AuthMailRequest();
        authMailRequest.setEmail(toEmail);
        authMailRequest.setCode(authCode);

        // When
        mockAuthMailApplicationService.save(authMailRequest);

        // Then
        verify(authMailDomainService, times(1)).save(toEmail, authCode);
    }
}
