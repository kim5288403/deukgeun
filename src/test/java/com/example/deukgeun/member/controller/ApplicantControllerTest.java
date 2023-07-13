package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.member.response.ApplicantResponse;
import com.example.deukgeun.member.service.ApplicantService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApplicantControllerTest {
    @InjectMocks
    private ApplicantController applicantController;
    @Mock
    private ApplicantService applicantService;

    @Test
    public void givenApplicantService_whenList_thenReturnResponseEntity() {
        // Given
        Long jobPostingId = 123L;
        int currentPage = 0;
        Page<ApplicantResponse.ListResponse> page = mock(Page.class);

        given(applicantService.getByJobPostingId(jobPostingId, currentPage)).willReturn(page);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);

        // When
        ResponseEntity<?> responseEntity = applicantController.list(jobPostingId, currentPage);

        // Then
        verify(applicantService, times(1)).getByJobPostingId(jobPostingId, currentPage);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenApplicantService_whenGetApplicantInfoAjax_thenReturnResponseEntity() {
        // Given
        Long id = 123L;
        ApplicantResponse.ApplicantInfo applicantInfo = mock(ApplicantResponse.ApplicantInfo.class);
        given(applicantService.getById(id)).willReturn(applicantInfo);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", applicantInfo);

        // When
        ResponseEntity<?> responseEntity = applicantController.getApplicantInfo(id);

        // Then
        verify(applicantService, times(1)).getById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
