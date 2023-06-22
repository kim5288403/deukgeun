package com.example.deukgeun.common.service;

import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserDetailServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TokenTest {
    @Mock
    private HttpServletResponse response;
    @Mock
    private UserDetailServiceImpl userDetailService;
    @InjectMocks
    private TokenServiceImpl tokenService;
    @Mock
    private TokenRepository tokenRepository;

    @Value("${jwt.authTokenTime}")
    private long authTokenTime;
    @Value("${jwt.refreshTokenTime}")
    private long refreshTokenTime;
    @Value("${jwt.secretKey}")
    private String secretKey;
    @Value("${deukgeun.role.trainer}")
    private String role;

    @BeforeEach
    public void setUp() {
        secretKey =  secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    @Test
    public void givenUserPk_whenCreateAuthToken_thenReturnAuthToken() {
        // Given
        String userPk = "testUserPk";
        String roles = "testRole";

        // When
        ReflectionTestUtils.setField(tokenService, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenService, "authTokenTime", authTokenTime);
        String authToken = tokenService.createAuthToken(userPk, roles);

        // Then
        Claims parsedClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getBody();
        assertEquals(userPk, parsedClaims.getSubject());
        assertEquals(roles, parsedClaims.get("roles"));
    }

    @Test
    public void givenUserPk_whenCreateRefreshToken_thenReturnAuthToken() {
        // Given
        String userPk = "testUserPk";
        String roles = "testRole";

        // When
        ReflectionTestUtils.setField(tokenService, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenService, "refreshTokenTime", refreshTokenTime);
        String authToken = tokenService.createRefreshToken(userPk, roles);

        // Then
        Claims parsedClaims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken).getBody();
        assertEquals(userPk, parsedClaims.getSubject());
        assertEquals(roles, parsedClaims.get("roles"));
    }

    @Test
    public void givenAuthToken_whenSetHeaderAuthToken_thenSetAuthorizationHeader() {
        // Given
        String authToken = "yourAuthToken";

        // When
        tokenService.setHeaderAuthToken(response, authToken);

        // Then
        verify(response, times(1)).setHeader("Authorization", "Bearer " + authToken);
    }

    @Test
    public void givenRole_whenSetHeaderRole_thenSetRoleHeader() {
        // Given
        String role = "admin";

        // When
        tokenService.setHeaderRole(response, role);

        // Then
        verify(response, times(1)).setHeader("role", role);
    }

    @Test
    public void givenValidToken_whenGetAuthentication_thenReturnAuthentication() {
        // Given
        String userPk = "user123";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        ReflectionTestUtils.setField(tokenService, "secretKey", secretKey);
        given(userDetailService.loadUserByUsername(anyString())).willReturn(userDetails);

        // When
        Authentication authentication = tokenService.getAuthentication(token);

        // Then
        verify(userDetailService, times(1)).loadUserByUsername(userPk);
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
        assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
        assertEquals(UsernamePasswordAuthenticationToken.class, authentication.getClass());
    }

    @Test
    public void givenToken_whenGetUserPk_thenReturnUserPk() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + authTokenTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        ReflectionTestUtils.setField(tokenService, "secretKey", secretKey);

        // When
        String result = tokenService.getUserPk(token);

        // Then
        assertEquals(userPk, result);
    }



}
