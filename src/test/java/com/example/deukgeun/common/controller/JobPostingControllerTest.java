package com.example.deukgeun.common.controller;

import com.example.deukgeun.commom.request.SaveJobPostingRequest;
import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.member.controller.JobPostingController;
import com.example.deukgeun.member.service.JobPostingService;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.member.entity.Member;
import com.example.deukgeun.member.service.implement.MemberServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
        String email = "testEmail";
        String authToken = "exampleAuthToken";
        Member member = Member
                .builder()
                .id(123L)
                .build();
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("등록 성공했습니다.", null);
        SaveJobPostingRequest saveJobPostingRequest = new SaveJobPostingRequest();

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(tokenService.getUserPk(authToken)).willReturn(email);
        given(memberService.getByEmail(email)).willReturn(member);

        // When
        ResponseEntity<?> responseEntity = jobPostingController.save(request, saveJobPostingRequest, bindingResult);

        // Then
        verify(tokenService, times(1)).getUserPk(anyString());
        verify(memberService, times(1)).getByEmail(email);
        verify(jobPostingService, times(1)).save(saveJobPostingRequest, member.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

}
