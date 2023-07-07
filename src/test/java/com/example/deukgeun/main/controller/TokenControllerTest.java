package com.example.deukgeun.main.controller;

import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.main.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TokenControllerTest {
    @InjectMocks
    private TokenController logoutController;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private HttpServletRequest request;

    @Test
    public void givenAuthToken_WhenLogout_ThenDeleteAuthTokenAndReturnOkResponse() {
        // Given
        String authToken = "dummyAuthToken";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그아웃 성공 했습니다.", null);
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = logoutController.logout(request);

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
        ResponseEntity<?> responseEntity = logoutController.getUserPK(request);

        // Then
        verify(tokenService).resolveAuthToken(request);
        verify(tokenService).getUserPk(authToken);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(expectedResponse.getBody());
    }
}
