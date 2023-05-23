package com.example.deukgeun.trainer.valid;

import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class EmptyGroupNameTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void shouldReturnFalseForGroupStatusYAndEmptyGroupName() {
        // Given
        String groupName =  "";
        String groupStatus = "Y";

        // When
        boolean result = userService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertFalse(result);
    }
    @Test
    void shouldReturnTrueForGroupStatusNAndEmptyGroupName() {
        // Given
        String groupName =  "";
        String groupStatus = "N";

        // When
        boolean result = userService.isEmptyGroupName(groupName, groupStatus);

        // Then
        assertTrue(result);
    }

}
