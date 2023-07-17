package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.auth.application.dto.response.RestResponse;
import com.example.deukgeun.auth.application.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.domain.entity.Trainer;
import com.example.deukgeun.trainer.domain.entity.Profile;
import com.example.deukgeun.trainer.application.controller.ProfileController;
import com.example.deukgeun.trainer.application.dto.request.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
import com.example.deukgeun.trainer.infrastructure.persistence.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;
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

    @Test
    void givenValidUserId_whenGetByUserId_thenReturnProfileResponse() {
        // Given
        Trainer trainer = Trainer
                .builder()
                .name("test")
                .price(3000)
                .jibunAddress("test")
                .detailAddress("test")
                .roadAddress("test")
                .gender(Gender.M)
                .groupName("")
                .build();

        Profile profile = Profile
                .builder()
                .id(123L)
                .path("test")
                .trainerId(123L)
                .trainer(trainer)
                .build();

        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", response);

        given(profileService.getByTrainerId(anyLong())).willReturn(profile);

        // When
        ResponseEntity<?> responseEntity = profileController.getByUserId(1L);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
    @Test
    void givenValidAuthToken_whenGetByAuthToken_thenReturnProfileResponse() {
        // Given
        String authToken = "validAuthToken";
        Long trainerId = 1L;

        Trainer trainer = Trainer
                .builder()
                .name("test")
                .price(3000)
                .jibunAddress("test")
                .detailAddress("test")
                .roadAddress("test")
                .gender(Gender.M)
                .groupName("")
                .build();

        Profile profile = Profile
                .builder()
                .id(123L)
                .path("test")
                .trainerId(123L)
                .trainer(trainer)
                .build();

        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", response);


        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(trainerService.getTrainerId(authToken)).willReturn(trainerId);
        given(profileService.getByTrainerId(trainerId)).willReturn(profile);

        // When
        ResponseEntity<?> responseEntity = profileController.getByAuthToken(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    void givenValidUpdateRequest_whenUpdate_thenReturnSuccessResponse() throws Exception {
        // Given
        UpdateProfileRequest updateRequest = new UpdateProfileRequest();
        String authToken = "validAuthToken";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = profileController.update(request, updateRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(profileService, times(1)).updateProfile(updateRequest.getProfile(), authToken);
    }

}
