package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.ProfileController;
//import com.example.deukgeun.trainer.application.dto.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.dto.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Group;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;
    @Mock
    private TrainerApplicationService trainerApplicationService;
    @Mock
    private AuthTokenApplicationService authTokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenValidTrainerId_whenGetProfileByTrainerId_thenProfileReturnedSuccessfully() {
        // given
        Long trainerId = 1L;
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                "test",
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test",
                mock(List.class),
                new Profile(123L, "test"),
                mock(Post.class)
        );

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", trainer.getProfile());

        given(trainerApplicationService.findById(trainerId)).willReturn(trainer);

        // When
        ResponseEntity<?> responseEntity = profileController.getProfileByTrainerId(1L);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenValidAuthToken_whenGetProfileByAuthToken_thenProfileReturnedSuccessfully() {
        // given
        String authToken = "validAuthToken";
        String email = "test@example.com";
        Trainer trainer = new Trainer(
                1L,
                "test",
                email,
                "test",
                new Group(
                        GroupStatus.Y,
                        "test"
                ),
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test",
                mock(List.class),
                new Profile(123L, "test"),
                mock(Post.class)
        );
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", trainer.getProfile());

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);

        // When
        ResponseEntity<?> responseEntity = profileController.getProfileByAuthToken(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenValidAuthTokenAndValidUpdateRequest_whenUpdateProfile_thenProfileUpdatedSuccessfully() throws Exception {
        // given
        UpdateProfileRequest updateRequest = mock(UpdateProfileRequest.class);
        String authToken = "validAuthToken";
        String email = "test@example.com";

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);


        ResponseEntity<?> responseEntity = profileController.updateProfile(request, updateRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(trainerApplicationService, times(1)).updateProfile(email, updateRequest.getProfile());
    }

}
