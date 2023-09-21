package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.service.AuthTokenApplicationService;
import com.example.deukgeun.global.dto.RestResponse;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.PostController;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.PostApplicationService;
import com.example.deukgeun.trainer.infrastructure.s3.S3Service;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private PostApplicationService postApplicationService;
    @Mock
    private S3Service s3Service;
    @Mock
    private AuthTokenApplicationService authTokenApplicationService;
    @Mock
    private HttpServletRequest request;
    @Mock
    private BindingResult bindingResult;
    @Mock
    private HttpServletResponse response;

    @Test
    public void givenValidSrc_whenDeletePost_thenReturnSuccessResponse() {
        // Given
        String authToken = "exampleAuthToken";
        String email = "test";
        String src = "test";
        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = postController.deletePost(request, src);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(postApplicationService, times(1)).deletePost(anyString());
        verify(s3Service, times(1)).delete(anyString());
    }

    @Test
    public void givenValidPostRequest_whenUploadPost_thenReturnSuccessResponse() {
        // Given
        PostRequest postRequest = mock(PostRequest.class);
        String authToken = "exampleAuthToken";
        String email = "test";
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 저장 성공했습니다.", null);

        given(authTokenApplicationService.resolveAuthToken(any(HttpServletRequest.class))).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(anyString())).willReturn(email);

        // When
        ResponseEntity<?> responseEntity = postController.uploadPost(request, postRequest, bindingResult);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
        verify(postApplicationService, times(1)).uploadPost(anyString(), any(PostRequest.class));
        verify(authTokenApplicationService, times(1)).resolveAuthToken(any(HttpServletRequest.class));
    }

    @Test
    public void givenValidRequestAndResponse_whenUploadS3Image_thenReturnJsonResponse() throws Exception {
        // Given
        PrintWriter writer = mock(PrintWriter.class);

        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("key1", "value1");
        responseData.put("key2", "value2");

        given(s3Service.saveImageToS3(any(HttpServletRequest.class))).willReturn(responseData);
        given(response.getWriter()).willReturn(writer);

        // When
        postController.uploadS3Image(request, response);

        // Then
        verify(s3Service, times(1)).saveImageToS3(any(HttpServletRequest.class));
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(writer).write("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }

}
