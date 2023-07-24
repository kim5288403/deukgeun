package com.example.deukgeun.trainer.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.PostEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.entity.TrainerEntity;
import com.example.deukgeun.trainer.infrastructure.persistence.repository.PostRepositoryImpl;
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
public class PostRepositoryTest {

    @Autowired
    private PostRepositoryImpl postRepository;
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
        assertNotNull(postRepository);
    }

    @Test
    public void givenExistingPostId_whenDeleteById_thenPostIsDeleted() {
        // Given
        Long existingPostId = 123L;
        PostEntity post = PostEntity
                .builder()
                .id(existingPostId)
                .html("test")
                .trainerId(trainerId)
                .build();

        postRepository.save(post);

        // When
        postRepository.deleteById(existingPostId);
        PostEntity result = postRepository.findById(existingPostId).orElse(null);

        // Then
        assertNull(result);
    }

    @Test
    public void givenExistingTrainerId_whenExistsByTrainerId_thenReturnTrue() {
        // Given
        PostEntity post = PostEntity
                .builder()
                .id(123L)
                .html("test")
                .trainerId(trainerId)
                .build();

        postRepository.save(post);

        // When
        boolean result = postRepository.existsByTrainerId(trainerId);

        // Then
        assertTrue(result);
    }

    @Test
    public void givenNonExistingTrainerId_whenExistsByTrainerId_thenReturnFalse() {
        // Given
        Long nonExistingTrainerId = 456L;

        // When
        boolean result = postRepository.existsByTrainerId(nonExistingTrainerId);

        // Then
        assertFalse(result);
    }

    @Test
    void givenPost_whenSaved_thenReturnValid() {
        // Given
        PostEntity post = PostEntity
                .builder()
                .id(123L)
                .html("test")
                .trainerId(trainerId)
                .build();

        // When
        PostEntity savePost = postRepository.save(post);

        // Then
        PostEntity foundPost = postRepository.findById(savePost.getId()).orElse(null);
        assertNotNull(foundPost);
        assertEquals(savePost.getHtml(), foundPost.getHtml());
    }

    @Test
    void givenPost_whenFindByTrainerId_thenReturnValid() {
        // Given
        PostEntity post = PostEntity
                .builder()
                .id(123L)
                .html("test")
                .trainerId(trainerId)
                .build();
        postRepository.save(post);

        // When
        PostEntity foundPost = postRepository.findByTrainerId(trainerId).orElse(null);

        // Then
        assertNotNull(foundPost);
        assertEquals(post.getHtml(), foundPost.getHtml());
    }

}
