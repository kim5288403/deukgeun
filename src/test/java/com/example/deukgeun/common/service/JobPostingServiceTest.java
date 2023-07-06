package com.example.deukgeun.common.service;

import com.example.deukgeun.commom.repository.JobPostingRepository;
import com.example.deukgeun.commom.response.JobPostingResponse;
import com.example.deukgeun.commom.service.implement.JobPostingServiceImpl;
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
    void givenKeywordAndPage_whenGetList_thenReturnsMatchingUsers() {
        // Givne
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

}
