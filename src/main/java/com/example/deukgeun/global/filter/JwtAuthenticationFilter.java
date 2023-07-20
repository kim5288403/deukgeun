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

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException, AccessDeniedException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (request.getHeader("Authorization") != null) {
      String authToken = authTokenApplicationService.resolveAuthToken(request);
      // 유효한 auth token 인지 확인합니다.
      if (authTokenApplicationService.validateToken(authToken)) {
        String role = authTokenApplicationService.getUserRole(authToken);

        setHeader(response, role, authToken);

        setAuthentication(authToken, role);
      }
      // 유효하지 않은 auth token 일 경우
      else {
          String refreshToken = authTokenApplicationService.getRefreshTokenByAuthToken(authToken);

         //유효한 refresh token 인지 확인합니다.
         if (authTokenApplicationService.validateToken(refreshToken)) {
           String newAuthToken = createAuthTokenByRefreshToken(refreshToken);
           String role = authTokenApplicationService.getUserRole(newAuthToken);

           authTokenApplicationService.updateAuthToken(authToken, newAuthToken);
           setHeader(response, role, newAuthToken);

           setAuthentication(newAuthToken, role);
         }
         // auth token and refresh token 이 유효 하지 않은 경우
         else {
           authTokenApplicationService.deleteByAuthToken(authToken);

           response.setContentType("application/json");
           response.setCharacterEncoding("utf-8");

           new ObjectMapper().writeValue(response.getOutputStream(),
                   RestResponseUtil.FORBIDDEN("로그인 유효기간이 초과했습니다. 재로그인 해주세요.", null));
         }
      }
    }

    chain.doFilter(request, response);
  }

  private void setHeader(HttpServletResponse response, String role, String token) {
    authTokenApplicationService.setHeaderRole(response, role);
    authTokenApplicationService.setHeaderAuthToken(response, token);
  }
  
  // SecurityContext 에 Authentication 객체를 저장합니다.
  private void setAuthentication(String token, String role) {
      // 토큰으로부터 유저 정보를 받아옵니다.
      Authentication authentication = authTokenApplicationService.getAuthentication(token, role);
      // SecurityContext 에 Authentication 객체를 저장합니다.
      SecurityContextHolder.getContext().setAuthentication(authentication);
  }
  
  private String createAuthTokenByRefreshToken(String refreshToken) {
    String email = authTokenApplicationService.getUserPk(refreshToken);
    String role = authTokenApplicationService.getUserRole(refreshToken);

    return authTokenApplicationService.createAuthToken(email, role);
  }
}
