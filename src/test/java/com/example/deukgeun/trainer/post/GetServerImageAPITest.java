package com.example.deukgeun.trainer.post;

import com.example.deukgeun.trainer.service.implement.PostServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.NoSuchFileException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GetServerImageAPITest {
    @Autowired
    private MockMvc mockMvc;
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
    void shouldGetServerImageAPIForValidRequest() throws Exception {
        // Given
        Resource classPath = new ClassPathResource("/static/test/images/testImage.jpg");
        MockMultipartFile file = new MockMultipartFile("file", "testImage.jpg", "image/jpeg", classPath.getInputStream());
        File uploadsFile = new File(file.getOriginalFilename());
        try (FileOutputStream fileOutputStream = new FileOutputStream(uploadsFile)) {
            fileOutputStream.write(file.getBytes());
        }
        File uploads = new File(postDirectoryPath);
        FileUtils.copyFileToDirectory(uploadsFile, uploads);

        // When
        mockMvc.perform(get("/api/trainer/post/image/testImage.jpg")
                )
                // Then
                .andExpect(status().isOk());
    }

    @Test
    void shouldNoSuchFileExceptionForInvalidRequest() {
        // When
        assertThrows(NoSuchFileException.class, () -> {
            mockMvc.perform(get("/api/trainer/post/image/testImage.jpg")
                    )
                    // Then
                    .andExpect(status().isBadRequest());
        });
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
