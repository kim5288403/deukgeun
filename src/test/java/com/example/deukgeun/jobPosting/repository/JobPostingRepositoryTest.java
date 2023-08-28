package com.example.deukgeun.jobPosting.repository;

import com.example.deukgeun.jobPosting.infrastructure.persistence.model.entity.JobPostingEntity;
import com.example.deukgeun.jobPosting.infrastructure.persistence.model.valueobject.JobAddressVo;
import com.example.deukgeun.jobPosting.infrastructure.persistence.repository.JobPostingJpaRepository;
import com.example.deukgeun.member.infrastructure.persistence.entity.MemberEntity;
import com.example.deukgeun.member.infrastructure.persistence.repository.MemberJpaRepository;
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
    private JobPostingJpaRepository jobPostingRepository;
    @Autowired
    private MemberJpaRepository memberRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(jobPostingRepository);
        assertNotNull(memberRepository);
    }

    @Test
    @Sql("/insert_member.sql")
    void givenJobPostings_whenFindByLikeKeyword_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobAddressVo jobAddressVo = new JobAddressVo(
                "test",
                "test",
                "test",
                "test",
                "test"
        );
        JobPostingEntity jobPostingEntity1 = JobPostingEntity
                .builder()
                .id(1L)
                .memberId(memberId)
                .title("test")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobPostingEntity jobPostingEntity2 = JobPostingEntity
                .builder()
                .id(2L)
                .memberId(memberId)
                .title("test")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        jobPostingRepository.save(jobPostingEntity1);
        jobPostingRepository.save(jobPostingEntity2);

        String keyword = "test";
        String converterKeyword = "%" + keyword +"%";
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<JobPostingEntity> foundJobPosting = jobPostingRepository.findByLikeKeyword(converterKeyword, pageable);

        // Then
        assertEquals(keyword, foundJobPosting.getContent().get(0).getTitle());
        assertEquals(keyword, foundJobPosting.getContent().get(1).getTitle());
    }

    @Test
    @Sql("/insert_member.sql")
    void givenJobPosting_whenFindById_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobPostingEntity jobPostingEntity = JobPostingEntity
                .builder()
                .id(5L)
                .memberId(memberId)
                .title("test")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobPostingEntity saveJobPostingEntity = jobPostingRepository.save(jobPostingEntity);

        // When
        JobPostingEntity foundJobPostingEntity = jobPostingRepository.findById(saveJobPostingEntity.getId()).orElse(null);

        // Then
        assertNotNull(foundJobPostingEntity);
        assertEquals(saveJobPostingEntity.getTitle(), foundJobPostingEntity.getTitle());
    }

    @Test
    @Sql("/insert_member.sql")
    void givenJobPosting_whenFindByMemberId_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobAddressVo jobAddressVo = new JobAddressVo(
                "test",
                "test",
                "test",
                "test",
                "test"
        );
        JobPostingEntity jobPostingEntity1 = JobPostingEntity
                .builder()
                .id(1L)
                .memberId(memberId)
                .title("test1")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        JobPostingEntity jobPostingEntity2 = JobPostingEntity
                .builder()
                .id(2L)
                .memberId(memberId)
                .title("test2")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        jobPostingRepository.save(jobPostingEntity1);
        jobPostingRepository.save(jobPostingEntity2);

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<JobPostingEntity> result = jobPostingRepository.findByMemberId(memberId, pageable);

        // Then
        assertEquals(memberId, result.getContent().get(0).getMemberId());
        assertEquals(memberId, result.getContent().get(1).getMemberId());
    }
    @Test
    @Sql("/insert_member.sql")
    void givenJobPosting_whenSaved_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        String title = "test";
        JobPostingEntity jobPostingEntity = JobPostingEntity
                .builder()
                .id(1L)
                .title(title)
                .memberId(memberId)
                .isActive(1)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build();

        // When
        JobPostingEntity saveJobPostingEntity = jobPostingRepository.save(jobPostingEntity);

        // Then
        JobPostingEntity foundJobPostingEntity = jobPostingRepository.findById(saveJobPostingEntity.getId()).orElse(null);
        assertNotNull(foundJobPostingEntity);
        assertEquals(saveJobPostingEntity.getTitle(), foundJobPostingEntity.getTitle());
    }

    @Test
    @Sql("/insert_member.sql")
    public void givenIdAndMemberId_whenExistsByIdAndMemberId_thenTrue() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobAddressVo jobAddressVo = new JobAddressVo(
                "test",
                "test",
                "test",
                "test",
                "test"
        );
        JobPostingEntity jobPostingEntity = JobPostingEntity
                .builder()
                .id(1L)
                .title("test")
                .memberId(memberId)
                .isActive(1)
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build();

        JobPostingEntity saveJobPostingEntity = jobPostingRepository.save(jobPostingEntity);

        // When
        boolean result = jobPostingRepository.existsByIdAndMemberId(saveJobPostingEntity.getId(), saveJobPostingEntity.getMemberId());

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
