package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.member.response.ApplicantResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApplicantRepositoryTest {

    @Autowired
    private ApplicantRepository applicantRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(applicantRepository);
    }

    @Test
    void givenApplicant_whenSave_thenReturnValid() {
        // Given
        Applicant applicant = Applicant
                .builder()
                .jobPostingId(123L)
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
    void givenApplicant_whenFindByJobPostingId_thenReturnValid() {
        // Given
        Long jobPostingId = 123L;
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
