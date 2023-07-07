package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.repository.ApplicantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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

}
