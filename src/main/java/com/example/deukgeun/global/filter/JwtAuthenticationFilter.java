package com.example.deukgeun.global.filter;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
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
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException, AccessDeniedException {
    String servletPath = ((HttpServletRequest) request).getServletPath();
    boolean flag = true;
    if (servletPath.equals("/trainer/test")) 
    {
      String authToken = jwtTokenProvider.resolveAuthToken((HttpServletRequest) request);
      String refreshToken = jwtTokenProvider.resolveRefreshToken((HttpServletRequest) request);

      // 유효한 토큰인지 확인합니다.
      if (jwtTokenProvider.validateToken(authToken)) {

        Authentication authentication = jwtTokenProvider.getAuthentication(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

      } else if (jwtTokenProvider.validateToken(refreshToken)) {
        Authentication authentication = jwtTokenProvider.getAuthentication(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
      } else {
        flag = false;
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        MessageResponse messageResponse = MessageResponse.builder()
            .code(StatusEnum.FORBIDDEN.getCode()).status(StatusEnum.FORBIDDEN.getStatus())
            .message("token 유효기간이 초과했습니다.").data(null).build();

        new ObjectMapper().writeValue(response.getOutputStream(),
            ResponseEntity.status(403).body(messageResponse));
      }
    }
    
    if (flag) {
      chain.doFilter(request, response);
    }
    
  }
}
