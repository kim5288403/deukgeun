package com.example.deukgeun.authToken.service.application;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.authToken.domain.service.AuthTokenDomainService;
import com.example.deukgeun.trainer.domain.service.TrainerDomainService;
import io.jsonwebtoken.Claims;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class AuthTokenApplicationServiceTest {
    @InjectMocks
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthTokenDomainService authTokenDomainService;
    @Mock
    private TrainerDomainService trainerDomainService;
    @Value("${jwt.authTokenTime}")
    private long AUTH_TOKEN_TIME;
    @Value("${jwt.refreshTokenTime}")
    private long REFRESH_TOKEN_TIME;
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;
    @Value("${trainer.role}")
    private String TRAINER_ROLE;
    @Value("${member.role}")
    private String MEMBER_ROLE;

    @BeforeEach
    void setUp() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        ReflectionTestUtils.setField(authTokenApplicationService, "SECRET_KEY", SECRET_KEY);
        ReflectionTestUtils.setField(authTokenApplicationService, "REFRESH_TOKEN_TIME", REFRESH_TOKEN_TIME);
        ReflectionTestUtils.setField(authTokenApplicationService, "AUTH_TOKEN_TIME", AUTH_TOKEN_TIME);
        ReflectionTestUtils.setField(authTokenApplicationService, "TRAINER_ROLE", TRAINER_ROLE);
        ReflectionTestUtils.setField(authTokenApplicationService, "MEMBER_ROLE", MEMBER_ROLE);
    }

    @Test
    void givenValidAuthAndRefreshTokens_whenCreateToken_thenCreateTokenCalled() {
        // Given
        String authToken = "yourAuthToken";
        String refreshToken = "yourRefreshToken";

        // When
        authTokenApplicationService.createToken(authToken, refreshToken);

        // Then
        verify(authTokenDomainService, times(1)).createToken(anyString(), anyString());
    }

    @Test
    void givenValidUserPkAndRoles_whenCreateAuthToken_thenReturnAuthToken() {
        // Given
        String userPk = "testUserPk";
        String roles = "testRole";

        // When
        String authToken = authTokenApplicationService.createAuthToken(userPk, roles);

        // Then
        Claims parsedClaims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
        assertEquals(userPk, parsedClaims.getSubject());
        assertEquals(roles, parsedClaims.get("roles"));
    }

    @Test
    void givenValidUserPk_whenCreateRefreshToken_thenReturnAuthToken() {
        // Given
        String userPk = "testUserPk";
        String roles = "testRole";

        // When
        String authToken = authTokenApplicationService.createRefreshToken(userPk, roles);

        // Then
        Claims parsedClaims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(authToken).getBody();
        assertEquals(userPk, parsedClaims.getSubject());
        assertEquals(roles, parsedClaims.get("roles"));
    }

    @Test
    void givenValidAuthToken_whenDeleteAuthToken_thenDeleteByAuthTokenCalled() {
        // Given
        String token = "token";

        // When
        authTokenApplicationService.deleteByAuthToken(token);

        // Then
        verify(authTokenDomainService, times(1)).deleteByAuthToken(anyString());
    }

    @Test
    void givenValidAuthToken_whenFindByAuthToken_thenReturnFoundIsAuthToken() {
        // Given
        String authToken = "authToken";
        AuthToken foundToken = AuthToken.create(authToken, "refreshToken");

        given(authTokenDomainService.findByAuthToken(anyString())).willReturn(foundToken);

        // When
        AuthToken resultToken = authTokenApplicationService.findByAuthToken(authToken);

        // Then
        assertNotNull(resultToken);
        assertEquals(resultToken.getAuthToken(), foundToken.getAuthToken());
        verify(authTokenDomainService, times(1)).findByAuthToken(anyString());
    }

    @Test
    void givenValidAuthToken_whenGetUserPk_thenReturnUserPk() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AUTH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // When
        String result = authTokenApplicationService.getUserPk(token);

        // Then
        assertEquals(userPk, result);
    }

    @Test
    void givenValidAuthToken_whenGetUserRole_thenReturnRoles() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AUTH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // When
        String actualRole = authTokenApplicationService.getUserRole(token);

        // Then
        assertEquals(roles, actualRole);
    }

    @Test
    void givenValidAuthToken_whenGetAuthentication_thenReturnAuthentication() {
        // Given
        String userPk = "user123";
        String roles = "trainer";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AUTH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        UserDetails userDetails = mock(UserDetails.class);
        given(trainerDomainService.loadUserByTrainerUsername(anyString())).willReturn(userDetails);

        // When
        Authentication authentication = authTokenApplicationService.getAuthentication(token, roles);

        // Then
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
        assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
        assertEquals(UsernamePasswordAuthenticationToken.class, authentication.getClass());
        verify(trainerDomainService, times(1)).loadUserByTrainerUsername(anyString());
    }

    @Test
    void givenValidAuthToken_whenGetRefreshTokenByAuthToken_thenReturnRefreshToken() {
        // Given
        String authToken = "existingToken";
        String refreshToken = "refreshToken";

        AuthToken foundToken = AuthToken.create(authToken, refreshToken);

        given(authTokenDomainService.findByAuthToken(anyString())).willReturn(foundToken);

        // When
        String result = authTokenApplicationService.getRefreshTokenByAuthToken(authToken);

        // Then
        assertEquals(refreshToken, result);
        verify(authTokenDomainService, times(1)).findByAuthToken(anyString());
    }

    @Test
    void givenInValidAuthToken_whenGetRefreshTokenByAuthToken_thenReturnNull() {
        // Given
        String authToken = "nonExistingToken";

        // When
        String result = authTokenApplicationService.getRefreshTokenByAuthToken(authToken);

        // Then
        assertNull(result);
        verify(authTokenDomainService, times(1)).findByAuthToken(anyString());
    }

    @Test
    void givenValidRequest_whenResolveAuthToken_thenReturnAuthToken() {
        // Given
        String authToken = "validAuthToken";
        HttpServletRequest request = mock(HttpServletRequest.class);

        given(request.getHeader("Authorization")).willReturn("Bearer " + authToken);

        // When
        String result = authTokenApplicationService.resolveAuthToken(request);

        // Then
        assertEquals(authToken, result);
    }

    @Test
    void givenValidResponseAndRole_whenSetHeaderRole_thenSetRoleHeader() {
        // Given
        String role = "admin";

        // When
        authTokenApplicationService.setHeaderRole(response, role);

        // Then
        verify(response, times(1)).setHeader(eq("role"), anyString());
    }

    @Test
    void givenValidResponseAndAuthToken_whenSetHeaderAuthToken_thenSetAuthTokenHeader() {
        // Given
        String authToken = "yourAuthToken";

        // When
        authTokenApplicationService.setHeaderAuthToken(response, authToken);

        // Then
        verify(response, times(1)).setHeader(eq("Authorization"), anyString());
    }

    @Test
    void givenValidAuthAndNewTokens_whenUpdateAuthToken_thenUpdateAuthTokenCalled() {
        // Given
        String authToken = "authToken";
        String newAuthToken = "newAuthToken";

        // When
        authTokenApplicationService.updateAuthToken(authToken, newAuthToken);

        // Then
        verify(authTokenDomainService, times(1)).updateAuthToken(anyString(), anyString());
    }

    @Test
    void givenValidAuthToken_whenValidToken_thenReturnTrue() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AUTH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // When
        boolean isValid = authTokenApplicationService.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void givenInValidToken_whenValidateToken_ThenReturnFalse() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime()))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        // When
        boolean isValid = authTokenApplicationService.validateToken(expiredToken);

        // Then
        assertFalse(isValid);
    }
}
