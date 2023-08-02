package com.example.deukgeun.applicant.service;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.service.implement.ApplicantApplicationServiceImpl;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.job.domain.entity.JobPosting;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApplicantApplicationServiceTest {
    @InjectMocks
    private ApplicantApplicationServiceImpl applicantApplicationService;
    @Mock
    private ApplicantDomainService applicantDomainService;

    @Test
    public void givenApplicantId_whenDeleteMatchInfoById_thenCallApplicantDomainService() {
        // Given
        Long applicantId = 1L;

        // When
        applicantApplicationService.deleteMatchInfoById(applicantId);

        // Then
        verify(applicantDomainService).deleteMatchInfoById(applicantId);
    }

    @Test
    public void givenExistingApplicantId_whenFindById_thenReturnApplicantInfo() {
        // Given
        Long applicantId = 1L;
        Applicant existingApplicant = mock(Applicant.class);
        JobPosting jobPosting = mock(JobPosting.class);
        MemberEntity member = mock(MemberEntity.class);

        given(existingApplicant.getJobPosting()).willReturn(jobPosting);
        given(jobPosting.getMember()).willReturn(member);
        given(jobPosting.getStartDate()).willReturn(LocalDateTime.now());
        given(jobPosting.getEndDate()).willReturn(LocalDateTime.now());
        given(applicantDomainService.findById(applicantId)).willReturn(existingApplicant);

        // When
        ApplicantResponse.ApplicantInfo applicantInfo = applicantApplicationService.findById(applicantId);

        // Then
        assertNotNull(applicantInfo);
        assertEquals(existingApplicant.getId(), applicantInfo.getId());
        assertEquals(existingApplicant.getJobPostingId(), applicantInfo.getJobPostingId());
        assertEquals(existingApplicant.getTrainerId(), applicantInfo.getTrainerId());
    }

    @Test
    public void givenNonExistingApplicantId_whenFindById_thenEntityNotFoundException() {
        // Given
        Long applicantId = 999L;
        Applicant existingApplicant = mock(Applicant.class);
        JobPosting jobPosting = mock(JobPosting.class);
        MemberEntity member = mock(MemberEntity.class);

        given(existingApplicant.getJobPosting()).willReturn(jobPosting);
        given(jobPosting.getMember()).willReturn(member);
        given(jobPosting.getStartDate()).willReturn(LocalDateTime.now());
        given(jobPosting.getEndDate()).willReturn(LocalDateTime.now());
        given(applicantDomainService.findById(applicantId)).willThrow(EntityNotFoundException.class);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            applicantApplicationService.findById(applicantId);
        });
    }

    @Test
    public void givenJobPostingIdAndCurrentPage_whenGetByJobPostingId_thenReturnApplicantListResponsePage() {
        // Given
        Long jobPostingId = 123L;
        int currentPage = 0;
        PageRequest pageRequest = PageRequest.of(currentPage, 10);

        List<Applicant> applicantsList = new ArrayList<>();
        applicantsList.add(new Applicant(1L, jobPostingId, 456L, 1000, 0));
        applicantsList.add(new Applicant(2L, jobPostingId, 789L, 1500, 0));

        Page<Applicant> applicantsPage = new PageImpl<>(applicantsList, pageRequest, applicantsList.size());

        given(applicantDomainService.getByJobPostingId(jobPostingId, pageRequest)).willReturn(applicantsPage);

        // When
        Page<ApplicantResponse.ListResponse> applicantResponsePage = applicantApplicationService.getByJobPostingId(jobPostingId, currentPage);

        // Then
        assertEquals(applicantsList.size(), applicantResponsePage.getContent().size());
        assertEquals(applicantsList.get(0).getId(), applicantResponsePage.getContent().get(0).getId());
        assertEquals(applicantsList.get(0).getTrainerId(), applicantResponsePage.getContent().get(0).getTrainerId());
        assertEquals(applicantsList.get(0).getSupportAmount(), applicantResponsePage.getContent().get(0).getSupportAmount());
    }

    @Test
    public void givenJobPostingIdWithNoAnnouncement_whenIsAnnouncementMatchedByJobPostingId_thenNoExceptionThrown() {
        // Given
        Long jobPostingId = 1L;

        given(applicantDomainService.isAnnouncementMatchedByJobPostingId(jobPostingId)).willReturn(false);

        // When and Then
        assertDoesNotThrow(() -> applicantApplicationService.isAnnouncementMatchedByJobPostingId(jobPostingId));
        verify(applicantDomainService, times(1)).isAnnouncementMatchedByJobPostingId(jobPostingId);
    }

    @Test
    public void givenJobPostingIdWithAnnouncement_whenIsAnnouncementMatchedByJobPostingId_thenEntityExistsExceptionThrown() {
        // Given
        Long jobPostingId = 1L;

        given(applicantDomainService.isAnnouncementMatchedByJobPostingId(jobPostingId)).willReturn(true);

        // When and Then
        assertThrows(EntityExistsException.class, () -> applicantApplicationService.isAnnouncementMatchedByJobPostingId(jobPostingId));
        verify(applicantDomainService, times(1)).isAnnouncementMatchedByJobPostingId(jobPostingId);
    }

    @Test
    public void givenSaveMatchInfoRequest_whenMatching_thenCallApplicantDomainService() {
        // Given
        int PAYMENT_WAITING = 1;
        ReflectionTestUtils.setField(applicantApplicationService, "PAYMENT_WAITING", PAYMENT_WAITING);
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        Applicant applicant = mock(Applicant.class);

        given(applicantDomainService.matching(saveMatchInfoRequest, PAYMENT_WAITING)).willReturn(applicant);

        // When
        Applicant matchedApplicant = applicantApplicationService.matching(saveMatchInfoRequest);

        // Then
        verify(applicantDomainService).matching(saveMatchInfoRequest, PAYMENT_WAITING);
        assertEquals(applicant, matchedApplicant);
    }

    @Test
    public void givenSaveApplicantRequestAndTrainerId_whenSave_thenReturnSavedApplicant() {
        // Given
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest(123L, 1000);
        Long trainerId = 456L;

        Applicant expectedSavedApplicant = new Applicant(1L, saveApplicantRequest.getJobPostingId(), trainerId, saveApplicantRequest.getSupportAmount(), 0);

        given(applicantDomainService.save(saveApplicantRequest, trainerId)).willReturn(expectedSavedApplicant);

        // When
        Applicant savedApplicant = applicantApplicationService.save(saveApplicantRequest, trainerId);

        // Then
        assertEquals(expectedSavedApplicant.getId(), savedApplicant.getId());
        assertEquals(expectedSavedApplicant.getJobPostingId(), savedApplicant.getJobPostingId());
        assertEquals(expectedSavedApplicant.getTrainerId(), savedApplicant.getTrainerId());
        assertEquals(expectedSavedApplicant.getSupportAmount(), savedApplicant.getSupportAmount());
    }

    @Test
    public void givenApplicantIdAndIsSelected_whenUpdateIsSelectedById_thenCallApplicantDomainService() {
        // Given
        Long applicantId = 1L;
        int isSelected = 1;

        // When
        applicantApplicationService.updateIsSelectedById(applicantId, isSelected);

        // Then
        verify(applicantDomainService).updateIsSelectedById(applicantId, isSelected);
    }
}
