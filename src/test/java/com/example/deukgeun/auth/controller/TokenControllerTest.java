package com.example.deukgeun.auth.controller;

import com.example.deukgeun.auth.application.controller.TokenController;
import com.example.deukgeun.auth.application.dto.request.LoginRequest;
import com.example.deukgeun.auth.application.service.PasswordEncoderService;
import com.example.deukgeun.auth.application.service.implement.TokenServiceImpl;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.trainer.domain.entity.Trainer;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.auth.application.dto.response.LoginResponse;
import com.example.deukgeun.auth.application.dto.response.RestResponse;
import com.example.deukgeun.member.infrastructure.persistence.MemberServiceImpl;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TokenControllerTest {
    @InjectMocks
    private TokenController tokenController;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private PasswordEncoderService passwordEncoderService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private TrainerServiceImpl trainerService;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private HttpServletResponse response;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenAuthToken_WhenLogout_ThenDeleteAuthTokenAndReturnOkResponse() {
        // Given
        String authToken = "dummyAuthToken";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그아웃 성공 했습니다.", null);
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = tokenController.logout(request);

        // Then
        verify(tokenService).deleteToken(authToken);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
    }

    @Test
    public void givenAuthToken_WhenGetUserPK_ThenResolveAuthTokenAndReturnUserPK() {
        // Given
        String authToken = "dummyAuthToken";
        String userPK = "dummyUserPK";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("이메일 조회 성공했습니다.", userPK);
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(tokenService.getUserPk(authToken)).willReturn(userPK);

        // When
        ResponseEntity<?> responseEntity = tokenController.getUserPK(request);

        // Then
        verify(tokenService).resolveAuthToken(request);
        verify(tokenService).getUserPk(authToken);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
    }

    @Test
    void givenValidLoginRequest_whenTrainerLogin_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String role = "trainer";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test");
        loginRequest.setPassword("test");
        loginRequest.setLoginType("trainer");

        LoginResponse loginResponse = LoginResponse
                .builder()
                .authToken(authToken)
                .role(role)
                .build();

        Trainer trainer = Trainer
                .builder()
                .id(123L)
                .password("encodePassword")
                .build();

        ReflectionTestUtils.setField(tokenController, "trainerRole", "trainer");
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(tokenService.setToken(loginRequest.getEmail(), response, role)).willReturn(authToken);
        given(trainerService.getByEmail(loginRequest.getEmail())).willReturn(trainer);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // When
        ResponseEntity<?> responseEntity = tokenController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(passwordEncoderService, times(1)).isPasswordMatches(anyString(), anyString());
        verify(tokenService, times(1)).setToken(loginRequest.getEmail(), response, role);
    }

    @Test
    void givenValidLoginRequest_whenMemberLogin_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String role = "member";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test");
        loginRequest.setPassword("test");
        loginRequest.setLoginType("member");

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

        ReflectionTestUtils.setField(tokenController, "memberRole", "member");
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(memberService.getByEmail(loginRequest.getEmail())).willReturn(member);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
        given(tokenService.setToken(loginRequest.getEmail(), response, role)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = tokenController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(passwordEncoderService, times(1)).isPasswordMatches(loginRequest.getPassword(), member.getPassword());
        verify(tokenService, times(1)).setToken(loginRequest.getEmail(), response, role);
    }
}
