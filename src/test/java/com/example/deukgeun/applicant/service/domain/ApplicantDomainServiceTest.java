package com.example.deukgeun.applicant.service.domain;

import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
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

import javax.persistence.EntityNotFoundException;
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
    void givenExistingJobId_whenFindPageByJobId_thenShouldReturnApplicantPage() {
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
        Page<Applicant> result = applicantDomainService.findPageByJobId(jobId, pageable);

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
    public void givenValidSaveApplicantDTO_whenSave_thenShouldSaveApplicant() {
        // Given
        Long jobId = 1L;
        Long trainerId = 2L;
        Applicant applicant = Applicant.create(jobId, trainerId, 1000, 0);
        SaveApplicantDTO saveApplicantDTO = mock(SaveApplicantDTO.class);

        given(applicantRepository.save(any(Applicant.class))).willReturn(applicant);

        // When
        Applicant savedApplicant = applicantDomainService.save(saveApplicantDTO);

        // Then
        assertEquals(jobId, savedApplicant.getJobId());
        assertEquals(trainerId, savedApplicant.getTrainerId());
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    public void givenSavePaymentInfoDTO_whenSavePaymentInfo_thenShouldSavePaymentInfo() {
        // Given
        Applicant applicant = mock(Applicant.class);
        SavePaymentInfoDTO savePaymentInfoDTO = mock(SavePaymentInfoDTO.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.ofNullable(applicant));

        // When
        applicantDomainService.savePaymentInfo(savePaymentInfoDTO);

        // Then
        verify(applicantRepository).findById(anyLong());
        verify(applicantRepository).save(applicant);
    }

    @Test
    public void givenValidSaveMatchInfoRequestAndStatus_whenSaveMatchInfo_thenReturnSavedApplicant() {
        // Given
        Applicant applicant = mock(Applicant.class);
        SaveMatchInfoDTO saveMatchInfoDTO = mock(SaveMatchInfoDTO.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.ofNullable(applicant));
        given(applicantRepository.save(any(Applicant.class))).willReturn(applicant);

        // When
        Applicant savedApplicant = applicantDomainService.saveMatchInfo(saveMatchInfoDTO);

        // Then
        assertEquals(applicant, savedApplicant);

        verify(applicantRepository).save(applicant);
        verify(applicantRepository).findById(anyLong());
    }

    @Test
    public void givenValidPaymentCancelInfoDTO_whenUpdatePaymentCancelInfoById_thenShouldUpdatePaymentCancelInfo() {
        // Given
        Applicant applicant = mock(Applicant.class);
        PaymentInfo paymentInfo = mock(PaymentInfo.class);
        PaymentCancelInfoDTO paymentCancelInfoDTO = mock(PaymentCancelInfoDTO.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));
        given(applicant.getPaymentInfo()).willReturn(paymentInfo);

        // When
        applicantDomainService.updatePaymentCancelInfoById(paymentCancelInfoDTO);

        // Then
        verify(applicantRepository).findById(anyLong());
        verify(applicantRepository).save(applicant);
    }

    @Test
    public void givenValidIdAndIsSelected_whenUpdateIsSelectedById_thenShouldUpdateIsSelect() {
        // Given
        Long jobId = 2L;
        Long trainerId = 3L;
        int isSelected = 1;
        Applicant applicant = Applicant.create(jobId, trainerId, 1000, isSelected);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));

        // When
        applicantDomainService.updateIsSelectedById(applicant.getId(), isSelected);

        // Then
        verify(applicantRepository, times(1)).findById(anyLong());
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }


}
