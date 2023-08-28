package com.example.deukgeun.jobPosting.service.domain;

import com.example.deukgeun.jobPosting.application.dto.request.SaveJobPostingRequest;
import com.example.deukgeun.jobPosting.domain.model.aggregate.JobPosting;
import com.example.deukgeun.jobPosting.domain.repository.JobPostingRepository;
import com.example.deukgeun.jobPosting.domain.service.implement.JobPostingDomainServiceImpl;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JobPostingDomainServiceTest {
    @InjectMocks
    private JobPostingDomainServiceImpl jobPostingDomainService;

    @Mock
    private JobPostingRepository jobPostingRepository;

    @Test
    void givenCheckJobPostingOwnershipRequest_whenExistsByIdAndMemberId_thenTure() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        given(jobPostingRepository.existsByIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        // When
        boolean result = jobPostingDomainService.existsByIdAndMemberId(id, memberId);

        // Then
        verify(jobPostingRepository, times(1)).existsByIdAndMemberId(id, memberId);
        assertTrue(result);
    }

    @Test
    void givenIdAndMemberId_whenExistsByIdAndMemberId_thenFalse() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        given(jobPostingRepository.existsByIdAndMemberId(anyLong(), anyLong())).willReturn(false);

        // When
        boolean result = jobPostingDomainService.existsByIdAndMemberId(id, memberId);

        // Then
        verify(jobPostingRepository, times(1)).existsByIdAndMemberId(id, memberId);
        assertFalse(result);
    }

    @Test
    void givenJobPostingId_whenFindById_thenReturnMatching() {
        // Given
        long jobPostingId = 5L;
        JobPosting jobPosting = mock(JobPosting.class);
        given(jobPostingRepository.findById(jobPostingId)).willReturn(Optional.of(jobPosting));

        // When
        JobPosting result = jobPostingDomainService.findById(jobPostingId);

        // Then
        assertNotNull(result);
        assertEquals(jobPosting.getTitle(), result.getTitle());
    }

    @Test
    void givenNonexistentId_whenFindById_thenThrowsEntityNotFoundException() {
        // Given
        long id = 5L;
        given(jobPostingRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> jobPostingDomainService.findById(id));

        // Verify
        verify(jobPostingRepository, times(1)).findById(id);
    }

    @Test
    void givenKeywordAndPage_whenGetListByKeyword_thenReturnsMatchingLists() {
        // Given
        String keyword = "john";
        int currentPage = 0;
        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        JobPosting list1 = mock(JobPosting.class);
        JobPosting list2 = mock(JobPosting.class);

        List<JobPosting> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<JobPosting> page = new PageImpl<>(list, pageable, list.size());

        given(jobPostingRepository.findByLikeKeyword(likeKeyword, pageable)).willReturn(page);

        // When
        Page<JobPosting> result = jobPostingDomainService.getListByKeyword(likeKeyword, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobPostingRepository, times(1)).findByLikeKeyword(likeKeyword, pageable);
    }

    @Test
    void givenExistingMemberId_whenGetByMemberId_thenReturnsMatching() {
        // Given
        Long memberId = 123L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        JobPosting list1 = mock(JobPosting.class);
        JobPosting list2 = mock(JobPosting.class);

        List<JobPosting> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<JobPosting> page = new PageImpl<>(list, pageable, list.size());

        given(jobPostingRepository.findByMemberId(memberId, pageable)).willReturn(page);

        // When
        Page<JobPosting> result = jobPostingDomainService.getListByMemberId(memberId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobPostingRepository, times(1)).findByMemberId(memberId, pageable);
    }

    @Test
    void givenSaveJobPostingRequest_whenSave_thenIsSaved() {
        // Given
        Long memberId = 123L;
        SaveJobPostingRequest saveJobPostingRequest = new SaveJobPostingRequest();
        saveJobPostingRequest.setTitle("test");
        saveJobPostingRequest.setPostcode("12-3");
        saveJobPostingRequest.setStartDate("2023-08-08T12:51");
        saveJobPostingRequest.setEndDate("2023-08-08T12:51");

        // When
        jobPostingDomainService.save(saveJobPostingRequest, memberId);

        // Then
        verify(jobPostingRepository, times(1)).save(any(JobPosting.class));
    }

    @Test
    public void givenJobPostingIdAndIsActive_whenUpdateIsActiveByJobPostingId_thenReturnUpdatedJobPosting() {
        // Given
        int isActive = 1;
        Long jobPostingId = 1L;
        JobPosting foundJobPosting = mock(JobPosting.class);
        JobPosting updateJobPosting = new JobPosting(
                1L,
                123L,
                "test",
                1,
                "test",
                mock(Address.class),
                1,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(jobPostingRepository.findById(jobPostingId)).willReturn(Optional.of(foundJobPosting));
        given(jobPostingRepository.save(any(JobPosting.class))).willReturn(updateJobPosting);

        // When
        JobPosting updatedJobPosting = jobPostingDomainService.updateIsActiveByJobPostingId(isActive, jobPostingId);

        // Then
        assertEquals(isActive, updatedJobPosting.getIsActive());
        assertEquals(jobPostingId, updatedJobPosting.getId());
    }

}
