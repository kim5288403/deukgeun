package com.example.deukgeun.job.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.controller.JobController;
import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.application.service.JobApplicationService;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import com.example.deukgeun.member.domain.aggregate.Member;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JobControllerTest {

    @InjectMocks
    private JobController jobController;
    @Mock
    private JobApplicationService jobApplicationService;
    @Mock
    private AuthTokenApplicationService authTokenApplicationService;
    @Mock
    private MemberApplicationService memberApplicationService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;

    @Test
    public void givenValidIdAndAuthToken_whenCheckJobOwnership_thenReturnSuccessResponse() {
        // Given
        Long id = 1L;
        String authToken = "testAuthToken";
        String email = "testEmail";
        Member member = mock(Member.class);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);
        given(memberApplicationService.findByEmail(anyString())).willReturn(member);
        given(jobApplicationService.existsByIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("체크 성공했습니다.", true);

        // When
        ResponseEntity<?> responseEntity = jobController.checkJobOwnership(request, id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(authTokenApplicationService, times(1)).getUserPk(anyString());
        verify(memberApplicationService, times(1)).findByEmail(anyString());
        verify(jobApplicationService, times(1)).existsByIdAndMemberId(anyLong(), anyLong());
    }

    @Test
    public void givenValidId_whenDetail_thenReturnReturnSuccessResponse() {
        // Given
        long id = 5L;
        JobResponse.Detail jobDetail = mock(JobResponse.Detail.class);

        given(jobApplicationService.getDetail(anyLong())).willReturn(jobDetail);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", jobDetail);

        // When
        ResponseEntity<?> responseEntity = jobController.detail(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(jobApplicationService, times(1)).getDetail(anyLong());
    }

    @Test
    public void givenValidKeywordAndCurrentPage_whenGetListByKeyword_thenReturnSuccessResponse() {
        // Given
        String keyword = "test";
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);
        List<JobResponse.List> list = new ArrayList<>();
        Page<JobResponse.List> page = new PageImpl<>(list, pageable, 0);

        given(jobApplicationService.getListByKeyword(anyString(), anyInt())).willReturn(page);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);

        // When
        ResponseEntity<?> responseEntity = jobController.getListByKeyword(keyword, currentPage);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(jobApplicationService, times(1)).getListByKeyword(anyString(), anyInt());
    }

    @Test
    public void givenValidAuthToken_whenGetList_thenReturnSuccessResponse() {
        // Given
        int currentPage = 0;
        String authToken = "testAuthToken";
        String email = "testEmail";
        Member member = mock(Member.class);

        PageRequest pageable = PageRequest.of(currentPage, 10);
        List<JobResponse.List> list = new ArrayList<>();
        Page<JobResponse.List> page = new PageImpl<>(list, pageable, 0);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);
        given(memberApplicationService.findByEmail(anyString())).willReturn(member);
        given(jobApplicationService.getListByMemberId(anyLong(), anyInt())).willReturn(page);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);

        // When
        ResponseEntity<?> responseEntity = jobController.getList(request, currentPage);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(jobApplicationService, times(1)).getListByMemberId(anyLong(), anyInt());
    }

    @Test
    public void givenValidSaveJobRequest_whenSave_thenReturnSuccessResponse() {
        // Given
        String authToken = "testAuthToken";
        String userPk = "testUserPk";
        Job job = mock(Job.class);
        Member member = mock(Member.class);

        SaveJobRequest saveJobRequest = mock(SaveJobRequest.class);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(userPk);
        given(memberApplicationService.findByEmail(anyString())).willReturn(member);
        given(jobApplicationService.save(any(SaveJobRequest.class), anyLong())).willReturn(job);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("등록 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = jobController.save(request, saveJobRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
        verify(authTokenApplicationService, times(1)).getUserPk(anyString());
        verify(memberApplicationService, times(1)).findByEmail(anyString());
        verify(jobApplicationService, times(1)).save(any(SaveJobRequest.class), anyLong());
    }
}
