package com.example.deukgeun.member.service.application;

import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.application.service.implement.MemberApplicationServiceImpl;
import com.example.deukgeun.member.domain.aggregate.Member;
import com.example.deukgeun.member.domain.dto.MemberJoinDTO;
import com.example.deukgeun.member.domain.service.implement.MemberDomainServiceImpl;
import com.example.deukgeun.member.infrastructure.persistence.mapper.MemberMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberApplicationServiceTest {

    @InjectMocks
    private MemberApplicationServiceImpl memberApplicationService;
    @Mock
    private MemberDomainServiceImpl memberDomainService;
    @Mock
    private MemberMapper memberMapper;


    @Test
    void givenValidId_whenFindById_thenReturnFoundIsMember() throws EntityNotFoundException {
        // Given
        Long id = 1L;
        Member member = mock(Member.class);

        given(memberDomainService.findById(anyLong())).willReturn(member);

        Member result = memberApplicationService.findById(id);

        // Then
        assertNotNull(result);
        verify(memberDomainService, times(1)).findById(anyLong());
    }

    @Test
    void givenValidEmail_whenFindByEmail_thenReturnFoundIsMember() throws EntityNotFoundException {
        // Given
        String email = "johndoe@example.com";
        Member member = mock(Member.class);

        given(memberDomainService.findByEmail(anyString())).willReturn(member);

        Member result = memberApplicationService.findByEmail(email);

        // Then
        assertNotNull(result);
        verify(memberDomainService, times(1)).findByEmail(anyString());
    }

    @Test
    void givenValidJoinRequest_whenSave_thenSavedMember() {
        // Given
        JoinRequest request = mock(JoinRequest.class);
        MemberJoinDTO memberJoinDTO = mock(MemberJoinDTO.class);

        Member member = mock(Member.class);

        given(memberMapper.toMemberJoinDto(any(JoinRequest.class))).willReturn(memberJoinDTO);
        given(memberDomainService.save(any(MemberJoinDTO.class))).willReturn(member);

        // When
        Member saveMember = memberApplicationService.save(request);

        // Then
        assertNotNull(saveMember);
        verify(memberMapper, times(1)).toMemberJoinDto(any(JoinRequest.class));
        verify(memberDomainService, times(1)).save(any(MemberJoinDTO.class));
    }

}
