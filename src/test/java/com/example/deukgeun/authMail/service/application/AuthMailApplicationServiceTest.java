package com.example.deukgeun.authMail.service.application;

import com.example.deukgeun.authMail.application.dto.request.AuthMailRequest;
import com.example.deukgeun.authMail.application.service.implement.AuthMailApplicationServiceImpl;
import com.example.deukgeun.authMail.domain.dto.ConfirmDTO;
import com.example.deukgeun.authMail.domain.service.AuthMailDomainService;
import com.example.deukgeun.authMail.infrastructure.persistence.mapper.AuthMailMapper;
import com.example.deukgeun.global.util.MailUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

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
    private AuthMailApplicationServiceImpl authMailApplicationService;
    @Mock
    private AuthMailMapper authMailMapper;


    @BeforeAll
    public static void staticSetup() {
        mockStatic(MailUtil.class);
    }

    @Test
    void givenValidAuthMailRequest_whenConfirm_thenConfirmCalled() throws EntityNotFoundException {
        // Given
        AuthMailRequest request = mock(AuthMailRequest.class);
        ConfirmDTO confirmDTO = mock(ConfirmDTO.class);
        given(authMailMapper.toConfirmDto(any(AuthMailRequest.class))).willReturn(confirmDTO);

        // When
        authMailApplicationService.confirm(request);

        // Then
        verify(authMailDomainService, times(1)).confirm(any(ConfirmDTO.class));
    }

    @Test
    void givenValidMailUtil_whenCreateCode_thenValidCodeGenerated() {
        // Given
        given(MailUtil.createCode()).willReturn("AB1C2dwd");

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
    void givenValidEmail_whenDeleteByEmail_thenDeleteByEmailCalled() {
        // Given
        String email = "test@example.com";
        given(authMailDomainService.existsByEmail(anyString())).willReturn(true);

        // When
        authMailApplicationService.deleteByEmail(email);

        // Then
        verify(authMailDomainService, times(1)).existsByEmail(anyString());
        verify(authMailDomainService, times(1)).deleteByEmail(anyString());
    }

    @Test
    void givenInValidEmail_whenDeleteByEmail_thenDeleteByEmailNotCalled() {
        // Given
        String email = "test@example.com";

        // When
        authMailApplicationService.deleteByEmail(email);

        // Then
        verify(authMailDomainService, times(1)).existsByEmail(anyString());
        verify(authMailDomainService, times(0)).deleteByEmail(anyString());
    }

    @Test
    void givenValidEmailAndCode_whenExistsByEmailAndCode_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(anyString(), anyString())).willReturn(true);

        // When
        boolean result = authMailApplicationService.existsByEmailAndCode(email, code);

        // Then
        assertTrue(result);
        verify(authMailDomainService, times(1)).existsByEmailAndCode(anyString(), anyString());
    }

    @Test
    void givenInValidEmailAndCode_whenExistsByEmailAndCode_thenReturnFalse() {
        // Given
        String email = "example@example.com";
        String code = "123456";

        given(authMailDomainService.existsByEmailAndCode(anyString(), anyString())).willReturn(false);

        // When
        boolean result = authMailApplicationService.existsByEmailAndCode(email, code);

        // Then
        assertFalse(result);
        verify(authMailDomainService, times(1)).existsByEmailAndCode(anyString(), anyString());
    }

    @Test
    void givenValidEmail_whenIsEmailAuthenticated_thenReturnTrue() {
        // Given
        String email = "example@example.com";
        given(authMailDomainService.isEmailAuthenticated(anyString())).willReturn(true);

        // When
        boolean result = authMailApplicationService.isEmailAuthenticated(email);

        // Then
        assertTrue(result);
        verify(authMailDomainService, times(1)).isEmailAuthenticated(anyString());
    }

    @Test
    void givenInValidEmail_whenIsEmailAuthenticated_thenReturnFalse() {
        // Given
        String email = "example@example.com";

        given(authMailDomainService.isEmailAuthenticated(anyString())).willReturn(false);

        // When
        boolean result = authMailApplicationService.isEmailAuthenticated(email);

        // Then
        assertFalse(result);
        verify(authMailDomainService, times(1)).isEmailAuthenticated(anyString());
    }

    @Test
    void givenValidAuthMailRequest_whenSave_thenSaveCalled() {
        // Given
        AuthMailRequest authMailRequest = new AuthMailRequest();
        authMailRequest.setEmail("email");
        authMailRequest.setCode("code");

        // When
        authMailApplicationService.save(authMailRequest);

        // Then
        verify(authMailDomainService, times(1)).save(anyString(), anyString());
    }
}
