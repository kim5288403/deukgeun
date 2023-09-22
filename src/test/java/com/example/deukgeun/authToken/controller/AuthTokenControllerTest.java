package com.example.deukgeun.authToken.controller;

import com.example.deukgeun.authToken.application.controller.AuthTokenController;
import com.example.deukgeun.authToken.application.dto.request.LoginRequest;
import com.example.deukgeun.authToken.application.dto.response.LoginResponse;
import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.PasswordEncoderUtil;
import com.example.deukgeun.global.util.RestResponseUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthTokenControllerTest {
    @InjectMocks
    private AuthTokenController tokenController;
    @Mock
    private AuthTokenApplicationService tokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private BindingResult bindingResult;

    @Test
    void givenValidLoginRequest_whenTrainerLogin_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String role = "trainer";
        LoginRequest loginRequest = new LoginRequest("email", "password", role);

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("matchPassword", PasswordEncoderUtil.encode(loginRequest.getPassword()));
        loginData.put("role", role);

        LoginResponse loginResponse = mock(LoginResponse.class);

        given(tokenApplicationService.getLoginData(anyString(), anyString())).willReturn(loginData);
        given(tokenApplicationService.setToken(anyString(), any(HttpServletResponse.class), anyString())).willReturn(authToken);
        given(tokenApplicationService.getLoginResponse(anyString(), anyString())).willReturn(loginResponse);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        // When
        ResponseEntity<?> responseEntity = tokenController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(tokenApplicationService, times(1)).getLoginData(anyString(), anyString());
        verify(tokenApplicationService, times(1)).setToken(anyString(), any(HttpServletResponse.class), anyString());
        verify(tokenApplicationService, times(1)).getLoginResponse(anyString(), anyString());
    }

    @Test
    void givenValidLoginRequest_whenMemberLogin_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String role = "member";

        LoginRequest loginRequest = new LoginRequest("email", "password", role);

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("matchPassword", PasswordEncoderUtil.encode(loginRequest.getPassword()));
        loginData.put("role", role);

        LoginResponse loginResponse = mock(LoginResponse.class);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(tokenApplicationService.getLoginData(anyString(), anyString())).willReturn(loginData);
        given(tokenApplicationService.setToken(anyString(), any(HttpServletResponse.class), anyString())).willReturn(authToken);
        given(tokenApplicationService.getLoginResponse(anyString(), anyString())).willReturn(loginResponse);

        // When
        ResponseEntity<?> responseEntity = tokenController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(tokenApplicationService, times(1)).getLoginData(anyString(), anyString());
        verify(tokenApplicationService, times(1)).setToken(anyString(), any(HttpServletResponse.class), anyString());
        verify(tokenApplicationService, times(1)).getLoginResponse(anyString(), anyString());
    }

    @Test
    public void givenValidAuthToken_whenLogout_thenReturnSuccessResponse() {
        // Given
        String authToken = "dummyAuthToken";
        given(tokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그아웃 성공 했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = tokenController.logout(request);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
        verify(tokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(tokenApplicationService, times(1)).deleteByAuthToken(anyString());
    }

    @Test
    public void givenValidAuthToken_WhenGetUserPK_ThenReturnSuccessResponse() {
        // Given
        String authToken = "dummyAuthToken";
        String userPK = "dummyUserPK";
        given(tokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(tokenApplicationService.getUserPk(anyString())).willReturn(userPK);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("이메일 조회 성공했습니다.", userPK);

        // When
        ResponseEntity<?> responseEntity = tokenController.getUserPK(request);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
        verify(tokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(tokenApplicationService, times(1)).getUserPk(anyString());
    }


}
