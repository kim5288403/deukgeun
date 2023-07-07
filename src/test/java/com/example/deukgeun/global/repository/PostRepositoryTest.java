package com.example.deukgeun.global.repository;

import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.entity.GroupStatus;
import com.example.deukgeun.global.entity.Trainer;
import com.example.deukgeun.global.entity.Post;
import com.example.deukgeun.global.repository.PostRepository;
import com.example.deukgeun.global.repository.TrainerRepository;
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
    private PostRepository postRepository;
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
        assertNotNull(postRepository);
    }

    @Test
    void givenPost_whenSaved_thenReturnValid() {
        // Given
        Post post = Post
                .builder()
                .html("test")
                .trainerId(trainerId)
                .build();

        // When
        Post savePost = postRepository.save(post);

        // Then
        Post foundPost = postRepository.findById(savePost.getId()).orElse(null);
        assertNotNull(foundPost);
        assertEquals(savePost.getHtml(), foundPost.getHtml());
    }

    @Test
    void givenPost_whenFindByTrainerId_thenReturnValid() {
        // Given
        Post post = Post
                .builder()
                .html("test")
                .trainerId(trainerId)
                .build();
        postRepository.save(post);

        // When
        Post foundPost = postRepository.findByTrainerId(trainerId).orElse(null);

        // Then
        assertNotNull(foundPost);
        assertEquals(post.getHtml(), foundPost.getHtml());
    }

    @Test
    void givenPost_whenUpdateHtml_thenIsUpdated() {
        // Given
        String html = "html";
        String newHtml = "newHtml";

        Post post = Post
                .builder()
                .html(html)
                .trainerId(trainerId)
                .build();

        postRepository.save(post);
        Post foundPost = postRepository.findByTrainerId(trainerId).orElse(null);

        // When
        assert foundPost != null;
        foundPost.updateHtml(newHtml);
        postRepository.save(foundPost);

        // Then
        assertNotNull(foundPost);
        assertEquals(newHtml, foundPost.getHtml());
        assertNotEquals(html, foundPost.getHtml());
    }

}
