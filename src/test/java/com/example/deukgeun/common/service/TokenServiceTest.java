package com.example.deukgeun.common.service;

import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.repository.TokenRepository;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.example.deukgeun.trainer.service.implement.UserDetailServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TokenServiceTest {
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

    @BeforeEach
    public void setUp() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        ReflectionTestUtils.setField(tokenService, "secretKey", secretKey);
        ReflectionTestUtils.setField(tokenService, "refreshTokenTime", refreshTokenTime);
        ReflectionTestUtils.setField(tokenService, "authTokenTime", authTokenTime);
    }

    @Test
    public void givenUserPk_whenCreateAuthToken_thenReturnAuthToken() {
        // Given
        String userPk = "testUserPk";
        String roles = "testRole";

        // When
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

        // When
        String result = tokenService.getUserPk(token);

        // Then
        assertEquals(userPk, result);
    }

    @Test
    public void getUserRole_ValidToken_ReturnsUserRole() {
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

        // When
        String actualRole = tokenService.getUserRole(token);

        // Then
        assertEquals(roles, actualRole);
    }

    @Test
    public void givenValidateToken_whenValidToken_thenReturnsTrue() {
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

        // When
        boolean isValid = tokenService.validateToken(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void givenExpiredToken_whenValidateToken_ThenReturnsFalse() {
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
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        // When
        boolean isValid = tokenService.validateToken(expiredToken);

        // Then
        assertFalse(isValid);
    }

    @Test
    public void givenToken_whenDeleteToken_ThenRepositoryCalled() {
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

        // When
        tokenService.deleteToken(token);

        // Then
        verify(tokenRepository, times(1)).deleteByAuthToken(token);
    }

    @Test
    public void givenExistingToken_whenUpdateAuthToken_ThenRepositoryCalled() {
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

        Token foundToken = Token
                .builder()
                .authToken(token)
                .refreshToken("refreshToken")
                .build();

        String newAuthToken = "newAuthToken";
        given(tokenRepository.findByAuthToken(token)).willReturn(Optional.of(foundToken));

        // When
        tokenService.updateAuthToken(token, newAuthToken);

        // Then
        verify(tokenRepository, times(1)).findByAuthToken(token);
        verify(tokenRepository, times(1)).save(foundToken);
    }

    @Test
    public void givenExistingToken_whenFindByAuthToken_thenReturnsToken() {
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

        Token returnToken = Token
                .builder()
                .authToken(token)
                .refreshToken("refreshToken")
                .build();

        given(tokenRepository.findByAuthToken(token)).willReturn(Optional.of(returnToken));

        // When
        Token foundToken = tokenService.findByAuthToken(token);

        // Then
        assertNotNull(foundToken);
        assertEquals(token, foundToken.getAuthToken());
    }

    @Test
    public void givenNonExistingToken_whenFindByAuthToken_thenReturnsNull() {
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

        given(tokenRepository.findByAuthToken(token)).willReturn(Optional.empty());


        // When
        Token foundToken = tokenService.findByAuthToken(token);

        // Then
        assertNull(foundToken);
    }

    @Test
    public void givenExistingAuthToken_whenGetRefreshTokenByAuthToken_thenReturnRefreshToken() {
        // Given
        String authToken = "existingToken";
        String refreshToken = "refreshToken";

        Token returnToken = Token
                .builder()
                .authToken(authToken)
                .refreshToken(refreshToken)
                .build();

        given(tokenRepository.findByAuthToken(authToken)).willReturn(Optional.ofNullable(returnToken));

        // When
        String result = tokenService.getRefreshTokenByAuthToken(authToken);

        // Then
        assertEquals(refreshToken, result);
    }

    @Test
    public void givenNonExistingAuthToken_whenGetRefreshTokenByAuthToken_thenReturnNull() {
        // Given
        String authToken = "nonExistingToken";

        given(tokenRepository.findByAuthToken(authToken)).willReturn(Optional.empty());

        // When
        String result = tokenService.getRefreshTokenByAuthToken(authToken);

        // Then
        assertNull(result);
    }

    @Test
    public void givenValidAuthorizationHeader_whenResolveAuthToken_thenReturnAuthToken() {
        // Given
        String authToken = "validAuthToken";
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + authToken);

        // When
        String result = tokenService.resolveAuthToken(request);

        // Then
        assertEquals(authToken, result);
    }

}
