package com.example.deukgeun.trainer.valid;

import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PasswordConfirmationTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void shouldReturnTrueWhenPasswordsMatch() {
        // Given
        String password = "test1";
        String confirm = "test1";

        // When
        boolean result = userService.isPasswordConfirmation(password, confirm);

        // Then
        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenPasswordsDoNotMatch() {
        // Given
        String password = "test1";
        String confirm = "test2";

        // When
        boolean result = userService.isPasswordConfirmation(password, confirm);

        // Then
        assertFalse(result);
    }

}
