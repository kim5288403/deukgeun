package com.example.deukgeun.job.repository;

import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.valueobject.JobAddressVo;
import com.example.deukgeun.job.infrastructure.persistence.repository.JobJpaRepository;
import org.junit.jupiter.api.BeforeEach;
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

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JobRepositoryTest {
    @Autowired
    private JobJpaRepository jobRepository;

    private JobAddressVo jobAddressVo;
    @BeforeEach
    void setup() {
        jobAddressVo = JobAddressVo
                .builder()
                .postcode("postcode")
                .detailAddress("detailAddress")
                .extraAddress("extraAddress")
                .jibunAddress("jibunAddress")
                .roadAddress("roadAddress")
                .build();
    }

    @Test
    void shouldNotNullRepository() {
        assertNotNull(jobRepository);
    }

    @Test
    @Sql("/insert_member.sql")
    void givenValidKeyword_whenFindByLikeKeyword_thenReturnFoundIsJobs() {
        // Given
        Long memberId = 123L;

        JobEntity jobEntity1 = JobEntity
                .builder()
                .id(1L)
                .memberId(memberId)
                .title("title")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobEntity jobEntity2 = JobEntity
                .builder()
                .id(2L)
                .memberId(memberId)
                .title("title")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        jobRepository.save(jobEntity1);
        jobRepository.save(jobEntity2);

        String keyword = "title";
        String converterKeyword = "%" + keyword +"%";
        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<JobEntity> foundJob = jobRepository.findByLikeKeyword(converterKeyword, pageable);

        // Then
        assertEquals(keyword, foundJob.getContent().get(0).getTitle());
        assertEquals(keyword, foundJob.getContent().get(1).getTitle());
    }

    @Test
    @Sql("/insert_member.sql")
    void givenValidId_whenFindById_thenReturnFoundIsJob() {
        // Given
        Long jobId = 1L;
        JobEntity jobEntity = JobEntity
                .builder()
                .id(jobId)
                .memberId(123L)
                .title("title")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobEntity saveJobEntity = jobRepository.save(jobEntity);

        // When
        JobEntity foundJobEntity = jobRepository.findById(jobId).orElse(null);

        // Then
        assertNotNull(foundJobEntity);
        assertEquals(saveJobEntity.getTitle(), foundJobEntity.getTitle());
    }

    @Test
    @Sql("/insert_member.sql")
    void givenValidMemberId_whenFindByMemberId_thenReturnFoundIsJob() {
        // Given
        Long memberId = 123L;

        JobEntity jobEntity1 = JobEntity
                .builder()
                .id(1L)
                .memberId(memberId)
                .title("title")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobEntity jobEntity2 = JobEntity
                .builder()
                .id(2L)
                .memberId(memberId)
                .title("title")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        jobRepository.save(jobEntity1);
        jobRepository.save(jobEntity2);

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<JobEntity> result = jobRepository.findByMemberId(memberId, pageable);

        // Then
        assertEquals(memberId, result.getContent().get(0).getMemberId());
        assertEquals(memberId, result.getContent().get(1).getMemberId());
    }
    @Test
    @Sql("/insert_member.sql")
    void givenValidJob_whenSave_thenJobIsSaved() {
        // Given
        Long memberId =123L;
        String title = "title";
        JobEntity jobEntity = JobEntity
                .builder()
                .id(1L)
                .title(title)
                .memberId(memberId)
                .isActive(1)
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build();

        // When
        JobEntity saveJobEntity = jobRepository.save(jobEntity);

        // Then
        JobEntity foundJobEntity = jobRepository.findById(saveJobEntity.getId()).orElse(null);
        assertNotNull(foundJobEntity);
        assertEquals(saveJobEntity.getTitle(), foundJobEntity.getTitle());
    }

    @Test
    @Sql("/insert_member.sql")
    public void givenValidIdAndMemberId_whenExistsByIdAndMemberId_thenReturnTrue() {
        // Given
        Long memberId = 123L;
        JobEntity jobEntity = JobEntity
                .builder()
                .id(1L)
                .title("title")
                .memberId(memberId)
                .isActive(1)
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(5))
                .build();

        JobEntity saveJobEntity = jobRepository.save(jobEntity);

        // When
        boolean result = jobRepository.existsByIdAndMemberId(saveJobEntity.getId(), saveJobEntity.getMemberId());

        // Then
        assertTrue(result);
    }

    @Test
    public void givenInValidIdAndMemberId_whenExistsByIdAndMemberId_thenFalse() {
        // Given
        Long id = 1L;
        Long memberId = 1L;

        // When
        boolean result = jobRepository.existsByIdAndMemberId(id, memberId);

        // Then
        assertFalse(result);
    }

}
