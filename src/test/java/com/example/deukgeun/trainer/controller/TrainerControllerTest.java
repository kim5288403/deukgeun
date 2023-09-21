package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.TrainerController;
import com.example.deukgeun.trainer.application.dto.request.JoinRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.application.dto.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.application.dto.request.WithdrawalUserRequest;
import com.example.deukgeun.trainer.application.dto.response.TrainerResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrainerControllerTest {
    @InjectMocks
    private TrainerController trainerController;
    @Mock
    private TrainerApplicationService trainerApplicationService;
    @Mock
    private AuthTokenApplicationService authTokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    @Test
    void givenValidAuthToken_whenGetInfo_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String email = "Test";
        TrainerResponse.Info response = mock(TrainerResponse.Info.class);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);
        given(trainerApplicationService.getInfoByEmail(anyString())).willReturn(response);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);

        // When
        ResponseEntity<?> responseEntity = trainerController.getInfo(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(authTokenApplicationService, times(1)).getUserPk(anyString());
        verify(trainerApplicationService, times(1)).getInfoByEmail(anyString());
    }

    @Test
    void givenValidAuthToken_whenGetDetail_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String email = "Test";
        TrainerResponse.Detail response = mock(TrainerResponse.Detail.class);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);
        given(trainerApplicationService.getDetailByEmail(anyString())).willReturn(response);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);

        // When
        ResponseEntity<?> responseEntity = trainerController.getDetail(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(authTokenApplicationService, times(1)).getUserPk(anyString());
        verify(trainerApplicationService, times(1)).getDetailByEmail(anyString());
    }

    @Test
    void givenValidJoinRequest_whenSave_thenReturnSuccessResponse() throws IOException {
        // Given
        JoinRequest joinRequest = mock(JoinRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 가입 성공 했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = trainerController.save(joinRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerApplicationService, times(1)).save(any(JoinRequest.class));
    }

    @Test
    void givenValidUpdateInfoRequest_whenUpdate_thenReturnSuccessResponse() {
        // Given
        UpdateInfoRequest updateInfoRequest = mock(UpdateInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("내 정보 수정 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = trainerController.update(updateInfoRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerApplicationService, times(1)).updateInfo(any(UpdateInfoRequest.class));
    }

    @Test
    void givenValidUpdatePasswordRequest_whenUpdatePassword_thenReturnSuccessResponse() {
        // Given
        UpdatePasswordRequest updatePasswordRequest = mock(UpdatePasswordRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("비밀번호 변경 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = trainerController.updatePassword(updatePasswordRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerApplicationService, times(1)).updatePassword(any(UpdatePasswordRequest.class));
    }

    @Test
    void givenValidWithdrawalUserRequest_whenWithdrawal_thenReturnSuccessResponse() throws IOException {
        // Given
        String authToken = "validAuthToken";
        WithdrawalUserRequest withdrawalUserRequest = mock(WithdrawalUserRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 탈퇴 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(withdrawalUserRequest.getEmail()).willReturn("email");

        // When
        ResponseEntity<?> responseEntity = trainerController.withdrawal(request, withdrawalUserRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(trainerApplicationService, times(1)).delete(anyString());
        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(authTokenApplicationService, times(1)).deleteByAuthToken(anyString());
    }
}
