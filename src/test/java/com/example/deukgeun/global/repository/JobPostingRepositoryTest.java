package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.entity.Member;
import com.example.deukgeun.main.response.JobPostingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobPostingRepositoryTest {
    @Autowired
    private JobPostingRepository jobPostingRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(jobPostingRepository);
        assertNotNull(memberRepository);
    }

    @Test
    @Sql("/insert_member.sql")
    void givenJobPostings_whenFindByLikeKeyword_thenReturnValid() {
        // Given
        List<Member> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobPosting jobPosting1 = JobPosting
                .builder()
                .memberId(memberId)
                .title("test")
                .postcode("123")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobPosting jobPosting2 = JobPosting
                .builder()
                .memberId(memberId)
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
    @Sql("/insert_member.sql")
    void givenJobPosting_whenFindById_thenReturnValid() {
        // Given
        List<Member> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobPosting jobPosting = JobPosting
                .builder()
                .id(5L)
                .memberId(memberId)
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
    @Sql("/insert_member.sql")
    void givenJobPosting_whenFindByMemberId_thenReturnValid() {
        // Given
        List<Member> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
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
    @Sql("/insert_member.sql")
    void givenJobPosting_whenSaved_thenReturnValid() {
        // Given
        List<Member> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        String title = "test";
        JobPosting jobPosting = JobPosting
                .builder()
                .title(title)
                .memberId(memberId)
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
    @Sql("/insert_member.sql")
    public void givenIdAndMemberId_whenExistsByIdAndMemberId_thenTrue() {
        // Given
        List<Member> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobPosting jobPosting = JobPosting
                .builder()
                .title("test")
                .memberId(memberId)
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
