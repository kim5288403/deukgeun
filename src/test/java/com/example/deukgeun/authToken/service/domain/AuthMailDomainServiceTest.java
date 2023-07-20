package com.example.deukgeun.authToken.service.domain;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.domain.model.valueobject.MailStatus;
import com.example.deukgeun.authToken.domain.model.entity.AuthMail;
import com.example.deukgeun.authToken.domain.repository.AuthMailRepository;
import com.example.deukgeun.authToken.domain.service.implement.AuthMailDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AuthMailDomainServiceTest {
    @InjectMocks
    private AuthMailDomainServiceImpl authMailDomainService;
    @Mock
    private AuthMailRepository authMailRepository;

    @Test
    public void givenAuthMailRequest_givenAuthMailExists_whenConfirmInvoked_thenUpdateMailStatusAndSaveAuthMail() {
        // Given
        AuthMailRequest request = new AuthMailRequest();
        request.setEmail("test@example.com");
        request.setCode("123456");

        AuthMail authMail = new AuthMail(123L,"test@example.com", "123456", MailStatus.N);
        given(authMailRepository.findByEmailAndCode(request.getEmail(), request.getCode())).willReturn(Optional.of(authMail));

        // When
        authMailDomainService.confirm(request);

        // Then
        assertEquals(authMail.getMailStatus(), MailStatus.Y);
        verify(authMailRepository, times(1)).save(authMail);
    }

    @Test
    public void givenExistingEmail_whenDeleteByEmailInvoked_thenDeleteByEmailInAuthMailRepository() {
        // Given
        String email = "test@example.com";
        given(authMailRepository.existsByEmail(email)).willReturn(true);

        // When
        authMailDomainService.deleteByEmail(email);

        // Then
        verify(authMailRepository, times(1)).deleteByEmail(email);
    }

    @Test
    public void givenExistingEmail_whenExistsByEmailInvoked_thenReturnTrue() {
        // Given
        String email = "test@example.com";
        given(authMailRepository.existsByEmail(email)).willReturn(true);

        // When
        boolean result = authMailDomainService.existsByEmail(email);

        // Then
        assertTrue(result);
    }

    @Test
    void givenEmailAndCode_whenExistsByEmailAndCode_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(email, code)).willReturn(true);

        // When
        boolean result = authMailDomainService.existsByEmailAndCode(email, code);

        // Then
        assertTrue(result);
        verify(authMailRepository, times(1)).existsByEmailAndCode(email, code);
    }

    @Test
    void givenEmailAndCode_whenExistsByEmailAndCode_thenReturnFalse() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(email, code)).willReturn(false);

        // When
        boolean result = authMailDomainService.existsByEmailAndCode(email, code);

        // Then
        assertFalse(result);
        verify(authMailRepository, times(1)).existsByEmailAndCode(email, code);
    }

    @Test
    public void givenExistingEmail_whenFindByEmail_thenReturnAuthMail() {
        // Given
        String email = "test@example.com";
        AuthMail authMail = new AuthMail(123L, email, "123456", MailStatus.N);
        given(authMailRepository.findByEmail(email)).willReturn(Optional.of(authMail));

        // When
        AuthMail result = authMailDomainService.findByEmail(email);

        // Then
        assertEquals(result, authMail);
    }

    @Test
    public void givenExistingEmail_whenFindByEmail_thenThrowEntityNotFoundException() {
        // Given
        String email = "test@example.com";
        AuthMail authMail = new AuthMail(123L, email, "123456", MailStatus.N);
        given(authMailRepository.findByEmail(email)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> authMailDomainService.findByEmail(email));
        verify(authMailRepository, times(1)).findByEmail(email);
    }

    @Test
    public void givenEmailAndCode_whenFindByEmailAndCode_thenReturnAuthMail() {
        // Given
        String email = "test@example.com";
        String code = "123456";
        AuthMail authMail = new AuthMail(123L, email, code, MailStatus.N);
        given(authMailRepository.findByEmailAndCode(email, code)).willReturn(Optional.of(authMail));

        // When
        AuthMail result = authMailDomainService.findByEmailAndCode(email, code);

        // Then
        assertEquals(result, authMail);
    }

    @Test
    public void givenEmailAndCode_whenFindByEmailAndCode_thenEntityNotFoundException() {
        // Given
        String email = "test@example.com";
        String code = "123456";
        AuthMail authMail = new AuthMail(123L, email, code, MailStatus.N);
        given(authMailRepository.findByEmailAndCode(email, code)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> authMailDomainService.findByEmailAndCode(email, code));
        verify(authMailRepository, times(1)).findByEmailAndCode(email, code);
    }

    @Test
    public void givenAuthenticatedEmail_whenIsEmailAuthenticated_thenReturnTrue() {
        // Given
        String email = "test@example.com";
        AuthMail authMail = new AuthMail(123L, email, "123456", MailStatus.Y);
        given(authMailRepository.findByEmail(email)).willReturn(Optional.of(authMail));

        // When
        boolean result = authMailDomainService.isEmailAuthenticated(email);

        // Then
        assertTrue(result);
    }

    @Test
    public void givenAuthenticatedEmail_whenIsEmailAuthenticated_thenReturnFalse() {
        // Given
        String email = "test@example.com";
        AuthMail authMail = new AuthMail(123L, email, "123456", MailStatus.N);
        given(authMailRepository.findByEmail(email)).willReturn(Optional.of(authMail));

        // When
        boolean result = authMailDomainService.isEmailAuthenticated(email);

        // Then
        assertFalse(result);
    }

    @Test
    void givenNonexistentEmail_whenIsEmailAuthenticated_thenThrowEntityNotFoundException() {
        // Given
        String email = "example@example.com";
        given(authMailRepository.findByEmail(email)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> authMailDomainService.isEmailAuthenticated(email));
    }

    @Test
    public void givenToEmailAndAuthCode_whenSave_thenCreateAuthMailAndSaveInAuthMailRepository() {
        // Given
        String toEmail = "test@example.com";
        String authCode = "123456";

        // When
        authMailDomainService.save(toEmail, authCode);

        // Then
        verify(authMailRepository, times(1)).save(any(AuthMail.class));
    }
}
