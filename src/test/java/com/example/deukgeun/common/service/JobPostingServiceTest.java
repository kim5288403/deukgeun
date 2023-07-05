package com.example.deukgeun.common.service;

import com.example.deukgeun.commom.entity.JobPosting;
import com.example.deukgeun.commom.repository.JobPostingRepository;
import com.example.deukgeun.commom.request.SaveJobPostingRequest;
import com.example.deukgeun.member.service.implement.JobPostingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JobPostingServiceTest {
    @InjectMocks
    private JobPostingServiceImpl jobPostingService;

    @Mock
    private JobPostingRepository jobPostingRepository;

    @Test
    void givenSaveJobPostingRequest_whenSave_thenIsSaved() {
        // Givne
        Long memberId = 123L;
        SaveJobPostingRequest saveJobPostingRequest = new SaveJobPostingRequest();
        saveJobPostingRequest.setTitle("test");
        saveJobPostingRequest.setRequirements(6);
        saveJobPostingRequest.setPostcode("12-3");
        saveJobPostingRequest.setStartDate(LocalDateTime.now());
        saveJobPostingRequest.setEndDate(LocalDateTime.now().plusDays(4));

        // When
        jobPostingService.save(saveJobPostingRequest, memberId);

        // Then
        verify(jobPostingRepository, times(1)).save(any(JobPosting.class));

    }

}
