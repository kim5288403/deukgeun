package com.example.deukgeun.applicant.controller;

import com.example.deukgeun.applicant.application.controller.ApplicantController;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.service.ApplicantApplicationService;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.job.application.service.JobApplicationService;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.member.application.service.MemberApplicationService;
import com.example.deukgeun.member.domain.aggregate.Member;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApplicantControllerTest {
    @InjectMocks
    private ApplicantController applicantController;
    @Mock
    private ApplicantApplicationService applicantApplicationService;
    @Mock
    private AuthTokenApplicationService authTokenApplicationService;
    @Mock
    private TrainerApplicationService trainerApplicationService;
    @Mock
    private MemberApplicationService memberApplicationService;
    @Mock
    private JobApplicationService jobApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;

    @Test
    public void givenValidJobId_whenList_thenReturnReturnOkResponse() {
        // Given
        Long jobId = 1L;
        int currentPage = 1;
        List<ApplicantResponse.List> list = new ArrayList<>();
        ApplicantResponse.List applicantResponse = mock(ApplicantResponse.List.class);
        list.add(applicantResponse);
        Pageable pageable = mock(Pageable.class);
        Page<ApplicantResponse.List> page = new PageImpl<>(list, pageable, list.size());

        given(applicantApplicationService.getListByJobId(jobId, currentPage)).willReturn(page);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", page);

        // When
        ResponseEntity<?> response = applicantController.list(jobId, currentPage);

        // Then
        verify(applicantApplicationService, times(1)).getListByJobId(jobId, currentPage);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }

    @Test
    public void givenValidSaveApplicantRequest_whenSave_thenReturnOkResponse() {
        // Given
        String authToken = "your_mocked_auth_token";
        String email = "mocked_user_email";
        Trainer trainer = mock(Trainer.class);
        SaveApplicantRequest saveApplicantRequest = mock(SaveApplicantRequest.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("지원 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);

        // When
        ResponseEntity<?> responseEntity = applicantController.save(request, saveApplicantRequest, bindingResult);

        // Then
        verify(authTokenApplicationService).resolveAuthToken(request);
        verify(authTokenApplicationService).getUserPk(authToken);
        verify(trainerApplicationService).findByEmail(email);
        verify(applicantApplicationService).save(eq(saveApplicantRequest), anyLong());
        assertEquals(expectedResponse, responseEntity);
    }

    @Test
    public void givenValidId_whenGetApplicantInfo_thenReturnOkResponse() {
        // Given
        Long id = 1L;
        Applicant applicant = mock(Applicant.class);
        Member member = mock(Member.class);
        Job job = mock(Job.class);

        given(applicantApplicationService.findById(anyLong())).willReturn(applicant);
        given(jobApplicationService.findById(anyLong())).willReturn(job);
        given(memberApplicationService.findById(anyLong())).willReturn(member);

        given(job.getAddress()).willReturn(mock(Address.class));
        given(job.getStartDate()).willReturn(LocalDateTime.now());
        given(job.getEndDate()).willReturn(LocalDateTime.now());

        ApplicantResponse.Info result = mock(ApplicantResponse.Info.class);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공했습니다.", result);

        // When
        ResponseEntity<?> response = applicantController.getApplicantInfo(id);

        // Then
        verify(applicantApplicationService, times(1)).findById(id);
        verify(jobApplicationService, times(1)).findById(anyLong());
        verify(memberApplicationService, times(1)).findById(anyLong());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getBody(), response.getBody());
    }
}
