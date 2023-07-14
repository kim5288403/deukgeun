package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.Applicant;
import com.example.deukgeun.global.entity.JobPosting;
import com.example.deukgeun.global.entity.MatchInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

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
    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(matchInfoRepository);
        assertNotNull(applicantRepository);
        assertNotNull(jobPostingRepository);
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql", "/insert_applicant.sql", "/insert_match_info.sql"})
    void givenMatchingInformation_whenSave_thenReturnValid() {
        // Given
        List<JobPosting> jobPosting = jobPostingRepository.findAll();
        Long jobPostingId = jobPosting.get(0).getId();
        Applicant applicant = Applicant
                .builder()
                .trainerId(123L)
                .jobPostingId(jobPostingId)
                .isSelected(0)
                .supportAmount(3000)
                .build();
        Applicant saveApplicant = applicantRepository.save(applicant);

        MatchInfo matchingInformation = MatchInfo
                .builder()
                .jobPostingId(jobPostingId)
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
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql", "/insert_applicant.sql", "/insert_match_info.sql"})
    void givenExistingApplicantId_whenDeleteByApplicantId_thenReturnValid() {
        // Given
        List<JobPosting> jobPosting = jobPostingRepository.findAll();
        Long jobPostingId = jobPosting.get(0).getId();
        MatchInfo matchInformation = matchInfoRepository.findByJobPostingId(jobPostingId);

        // When
        matchInfoRepository.deleteByApplicantId(matchInformation.getApplicantId());

        // Then
        MatchInfo foundMatchInfo = matchInfoRepository.findById(matchInformation.getId()).orElse(null);
        assertNull(foundMatchInfo);
    }

    @Test
    @Sql({"/insert_member.sql", "/insert_jobPosting.sql", "/insert_applicant.sql", "/insert_match_info.sql"})
    void givenMatchingInformation_whenFindByJobPostingId_thenReturnValid() {
        // Given
        List<JobPosting> jobPosting = jobPostingRepository.findAll();
        Long jobPostingId = jobPosting.get(0).getId();

        // When
        MatchInfo foundMatchInfo = matchInfoRepository.findByJobPostingId(jobPostingId);

        // Then
        assertNotNull(foundMatchInfo);
        assertEquals(jobPostingId, foundMatchInfo.getJobPostingId());
    }
}
