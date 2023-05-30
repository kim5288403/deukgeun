package com.example.deukgeun.trainer.user;

import com.example.deukgeun.commom.enums.Gender;
import com.example.deukgeun.trainer.entity.GroupStatus;
import com.example.deukgeun.trainer.entity.User;
import com.example.deukgeun.trainer.repository.UserRepository;
import com.example.deukgeun.trainer.request.JoinRequest;
import com.example.deukgeun.trainer.service.implement.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class GetUserIdTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserServiceImpl userService;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;
    @Value("${jwt.authTokenTime}")
    private long authTokenTime;

    @Test
    void shouldGetUserIdForValidParameter() {
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

        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        // When
        long result = userService.getUserId(jwt);

        // Then
        assertEquals(saveUser.getId(), result);
    }

    @Test
    void shouldSignatureExceptionForInvalidParameter() {
        // Given
        String email = "testEmail@test.com";
        String invalidSecretKey = "invalidSecretKey";
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, invalidSecretKey)
                .compact();

        // When, Then
        assertThrows(SignatureException.class, () -> {
            userService.getUserId(jwt);
        });
    }

    @Test
    void shouldEntityNotFoundExceptionForInvalidParameter() {
        // Given
        String email = "testEmail@test.com";
        String encodeSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("roles", role);
        Date now = new Date();

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, encodeSecretKey)
                .compact();

        // When, Then
        assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserId(jwt);
        });
    }


}
