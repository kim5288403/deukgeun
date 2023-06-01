package com.example.deukgeun.trainer.post;

import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
public class DeleteFileToDirectoryTest {
    @Autowired
    private PostServiceImpl postService;
    @Value("${trainer.backup.post.filePath}")
    private String backupDirectoryPath;
    @Value("${trainer.post.filePath}")
    private String postDirectoryPath;

    @BeforeEach
    void setUp() throws IOException {
        // 디렉토리 백업
        File profileDirectory = new File(postDirectoryPath);
        File backupDirectory = new File(backupDirectoryPath);

        FileUtils.copyDirectory(profileDirectory, backupDirectory);
    }

    @Test
    void shouldDeleteFileToDirectoryForValidParameter() throws IOException {
        // Given
        String fileName = "testImage.jpg";
        String path = postDirectoryPath + "\\" + fileName;

        Resource classPath = new ClassPathResource("/static/test/images/testImage.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "testImage.jpg", "image/jpeg", classPath.getInputStream());
        File uploadsFile = new File(file.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(uploadsFile)) {
            fileOutputStream.write(file.getBytes());
        }
        File uploads = new File(postDirectoryPath);
        FileUtils.copyFileToDirectory(uploadsFile, uploads);

        // Whem
        postService.deleteFileToDirectory(path);
        File result = new File(path);

        // Then
        assertFalse(result.exists());
    }

    @AfterEach
    void restoreDirectory() throws IOException {
        // 디렉토리 삭제
        File profileDirectory = new File(postDirectoryPath);
        File backupDirectory = new File(backupDirectoryPath);
        boolean deleteCheck = FileSystemUtils.deleteRecursively(profileDirectory);

        if (deleteCheck) {
            // 디렉토리 덮어쓰우기
            boolean mkdirCheck = profileDirectory.mkdir();
            if (mkdirCheck) {
                FileUtils.copyDirectory(backupDirectory, profileDirectory);
            }
        }
    }

}
