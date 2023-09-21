package com.example.deukgeun.applicant.service.domain;

import com.example.deukgeun.applicant.domain.dto.PaymentCancelInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SaveApplicantDTO;
import com.example.deukgeun.applicant.domain.dto.SaveMatchInfoDTO;
import com.example.deukgeun.applicant.domain.dto.SavePaymentInfoDTO;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.model.entity.MatchInfo;
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
    public void givenValidId_whenDeleteMatchInfoById_thenApplicantIsDeleted() {
        // Given
        Applicant applicant = Applicant.create(
                1L,
                1L,
                10000,
                0
        );
        MatchInfo matchInfo = mock(MatchInfo.class);
        applicant.setMatchInfo(matchInfo);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.ofNullable(applicant));

        // When
        applicantDomainService.deleteMatchInfoById(applicant.getId());

        // Then
        assertNull(applicant.getMatchInfo());
        verify(applicantRepository, times(1)).findById(anyLong());
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    public void givenValidId_whenFindById_thenApplicantIsFound() {
        // Given
        Long applicantId = 1L;
        Applicant applicant = mock(Applicant.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));

        // When
        Applicant result = applicantDomainService.findById(applicantId);

        // Then
        assertNotNull(result);
        assertEquals(applicant.getId(), result.getId());
        verify(applicantRepository, times(1)).findById(anyLong());
    }

    @Test
    public void givenInValidId_whenFindById_thenThrowEntityNotFoundException() {
        // Given
        Long applicantId = 1L;
        given(applicantRepository.findById(anyLong())).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> {
            applicantDomainService.findById(applicantId);
        });

        verify(applicantRepository, times(1)).findById(anyLong());
    }

    @Test
    void givenValidJobId_whenFindPageByJobId_thenApplicantPageIsFound() {
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

        given(applicantRepository.findPageByJobId(anyLong(), any(PageRequest.class))).willReturn(page);

        // When
        Page<Applicant> result = applicantDomainService.findPageByJobId(jobId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());

        verify(applicantRepository, times(1)).findPageByJobId(anyLong(), any(PageRequest.class));
    }

    @Test
    public void givenValidJobId_whenIsAnnouncementMatchedByJobId_thenReturnTrue() {
        // Given
        Long jobId = 1L;

        given(applicantRepository.existsByJobIdAndMatchInfoIdNotNull(anyLong())).willReturn(true);

        // When
        boolean isMatched = applicantDomainService.isAnnouncementMatchedByJobId(jobId);

        // Then
        assertTrue(isMatched);
        verify(applicantRepository, times(1)).existsByJobIdAndMatchInfoIdNotNull(anyLong());
    }

    @Test
    public void givenInValidJobId_whenIsAnnouncementMatchedByJobId_thenReturnFalse() {
        // Given
        Long jobId = 2L;

        given(applicantRepository.existsByJobIdAndMatchInfoIdNotNull(anyLong())).willReturn(false);

        // When
        boolean isMatched = applicantDomainService.isAnnouncementMatchedByJobId(jobId);

        // Then
        assertFalse(isMatched);
        verify(applicantRepository, times(1)).existsByJobIdAndMatchInfoIdNotNull(anyLong());
    }

    @Test
    public void givenValidSaveApplicantDTO_whenSave_thenApplicantIsSaved() {
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
    public void givenValidSavePaymentInfoDTO_whenSavePaymentInfo_thenPaymentInfoIsSaved() {
        // Given
        Applicant applicant = Applicant.create(1L, 1L, 1000, 0);
        SavePaymentInfoDTO savePaymentInfoDTO = mock(SavePaymentInfoDTO.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.ofNullable(applicant));

        // When
        applicantDomainService.savePaymentInfo(savePaymentInfoDTO);

        // Then
        assertNotNull(applicant.getPaymentInfo());
        verify(applicantRepository, times(1)).findById(anyLong());
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    public void givenValidSaveMatchInfoDTO_whenSaveMatchInfo_thenMatchInfoIsSaved() {
        // Given
        Applicant applicant = Applicant.create(1L, 1L, 1000, 0);
        SaveMatchInfoDTO saveMatchInfoDTO = mock(SaveMatchInfoDTO.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.ofNullable(applicant));
        given(applicantRepository.save(any(Applicant.class))).willReturn(applicant);

        // When
        Applicant savedApplicant = applicantDomainService.saveMatchInfo(saveMatchInfoDTO);

        // Then
        assertNotNull(savedApplicant.getMatchInfo());
        verify(applicantRepository).save(applicant);
        verify(applicantRepository).findById(anyLong());
    }

    @Test
    public void givenValidPaymentCancelInfoDTO_whenUpdatePaymentCancelInfoById_thenPaymentCancelInfoUpdated() {
        // Given
        Applicant applicant = Applicant.create(1L, 1L, 1000, 0);
        PaymentInfo paymentInfo = PaymentInfo.create(
                "impUid",
                "pgProvider",
                "pgTid",
                "channel",
                10000,
                LocalDateTime.now()
        );
        applicant.setPaymentInfo(paymentInfo);
        PaymentCancelInfoDTO paymentCancelInfoDTO = mock(PaymentCancelInfoDTO.class);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));

        // When
        applicantDomainService.updatePaymentCancelInfoById(paymentCancelInfoDTO);

        // Then
        assertNotNull(applicant.getPaymentInfo().getPaymentCancelInfo());
        verify(applicantRepository, times(1)).findById(anyLong());
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    public void givenValidIdAndIsSelected_whenUpdateIsSelectedById_thenIsSelectedIsUpdated() {
        // Given
        int isSelected = 1;
        Applicant applicant = Applicant.create(1L, 1L, 1000, 0);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));

        // When
        applicantDomainService.updateIsSelectedById(applicant.getId(), isSelected);

        // Then
        assertEquals(isSelected, applicant.getIsSelected());
        assertNotEquals(0, applicant.getIsSelected());
        verify(applicantRepository, times(1)).findById(anyLong());
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }


}
