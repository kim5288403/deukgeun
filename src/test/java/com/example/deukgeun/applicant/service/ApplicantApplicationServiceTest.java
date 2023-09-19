package com.example.deukgeun.applicant.service;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.ApplicantResponse;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.application.service.implement.ApplicantApplicationServiceImpl;
import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.service.ApplicantDomainService;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.ApplicantMapper;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.MatchMapper;
import com.example.deukgeun.applicant.infrastructure.persistence.mapper.PaymentMapper;
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
    @Mock
    private ApplicantMapper applicantMapper;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private MatchMapper matchMapper;

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
    public void givenJobIdAndCurrentPage_whenGetListByJobId_thenReturnApplicantListResponsePage() {
        // Given
        Long jobId = 3L;
        Long trainerId = 4L;
        int supportAmount = 10000;
        int currentPage = 0;
        PageRequest pageRequest = PageRequest.of(currentPage, 10);
        List<Applicant> applicantsList = new ArrayList<>();
        applicantsList.add(Applicant.create(jobId, trainerId, supportAmount, 0));
        applicantsList.add(Applicant.create(jobId, trainerId, supportAmount, 0));
        Page<Applicant> applicantsPage = new PageImpl<>(applicantsList, pageRequest, applicantsList.size());
        ApplicantResponse.List applicantResponseList = new ApplicantResponse.List();
        applicantResponseList.setId(applicantsList.get(0).getId());
        applicantResponseList.setJobId(jobId);
        applicantResponseList.setTrainerId(trainerId);
        applicantResponseList.setSupportAmount(supportAmount);
        applicantResponseList.setIsSelected(0);

        given(applicantDomainService.findPageByJobId(jobId, pageRequest)).willReturn(applicantsPage);
        given(applicantMapper.toApplicantResponseList(any(Applicant.class))).willReturn(applicantResponseList);

        // When
        Page<ApplicantResponse.List> applicantResponsePage = applicantApplicationService.getListByJobId(jobId, currentPage);

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
        SaveMatchInfoDTO saveMatchInfoDTO = mock(SaveMatchInfoDTO.class);
        Applicant applicant = mock(Applicant.class);

        given(matchMapper.toSaveMatchInfoDto(anyInt(), any(SaveMatchInfoRequest.class))).willReturn(saveMatchInfoDTO);
        given(applicantDomainService.saveMatchInfo(any(SaveMatchInfoDTO.class))).willReturn(applicant);

        // When
        Applicant matchedApplicant = applicantApplicationService.saveMatchInfo(saveMatchInfoRequest, PAYMENT_WAITING);

        // Then
        verify(applicantDomainService).saveMatchInfo(any(SaveMatchInfoDTO.class));
        assertEquals(applicant, matchedApplicant);
    }

    @Test
    public void givenPaymentInfoRequest_whenPayment_thenShouldCreatePaymentInfo() {
        // Given
        PaymentInfoRequest paymentInfoRequest = new PaymentInfoRequest();
        paymentInfoRequest.setPaidAt("2022-12-15T13:00:00.123+09:00");
        SavePaymentInfoDTO savePaymentInfoDTO = mock(SavePaymentInfoDTO.class);
        given(paymentMapper.toSavePaymentInfoDto(any(LocalDateTime.class), any(PaymentInfoRequest.class))).willReturn(savePaymentInfoDTO);

        // When
        applicantApplicationService.savePaymentInfo(paymentInfoRequest);

        // Then
        verify(applicantDomainService).savePaymentInfo(any(SavePaymentInfoDTO.class));
    }

    @Test
    public void givenSaveApplicantRequestAndTrainerId_whenSave_thenReturnSavedApplicant() {
        // Given
        Long trainerId = 456L;
        SaveApplicantDTO saveApplicantDTO = mock(SaveApplicantDTO.class);
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest(123L, 1000);
        Applicant expectedSavedApplicant = Applicant.create(saveApplicantRequest.getJobId(), trainerId, saveApplicantRequest.getSupportAmount(), 0);

        given(applicantMapper.toSaveApplicantDto(anyLong(), any(SaveApplicantRequest.class))).willReturn(saveApplicantDTO);
        given(applicantDomainService.save(any(SaveApplicantDTO.class))).willReturn(expectedSavedApplicant);

        // When
        Applicant savedApplicant = applicantApplicationService.save(saveApplicantRequest, trainerId);

        // Then
        assertEquals(expectedSavedApplicant.getId(), savedApplicant.getId());
        assertEquals(expectedSavedApplicant.getJobId(), savedApplicant.getJobId());
        assertEquals(expectedSavedApplicant.getTrainerId(), savedApplicant.getTrainerId());
        assertEquals(expectedSavedApplicant.getSupportAmount(), savedApplicant.getSupportAmount());
    }

    @Test
    public void givenIdAndIamPortCancelResponse_whenUpdatePaymentCancelInfoById_thenShouldUpdatePaymentInfo() {
        // Given
        Long applicantId = 1L;
        IamPortCancelResponse iamPortCancelResponse = mock(IamPortCancelResponse.class);
        Applicant applicant = mock(Applicant.class);
        PaymentCancelInfoDTO paymentCancelInfoDTO = mock(PaymentCancelInfoDTO.class);

        given(applicantDomainService.findById(applicantId)).willReturn(applicant);
        given(paymentMapper.toPaymentCancelInfoDto(anyLong(), any(IamPortCancelResponse.class))).willReturn(paymentCancelInfoDTO);

        // When
        applicantApplicationService.updatePaymentCancelInfoById(applicantId, iamPortCancelResponse);

        // Then
        verify(applicantDomainService).updatePaymentCancelInfoById(any(PaymentCancelInfoDTO.class));
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
