package com.example.deukgeun.trainer.controller;

import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.commom.util.RestResponseUtil;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.response.PostResponse;
import com.example.deukgeun.trainer.service.implement.MemberServiceImpl;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private MemberServiceImpl memberService;
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
        Long memberId = 123L;
        Post post = Post
                .builder()
                .id(123L)
                .memberId(memberId)
                .html("test")
                .build();
        PostResponse response = new PostResponse(post);
        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", response);

        given(postService.findByMemberId(memberId)).willReturn(post);

        // When
        ResponseEntity<?> responseEntity = postController.getDetailByUserId(memberId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
    }
//
//    @Test
//    public void givenTokenServiceMemberServicePostService_whenGetDetailByAuthToken_thenReturnResponseEntityWithPostResponse() {
//        // Given
//        String authToken = "exampleAuthToken";
//        Long memberId = 123L;
//        Post post = Post
//                .builder()
//                .id(123L)
//                .memberId(memberId)
//                .html("test")
//                .build();
//        PostResponse response = new PostResponse(post);
//        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("조회 성공 했습니다.", response);
//
//        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
//        given(memberService.getMemberId(authToken)).willReturn(memberId);
//        given(postService.findByMemberId(memberId)).willReturn(post);
//
//        // When
//        ResponseEntity<?> responseEntity = postController.getDetailByAuthToken(request);
//
//        // Then
//        verify(tokenService, times(1)).resolveAuthToken(request);
//        verify(memberService, times(1)).getMemberId(authToken);
//        verify(postService, times(1)).findByMemberId(memberId);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
//    }
//
//    @Test
//    public void givenTokenServicePostService_whenUploadPost_thenReturnResponseEntity() throws Exception {
//        // Given
//        PostRequest postRequest = new PostRequest();
//        String authToken = "exampleAuthToken";
//        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 저장 성공했습니다.", null);
//
//        given(tokenService.resolveAuthToken(request)).willReturn(authToken);
//
//        // When
//        ResponseEntity<?> responseEntity = postController.upload(request, postRequest, bindingResult);
//
//        // Then
//        verify(tokenService, times(1)).resolveAuthToken(request);
//        verify(postService, times(1)).upload(postRequest, authToken);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
//    }
//
//    @Test
//    public void givenPostService_whenUploadServerImage_thenSaveImageAndWriteJsonResponse() throws Exception {
//        // Given
//        PrintWriter writer = mock(PrintWriter.class);
//
//        Map<Object, Object> responseData = new HashMap<>();
//        responseData.put("key1", "value1");
//        responseData.put("key2", "value2");
//
//        given(postService.saveImage(request, response)).willReturn(responseData);
//        given(response.getWriter()).willReturn(writer);
//
//        // When
//        postController.uploadServerImage(request, response);
//
//        // Then
//        verify(postService, times(1)).saveImage(request, response);
//        verify(response, times(1)).setContentType("application/json");
//        verify(response, times(1)).setCharacterEncoding("UTF-8");
//        verify(writer).write("{\"key1\":\"value1\",\"key2\":\"value2\"}");
//    }
//
//    @Test
//    public void givenPostService_whenDeleteServerImage_thenDeleteFile() {
//        // Given
//        String src = "example/src/image.jpg";
//        String filePath = "/path/to/image.jpg";
//        File file = new File(filePath);
//
//        given(postService.getFilePathFromUrl(src)).willReturn(filePath);
//
//        // When
//        postController.deleteServerImage(src);
//
//        // Then
//        verify(postService, times(1)).getFilePathFromUrl(src);
//        verify(postService, times(1)).deleteFileToDirectory(file);
//    }
//
//    @Test
//    public void givenPostService_whenGetServerImage_thenSetResponseHeadersAndCopyFileToOutputStream() throws Exception {
//        // Given
//        File file = mock(File.class);
//        Path filePath = mock(Path.class);
//        ServletContext context = mock(ServletContext.class);
//
//        String requestURI = "/image/example.jpg";
//        String mimeType = "image/jpeg";
//        long fileLength = 123L;
//        String fileName = "example.jpg";
//        mockStatic(Files.class);
//
//        given(file.length()).willReturn(fileLength);
//        given(file.getName()).willReturn(fileName);
//        given(file.toPath()).willReturn(filePath);
//        given(postService.getServerImage(requestURI)).willReturn(file);
//        given(request.getRequestURI()).willReturn(requestURI);
//        given(request.getServletContext()).willReturn(context);
//        given(request.getServletContext().getMimeType(file.getName())).willReturn(mimeType);
//
//        // When
//        postController.getServerImage(request, response);
//
//        // Then
//        verify(postService).getServerImage(requestURI);
//        verify(request).getRequestURI();
//        verify(request.getServletContext()).getMimeType(file.getName());
//        verify(response).setHeader("Content-Type", mimeType);
//        verify(response).setHeader("Content-Length", String.valueOf(fileLength));
//        verify(response).setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
//    }
//
//    @Test
//    public void givenPostService_whenDeletePost_thenDeletePostAndReturnSuccessResponse() {
//        // Given
//        Long postId = 123L;
//        ResponseEntity<RestResponse> expectedResponse = RestResponseUtil.ok("게시글 삭제 성공했습니다.", null);
//
//        // When
//        ResponseEntity<?> responseEntity = postController.delete(postId);
//
//        // Then
//        verify(postService).deletePost(postId);
//        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//        assertEquals(expectedResponse.getBody(), responseEntity.getBody());
//    }
}
