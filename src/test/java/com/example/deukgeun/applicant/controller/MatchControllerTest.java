package com.example.deukgeun.applicant.controller;

import com.example.deukgeun.applicant.application.controller.MatchController;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.jobPosting.application.service.JobPostingApplicationService;
import com.example.deukgeun.jobPosting.domain.model.aggregate.JobPosting;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
    private JobPostingApplicationService jobPostingApplicationService;
    @Mock
    private ApplicantApplicationService applicantApplicationService;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenMatchInfoService_whenMatching_thenReturnResponseEntity() {
        // Given
        Applicant applicant = mock(Applicant.class);
        JobPosting jobPosting = mock(JobPosting.class);
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("매칭 성공했습니다.", null);

        given(applicantApplicationService.matching(saveMatchInfoRequest)).willReturn(applicant);

        // When
        ResponseEntity<?> responseEntity = matchController.matching(saveMatchInfoRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(applicantApplicationService, times(1)).matching(saveMatchInfoRequest);
        verify(jobPostingApplicationService, times(1)).updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId());
    }

    @Test
    public void givenMatchInfoService_whenIsAnnouncementMatched_thenReturnResponseEntity() {
        // Given
        Long jobPostingId = 123L;
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("검사 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = matchController.isAnnouncementMatched(jobPostingId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(applicantApplicationService, times(1)).isAnnouncementMatchedByJobPostingId(jobPostingId);
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
