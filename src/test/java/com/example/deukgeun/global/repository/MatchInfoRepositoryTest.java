package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.entity.MatchInfo;
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
public class MatchInfoRepositoryTest {

    @Autowired
    private MatchInfoRepository matchInfoRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(matchInfoRepository);
    }

    @Test
    void givenMatchingInformation_whenSave_thenReturnValid() {
        // Given
        MatchInfo matchingInformation = MatchInfo
                .builder()
                .jobPostingId(123L)
                .applicantId(123L)
                .status(1)
                .build();

        // When
        MatchInfo saveMatchInfo = matchInfoRepository.save(matchingInformation);

        // Then
        MatchInfo foundMatchInfo = matchInfoRepository.findById(saveMatchInfo.getId()).orElse(null);
        assertNotNull(foundMatchInfo);
        assertEquals(saveMatchInfo.getApplicantId(), foundMatchInfo.getApplicantId());
    }
}
