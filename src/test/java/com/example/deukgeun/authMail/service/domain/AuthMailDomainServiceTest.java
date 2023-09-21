package com.example.deukgeun.authMail.service.domain;

import com.example.deukgeun.authMail.domain.dto.ConfirmDTO;
import com.example.deukgeun.authMail.domain.model.entity.AuthMail;
import com.example.deukgeun.authMail.domain.model.valueobject.MailStatus;
import com.example.deukgeun.authMail.domain.repository.AuthMailRepository;
import com.example.deukgeun.authMail.domain.service.implement.AuthMailDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    public void givenValidConfirmDTO_whenConfirm_thenMailStatusIsUpdated() {
        // Given
        ConfirmDTO confirmDTO = new ConfirmDTO();
        confirmDTO.setEmail("test@example.com");
        confirmDTO.setCode("123456");

        AuthMail authMail = AuthMail.create(confirmDTO.getEmail(), confirmDTO.getCode());
        given(authMailRepository.findByEmailAndCode(anyString(), anyString())).willReturn(Optional.of(authMail));

        // When
        authMailDomainService.confirm(confirmDTO);

        // Then
        assertEquals(authMail.getMailStatus(), MailStatus.Y);
        verify(authMailRepository, times(1)).save(any(AuthMail.class));
    }

    @Test
    public void givenValidEmail_whenDeleteByEmail_thenDeleteByEmailCalled() {
        // Given
        String email = "test@example.com";

        // When
        authMailDomainService.deleteByEmail(email);

        // Then
        verify(authMailRepository, times(1)).deleteByEmail(email);
    }

    @Test
    public void givenValidEmail_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "test@example.com";
        given(authMailRepository.existsByEmail(anyString())).willReturn(true);

        // When
        boolean result = authMailDomainService.existsByEmail(email);

        // Then
        assertTrue(result);
        verify(authMailRepository, times(1)).existsByEmail(anyString());
    }

    @Test
    void givenValidEmailAndCode_whenExistsByEmailAndCode_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(anyString(), anyString())).willReturn(true);

        // When
        boolean result = authMailDomainService.existsByEmailAndCode(email, code);

        // Then
        assertTrue(result);
        verify(authMailRepository, times(1)).existsByEmailAndCode(anyString(), anyString());
    }

    @Test
    void givenInValidEmailAndCode_whenExistsByEmailAndCode_thenReturnFalse() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(anyString(), anyString())).willReturn(false);

        // When
        boolean result = authMailDomainService.existsByEmailAndCode(email, code);

        // Then
        assertFalse(result);
        verify(authMailRepository, times(1)).existsByEmailAndCode(anyString(), anyString());
    }

    @Test
    public void givenValidEmail_whenFindByEmail_thenReturnFoundIsAuthMail() {
        // Given
        String email = "test@example.com";
        AuthMail authMail = AuthMail.create(email, "code");
        given(authMailRepository.findByEmail(anyString())).willReturn(Optional.of(authMail));

        // When
        AuthMail result = authMailDomainService.findByEmail(email);

        // Then
        assertEquals(result, authMail);
        verify(authMailRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void givenInValidEmail_whenFindByEmail_thenThrowEntityNotFoundException() {
        // Given
        String email = "test@example.com";
        given(authMailRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> authMailDomainService.findByEmail(email));
        verify(authMailRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void givenValidEmailAndCode_whenFindByEmailAndCode_thenReturnFoundIsAuthMail() {
        // Given
        String email = "test@example.com";
        String code = "123456";
        AuthMail authMail = AuthMail.create(email, code);
        given(authMailRepository.findByEmailAndCode(anyString(), anyString())).willReturn(Optional.of(authMail));

        // When
        AuthMail result = authMailDomainService.findByEmailAndCode(email, code);

        // Then
        assertEquals(result, authMail);
        verify(authMailRepository, times(1)).findByEmailAndCode(anyString(), anyString());
    }

    @Test
    public void givenInValidEmailAndCode_whenFindByEmailAndCode_thenEntityNotFoundException() {
        // Given
        String email = "test@example.com";
        String code = "123456";
        given(authMailRepository.findByEmailAndCode(anyString(), anyString())).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> authMailDomainService.findByEmailAndCode(email, code));
        verify(authMailRepository, times(1)).findByEmailAndCode(anyString(), anyString());
    }

    @Test
    public void givenValidMailStatus_whenIsEmailAuthenticated_thenReturnTrue() {
        // Given
        String email = "test@example.com";
        AuthMail authMail = AuthMail.create(email, "code");
        authMail.updateMailStatus(MailStatus.Y);

        given(authMailRepository.findByEmail(anyString())).willReturn(Optional.of(authMail));

        // When
        boolean result = authMailDomainService.isEmailAuthenticated(email);

        // Then
        assertTrue(result);
        verify(authMailRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void givenInValidMailStatus_whenIsEmailAuthenticated_thenReturnFalse() {
        // Given
        String email = "test@example.com";
        AuthMail authMail = AuthMail.create(email, "code");
        given(authMailRepository.findByEmail(anyString())).willReturn(Optional.of(authMail));

        // When
        boolean result = authMailDomainService.isEmailAuthenticated(email);

        // Then
        assertFalse(result);
        verify(authMailRepository, times(1)).findByEmail(anyString());
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
