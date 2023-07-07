package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.entity.GroupStatus;
import com.example.deukgeun.global.entity.Trainer;
import com.example.deukgeun.global.repository.TrainerRepository;
import com.example.deukgeun.trainer.request.UpdateInfoRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrainerRepositoryTest {
    @Autowired
    private TrainerRepository trainerRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(trainerRepository);
    }

    @Test
    void givenTrainer_whenSaved_thenReturnValid() {
        // Given
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

        // When
        Trainer saveTrainer = trainerRepository.save(trainer);

        // Then
        Trainer foundTrainer = trainerRepository.findById(saveTrainer.getId()).orElse(null);
        assertNotNull(foundTrainer);
        assertEquals(saveTrainer.getEmail(), foundTrainer.getEmail());
        assertEquals(saveTrainer.getName(), foundTrainer.getName());
    }

    @Test
    void givenTrainer_whenFindByEmail_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        Trainer trainer = Trainer
                .builder()
                .name("테스트")
                .email(email)
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
        trainerRepository.save(trainer);

        // When
        Optional<Trainer> foundTrainer = trainerRepository.findByEmail(email);

        // Then
        assertTrue(foundTrainer.isPresent());
        assertEquals(email, foundTrainer.get().getEmail());
    }

    @Test
    void givenTrainer_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "testEmail@test.com";
        Trainer trainer = Trainer.builder()
                .name("테스트")
                .email(email)
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
        trainerRepository.save(trainer);

        // When
        boolean existsByEmail = trainerRepository.existsByEmail(email);

        // Then
        assertTrue(existsByEmail);
    }

    @Test
    void givenTrainer_whenUpdateInfo_thenIsUpdated() {
        // Given
        String email = "testEmail@test.com";
        Trainer trainer = Trainer
                .builder()
                .name("테스트")
                .email(email)
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
        trainerRepository.save(trainer);
        Trainer foundTrainer = trainerRepository.findByEmail(email).orElse(null);

        UpdateInfoRequest updateInfoRequest = new UpdateInfoRequest();
        updateInfoRequest.setEmail("updateTestEmail@test.com");
        updateInfoRequest.setName("test");
        updateInfoRequest.setGender(Gender.F);
        updateInfoRequest.setPostcode("test");
        updateInfoRequest.setJibunAddress("test");
        updateInfoRequest.setRoadAddress("test");
        updateInfoRequest.setDetailAddress("test");
        updateInfoRequest.setExtraAddress("test");
        updateInfoRequest.setPrice(10000);
        updateInfoRequest.setGroupStatus(GroupStatus.Y);
        updateInfoRequest.setGroupName("test");
        updateInfoRequest.setIntroduction("test");

        // When
        assert foundTrainer != null;
        foundTrainer.updateInfo(updateInfoRequest);
        trainerRepository.save(foundTrainer);

        // Then
        Trainer resultTrainer = trainerRepository.findById(foundTrainer.getId()).orElse(null);
        assertNotNull(resultTrainer);
        assertNotEquals(email, resultTrainer.getEmail());
    }

    @Test
    void givenTrainer_whenUpdatePassword_thenIsUpdated() {
        // Given
        String email = "testEmail@test.com";
        String password = "testPassword";
        Trainer trainer = Trainer
                .builder()
                .name("테스트")
                .email(email)
                .password(password)
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
        trainerRepository.save(trainer);
        Trainer foundTrainer = trainerRepository.findByEmail(email).orElse(null);

        // When
        assert foundTrainer != null;
        foundTrainer.updatePassword("newPasswordTest");
        trainerRepository.save(foundTrainer);

        // Then
        Trainer resultTrainer = trainerRepository.findById(foundTrainer.getId()).orElse(null);
        assertNotNull(resultTrainer);
        assertNotEquals(password, resultTrainer.getPassword());
    }

}
