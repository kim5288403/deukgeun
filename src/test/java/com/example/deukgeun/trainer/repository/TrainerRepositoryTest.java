package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
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
    void givenValidTrainer_whenDeleteById_thenTrainerDeleted() {
        // Given
        String email = "testEmail@test.com";
        GroupVo groupVo = new GroupVo(GroupStatus.Y, "test");
        AddressVo addressVo = new AddressVo("postCode", "jibunAddress", "roadAddress", "detailAddress", "extraAddress");
        TrainerEntity trainer = TrainerEntity.builder()
                .id(1L)
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .introduction("test")
                .groupVo(groupVo)
                .addressVo(addressVo)
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
    void givenValidTrainerAndEmail_whenExistsByEmail_thenReturnTrue() {
        // Given
        String email = "testEmail@test.com";
        GroupVo groupVo = new GroupVo(GroupStatus.Y, "test");
        AddressVo addressVo = new AddressVo("postCode", "jibunAddress", "roadAddress", "detailAddress", "extraAddress");
        TrainerEntity trainer = TrainerEntity.builder()
                .id(1L)
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .introduction("test")
                .groupVo(groupVo)
                .addressVo(addressVo)
                .price(3000)
                .build();

        trainerJpaRepository.save(trainer);

        // When
        boolean existsByEmail = trainerJpaRepository.existsByEmail(email);

        // Then
        assertTrue(existsByEmail);
    }

    @Test
    void givenValidEmail_whenFindByEmail_thenTrainerIsFound() {
        // Given
        String email = "testEmail@test.com";
        GroupVo groupVo = new GroupVo(GroupStatus.Y, "test");
        AddressVo addressVo = new AddressVo("postCode", "jibunAddress", "roadAddress", "detailAddress", "extraAddress");
        TrainerEntity trainer = TrainerEntity.builder()
                .id(123L)
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .introduction("test")
                .groupVo(groupVo)
                .addressVo(addressVo)
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
    void givenValidTrainerEntity_whenSave_thenTrainerIsSaved() {
        // Given
        String email = "testEmail@test.com";
        GroupVo groupVo = new GroupVo(GroupStatus.Y, "test");
        AddressVo addressVo = new AddressVo("postCode", "jibunAddress", "roadAddress", "detailAddress", "extraAddress");
        TrainerEntity trainer = TrainerEntity.builder()
                .id(1L)
                .name("테스트")
                .email(email)
                .password("test1234!")
                .gender(Gender.F)
                .introduction("test")
                .groupVo(groupVo)
                .addressVo(addressVo)
                .price(3000)
                .build();

        // When
        TrainerEntity saveTrainer = trainerJpaRepository.save(trainer);

        // Then
        TrainerEntity foundTrainer = trainerJpaRepository.findById(saveTrainer.getId()).orElse(null);
        assertNotNull(foundTrainer);
        assertEquals(saveTrainer.getEmail(), foundTrainer.getEmail());
        assertEquals(saveTrainer.getName(), foundTrainer.getName());
    }

}
