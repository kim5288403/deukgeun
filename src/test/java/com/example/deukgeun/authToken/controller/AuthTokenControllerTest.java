package com.example.deukgeun.authToken.controller;

import com.example.deukgeun.authToken.application.controller.AuthTokenController;
import com.example.deukgeun.authToken.application.dto.request.LoginRequest;
import com.example.deukgeun.authToken.application.dto.response.LoginResponse;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
    public void givenAuthToken_WhenLogout_ThenDeleteAuthTokenAndReturnOkResponse() {
        // Given
        String authToken = "dummyAuthToken";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그아웃 성공 했습니다.", null);
        given(tokenApplicationService.resolveAuthToken(request)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = tokenController.logout(request);

        // Then
        verify(tokenApplicationService).deleteByAuthToken(authToken);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
    }

    @Test
    public void givenAuthToken_WhenGetUserPK_ThenResolveAuthTokenAndReturnUserPK() {
        // Given
        String authToken = "dummyAuthToken";
        String userPK = "dummyUserPK";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("이메일 조회 성공했습니다.", userPK);
        given(tokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(tokenApplicationService.getUserPk(authToken)).willReturn(userPK);

        // When
        ResponseEntity<?> responseEntity = tokenController.getUserPK(request);

        // Then
        verify(tokenApplicationService).resolveAuthToken(request);
        verify(tokenApplicationService).getUserPk(authToken);
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
        loginRequest.setLoginType(role);

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("matchPassword", PasswordEncoderUtil.encode(loginRequest.getPassword()));
        loginData.put("role", role);

        LoginResponse loginResponse = LoginResponse
                .builder()
                .authToken(authToken)
                .role(role)
                .build();

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(tokenApplicationService.getLoginData(loginRequest.getLoginType(), loginRequest.getEmail())).willReturn(loginData);
        given(tokenApplicationService.setToken(loginRequest.getEmail(), response, role)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = tokenController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(tokenApplicationService, times(1)).setToken(loginRequest.getEmail(), response, role);
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

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("matchPassword", PasswordEncoderUtil.encode(loginRequest.getPassword()));
        loginData.put("role", role);

        LoginResponse loginResponse = LoginResponse
                .builder()
                .authToken(authToken)
                .role(role)
                .build();

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(tokenApplicationService.getLoginData(loginRequest.getLoginType(), loginRequest.getEmail())).willReturn(loginData);
        given(tokenApplicationService.setToken(loginRequest.getEmail(), response, role)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = tokenController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(tokenApplicationService, times(1)).setToken(loginRequest.getEmail(), response, role);
    }
}
