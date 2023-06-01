package com.example.deukgeun.trainer.post;

import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GetFilePathFromUrlTest {

    @Autowired
    private PostServiceImpl postService;
    @Value("${trainer.post.filePath}")
    private String postDirectoryPath;
    @Test
    void shouldGetFilePathFromUrlForValidParameter() {
        // Given
        String fileName = "testImage.jpg";
        String src = "test/" + fileName;

        // Whem
        String result = postService.getFilePathFromUrl(src);

        // Then
        assertEquals(postDirectoryPath + "\\" + fileName, result);
    }

}
