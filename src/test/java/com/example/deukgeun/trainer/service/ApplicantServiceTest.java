package com.example.deukgeun.trainer.service;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.repository.ApplicantRepository;
import com.example.deukgeun.trainer.request.SaveApplicantRequest;
import com.example.deukgeun.trainer.service.implement.ApplicantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
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
    void givenSaveApplicantRequest_whenSave_thenIsSaved() {
        // Given
        SaveApplicantRequest saveApplicantRequest = new SaveApplicantRequest();

        // When
        applicantService.save(saveApplicantRequest);

        // Then
        verify(applicantRepository, times(1)).save(any(Applicant.class));

    }
}
