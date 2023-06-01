package com.example.deukgeun.trainer.post;

import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class GetServerImageTest {
    @Autowired
    private PostServiceImpl postService;
    @Value("${trainer.post.filePath}")
    private String postDirectoryPath;

    @Test
    void shouldGetServerImageForValidParameter() {
        // Given
        String fileName = "testImage.jpg";
        String path = "testPath/" + fileName;
        File file = new File(postDirectoryPath, fileName);

        // Whem
        File result = postService.getServerImage(path);

        // Then
        assertEquals(file.getName(), result.getName());
        assertEquals(file.getPath(), result.getPath());
    }
}
