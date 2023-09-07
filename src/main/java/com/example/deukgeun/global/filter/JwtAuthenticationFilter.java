package com.example.deukgeun.global.filter;

import com.example.deukgeun.authToken.application.service.implement.AuthTokenApplicationServiceImpl;
import com.example.deukgeun.global.util.RestResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final AuthTokenApplicationServiceImpl authTokenApplicationService;

    /**
     * 이 메서드는 Spring Security 필터에서 JWT 인증 및 권한을 처리합니다.
     *
     * @param req   ServletRequest 객체입니다.
     * @param res   ServletResponse 객체입니다.
     * @param chain FilterChain 객체입니다.
     * @throws IOException           입출력 관련 예외가 발생할 수 있습니다.
     * @throws ServletException      서블릿 관련 예외가 발생할 수 있습니다.
     * @throws AccessDeniedException 접근 거부 예외가 발생할 수 있습니다.
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException, AccessDeniedException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getHeader("Authorization") != null) {
            String authToken = authTokenApplicationService.resolveAuthToken(request);

            if (authTokenApplicationService.validateToken(authToken)) {
                // 유효한 auth token일 경우 처리합니다.
                handleValidAuthToken(authToken, response);
            } else {
                // 유효하지 않은 auth token일 경우 refresh token을 처리합니다.
                handleValidRefreshToken(authToken, response);
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * HTTP 응답 헤더에 역할 (role)과 토큰 (token) 정보를 설정합니다.
     *
     * @param response HTTP 응답 객체입니다.
     * @param role     설정할 역할 (role) 정보입니다.
     * @param token    설정할 토큰 (token) 정보입니다.
     */
    private void setHeader(HttpServletResponse response, String role, String token) {
        authTokenApplicationService.setHeaderRole(response, role);
        authTokenApplicationService.setHeaderAuthToken(response, token);
    }

    /**
     * Spring Security 의 인증 객체를 설정합니다.
     *
     * @param token JWT 토큰입니다.
     * @param role  현재 사용자의 역할 (role)입니다.
     */
    private void setAuthentication(String token, String role) {
        // authTokenApplicationService를 사용하여 JWT 토큰과 역할 (role)을 기반으로 인증 객체를 가져옵니다.
        Authentication authentication = authTokenApplicationService.getAuthentication(token, role);

        // SecurityContextHolder를 사용하여 현재 스레드의 인증 정보를 설정합니다.
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * 리프레시 토큰을 사용하여 새로운 인증 토큰을 생성합니다.
     *
     * @param refreshToken 리프레시 토큰입니다.
     * @return 생성된 인증 토큰입니다.
     */
    private String createAuthTokenByRefreshToken(String refreshToken) {
        String email = authTokenApplicationService.getUserPk(refreshToken);
        String role = authTokenApplicationService.getUserRole(refreshToken);

        return authTokenApplicationService.createAuthToken(email, role);
    }


    /**
     * 유효한 인증 토큰을 처리하고 HTTP 응답 헤더와 Spring Security 의 인증 객체를 설정합니다.
     *
     * @param authToken 유효한 JWT 인증 토큰입니다.
     * @param response  HTTP 응답 객체입니다.
     */
    private void handleValidAuthToken(String authToken, HttpServletResponse response) {
        String role = authTokenApplicationService.getUserRole(authToken);
        setHeader(response, role, authToken);
        setAuthentication(authToken, role);
    }

    /**
     * 유효한 리프레시 토큰을 처리하고 새로운 인증 토큰을 생성하여 HTTP 응답 헤더와 Spring Security 의 인증 객체를 설정합니다.
     * 리프레시 토큰이 유효하면 새로운 인증 토큰을 생성하고 이를 사용하여 역할 (role) 정보 및 인증을 설정합니다.
     * 리프레시 토큰이 유효하지 않으면 로그인 유효기간이 초과한 메시지를 클라이언트에 반환합니다.
     *
     * @param authToken 유효한 JWT 인증 토큰입니다.
     * @param response  HTTP 응답 객체입니다.
     * @throws IOException 입출력 예외가 발생할 수 있습니다.
     */
    private void handleValidRefreshToken(String authToken, HttpServletResponse response) throws IOException {
        String refreshToken = authTokenApplicationService.getRefreshTokenByAuthToken(authToken);

        if (authTokenApplicationService.validateToken(refreshToken)) {
            String newAuthToken = createAuthTokenByRefreshToken(refreshToken);
            String role = authTokenApplicationService.getUserRole(newAuthToken);

            authTokenApplicationService.updateAuthToken(authToken, newAuthToken);
            setHeader(response, role, newAuthToken);

            setAuthentication(newAuthToken, role);
        } else {
            authTokenApplicationService.deleteByAuthToken(authToken);

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");

            new ObjectMapper().writeValue(response.getOutputStream(),
                    RestResponseUtil.FORBIDDEN("로그인 유효기간이 초과했습니다. 재로그인 해주세요.", null));
        }
    }

}
