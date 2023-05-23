package com.example.deukgeun.trainer.valid;

import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class FileTypeTest {

    @Autowired
    private ProfileServiceImpl profileService;

    @Test
    void shouldReturnTrueForSupportedContentType() throws IOException {
        // Given
        Resource testFile = new ClassPathResource("/static/test/images/testImage.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "testImage.jpg", "image/jpg", testFile.getInputStream());

        // When
        boolean result = profileService.isSupportedContentType(file);

        // Then
        assertTrue(result);
    }

    void shouldReturnFalseForUnsupportedContentType() throws IOException {
        // Given
        Resource testFile = new ClassPathResource("/static/test/texts/text.txt");
        MockMultipartFile file = new MockMultipartFile("file", "testText.txt", "text/plain", testFile.getInputStream());

        // When
        boolean result = profileService.isSupportedContentType(file);

        // Then
        assertFalse(result);
    }
}
