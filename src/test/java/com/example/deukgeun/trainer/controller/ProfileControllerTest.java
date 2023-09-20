package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.ProfileController;
import com.example.deukgeun.trainer.application.dto.UpdateProfileRequest;
import com.example.deukgeun.trainer.application.dto.response.ProfileResponse;
import com.example.deukgeun.trainer.application.service.ProfileApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProfileControllerTest {

    @InjectMocks
    private ProfileController profileController;
    @Mock
    private ProfileApplicationService profileApplicationService;
    @Mock
    private AuthTokenApplicationService authTokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenValidTrainerIdAndAuthToken_whenGetProfileById_thenReturnedSuccessResponse() {
        // given
        ProfileResponse profileResponse = mock(ProfileResponse.class);
        given(profileApplicationService.getProfileId(anyLong())).willReturn(profileResponse);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", profileResponse);

        // When
        ResponseEntity<?> responseEntity = profileController.getProfile(1L);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(profileApplicationService, times(1)).getProfileId(anyLong());
    }

    @Test
    public void givenValidEmailAndAuthToken_whenGetProfileByAuthToken_thenReturnedSuccessResponse() {
        // given
        String authToken = "validAuthToken";
        String email = "test@example.com";
        ProfileResponse profileResponse = mock(ProfileResponse.class);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);
        given(profileApplicationService.getProfileByEmail(anyString())).willReturn(profileResponse);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("트레이너 상세보기 성공했습니다.", profileResponse);

        // When
        ResponseEntity<?> responseEntity = profileController.getProfile(request);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(profileApplicationService, times(1)).getProfileByEmail(anyString());
    }

    @Test
    public void givenValidAuthTokenAndValidUpdateRequest_whenUpdateProfile_thenReturnedSuccessResponse() throws Exception {
        // given
        String email = "test@example.com";
        String authToken = "validAuthToken";
        UpdateProfileRequest updateRequest = mock(UpdateProfileRequest.class);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("프로필 정보 수정 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = profileController.updateProfile(request, updateRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(profileApplicationService, times(1)).updateProfile(anyString(), any(MultipartFile.class));
    }

}
