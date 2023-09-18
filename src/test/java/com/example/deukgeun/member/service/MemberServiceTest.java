package com.example.deukgeun.member.service;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.application.service.implement.MemberApplicationServiceImpl;
import com.example.deukgeun.member.domain.aggregate.Member;
import com.example.deukgeun.member.domain.service.implement.MemberDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberServiceTest {

    @InjectMocks
    private MemberApplicationServiceImpl memberApplicationService;
    @Mock
    private MemberDomainServiceImpl memberDomainService;
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

        Member member = Member.create(
                request.getEmail(),
                "encodePassword",
                request.getName(),
                request.getAge(),
                request.getGender()
                );

        given(memberDomainService.save(request)).willReturn(member);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encodePassword");

        // When
        Member saveMember = memberApplicationService.save(request);

        // Then
        assertNotNull(saveMember);
        assertEquals(member.getId(), saveMember.getId());
        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getPassword(), "encodePassword");

        // Verify
        verify(memberDomainService, times(1)).save(request);
    }

    @Test
    void givenExistingEmail_whenGetByEmail_thenReturnsMatchingTrainer() throws EntityNotFoundException {
        // Given
        String email = "johndoe@example.com";
        Member member = Member.create(
                email,
                "encodePassword",
                "test",
                23,
                Gender.M
        );

        given(memberDomainService.findByEmail(email)).willReturn(member);

        Member result = memberApplicationService.findByEmail(email);

        // Then
        assertNotNull(result);
        assertEquals(member.getEmail(), result.getEmail());

        // Verify
        verify(memberDomainService, times(1)).findByEmail(email);
    }

    @Test
    void givenNonexistentEmail_whenGetByEmail_thenThrowsEntityNotFoundException() {
        // Given
        String email = "nonexistent@example.com";

        given(memberDomainService.findByEmail(email)).willThrow(new EntityNotFoundException());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> memberApplicationService.findByEmail(email));

        // Verify
        verify(memberDomainService, times(1)).findByEmail(email);
    }

}
