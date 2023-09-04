package com.example.deukgeun.applicant.controller;

import com.example.deukgeun.applicant.application.controller.MatchController;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.service.JobApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MatchControllerTest {
    @InjectMocks
    private MatchController matchController;
    @Mock
    private JobApplicationService jobApplicationService;
    @Mock
    private ApplicantApplicationService applicantApplicationService;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenMatchInfoService_whenMatching_thenReturnResponseEntity() {
        // Given
        Applicant applicant = mock(Applicant.class);
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("매칭 성공했습니다.", null);
        ReflectionTestUtils.setField(matchController, "PAYMENT_WAITING", 1);
        ReflectionTestUtils.setField(matchController, "APPLICANT_SELECT", 1);
        ReflectionTestUtils.setField(matchController, "JOB_INACTIVE", 2);

        given(applicantApplicationService.matching(saveMatchInfoRequest, 1)).willReturn(applicant);

        // When
        ResponseEntity<?> responseEntity = matchController.matching(saveMatchInfoRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(applicantApplicationService, times(1)).matching(saveMatchInfoRequest, 1);
        verify(applicantApplicationService, times(1)).updateIsSelectedById(saveMatchInfoRequest.getApplicantId(), 1);
        verify(jobApplicationService, times(1)).updateIsActiveByJobId(2, saveMatchInfoRequest.getJobId());
    }

    @Test
    public void givenMatchInfoService_whenIsAnnouncementMatched_thenReturnResponseEntity() {
        // Given
        Long jobId = 123L;
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("검사 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = matchController.isAnnouncementMatched(jobId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(applicantApplicationService, times(1)).isAnnouncementMatchedByJobId(jobId);
    }

    @Test
    public void givenMatchInfoService_whenCancel_thenReturnResponseEntity() {
        // Given
        Long id = 123L;
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("취소 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = matchController.cancel(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(applicantApplicationService, times(1)).deleteMatchInfoById(id);
        verify(applicantApplicationService, times(1)).updateIsSelectedById(id, 0);
    }
}
