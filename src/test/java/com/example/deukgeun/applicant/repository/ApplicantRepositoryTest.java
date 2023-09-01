package com.example.deukgeun.applicant.repository;

import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.ApplicantJpaRepository;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobEntity;
import com.example.deukgeun.job.infrastructure.persistence.repository.JobJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApplicantRepositoryTest {
    @Autowired
    private ApplicantJpaRepository applicantJpaRepository;
    @Autowired
    private JobJpaRepository jobRepository;


    @Test
    void shouldNotNullRepository() {
        assertNotNull(applicantJpaRepository);
        assertNotNull(jobRepository);
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_job.sql"})
    void givenApplicant_whenSave_thenReturnValid() {
        // Given
        List<JobEntity> jobEntity = jobRepository.findAll();
        ApplicantEntity applicantEntity = ApplicantEntity
                .builder()
                .jobId(jobEntity.get(0).getId())
                .trainerId(123L)
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
    void givenApplicant_whenFindByJobId_thenReturnValid() {
        // Given
        List<JobEntity> jobEntity = jobRepository.findAll();
        Long jobId = jobEntity.get(0).getId();
        ApplicantEntity applicantEntity1 = ApplicantEntity
                .builder()
                .jobId(jobId)
                .trainerId(123L)
                .supportAmount(30000)
                .build();
        ApplicantEntity applicantEntity2 = ApplicantEntity
                .builder()
                .jobId(jobId)
                .trainerId(123L)
                .supportAmount(30000)
                .build();

        applicantJpaRepository.save(applicantEntity1);
        applicantJpaRepository.save(applicantEntity2);

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<ApplicantEntity> result = applicantJpaRepository.findPageByJobId(jobId, pageable);

        // Then
        assertEquals(jobId, result.getContent().get(0).getJobId());
        assertEquals(jobId, result.getContent().get(1).getJobId());
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_job.sql"})
    public void givenJobIdAndTrainerId_whenExists_thenReturnTrue() {
        // Given
        Long jobId = 123L;
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
    public void givenJobIdAndTrainerId_whenNotExists_thenReturnFalse() {
        // Given
        Long jobId = 1L;
        Long trainerId = 1L;

        // When
        boolean exists = applicantJpaRepository.existsByJobIdAndTrainerId(jobId, trainerId);

        // Then
        assertFalse(exists);
    }

}
