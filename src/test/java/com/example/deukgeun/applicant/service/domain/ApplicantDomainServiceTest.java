package com.example.deukgeun.applicant.service.domain;

import com.example.deukgeun.applicant.application.dto.request.SaveApplicantRequest;
import com.example.deukgeun.applicant.application.dto.request.SaveMatchInfoRequest;
import com.example.deukgeun.applicant.domain.model.aggregate.Applicant;
import com.example.deukgeun.applicant.domain.repository.ApplicantRepository;
import com.example.deukgeun.applicant.domain.service.implement.ApplicantDomainServiceImpl;
import com.example.deukgeun.job.domain.entity.JobPosting;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
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
    public void givenApplicantId_whenDeleteMatchInfoById_thenDeleteMatchInfoAndSave() {
        // Given
        Long applicantId = 1L;
        Applicant applicant = mock(Applicant.class);

        given(applicantRepository.findById(applicantId)).willReturn(Optional.ofNullable(applicant));

        // When
        applicantDomainService.deleteMatchInfoById(applicantId);

        // Then
        verify(applicantRepository, times(1)).findById(applicantId);
        verify(applicant, times(1)).deleteMatchInfo();
        verify(applicantRepository, times(1)).save(applicant);
    }

    @Test
    public void givenExistingApplicantId_whenFindById_thenShouldReturnApplicantInfo() {
        // Given
        MemberEntity member = mock(MemberEntity.class);

        JobPosting jobPosting = mock(JobPosting.class);

        Long applicantId = 12345L;
        Applicant applicant = mock(Applicant.class);

        given(applicant.getJobPosting()).willReturn(jobPosting);
        given(applicant.getJobPosting().getMember()).willReturn(member);
        given(applicant.getJobPosting().getStartDate()).willReturn(LocalDateTime.now());
        given(applicant.getJobPosting().getEndDate()).willReturn(LocalDateTime.now());
        given(applicantRepository.findById(applicantId)).willReturn(Optional.of(applicant));

        // When
        Applicant result = applicantDomainService.findById(applicantId);

        // Then
        assertNotNull(result);
        assertEquals(applicant.getId(), result.getId());
        verify(applicantRepository, times(1)).findById(applicantId);
    }

    @Test
    public void givenNonExistingApplicantId_whenFindById_thenShouldThrowEntityNotFoundException() {
        // Given
        Long applicantId = 99999L;
        given(applicantRepository.findById(applicantId)).willReturn(Optional.empty());

        // When and Then
        assertThrows(EntityNotFoundException.class, () -> {
            applicantDomainService.findById(applicantId);
        });

        verify(applicantRepository, times(1)).findById(applicantId);
    }

    @Test
    void givenExistingJobPostingId_whenGetByJobPostingId_thenReturnsMatching() {
        // Given
        Long jobPostingId = 123L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        Applicant list1= new Applicant(1L, jobPostingId, 123L, 30000, 0);
        Applicant list2 = new Applicant(2L, jobPostingId, 123L, 30000, 0);

        List<Applicant> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<Applicant> page = new PageImpl<>(list, pageable, list.size());

        given(applicantRepository.findPageByJobPostingId(jobPostingId, pageable)).willReturn(page);

        // When
        Page<Applicant> result = applicantDomainService.getByJobPostingId(jobPostingId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(applicantRepository, times(1)).findPageByJobPostingId(jobPostingId, pageable);
    }

    @Test
    public void givenJobPostingId_whenIsAnnouncementMatchedByJobPostingId_thenCheckExistsByJobPostingIdAndMatchInfoIdNotNull() {
        // Given
        Long jobPostingId = 1L;

        given(applicantRepository.existsByJobPostingIdAndMatchInfoIdNotNull(jobPostingId)).willReturn(true);

        // When
        boolean isMatched = applicantDomainService.isAnnouncementMatchedByJobPostingId(jobPostingId);

        // Then
        verify(applicantRepository).existsByJobPostingIdAndMatchInfoIdNotNull(jobPostingId);
        assertTrue(isMatched);
    }

    @Test
    public void givenJobPostingId_whenIsAnnouncementMatchedByJobPostingId_thenReturnFalse() {
        // Given
        Long jobPostingId = 2L;

        given(applicantRepository.existsByJobPostingIdAndMatchInfoIdNotNull(jobPostingId)).willReturn(false);

        // When
        boolean isMatched = applicantDomainService.isAnnouncementMatchedByJobPostingId(jobPostingId);

        // Then
        verify(applicantRepository).existsByJobPostingIdAndMatchInfoIdNotNull(jobPostingId);
        assertFalse(isMatched); // Assert that the result is false since the mock returns false
    }

    @Test
    public void givenSaveMatchInfoRequestAndStatus_whenMatching_thenReturnSavedApplicant() {
        // Given
        Long applicantId = 1L;
        Long jobPostingId = 123L;
        int status = 1;
        SaveMatchInfoRequest saveMatchInfoRequest = new SaveMatchInfoRequest(applicantId, jobPostingId);
        Applicant applicant = mock(Applicant.class);

        given(applicantRepository.findById(applicantId)).willReturn(Optional.ofNullable(applicant));
        given(applicantRepository.save(applicant)).willReturn(applicant);

        // When
        Applicant savedApplicant = applicantDomainService.matching(saveMatchInfoRequest, status);

        // Then
        verify(applicantRepository).findById(applicantId);
        verify(applicantRepository).save(applicant);
        assertEquals(applicant, savedApplicant);
    }

    @Test
    public void givenNonExistingJobPostingAndTrainer_whenSave_thenSaveSuccessful() {
        // Given
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest(123L, 1000);
        Long trainerId = 456L;
        given(applicantRepository.existsByJobPostingIdAndTrainerId(saveApplicantRequest.getJobPostingId(), trainerId)).willReturn(false);
        given(applicantRepository.save(any(Applicant.class))).willReturn(new Applicant(1L, 123L, trainerId, 1000, 0));

        // When
        Applicant savedApplicant = applicantDomainService.save(saveApplicantRequest, trainerId);

        // Then
        assertEquals(1L, savedApplicant.getId());
        assertEquals(123L, savedApplicant.getJobPostingId());
        assertEquals(trainerId, savedApplicant.getTrainerId());
        assertEquals(1000, savedApplicant.getSupportAmount());
        verify(applicantRepository, times(1)).existsByJobPostingIdAndTrainerId(saveApplicantRequest.getJobPostingId(), trainerId);
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    public void givenExistingJobPostingAndTrainer_whenSaveApplicant_thenThrowEntityExistsException() {
        // Given
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest(123L, 1000);
        Long trainerId = 456L;
        given(applicantRepository.existsByJobPostingIdAndTrainerId(
                saveApplicantRequest.getJobPostingId(),
                trainerId
                ))
                .willReturn(true);

        // When - Then
        assertThrows(EntityExistsException.class, () -> applicantDomainService.save(saveApplicantRequest, trainerId));

        verify(applicantRepository, times(1))
                .existsByJobPostingIdAndTrainerId(
                        saveApplicantRequest.getJobPostingId(),
                        trainerId
                );
        verify(applicantRepository, never()).save(any(Applicant.class));
    }

    @Test
    public void givenApplicantExists_whenUpdateIsSelectedById_thenIsSelectedUpdated() {
        // Given
        Long applicantId = 1L;
        Applicant applicant = new Applicant(applicantId, 123L, 456L, 1000, 0);

        given(applicantRepository.findById(anyLong())).willReturn(Optional.of(applicant));

        // When
        applicantDomainService.updateIsSelectedById(applicantId, 1);

        // Then
        verify(applicantRepository, times(1)).findById(applicantId);
        verify(applicantRepository, times(1)).save(applicant);
    }


}
