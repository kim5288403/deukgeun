package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.entity.MatchInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MatchInfoRepositoryTest {

    @Autowired
    private MatchInfoRepository matchInfoRepository;
    @Autowired
    private ApplicantRepository applicantRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(matchInfoRepository);
        assertNotNull(applicantRepository);
    }

    @Test
    void givenMatchingInformation_whenSave_thenReturnValid() {
        // Given
        Applicant applicant = Applicant
                .builder()
                .trainerId(123L)
                .jobPostingId(123L)
                .isSelected(0)
                .supportAmount(3000)
                .build();
        Applicant saveApplicant = applicantRepository.save(applicant);

        MatchInfo matchingInformation = MatchInfo
                .builder()
                .jobPostingId(123L)
                .applicantId(saveApplicant.getId())
                .status(1)
                .build();

        // When
        MatchInfo saveMatchInfo = matchInfoRepository.save(matchingInformation);

        // Then
        MatchInfo foundMatchInfo = matchInfoRepository.findById(saveMatchInfo.getId()).orElse(null);
        assertNotNull(foundMatchInfo);
        assertEquals(saveMatchInfo.getApplicantId(), foundMatchInfo.getApplicantId());
    }

    @Test
    @Sql({"/insert_applicant.sql", "/insert_match_info.sql"})
    void givenExistingApplicantId_whenDeleteByApplicantId_thenReturnValid() {
        // Given
        MatchInfo matchingInformation = matchInfoRepository.findByJobPostingId(123L);

        // When
        matchInfoRepository.deleteByApplicantId(matchingInformation.getApplicantId());

        // Then
        MatchInfo foundMatchInfo = matchInfoRepository.findById(matchingInformation.getId()).orElse(null);
        assertNull(foundMatchInfo);
    }

    @Test
    @Sql({"/insert_applicant.sql", "/insert_match_info.sql"})
    void givenMatchingInformation_whenFindByJobPostingId_thenReturnValid() {
        // Given
        Long jobPostingId = 123L;

        // When
        MatchInfo foundMatchInfo = matchInfoRepository.findByJobPostingId(jobPostingId);

        // Then
        assertNotNull(foundMatchInfo);
        assertEquals(jobPostingId, foundMatchInfo.getJobPostingId());
    }
}
