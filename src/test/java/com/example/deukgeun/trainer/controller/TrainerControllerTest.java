package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.commom.request.LoginRequest;
import com.example.deukgeun.commom.response.LoginResponse;
import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.Trainer;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
import com.example.deukgeun.trainer.request.UpdatePasswordRequest;
import com.example.deukgeun.trainer.request.WithdrawalUserRequest;
import com.example.deukgeun.trainer.response.TrainerResponse;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrainerControllerTest {
    @InjectMocks
    private TrainerController trainerController;
    @Mock
    private ProfileServiceImpl profileService;
    @Mock
    private TrainerServiceImpl trainerService;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletResponse response;
    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup () {
        ReflectionTestUtils.setField(trainerController, "role", "trainer");
    }

    @Test
    void givenValidAuthToken_whenGetDetail_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        Trainer expectedTrainer = Trainer
                .builder()
                .name("test")
                .price(3000)
                .jibunAddress("test")
                .detailAddress("test")
                .roadAddress("test")
                .gender(Gender.M)
                .groupName("")
                .build();
        TrainerResponse response = new TrainerResponse(expectedTrainer);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("마이 페이지 조회 성공했습니다.", response);

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(trainerService.getByAuthToken(authToken)).willReturn(expectedTrainer);

        // When
        ResponseEntity<?> responseEntity = trainerController.getDetail(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(trainerService, times(1)).getByAuthToken(authToken);
    }

    @Test
    void givenValidJoinRequest_whenSave_thenReturnSuccessResponse() throws IOException {
        // Given
        Trainer savedTrainer = Trainer
                .builder()
                .name("test")
                .price(3000)
                .jibunAddress("test")
                .detailAddress("test")
                .roadAddress("test")
                .gender(Gender.M)
                .groupName("")
                .build();

        JoinRequest joinRequest = mock(JoinRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 가입 성공 했습니다.", null);

        given(trainerService.save(joinRequest)).willReturn(savedTrainer);

        // When
        ResponseEntity<?> responseEntity = trainerController.save(joinRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(trainerService, times(1)).save(joinRequest);
        verify(profileService, times(1)).save(joinRequest.getProfile(), savedTrainer.getId());
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
        verify(trainerService).updateInfo(updateInfoRequest);
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
        verify(trainerService).updatePassword(updatePasswordRequest);
    }

    @Test
    void givenValidWithdrawalUserRequest_whenWithdrawal_thenReturnSuccessResponse() throws IOException {
        // Given
        String authToken = "validAuthToken";
        WithdrawalUserRequest withdrawalUserRequest = mock(WithdrawalUserRequest.class);
        Trainer trainer = new Trainer();
        Profile userProfile = new Profile();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("회원 탈퇴 성공했습니다.", null);

        given(trainerService.getByEmail(withdrawalUserRequest.getEmail())).willReturn(trainer);
        given(profileService.getByTrainerId(trainer.getId())).willReturn(userProfile);
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = trainerController.withdrawal(request, withdrawalUserRequest, null);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(profileService, times(1)).deleteFileToDirectory(userProfile.getPath());
        verify(profileService, times(1)).withdrawal(userProfile.getId());
        verify(trainerService, times(1)).withdrawal(trainer.getId());
        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(tokenService, times(1)).deleteToken(anyString());
    }

    @Test
    void givenValidLoginRequest_whenLogin_thenReturnSuccessResponse() {
        // Given
        String authToken = "validAuthToken";
        String role = "trainer";
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test");
        loginRequest.setPassword("test");

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

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("로그인 성공 했습니다.", loginResponse);

        given(tokenService.setToken(loginRequest.getEmail(), response, role)).willReturn(authToken);
        given(trainerService.getByEmail(loginRequest.getEmail())).willReturn(trainer);
        given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);

        // When
        ResponseEntity<?> responseEntity = trainerController.login(loginRequest, bindingResult, response);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerService, times(1)).isPasswordMatches(anyString(), any(Trainer.class));
        verify(tokenService, times(1)).setToken(loginRequest.getEmail(), response, role);
    }
}
