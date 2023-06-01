package com.example.deukgeun.trainer.profile;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.Profile;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.ProfileRepository;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.request.ProfileRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UpdateAPITest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;

    @Test
    void shouldGetByAuthTokenAPIForValidRequest() throws Exception {
        // Given
        String email = "testEmail@test.com";
        String password = "testPassword1!2@";
        JoinRequest joinRequest = new JoinRequest();
        joinRequest.setEmail(email);
        joinRequest.setName("테스트");
        joinRequest.setPassword(password);
        joinRequest.setGroupStatus(GroupStatus.Y);
        joinRequest.setGroupName("testGroupName");
        joinRequest.setPostcode("testPostCode");
        joinRequest.setJibunAddress("testJibunAddress");
        joinRequest.setRoadAddress("testRoadAddress");
        joinRequest.setDetailAddress("testDetailAddress");
        joinRequest.setExtraAddress("testExtraAddress");
        joinRequest.setGender(Gender.M);
        joinRequest.setPrice(30000);
        joinRequest.setIntroduction("testIntroduction");

        User user = JoinRequest.create(joinRequest, passwordEncoder);
        User saveUser = userRepository.save(user);

        Profile saveProfileData = ProfileRequest.create("testFileName", saveUser.getId());
        profileRepository.save(saveProfileData);

        Resource classPath = new ClassPathResource("/static/test/images/testImage.jpg");
        MockMultipartFile profile = new MockMultipartFile("profile", "testImage.jpg", "image/jpeg", classPath.getInputStream());

        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        String jwt = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        // When
        mockMvc.perform(multipart("/api/trainer/profile/")
                        .file(profile)
                        .header("Authorization", jwt)
                )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("프로필 정보 수정 성공했습니다."));
    }

    @Test
    void shouldBadRequestForInvalidRequest() throws Exception {
        // Given
        String email = "testEmail@test.com";

        Resource classPath = new ClassPathResource("/static/test/images/testImage.jpg");
        MockMultipartFile profile = new MockMultipartFile("profile", "testImage.jpg", "image/jpeg", classPath.getInputStream());

        String encodeSecretKey = Base64.getEncoder().encodeToString("invalidSecretKey".getBytes());
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        String jwt = "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        // When
        mockMvc.perform(multipart("/api/trainer/profile/")
                        .file(profile)
                        .header("Authorization", jwt)
                )
                // Then
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("잘못된 JWT signature 형식입니다."))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof SignatureException));
    }

}
