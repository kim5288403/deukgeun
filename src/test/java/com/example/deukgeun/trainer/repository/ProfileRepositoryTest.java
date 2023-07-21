package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.Profile;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.ProfileRepository;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerRepositoryImpl;
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
    private TrainerRepositoryImpl trainerRepositoryImpl;

    private long trainerId;

    @BeforeEach
    void setUp() {
        TrainerEntity trainer = TrainerEntity
                .builder()
                .id(123L)
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

        TrainerEntity saveTrainer = trainerRepositoryImpl.save(trainer);
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
