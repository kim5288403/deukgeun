package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.Profile;
import com.example.deukgeun.trainer.application.controller.ProfileController;
import com.example.deukgeun.trainer.application.dto.request.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.implement.TrainerApplicationServiceImpl;
import com.example.deukgeun.trainer.application.service.implement.ProfileServiceImpl;
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
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;
    @Mock
    private ProfileServiceImpl profileService;
    @Mock
    private TrainerApplicationServiceImpl trainerService;
    @Mock
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    @Test
    void givenValidUserId_whenGetByUserId_thenReturnProfileResponse() {
        // Given
        TrainerEntity trainer = TrainerEntity
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
        String email = "test";
        Long trainerId = 1L;
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                "test",
                "test",
                GroupStatus.N,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                Gender.M,
                3000,
                "test"
        );
        TrainerEntity trainerEntity = mock(TrainerEntity.class);
        Profile profile = Profile
                .builder()
                .id(123L)
                .path("test")
                .trainerId(123L)
                .trainer(trainerEntity)
                .build();

        ProfileResponse.ProfileAndUserResponse response = new ProfileResponse.ProfileAndUserResponse(profile);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", response);


        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerService.findByEmail(email)).willReturn(trainer);
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
        String email = "test";
        Long trainerId = 1L;
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                email,
                "test",
                GroupStatus.N,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                Gender.M,
                3000,
                "test"
        );
        Profile profile = Profile
                .builder()
                .id(123L)
                .build();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerService.findByEmail(email)).willReturn(trainer);
        given(profileService.getByTrainerId(trainerId)).willReturn(profile);

        // When
        ResponseEntity<?> responseEntity = profileController.update(request, updateRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(profileService, times(1)).updateProfile(updateRequest.getProfile(), 123L);
    }

}
