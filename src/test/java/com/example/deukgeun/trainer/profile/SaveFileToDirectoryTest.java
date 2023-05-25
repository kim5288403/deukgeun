package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.request.AuthMailRequest;
import com.example.deukgeun.trainer.service.implement.ProfileServiceImpl;
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
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SaveFileToDirectoryTest {
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
    void shouldSaveFileToDirectoryValidFiles() throws IOException {
        // Given
        Resource classPath = new ClassPathResource("/static/test/images/testImage.jpg");
        MockMultipartFile file = new MockMultipartFile("testImage", "testImage.jpg", "image/jpg", classPath.getInputStream());
        String fileName = profileService.getUUIDPath(file.getOriginalFilename());

        // When
        profileService.saveFileToDirectory(file, fileName);

        // Then
        File fileTest = new File(profileDirectoryPath + "\\" + fileName);
        assertTrue(fileTest.exists());
        assertEquals(fileName, fileTest.getName());
    }

    @Test
    void shouldThrowNullPointerExceptionDirectoryInvalidFileName() {
        // Given
        MockMultipartFile file = new MockMultipartFile("testImage", new byte[0]);
        String fileName = null;

        // When, Then
        assertThrows(NullPointerException.class, () -> {
            profileService.saveFileToDirectory(file, fileName);
        });
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
