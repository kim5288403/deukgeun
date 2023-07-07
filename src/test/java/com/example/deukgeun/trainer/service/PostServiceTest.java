package com.example.deukgeun.trainer.service;

import com.example.deukgeun.global.entity.Post;
import com.example.deukgeun.global.entity.Trainer;
import com.example.deukgeun.global.repository.PostRepository;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostServiceTest {
    @InjectMocks
    private PostServiceImpl postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private TrainerServiceImpl trainerService;
    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private Part filePart;


    @Test
    public void givenValidRequestAndAuthToken_whenUpload_thenPostIsSaved() throws Exception {
        // Given
        PostRequest request = new PostRequest();
        request.setContent("Sample content");

        String authToken = "validAuthToken";
        Trainer trainer = Trainer
                .builder()
                .id(123L)
                .email("johndoe@example.com")
                .build();

        given(trainerService.getByAuthToken(anyString())).willReturn(trainer);
        given(postRepository.existsByTrainerId(trainer.getId())).willReturn(false);

        // When
        postService.upload(request, authToken);

        // Verify
        verify(trainerService, times(1)).getByAuthToken(authToken);
        verify(postRepository, times(1)).existsByTrainerId(trainer.getId());
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void givenValidRequestAndAuthToken_whenUpload_thenPostIsUpdated() throws Exception {
        // Given
        PostRequest request = new PostRequest();
        request.setContent("Sample content");

        String authToken = "validAuthToken";
        Trainer trainer = Trainer
                .builder()
                .id(123L)
                .email("johndoe@example.com")
                .build();

        Post existingPost = new Post();

        given(trainerService.getByAuthToken(authToken)).willReturn(trainer);
        given(postRepository.existsByTrainerId(trainer.getId())).willReturn(true);
        given(postRepository.findByTrainerId(trainer.getId())).willReturn(Optional.of(existingPost));

        // When
        postService.upload(request, authToken);

        // Verify
        verify(trainerService, times(1)).getByAuthToken(authToken);
        verify(postRepository, times(1)).existsByTrainerId(trainer.getId());
        verify(postRepository, times(1)).findByTrainerId(trainer.getId());
        verify(postRepository, times(1)).save(existingPost);
    }

    @Test
    public void givenUserIdAndHtml_whenSave_thenPostIsCreatedAndSaved() {
        // Given
        Long userId = 123L;
        String html = "<p>Sample HTML content</p>";

        // When
        postService.save(userId, html);

        // Verify
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    public void givenTrainerIdAndHtml_whenUpdateHtml_thenPostIsUpdatedAndSaved() {
        // Given
        Long trainerId = 123L;
        String html = "<p>Updated HTML content</p>";

        Post foundPost = new Post();

        given(postRepository.findByTrainerId(trainerId)).willReturn(Optional.of(foundPost));

        // When
        postService.updateHtml(trainerId, html);

        // Verify
        verify(postRepository, times(1)).findByTrainerId(trainerId);
        verify(postRepository, times(1)).save(foundPost);
    }

    @Test
    public void givenInvalidTrainerIdAndHtml_whenUpdateHtml_thenEntityNotFoundExceptionIsThrown() {
        // Given
        Long trainerId = 123L;
        String html = "<p>Updated HTML content</p>";

        given(postRepository.findByTrainerId(trainerId)).willThrow(EntityNotFoundException.class);

        // When
        assertThrows(EntityNotFoundException.class, () -> postService.updateHtml(trainerId, html));

        // Verify
        verify(postRepository, times(1)).findByTrainerId(trainerId);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    public void givenValidRequestAndResponse_whenSaveImage_thenImageIsSavedAndLinkIsReturned() throws Exception {
        // Given
        String postFilePath = "/path/to/uploads";
        String postUrl = "https://example.com/images/";
        String contentType = "image/jpeg";
        PrintWriter writer = mock(PrintWriter.class);

        given(request.getPart("file")).willReturn(filePart);
        given(request.getContentType()).willReturn("multipart/form-data");
        given(filePart.getContentType()).willReturn(contentType);
        given(filePart.getInputStream()).willReturn(null);
        given(response.getWriter()).willReturn(writer);

        ReflectionTestUtils.setField(postService, "postFilePath", postFilePath);
        ReflectionTestUtils.setField(postService, "postUrl", postUrl);

        // When
        Map<Object, Object> responseData = postService.saveImage(request, response);

        // Then
        assertNotNull(responseData.get("link"));

        // Verify
        verify(request, times(1)).getPart("file");
        verify(filePart, times(1)).getContentType();
        verify(response, times(1)).getWriter();
        verify(response.getWriter(), times(1)).println(anyString());
    }

    @Test
    public void givenFilePart_whenSaveServerImage_thenFileCopied() throws Exception {
        // Given
        Part filePart = mock(Part.class);
        InputStream inputStream = new ByteArrayInputStream("Test data".getBytes());
        File file = new File("test.png");
        PrintWriter writer = mock(PrintWriter.class);
        mockStatic(Files.class);

        given(filePart.getInputStream()).willReturn(inputStream);

        // When
        postService.saveServerImage(filePart, writer, file);

        // Then
        verify(filePart, times(1)).getInputStream();
        verify(writer, never()).println(anyString());
    }

    @Test
    public void givenValidContentType_whenValidContentType_thenDoesNotThrowException() {
        // Given
        String validContentType = "multipart/form-data";

        // When & Then
        assertDoesNotThrow(() -> postService.validContentType(validContentType));
    }

    @Test
    public void givenNullContentType_whenValidContentType_thenThrowException() {
        // Given
        String nullContentType = null;

        // When/Then
        assertThrows(Exception.class, () -> postService.validContentType(nullContentType));
    }

    @Test
    public void givenValidMimeType_whenValidMimeType_thenDoesNotThrowException() {
        // Given
        String validMimeType = "image/jpeg";
        File uploads = mock(File.class);
        String name = "image.jpg";

        // When & Then
        assertDoesNotThrow(() ->postService.validMimeType(validMimeType, uploads, name));
    }

    @Test
    public void givenInvalidMimeType_whenValidMimeType_thenThrowExceptionAndDeleteFile() {
        // Given
        String invalidMimeType = "application/pdf";
        File uploads = mock(File.class);
        String name = "file.pdf";

        // When/Then
        assertThrows(Exception.class, () -> postService.validMimeType(invalidMimeType, uploads, name));
    }

    @Test
    public void givenRequestURI_whenGetServerImage_thenReturnFile() {
        // Given
        String getRequestURI = "/images/image.jpg";
        String postFilePath = "path/to/posts";
        File file = new File(postFilePath , "/image.jpg");
        ReflectionTestUtils.setField(postService, "postFilePath", postFilePath);

        // When
        File result = postService.getServerImage(getRequestURI);

        // Then
        assertNotNull(result);
        assertEquals(file.getPath(), result.getPath());
    }

    @Test
    public void givenUrl_whenGetFilePathFromUrl_thenReturnFilePath() {
        // Given
        String src = "http://example.com/images/image.jpg";
        String postFilePath = "path/to/posts";
        ReflectionTestUtils.setField(postService, "postFilePath", postFilePath);

        // When
        String result = postService.getFilePathFromUrl(src);

        // Then
        assertEquals("path/to/posts\\image.jpg", result);
    }

    @Test
    public void givenExistingFile_whenDeleteFileToDirectory_thenFileDeleted() {
        // Given
        File file = mock(File.class);
        given(file.exists()).willReturn(true);

        // When
        postService.deleteFileToDirectory(file);

        // Then
        verify(file, times(1)).exists();
        verify(file, times(1)).delete();
    }

    @Test
    public void givenNonExistingFile_whenDeleteFileToDirectory_thenNoAction() {
        // Given
        File file = mock(File.class);
        given(file.exists()).willReturn(false);

        // When
        postService.deleteFileToDirectory(file);

        // Then
        verify(file, times(1)).exists();
        verify(file, never()).delete();
    }

    @Test
    void givenExistingTrainerId_whenFindByTrainerId_thenReturnsPost() throws EntityNotFoundException {
        // Given
        Long trainerId = 1L;
        Post expectedPost = new Post();
        given(postRepository.findByTrainerId(trainerId)).willReturn(Optional.of(expectedPost));

        // When
        Post result = postService.findByTrainerId(trainerId);

        // Then
        assertEquals(expectedPost, result);
    }

    @Test
    void givenNonExistingTrainerId_whenFindByTrainerId_thenThrowsEntityNotFoundException() {
        // Given
        Long trainerId = 1L;
        given(postRepository.findByTrainerId(trainerId)).willReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> postService.findByTrainerId(trainerId));
    }
    @Test
    void givenPostId_whenDeletePost_thenRepositoryDeleteByIdCalled() {
        // Given
        Long postId = 1L;

        // When
        postService.deletePost(postId);

        // Then
        verify(postRepository, times(1)).deleteById(postId);
    }
}
