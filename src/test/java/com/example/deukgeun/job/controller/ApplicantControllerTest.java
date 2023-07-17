package com.example.deukgeun.job.controller;

import com.example.deukgeun.auth.application.dto.response.RestResponse;
import com.example.deukgeun.auth.application.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.controller.ApplicantController;
import com.example.deukgeun.job.domain.service.ApplicantService;
import com.example.deukgeun.job.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.trainer.infrastructure.persistence.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicantControllerTest {
    @InjectMocks
    private ApplicantController applicantController;
    @Mock
    private ApplicantService applicantService;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private TrainerServiceImpl trainerService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;

    @Test
    public void givenApplicantService_whenSave_thenReturnResponseEntity() {
        // Given
        Long trainerId = 123L;
        String authToken = "testAuthToken";
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("지원 성공했습니다.", null);

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(trainerService.getTrainerId(authToken)).willReturn(trainerId);

        // When
        ResponseEntity<?> responseEntity = applicantController.save(request, saveApplicantRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(applicantService, times(1)).save(saveApplicantRequest, trainerId);
    }

}
