package com.example.deukgeun.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.example.deukgeun.commom.service.implement.JwtServiceImpl;
import com.example.deukgeun.global.filter.JwtAuthenticationFilter;
import com.example.deukgeun.global.provider.JwtProvider;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
  
    private final JwtProvider jwtTokenProvider;
    private final JwtServiceImpl jwtService;
  
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public WebSecurityCustomizer configure() {
	  return (web) -> web.ignoring().mvcMatchers(
	      "/v3/api-docs/**",
	      "/swagger-ui/**",
	      "/api/v1/login"
	  );
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
		.cors().and()
		.csrf().disable()
		.authorizeRequests()
		.antMatchers("/trainer/test").authenticated()
		.antMatchers("/trainer").permitAll()
		.and()
		.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, jwtService),
            UsernamePasswordAuthenticationFilter.class)
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.headers()
        .frameOptions().sameOrigin().and().build();
	}

}
