package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.entity.MatchInfo;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.member.request.SaveMatchInfoRequest;
import com.example.deukgeun.member.service.JobPostingService;
import com.example.deukgeun.member.service.MatchInfoService;
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
public class MatchInfoControllerTest {
    @InjectMocks
    private MatchInfoController matchInfoController;
    @Mock
    private MatchInfoService matchInfoService;
    @Mock
    private JobPostingService jobPostingService;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenMatchInfoService_whenSave_thenReturnResponseEntity() {
        // Given
        MatchInfo matchInfo = mock(MatchInfo.class);
        JobPosting jobPosting = mock(JobPosting.class);
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("매칭 성공했습니다.", null);

        given(matchInfoService.save(saveMatchInfoRequest)).willReturn(matchInfo);
        given(jobPostingService.updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId())).willReturn(jobPosting);

        // When
        ResponseEntity<?> responseEntity = matchInfoController.save(saveMatchInfoRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(matchInfoService, times(1)).save(saveMatchInfoRequest);
        verify(jobPostingService, times(1)).updateIsActiveByJobPostingId(2, saveMatchInfoRequest.getJobPostingId());
    }

}
