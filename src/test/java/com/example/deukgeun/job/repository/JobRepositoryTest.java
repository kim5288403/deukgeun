package com.example.deukgeun.job.repository;

import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import com.example.deukgeun.job.infrastructure.persistence.model.valueobject.JobAddressVo;
import com.example.deukgeun.job.infrastructure.persistence.repository.JobJpaRepository;
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
public class JobRepositoryTest {
    @Autowired
    private JobJpaRepository jobRepository;
    @Autowired
    private MemberJpaRepository memberRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(jobRepository);
        assertNotNull(memberRepository);
    }

    @Test
    @Sql("/insert_member.sql")
    void givenJobs_whenFindByLikeKeyword_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobAddressVo jobAddressVo = JobAddressVo
                .builder()
                .postcode("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .build();
//                new JobAddressVo(
//                "test",
//                "test",
//                "test",
//                "test",
//                "test"
//        );

        JobEntity jobEntity1 = JobEntity
                .builder()
                .id(1L)
                .memberId(memberId)
                .title("test")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobEntity jobEntity2 = JobEntity
                .builder()
                .id(2L)
                .memberId(memberId)
                .title("test")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        jobRepository.save(jobEntity1);
        jobRepository.save(jobEntity2);

        String keyword = "test";
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
    void givenJob_whenFindById_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobEntity jobEntity = JobEntity
                .builder()
                .id(5L)
                .memberId(memberId)
                .title("test")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        JobEntity saveJobEntity = jobRepository.save(jobEntity);

        // When
        JobEntity foundJobEntity = jobRepository.findById(saveJobEntity.getId()).orElse(null);

        // Then
        assertNotNull(foundJobEntity);
        assertEquals(saveJobEntity.getTitle(), foundJobEntity.getTitle());
    }

    @Test
    @Sql("/insert_member.sql")
    void givenJob_whenFindByMemberId_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobAddressVo jobAddressVo = JobAddressVo
                .builder()
                .postcode("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .build();
        JobEntity jobEntity1 = JobEntity
                .builder()
                .id(1L)
                .memberId(memberId)
                .title("test1")
                .jobAddressVo(jobAddressVo)
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();
        JobEntity jobEntity2 = JobEntity
                .builder()
                .id(2L)
                .memberId(memberId)
                .title("test2")
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
    void givenJob_whenSaved_thenReturnValid() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        String title = "test";
        JobEntity jobEntity = JobEntity
                .builder()
                .id(1L)
                .title(title)
                .memberId(memberId)
                .isActive(1)
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
    public void givenIdAndMemberId_whenExistsByIdAndMemberId_thenTrue() {
        // Given
        List<MemberEntity> member = memberRepository.findAll();
        Long memberId = member.get(0).getId();
        JobAddressVo jobAddressVo = JobAddressVo
                .builder()
                .postcode("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .build();
        JobEntity jobEntity = JobEntity
                .builder()
                .id(1L)
                .title("test")
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
    public void givenIdAndMemberId_whenExistsByIdAndMemberId_thenFalse() {
        // Given
        Long id = 1L;
        Long memberId = 123L;

        // When
        boolean result = jobRepository.existsByIdAndMemberId(id, memberId);

        // Then
        assertFalse(result);
    }

}
