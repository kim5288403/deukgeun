package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.LicenseEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.model.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject.AddressVo;
import com.example.deukgeun.trainer.infrastructure.persistence.model.valueobject.GroupVo;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.TrainerJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TrainerRepositoryTest {
    @Autowired
    private TrainerJpaRepository trainerJpaRepository;

    @Test
    void shouldNotNullRepository() {
        assertNotNull(trainerJpaRepository);
    }

    @Test
    void givenTrainer_whenDeleteById_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        TrainerEntity trainer = TrainerEntity.builder()
                .id(123L)
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .introduction("test")
                .groupVo(
                        new GroupVo(
                                GroupStatus.Y,
                                "test"
                        )
                )
                .addressVo(new AddressVo(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ))
                .price(3000)
                .build();

        TrainerEntity saveTrainer = trainerJpaRepository.save(trainer);

        // When
        trainerJpaRepository.deleteById(saveTrainer.getId());
        TrainerEntity result = trainerJpaRepository.findById(saveTrainer.getId()).orElse(null);

        // Then
        assertNull(result);
    }

    @Test
    void givenTrainer_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "testEmail@test.com";
        TrainerEntity trainer = TrainerEntity.builder()
                .id(123L)
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .introduction("test")
                .groupVo(
                        new GroupVo(
                                GroupStatus.Y,
                                "test"
                        )
                )
                .addressVo(new AddressVo(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ))
                .price(3000)
                .build();
        trainerJpaRepository.save(trainer);

        // When
        boolean existsByEmail = trainerJpaRepository.existsByEmail(email);

        // Then
        assertTrue(existsByEmail);
    }

    @Test
    void givenTrainer_whenFindByEmail_thenReturnValid() {
        // Given
        String email = "testEmail@test.com";
        TrainerEntity trainer = TrainerEntity
                .builder()
                .id(123L)
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .groupVo(
                        new GroupVo(
                                GroupStatus.Y,
                                "test"
                        )
                )
                .introduction("test")
                .addressVo(new AddressVo(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ))
                .price(3000)
                .build();
        trainerJpaRepository.save(trainer);

        // When
        Optional<TrainerEntity> foundTrainer = trainerJpaRepository.findByEmail(email);

        // Then
        assertTrue(foundTrainer.isPresent());
        assertEquals(email, foundTrainer.get().getEmail());
    }

    @Test
    void givenTrainer_whenSave_thenReturnValid() {
        // Given
        TrainerEntity trainer = TrainerEntity
                .builder()
                .id(123L)
                .name("테스트")
                .email("testEmail@test.com")
                .password("test1234!")
                .gender(Gender.F)
                .introduction("test")
                .groupVo(
                        new GroupVo(
                                GroupStatus.Y,
                                "test"
                        )
                )
                .addressVo(new AddressVo(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ))
                .price(3000)
                .licenseEntities(new ArrayList<>())
                .build();

        LicenseEntity licenseEntity = LicenseEntity
                .builder()
                .id(123435L)
                .trainerId(123L)
                .licenseNumber("123145")
                .certificateName("test")
                .build();

        trainer.getLicenseEntities().add(licenseEntity);

        // When
        TrainerEntity saveTrainer = trainerJpaRepository.save(trainer);

        // Then
        TrainerEntity foundTrainer = trainerJpaRepository.findById(saveTrainer.getId()).orElse(null);
        assertNotNull(foundTrainer);
        assertEquals(saveTrainer.getEmail(), foundTrainer.getEmail());
        assertEquals(saveTrainer.getName(), foundTrainer.getName());
    }

}
