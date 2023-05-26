package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.entity.AuthMail;
import com.example.deukgeun.commom.enums.MailStatus;
import com.example.deukgeun.commom.repository.AuthMailRepository;
import com.example.deukgeun.commom.request.AuthMailRequest;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class SaveAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthMailRepository authMailRepository;

    @Value("${trainer.backup.profile.filePath}")
    private String backupDirectoryPath;

    @Value("${trainer.profile.filePath}")
    private String profileDirectoryPath;

    @BeforeEach
    void setUp() throws IOException {
        System.out.println("-------------------- UserSaveTest Start --------------------");

        AuthMail authMail = AuthMailRequest.create("testEmail@test.com", "1234", MailStatus.Y);
        authMailRepository.save(authMail);

        // 디렉토리 백업
        File profileDirectory = new File(profileDirectoryPath);
        File backupDirectory = new File(backupDirectoryPath);

        FileUtils.copyDirectory(profileDirectory, backupDirectory);
    }


    @Test
    void shouldSaveUserAPIForValidRequest() throws Exception {
        // Given
        Resource classPath = new ClassPathResource("/static/test/images/testImage.jpg");
        MockMultipartFile profile = new MockMultipartFile("profile", "testImage.jpg", "image/jpg", classPath.getInputStream());

        // When
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/trainer/")
                        .file(profile)
                        .contentType("")
                        .param("processData", "false")
                        .param("name", "테스트이름")
                        .param("email", "testEmail@test.com")
                        .param("password", "testPassword1!2@")
                        .param("passwordConfirm", "testPassword1!2@")
                        .param("postcode", "testPostCode")
                        .param("jibunAddress", "testJibunAddress")
                        .param("roadAddress", "testRoadAddress")
                        .param("detailAddress", "testDetailAddress")
                        .param("extraAddress", "testExtraAddress")
                        .param("price", "30000")
                        .param("gender", "M")
                        .param("groupStatus", "Y")
                        .param("groupName", "testGroupName")
                        .param("code", "1234")
                        .param("introduction", "testIntroduction")
                )

                // then
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("회원 가입 성공 했습니다."));
    }

    @Test
    void shouldReturnBadRequestForInvalidFormData() throws Exception {
        // Given
        Resource classPath = new ClassPathResource("/static/test/texts/text.txt");
        MockMultipartFile profile = new MockMultipartFile("profile", "testImage.txt", "text/plain", classPath.getInputStream());

        // When
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/trainer/")
                        .file(profile)
                        .contentType("")
                        .param("processData", "false")
                        .param("name", "")
                        .param("email", "")
                        .param("password", "testPassword1!2")
                        .param("passwordConfirm", "testPassword1!2@")
                        .param("postcode", "")
                        .param("jibunAddress", "testJibunAddress")
                        .param("roadAddress", "testRoadAddress")
                        .param("detailAddress", "testDetailAddress")
                        .param("extraAddress", "testExtraAddress")
                        .param("price", "")
                        .param("gender", "")
                        .param("groupStatus", "")
                        .param("groupName", "testGroupName")
                        .param("code", "")
                        .param("introduction", "")
                )

                // then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("request error!"));
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
