package com.example.deukgeun.applicant.repository;

import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.ApplicantJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApplicantRepositoryTest {
    @Autowired
    private ApplicantJpaRepository applicantJpaRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(applicantJpaRepository);
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_job.sql"})
    void givenValidApplicant_whenSave_thenApplicantIsSaved() {
        // Given
        ApplicantEntity applicantEntity = ApplicantEntity
                .builder()
                .jobId(1L)
                .trainerId(1L)
                .supportAmount(30000)
                .build();

        // When
        ApplicantEntity saveApplicantEntity = applicantJpaRepository.save(applicantEntity);

        // Then
        ApplicantEntity foundApplicant = applicantJpaRepository.findById(saveApplicantEntity.getId()).orElse(null);
        assertNotNull(foundApplicant);
        assertEquals(saveApplicantEntity.getSupportAmount(), foundApplicant.getSupportAmount());
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_job.sql"})
    void givenValidApplicantWithJobId_whenFindByJobId_thenApplicantIsFound() {
        // Given
        ApplicantEntity applicantEntity1 = ApplicantEntity
                .builder()
                .jobId(1L)
                .trainerId(1L)
                .supportAmount(30000)
                .build();
        ApplicantEntity applicantEntity2 = ApplicantEntity
                .builder()
                .jobId(1L)
                .trainerId(1L)
                .supportAmount(30000)
                .build();

        applicantJpaRepository.save(applicantEntity1);
        applicantJpaRepository.save(applicantEntity2);

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<ApplicantEntity> result = applicantJpaRepository.findPageByJobId(1L, pageable);

        // Then
        assertEquals(1L, result.getContent().get(0).getJobId());
        assertEquals(1L, result.getContent().get(1).getJobId());
        assertEquals(2, result.getContent().size());
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_job.sql"})
    public void givenValidJobIdAndTrainerId_whenExists_thenReturnTrue() {
        // Given
        Long jobId = 1L;
        Long trainerId = 1L;
        ApplicantEntity applicant = ApplicantEntity
                .builder()
                .trainerId(trainerId)
                .jobId(jobId)
                .supportAmount(30000)
                .build();

        applicantJpaRepository.save(applicant);

        // When
        boolean exists = applicantJpaRepository.existsByJobIdAndTrainerId(jobId, trainerId);

        // Then
        assertTrue(exists);
    }

    @Test
    public void givenValidJobIdAndTrainerId_whenNotExists_thenReturnFalse() {
        // Given
        Long jobId = 1L;
        Long trainerId = 1L;

        // When
        boolean exists = applicantJpaRepository.existsByJobIdAndTrainerId(jobId, trainerId);

        // Then
        assertFalse(exists);
    }

}
