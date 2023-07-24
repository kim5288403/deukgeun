package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.PostController;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.dto.response.PostResponse;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.application.service.implement.PostApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Trainer;
import com.example.deukgeun.trainer.domain.model.valueobjcet.GroupStatus;
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
    private PostApplicationServiceImpl postApplicationService;
    @Mock
    private TrainerApplicationService trainerApplicationService;
    @Mock
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletResponse response;

    @Test
    public void givenPostService_whenDeletePost_thenDeletePostAndReturnSuccessResponse() {
        // Given
        Long postId = 123L;
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = postController.delete(postId);

        // Then
        verify(postApplicationService).deleteById(postId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenPostService_whenDeleteServerImage_thenDeleteFile() {
        // Given
        String src = "example/src/image.jpg";
        String filePath = "/path/to/image.jpg";
        File file = new File(filePath);

        given(postApplicationService.getFilePathFromUrl(src)).willReturn(filePath);

        // When
        postController.deleteServerImage(src);

        // Then
        verify(postApplicationService, times(1)).getFilePathFromUrl(src);
        verify(postApplicationService, times(1)).deleteFileToDirectory(file);
    }

    @Test
    public void givenPostService_whenGetDetailByUserId_thenReturnResponseEntityWithPostResponse() {
        // Given
        Long trainerId = 123L;
        Post post = new Post(123L, "test", trainerId);

        PostResponse response = new PostResponse(post);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", response);

        given(postApplicationService.findByTrainerId(trainerId)).willReturn(post);

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
        String email = "test";
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                email,
                "test",
                GroupStatus.N,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                Gender.M,
                3000,
                "test"
        );
        Post post = new Post(123L, "test", trainerId);

        PostResponse response = new PostResponse(post);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", response);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);
        given(postApplicationService.findByTrainerId(trainerId)).willReturn(post);

        // When
        ResponseEntity<?> responseEntity = postController.getDetailByAuthToken(request);

        // Then
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerApplicationService, times(1)).findByEmail(email);
        verify(postApplicationService, times(1)).findByTrainerId(trainerId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServicePostService_whenUploadPost_thenReturnResponseEntity() throws Exception {
        // Given
        PostRequest postRequest = new PostRequest();
        Long trainerId = 123L;
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                "test",
                "test",
                GroupStatus.N,
                "test",
                "test",
                "test",
                "test",
                "test",
                "test",
                Gender.M,
                3000,
                "test"
        );
        String authToken = "exampleAuthToken";
        String email = "test";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 저장 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);

        // When
        ResponseEntity<?> responseEntity = postController.upload(request, postRequest, bindingResult);

        // Then
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(postApplicationService, times(1)).upload(postRequest, trainerId);
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

        given(postApplicationService.saveImage(request, response)).willReturn(responseData);
        given(response.getWriter()).willReturn(writer);

        // When
        postController.uploadServerImage(request, response);

        // Then
        verify(postApplicationService, times(1)).saveImage(request, response);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(writer).write("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }

}
