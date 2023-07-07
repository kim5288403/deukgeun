package com.example.deukgeun.member.service;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.exception.PasswordMismatchException;
import com.example.deukgeun.main.request.LoginRequest;
import com.example.deukgeun.global.entity.Member;
import com.example.deukgeun.global.repository.MemberRepository;
import com.example.deukgeun.member.request.JoinRequest;
import com.example.deukgeun.member.service.implement.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void givenMember_whenSave_thenTrainerIsSavedAndReturned() {
        // Given
        JoinRequest request = new JoinRequest();
        request.setName("test");
        request.setAge(123);
        request.setEmail("test");
        request.setPassword("test");
        request.setGender(Gender.M);

        Member member = Member
                .builder()
                .id(123L)
                .name(request.getName())
                .age(request.getAge())
                .gender(request.getGender())
                .email(request.getEmail())
                .password("encodePassword")
                .build();

        given(memberRepository.save(any(Member.class))).willReturn(member);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodePassword");

        // When
        Member saveMember = memberService.save(request);

        // Then
        assertNotNull(saveMember);
        assertEquals(member.getId(), saveMember.getId());
        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getPassword(), "encodePassword");

        // Verify
        verify(memberRepository, times(1)).save(any(Member.class));
    }

    @Test
    void givenMatchingPassword_whenIsPasswordMatches_thenNoExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest(email, password);
        Member member = Member
                .builder()
                .id(123L)
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        given(passwordEncoder.matches(password, member.getPassword())).willReturn(true);

        // When & Then
        assertDoesNotThrow(() -> memberService.isPasswordMatches(request.getPassword(), member));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void givenMismatchingPassword_whenIsPasswordMatches_thenPasswordMismatchExceptionThrown() throws EntityNotFoundException {
        // Given
        String email = "example@example.com";
        String password = "password123";
        String encodedPassword = "encodedPassword123";

        LoginRequest request = new LoginRequest(email, password);
        Member member = Member
                .builder()
                .id(123L)
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        given(passwordEncoder.matches(password, member.getPassword())).willReturn(false);

        // When & Then
        assertThrows(PasswordMismatchException.class, () -> memberService.isPasswordMatches(request.getPassword(), member));

        // Verify
        verify(passwordEncoder, times(1)).matches(password, encodedPassword);
    }

    @Test
    void givenExistingEmail_whenGetByEmail_thenReturnsMatchingTrainer() throws EntityNotFoundException {
        // Given
        String email = "johndoe@example.com";
        Member member = Member
                .builder()
                .id(123L)
                .email(email)
                .build();

        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        Member result = memberService.getByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(member.getEmail(), result.getEmail());

        // Verify
        verify(memberRepository, times(1)).findByEmail(email);
    }

    @Test
    void givenNonexistentEmail_whenGetByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";

        given(memberRepository.findByEmail(email)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> memberService.getByEmail(email));

        // Verify
        verify(memberRepository, times(1)).findByEmail(email);
    }

}
