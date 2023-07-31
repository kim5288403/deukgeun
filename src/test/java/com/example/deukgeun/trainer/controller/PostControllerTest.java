package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.authToken.application.dto.response.RestResponse;
import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.enums.Gender;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.example.deukgeun.trainer.application.controller.PostController;
import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.TrainerApplicationService;
import com.example.deukgeun.trainer.domain.model.aggregate.Trainer;
import com.example.deukgeun.trainer.domain.model.entity.Post;
import com.example.deukgeun.trainer.domain.model.entity.Profile;
import com.example.deukgeun.trainer.domain.model.valueobjcet.Address;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
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
    public void givenPostService_whenDeletePost_thenDeletePostAndReturnSuccessResponse() {
        // Given
        String authToken = "exampleAuthToken";
        String email = "test";
        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);

        // When
        ResponseEntity<?> responseEntity = postController.delete(request);

        // Then
        verify(trainerApplicationService).deletePost(email);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenPostService_whenDeleteServerImage_thenDeleteFile() throws IOException {
        // Given
        String src = "example/src/image.jpg";

        // When
        postController.deleteServerImage(src);

        // Then
        verify(trainerApplicationService, times(1)).deleteImageToServer(src);
    }

//    @Test
//    public void givenRequestAndResponse_whenGetServerImage_thenImageShouldBeSentInResponse() throws Exception {
//        // Given
//        String requestUri = "/images/image.jpg";
//
//        ServletContext servletContextMock = mock(ServletContext.class);
//        File fileMock = mock(File.class);
//        Path pathMock = mock(Path.class);
//
//        given(request.getRequestURI()).willReturn(requestUri);
//        given(request.getServletContext()).willReturn(servletContextMock);
//        given(fileMock.getName()).willReturn("image.jpg");
//        given(fileMock.length()).willReturn(12345L);
//        given(fileMock.toPath()).willReturn(pathMock);
//        given(servletContextMock.getMimeType(fileMock.getName())).willReturn("image/jpeg");
//
//        given(trainerApplicationService.getServerImage(requestUri)).willReturn(fileMock);
//
//        // When
//        postController.getServerImage(request, response);
//
//        // Then
//        verify(trainerApplicationService, times(1)).getServerImage(requestUri);
//        verify(response, times(1)).setHeader("Content-Type", "image/jpeg");
//        verify(response, times(1)).setHeader("Content-Length", "12345");
//        verify(response, times(1)).setHeader("Content-Disposition", "inline; filename=\"image.jpg\"");
//    }

    @Test
    public void givenPostService_whenGetPostById_thenReturnResponseEntityWithPostResponse() {
        // Given
        Long trainerId = 123L;
        Post post = new Post(123L, "test", trainerId);
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                "email",
                "test",
                GroupStatus.N,
                "test",
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test",
                mock(List.class),
                mock(Profile.class),
                post
        );

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", post);

        given(trainerApplicationService.findById(trainerId)).willReturn(trainer);

        // When
        ResponseEntity<?> responseEntity = postController.getPostById(trainerId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServiceTrainerServicePostService_whenGetPostByAuthToken_thenReturnResponseEntityWithPostResponse() {
        // Given
        String authToken = "exampleAuthToken";
        Long trainerId = 123L;
        String email = "test";
        Post post = new Post(123L, "test", trainerId);
        Trainer trainer = new Trainer(
                trainerId,
                "test",
                "email",
                "test",
                GroupStatus.N,
                "test",
                new Address(
                        "test",
                        "test",
                        "test",
                        "test",
                        "test"
                ),
                Gender.M,
                3000,
                "test",
                mock(List.class),
                mock(Profile.class),
                post
        );

        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", post);

        given(authTokenApplicationService.resolveAuthToken(request)).willReturn(authToken);
        given(authTokenApplicationService.getUserPk(authToken)).willReturn(email);
        given(trainerApplicationService.findByEmail(email)).willReturn(trainer);

        // When
        ResponseEntity<?> responseEntity = postController.getPostByAuthToken(request);

        // Then
        verify(authTokenApplicationService, times(1)).resolveAuthToken(request);
        verify(authTokenApplicationService, times(1)).getUserPk(authToken);
        verify(trainerApplicationService, times(1)).findByEmail(email);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }

    @Test
    public void givenTokenServicePostService_whenUploadPost_thenReturnResponseEntity() throws Exception {
        // Given
        PostRequest postRequest = new PostRequest();
        Long trainerId = 123L;

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
    public void givenPostService_whenUploadServerImage_thenSaveImageAndWriteJsonResponse() throws Exception {
        // Given
        PrintWriter writer = mock(PrintWriter.class);

        Map<Object, Object> responseData = new HashMap<>();
        responseData.put("key1", "value1");
        responseData.put("key2", "value2");

        given(trainerApplicationService.saveImageToServer(request, response)).willReturn(responseData);
        given(response.getWriter()).willReturn(writer);

        // When
        postController.uploadServerImage(request, response);

        // Then
        verify(trainerApplicationService, times(1)).saveImageToServer(request, response);
        verify(response, times(1)).setContentType("application/json");
        verify(response, times(1)).setCharacterEncoding("UTF-8");
        verify(writer).write("{\"key1\":\"value1\",\"key2\":\"value2\"}");
    }

}
