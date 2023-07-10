package com.example.deukgeun.main.service;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.repository.JobPostingRepository;
import com.example.deukgeun.main.response.JobPostingResponse;
import com.example.deukgeun.main.service.implement.JobPostingServiceImpl;
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
    void givenKeywordAndPage_whenGetList_thenReturnsMatchingLists() {
        // Given
        String keyword = "john";
        int currentPage = 0;
        String likeKeyword = "%" + keyword + "%";
        PageRequest pageable = PageRequest.of(currentPage, 10);

        JobPostingResponse.ListResponse list1 = new JobPostingResponse.ListResponse();
        list1.setId(1L);
        list1.setTitle("test1");
        JobPostingResponse.ListResponse list2 = new JobPostingResponse.ListResponse();
        list2.setId(2L);
        list2.setTitle("test2");

        List<JobPostingResponse.ListResponse> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        Page<JobPostingResponse.ListResponse> page = new PageImpl<>(list, pageable, list.size());

        given(jobPostingRepository.findByLikeKeyword(likeKeyword, pageable)).willReturn(page);

        // When
        Page<JobPostingResponse.ListResponse> result = jobPostingService.getList(keyword, currentPage);

        // Then
        assertNotNull(result);
        assertEquals(list.size(), result.getContent().size());
        verify(jobPostingRepository, times(1)).findByLikeKeyword(likeKeyword, pageable);
    }

    @Test
    void givenJobPostingId_whenGetById_thenReturnMatching() {
        // Given
        long jobPostingId = 5L;
        JobPosting jobPosting = JobPosting
                .builder()
                .id(5L)
                .memberId(123L)
                .title("test")
                .postcode("123")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        given(jobPostingRepository.findById(jobPostingId)).willReturn(Optional.of(jobPosting));

        // When
        JobPosting result = jobPostingService.getById(jobPostingId);

        // Then
        assertNotNull(result);
        assertEquals(jobPosting.getTitle(), result.getTitle());
    }

    @Test
    void givenNonexistentId_whenGetById_thenThrowsEntityNotFoundException() {
        // Given
        long id = 5L;
        given(jobPostingRepository.findById(id)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> jobPostingService.getById(id));

        // Verify
        verify(jobPostingRepository, times(1)).findById(id);
    }


}
