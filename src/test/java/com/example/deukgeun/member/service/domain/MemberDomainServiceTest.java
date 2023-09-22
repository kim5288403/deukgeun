package com.example.deukgeun.member.service.domain;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.member.domain.aggregate.Member;
import com.example.deukgeun.member.domain.dto.MemberJoinDTO;
import com.example.deukgeun.member.domain.repository.MemberRepository;
import com.example.deukgeun.member.domain.service.implement.MemberDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberDomainServiceTest {
    @InjectMocks
    private MemberDomainServiceImpl memberDomainService;
    @Mock
    private MemberRepository memberRepository;

    @Test
    void givenValidId_whenFindById_thenReturnFoundIsMember() {
        // Given
        Long id = 1L;
        Member member = mock(Member.class);

        given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));

        // When
        Member result = memberDomainService.findById(id);

        // Then
        assertNotNull(result);
        verify(memberRepository, times(1)).findById(anyLong());
    }

    @Test
    void givenValidId_whenFindById_thenThrowsEntityNotFoundException() {
        // Given
        Long id = 1L;

        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> memberDomainService.findById(id));
    }

    @Test
    void givenValidEmail_whenFindById_thenReturnFoundIsMember() {
        // Given
        String email = "email";
        Member member = mock(Member.class);

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(member));

        // When
        Member result = memberDomainService.findByEmail(email);

        // Then
        assertNotNull(result);
        verify(memberRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void givenValidEmail_whenFindById_thenThrowsEntityNotFoundException() {
        // Given
        String email = "email";

        given(memberRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> memberDomainService.findByEmail(email));
    }

    @Test
    void givenValidEmail_whenLoadUserByMemberUsername_thenReturnUserDetails() {
        // Given
        String email = "email";
        UserDetails userDetails = mock(UserDetails.class);

        given(memberRepository.loadUserByUsername(anyString())).willReturn(userDetails);

        // When
        UserDetails result = memberDomainService.loadUserByMemberUsername(email);

        // Then
        assertNotNull(result);
        verify(memberRepository, times(1)).loadUserByUsername(anyString());
    }

    @Test
    void givenValidEmail_whenLoadUserByMemberUsername_thenThrowsUsernameNotFoundException() {
        // Given
        String email = "email";

        given(memberRepository.loadUserByUsername(anyString())).willThrow(UsernameNotFoundException.class);

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> memberDomainService.loadUserByMemberUsername(email));
    }




    @Test
    void givenValidMemberJoinDTO_whenSave_thenReturnSavedMember() {
        // Given
        Member member = mock(Member.class);
        MemberJoinDTO memberJoinDTO = new MemberJoinDTO();
        memberJoinDTO.setName("name");
        memberJoinDTO.setAge(20);
        memberJoinDTO.setEmail("email");
        memberJoinDTO.setPassword("password");
        memberJoinDTO.setGender(Gender.M);

        given(memberRepository.save(any(Member.class))).willReturn(member);

        // When
        Member result = memberDomainService.save(memberJoinDTO);

        // Then
        assertNotNull(result);
        verify(memberRepository, times(1)).save(any(Member.class));
    }

}
