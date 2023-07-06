package com.example.deukgeun.common.repository;

import com.example.deukgeun.commom.entity.JobPosting;
import com.example.deukgeun.commom.repository.JobPostingRepository;
import com.example.deukgeun.commom.response.JobPostingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobPostingRepositoryTest {
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(jobPostingRepository);
    }

    @Test
    void givenJobPostings_whenFindByLikeKeyword_thenReturnValid() {
        // Given
        JobPosting jobPosting1 = JobPosting
                .builder()
                .memberId(123L)
                .title("test")
                .postcode("123")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobPosting jobPosting2 = JobPosting
                .builder()
                .memberId(124L)
                .title("test")
                .postcode("123")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        jobPostingRepository.save(jobPosting1);
        jobPostingRepository.save(jobPosting2);

        String keyword = "test";
        String converterKeyword = "%" + keyword +"%";
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<JobPostingResponse.ListResponse> foundJobPosting = jobPostingRepository.findByLikeKeyword(converterKeyword, pageable);

        // Then
        assertEquals(keyword, foundJobPosting.getContent().get(0).getTitle());
        assertEquals(keyword, foundJobPosting.getContent().get(1).getTitle());
    }

}
