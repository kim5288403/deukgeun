package com.example.deukgeun.global.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import com.example.deukgeun.commom.entity.Token;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.MessageResponse;
import com.example.deukgeun.global.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException, AccessDeniedException {
    
    String servletPath = ((HttpServletRequest) request).getServletPath();
    if (!servletPath.equals("/trainer/test")) {
      chain.doFilter(request, response);
    } else {
      String authToken = jwtTokenProvider.resolveAuthToken((HttpServletRequest) request);
      
      // 유효한 auth token인지 확인합니다.
      if (jwtTokenProvider.validateToken(authToken)) {
        this.setAuthentication(authToken);
        chain.doFilter(request, response);
      }
      // 유효하지 않은 auth token일 경우
      else {
         String refreshToken = jwtTokenProvider.getRefreshToken(authToken);
         //유효한 refresh token인지 확인합니다.
         if (jwtTokenProvider.validateToken(refreshToken)) {
           String email = jwtTokenProvider.getUserPk(refreshToken);
           String role = jwtTokenProvider.getUserRole(refreshToken);
           
           String newAuthToken = jwtTokenProvider.createAuthToken(email, role);
           jwtTokenProvider.createTokenEntity(newAuthToken, refreshToken);
           jwtTokenProvider.setHeaderAccessToken((HttpServletResponse) response, newAuthToken);
           
           this.setAuthentication(newAuthToken);
           chain.doFilter(request, response);
         } else {
           
         }
         
      }
    }
  }
  
  // SecurityContext 에 Authentication 객체를 저장합니다.
  public void setAuthentication(String token) {
      // 토큰으로부터 유저 정보를 받아옵니다.
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      // SecurityContext 에 Authentication 객체를 저장합니다.
      SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
