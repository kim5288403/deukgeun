package com.example.deukgeun.job.service.application;

import com.example.deukgeun.job.application.dto.request.SaveJobRequest;
import com.example.deukgeun.job.application.dto.response.JobResponse;
import com.example.deukgeun.job.application.service.implement.JobApplicationServiceImpl;
import com.example.deukgeun.job.domain.dto.SaveJobDTO;
import com.example.deukgeun.job.domain.model.aggregate.Job;
import com.example.deukgeun.job.domain.service.JobDomainService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JobApplicationServiceTest {

    @InjectMocks
    private JobApplicationServiceImpl jobApplicationService;

    @Mock
    private JobDomainService jobDomainService;
    @Mock
    private JobMapper jobMapper;

    @Test
    void givenValidId_whenGetDetail_thenReturnJobResponse() {
        // Given
        Long id = 1L;
        Job job = mock(Job.class);
        JobResponse.Detail detail = mock(JobResponse.Detail.class);


        given(jobDomainService.findById(anyLong())).willReturn(job);
        given(jobMapper.toJobResponseDetail(any(Job.class))).willReturn(detail);

        // When
        JobResponse.Detail result = jobApplicationService.getDetail(id);

        // Then
        assertNotNull(result);
        assertEquals(job.getTitle(), result.getTitle());
        verify(jobDomainService, times(1)).findById(anyLong());
    }

    @Test
    void givenValidIdAndMemberId_whenExistsByIdAndMemberId_thenExistsByIdAndMemberIdCalled() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        // When
        jobApplicationService.existsByIdAndMemberId(id, memberId);

        // Then
        verify(jobDomainService, times(1)).existsByIdAndMemberId(anyLong(), anyLong());
    }

    @Test
    void givenValidId_whenFindById_thenFindByIdCalled() {
        // Given
        Long id = 1L;

        // When
        jobApplicationService.findById(id);

        // Then
        verify(jobDomainService, times(1)).findById(anyLong());
    }

    @Test
    void givenValidKeywordAndCurrentPage_whenGetListByKeyword_thenReturnJobResponseListPage() {
        // Given
        String keyword = "keyword";
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        Job job = mock(Job.class);
        List<Job> list = new ArrayList<>();
        list.add(job);
        Page<Job> page = new PageImpl<>(list, pageable, list.size());


        given(jobDomainService.findListByKeyword(anyString(), any(PageRequest.class))).willReturn(page);

        // When
        Page<JobResponse.List> result = jobApplicationService.getListByKeyword(keyword, currentPage);

        // Then
        assertNotNull(result);
        verify(jobDomainService, times(1)).findListByKeyword(anyString(), any(PageRequest.class));
    }

    @Test
    void givenValidMemberIdAndCurrentPage_whenGetListByMemberId_thenReturnJobResponseListPage() {
        // Given
        Long memberId = 1L;
        int currentPage = 0;
        PageRequest pageable = PageRequest.of(currentPage, 10);

        Job job = mock(Job.class);
        List<Job> list = new ArrayList<>();
        list.add(job);
        Page<Job> page = new PageImpl<>(list, pageable, list.size());


        given(jobDomainService.findListByMemberId(anyLong(), any(PageRequest.class))).willReturn(page);

        // When
        Page<JobResponse.List> result = jobApplicationService.getListByMemberId(memberId, currentPage);

        // Then
        assertNotNull(result);
        verify(jobDomainService, times(1)).findListByMemberId(anyLong(), any(PageRequest.class));
    }

    @Test
    void givenValidSaveJobRequestAndMemberId_whenSave_thenSaveCalled() {
        // Given
        Long memberId = 1L;
        SaveJobRequest saveJobRequest = mock(SaveJobRequest.class);
        Job job = mock(Job.class);
        SaveJobDTO saveJobDTO = mock(SaveJobDTO.class);

        given(jobDomainService.save(any(SaveJobDTO.class))).willReturn(job);
        given(jobMapper.toSaveJobDto(anyLong(), any(SaveJobRequest.class))).willReturn(saveJobDTO);

        // When
        Job result = jobApplicationService.save(saveJobRequest, memberId);

        // Then
        assertNotNull(result);
        verify(jobMapper, times(1)).toSaveJobDto(anyLong(), any(SaveJobRequest.class));
        verify(jobDomainService, times(1)).save(any(SaveJobDTO.class));
    }

    @Test
    void givenValidIsActiveAndId_whenUpdateIsActiveByJobId_thenUpdateIsActiveByJobIdCalled() {
        // Given
        Long id = 1L;
        int isActive = 1;

        // When
        jobApplicationService.updateIsActiveByJobId(isActive, id);

        // Then
        verify(jobDomainService, times(1)).updateIsActiveByJobId(anyInt(), anyLong());
    }

}
