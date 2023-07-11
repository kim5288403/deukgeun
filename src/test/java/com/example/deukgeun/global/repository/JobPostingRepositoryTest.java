package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.main.response.JobPostingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void givenJobPosting_whenFindById_thenReturnValid() {
        // Given
        JobPosting jobPosting = JobPosting
                .builder()
                .id(5L)
                .memberId(123L)
                .title("test")
                .postcode("123")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobPosting saveJobPosting = jobPostingRepository.save(jobPosting);

        // When
        JobPosting foundJobPosting = jobPostingRepository.findById(saveJobPosting.getId()).orElse(null);

        // Then
        assertNotNull(foundJobPosting);
        assertEquals(saveJobPosting.getTitle(), foundJobPosting.getTitle());
    }

    @Test
    void givenJobPosting_whenFindByMemberId_thenReturnValid() {
        // Given
        Long memberId = 123L;
        JobPosting jobPosting1 = JobPosting
                .builder()
                .memberId(memberId)
                .title("test1")
                .postcode("123")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        JobPosting jobPosting2 = JobPosting
                .builder()
                .memberId(memberId)
                .title("test2")
                .postcode("123")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        jobPostingRepository.save(jobPosting1);
        jobPostingRepository.save(jobPosting2);

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<JobPostingResponse.ListResponse> result = jobPostingRepository.findByMemberId(memberId, pageable);

        // Then
        assertEquals(memberId, result.getContent().get(0).getMemberId());
        assertEquals(memberId, result.getContent().get(1).getMemberId());
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

    @Test
    public void givenIdAndMemberId_whenExistsByIdAndMemberId_thenTrue() {
        // Given
        JobPosting jobPosting = JobPosting
                .builder()
                .title("test")
                .memberId(123L)
                .postcode("12-2")
                .isActive(1)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build();

        JobPosting saveJobPosting = jobPostingRepository.save(jobPosting);

        // When
        boolean result = jobPostingRepository.existsByIdAndMemberId(saveJobPosting.getId(), saveJobPosting.getMemberId());

        // Then
        assertTrue(result);
    }

    @Test
    public void givenIdAndMemberId_whenExistsByIdAndMemberId_thenFalse() {
        // Given
        Long id = 1L;
        Long memberId = 123L;

        // When
        boolean result = jobPostingRepository.existsByIdAndMemberId(id, memberId);

        // Then
        assertFalse(result);
    }

}
