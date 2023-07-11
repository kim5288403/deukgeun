package com.example.deukgeun.main.controller;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.entity.Member;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.main.response.JobPostingResponse;
import com.example.deukgeun.main.response.RestResponse;
import com.example.deukgeun.main.service.JobPostingService;
import com.example.deukgeun.main.service.implement.TokenServiceImpl;
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
    public void givenJobPostingService_whenList_thenReturnResponseEntity() {
        // Given
        String keyword = "test";
        int currentPage = 0;
        Page<JobPostingResponse.ListResponse> page = mock(Page.class);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);
        given(jobPostingService.getList(keyword, currentPage)).willReturn(page);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.list(keyword, currentPage);

        // Then
        verify(jobPostingService, times(1)).getList(keyword, currentPage);
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
        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(tokenService.getUserPk(authToken)).willReturn(email);
        given(memberService.getByEmail(email)).willReturn(member);
        given(jobPostingService.existsByIdAndMemberId(id, member.getId())).willReturn(true);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.checkJobPostingOwnership(request, id);

        // Then
        verify(jobPostingService, times(1)).existsByIdAndMemberId(id, memberId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

}
