package com.example.deukgeun.common.repository;

import com.example.deukgeun.commom.entity.JobPosting;
import com.example.deukgeun.commom.repository.JobPostingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
    void givenJobPosting_whenSaved_thenReturnValid() {
        // Given
        String title = "test";
        JobPosting jobPosting = JobPosting
                .builder()
                .title(title)
                .memberId(123L)
                .postcode("12-2")
                .isActive(1)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build();

        // When
        JobPosting saveJobPosting = jobPostingRepository.save(jobPosting);

        // Then
        JobPosting foundJobPosting = jobPostingRepository.findById(saveJobPosting.getId()).orElse(null);
        assertNotNull(foundJobPosting);
        assertEquals(saveJobPosting.getTitle(), foundJobPosting.getTitle());

    }

}
