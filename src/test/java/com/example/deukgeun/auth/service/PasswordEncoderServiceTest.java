package com.example.deukgeun.auth.service;

import com.example.deukgeun.auth.application.dto.request.LoginRequest;
import com.example.deukgeun.auth.application.service.implement.PasswordEncoderServiceImpl;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.trainer.domain.entity.Trainer;
import com.example.deukgeun.global.exception.PasswordMismatchException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PasswordEncoderServiceTest {
    @InjectMocks
    private PasswordEncoderServiceImpl passwordEncoderService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenMatchingPassword_whenTrainerIsPasswordMatches_thenNoExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setLoginType("trainer");
        Trainer trainer = Trainer.builder().email(email).password(encodedPassword).build();

        given(passwordEncoder.matches(password, encodedPassword)).willReturn(true);

        // When & Then
        assertDoesNotThrow(() -> passwordEncoderService.isPasswordMatches(request.getPassword(), trainer.getPassword()));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void givenMismatchingPassword_whenTrainerIsPasswordMatches_thenPasswordMismatchExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setLoginType("trainer");
        Trainer trainer = Trainer.builder().email(email).password(encodedPassword).build();

        given(passwordEncoder.matches(password, encodedPassword)).willReturn(false);

        // When & Then
        assertThrows(PasswordMismatchException.class, () -> passwordEncoderService.isPasswordMatches(request.getPassword(), trainer.getPassword()));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void givenMatchingPassword_whenMemberIsPasswordMatches_thenNoExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setLoginType("member");
        Member member = Member.builder().id(123L).email(request.getEmail()).password(encodedPassword).build();

        given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);

        // When & Then
        assertDoesNotThrow(() -> passwordEncoderService.isPasswordMatches(request.getPassword(), member.getPassword()));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void givenMismatchingPassword_whenMemberIsPasswordMatches_thenPasswordMismatchExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);
        request.setLoginType("member");
        Member member = Member.builder().id(123L).email(request.getEmail()).password(encodedPassword).build();

        given(passwordEncoder.matches(password, member.getPassword())).willReturn(false);

        // When & Then
        assertThrows(PasswordMismatchException.class, () -> passwordEncoderService.isPasswordMatches(request.getPassword(), member.getPassword()));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, member.getPassword());
    }

}
