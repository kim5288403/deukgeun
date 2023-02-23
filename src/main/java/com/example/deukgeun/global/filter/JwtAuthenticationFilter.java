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
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.MessageResponse;
import com.example.deukgeun.global.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException, AccessDeniedException {
    
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    
    String servletPath = request.getServletPath();
    
    if (servletPath.equals("/jwt/check")) {
      String authToken = jwtTokenProvider.resolveAuthToken(request).replace("Bearer ", "");
      
      // 유효한 auth token인지 확인합니다.
      if (jwtTokenProvider.validateToken(authToken)) {
        this.setAuthentication(authToken);
      }
      // 유효하지 않은 auth token일 경우
      else {
         String refreshToken = jwtTokenProvider.getRefreshToken(authToken);
         
         //유효한 refresh token인지 확인합니다.
         if (jwtTokenProvider.validateToken(refreshToken)) {
           String email = jwtTokenProvider.getUserPk(refreshToken);
           String role = jwtTokenProvider.getUserRole(refreshToken);
           String newAuthToken = jwtTokenProvider.createAuthToken(email, role);
           
           jwtTokenProvider.deleteTokenEntity(authToken);
           jwtTokenProvider.createTokenEntity(newAuthToken, refreshToken);
           jwtTokenProvider.setHeaderAccessToken(response, newAuthToken);
           
           this.setAuthentication(newAuthToken);
         }
         // auth token and refresh token이 유효 하지 않은 경우
         else {
           jwtTokenProvider.deleteTokenEntity(authToken);
           
           response.setContentType("application/json");
           response.setCharacterEncoding("utf-8");
           
           MessageResponse messageResponse = MessageResponse
               .builder()
               .code(StatusEnum.FORBIDDEN.getCode())
               .status(StatusEnum.FORBIDDEN.getStatus())
               .message("로그인 유효기간이 초과했습니다. 재로그인 해주세요.")
               .data(null)
               .build();

           new ObjectMapper().writeValue(response.getOutputStream(),
               ResponseEntity.status(403).body(messageResponse));
         }
      }
    }
    chain.doFilter(request, response);
  }
  
  // SecurityContext 에 Authentication 객체를 저장합니다.
  public void setAuthentication(String token) {
      // 토큰으로부터 유저 정보를 받아옵니다.
      Authentication authentication = jwtTokenProvider.getAuthentication(token);
      // SecurityContext 에 Authentication 객체를 저장합니다.
      SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
