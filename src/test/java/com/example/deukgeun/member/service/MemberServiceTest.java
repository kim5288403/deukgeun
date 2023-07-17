package com.example.deukgeun.member.service;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.infrastructure.persistence.MemberServiceImpl;
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
