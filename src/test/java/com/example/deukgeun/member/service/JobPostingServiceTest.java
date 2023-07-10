package com.example.deukgeun.member.service;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.repository.JobPostingRepository;
import com.example.deukgeun.main.response.JobPostingResponse;
import com.example.deukgeun.member.request.SaveJobPostingRequest;
import com.example.deukgeun.member.service.implement.JobPostingServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
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
        // Given
        Long memberId = 123L;
        SaveJobPostingRequest saveJobPostingRequest = new SaveJobPostingRequest();
        saveJobPostingRequest.setTitle("test");
        saveJobPostingRequest.setPostcode("12-3");
        saveJobPostingRequest.setStartDate(String.valueOf(LocalDateTime.now()));
        saveJobPostingRequest.setEndDate(String.valueOf(LocalDateTime.now().plusDays(4)));

        // When
        jobPostingService.save(saveJobPostingRequest, memberId);

        // Then
        verify(jobPostingRepository, times(1)).save(any(JobPosting.class));
    }

    @Test
    void givenExistingMemberId_whenGetByMemberId_thenReturnsMatching() {
        // Given
        Long memberId = 123L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        JobPostingResponse.ListResponse list1 = new JobPostingResponse.ListResponse();
        list1.setId(1L);
        list1.setMemberId(memberId);
        list1.setTitle("test1");
        JobPostingResponse.ListResponse list2 = new JobPostingResponse.ListResponse();
        list2.setId(2L);
        list2.setMemberId(memberId);
        list2.setTitle("test2");

        List<JobPostingResponse.ListResponse> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<JobPostingResponse.ListResponse> page = new PageImpl<>(list, pageable, list.size());

        given(jobPostingRepository.findByMemberId(memberId, pageable)).willReturn(page);

        // When
        Page<JobPostingResponse.ListResponse> result = jobPostingService.getByMemberId(memberId, currentPage);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobPostingRepository, times(1)).findByMemberId(memberId, pageable);
    }

    @Test
    public void givenJobPostingIdAndIsActive_whenUpdateIsActiveByJobPostingId_thenReturnUpdatedJobPosting() {
        // Given
        int isActive = 1; // 예시로 isActive를 1로 설정 (활성화)
        Long jobPostingId = 1L; // 예시로 jobPostingId를 1로 설정
        JobPosting foundJobPosting = JobPosting
                .builder()
                .id(jobPostingId)
                .isActive(0)
                .build();

        JobPosting updaeteJobPosting = JobPosting
                .builder()
                .id(jobPostingId)
                .isActive(isActive)
                .build();

        given(jobPostingRepository.findById(jobPostingId)).willReturn(Optional.of(foundJobPosting));
        given(jobPostingRepository.save(any(JobPosting.class))).willReturn(updaeteJobPosting);

        // When
        JobPosting updatedJobPosting = jobPostingService.updateIsActiveByJobPostingId(isActive, jobPostingId);

        // Then
        assertEquals(isActive, updatedJobPosting.getIsActive());
        assertEquals(jobPostingId, updatedJobPosting.getId());
    }

}
