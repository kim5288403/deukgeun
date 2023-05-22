package com.example.deukgeun.trainer.valid;

import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Transactional
@SpringBootTest
public class DuplicateEmailTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void  shouldReturnTrueWhenEmailIsDuplicate() {
        // Given
        String email = anyString();
        given(userRepository.existsByEmail(email)).willReturn(true);

        // When
        boolean result = userService.isDuplicateEmail(email);

        // Then
        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    void  shouldReturnFalseWhenEmailIsNotDuplicate() {
        // Given
        String email = anyString();
        given(userRepository.existsByEmail(email)).willReturn(false);

        // When
        boolean result = userService.isDuplicateEmail(email);

        // Then
        assertFalse(result);
        verify(userRepository, times(1)).existsByEmail(email);
    }
}
