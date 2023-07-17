package com.example.deukgeun.job.repository;

import com.example.deukgeun.job.domain.entity.Applicant;
import com.example.deukgeun.job.domain.entity.JobPosting;
import com.example.deukgeun.job.application.dto.response.ApplicantResponse;
import com.example.deukgeun.job.domain.repository.ApplicantRepository;
import com.example.deukgeun.job.domain.repository.JobPostingRepository;
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
    private ApplicantRepository applicantRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;


    @Test
    void shouldNotNullRepository() {
        assertNotNull(applicantRepository);
        assertNotNull(jobPostingRepository);
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql"})
    void givenApplicant_whenSave_thenReturnValid() {
        // Given
        List<JobPosting> jobPosting = jobPostingRepository.findAll();
        Applicant applicant = Applicant
                .builder()
                .jobPostingId(jobPosting.get(0).getId())
                .trainerId(123L)
                .supportAmount(30000)
                .build();

        // When
        Applicant saveApplicant = applicantRepository.save(applicant);

        // Then
        Applicant foundApplicant = applicantRepository.findById(saveApplicant.getId()).orElse(null);
        assertNotNull(foundApplicant);
        assertEquals(saveApplicant.getSupportAmount(), foundApplicant.getSupportAmount());
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql"})
    void givenApplicant_whenFindByJobPostingId_thenReturnValid() {
        // Given
        List<JobPosting> jobPosting = jobPostingRepository.findAll();
        Long jobPostingId = jobPosting.get(0).getId();
        Applicant applicant1 = Applicant
                .builder()
                .jobPostingId(jobPostingId)
                .trainerId(123L)
                .supportAmount(30000)
                .build();
        Applicant applicant2 = Applicant
                .builder()
                .jobPostingId(jobPostingId)
                .trainerId(123L)
                .supportAmount(30000)
                .build();

        applicantRepository.save(applicant1);
        applicantRepository.save(applicant2);

        PageRequest pageable = PageRequest.of(0, 10);

        // When
        Page<ApplicantResponse.ListResponse> result = applicantRepository.findByJobPostingId(jobPostingId, pageable);

        // Then
        assertEquals(jobPostingId, result.getContent().get(0).getJobPostingId());
        assertEquals(jobPostingId, result.getContent().get(1).getJobPostingId());
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql"})
    public void givenJobPostingIdAndTrainerId_whenExists_thenReturnTrue() {
        // Given
        Long jobPostingId = 1L;
        Long trainerId = 1L;
        Applicant applicant = Applicant
                .builder()
                .trainerId(trainerId)
                .jobPostingId(jobPostingId)
                .supportAmount(30000)
                .build();

        applicantRepository.save(applicant);

        // When
        boolean exists = applicantRepository.existsByJobPostingIdAndTrainerId(jobPostingId, trainerId);

        // Then
        assertTrue(exists);
    }

    @Test
    public void givenJobPostingIdAndTrainerId_whenNotExists_thenReturnFalse() {
        // Given
        Long jobPostingId = 1L;
        Long trainerId = 1L;

        // When
        boolean exists = applicantRepository.existsByJobPostingIdAndTrainerId(jobPostingId, trainerId);

        // Then
        assertFalse(exists);
    }

}
