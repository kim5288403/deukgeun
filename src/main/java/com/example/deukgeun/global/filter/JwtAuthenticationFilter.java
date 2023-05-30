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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends GenericFilterBean {

  private final JwtServiceImpl jwtService;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException, AccessDeniedException {
    
    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;
    
    String servletPath = request.getServletPath();
    if (servletPath.equals("/jwt/check")) {
      String authToken = jwtService.resolveAuthToken(request);

      // 유효한 auth token 인지 확인합니다.
      if (jwtService.validateToken(authToken)) {
        String role = jwtService.getUserRole(authToken);

        jwtService.setHeaderRole(response, role);
        jwtService.setHeaderAuthToken(response, authToken);
        
        this.setAuthentication(authToken);
      }
      // 유효하지 않은 auth token 일 경우
      else {
          String refreshToken = jwtService.getRefreshTokenByAuthToken(authToken);

         //유효한 refresh token 인지 확인합니다.
         if (jwtService.validateToken(refreshToken)) {
           String newAuthToken = getNewAuthToken(refreshToken);
           String role = jwtService.getUserRole(newAuthToken);
           
           jwtService.updateAuthToken(authToken, newAuthToken);
           jwtService.setHeaderRole(response, role);
           jwtService.setHeaderAuthToken(response, newAuthToken);
           
           this.setAuthentication(newAuthToken);
         }
         // auth token and refresh token 이 유효 하지 않은 경우
         else {
           jwtService.deleteToken(authToken);
           
           response.setContentType("application/json");
           response.setCharacterEncoding("utf-8");
           
           RestResponse messageResponse = RestResponse
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
  private void setAuthentication(String token) {
      // 토큰으로부터 유저 정보를 받아옵니다.
      Authentication authentication = jwtService.getAuthentication(token);
      // SecurityContext 에 Authentication 객체를 저장합니다.
      SecurityContextHolder.getContext().setAuthentication(authentication);
  }
  
  private String getNewAuthToken(String refreshToken) {
    String email = jwtService.getUserPk(refreshToken);
    String role = jwtService.getUserRole(refreshToken);

    return jwtService.createAuthToken(email, role);
  }
}
