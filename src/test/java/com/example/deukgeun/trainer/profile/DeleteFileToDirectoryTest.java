package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class DeleteFileToDirectoryTest {
    @Value("${trainer.backup.profile.filePath}")
    private String backupDirectoryPath;
    @Value("${trainer.profile.filePath}")
    private String profileDirectoryPath;
    @Autowired
    private ProfileServiceImpl profileService;

    @BeforeEach
    void setUp() throws IOException {
        System.out.println("-------------------- UserSaveTest Start --------------------");

        // 디렉토리 백업
        File profileDirectory = new File(profileDirectoryPath);
        File backupDirectory = new File(backupDirectoryPath);

        FileUtils.copyDirectory(profileDirectory, backupDirectory);
    }

    @Test
    void shouldDeleteFileToDirectoryForValidPath() {
        // Given
        String fileName = "testImage.jpg";
        File file = new File(profileDirectoryPath + "\\" + fileName);
        assertTrue(file.exists());

        // When
        profileService.deleteFileToDirectory(fileName);

        // Then
        assertFalse(file.exists());
    }

    @AfterEach
    void restoreDirectory() throws IOException {
        // 디렉토리 삭제
        File profileDirectory = new File(profileDirectoryPath);
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
