package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.Trainer;
import org.junit.jupiter.api.BeforeEach;
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
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    private long trainerId;

    @BeforeEach
    void setUp() {
        Trainer trainer = Trainer
                .builder()
                .name("테스트")
                .email("testEmail@test.com")
                .password("test1234!")
                .gender(Gender.F)
                .groupStatus(GroupStatus.Y)
                .groupName("test")
                .introduction("test")
                .detailAddress("test")
                .extraAddress("test")
                .jibunAddress("test")
                .roadAddress("test")
                .postcode("test")
                .price(3000)
                .build();

        Trainer saveTrainer = trainerRepository.save(trainer);
        trainerId = saveTrainer.getId();
    }

    @Test
    void shouldNotNullRepository() {
        assertNotNull(profileRepository);
    }

    @Test
    void givenProfile_whenSaved_thenReturnValid() {
        // Given
        Profile profile = Profile
                .builder()
                .trainerId(trainerId)
                .path("test")
                .build();

        // When
        Profile saveProfile = profileRepository.save(profile);

        // Then
        Profile foundProfile = profileRepository.findById(saveProfile.getId()).orElse(null);
        assertNotNull(foundProfile);
        assertEquals(saveProfile.getPath(), foundProfile.getPath());
    }

    @Test
    void givenProfile_whenFindByTrainerId_thenReturnValid() {
        // Given
        Profile profile = Profile
                .builder()
                .trainerId(trainerId)
                .path("test")
                .build();
        profileRepository.save(profile);

        // When
        Profile foundProfile = profileRepository.findByTrainerId(trainerId).orElse(null);

        // Then
        assertNotNull(foundProfile);
        assertEquals(profile.getPath(), foundProfile.getPath());
    }

    @Test
    void givenProfile_whenUpdatePath_thenIsUpdated() {
        // Given
        String newPath = "newPath";
        Profile profile = Profile
                .builder()
                .trainerId(trainerId)
                .path("test")
                .build();
        Profile saveProfile = profileRepository.save(profile);
        Profile foundProfile = profileRepository.findById(saveProfile.getId()).orElse(null);

        // When
        assert foundProfile != null;
        foundProfile.updatePath(newPath);

        // Then
        assertEquals(newPath, foundProfile.getPath());
    }


}
