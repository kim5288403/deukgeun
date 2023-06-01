package com.example.deukgeun.trainer.post;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Post;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.PostRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.PostRequest;
import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DeletePostTest {
    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private PostRepository postRepository;
    @Test
    void shouldDeleteForValidParameter() {
        // Given
        String html = "test content";
        String email = "testEmail@test.com";
        String password = "testPassword1!2@";
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail(email);
        joinRequest.setName("테스트");
        joinRequest.setPassword(password);
        joinRequest.setGroupStatus(GroupStatus.Y);
        joinRequest.setGroupName("testGroupName");
        joinRequest.setPostcode("testPostCode");
        joinRequest.setJibunAddress("testJibunAddress");
        joinRequest.setRoadAddress("testRoadAddress");
        joinRequest.setDetailAddress("testDetailAddress");
        joinRequest.setExtraAddress("testExtraAddress");
        joinRequest.setGender(Gender.M);
        joinRequest.setPrice(30000);
        joinRequest.setIntroduction("testIntroduction");
        User user = JoinRequest.create(joinRequest, passwordEncoder);
        User saveUser = userRepository.save(user);

        Post post = PostRequest.create(html, saveUser.getId());
        Post savePost = postRepository.save(post);
        long postId = savePost.getId();
        // When
        postService.deletePost(postId);
        Post result = postRepository.findById(postId).orElse(null);

        // Then
        assertNull(result);
    }

    @Test
    void shouldEmptyResultDataAccessExceptionForInvalidParameter() {
        // Given
        long postId = 99999;

        // When
        assertThrows(EmptyResultDataAccessException.class, () -> {
            postService.deletePost(postId);
        });
    }
}
