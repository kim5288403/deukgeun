package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.ProfileEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.ProfileRepositoryImpl;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepositoryImpl profileRepository;
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
    void givenProfile_whenDeleteById_thenReturnValid() {
        // Given
        Long profileId = 123L;
        ProfileEntity profile = ProfileEntity
                .builder()
                .id(profileId)
                .trainerId(trainerId)
                .path("test")
                .build();
        profileRepository.save(profile);

        // When
        profileRepository.deleteById(profileId);
        ProfileEntity foundProfile = profileRepository.findById(profileId).orElse(null);

        // Then
        assertNull(foundProfile);
    }

    @Test
    void givenProfile_whenFindById_thenReturnValid() {
        // Given
        Long profileId = 123L;
        ProfileEntity profile = ProfileEntity
                .builder()
                .id(profileId)
                .trainerId(trainerId)
                .path("test")
                .build();
        profileRepository.save(profile);

        // When
        ProfileEntity foundProfile = profileRepository.findById(profileId).orElse(null);

        // Then
        assertNotNull(foundProfile);
        assertEquals(profile.getPath(), foundProfile.getPath());
    }

    @Test
    void givenProfile_whenFindByTrainerId_thenReturnValid() {
        // Given
        ProfileEntity profile = ProfileEntity
                .builder()
                .id(123L)
                .trainerId(trainerId)
                .path("test")
                .build();
        profileRepository.save(profile);

        // When
        ProfileEntity foundProfile = profileRepository.findByTrainerId(trainerId).orElse(null);

        // Then
        assertNotNull(foundProfile);
        assertEquals(profile.getPath(), foundProfile.getPath());
    }

    @Test
    void givenProfile_whenSaved_thenReturnValid() {
        // Given
        ProfileEntity profile = ProfileEntity
                .builder()
                .id(123L)
                .trainerId(trainerId)
                .path("test")
                .build();

        // When
        ProfileEntity saveProfile = profileRepository.save(profile);

        // Then
        ProfileEntity foundProfile = profileRepository.findById(saveProfile.getId()).orElse(null);
        assertNotNull(foundProfile);
        assertEquals(saveProfile.getPath(), foundProfile.getPath());
    }
}
