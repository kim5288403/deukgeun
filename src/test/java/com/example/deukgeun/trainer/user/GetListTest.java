package com.example.deukgeun.trainer.user;

import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.request.ProfileRequest;
import com.example.deukgeun.trainer.response.UserResponse;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class GetListTest {

    @InjectMocks
    private UserServiceImpl mockUserService;
    @Mock
    private ProfileRepository mockProfileRepository;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void shouldGetListForValidKeywordAndCurrentPage() {
        // Given
        String keyword = "김";
        Integer currentPage = 1;
        User user = User.builder()
                .name("김")
                .build();

        Profile profile = Profile.builder()
                .path("testPath")
                .userId(1L)
                .user(user)
                .build();

        List<UserResponse.UserListResponse> userList = new ArrayList<>();
        userList.add(new UserResponse.UserListResponse(profile));
        Page<UserResponse.UserListResponse> expectedList = new PageImpl<>(userList);
        given(mockProfileRepository.findByUserLikeKeyword(anyString(), any(Pageable.class))).willReturn(expectedList);

        // When
        Page<UserResponse.UserListResponse> resultList = mockUserService.getList(keyword, currentPage);

        // Then
        assertEquals(expectedList.getSize(), resultList.getSize());
        assertEquals(expectedList.getTotalElements(), resultList.getTotalElements());
        assertEquals(expectedList.getTotalPages(), resultList.getTotalPages());
        verify(mockProfileRepository, times(1)).findByUserLikeKeyword(anyString(), any(Pageable.class));
    }

    @Test
    void shouldThrowNullPointerExceptionForInvalidCurrentPage() {
        // Given
        String keyword = "김";
        Integer currentPage = null;

        // When, Then
        assertThrows(NullPointerException.class, () -> {
            userService.getList(keyword, currentPage);
        });
    }

    @Test
    void shouldNotThrowNullPointerExceptionForInvalidKeyword() {
        // Given
        String keyword = null;
        Integer currentPage = 1;

        // When, Then
        assertDoesNotThrow(() -> {
            userService.getList(keyword, currentPage);
        });
    }

}
