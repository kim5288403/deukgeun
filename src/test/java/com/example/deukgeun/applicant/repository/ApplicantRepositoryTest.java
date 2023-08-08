package com.example.deukgeun.applicant.repository;

import com.example.deukgeun.applicant.infrastructure.persistence.model.entity.ApplicantEntity;
import com.example.deukgeun.applicant.infrastructure.persistence.repository.ApplicantJpaRepository;
import com.example.deukgeun.job.infrastructure.persistence.model.entity.JobPostingEntity;
import com.example.deukgeun.job.infrastructure.persistence.repository.JobPostingJpaRepository;
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
    private JobPostingJpaRepository jobPostingRepository;


    @Test
    void shouldNotNullRepository() {
        assertNotNull(applicantJpaRepository);
        assertNotNull(jobPostingRepository);
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql"})
    void givenApplicant_whenSave_thenReturnValid() {
        // Given
        List<JobPostingEntity> jobPostingEntity = jobPostingRepository.findAll();
        ApplicantEntity applicantEntity = ApplicantEntity
                .builder()
                .jobPostingId(jobPostingEntity.get(0).getId())
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
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql"})
    void givenApplicant_whenFindByJobPostingId_thenReturnValid() {
        // Given
        List<JobPostingEntity> jobPostingEntity = jobPostingRepository.findAll();
        Long jobPostingId = jobPostingEntity.get(0).getId();
        ApplicantEntity applicantEntity1 = ApplicantEntity
                .builder()
                .jobPostingId(jobPostingId)
                .trainerId(123L)
                .supportAmount(30000)
                .build();
        ApplicantEntity applicantEntity2 = ApplicantEntity
                .builder()
                .jobPostingId(jobPostingId)
                .trainerId(123L)
                .supportAmount(30000)
                .build();

        applicantJpaRepository.save(applicantEntity1);
        applicantJpaRepository.save(applicantEntity2);

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<ApplicantEntity> result = applicantJpaRepository.findPageByJobPostingId(jobPostingId, pageable);

        // Then
        assertEquals(jobPostingId, result.getContent().get(0).getJobPostingId());
        assertEquals(jobPostingId, result.getContent().get(1).getJobPostingId());
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql"})
    public void givenJobPostingIdAndTrainerId_whenExists_thenReturnTrue() {
        // Given
        Long jobPostingId = 123L;
        Long trainerId = 1L;
        ApplicantEntity applicant = ApplicantEntity
                .builder()
                .trainerId(trainerId)
                .jobPostingId(jobPostingId)
                .supportAmount(30000)
                .build();

        applicantJpaRepository.save(applicant);

        // When
        boolean exists = applicantJpaRepository.existsByJobPostingIdAndTrainerId(jobPostingId, trainerId);

        // Then
        assertTrue(exists);
    }

    @Test
    public void givenJobPostingIdAndTrainerId_whenNotExists_thenReturnFalse() {
        // Given
        Long jobPostingId = 1L;
        Long trainerId = 1L;

        // When
        boolean exists = applicantJpaRepository.existsByJobPostingIdAndTrainerId(jobPostingId, trainerId);

        // Then
        assertFalse(exists);
    }

}
