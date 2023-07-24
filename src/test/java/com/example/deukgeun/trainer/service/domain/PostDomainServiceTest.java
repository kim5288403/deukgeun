package com.example.deukgeun.trainer.service.domain;

import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.repository.PostRepository;
import com.example.deukgeun.trainer.domain.service.implement.PostDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PostDomainServiceTest {
    @InjectMocks
    private PostDomainServiceImpl postDomainService;
    @Mock
    private PostRepository postRepository;

    @Test
    public void givenExistingPostId_whenDeleteById_thenPostIsDeleted() {
        // Given
        Long existingPostId = 123L;

        // When
        postDomainService.deleteById(existingPostId);

        // Then
        verify(postRepository).deleteById(existingPostId);
    }

    @Test
    public void givenExistingTrainerId_whenExistsByTrainerId_thenReturnTrue() {
        // Given
        Long existingTrainerId = 123L;

        given(postRepository.existsByTrainerId(existingTrainerId)).willReturn(true);

        // When
        boolean result = postDomainService.existsByTrainerId(existingTrainerId);

        // Then
        assertTrue(result);
    }

    @Test
    public void givenNonExistingTrainerId_whenExistsByTrainerId_thenReturnFalse() {
        // Given
        Long nonExistingTrainerId = 456L;

        given(postRepository.existsByTrainerId(nonExistingTrainerId)).willReturn(false);

        // When
        boolean result = postDomainService.existsByTrainerId(nonExistingTrainerId);

        // Then
        assertFalse(result);
    }

    @Test
    public void givenValidTrainerId_whenFindByTrainerId_thenReturnPost() {
        // Given
        Long trainerId = 123L;
        Post postFound = new Post(123L, "udateTest", 1234L);

        given(postRepository.findByTrainerId(trainerId)).willReturn(Optional.of(postFound));

        // When
        Post result = postDomainService.findByTrainerId(trainerId);

        // Then
        assertEquals(postFound, result);
    }

    @Test
    public void givenInvalidTrainerId_whenFindByTrainerId_thenThrowEntityNotFoundException() {
        // Given
        Long trainerId = 456L;

        given(postRepository.findByTrainerId(trainerId)).willReturn(Optional.empty());

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> postDomainService.findByTrainerId(trainerId));
    }

    @Test
    public void givenValidTrainerIdAndHtml_whenSave_thenPostIsSaved() {
        // Given
        Long trainerId = 123L;
        String html = "<html><body>Test post content</body></html>";
        Post postToSave = Post.create(html, trainerId);

        given(postRepository.save(any(Post.class))).willReturn(postToSave);

        // When
        Post result = postDomainService.save(html, trainerId);

        // Then
        assertEquals(postToSave, result);
    }

    @Test
    public void givenValidPost_whenUpdate_thenPostIsSaved() {
        // Given
        Post postToUpdate = new Post(123L, "test", 1234L);
        Post updatedPost = new Post(123L, "updateTest", 1234L);

        given(postRepository.save(postToUpdate)).willReturn(updatedPost);

        // When
        Post result = postDomainService.update(postToUpdate);

        // Then
        assertEquals(updatedPost, result);
    }
}
