package com.example.deukgeun.job.service.domain;

import com.example.deukgeun.job.domain.dto.SaveJobDTO;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.model.valueobject.JobAddress;
import com.example.deukgeun.job.domain.repository.JobRepository;
import com.example.deukgeun.job.domain.service.implement.JobDomainServiceImpl;
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

    @Test
    void givenValidIdAndMemberId_whenExistsByIdAndMemberId_thenReturnTure() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        given(jobRepository.existsByIdAndMemberId(anyLong(), anyLong())).willReturn(true);

        // When
        boolean result = jobDomainService.existsByIdAndMemberId(id, memberId);

        // Then
        assertTrue(result);
        verify(jobRepository, times(1)).existsByIdAndMemberId(anyLong(), anyLong());
    }

    @Test
    void givenIdAndMemberId_whenExistsByIdAndMemberId_thenFalse() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        // When
        boolean result = jobDomainService.existsByIdAndMemberId(id, memberId);

        // Then
        assertFalse(result);
        verify(jobRepository, times(1)).existsByIdAndMemberId(anyLong(), anyLong());
    }

    @Test
    void givenValidId_whenFindById_thenReturnFoundIsJob() {
        // Given
        long jobId = 1L;
        Job job = mock(Job.class);
        given(jobRepository.findById(anyLong())).willReturn(Optional.of(job));

        // When
        Job result = jobDomainService.findById(jobId);

        // Then
        assertNotNull(result);
        assertEquals(job.getTitle(), result.getTitle());
        verify(jobRepository, times(1)).findById(anyLong());
    }

    @Test
    void givenInValidId_whenFindById_thenThrowsEntityNotFoundException() {
        // Given
        long id = 1L;
        given(jobRepository.findById(anyLong())).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> jobDomainService.findById(id));

        // Verify
        verify(jobRepository, times(1)).findById(anyLong());
    }

    @Test
    void givenValidMemberId_whenFindByMemberId_thenReturnFoundIsJobList() {
        // Given
        Long memberId = 1L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        Job list1 = mock(Job.class);
        Job list2 = mock(Job.class);

        List<Job> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<Job> page = new PageImpl<>(list, pageable, list.size());

        given(jobRepository.findByMemberId(anyLong(), any(PageRequest.class))).willReturn(page);

        // When
        Page<Job> result = jobDomainService.findListByMemberId(memberId, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobRepository, times(1)).findByMemberId(anyLong(), any(PageRequest.class));
    }

    @Test
    void givenValidKeywordAndPage_whenFindListByKeyword_thenReturnFoundIsJobList() {
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

        given(jobRepository.findByLikeKeyword(anyString(), any(PageRequest.class))).willReturn(page);

        // When
        Page<Job> result = jobDomainService.findListByKeyword(likeKeyword, pageable);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobRepository, times(1)).findByLikeKeyword(anyString(), any(PageRequest.class));
    }



    @Test
    void givenValidSaveJobDTO_whenSave_thenJobIsSaved() {
        // Given
        SaveJobDTO saveJobDTO = new SaveJobDTO();
        saveJobDTO.setMemberId(1L);
        saveJobDTO.setTitle("title");
        saveJobDTO.setPostcode("postcode");
        saveJobDTO.setStartDate("2023-08-08T12:51");
        saveJobDTO.setEndDate("2023-08-08T12:51");

        Job job = Job.create(
                saveJobDTO.getMemberId(),
                saveJobDTO.getTitle(),
                saveJobDTO.getRequirementLicense(),
                saveJobDTO.getRequirementEtc(),
                mock(JobAddress.class),
                1,
                saveJobDTO.getStartDate(),
                saveJobDTO.getEndDate()
        );

        given(jobRepository.save(any(Job.class))).willReturn(job);

        // When
        Job saveJob = jobDomainService.save(saveJobDTO);

        // Then
        assertNotNull(saveJob);
        assertEquals(saveJobDTO.getTitle(), saveJob.getTitle());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    public void givenValidIdAndIsActive_whenUpdateIsActiveByJobId_thenActiveIsUpdate() {
        // Given
        int isActive = 1;
        Long jobId = 1L;
        Job job = Job.create(
                1L,
                "title",
                0,
                "requirementEtc",
                mock(JobAddress.class),
                0,
                "2023-08-08T12:51",
                "2023-08-08T12:51"
        );

        given(jobRepository.findById(anyLong())).willReturn(Optional.of(job));

        // When
        jobDomainService.updateIsActiveByJobId(isActive, jobId);

        // Then
        assertEquals(isActive, job.getIsActive());
        verify(jobRepository, times(1)).findById(anyLong());
        verify(jobRepository, times(1)).save(any(Job.class));

    }

}
