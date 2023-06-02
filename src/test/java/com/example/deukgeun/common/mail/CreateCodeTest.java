package com.example.deukgeun.common.mail;

import com.example.deukgeun.commom.service.implement.MailServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CreateCodeTest {
    @Autowired
    private MailServiceImpl mailService;

    @Test
    void shouldCreateCodeForValidParameter() {
        // Given
        String before = mailService.createCode();

        // When
        String result = mailService.createCode();

        // Then
        assertNotEquals(before, result);
        assertEquals(8, result.length());
    }

}
