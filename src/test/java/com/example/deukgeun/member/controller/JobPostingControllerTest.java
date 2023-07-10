package com.example.deukgeun.member.controller;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.entity.Member;
import com.example.deukgeun.main.response.JobPostingResponse;
import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.main.service.implement.TokenServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.member.request.SaveJobPostingRequest;
import com.example.deukgeun.member.service.JobPostingService;
import com.example.deukgeun.member.service.implement.MemberServiceImpl;
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
    private TokenServiceImpl tokenService;
    @Mock
    private MemberServiceImpl memberService;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletRequest request;

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

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(tokenService.getUserPk(authToken)).willReturn(userPk);
        given(memberService.getByEmail(userPk)).willReturn(member);
        given(jobPostingService.save(saveJobPostingRequest, member.getId())).willReturn(jobPosting);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.save(request, saveJobPostingRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());

        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(tokenService, times(1)).getUserPk(authToken);
        verify(memberService, times(1)).getByEmail(userPk);
        verify(jobPostingService, times(1)).save(saveJobPostingRequest, member.getId());
    }

    @Test
    public void givenJobPostingService_whenList_thenReturnResponseEntity() {
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
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(tokenService.getUserPk(authToken)).willReturn(email);
        given(memberService.getByEmail(email)).willReturn(member);
        given(jobPostingService.getByMemberId(memberId, currentPage)).willReturn(page);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.list(request, currentPage);

        // Then
        verify(jobPostingService, times(1)).getByMemberId(memberId, currentPage);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
