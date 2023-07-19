package com.example.deukgeun.job.controller;

import com.example.deukgeun.auth.application.dto.response.JobPostingResponse;
import com.example.deukgeun.auth.application.dto.response.RestResponse;
import com.example.deukgeun.auth.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.controller.JobPostingController;
import com.example.deukgeun.job.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.job.domain.entity.JobPosting;
import com.example.deukgeun.job.domain.service.JobPostingService;
import com.example.deukgeun.member.domain.entity.Member;
import com.example.deukgeun.member.application.service.implement.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JobPostingControllerTest {

    @InjectMocks
    private JobPostingController jobPostingController;
    @Mock
    private JobPostingService jobPostingService;
    @Mock
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;

    @Test
    public void givenJobPostingService_whenList_thenReturnResponseEntity() {
        // Given
        String keyword = "test";
        int currentPage = 0;
        Page<JobPostingResponse.ListResponse> page = mock(Page.class);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);
        given(jobPostingService.getListByKeyword(keyword, currentPage)).willReturn(page);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.list(keyword, currentPage);

        // Then
        verify(jobPostingService, times(1)).getListByKeyword(keyword, currentPage);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenJobPostingService_whenDetail_thenReturnResponseEntity() {
        // Given
        long id = 5L;
        JobPosting jobPosting = mock(JobPosting.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", jobPosting);
        given(jobPostingService.getById(id)).willReturn(jobPosting);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.detail(id);

        // Then
        verify(jobPostingService, times(1)).getById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenJobPostingService_whenCheckJobPostingOwnership_thenReturnResponseEntity() {
        // Given
        Long id = 1L;
        Long memberId = 1L;
        String authToken = "testAuthToken";
        String email = "testEmail";
        Member member = Member
                .builder()
                .id(memberId)
                .build();

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("체크 성공했습니다.", true);
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(memberService.getByEmail(email)).willReturn(member);
        given(jobPostingService.existsByIdAndMemberId(id, member.getId())).willReturn(true);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.checkJobPostingOwnership(request, id);

        // Then
        verify(jobPostingService, times(1)).existsByIdAndMemberId(id, memberId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenJobPostingService_whenSave_thenReturnResponseEntity() {
        // Given
        String authToken = "testAuthToken";
        String userPk = "testUserPk";
        JobPosting jobPosting = mock(JobPosting.class);
        Member member = Member
                .builder()
                .id(123L)
                .build();
        SaveJobPostingRequest saveJobPostingRequest = mock(SaveJobPostingRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("등록 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(userPk);
        given(memberService.getByEmail(userPk)).willReturn(member);
        given(jobPostingService.save(saveJobPostingRequest, member.getId())).willReturn(jobPosting);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.save(request, saveJobPostingRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(memberService, times(1)).getByEmail(userPk);
        verify(jobPostingService, times(1)).save(saveJobPostingRequest, member.getId());
    }

    @Test
    public void givenJobPostingService_whenListByMemberId_thenReturnResponseEntity() {
        // Given
        Long memberId = 123L;
        int currentPage = 0;
        String authToken = "testAuthToken";
        String email = "testEmail";
        Member member = Member
                .builder()
                .id(memberId)
                .build();
        Page<JobPostingResponse.ListResponse> page = mock(Page.class);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(memberService.getByEmail(email)).willReturn(member);
        given(jobPostingService.getByMemberId(memberId, currentPage)).willReturn(page);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.listByMemberId(request, currentPage);

        // Then
        verify(jobPostingService, times(1)).getByMemberId(memberId, currentPage);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
