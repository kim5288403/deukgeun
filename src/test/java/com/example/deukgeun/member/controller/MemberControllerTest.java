package com.example.deukgeun.member.controller;

import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.member.entity.Member;
import com.example.deukgeun.member.request.JoinRequest;
import com.example.deukgeun.commom.request.LoginRequest;
import com.example.deukgeun.member.service.implement.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
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

    @Test
    void givenValidLoginRequest_whenLogin_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String role = "member";
        LoginRequest loginRequest = mock(LoginRequest.class);
        LoginResponse loginResponse = LoginResponse
                .builder()
                .authToken(authToken)
                .role(role)
                .build();
        Member member = Member
                .builder()
                .id(123L)
                .password("encodePassword")
                .build();

        ReflectionTestUtils.setField(memberController, "role", "member");
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(memberService.getByEmail(loginRequest.getEmail())).willReturn(member);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(tokenService.setToken(loginRequest.getEmail(), response, role)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = memberController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(memberService, times(1)).isPasswordMatches(loginRequest.getPassword(), member);
        verify(tokenService, times(1)).setToken(loginRequest.getEmail(), response, role);
    }

}
