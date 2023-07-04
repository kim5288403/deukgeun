package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.service.implement.TrainerServiceImpl;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PostControllerTest {
    @InjectMocks
    private PostController postController;
    @Mock
    private PostServiceImpl postService;
    @Mock
    private TrainerServiceImpl trainerService;
    @Mock
    private TokenServiceImpl tokenService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletResponse response;

    @Test
    public void givenPostService_whenGetDetailByUserId_thenReturnResponseEntityWithPostResponse() {
        // Given
        Long trainerId = 123L;
        Post post = Post
                .builder()
                .id(123L)
                .trainerId(trainerId)
                .html("test")
                .build();
        PostResponse response = new PostResponse(post);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", response);

        given(postService.findByTrainerId(trainerId)).willReturn(post);

        // When
        ResponseEntity<?> responseEntity = postController.getDetailByUserId(trainerId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceTrainerServicePostService_whenGetDetailByAuthToken_thenReturnResponseEntityWithPostResponse() {
        // Given
        String authToken = "exampleAuthToken";
        Long trainerId = 123L;
        Post post = Post
                .builder()
                .id(123L)
                .trainerId(trainerId)
                .html("test")
                .build();
        PostResponse response = new PostResponse(post);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", response);

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
        given(trainerService.getTrainerId(authToken)).willReturn(trainerId);
        given(postService.findByTrainerId(trainerId)).willReturn(post);

        // When
        ResponseEntity<?> responseEntity = postController.getDetailByAuthToken(request);

        // Then
        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(trainerService, times(1)).getTrainerId(authToken);
        verify(postService, times(1)).findByTrainerId(trainerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServicePostService_whenUploadPost_thenReturnResponseEntity() throws Exception {
        // Given
        PostRequest postRequest = new PostRequest();
        String authToken = "exampleAuthToken";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 저장 성공했습니다.", null);

        given(tokenService.resolveAuthToken(request)).willReturn(authToken);

        // When
        ResponseEntity<?> responseEntity = postController.upload(request, postRequest, bindingResult);

        // Then
        verify(tokenService, times(1)).resolveAuthToken(request);
        verify(postService, times(1)).upload(postRequest, authToken);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenPostService_whenUploadServerImage_thenSaveImageAndWriteJsonResponse() throws Exception {
        // Given
        PrintWriter writer = mock(PrintWriter.class);

        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("key1", "value1");
        responseData.put("key2", "value2");

        given(postService.saveImage(request, response)).willReturn(responseData);
        given(response.getWriter()).willReturn(writer);

        // When
        postController.uploadServerImage(request, response);

        // Then
        verify(postService, times(1)).saveImage(request, response);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(writer).write("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }

    @Test
    public void givenPostService_whenDeleteServerImage_thenDeleteFile() {
        // Given
        String src = "example/src/image.jpg";
        String filePath = "/path/to/image.jpg";
        File file = new File(filePath);

        given(postService.getFilePathFromUrl(src)).willReturn(filePath);

        // When
        postController.deleteServerImage(src);

        // Then
        verify(postService, times(1)).getFilePathFromUrl(src);
        verify(postService, times(1)).deleteFileToDirectory(file);
    }

    @Test
    public void givenPostService_whenDeletePost_thenDeletePostAndReturnSuccessResponse() {
        // Given
        Long postId = 123L;
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = postController.delete(postId);

        // Then
        verify(postService).deletePost(postId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
}
