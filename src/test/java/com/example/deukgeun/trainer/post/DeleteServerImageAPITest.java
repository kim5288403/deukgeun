package com.example.deukgeun.trainer.post;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class DeleteServerImageAPITest {
    @Autowired
    private MockMvc mockMvc;
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
    void shouldDeleteServerImageAPIForValidRequest() throws Exception {
        // When
        mockMvc.perform(delete("/api/trainer/post/")
                        .param("src", postDirectoryPath)
                )
                // Then
                .andExpect(status().isOk());
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
