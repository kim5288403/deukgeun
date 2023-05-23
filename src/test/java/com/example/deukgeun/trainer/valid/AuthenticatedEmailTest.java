package com.example.deukgeun.trainer.valid;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class AuthenticatedEmailTest {

    @Mock
    private AuthMailRepository authMailRepository;

    @InjectMocks
    private MailServiceImpl mailService;

    @Test
    void shouldReturnTrueForAuthenticatedEmail() throws Exception {
        // Given
        AuthMail authMail = new AuthMail("testEmail", "1234", MailStatus.Y);
        given(authMailRepository.findByEmail(anyString())).willReturn(Optional.of(authMail));

        // When
        boolean result = mailService.isEmailAuthenticated(anyString());

        // Then
        assertTrue(result);
        verify(authMailRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void shouldReturnFalseForUnauthenticatedEmail() throws Exception {
        // Given
        AuthMail authMail = new AuthMail("testEmail", "1234", MailStatus.N);
        given(authMailRepository.findByEmail(anyString())).willReturn(Optional.of(authMail));

        // When
        boolean result = mailService.isEmailAuthenticated(anyString());

        // Then
        assertFalse(result);
        verify(authMailRepository, times(1)).findByEmail(anyString());
    }
}
