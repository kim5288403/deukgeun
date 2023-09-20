package com.example.deukgeun.trainer.service.application;

import com.example.deukgeun.trainer.application.dto.request.PostRequest;
import com.example.deukgeun.trainer.application.service.implement.PostApplicationServiceImpl;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.HtmlUtils;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PostApplicationServiceTest {
    @InjectMocks
    private PostApplicationServiceImpl postApplicationService;
    @Mock
    private TrainerDomainService trainerDomainService;

    @Test
    public void givenValidEmail_whenDeletePostByEmail_thenPostDeleteCalled() {
        // Given
        String email = "test@example.com";

        // When
        postApplicationService.deletePost(email);

        // Then
        verify(trainerDomainService, times(1)).deletePostByEmail(email);
    }

    @Test
    public void givenValidEmailAndPostRequest_whenUploadPost_thenUploadPostByEmailCalled() {
        // Given
        String email = "test@example.com";
        String content = "<script>alert('Hello, World!');</script>";

        PostRequest postRequest = new PostRequest();
        postRequest.setContent(content);

        // When
        postApplicationService.uploadPost(email, postRequest);

        // Then
        verify(trainerDomainService, times(1)).uploadPostByEmail(email, HtmlUtils.htmlEscape(content));
    }

}
