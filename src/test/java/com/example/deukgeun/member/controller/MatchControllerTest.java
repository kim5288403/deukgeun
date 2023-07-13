package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.entity.MatchInfo;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import com.example.deukgeun.member.service.ApplicantService;
import com.example.deukgeun.member.service.JobPostingService;
import com.example.deukgeun.member.service.MatchService;
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
    private MatchService matchService;
    @Mock
    private JobPostingService jobPostingService;
    @Mock
    private ApplicantService applicantService;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenMatchInfoService_whenSelect_thenReturnResponseEntity() {
        // Given
        MatchInfo matchInfo = mock(MatchInfo.class);
        JobPosting jobPosting = mock(JobPosting.class);
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("매칭 성공했습니다.", null);

        given(matchService.save(saveMatchInfoRequest)).willReturn(matchInfo);
        given(jobPostingService.updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId())).willReturn(jobPosting);

        // When
        ResponseEntity<?> responseEntity = matchController.select(saveMatchInfoRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(matchService, times(1)).save(saveMatchInfoRequest);
        verify(jobPostingService, times(1)).updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId());
    }

    @Test
    public void givenMatchInfoService_whenCancel_thenReturnResponseEntity() {
        // Given
        Long applicantId = 123L;
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("취소 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = matchController.cancel(applicantId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(matchService, times(1)).deleteByApplicantId(applicantId);
        verify(applicantService, times(1)).updateIsSelectedByApplicantId(applicantId, 0);
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

        verify(matchService, times(1)).isAnnouncementMatchedByJobPostingId(jobPostingId);
    }
}
