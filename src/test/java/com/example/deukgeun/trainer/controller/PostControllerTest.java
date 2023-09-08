package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.PostController;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
    public void givenPostService_whenDeletePost_thenDeletePostAndReturnSuccessResponse() throws IOException {
        // Given
        String authToken = "exampleAuthToken";
        String email = "test";
        String src = "test";
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = postController.deletePost(request, src);

        // Then
        verify(trainerApplicationService).deletePostByEmail(email);
        verify(trainerApplicationService).deleteImageToS3(src);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenPostService_whenDeleteS3Image_thenDeleteFile() throws IOException {
        // Given
        String src = "example/src/image.jpg";

        // When
        postController.deleteS3Image(src);

        // Then
        verify(trainerApplicationService, times(1)).deleteImageToS3(src);
    }

    @Test
    public void givenTokenServicePostService_whenUploadPost_thenReturnResponseEntity() throws Exception {
        // Given
        PostRequest postRequest = new PostRequest();

        String authToken = "exampleAuthToken";
        String email = "test";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 저장 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);

        // When
        ResponseEntity<?> responseEntity = postController.uploadPost(request, postRequest, bindingResult);

        // Then
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(trainerApplicationService, times(1)).uploadPost(email, postRequest);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenPostService_whenUploadS3Image_thenSaveImageAndWriteJsonResponse() throws Exception {
        // Given
        PrintWriter writer = mock(PrintWriter.class);

        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("key1", "value1");
        responseData.put("key2", "value2");

        given(trainerApplicationService.saveImageToS3(request, response)).willReturn(responseData);
        given(response.getWriter()).willReturn(writer);

        // When
        postController.uploadS3Image(request, response);

        // Then
        verify(trainerApplicationService, times(1)).saveImageToS3(request, response);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(writer).write("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }

}
