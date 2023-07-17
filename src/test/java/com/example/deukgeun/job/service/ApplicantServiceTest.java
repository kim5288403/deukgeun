package com.example.deukgeun.job.service;

import com.example.deukgeun.job.domain.entity.Applicant;
import com.example.deukgeun.job.domain.repository.ApplicantRepository;
import com.example.deukgeun.job.infrastructure.persistence.ApplicantServiceImpl;
import com.example.deukgeun.job.application.dto.request.SaveApplicantRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityExistsException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicantServiceTest {
    @InjectMocks
    private ApplicantServiceImpl applicantService;

    @Mock
    private ApplicantRepository applicantRepository;

    @Test
    void givenSaveApplicantRequestAndTrainerId_whenSave_thenIsSaved() {
        // Given
        Long trainerId = 123L;
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest();

        // When
        applicantService.save(saveApplicantRequest, trainerId);

        // Then
        verify(applicantRepository, times(1)).save(any(Applicant.class));
    }

    @Test
    public void givenSaveApplicantRequestAndTrainerId_whenAlreadyExists_thenThrowEntityExistsException() {
        // Given
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest();
        saveApplicantRequest.setJobPostingId(1L);
        saveApplicantRequest.setSupportAmount(1000);

        Long trainerId = 1L;

        given(applicantRepository.existsByJobPostingIdAndTrainerId(anyLong(), anyLong()))
                .willReturn(true);

        // When, Then
        assertThrows(EntityExistsException.class, () -> applicantService.save(saveApplicantRequest, trainerId));
    }
}
