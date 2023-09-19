package com.example.deukgeun.job.service.domain;

import com.example.deukgeun.job.domain.dto.SaveJobDTO;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.model.valueobject.JobAddress;
import com.example.deukgeun.job.domain.repository.JobRepository;
import com.example.deukgeun.job.domain.service.implement.JobDomainServiceImpl;
import com.example.deukgeun.job.infrastructure.persistence.mapper.JobMapper;
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
public class JobDomainServiceTest {
    @InjectMocks
    private JobDomainServiceImpl jobDomainService;
    @Mock
    private JobRepository jobRepository;
    @Mock
    private JobMapper jobMapper;

    @Test
    void givenCheckJobOwnershipRequest_whenExistsByIdAndMemberId_thenTure() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        given(jobRepository.existsByIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        // When
        boolean result = jobDomainService.existsByIdAndMemberId(id, memberId);

        // Then
        verify(jobRepository, times(1)).existsByIdAndMemberId(id, memberId);
        assertTrue(result);
    }

    @Test
    void givenIdAndMemberId_whenExistsByIdAndMemberId_thenFalse() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        given(jobRepository.existsByIdAndMemberId(anyLong(), anyLong())).willReturn(false);

        // When
        boolean result = jobDomainService.existsByIdAndMemberId(id, memberId);

        // Then
        verify(jobRepository, times(1)).existsByIdAndMemberId(id, memberId);
        assertFalse(result);
    }

    @Test
    void givenJobId_whenFindById_thenReturnMatching() {
        // Given
        long jobId = 5L;
        Job job = mock(Job.class);
        given(jobRepository.findById(jobId)).willReturn(Optional.of(job));

        // When
        Job result = jobDomainService.findById(jobId);

        // Then
        assertNotNull(result);
        assertEquals(job.getTitle(), result.getTitle());
    }

    @Test
    void givenNonexistentId_whenFindById_thenThrowsEntityNotFoundException() {
        // Given
        long id = 5L;
        given(jobRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> jobDomainService.findById(id));

        // Verify
        verify(jobRepository, times(1)).findById(id);
    }

    @Test
    void givenKeywordAndPage_whenGetListByKeyword_thenReturnsMatchingLists() {
        // Given
        String keyword = "john";
        int currentPage = 0;
        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        Job list1 = mock(Job.class);
        Job list2 = mock(Job.class);

        List<Job> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<Job> page = new PageImpl<>(list, pageable, list.size());

        given(jobRepository.findByLikeKeyword(likeKeyword, pageable)).willReturn(page);

        // When
        Page<Job> result = jobDomainService.findListByKeyword(likeKeyword, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobRepository, times(1)).findByLikeKeyword(likeKeyword, pageable);
    }

    @Test
    void givenExistingMemberId_whenGetByMemberId_thenReturnsMatching() {
        // Given
        Long memberId = 123L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        Job list1 = mock(Job.class);
        Job list2 = mock(Job.class);

        List<Job> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<Job> page = new PageImpl<>(list, pageable, list.size());

        given(jobRepository.findByMemberId(memberId, pageable)).willReturn(page);

        // When
        Page<Job> result = jobDomainService.findListByMemberId(memberId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobRepository, times(1)).findByMemberId(memberId, pageable);
    }

    @Test
    void givenSaveJobDTO_whenSave_thenIsSaved() {
        // Given
        SaveJobDTO saveJobDTO = new SaveJobDTO();
        saveJobDTO.setMemberId(1L);
        saveJobDTO.setTitle("test");
        saveJobDTO.setPostcode("12-3");
        saveJobDTO.setStartDate("2023-08-08T12:51");
        saveJobDTO.setEndDate("2023-08-08T12:51");
        given(jobMapper.toJobAddress(any(SaveJobDTO.class))).willReturn(mock(JobAddress.class));

        // When
        jobDomainService.save(saveJobDTO);

        // Then
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    public void givenJobIdAndIsActive_whenUpdateIsActiveByJobId_thenReturnUpdatedJob() {
        // Given
        int isActive = 1;
        Long jobId = 1L;
        Job foundJob = mock(Job.class);
        Job updateJob = new Job(
                1L,
                123L,
                "test",
                1,
                "test",
                mock(JobAddress.class),
                1,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        given(jobRepository.findById(jobId)).willReturn(Optional.of(foundJob));
        given(jobRepository.save(any(Job.class))).willReturn(updateJob);

        // When
        Job updatedJob = jobDomainService.updateIsActiveByJobId(isActive, jobId);

        // Then
        assertEquals(isActive, updatedJob.getIsActive());
        assertEquals(jobId, updatedJob.getId());
    }

}
