package com.example.deukgeun.job.controller;

import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.enums.Gender;
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
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Mock
    private MemberApplicationService memberApplicationService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;

    @Test
    public void givenJobService_whenCheckJobOwnership_thenReturnResponseEntity() {
        // Given
        Long id = 1L;
        Long memberId = 1L;
        String authToken = "testAuthToken";
        String email = "testEmail";
        Member member = new Member(
                memberId,
                email,
                "test",
                "test",
                23,
                Gender.M
        );

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("체크 성공했습니다.", true);
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(memberApplicationService.findByEmail(email)).willReturn(member);
        given(jobApplicationService.existsByIdAndMemberId(id, member.getId())).willReturn(true);

        // When
        ResponseEntity<?> responseEntity = jobController.checkJobOwnership(request, id);

        // Then
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(memberApplicationService, times(1)).findByEmail(email);
        verify(jobApplicationService, times(1)).existsByIdAndMemberId(id, memberId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenJobService_whenDetail_thenReturnResponseEntity() {
        // Given
        long id = 5L;
        JobResponse.Detail jobDetail = mock(JobResponse.Detail.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", jobDetail);
        given(jobApplicationService.getDetail(id)).willReturn(jobDetail);

        // When
        ResponseEntity<?> responseEntity = jobController.detail(id);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(jobApplicationService, times(1)).getDetail(anyLong());
    }

    @Test
    public void givenJobService_whenGetListByKeyword_thenReturnResponseEntity() {
        // Given
        String keyword = "test";
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);
        List<JobResponse.List> list = new ArrayList<>();
        Page<JobResponse.List> page = new PageImpl<>(list, pageable, list.size());

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);
        given(jobApplicationService.getListByKeyword(keyword, currentPage)).willReturn(page);

        // When
        ResponseEntity<?> responseEntity = jobController.getListByKeyword(keyword, currentPage);

        // Then
        verify(jobApplicationService, times(1)).getListByKeyword(keyword, currentPage);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenJobService_whenGetListByMemberId_thenReturnResponseEntity() {
        // Given
        Long memberId = 123L;
        int currentPage = 0;
        String authToken = "testAuthToken";
        String email = "testEmail";
        Member member = mock(Member.class);

        PageRequest pageable = PageRequest.of(currentPage, 10);
        List<JobResponse.List> list = new ArrayList<>();
        Page<JobResponse.List> page = new PageImpl<>(list, pageable, list.size());

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(memberApplicationService.findByEmail(email)).willReturn(member);
        given(jobApplicationService.getListByMemberId(memberId, currentPage)).willReturn(page);

        // When
        ResponseEntity<?> responseEntity = jobController.getListByMemberId(request, currentPage);

        // Then
        verify(jobApplicationService, times(1)).getListByMemberId(memberId, currentPage);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenJobService_whenSave_thenReturnResponseEntity() {
        // Given
        String authToken = "testAuthToken";
        String userPk = "testUserPk";
        Job job = mock(Job.class);
        Member member = new Member(
                123L,
                userPk,
                "test",
                "test",
                23,
                Gender.M
        );
        SaveJobRequest saveJobRequest = mock(SaveJobRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("등록 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(userPk);
        given(memberApplicationService.findByEmail(userPk)).willReturn(member);
        given(jobApplicationService.save(saveJobRequest, member.getId())).willReturn(job);

        // When
        ResponseEntity<?> responseEntity = jobController.save(request, saveJobRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(memberApplicationService, times(1)).findByEmail(userPk);
        verify(jobApplicationService, times(1)).save(saveJobRequest, member.getId());
    }
}
