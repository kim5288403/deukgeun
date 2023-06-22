package com.example.deukgeun.global.filter;

import com.example.deukgeun.commom.enums.StatusEnum;
import com.example.deukgeun.commom.response.RestResponse;
import com.example.deukgeun.commom.service.implement.TokenServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

  private final TokenServiceImpl tokenService;

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException, AccessDeniedException {

    HttpServletRequest request = (HttpServletRequest) req;
    HttpServletResponse response = (HttpServletResponse) res;

    if (request.getHeader("Authorization") != null) {
      String authToken = tokenService.resolveAuthToken(request);

      // 유효한 auth token 인지 확인합니다.
      if (tokenService.validateToken(authToken)) {
        String role = tokenService.getUserRole(authToken);

        setHeader(response, role, authToken);

        setAuthentication(authToken);
      }
      // 유효하지 않은 auth token 일 경우
      else {
          String refreshToken = tokenService.getRefreshTokenByAuthToken(authToken);

         //유효한 refresh token 인지 확인합니다.
         if (tokenService.validateToken(refreshToken)) {
           String newAuthToken = createAuthTokenByRefreshToken(refreshToken);
           String role = tokenService.getUserRole(newAuthToken);

           tokenService.updateAuthToken(authToken, newAuthToken);
           setHeader(response, role, newAuthToken);

           setAuthentication(newAuthToken);
         }
         // auth token and refresh token 이 유효 하지 않은 경우
         else {
           tokenService.deleteToken(authToken);

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

  private void setHeader(HttpServletResponse response, String role, String token) {
    tokenService.setHeaderRole(response, role);
    tokenService.setHeaderAuthToken(response, token);
  }
  
  // SecurityContext 에 Authentication 객체를 저장합니다.
  private void setAuthentication(String token) {
      // 토큰으로부터 유저 정보를 받아옵니다.
      Authentication authentication = tokenService.getAuthentication(token);
      // SecurityContext 에 Authentication 객체를 저장합니다.
      SecurityContextHolder.getContext().setAuthentication(authentication);
  }
  
  private String createAuthTokenByRefreshToken(String refreshToken) {
    String email = tokenService.getUserPk(refreshToken);
    String role = tokenService.getUserRole(refreshToken);

    return tokenService.createAuthToken(email, role);
  }
}
