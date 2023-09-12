package com.example.deukgeun.applicant.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.service.implement.ApplicantApplicationServiceImpl;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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
    public void givenApplicantIdAndIamPortCancelResponse_whenCancel_thenShouldUpdatePaymentInfo() {
        // Given
        Long applicantId = 1L;
        IamPortCancelResponse iamPortCancelResponse = mock(IamPortCancelResponse.class);
        Applicant applicant = mock(Applicant.class);
        given(applicantDomainService.findById(applicantId)).willReturn(applicant);

        // When
        applicantApplicationService.updatePaymentCancelInfoById(applicantId, iamPortCancelResponse);

        // Then
        verify(applicantDomainService).updatePaymentCancelInfoById(applicantId, iamPortCancelResponse);
    }

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

        given(applicantDomainService.findById(applicantId)).willReturn(existingApplicant);

        // When
        Applicant applicantInfo = applicantApplicationService.findById(applicantId);

        // Then
        assertNotNull(applicantInfo);
        assertEquals(existingApplicant.getId(), applicantInfo.getId());
        assertEquals(existingApplicant.getJobId(), applicantInfo.getJobId());
        assertEquals(existingApplicant.getTrainerId(), applicantInfo.getTrainerId());
    }

    @Test
    public void givenNonExistingApplicantId_whenFindById_thenEntityNotFoundException() {
        // Given
        Long applicantId = 999L;

        given(applicantDomainService.findById(applicantId)).willThrow(EntityNotFoundException.class);

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            applicantApplicationService.findById(applicantId);
        });
    }

    @Test
    public void givenJobIdAndCurrentPage_whenGetByJobId_thenReturnApplicantListResponsePage() {
        // Given
        Long jobId = 123L;
        int currentPage = 0;
        PageRequest pageRequest = PageRequest.of(currentPage, 10);

        List<Applicant> applicantsList = new ArrayList<>();
        applicantsList.add(new Applicant(1L, jobId, 456L, 1000, 0));
        applicantsList.add(new Applicant(2L, jobId, 789L, 1500, 0));

        Page<Applicant> applicantsPage = new PageImpl<>(applicantsList, pageRequest, applicantsList.size());

        given(applicantDomainService.getByJobId(jobId, pageRequest)).willReturn(applicantsPage);

        // When
        Page<ApplicantResponse.List> applicantResponsePage = applicantApplicationService.getByJobId(jobId, currentPage);

        // Then
        assertEquals(applicantsList.size(), applicantResponsePage.getContent().size());
        assertEquals(applicantsList.get(0).getId(), applicantResponsePage.getContent().get(0).getId());
        assertEquals(applicantsList.get(0).getTrainerId(), applicantResponsePage.getContent().get(0).getTrainerId());
        assertEquals(applicantsList.get(0).getSupportAmount(), applicantResponsePage.getContent().get(0).getSupportAmount());
    }

    @Test
    public void givenJobIdWithNoAnnouncement_whenIsAnnouncementMatchedByJobId_thenNoExceptionThrown() {
        // Given
        Long jobId = 1L;

        given(applicantDomainService.isAnnouncementMatchedByJobId(jobId)).willReturn(false);

        // When and Then
        assertDoesNotThrow(() -> applicantApplicationService.isAnnouncementMatchedByJobId(jobId));
        verify(applicantDomainService, times(1)).isAnnouncementMatchedByJobId(jobId);
    }

    @Test
    public void givenJobIdWithAnnouncement_whenIsAnnouncementMatchedByJobId_thenEntityExistsExceptionThrown() {
        // Given
        Long jobId = 1L;

        given(applicantDomainService.isAnnouncementMatchedByJobId(jobId)).willReturn(true);

        // When and Then
        assertThrows(EntityExistsException.class, () -> applicantApplicationService.isAnnouncementMatchedByJobId(jobId));
        verify(applicantDomainService, times(1)).isAnnouncementMatchedByJobId(jobId);
    }

    @Test
    public void givenSaveMatchInfoRequest_whenMatching_thenCallApplicantDomainService() {
        // Given
        int PAYMENT_WAITING = 1;
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        Applicant applicant = mock(Applicant.class);

        given(applicantDomainService.saveMatchInfo(saveMatchInfoRequest, PAYMENT_WAITING)).willReturn(applicant);

        // When
        Applicant matchedApplicant = applicantApplicationService.saveMatchInfo(saveMatchInfoRequest, PAYMENT_WAITING);

        // Then
        verify(applicantDomainService).saveMatchInfo(saveMatchInfoRequest, PAYMENT_WAITING);
        assertEquals(applicant, matchedApplicant);
    }

    @Test
    public void givenPaymentInfoRequest_whenPayment_thenShouldCreatePaymentInfo() {
        // Given
        PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
        paymentInfoRequest.setPaidAt("2022-12-15T13:00:00.123+09:00");

        // When
        applicantApplicationService.savePaymentInfo(paymentInfoRequest);

        // Then
        verify(applicantDomainService).savePaymentInfo(any(PaymentInfoRequest.class), any(LocalDateTime.class));
    }

    @Test
    public void givenSaveApplicantRequestAndTrainerId_whenSave_thenReturnSavedApplicant() {
        // Given
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest(123L, 1000);
        Long trainerId = 456L;

        Applicant expectedSavedApplicant = new Applicant(1L, saveApplicantRequest.getJobId(), trainerId, saveApplicantRequest.getSupportAmount(), 0);

        given(applicantDomainService.save(saveApplicantRequest, trainerId)).willReturn(expectedSavedApplicant);

        // When
        Applicant savedApplicant = applicantApplicationService.save(saveApplicantRequest, trainerId);

        // Then
        assertEquals(expectedSavedApplicant.getId(), savedApplicant.getId());
        assertEquals(expectedSavedApplicant.getJobId(), savedApplicant.getJobId());
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
