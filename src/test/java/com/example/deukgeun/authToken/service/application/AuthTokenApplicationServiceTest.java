package com.example.deukgeun.authToken.service.application;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.authToken.domain.model.entity.AuthToken;
import com.example.deukgeun.authToken.domain.service.implement.AuthTokenDomainServiceImpl;
import com.example.deukgeun.member.domain.service.implement.MemberDomainServiceImpl;
import com.example.deukgeun.trainer.domain.service.implement.TrainerDomainServiceImpl;
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
    @Mock
    private HttpServletResponse response;
    @Mock
    private AuthTokenDomainServiceImpl authTokenDomainService;
    @Mock
    private MemberDomainServiceImpl memberDomainService;
    @Mock
    private TrainerDomainServiceImpl trainerDomainService;
    @InjectMocks
    private AuthTokenApplicationServiceImpl authTokenApplicationService;
    @Value("${jwt.authTokenTime}")
    private long AUTH_TOKEN_TIME;
    @Value("${jwt.refreshTokenTime}")
    private long REFRESH_TOKEN_TIME;
    @Value("${jwt.secretKey}")
    private String SECRET_KEY;

    @BeforeEach
    void setUp() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
        ReflectionTestUtils.setField(authTokenApplicationService, "SECRET_KEY", SECRET_KEY);
        ReflectionTestUtils.setField(authTokenApplicationService, "REFRESH_TOKEN_TIME", REFRESH_TOKEN_TIME);
        ReflectionTestUtils.setField(authTokenApplicationService, "AUTH_TOKEN_TIME", AUTH_TOKEN_TIME);
    }

    @Test
    void givenAuthAndRefreshTokens_whenCreateToken_thenTokenCreated() {
        // Given
        String authToken = "yourAuthToken";
        String refreshToken = "yourRefreshToken";

        // When
        authTokenApplicationService.createToken(authToken, refreshToken);

        // Then
        verify(authTokenDomainService, times(1)).createToken(authToken, refreshToken);
    }

    @Test
    void givenUserPk_whenCreateAuthToken_thenReturnAuthToken() {
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
    void givenUserPk_whenCreateRefreshToken_thenReturnAuthToken() {
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
    void givenValidAuthToken_whenDeleteAuthToken_thenAuthTokenDeleted() {
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
        authTokenApplicationService.deleteByAuthToken(token);

        // Then
        verify(authTokenDomainService, times(1)).deleteByAuthToken(token);
    }

    @Test
    void givenExistingToken_whenFindByAuthToken_thenReturnsToken() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String authToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AUTH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        AuthToken foundToken = new AuthToken(123L, authToken, "refreshToken");


        given(authTokenDomainService.findByAuthToken(authToken)).willReturn(foundToken);

        // When
        AuthToken resultToken = authTokenApplicationService.findByAuthToken(authToken);

        // Then
        assertNotNull(resultToken);
        assertEquals(resultToken.getAuthToken(), foundToken.getAuthToken());
    }

    @Test
    void givenNonExistingToken_whenFindByAuthToken_thenReturnsNull() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String authToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AUTH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        given(authTokenDomainService.findByAuthToken(authToken)).willReturn(null);


        // When
        AuthToken foundToken = authTokenApplicationService.findByAuthToken(authToken);

        // Then
        assertNull(foundToken);
    }

    @Test
    void givenToken_whenGetUserPk_thenReturnUserPk() {
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
    void givenValidAuthToken_whenGetUserRole_thenCorrectUserRoleRetrieved() {
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
    void givenValidToken_whenGetAuthentication_thenReturnAuthentication() {
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
        ReflectionTestUtils.setField(authTokenApplicationService, "TRAINER_ROLE", roles);
        given(trainerDomainService.loadUserByTrainerUsername(anyString())).willReturn(userDetails);

        // When
        Authentication authentication = authTokenApplicationService.getAuthentication(token, roles);

        // Then
        verify(trainerDomainService, times(1)).loadUserByTrainerUsername(userPk);
        assertEquals(userDetails, authentication.getPrincipal());
        assertEquals("", authentication.getCredentials());
        assertEquals(userDetails.getAuthorities(), authentication.getAuthorities());
        assertEquals(UsernamePasswordAuthenticationToken.class, authentication.getClass());
    }

    @Test
    void givenExistingAuthToken_whenGetRefreshTokenByAuthToken_thenReturnRefreshToken() {
        // Given
        String authToken = "existingToken";
        String refreshToken = "refreshToken";

        AuthToken foundToken = new AuthToken(123L, authToken, "refreshToken");

        given(authTokenDomainService.findByAuthToken(authToken)).willReturn(foundToken);

        // When
        String result = authTokenApplicationService.getRefreshTokenByAuthToken(authToken);

        // Then
        assertEquals(refreshToken, result);
    }

    @Test
    void givenNonExistingAuthToken_whenGetRefreshTokenByAuthToken_thenReturnNull() {
        // Given
        String authToken = "nonExistingToken";

        given(authTokenDomainService.findByAuthToken(authToken)).willReturn(null);

        // When
        String result = authTokenApplicationService.getRefreshTokenByAuthToken(authToken);

        // Then
        assertNull(result);
    }

    @Test
    void givenValidAuthorizationHeader_whenResolveAuthToken_thenReturnAuthToken() {
        // Given
        String authToken = "validAuthToken";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + authToken);

        // When
        String result = authTokenApplicationService.resolveAuthToken(request);

        // Then
        assertEquals(authToken, result);
    }

    @Test
    void givenRole_whenSetHeaderRole_thenSetRoleHeader() {
        // Given
        String role = "admin";

        // When
        authTokenApplicationService.setHeaderRole(response, role);

        // Then
        verify(response, times(1)).setHeader("role", role);
    }

    @Test
    void givenAuthToken_whenSetHeaderAuthToken_thenSetAuthorizationHeader() {
        // Given
        String authToken = "yourAuthToken";

        // When
        authTokenApplicationService.setHeaderAuthToken(response, authToken);

        // Then
        verify(response, times(1)).setHeader("Authorization", "Bearer " + authToken);
    }

    @Test
    void givenValidAuthToken_whenUpdateAuthToken_thenAuthTokenUpdated() {
        // Given
        String userPk = "yourUserPk";
        String roles = "testRole";

        Claims claims = Jwts.claims().setSubject(userPk);
        claims.put("roles", roles);
        Date now = new Date();

        String authToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + AUTH_TOKEN_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        String newAuthToken = "newAuthToken";

        // When
        authTokenApplicationService.updateAuthToken(authToken, newAuthToken);

        // Then
        verify(authTokenDomainService, times(1)).updateAuthToken(authToken, newAuthToken);
    }

    @Test
    void givenValidateToken_whenValidToken_thenReturnsTrue() {
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
    void givenExpiredToken_whenValidateToken_ThenReturnsFalse() {
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
