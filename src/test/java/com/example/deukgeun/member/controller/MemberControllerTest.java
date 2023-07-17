package com.example.deukgeun.member.controller;

import com.example.deukgeun.auth.application.dto.response.RestResponse;
import com.example.deukgeun.auth.application.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.application.controller.MemberController;
import com.example.deukgeun.member.application.dto.request.JoinRequest;
import com.example.deukgeun.member.infrastructure.persistence.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class MemberControllerTest {
    @InjectMocks
    private MemberController memberController;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Test
    void givenValidJoinRequest_whenSave_thenReturnSuccessResponse() {
        // Given
        JoinRequest joinRequest = mock(JoinRequest.class);
        Member member = Member
                .builder()
                .id(123L)
                .name(joinRequest.getName())
                .age(joinRequest.getAge())
                .gender(joinRequest.getGender())
                .email(joinRequest.getEmail())
                .password("encodePassword")
                .build();

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 가입 성공 했습니다.", member);
        given(memberService.save(joinRequest)).willReturn(member);

        // When
        ResponseEntity<?> responseEntity = memberController.save(joinRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(memberService, times(1)).save(any(JoinRequest.class));
    }
}
