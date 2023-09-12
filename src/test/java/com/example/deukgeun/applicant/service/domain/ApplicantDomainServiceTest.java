package com.example.deukgeun.applicant.service.domain;

import com.example.deukgeun.applicant.application.dto.request.PaymentInfoRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.application.dto.response.IamPortCancelResponse;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.PaymentInfo;
import com.example.deukgeun.applicant.domain.repository.ApplicantRepository;
import com.example.deukgeun.applicant.domain.service.implement.ApplicantDomainServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ApplicantDomainServiceTest {
    @InjectMocks
    private ApplicantDomainServiceImpl applicantDomainService;
    @Mock
    private ApplicantRepository applicantRepository;

    @Test
    public void givenValidIdAndIamPortCancelResponse_whenUpdatePaymentCancelInfoById_thenShouldUpdatePaymentCancelInfo() {
        // Given
        Long applicantId = 1L;
        IamPortCancelResponse iamPortCancelResponse = new IamPortCancelResponse();
        iamPortCancelResponse.setResponse(new IamPortCancelResponse.Response(
                "test",
                "test",
                "test",
                30000));
        Applicant applicant = mock(Applicant.class);
        PaymentInfo paymentInfo = mock(PaymentInfo.class);

        given(applicantRepository.findById(applicantId)).willReturn(Optional.of(applicant));
        given(applicant.getPaymentInfo()).willReturn(paymentInfo);

        // When
        applicantDomainService.updatePaymentCancelInfoById(applicantId, iamPortCancelResponse);

        // Then
        verify(applicantRepository).findById(applicantId);
        verify(applicantRepository).save(applicant);
    }

    @Test
    public void givenValidId_whenDeleteMatchInfoById_thenShouldDeleteMatchInfo() {
        // Given
        Long applicantId = 1L;
        Applicant applicant = mock(Applicant.class);

        given(applicantRepository.findById(applicantId)).willReturn(Optional.ofNullable(applicant));

        // When
        applicantDomainService.deleteMatchInfoById(applicantId);

        // Then
        verify(applicantRepository, times(1)).findById(applicantId);
        verify(applicantRepository, times(1)).save(applicant);
    }

    @Test
    public void givenExistingId_whenFindById_thenShouldReturnApplicant() {
        // Given
        Long applicantId = 1L;
        Applicant applicant = mock(Applicant.class);

        given(applicantRepository.findById(applicantId)).willReturn(Optional.of(applicant));

        // When
        Applicant result = applicantDomainService.findById(applicantId);

        // Then
        assertNotNull(result);
        assertEquals(applicant.getId(), result.getId());
        verify(applicantRepository, times(1)).findById(applicantId);
    }

    @Test
    public void givenNonExistingId_whenFindById_thenShouldThrowEntityNotFoundException() {
        // Given
        Long applicantId = 1L;
        given(applicantRepository.findById(applicantId)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> {
            applicantDomainService.findById(applicantId);
        });

        verify(applicantRepository, times(1)).findById(applicantId);
    }

    @Test
    void givenExistingJobId_whenGetByJobId_thenShouldReturnApplicantPage() {
        // Given
        Long jobId = 1L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        Applicant applicant1= mock(Applicant.class);
        Applicant applicant2= mock(Applicant.class);

        List<Applicant> list = new ArrayList<>();
        list.add(applicant1);
        list.add(applicant2);
        Page<Applicant> page = new PageImpl<>(list, pageable, list.size());

        given(applicantRepository.findPageByJobId(jobId, pageable)).willReturn(page);

        // When
        Page<Applicant> result = applicantDomainService.getByJobId(jobId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());

        verify(applicantRepository, times(1)).findPageByJobId(jobId, pageable);
    }

    @Test
    public void givenJobId_whenIsAnnouncementMatchedByJobId_thenShouldReturnTrue() {
        // Given
        Long jobId = 1L;

        given(applicantRepository.existsByJobIdAndMatchInfoIdNotNull(jobId)).willReturn(true);

        // When
        boolean isMatched = applicantDomainService.isAnnouncementMatchedByJobId(jobId);

        // Then
        assertTrue(isMatched);
        verify(applicantRepository).existsByJobIdAndMatchInfoIdNotNull(jobId);
    }

    @Test
    public void givenJobId_whenIsAnnouncementMatchedByJobId_thenShouldReturnFalse() {
        // Given
        Long jobId = 2L;

        given(applicantRepository.existsByJobIdAndMatchInfoIdNotNull(jobId)).willReturn(false);

        // When
        boolean isMatched = applicantDomainService.isAnnouncementMatchedByJobId(jobId);

        // Then
        assertFalse(isMatched);
        verify(applicantRepository).existsByJobIdAndMatchInfoIdNotNull(jobId);
    }

    @Test
    public void givenValidSaveApplicantRequest_whenSave_thenShouldSaveApplicant() {
        // Given
        Long jobId = 1L;
        Long trainerId = 2L;
        Long ApplicantId = 3L;
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest(jobId, 1000);
        Applicant applicant = new Applicant(ApplicantId, jobId, trainerId, 1000, 0);

        given(applicantRepository.existsByJobIdAndTrainerId(anyLong(), anyLong())).willReturn(false);
        given(applicantRepository.save(any(Applicant.class))).willReturn(applicant);

        // When
        Applicant savedApplicant = applicantDomainService.save(saveApplicantRequest, trainerId);

        // Then
        assertEquals(ApplicantId, savedApplicant.getId());
        assertEquals(jobId, savedApplicant.getJobId());
        assertEquals(trainerId, savedApplicant.getTrainerId());
        verify(applicantRepository, times(1)).existsByJobIdAndTrainerId(saveApplicantRequest.getJobId(), trainerId);
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    public void givenPaymentInfoRequestAndPaidAt_whenSavePaymentInfo_thenShouldSavePaymentInfo() {
        // Given
        LocalDateTime paidAt = LocalDateTime.now();
        PaymentInfoRequest paymentInfoRequest = mock(PaymentInfoRequest.class);
        Applicant applicant = mock(Applicant.class);
        given(applicantRepository.findById(anyLong())).willReturn(Optional.ofNullable(applicant));

        // When
        applicantDomainService.savePaymentInfo(paymentInfoRequest, paidAt);

        // Then
        verify(applicantRepository).findById(anyLong());
        verify(applicantRepository).save(applicant);
    }

    @Test
    public void givenValidSaveMatchInfoRequestAndStatus_whenSaveMatchInfo_thenReturnSavedApplicant() {
        // Given
        int status = 1;
        SaveMatchInfoRequest saveMatchInfoRequest = mock(SaveMatchInfoRequest.class);
        Applicant applicant = mock(Applicant.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.ofNullable(applicant));
        given(applicantRepository.save(any(Applicant.class))).willReturn(applicant);

        // When
        Applicant savedApplicant = applicantDomainService.saveMatchInfo(saveMatchInfoRequest, status);

        // Then
        assertEquals(applicant, savedApplicant);

        verify(applicantRepository).save(applicant);
        verify(applicantRepository).findById(anyLong());
    }

    @Test
    public void givenValidIdAndIsSelected_whenUpdateIsSelectedById_thenShouldUpdateIsselect() {
        // Given
        Long applicantId = 1L;
        Long jobId = 2L;
        Long trainerId = 3L;
        int isSelected = 1;
        Applicant applicant = new Applicant(applicantId, jobId, trainerId, 1000, isSelected);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));

        // When
        applicantDomainService.updateIsSelectedById(applicantId, isSelected);

        // Then
        verify(applicantRepository, times(1)).findById(applicantId);
        verify(applicantRepository, times(1)).save(applicant);
    }


}
